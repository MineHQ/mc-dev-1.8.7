package com.google.common.collect;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;
import javax.annotation.Nullable;

abstract class AbstractNavigableMap<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V> {
   AbstractNavigableMap() {
   }

   @Nullable
   public abstract V get(@Nullable Object var1);

   @Nullable
   public Entry<K, V> firstEntry() {
      return (Entry)Iterators.getNext(this.entryIterator(), (Object)null);
   }

   @Nullable
   public Entry<K, V> lastEntry() {
      return (Entry)Iterators.getNext(this.descendingEntryIterator(), (Object)null);
   }

   @Nullable
   public Entry<K, V> pollFirstEntry() {
      return (Entry)Iterators.pollNext(this.entryIterator());
   }

   @Nullable
   public Entry<K, V> pollLastEntry() {
      return (Entry)Iterators.pollNext(this.descendingEntryIterator());
   }

   public K firstKey() {
      Entry var1 = this.firstEntry();
      if(var1 == null) {
         throw new NoSuchElementException();
      } else {
         return var1.getKey();
      }
   }

   public K lastKey() {
      Entry var1 = this.lastEntry();
      if(var1 == null) {
         throw new NoSuchElementException();
      } else {
         return var1.getKey();
      }
   }

   @Nullable
   public Entry<K, V> lowerEntry(K var1) {
      return this.headMap(var1, false).lastEntry();
   }

   @Nullable
   public Entry<K, V> floorEntry(K var1) {
      return this.headMap(var1, true).lastEntry();
   }

   @Nullable
   public Entry<K, V> ceilingEntry(K var1) {
      return this.tailMap(var1, true).firstEntry();
   }

   @Nullable
   public Entry<K, V> higherEntry(K var1) {
      return this.tailMap(var1, false).firstEntry();
   }

   public K lowerKey(K var1) {
      return Maps.keyOrNull(this.lowerEntry(var1));
   }

   public K floorKey(K var1) {
      return Maps.keyOrNull(this.floorEntry(var1));
   }

   public K ceilingKey(K var1) {
      return Maps.keyOrNull(this.ceilingEntry(var1));
   }

   public K higherKey(K var1) {
      return Maps.keyOrNull(this.higherEntry(var1));
   }

   abstract Iterator<Entry<K, V>> entryIterator();

   abstract Iterator<Entry<K, V>> descendingEntryIterator();

   public SortedMap<K, V> subMap(K var1, K var2) {
      return this.subMap(var1, true, var2, false);
   }

   public SortedMap<K, V> headMap(K var1) {
      return this.headMap(var1, false);
   }

   public SortedMap<K, V> tailMap(K var1) {
      return this.tailMap(var1, true);
   }

   public NavigableSet<K> navigableKeySet() {
      return new Maps.NavigableKeySet(this);
   }

   public Set<K> keySet() {
      return this.navigableKeySet();
   }

   public abstract int size();

   public Set<Entry<K, V>> entrySet() {
      return new Maps.EntrySet() {
         Map<K, V> map() {
            return AbstractNavigableMap.this;
         }

         public Iterator<Entry<K, V>> iterator() {
            return AbstractNavigableMap.this.entryIterator();
         }
      };
   }

   public NavigableSet<K> descendingKeySet() {
      return this.descendingMap().navigableKeySet();
   }

   public NavigableMap<K, V> descendingMap() {
      return new AbstractNavigableMap.DescendingMap(null);
   }

   private final class DescendingMap extends Maps.DescendingMap<K, V> {
      private DescendingMap() {
      }

      NavigableMap<K, V> forward() {
         return AbstractNavigableMap.this;
      }

      Iterator<Entry<K, V>> entryIterator() {
         return AbstractNavigableMap.this.descendingEntryIterator();
      }

      // $FF: synthetic method
      DescendingMap(Object var2) {
         this();
      }
   }
}
