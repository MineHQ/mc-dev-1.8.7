package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.AbstractMapEntry;
import com.google.common.collect.AbstractNavigableMap;
import com.google.common.collect.BiMap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.ForwardingNavigableSet;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ForwardingSortedMap;
import com.google.common.collect.ForwardingSortedSet;
import com.google.common.collect.ImmutableEntry;
import com.google.common.collect.ImmutableEnumMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Ordering;
import com.google.common.collect.Platform;
import com.google.common.collect.Sets;
import com.google.common.collect.SortedMapDifference;
import com.google.common.collect.Synchronized;
import com.google.common.collect.TransformedIterator;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class Maps {
   static final Joiner.MapJoiner STANDARD_JOINER;

   private Maps() {
   }

   static <K> Function<Entry<K, ?>, K> keyFunction() {
      return Maps.EntryFunction.KEY;
   }

   static <V> Function<Entry<?, V>, V> valueFunction() {
      return Maps.EntryFunction.VALUE;
   }

   static <K, V> Iterator<K> keyIterator(Iterator<Entry<K, V>> var0) {
      return Iterators.transform(var0, keyFunction());
   }

   static <K, V> Iterator<V> valueIterator(Iterator<Entry<K, V>> var0) {
      return Iterators.transform(var0, valueFunction());
   }

   static <K, V> UnmodifiableIterator<V> valueIterator(final UnmodifiableIterator<Entry<K, V>> var0) {
      return new UnmodifiableIterator() {
         public boolean hasNext() {
            return var0.hasNext();
         }

         public V next() {
            return ((Entry)var0.next()).getValue();
         }
      };
   }

   @GwtCompatible(
      serializable = true
   )
   @Beta
   public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(Map<K, ? extends V> var0) {
      if(var0 instanceof ImmutableEnumMap) {
         ImmutableEnumMap var3 = (ImmutableEnumMap)var0;
         return var3;
      } else if(var0.isEmpty()) {
         return ImmutableMap.of();
      } else {
         Iterator var1 = var0.entrySet().iterator();

         while(var1.hasNext()) {
            Entry var2 = (Entry)var1.next();
            Preconditions.checkNotNull(var2.getKey());
            Preconditions.checkNotNull(var2.getValue());
         }

         return ImmutableEnumMap.asImmutable(new EnumMap(var0));
      }
   }

   public static <K, V> HashMap<K, V> newHashMap() {
      return new HashMap();
   }

   public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int var0) {
      return new HashMap(capacity(var0));
   }

   static int capacity(int var0) {
      if(var0 < 3) {
         CollectPreconditions.checkNonnegative(var0, "expectedSize");
         return var0 + 1;
      } else {
         return var0 < 1073741824?var0 + var0 / 3:Integer.MAX_VALUE;
      }
   }

   public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> var0) {
      return new HashMap(var0);
   }

   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
      return new LinkedHashMap();
   }

   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> var0) {
      return new LinkedHashMap(var0);
   }

   public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
      return (new MapMaker()).makeMap();
   }

   public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
      return new TreeMap();
   }

   public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> var0) {
      return new TreeMap(var0);
   }

   public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable Comparator<C> var0) {
      return new TreeMap(var0);
   }

   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> var0) {
      return new EnumMap((Class)Preconditions.checkNotNull(var0));
   }

   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> var0) {
      return new EnumMap(var0);
   }

   public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
      return new IdentityHashMap();
   }

   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> var0, Map<? extends K, ? extends V> var1) {
      if(var0 instanceof SortedMap) {
         SortedMap var2 = (SortedMap)var0;
         SortedMapDifference var3 = difference(var2, var1);
         return var3;
      } else {
         return difference(var0, var1, Equivalence.equals());
      }
   }

   @Beta
   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> var0, Map<? extends K, ? extends V> var1, Equivalence<? super V> var2) {
      Preconditions.checkNotNull(var2);
      HashMap var3 = newHashMap();
      HashMap var4 = new HashMap(var1);
      HashMap var5 = newHashMap();
      HashMap var6 = newHashMap();
      doDifference(var0, var1, var2, var3, var4, var5, var6);
      return new Maps.MapDifferenceImpl(var3, var4, var5, var6);
   }

   private static <K, V> void doDifference(Map<? extends K, ? extends V> var0, Map<? extends K, ? extends V> var1, Equivalence<? super V> var2, Map<K, V> var3, Map<K, V> var4, Map<K, V> var5, Map<K, MapDifference.ValueDifference<V>> var6) {
      Iterator var7 = var0.entrySet().iterator();

      while(var7.hasNext()) {
         Entry var8 = (Entry)var7.next();
         Object var9 = var8.getKey();
         Object var10 = var8.getValue();
         if(var1.containsKey(var9)) {
            Object var11 = var4.remove(var9);
            if(var2.equivalent(var10, var11)) {
               var5.put(var9, var10);
            } else {
               var6.put(var9, Maps.ValueDifferenceImpl.create(var10, var11));
            }
         } else {
            var3.put(var9, var10);
         }
      }

   }

   private static <K, V> Map<K, V> unmodifiableMap(Map<K, V> var0) {
      return (Map)(var0 instanceof SortedMap?Collections.unmodifiableSortedMap((SortedMap)var0):Collections.unmodifiableMap(var0));
   }

   public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> var0, Map<? extends K, ? extends V> var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Comparator var2 = orNaturalOrder(var0.comparator());
      TreeMap var3 = newTreeMap(var2);
      TreeMap var4 = newTreeMap(var2);
      var4.putAll(var1);
      TreeMap var5 = newTreeMap(var2);
      TreeMap var6 = newTreeMap(var2);
      doDifference(var0, var1, Equivalence.equals(), var3, var4, var5, var6);
      return new Maps.SortedMapDifferenceImpl(var3, var4, var5, var6);
   }

   static <E> Comparator<? super E> orNaturalOrder(@Nullable Comparator<? super E> var0) {
      return (Comparator)(var0 != null?var0:Ordering.natural());
   }

   @Beta
   public static <K, V> Map<K, V> asMap(Set<K> var0, Function<? super K, V> var1) {
      return (Map)(var0 instanceof SortedSet?asMap((SortedSet)var0, var1):new Maps.AsMapView(var0, var1));
   }

   @Beta
   public static <K, V> SortedMap<K, V> asMap(SortedSet<K> var0, Function<? super K, V> var1) {
      return Platform.mapsAsMapSortedSet(var0, var1);
   }

   static <K, V> SortedMap<K, V> asMapSortedIgnoreNavigable(SortedSet<K> var0, Function<? super K, V> var1) {
      return new Maps.SortedAsMapView(var0, var1);
   }

   @Beta
   @GwtIncompatible("NavigableMap")
   public static <K, V> NavigableMap<K, V> asMap(NavigableSet<K> var0, Function<? super K, V> var1) {
      return new Maps.NavigableAsMapView(var0, var1);
   }

   static <K, V> Iterator<Entry<K, V>> asMapEntryIterator(Set<K> var0, final Function<? super K, V> var1) {
      return new TransformedIterator(var0.iterator()) {
         Entry<K, V> transform(K var1x) {
            return Maps.immutableEntry(var1x, var1.apply(var1x));
         }

         // $FF: synthetic method
         // $FF: bridge method
         Object transform(Object var1x) {
            return this.transform(var1x);
         }
      };
   }

   private static <E> Set<E> removeOnlySet(final Set<E> var0) {
      return new ForwardingSet() {
         protected Set<E> delegate() {
            return var0;
         }

         public boolean add(E var1) {
            throw new UnsupportedOperationException();
         }

         public boolean addAll(Collection<? extends E> var1) {
            throw new UnsupportedOperationException();
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Collection delegate() {
            return this.delegate();
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> var0) {
      return new ForwardingSortedSet() {
         protected SortedSet<E> delegate() {
            return var0;
         }

         public boolean add(E var1) {
            throw new UnsupportedOperationException();
         }

         public boolean addAll(Collection<? extends E> var1) {
            throw new UnsupportedOperationException();
         }

         public SortedSet<E> headSet(E var1) {
            return Maps.removeOnlySortedSet(super.headSet(var1));
         }

         public SortedSet<E> subSet(E var1, E var2) {
            return Maps.removeOnlySortedSet(super.subSet(var1, var2));
         }

         public SortedSet<E> tailSet(E var1) {
            return Maps.removeOnlySortedSet(super.tailSet(var1));
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Set delegate() {
            return this.delegate();
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Collection delegate() {
            return this.delegate();
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   @GwtIncompatible("NavigableSet")
   private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> var0) {
      return new ForwardingNavigableSet() {
         protected NavigableSet<E> delegate() {
            return var0;
         }

         public boolean add(E var1) {
            throw new UnsupportedOperationException();
         }

         public boolean addAll(Collection<? extends E> var1) {
            throw new UnsupportedOperationException();
         }

         public SortedSet<E> headSet(E var1) {
            return Maps.removeOnlySortedSet(super.headSet(var1));
         }

         public SortedSet<E> subSet(E var1, E var2) {
            return Maps.removeOnlySortedSet(super.subSet(var1, var2));
         }

         public SortedSet<E> tailSet(E var1) {
            return Maps.removeOnlySortedSet(super.tailSet(var1));
         }

         public NavigableSet<E> headSet(E var1, boolean var2) {
            return Maps.removeOnlyNavigableSet(super.headSet(var1, var2));
         }

         public NavigableSet<E> tailSet(E var1, boolean var2) {
            return Maps.removeOnlyNavigableSet(super.tailSet(var1, var2));
         }

         public NavigableSet<E> subSet(E var1, boolean var2, E var3, boolean var4) {
            return Maps.removeOnlyNavigableSet(super.subSet(var1, var2, var3, var4));
         }

         public NavigableSet<E> descendingSet() {
            return Maps.removeOnlyNavigableSet(super.descendingSet());
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected SortedSet delegate() {
            return this.delegate();
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Set delegate() {
            return this.delegate();
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Collection delegate() {
            return this.delegate();
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   @Beta
   public static <K, V> ImmutableMap<K, V> toMap(Iterable<K> var0, Function<? super K, V> var1) {
      return toMap(var0.iterator(), var1);
   }

   @Beta
   public static <K, V> ImmutableMap<K, V> toMap(Iterator<K> var0, Function<? super K, V> var1) {
      Preconditions.checkNotNull(var1);
      LinkedHashMap var2 = newLinkedHashMap();

      while(var0.hasNext()) {
         Object var3 = var0.next();
         var2.put(var3, var1.apply(var3));
      }

      return ImmutableMap.copyOf(var2);
   }

   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> var0, Function<? super V, K> var1) {
      return uniqueIndex(var0.iterator(), var1);
   }

   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> var0, Function<? super V, K> var1) {
      Preconditions.checkNotNull(var1);
      ImmutableMap.Builder var2 = ImmutableMap.builder();

      while(var0.hasNext()) {
         Object var3 = var0.next();
         var2.put(var1.apply(var3), var3);
      }

      return var2.build();
   }

   @GwtIncompatible("java.util.Properties")
   public static ImmutableMap<String, String> fromProperties(Properties var0) {
      ImmutableMap.Builder var1 = ImmutableMap.builder();
      Enumeration var2 = var0.propertyNames();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         var1.put(var3, var0.getProperty(var3));
      }

      return var1.build();
   }

   @GwtCompatible(
      serializable = true
   )
   public static <K, V> Entry<K, V> immutableEntry(@Nullable K var0, @Nullable V var1) {
      return new ImmutableEntry(var0, var1);
   }

   static <K, V> Set<Entry<K, V>> unmodifiableEntrySet(Set<Entry<K, V>> var0) {
      return new Maps.UnmodifiableEntrySet(Collections.unmodifiableSet(var0));
   }

   static <K, V> Entry<K, V> unmodifiableEntry(final Entry<? extends K, ? extends V> var0) {
      Preconditions.checkNotNull(var0);
      return new AbstractMapEntry() {
         public K getKey() {
            return var0.getKey();
         }

         public V getValue() {
            return var0.getValue();
         }
      };
   }

   @Beta
   public static <A, B> Converter<A, B> asConverter(BiMap<A, B> var0) {
      return new Maps.BiMapConverter(var0);
   }

   public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> var0) {
      return Synchronized.biMap(var0, (Object)null);
   }

   public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> var0) {
      return new Maps.UnmodifiableBiMap(var0, (BiMap)null);
   }

   public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> var0, Function<? super V1, V2> var1) {
      return transformEntries(var0, asEntryTransformer(var1));
   }

   public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> var0, Function<? super V1, V2> var1) {
      return transformEntries(var0, asEntryTransformer(var1));
   }

   @GwtIncompatible("NavigableMap")
   public static <K, V1, V2> NavigableMap<K, V2> transformValues(NavigableMap<K, V1> var0, Function<? super V1, V2> var1) {
      return transformEntries(var0, asEntryTransformer(var1));
   }

   public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> var0, Maps.EntryTransformer<? super K, ? super V1, V2> var1) {
      return (Map)(var0 instanceof SortedMap?transformEntries((SortedMap)var0, var1):new Maps.TransformedEntriesMap(var0, var1));
   }

   public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> var0, Maps.EntryTransformer<? super K, ? super V1, V2> var1) {
      return Platform.mapsTransformEntriesSortedMap(var0, var1);
   }

   @GwtIncompatible("NavigableMap")
   public static <K, V1, V2> NavigableMap<K, V2> transformEntries(NavigableMap<K, V1> var0, Maps.EntryTransformer<? super K, ? super V1, V2> var1) {
      return new Maps.TransformedEntriesNavigableMap(var0, var1);
   }

   static <K, V1, V2> SortedMap<K, V2> transformEntriesIgnoreNavigable(SortedMap<K, V1> var0, Maps.EntryTransformer<? super K, ? super V1, V2> var1) {
      return new Maps.TransformedEntriesSortedMap(var0, var1);
   }

   static <K, V1, V2> Maps.EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> var0) {
      Preconditions.checkNotNull(var0);
      return new Maps.EntryTransformer() {
         public V2 transformEntry(K var1, V1 var2) {
            return var0.apply(var2);
         }
      };
   }

   static <K, V1, V2> Function<V1, V2> asValueToValueFunction(final Maps.EntryTransformer<? super K, V1, V2> var0, final K var1) {
      Preconditions.checkNotNull(var0);
      return new Function() {
         public V2 apply(@Nullable V1 var1x) {
            return var0.transformEntry(var1, var1x);
         }
      };
   }

   static <K, V1, V2> Function<Entry<K, V1>, V2> asEntryToValueFunction(final Maps.EntryTransformer<? super K, ? super V1, V2> var0) {
      Preconditions.checkNotNull(var0);
      return new Function() {
         public V2 apply(Entry<K, V1> var1) {
            return var0.transformEntry(var1.getKey(), var1.getValue());
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object apply(Object var1) {
            return this.apply((Entry)var1);
         }
      };
   }

   static <V2, K, V1> Entry<K, V2> transformEntry(final Maps.EntryTransformer<? super K, ? super V1, V2> var0, final Entry<K, V1> var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new AbstractMapEntry() {
         public K getKey() {
            return var1.getKey();
         }

         public V2 getValue() {
            return var0.transformEntry(var1.getKey(), var1.getValue());
         }
      };
   }

   static <K, V1, V2> Function<Entry<K, V1>, Entry<K, V2>> asEntryToEntryFunction(final Maps.EntryTransformer<? super K, ? super V1, V2> var0) {
      Preconditions.checkNotNull(var0);
      return new Function() {
         public Entry<K, V2> apply(Entry<K, V1> var1) {
            return Maps.transformEntry(var0, var1);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object apply(Object var1) {
            return this.apply((Entry)var1);
         }
      };
   }

   static <K> Predicate<Entry<K, ?>> keyPredicateOnEntries(Predicate<? super K> var0) {
      return Predicates.compose(var0, keyFunction());
   }

   static <V> Predicate<Entry<?, V>> valuePredicateOnEntries(Predicate<? super V> var0) {
      return Predicates.compose(var0, valueFunction());
   }

   public static <K, V> Map<K, V> filterKeys(Map<K, V> var0, Predicate<? super K> var1) {
      if(var0 instanceof SortedMap) {
         return filterKeys((SortedMap)var0, var1);
      } else if(var0 instanceof BiMap) {
         return filterKeys((BiMap)var0, var1);
      } else {
         Preconditions.checkNotNull(var1);
         Predicate var2 = keyPredicateOnEntries(var1);
         return (Map)(var0 instanceof Maps.AbstractFilteredMap?filterFiltered((Maps.AbstractFilteredMap)var0, var2):new Maps.FilteredKeyMap((Map)Preconditions.checkNotNull(var0), var1, var2));
      }
   }

   public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> var0, Predicate<? super K> var1) {
      return filterEntries(var0, keyPredicateOnEntries(var1));
   }

   @GwtIncompatible("NavigableMap")
   public static <K, V> NavigableMap<K, V> filterKeys(NavigableMap<K, V> var0, Predicate<? super K> var1) {
      return filterEntries(var0, keyPredicateOnEntries(var1));
   }

   public static <K, V> BiMap<K, V> filterKeys(BiMap<K, V> var0, Predicate<? super K> var1) {
      Preconditions.checkNotNull(var1);
      return filterEntries(var0, keyPredicateOnEntries(var1));
   }

   public static <K, V> Map<K, V> filterValues(Map<K, V> var0, Predicate<? super V> var1) {
      return (Map)(var0 instanceof SortedMap?filterValues((SortedMap)var0, var1):(var0 instanceof BiMap?filterValues((BiMap)var0, var1):filterEntries(var0, valuePredicateOnEntries(var1))));
   }

   public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> var0, Predicate<? super V> var1) {
      return filterEntries(var0, valuePredicateOnEntries(var1));
   }

   @GwtIncompatible("NavigableMap")
   public static <K, V> NavigableMap<K, V> filterValues(NavigableMap<K, V> var0, Predicate<? super V> var1) {
      return filterEntries(var0, valuePredicateOnEntries(var1));
   }

   public static <K, V> BiMap<K, V> filterValues(BiMap<K, V> var0, Predicate<? super V> var1) {
      return filterEntries(var0, valuePredicateOnEntries(var1));
   }

   public static <K, V> Map<K, V> filterEntries(Map<K, V> var0, Predicate<? super Entry<K, V>> var1) {
      if(var0 instanceof SortedMap) {
         return filterEntries((SortedMap)var0, var1);
      } else if(var0 instanceof BiMap) {
         return filterEntries((BiMap)var0, var1);
      } else {
         Preconditions.checkNotNull(var1);
         return (Map)(var0 instanceof Maps.AbstractFilteredMap?filterFiltered((Maps.AbstractFilteredMap)var0, var1):new Maps.FilteredEntryMap((Map)Preconditions.checkNotNull(var0), var1));
      }
   }

   public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> var0, Predicate<? super Entry<K, V>> var1) {
      return Platform.mapsFilterSortedMap(var0, var1);
   }

   static <K, V> SortedMap<K, V> filterSortedIgnoreNavigable(SortedMap<K, V> var0, Predicate<? super Entry<K, V>> var1) {
      Preconditions.checkNotNull(var1);
      return (SortedMap)(var0 instanceof Maps.FilteredEntrySortedMap?filterFiltered((Maps.FilteredEntrySortedMap)var0, var1):new Maps.FilteredEntrySortedMap((SortedMap)Preconditions.checkNotNull(var0), var1));
   }

   @GwtIncompatible("NavigableMap")
   public static <K, V> NavigableMap<K, V> filterEntries(NavigableMap<K, V> var0, Predicate<? super Entry<K, V>> var1) {
      Preconditions.checkNotNull(var1);
      return (NavigableMap)(var0 instanceof Maps.FilteredEntryNavigableMap?filterFiltered((Maps.FilteredEntryNavigableMap)var0, var1):new Maps.FilteredEntryNavigableMap((NavigableMap)Preconditions.checkNotNull(var0), var1));
   }

   public static <K, V> BiMap<K, V> filterEntries(BiMap<K, V> var0, Predicate<? super Entry<K, V>> var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return (BiMap)(var0 instanceof Maps.FilteredEntryBiMap?filterFiltered((Maps.FilteredEntryBiMap)var0, var1):new Maps.FilteredEntryBiMap(var0, var1));
   }

   private static <K, V> Map<K, V> filterFiltered(Maps.AbstractFilteredMap<K, V> var0, Predicate<? super Entry<K, V>> var1) {
      return new Maps.FilteredEntryMap(var0.unfiltered, Predicates.and(var0.predicate, var1));
   }

   private static <K, V> SortedMap<K, V> filterFiltered(Maps.FilteredEntrySortedMap<K, V> var0, Predicate<? super Entry<K, V>> var1) {
      Predicate var2 = Predicates.and(var0.predicate, var1);
      return new Maps.FilteredEntrySortedMap(var0.sortedMap(), var2);
   }

   @GwtIncompatible("NavigableMap")
   private static <K, V> NavigableMap<K, V> filterFiltered(Maps.FilteredEntryNavigableMap<K, V> var0, Predicate<? super Entry<K, V>> var1) {
      Predicate var2 = Predicates.and(var0.entryPredicate, var1);
      return new Maps.FilteredEntryNavigableMap(var0.unfiltered, var2);
   }

   private static <K, V> BiMap<K, V> filterFiltered(Maps.FilteredEntryBiMap<K, V> var0, Predicate<? super Entry<K, V>> var1) {
      Predicate var2 = Predicates.and(var0.predicate, var1);
      return new Maps.FilteredEntryBiMap(var0.unfiltered(), var2);
   }

   @GwtIncompatible("NavigableMap")
   public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, V> var0) {
      Preconditions.checkNotNull(var0);
      return (NavigableMap)(var0 instanceof Maps.UnmodifiableNavigableMap?var0:new Maps.UnmodifiableNavigableMap(var0));
   }

   @Nullable
   private static <K, V> Entry<K, V> unmodifiableOrNull(@Nullable Entry<K, V> var0) {
      return var0 == null?null:unmodifiableEntry(var0);
   }

   @GwtIncompatible("NavigableMap")
   public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> var0) {
      return Synchronized.navigableMap(var0);
   }

   static <V> V safeGet(Map<?, V> var0, @Nullable Object var1) {
      Preconditions.checkNotNull(var0);

      try {
         return var0.get(var1);
      } catch (ClassCastException var3) {
         return null;
      } catch (NullPointerException var4) {
         return null;
      }
   }

   static boolean safeContainsKey(Map<?, ?> var0, Object var1) {
      Preconditions.checkNotNull(var0);

      try {
         return var0.containsKey(var1);
      } catch (ClassCastException var3) {
         return false;
      } catch (NullPointerException var4) {
         return false;
      }
   }

   static <V> V safeRemove(Map<?, V> var0, Object var1) {
      Preconditions.checkNotNull(var0);

      try {
         return var0.remove(var1);
      } catch (ClassCastException var3) {
         return null;
      } catch (NullPointerException var4) {
         return null;
      }
   }

   static boolean containsKeyImpl(Map<?, ?> var0, @Nullable Object var1) {
      return Iterators.contains(keyIterator(var0.entrySet().iterator()), var1);
   }

   static boolean containsValueImpl(Map<?, ?> var0, @Nullable Object var1) {
      return Iterators.contains(valueIterator(var0.entrySet().iterator()), var1);
   }

   static <K, V> boolean containsEntryImpl(Collection<Entry<K, V>> var0, Object var1) {
      return !(var1 instanceof Entry)?false:var0.contains(unmodifiableEntry((Entry)var1));
   }

   static <K, V> boolean removeEntryImpl(Collection<Entry<K, V>> var0, Object var1) {
      return !(var1 instanceof Entry)?false:var0.remove(unmodifiableEntry((Entry)var1));
   }

   static boolean equalsImpl(Map<?, ?> var0, Object var1) {
      if(var0 == var1) {
         return true;
      } else if(var1 instanceof Map) {
         Map var2 = (Map)var1;
         return var0.entrySet().equals(var2.entrySet());
      } else {
         return false;
      }
   }

   static String toStringImpl(Map<?, ?> var0) {
      StringBuilder var1 = Collections2.newStringBuilderForCollection(var0.size()).append('{');
      STANDARD_JOINER.appendTo(var1, var0);
      return var1.append('}').toString();
   }

   static <K, V> void putAllImpl(Map<K, V> var0, Map<? extends K, ? extends V> var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var0.put(var3.getKey(), var3.getValue());
      }

   }

   @Nullable
   static <K> K keyOrNull(@Nullable Entry<K, ?> var0) {
      return var0 == null?null:var0.getKey();
   }

   @Nullable
   static <V> V valueOrNull(@Nullable Entry<?, V> var0) {
      return var0 == null?null:var0.getValue();
   }

   static {
      STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");
   }

   @GwtIncompatible("NavigableMap")
   abstract static class DescendingMap<K, V> extends ForwardingMap<K, V> implements NavigableMap<K, V> {
      private transient Comparator<? super K> comparator;
      private transient Set<Entry<K, V>> entrySet;
      private transient NavigableSet<K> navigableKeySet;

      DescendingMap() {
      }

      abstract NavigableMap<K, V> forward();

      protected final Map<K, V> delegate() {
         return this.forward();
      }

      public Comparator<? super K> comparator() {
         Comparator var1 = this.comparator;
         if(var1 == null) {
            Object var2 = this.forward().comparator();
            if(var2 == null) {
               var2 = Ordering.natural();
            }

            var1 = this.comparator = reverse((Comparator)var2);
         }

         return var1;
      }

      private static <T> Ordering<T> reverse(Comparator<T> var0) {
         return Ordering.from(var0).reverse();
      }

      public K firstKey() {
         return this.forward().lastKey();
      }

      public K lastKey() {
         return this.forward().firstKey();
      }

      public Entry<K, V> lowerEntry(K var1) {
         return this.forward().higherEntry(var1);
      }

      public K lowerKey(K var1) {
         return this.forward().higherKey(var1);
      }

      public Entry<K, V> floorEntry(K var1) {
         return this.forward().ceilingEntry(var1);
      }

      public K floorKey(K var1) {
         return this.forward().ceilingKey(var1);
      }

      public Entry<K, V> ceilingEntry(K var1) {
         return this.forward().floorEntry(var1);
      }

      public K ceilingKey(K var1) {
         return this.forward().floorKey(var1);
      }

      public Entry<K, V> higherEntry(K var1) {
         return this.forward().lowerEntry(var1);
      }

      public K higherKey(K var1) {
         return this.forward().lowerKey(var1);
      }

      public Entry<K, V> firstEntry() {
         return this.forward().lastEntry();
      }

      public Entry<K, V> lastEntry() {
         return this.forward().firstEntry();
      }

      public Entry<K, V> pollFirstEntry() {
         return this.forward().pollLastEntry();
      }

      public Entry<K, V> pollLastEntry() {
         return this.forward().pollFirstEntry();
      }

      public NavigableMap<K, V> descendingMap() {
         return this.forward();
      }

      public Set<Entry<K, V>> entrySet() {
         Set var1 = this.entrySet;
         return var1 == null?(this.entrySet = this.createEntrySet()):var1;
      }

      abstract Iterator<Entry<K, V>> entryIterator();

      Set<Entry<K, V>> createEntrySet() {
         return new Maps.EntrySet() {
            Map<K, V> map() {
               return DescendingMap.this;
            }

            public Iterator<Entry<K, V>> iterator() {
               return DescendingMap.this.entryIterator();
            }
         };
      }

      public Set<K> keySet() {
         return this.navigableKeySet();
      }

      public NavigableSet<K> navigableKeySet() {
         NavigableSet var1 = this.navigableKeySet;
         return var1 == null?(this.navigableKeySet = new Maps.NavigableKeySet(this)):var1;
      }

      public NavigableSet<K> descendingKeySet() {
         return this.forward().navigableKeySet();
      }

      public NavigableMap<K, V> subMap(K var1, boolean var2, K var3, boolean var4) {
         return this.forward().subMap(var3, var4, var1, var2).descendingMap();
      }

      public NavigableMap<K, V> headMap(K var1, boolean var2) {
         return this.forward().tailMap(var1, var2).descendingMap();
      }

      public NavigableMap<K, V> tailMap(K var1, boolean var2) {
         return this.forward().headMap(var1, var2).descendingMap();
      }

      public SortedMap<K, V> subMap(K var1, K var2) {
         return this.subMap(var1, true, var2, false);
      }

      public SortedMap<K, V> headMap(K var1) {
         return this.headMap(var1, false);
      }

      public SortedMap<K, V> tailMap(K var1) {
         return this.tailMap(var1, true);
      }

      public Collection<V> values() {
         return new Maps.Values(this);
      }

      public String toString() {
         return this.standardToString();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   abstract static class EntrySet<K, V> extends Sets.ImprovedAbstractSet<Entry<K, V>> {
      EntrySet() {
      }

      abstract Map<K, V> map();

      public int size() {
         return this.map().size();
      }

      public void clear() {
         this.map().clear();
      }

      public boolean contains(Object var1) {
         if(!(var1 instanceof Entry)) {
            return false;
         } else {
            Entry var2 = (Entry)var1;
            Object var3 = var2.getKey();
            Object var4 = Maps.safeGet(this.map(), var3);
            return Objects.equal(var4, var2.getValue()) && (var4 != null || this.map().containsKey(var3));
         }
      }

      public boolean isEmpty() {
         return this.map().isEmpty();
      }

      public boolean remove(Object var1) {
         if(this.contains(var1)) {
            Entry var2 = (Entry)var1;
            return this.map().keySet().remove(var2.getKey());
         } else {
            return false;
         }
      }

      public boolean removeAll(Collection<?> var1) {
         try {
            return super.removeAll((Collection)Preconditions.checkNotNull(var1));
         } catch (UnsupportedOperationException var3) {
            return Sets.removeAllImpl(this, (Iterator)var1.iterator());
         }
      }

      public boolean retainAll(Collection<?> var1) {
         try {
            return super.retainAll((Collection)Preconditions.checkNotNull(var1));
         } catch (UnsupportedOperationException var7) {
            HashSet var3 = Sets.newHashSetWithExpectedSize(var1.size());
            Iterator var4 = var1.iterator();

            while(var4.hasNext()) {
               Object var5 = var4.next();
               if(this.contains(var5)) {
                  Entry var6 = (Entry)var5;
                  var3.add(var6.getKey());
               }
            }

            return this.map().keySet().retainAll(var3);
         }
      }
   }

   static class Values<K, V> extends AbstractCollection<V> {
      final Map<K, V> map;

      Values(Map<K, V> var1) {
         this.map = (Map)Preconditions.checkNotNull(var1);
      }

      final Map<K, V> map() {
         return this.map;
      }

      public Iterator<V> iterator() {
         return Maps.valueIterator(this.map().entrySet().iterator());
      }

      public boolean remove(Object var1) {
         try {
            return super.remove(var1);
         } catch (UnsupportedOperationException var5) {
            Iterator var3 = this.map().entrySet().iterator();

            Entry var4;
            do {
               if(!var3.hasNext()) {
                  return false;
               }

               var4 = (Entry)var3.next();
            } while(!Objects.equal(var1, var4.getValue()));

            this.map().remove(var4.getKey());
            return true;
         }
      }

      public boolean removeAll(Collection<?> var1) {
         try {
            return super.removeAll((Collection)Preconditions.checkNotNull(var1));
         } catch (UnsupportedOperationException var6) {
            HashSet var3 = Sets.newHashSet();
            Iterator var4 = this.map().entrySet().iterator();

            while(var4.hasNext()) {
               Entry var5 = (Entry)var4.next();
               if(var1.contains(var5.getValue())) {
                  var3.add(var5.getKey());
               }
            }

            return this.map().keySet().removeAll(var3);
         }
      }

      public boolean retainAll(Collection<?> var1) {
         try {
            return super.retainAll((Collection)Preconditions.checkNotNull(var1));
         } catch (UnsupportedOperationException var6) {
            HashSet var3 = Sets.newHashSet();
            Iterator var4 = this.map().entrySet().iterator();

            while(var4.hasNext()) {
               Entry var5 = (Entry)var4.next();
               if(var1.contains(var5.getValue())) {
                  var3.add(var5.getKey());
               }
            }

            return this.map().keySet().retainAll(var3);
         }
      }

      public int size() {
         return this.map().size();
      }

      public boolean isEmpty() {
         return this.map().isEmpty();
      }

      public boolean contains(@Nullable Object var1) {
         return this.map().containsValue(var1);
      }

      public void clear() {
         this.map().clear();
      }
   }

   @GwtIncompatible("NavigableMap")
   static class NavigableKeySet<K, V> extends Maps.SortedKeySet<K, V> implements NavigableSet<K> {
      NavigableKeySet(NavigableMap<K, V> var1) {
         super(var1);
      }

      NavigableMap<K, V> map() {
         return (NavigableMap)this.map;
      }

      public K lower(K var1) {
         return this.map().lowerKey(var1);
      }

      public K floor(K var1) {
         return this.map().floorKey(var1);
      }

      public K ceiling(K var1) {
         return this.map().ceilingKey(var1);
      }

      public K higher(K var1) {
         return this.map().higherKey(var1);
      }

      public K pollFirst() {
         return Maps.keyOrNull(this.map().pollFirstEntry());
      }

      public K pollLast() {
         return Maps.keyOrNull(this.map().pollLastEntry());
      }

      public NavigableSet<K> descendingSet() {
         return this.map().descendingKeySet();
      }

      public Iterator<K> descendingIterator() {
         return this.descendingSet().iterator();
      }

      public NavigableSet<K> subSet(K var1, boolean var2, K var3, boolean var4) {
         return this.map().subMap(var1, var2, var3, var4).navigableKeySet();
      }

      public NavigableSet<K> headSet(K var1, boolean var2) {
         return this.map().headMap(var1, var2).navigableKeySet();
      }

      public NavigableSet<K> tailSet(K var1, boolean var2) {
         return this.map().tailMap(var1, var2).navigableKeySet();
      }

      public SortedSet<K> subSet(K var1, K var2) {
         return this.subSet(var1, true, var2, false);
      }

      public SortedSet<K> headSet(K var1) {
         return this.headSet(var1, false);
      }

      public SortedSet<K> tailSet(K var1) {
         return this.tailSet(var1, true);
      }

      // $FF: synthetic method
      // $FF: bridge method
      SortedMap map() {
         return this.map();
      }

      // $FF: synthetic method
      // $FF: bridge method
      Map map() {
         return this.map();
      }
   }

   static class SortedKeySet<K, V> extends Maps.KeySet<K, V> implements SortedSet<K> {
      SortedKeySet(SortedMap<K, V> var1) {
         super(var1);
      }

      SortedMap<K, V> map() {
         return (SortedMap)super.map();
      }

      public Comparator<? super K> comparator() {
         return this.map().comparator();
      }

      public SortedSet<K> subSet(K var1, K var2) {
         return new Maps.SortedKeySet(this.map().subMap(var1, var2));
      }

      public SortedSet<K> headSet(K var1) {
         return new Maps.SortedKeySet(this.map().headMap(var1));
      }

      public SortedSet<K> tailSet(K var1) {
         return new Maps.SortedKeySet(this.map().tailMap(var1));
      }

      public K first() {
         return this.map().firstKey();
      }

      public K last() {
         return this.map().lastKey();
      }

      // $FF: synthetic method
      // $FF: bridge method
      Map map() {
         return this.map();
      }
   }

   static class KeySet<K, V> extends Sets.ImprovedAbstractSet<K> {
      final Map<K, V> map;

      KeySet(Map<K, V> var1) {
         this.map = (Map)Preconditions.checkNotNull(var1);
      }

      Map<K, V> map() {
         return this.map;
      }

      public Iterator<K> iterator() {
         return Maps.keyIterator(this.map().entrySet().iterator());
      }

      public int size() {
         return this.map().size();
      }

      public boolean isEmpty() {
         return this.map().isEmpty();
      }

      public boolean contains(Object var1) {
         return this.map().containsKey(var1);
      }

      public boolean remove(Object var1) {
         if(this.contains(var1)) {
            this.map().remove(var1);
            return true;
         } else {
            return false;
         }
      }

      public void clear() {
         this.map().clear();
      }
   }

   @GwtCompatible
   abstract static class ImprovedAbstractMap<K, V> extends AbstractMap<K, V> {
      private transient Set<Entry<K, V>> entrySet;
      private transient Set<K> keySet;
      private transient Collection<V> values;

      ImprovedAbstractMap() {
      }

      abstract Set<Entry<K, V>> createEntrySet();

      public Set<Entry<K, V>> entrySet() {
         Set var1 = this.entrySet;
         return var1 == null?(this.entrySet = this.createEntrySet()):var1;
      }

      public Set<K> keySet() {
         Set var1 = this.keySet;
         return var1 == null?(this.keySet = this.createKeySet()):var1;
      }

      Set<K> createKeySet() {
         return new Maps.KeySet(this);
      }

      public Collection<V> values() {
         Collection var1 = this.values;
         return var1 == null?(this.values = this.createValues()):var1;
      }

      Collection<V> createValues() {
         return new Maps.Values(this);
      }
   }

   @GwtIncompatible("NavigableMap")
   static class UnmodifiableNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
      private final NavigableMap<K, V> delegate;
      private transient Maps.UnmodifiableNavigableMap<K, V> descendingMap;

      UnmodifiableNavigableMap(NavigableMap<K, V> var1) {
         this.delegate = var1;
      }

      UnmodifiableNavigableMap(NavigableMap<K, V> var1, Maps.UnmodifiableNavigableMap<K, V> var2) {
         this.delegate = var1;
         this.descendingMap = var2;
      }

      protected SortedMap<K, V> delegate() {
         return Collections.unmodifiableSortedMap(this.delegate);
      }

      public Entry<K, V> lowerEntry(K var1) {
         return Maps.unmodifiableOrNull(this.delegate.lowerEntry(var1));
      }

      public K lowerKey(K var1) {
         return this.delegate.lowerKey(var1);
      }

      public Entry<K, V> floorEntry(K var1) {
         return Maps.unmodifiableOrNull(this.delegate.floorEntry(var1));
      }

      public K floorKey(K var1) {
         return this.delegate.floorKey(var1);
      }

      public Entry<K, V> ceilingEntry(K var1) {
         return Maps.unmodifiableOrNull(this.delegate.ceilingEntry(var1));
      }

      public K ceilingKey(K var1) {
         return this.delegate.ceilingKey(var1);
      }

      public Entry<K, V> higherEntry(K var1) {
         return Maps.unmodifiableOrNull(this.delegate.higherEntry(var1));
      }

      public K higherKey(K var1) {
         return this.delegate.higherKey(var1);
      }

      public Entry<K, V> firstEntry() {
         return Maps.unmodifiableOrNull(this.delegate.firstEntry());
      }

      public Entry<K, V> lastEntry() {
         return Maps.unmodifiableOrNull(this.delegate.lastEntry());
      }

      public final Entry<K, V> pollFirstEntry() {
         throw new UnsupportedOperationException();
      }

      public final Entry<K, V> pollLastEntry() {
         throw new UnsupportedOperationException();
      }

      public NavigableMap<K, V> descendingMap() {
         Maps.UnmodifiableNavigableMap var1 = this.descendingMap;
         return var1 == null?(this.descendingMap = new Maps.UnmodifiableNavigableMap(this.delegate.descendingMap(), this)):var1;
      }

      public Set<K> keySet() {
         return this.navigableKeySet();
      }

      public NavigableSet<K> navigableKeySet() {
         return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet());
      }

      public NavigableSet<K> descendingKeySet() {
         return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet());
      }

      public SortedMap<K, V> subMap(K var1, K var2) {
         return this.subMap(var1, true, var2, false);
      }

      public SortedMap<K, V> headMap(K var1) {
         return this.headMap(var1, false);
      }

      public SortedMap<K, V> tailMap(K var1) {
         return this.tailMap(var1, true);
      }

      public NavigableMap<K, V> subMap(K var1, boolean var2, K var3, boolean var4) {
         return Maps.unmodifiableNavigableMap(this.delegate.subMap(var1, var2, var3, var4));
      }

      public NavigableMap<K, V> headMap(K var1, boolean var2) {
         return Maps.unmodifiableNavigableMap(this.delegate.headMap(var1, var2));
      }

      public NavigableMap<K, V> tailMap(K var1, boolean var2) {
         return Maps.unmodifiableNavigableMap(this.delegate.tailMap(var1, var2));
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Map delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   static final class FilteredEntryBiMap<K, V> extends Maps.FilteredEntryMap<K, V> implements BiMap<K, V> {
      private final BiMap<V, K> inverse;

      private static <K, V> Predicate<Entry<V, K>> inversePredicate(final Predicate<? super Entry<K, V>> var0) {
         return new Predicate() {
            public boolean apply(Entry<V, K> var1) {
               return var0.apply(Maps.immutableEntry(var1.getValue(), var1.getKey()));
            }

            // $FF: synthetic method
            // $FF: bridge method
            public boolean apply(Object var1) {
               return this.apply((Entry)var1);
            }
         };
      }

      FilteredEntryBiMap(BiMap<K, V> var1, Predicate<? super Entry<K, V>> var2) {
         super(var1, var2);
         this.inverse = new Maps.FilteredEntryBiMap(var1.inverse(), inversePredicate(var2), this);
      }

      private FilteredEntryBiMap(BiMap<K, V> var1, Predicate<? super Entry<K, V>> var2, BiMap<V, K> var3) {
         super(var1, var2);
         this.inverse = var3;
      }

      BiMap<K, V> unfiltered() {
         return (BiMap)this.unfiltered;
      }

      public V forcePut(@Nullable K var1, @Nullable V var2) {
         Preconditions.checkArgument(this.apply(var1, var2));
         return this.unfiltered().forcePut(var1, var2);
      }

      public BiMap<V, K> inverse() {
         return this.inverse;
      }

      public Set<V> values() {
         return this.inverse.keySet();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection values() {
         return this.values();
      }
   }

   @GwtIncompatible("NavigableMap")
   private static class FilteredEntryNavigableMap<K, V> extends AbstractNavigableMap<K, V> {
      private final NavigableMap<K, V> unfiltered;
      private final Predicate<? super Entry<K, V>> entryPredicate;
      private final Map<K, V> filteredDelegate;

      FilteredEntryNavigableMap(NavigableMap<K, V> var1, Predicate<? super Entry<K, V>> var2) {
         this.unfiltered = (NavigableMap)Preconditions.checkNotNull(var1);
         this.entryPredicate = var2;
         this.filteredDelegate = new Maps.FilteredEntryMap(var1, var2);
      }

      public Comparator<? super K> comparator() {
         return this.unfiltered.comparator();
      }

      public NavigableSet<K> navigableKeySet() {
         return new Maps.NavigableKeySet(this) {
            public boolean removeAll(Collection<?> var1) {
               return Iterators.removeIf(FilteredEntryNavigableMap.this.unfiltered.entrySet().iterator(), Predicates.and(FilteredEntryNavigableMap.this.entryPredicate, Maps.keyPredicateOnEntries(Predicates.in(var1))));
            }

            public boolean retainAll(Collection<?> var1) {
               return Iterators.removeIf(FilteredEntryNavigableMap.this.unfiltered.entrySet().iterator(), Predicates.and(FilteredEntryNavigableMap.this.entryPredicate, Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(var1)))));
            }
         };
      }

      public Collection<V> values() {
         return new Maps.FilteredMapValues(this, this.unfiltered, this.entryPredicate);
      }

      Iterator<Entry<K, V>> entryIterator() {
         return Iterators.filter(this.unfiltered.entrySet().iterator(), this.entryPredicate);
      }

      Iterator<Entry<K, V>> descendingEntryIterator() {
         return Iterators.filter(this.unfiltered.descendingMap().entrySet().iterator(), this.entryPredicate);
      }

      public int size() {
         return this.filteredDelegate.size();
      }

      @Nullable
      public V get(@Nullable Object var1) {
         return this.filteredDelegate.get(var1);
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.filteredDelegate.containsKey(var1);
      }

      public V put(K var1, V var2) {
         return this.filteredDelegate.put(var1, var2);
      }

      public V remove(@Nullable Object var1) {
         return this.filteredDelegate.remove(var1);
      }

      public void putAll(Map<? extends K, ? extends V> var1) {
         this.filteredDelegate.putAll(var1);
      }

      public void clear() {
         this.filteredDelegate.clear();
      }

      public Set<Entry<K, V>> entrySet() {
         return this.filteredDelegate.entrySet();
      }

      public Entry<K, V> pollFirstEntry() {
         return (Entry)Iterables.removeFirstMatching(this.unfiltered.entrySet(), this.entryPredicate);
      }

      public Entry<K, V> pollLastEntry() {
         return (Entry)Iterables.removeFirstMatching(this.unfiltered.descendingMap().entrySet(), this.entryPredicate);
      }

      public NavigableMap<K, V> descendingMap() {
         return Maps.filterEntries(this.unfiltered.descendingMap(), this.entryPredicate);
      }

      public NavigableMap<K, V> subMap(K var1, boolean var2, K var3, boolean var4) {
         return Maps.filterEntries(this.unfiltered.subMap(var1, var2, var3, var4), this.entryPredicate);
      }

      public NavigableMap<K, V> headMap(K var1, boolean var2) {
         return Maps.filterEntries(this.unfiltered.headMap(var1, var2), this.entryPredicate);
      }

      public NavigableMap<K, V> tailMap(K var1, boolean var2) {
         return Maps.filterEntries(this.unfiltered.tailMap(var1, var2), this.entryPredicate);
      }
   }

   private static class FilteredEntrySortedMap<K, V> extends Maps.FilteredEntryMap<K, V> implements SortedMap<K, V> {
      FilteredEntrySortedMap(SortedMap<K, V> var1, Predicate<? super Entry<K, V>> var2) {
         super(var1, var2);
      }

      SortedMap<K, V> sortedMap() {
         return (SortedMap)this.unfiltered;
      }

      public SortedSet<K> keySet() {
         return (SortedSet)super.keySet();
      }

      SortedSet<K> createKeySet() {
         return new Maps.FilteredEntrySortedMap.FilteredEntrySortedMap$SortedKeySet();
      }

      public Comparator<? super K> comparator() {
         return this.sortedMap().comparator();
      }

      public K firstKey() {
         return this.keySet().iterator().next();
      }

      public K lastKey() {
         SortedMap var1 = this.sortedMap();

         while(true) {
            Object var2 = var1.lastKey();
            if(this.apply(var2, this.unfiltered.get(var2))) {
               return var2;
            }

            var1 = this.sortedMap().headMap(var2);
         }
      }

      public SortedMap<K, V> headMap(K var1) {
         return new Maps.FilteredEntrySortedMap(this.sortedMap().headMap(var1), this.predicate);
      }

      public SortedMap<K, V> subMap(K var1, K var2) {
         return new Maps.FilteredEntrySortedMap(this.sortedMap().subMap(var1, var2), this.predicate);
      }

      public SortedMap<K, V> tailMap(K var1) {
         return new Maps.FilteredEntrySortedMap(this.sortedMap().tailMap(var1), this.predicate);
      }

      // $FF: synthetic method
      // $FF: bridge method
      Set createKeySet() {
         return this.createKeySet();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Set keySet() {
         return this.keySet();
      }

      class FilteredEntrySortedMap$SortedKeySet extends Maps.FilteredEntryMap<K, V>.KeySet implements SortedSet<K> {
         FilteredEntrySortedMap$SortedKeySet() {
            super();
         }

         public Comparator<? super K> comparator() {
            return FilteredEntrySortedMap.this.sortedMap().comparator();
         }

         public SortedSet<K> subSet(K var1, K var2) {
            return (SortedSet)FilteredEntrySortedMap.this.subMap(var1, var2).keySet();
         }

         public SortedSet<K> headSet(K var1) {
            return (SortedSet)FilteredEntrySortedMap.this.headMap(var1).keySet();
         }

         public SortedSet<K> tailSet(K var1) {
            return (SortedSet)FilteredEntrySortedMap.this.tailMap(var1).keySet();
         }

         public K first() {
            return FilteredEntrySortedMap.this.firstKey();
         }

         public K last() {
            return FilteredEntrySortedMap.this.lastKey();
         }
      }
   }

   static class FilteredEntryMap<K, V> extends Maps.AbstractFilteredMap<K, V> {
      final Set<Entry<K, V>> filteredEntrySet;

      FilteredEntryMap(Map<K, V> var1, Predicate<? super Entry<K, V>> var2) {
         super(var1, var2);
         this.filteredEntrySet = Sets.filter(var1.entrySet(), this.predicate);
      }

      protected Set<Entry<K, V>> createEntrySet() {
         return new Maps.FilteredEntryMap.FilteredEntryMap$EntrySet(null);
      }

      Set<K> createKeySet() {
         return new Maps.FilteredEntryMap.FilteredEntryMap$KeySet();
      }

      class FilteredEntryMap$KeySet extends Maps.KeySet<K, V> {
         FilteredEntryMap$KeySet() {
            super(FilteredEntryMap.this);
         }

         public boolean remove(Object var1) {
            if(FilteredEntryMap.this.containsKey(var1)) {
               FilteredEntryMap.this.unfiltered.remove(var1);
               return true;
            } else {
               return false;
            }
         }

         private boolean removeIf(Predicate<? super K> var1) {
            return Iterables.removeIf(FilteredEntryMap.this.unfiltered.entrySet(), Predicates.and(FilteredEntryMap.this.predicate, Maps.keyPredicateOnEntries(var1)));
         }

         public boolean removeAll(Collection<?> var1) {
            return this.removeIf(Predicates.in(var1));
         }

         public boolean retainAll(Collection<?> var1) {
            return this.removeIf(Predicates.not(Predicates.in(var1)));
         }

         public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
         }

         public <T> T[] toArray(T[] var1) {
            return Lists.newArrayList(this.iterator()).toArray(var1);
         }
      }

      private class FilteredEntryMap$EntrySet extends ForwardingSet<Entry<K, V>> {
         private FilteredEntryMap$EntrySet() {
         }

         protected Set<Entry<K, V>> delegate() {
            return FilteredEntryMap.this.filteredEntrySet;
         }

         public Iterator<Entry<K, V>> iterator() {
            return new TransformedIterator(FilteredEntryMap.this.filteredEntrySet.iterator()) {
               Entry<K, V> transform(final Entry<K, V> var1) {
                  return new ForwardingMapEntry() {
                     protected Entry<K, V> delegate() {
                        return var1;
                     }

                     public V setValue(V var1x) {
                        Preconditions.checkArgument(FilteredEntryMap.this.apply(this.getKey(), var1x));
                        return super.setValue(var1x);
                     }

                     // $FF: synthetic method
                     // $FF: bridge method
                     protected Object delegate() {
                        return this.delegate();
                     }
                  };
               }

               // $FF: synthetic method
               // $FF: bridge method
               Object transform(Object var1) {
                  return this.transform((Entry)var1);
               }
            };
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Collection delegate() {
            return this.delegate();
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object delegate() {
            return this.delegate();
         }

         // $FF: synthetic method
         FilteredEntryMap$EntrySet(Object var2) {
            this();
         }
      }
   }

   private static class FilteredKeyMap<K, V> extends Maps.AbstractFilteredMap<K, V> {
      Predicate<? super K> keyPredicate;

      FilteredKeyMap(Map<K, V> var1, Predicate<? super K> var2, Predicate<? super Entry<K, V>> var3) {
         super(var1, var3);
         this.keyPredicate = var2;
      }

      protected Set<Entry<K, V>> createEntrySet() {
         return Sets.filter(this.unfiltered.entrySet(), this.predicate);
      }

      Set<K> createKeySet() {
         return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
      }

      public boolean containsKey(Object var1) {
         return this.unfiltered.containsKey(var1) && this.keyPredicate.apply(var1);
      }
   }

   private static final class FilteredMapValues<K, V> extends Maps.Values<K, V> {
      Map<K, V> unfiltered;
      Predicate<? super Entry<K, V>> predicate;

      FilteredMapValues(Map<K, V> var1, Map<K, V> var2, Predicate<? super Entry<K, V>> var3) {
         super(var1);
         this.unfiltered = var2;
         this.predicate = var3;
      }

      public boolean remove(Object var1) {
         return Iterables.removeFirstMatching(this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(Predicates.equalTo(var1)))) != null;
      }

      private boolean removeIf(Predicate<? super V> var1) {
         return Iterables.removeIf(this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(var1)));
      }

      public boolean removeAll(Collection<?> var1) {
         return this.removeIf(Predicates.in(var1));
      }

      public boolean retainAll(Collection<?> var1) {
         return this.removeIf(Predicates.not(Predicates.in(var1)));
      }

      public Object[] toArray() {
         return Lists.newArrayList(this.iterator()).toArray();
      }

      public <T> T[] toArray(T[] var1) {
         return Lists.newArrayList(this.iterator()).toArray(var1);
      }
   }

   private abstract static class AbstractFilteredMap<K, V> extends Maps.ImprovedAbstractMap<K, V> {
      final Map<K, V> unfiltered;
      final Predicate<? super Entry<K, V>> predicate;

      AbstractFilteredMap(Map<K, V> var1, Predicate<? super Entry<K, V>> var2) {
         this.unfiltered = var1;
         this.predicate = var2;
      }

      boolean apply(@Nullable Object var1, @Nullable V var2) {
         return this.predicate.apply(Maps.immutableEntry(var1, var2));
      }

      public V put(K var1, V var2) {
         Preconditions.checkArgument(this.apply(var1, var2));
         return this.unfiltered.put(var1, var2);
      }

      public void putAll(Map<? extends K, ? extends V> var1) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            Preconditions.checkArgument(this.apply(var3.getKey(), var3.getValue()));
         }

         this.unfiltered.putAll(var1);
      }

      public boolean containsKey(Object var1) {
         return this.unfiltered.containsKey(var1) && this.apply(var1, this.unfiltered.get(var1));
      }

      public V get(Object var1) {
         Object var2 = this.unfiltered.get(var1);
         return var2 != null && this.apply(var1, var2)?var2:null;
      }

      public boolean isEmpty() {
         return this.entrySet().isEmpty();
      }

      public V remove(Object var1) {
         return this.containsKey(var1)?this.unfiltered.remove(var1):null;
      }

      Collection<V> createValues() {
         return new Maps.FilteredMapValues(this, this.unfiltered, this.predicate);
      }
   }

   @GwtIncompatible("NavigableMap")
   private static class TransformedEntriesNavigableMap<K, V1, V2> extends Maps.TransformedEntriesSortedMap<K, V1, V2> implements NavigableMap<K, V2> {
      TransformedEntriesNavigableMap(NavigableMap<K, V1> var1, Maps.EntryTransformer<? super K, ? super V1, V2> var2) {
         super(var1, var2);
      }

      public Entry<K, V2> ceilingEntry(K var1) {
         return this.transformEntry(this.fromMap().ceilingEntry(var1));
      }

      public K ceilingKey(K var1) {
         return this.fromMap().ceilingKey(var1);
      }

      public NavigableSet<K> descendingKeySet() {
         return this.fromMap().descendingKeySet();
      }

      public NavigableMap<K, V2> descendingMap() {
         return Maps.transformEntries(this.fromMap().descendingMap(), this.transformer);
      }

      public Entry<K, V2> firstEntry() {
         return this.transformEntry(this.fromMap().firstEntry());
      }

      public Entry<K, V2> floorEntry(K var1) {
         return this.transformEntry(this.fromMap().floorEntry(var1));
      }

      public K floorKey(K var1) {
         return this.fromMap().floorKey(var1);
      }

      public NavigableMap<K, V2> headMap(K var1) {
         return this.headMap(var1, false);
      }

      public NavigableMap<K, V2> headMap(K var1, boolean var2) {
         return Maps.transformEntries(this.fromMap().headMap(var1, var2), this.transformer);
      }

      public Entry<K, V2> higherEntry(K var1) {
         return this.transformEntry(this.fromMap().higherEntry(var1));
      }

      public K higherKey(K var1) {
         return this.fromMap().higherKey(var1);
      }

      public Entry<K, V2> lastEntry() {
         return this.transformEntry(this.fromMap().lastEntry());
      }

      public Entry<K, V2> lowerEntry(K var1) {
         return this.transformEntry(this.fromMap().lowerEntry(var1));
      }

      public K lowerKey(K var1) {
         return this.fromMap().lowerKey(var1);
      }

      public NavigableSet<K> navigableKeySet() {
         return this.fromMap().navigableKeySet();
      }

      public Entry<K, V2> pollFirstEntry() {
         return this.transformEntry(this.fromMap().pollFirstEntry());
      }

      public Entry<K, V2> pollLastEntry() {
         return this.transformEntry(this.fromMap().pollLastEntry());
      }

      public NavigableMap<K, V2> subMap(K var1, boolean var2, K var3, boolean var4) {
         return Maps.transformEntries(this.fromMap().subMap(var1, var2, var3, var4), this.transformer);
      }

      public NavigableMap<K, V2> subMap(K var1, K var2) {
         return this.subMap(var1, true, var2, false);
      }

      public NavigableMap<K, V2> tailMap(K var1) {
         return this.tailMap(var1, true);
      }

      public NavigableMap<K, V2> tailMap(K var1, boolean var2) {
         return Maps.transformEntries(this.fromMap().tailMap(var1, var2), this.transformer);
      }

      @Nullable
      private Entry<K, V2> transformEntry(@Nullable Entry<K, V1> var1) {
         return var1 == null?null:Maps.transformEntry(this.transformer, var1);
      }

      protected NavigableMap<K, V1> fromMap() {
         return (NavigableMap)super.fromMap();
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
      protected SortedMap fromMap() {
         return this.fromMap();
      }
   }

   static class TransformedEntriesSortedMap<K, V1, V2> extends Maps.TransformedEntriesMap<K, V1, V2> implements SortedMap<K, V2> {
      protected SortedMap<K, V1> fromMap() {
         return (SortedMap)this.fromMap;
      }

      TransformedEntriesSortedMap(SortedMap<K, V1> var1, Maps.EntryTransformer<? super K, ? super V1, V2> var2) {
         super(var1, var2);
      }

      public Comparator<? super K> comparator() {
         return this.fromMap().comparator();
      }

      public K firstKey() {
         return this.fromMap().firstKey();
      }

      public SortedMap<K, V2> headMap(K var1) {
         return Platform.mapsTransformEntriesSortedMap(this.fromMap().headMap(var1), this.transformer);
      }

      public K lastKey() {
         return this.fromMap().lastKey();
      }

      public SortedMap<K, V2> subMap(K var1, K var2) {
         return Platform.mapsTransformEntriesSortedMap(this.fromMap().subMap(var1, var2), this.transformer);
      }

      public SortedMap<K, V2> tailMap(K var1) {
         return Platform.mapsTransformEntriesSortedMap(this.fromMap().tailMap(var1), this.transformer);
      }
   }

   static class TransformedEntriesMap<K, V1, V2> extends Maps.ImprovedAbstractMap<K, V2> {
      final Map<K, V1> fromMap;
      final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;

      TransformedEntriesMap(Map<K, V1> var1, Maps.EntryTransformer<? super K, ? super V1, V2> var2) {
         this.fromMap = (Map)Preconditions.checkNotNull(var1);
         this.transformer = (Maps.EntryTransformer)Preconditions.checkNotNull(var2);
      }

      public int size() {
         return this.fromMap.size();
      }

      public boolean containsKey(Object var1) {
         return this.fromMap.containsKey(var1);
      }

      public V2 get(Object var1) {
         Object var2 = this.fromMap.get(var1);
         return var2 == null && !this.fromMap.containsKey(var1)?null:this.transformer.transformEntry(var1, var2);
      }

      public V2 remove(Object var1) {
         return this.fromMap.containsKey(var1)?this.transformer.transformEntry(var1, this.fromMap.remove(var1)):null;
      }

      public void clear() {
         this.fromMap.clear();
      }

      public Set<K> keySet() {
         return this.fromMap.keySet();
      }

      protected Set<Entry<K, V2>> createEntrySet() {
         return new Maps.EntrySet() {
            Map<K, V2> map() {
               return TransformedEntriesMap.this;
            }

            public Iterator<Entry<K, V2>> iterator() {
               return Iterators.transform(TransformedEntriesMap.this.fromMap.entrySet().iterator(), Maps.asEntryToEntryFunction(TransformedEntriesMap.this.transformer));
            }
         };
      }
   }

   public interface EntryTransformer<K, V1, V2> {
      V2 transformEntry(@Nullable K var1, @Nullable V1 var2);
   }

   private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
      final Map<K, V> unmodifiableMap;
      final BiMap<? extends K, ? extends V> delegate;
      BiMap<V, K> inverse;
      transient Set<V> values;
      private static final long serialVersionUID = 0L;

      UnmodifiableBiMap(BiMap<? extends K, ? extends V> var1, @Nullable BiMap<V, K> var2) {
         this.unmodifiableMap = Collections.unmodifiableMap(var1);
         this.delegate = var1;
         this.inverse = var2;
      }

      protected Map<K, V> delegate() {
         return this.unmodifiableMap;
      }

      public V forcePut(K var1, V var2) {
         throw new UnsupportedOperationException();
      }

      public BiMap<V, K> inverse() {
         BiMap var1 = this.inverse;
         return var1 == null?(this.inverse = new Maps.UnmodifiableBiMap(this.delegate.inverse(), this)):var1;
      }

      public Set<V> values() {
         Set var1 = this.values;
         return var1 == null?(this.values = Collections.unmodifiableSet(this.delegate.values())):var1;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection values() {
         return this.values();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   private static final class BiMapConverter<A, B> extends Converter<A, B> implements Serializable {
      private final BiMap<A, B> bimap;
      private static final long serialVersionUID = 0L;

      BiMapConverter(BiMap<A, B> var1) {
         this.bimap = (BiMap)Preconditions.checkNotNull(var1);
      }

      protected B doForward(A var1) {
         return convert(this.bimap, var1);
      }

      protected A doBackward(B var1) {
         return convert(this.bimap.inverse(), var1);
      }

      private static <X, Y> Y convert(BiMap<X, Y> var0, X var1) {
         Object var2 = var0.get(var1);
         Preconditions.checkArgument(var2 != null, "No non-null mapping present for input: %s", new Object[]{var1});
         return var2;
      }

      public boolean equals(@Nullable Object var1) {
         if(var1 instanceof Maps.BiMapConverter) {
            Maps.BiMapConverter var2 = (Maps.BiMapConverter)var1;
            return this.bimap.equals(var2.bimap);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.bimap.hashCode();
      }

      public String toString() {
         return "Maps.asConverter(" + this.bimap + ")";
      }
   }

   static class UnmodifiableEntrySet<K, V> extends Maps.UnmodifiableEntries<K, V> implements Set<Entry<K, V>> {
      UnmodifiableEntrySet(Set<Entry<K, V>> var1) {
         super(var1);
      }

      public boolean equals(@Nullable Object var1) {
         return Sets.equalsImpl(this, var1);
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }
   }

   static class UnmodifiableEntries<K, V> extends ForwardingCollection<Entry<K, V>> {
      private final Collection<Entry<K, V>> entries;

      UnmodifiableEntries(Collection<Entry<K, V>> var1) {
         this.entries = var1;
      }

      protected Collection<Entry<K, V>> delegate() {
         return this.entries;
      }

      public Iterator<Entry<K, V>> iterator() {
         final Iterator var1 = super.iterator();
         return new UnmodifiableIterator() {
            public boolean hasNext() {
               return var1.hasNext();
            }

            public Entry<K, V> next() {
               return Maps.unmodifiableEntry((Entry)var1.next());
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object next() {
               return this.next();
            }
         };
      }

      public Object[] toArray() {
         return this.standardToArray();
      }

      public <T> T[] toArray(T[] var1) {
         return this.standardToArray(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   @GwtIncompatible("NavigableMap")
   private static final class NavigableAsMapView<K, V> extends AbstractNavigableMap<K, V> {
      private final NavigableSet<K> set;
      private final Function<? super K, V> function;

      NavigableAsMapView(NavigableSet<K> var1, Function<? super K, V> var2) {
         this.set = (NavigableSet)Preconditions.checkNotNull(var1);
         this.function = (Function)Preconditions.checkNotNull(var2);
      }

      public NavigableMap<K, V> subMap(K var1, boolean var2, K var3, boolean var4) {
         return Maps.asMap(this.set.subSet(var1, var2, var3, var4), this.function);
      }

      public NavigableMap<K, V> headMap(K var1, boolean var2) {
         return Maps.asMap(this.set.headSet(var1, var2), this.function);
      }

      public NavigableMap<K, V> tailMap(K var1, boolean var2) {
         return Maps.asMap(this.set.tailSet(var1, var2), this.function);
      }

      public Comparator<? super K> comparator() {
         return this.set.comparator();
      }

      @Nullable
      public V get(@Nullable Object var1) {
         return Collections2.safeContains(this.set, var1)?this.function.apply(var1):null;
      }

      public void clear() {
         this.set.clear();
      }

      Iterator<Entry<K, V>> entryIterator() {
         return Maps.asMapEntryIterator(this.set, this.function);
      }

      Iterator<Entry<K, V>> descendingEntryIterator() {
         return this.descendingMap().entrySet().iterator();
      }

      public NavigableSet<K> navigableKeySet() {
         return Maps.removeOnlyNavigableSet(this.set);
      }

      public int size() {
         return this.set.size();
      }

      public NavigableMap<K, V> descendingMap() {
         return Maps.asMap(this.set.descendingSet(), this.function);
      }
   }

   private static class SortedAsMapView<K, V> extends Maps.AsMapView<K, V> implements SortedMap<K, V> {
      SortedAsMapView(SortedSet<K> var1, Function<? super K, V> var2) {
         super(var1, var2);
      }

      SortedSet<K> backingSet() {
         return (SortedSet)super.backingSet();
      }

      public Comparator<? super K> comparator() {
         return this.backingSet().comparator();
      }

      public Set<K> keySet() {
         return Maps.removeOnlySortedSet(this.backingSet());
      }

      public SortedMap<K, V> subMap(K var1, K var2) {
         return Platform.mapsAsMapSortedSet(this.backingSet().subSet(var1, var2), this.function);
      }

      public SortedMap<K, V> headMap(K var1) {
         return Platform.mapsAsMapSortedSet(this.backingSet().headSet(var1), this.function);
      }

      public SortedMap<K, V> tailMap(K var1) {
         return Platform.mapsAsMapSortedSet(this.backingSet().tailSet(var1), this.function);
      }

      public K firstKey() {
         return this.backingSet().first();
      }

      public K lastKey() {
         return this.backingSet().last();
      }

      // $FF: synthetic method
      // $FF: bridge method
      Set backingSet() {
         return this.backingSet();
      }
   }

   private static class AsMapView<K, V> extends Maps.ImprovedAbstractMap<K, V> {
      private final Set<K> set;
      final Function<? super K, V> function;

      Set<K> backingSet() {
         return this.set;
      }

      AsMapView(Set<K> var1, Function<? super K, V> var2) {
         this.set = (Set)Preconditions.checkNotNull(var1);
         this.function = (Function)Preconditions.checkNotNull(var2);
      }

      public Set<K> createKeySet() {
         return Maps.removeOnlySet(this.backingSet());
      }

      Collection<V> createValues() {
         return Collections2.transform(this.set, this.function);
      }

      public int size() {
         return this.backingSet().size();
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.backingSet().contains(var1);
      }

      public V get(@Nullable Object var1) {
         return Collections2.safeContains(this.backingSet(), var1)?this.function.apply(var1):null;
      }

      public V remove(@Nullable Object var1) {
         return this.backingSet().remove(var1)?this.function.apply(var1):null;
      }

      public void clear() {
         this.backingSet().clear();
      }

      protected Set<Entry<K, V>> createEntrySet() {
         return new Maps.EntrySet() {
            Map<K, V> map() {
               return AsMapView.this;
            }

            public Iterator<Entry<K, V>> iterator() {
               return Maps.asMapEntryIterator(AsMapView.this.backingSet(), AsMapView.this.function);
            }
         };
      }
   }

   static class SortedMapDifferenceImpl<K, V> extends Maps.MapDifferenceImpl<K, V> implements SortedMapDifference<K, V> {
      SortedMapDifferenceImpl(SortedMap<K, V> var1, SortedMap<K, V> var2, SortedMap<K, V> var3, SortedMap<K, MapDifference.ValueDifference<V>> var4) {
         super(var1, var2, var3, var4);
      }

      public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering() {
         return (SortedMap)super.entriesDiffering();
      }

      public SortedMap<K, V> entriesInCommon() {
         return (SortedMap)super.entriesInCommon();
      }

      public SortedMap<K, V> entriesOnlyOnLeft() {
         return (SortedMap)super.entriesOnlyOnLeft();
      }

      public SortedMap<K, V> entriesOnlyOnRight() {
         return (SortedMap)super.entriesOnlyOnRight();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Map entriesDiffering() {
         return this.entriesDiffering();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Map entriesInCommon() {
         return this.entriesInCommon();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Map entriesOnlyOnRight() {
         return this.entriesOnlyOnRight();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Map entriesOnlyOnLeft() {
         return this.entriesOnlyOnLeft();
      }
   }

   static class ValueDifferenceImpl<V> implements MapDifference.ValueDifference<V> {
      private final V left;
      private final V right;

      static <V> MapDifference.ValueDifference<V> create(@Nullable V var0, @Nullable V var1) {
         return new Maps.ValueDifferenceImpl(var0, var1);
      }

      private ValueDifferenceImpl(@Nullable V var1, @Nullable V var2) {
         this.left = var1;
         this.right = var2;
      }

      public V leftValue() {
         return this.left;
      }

      public V rightValue() {
         return this.right;
      }

      public boolean equals(@Nullable Object var1) {
         if(!(var1 instanceof MapDifference.ValueDifference)) {
            return false;
         } else {
            MapDifference.ValueDifference var2 = (MapDifference.ValueDifference)var1;
            return Objects.equal(this.left, var2.leftValue()) && Objects.equal(this.right, var2.rightValue());
         }
      }

      public int hashCode() {
         return Objects.hashCode(new Object[]{this.left, this.right});
      }

      public String toString() {
         return "(" + this.left + ", " + this.right + ")";
      }
   }

   static class MapDifferenceImpl<K, V> implements MapDifference<K, V> {
      final Map<K, V> onlyOnLeft;
      final Map<K, V> onlyOnRight;
      final Map<K, V> onBoth;
      final Map<K, MapDifference.ValueDifference<V>> differences;

      MapDifferenceImpl(Map<K, V> var1, Map<K, V> var2, Map<K, V> var3, Map<K, MapDifference.ValueDifference<V>> var4) {
         this.onlyOnLeft = Maps.unmodifiableMap(var1);
         this.onlyOnRight = Maps.unmodifiableMap(var2);
         this.onBoth = Maps.unmodifiableMap(var3);
         this.differences = Maps.unmodifiableMap(var4);
      }

      public boolean areEqual() {
         return this.onlyOnLeft.isEmpty() && this.onlyOnRight.isEmpty() && this.differences.isEmpty();
      }

      public Map<K, V> entriesOnlyOnLeft() {
         return this.onlyOnLeft;
      }

      public Map<K, V> entriesOnlyOnRight() {
         return this.onlyOnRight;
      }

      public Map<K, V> entriesInCommon() {
         return this.onBoth;
      }

      public Map<K, MapDifference.ValueDifference<V>> entriesDiffering() {
         return this.differences;
      }

      public boolean equals(Object var1) {
         if(var1 == this) {
            return true;
         } else if(!(var1 instanceof MapDifference)) {
            return false;
         } else {
            MapDifference var2 = (MapDifference)var1;
            return this.entriesOnlyOnLeft().equals(var2.entriesOnlyOnLeft()) && this.entriesOnlyOnRight().equals(var2.entriesOnlyOnRight()) && this.entriesInCommon().equals(var2.entriesInCommon()) && this.entriesDiffering().equals(var2.entriesDiffering());
         }
      }

      public int hashCode() {
         return Objects.hashCode(new Object[]{this.entriesOnlyOnLeft(), this.entriesOnlyOnRight(), this.entriesInCommon(), this.entriesDiffering()});
      }

      public String toString() {
         if(this.areEqual()) {
            return "equal";
         } else {
            StringBuilder var1 = new StringBuilder("not equal");
            if(!this.onlyOnLeft.isEmpty()) {
               var1.append(": only on left=").append(this.onlyOnLeft);
            }

            if(!this.onlyOnRight.isEmpty()) {
               var1.append(": only on right=").append(this.onlyOnRight);
            }

            if(!this.differences.isEmpty()) {
               var1.append(": value differences=").append(this.differences);
            }

            return var1.toString();
         }
      }
   }

   private static enum EntryFunction implements Function<Entry<?, ?>, Object> {
      KEY {
         @Nullable
         public Object apply(Entry<?, ?> var1) {
            return var1.getKey();
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object apply(Object var1) {
            return this.apply((Entry)var1);
         }
      },
      VALUE {
         @Nullable
         public Object apply(Entry<?, ?> var1) {
            return var1.getValue();
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object apply(Object var1) {
            return this.apply((Entry)var1);
         }
      };

      private EntryFunction() {
      }

      // $FF: synthetic method
      EntryFunction(Object var3) {
         this();
      }
   }
}
