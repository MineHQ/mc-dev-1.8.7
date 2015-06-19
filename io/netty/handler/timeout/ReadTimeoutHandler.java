package io.netty.handler.timeout;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ReadTimeoutHandler extends ChannelInboundHandlerAdapter {
   private static final long MIN_TIMEOUT_NANOS;
   private final long timeoutNanos;
   private volatile ScheduledFuture<?> timeout;
   private volatile long lastReadTime;
   private volatile int state;
   private boolean closed;

   public ReadTimeoutHandler(int var1) {
      this((long)var1, TimeUnit.SECONDS);
   }

   public ReadTimeoutHandler(long var1, TimeUnit var3) {
      if(var3 == null) {
         throw new NullPointerException("unit");
      } else {
         if(var1 <= 0L) {
            this.timeoutNanos = 0L;
         } else {
            this.timeoutNanos = Math.max(var3.toNanos(var1), MIN_TIMEOUT_NANOS);
         }

      }
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
      var1.fireChannelRead(var2);
   }

   private void initialize(ChannelHandlerContext var1) {
      switch(this.state) {
      case 1:
      case 2:
         return;
      default:
         this.state = 1;
         this.lastReadTime = System.nanoTime();
         if(this.timeoutNanos > 0L) {
            this.timeout = var1.executor().schedule(new ReadTimeoutHandler.ReadTimeoutTask(var1), this.timeoutNanos, TimeUnit.NANOSECONDS);
         }

      }
   }

   private void destroy() {
      this.state = 2;
      if(this.timeout != null) {
         this.timeout.cancel(false);
         this.timeout = null;
      }

   }

   protected void readTimedOut(ChannelHandlerContext var1) throws Exception {
      if(!this.closed) {
         var1.fireExceptionCaught(ReadTimeoutException.INSTANCE);
         var1.close();
         this.closed = true;
      }

   }

   static {
      MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
   }

   private final class ReadTimeoutTask implements Runnable {
      private final ChannelHandlerContext ctx;

      ReadTimeoutTask(ChannelHandlerContext var2) {
         this.ctx = var2;
      }

      public void run() {
         if(this.ctx.channel().isOpen()) {
            long var1 = System.nanoTime();
            long var3 = ReadTimeoutHandler.this.timeoutNanos - (var1 - ReadTimeoutHandler.this.lastReadTime);
            if(var3 <= 0L) {
               ReadTimeoutHandler.this.timeout = this.ctx.executor().schedule(this, ReadTimeoutHandler.this.timeoutNanos, TimeUnit.NANOSECONDS);

               try {
                  ReadTimeoutHandler.this.readTimedOut(this.ctx);
               } catch (Throwable var6) {
                  this.ctx.fireExceptionCaught(var6);
               }
            } else {
               ReadTimeoutHandler.this.timeout = this.ctx.executor().schedule(this, var3, TimeUnit.NANOSECONDS);
            }

         }
      }
   }
}
