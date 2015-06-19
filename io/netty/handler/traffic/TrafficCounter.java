package io.netty.handler.traffic;

import io.netty.handler.traffic.AbstractTrafficShapingHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TrafficCounter {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(TrafficCounter.class);
   private final AtomicLong currentWrittenBytes = new AtomicLong();
   private final AtomicLong currentReadBytes = new AtomicLong();
   private final AtomicLong cumulativeWrittenBytes = new AtomicLong();
   private final AtomicLong cumulativeReadBytes = new AtomicLong();
   private long lastCumulativeTime;
   private long lastWriteThroughput;
   private long lastReadThroughput;
   private final AtomicLong lastTime = new AtomicLong();
   private long lastWrittenBytes;
   private long lastReadBytes;
   private long lastNonNullWrittenBytes;
   private long lastNonNullWrittenTime;
   private long lastNonNullReadTime;
   private long lastNonNullReadBytes;
   final AtomicLong checkInterval = new AtomicLong(1000L);
   final String name;
   private final AbstractTrafficShapingHandler trafficShapingHandler;
   private final ScheduledExecutorService executor;
   private Runnable monitor;
   private volatile ScheduledFuture<?> scheduledFuture;
   final AtomicBoolean monitorActive = new AtomicBoolean();

   public synchronized void start() {
      if(!this.monitorActive.get()) {
         this.lastTime.set(System.currentTimeMillis());
         if(this.checkInterval.get() > 0L) {
            this.monitorActive.set(true);
            this.monitor = new TrafficCounter.TrafficMonitoringTask(this.trafficShapingHandler, this);
            this.scheduledFuture = this.executor.schedule(this.monitor, this.checkInterval.get(), TimeUnit.MILLISECONDS);
         }

      }
   }

   public synchronized void stop() {
      if(this.monitorActive.get()) {
         this.monitorActive.set(false);
         this.resetAccounting(System.currentTimeMillis());
         if(this.trafficShapingHandler != null) {
            this.trafficShapingHandler.doAccounting(this);
         }

         if(this.scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
         }

      }
   }

   synchronized void resetAccounting(long var1) {
      long var3 = var1 - this.lastTime.getAndSet(var1);
      if(var3 != 0L) {
         if(logger.isDebugEnabled() && var3 > 2L * this.checkInterval()) {
            logger.debug("Acct schedule not ok: " + var3 + " > 2*" + this.checkInterval() + " from " + this.name);
         }

         this.lastReadBytes = this.currentReadBytes.getAndSet(0L);
         this.lastWrittenBytes = this.currentWrittenBytes.getAndSet(0L);
         this.lastReadThroughput = this.lastReadBytes / var3 * 1000L;
         this.lastWriteThroughput = this.lastWrittenBytes / var3 * 1000L;
         if(this.lastWrittenBytes > 0L) {
            this.lastNonNullWrittenBytes = this.lastWrittenBytes;
            this.lastNonNullWrittenTime = var1;
         }

         if(this.lastReadBytes > 0L) {
            this.lastNonNullReadBytes = this.lastReadBytes;
            this.lastNonNullReadTime = var1;
         }

      }
   }

   public TrafficCounter(AbstractTrafficShapingHandler var1, ScheduledExecutorService var2, String var3, long var4) {
      this.trafficShapingHandler = var1;
      this.executor = var2;
      this.name = var3;
      this.lastCumulativeTime = System.currentTimeMillis();
      this.configure(var4);
   }

   public void configure(long var1) {
      long var3 = var1 / 10L * 10L;
      if(this.checkInterval.get() != var3) {
         this.checkInterval.set(var3);
         if(var3 <= 0L) {
            this.stop();
            this.lastTime.set(System.currentTimeMillis());
         } else {
            this.start();
         }
      }

   }

   void bytesRecvFlowControl(long var1) {
      this.currentReadBytes.addAndGet(var1);
      this.cumulativeReadBytes.addAndGet(var1);
   }

   void bytesWriteFlowControl(long var1) {
      this.currentWrittenBytes.addAndGet(var1);
      this.cumulativeWrittenBytes.addAndGet(var1);
   }

   public long checkInterval() {
      return this.checkInterval.get();
   }

   public long lastReadThroughput() {
      return this.lastReadThroughput;
   }

   public long lastWriteThroughput() {
      return this.lastWriteThroughput;
   }

   public long lastReadBytes() {
      return this.lastReadBytes;
   }

   public long lastWrittenBytes() {
      return this.lastWrittenBytes;
   }

   public long currentReadBytes() {
      return this.currentReadBytes.get();
   }

   public long currentWrittenBytes() {
      return this.currentWrittenBytes.get();
   }

   public long lastTime() {
      return this.lastTime.get();
   }

   public long cumulativeWrittenBytes() {
      return this.cumulativeWrittenBytes.get();
   }

   public long cumulativeReadBytes() {
      return this.cumulativeReadBytes.get();
   }

   public long lastCumulativeTime() {
      return this.lastCumulativeTime;
   }

   public void resetCumulativeTime() {
      this.lastCumulativeTime = System.currentTimeMillis();
      this.cumulativeReadBytes.set(0L);
      this.cumulativeWrittenBytes.set(0L);
   }

   public String name() {
      return this.name;
   }

   public synchronized long readTimeToWait(long var1, long var3, long var5) {
      long var7 = System.currentTimeMillis();
      this.bytesRecvFlowControl(var1);
      if(var3 == 0L) {
         return 0L;
      } else {
         long var9 = this.currentReadBytes.get();
         long var11 = var7 - this.lastTime.get();
         long var13;
         if(var11 > 10L && var9 > 0L) {
            var13 = (var9 * 1000L / var3 - var11) / 10L * 10L;
            if(var13 > 10L) {
               if(logger.isDebugEnabled()) {
                  logger.debug("Time: " + var13 + ":" + var9 + ":" + var11);
               }

               return var13 > var5?var5:var13;
            } else {
               return 0L;
            }
         } else {
            long var15;
            if(this.lastNonNullReadBytes > 0L && this.lastNonNullReadTime + 10L < var7) {
               var13 = var9 + this.lastNonNullReadBytes;
               var15 = var7 - this.lastNonNullReadTime;
               long var17 = (var13 * 1000L / var3 - var15) / 10L * 10L;
               if(var17 > 10L) {
                  if(logger.isDebugEnabled()) {
                     logger.debug("Time: " + var17 + ":" + var13 + ":" + var15);
                  }

                  return var17 > var5?var5:var17;
               }
            } else {
               var9 += this.lastReadBytes;
               var13 = 10L;
               var15 = (var9 * 1000L / var3 - var13) / 10L * 10L;
               if(var15 > 10L) {
                  if(logger.isDebugEnabled()) {
                     logger.debug("Time: " + var15 + ":" + var9 + ":" + var13);
                  }

                  return var15 > var5?var5:var15;
               }
            }

            return 0L;
         }
      }
   }

   public synchronized long writeTimeToWait(long var1, long var3, long var5) {
      this.bytesWriteFlowControl(var1);
      if(var3 == 0L) {
         return 0L;
      } else {
         long var7 = this.currentWrittenBytes.get();
         long var9 = System.currentTimeMillis();
         long var11 = var9 - this.lastTime.get();
         long var13;
         if(var11 > 10L && var7 > 0L) {
            var13 = (var7 * 1000L / var3 - var11) / 10L * 10L;
            if(var13 > 10L) {
               if(logger.isDebugEnabled()) {
                  logger.debug("Time: " + var13 + ":" + var7 + ":" + var11);
               }

               return var13 > var5?var5:var13;
            } else {
               return 0L;
            }
         } else {
            long var15;
            if(this.lastNonNullWrittenBytes > 0L && this.lastNonNullWrittenTime + 10L < var9) {
               var13 = var7 + this.lastNonNullWrittenBytes;
               var15 = var9 - this.lastNonNullWrittenTime;
               long var17 = (var13 * 1000L / var3 - var15) / 10L * 10L;
               if(var17 > 10L) {
                  if(logger.isDebugEnabled()) {
                     logger.debug("Time: " + var17 + ":" + var13 + ":" + var15);
                  }

                  return var17 > var5?var5:var17;
               }
            } else {
               var7 += this.lastWrittenBytes;
               var13 = 10L + Math.abs(var11);
               var15 = (var7 * 1000L / var3 - var13) / 10L * 10L;
               if(var15 > 10L) {
                  if(logger.isDebugEnabled()) {
                     logger.debug("Time: " + var15 + ":" + var7 + ":" + var13);
                  }

                  return var15 > var5?var5:var15;
               }
            }

            return 0L;
         }
      }
   }

   public String toString() {
      return "Monitor " + this.name + " Current Speed Read: " + (this.lastReadThroughput >> 10) + " KB/s, Write: " + (this.lastWriteThroughput >> 10) + " KB/s Current Read: " + (this.currentReadBytes.get() >> 10) + " KB Current Write: " + (this.currentWrittenBytes.get() >> 10) + " KB";
   }

   private static class TrafficMonitoringTask implements Runnable {
      private final AbstractTrafficShapingHandler trafficShapingHandler1;
      private final TrafficCounter counter;

      protected TrafficMonitoringTask(AbstractTrafficShapingHandler var1, TrafficCounter var2) {
         this.trafficShapingHandler1 = var1;
         this.counter = var2;
      }

      public void run() {
         if(this.counter.monitorActive.get()) {
            long var1 = System.currentTimeMillis();
            this.counter.resetAccounting(var1);
            if(this.trafficShapingHandler1 != null) {
               this.trafficShapingHandler1.doAccounting(this.counter);
            }

            this.counter.scheduledFuture = this.counter.executor.schedule(this, this.counter.checkInterval.get(), TimeUnit.MILLISECONDS);
         }
      }
   }
}
