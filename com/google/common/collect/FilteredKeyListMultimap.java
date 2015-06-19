package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Predicate;
import com.google.common.collect.FilteredKeyMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
final class FilteredKeyListMultimap<K, V> extends FilteredKeyMultimap<K, V> implements ListMultimap<K, V> {
   FilteredKeyListMultimap(ListMultimap<K, V> var1, Predicate<? super K> var2) {
      super(var1, var2);
   }

   public ListMultimap<K, V> unfiltered() {
      return (ListMultimap)super.unfiltered();
   }

   public List<V> get(K var1) {
      return (List)super.get(var1);
   }

   public List<V> removeAll(@Nullable Object var1) {
      return (List)super.removeAll(var1);
   }

   public List<V> replaceValues(K var1, Iterable<? extends V> var2) {
      return (List)super.replaceValues(var1, var2);
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
   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }
}
