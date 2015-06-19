package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.BoundType;
import com.google.common.collect.DescendingImmutableSortedMultiset;
import com.google.common.collect.EmptyImmutableSortedMultiset;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSortedMultisetFauxverideShim;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.RegularImmutableSortedMultiset;
import com.google.common.collect.RegularImmutableSortedSet;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

@Beta
@GwtIncompatible("hasn\'t been tested yet")
public abstract class ImmutableSortedMultiset<E> extends ImmutableSortedMultisetFauxverideShim<E> implements SortedMultiset<E> {
   private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
   private static final ImmutableSortedMultiset<Comparable> NATURAL_EMPTY_MULTISET;
   transient ImmutableSortedMultiset<E> descendingMultiset;

   public static <E> ImmutableSortedMultiset<E> of() {
      return NATURAL_EMPTY_MULTISET;
   }

   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E var0) {
      RegularImmutableSortedSet var1 = (RegularImmutableSortedSet)ImmutableSortedSet.of(var0);
      int[] var2 = new int[]{1};
      long[] var3 = new long[]{0L, 1L};
      return new RegularImmutableSortedMultiset(var1, var2, var3, 0, 1);
   }

   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E var0, E var1) {
      return copyOf(Ordering.natural(), (Iterable)Arrays.asList(new Comparable[]{var0, var1}));
   }

   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E var0, E var1, E var2) {
      return copyOf(Ordering.natural(), (Iterable)Arrays.asList(new Comparable[]{var0, var1, var2}));
   }

   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E var0, E var1, E var2, E var3) {
      return copyOf(Ordering.natural(), (Iterable)Arrays.asList(new Comparable[]{var0, var1, var2, var3}));
   }

   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E var0, E var1, E var2, E var3, E var4) {
      return copyOf(Ordering.natural(), (Iterable)Arrays.asList(new Comparable[]{var0, var1, var2, var3, var4}));
   }

   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E var0, E var1, E var2, E var3, E var4, E var5, E... var6) {
      int var7 = var6.length + 6;
      ArrayList var8 = Lists.newArrayListWithCapacity(var7);
      Collections.addAll(var8, new Comparable[]{var0, var1, var2, var3, var4, var5});
      Collections.addAll(var8, var6);
      return copyOf(Ordering.natural(), (Iterable)var8);
   }

   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> copyOf(E[] var0) {
      return copyOf(Ordering.natural(), (Iterable)Arrays.asList(var0));
   }

   public static <E> ImmutableSortedMultiset<E> copyOf(Iterable<? extends E> var0) {
      Ordering var1 = Ordering.natural();
      return copyOf(var1, (Iterable)var0);
   }

   public static <E> ImmutableSortedMultiset<E> copyOf(Iterator<? extends E> var0) {
      Ordering var1 = Ordering.natural();
      return copyOf(var1, (Iterator)var0);
   }

   public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> var0, Iterator<? extends E> var1) {
      Preconditions.checkNotNull(var0);
      return (new ImmutableSortedMultiset.Builder(var0)).addAll(var1).build();
   }

   public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> var0, Iterable<? extends E> var1) {
      if(var1 instanceof ImmutableSortedMultiset) {
         ImmutableSortedMultiset var2 = (ImmutableSortedMultiset)var1;
         if(var0.equals(var2.comparator())) {
            if(var2.isPartialView()) {
               return copyOfSortedEntries(var0, var2.entrySet().asList());
            }

            return var2;
         }
      }

      ArrayList var3 = Lists.newArrayList(var1);
      TreeMultiset var4 = TreeMultiset.create((Comparator)Preconditions.checkNotNull(var0));
      Iterables.addAll(var4, var3);
      return copyOfSortedEntries(var0, var4.entrySet());
   }

   public static <E> ImmutableSortedMultiset<E> copyOfSorted(SortedMultiset<E> var0) {
      return copyOfSortedEntries(var0.comparator(), Lists.newArrayList((Iterable)var0.entrySet()));
   }

   private static <E> ImmutableSortedMultiset<E> copyOfSortedEntries(Comparator<? super E> var0, Collection<Multiset.Entry<E>> var1) {
      if(var1.isEmpty()) {
         return emptyMultiset(var0);
      } else {
         ImmutableList.Builder var2 = new ImmutableList.Builder(var1.size());
         int[] var3 = new int[var1.size()];
         long[] var4 = new long[var1.size() + 1];
         int var5 = 0;

         for(Iterator var6 = var1.iterator(); var6.hasNext(); ++var5) {
            Multiset.Entry var7 = (Multiset.Entry)var6.next();
            var2.add(var7.getElement());
            var3[var5] = var7.getCount();
            var4[var5 + 1] = var4[var5] + (long)var3[var5];
         }

         return new RegularImmutableSortedMultiset(new RegularImmutableSortedSet(var2.build(), var0), var3, var4, 0, var1.size());
      }
   }

   static <E> ImmutableSortedMultiset<E> emptyMultiset(Comparator<? super E> var0) {
      return (ImmutableSortedMultiset)(NATURAL_ORDER.equals(var0)?NATURAL_EMPTY_MULTISET:new EmptyImmutableSortedMultiset(var0));
   }

   ImmutableSortedMultiset() {
   }

   public final Comparator<? super E> comparator() {
      return this.elementSet().comparator();
   }

   public abstract ImmutableSortedSet<E> elementSet();

   public ImmutableSortedMultiset<E> descendingMultiset() {
      ImmutableSortedMultiset var1 = this.descendingMultiset;
      return var1 == null?(this.descendingMultiset = new DescendingImmutableSortedMultiset(this)):var1;
   }

   /** @deprecated */
   @Deprecated
   public final Multiset.Entry<E> pollFirstEntry() {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final Multiset.Entry<E> pollLastEntry() {
      throw new UnsupportedOperationException();
   }

   public abstract ImmutableSortedMultiset<E> headMultiset(E var1, BoundType var2);

   public ImmutableSortedMultiset<E> subMultiset(E var1, BoundType var2, E var3, BoundType var4) {
      Preconditions.checkArgument(this.comparator().compare(var1, var3) <= 0, "Expected lowerBound <= upperBound but %s > %s", new Object[]{var1, var3});
      return this.tailMultiset(var1, var2).headMultiset(var3, var4);
   }

   public abstract ImmutableSortedMultiset<E> tailMultiset(E var1, BoundType var2);

   public static <E> ImmutableSortedMultiset.Builder<E> orderedBy(Comparator<E> var0) {
      return new ImmutableSortedMultiset.Builder(var0);
   }

   public static <E extends Comparable<E>> ImmutableSortedMultiset.Builder<E> reverseOrder() {
      return new ImmutableSortedMultiset.Builder(Ordering.natural().reverse());
   }

   public static <E extends Comparable<E>> ImmutableSortedMultiset.Builder<E> naturalOrder() {
      return new ImmutableSortedMultiset.Builder(Ordering.natural());
   }

   Object writeReplace() {
      return new ImmutableSortedMultiset.SerializedForm(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set elementSet() {
      return this.elementSet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedMultiset tailMultiset(Object var1, BoundType var2) {
      return this.tailMultiset(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedMultiset subMultiset(Object var1, BoundType var2, Object var3, BoundType var4) {
      return this.subMultiset(var1, var2, var3, var4);
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

   static {
      NATURAL_EMPTY_MULTISET = new EmptyImmutableSortedMultiset(NATURAL_ORDER);
   }

   private static final class SerializedForm<E> implements Serializable {
      Comparator<? super E> comparator;
      E[] elements;
      int[] counts;

      SerializedForm(SortedMultiset<E> var1) {
         this.comparator = var1.comparator();
         int var2 = var1.entrySet().size();
         this.elements = (Object[])(new Object[var2]);
         this.counts = new int[var2];
         int var3 = 0;

         for(Iterator var4 = var1.entrySet().iterator(); var4.hasNext(); ++var3) {
            Multiset.Entry var5 = (Multiset.Entry)var4.next();
            this.elements[var3] = var5.getElement();
            this.counts[var3] = var5.getCount();
         }

      }

      Object readResolve() {
         int var1 = this.elements.length;
         ImmutableSortedMultiset.Builder var2 = new ImmutableSortedMultiset.Builder(this.comparator);

         for(int var3 = 0; var3 < var1; ++var3) {
            var2.addCopies(this.elements[var3], this.counts[var3]);
         }

         return var2.build();
      }
   }

   public static class Builder<E> extends ImmutableMultiset.Builder<E> {
      public Builder(Comparator<? super E> var1) {
         super(TreeMultiset.create((Comparator)Preconditions.checkNotNull(var1)));
      }

      public ImmutableSortedMultiset.Builder<E> add(E var1) {
         super.add(var1);
         return this;
      }

      public ImmutableSortedMultiset.Builder<E> addCopies(E var1, int var2) {
         super.addCopies(var1, var2);
         return this;
      }

      public ImmutableSortedMultiset.Builder<E> setCount(E var1, int var2) {
         super.setCount(var1, var2);
         return this;
      }

      public ImmutableSortedMultiset.Builder<E> add(E... var1) {
         super.add(var1);
         return this;
      }

      public ImmutableSortedMultiset.Builder<E> addAll(Iterable<? extends E> var1) {
         super.addAll(var1);
         return this;
      }

      public ImmutableSortedMultiset.Builder<E> addAll(Iterator<? extends E> var1) {
         super.addAll(var1);
         return this;
      }

      public ImmutableSortedMultiset<E> build() {
         return ImmutableSortedMultiset.copyOfSorted((SortedMultiset)this.contents);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultiset build() {
         return this.build();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultiset.Builder addAll(Iterator var1) {
         return this.addAll(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultiset.Builder addAll(Iterable var1) {
         return this.addAll(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultiset.Builder add(Object[] var1) {
         return this.add(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultiset.Builder setCount(Object var1, int var2) {
         return this.setCount(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultiset.Builder addCopies(Object var1, int var2) {
         return this.addCopies(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultiset.Builder add(Object var1) {
         return this.add(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableCollection build() {
         return this.build();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableCollection.Builder addAll(Iterator var1) {
         return this.addAll(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableCollection.Builder addAll(Iterable var1) {
         return this.addAll(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableCollection.Builder add(Object[] var1) {
         return this.add(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableCollection.Builder add(Object var1) {
         return this.add(var1);
      }
   }
}
