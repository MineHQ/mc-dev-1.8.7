package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.RegularImmutableSortedSet;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class RegularImmutableSortedMap<K, V> extends ImmutableSortedMap<K, V> {
   private final transient RegularImmutableSortedSet<K> keySet;
   private final transient ImmutableList<V> valueList;

   RegularImmutableSortedMap(RegularImmutableSortedSet<K> var1, ImmutableList<V> var2) {
      this.keySet = var1;
      this.valueList = var2;
   }

   RegularImmutableSortedMap(RegularImmutableSortedSet<K> var1, ImmutableList<V> var2, ImmutableSortedMap<K, V> var3) {
      super(var3);
      this.keySet = var1;
      this.valueList = var2;
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      return new RegularImmutableSortedMap.EntrySet();
   }

   public ImmutableSortedSet<K> keySet() {
      return this.keySet;
   }

   public ImmutableCollection<V> values() {
      return this.valueList;
   }

   public V get(@Nullable Object var1) {
      int var2 = this.keySet.indexOf(var1);
      return var2 == -1?null:this.valueList.get(var2);
   }

   private ImmutableSortedMap<K, V> getSubMap(int var1, int var2) {
      return (ImmutableSortedMap)(var1 == 0 && var2 == this.size()?this:(var1 == var2?emptyMap(this.comparator()):from(this.keySet.getSubSet(var1, var2), this.valueList.subList(var1, var2))));
   }

   public ImmutableSortedMap<K, V> headMap(K var1, boolean var2) {
      return this.getSubMap(0, this.keySet.headIndex(Preconditions.checkNotNull(var1), var2));
   }

   public ImmutableSortedMap<K, V> tailMap(K var1, boolean var2) {
      return this.getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(var1), var2), this.size());
   }

   ImmutableSortedMap<K, V> createDescendingMap() {
      return new RegularImmutableSortedMap((RegularImmutableSortedSet)this.keySet.descendingSet(), this.valueList.reverse(), this);
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

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private class EntrySet extends ImmutableMapEntrySet<K, V> {
      private EntrySet() {
      }

      public UnmodifiableIterator<Entry<K, V>> iterator() {
         return this.asList().iterator();
      }

      ImmutableList<Entry<K, V>> createAsList() {
         return new ImmutableAsList() {
            private final ImmutableList<K> keyList = RegularImmutableSortedMap.this.keySet().asList();

            public Entry<K, V> get(int var1) {
               return Maps.immutableEntry(this.keyList.get(var1), RegularImmutableSortedMap.this.valueList.get(var1));
            }

            ImmutableCollection<Entry<K, V>> delegateCollection() {
               return EntrySet.this;
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object get(int var1) {
               return this.get(var1);
            }
         };
      }

      ImmutableMap<K, V> map() {
         return RegularImmutableSortedMap.this;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Iterator iterator() {
         return this.iterator();
      }

      // $FF: synthetic method
      EntrySet(RegularImmutableSortedMap.SyntheticClass_1 var2) {
         this();
      }
   }
}
