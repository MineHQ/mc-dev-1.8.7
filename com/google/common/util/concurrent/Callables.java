package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;

public final class Callables {
   private Callables() {
   }

   public static <T> Callable<T> returning(@Nullable final T var0) {
      return new Callable() {
         public T call() {
            return var0;
         }
      };
   }

   static <T> Callable<T> threadRenaming(final Callable<T> var0, final Supplier<String> var1) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var0);
      return new Callable() {
         public T call() throws Exception {
            Thread var1x = Thread.currentThread();
            String var2 = var1x.getName();
            boolean var3 = Callables.trySetName((String)var1.get(), var1x);

            Object var4;
            try {
               var4 = var0.call();
            } finally {
               if(var3) {
                  Callables.trySetName(var2, var1x);
               }

            }

            return var4;
         }
      };
   }

   static Runnable threadRenaming(final Runnable var0, final Supplier<String> var1) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var0);
      return new Runnable() {
         public void run() {
            Thread var1x = Thread.currentThread();
            String var2 = var1x.getName();
            boolean var3 = Callables.trySetName((String)var1.get(), var1x);

            try {
               var0.run();
            } finally {
               if(var3) {
                  Callables.trySetName(var2, var1x);
               }

            }

         }
      };
   }

   private static boolean trySetName(String var0, Thread var1) {
      try {
         var1.setName(var0);
         return true;
      } catch (SecurityException var3) {
         return false;
      }
   }
}
