package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.channel.DefaultChannelProgressivePromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.FailedChannelFuture;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.SucceededChannelFuture;
import io.netty.channel.VoidChannelPromise;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.internal.OneTimeTask;
import io.netty.util.internal.RecyclableMpscLinkedQueueNode;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;

abstract class AbstractChannelHandlerContext extends DefaultAttributeMap implements ChannelHandlerContext {
   volatile AbstractChannelHandlerContext next;
   volatile AbstractChannelHandlerContext prev;
   private final boolean inbound;
   private final boolean outbound;
   private final AbstractChannel channel;
   private final DefaultChannelPipeline pipeline;
   private final String name;
   private boolean removed;
   final EventExecutor executor;
   private ChannelFuture succeededFuture;
   private volatile Runnable invokeChannelReadCompleteTask;
   private volatile Runnable invokeReadTask;
   private volatile Runnable invokeChannelWritableStateChangedTask;
   private volatile Runnable invokeFlushTask;

   AbstractChannelHandlerContext(DefaultChannelPipeline var1, EventExecutorGroup var2, String var3, boolean var4, boolean var5) {
      if(var3 == null) {
         throw new NullPointerException("name");
      } else {
         this.channel = var1.channel;
         this.pipeline = var1;
         this.name = var3;
         if(var2 != null) {
            EventExecutor var6 = (EventExecutor)var1.childExecutors.get(var2);
            if(var6 == null) {
               var6 = var2.next();
               var1.childExecutors.put(var2, var6);
            }

            this.executor = var6;
         } else {
            this.executor = null;
         }

         this.inbound = var4;
         this.outbound = var5;
      }
   }

   void teardown() {
      EventExecutor var1 = this.executor();
      if(var1.inEventLoop()) {
         this.teardown0();
      } else {
         var1.execute(new Runnable() {
            public void run() {
               AbstractChannelHandlerContext.this.teardown0();
            }
         });
      }

   }

   private void teardown0() {
      AbstractChannelHandlerContext var1 = this.prev;
      if(var1 != null) {
         DefaultChannelPipeline var2 = this.pipeline;
         synchronized(this.pipeline) {
            this.pipeline.remove0(this);
         }

         var1.teardown();
      }

   }

   public Channel channel() {
      return this.channel;
   }

   public ChannelPipeline pipeline() {
      return this.pipeline;
   }

   public ByteBufAllocator alloc() {
      return this.channel().config().getAllocator();
   }

   public EventExecutor executor() {
      return (EventExecutor)(this.executor == null?this.channel().eventLoop():this.executor);
   }

   public String name() {
      return this.name;
   }

   public ChannelHandlerContext fireChannelRegistered() {
      final AbstractChannelHandlerContext var1 = this.findContextInbound();
      EventExecutor var2 = var1.executor();
      if(var2.inEventLoop()) {
         var1.invokeChannelRegistered();
      } else {
         var2.execute(new OneTimeTask() {
            public void run() {
               var1.invokeChannelRegistered();
            }
         });
      }

      return this;
   }

   private void invokeChannelRegistered() {
      try {
         ((ChannelInboundHandler)this.handler()).channelRegistered(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelHandlerContext fireChannelUnregistered() {
      final AbstractChannelHandlerContext var1 = this.findContextInbound();
      EventExecutor var2 = var1.executor();
      if(var2.inEventLoop()) {
         var1.invokeChannelUnregistered();
      } else {
         var2.execute(new OneTimeTask() {
            public void run() {
               var1.invokeChannelUnregistered();
            }
         });
      }

      return this;
   }

   private void invokeChannelUnregistered() {
      try {
         ((ChannelInboundHandler)this.handler()).channelUnregistered(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelHandlerContext fireChannelActive() {
      final AbstractChannelHandlerContext var1 = this.findContextInbound();
      EventExecutor var2 = var1.executor();
      if(var2.inEventLoop()) {
         var1.invokeChannelActive();
      } else {
         var2.execute(new OneTimeTask() {
            public void run() {
               var1.invokeChannelActive();
            }
         });
      }

      return this;
   }

   private void invokeChannelActive() {
      try {
         ((ChannelInboundHandler)this.handler()).channelActive(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelHandlerContext fireChannelInactive() {
      final AbstractChannelHandlerContext var1 = this.findContextInbound();
      EventExecutor var2 = var1.executor();
      if(var2.inEventLoop()) {
         var1.invokeChannelInactive();
      } else {
         var2.execute(new OneTimeTask() {
            public void run() {
               var1.invokeChannelInactive();
            }
         });
      }

      return this;
   }

   private void invokeChannelInactive() {
      try {
         ((ChannelInboundHandler)this.handler()).channelInactive(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelHandlerContext fireExceptionCaught(final Throwable var1) {
      if(var1 == null) {
         throw new NullPointerException("cause");
      } else {
         final AbstractChannelHandlerContext var2 = this.next;
         EventExecutor var3 = var2.executor();
         if(var3.inEventLoop()) {
            var2.invokeExceptionCaught(var1);
         } else {
            try {
               var3.execute(new OneTimeTask() {
                  public void run() {
                     var2.invokeExceptionCaught(var1);
                  }
               });
            } catch (Throwable var5) {
               if(DefaultChannelPipeline.logger.isWarnEnabled()) {
                  DefaultChannelPipeline.logger.warn("Failed to submit an exceptionCaught() event.", var5);
                  DefaultChannelPipeline.logger.warn("The exceptionCaught() event that was failed to submit was:", var1);
               }
            }
         }

         return this;
      }
   }

   private void invokeExceptionCaught(Throwable var1) {
      try {
         this.handler().exceptionCaught(this, var1);
      } catch (Throwable var3) {
         if(DefaultChannelPipeline.logger.isWarnEnabled()) {
            DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler\'s exceptionCaught() method while handling the following exception:", var1);
         }
      }

   }

   public ChannelHandlerContext fireUserEventTriggered(final Object var1) {
      if(var1 == null) {
         throw new NullPointerException("event");
      } else {
         final AbstractChannelHandlerContext var2 = this.findContextInbound();
         EventExecutor var3 = var2.executor();
         if(var3.inEventLoop()) {
            var2.invokeUserEventTriggered(var1);
         } else {
            var3.execute(new OneTimeTask() {
               public void run() {
                  var2.invokeUserEventTriggered(var1);
               }
            });
         }

         return this;
      }
   }

   private void invokeUserEventTriggered(Object var1) {
      try {
         ((ChannelInboundHandler)this.handler()).userEventTriggered(this, var1);
      } catch (Throwable var3) {
         this.notifyHandlerException(var3);
      }

   }

   public ChannelHandlerContext fireChannelRead(final Object var1) {
      if(var1 == null) {
         throw new NullPointerException("msg");
      } else {
         final AbstractChannelHandlerContext var2 = this.findContextInbound();
         EventExecutor var3 = var2.executor();
         if(var3.inEventLoop()) {
            var2.invokeChannelRead(var1);
         } else {
            var3.execute(new OneTimeTask() {
               public void run() {
                  var2.invokeChannelRead(var1);
               }
            });
         }

         return this;
      }
   }

   private void invokeChannelRead(Object var1) {
      try {
         ((ChannelInboundHandler)this.handler()).channelRead(this, var1);
      } catch (Throwable var3) {
         this.notifyHandlerException(var3);
      }

   }

   public ChannelHandlerContext fireChannelReadComplete() {
      final AbstractChannelHandlerContext var1 = this.findContextInbound();
      EventExecutor var2 = var1.executor();
      if(var2.inEventLoop()) {
         var1.invokeChannelReadComplete();
      } else {
         Runnable var3 = var1.invokeChannelReadCompleteTask;
         if(var3 == null) {
            var1.invokeChannelReadCompleteTask = var3 = new Runnable() {
               public void run() {
                  var1.invokeChannelReadComplete();
               }
            };
         }

         var2.execute(var3);
      }

      return this;
   }

   private void invokeChannelReadComplete() {
      try {
         ((ChannelInboundHandler)this.handler()).channelReadComplete(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelHandlerContext fireChannelWritabilityChanged() {
      final AbstractChannelHandlerContext var1 = this.findContextInbound();
      EventExecutor var2 = var1.executor();
      if(var2.inEventLoop()) {
         var1.invokeChannelWritabilityChanged();
      } else {
         Runnable var3 = var1.invokeChannelWritableStateChangedTask;
         if(var3 == null) {
            var1.invokeChannelWritableStateChangedTask = var3 = new Runnable() {
               public void run() {
                  var1.invokeChannelWritabilityChanged();
               }
            };
         }

         var2.execute(var3);
      }

      return this;
   }

   private void invokeChannelWritabilityChanged() {
      try {
         ((ChannelInboundHandler)this.handler()).channelWritabilityChanged(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelFuture bind(SocketAddress var1) {
      return this.bind(var1, this.newPromise());
   }

   public ChannelFuture connect(SocketAddress var1) {
      return this.connect(var1, this.newPromise());
   }

   public ChannelFuture connect(SocketAddress var1, SocketAddress var2) {
      return this.connect(var1, var2, this.newPromise());
   }

   public ChannelFuture disconnect() {
      return this.disconnect(this.newPromise());
   }

   public ChannelFuture close() {
      return this.close(this.newPromise());
   }

   public ChannelFuture deregister() {
      return this.deregister(this.newPromise());
   }

   public ChannelFuture bind(final SocketAddress var1, final ChannelPromise var2) {
      if(var1 == null) {
         throw new NullPointerException("localAddress");
      } else if(!this.validatePromise(var2, false)) {
         return var2;
      } else {
         final AbstractChannelHandlerContext var3 = this.findContextOutbound();
         EventExecutor var4 = var3.executor();
         if(var4.inEventLoop()) {
            var3.invokeBind(var1, var2);
         } else {
            safeExecute(var4, new OneTimeTask() {
               public void run() {
                  var3.invokeBind(var1, var2);
               }
            }, var2, (Object)null);
         }

         return var2;
      }
   }

   private void invokeBind(SocketAddress var1, ChannelPromise var2) {
      try {
         ((ChannelOutboundHandler)this.handler()).bind(this, var1, var2);
      } catch (Throwable var4) {
         notifyOutboundHandlerException(var4, var2);
      }

   }

   public ChannelFuture connect(SocketAddress var1, ChannelPromise var2) {
      return this.connect(var1, (SocketAddress)null, var2);
   }

   public ChannelFuture connect(final SocketAddress var1, final SocketAddress var2, final ChannelPromise var3) {
      if(var1 == null) {
         throw new NullPointerException("remoteAddress");
      } else if(!this.validatePromise(var3, false)) {
         return var3;
      } else {
         final AbstractChannelHandlerContext var4 = this.findContextOutbound();
         EventExecutor var5 = var4.executor();
         if(var5.inEventLoop()) {
            var4.invokeConnect(var1, var2, var3);
         } else {
            safeExecute(var5, new OneTimeTask() {
               public void run() {
                  var4.invokeConnect(var1, var2, var3);
               }
            }, var3, (Object)null);
         }

         return var3;
      }
   }

   private void invokeConnect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
      try {
         ((ChannelOutboundHandler)this.handler()).connect(this, var1, var2, var3);
      } catch (Throwable var5) {
         notifyOutboundHandlerException(var5, var3);
      }

   }

   public ChannelFuture disconnect(final ChannelPromise var1) {
      if(!this.validatePromise(var1, false)) {
         return var1;
      } else {
         final AbstractChannelHandlerContext var2 = this.findContextOutbound();
         EventExecutor var3 = var2.executor();
         if(var3.inEventLoop()) {
            if(!this.channel().metadata().hasDisconnect()) {
               var2.invokeClose(var1);
            } else {
               var2.invokeDisconnect(var1);
            }
         } else {
            safeExecute(var3, new OneTimeTask() {
               public void run() {
                  if(!AbstractChannelHandlerContext.this.channel().metadata().hasDisconnect()) {
                     var2.invokeClose(var1);
                  } else {
                     var2.invokeDisconnect(var1);
                  }

               }
            }, var1, (Object)null);
         }

         return var1;
      }
   }

   private void invokeDisconnect(ChannelPromise var1) {
      try {
         ((ChannelOutboundHandler)this.handler()).disconnect(this, var1);
      } catch (Throwable var3) {
         notifyOutboundHandlerException(var3, var1);
      }

   }

   public ChannelFuture close(final ChannelPromise var1) {
      if(!this.validatePromise(var1, false)) {
         return var1;
      } else {
         final AbstractChannelHandlerContext var2 = this.findContextOutbound();
         EventExecutor var3 = var2.executor();
         if(var3.inEventLoop()) {
            var2.invokeClose(var1);
         } else {
            safeExecute(var3, new OneTimeTask() {
               public void run() {
                  var2.invokeClose(var1);
               }
            }, var1, (Object)null);
         }

         return var1;
      }
   }

   private void invokeClose(ChannelPromise var1) {
      try {
         ((ChannelOutboundHandler)this.handler()).close(this, var1);
      } catch (Throwable var3) {
         notifyOutboundHandlerException(var3, var1);
      }

   }

   public ChannelFuture deregister(final ChannelPromise var1) {
      if(!this.validatePromise(var1, false)) {
         return var1;
      } else {
         final AbstractChannelHandlerContext var2 = this.findContextOutbound();
         EventExecutor var3 = var2.executor();
         if(var3.inEventLoop()) {
            var2.invokeDeregister(var1);
         } else {
            safeExecute(var3, new OneTimeTask() {
               public void run() {
                  var2.invokeDeregister(var1);
               }
            }, var1, (Object)null);
         }

         return var1;
      }
   }

   private void invokeDeregister(ChannelPromise var1) {
      try {
         ((ChannelOutboundHandler)this.handler()).deregister(this, var1);
      } catch (Throwable var3) {
         notifyOutboundHandlerException(var3, var1);
      }

   }

   public ChannelHandlerContext read() {
      final AbstractChannelHandlerContext var1 = this.findContextOutbound();
      EventExecutor var2 = var1.executor();
      if(var2.inEventLoop()) {
         var1.invokeRead();
      } else {
         Runnable var3 = var1.invokeReadTask;
         if(var3 == null) {
            var1.invokeReadTask = var3 = new Runnable() {
               public void run() {
                  var1.invokeRead();
               }
            };
         }

         var2.execute(var3);
      }

      return this;
   }

   private void invokeRead() {
      try {
         ((ChannelOutboundHandler)this.handler()).read(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelFuture write(Object var1) {
      return this.write(var1, this.newPromise());
   }

   public ChannelFuture write(Object var1, ChannelPromise var2) {
      if(var1 == null) {
         throw new NullPointerException("msg");
      } else if(!this.validatePromise(var2, true)) {
         ReferenceCountUtil.release(var1);
         return var2;
      } else {
         this.write(var1, false, var2);
         return var2;
      }
   }

   private void invokeWrite(Object var1, ChannelPromise var2) {
      try {
         ((ChannelOutboundHandler)this.handler()).write(this, var1, var2);
      } catch (Throwable var4) {
         notifyOutboundHandlerException(var4, var2);
      }

   }

   public ChannelHandlerContext flush() {
      final AbstractChannelHandlerContext var1 = this.findContextOutbound();
      EventExecutor var2 = var1.executor();
      if(var2.inEventLoop()) {
         var1.invokeFlush();
      } else {
         Runnable var3 = var1.invokeFlushTask;
         if(var3 == null) {
            var1.invokeFlushTask = var3 = new Runnable() {
               public void run() {
                  var1.invokeFlush();
               }
            };
         }

         safeExecute(var2, var3, this.channel.voidPromise(), (Object)null);
      }

      return this;
   }

   private void invokeFlush() {
      try {
         ((ChannelOutboundHandler)this.handler()).flush(this);
      } catch (Throwable var2) {
         this.notifyHandlerException(var2);
      }

   }

   public ChannelFuture writeAndFlush(Object var1, ChannelPromise var2) {
      if(var1 == null) {
         throw new NullPointerException("msg");
      } else if(!this.validatePromise(var2, true)) {
         ReferenceCountUtil.release(var1);
         return var2;
      } else {
         this.write(var1, true, var2);
         return var2;
      }
   }

   private void write(Object var1, boolean var2, ChannelPromise var3) {
      AbstractChannelHandlerContext var4 = this.findContextOutbound();
      EventExecutor var5 = var4.executor();
      if(var5.inEventLoop()) {
         var4.invokeWrite(var1, var3);
         if(var2) {
            var4.invokeFlush();
         }
      } else {
         int var6 = this.channel.estimatorHandle().size(var1);
         if(var6 > 0) {
            ChannelOutboundBuffer var7 = this.channel.unsafe().outboundBuffer();
            if(var7 != null) {
               var7.incrementPendingOutboundBytes((long)var6);
            }
         }

         Object var8;
         if(var2) {
            var8 = AbstractChannelHandlerContext.WriteAndFlushTask.newInstance(var4, var1, var6, var3);
         } else {
            var8 = AbstractChannelHandlerContext.WriteTask.newInstance(var4, var1, var6, var3);
         }

         safeExecute(var5, (Runnable)var8, var3, var1);
      }

   }

   public ChannelFuture writeAndFlush(Object var1) {
      return this.writeAndFlush(var1, this.newPromise());
   }

   private static void notifyOutboundHandlerException(Throwable var0, ChannelPromise var1) {
      if(!(var1 instanceof VoidChannelPromise)) {
         if(!var1.tryFailure(var0) && DefaultChannelPipeline.logger.isWarnEnabled()) {
            DefaultChannelPipeline.logger.warn("Failed to fail the promise because it\'s done already: {}", var1, var0);
         }

      }
   }

   private void notifyHandlerException(Throwable var1) {
      if(inExceptionCaught(var1)) {
         if(DefaultChannelPipeline.logger.isWarnEnabled()) {
            DefaultChannelPipeline.logger.warn("An exception was thrown by a user handler while handling an exceptionCaught event", var1);
         }

      } else {
         this.invokeExceptionCaught(var1);
      }
   }

   private static boolean inExceptionCaught(Throwable var0) {
      do {
         StackTraceElement[] var1 = var0.getStackTrace();
         if(var1 != null) {
            StackTraceElement[] var2 = var1;
            int var3 = var1.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               StackTraceElement var5 = var2[var4];
               if(var5 == null) {
                  break;
               }

               if("exceptionCaught".equals(var5.getMethodName())) {
                  return true;
               }
            }
         }

         var0 = var0.getCause();
      } while(var0 != null);

      return false;
   }

   public ChannelPromise newPromise() {
      return new DefaultChannelPromise(this.channel(), this.executor());
   }

   public ChannelProgressivePromise newProgressivePromise() {
      return new DefaultChannelProgressivePromise(this.channel(), this.executor());
   }

   public ChannelFuture newSucceededFuture() {
      Object var1 = this.succeededFuture;
      if(var1 == null) {
         this.succeededFuture = (ChannelFuture)(var1 = new SucceededChannelFuture(this.channel(), this.executor()));
      }

      return (ChannelFuture)var1;
   }

   public ChannelFuture newFailedFuture(Throwable var1) {
      return new FailedChannelFuture(this.channel(), this.executor(), var1);
   }

   private boolean validatePromise(ChannelPromise var1, boolean var2) {
      if(var1 == null) {
         throw new NullPointerException("promise");
      } else if(var1.isDone()) {
         if(var1.isCancelled()) {
            return false;
         } else {
            throw new IllegalArgumentException("promise already done: " + var1);
         }
      } else if(var1.channel() != this.channel()) {
         throw new IllegalArgumentException(String.format("promise.channel does not match: %s (expected: %s)", new Object[]{var1.channel(), this.channel()}));
      } else if(var1.getClass() == DefaultChannelPromise.class) {
         return true;
      } else if(!var2 && var1 instanceof VoidChannelPromise) {
         throw new IllegalArgumentException(StringUtil.simpleClassName(VoidChannelPromise.class) + " not allowed for this operation");
      } else if(var1 instanceof AbstractChannel.CloseFuture) {
         throw new IllegalArgumentException(StringUtil.simpleClassName(AbstractChannel.CloseFuture.class) + " not allowed in a pipeline");
      } else {
         return true;
      }
   }

   private AbstractChannelHandlerContext findContextInbound() {
      AbstractChannelHandlerContext var1 = this;

      do {
         var1 = var1.next;
      } while(!var1.inbound);

      return var1;
   }

   private AbstractChannelHandlerContext findContextOutbound() {
      AbstractChannelHandlerContext var1 = this;

      do {
         var1 = var1.prev;
      } while(!var1.outbound);

      return var1;
   }

   public ChannelPromise voidPromise() {
      return this.channel.voidPromise();
   }

   void setRemoved() {
      this.removed = true;
   }

   public boolean isRemoved() {
      return this.removed;
   }

   private static void safeExecute(EventExecutor var0, Runnable var1, ChannelPromise var2, Object var3) {
      try {
         var0.execute(var1);
      } catch (Throwable var9) {
         Throwable var4 = var9;

         try {
            var2.setFailure(var4);
         } finally {
            if(var3 != null) {
               ReferenceCountUtil.release(var3);
            }

         }
      }

   }

   static final class WriteAndFlushTask extends AbstractChannelHandlerContext.AbstractWriteTask {
      private static final Recycler<AbstractChannelHandlerContext.WriteAndFlushTask> RECYCLER = new Recycler() {
         protected AbstractChannelHandlerContext.WriteAndFlushTask newObject(Recycler.Handle var1) {
            return new AbstractChannelHandlerContext.WriteAndFlushTask(var1, null);
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object newObject(Recycler.Handle var1) {
            return this.newObject(var1);
         }
      };

      private static AbstractChannelHandlerContext.WriteAndFlushTask newInstance(AbstractChannelHandlerContext var0, Object var1, int var2, ChannelPromise var3) {
         AbstractChannelHandlerContext.WriteAndFlushTask var4 = (AbstractChannelHandlerContext.WriteAndFlushTask)RECYCLER.get();
         init(var4, var0, var1, var2, var3);
         return var4;
      }

      private WriteAndFlushTask(Recycler.Handle var1) {
         super(var1, null);
      }

      public void write(AbstractChannelHandlerContext var1, Object var2, ChannelPromise var3) {
         super.write(var1, var2, var3);
         var1.invokeFlush();
      }

      protected void recycle(Recycler.Handle var1) {
         RECYCLER.recycle(this, var1);
      }

      // $FF: synthetic method
      WriteAndFlushTask(Recycler.Handle var1, Object var2) {
         this(var1);
      }
   }

   static final class WriteTask extends AbstractChannelHandlerContext.AbstractWriteTask implements SingleThreadEventLoop.NonWakeupRunnable {
      private static final Recycler<AbstractChannelHandlerContext.WriteTask> RECYCLER = new Recycler() {
         protected AbstractChannelHandlerContext.WriteTask newObject(Recycler.Handle var1) {
            return new AbstractChannelHandlerContext.WriteTask(var1, null);
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object newObject(Recycler.Handle var1) {
            return this.newObject(var1);
         }
      };

      private static AbstractChannelHandlerContext.WriteTask newInstance(AbstractChannelHandlerContext var0, Object var1, int var2, ChannelPromise var3) {
         AbstractChannelHandlerContext.WriteTask var4 = (AbstractChannelHandlerContext.WriteTask)RECYCLER.get();
         init(var4, var0, var1, var2, var3);
         return var4;
      }

      private WriteTask(Recycler.Handle var1) {
         super(var1, null);
      }

      protected void recycle(Recycler.Handle var1) {
         RECYCLER.recycle(this, var1);
      }

      // $FF: synthetic method
      WriteTask(Recycler.Handle var1, Object var2) {
         this(var1);
      }
   }

   abstract static class AbstractWriteTask extends RecyclableMpscLinkedQueueNode<Runnable> implements Runnable {
      private AbstractChannelHandlerContext ctx;
      private Object msg;
      private ChannelPromise promise;
      private int size;

      private AbstractWriteTask(Recycler.Handle var1) {
         super(var1);
      }

      protected static void init(AbstractChannelHandlerContext.AbstractWriteTask var0, AbstractChannelHandlerContext var1, Object var2, int var3, ChannelPromise var4) {
         var0.ctx = var1;
         var0.msg = var2;
         var0.promise = var4;
         var0.size = var3;
      }

      public final void run() {
         try {
            if(this.size > 0) {
               ChannelOutboundBuffer var1 = this.ctx.channel.unsafe().outboundBuffer();
               if(var1 != null) {
                  var1.decrementPendingOutboundBytes((long)this.size);
               }
            }

            this.write(this.ctx, this.msg, this.promise);
         } finally {
            this.ctx = null;
            this.msg = null;
            this.promise = null;
         }

      }

      public Runnable value() {
         return this;
      }

      protected void write(AbstractChannelHandlerContext var1, Object var2, ChannelPromise var3) {
         var1.invokeWrite(var2, var3);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object value() {
         return this.value();
      }

      // $FF: synthetic method
      AbstractWriteTask(Recycler.Handle var1, Object var2) {
         this(var1);
      }
   }
}
