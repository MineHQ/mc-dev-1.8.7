package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

@GwtCompatible(
   serializable = true,
   emulated = true
)
final class ImmutableEnumSet<E extends Enum<E>> extends ImmutableSet<E> {
   private final transient EnumSet<E> delegate;
   private transient int hashCode;

   static <E extends Enum<E>> ImmutableSet<E> asImmutable(EnumSet<E> var0) {
      switch(var0.size()) {
      case 0:
         return ImmutableSet.of();
      case 1:
         return ImmutableSet.of(Iterables.getOnlyElement(var0));
      default:
         return new ImmutableEnumSet(var0);
      }
   }

   private ImmutableEnumSet(EnumSet<E> var1) {
      this.delegate = var1;
   }

   boolean isPartialView() {
      return false;
   }

   public UnmodifiableIterator<E> iterator() {
      return Iterators.unmodifiableIterator(this.delegate.iterator());
   }

   public int size() {
      return this.delegate.size();
   }

   public boolean contains(Object var1) {
      return this.delegate.contains(var1);
   }

   public boolean containsAll(Collection<?> var1) {
      return this.delegate.containsAll(var1);
   }

   public boolean isEmpty() {
      return this.delegate.isEmpty();
   }

   public boolean equals(Object var1) {
      return var1 == this || this.delegate.equals(var1);
   }

   public int hashCode() {
      int var1 = this.hashCode;
      return var1 == 0?(this.hashCode = this.delegate.hashCode()):var1;
   }

   public String toString() {
      return this.delegate.toString();
   }

   Object writeReplace() {
      return new ImmutableEnumSet.EnumSerializedForm(this.delegate);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator iterator() {
      return this.iterator();
   }

   // $FF: synthetic method
   ImmutableEnumSet(EnumSet var1, ImmutableEnumSet.SyntheticClass_1 var2) {
      this(var1);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class EnumSerializedForm<E extends Enum<E>> implements Serializable {
      final EnumSet<E> delegate;
      private static final long serialVersionUID = 0L;

      EnumSerializedForm(EnumSet<E> var1) {
         this.delegate = var1;
      }

      Object readResolve() {
         return new ImmutableEnumSet(this.delegate.clone());
      }
   }
}
