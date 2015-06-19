package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
final class EmptyImmutableSet extends ImmutableSet<Object> {
   static final EmptyImmutableSet INSTANCE = new EmptyImmutableSet();
   private static final long serialVersionUID = 0L;

   private EmptyImmutableSet() {
   }

   public int size() {
      return 0;
   }

   public boolean isEmpty() {
      return true;
   }

   public boolean contains(@Nullable Object var1) {
      return false;
   }

   public boolean containsAll(Collection<?> var1) {
      return var1.isEmpty();
   }

   public UnmodifiableIterator<Object> iterator() {
      return Iterators.emptyIterator();
   }

   boolean isPartialView() {
      return false;
   }

   int copyIntoArray(Object[] var1, int var2) {
      return var2;
   }

   public ImmutableList<Object> asList() {
      return ImmutableList.of();
   }

   public boolean equals(@Nullable Object var1) {
      if(var1 instanceof Set) {
         Set var2 = (Set)var1;
         return var2.isEmpty();
      } else {
         return false;
      }
   }

   public final int hashCode() {
      return 0;
   }

   boolean isHashCodeFast() {
      return true;
   }

   public String toString() {
      return "[]";
   }

   Object readResolve() {
      return INSTANCE;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator iterator() {
      return this.iterator();
   }
}
