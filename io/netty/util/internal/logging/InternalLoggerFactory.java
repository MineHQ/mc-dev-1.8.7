package io.netty.util.internal.logging;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.JdkLoggerFactory;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

public abstract class InternalLoggerFactory {
   private static volatile InternalLoggerFactory defaultFactory = newDefaultFactory(InternalLoggerFactory.class.getName());

   public InternalLoggerFactory() {
   }

   private static InternalLoggerFactory newDefaultFactory(String var0) {
      Object var1;
      try {
         var1 = new Slf4JLoggerFactory(true);
         ((InternalLoggerFactory)var1).newInstance(var0).debug("Using SLF4J as the default logging framework");
      } catch (Throwable var5) {
         try {
            var1 = new Log4JLoggerFactory();
            ((InternalLoggerFactory)var1).newInstance(var0).debug("Using Log4J as the default logging framework");
         } catch (Throwable var4) {
            var1 = new JdkLoggerFactory();
            ((InternalLoggerFactory)var1).newInstance(var0).debug("Using java.util.logging as the default logging framework");
         }
      }

      return (InternalLoggerFactory)var1;
   }

   public static InternalLoggerFactory getDefaultFactory() {
      return defaultFactory;
   }

   public static void setDefaultFactory(InternalLoggerFactory var0) {
      if(var0 == null) {
         throw new NullPointerException("defaultFactory");
      } else {
         defaultFactory = var0;
      }
   }

   public static InternalLogger getInstance(Class<?> var0) {
      return getInstance(var0.getName());
   }

   public static InternalLogger getInstance(String var0) {
      return getDefaultFactory().newInstance(var0);
   }

   protected abstract InternalLogger newInstance(String var1);
}
