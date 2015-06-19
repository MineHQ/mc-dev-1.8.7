package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Predicate;
import com.google.common.collect.FilteredEntryMultimap;
import com.google.common.collect.FilteredSetMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;

@GwtCompatible
final class FilteredEntrySetMultimap<K, V> extends FilteredEntryMultimap<K, V> implements FilteredSetMultimap<K, V> {
   FilteredEntrySetMultimap(SetMultimap<K, V> var1, Predicate<? super Entry<K, V>> var2) {
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

   Set<Entry<K, V>> createEntries() {
      return Sets.filter(this.unfiltered().entries(), this.entryPredicate());
   }

   public Set<Entry<K, V>> entries() {
      return (Set)super.entries();
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
}
