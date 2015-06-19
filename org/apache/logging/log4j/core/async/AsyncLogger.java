package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.async.DaemonThreadFactory;
import org.apache.logging.log4j.core.async.RingBufferLogEvent;
import org.apache.logging.log4j.core.async.RingBufferLogEventHandler;
import org.apache.logging.log4j.core.async.RingBufferLogEventTranslator;
import org.apache.logging.log4j.core.helpers.Clock;
import org.apache.logging.log4j.core.helpers.ClockFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.status.StatusLogger;

public class AsyncLogger extends Logger {
   private static final int HALF_A_SECOND = 500;
   private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 20;
   private static final int RINGBUFFER_MIN_SIZE = 128;
   private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
   private static final StatusLogger LOGGER = StatusLogger.getLogger();
   private static volatile Disruptor<RingBufferLogEvent> disruptor;
   private static Clock clock = ClockFactory.getClock();
   private static ExecutorService executor = Executors.newSingleThreadExecutor(new DaemonThreadFactory("AsyncLogger-"));
   private final ThreadLocal<AsyncLogger.Info> threadlocalInfo = new ThreadLocal();

   private static int calculateRingBufferSize() {
      int var0 = 262144;
      String var1 = System.getProperty("AsyncLogger.RingBufferSize", String.valueOf(var0));

      try {
         int var2 = Integer.parseInt(var1);
         if(var2 < 128) {
            var2 = 128;
            LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", new Object[]{var1, Integer.valueOf(128)});
         }

         var0 = var2;
      } catch (Exception var3) {
         LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", new Object[]{var1, Integer.valueOf(var0)});
      }

      return Util.ceilingNextPowerOfTwo(var0);
   }

   private static WaitStrategy createWaitStrategy() {
      String var0 = System.getProperty("AsyncLogger.WaitStrategy");
      LOGGER.debug("property AsyncLogger.WaitStrategy={}", new Object[]{var0});
      if("Sleep".equals(var0)) {
         LOGGER.debug("disruptor event handler uses SleepingWaitStrategy");
         return new SleepingWaitStrategy();
      } else if("Yield".equals(var0)) {
         LOGGER.debug("disruptor event handler uses YieldingWaitStrategy");
         return new YieldingWaitStrategy();
      } else if("Block".equals(var0)) {
         LOGGER.debug("disruptor event handler uses BlockingWaitStrategy");
         return new BlockingWaitStrategy();
      } else {
         LOGGER.debug("disruptor event handler uses SleepingWaitStrategy");
         return new SleepingWaitStrategy();
      }
   }

   private static ExceptionHandler getExceptionHandler() {
      String var0 = System.getProperty("AsyncLogger.ExceptionHandler");
      if(var0 == null) {
         LOGGER.debug("No AsyncLogger.ExceptionHandler specified");
         return null;
      } else {
         try {
            Class var1 = Class.forName(var0);
            ExceptionHandler var2 = (ExceptionHandler)var1.newInstance();
            LOGGER.debug("AsyncLogger.ExceptionHandler=" + var2);
            return var2;
         } catch (Exception var3) {
            LOGGER.debug("AsyncLogger.ExceptionHandler not set: error creating " + var0 + ": ", var3);
            return null;
         }
      }
   }

   public AsyncLogger(LoggerContext var1, String var2, MessageFactory var3) {
      super(var1, var2, var3);
   }

   public void log(Marker var1, String var2, Level var3, Message var4, Throwable var5) {
      AsyncLogger.Info var6 = (AsyncLogger.Info)this.threadlocalInfo.get();
      if(var6 == null) {
         var6 = new AsyncLogger.Info();
         var6.translator = new RingBufferLogEventTranslator();
         var6.cachedThreadName = Thread.currentThread().getName();
         this.threadlocalInfo.set(var6);
      }

      boolean var7 = this.config.loggerConfig.isIncludeLocation();
      var6.translator.setValues(this, this.getName(), var1, var2, var3, var4, var5, ThreadContext.getImmutableContext(), ThreadContext.getImmutableStack(), var6.cachedThreadName, var7?this.location(var2):null, clock.currentTimeMillis());
      disruptor.publishEvent(var6.translator);
   }

   private StackTraceElement location(String var1) {
      return Log4jLogEvent.calcLocation(var1);
   }

   public void actualAsyncLog(RingBufferLogEvent var1) {
      Map var2 = this.config.loggerConfig.getProperties();
      var1.mergePropertiesIntoContextMap(var2, this.config.config.getStrSubstitutor());
      this.config.logEvent(var1);
   }

   public static void stop() {
      Disruptor var0 = disruptor;
      disruptor = null;
      var0.shutdown();
      RingBuffer var1 = var0.getRingBuffer();

      for(int var2 = 0; var2 < 20 && !var1.hasAvailableCapacity(var1.getBufferSize()); ++var2) {
         try {
            Thread.sleep(500L);
         } catch (InterruptedException var4) {
            ;
         }
      }

      executor.shutdown();
   }

   static {
      int var0 = calculateRingBufferSize();
      WaitStrategy var1 = createWaitStrategy();
      disruptor = new Disruptor(RingBufferLogEvent.FACTORY, var0, executor, ProducerType.MULTI, var1);
      RingBufferLogEventHandler[] var2 = new RingBufferLogEventHandler[]{new RingBufferLogEventHandler()};
      disruptor.handleExceptionsWith(getExceptionHandler());
      disruptor.handleEventsWith(var2);
      LOGGER.debug("Starting AsyncLogger disruptor with ringbuffer size {}...", new Object[]{Integer.valueOf(disruptor.getRingBuffer().getBufferSize())});
      disruptor.start();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class Info {
      private RingBufferLogEventTranslator translator;
      private String cachedThreadName;

      private Info() {
      }

      // $FF: synthetic method
      Info(AsyncLogger.SyntheticClass_1 var1) {
         this();
      }
   }
}
