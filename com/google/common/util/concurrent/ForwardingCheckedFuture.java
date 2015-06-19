package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.ForwardingListenableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public abstract class ForwardingCheckedFuture<V, X extends Exception> extends ForwardingListenableFuture<V> implements CheckedFuture<V, X> {
   public ForwardingCheckedFuture() {
   }

   public V checkedGet() throws X {
      return this.delegate().checkedGet();
   }

   public V checkedGet(long var1, TimeUnit var3) throws TimeoutException, X {
      return this.delegate().checkedGet(var1, var3);
   }

   protected abstract CheckedFuture<V, X> delegate();

   // $FF: synthetic method
   // $FF: bridge method
   protected ListenableFuture delegate() {
      return this.delegate();
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

   @Beta
   public abstract static class SimpleForwardingCheckedFuture<V, X extends Exception> extends ForwardingCheckedFuture<V, X> {
      private final CheckedFuture<V, X> delegate;

      protected SimpleForwardingCheckedFuture(CheckedFuture<V, X> var1) {
         this.delegate = (CheckedFuture)Preconditions.checkNotNull(var1);
      }

      protected final CheckedFuture<V, X> delegate() {
         return this.delegate;
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected ListenableFuture delegate() {
         return this.delegate();
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
