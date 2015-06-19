package io.netty.util.concurrent;

import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ProgressivePromise;
import io.netty.util.concurrent.Promise;

public interface EventExecutor extends EventExecutorGroup {
   EventExecutor next();

   EventExecutorGroup parent();

   boolean inEventLoop();

   boolean inEventLoop(Thread var1);

   <V> Promise<V> newPromise();

   <V> ProgressivePromise<V> newProgressivePromise();

   <V> Future<V> newSucceededFuture(V var1);

   <V> Future<V> newFailedFuture(Throwable var1);
}
