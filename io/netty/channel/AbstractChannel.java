package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.channel.DefaultChannelProgressivePromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.FailedChannelFuture;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.SucceededChannelFuture;
import io.netty.channel.VoidChannelPromise;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.OneTimeTask;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ThreadLocalRandom;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import java.util.concurrent.RejectedExecutionException;

public abstract class AbstractChannel extends DefaultAttributeMap implements Channel {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractChannel.class);
   static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION = new ClosedChannelException();
   static final NotYetConnectedException NOT_YET_CONNECTED_EXCEPTION = new NotYetConnectedException();
   private MessageSizeEstimator.Handle estimatorHandle;
   private final Channel parent;
   private final long hashCode = ThreadLocalRandom.current().nextLong();
   private final Channel.Unsafe unsafe;
   private final DefaultChannelPipeline pipeline;
   private final ChannelFuture succeededFuture = new SucceededChannelFuture(this, (EventExecutor)null);
   private final VoidChannelPromise voidPromise = new VoidChannelPromise(this, true);
   private final VoidChannelPromise unsafeVoidPromise = new VoidChannelPromise(this, false);
   private final AbstractChannel.CloseFuture closeFuture = new AbstractChannel.CloseFuture(this);
   private volatile SocketAddress localAddress;
   private volatile SocketAddress remoteAddress;
   private volatile EventLoop eventLoop;
   private volatile boolean registered;
   private boolean strValActive;
   private String strVal;

   protected AbstractChannel(Channel var1) {
      this.parent = var1;
      this.unsafe = this.newUnsafe();
      this.pipeline = new DefaultChannelPipeline(this);
   }

   public boolean isWritable() {
      ChannelOutboundBuffer var1 = this.unsafe.outboundBuffer();
      return var1 != null && var1.isWritable();
   }

   public Channel parent() {
      return this.parent;
   }

   public ChannelPipeline pipeline() {
      return this.pipeline;
   }

   public ByteBufAllocator alloc() {
      return this.config().getAllocator();
   }

   public EventLoop eventLoop() {
      EventLoop var1 = this.eventLoop;
      if(var1 == null) {
         throw new IllegalStateException("channel not registered to an event loop");
      } else {
         return var1;
      }
   }

   public SocketAddress localAddress() {
      SocketAddress var1 = this.localAddress;
      if(var1 == null) {
         try {
            this.localAddress = var1 = this.unsafe().localAddress();
         } catch (Throwable var3) {
            return null;
         }
      }

      return var1;
   }

   protected void invalidateLocalAddress() {
      this.localAddress = null;
   }

   public SocketAddress remoteAddress() {
      SocketAddress var1 = this.remoteAddress;
      if(var1 == null) {
         try {
            this.remoteAddress = var1 = this.unsafe().remoteAddress();
         } catch (Throwable var3) {
            return null;
         }
      }

      return var1;
   }

   protected void invalidateRemoteAddress() {
      this.remoteAddress = null;
   }

   public boolean isRegistered() {
      return this.registered;
   }

   public ChannelFuture bind(SocketAddress var1) {
      return this.pipeline.bind(var1);
   }

   public ChannelFuture connect(SocketAddress var1) {
      return this.pipeline.connect(var1);
   }

   public ChannelFuture connect(SocketAddress var1, SocketAddress var2) {
      return this.pipeline.connect(var1, var2);
   }

   public ChannelFuture disconnect() {
      return this.pipeline.disconnect();
   }

   public ChannelFuture close() {
      return this.pipeline.close();
   }

   public ChannelFuture deregister() {
      return this.pipeline.deregister();
   }

   public Channel flush() {
      this.pipeline.flush();
      return this;
   }

   public ChannelFuture bind(SocketAddress var1, ChannelPromise var2) {
      return this.pipeline.bind(var1, var2);
   }

   public ChannelFuture connect(SocketAddress var1, ChannelPromise var2) {
      return this.pipeline.connect(var1, var2);
   }

   public ChannelFuture connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
      return this.pipeline.connect(var1, var2, var3);
   }

   public ChannelFuture disconnect(ChannelPromise var1) {
      return this.pipeline.disconnect(var1);
   }

   public ChannelFuture close(ChannelPromise var1) {
      return this.pipeline.close(var1);
   }

   public ChannelFuture deregister(ChannelPromise var1) {
      return this.pipeline.deregister(var1);
   }

   public Channel read() {
      this.pipeline.read();
      return this;
   }

   public ChannelFuture write(Object var1) {
      return this.pipeline.write(var1);
   }

   public ChannelFuture write(Object var1, ChannelPromise var2) {
      return this.pipeline.write(var1, var2);
   }

   public ChannelFuture writeAndFlush(Object var1) {
      return this.pipeline.writeAndFlush(var1);
   }

   public ChannelFuture writeAndFlush(Object var1, ChannelPromise var2) {
      return this.pipeline.writeAndFlush(var1, var2);
   }

   public ChannelPromise newPromise() {
      return new DefaultChannelPromise(this);
   }

   public ChannelProgressivePromise newProgressivePromise() {
      return new DefaultChannelProgressivePromise(this);
   }

   public ChannelFuture newSucceededFuture() {
      return this.succeededFuture;
   }

   public ChannelFuture newFailedFuture(Throwable var1) {
      return new FailedChannelFuture(this, (EventExecutor)null, var1);
   }

   public ChannelFuture closeFuture() {
      return this.closeFuture;
   }

   public Channel.Unsafe unsafe() {
      return this.unsafe;
   }

   protected abstract AbstractChannel.AbstractUnsafe newUnsafe();

   public final int hashCode() {
      return (int)this.hashCode;
   }

   public final boolean equals(Object var1) {
      return this == var1;
   }

   public final int compareTo(Channel var1) {
      if(this == var1) {
         return 0;
      } else {
         long var2 = this.hashCode - (long)var1.hashCode();
         if(var2 > 0L) {
            return 1;
         } else if(var2 < 0L) {
            return -1;
         } else {
            var2 = (long)(System.identityHashCode(this) - System.identityHashCode(var1));
            if(var2 != 0L) {
               return (int)var2;
            } else {
               throw new Error();
            }
         }
      }
   }

   public String toString() {
      boolean var1 = this.isActive();
      if(this.strValActive == var1 && this.strVal != null) {
         return this.strVal;
      } else {
         SocketAddress var2 = this.remoteAddress();
         SocketAddress var3 = this.localAddress();
         if(var2 != null) {
            SocketAddress var4;
            SocketAddress var5;
            if(this.parent == null) {
               var4 = var3;
               var5 = var2;
            } else {
               var4 = var2;
               var5 = var3;
            }

            this.strVal = String.format("[id: 0x%08x, %s %s %s]", new Object[]{Integer.valueOf((int)this.hashCode), var4, var1?"=>":":>", var5});
         } else if(var3 != null) {
            this.strVal = String.format("[id: 0x%08x, %s]", new Object[]{Integer.valueOf((int)this.hashCode), var3});
         } else {
            this.strVal = String.format("[id: 0x%08x]", new Object[]{Integer.valueOf((int)this.hashCode)});
         }

         this.strValActive = var1;
         return this.strVal;
      }
   }

   public final ChannelPromise voidPromise() {
      return this.voidPromise;
   }

   final MessageSizeEstimator.Handle estimatorHandle() {
      if(this.estimatorHandle == null) {
         this.estimatorHandle = this.config().getMessageSizeEstimator().newHandle();
      }

      return this.estimatorHandle;
   }

   protected abstract boolean isCompatible(EventLoop var1);

   protected abstract SocketAddress localAddress0();

   protected abstract SocketAddress remoteAddress0();

   protected void doRegister() throws Exception {
   }

   protected abstract void doBind(SocketAddress var1) throws Exception;

   protected abstract void doDisconnect() throws Exception;

   protected abstract void doClose() throws Exception;

   protected void doDeregister() throws Exception {
   }

   protected abstract void doBeginRead() throws Exception;

   protected abstract void doWrite(ChannelOutboundBuffer var1) throws Exception;

   protected Object filterOutboundMessage(Object var1) throws Exception {
      return var1;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int compareTo(Object var1) {
      return this.compareTo((Channel)var1);
   }

   static {
      CLOSED_CHANNEL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
      NOT_YET_CONNECTED_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
   }

   static final class CloseFuture extends DefaultChannelPromise {
      CloseFuture(AbstractChannel var1) {
         super(var1);
      }

      public ChannelPromise setSuccess() {
         throw new IllegalStateException();
      }

      public ChannelPromise setFailure(Throwable var1) {
         throw new IllegalStateException();
      }

      public boolean trySuccess() {
         throw new IllegalStateException();
      }

      public boolean tryFailure(Throwable var1) {
         throw new IllegalStateException();
      }

      boolean setClosed() {
         return super.trySuccess();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Promise setFailure(Throwable var1) {
         return this.setFailure(var1);
      }
   }

   protected abstract class AbstractUnsafe implements Channel.Unsafe {
      private ChannelOutboundBuffer outboundBuffer = new ChannelOutboundBuffer(AbstractChannel.this);
      private boolean inFlush0;

      protected AbstractUnsafe() {
      }

      public final ChannelOutboundBuffer outboundBuffer() {
         return this.outboundBuffer;
      }

      public final SocketAddress localAddress() {
         return AbstractChannel.this.localAddress0();
      }

      public final SocketAddress remoteAddress() {
         return AbstractChannel.this.remoteAddress0();
      }

      public final void register(EventLoop var1, final ChannelPromise var2) {
         if(var1 == null) {
            throw new NullPointerException("eventLoop");
         } else if(AbstractChannel.this.isRegistered()) {
            var2.setFailure(new IllegalStateException("registered to an event loop already"));
         } else if(!AbstractChannel.this.isCompatible(var1)) {
            var2.setFailure(new IllegalStateException("incompatible event loop type: " + var1.getClass().getName()));
         } else {
            AbstractChannel.this.eventLoop = var1;
            if(var1.inEventLoop()) {
               this.register0(var2);
            } else {
               try {
                  var1.execute(new OneTimeTask() {
                     public void run() {
                        AbstractUnsafe.this.register0(var2);
                     }
                  });
               } catch (Throwable var4) {
                  AbstractChannel.logger.warn("Force-closing a channel whose registration task was not accepted by an event loop: {}", AbstractChannel.this, var4);
                  this.closeForcibly();
                  AbstractChannel.this.closeFuture.setClosed();
                  this.safeSetFailure(var2, var4);
               }
            }

         }
      }

      private void register0(ChannelPromise var1) {
         try {
            if(!var1.setUncancellable() || !this.ensureOpen(var1)) {
               return;
            }

            AbstractChannel.this.doRegister();
            AbstractChannel.this.registered = true;
            this.safeSetSuccess(var1);
            AbstractChannel.this.pipeline.fireChannelRegistered();
            if(AbstractChannel.this.isActive()) {
               AbstractChannel.this.pipeline.fireChannelActive();
            }
         } catch (Throwable var3) {
            this.closeForcibly();
            AbstractChannel.this.closeFuture.setClosed();
            this.safeSetFailure(var1, var3);
         }

      }

      public final void bind(SocketAddress var1, ChannelPromise var2) {
         if(var2.setUncancellable() && this.ensureOpen(var2)) {
            if(!PlatformDependent.isWindows() && !PlatformDependent.isRoot() && Boolean.TRUE.equals(AbstractChannel.this.config().getOption(ChannelOption.SO_BROADCAST)) && var1 instanceof InetSocketAddress && !((InetSocketAddress)var1).getAddress().isAnyLocalAddress()) {
               AbstractChannel.logger.warn("A non-root user can\'t receive a broadcast packet if the socket is not bound to a wildcard address; binding to a non-wildcard address (" + var1 + ") anyway as requested.");
            }

            boolean var3 = AbstractChannel.this.isActive();

            try {
               AbstractChannel.this.doBind(var1);
            } catch (Throwable var5) {
               this.safeSetFailure(var2, var5);
               this.closeIfClosed();
               return;
            }

            if(!var3 && AbstractChannel.this.isActive()) {
               this.invokeLater(new OneTimeTask() {
                  public void run() {
                     AbstractChannel.this.pipeline.fireChannelActive();
                  }
               });
            }

            this.safeSetSuccess(var2);
         }
      }

      public final void disconnect(ChannelPromise var1) {
         if(var1.setUncancellable()) {
            boolean var2 = AbstractChannel.this.isActive();

            try {
               AbstractChannel.this.doDisconnect();
            } catch (Throwable var4) {
               this.safeSetFailure(var1, var4);
               this.closeIfClosed();
               return;
            }

            if(var2 && !AbstractChannel.this.isActive()) {
               this.invokeLater(new OneTimeTask() {
                  public void run() {
                     AbstractChannel.this.pipeline.fireChannelInactive();
                  }
               });
            }

            this.safeSetSuccess(var1);
            this.closeIfClosed();
         }
      }

      public final void close(final ChannelPromise var1) {
         if(var1.setUncancellable()) {
            if(this.inFlush0) {
               this.invokeLater(new OneTimeTask() {
                  public void run() {
                     AbstractUnsafe.this.close(var1);
                  }
               });
            } else if(AbstractChannel.this.closeFuture.isDone()) {
               this.safeSetSuccess(var1);
            } else {
               boolean var2 = AbstractChannel.this.isActive();
               ChannelOutboundBuffer var3 = this.outboundBuffer;
               this.outboundBuffer = null;

               try {
                  AbstractChannel.this.doClose();
                  AbstractChannel.this.closeFuture.setClosed();
                  this.safeSetSuccess(var1);
               } catch (Throwable var8) {
                  AbstractChannel.this.closeFuture.setClosed();
                  this.safeSetFailure(var1, var8);
               }

               try {
                  var3.failFlushed(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
                  var3.close(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
               } finally {
                  if(var2 && !AbstractChannel.this.isActive()) {
                     this.invokeLater(new OneTimeTask() {
                        public void run() {
                           AbstractChannel.this.pipeline.fireChannelInactive();
                        }
                     });
                  }

                  this.deregister(this.voidPromise());
               }

            }
         }
      }

      public final void closeForcibly() {
         try {
            AbstractChannel.this.doClose();
         } catch (Exception var2) {
            AbstractChannel.logger.warn("Failed to close a channel.", (Throwable)var2);
         }

      }

      public final void deregister(ChannelPromise var1) {
         if(var1.setUncancellable()) {
            if(!AbstractChannel.this.registered) {
               this.safeSetSuccess(var1);
            } else {
               try {
                  AbstractChannel.this.doDeregister();
               } catch (Throwable var6) {
                  AbstractChannel.logger.warn("Unexpected exception occurred while deregistering a channel.", var6);
               } finally {
                  if(AbstractChannel.this.registered) {
                     AbstractChannel.this.registered = false;
                     this.invokeLater(new OneTimeTask() {
                        public void run() {
                           AbstractChannel.this.pipeline.fireChannelUnregistered();
                        }
                     });
                     this.safeSetSuccess(var1);
                  } else {
                     this.safeSetSuccess(var1);
                  }

               }

            }
         }
      }

      public final void beginRead() {
         if(AbstractChannel.this.isActive()) {
            try {
               AbstractChannel.this.doBeginRead();
            } catch (final Exception var2) {
               this.invokeLater(new OneTimeTask() {
                  public void run() {
                     AbstractChannel.this.pipeline.fireExceptionCaught(var2);
                  }
               });
               this.close(this.voidPromise());
            }

         }
      }

      public final void write(Object var1, ChannelPromise var2) {
         ChannelOutboundBuffer var3 = this.outboundBuffer;
         if(var3 == null) {
            this.safeSetFailure(var2, AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
            ReferenceCountUtil.release(var1);
         } else {
            int var4;
            try {
               var1 = AbstractChannel.this.filterOutboundMessage(var1);
               var4 = AbstractChannel.this.estimatorHandle().size(var1);
               if(var4 < 0) {
                  var4 = 0;
               }
            } catch (Throwable var6) {
               this.safeSetFailure(var2, var6);
               ReferenceCountUtil.release(var1);
               return;
            }

            var3.addMessage(var1, var4, var2);
         }
      }

      public final void flush() {
         ChannelOutboundBuffer var1 = this.outboundBuffer;
         if(var1 != null) {
            var1.addFlush();
            this.flush0();
         }
      }

      protected void flush0() {
         if(!this.inFlush0) {
            ChannelOutboundBuffer var1 = this.outboundBuffer;
            if(var1 != null && !var1.isEmpty()) {
               this.inFlush0 = true;
               if(!AbstractChannel.this.isActive()) {
                  try {
                     if(AbstractChannel.this.isOpen()) {
                        var1.failFlushed(AbstractChannel.NOT_YET_CONNECTED_EXCEPTION);
                     } else {
                        var1.failFlushed(AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
                     }
                  } finally {
                     this.inFlush0 = false;
                  }

               } else {
                  try {
                     AbstractChannel.this.doWrite(var1);
                  } catch (Throwable var11) {
                     var1.failFlushed(var11);
                     if(var11 instanceof IOException && AbstractChannel.this.config().isAutoClose()) {
                        this.close(this.voidPromise());
                     }
                  } finally {
                     this.inFlush0 = false;
                  }

               }
            }
         }
      }

      public final ChannelPromise voidPromise() {
         return AbstractChannel.this.unsafeVoidPromise;
      }

      protected final boolean ensureOpen(ChannelPromise var1) {
         if(AbstractChannel.this.isOpen()) {
            return true;
         } else {
            this.safeSetFailure(var1, AbstractChannel.CLOSED_CHANNEL_EXCEPTION);
            return false;
         }
      }

      protected final void safeSetSuccess(ChannelPromise var1) {
         if(!(var1 instanceof VoidChannelPromise) && !var1.trySuccess()) {
            AbstractChannel.logger.warn("Failed to mark a promise as success because it is done already: {}", (Object)var1);
         }

      }

      protected final void safeSetFailure(ChannelPromise var1, Throwable var2) {
         if(!(var1 instanceof VoidChannelPromise) && !var1.tryFailure(var2)) {
            AbstractChannel.logger.warn("Failed to mark a promise as failure because it\'s done already: {}", var1, var2);
         }

      }

      protected final void closeIfClosed() {
         if(!AbstractChannel.this.isOpen()) {
            this.close(this.voidPromise());
         }
      }

      private void invokeLater(Runnable var1) {
         try {
            AbstractChannel.this.eventLoop().execute(var1);
         } catch (RejectedExecutionException var3) {
            AbstractChannel.logger.warn("Can\'t invoke task later as EventLoop rejected it", (Throwable)var3);
         }

      }
   }
}
