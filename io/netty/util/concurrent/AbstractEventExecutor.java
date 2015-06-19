package io.netty.util.concurrent;

import io.netty.util.concurrent.DefaultProgressivePromise;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.FailedFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ProgressivePromise;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.PromiseTask;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.concurrent.SucceededFuture;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractEventExecutor extends AbstractExecutorService implements EventExecutor {
   public AbstractEventExecutor() {
   }

   public EventExecutor next() {
      return this;
   }

   public boolean inEventLoop() {
      return this.inEventLoop(Thread.currentThread());
   }

   public Iterator<EventExecutor> iterator() {
      return new AbstractEventExecutor.EventExecutorIterator();
   }

   public Future<?> shutdownGracefully() {
      return this.shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
   }

   /** @deprecated */
   @Deprecated
   public abstract void shutdown();

   /** @deprecated */
   @Deprecated
   public List<Runnable> shutdownNow() {
      this.shutdown();
      return Collections.emptyList();
   }

   public <V> Promise<V> newPromise() {
      return new DefaultPromise(this);
   }

   public <V> ProgressivePromise<V> newProgressivePromise() {
      return new DefaultProgressivePromise(this);
   }

   public <V> Future<V> newSucceededFuture(V var1) {
      return new SucceededFuture(this, var1);
   }

   public <V> Future<V> newFailedFuture(Throwable var1) {
      return new FailedFuture(this, var1);
   }

   public Future<?> submit(Runnable var1) {
      return (Future)super.submit(var1);
   }

   public <T> Future<T> submit(Runnable var1, T var2) {
      return (Future)super.submit(var1, var2);
   }

   public <T> Future<T> submit(Callable<T> var1) {
      return (Future)super.submit(var1);
   }

   protected final <T> RunnableFuture<T> newTaskFor(Runnable var1, T var2) {
      return new PromiseTask(this, var1, var2);
   }

   protected final <T> RunnableFuture<T> newTaskFor(Callable<T> var1) {
      return new PromiseTask(this, var1);
   }

   public ScheduledFuture<?> schedule(Runnable var1, long var2, TimeUnit var4) {
      throw new UnsupportedOperationException();
   }

   public <V> ScheduledFuture<V> schedule(Callable<V> var1, long var2, TimeUnit var4) {
      throw new UnsupportedOperationException();
   }

   public ScheduledFuture<?> scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6) {
      throw new UnsupportedOperationException();
   }

   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
      throw new UnsupportedOperationException();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public java.util.concurrent.Future submit(Callable var1) {
      return this.submit(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public java.util.concurrent.Future submit(Runnable var1, Object var2) {
      return this.submit(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public java.util.concurrent.Future submit(Runnable var1) {
      return this.submit(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public java.util.concurrent.ScheduledFuture scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.scheduleWithFixedDelay(var1, var2, var4, var6);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public java.util.concurrent.ScheduledFuture scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.scheduleAtFixedRate(var1, var2, var4, var6);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public java.util.concurrent.ScheduledFuture schedule(Callable var1, long var2, TimeUnit var4) {
      return this.schedule(var1, var2, var4);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public java.util.concurrent.ScheduledFuture schedule(Runnable var1, long var2, TimeUnit var4) {
      return this.schedule(var1, var2, var4);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private final class EventExecutorIterator implements Iterator<EventExecutor> {
      private boolean nextCalled;

      private EventExecutorIterator() {
      }

      public boolean hasNext() {
         return !this.nextCalled;
      }

      public EventExecutor next() {
         if(!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            this.nextCalled = true;
            return AbstractEventExecutor.this;
         }
      }

      public void remove() {
         throw new UnsupportedOperationException("read-only");
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object next() {
         return this.next();
      }

      // $FF: synthetic method
      EventExecutorIterator(AbstractEventExecutor.SyntheticClass_1 var2) {
         this();
      }
   }
}
