package io.netty.handler.timeout;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.WriteTimeoutException;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WriteTimeoutHandler extends ChannelOutboundHandlerAdapter {
   private static final long MIN_TIMEOUT_NANOS;
   private final long timeoutNanos;
   private boolean closed;

   public WriteTimeoutHandler(int var1) {
      this((long)var1, TimeUnit.SECONDS);
   }

   public WriteTimeoutHandler(long var1, TimeUnit var3) {
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

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      this.scheduleTimeout(var1, var3);
      var1.write(var2, var3);
   }

   private void scheduleTimeout(final ChannelHandlerContext var1, final ChannelPromise var2) {
      if(this.timeoutNanos > 0L) {
         final ScheduledFuture var3 = var1.executor().schedule(new Runnable() {
            public void run() {
               if(!var2.isDone()) {
                  try {
                     WriteTimeoutHandler.this.writeTimedOut(var1);
                  } catch (Throwable var2x) {
                     var1.fireExceptionCaught(var2x);
                  }
               }

            }
         }, this.timeoutNanos, TimeUnit.NANOSECONDS);
         var2.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture var1) throws Exception {
               var3.cancel(false);
            }

            // $FF: synthetic method
            // $FF: bridge method
            public void operationComplete(Future var1) throws Exception {
               this.operationComplete((ChannelFuture)var1);
            }
         });
      }

   }

   protected void writeTimedOut(ChannelHandlerContext var1) throws Exception {
      if(!this.closed) {
         var1.fireExceptionCaught(WriteTimeoutException.INSTANCE);
         var1.close();
         this.closed = true;
      }

   }

   static {
      MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
   }
}
