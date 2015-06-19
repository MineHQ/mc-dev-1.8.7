package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapBasedMultimap;
import com.google.common.collect.AbstractMultimap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.Ordering;
import com.google.common.collect.Serialization;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public abstract class ImmutableMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable {
   final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
   final transient int size;
   private static final long serialVersionUID = 0L;

   public static <K, V> ImmutableMultimap<K, V> of() {
      return ImmutableListMultimap.of();
   }

   public static <K, V> ImmutableMultimap<K, V> of(K var0, V var1) {
      return ImmutableListMultimap.of(var0, var1);
   }

   public static <K, V> ImmutableMultimap<K, V> of(K var0, V var1, K var2, V var3) {
      return ImmutableListMultimap.of(var0, var1, var2, var3);
   }

   public static <K, V> ImmutableMultimap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5) {
      return ImmutableListMultimap.of(var0, var1, var2, var3, var4, var5);
   }

   public static <K, V> ImmutableMultimap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5, K var6, V var7) {
      return ImmutableListMultimap.of(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   public static <K, V> ImmutableMultimap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5, K var6, V var7, K var8, V var9) {
      return ImmutableListMultimap.of(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public static <K, V> ImmutableMultimap.Builder<K, V> builder() {
      return new ImmutableMultimap.Builder();
   }

   public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> var0) {
      if(var0 instanceof ImmutableMultimap) {
         ImmutableMultimap var1 = (ImmutableMultimap)var0;
         if(!var1.isPartialView()) {
            return var1;
         }
      }

      return ImmutableListMultimap.copyOf(var0);
   }

   ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> var1, int var2) {
      this.map = var1;
      this.size = var2;
   }

   /** @deprecated */
   @Deprecated
   public ImmutableCollection<V> removeAll(Object var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public ImmutableCollection<V> replaceValues(K var1, Iterable<? extends V> var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public void clear() {
      throw new UnsupportedOperationException();
   }

   public abstract ImmutableCollection<V> get(K var1);

   public abstract ImmutableMultimap<V, K> inverse();

   /** @deprecated */
   @Deprecated
   public boolean put(K var1, V var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public boolean putAll(K var1, Iterable<? extends V> var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public boolean putAll(Multimap<? extends K, ? extends V> var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public boolean remove(Object var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   boolean isPartialView() {
      return this.map.isPartialView();
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.map.containsKey(var1);
   }

   public boolean containsValue(@Nullable Object var1) {
      return var1 != null && super.containsValue(var1);
   }

   public int size() {
      return this.size;
   }

   public ImmutableSet<K> keySet() {
      return this.map.keySet();
   }

   public ImmutableMap<K, Collection<V>> asMap() {
      return this.map;
   }

   Map<K, Collection<V>> createAsMap() {
      throw new AssertionError("should never be called");
   }

   public ImmutableCollection<Entry<K, V>> entries() {
      return (ImmutableCollection)super.entries();
   }

   ImmutableCollection<Entry<K, V>> createEntries() {
      return new ImmutableMultimap.EntryCollection(this);
   }

   UnmodifiableIterator<Entry<K, V>> entryIterator() {
      return new ImmutableMultimap.Itr(null) {
         Entry<K, V> output(K var1, V var2) {
            return Maps.immutableEntry(var1, var2);
         }

         // $FF: synthetic method
         // $FF: bridge method
         Object output(Object var1, Object var2) {
            return this.output(var1, var2);
         }
      };
   }

   public ImmutableMultiset<K> keys() {
      return (ImmutableMultiset)super.keys();
   }

   ImmutableMultiset<K> createKeys() {
      return new ImmutableMultimap.Keys();
   }

   public ImmutableCollection<V> values() {
      return (ImmutableCollection)super.values();
   }

   ImmutableCollection<V> createValues() {
      return new ImmutableMultimap.Values(this);
   }

   UnmodifiableIterator<V> valueIterator() {
      return new ImmutableMultimap.Itr(null) {
         V output(K var1, V var2) {
            return var2;
         }
      };
   }

   // $FF: synthetic method
   // $FF: bridge method
   public String toString() {
      return super.toString();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map asMap() {
      return this.asMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Iterator valueIterator() {
      return this.valueIterator();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Collection createValues() {
      return this.createValues();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection values() {
      return this.values();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Multiset createKeys() {
      return this.createKeys();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Multiset keys() {
      return this.keys();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set keySet() {
      return this.keySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Iterator entryIterator() {
      return this.entryIterator();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Collection createEntries() {
      return this.createEntries();
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

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsEntry(Object var1, Object var2) {
      return super.containsEntry(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean isEmpty() {
      return super.isEmpty();
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

   private static final class Values<K, V> extends ImmutableCollection<V> {
      private final transient ImmutableMultimap<K, V> multimap;
      private static final long serialVersionUID = 0L;

      Values(ImmutableMultimap<K, V> var1) {
         this.multimap = var1;
      }

      public boolean contains(@Nullable Object var1) {
         return this.multimap.containsValue(var1);
      }

      public UnmodifiableIterator<V> iterator() {
         return this.multimap.valueIterator();
      }

      @GwtIncompatible("not present in emulated superclass")
      int copyIntoArray(Object[] var1, int var2) {
         ImmutableCollection var4;
         for(Iterator var3 = this.multimap.map.values().iterator(); var3.hasNext(); var2 = var4.copyIntoArray(var1, var2)) {
            var4 = (ImmutableCollection)var3.next();
         }

         return var2;
      }

      public int size() {
         return this.multimap.size();
      }

      boolean isPartialView() {
         return true;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Iterator iterator() {
         return this.iterator();
      }
   }

   class Keys extends ImmutableMultiset<K> {
      Keys() {
      }

      public boolean contains(@Nullable Object var1) {
         return ImmutableMultimap.this.containsKey(var1);
      }

      public int count(@Nullable Object var1) {
         Collection var2 = (Collection)ImmutableMultimap.this.map.get(var1);
         return var2 == null?0:var2.size();
      }

      public Set<K> elementSet() {
         return ImmutableMultimap.this.keySet();
      }

      public int size() {
         return ImmutableMultimap.this.size();
      }

      Multiset.Entry<K> getEntry(int var1) {
         Entry var2 = (Entry)ImmutableMultimap.this.map.entrySet().asList().get(var1);
         return Multisets.immutableEntry(var2.getKey(), ((Collection)var2.getValue()).size());
      }

      boolean isPartialView() {
         return true;
      }
   }

   private abstract class Itr<T> extends UnmodifiableIterator<T> {
      final Iterator<Entry<K, Collection<V>>> mapIterator;
      K key;
      Iterator<V> valueIterator;

      private Itr() {
         this.mapIterator = ImmutableMultimap.this.asMap().entrySet().iterator();
         this.key = null;
         this.valueIterator = Iterators.emptyIterator();
      }

      abstract T output(K var1, V var2);

      public boolean hasNext() {
         return this.mapIterator.hasNext() || this.valueIterator.hasNext();
      }

      public T next() {
         if(!this.valueIterator.hasNext()) {
            Entry var1 = (Entry)this.mapIterator.next();
            this.key = var1.getKey();
            this.valueIterator = ((Collection)var1.getValue()).iterator();
         }

         return this.output(this.key, this.valueIterator.next());
      }

      // $FF: synthetic method
      Itr(Object var2) {
         this();
      }
   }

   private static class EntryCollection<K, V> extends ImmutableCollection<Entry<K, V>> {
      final ImmutableMultimap<K, V> multimap;
      private static final long serialVersionUID = 0L;

      EntryCollection(ImmutableMultimap<K, V> var1) {
         this.multimap = var1;
      }

      public UnmodifiableIterator<Entry<K, V>> iterator() {
         return this.multimap.entryIterator();
      }

      boolean isPartialView() {
         return this.multimap.isPartialView();
      }

      public int size() {
         return this.multimap.size();
      }

      public boolean contains(Object var1) {
         if(var1 instanceof Entry) {
            Entry var2 = (Entry)var1;
            return this.multimap.containsEntry(var2.getKey(), var2.getValue());
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Iterator iterator() {
         return this.iterator();
      }
   }

   @GwtIncompatible("java serialization is not supported")
   static class FieldSettersHolder {
      static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
      static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
      static final Serialization.FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");

      FieldSettersHolder() {
      }
   }

   public static class Builder<K, V> {
      Multimap<K, V> builderMultimap = new ImmutableMultimap.BuilderMultimap();
      Comparator<? super K> keyComparator;
      Comparator<? super V> valueComparator;

      public Builder() {
      }

      public ImmutableMultimap.Builder<K, V> put(K var1, V var2) {
         CollectPreconditions.checkEntryNotNull(var1, var2);
         this.builderMultimap.put(var1, var2);
         return this;
      }

      public ImmutableMultimap.Builder<K, V> put(Entry<? extends K, ? extends V> var1) {
         return this.put(var1.getKey(), var1.getValue());
      }

      public ImmutableMultimap.Builder<K, V> putAll(K var1, Iterable<? extends V> var2) {
         if(var1 == null) {
            throw new NullPointerException("null key in entry: null=" + Iterables.toString(var2));
         } else {
            Collection var3 = this.builderMultimap.get(var1);
            Iterator var4 = var2.iterator();

            while(var4.hasNext()) {
               Object var5 = var4.next();
               CollectPreconditions.checkEntryNotNull(var1, var5);
               var3.add(var5);
            }

            return this;
         }
      }

      public ImmutableMultimap.Builder<K, V> putAll(K var1, V... var2) {
         return this.putAll(var1, (Iterable)Arrays.asList(var2));
      }

      public ImmutableMultimap.Builder<K, V> putAll(Multimap<? extends K, ? extends V> var1) {
         Iterator var2 = var1.asMap().entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            this.putAll(var3.getKey(), (Iterable)var3.getValue());
         }

         return this;
      }

      public ImmutableMultimap.Builder<K, V> orderKeysBy(Comparator<? super K> var1) {
         this.keyComparator = (Comparator)Preconditions.checkNotNull(var1);
         return this;
      }

      public ImmutableMultimap.Builder<K, V> orderValuesBy(Comparator<? super V> var1) {
         this.valueComparator = (Comparator)Preconditions.checkNotNull(var1);
         return this;
      }

      public ImmutableMultimap<K, V> build() {
         if(this.valueComparator != null) {
            Iterator var1 = this.builderMultimap.asMap().values().iterator();

            while(var1.hasNext()) {
               Collection var2 = (Collection)var1.next();
               List var3 = (List)var2;
               Collections.sort(var3, this.valueComparator);
            }
         }

         if(this.keyComparator != null) {
            ImmutableMultimap.BuilderMultimap var5 = new ImmutableMultimap.BuilderMultimap();
            ArrayList var6 = Lists.newArrayList((Iterable)this.builderMultimap.asMap().entrySet());
            Collections.sort(var6, Ordering.from(this.keyComparator).onKeys());
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               Entry var4 = (Entry)var7.next();
               var5.putAll(var4.getKey(), (Iterable)var4.getValue());
            }

            this.builderMultimap = var5;
         }

         return ImmutableMultimap.copyOf(this.builderMultimap);
      }
   }

   private static class BuilderMultimap<K, V> extends AbstractMapBasedMultimap<K, V> {
      private static final long serialVersionUID = 0L;

      BuilderMultimap() {
         super(new LinkedHashMap());
      }

      Collection<V> createCollection() {
         return Lists.newArrayList();
      }
   }
}
