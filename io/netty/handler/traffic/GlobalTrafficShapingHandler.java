package io.netty.handler.traffic;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import io.netty.util.concurrent.EventExecutor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class GlobalTrafficShapingHandler extends AbstractTrafficShapingHandler {
   private Map<Integer, List<GlobalTrafficShapingHandler.ToSend>> messagesQueues = new HashMap();

   void createGlobalTrafficCounter(ScheduledExecutorService var1) {
      if(var1 == null) {
         throw new NullPointerException("executor");
      } else {
         TrafficCounter var2 = new TrafficCounter(this, var1, "GlobalTC", this.checkInterval);
         this.setTrafficCounter(var2);
         var2.start();
      }
   }

   public GlobalTrafficShapingHandler(ScheduledExecutorService var1, long var2, long var4, long var6, long var8) {
      super(var2, var4, var6, var8);
      this.createGlobalTrafficCounter(var1);
   }

   public GlobalTrafficShapingHandler(ScheduledExecutorService var1, long var2, long var4, long var6) {
      super(var2, var4, var6);
      this.createGlobalTrafficCounter(var1);
   }

   public GlobalTrafficShapingHandler(ScheduledExecutorService var1, long var2, long var4) {
      super(var2, var4);
      this.createGlobalTrafficCounter(var1);
   }

   public GlobalTrafficShapingHandler(ScheduledExecutorService var1, long var2) {
      super(var2);
      this.createGlobalTrafficCounter(var1);
   }

   public GlobalTrafficShapingHandler(EventExecutor var1) {
      this.createGlobalTrafficCounter(var1);
   }

   public final void release() {
      if(this.trafficCounter != null) {
         this.trafficCounter.stop();
      }

   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      Integer var2 = Integer.valueOf(var1.channel().hashCode());
      LinkedList var3 = new LinkedList();
      this.messagesQueues.put(var2, var3);
   }

   public synchronized void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      Integer var2 = Integer.valueOf(var1.channel().hashCode());
      List var3 = (List)this.messagesQueues.remove(var2);
      if(var3 != null) {
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            GlobalTrafficShapingHandler.ToSend var5 = (GlobalTrafficShapingHandler.ToSend)var4.next();
            if(var5.toSend instanceof ByteBuf) {
               ((ByteBuf)var5.toSend).release();
            }
         }

         var3.clear();
      }

   }

   protected synchronized void submitWrite(final ChannelHandlerContext var1, Object var2, long var3, ChannelPromise var5) {
      Integer var6 = Integer.valueOf(var1.channel().hashCode());
      final Object var7 = (List)this.messagesQueues.get(var6);
      if(var3 != 0L || var7 != null && !((List)var7).isEmpty()) {
         GlobalTrafficShapingHandler.ToSend var8 = new GlobalTrafficShapingHandler.ToSend(var3, var2, var5, null);
         if(var7 == null) {
            var7 = new LinkedList();
            this.messagesQueues.put(var6, var7);
         }

         ((List)var7).add(var8);
         var1.executor().schedule(new Runnable() {
            public void run() {
               GlobalTrafficShapingHandler.this.sendAllValid(var1, (List)var7);
            }
         }, var3, TimeUnit.MILLISECONDS);
      } else {
         var1.write(var2, var5);
      }
   }

   private synchronized void sendAllValid(ChannelHandlerContext var1, List<GlobalTrafficShapingHandler.ToSend> var2) {
      while(true) {
         if(!var2.isEmpty()) {
            GlobalTrafficShapingHandler.ToSend var3 = (GlobalTrafficShapingHandler.ToSend)var2.remove(0);
            if(var3.date <= System.currentTimeMillis()) {
               var1.write(var3.toSend, var3.promise);
               continue;
            }

            var2.add(0, var3);
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
