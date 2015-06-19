package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AbstractCheckedFuture;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.AsyncSettableFuture;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.FutureFallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SerializingExecutor;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.util.concurrent.Uninterruptibles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
public final class Futures {
   private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new AsyncFunction() {
      public ListenableFuture<Object> apply(ListenableFuture<Object> var1) {
         return var1;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ListenableFuture apply(Object var1) throws Exception {
         return this.apply((ListenableFuture)var1);
      }
   };
   private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf(new Function() {
      public Boolean apply(Constructor<?> var1) {
         return Boolean.valueOf(Arrays.asList(var1.getParameterTypes()).contains(String.class));
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object apply(Object var1) {
         return this.apply((Constructor)var1);
      }
   }).reverse();

   private Futures() {
   }

   public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> var0, Function<Exception, X> var1) {
      return new Futures.MappingCheckedFuture((ListenableFuture)Preconditions.checkNotNull(var0), var1);
   }

   public static <V> ListenableFuture<V> immediateFuture(@Nullable V var0) {
      return new Futures.ImmediateSuccessfulFuture(var0);
   }

   public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V var0) {
      return new Futures.ImmediateSuccessfulCheckedFuture(var0);
   }

   public static <V> ListenableFuture<V> immediateFailedFuture(Throwable var0) {
      Preconditions.checkNotNull(var0);
      return new Futures.ImmediateFailedFuture(var0);
   }

   public static <V> ListenableFuture<V> immediateCancelledFuture() {
      return new Futures.ImmediateCancelledFuture();
   }

   public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X var0) {
      Preconditions.checkNotNull(var0);
      return new Futures.ImmediateFailedCheckedFuture(var0);
   }

   public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> var0, FutureFallback<? extends V> var1) {
      return withFallback(var0, var1, MoreExecutors.sameThreadExecutor());
   }

   public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> var0, FutureFallback<? extends V> var1, Executor var2) {
      Preconditions.checkNotNull(var1);
      return new Futures.FallbackFuture(var0, var1, var2);
   }

   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> var0, AsyncFunction<? super I, ? extends O> var1) {
      return transform(var0, (AsyncFunction)var1, MoreExecutors.sameThreadExecutor());
   }

   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> var0, AsyncFunction<? super I, ? extends O> var1, Executor var2) {
      Futures.ChainingListenableFuture var3 = new Futures.ChainingListenableFuture(var1, var0, null);
      var0.addListener(var3, var2);
      return var3;
   }

   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> var0, Function<? super I, ? extends O> var1) {
      return transform(var0, (Function)var1, MoreExecutors.sameThreadExecutor());
   }

   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> var0, final Function<? super I, ? extends O> var1, Executor var2) {
      Preconditions.checkNotNull(var1);
      AsyncFunction var3 = new AsyncFunction() {
         public ListenableFuture<O> apply(I var1x) {
            Object var2 = var1.apply(var1x);
            return Futures.immediateFuture(var2);
         }
      };
      return transform(var0, var3, var2);
   }

   public static <I, O> Future<O> lazyTransform(final Future<I> var0, final Function<? super I, ? extends O> var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new Future() {
         public boolean cancel(boolean var1x) {
            return var0.cancel(var1x);
         }

         public boolean isCancelled() {
            return var0.isCancelled();
         }

         public boolean isDone() {
            return var0.isDone();
         }

         public O get() throws InterruptedException, ExecutionException {
            return this.applyTransformation(var0.get());
         }

         public O get(long var1x, TimeUnit var3) throws InterruptedException, ExecutionException, TimeoutException {
            return this.applyTransformation(var0.get(var1x, var3));
         }

         private O applyTransformation(I var1x) throws ExecutionException {
            try {
               return var1.apply(var1x);
            } catch (Throwable var3) {
               throw new ExecutionException(var3);
            }
         }
      };
   }

   public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> var0) {
      return transform(var0, DEREFERENCER);
   }

   @Beta
   public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture... var0) {
      return listFuture(ImmutableList.copyOf((Object[])var0), true, MoreExecutors.sameThreadExecutor());
   }

   @Beta
   public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> var0) {
      return listFuture(ImmutableList.copyOf(var0), true, MoreExecutors.sameThreadExecutor());
   }

   public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> var0) {
      return new Futures.NonCancellationPropagatingFuture(var0);
   }

   @Beta
   public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture... var0) {
      return listFuture(ImmutableList.copyOf((Object[])var0), false, MoreExecutors.sameThreadExecutor());
   }

   @Beta
   public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> var0) {
      return listFuture(ImmutableList.copyOf(var0), false, MoreExecutors.sameThreadExecutor());
   }

   @Beta
   public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> var0) {
      final ConcurrentLinkedQueue var1 = Queues.newConcurrentLinkedQueue();
      ImmutableList.Builder var2 = ImmutableList.builder();
      SerializingExecutor var3 = new SerializingExecutor(MoreExecutors.sameThreadExecutor());
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         final ListenableFuture var5 = (ListenableFuture)var4.next();
         AsyncSettableFuture var6 = AsyncSettableFuture.create();
         var1.add(var6);
         var5.addListener(new Runnable() {
            public void run() {
               ((AsyncSettableFuture)var1.remove()).setFuture(var5);
            }
         }, var3);
         var2.add((Object)var6);
      }

      return var2.build();
   }

   public static <V> void addCallback(ListenableFuture<V> var0, FutureCallback<? super V> var1) {
      addCallback(var0, var1, MoreExecutors.sameThreadExecutor());
   }

   public static <V> void addCallback(final ListenableFuture<V> var0, final FutureCallback<? super V> var1, Executor var2) {
      Preconditions.checkNotNull(var1);
      Runnable var3 = new Runnable() {
         public void run() {
            Object var1x;
            try {
               var1x = Uninterruptibles.getUninterruptibly(var0);
            } catch (ExecutionException var3) {
               var1.onFailure(var3.getCause());
               return;
            } catch (RuntimeException var4) {
               var1.onFailure(var4);
               return;
            } catch (Error var5) {
               var1.onFailure(var5);
               return;
            }

            var1.onSuccess(var1x);
         }
      };
      var0.addListener(var3, var2);
   }

   public static <V, X extends Exception> V get(Future<V> var0, Class<X> var1) throws X {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(var1), "Futures.get exception type (%s) must not be a RuntimeException", new Object[]{var1});

      try {
         return var0.get();
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         throw newWithCause(var1, var3);
      } catch (ExecutionException var4) {
         wrapAndThrowExceptionOrError(var4.getCause(), var1);
         throw new AssertionError();
      }
   }

   public static <V, X extends Exception> V get(Future<V> var0, long var1, TimeUnit var3, Class<X> var4) throws X {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var3);
      Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(var4), "Futures.get exception type (%s) must not be a RuntimeException", new Object[]{var4});

      try {
         return var0.get(var1, var3);
      } catch (InterruptedException var6) {
         Thread.currentThread().interrupt();
         throw newWithCause(var4, var6);
      } catch (TimeoutException var7) {
         throw newWithCause(var4, var7);
      } catch (ExecutionException var8) {
         wrapAndThrowExceptionOrError(var8.getCause(), var4);
         throw new AssertionError();
      }
   }

   private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable var0, Class<X> var1) throws X {
      if(var0 instanceof Error) {
         throw new ExecutionError((Error)var0);
      } else if(var0 instanceof RuntimeException) {
         throw new UncheckedExecutionException(var0);
      } else {
         throw newWithCause(var1, var0);
      }
   }

   public static <V> V getUnchecked(Future<V> var0) {
      Preconditions.checkNotNull(var0);

      try {
         return Uninterruptibles.getUninterruptibly(var0);
      } catch (ExecutionException var2) {
         wrapAndThrowUnchecked(var2.getCause());
         throw new AssertionError();
      }
   }

   private static void wrapAndThrowUnchecked(Throwable var0) {
      if(var0 instanceof Error) {
         throw new ExecutionError((Error)var0);
      } else {
         throw new UncheckedExecutionException(var0);
      }
   }

   private static <X extends Exception> X newWithCause(Class<X> var0, Throwable var1) {
      List var2 = Arrays.asList(var0.getConstructors());
      Iterator var3 = preferringStrings(var2).iterator();

      Exception var5;
      do {
         if(!var3.hasNext()) {
            throw new IllegalArgumentException("No appropriate constructor for exception of type " + var0 + " in response to chained exception", var1);
         }

         Constructor var4 = (Constructor)var3.next();
         var5 = (Exception)newFromConstructor(var4, var1);
      } while(var5 == null);

      if(var5.getCause() == null) {
         var5.initCause(var1);
      }

      return var5;
   }

   private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> var0) {
      return WITH_STRING_PARAM_FIRST.sortedCopy(var0);
   }

   @Nullable
   private static <X> X newFromConstructor(Constructor<X> var0, Throwable var1) {
      Class[] var2 = var0.getParameterTypes();
      Object[] var3 = new Object[var2.length];

      for(int var4 = 0; var4 < var2.length; ++var4) {
         Class var5 = var2[var4];
         if(var5.equals(String.class)) {
            var3[var4] = var1.toString();
         } else {
            if(!var5.equals(Throwable.class)) {
               return null;
            }

            var3[var4] = var1;
         }
      }

      try {
         return var0.newInstance(var3);
      } catch (IllegalArgumentException var6) {
         return null;
      } catch (InstantiationException var7) {
         return null;
      } catch (IllegalAccessException var8) {
         return null;
      } catch (InvocationTargetException var9) {
         return null;
      }
   }

   private static <V> ListenableFuture<List<V>> listFuture(ImmutableList<ListenableFuture<? extends V>> var0, boolean var1, Executor var2) {
      return new Futures.CombinedFuture(var0, var1, var2, new Futures.FutureCombiner() {
         public List<V> combine(List<Optional<V>> var1) {
            ArrayList var2 = Lists.newArrayList();
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               Optional var4 = (Optional)var3.next();
               var2.add(var4 != null?var4.orNull():null);
            }

            return Collections.unmodifiableList(var2);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object combine(List var1) {
            return this.combine(var1);
         }
      });
   }

   private static class MappingCheckedFuture<V, X extends Exception> extends AbstractCheckedFuture<V, X> {
      final Function<Exception, X> mapper;

      MappingCheckedFuture(ListenableFuture<V> var1, Function<Exception, X> var2) {
         super(var1);
         this.mapper = (Function)Preconditions.checkNotNull(var2);
      }

      protected X mapException(Exception var1) {
         return (Exception)this.mapper.apply(var1);
      }
   }

   private static class CombinedFuture<V, C> extends AbstractFuture<C> {
      private static final Logger logger = Logger.getLogger(Futures.CombinedFuture.class.getName());
      ImmutableCollection<? extends ListenableFuture<? extends V>> futures;
      final boolean allMustSucceed;
      final AtomicInteger remaining;
      Futures.FutureCombiner<V, C> combiner;
      List<Optional<V>> values;
      final Object seenExceptionsLock = new Object();
      Set<Throwable> seenExceptions;

      CombinedFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> var1, boolean var2, Executor var3, Futures.FutureCombiner<V, C> var4) {
         this.futures = var1;
         this.allMustSucceed = var2;
         this.remaining = new AtomicInteger(var1.size());
         this.combiner = var4;
         this.values = Lists.newArrayListWithCapacity(var1.size());
         this.init(var3);
      }

      protected void init(Executor var1) {
         this.addListener(new Runnable() {
            public void run() {
               if(CombinedFuture.this.isCancelled()) {
                  Iterator var1 = CombinedFuture.this.futures.iterator();

                  while(var1.hasNext()) {
                     ListenableFuture var2 = (ListenableFuture)var1.next();
                     var2.cancel(CombinedFuture.this.wasInterrupted());
                  }
               }

               CombinedFuture.this.futures = null;
               CombinedFuture.this.values = null;
               CombinedFuture.this.combiner = null;
            }
         }, MoreExecutors.sameThreadExecutor());
         if(this.futures.isEmpty()) {
            this.set(this.combiner.combine(ImmutableList.of()));
         } else {
            int var2;
            for(var2 = 0; var2 < this.futures.size(); ++var2) {
               this.values.add((Object)null);
            }

            var2 = 0;
            Iterator var3 = this.futures.iterator();

            while(var3.hasNext()) {
               final ListenableFuture var4 = (ListenableFuture)var3.next();
               final int var5 = var2++;
               var4.addListener(new Runnable() {
                  public void run() {
                     CombinedFuture.this.setOneValue(var5, var4);
                  }
               }, var1);
            }

         }
      }

      private void setExceptionAndMaybeLog(Throwable var1) {
         boolean var2 = false;
         boolean var3 = true;
         if(this.allMustSucceed) {
            var2 = super.setException(var1);
            Object var4 = this.seenExceptionsLock;
            synchronized(this.seenExceptionsLock) {
               if(this.seenExceptions == null) {
                  this.seenExceptions = Sets.newHashSet();
               }

               var3 = this.seenExceptions.add(var1);
            }
         }

         if(var1 instanceof Error || this.allMustSucceed && !var2 && var3) {
            logger.log(Level.SEVERE, "input future failed.", var1);
         }

      }

      private void setOneValue(int var1, Future<? extends V> var2) {
         List var3 = this.values;
         if(this.isDone() || var3 == null) {
            Preconditions.checkState(this.allMustSucceed || this.isCancelled(), "Future was done before all dependencies completed");
         }

         boolean var13 = false;

         int var18;
         Futures.FutureCombiner var5;
         label303: {
            label304: {
               label305: {
                  try {
                     var13 = true;
                     Preconditions.checkState(var2.isDone(), "Tried to set value from future which is not done");
                     Object var4 = Uninterruptibles.getUninterruptibly(var2);
                     if(var3 != null) {
                        var3.set(var1, Optional.fromNullable(var4));
                        var13 = false;
                     } else {
                        var13 = false;
                     }
                     break label303;
                  } catch (CancellationException var14) {
                     if(this.allMustSucceed) {
                        this.cancel(false);
                        var13 = false;
                     } else {
                        var13 = false;
                     }
                  } catch (ExecutionException var15) {
                     this.setExceptionAndMaybeLog(var15.getCause());
                     var13 = false;
                     break label305;
                  } catch (Throwable var16) {
                     this.setExceptionAndMaybeLog(var16);
                     var13 = false;
                     break label304;
                  } finally {
                     if(var13) {
                        int var7 = this.remaining.decrementAndGet();
                        Preconditions.checkState(var7 >= 0, "Less than 0 remaining futures");
                        if(var7 == 0) {
                           Futures.FutureCombiner var8 = this.combiner;
                           if(var8 != null && var3 != null) {
                              this.set(var8.combine(var3));
                           } else {
                              Preconditions.checkState(this.isDone());
                           }
                        }

                     }
                  }

                  var18 = this.remaining.decrementAndGet();
                  Preconditions.checkState(var18 >= 0, "Less than 0 remaining futures");
                  if(var18 == 0) {
                     var5 = this.combiner;
                     if(var5 != null && var3 != null) {
                        this.set(var5.combine(var3));
                     } else {
                        Preconditions.checkState(this.isDone());
                     }

                     return;
                  }

                  return;
               }

               var18 = this.remaining.decrementAndGet();
               Preconditions.checkState(var18 >= 0, "Less than 0 remaining futures");
               if(var18 == 0) {
                  var5 = this.combiner;
                  if(var5 != null && var3 != null) {
                     this.set(var5.combine(var3));
                  } else {
                     Preconditions.checkState(this.isDone());
                  }

                  return;
               }

               return;
            }

            var18 = this.remaining.decrementAndGet();
            Preconditions.checkState(var18 >= 0, "Less than 0 remaining futures");
            if(var18 == 0) {
               var5 = this.combiner;
               if(var5 != null && var3 != null) {
                  this.set(var5.combine(var3));
               } else {
                  Preconditions.checkState(this.isDone());
               }

               return;
            }

            return;
         }

         var18 = this.remaining.decrementAndGet();
         Preconditions.checkState(var18 >= 0, "Less than 0 remaining futures");
         if(var18 == 0) {
            var5 = this.combiner;
            if(var5 != null && var3 != null) {
               this.set(var5.combine(var3));
            } else {
               Preconditions.checkState(this.isDone());
            }
         }

      }
   }

   private interface FutureCombiner<V, C> {
      C combine(List<Optional<V>> var1);
   }

   private static class NonCancellationPropagatingFuture<V> extends AbstractFuture<V> {
      NonCancellationPropagatingFuture(final ListenableFuture<V> var1) {
         Preconditions.checkNotNull(var1);
         Futures.addCallback(var1, new FutureCallback() {
            public void onSuccess(V var1x) {
               NonCancellationPropagatingFuture.this.set(var1x);
            }

            public void onFailure(Throwable var1x) {
               if(var1.isCancelled()) {
                  NonCancellationPropagatingFuture.this.cancel(false);
               } else {
                  NonCancellationPropagatingFuture.this.setException(var1x);
               }

            }
         }, MoreExecutors.sameThreadExecutor());
      }
   }

   private static class ChainingListenableFuture<I, O> extends AbstractFuture<O> implements Runnable {
      private AsyncFunction<? super I, ? extends O> function;
      private ListenableFuture<? extends I> inputFuture;
      private volatile ListenableFuture<? extends O> outputFuture;
      private final CountDownLatch outputCreated;

      private ChainingListenableFuture(AsyncFunction<? super I, ? extends O> var1, ListenableFuture<? extends I> var2) {
         this.outputCreated = new CountDownLatch(1);
         this.function = (AsyncFunction)Preconditions.checkNotNull(var1);
         this.inputFuture = (ListenableFuture)Preconditions.checkNotNull(var2);
      }

      public boolean cancel(boolean var1) {
         if(super.cancel(var1)) {
            this.cancel(this.inputFuture, var1);
            this.cancel(this.outputFuture, var1);
            return true;
         } else {
            return false;
         }
      }

      private void cancel(@Nullable Future<?> var1, boolean var2) {
         if(var1 != null) {
            var1.cancel(var2);
         }

      }

      public void run() {
         try {
            try {
               Object var1;
               try {
                  var1 = Uninterruptibles.getUninterruptibly(this.inputFuture);
               } catch (CancellationException var9) {
                  this.cancel(false);
                  return;
               } catch (ExecutionException var10) {
                  this.setException(var10.getCause());
                  return;
               }

               final ListenableFuture var2 = this.outputFuture = (ListenableFuture)Preconditions.checkNotNull(this.function.apply(var1), "AsyncFunction may not return null.");
               if(this.isCancelled()) {
                  var2.cancel(this.wasInterrupted());
                  this.outputFuture = null;
                  return;
               }

               var2.addListener(new Runnable() {
                  public void run() {
                     try {
                        ChainingListenableFuture.this.set(Uninterruptibles.getUninterruptibly(var2));
                        return;
                     } catch (CancellationException var6) {
                        ChainingListenableFuture.this.cancel(false);
                     } catch (ExecutionException var7) {
                        ChainingListenableFuture.this.setException(var7.getCause());
                        return;
                     } finally {
                        ChainingListenableFuture.this.outputFuture = null;
                     }

                  }
               }, MoreExecutors.sameThreadExecutor());
            } catch (UndeclaredThrowableException var11) {
               this.setException(var11.getCause());
            } catch (Throwable var12) {
               this.setException(var12);
            }

         } finally {
            this.function = null;
            this.inputFuture = null;
            this.outputCreated.countDown();
         }
      }

      // $FF: synthetic method
      ChainingListenableFuture(AsyncFunction var1, ListenableFuture var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class FallbackFuture<V> extends AbstractFuture<V> {
      private volatile ListenableFuture<? extends V> running;

      FallbackFuture(ListenableFuture<? extends V> var1, final FutureFallback<? extends V> var2, Executor var3) {
         this.running = var1;
         Futures.addCallback(this.running, new FutureCallback() {
            public void onSuccess(V var1) {
               FallbackFuture.this.set(var1);
            }

            public void onFailure(Throwable var1) {
               if(!FallbackFuture.this.isCancelled()) {
                  try {
                     FallbackFuture.this.running = var2.create(var1);
                     if(FallbackFuture.this.isCancelled()) {
                        FallbackFuture.this.running.cancel(FallbackFuture.this.wasInterrupted());
                        return;
                     }

                     Futures.addCallback(FallbackFuture.this.running, new FutureCallback() {
                        public void onSuccess(V var1) {
                           FallbackFuture.this.set(var1);
                        }

                        public void onFailure(Throwable var1) {
                           if(FallbackFuture.this.running.isCancelled()) {
                              FallbackFuture.this.cancel(false);
                           } else {
                              FallbackFuture.this.setException(var1);
                           }

                        }
                     }, MoreExecutors.sameThreadExecutor());
                  } catch (Throwable var3) {
                     FallbackFuture.this.setException(var3);
                  }

               }
            }
         }, var3);
      }

      public boolean cancel(boolean var1) {
         if(super.cancel(var1)) {
            this.running.cancel(var1);
            return true;
         } else {
            return false;
         }
      }
   }

   private static class ImmediateFailedCheckedFuture<V, X extends Exception> extends Futures.ImmediateFuture<V> implements CheckedFuture<V, X> {
      private final X thrown;

      ImmediateFailedCheckedFuture(X var1) {
         super(null);
         this.thrown = var1;
      }

      public V get() throws ExecutionException {
         throw new ExecutionException(this.thrown);
      }

      public V checkedGet() throws X {
         throw this.thrown;
      }

      public V checkedGet(long var1, TimeUnit var3) throws X {
         Preconditions.checkNotNull(var3);
         throw this.thrown;
      }
   }

   private static class ImmediateCancelledFuture<V> extends Futures.ImmediateFuture<V> {
      private final CancellationException thrown = new CancellationException("Immediate cancelled future.");

      ImmediateCancelledFuture() {
         super(null);
      }

      public boolean isCancelled() {
         return true;
      }

      public V get() {
         throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.thrown);
      }
   }

   private static class ImmediateFailedFuture<V> extends Futures.ImmediateFuture<V> {
      private final Throwable thrown;

      ImmediateFailedFuture(Throwable var1) {
         super(null);
         this.thrown = var1;
      }

      public V get() throws ExecutionException {
         throw new ExecutionException(this.thrown);
      }
   }

   private static class ImmediateSuccessfulCheckedFuture<V, X extends Exception> extends Futures.ImmediateFuture<V> implements CheckedFuture<V, X> {
      @Nullable
      private final V value;

      ImmediateSuccessfulCheckedFuture(@Nullable V var1) {
         super(null);
         this.value = var1;
      }

      public V get() {
         return this.value;
      }

      public V checkedGet() {
         return this.value;
      }

      public V checkedGet(long var1, TimeUnit var3) {
         Preconditions.checkNotNull(var3);
         return this.value;
      }
   }

   private static class ImmediateSuccessfulFuture<V> extends Futures.ImmediateFuture<V> {
      @Nullable
      private final V value;

      ImmediateSuccessfulFuture(@Nullable V var1) {
         super(null);
         this.value = var1;
      }

      public V get() {
         return this.value;
      }
   }

   private abstract static class ImmediateFuture<V> implements ListenableFuture<V> {
      private static final Logger log = Logger.getLogger(Futures.ImmediateFuture.class.getName());

      private ImmediateFuture() {
      }

      public void addListener(Runnable var1, Executor var2) {
         Preconditions.checkNotNull(var1, "Runnable was null.");
         Preconditions.checkNotNull(var2, "Executor was null.");

         try {
            var2.execute(var1);
         } catch (RuntimeException var4) {
            log.log(Level.SEVERE, "RuntimeException while executing runnable " + var1 + " with executor " + var2, var4);
         }

      }

      public boolean cancel(boolean var1) {
         return false;
      }

      public abstract V get() throws ExecutionException;

      public V get(long var1, TimeUnit var3) throws ExecutionException {
         Preconditions.checkNotNull(var3);
         return this.get();
      }

      public boolean isCancelled() {
         return false;
      }

      public boolean isDone() {
         return true;
      }

      // $FF: synthetic method
      ImmediateFuture(Object var1) {
         this();
      }
   }
}
