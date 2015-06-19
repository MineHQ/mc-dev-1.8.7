package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Predicate;
import com.google.common.collect.FilteredKeyMultimap;
import com.google.common.collect.FilteredSetMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible
final class FilteredKeySetMultimap<K, V> extends FilteredKeyMultimap<K, V> implements FilteredSetMultimap<K, V> {
   FilteredKeySetMultimap(SetMultimap<K, V> var1, Predicate<? super K> var2) {
      super(var1, var2);
   }

   public SetMultimap<K, V> unfiltered() {
      return (SetMultimap)this.unfiltered;
   }

   public Set<V> get(K var1) {
      return (Set)super.get(var1);
   }

   public Set<V> removeAll(Object var1) {
      return (Set)super.removeAll(var1);
   }

   public Set<V> replaceValues(K var1, Iterable<? extends V> var2) {
      return (Set)super.replaceValues(var1, var2);
   }

   public Set<Entry<K, V>> entries() {
      return (Set)super.entries();
   }

   Set<Entry<K, V>> createEntries() {
      return new FilteredKeySetMultimap.EntrySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Collection createEntries() {
      return this.createEntries();
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
   public Multimap unfiltered() {
      return this.unfiltered();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection entries() {
      return this.entries();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   class EntrySet extends FilteredKeyMultimap<K, V>.Entries implements Set<Entry<K, V>> {
      EntrySet() {
         super();
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }

      public boolean equals(@Nullable Object var1) {
         return Sets.equalsImpl(this, var1);
      }
   }
}
