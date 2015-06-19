package io.netty.util.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import sun.misc.Unsafe;

final class UnsafeAtomicReferenceFieldUpdater<U, M> extends AtomicReferenceFieldUpdater<U, M> {
   private final long offset;
   private final Unsafe unsafe;

   UnsafeAtomicReferenceFieldUpdater(Unsafe var1, Class<U> var2, String var3) throws NoSuchFieldException {
      Field var4 = var2.getDeclaredField(var3);
      if(!Modifier.isVolatile(var4.getModifiers())) {
         throw new IllegalArgumentException("Must be volatile");
      } else {
         this.unsafe = var1;
         this.offset = var1.objectFieldOffset(var4);
      }
   }

   public boolean compareAndSet(U var1, M var2, M var3) {
      return this.unsafe.compareAndSwapObject(var1, this.offset, var2, var3);
   }

   public boolean weakCompareAndSet(U var1, M var2, M var3) {
      return this.unsafe.compareAndSwapObject(var1, this.offset, var2, var3);
   }

   public void set(U var1, M var2) {
      this.unsafe.putObjectVolatile(var1, this.offset, var2);
   }

   public void lazySet(U var1, M var2) {
      this.unsafe.putOrderedObject(var1, this.offset, var2);
   }

   public M get(U var1) {
      return this.unsafe.getObjectVolatile(var1, this.offset);
   }
}
