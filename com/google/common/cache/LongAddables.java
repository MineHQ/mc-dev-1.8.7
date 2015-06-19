package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Supplier;
import com.google.common.cache.LongAddable;
import com.google.common.cache.LongAdder;
import java.util.concurrent.atomic.AtomicLong;

@GwtCompatible(
   emulated = true
)
final class LongAddables {
   private static final Supplier<LongAddable> SUPPLIER;

   LongAddables() {
   }

   public static LongAddable create() {
      return (LongAddable)SUPPLIER.get();
   }

   static {
      Supplier var0;
      try {
         new LongAdder();
         var0 = new Supplier() {
            public LongAddable get() {
               return new LongAdder();
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object get() {
               return this.get();
            }
         };
      } catch (Throwable var2) {
         var0 = new Supplier() {
            public LongAddable get() {
               return new LongAddables.PureJavaLongAddable(null);
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object get() {
               return this.get();
            }
         };
      }

      SUPPLIER = var0;
   }

   private static final class PureJavaLongAddable extends AtomicLong implements LongAddable {
      private PureJavaLongAddable() {
      }

      public void increment() {
         this.getAndIncrement();
      }

      public void add(long var1) {
         this.getAndAdd(var1);
      }

      public long sum() {
         return this.get();
      }

      // $FF: synthetic method
      PureJavaLongAddable(Object var1) {
         this();
      }
   }
}
