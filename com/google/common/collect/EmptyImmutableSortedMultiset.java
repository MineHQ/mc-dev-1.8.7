package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.BoundType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

final class EmptyImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E> {
   private final ImmutableSortedSet<E> elementSet;

   EmptyImmutableSortedMultiset(Comparator<? super E> var1) {
      this.elementSet = ImmutableSortedSet.emptySet(var1);
   }

   public Multiset.Entry<E> firstEntry() {
      return null;
   }

   public Multiset.Entry<E> lastEntry() {
      return null;
   }

   public int count(@Nullable Object var1) {
      return 0;
   }

   public boolean containsAll(Collection<?> var1) {
      return var1.isEmpty();
   }

   public int size() {
      return 0;
   }

   public ImmutableSortedSet<E> elementSet() {
      return this.elementSet;
   }

   Multiset.Entry<E> getEntry(int var1) {
      throw new AssertionError("should never be called");
   }

   public ImmutableSortedMultiset<E> headMultiset(E var1, BoundType var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      return this;
   }

   public ImmutableSortedMultiset<E> tailMultiset(E var1, BoundType var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      return this;
   }

   public UnmodifiableIterator<E> iterator() {
      return Iterators.emptyIterator();
   }

   public boolean equals(@Nullable Object var1) {
      if(var1 instanceof Multiset) {
         Multiset var2 = (Multiset)var1;
         return var2.isEmpty();
      } else {
         return false;
      }
   }

   boolean isPartialView() {
      return false;
   }

   int copyIntoArray(Object[] var1, int var2) {
      return var2;
   }

   public ImmutableList<E> asList() {
      return ImmutableList.of();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedMultiset tailMultiset(Object var1, BoundType var2) {
      return this.tailMultiset(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedMultiset headMultiset(Object var1, BoundType var2) {
      return this.headMultiset(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator iterator() {
      return this.iterator();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public NavigableSet elementSet() {
      return this.elementSet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedSet elementSet() {
      return this.elementSet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set elementSet() {
      return this.elementSet();
   }
}
