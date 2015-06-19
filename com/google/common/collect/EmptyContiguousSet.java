package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.BoundType;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.EmptyImmutableSortedSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class EmptyContiguousSet<C extends Comparable> extends ContiguousSet<C> {
   EmptyContiguousSet(DiscreteDomain<C> var1) {
      super(var1);
   }

   public C first() {
      throw new NoSuchElementException();
   }

   public C last() {
      throw new NoSuchElementException();
   }

   public int size() {
      return 0;
   }

   public ContiguousSet<C> intersection(ContiguousSet<C> var1) {
      return this;
   }

   public Range<C> range() {
      throw new NoSuchElementException();
   }

   public Range<C> range(BoundType var1, BoundType var2) {
      throw new NoSuchElementException();
   }

   ContiguousSet<C> headSetImpl(C var1, boolean var2) {
      return this;
   }

   ContiguousSet<C> subSetImpl(C var1, boolean var2, C var3, boolean var4) {
      return this;
   }

   ContiguousSet<C> tailSetImpl(C var1, boolean var2) {
      return this;
   }

   @GwtIncompatible("not used by GWT emulation")
   int indexOf(Object var1) {
      return -1;
   }

   public UnmodifiableIterator<C> iterator() {
      return Iterators.emptyIterator();
   }

   @GwtIncompatible("NavigableSet")
   public UnmodifiableIterator<C> descendingIterator() {
      return Iterators.emptyIterator();
   }

   boolean isPartialView() {
      return false;
   }

   public boolean isEmpty() {
      return true;
   }

   public ImmutableList<C> asList() {
      return ImmutableList.of();
   }

   public String toString() {
      return "[]";
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

   @GwtIncompatible("serialization")
   Object writeReplace() {
      return new EmptyContiguousSet.SerializedForm(this.domain);
   }

   @GwtIncompatible("NavigableSet")
   ImmutableSortedSet<C> createDescendingSet() {
      return new EmptyImmutableSortedSet(Ordering.natural().reverse());
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object last() {
      return this.last();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object first() {
      return this.first();
   }

   // $FF: synthetic method
   // $FF: bridge method
   ImmutableSortedSet tailSetImpl(Object var1, boolean var2) {
      return this.tailSetImpl((Comparable)var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   ImmutableSortedSet subSetImpl(Object var1, boolean var2, Object var3, boolean var4) {
      return this.subSetImpl((Comparable)var1, var2, (Comparable)var3, var4);
   }

   // $FF: synthetic method
   // $FF: bridge method
   ImmutableSortedSet headSetImpl(Object var1, boolean var2) {
      return this.headSetImpl((Comparable)var1, var2);
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

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   @GwtIncompatible("serialization")
   private static final class SerializedForm<C extends Comparable> implements Serializable {
      private final DiscreteDomain<C> domain;
      private static final long serialVersionUID = 0L;

      private SerializedForm(DiscreteDomain<C> var1) {
         this.domain = var1;
      }

      private Object readResolve() {
         return new EmptyContiguousSet(this.domain);
      }

      // $FF: synthetic method
      SerializedForm(DiscreteDomain var1, EmptyContiguousSet.SyntheticClass_1 var2) {
         this(var1);
      }
   }
}
