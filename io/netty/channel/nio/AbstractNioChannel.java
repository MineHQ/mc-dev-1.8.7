package io.netty.channel.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoop;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.OneTimeTask;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractNioChannel extends AbstractChannel {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractNioChannel.class);
   private final SelectableChannel ch;
   protected final int readInterestOp;
   volatile SelectionKey selectionKey;
   private volatile boolean inputShutdown;
   private volatile boolean readPending;
   private ChannelPromise connectPromise;
   private ScheduledFuture<?> connectTimeoutFuture;
   private SocketAddress requestedRemoteAddress;

   protected AbstractNioChannel(Channel var1, SelectableChannel var2, int var3) {
      super(var1);
      this.ch = var2;
      this.readInterestOp = var3;

      try {
         var2.configureBlocking(false);
      } catch (IOException var7) {
         try {
            var2.close();
         } catch (IOException var6) {
            if(logger.isWarnEnabled()) {
               logger.warn("Failed to close a partially initialized socket.", (Throwable)var6);
            }
         }

         throw new ChannelException("Failed to enter non-blocking mode.", var7);
      }
   }

   public boolean isOpen() {
      return this.ch.isOpen();
   }

   public AbstractNioChannel.NioUnsafe unsafe() {
      return (AbstractNioChannel.NioUnsafe)super.unsafe();
   }

   protected SelectableChannel javaChannel() {
      return this.ch;
   }

   public NioEventLoop eventLoop() {
      return (NioEventLoop)super.eventLoop();
   }

   protected SelectionKey selectionKey() {
      assert this.selectionKey != null;

      return this.selectionKey;
   }

   protected boolean isReadPending() {
      return this.readPending;
   }

   protected void setReadPending(boolean var1) {
      this.readPending = var1;
   }

   protected boolean isInputShutdown() {
      return this.inputShutdown;
   }

   void setInputShutdown() {
      this.inputShutdown = true;
   }

   protected boolean isCompatible(EventLoop var1) {
      return var1 instanceof NioEventLoop;
   }

   protected void doRegister() throws Exception {
      boolean var1 = false;

      while(true) {
         try {
            this.selectionKey = this.javaChannel().register(this.eventLoop().selector, 0, this);
            return;
         } catch (CancelledKeyException var3) {
            if(var1) {
               throw var3;
            }

            this.eventLoop().selectNow();
            var1 = true;
         }
      }
   }

   protected void doDeregister() throws Exception {
      this.eventLoop().cancel(this.selectionKey());
   }

   protected void doBeginRead() throws Exception {
      if(!this.inputShutdown) {
         SelectionKey var1 = this.selectionKey;
         if(var1.isValid()) {
            this.readPending = true;
            int var2 = var1.interestOps();
            if((var2 & this.readInterestOp) == 0) {
               var1.interestOps(var2 | this.readInterestOp);
            }

         }
      }
   }

   protected abstract boolean doConnect(SocketAddress var1, SocketAddress var2) throws Exception;

   protected abstract void doFinishConnect() throws Exception;

   protected final ByteBuf newDirectBuffer(ByteBuf var1) {
      int var2 = var1.readableBytes();
      if(var2 == 0) {
         ReferenceCountUtil.safeRelease(var1);
         return Unpooled.EMPTY_BUFFER;
      } else {
         ByteBufAllocator var3 = this.alloc();
         ByteBuf var4;
         if(var3.isDirectBufferPooled()) {
            var4 = var3.directBuffer(var2);
            var4.writeBytes(var1, var1.readerIndex(), var2);
            ReferenceCountUtil.safeRelease(var1);
            return var4;
         } else {
            var4 = ByteBufUtil.threadLocalDirectBuffer();
            if(var4 != null) {
               var4.writeBytes(var1, var1.readerIndex(), var2);
               ReferenceCountUtil.safeRelease(var1);
               return var4;
            } else {
               return var1;
            }
         }
      }
   }

   protected final ByteBuf newDirectBuffer(ReferenceCounted var1, ByteBuf var2) {
      int var3 = var2.readableBytes();
      if(var3 == 0) {
         ReferenceCountUtil.safeRelease(var1);
         return Unpooled.EMPTY_BUFFER;
      } else {
         ByteBufAllocator var4 = this.alloc();
         ByteBuf var5;
         if(var4.isDirectBufferPooled()) {
            var5 = var4.directBuffer(var3);
            var5.writeBytes(var2, var2.readerIndex(), var3);
            ReferenceCountUtil.safeRelease(var1);
            return var5;
         } else {
            var5 = ByteBufUtil.threadLocalDirectBuffer();
            if(var5 != null) {
               var5.writeBytes(var2, var2.readerIndex(), var3);
               ReferenceCountUtil.safeRelease(var1);
               return var5;
            } else {
               if(var1 != var2) {
                  var2.retain();
                  ReferenceCountUtil.safeRelease(var1);
               }

               return var2;
            }
         }
      }
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Channel.Unsafe unsafe() {
      return this.unsafe();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public EventLoop eventLoop() {
      return this.eventLoop();
   }

   protected abstract class AbstractNioUnsafe extends AbstractChannel.AbstractUnsafe implements AbstractNioChannel.NioUnsafe {
      protected AbstractNioUnsafe() {
         super();
      }

      protected final void removeReadOp() {
         SelectionKey var1 = AbstractNioChannel.this.selectionKey();
         if(var1.isValid()) {
            int var2 = var1.interestOps();
            if((var2 & AbstractNioChannel.this.readInterestOp) != 0) {
               var1.interestOps(var2 & ~AbstractNioChannel.this.readInterestOp);
            }

         }
      }

      public final SelectableChannel ch() {
         return AbstractNioChannel.this.javaChannel();
      }

      public final void connect(final SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         if(var3.setUncancellable() && this.ensureOpen(var3)) {
            try {
               if(AbstractNioChannel.this.connectPromise != null) {
                  throw new IllegalStateException("connection attempt already made");
               }

               boolean var7 = AbstractNioChannel.this.isActive();
               if(AbstractNioChannel.this.doConnect(var1, var2)) {
                  this.fulfillConnectPromise(var3, var7);
               } else {
                  AbstractNioChannel.this.connectPromise = var3;
                  AbstractNioChannel.this.requestedRemoteAddress = var1;
                  int var8 = AbstractNioChannel.this.config().getConnectTimeoutMillis();
                  if(var8 > 0) {
                     AbstractNioChannel.this.connectTimeoutFuture = AbstractNioChannel.this.eventLoop().schedule(new OneTimeTask() {
                        public void run() {
                           ChannelPromise var1x = AbstractNioChannel.this.connectPromise;
                           ConnectTimeoutException var2 = new ConnectTimeoutException("connection timed out: " + var1);
                           if(var1x != null && var1x.tryFailure(var2)) {
                              AbstractNioUnsafe.this.close(AbstractNioUnsafe.this.voidPromise());
                           }

                        }
                     }, (long)var8, TimeUnit.MILLISECONDS);
                  }

                  var3.addListener(new ChannelFutureListener() {
                     public void operationComplete(ChannelFuture var1) throws Exception {
                        if(var1.isCancelled()) {
                           if(AbstractNioChannel.this.connectTimeoutFuture != null) {
                              AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
                           }

                           AbstractNioChannel.this.connectPromise = null;
                           AbstractNioUnsafe.this.close(AbstractNioUnsafe.this.voidPromise());
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

               var3.tryFailure((Throwable)var4);
               this.closeIfClosed();
            }

         }
      }

      private void fulfillConnectPromise(ChannelPromise var1, boolean var2) {
         if(var1 != null) {
            boolean var3 = var1.trySuccess();
            if(!var2 && AbstractNioChannel.this.isActive()) {
               AbstractNioChannel.this.pipeline().fireChannelActive();
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

      public final void finishConnect() {
         assert AbstractNioChannel.this.eventLoop().inEventLoop();

         try {
            boolean var8 = AbstractNioChannel.this.isActive();
            AbstractNioChannel.this.doFinishConnect();
            this.fulfillConnectPromise(AbstractNioChannel.this.connectPromise, var8);
         } catch (Throwable var6) {
            Object var1 = var6;
            if(var6 instanceof ConnectException) {
               ConnectException var2 = new ConnectException(var6.getMessage() + ": " + AbstractNioChannel.this.requestedRemoteAddress);
               var2.setStackTrace(var6.getStackTrace());
               var1 = var2;
            }

            this.fulfillConnectPromise(AbstractNioChannel.this.connectPromise, (Throwable)var1);
         } finally {
            if(AbstractNioChannel.this.connectTimeoutFuture != null) {
               AbstractNioChannel.this.connectTimeoutFuture.cancel(false);
            }

            AbstractNioChannel.this.connectPromise = null;
         }

      }

      protected final void flush0() {
         if(!this.isFlushPending()) {
            super.flush0();
         }
      }

      public final void forceFlush() {
         super.flush0();
      }

      private boolean isFlushPending() {
         SelectionKey var1 = AbstractNioChannel.this.selectionKey();
         return var1.isValid() && (var1.interestOps() & 4) != 0;
      }
   }

   public interface NioUnsafe extends Channel.Unsafe {
      SelectableChannel ch();

      void finishConnect();

      void read();

      void forceFlush();
   }
}
