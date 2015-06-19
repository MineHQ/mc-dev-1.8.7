package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.ThreadPerChannelEventLoopGroup;
import io.netty.util.concurrent.Future;

public class ThreadPerChannelEventLoop extends SingleThreadEventLoop {
   private final ThreadPerChannelEventLoopGroup parent;
   private Channel ch;

   public ThreadPerChannelEventLoop(ThreadPerChannelEventLoopGroup var1) {
      super(var1, var1.threadFactory, true);
      this.parent = var1;
   }

   public ChannelFuture register(Channel var1, ChannelPromise var2) {
      return super.register(var1, var2).addListener(new ChannelFutureListener() {
         public void operationComplete(ChannelFuture var1) throws Exception {
            if(var1.isSuccess()) {
               ThreadPerChannelEventLoop.this.ch = var1.channel();
            } else {
               ThreadPerChannelEventLoop.this.deregister();
            }

         }

         // $FF: synthetic method
         // $FF: bridge method
         public void operationComplete(Future var1) throws Exception {
            this.operationComplete((ChannelFuture)var1);
         }
      });
   }

   protected void run() {
      while(true) {
         Runnable var1 = this.takeTask();
         if(var1 != null) {
            var1.run();
            this.updateLastExecutionTime();
         }

         Channel var2 = this.ch;
         if(this.isShuttingDown()) {
            if(var2 != null) {
               var2.unsafe().close(var2.unsafe().voidPromise());
            }

            if(this.confirmShutdown()) {
               return;
            }
         } else if(var2 != null && !var2.isRegistered()) {
            this.runAllTasks();
            this.deregister();
         }
      }
   }

   protected void deregister() {
      this.ch = null;
      this.parent.activeChildren.remove(this);
      this.parent.idleChildren.add(this);
   }
}
