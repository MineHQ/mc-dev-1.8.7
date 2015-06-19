package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMultimap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
abstract class AbstractMapBasedMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable {
   private transient Map<K, Collection<V>> map;
   private transient int totalSize;
   private static final long serialVersionUID = 2447537837011683357L;

   protected AbstractMapBasedMultimap(Map<K, Collection<V>> var1) {
      Preconditions.checkArgument(var1.isEmpty());
      this.map = var1;
   }

   final void setMap(Map<K, Collection<V>> var1) {
      this.map = var1;
      this.totalSize = 0;

      Collection var3;
      for(Iterator var2 = var1.values().iterator(); var2.hasNext(); this.totalSize += var3.size()) {
         var3 = (Collection)var2.next();
         Preconditions.checkArgument(!var3.isEmpty());
      }

   }

   Collection<V> createUnmodifiableEmptyCollection() {
      return this.unmodifiableCollectionSubclass(this.createCollection());
   }

   abstract Collection<V> createCollection();

   Collection<V> createCollection(@Nullable K var1) {
      return this.createCollection();
   }

   Map<K, Collection<V>> backingMap() {
      return this.map;
   }

   public int size() {
      return this.totalSize;
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.map.containsKey(var1);
   }

   public boolean put(@Nullable K var1, @Nullable V var2) {
      Collection var3 = (Collection)this.map.get(var1);
      if(var3 == null) {
         var3 = this.createCollection(var1);
         if(var3.add(var2)) {
            ++this.totalSize;
            this.map.put(var1, var3);
            return true;
         } else {
            throw new AssertionError("New Collection violated the Collection spec");
         }
      } else if(var3.add(var2)) {
         ++this.totalSize;
         return true;
      } else {
         return false;
      }
   }

   private Collection<V> getOrCreateCollection(@Nullable K var1) {
      Collection var2 = (Collection)this.map.get(var1);
      if(var2 == null) {
         var2 = this.createCollection(var1);
         this.map.put(var1, var2);
      }

      return var2;
   }

   public Collection<V> replaceValues(@Nullable K var1, Iterable<? extends V> var2) {
      Iterator var3 = var2.iterator();
      if(!var3.hasNext()) {
         return this.removeAll(var1);
      } else {
         Collection var4 = this.getOrCreateCollection(var1);
         Collection var5 = this.createCollection();
         var5.addAll(var4);
         this.totalSize -= var4.size();
         var4.clear();

         while(var3.hasNext()) {
            if(var4.add(var3.next())) {
               ++this.totalSize;
            }
         }

         return this.unmodifiableCollectionSubclass(var5);
      }
   }

   public Collection<V> removeAll(@Nullable Object var1) {
      Collection var2 = (Collection)this.map.remove(var1);
      if(var2 == null) {
         return this.createUnmodifiableEmptyCollection();
      } else {
         Collection var3 = this.createCollection();
         var3.addAll(var2);
         this.totalSize -= var2.size();
         var2.clear();
         return this.unmodifiableCollectionSubclass(var3);
      }
   }

   Collection<V> unmodifiableCollectionSubclass(Collection<V> var1) {
      return (Collection)(var1 instanceof SortedSet?Collections.unmodifiableSortedSet((SortedSet)var1):(var1 instanceof Set?Collections.unmodifiableSet((Set)var1):(var1 instanceof List?Collections.unmodifiableList((List)var1):Collections.unmodifiableCollection(var1))));
   }

   public void clear() {
      Iterator var1 = this.map.values().iterator();

      while(var1.hasNext()) {
         Collection var2 = (Collection)var1.next();
         var2.clear();
      }

      this.map.clear();
      this.totalSize = 0;
   }

   public Collection<V> get(@Nullable K var1) {
      Collection var2 = (Collection)this.map.get(var1);
      if(var2 == null) {
         var2 = this.createCollection(var1);
      }

      return this.wrapCollection(var1, var2);
   }

   Collection<V> wrapCollection(@Nullable K var1, Collection<V> var2) {
      return (Collection)(var2 instanceof SortedSet?new AbstractMapBasedMultimap.WrappedSortedSet(var1, (SortedSet)var2, (AbstractMapBasedMultimap.WrappedCollection)null):(var2 instanceof Set?new AbstractMapBasedMultimap.WrappedSet(var1, (Set)var2):(var2 instanceof List?this.wrapList(var1, (List)var2, (AbstractMapBasedMultimap.WrappedCollection)null):new AbstractMapBasedMultimap.WrappedCollection(var1, var2, (AbstractMapBasedMultimap.WrappedCollection)null))));
   }

   private List<V> wrapList(@Nullable K var1, List<V> var2, @Nullable AbstractMapBasedMultimap<K, V>.WrappedCollection var3) {
      return (List)(var2 instanceof RandomAccess?new AbstractMapBasedMultimap.RandomAccessWrappedList(var1, var2, var3):new AbstractMapBasedMultimap.WrappedList(var1, var2, var3));
   }

   private Iterator<V> iteratorOrListIterator(Collection<V> var1) {
      return (Iterator)(var1 instanceof List?((List)var1).listIterator():var1.iterator());
   }

   Set<K> createKeySet() {
      return (Set)(this.map instanceof SortedMap?new AbstractMapBasedMultimap.SortedKeySet((SortedMap)this.map):new AbstractMapBasedMultimap.KeySet(this.map));
   }

   private int removeValuesForKey(Object var1) {
      Collection var2 = (Collection)Maps.safeRemove(this.map, var1);
      int var3 = 0;
      if(var2 != null) {
         var3 = var2.size();
         var2.clear();
         this.totalSize -= var3;
      }

      return var3;
   }

   public Collection<V> values() {
      return super.values();
   }

   Iterator<V> valueIterator() {
      return new AbstractMapBasedMultimap.Itr() {
         V output(K var1, V var2) {
            return var2;
         }
      };
   }

   public Collection<Entry<K, V>> entries() {
      return super.entries();
   }

   Iterator<Entry<K, V>> entryIterator() {
      return new AbstractMapBasedMultimap.Itr() {
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

   Map<K, Collection<V>> createAsMap() {
      return (Map)(this.map instanceof SortedMap?new AbstractMapBasedMultimap.SortedAsMap((SortedMap)this.map):new AbstractMapBasedMultimap.AsMap(this.map));
   }

   // $FF: synthetic method
   static int access$210(AbstractMapBasedMultimap var0) {
      return var0.totalSize--;
   }

   // $FF: synthetic method
   static int access$208(AbstractMapBasedMultimap var0) {
      return var0.totalSize++;
   }

   @GwtIncompatible("NavigableAsMap")
   class NavigableAsMap extends AbstractMapBasedMultimap<K, V>.SortedAsMap implements NavigableMap<K, Collection<V>> {
      NavigableAsMap(NavigableMap<K, Collection<V>> var1) {
         super();
      }

      NavigableMap<K, Collection<V>> sortedMap() {
         return (NavigableMap)super.sortedMap();
      }

      public Entry<K, Collection<V>> lowerEntry(K var1) {
         Entry var2 = this.sortedMap().lowerEntry(var1);
         return var2 == null?null:this.wrapEntry(var2);
      }

      public K lowerKey(K var1) {
         return this.sortedMap().lowerKey(var1);
      }

      public Entry<K, Collection<V>> floorEntry(K var1) {
         Entry var2 = this.sortedMap().floorEntry(var1);
         return var2 == null?null:this.wrapEntry(var2);
      }

      public K floorKey(K var1) {
         return this.sortedMap().floorKey(var1);
      }

      public Entry<K, Collection<V>> ceilingEntry(K var1) {
         Entry var2 = this.sortedMap().ceilingEntry(var1);
         return var2 == null?null:this.wrapEntry(var2);
      }

      public K ceilingKey(K var1) {
         return this.sortedMap().ceilingKey(var1);
      }

      public Entry<K, Collection<V>> higherEntry(K var1) {
         Entry var2 = this.sortedMap().higherEntry(var1);
         return var2 == null?null:this.wrapEntry(var2);
      }

      public K higherKey(K var1) {
         return this.sortedMap().higherKey(var1);
      }

      public Entry<K, Collection<V>> firstEntry() {
         Entry var1 = this.sortedMap().firstEntry();
         return var1 == null?null:this.wrapEntry(var1);
      }

      public Entry<K, Collection<V>> lastEntry() {
         Entry var1 = this.sortedMap().lastEntry();
         return var1 == null?null:this.wrapEntry(var1);
      }

      public Entry<K, Collection<V>> pollFirstEntry() {
         return this.pollAsMapEntry(this.entrySet().iterator());
      }

      public Entry<K, Collection<V>> pollLastEntry() {
         return this.pollAsMapEntry(this.descendingMap().entrySet().iterator());
      }

      Entry<K, Collection<V>> pollAsMapEntry(Iterator<Entry<K, Collection<V>>> var1) {
         if(!var1.hasNext()) {
            return null;
         } else {
            Entry var2 = (Entry)var1.next();
            Collection var3 = AbstractMapBasedMultimap.this.createCollection();
            var3.addAll((Collection)var2.getValue());
            var1.remove();
            return Maps.immutableEntry(var2.getKey(), AbstractMapBasedMultimap.this.unmodifiableCollectionSubclass(var3));
         }
      }

      public NavigableMap<K, Collection<V>> descendingMap() {
         return AbstractMapBasedMultimap.this.new NavigableAsMap(this.sortedMap().descendingMap());
      }

      public NavigableSet<K> keySet() {
         return (NavigableSet)super.keySet();
      }

      NavigableSet<K> createKeySet() {
         return AbstractMapBasedMultimap.this.new NavigableKeySet(this.sortedMap());
      }

      public NavigableSet<K> navigableKeySet() {
         return this.keySet();
      }

      public NavigableSet<K> descendingKeySet() {
         return this.descendingMap().navigableKeySet();
      }

      public NavigableMap<K, Collection<V>> subMap(K var1, K var2) {
         return this.subMap(var1, true, var2, false);
      }

      public NavigableMap<K, Collection<V>> subMap(K var1, boolean var2, K var3, boolean var4) {
         return AbstractMapBasedMultimap.this.new NavigableAsMap(this.sortedMap().subMap(var1, var2, var3, var4));
      }

      public NavigableMap<K, Collection<V>> headMap(K var1) {
         return this.headMap(var1, false);
      }

      public NavigableMap<K, Collection<V>> headMap(K var1, boolean var2) {
         return AbstractMapBasedMultimap.this.new NavigableAsMap(this.sortedMap().headMap(var1, var2));
      }

      public NavigableMap<K, Collection<V>> tailMap(K var1) {
         return this.tailMap(var1, true);
      }

      public NavigableMap<K, Collection<V>> tailMap(K var1, boolean var2) {
         return AbstractMapBasedMultimap.this.new NavigableAsMap(this.sortedMap().tailMap(var1, var2));
      }

      // $FF: synthetic method
      // $FF: bridge method
      SortedSet createKeySet() {
         return this.createKeySet();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public SortedSet keySet() {
         return this.keySet();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public SortedMap tailMap(Object var1) {
         return this.tailMap(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public SortedMap subMap(Object var1, Object var2) {
         return this.subMap(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public SortedMap headMap(Object var1) {
         return this.headMap(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      SortedMap sortedMap() {
         return this.sortedMap();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Set keySet() {
         return this.keySet();
      }

      // $FF: synthetic method
      // $FF: bridge method
      Set createKeySet() {
         return this.createKeySet();
      }
   }

   private class SortedAsMap extends AbstractMapBasedMultimap<K, V>.AsMap implements SortedMap<K, Collection<V>> {
      SortedSet<K> sortedKeySet;

      SortedAsMap(SortedMap<K, Collection<V>> var1) {
         super();
      }

      SortedMap<K, Collection<V>> sortedMap() {
         return (SortedMap)this.submap;
      }

      public Comparator<? super K> comparator() {
         return this.sortedMap().comparator();
      }

      public K firstKey() {
         return this.sortedMap().firstKey();
      }

      public K lastKey() {
         return this.sortedMap().lastKey();
      }

      public SortedMap<K, Collection<V>> headMap(K var1) {
         return AbstractMapBasedMultimap.this.new SortedAsMap(this.sortedMap().headMap(var1));
      }

      public SortedMap<K, Collection<V>> subMap(K var1, K var2) {
         return AbstractMapBasedMultimap.this.new SortedAsMap(this.sortedMap().subMap(var1, var2));
      }

      public SortedMap<K, Collection<V>> tailMap(K var1) {
         return AbstractMapBasedMultimap.this.new SortedAsMap(this.sortedMap().tailMap(var1));
      }

      public SortedSet<K> keySet() {
         SortedSet var1 = this.sortedKeySet;
         return var1 == null?(this.sortedKeySet = this.createKeySet()):var1;
      }

      SortedSet<K> createKeySet() {
         return AbstractMapBasedMultimap.this.new SortedKeySet(this.sortedMap());
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Set keySet() {
         return this.keySet();
      }

      // $FF: synthetic method
      // $FF: bridge method
      Set createKeySet() {
         return this.createKeySet();
      }
   }

   private class AsMap extends Maps.ImprovedAbstractMap<K, Collection<V>> {
      final transient Map<K, Collection<V>> submap;

      AsMap(Map<K, Collection<V>> var1) {
         this.submap = var2;
      }

      protected Set<Entry<K, Collection<V>>> createEntrySet() {
         return new AbstractMapBasedMultimap.AsMap.AsMap$AsMapEntries();
      }

      public boolean containsKey(Object var1) {
         return Maps.safeContainsKey(this.submap, var1);
      }

      public Collection<V> get(Object var1) {
         Collection var2 = (Collection)Maps.safeGet(this.submap, var1);
         return var2 == null?null:AbstractMapBasedMultimap.this.wrapCollection(var1, var2);
      }

      public Set<K> keySet() {
         return AbstractMapBasedMultimap.this.keySet();
      }

      public int size() {
         return this.submap.size();
      }

      public Collection<V> remove(Object var1) {
         Collection var2 = (Collection)this.submap.remove(var1);
         if(var2 == null) {
            return null;
         } else {
            Collection var3 = AbstractMapBasedMultimap.this.createCollection();
            var3.addAll(var2);
            AbstractMapBasedMultimap.this.totalSize = var2.size();
            var2.clear();
            return var3;
         }
      }

      public boolean equals(@Nullable Object var1) {
         return this == var1 || this.submap.equals(var1);
      }

      public int hashCode() {
         return this.submap.hashCode();
      }

      public String toString() {
         return this.submap.toString();
      }

      public void clear() {
         if(this.submap == AbstractMapBasedMultimap.this.map) {
            AbstractMapBasedMultimap.this.clear();
         } else {
            Iterators.clear(new AbstractMapBasedMultimap.AsMap.AsMap$AsMapIterator());
         }

      }

      Entry<K, Collection<V>> wrapEntry(Entry<K, Collection<V>> var1) {
         Object var2 = var1.getKey();
         return Maps.immutableEntry(var2, AbstractMapBasedMultimap.this.wrapCollection(var2, (Collection)var1.getValue()));
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object remove(Object var1) {
         return this.remove(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get(Object var1) {
         return this.get(var1);
      }

      class AsMap$AsMapIterator implements Iterator<Entry<K, Collection<V>>> {
         final Iterator<Entry<K, Collection<V>>> delegateIterator;
         Collection<V> collection;

         AsMap$AsMapIterator() {
            this.delegateIterator = AsMap.this.submap.entrySet().iterator();
         }

         public boolean hasNext() {
            return this.delegateIterator.hasNext();
         }

         public Entry<K, Collection<V>> next() {
            Entry var1 = (Entry)this.delegateIterator.next();
            this.collection = (Collection)var1.getValue();
            return AsMap.this.wrapEntry(var1);
         }

         public void remove() {
            this.delegateIterator.remove();
            AbstractMapBasedMultimap.this.totalSize = this.collection.size();
            this.collection.clear();
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object next() {
            return this.next();
         }
      }

      class AsMap$AsMapEntries extends Maps.EntrySet<K, Collection<V>> {
         AsMap$AsMapEntries() {
         }

         Map<K, Collection<V>> map() {
            return AsMap.this;
         }

         public Iterator<Entry<K, Collection<V>>> iterator() {
            return AsMap.this.new AsMap$AsMapIterator();
         }

         public boolean contains(Object var1) {
            return Collections2.safeContains(AsMap.this.submap.entrySet(), var1);
         }

         public boolean remove(Object var1) {
            if(!this.contains(var1)) {
               return false;
            } else {
               Entry var2 = (Entry)var1;
               AbstractMapBasedMultimap.this.removeValuesForKey(var2.getKey());
               return true;
            }
         }
      }
   }

   private abstract class Itr<T> implements Iterator<T> {
      final Iterator<Entry<K, Collection<V>>> keyIterator;
      K key;
      Collection<V> collection;
      Iterator<V> valueIterator;

      Itr() {
         this.keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator();
         this.key = null;
         this.collection = null;
         this.valueIterator = Iterators.emptyModifiableIterator();
      }

      abstract T output(K var1, V var2);

      public boolean hasNext() {
         return this.keyIterator.hasNext() || this.valueIterator.hasNext();
      }

      public T next() {
         if(!this.valueIterator.hasNext()) {
            Entry var1 = (Entry)this.keyIterator.next();
            this.key = var1.getKey();
            this.collection = (Collection)var1.getValue();
            this.valueIterator = this.collection.iterator();
         }

         return this.output(this.key, this.valueIterator.next());
      }

      public void remove() {
         this.valueIterator.remove();
         if(this.collection.isEmpty()) {
            this.keyIterator.remove();
         }

         AbstractMapBasedMultimap.access$210(AbstractMapBasedMultimap.this);
      }
   }

   @GwtIncompatible("NavigableSet")
   class NavigableKeySet extends AbstractMapBasedMultimap<K, V>.SortedKeySet implements NavigableSet<K> {
      NavigableKeySet(NavigableMap<K, Collection<V>> var1) {
         super();
      }

      NavigableMap<K, Collection<V>> sortedMap() {
         return (NavigableMap)super.sortedMap();
      }

      public K lower(K var1) {
         return this.sortedMap().lowerKey(var1);
      }

      public K floor(K var1) {
         return this.sortedMap().floorKey(var1);
      }

      public K ceiling(K var1) {
         return this.sortedMap().ceilingKey(var1);
      }

      public K higher(K var1) {
         return this.sortedMap().higherKey(var1);
      }

      public K pollFirst() {
         return Iterators.pollNext(this.iterator());
      }

      public K pollLast() {
         return Iterators.pollNext(this.descendingIterator());
      }

      public NavigableSet<K> descendingSet() {
         return AbstractMapBasedMultimap.this.new NavigableKeySet(this.sortedMap().descendingMap());
      }

      public Iterator<K> descendingIterator() {
         return this.descendingSet().iterator();
      }

      public NavigableSet<K> headSet(K var1) {
         return this.headSet(var1, false);
      }

      public NavigableSet<K> headSet(K var1, boolean var2) {
         return AbstractMapBasedMultimap.this.new NavigableKeySet(this.sortedMap().headMap(var1, var2));
      }

      public NavigableSet<K> subSet(K var1, K var2) {
         return this.subSet(var1, true, var2, false);
      }

      public NavigableSet<K> subSet(K var1, boolean var2, K var3, boolean var4) {
         return AbstractMapBasedMultimap.this.new NavigableKeySet(this.sortedMap().subMap(var1, var2, var3, var4));
      }

      public NavigableSet<K> tailSet(K var1) {
         return this.tailSet(var1, true);
      }

      public NavigableSet<K> tailSet(K var1, boolean var2) {
         return AbstractMapBasedMultimap.this.new NavigableKeySet(this.sortedMap().tailMap(var1, var2));
      }

      // $FF: synthetic method
      // $FF: bridge method
      public SortedSet tailSet(Object var1) {
         return this.tailSet(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public SortedSet subSet(Object var1, Object var2) {
         return this.subSet(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public SortedSet headSet(Object var1) {
         return this.headSet(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      SortedMap sortedMap() {
         return this.sortedMap();
      }
   }

   private class SortedKeySet extends AbstractMapBasedMultimap<K, V>.KeySet implements SortedSet<K> {
      SortedKeySet(SortedMap<K, Collection<V>> var1) {
         super();
      }

      SortedMap<K, Collection<V>> sortedMap() {
         return (SortedMap)super.map();
      }

      public Comparator<? super K> comparator() {
         return this.sortedMap().comparator();
      }

      public K first() {
         return this.sortedMap().firstKey();
      }

      public SortedSet<K> headSet(K var1) {
         return AbstractMapBasedMultimap.this.new SortedKeySet(this.sortedMap().headMap(var1));
      }

      public K last() {
         return this.sortedMap().lastKey();
      }

      public SortedSet<K> subSet(K var1, K var2) {
         return AbstractMapBasedMultimap.this.new SortedKeySet(this.sortedMap().subMap(var1, var2));
      }

      public SortedSet<K> tailSet(K var1) {
         return AbstractMapBasedMultimap.this.new SortedKeySet(this.sortedMap().tailMap(var1));
      }
   }

   private class KeySet extends Maps.KeySet<K, Collection<V>> {
      KeySet(Map<K, Collection<V>> var1) {
         super(var2);
      }

      public Iterator<K> iterator() {
         final Iterator var1 = this.map().entrySet().iterator();
         return new Iterator() {
            Entry<K, Collection<V>> entry;

            public boolean hasNext() {
               return var1.hasNext();
            }

            public K next() {
               this.entry = (Entry)var1.next();
               return this.entry.getKey();
            }

            public void remove() {
               CollectPreconditions.checkRemove(this.entry != null);
               Collection var1x = (Collection)this.entry.getValue();
               var1.remove();
               AbstractMapBasedMultimap.this.totalSize = var1x.size();
               var1x.clear();
            }
         };
      }

      public boolean remove(Object var1) {
         int var2 = 0;
         Collection var3 = (Collection)this.map().remove(var1);
         if(var3 != null) {
            var2 = var3.size();
            var3.clear();
            AbstractMapBasedMultimap.this.totalSize = var2;
         }

         return var2 > 0;
      }

      public void clear() {
         Iterators.clear(this.iterator());
      }

      public boolean containsAll(Collection<?> var1) {
         return this.map().keySet().containsAll(var1);
      }

      public boolean equals(@Nullable Object var1) {
         return this == var1 || this.map().keySet().equals(var1);
      }

      public int hashCode() {
         return this.map().keySet().hashCode();
      }
   }

   private class RandomAccessWrappedList extends AbstractMapBasedMultimap<K, V>.WrappedList implements RandomAccess {
      RandomAccessWrappedList(@Nullable K var1, List<V> var2, @Nullable AbstractMapBasedMultimap<K, V>.WrappedCollection var3) {
         super();
      }
   }

   private class WrappedList extends AbstractMapBasedMultimap<K, V>.WrappedCollection implements List<V> {
      WrappedList(@Nullable K var1, List<V> var2, @Nullable AbstractMapBasedMultimap<K, V>.WrappedCollection var3) {
         super();
      }

      List<V> getListDelegate() {
         return (List)this.getDelegate();
      }

      public boolean addAll(int var1, Collection<? extends V> var2) {
         if(var2.isEmpty()) {
            return false;
         } else {
            int var3 = this.size();
            boolean var4 = this.getListDelegate().addAll(var1, var2);
            if(var4) {
               int var5 = this.getDelegate().size();
               AbstractMapBasedMultimap.this.totalSize = var5 - var3;
               if(var3 == 0) {
                  this.addToMap();
               }
            }

            return var4;
         }
      }

      public V get(int var1) {
         this.refreshIfEmpty();
         return this.getListDelegate().get(var1);
      }

      public V set(int var1, V var2) {
         this.refreshIfEmpty();
         return this.getListDelegate().set(var1, var2);
      }

      public void add(int var1, V var2) {
         this.refreshIfEmpty();
         boolean var3 = this.getDelegate().isEmpty();
         this.getListDelegate().add(var1, var2);
         AbstractMapBasedMultimap.access$208(AbstractMapBasedMultimap.this);
         if(var3) {
            this.addToMap();
         }

      }

      public V remove(int var1) {
         this.refreshIfEmpty();
         Object var2 = this.getListDelegate().remove(var1);
         AbstractMapBasedMultimap.access$210(AbstractMapBasedMultimap.this);
         this.removeIfEmpty();
         return var2;
      }

      public int indexOf(Object var1) {
         this.refreshIfEmpty();
         return this.getListDelegate().indexOf(var1);
      }

      public int lastIndexOf(Object var1) {
         this.refreshIfEmpty();
         return this.getListDelegate().lastIndexOf(var1);
      }

      public ListIterator<V> listIterator() {
         this.refreshIfEmpty();
         return new AbstractMapBasedMultimap.WrappedList.WrappedList$WrappedListIterator();
      }

      public ListIterator<V> listIterator(int var1) {
         this.refreshIfEmpty();
         return new AbstractMapBasedMultimap.WrappedList.WrappedList$WrappedListIterator(var1);
      }

      public List<V> subList(int var1, int var2) {
         this.refreshIfEmpty();
         return AbstractMapBasedMultimap.this.wrapList(this.getKey(), this.getListDelegate().subList(var1, var2), (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null?this:this.getAncestor()));
      }

      private class WrappedList$WrappedListIterator extends AbstractMapBasedMultimap<K, V>.WrappedCollection.WrappedIterator implements ListIterator<V> {
         WrappedList$WrappedListIterator() {
            super();
         }

         public WrappedList$WrappedListIterator(int var2) {
            super();
         }

         private ListIterator<V> getDelegateListIterator() {
            return (ListIterator)this.getDelegateIterator();
         }

         public boolean hasPrevious() {
            return this.getDelegateListIterator().hasPrevious();
         }

         public V previous() {
            return this.getDelegateListIterator().previous();
         }

         public int nextIndex() {
            return this.getDelegateListIterator().nextIndex();
         }

         public int previousIndex() {
            return this.getDelegateListIterator().previousIndex();
         }

         public void set(V var1) {
            this.getDelegateListIterator().set(var1);
         }

         public void add(V var1) {
            boolean var2 = WrappedList.this.isEmpty();
            this.getDelegateListIterator().add(var1);
            AbstractMapBasedMultimap.access$208(AbstractMapBasedMultimap.this);
            if(var2) {
               WrappedList.this.addToMap();
            }

         }
      }
   }

   @GwtIncompatible("NavigableSet")
   class WrappedNavigableSet extends AbstractMapBasedMultimap<K, V>.WrappedSortedSet implements NavigableSet<V> {
      WrappedNavigableSet(@Nullable K var1, NavigableSet<V> var2, @Nullable AbstractMapBasedMultimap<K, V>.WrappedCollection var3) {
         super();
      }

      NavigableSet<V> getSortedSetDelegate() {
         return (NavigableSet)super.getSortedSetDelegate();
      }

      public V lower(V var1) {
         return this.getSortedSetDelegate().lower(var1);
      }

      public V floor(V var1) {
         return this.getSortedSetDelegate().floor(var1);
      }

      public V ceiling(V var1) {
         return this.getSortedSetDelegate().ceiling(var1);
      }

      public V higher(V var1) {
         return this.getSortedSetDelegate().higher(var1);
      }

      public V pollFirst() {
         return Iterators.pollNext(this.iterator());
      }

      public V pollLast() {
         return Iterators.pollNext(this.descendingIterator());
      }

      private NavigableSet<V> wrap(NavigableSet<V> var1) {
         return AbstractMapBasedMultimap.this.new WrappedNavigableSet(this.key, var1, (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null?this:this.getAncestor()));
      }

      public NavigableSet<V> descendingSet() {
         return this.wrap(this.getSortedSetDelegate().descendingSet());
      }

      public Iterator<V> descendingIterator() {
         return new AbstractMapBasedMultimap.WrappedCollection.WrappedCollection$WrappedIterator(this.getSortedSetDelegate().descendingIterator());
      }

      public NavigableSet<V> subSet(V var1, boolean var2, V var3, boolean var4) {
         return this.wrap(this.getSortedSetDelegate().subSet(var1, var2, var3, var4));
      }

      public NavigableSet<V> headSet(V var1, boolean var2) {
         return this.wrap(this.getSortedSetDelegate().headSet(var1, var2));
      }

      public NavigableSet<V> tailSet(V var1, boolean var2) {
         return this.wrap(this.getSortedSetDelegate().tailSet(var1, var2));
      }

      // $FF: synthetic method
      // $FF: bridge method
      SortedSet getSortedSetDelegate() {
         return this.getSortedSetDelegate();
      }
   }

   private class WrappedSortedSet extends AbstractMapBasedMultimap<K, V>.WrappedCollection implements SortedSet<V> {
      WrappedSortedSet(@Nullable K var1, SortedSet<V> var2, @Nullable AbstractMapBasedMultimap<K, V>.WrappedCollection var3) {
         super();
      }

      SortedSet<V> getSortedSetDelegate() {
         return (SortedSet)this.getDelegate();
      }

      public Comparator<? super V> comparator() {
         return this.getSortedSetDelegate().comparator();
      }

      public V first() {
         this.refreshIfEmpty();
         return this.getSortedSetDelegate().first();
      }

      public V last() {
         this.refreshIfEmpty();
         return this.getSortedSetDelegate().last();
      }

      public SortedSet<V> headSet(V var1) {
         this.refreshIfEmpty();
         return AbstractMapBasedMultimap.this.new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().headSet(var1), (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null?this:this.getAncestor()));
      }

      public SortedSet<V> subSet(V var1, V var2) {
         this.refreshIfEmpty();
         return AbstractMapBasedMultimap.this.new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().subSet(var1, var2), (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null?this:this.getAncestor()));
      }

      public SortedSet<V> tailSet(V var1) {
         this.refreshIfEmpty();
         return AbstractMapBasedMultimap.this.new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().tailSet(var1), (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null?this:this.getAncestor()));
      }
   }

   private class WrappedSet extends AbstractMapBasedMultimap<K, V>.WrappedCollection implements Set<V> {
      WrappedSet(@Nullable K var1, Set<V> var2) {
         super();
      }

      public boolean removeAll(Collection<?> var1) {
         if(var1.isEmpty()) {
            return false;
         } else {
            int var2 = this.size();
            boolean var3 = Sets.removeAllImpl((Set)this.delegate, var1);
            if(var3) {
               int var4 = this.delegate.size();
               AbstractMapBasedMultimap.this.totalSize = var4 - var2;
               this.removeIfEmpty();
            }

            return var3;
         }
      }
   }

   private class WrappedCollection extends AbstractCollection<V> {
      final K key;
      Collection<V> delegate;
      final AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor;
      final Collection<V> ancestorDelegate;

      WrappedCollection(@Nullable K var1, Collection<V> var2, @Nullable AbstractMapBasedMultimap<K, V>.WrappedCollection var3) {
         this.key = var2;
         this.delegate = var3;
         this.ancestor = var4;
         this.ancestorDelegate = var4 == null?null:var4.getDelegate();
      }

      void refreshIfEmpty() {
         if(this.ancestor != null) {
            this.ancestor.refreshIfEmpty();
            if(this.ancestor.getDelegate() != this.ancestorDelegate) {
               throw new ConcurrentModificationException();
            }
         } else if(this.delegate.isEmpty()) {
            Collection var1 = (Collection)AbstractMapBasedMultimap.this.map.get(this.key);
            if(var1 != null) {
               this.delegate = var1;
            }
         }

      }

      void removeIfEmpty() {
         if(this.ancestor != null) {
            this.ancestor.removeIfEmpty();
         } else if(this.delegate.isEmpty()) {
            AbstractMapBasedMultimap.this.map.remove(this.key);
         }

      }

      K getKey() {
         return this.key;
      }

      void addToMap() {
         if(this.ancestor != null) {
            this.ancestor.addToMap();
         } else {
            AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
         }

      }

      public int size() {
         this.refreshIfEmpty();
         return this.delegate.size();
      }

      public boolean equals(@Nullable Object var1) {
         if(var1 == this) {
            return true;
         } else {
            this.refreshIfEmpty();
            return this.delegate.equals(var1);
         }
      }

      public int hashCode() {
         this.refreshIfEmpty();
         return this.delegate.hashCode();
      }

      public String toString() {
         this.refreshIfEmpty();
         return this.delegate.toString();
      }

      Collection<V> getDelegate() {
         return this.delegate;
      }

      public Iterator<V> iterator() {
         this.refreshIfEmpty();
         return new AbstractMapBasedMultimap.WrappedCollection.WrappedCollection$WrappedIterator();
      }

      public boolean add(V var1) {
         this.refreshIfEmpty();
         boolean var2 = this.delegate.isEmpty();
         boolean var3 = this.delegate.add(var1);
         if(var3) {
            AbstractMapBasedMultimap.access$208(AbstractMapBasedMultimap.this);
            if(var2) {
               this.addToMap();
            }
         }

         return var3;
      }

      AbstractMapBasedMultimap<K, V>.WrappedCollection getAncestor() {
         return this.ancestor;
      }

      public boolean addAll(Collection<? extends V> var1) {
         if(var1.isEmpty()) {
            return false;
         } else {
            int var2 = this.size();
            boolean var3 = this.delegate.addAll(var1);
            if(var3) {
               int var4 = this.delegate.size();
               AbstractMapBasedMultimap.this.totalSize = var4 - var2;
               if(var2 == 0) {
                  this.addToMap();
               }
            }

            return var3;
         }
      }

      public boolean contains(Object var1) {
         this.refreshIfEmpty();
         return this.delegate.contains(var1);
      }

      public boolean containsAll(Collection<?> var1) {
         this.refreshIfEmpty();
         return this.delegate.containsAll(var1);
      }

      public void clear() {
         int var1 = this.size();
         if(var1 != 0) {
            this.delegate.clear();
            AbstractMapBasedMultimap.this.totalSize = var1;
            this.removeIfEmpty();
         }
      }

      public boolean remove(Object var1) {
         this.refreshIfEmpty();
         boolean var2 = this.delegate.remove(var1);
         if(var2) {
            AbstractMapBasedMultimap.access$210(AbstractMapBasedMultimap.this);
            this.removeIfEmpty();
         }

         return var2;
      }

      public boolean removeAll(Collection<?> var1) {
         if(var1.isEmpty()) {
            return false;
         } else {
            int var2 = this.size();
            boolean var3 = this.delegate.removeAll(var1);
            if(var3) {
               int var4 = this.delegate.size();
               AbstractMapBasedMultimap.this.totalSize = var4 - var2;
               this.removeIfEmpty();
            }

            return var3;
         }
      }

      public boolean retainAll(Collection<?> var1) {
         Preconditions.checkNotNull(var1);
         int var2 = this.size();
         boolean var3 = this.delegate.retainAll(var1);
         if(var3) {
            int var4 = this.delegate.size();
            AbstractMapBasedMultimap.this.totalSize = var4 - var2;
            this.removeIfEmpty();
         }

         return var3;
      }

      class WrappedCollection$WrappedIterator implements Iterator<V> {
         final Iterator<V> delegateIterator;
         final Collection<V> originalDelegate;

         WrappedCollection$WrappedIterator() {
            this.originalDelegate = WrappedCollection.this.delegate;
            this.delegateIterator = AbstractMapBasedMultimap.this.iteratorOrListIterator(WrappedCollection.this.delegate);
         }

         WrappedCollection$WrappedIterator(Iterator<V> var1) {
            this.originalDelegate = WrappedCollection.this.delegate;
            this.delegateIterator = var2;
         }

         void validateIterator() {
            WrappedCollection.this.refreshIfEmpty();
            if(WrappedCollection.this.delegate != this.originalDelegate) {
               throw new ConcurrentModificationException();
            }
         }

         public boolean hasNext() {
            this.validateIterator();
            return this.delegateIterator.hasNext();
         }

         public V next() {
            this.validateIterator();
            return this.delegateIterator.next();
         }

         public void remove() {
            this.delegateIterator.remove();
            AbstractMapBasedMultimap.access$210(AbstractMapBasedMultimap.this);
            WrappedCollection.this.removeIfEmpty();
         }

         Iterator<V> getDelegateIterator() {
            this.validateIterator();
            return this.delegateIterator;
         }
      }
   }
}
