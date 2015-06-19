package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ForwardingFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract class ForwardingListenableFuture<V> extends ForwardingFuture<V> implements ListenableFuture<V> {
   protected ForwardingListenableFuture() {
   }

   protected abstract ListenableFuture<V> delegate();

   public void addListener(Runnable var1, Executor var2) {
      this.delegate().addListener(var1, var2);
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

   public abstract static class SimpleForwardingListenableFuture<V> extends ForwardingListenableFuture<V> {
      private final ListenableFuture<V> delegate;

      protected SimpleForwardingListenableFuture(ListenableFuture<V> var1) {
         this.delegate = (ListenableFuture)Preconditions.checkNotNull(var1);
      }

      protected final ListenableFuture<V> delegate() {
         return this.delegate;
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
   }
}
