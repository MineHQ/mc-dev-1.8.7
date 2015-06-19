package io.netty.util.concurrent;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ProgressiveFuture;
import io.netty.util.concurrent.Promise;

public interface ProgressivePromise<V> extends Promise<V>, ProgressiveFuture<V> {
   ProgressivePromise<V> setProgress(long var1, long var3);

   boolean tryProgress(long var1, long var3);

   ProgressivePromise<V> setSuccess(V var1);

   ProgressivePromise<V> setFailure(Throwable var1);

   ProgressivePromise<V> addListener(GenericFutureListener<? extends Future<? super V>> var1);

   ProgressivePromise<V> addListeners(GenericFutureListener... var1);

   ProgressivePromise<V> removeListener(GenericFutureListener<? extends Future<? super V>> var1);

   ProgressivePromise<V> removeListeners(GenericFutureListener... var1);

   ProgressivePromise<V> await() throws InterruptedException;

   ProgressivePromise<V> awaitUninterruptibly();

   ProgressivePromise<V> sync() throws InterruptedException;

   ProgressivePromise<V> syncUninterruptibly();
}
