package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.AbstractMapBasedMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractListMultimap<K, V> extends AbstractMapBasedMultimap<K, V> implements ListMultimap<K, V> {
   private static final long serialVersionUID = 6588350623831699109L;

   protected AbstractListMultimap(Map<K, Collection<V>> var1) {
      super(var1);
   }

   abstract List<V> createCollection();

   List<V> createUnmodifiableEmptyCollection() {
      return ImmutableList.of();
   }

   public List<V> get(@Nullable K var1) {
      return (List)super.get(var1);
   }

   public List<V> removeAll(@Nullable Object var1) {
      return (List)super.removeAll(var1);
   }

   public List<V> replaceValues(@Nullable K var1, Iterable<? extends V> var2) {
      return (List)super.replaceValues(var1, var2);
   }

   public boolean put(@Nullable K var1, @Nullable V var2) {
      return super.put(var1, var2);
   }

   public Map<K, Collection<V>> asMap() {
      return super.asMap();
   }

   public boolean equals(@Nullable Object var1) {
      return super.equals(var1);
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
