package io.netty.util.concurrent;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ProgressiveFuture;
import io.netty.util.concurrent.ProgressivePromise;
import io.netty.util.concurrent.Promise;

public class DefaultProgressivePromise<V> extends DefaultPromise<V> implements ProgressivePromise<V> {
   public DefaultProgressivePromise(EventExecutor var1) {
      super(var1);
   }

   protected DefaultProgressivePromise() {
   }

   public ProgressivePromise<V> setProgress(long var1, long var3) {
      if(var3 < 0L) {
         var3 = -1L;
         if(var1 < 0L) {
            throw new IllegalArgumentException("progress: " + var1 + " (expected: >= 0)");
         }
      } else if(var1 < 0L || var1 > var3) {
         throw new IllegalArgumentException("progress: " + var1 + " (expected: 0 <= progress <= total (" + var3 + "))");
      }

      if(this.isDone()) {
         throw new IllegalStateException("complete already");
      } else {
         this.notifyProgressiveListeners(var1, var3);
         return this;
      }
   }

   public boolean tryProgress(long var1, long var3) {
      if(var3 < 0L) {
         var3 = -1L;
         if(var1 < 0L || this.isDone()) {
            return false;
         }
      } else if(var1 < 0L || var1 > var3 || this.isDone()) {
         return false;
      }

      this.notifyProgressiveListeners(var1, var3);
      return true;
   }

   public ProgressivePromise<V> addListener(GenericFutureListener<? extends Future<? super V>> var1) {
      super.addListener(var1);
      return this;
   }

   public ProgressivePromise<V> addListeners(GenericFutureListener... var1) {
      super.addListeners(var1);
      return this;
   }

   public ProgressivePromise<V> removeListener(GenericFutureListener<? extends Future<? super V>> var1) {
      super.removeListener(var1);
      return this;
   }

   public ProgressivePromise<V> removeListeners(GenericFutureListener... var1) {
      super.removeListeners(var1);
      return this;
   }

   public ProgressivePromise<V> sync() throws InterruptedException {
      super.sync();
      return this;
   }

   public ProgressivePromise<V> syncUninterruptibly() {
      super.syncUninterruptibly();
      return this;
   }

   public ProgressivePromise<V> await() throws InterruptedException {
      super.await();
      return this;
   }

   public ProgressivePromise<V> awaitUninterruptibly() {
      super.awaitUninterruptibly();
      return this;
   }

   public ProgressivePromise<V> setSuccess(V var1) {
      super.setSuccess(var1);
      return this;
   }

   public ProgressivePromise<V> setFailure(Throwable var1) {
      super.setFailure(var1);
      return this;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise setFailure(Throwable var1) {
      return this.setFailure(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise setSuccess(Object var1) {
      return this.setSuccess(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise await() throws InterruptedException {
      return this.await();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise sync() throws InterruptedException {
      return this.sync();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future await() throws InterruptedException {
      return this.await();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future sync() throws InterruptedException {
      return this.sync();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture await() throws InterruptedException {
      return this.await();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture sync() throws InterruptedException {
      return this.sync();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }
}
