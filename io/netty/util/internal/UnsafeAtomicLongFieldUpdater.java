package io.netty.util.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import sun.misc.Unsafe;

final class UnsafeAtomicLongFieldUpdater<T> extends AtomicLongFieldUpdater<T> {
   private final long offset;
   private final Unsafe unsafe;

   UnsafeAtomicLongFieldUpdater(Unsafe var1, Class<?> var2, String var3) throws NoSuchFieldException {
      Field var4 = var2.getDeclaredField(var3);
      if(!Modifier.isVolatile(var4.getModifiers())) {
         throw new IllegalArgumentException("Must be volatile");
      } else {
         this.unsafe = var1;
         this.offset = var1.objectFieldOffset(var4);
      }
   }

   public boolean compareAndSet(T var1, long var2, long var4) {
      return this.unsafe.compareAndSwapLong(var1, this.offset, var2, var4);
   }

   public boolean weakCompareAndSet(T var1, long var2, long var4) {
      return this.unsafe.compareAndSwapLong(var1, this.offset, var2, var4);
   }

   public void set(T var1, long var2) {
      this.unsafe.putLongVolatile(var1, this.offset, var2);
   }

   public void lazySet(T var1, long var2) {
      this.unsafe.putOrderedLong(var1, this.offset, var2);
   }

   public long get(T var1) {
      return this.unsafe.getLongVolatile(var1, this.offset);
   }
}
