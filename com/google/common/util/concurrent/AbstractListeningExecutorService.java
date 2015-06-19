package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListeningExecutorService;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractListeningExecutorService extends AbstractExecutorService implements ListeningExecutorService {
   public AbstractListeningExecutorService() {
   }

   protected final <T> ListenableFutureTask<T> newTaskFor(Runnable var1, T var2) {
      return ListenableFutureTask.create(var1, var2);
   }

   protected final <T> ListenableFutureTask<T> newTaskFor(Callable<T> var1) {
      return ListenableFutureTask.create(var1);
   }

   public ListenableFuture<?> submit(Runnable var1) {
      return (ListenableFuture)super.submit(var1);
   }

   public <T> ListenableFuture<T> submit(Runnable var1, @Nullable T var2) {
      return (ListenableFuture)super.submit(var1, var2);
   }

   public <T> ListenableFuture<T> submit(Callable<T> var1) {
      return (ListenableFuture)super.submit(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future submit(Callable var1) {
      return this.submit(var1);
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
   protected RunnableFuture newTaskFor(Callable var1) {
      return this.newTaskFor(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected RunnableFuture newTaskFor(Runnable var1, Object var2) {
      return this.newTaskFor(var1, var2);
   }
}
