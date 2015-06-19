package com.google.common.util.concurrent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import javax.annotation.Nullable;

public final class Atomics {
   private Atomics() {
   }

   public static <V> AtomicReference<V> newReference() {
      return new AtomicReference();
   }

   public static <V> AtomicReference<V> newReference(@Nullable V var0) {
      return new AtomicReference(var0);
   }

   public static <E> AtomicReferenceArray<E> newReferenceArray(int var0) {
      return new AtomicReferenceArray(var0);
   }

   public static <E> AtomicReferenceArray<E> newReferenceArray(E[] var0) {
      return new AtomicReferenceArray(var0);
   }
}
