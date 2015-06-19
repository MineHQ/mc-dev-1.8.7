package com.google.common.util.concurrent;

import com.google.common.util.concurrent.ForwardingExecutorService;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class ForwardingListeningExecutorService extends ForwardingExecutorService implements ListeningExecutorService {
   protected ForwardingListeningExecutorService() {
   }

   protected abstract ListeningExecutorService delegate();

   public <T> ListenableFuture<T> submit(Callable<T> var1) {
      return this.delegate().submit(var1);
   }

   public ListenableFuture<?> submit(Runnable var1) {
      return this.delegate().submit(var1);
   }

   public <T> ListenableFuture<T> submit(Runnable var1, T var2) {
      return this.delegate().submit(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future submit(Runnable var1, Object var2) {
      return this.submit(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future submit(Runnable var1) {
      return this.submit(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future submit(Callable var1) {
      return this.submit(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected ExecutorService delegate() {
      return this.delegate();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Object delegate() {
      return this.delegate();
   }
}
