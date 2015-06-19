package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Map.Entry;

@GwtCompatible(
   emulated = true
)
final class Platform {
   static <T> T[] newArray(T[] var0, int var1) {
      Class var2 = var0.getClass().getComponentType();
      Object[] var3 = (Object[])((Object[])Array.newInstance(var2, var1));
      return var3;
   }

   static <E> Set<E> newSetFromMap(Map<E, Boolean> var0) {
      return Collections.newSetFromMap(var0);
   }

   static MapMaker tryWeakKeys(MapMaker var0) {
      return var0.weakKeys();
   }

   static <K, V1, V2> SortedMap<K, V2> mapsTransformEntriesSortedMap(SortedMap<K, V1> var0, Maps.EntryTransformer<? super K, ? super V1, V2> var1) {
      return (SortedMap)(var0 instanceof NavigableMap?Maps.transformEntries((NavigableMap)var0, var1):Maps.transformEntriesIgnoreNavigable(var0, var1));
   }

   static <K, V> SortedMap<K, V> mapsAsMapSortedSet(SortedSet<K> var0, Function<? super K, V> var1) {
      return (SortedMap)(var0 instanceof NavigableSet?Maps.asMap((NavigableSet)var0, var1):Maps.asMapSortedIgnoreNavigable(var0, var1));
   }

   static <E> SortedSet<E> setsFilterSortedSet(SortedSet<E> var0, Predicate<? super E> var1) {
      return (SortedSet)(var0 instanceof NavigableSet?Sets.filter((NavigableSet)var0, var1):Sets.filterSortedIgnoreNavigable(var0, var1));
   }

   static <K, V> SortedMap<K, V> mapsFilterSortedMap(SortedMap<K, V> var0, Predicate<? super Entry<K, V>> var1) {
      return (SortedMap)(var0 instanceof NavigableMap?Maps.filterEntries((NavigableMap)var0, var1):Maps.filterSortedIgnoreNavigable(var0, var1));
   }

   private Platform() {
   }
}
