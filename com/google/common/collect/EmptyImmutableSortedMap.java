package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class EmptyImmutableSortedMap<K, V> extends ImmutableSortedMap<K, V> {
   private final transient ImmutableSortedSet<K> keySet;

   EmptyImmutableSortedMap(Comparator<? super K> var1) {
      this.keySet = ImmutableSortedSet.emptySet(var1);
   }

   EmptyImmutableSortedMap(Comparator<? super K> var1, ImmutableSortedMap<K, V> var2) {
      super(var2);
      this.keySet = ImmutableSortedSet.emptySet(var1);
   }

   public V get(@Nullable Object var1) {
      return null;
   }

   public ImmutableSortedSet<K> keySet() {
      return this.keySet;
   }

   public int size() {
      return 0;
   }

   public boolean isEmpty() {
      return true;
   }

   public ImmutableCollection<V> values() {
      return ImmutableList.of();
   }

   public String toString() {
      return "{}";
   }

   boolean isPartialView() {
      return false;
   }

   public ImmutableSet<Entry<K, V>> entrySet() {
      return ImmutableSet.of();
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      throw new AssertionError("should never be called");
   }

   public ImmutableSetMultimap<K, V> asMultimap() {
      return ImmutableSetMultimap.of();
   }

   public ImmutableSortedMap<K, V> headMap(K var1, boolean var2) {
      Preconditions.checkNotNull(var1);
      return this;
   }

   public ImmutableSortedMap<K, V> tailMap(K var1, boolean var2) {
      Preconditions.checkNotNull(var1);
      return this;
   }

   ImmutableSortedMap<K, V> createDescendingMap() {
      return new EmptyImmutableSortedMap(Ordering.from(this.comparator()).reverse(), this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public NavigableMap tailMap(Object var1, boolean var2) {
      return this.tailMap(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public NavigableMap headMap(Object var1, boolean var2) {
      return this.headMap(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set entrySet() {
      return this.entrySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection values() {
      return this.values();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set keySet() {
      return this.keySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ImmutableSet keySet() {
      return this.keySet();
   }
}
