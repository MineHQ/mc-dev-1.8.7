package io.netty.util.concurrent;

import io.netty.util.concurrent.CompleteFuture;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.PlatformDependent;

public final class FailedFuture<V> extends CompleteFuture<V> {
   private final Throwable cause;

   public FailedFuture(EventExecutor var1, Throwable var2) {
      super(var1);
      if(var2 == null) {
         throw new NullPointerException("cause");
      } else {
         this.cause = var2;
      }
   }

   public Throwable cause() {
      return this.cause;
   }

   public boolean isSuccess() {
      return false;
   }

   public Future<V> sync() {
      PlatformDependent.throwException(this.cause);
      return this;
   }

   public Future<V> syncUninterruptibly() {
      PlatformDependent.throwException(this.cause);
      return this;
   }

   public V getNow() {
      return null;
   }
}
