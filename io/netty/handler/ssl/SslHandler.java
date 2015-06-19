package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.channel.PendingWriteQueue;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.ssl.NotSslRecordException;
import io.netty.handler.ssl.OpenSslEngine;
import io.netty.handler.ssl.SslHandshakeCompletionEvent;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateExecutor;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;

public class SslHandler extends ByteToMessageDecoder implements ChannelOutboundHandler {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SslHandler.class);
   private static final Pattern IGNORABLE_CLASS_IN_STACK = Pattern.compile("^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
   private static final Pattern IGNORABLE_ERROR_MESSAGE = Pattern.compile("^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", 2);
   private static final SSLException SSLENGINE_CLOSED = new SSLException("SSLEngine closed already");
   private static final SSLException HANDSHAKE_TIMED_OUT = new SSLException("handshake timed out");
   private static final ClosedChannelException CHANNEL_CLOSED = new ClosedChannelException();
   private volatile ChannelHandlerContext ctx;
   private final SSLEngine engine;
   private final int maxPacketBufferSize;
   private final Executor delegatedTaskExecutor;
   private final boolean wantsDirectBuffer;
   private final boolean wantsLargeOutboundNetworkBuffer;
   private boolean wantsInboundHeapBuffer;
   private final boolean startTls;
   private boolean sentFirstMessage;
   private boolean flushedBeforeHandshakeDone;
   private PendingWriteQueue pendingUnencryptedWrites;
   private final SslHandler.LazyChannelPromise handshakePromise;
   private final SslHandler.LazyChannelPromise sslCloseFuture;
   private boolean needsFlush;
   private int packetLength;
   private volatile long handshakeTimeoutMillis;
   private volatile long closeNotifyTimeoutMillis;

   public SslHandler(SSLEngine var1) {
      this(var1, false);
   }

   public SslHandler(SSLEngine var1, boolean var2) {
      this(var1, var2, ImmediateExecutor.INSTANCE);
   }

   /** @deprecated */
   @Deprecated
   public SslHandler(SSLEngine var1, Executor var2) {
      this(var1, false, var2);
   }

   /** @deprecated */
   @Deprecated
   public SslHandler(SSLEngine var1, boolean var2, Executor var3) {
      this.handshakePromise = new SslHandler.LazyChannelPromise(null);
      this.sslCloseFuture = new SslHandler.LazyChannelPromise(null);
      this.handshakeTimeoutMillis = 10000L;
      this.closeNotifyTimeoutMillis = 3000L;
      if(var1 == null) {
         throw new NullPointerException("engine");
      } else if(var3 == null) {
         throw new NullPointerException("delegatedTaskExecutor");
      } else {
         this.engine = var1;
         this.delegatedTaskExecutor = var3;
         this.startTls = var2;
         this.maxPacketBufferSize = var1.getSession().getPacketBufferSize();
         this.wantsDirectBuffer = var1 instanceof OpenSslEngine;
         this.wantsLargeOutboundNetworkBuffer = !(var1 instanceof OpenSslEngine);
      }
   }

   public long getHandshakeTimeoutMillis() {
      return this.handshakeTimeoutMillis;
   }

   public void setHandshakeTimeout(long var1, TimeUnit var3) {
      if(var3 == null) {
         throw new NullPointerException("unit");
      } else {
         this.setHandshakeTimeoutMillis(var3.toMillis(var1));
      }
   }

   public void setHandshakeTimeoutMillis(long var1) {
      if(var1 < 0L) {
         throw new IllegalArgumentException("handshakeTimeoutMillis: " + var1 + " (expected: >= 0)");
      } else {
         this.handshakeTimeoutMillis = var1;
      }
   }

   public long getCloseNotifyTimeoutMillis() {
      return this.closeNotifyTimeoutMillis;
   }

   public void setCloseNotifyTimeout(long var1, TimeUnit var3) {
      if(var3 == null) {
         throw new NullPointerException("unit");
      } else {
         this.setCloseNotifyTimeoutMillis(var3.toMillis(var1));
      }
   }

   public void setCloseNotifyTimeoutMillis(long var1) {
      if(var1 < 0L) {
         throw new IllegalArgumentException("closeNotifyTimeoutMillis: " + var1 + " (expected: >= 0)");
      } else {
         this.closeNotifyTimeoutMillis = var1;
      }
   }

   public SSLEngine engine() {
      return this.engine;
   }

   public Future<Channel> handshakeFuture() {
      return this.handshakePromise;
   }

   public ChannelFuture close() {
      return this.close(this.ctx.newPromise());
   }

   public ChannelFuture close(final ChannelPromise var1) {
      final ChannelHandlerContext var2 = this.ctx;
      var2.executor().execute(new Runnable() {
         public void run() {
            SslHandler.this.engine.closeOutbound();

            try {
               SslHandler.this.write(var2, Unpooled.EMPTY_BUFFER, var1);
               SslHandler.this.flush(var2);
            } catch (Exception var2x) {
               if(!var1.tryFailure(var2x)) {
                  SslHandler.logger.warn("flush() raised a masked exception.", (Throwable)var2x);
               }
            }

         }
      });
      return var1;
   }

   public Future<Channel> sslCloseFuture() {
      return this.sslCloseFuture;
   }

   public void handlerRemoved0(ChannelHandlerContext var1) throws Exception {
      if(!this.pendingUnencryptedWrites.isEmpty()) {
         this.pendingUnencryptedWrites.removeAndFailAll(new ChannelException("Pending write on removal of SslHandler"));
      }

   }

   public void bind(ChannelHandlerContext var1, SocketAddress var2, ChannelPromise var3) throws Exception {
      var1.bind(var2, var3);
   }

   public void connect(ChannelHandlerContext var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) throws Exception {
      var1.connect(var2, var3, var4);
   }

   public void deregister(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      var1.deregister(var2);
   }

   public void disconnect(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      this.closeOutboundAndChannel(var1, var2, true);
   }

   public void close(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      this.closeOutboundAndChannel(var1, var2, false);
   }

   public void read(ChannelHandlerContext var1) {
      var1.read();
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      this.pendingUnencryptedWrites.add(var2, var3);
   }

   public void flush(ChannelHandlerContext var1) throws Exception {
      if(this.startTls && !this.sentFirstMessage) {
         this.sentFirstMessage = true;
         this.pendingUnencryptedWrites.removeAndWriteAll();
         var1.flush();
      } else {
         if(this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, var1.voidPromise());
         }

         if(!this.handshakePromise.isDone()) {
            this.flushedBeforeHandshakeDone = true;
         }

         this.wrap(var1, false);
         var1.flush();
      }
   }

   private void wrap(ChannelHandlerContext var1, boolean var2) throws SSLException {
      ByteBuf var3 = null;
      ChannelPromise var4 = null;

      try {
         while(true) {
            ByteBuf var6;
            while(true) {
               Object var5 = this.pendingUnencryptedWrites.current();
               if(var5 == null) {
                  return;
               }

               if(var5 instanceof ByteBuf) {
                  var6 = (ByteBuf)var5;
                  if(var3 == null) {
                     var3 = this.allocateOutNetBuf(var1, var6.readableBytes());
                  }
                  break;
               }

               this.pendingUnencryptedWrites.removeAndWrite();
            }

            SSLEngineResult var7 = this.wrap(this.engine, var6, var3);
            if(!var6.isReadable()) {
               var4 = this.pendingUnencryptedWrites.remove();
            } else {
               var4 = null;
            }

            if(var7.getStatus() == Status.CLOSED) {
               this.pendingUnencryptedWrites.removeAndFailAll(SSLENGINE_CLOSED);
               return;
            }

            switch(SslHandler.SyntheticClass_1.$SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[var7.getHandshakeStatus().ordinal()]) {
            case 1:
               this.runDelegatedTasks();
               break;
            case 2:
               this.setHandshakeSuccess();
            case 3:
               this.setHandshakeSuccessIfStillHandshaking();
            case 4:
               this.finishWrap(var1, var3, var4, var2);
               var4 = null;
               var3 = null;
               break;
            case 5:
               return;
            default:
               throw new IllegalStateException("Unknown handshake status: " + var7.getHandshakeStatus());
            }
         }
      } catch (SSLException var11) {
         this.setHandshakeFailure(var11);
         throw var11;
      } finally {
         this.finishWrap(var1, var3, var4, var2);
      }
   }

   private void finishWrap(ChannelHandlerContext var1, ByteBuf var2, ChannelPromise var3, boolean var4) {
      if(var2 == null) {
         var2 = Unpooled.EMPTY_BUFFER;
      } else if(!var2.isReadable()) {
         var2.release();
         var2 = Unpooled.EMPTY_BUFFER;
      }

      if(var3 != null) {
         var1.write(var2, var3);
      } else {
         var1.write(var2);
      }

      if(var4) {
         this.needsFlush = true;
      }

   }

   private void wrapNonAppData(ChannelHandlerContext var1, boolean var2) throws SSLException {
      ByteBuf var3 = null;

      try {
         SSLEngineResult var4;
         try {
            do {
               if(var3 == null) {
                  var3 = this.allocateOutNetBuf(var1, 0);
               }

               var4 = this.wrap(this.engine, Unpooled.EMPTY_BUFFER, var3);
               if(var4.bytesProduced() > 0) {
                  var1.write(var3);
                  if(var2) {
                     this.needsFlush = true;
                  }

                  var3 = null;
               }

               switch(SslHandler.SyntheticClass_1.$SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[var4.getHandshakeStatus().ordinal()]) {
               case 1:
                  this.runDelegatedTasks();
                  break;
               case 2:
                  this.setHandshakeSuccess();
                  break;
               case 3:
                  this.setHandshakeSuccessIfStillHandshaking();
                  if(!var2) {
                     this.unwrapNonAppData(var1);
                  }
               case 4:
                  break;
               case 5:
                  if(!var2) {
                     this.unwrapNonAppData(var1);
                  }
                  break;
               default:
                  throw new IllegalStateException("Unknown handshake status: " + var4.getHandshakeStatus());
               }
            } while(var4.bytesProduced() != 0);
         } catch (SSLException var8) {
            this.setHandshakeFailure(var8);
            throw var8;
         }
      } finally {
         if(var3 != null) {
            var3.release();
         }

      }

   }

   private SSLEngineResult wrap(SSLEngine var1, ByteBuf var2, ByteBuf var3) throws SSLException {
      ByteBuffer var4 = var2.nioBuffer();
      ByteBuffer var5;
      if(!var4.isDirect()) {
         var5 = ByteBuffer.allocateDirect(var4.remaining());
         var5.put(var4).flip();
         var4 = var5;
      }

      while(true) {
         var5 = var3.nioBuffer(var3.writerIndex(), var3.writableBytes());
         SSLEngineResult var6 = var1.wrap(var4, var5);
         var2.skipBytes(var6.bytesConsumed());
         var3.writerIndex(var3.writerIndex() + var6.bytesProduced());
         switch(SslHandler.SyntheticClass_1.$SwitchMap$javax$net$ssl$SSLEngineResult$Status[var6.getStatus().ordinal()]) {
         case 1:
            var3.ensureWritable(this.maxPacketBufferSize);
            break;
         default:
            return var6;
         }
      }
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      this.setHandshakeFailure(CHANNEL_CLOSED);
      super.channelInactive(var1);
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      if(this.ignoreException(var2)) {
         if(logger.isDebugEnabled()) {
            logger.debug("Swallowing a harmless \'connection reset by peer / broken pipe\' error that occurred while writing close_notify in response to the peer\'s close_notify", var2);
         }

         if(var1.channel().isActive()) {
            var1.close();
         }
      } else {
         var1.fireExceptionCaught(var2);
      }

   }

   private boolean ignoreException(Throwable var1) {
      if(!(var1 instanceof SSLException) && var1 instanceof IOException && this.sslCloseFuture.isDone()) {
         String var2 = String.valueOf(var1.getMessage()).toLowerCase();
         if(IGNORABLE_ERROR_MESSAGE.matcher(var2).matches()) {
            return true;
         }

         StackTraceElement[] var3 = var1.getStackTrace();
         StackTraceElement[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            StackTraceElement var7 = var4[var6];
            String var8 = var7.getClassName();
            String var9 = var7.getMethodName();
            if(!var8.startsWith("io.netty.") && "read".equals(var9)) {
               if(IGNORABLE_CLASS_IN_STACK.matcher(var8).matches()) {
                  return true;
               }

               try {
                  Class var10 = PlatformDependent.getClassLoader(this.getClass()).loadClass(var8);
                  if(SocketChannel.class.isAssignableFrom(var10) || DatagramChannel.class.isAssignableFrom(var10)) {
                     return true;
                  }

                  if(PlatformDependent.javaVersion() >= 7 && "com.sun.nio.sctp.SctpChannel".equals(var10.getSuperclass().getName())) {
                     return true;
                  }
               } catch (ClassNotFoundException var11) {
                  ;
               }
            }
         }
      }

      return false;
   }

   public static boolean isEncrypted(ByteBuf var0) {
      if(var0.readableBytes() < 5) {
         throw new IllegalArgumentException("buffer must have at least 5 readable bytes");
      } else {
         return getEncryptedPacketLength(var0, var0.readerIndex()) != -1;
      }
   }

   private static int getEncryptedPacketLength(ByteBuf var0, int var1) {
      int var2 = 0;
      boolean var3;
      switch(var0.getUnsignedByte(var1)) {
      case 20:
      case 21:
      case 22:
      case 23:
         var3 = true;
         break;
      default:
         var3 = false;
      }

      if(var3) {
         short var4 = var0.getUnsignedByte(var1 + 1);
         if(var4 == 3) {
            var2 = var0.getUnsignedShort(var1 + 3) + 5;
            if(var2 <= 5) {
               var3 = false;
            }
         } else {
            var3 = false;
         }
      }

      if(!var3) {
         boolean var7 = true;
         int var5 = (var0.getUnsignedByte(var1) & 128) != 0?2:3;
         short var6 = var0.getUnsignedByte(var1 + var5 + 1);
         if(var6 != 2 && var6 != 3) {
            var7 = false;
         } else {
            if(var5 == 2) {
               var2 = (var0.getShort(var1) & 32767) + 2;
            } else {
               var2 = (var0.getShort(var1) & 16383) + 3;
            }

            if(var2 <= var5) {
               var7 = false;
            }
         }

         if(!var7) {
            return -1;
         }
      }

      return var2;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws SSLException {
      int var4 = var2.readerIndex();
      int var5 = var2.writerIndex();
      int var6 = var4;
      int var7 = 0;
      if(this.packetLength > 0) {
         if(var5 - var4 < this.packetLength) {
            return;
         }

         var6 = var4 + this.packetLength;
         var7 = this.packetLength;
         this.packetLength = 0;
      }

      boolean var8;
      int var11;
      for(var8 = false; var7 < 18713; var7 = var11) {
         int var9 = var5 - var6;
         if(var9 < 5) {
            break;
         }

         int var10 = getEncryptedPacketLength(var2, var6);
         if(var10 == -1) {
            var8 = true;
            break;
         }

         assert var10 > 0;

         if(var10 > var9) {
            this.packetLength = var10;
            break;
         }

         var11 = var7 + var10;
         if(var11 > 18713) {
            break;
         }

         var6 += var10;
      }

      if(var7 > 0) {
         var2.skipBytes(var7);
         ByteBuffer var12 = var2.nioBuffer(var4, var7);
         this.unwrap(var1, var12, var7);

         assert !var12.hasRemaining() || this.engine.isInboundDone();
      }

      if(var8) {
         NotSslRecordException var13 = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(var2));
         var2.skipBytes(var2.readableBytes());
         var1.fireExceptionCaught(var13);
         this.setHandshakeFailure(var13);
      }

   }

   public void channelReadComplete(ChannelHandlerContext var1) throws Exception {
      if(this.needsFlush) {
         this.needsFlush = false;
         var1.flush();
      }

      super.channelReadComplete(var1);
   }

   private void unwrapNonAppData(ChannelHandlerContext var1) throws SSLException {
      this.unwrap(var1, Unpooled.EMPTY_BUFFER.nioBuffer(), 0);
   }

   private void unwrap(ChannelHandlerContext var1, ByteBuffer var2, int var3) throws SSLException {
      int var6 = var2.position();
      ByteBuffer var4;
      ByteBuf var5;
      if(this.wantsInboundHeapBuffer && var2.isDirect()) {
         var5 = var1.alloc().heapBuffer(var2.limit() - var6);
         var5.writeBytes(var2);
         var4 = var2;
         var2 = var5.nioBuffer();
      } else {
         var4 = null;
         var5 = null;
      }

      boolean var7 = false;
      ByteBuf var8 = this.allocate(var1, var3);

      while(true) {
         try {
            SSLEngineResult var9 = unwrap(this.engine, var2, var8);
            Status var10 = var9.getStatus();
            HandshakeStatus var11 = var9.getHandshakeStatus();
            int var12 = var9.bytesProduced();
            int var13 = var9.bytesConsumed();
            if(var10 == Status.CLOSED) {
               this.sslCloseFuture.trySuccess(var1.channel());
            } else {
               switch(SslHandler.SyntheticClass_1.$SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[var11.ordinal()]) {
               case 1:
                  this.runDelegatedTasks();
                  break;
               case 2:
                  this.setHandshakeSuccess();
                  var7 = true;
                  continue;
               case 3:
                  if(this.setHandshakeSuccessIfStillHandshaking()) {
                     var7 = true;
                     continue;
                  }

                  if(this.flushedBeforeHandshakeDone) {
                     this.flushedBeforeHandshakeDone = false;
                     var7 = true;
                  }
                  break;
               case 4:
                  this.wrapNonAppData(var1, true);
               case 5:
                  break;
               default:
                  throw new IllegalStateException("Unknown handshake status: " + var11);
               }

               if(var10 != Status.BUFFER_UNDERFLOW && (var13 != 0 || var12 != 0)) {
                  continue;
               }
            }

            if(var7) {
               this.wrap(var1, true);
            }
         } catch (SSLException var17) {
            this.setHandshakeFailure(var17);
            throw var17;
         } finally {
            if(var5 != null) {
               var4.position(var6 + var2.position());
               var5.release();
            }

            if(var8.isReadable()) {
               var1.fireChannelRead(var8);
            } else {
               var8.release();
            }

         }

         return;
      }
   }

   private static SSLEngineResult unwrap(SSLEngine var0, ByteBuffer var1, ByteBuf var2) throws SSLException {
      int var3 = 0;

      while(true) {
         ByteBuffer var4 = var2.nioBuffer(var2.writerIndex(), var2.writableBytes());
         SSLEngineResult var5 = var0.unwrap(var1, var4);
         var2.writerIndex(var2.writerIndex() + var5.bytesProduced());
         switch(SslHandler.SyntheticClass_1.$SwitchMap$javax$net$ssl$SSLEngineResult$Status[var5.getStatus().ordinal()]) {
         case 1:
            int var6 = var0.getSession().getApplicationBufferSize();
            switch(var3++) {
            case 0:
               var2.ensureWritable(Math.min(var6, var1.remaining()));
               continue;
            default:
               var2.ensureWritable(var6);
               continue;
            }
         default:
            return var5;
         }
      }
   }

   private void runDelegatedTasks() {
      if(this.delegatedTaskExecutor == ImmediateExecutor.INSTANCE) {
         while(true) {
            Runnable var6 = this.engine.getDelegatedTask();
            if(var6 == null) {
               break;
            }

            var6.run();
         }
      } else {
         final ArrayList var1 = new ArrayList(2);

         while(true) {
            Runnable var2 = this.engine.getDelegatedTask();
            if(var2 == null) {
               if(var1.isEmpty()) {
                  return;
               }

               final CountDownLatch var7 = new CountDownLatch(1);
               this.delegatedTaskExecutor.execute(new Runnable() {
                  public void run() {
                     try {
                        Iterator var1x = var1.iterator();

                        while(var1x.hasNext()) {
                           Runnable var2 = (Runnable)var1x.next();
                           var2.run();
                        }
                     } catch (Exception var6) {
                        SslHandler.this.ctx.fireExceptionCaught(var6);
                     } finally {
                        var7.countDown();
                     }

                  }
               });
               boolean var3 = false;

               while(var7.getCount() != 0L) {
                  try {
                     var7.await();
                  } catch (InterruptedException var5) {
                     var3 = true;
                  }
               }

               if(var3) {
                  Thread.currentThread().interrupt();
               }
               break;
            }

            var1.add(var2);
         }
      }

   }

   private boolean setHandshakeSuccessIfStillHandshaking() {
      if(!this.handshakePromise.isDone()) {
         this.setHandshakeSuccess();
         return true;
      } else {
         return false;
      }
   }

   private void setHandshakeSuccess() {
      String var1 = String.valueOf(this.engine.getSession().getCipherSuite());
      if(!this.wantsDirectBuffer && (var1.contains("_GCM_") || var1.contains("-GCM-"))) {
         this.wantsInboundHeapBuffer = true;
      }

      if(this.handshakePromise.trySuccess(this.ctx.channel())) {
         if(logger.isDebugEnabled()) {
            logger.debug(this.ctx.channel() + " HANDSHAKEN: " + this.engine.getSession().getCipherSuite());
         }

         this.ctx.fireUserEventTriggered(SslHandshakeCompletionEvent.SUCCESS);
      }

   }

   private void setHandshakeFailure(Throwable var1) {
      this.engine.closeOutbound();

      try {
         this.engine.closeInbound();
      } catch (SSLException var4) {
         String var3 = var4.getMessage();
         if(var3 == null || !var3.contains("possible truncation attack")) {
            logger.debug("SSLEngine.closeInbound() raised an exception.", (Throwable)var4);
         }
      }

      this.notifyHandshakeFailure(var1);
      this.pendingUnencryptedWrites.removeAndFailAll(var1);
   }

   private void notifyHandshakeFailure(Throwable var1) {
      if(this.handshakePromise.tryFailure(var1)) {
         this.ctx.fireUserEventTriggered(new SslHandshakeCompletionEvent(var1));
         this.ctx.close();
      }

   }

   private void closeOutboundAndChannel(ChannelHandlerContext var1, ChannelPromise var2, boolean var3) throws Exception {
      if(!var1.channel().isActive()) {
         if(var3) {
            var1.disconnect(var2);
         } else {
            var1.close(var2);
         }

      } else {
         this.engine.closeOutbound();
         ChannelPromise var4 = var1.newPromise();
         this.write(var1, Unpooled.EMPTY_BUFFER, var4);
         this.flush(var1);
         this.safeClose(var1, var4, var2);
      }
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      this.ctx = var1;
      this.pendingUnencryptedWrites = new PendingWriteQueue(var1);
      if(var1.channel().isActive() && this.engine.getUseClientMode()) {
         this.handshake();
      }

   }

   private Future<Channel> handshake() {
      final ScheduledFuture var1;
      if(this.handshakeTimeoutMillis > 0L) {
         var1 = this.ctx.executor().schedule(new Runnable() {
            public void run() {
               if(!SslHandler.this.handshakePromise.isDone()) {
                  SslHandler.this.notifyHandshakeFailure(SslHandler.HANDSHAKE_TIMED_OUT);
               }
            }
         }, this.handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
      } else {
         var1 = null;
      }

      this.handshakePromise.addListener(new GenericFutureListener() {
         public void operationComplete(Future<Channel> var1x) throws Exception {
            if(var1 != null) {
               var1.cancel(false);
            }

         }
      });

      try {
         this.engine.beginHandshake();
         this.wrapNonAppData(this.ctx, false);
         this.ctx.flush();
      } catch (Exception var3) {
         this.notifyHandshakeFailure(var3);
      }

      return this.handshakePromise;
   }

   public void channelActive(final ChannelHandlerContext var1) throws Exception {
      if(!this.startTls && this.engine.getUseClientMode()) {
         this.handshake().addListener(new GenericFutureListener() {
            public void operationComplete(Future<Channel> var1x) throws Exception {
               if(!var1x.isSuccess()) {
                  SslHandler.logger.debug("Failed to complete handshake", var1x.cause());
                  var1.close();
               }

            }
         });
      }

      var1.fireChannelActive();
   }

   private void safeClose(final ChannelHandlerContext var1, ChannelFuture var2, final ChannelPromise var3) {
      if(!var1.channel().isActive()) {
         var1.close(var3);
      } else {
         final ScheduledFuture var4;
         if(this.closeNotifyTimeoutMillis > 0L) {
            var4 = var1.executor().schedule(new Runnable() {
               public void run() {
                  SslHandler.logger.warn(var1.channel() + " last write attempt timed out." + " Force-closing the connection.");
                  var1.close(var3);
               }
            }, this.closeNotifyTimeoutMillis, TimeUnit.MILLISECONDS);
         } else {
            var4 = null;
         }

         var2.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture var1x) throws Exception {
               if(var4 != null) {
                  var4.cancel(false);
               }

               var1.close(var3);
            }

            // $FF: synthetic method
            // $FF: bridge method
            public void operationComplete(Future var1x) throws Exception {
               this.operationComplete((ChannelFuture)var1x);
            }
         });
      }
   }

   private ByteBuf allocate(ChannelHandlerContext var1, int var2) {
      ByteBufAllocator var3 = var1.alloc();
      return this.wantsDirectBuffer?var3.directBuffer(var2):var3.buffer(var2);
   }

   private ByteBuf allocateOutNetBuf(ChannelHandlerContext var1, int var2) {
      return this.wantsLargeOutboundNetworkBuffer?this.allocate(var1, this.maxPacketBufferSize):this.allocate(var1, Math.min(var2 + 2329, this.maxPacketBufferSize));
   }

   static {
      SSLENGINE_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
      HANDSHAKE_TIMED_OUT.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
      CHANNEL_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus;
      // $FF: synthetic field
      static final int[] $SwitchMap$javax$net$ssl$SSLEngineResult$Status = new int[Status.values().length];

      static {
         try {
            $SwitchMap$javax$net$ssl$SSLEngineResult$Status[Status.BUFFER_OVERFLOW.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus = new int[HandshakeStatus.values().length];

         try {
            $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[HandshakeStatus.NEED_TASK.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[HandshakeStatus.FINISHED.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[HandshakeStatus.NOT_HANDSHAKING.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[HandshakeStatus.NEED_WRAP.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$javax$net$ssl$SSLEngineResult$HandshakeStatus[HandshakeStatus.NEED_UNWRAP.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   private final class LazyChannelPromise extends DefaultPromise<Channel> {
      private LazyChannelPromise() {
      }

      protected EventExecutor executor() {
         if(SslHandler.this.ctx == null) {
            throw new IllegalStateException();
         } else {
            return SslHandler.this.ctx.executor();
         }
      }

      // $FF: synthetic method
      LazyChannelPromise(Object var2) {
         this();
      }
   }
}
