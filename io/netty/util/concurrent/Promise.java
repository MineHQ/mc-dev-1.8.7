package io.netty.util.concurrent;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public interface Promise<V> extends Future<V> {
   Promise<V> setSuccess(V var1);

   boolean trySuccess(V var1);

   Promise<V> setFailure(Throwable var1);

   boolean tryFailure(Throwable var1);

   boolean setUncancellable();

   Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> var1);

   Promise<V> addListeners(GenericFutureListener... var1);

   Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> var1);

   Promise<V> removeListeners(GenericFutureListener... var1);

   Promise<V> await() throws InterruptedException;

   Promise<V> awaitUninterruptibly();

   Promise<V> sync() throws InterruptedException;

   Promise<V> syncUninterruptibly();
}
