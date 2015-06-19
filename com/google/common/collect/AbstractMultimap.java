package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultimap<K, V> implements Multimap<K, V> {
   private transient Collection<Entry<K, V>> entries;
   private transient Set<K> keySet;
   private transient Multiset<K> keys;
   private transient Collection<V> values;
   private transient Map<K, Collection<V>> asMap;

   AbstractMultimap() {
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean containsValue(@Nullable Object var1) {
      Iterator var2 = this.asMap().values().iterator();

      Collection var3;
      do {
         if(!var2.hasNext()) {
            return false;
         }

         var3 = (Collection)var2.next();
      } while(!var3.contains(var1));

      return true;
   }

   public boolean containsEntry(@Nullable Object var1, @Nullable Object var2) {
      Collection var3 = (Collection)this.asMap().get(var1);
      return var3 != null && var3.contains(var2);
   }

   public boolean remove(@Nullable Object var1, @Nullable Object var2) {
      Collection var3 = (Collection)this.asMap().get(var1);
      return var3 != null && var3.remove(var2);
   }

   public boolean put(@Nullable K var1, @Nullable V var2) {
      return this.get(var1).add(var2);
   }

   public boolean putAll(@Nullable K var1, Iterable<? extends V> var2) {
      Preconditions.checkNotNull(var2);
      if(var2 instanceof Collection) {
         Collection var4 = (Collection)var2;
         return !var4.isEmpty() && this.get(var1).addAll(var4);
      } else {
         Iterator var3 = var2.iterator();
         return var3.hasNext() && Iterators.addAll(this.get(var1), var3);
      }
   }

   public boolean putAll(Multimap<? extends K, ? extends V> var1) {
      boolean var2 = false;

      Entry var4;
      for(Iterator var3 = var1.entries().iterator(); var3.hasNext(); var2 |= this.put(var4.getKey(), var4.getValue())) {
         var4 = (Entry)var3.next();
      }

      return var2;
   }

   public Collection<V> replaceValues(@Nullable K var1, Iterable<? extends V> var2) {
      Preconditions.checkNotNull(var2);
      Collection var3 = this.removeAll(var1);
      this.putAll(var1, var2);
      return var3;
   }

   public Collection<Entry<K, V>> entries() {
      Collection var1 = this.entries;
      return var1 == null?(this.entries = this.createEntries()):var1;
   }

   Collection<Entry<K, V>> createEntries() {
      return (Collection)(this instanceof SetMultimap?new AbstractMultimap.EntrySet():new AbstractMultimap.Entries());
   }

   abstract Iterator<Entry<K, V>> entryIterator();

   public Set<K> keySet() {
      Set var1 = this.keySet;
      return var1 == null?(this.keySet = this.createKeySet()):var1;
   }

   Set<K> createKeySet() {
      return new Maps.KeySet(this.asMap());
   }

   public Multiset<K> keys() {
      Multiset var1 = this.keys;
      return var1 == null?(this.keys = this.createKeys()):var1;
   }

   Multiset<K> createKeys() {
      return new Multimaps.Keys(this);
   }

   public Collection<V> values() {
      Collection var1 = this.values;
      return var1 == null?(this.values = this.createValues()):var1;
   }

   Collection<V> createValues() {
      return new AbstractMultimap.Values();
   }

   Iterator<V> valueIterator() {
      return Maps.valueIterator(this.entries().iterator());
   }

   public Map<K, Collection<V>> asMap() {
      Map var1 = this.asMap;
      return var1 == null?(this.asMap = this.createAsMap()):var1;
   }

   abstract Map<K, Collection<V>> createAsMap();

   public boolean equals(@Nullable Object var1) {
      return Multimaps.equalsImpl(this, var1);
   }

   public int hashCode() {
      return this.asMap().hashCode();
   }

   public String toString() {
      return this.asMap().toString();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   class Values extends AbstractCollection<V> {
      Values() {
      }

      public Iterator<V> iterator() {
         return AbstractMultimap.this.valueIterator();
      }

      public int size() {
         return AbstractMultimap.this.size();
      }

      public boolean contains(@Nullable Object var1) {
         return AbstractMultimap.this.containsValue(var1);
      }

      public void clear() {
         AbstractMultimap.this.clear();
      }
   }

   private class EntrySet extends AbstractMultimap<K, V>.Entries implements Set<Entry<K, V>> {
      private EntrySet() {
         super();
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }

      public boolean equals(@Nullable Object var1) {
         return Sets.equalsImpl(this, var1);
      }

      // $FF: synthetic method
      EntrySet(AbstractMultimap.SyntheticClass_1 var2) {
         this();
      }
   }

   private class Entries extends Multimaps.Entries<K, V> {
      private Entries() {
      }

      Multimap<K, V> multimap() {
         return AbstractMultimap.this;
      }

      public Iterator<Entry<K, V>> iterator() {
         return AbstractMultimap.this.entryIterator();
      }

      // $FF: synthetic method
      Entries(AbstractMultimap.SyntheticClass_1 var2) {
         this();
      }
   }
}
