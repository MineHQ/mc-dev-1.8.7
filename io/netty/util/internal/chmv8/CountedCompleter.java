package io.netty.util.internal.chmv8;

import io.netty.util.internal.chmv8.ForkJoinTask;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;

public abstract class CountedCompleter<T> extends ForkJoinTask<T> {
   private static final long serialVersionUID = 5232453752276485070L;
   final CountedCompleter<?> completer;
   volatile int pending;
   private static final Unsafe U;
   private static final long PENDING;

   protected CountedCompleter(CountedCompleter<?> var1, int var2) {
      this.completer = var1;
      this.pending = var2;
   }

   protected CountedCompleter(CountedCompleter<?> var1) {
      this.completer = var1;
   }

   protected CountedCompleter() {
      this.completer = null;
   }

   public abstract void compute();

   public void onCompletion(CountedCompleter<?> var1) {
   }

   public boolean onExceptionalCompletion(Throwable var1, CountedCompleter<?> var2) {
      return true;
   }

   public final CountedCompleter<?> getCompleter() {
      return this.completer;
   }

   public final int getPendingCount() {
      return this.pending;
   }

   public final void setPendingCount(int var1) {
      this.pending = var1;
   }

   public final void addToPendingCount(int var1) {
      int var2;
      do {
         var2 = this.pending;
      } while(!U.compareAndSwapInt(this, PENDING, this.pending, var2 + var1));

   }

   public final boolean compareAndSetPendingCount(int var1, int var2) {
      return U.compareAndSwapInt(this, PENDING, var1, var2);
   }

   public final int decrementPendingCountUnlessZero() {
      int var1;
      do {
         var1 = this.pending;
      } while(this.pending != 0 && !U.compareAndSwapInt(this, PENDING, var1, var1 - 1));

      return var1;
   }

   public final CountedCompleter<?> getRoot() {
      CountedCompleter var1 = this;

      while(true) {
         CountedCompleter var2 = var1.completer;
         if(var1.completer == null) {
            return var1;
         }

         var1 = var2;
      }
   }

   public final void tryComplete() {
      CountedCompleter var1 = this;
      CountedCompleter var2 = this;

      label17:
      do {
         int var3;
         do {
            var3 = var1.pending;
            if(var1.pending == 0) {
               var1.onCompletion(var2);
               var2 = var1;
               continue label17;
            }
         } while(!U.compareAndSwapInt(var1, PENDING, var3, var3 - 1));

         return;
      } while((var1 = var1.completer) != null);

      var2.quietlyComplete();
   }

   public final void propagateCompletion() {
      CountedCompleter var1 = this;

      CountedCompleter var2;
      label17:
      do {
         int var3;
         do {
            var3 = var1.pending;
            if(var1.pending == 0) {
               var2 = var1;
               continue label17;
            }
         } while(!U.compareAndSwapInt(var1, PENDING, var3, var3 - 1));

         return;
      } while((var1 = var1.completer) != null);

      var2.quietlyComplete();
   }

   public void complete(T var1) {
      this.setRawResult(var1);
      this.onCompletion(this);
      this.quietlyComplete();
      CountedCompleter var2 = this.completer;
      if(this.completer != null) {
         var2.tryComplete();
      }

   }

   public final CountedCompleter<?> firstComplete() {
      int var1;
      do {
         var1 = this.pending;
         if(this.pending == 0) {
            return this;
         }
      } while(!U.compareAndSwapInt(this, PENDING, var1, var1 - 1));

      return null;
   }

   public final CountedCompleter<?> nextComplete() {
      CountedCompleter var1 = this.completer;
      if(this.completer != null) {
         return var1.firstComplete();
      } else {
         this.quietlyComplete();
         return null;
      }
   }

   public final void quietlyCompleteRoot() {
      CountedCompleter var1 = this;

      while(true) {
         CountedCompleter var2 = var1.completer;
         if(var1.completer == null) {
            var1.quietlyComplete();
            return;
         }

         var1 = var2;
      }
   }

   void internalPropagateException(Throwable var1) {
      CountedCompleter var2 = this;
      CountedCompleter var3 = this;

      while(var2.onExceptionalCompletion(var1, var3)) {
         var3 = var2;
         if((var2 = var2.completer) == null || var2.status < 0 || var2.recordExceptionalCompletion(var1) != Integer.MIN_VALUE) {
            break;
         }
      }

   }

   protected final boolean exec() {
      this.compute();
      return false;
   }

   public T getRawResult() {
      return null;
   }

   protected void setRawResult(T var1) {
   }

   private static Unsafe getUnsafe() {
      try {
         return Unsafe.getUnsafe();
      } catch (SecurityException var2) {
         try {
            return (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public Unsafe run() throws Exception {
                  Class var1 = Unsafe.class;
                  Field[] var2 = var1.getDeclaredFields();
                  int var3 = var2.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                     Field var5 = var2[var4];
                     var5.setAccessible(true);
                     Object var6 = var5.get((Object)null);
                     if(var1.isInstance(var6)) {
                        return (Unsafe)var1.cast(var6);
                     }
                  }

                  throw new NoSuchFieldError("the Unsafe");
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object run() throws Exception {
                  return this.run();
               }
            });
         } catch (PrivilegedActionException var1) {
            throw new RuntimeException("Could not initialize intrinsics", var1.getCause());
         }
      }
   }

   static {
      try {
         U = getUnsafe();
         PENDING = U.objectFieldOffset(CountedCompleter.class.getDeclaredField("pending"));
      } catch (Exception var1) {
         throw new Error(var1);
      }
   }
}
