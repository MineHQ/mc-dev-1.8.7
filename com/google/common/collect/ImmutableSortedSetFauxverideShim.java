package com.google.common.collect;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;

abstract class ImmutableSortedSetFauxverideShim<E> extends ImmutableSet<E> {
   ImmutableSortedSetFauxverideShim() {
   }

   /** @deprecated */
   @Deprecated
   public static <E> ImmutableSortedSet.Builder<E> builder() {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public static <E> ImmutableSortedSet<E> of(E var0) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public static <E> ImmutableSortedSet<E> of(E var0, E var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public static <E> ImmutableSortedSet<E> of(E var0, E var1, E var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public static <E> ImmutableSortedSet<E> of(E var0, E var1, E var2, E var3) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public static <E> ImmutableSortedSet<E> of(E var0, E var1, E var2, E var3, E var4) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public static <E> ImmutableSortedSet<E> of(E var0, E var1, E var2, E var3, E var4, E var5, E... var6) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public static <E> ImmutableSortedSet<E> copyOf(E[] var0) {
      throw new UnsupportedOperationException();
   }
}
