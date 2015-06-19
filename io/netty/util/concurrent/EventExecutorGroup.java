package io.netty.util.concurrent;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public interface EventExecutorGroup extends ScheduledExecutorService, Iterable<EventExecutor> {
   boolean isShuttingDown();

   Future<?> shutdownGracefully();

   Future<?> shutdownGracefully(long var1, long var3, TimeUnit var5);

   Future<?> terminationFuture();

   /** @deprecated */
   @Deprecated
   void shutdown();

   /** @deprecated */
   @Deprecated
   List<Runnable> shutdownNow();

   EventExecutor next();

   Iterator<EventExecutor> iterator();

   Future<?> submit(Runnable var1);

   <T> Future<T> submit(Runnable var1, T var2);

   <T> Future<T> submit(Callable<T> var1);

   ScheduledFuture<?> schedule(Runnable var1, long var2, TimeUnit var4);

   <V> ScheduledFuture<V> schedule(Callable<V> var1, long var2, TimeUnit var4);

   ScheduledFuture<?> scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6);

   ScheduledFuture<?> scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6);
}
