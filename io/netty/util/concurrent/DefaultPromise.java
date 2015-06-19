package io.netty.util.concurrent;

import io.netty.util.Signal;
import io.netty.util.concurrent.AbstractFuture;
import io.netty.util.concurrent.BlockingOperationException;
import io.netty.util.concurrent.DefaultFutureListeners;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GenericProgressiveFutureListener;
import io.netty.util.concurrent.ProgressiveFuture;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayDeque;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

public class DefaultPromise<V> extends AbstractFuture<V> implements Promise<V> {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultPromise.class);
   private static final InternalLogger rejectedExecutionLogger = InternalLoggerFactory.getInstance(DefaultPromise.class.getName() + ".rejectedExecution");
   private static final int MAX_LISTENER_STACK_DEPTH = 8;
   private static final Signal SUCCESS = Signal.valueOf(DefaultPromise.class.getName() + ".SUCCESS");
   private static final Signal UNCANCELLABLE = Signal.valueOf(DefaultPromise.class.getName() + ".UNCANCELLABLE");
   private static final DefaultPromise.CauseHolder CANCELLATION_CAUSE_HOLDER = new DefaultPromise.CauseHolder(new CancellationException());
   private final EventExecutor executor;
   private volatile Object result;
   private Object listeners;
   private DefaultPromise<V>.LateListeners lateListeners;
   private short waiters;

   public DefaultPromise(EventExecutor var1) {
      if(var1 == null) {
         throw new NullPointerException("executor");
      } else {
         this.executor = var1;
      }
   }

   protected DefaultPromise() {
      this.executor = null;
   }

   protected EventExecutor executor() {
      return this.executor;
   }

   public boolean isCancelled() {
      return isCancelled0(this.result);
   }

   private static boolean isCancelled0(Object var0) {
      return var0 instanceof DefaultPromise.CauseHolder && ((DefaultPromise.CauseHolder)var0).cause instanceof CancellationException;
   }

   public boolean isCancellable() {
      return this.result == null;
   }

   public boolean isDone() {
      return isDone0(this.result);
   }

   private static boolean isDone0(Object var0) {
      return var0 != null && var0 != UNCANCELLABLE;
   }

   public boolean isSuccess() {
      Object var1 = this.result;
      return var1 != null && var1 != UNCANCELLABLE?!(var1 instanceof DefaultPromise.CauseHolder):false;
   }

   public Throwable cause() {
      Object var1 = this.result;
      return var1 instanceof DefaultPromise.CauseHolder?((DefaultPromise.CauseHolder)var1).cause:null;
   }

   public Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> var1) {
      if(var1 == null) {
         throw new NullPointerException("listener");
      } else if(this.isDone()) {
         this.notifyLateListener(var1);
         return this;
      } else {
         synchronized(this) {
            if(!this.isDone()) {
               if(this.listeners == null) {
                  this.listeners = var1;
               } else if(this.listeners instanceof DefaultFutureListeners) {
                  ((DefaultFutureListeners)this.listeners).add(var1);
               } else {
                  GenericFutureListener var3 = (GenericFutureListener)this.listeners;
                  this.listeners = new DefaultFutureListeners(var3, var1);
               }

               return this;
            }
         }

         this.notifyLateListener(var1);
         return this;
      }
   }

   public Promise<V> addListeners(GenericFutureListener... var1) {
      if(var1 == null) {
         throw new NullPointerException("listeners");
      } else {
         GenericFutureListener[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            GenericFutureListener var5 = var2[var4];
            if(var5 == null) {
               break;
            }

            this.addListener(var5);
         }

         return this;
      }
   }

   public Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> var1) {
      if(var1 == null) {
         throw new NullPointerException("listener");
      } else if(this.isDone()) {
         return this;
      } else {
         synchronized(this) {
            if(!this.isDone()) {
               if(this.listeners instanceof DefaultFutureListeners) {
                  ((DefaultFutureListeners)this.listeners).remove(var1);
               } else if(this.listeners == var1) {
                  this.listeners = null;
               }
            }

            return this;
         }
      }
   }

   public Promise<V> removeListeners(GenericFutureListener... var1) {
      if(var1 == null) {
         throw new NullPointerException("listeners");
      } else {
         GenericFutureListener[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            GenericFutureListener var5 = var2[var4];
            if(var5 == null) {
               break;
            }

            this.removeListener(var5);
         }

         return this;
      }
   }

   public Promise<V> sync() throws InterruptedException {
      this.await();
      this.rethrowIfFailed();
      return this;
   }

   public Promise<V> syncUninterruptibly() {
      this.awaitUninterruptibly();
      this.rethrowIfFailed();
      return this;
   }

   private void rethrowIfFailed() {
      Throwable var1 = this.cause();
      if(var1 != null) {
         PlatformDependent.throwException(var1);
      }
   }

   public Promise<V> await() throws InterruptedException {
      if(this.isDone()) {
         return this;
      } else if(Thread.interrupted()) {
         throw new InterruptedException(this.toString());
      } else {
         synchronized(this) {
            while(!this.isDone()) {
               this.checkDeadLock();
               this.incWaiters();

               try {
                  this.wait();
               } finally {
                  this.decWaiters();
               }
            }

            return this;
         }
      }
   }

   public boolean await(long var1, TimeUnit var3) throws InterruptedException {
      return this.await0(var3.toNanos(var1), true);
   }

   public boolean await(long var1) throws InterruptedException {
      return this.await0(TimeUnit.MILLISECONDS.toNanos(var1), true);
   }

   public Promise<V> awaitUninterruptibly() {
      if(this.isDone()) {
         return this;
      } else {
         boolean var1 = false;
         synchronized(this) {
            while(!this.isDone()) {
               this.checkDeadLock();
               this.incWaiters();

               try {
                  this.wait();
               } catch (InterruptedException var9) {
                  var1 = true;
               } finally {
                  this.decWaiters();
               }
            }
         }

         if(var1) {
            Thread.currentThread().interrupt();
         }

         return this;
      }
   }

   public boolean awaitUninterruptibly(long var1, TimeUnit var3) {
      try {
         return this.await0(var3.toNanos(var1), false);
      } catch (InterruptedException var5) {
         throw new InternalError();
      }
   }

   public boolean awaitUninterruptibly(long var1) {
      try {
         return this.await0(TimeUnit.MILLISECONDS.toNanos(var1), false);
      } catch (InterruptedException var4) {
         throw new InternalError();
      }
   }

   private boolean await0(long var1, boolean var3) throws InterruptedException {
      if(this.isDone()) {
         return true;
      } else if(var1 <= 0L) {
         return this.isDone();
      } else if(var3 && Thread.interrupted()) {
         throw new InterruptedException(this.toString());
      } else {
         long var4 = System.nanoTime();
         long var6 = var1;
         boolean var8 = false;

         boolean var10;
         try {
            synchronized(this) {
               if(!this.isDone()) {
                  if(var6 <= 0L) {
                     var10 = this.isDone();
                     return var10;
                  }

                  this.checkDeadLock();
                  this.incWaiters();

                  try {
                     InterruptedException var26;
                     do {
                        try {
                           this.wait(var6 / 1000000L, (int)(var6 % 1000000L));
                        } catch (InterruptedException var22) {
                           var26 = var22;
                           if(var3) {
                              throw var22;
                           }

                           var8 = true;
                        }

                        if(this.isDone()) {
                           var26 = true;
                           return (boolean)var26;
                        }

                        var6 = var1 - (System.nanoTime() - var4);
                     } while(var6 > 0L);

                     var26 = this.isDone();
                     return (boolean)var26;
                  } finally {
                     this.decWaiters();
                  }
               }

               var10 = true;
            }
         } finally {
            if(var8) {
               Thread.currentThread().interrupt();
            }

         }

         return var10;
      }
   }

   protected void checkDeadLock() {
      EventExecutor var1 = this.executor();
      if(var1 != null && var1.inEventLoop()) {
         throw new BlockingOperationException(this.toString());
      }
   }

   public Promise<V> setSuccess(V var1) {
      if(this.setSuccess0(var1)) {
         this.notifyListeners();
         return this;
      } else {
         throw new IllegalStateException("complete already: " + this);
      }
   }

   public boolean trySuccess(V var1) {
      if(this.setSuccess0(var1)) {
         this.notifyListeners();
         return true;
      } else {
         return false;
      }
   }

   public Promise<V> setFailure(Throwable var1) {
      if(this.setFailure0(var1)) {
         this.notifyListeners();
         return this;
      } else {
         throw new IllegalStateException("complete already: " + this, var1);
      }
   }

   public boolean tryFailure(Throwable var1) {
      if(this.setFailure0(var1)) {
         this.notifyListeners();
         return true;
      } else {
         return false;
      }
   }

   public boolean cancel(boolean var1) {
      Object var2 = this.result;
      if(!isDone0(var2) && var2 != UNCANCELLABLE) {
         synchronized(this) {
            var2 = this.result;
            if(isDone0(var2) || var2 == UNCANCELLABLE) {
               return false;
            }

            this.result = CANCELLATION_CAUSE_HOLDER;
            if(this.hasWaiters()) {
               this.notifyAll();
            }
         }

         this.notifyListeners();
         return true;
      } else {
         return false;
      }
   }

   public boolean setUncancellable() {
      Object var1 = this.result;
      if(isDone0(var1)) {
         return !isCancelled0(var1);
      } else {
         synchronized(this) {
            var1 = this.result;
            if(isDone0(var1)) {
               return !isCancelled0(var1);
            } else {
               this.result = UNCANCELLABLE;
               return true;
            }
         }
      }
   }

   private boolean setFailure0(Throwable var1) {
      if(var1 == null) {
         throw new NullPointerException("cause");
      } else if(this.isDone()) {
         return false;
      } else {
         synchronized(this) {
            if(this.isDone()) {
               return false;
            } else {
               this.result = new DefaultPromise.CauseHolder(var1);
               if(this.hasWaiters()) {
                  this.notifyAll();
               }

               return true;
            }
         }
      }
   }

   private boolean setSuccess0(V var1) {
      if(this.isDone()) {
         return false;
      } else {
         synchronized(this) {
            if(this.isDone()) {
               return false;
            } else {
               if(var1 == null) {
                  this.result = SUCCESS;
               } else {
                  this.result = var1;
               }

               if(this.hasWaiters()) {
                  this.notifyAll();
               }

               return true;
            }
         }
      }
   }

   public V getNow() {
      Object var1 = this.result;
      return !(var1 instanceof DefaultPromise.CauseHolder) && var1 != SUCCESS?var1:null;
   }

   private boolean hasWaiters() {
      return this.waiters > 0;
   }

   private void incWaiters() {
      if(this.waiters == 32767) {
         throw new IllegalStateException("too many waiters: " + this);
      } else {
         ++this.waiters;
      }
   }

   private void decWaiters() {
      --this.waiters;
   }

   private void notifyListeners() {
      Object var1 = this.listeners;
      if(var1 != null) {
         EventExecutor var2 = this.executor();
         if(var2.inEventLoop()) {
            InternalThreadLocalMap var3 = InternalThreadLocalMap.get();
            int var4 = var3.futureListenerStackDepth();
            if(var4 < 8) {
               var3.setFutureListenerStackDepth(var4 + 1);

               try {
                  if(var1 instanceof DefaultFutureListeners) {
                     notifyListeners0(this, (DefaultFutureListeners)var1);
                  } else {
                     GenericFutureListener var5 = (GenericFutureListener)var1;
                     notifyListener0(this, var5);
                  }
               } finally {
                  this.listeners = null;
                  var3.setFutureListenerStackDepth(var4);
               }

               return;
            }
         }

         if(var1 instanceof DefaultFutureListeners) {
            final DefaultFutureListeners var9 = (DefaultFutureListeners)var1;
            execute(var2, new Runnable() {
               public void run() {
                  DefaultPromise.notifyListeners0(DefaultPromise.this, var9);
                  DefaultPromise.this.listeners = null;
               }
            });
         } else {
            final GenericFutureListener var10 = (GenericFutureListener)var1;
            execute(var2, new Runnable() {
               public void run() {
                  DefaultPromise.notifyListener0(DefaultPromise.this, var10);
                  DefaultPromise.this.listeners = null;
               }
            });
         }

      }
   }

   private static void notifyListeners0(Future<?> var0, DefaultFutureListeners var1) {
      GenericFutureListener[] var2 = var1.listeners();
      int var3 = var1.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         notifyListener0(var0, var2[var4]);
      }

   }

   private void notifyLateListener(GenericFutureListener<?> var1) {
      EventExecutor var2 = this.executor();
      if(var2.inEventLoop()) {
         if(this.listeners != null || this.lateListeners != null) {
            DefaultPromise.LateListeners var8 = this.lateListeners;
            if(var8 == null) {
               this.lateListeners = var8 = new DefaultPromise.LateListeners();
            }

            var8.add(var1);
            execute(var2, var8);
            return;
         }

         InternalThreadLocalMap var3 = InternalThreadLocalMap.get();
         int var4 = var3.futureListenerStackDepth();
         if(var4 < 8) {
            var3.setFutureListenerStackDepth(var4 + 1);

            try {
               notifyListener0(this, var1);
            } finally {
               var3.setFutureListenerStackDepth(var4);
            }

            return;
         }
      }

      execute(var2, new DefaultPromise.LateListenerNotifier(var1));
   }

   protected static void notifyListener(EventExecutor var0, final Future<?> var1, final GenericFutureListener<?> var2) {
      if(var0.inEventLoop()) {
         InternalThreadLocalMap var3 = InternalThreadLocalMap.get();
         int var4 = var3.futureListenerStackDepth();
         if(var4 < 8) {
            var3.setFutureListenerStackDepth(var4 + 1);

            try {
               notifyListener0(var1, var2);
            } finally {
               var3.setFutureListenerStackDepth(var4);
            }

            return;
         }
      }

      execute(var0, new Runnable() {
         public void run() {
            DefaultPromise.notifyListener0(var1, var2);
         }
      });
   }

   private static void execute(EventExecutor var0, Runnable var1) {
      try {
         var0.execute(var1);
      } catch (Throwable var3) {
         rejectedExecutionLogger.error("Failed to submit a listener notification task. Event loop shut down?", var3);
      }

   }

   static void notifyListener0(Future var0, GenericFutureListener var1) {
      try {
         var1.operationComplete(var0);
      } catch (Throwable var3) {
         if(logger.isWarnEnabled()) {
            logger.warn("An exception was thrown by " + var1.getClass().getName() + ".operationComplete()", var3);
         }
      }

   }

   private synchronized Object progressiveListeners() {
      Object var1 = this.listeners;
      if(var1 == null) {
         return null;
      } else if(var1 instanceof DefaultFutureListeners) {
         DefaultFutureListeners var2 = (DefaultFutureListeners)var1;
         int var3 = var2.progressiveSize();
         GenericFutureListener[] var4;
         int var6;
         switch(var3) {
         case 0:
            return null;
         case 1:
            var4 = var2.listeners();
            int var5 = var4.length;

            for(var6 = 0; var6 < var5; ++var6) {
               GenericFutureListener var7 = var4[var6];
               if(var7 instanceof GenericProgressiveFutureListener) {
                  return var7;
               }
            }

            return null;
         default:
            var4 = var2.listeners();
            GenericProgressiveFutureListener[] var9 = new GenericProgressiveFutureListener[var3];
            var6 = 0;

            for(int var10 = 0; var10 < var3; ++var6) {
               GenericFutureListener var8 = var4[var6];
               if(var8 instanceof GenericProgressiveFutureListener) {
                  var9[var10++] = (GenericProgressiveFutureListener)var8;
               }
            }

            return var9;
         }
      } else {
         return var1 instanceof GenericProgressiveFutureListener?var1:null;
      }
   }

   void notifyProgressiveListeners(final long var1, final long var3) {
      Object var5 = this.progressiveListeners();
      if(var5 != null) {
         final ProgressiveFuture var6 = (ProgressiveFuture)this;
         EventExecutor var7 = this.executor();
         if(var7.inEventLoop()) {
            if(var5 instanceof GenericProgressiveFutureListener[]) {
               notifyProgressiveListeners0(var6, (GenericProgressiveFutureListener[])((GenericProgressiveFutureListener[])var5), var1, var3);
            } else {
               notifyProgressiveListener0(var6, (GenericProgressiveFutureListener)var5, var1, var3);
            }
         } else if(var5 instanceof GenericProgressiveFutureListener[]) {
            final GenericProgressiveFutureListener[] var8 = (GenericProgressiveFutureListener[])((GenericProgressiveFutureListener[])var5);
            execute(var7, new Runnable() {
               public void run() {
                  DefaultPromise.access$200(var6, var8, var1, var3);
               }
            });
         } else {
            final GenericProgressiveFutureListener var9 = (GenericProgressiveFutureListener)var5;
            execute(var7, new Runnable() {
               public void run() {
                  DefaultPromise.access$300(var6, var9, var1, var3);
               }
            });
         }

      }
   }

   private static void notifyProgressiveListeners0(ProgressiveFuture<?> var0, GenericProgressiveFutureListener<?>[] var1, long var2, long var4) {
      GenericProgressiveFutureListener[] var6 = var1;
      int var7 = var1.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         GenericProgressiveFutureListener var9 = var6[var8];
         if(var9 == null) {
            break;
         }

         notifyProgressiveListener0(var0, var9, var2, var4);
      }

   }

   private static void notifyProgressiveListener0(ProgressiveFuture var0, GenericProgressiveFutureListener var1, long var2, long var4) {
      try {
         var1.operationProgressed(var0, var2, var4);
      } catch (Throwable var7) {
         if(logger.isWarnEnabled()) {
            logger.warn("An exception was thrown by " + var1.getClass().getName() + ".operationProgressed()", var7);
         }
      }

   }

   public String toString() {
      return this.toStringBuilder().toString();
   }

   protected StringBuilder toStringBuilder() {
      StringBuilder var1 = new StringBuilder(64);
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append('@');
      var1.append(Integer.toHexString(this.hashCode()));
      Object var2 = this.result;
      if(var2 == SUCCESS) {
         var1.append("(success)");
      } else if(var2 == UNCANCELLABLE) {
         var1.append("(uncancellable)");
      } else if(var2 instanceof DefaultPromise.CauseHolder) {
         var1.append("(failure(");
         var1.append(((DefaultPromise.CauseHolder)var2).cause);
         var1.append(')');
      } else {
         var1.append("(incomplete)");
      }

      return var1;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future await() throws InterruptedException {
      return this.await();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future sync() throws InterruptedException {
      return this.sync();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }

   // $FF: synthetic method
   static void access$200(ProgressiveFuture var0, GenericProgressiveFutureListener[] var1, long var2, long var4) {
      notifyProgressiveListeners0(var0, var1, var2, var4);
   }

   // $FF: synthetic method
   static void access$300(ProgressiveFuture var0, GenericProgressiveFutureListener var1, long var2, long var4) {
      notifyProgressiveListener0(var0, var1, var2, var4);
   }

   static {
      CANCELLATION_CAUSE_HOLDER.cause.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
   }

   private final class LateListenerNotifier implements Runnable {
      private GenericFutureListener<?> l;

      LateListenerNotifier(GenericFutureListener<?> var1) {
         this.l = var2;
      }

      public void run() {
         DefaultPromise.LateListeners var1 = DefaultPromise.this.lateListeners;
         if(this.l != null) {
            if(var1 == null) {
               DefaultPromise.this.lateListeners = var1 = DefaultPromise.this.new LateListeners();
            }

            var1.add(this.l);
            this.l = null;
         }

         var1.run();
      }
   }

   private final class LateListeners extends ArrayDeque<GenericFutureListener<?>> implements Runnable {
      private static final long serialVersionUID = -687137418080392244L;

      LateListeners() {
         super(2);
      }

      public void run() {
         if(DefaultPromise.this.listeners == null) {
            while(true) {
               GenericFutureListener var1 = (GenericFutureListener)this.poll();
               if(var1 == null) {
                  break;
               }

               DefaultPromise.notifyListener0(DefaultPromise.this, var1);
            }
         } else {
            DefaultPromise.execute(DefaultPromise.this.executor(), this);
         }

      }
   }

   private static final class CauseHolder {
      final Throwable cause;

      CauseHolder(Throwable var1) {
         this.cause = var1;
      }
   }
}
