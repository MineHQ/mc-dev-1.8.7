package com.google.common.util.concurrent;

import com.google.common.util.concurrent.AbstractFuture;
import javax.annotation.Nullable;

public final class SettableFuture<V> extends AbstractFuture<V> {
   public static <V> SettableFuture<V> create() {
      return new SettableFuture();
   }

   private SettableFuture() {
   }

   public boolean set(@Nullable V var1) {
      return super.set(var1);
   }

   public boolean setException(Throwable var1) {
      return super.setException(var1);
   }
}
