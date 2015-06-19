package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapBasedMultimap;
import com.google.common.collect.AbstractSortedKeySortedSetMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.Serialization;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public class TreeMultimap<K, V> extends AbstractSortedKeySortedSetMultimap<K, V> {
   private transient Comparator<? super K> keyComparator;
   private transient Comparator<? super V> valueComparator;
   @GwtIncompatible("not needed in emulated source")
   private static final long serialVersionUID = 0L;

   public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create() {
      return new TreeMultimap(Ordering.natural(), Ordering.natural());
   }

   public static <K, V> TreeMultimap<K, V> create(Comparator<? super K> var0, Comparator<? super V> var1) {
      return new TreeMultimap((Comparator)Preconditions.checkNotNull(var0), (Comparator)Preconditions.checkNotNull(var1));
   }

   public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create(Multimap<? extends K, ? extends V> var0) {
      return new TreeMultimap(Ordering.natural(), Ordering.natural(), var0);
   }

   TreeMultimap(Comparator<? super K> var1, Comparator<? super V> var2) {
      super(new TreeMap(var1));
      this.keyComparator = var1;
      this.valueComparator = var2;
   }

   private TreeMultimap(Comparator<? super K> var1, Comparator<? super V> var2, Multimap<? extends K, ? extends V> var3) {
      this(var1, var2);
      this.putAll(var3);
   }

   SortedSet<V> createCollection() {
      return new TreeSet(this.valueComparator);
   }

   Collection<V> createCollection(@Nullable K var1) {
      if(var1 == null) {
         this.keyComparator().compare(var1, var1);
      }

      return super.createCollection(var1);
   }

   public Comparator<? super K> keyComparator() {
      return this.keyComparator;
   }

   public Comparator<? super V> valueComparator() {
      return this.valueComparator;
   }

   @GwtIncompatible("NavigableMap")
   NavigableMap<K, Collection<V>> backingMap() {
      return (NavigableMap)super.backingMap();
   }

   @GwtIncompatible("NavigableSet")
   public NavigableSet<V> get(@Nullable K var1) {
      return (NavigableSet)super.get(var1);
   }

   @GwtIncompatible("NavigableSet")
   Collection<V> unmodifiableCollectionSubclass(Collection<V> var1) {
      return Sets.unmodifiableNavigableSet((NavigableSet)var1);
   }

   @GwtIncompatible("NavigableSet")
   Collection<V> wrapCollection(K var1, Collection<V> var2) {
      return new AbstractMapBasedMultimap.WrappedNavigableSet(var1, (NavigableSet)var2, (AbstractMapBasedMultimap.WrappedCollection)null);
   }

   @GwtIncompatible("NavigableSet")
   public NavigableSet<K> keySet() {
      return (NavigableSet)super.keySet();
   }

   @GwtIncompatible("NavigableSet")
   NavigableSet<K> createKeySet() {
      return new AbstractMapBasedMultimap.NavigableKeySet(this.backingMap());
   }

   @GwtIncompatible("NavigableMap")
   public NavigableMap<K, Collection<V>> asMap() {
      return (NavigableMap)super.asMap();
   }

   @GwtIncompatible("NavigableMap")
   NavigableMap<K, Collection<V>> createAsMap() {
      return new AbstractMapBasedMultimap.NavigableAsMap(this.backingMap());
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.keyComparator());
      var1.writeObject(this.valueComparator());
      Serialization.writeMultimap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.keyComparator = (Comparator)Preconditions.checkNotNull((Comparator)var1.readObject());
      this.valueComparator = (Comparator)Preconditions.checkNotNull((Comparator)var1.readObject());
      this.setMap(new TreeMap(this.keyComparator));
      Serialization.populateMultimap(this, var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedSet keySet() {
      return this.keySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   SortedMap backingMap() {
      return this.backingMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedMap asMap() {
      return this.asMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection values() {
      return super.values();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map asMap() {
      return this.asMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedSet replaceValues(Object var1, Iterable var2) {
      return super.replaceValues(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedSet removeAll(Object var1) {
      return super.removeAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedSet get(Object var1) {
      return this.get(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set get(Object var1) {
      return this.get(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set keySet() {
      return this.keySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection get(Object var1) {
      return this.get(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean put(Object var1, Object var2) {
      return super.put(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set entries() {
      return super.entries();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Set createCollection() {
      return this.createCollection();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Map createAsMap() {
      return this.createAsMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Set createKeySet() {
      return this.createKeySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void clear() {
      super.clear();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsKey(Object var1) {
      return super.containsKey(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int size() {
      return super.size();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Map backingMap() {
      return this.backingMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Collection createCollection() {
      return this.createCollection();
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
   public Multiset keys() {
      return super.keys();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean putAll(Multimap var1) {
      return super.putAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean putAll(Object var1, Iterable var2) {
      return super.putAll(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean remove(Object var1, Object var2) {
      return super.remove(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsEntry(Object var1, Object var2) {
      return super.containsEntry(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsValue(Object var1) {
      return super.containsValue(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean isEmpty() {
      return super.isEmpty();
   }
}
