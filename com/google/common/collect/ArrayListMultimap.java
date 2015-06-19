package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.AbstractListMultimap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Serialization;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public final class ArrayListMultimap<K, V> extends AbstractListMultimap<K, V> {
   private static final int DEFAULT_VALUES_PER_KEY = 3;
   @VisibleForTesting
   transient int expectedValuesPerKey;
   @GwtIncompatible("Not needed in emulated source.")
   private static final long serialVersionUID = 0L;

   public static <K, V> ArrayListMultimap<K, V> create() {
      return new ArrayListMultimap();
   }

   public static <K, V> ArrayListMultimap<K, V> create(int var0, int var1) {
      return new ArrayListMultimap(var0, var1);
   }

   public static <K, V> ArrayListMultimap<K, V> create(Multimap<? extends K, ? extends V> var0) {
      return new ArrayListMultimap(var0);
   }

   private ArrayListMultimap() {
      super(new HashMap());
      this.expectedValuesPerKey = 3;
   }

   private ArrayListMultimap(int var1, int var2) {
      super(Maps.newHashMapWithExpectedSize(var1));
      CollectPreconditions.checkNonnegative(var2, "expectedValuesPerKey");
      this.expectedValuesPerKey = var2;
   }

   private ArrayListMultimap(Multimap<? extends K, ? extends V> var1) {
      this(var1.keySet().size(), var1 instanceof ArrayListMultimap?((ArrayListMultimap)var1).expectedValuesPerKey:3);
      this.putAll(var1);
   }

   List<V> createCollection() {
      return new ArrayList(this.expectedValuesPerKey);
   }

   public void trimToSize() {
      Iterator var1 = this.backingMap().values().iterator();

      while(var1.hasNext()) {
         Collection var2 = (Collection)var1.next();
         ArrayList var3 = (ArrayList)var2;
         var3.trimToSize();
      }

   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeInt(this.expectedValuesPerKey);
      Serialization.writeMultimap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.expectedValuesPerKey = var1.readInt();
      int var2 = Serialization.readCount(var1);
      HashMap var3 = Maps.newHashMapWithExpectedSize(var2);
      this.setMap(var3);
      Serialization.populateMultimap(this, var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map asMap() {
      return super.asMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean put(Object var1, Object var2) {
      return super.put(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public List replaceValues(Object var1, Iterable var2) {
      return super.replaceValues(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public List removeAll(Object var1) {
      return super.removeAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public List get(Object var1) {
      return super.get(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection entries() {
      return super.entries();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection values() {
      return super.values();
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
   public Set keySet() {
      return super.keySet();
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
