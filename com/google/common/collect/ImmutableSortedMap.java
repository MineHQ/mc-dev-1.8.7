package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.EmptyImmutableSortedMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMapFauxverideShim;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.RegularImmutableSortedMap;
import com.google.common.collect.RegularImmutableSortedSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V> {
   private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
   private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP;
   private transient ImmutableSortedMap<K, V> descendingMap;
   private static final long serialVersionUID = 0L;

   static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> var0) {
      return (ImmutableSortedMap)(Ordering.natural().equals(var0)?of():new EmptyImmutableSortedMap(var0));
   }

   static <K, V> ImmutableSortedMap<K, V> fromSortedEntries(Comparator<? super K> var0, int var1, Entry<K, V>[] var2) {
      if(var1 == 0) {
         return emptyMap(var0);
      } else {
         ImmutableList.Builder var3 = ImmutableList.builder();
         ImmutableList.Builder var4 = ImmutableList.builder();

         for(int var5 = 0; var5 < var1; ++var5) {
            Entry var6 = var2[var5];
            var3.add(var6.getKey());
            var4.add(var6.getValue());
         }

         return new RegularImmutableSortedMap(new RegularImmutableSortedSet(var3.build(), var0), var4.build());
      }
   }

   static <K, V> ImmutableSortedMap<K, V> from(ImmutableSortedSet<K> var0, ImmutableList<V> var1) {
      return (ImmutableSortedMap)(var0.isEmpty()?emptyMap(var0.comparator()):new RegularImmutableSortedMap((RegularImmutableSortedSet)var0, var1));
   }

   public static <K, V> ImmutableSortedMap<K, V> of() {
      return NATURAL_EMPTY_MAP;
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K var0, V var1) {
      return from(ImmutableSortedSet.of(var0), ImmutableList.of(var1));
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K var0, V var1, K var2, V var3) {
      return fromEntries(Ordering.natural(), false, 2, new Entry[]{entryOf(var0, var1), entryOf(var2, var3)});
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5) {
      return fromEntries(Ordering.natural(), false, 3, new Entry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5)});
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5, K var6, V var7) {
      return fromEntries(Ordering.natural(), false, 4, new Entry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5), entryOf(var6, var7)});
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5, K var6, V var7, K var8, V var9) {
      return fromEntries(Ordering.natural(), false, 5, new Entry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5), entryOf(var6, var7), entryOf(var8, var9)});
   }

   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> var0) {
      Ordering var1 = Ordering.natural();
      return copyOfInternal(var0, var1);
   }

   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> var0, Comparator<? super K> var1) {
      return copyOfInternal(var0, (Comparator)Preconditions.checkNotNull(var1));
   }

   public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> var0) {
      Comparator var1 = var0.comparator();
      if(var1 == null) {
         var1 = NATURAL_ORDER;
      }

      return copyOfInternal(var0, var1);
   }

   private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> var0, Comparator<? super K> var1) {
      boolean var2 = false;
      if(var0 instanceof SortedMap) {
         SortedMap var3 = (SortedMap)var0;
         Comparator var4 = var3.comparator();
         var2 = var4 == null?var1 == NATURAL_ORDER:var1.equals(var4);
      }

      if(var2 && var0 instanceof ImmutableSortedMap) {
         ImmutableSortedMap var5 = (ImmutableSortedMap)var0;
         if(!var5.isPartialView()) {
            return var5;
         }
      }

      Entry[] var6 = (Entry[])var0.entrySet().toArray(new Entry[0]);
      return fromEntries(var1, var2, var6.length, var6);
   }

   static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> var0, boolean var1, int var2, Entry... var3) {
      for(int var4 = 0; var4 < var2; ++var4) {
         Entry var5 = var3[var4];
         var3[var4] = entryOf(var5.getKey(), var5.getValue());
      }

      if(!var1) {
         sortEntries(var0, var2, var3);
         validateEntries(var2, var3, var0);
      }

      return fromSortedEntries(var0, var2, var3);
   }

   private static <K, V> void sortEntries(Comparator<? super K> var0, int var1, Entry<K, V>[] var2) {
      Arrays.sort(var2, 0, var1, Ordering.from(var0).onKeys());
   }

   private static <K, V> void validateEntries(int var0, Entry<K, V>[] var1, Comparator<? super K> var2) {
      for(int var3 = 1; var3 < var0; ++var3) {
         checkNoConflict(var2.compare(var1[var3 - 1].getKey(), var1[var3].getKey()) != 0, "key", var1[var3 - 1], var1[var3]);
      }

   }

   public static <K extends Comparable<?>, V> ImmutableSortedMap.Builder<K, V> naturalOrder() {
      return new ImmutableSortedMap.Builder(Ordering.natural());
   }

   public static <K, V> ImmutableSortedMap.Builder<K, V> orderedBy(Comparator<K> var0) {
      return new ImmutableSortedMap.Builder(var0);
   }

   public static <K extends Comparable<?>, V> ImmutableSortedMap.Builder<K, V> reverseOrder() {
      return new ImmutableSortedMap.Builder(Ordering.natural().reverse());
   }

   ImmutableSortedMap() {
   }

   ImmutableSortedMap(ImmutableSortedMap<K, V> var1) {
      this.descendingMap = var1;
   }

   public int size() {
      return this.values().size();
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.values().contains(var1);
   }

   boolean isPartialView() {
      return this.keySet().isPartialView() || this.values().isPartialView();
   }

   public ImmutableSet<Entry<K, V>> entrySet() {
      return super.entrySet();
   }

   public abstract ImmutableSortedSet<K> keySet();

   public abstract ImmutableCollection<V> values();

   public Comparator<? super K> comparator() {
      return this.keySet().comparator();
   }

   public K firstKey() {
      return this.keySet().first();
   }

   public K lastKey() {
      return this.keySet().last();
   }

   public ImmutableSortedMap<K, V> headMap(K var1) {
      return this.headMap(var1, false);
   }

   public abstract ImmutableSortedMap<K, V> headMap(K var1, boolean var2);

   public ImmutableSortedMap<K, V> subMap(K var1, K var2) {
      return this.subMap(var1, true, var2, false);
   }

   public ImmutableSortedMap<K, V> subMap(K var1, boolean var2, K var3, boolean var4) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var3);
      Preconditions.checkArgument(this.comparator().compare(var1, var3) <= 0, "expected fromKey <= toKey but %s > %s", new Object[]{var1, var3});
      return this.headMap(var3, var4).tailMap(var1, var2);
   }

   public ImmutableSortedMap<K, V> tailMap(K var1) {
      return this.tailMap(var1, true);
   }

   public abstract ImmutableSortedMap<K, V> tailMap(K var1, boolean var2);

   public Entry<K, V> lowerEntry(K var1) {
      return this.headMap(var1, false).lastEntry();
   }

   public K lowerKey(K var1) {
      return Maps.keyOrNull(this.lowerEntry(var1));
   }

   public Entry<K, V> floorEntry(K var1) {
      return this.headMap(var1, true).lastEntry();
   }

   public K floorKey(K var1) {
      return Maps.keyOrNull(this.floorEntry(var1));
   }

   public Entry<K, V> ceilingEntry(K var1) {
      return this.tailMap(var1, true).firstEntry();
   }

   public K ceilingKey(K var1) {
      return Maps.keyOrNull(this.ceilingEntry(var1));
   }

   public Entry<K, V> higherEntry(K var1) {
      return this.tailMap(var1, false).firstEntry();
   }

   public K higherKey(K var1) {
      return Maps.keyOrNull(this.higherEntry(var1));
   }

   public Entry<K, V> firstEntry() {
      return this.isEmpty()?null:(Entry)this.entrySet().asList().get(0);
   }

   public Entry<K, V> lastEntry() {
      return this.isEmpty()?null:(Entry)this.entrySet().asList().get(this.size() - 1);
   }

   /** @deprecated */
   @Deprecated
   public final Entry<K, V> pollFirstEntry() {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final Entry<K, V> pollLastEntry() {
      throw new UnsupportedOperationException();
   }

   public ImmutableSortedMap<K, V> descendingMap() {
      ImmutableSortedMap var1 = this.descendingMap;
      if(var1 == null) {
         var1 = this.descendingMap = this.createDescendingMap();
      }

      return var1;
   }

   abstract ImmutableSortedMap<K, V> createDescendingMap();

   public ImmutableSortedSet<K> navigableKeySet() {
      return this.keySet();
   }

   public ImmutableSortedSet<K> descendingKeySet() {
      return this.keySet().descendingSet();
   }

   Object writeReplace() {
      return new ImmutableSortedMap.SerializedForm(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ImmutableSet keySet() {
      return this.keySet();
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
   public SortedMap tailMap(Object var1) {
      return this.tailMap(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedMap headMap(Object var1) {
      return this.headMap(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedMap subMap(Object var1, Object var2) {
      return this.subMap(var1, var2);
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
   public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
      return this.subMap(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public NavigableSet descendingKeySet() {
      return this.descendingKeySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public NavigableSet navigableKeySet() {
      return this.navigableKeySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public NavigableMap descendingMap() {
      return this.descendingMap();
   }

   static {
      NATURAL_EMPTY_MAP = new EmptyImmutableSortedMap(NATURAL_ORDER);
   }

   private static class SerializedForm extends ImmutableMap.SerializedForm {
      private final Comparator<Object> comparator;
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableSortedMap<?, ?> var1) {
         super(var1);
         this.comparator = var1.comparator();
      }

      Object readResolve() {
         ImmutableSortedMap.Builder var1 = new ImmutableSortedMap.Builder(this.comparator);
         return this.createMap(var1);
      }
   }

   public static class Builder<K, V> extends ImmutableMap.Builder<K, V> {
      private final Comparator<? super K> comparator;

      public Builder(Comparator<? super K> var1) {
         this.comparator = (Comparator)Preconditions.checkNotNull(var1);
      }

      public ImmutableSortedMap.Builder<K, V> put(K var1, V var2) {
         super.put(var1, var2);
         return this;
      }

      public ImmutableSortedMap.Builder<K, V> put(Entry<? extends K, ? extends V> var1) {
         super.put(var1);
         return this;
      }

      public ImmutableSortedMap.Builder<K, V> putAll(Map<? extends K, ? extends V> var1) {
         super.putAll(var1);
         return this;
      }

      public ImmutableSortedMap<K, V> build() {
         return ImmutableSortedMap.fromEntries(this.comparator, false, this.size, this.entries);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMap build() {
         return this.build();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMap.Builder putAll(Map var1) {
         return this.putAll(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMap.Builder put(Entry var1) {
         return this.put(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMap.Builder put(Object var1, Object var2) {
         return this.put(var1, var2);
      }
   }
}
