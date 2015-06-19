package io.netty.handler.traffic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.traffic.TrafficCounter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;

public abstract class AbstractTrafficShapingHandler extends ChannelDuplexHandler {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractTrafficShapingHandler.class);
   public static final long DEFAULT_CHECK_INTERVAL = 1000L;
   public static final long DEFAULT_MAX_TIME = 15000L;
   static final long MINIMAL_WAIT = 10L;
   protected TrafficCounter trafficCounter;
   private long writeLimit;
   private long readLimit;
   protected long maxTime;
   protected long checkInterval;
   private static final AttributeKey<Boolean> READ_SUSPENDED = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".READ_SUSPENDED");
   private static final AttributeKey<Runnable> REOPEN_TASK = AttributeKey.valueOf(AbstractTrafficShapingHandler.class.getName() + ".REOPEN_TASK");

   void setTrafficCounter(TrafficCounter var1) {
      this.trafficCounter = var1;
   }

   protected AbstractTrafficShapingHandler(long var1, long var3, long var5, long var7) {
      this.maxTime = 15000L;
      this.checkInterval = 1000L;
      this.writeLimit = var1;
      this.readLimit = var3;
      this.checkInterval = var5;
      this.maxTime = var7;
   }

   protected AbstractTrafficShapingHandler(long var1, long var3, long var5) {
      this(var1, var3, var5, 15000L);
   }

   protected AbstractTrafficShapingHandler(long var1, long var3) {
      this(var1, var3, 1000L, 15000L);
   }

   protected AbstractTrafficShapingHandler() {
      this(0L, 0L, 1000L, 15000L);
   }

   protected AbstractTrafficShapingHandler(long var1) {
      this(0L, 0L, var1, 15000L);
   }

   public void configure(long var1, long var3, long var5) {
      this.configure(var1, var3);
      this.configure(var5);
   }

   public void configure(long var1, long var3) {
      this.writeLimit = var1;
      this.readLimit = var3;
      if(this.trafficCounter != null) {
         this.trafficCounter.resetAccounting(System.currentTimeMillis() + 1L);
      }

   }

   public void configure(long var1) {
      this.checkInterval = var1;
      if(this.trafficCounter != null) {
         this.trafficCounter.configure(this.checkInterval);
      }

   }

   public long getWriteLimit() {
      return this.writeLimit;
   }

   public void setWriteLimit(long var1) {
      this.writeLimit = var1;
      if(this.trafficCounter != null) {
         this.trafficCounter.resetAccounting(System.currentTimeMillis() + 1L);
      }

   }

   public long getReadLimit() {
      return this.readLimit;
   }

   public void setReadLimit(long var1) {
      this.readLimit = var1;
      if(this.trafficCounter != null) {
         this.trafficCounter.resetAccounting(System.currentTimeMillis() + 1L);
      }

   }

   public long getCheckInterval() {
      return this.checkInterval;
   }

   public void setCheckInterval(long var1) {
      this.checkInterval = var1;
      if(this.trafficCounter != null) {
         this.trafficCounter.configure(var1);
      }

   }

   public void setMaxTimeWait(long var1) {
      this.maxTime = var1;
   }

   public long getMaxTimeWait() {
      return this.maxTime;
   }

   protected void doAccounting(TrafficCounter var1) {
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      long var3 = this.calculateSize(var2);
      if(var3 > 0L && this.trafficCounter != null) {
         long var5 = this.trafficCounter.readTimeToWait(var3, this.readLimit, this.maxTime);
         if(var5 >= 10L) {
            if(logger.isDebugEnabled()) {
               logger.debug("Channel:" + var1.channel().hashCode() + " Read Suspend: " + var5 + ":" + var1.channel().config().isAutoRead() + ":" + isHandlerActive(var1));
            }

            if(var1.channel().config().isAutoRead() && isHandlerActive(var1)) {
               var1.channel().config().setAutoRead(false);
               var1.attr(READ_SUSPENDED).set(Boolean.valueOf(true));
               Attribute var7 = var1.attr(REOPEN_TASK);
               Object var8 = (Runnable)var7.get();
               if(var8 == null) {
                  var8 = new AbstractTrafficShapingHandler.ReopenReadTimerTask(var1);
                  var7.set(var8);
               }

               var1.executor().schedule((Runnable)var8, var5, TimeUnit.MILLISECONDS);
               if(logger.isDebugEnabled()) {
                  logger.debug("Channel:" + var1.channel().hashCode() + " Suspend final status => " + var1.channel().config().isAutoRead() + ":" + isHandlerActive(var1) + " will reopened at: " + var5);
               }
            }
         }
      }

      var1.fireChannelRead(var2);
   }

   protected static boolean isHandlerActive(ChannelHandlerContext var0) {
      Boolean var1 = (Boolean)var0.attr(READ_SUSPENDED).get();
      return var1 == null || Boolean.FALSE.equals(var1);
   }

   public void read(ChannelHandlerContext var1) {
      if(isHandlerActive(var1)) {
         var1.read();
      }

   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      long var4 = this.calculateSize(var2);
      if(var4 > 0L && this.trafficCounter != null) {
         long var6 = this.trafficCounter.writeTimeToWait(var4, this.writeLimit, this.maxTime);
         if(var6 >= 10L) {
            if(logger.isDebugEnabled()) {
               logger.debug("Channel:" + var1.channel().hashCode() + " Write suspend: " + var6 + ":" + var1.channel().config().isAutoRead() + ":" + isHandlerActive(var1));
            }

            this.submitWrite(var1, var2, var6, var3);
            return;
         }
      }

      this.submitWrite(var1, var2, 0L, var3);
   }

   protected abstract void submitWrite(ChannelHandlerContext var1, Object var2, long var3, ChannelPromise var5);

   public TrafficCounter trafficCounter() {
      return this.trafficCounter;
   }

   public String toString() {
      return "TrafficShaping with Write Limit: " + this.writeLimit + " Read Limit: " + this.readLimit + " and Counter: " + (this.trafficCounter != null?this.trafficCounter.toString():"none");
   }

   protected long calculateSize(Object var1) {
      return var1 instanceof ByteBuf?(long)((ByteBuf)var1).readableBytes():(var1 instanceof ByteBufHolder?(long)((ByteBufHolder)var1).content().readableBytes():-1L);
   }

   private static final class ReopenReadTimerTask implements Runnable {
      final ChannelHandlerContext ctx;

      ReopenReadTimerTask(ChannelHandlerContext var1) {
         this.ctx = var1;
      }

      public void run() {
         if(!this.ctx.channel().config().isAutoRead() && AbstractTrafficShapingHandler.isHandlerActive(this.ctx)) {
            if(AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
               AbstractTrafficShapingHandler.logger.debug("Channel:" + this.ctx.channel().hashCode() + " Not Unsuspend: " + this.ctx.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
            }

            this.ctx.attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(Boolean.valueOf(false));
         } else {
            if(AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
               if(this.ctx.channel().config().isAutoRead() && !AbstractTrafficShapingHandler.isHandlerActive(this.ctx)) {
                  AbstractTrafficShapingHandler.logger.debug("Channel:" + this.ctx.channel().hashCode() + " Unsuspend: " + this.ctx.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
               } else {
                  AbstractTrafficShapingHandler.logger.debug("Channel:" + this.ctx.channel().hashCode() + " Normal Unsuspend: " + this.ctx.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
               }
            }

            this.ctx.attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(Boolean.valueOf(false));
            this.ctx.channel().config().setAutoRead(true);
            this.ctx.channel().read();
         }

         if(AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
            AbstractTrafficShapingHandler.logger.debug("Channel:" + this.ctx.channel().hashCode() + " Unsupsend final status => " + this.ctx.channel().config().isAutoRead() + ":" + AbstractTrafficShapingHandler.isHandlerActive(this.ctx));
         }

      }
   }
}
