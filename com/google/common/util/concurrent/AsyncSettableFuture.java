package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ForwardingListenableFuture;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.Future;
import javax.annotation.Nullable;

final class AsyncSettableFuture<V> extends ForwardingListenableFuture<V> {
   private final AsyncSettableFuture.NestedFuture<V> nested = new AsyncSettableFuture.NestedFuture();
   private final ListenableFuture<V> dereferenced;

   public static <V> AsyncSettableFuture<V> create() {
      return new AsyncSettableFuture();
   }

   private AsyncSettableFuture() {
      this.dereferenced = Futures.dereference(this.nested);
   }

   protected ListenableFuture<V> delegate() {
      return this.dereferenced;
   }

   public boolean setFuture(ListenableFuture<? extends V> var1) {
      return this.nested.setFuture((ListenableFuture)Preconditions.checkNotNull(var1));
   }

   public boolean setValue(@Nullable V var1) {
      return this.setFuture(Futures.immediateFuture(var1));
   }

   public boolean setException(Throwable var1) {
      return this.setFuture(Futures.immediateFailedFuture(var1));
   }

   public boolean isSet() {
      return this.nested.isDone();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Future delegate() {
      return this.delegate();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Object delegate() {
      return this.delegate();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static final class NestedFuture<V> extends AbstractFuture<ListenableFuture<? extends V>> {
      private NestedFuture() {
      }

      boolean setFuture(ListenableFuture<? extends V> var1) {
         boolean var2 = this.set(var1);
         if(this.isCancelled()) {
            var1.cancel(this.wasInterrupted());
         }

         return var2;
      }

      // $FF: synthetic method
      NestedFuture(AsyncSettableFuture.SyntheticClass_1 var1) {
         this();
      }
   }
}
