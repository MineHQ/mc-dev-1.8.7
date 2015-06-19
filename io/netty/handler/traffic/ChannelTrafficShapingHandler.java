package io.netty.handler.traffic;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChannelTrafficShapingHandler extends AbstractTrafficShapingHandler {
   private List<ChannelTrafficShapingHandler.ToSend> messagesQueue = new LinkedList();

   public ChannelTrafficShapingHandler(long var1, long var3, long var5, long var7) {
      super(var1, var3, var5, var7);
   }

   public ChannelTrafficShapingHandler(long var1, long var3, long var5) {
      super(var1, var3, var5);
   }

   public ChannelTrafficShapingHandler(long var1, long var3) {
      super(var1, var3);
   }

   public ChannelTrafficShapingHandler(long var1) {
      super(var1);
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      TrafficCounter var2 = new TrafficCounter(this, var1.executor(), "ChannelTC" + var1.channel().hashCode(), this.checkInterval);
      this.setTrafficCounter(var2);
      var2.start();
   }

   public synchronized void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      if(this.trafficCounter != null) {
         this.trafficCounter.stop();
      }

      Iterator var2 = this.messagesQueue.iterator();

      while(var2.hasNext()) {
         ChannelTrafficShapingHandler.ToSend var3 = (ChannelTrafficShapingHandler.ToSend)var2.next();
         if(var3.toSend instanceof ByteBuf) {
            ((ByteBuf)var3.toSend).release();
         }
      }

      this.messagesQueue.clear();
   }

   protected synchronized void submitWrite(final ChannelHandlerContext var1, Object var2, long var3, ChannelPromise var5) {
      if(var3 == 0L && this.messagesQueue.isEmpty()) {
         var1.write(var2, var5);
      } else {
         ChannelTrafficShapingHandler.ToSend var6 = new ChannelTrafficShapingHandler.ToSend(var3, var2, var5, null);
         this.messagesQueue.add(var6);
         var1.executor().schedule(new Runnable() {
            public void run() {
               ChannelTrafficShapingHandler.this.sendAllValid(var1);
            }
         }, var3, TimeUnit.MILLISECONDS);
      }
   }

   private synchronized void sendAllValid(ChannelHandlerContext var1) {
      while(true) {
         if(!this.messagesQueue.isEmpty()) {
            ChannelTrafficShapingHandler.ToSend var2 = (ChannelTrafficShapingHandler.ToSend)this.messagesQueue.remove(0);
            if(var2.date <= System.currentTimeMillis()) {
               var1.write(var2.toSend, var2.promise);
               continue;
            }

            this.messagesQueue.add(0, var2);
         }

         var1.flush();
         return;
      }
   }

   private static final class ToSend {
      final long date;
      final Object toSend;
      final ChannelPromise promise;

      private ToSend(long var1, Object var3, ChannelPromise var4) {
         this.date = System.currentTimeMillis() + var1;
         this.toSend = var3;
         this.promise = var4;
      }

      // $FF: synthetic method
      ToSend(long var1, Object var3, ChannelPromise var4, Object var5) {
         this(var1, var3, var4);
      }
   }
}
