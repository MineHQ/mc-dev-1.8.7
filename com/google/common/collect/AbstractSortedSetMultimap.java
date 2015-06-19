package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.AbstractSetMultimap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.SortedSetMultimap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractSortedSetMultimap<K, V> extends AbstractSetMultimap<K, V> implements SortedSetMultimap<K, V> {
   private static final long serialVersionUID = 430848587173315748L;

   protected AbstractSortedSetMultimap(Map<K, Collection<V>> var1) {
      super(var1);
   }

   abstract SortedSet<V> createCollection();

   SortedSet<V> createUnmodifiableEmptyCollection() {
      Comparator var1 = this.valueComparator();
      return (SortedSet)(var1 == null?Collections.unmodifiableSortedSet(this.createCollection()):ImmutableSortedSet.emptySet(this.valueComparator()));
   }

   public SortedSet<V> get(@Nullable K var1) {
      return (SortedSet)super.get(var1);
   }

   public SortedSet<V> removeAll(@Nullable Object var1) {
      return (SortedSet)super.removeAll(var1);
   }

   public SortedSet<V> replaceValues(@Nullable K var1, Iterable<? extends V> var2) {
      return (SortedSet)super.replaceValues(var1, var2);
   }

   public Map<K, Collection<V>> asMap() {
      return super.asMap();
   }

   public Collection<V> values() {
      return super.values();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set removeAll(Object var1) {
      return this.removeAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set get(Object var1) {
      return this.get(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   Set createUnmodifiableEmptyCollection() {
      return this.createUnmodifiableEmptyCollection();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Set createCollection() {
      return this.createCollection();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection get(Object var1) {
      return this.get(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection removeAll(Object var1) {
      return this.removeAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   Collection createCollection() {
      return this.createCollection();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Collection createUnmodifiableEmptyCollection() {
      return this.createUnmodifiableEmptyCollection();
   }
}
