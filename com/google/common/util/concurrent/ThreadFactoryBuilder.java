package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadFactoryBuilder {
   private String nameFormat = null;
   private Boolean daemon = null;
   private Integer priority = null;
   private UncaughtExceptionHandler uncaughtExceptionHandler = null;
   private ThreadFactory backingThreadFactory = null;

   public ThreadFactoryBuilder() {
   }

   public ThreadFactoryBuilder setNameFormat(String var1) {
      String.format(var1, new Object[]{Integer.valueOf(0)});
      this.nameFormat = var1;
      return this;
   }

   public ThreadFactoryBuilder setDaemon(boolean var1) {
      this.daemon = Boolean.valueOf(var1);
      return this;
   }

   public ThreadFactoryBuilder setPriority(int var1) {
      Preconditions.checkArgument(var1 >= 1, "Thread priority (%s) must be >= %s", new Object[]{Integer.valueOf(var1), Integer.valueOf(1)});
      Preconditions.checkArgument(var1 <= 10, "Thread priority (%s) must be <= %s", new Object[]{Integer.valueOf(var1), Integer.valueOf(10)});
      this.priority = Integer.valueOf(var1);
      return this;
   }

   public ThreadFactoryBuilder setUncaughtExceptionHandler(UncaughtExceptionHandler var1) {
      this.uncaughtExceptionHandler = (UncaughtExceptionHandler)Preconditions.checkNotNull(var1);
      return this;
   }

   public ThreadFactoryBuilder setThreadFactory(ThreadFactory var1) {
      this.backingThreadFactory = (ThreadFactory)Preconditions.checkNotNull(var1);
      return this;
   }

   public ThreadFactory build() {
      return build(this);
   }

   private static ThreadFactory build(ThreadFactoryBuilder var0) {
      final String var1 = var0.nameFormat;
      final Boolean var2 = var0.daemon;
      final Integer var3 = var0.priority;
      final UncaughtExceptionHandler var4 = var0.uncaughtExceptionHandler;
      final ThreadFactory var5 = var0.backingThreadFactory != null?var0.backingThreadFactory:Executors.defaultThreadFactory();
      final AtomicLong var6 = var1 != null?new AtomicLong(0L):null;
      return new ThreadFactory() {
         public Thread newThread(Runnable var1x) {
            Thread var2x = var5.newThread(var1x);
            if(var1 != null) {
               var2x.setName(String.format(var1, new Object[]{Long.valueOf(var6.getAndIncrement())}));
            }

            if(var2 != null) {
               var2x.setDaemon(var2.booleanValue());
            }

            if(var3 != null) {
               var2x.setPriority(var3.intValue());
            }

            if(var4 != null) {
               var2x.setUncaughtExceptionHandler(var4);
            }

            return var2x;
         }
      };
   }
}
