package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ExecutionList;
import com.google.common.util.concurrent.ForwardingFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.common.util.concurrent.Uninterruptibles;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

@Beta
public final class JdkFutureAdapters {
   public static <V> ListenableFuture<V> listenInPoolThread(Future<V> var0) {
      return (ListenableFuture)(var0 instanceof ListenableFuture?(ListenableFuture)var0:new JdkFutureAdapters.ListenableFutureAdapter(var0));
   }

   public static <V> ListenableFuture<V> listenInPoolThread(Future<V> var0, Executor var1) {
      Preconditions.checkNotNull(var1);
      return (ListenableFuture)(var0 instanceof ListenableFuture?(ListenableFuture)var0:new JdkFutureAdapters.ListenableFutureAdapter(var0, var1));
   }

   private JdkFutureAdapters() {
   }

   private static class ListenableFutureAdapter<V> extends ForwardingFuture<V> implements ListenableFuture<V> {
      private static final ThreadFactory threadFactory = (new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("ListenableFutureAdapter-thread-%d").build();
      private static final Executor defaultAdapterExecutor;
      private final Executor adapterExecutor;
      private final ExecutionList executionList;
      private final AtomicBoolean hasListeners;
      private final Future<V> delegate;

      ListenableFutureAdapter(Future<V> var1) {
         this(var1, defaultAdapterExecutor);
      }

      ListenableFutureAdapter(Future<V> var1, Executor var2) {
         this.executionList = new ExecutionList();
         this.hasListeners = new AtomicBoolean(false);
         this.delegate = (Future)Preconditions.checkNotNull(var1);
         this.adapterExecutor = (Executor)Preconditions.checkNotNull(var2);
      }

      protected Future<V> delegate() {
         return this.delegate;
      }

      public void addListener(Runnable var1, Executor var2) {
         this.executionList.add(var1, var2);
         if(this.hasListeners.compareAndSet(false, true)) {
            if(this.delegate.isDone()) {
               this.executionList.execute();
               return;
            }

            this.adapterExecutor.execute(new Runnable() {
               public void run() {
                  try {
                     Uninterruptibles.getUninterruptibly(ListenableFutureAdapter.this.delegate);
                  } catch (Error var2) {
                     throw var2;
                  } catch (Throwable var3) {
                     ;
                  }

                  ListenableFutureAdapter.this.executionList.execute();
               }
            });
         }

      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }

      static {
         defaultAdapterExecutor = Executors.newCachedThreadPool(threadFactory);
      }
   }
}
