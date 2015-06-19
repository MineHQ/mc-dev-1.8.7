package io.netty.handler.timeout;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class IdleStateHandler extends ChannelDuplexHandler {
   private static final long MIN_TIMEOUT_NANOS;
   private final long readerIdleTimeNanos;
   private final long writerIdleTimeNanos;
   private final long allIdleTimeNanos;
   volatile ScheduledFuture<?> readerIdleTimeout;
   volatile long lastReadTime;
   private boolean firstReaderIdleEvent;
   volatile ScheduledFuture<?> writerIdleTimeout;
   volatile long lastWriteTime;
   private boolean firstWriterIdleEvent;
   volatile ScheduledFuture<?> allIdleTimeout;
   private boolean firstAllIdleEvent;
   private volatile int state;

   public IdleStateHandler(int var1, int var2, int var3) {
      this((long)var1, (long)var2, (long)var3, TimeUnit.SECONDS);
   }

   public IdleStateHandler(long var1, long var3, long var5, TimeUnit var7) {
      this.firstReaderIdleEvent = true;
      this.firstWriterIdleEvent = true;
      this.firstAllIdleEvent = true;
      if(var7 == null) {
         throw new NullPointerException("unit");
      } else {
         if(var1 <= 0L) {
            this.readerIdleTimeNanos = 0L;
         } else {
            this.readerIdleTimeNanos = Math.max(var7.toNanos(var1), MIN_TIMEOUT_NANOS);
         }

         if(var3 <= 0L) {
            this.writerIdleTimeNanos = 0L;
         } else {
            this.writerIdleTimeNanos = Math.max(var7.toNanos(var3), MIN_TIMEOUT_NANOS);
         }

         if(var5 <= 0L) {
            this.allIdleTimeNanos = 0L;
         } else {
            this.allIdleTimeNanos = Math.max(var7.toNanos(var5), MIN_TIMEOUT_NANOS);
         }

      }
   }

   public long getReaderIdleTimeInMillis() {
      return TimeUnit.NANOSECONDS.toMillis(this.readerIdleTimeNanos);
   }

   public long getWriterIdleTimeInMillis() {
      return TimeUnit.NANOSECONDS.toMillis(this.writerIdleTimeNanos);
   }

   public long getAllIdleTimeInMillis() {
      return TimeUnit.NANOSECONDS.toMillis(this.allIdleTimeNanos);
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      if(var1.channel().isActive() && var1.channel().isRegistered()) {
         this.initialize(var1);
      }

   }

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      this.destroy();
   }

   public void channelRegistered(ChannelHandlerContext var1) throws Exception {
      if(var1.channel().isActive()) {
         this.initialize(var1);
      }

      super.channelRegistered(var1);
   }

   public void channelActive(ChannelHandlerContext var1) throws Exception {
      this.initialize(var1);
      super.channelActive(var1);
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      this.destroy();
      super.channelInactive(var1);
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      this.lastReadTime = System.nanoTime();
      this.firstReaderIdleEvent = this.firstAllIdleEvent = true;
      var1.fireChannelRead(var2);
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      var3.addListener(new ChannelFutureListener() {
         public void operationComplete(ChannelFuture var1) throws Exception {
            IdleStateHandler.this.lastWriteTime = System.nanoTime();
            IdleStateHandler.this.firstWriterIdleEvent = IdleStateHandler.this.firstAllIdleEvent = true;
         }

         // $FF: synthetic method
         // $FF: bridge method
         public void operationComplete(Future var1) throws Exception {
            this.operationComplete((ChannelFuture)var1);
         }
      });
      var1.write(var2, var3);
   }

   private void initialize(ChannelHandlerContext var1) {
      switch(this.state) {
      case 1:
      case 2:
         return;
      default:
         this.state = 1;
         EventExecutor var2 = var1.executor();
         this.lastReadTime = this.lastWriteTime = System.nanoTime();
         if(this.readerIdleTimeNanos > 0L) {
            this.readerIdleTimeout = var2.schedule(new IdleStateHandler.ReaderIdleTimeoutTask(var1), this.readerIdleTimeNanos, TimeUnit.NANOSECONDS);
         }

         if(this.writerIdleTimeNanos > 0L) {
            this.writerIdleTimeout = var2.schedule(new IdleStateHandler.WriterIdleTimeoutTask(var1), this.writerIdleTimeNanos, TimeUnit.NANOSECONDS);
         }

         if(this.allIdleTimeNanos > 0L) {
            this.allIdleTimeout = var2.schedule(new IdleStateHandler.AllIdleTimeoutTask(var1), this.allIdleTimeNanos, TimeUnit.NANOSECONDS);
         }

      }
   }

   private void destroy() {
      this.state = 2;
      if(this.readerIdleTimeout != null) {
         this.readerIdleTimeout.cancel(false);
         this.readerIdleTimeout = null;
      }

      if(this.writerIdleTimeout != null) {
         this.writerIdleTimeout.cancel(false);
         this.writerIdleTimeout = null;
      }

      if(this.allIdleTimeout != null) {
         this.allIdleTimeout.cancel(false);
         this.allIdleTimeout = null;
      }

   }

   protected void channelIdle(ChannelHandlerContext var1, IdleStateEvent var2) throws Exception {
      var1.fireUserEventTriggered(var2);
   }

   static {
      MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
   }

   private final class AllIdleTimeoutTask implements Runnable {
      private final ChannelHandlerContext ctx;

      AllIdleTimeoutTask(ChannelHandlerContext var2) {
         this.ctx = var2;
      }

      public void run() {
         if(this.ctx.channel().isOpen()) {
            long var1 = System.nanoTime();
            long var3 = Math.max(IdleStateHandler.this.lastReadTime, IdleStateHandler.this.lastWriteTime);
            long var5 = IdleStateHandler.this.allIdleTimeNanos - (var1 - var3);
            if(var5 <= 0L) {
               IdleStateHandler.this.allIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.this.allIdleTimeNanos, TimeUnit.NANOSECONDS);

               try {
                  IdleStateEvent var7;
                  if(IdleStateHandler.this.firstAllIdleEvent) {
                     IdleStateHandler.this.firstAllIdleEvent = false;
                     var7 = IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT;
                  } else {
                     var7 = IdleStateEvent.ALL_IDLE_STATE_EVENT;
                  }

                  IdleStateHandler.this.channelIdle(this.ctx, var7);
               } catch (Throwable var8) {
                  this.ctx.fireExceptionCaught(var8);
               }
            } else {
               IdleStateHandler.this.allIdleTimeout = this.ctx.executor().schedule(this, var5, TimeUnit.NANOSECONDS);
            }

         }
      }
   }

   private final class WriterIdleTimeoutTask implements Runnable {
      private final ChannelHandlerContext ctx;

      WriterIdleTimeoutTask(ChannelHandlerContext var2) {
         this.ctx = var2;
      }

      public void run() {
         if(this.ctx.channel().isOpen()) {
            long var1 = System.nanoTime();
            long var3 = IdleStateHandler.this.lastWriteTime;
            long var5 = IdleStateHandler.this.writerIdleTimeNanos - (var1 - var3);
            if(var5 <= 0L) {
               IdleStateHandler.this.writerIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.this.writerIdleTimeNanos, TimeUnit.NANOSECONDS);

               try {
                  IdleStateEvent var7;
                  if(IdleStateHandler.this.firstWriterIdleEvent) {
                     IdleStateHandler.this.firstWriterIdleEvent = false;
                     var7 = IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT;
                  } else {
                     var7 = IdleStateEvent.WRITER_IDLE_STATE_EVENT;
                  }

                  IdleStateHandler.this.channelIdle(this.ctx, var7);
               } catch (Throwable var8) {
                  this.ctx.fireExceptionCaught(var8);
               }
            } else {
               IdleStateHandler.this.writerIdleTimeout = this.ctx.executor().schedule(this, var5, TimeUnit.NANOSECONDS);
            }

         }
      }
   }

   private final class ReaderIdleTimeoutTask implements Runnable {
      private final ChannelHandlerContext ctx;

      ReaderIdleTimeoutTask(ChannelHandlerContext var2) {
         this.ctx = var2;
      }

      public void run() {
         if(this.ctx.channel().isOpen()) {
            long var1 = System.nanoTime();
            long var3 = IdleStateHandler.this.lastReadTime;
            long var5 = IdleStateHandler.this.readerIdleTimeNanos - (var1 - var3);
            if(var5 <= 0L) {
               IdleStateHandler.this.readerIdleTimeout = this.ctx.executor().schedule(this, IdleStateHandler.this.readerIdleTimeNanos, TimeUnit.NANOSECONDS);

               try {
                  IdleStateEvent var7;
                  if(IdleStateHandler.this.firstReaderIdleEvent) {
                     IdleStateHandler.this.firstReaderIdleEvent = false;
                     var7 = IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT;
                  } else {
                     var7 = IdleStateEvent.READER_IDLE_STATE_EVENT;
                  }

                  IdleStateHandler.this.channelIdle(this.ctx, var7);
               } catch (Throwable var8) {
                  this.ctx.fireExceptionCaught(var8);
               }
            } else {
               IdleStateHandler.this.readerIdleTimeout = this.ctx.executor().schedule(this, var5, TimeUnit.NANOSECONDS);
            }

         }
      }
   }
}
