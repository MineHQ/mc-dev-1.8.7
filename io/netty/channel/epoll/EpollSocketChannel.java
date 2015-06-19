package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.EventLoop;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.epoll.EpollSocketChannelConfig;
import io.netty.channel.epoll.IovArray;
import io.netty.channel.epoll.Native;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class EpollSocketChannel extends AbstractEpollChannel implements SocketChannel {
   private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(DefaultFileRegion.class) + ')';
   private final EpollSocketChannelConfig config = new EpollSocketChannelConfig(this);
   private ChannelPromise connectPromise;
   private ScheduledFuture<?> connectTimeoutFuture;
   private SocketAddress requestedRemoteAddress;
   private volatile InetSocketAddress local;
   private volatile InetSocketAddress remote;
   private volatile boolean inputShutdown;
   private volatile boolean outputShutdown;

   EpollSocketChannel(Channel var1, int var2) {
      super(var1, var2, 1, true);
      this.remote = Native.remoteAddress(var2);
      this.local = Native.localAddress(var2);
   }

   public EpollSocketChannel() {
      super(Native.socketStreamFd(), 1);
   }

   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
      return new EpollSocketChannel.EpollSocketUnsafe();
   }

   protected SocketAddress localAddress0() {
      return this.local;
   }

   protected SocketAddress remoteAddress0() {
      return this.remote;
   }

   protected void doBind(SocketAddress var1) throws Exception {
      InetSocketAddress var2 = (InetSocketAddress)var1;
      Native.bind(this.fd, var2.getAddress(), var2.getPort());
      this.local = Native.localAddress(this.fd);
   }

   private boolean writeBytes(ChannelOutboundBuffer var1, ByteBuf var2) throws Exception {
      int var3 = var2.readableBytes();
      if(var3 == 0) {
         var1.remove();
         return true;
      } else {
         boolean var4 = false;
         long var5 = 0L;
         int var9;
         int var10;
         int var11;
         if(var2.hasMemoryAddress()) {
            long var13 = var2.memoryAddress();
            var9 = var2.readerIndex();
            var10 = var2.writerIndex();

            while(true) {
               var11 = Native.writeAddress(this.fd, var13, var9, var10);
               if(var11 <= 0) {
                  this.setEpollOut();
                  break;
               }

               var5 += (long)var11;
               if(var5 == (long)var3) {
                  var4 = true;
                  break;
               }

               var9 += var11;
            }

            var1.removeBytes(var5);
            return var4;
         } else if(var2.nioBufferCount() != 1) {
            ByteBuffer[] var12 = var2.nioBuffers();
            return this.writeBytesMultiple(var1, var12, var12.length, (long)var3);
         } else {
            int var7 = var2.readerIndex();
            ByteBuffer var8 = var2.internalNioBuffer(var7, var2.readableBytes());

            while(true) {
               var9 = var8.position();
               var10 = var8.limit();
               var11 = Native.write(this.fd, var8, var9, var10);
               if(var11 > 0) {
                  var8.position(var9 + var11);
                  var5 += (long)var11;
                  if(var5 != (long)var3) {
                     continue;
                  }

                  var4 = true;
                  break;
               }

               this.setEpollOut();
               break;
            }

            var1.removeBytes(var5);
            return var4;
         }
      }
   }

   private boolean writeBytesMultiple(ChannelOutboundBuffer var1, IovArray var2) throws IOException {
      long var3 = var2.size();
      int var5 = var2.count();

      assert var3 != 0L;

      assert var5 != 0;

      boolean var6 = false;
      long var7 = 0L;
      int var9 = 0;
      int var10 = var9 + var5;

      while(true) {
         long var11 = Native.writevAddresses(this.fd, var2.memoryAddress(var9), var5);
         if(var11 == 0L) {
            this.setEpollOut();
            break;
         }

         var3 -= var11;
         var7 += var11;
         if(var3 == 0L) {
            var6 = true;
            break;
         }

         while(true) {
            long var13 = var2.processWritten(var9, var11);
            if(var13 == -1L) {
               break;
            }

            ++var9;
            --var5;
            var11 -= var13;
            if(var9 >= var10 || var11 <= 0L) {
               break;
            }
         }
      }

      var1.removeBytes(var7);
      return var6;
   }

   private boolean writeBytesMultiple(ChannelOutboundBuffer var1, ByteBuffer[] var2, int var3, long var4) throws IOException {
      assert var4 != 0L;

      boolean var6 = false;
      long var7 = 0L;
      int var9 = 0;
      int var10 = var9 + var3;

      while(true) {
         long var11 = Native.writev(this.fd, var2, var9, var3);
         if(var11 == 0L) {
            this.setEpollOut();
            break;
         }

         var4 -= var11;
         var7 += var11;
         if(var4 == 0L) {
            var6 = true;
            break;
         }

         while(true) {
            ByteBuffer var13 = var2[var9];
            int var14 = var13.position();
            int var15 = var13.limit() - var14;
            if((long)var15 > var11) {
               var13.position(var14 + (int)var11);
               break;
            }

            ++var9;
            --var3;
            var11 -= (long)var15;
            if(var9 >= var10 || var11 <= 0L) {
               break;
            }
         }
      }

      var1.removeBytes(var7);
      return var6;
   }

   private boolean writeFileRegion(ChannelOutboundBuffer var1, DefaultFileRegion var2) throws Exception {
      long var3 = var2.count();
      if(var2.transfered() >= var3) {
         var1.remove();
         return true;
      } else {
         long var5 = var2.position();
         boolean var7 = false;
         long var8 = 0L;

         for(int var10 = this.config().getWriteSpinCount() - 1; var10 >= 0; --var10) {
            long var11 = var2.transfered();
            long var13 = Native.sendfile(this.fd, var2, var5, var11, var3 - var11);
            if(var13 == 0L) {
               this.setEpollOut();
               break;
            }

            var8 += var13;
            if(var2.transfered() >= var3) {
               var7 = true;
               break;
            }
         }

         if(var8 > 0L) {
            var1.progress(var8);
         }

         if(var7) {
            var1.remove();
         }

         return var7;
      }
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      while(true) {
         int var2 = var1.size();
         if(var2 == 0) {
            this.clearEpollOut();
         } else if(var2 > 1 && var1.current() instanceof ByteBuf) {
            if(this.doWriteMultiple(var1)) {
               continue;
            }
         } else if(this.doWriteSingle(var1)) {
            continue;
         }

         return;
      }
   }

   private boolean doWriteSingle(ChannelOutboundBuffer var1) throws Exception {
      Object var2 = var1.current();
      if(var2 instanceof ByteBuf) {
         ByteBuf var3 = (ByteBuf)var2;
         if(!this.writeBytes(var1, var3)) {
            return false;
         }
      } else {
         if(!(var2 instanceof DefaultFileRegion)) {
            throw new Error();
         }

         DefaultFileRegion var4 = (DefaultFileRegion)var2;
         if(!this.writeFileRegion(var1, var4)) {
            return false;
         }
      }

      return true;
   }

   private boolean doWriteMultiple(ChannelOutboundBuffer var1) throws Exception {
      int var3;
      if(PlatformDependent.hasUnsafe()) {
         IovArray var2 = IovArray.get(var1);
         var3 = var2.count();
         if(var3 >= 1) {
            if(!this.writeBytesMultiple(var1, var2)) {
               return false;
            }
         } else {
            var1.removeBytes(0L);
         }
      } else {
         ByteBuffer[] var4 = var1.nioBuffers();
         var3 = var1.nioBufferCount();
         if(var3 >= 1) {
            if(!this.writeBytesMultiple(var1, var4, var3, var1.nioBufferSize())) {
               return false;
            }
         } else {
            var1.removeBytes(0L);
         }
      }

      return true;
   }

   protected Object filterOutboundMessage(Object var1) {
      if(!(var1 instanceof ByteBuf)) {
         if(var1 instanceof DefaultFileRegion) {
            return var1;
         } else {
            throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(var1) + EXPECTED_TYPES);
         }
      } else {
         ByteBuf var2 = (ByteBuf)var1;
         if(!var2.hasMemoryAddress() && (PlatformDependent.hasUnsafe() || !var2.isDirect())) {
            var2 = this.newDirectBuffer(var2);

            assert var2.hasMemoryAddress();
         }

         return var2;
      }
   }

   public EpollSocketChannelConfig config() {
      return this.config;
   }

   public boolean isInputShutdown() {
      return this.inputShutdown;
   }

   public boolean isOutputShutdown() {
      return this.outputShutdown || !this.isActive();
   }

   public ChannelFuture shutdownOutput() {
      return this.shutdownOutput(this.newPromise());
   }

   public ChannelFuture shutdownOutput(final ChannelPromise var1) {
      EventLoop var2 = this.eventLoop();
      if(var2.inEventLoop()) {
         try {
            Native.shutdown(this.fd, false, true);
            this.outputShutdown = true;
            var1.setSuccess();
         } catch (Throwable var4) {
            var1.setFailure(var4);
         }
      } else {
         var2.execute(new Runnable() {
            public void run() {
               EpollSocketChannel.this.shutdownOutput(var1);
            }
         });
      }

      return var1;
   }

   public ServerSocketChannel parent() {
      return (ServerSocketChannel)super.parent();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean isOpen() {
      return super.isOpen();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public InetSocketAddress localAddress() {
      return super.localAddress();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public InetSocketAddress remoteAddress() {
      return super.remoteAddress();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelMetadata metadata() {
      return super.metadata();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean isActive() {
      return super.isActive();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return this.newUnsafe();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Channel parent() {
      return this.parent();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig config() {
      return this.config();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig config() {
      return this.config();
   }

   final class EpollSocketUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
      private RecvByteBufAllocator.Handle allocHandle;

      EpollSocketUnsafe() {
         super();
      }

      private void closeOnRead(ChannelPipeline var1) {
         EpollSocketChannel.this.inputShutdown = true;
         if(EpollSocketChannel.this.isOpen()) {
            if(Boolean.TRUE.equals(EpollSocketChannel.this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
               this.clearEpollIn0();
               var1.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
            } else {
               this.close(this.voidPromise());
            }
         }

      }

      private boolean handleReadException(ChannelPipeline var1, ByteBuf var2, Throwable var3, boolean var4) {
         if(var2 != null) {
            if(var2.isReadable()) {
               this.readPending = false;
               var1.fireChannelRead(var2);
            } else {
               var2.release();
            }
         }

         var1.fireChannelReadComplete();
         var1.fireExceptionCaught(var3);
         if(!var4 && !(var3 instanceof IOException)) {
            return false;
         } else {
            this.closeOnRead(var1);
            return true;
         }
      }

      public void connect(final SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         if(var3.setUncancellable() && this.ensureOpen(var3)) {
            try {
               if(EpollSocketChannel.this.connectPromise != null) {
                  throw new IllegalStateException("connection attempt already made");
               }

               boolean var7 = EpollSocketChannel.this.isActive();
               if(this.doConnect((InetSocketAddress)var1, (InetSocketAddress)var2)) {
                  this.fulfillConnectPromise(var3, var7);
               } else {
                  EpollSocketChannel.this.connectPromise = var3;
                  EpollSocketChannel.this.requestedRemoteAddress = var1;
                  int var8 = EpollSocketChannel.this.config().getConnectTimeoutMillis();
                  if(var8 > 0) {
                     EpollSocketChannel.this.connectTimeoutFuture = EpollSocketChannel.this.eventLoop().schedule(new Runnable() {
                        public void run() {
                           ChannelPromise var1x = EpollSocketChannel.this.connectPromise;
                           ConnectTimeoutException var2 = new ConnectTimeoutException("connection timed out: " + var1);
                           if(var1x != null && var1x.tryFailure(var2)) {
                              EpollSocketUnsafe.this.close(EpollSocketUnsafe.this.voidPromise());
                           }

                        }
                     }, (long)var8, TimeUnit.MILLISECONDS);
                  }

                  var3.addListener(new ChannelFutureListener() {
                     public void operationComplete(ChannelFuture var1) throws Exception {
                        if(var1.isCancelled()) {
                           if(EpollSocketChannel.this.connectTimeoutFuture != null) {
                              EpollSocketChannel.this.connectTimeoutFuture.cancel(false);
                           }

                           EpollSocketChannel.this.connectPromise = null;
                           EpollSocketUnsafe.this.close(EpollSocketUnsafe.this.voidPromise());
                        }

                     }

                     // $FF: synthetic method
                     // $FF: bridge method
                     public void operationComplete(Future var1) throws Exception {
                        this.operationComplete((ChannelFuture)var1);
                     }
                  });
               }
            } catch (Throwable var6) {
               Object var4 = var6;
               if(var6 instanceof ConnectException) {
                  ConnectException var5 = new ConnectException(var6.getMessage() + ": " + var1);
                  var5.setStackTrace(var6.getStackTrace());
                  var4 = var5;
               }

               this.closeIfClosed();
               var3.tryFailure((Throwable)var4);
            }

         }
      }

      private void fulfillConnectPromise(ChannelPromise var1, boolean var2) {
         if(var1 != null) {
            EpollSocketChannel.this.active = true;
            boolean var3 = var1.trySuccess();
            if(!var2 && EpollSocketChannel.this.isActive()) {
               EpollSocketChannel.this.pipeline().fireChannelActive();
            }

            if(!var3) {
               this.close(this.voidPromise());
            }

         }
      }

      private void fulfillConnectPromise(ChannelPromise var1, Throwable var2) {
         if(var1 != null) {
            var1.tryFailure(var2);
            this.closeIfClosed();
         }
      }

      private void finishConnect() {
         assert EpollSocketChannel.this.eventLoop().inEventLoop();

         boolean var1 = false;

         try {
            boolean var9 = EpollSocketChannel.this.isActive();
            if(this.doFinishConnect()) {
               this.fulfillConnectPromise(EpollSocketChannel.this.connectPromise, var9);
               return;
            }

            var1 = true;
         } catch (Throwable var7) {
            Object var2 = var7;
            if(var7 instanceof ConnectException) {
               ConnectException var3 = new ConnectException(var7.getMessage() + ": " + EpollSocketChannel.this.requestedRemoteAddress);
               var3.setStackTrace(var7.getStackTrace());
               var2 = var3;
            }

            this.fulfillConnectPromise(EpollSocketChannel.this.connectPromise, (Throwable)var2);
            return;
         } finally {
            if(!var1) {
               if(EpollSocketChannel.this.connectTimeoutFuture != null) {
                  EpollSocketChannel.this.connectTimeoutFuture.cancel(false);
               }

               EpollSocketChannel.this.connectPromise = null;
            }

         }

      }

      void epollOutReady() {
         if(EpollSocketChannel.this.connectPromise != null) {
            this.finishConnect();
         } else {
            super.epollOutReady();
         }

      }

      private boolean doConnect(InetSocketAddress var1, InetSocketAddress var2) throws Exception {
         if(var2 != null) {
            AbstractEpollChannel.checkResolvable(var2);
            Native.bind(EpollSocketChannel.this.fd, var2.getAddress(), var2.getPort());
         }

         boolean var3 = false;

         boolean var5;
         try {
            AbstractEpollChannel.checkResolvable(var1);
            boolean var4 = Native.connect(EpollSocketChannel.this.fd, var1.getAddress(), var1.getPort());
            EpollSocketChannel.this.remote = var1;
            EpollSocketChannel.this.local = Native.localAddress(EpollSocketChannel.this.fd);
            if(!var4) {
               EpollSocketChannel.this.setEpollOut();
            }

            var3 = true;
            var5 = var4;
         } finally {
            if(!var3) {
               EpollSocketChannel.this.doClose();
            }

         }

         return var5;
      }

      private boolean doFinishConnect() throws Exception {
         if(Native.finishConnect(EpollSocketChannel.this.fd)) {
            EpollSocketChannel.this.clearEpollOut();
            return true;
         } else {
            EpollSocketChannel.this.setEpollOut();
            return false;
         }
      }

      private int doReadBytes(ByteBuf var1) throws Exception {
         int var2 = var1.writerIndex();
         int var3;
         if(var1.hasMemoryAddress()) {
            var3 = Native.readAddress(EpollSocketChannel.this.fd, var1.memoryAddress(), var2, var1.capacity());
         } else {
            ByteBuffer var4 = var1.internalNioBuffer(var2, var1.writableBytes());
            var3 = Native.read(EpollSocketChannel.this.fd, var4, var4.position(), var4.limit());
         }

         if(var3 > 0) {
            var1.writerIndex(var2 + var3);
         }

         return var3;
      }

      void epollRdHupReady() {
         if(EpollSocketChannel.this.isActive()) {
            this.epollInReady();
         } else {
            this.closeOnRead(EpollSocketChannel.this.pipeline());
         }

      }

      void epollInReady() {
         EpollSocketChannelConfig var1 = EpollSocketChannel.this.config();
         ChannelPipeline var2 = EpollSocketChannel.this.pipeline();
         ByteBufAllocator var3 = var1.getAllocator();
         RecvByteBufAllocator.Handle var4 = this.allocHandle;
         if(var4 == null) {
            this.allocHandle = var4 = var1.getRecvByteBufAllocator().newHandle();
         }

         ByteBuf var5 = null;
         boolean var6 = false;

         try {
            int var7 = 0;

            int var9;
            int var15;
            do {
               var5 = var4.allocate(var3);
               var15 = var5.writableBytes();
               var9 = this.doReadBytes(var5);
               if(var9 <= 0) {
                  var5.release();
                  var6 = var9 < 0;
                  break;
               }

               this.readPending = false;
               var2.fireChannelRead(var5);
               var5 = null;
               if(var7 >= Integer.MAX_VALUE - var9) {
                  var4.record(var7);
                  var7 = var9;
               } else {
                  var7 += var9;
               }
            } while(var9 >= var15);

            var2.fireChannelReadComplete();
            var4.record(var7);
            if(var6) {
               this.closeOnRead(var2);
               var6 = false;
            }
         } catch (Throwable var13) {
            boolean var8 = this.handleReadException(var2, var5, var13, var6);
            if(!var8) {
               EpollSocketChannel.this.eventLoop().execute(new Runnable() {
                  public void run() {
                     EpollSocketUnsafe.this.epollInReady();
                  }
               });
            }
         } finally {
            if(!var1.isAutoRead() && !this.readPending) {
               this.clearEpollIn0();
            }

         }

      }
   }
}
