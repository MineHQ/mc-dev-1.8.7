package io.netty.util.concurrent;

import io.netty.util.concurrent.CompleteFuture;
import io.netty.util.concurrent.EventExecutor;

public final class SucceededFuture<V> extends CompleteFuture<V> {
   private final V result;

   public SucceededFuture(EventExecutor var1, V var2) {
      super(var1);
      this.result = var2;
   }

   public Throwable cause() {
      return null;
   }

   public boolean isSuccess() {
      return true;
   }

   public V getNow() {
      return this.result;
   }
}
