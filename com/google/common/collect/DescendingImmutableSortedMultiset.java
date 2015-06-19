package com.google.common.collect;

import com.google.common.collect.BoundType;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

final class DescendingImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E> {
   private final transient ImmutableSortedMultiset<E> forward;

   DescendingImmutableSortedMultiset(ImmutableSortedMultiset<E> var1) {
      this.forward = var1;
   }

   public int count(@Nullable Object var1) {
      return this.forward.count(var1);
   }

   public Multiset.Entry<E> firstEntry() {
      return this.forward.lastEntry();
   }

   public Multiset.Entry<E> lastEntry() {
      return this.forward.firstEntry();
   }

   public int size() {
      return this.forward.size();
   }

   public ImmutableSortedSet<E> elementSet() {
      return this.forward.elementSet().descendingSet();
   }

   Multiset.Entry<E> getEntry(int var1) {
      return (Multiset.Entry)this.forward.entrySet().asList().reverse().get(var1);
   }

   public ImmutableSortedMultiset<E> descendingMultiset() {
      return this.forward;
   }

   public ImmutableSortedMultiset<E> headMultiset(E var1, BoundType var2) {
      return this.forward.tailMultiset(var1, var2).descendingMultiset();
   }

   public ImmutableSortedMultiset<E> tailMultiset(E var1, BoundType var2) {
      return this.forward.headMultiset(var1, var2).descendingMultiset();
   }

   boolean isPartialView() {
      return this.forward.isPartialView();
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
   public SortedMultiset descendingMultiset() {
      return this.descendingMultiset();
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
