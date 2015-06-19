package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
class EmptyImmutableSortedSet<E> extends ImmutableSortedSet<E> {
   EmptyImmutableSortedSet(Comparator<? super E> var1) {
      super(var1);
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

   public UnmodifiableIterator<E> iterator() {
      return Iterators.emptyIterator();
   }

   @GwtIncompatible("NavigableSet")
   public UnmodifiableIterator<E> descendingIterator() {
      return Iterators.emptyIterator();
   }

   boolean isPartialView() {
      return false;
   }

   public ImmutableList<E> asList() {
      return ImmutableList.of();
   }

   int copyIntoArray(Object[] var1, int var2) {
      return var2;
   }

   public boolean equals(@Nullable Object var1) {
      if(var1 instanceof Set) {
         Set var2 = (Set)var1;
         return var2.isEmpty();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return 0;
   }

   public String toString() {
      return "[]";
   }

   public E first() {
      throw new NoSuchElementException();
   }

   public E last() {
      throw new NoSuchElementException();
   }

   ImmutableSortedSet<E> headSetImpl(E var1, boolean var2) {
      return this;
   }

   ImmutableSortedSet<E> subSetImpl(E var1, boolean var2, E var3, boolean var4) {
      return this;
   }

   ImmutableSortedSet<E> tailSetImpl(E var1, boolean var2) {
      return this;
   }

   int indexOf(@Nullable Object var1) {
      return -1;
   }

   ImmutableSortedSet<E> createDescendingSet() {
      return new EmptyImmutableSortedSet(Ordering.from(this.comparator).reverse());
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator descendingIterator() {
      return this.descendingIterator();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator iterator() {
      return this.iterator();
   }
}
