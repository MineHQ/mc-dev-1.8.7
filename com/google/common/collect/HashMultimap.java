package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractSetMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Serialization;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public final class HashMultimap<K, V> extends AbstractSetMultimap<K, V> {
   private static final int DEFAULT_VALUES_PER_KEY = 2;
   @VisibleForTesting
   transient int expectedValuesPerKey = 2;
   @GwtIncompatible("Not needed in emulated source")
   private static final long serialVersionUID = 0L;

   public static <K, V> HashMultimap<K, V> create() {
      return new HashMultimap();
   }

   public static <K, V> HashMultimap<K, V> create(int var0, int var1) {
      return new HashMultimap(var0, var1);
   }

   public static <K, V> HashMultimap<K, V> create(Multimap<? extends K, ? extends V> var0) {
      return new HashMultimap(var0);
   }

   private HashMultimap() {
      super(new HashMap());
   }

   private HashMultimap(int var1, int var2) {
      super(Maps.newHashMapWithExpectedSize(var1));
      Preconditions.checkArgument(var2 >= 0);
      this.expectedValuesPerKey = var2;
   }

   private HashMultimap(Multimap<? extends K, ? extends V> var1) {
      super(Maps.newHashMapWithExpectedSize(var1.keySet().size()));
      this.putAll(var1);
   }

   Set<V> createCollection() {
      return Sets.newHashSetWithExpectedSize(this.expectedValuesPerKey);
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeInt(this.expectedValuesPerKey);
      Serialization.writeMultimap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
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
   public boolean put(Object var1, Object var2) {
      return super.put(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map asMap() {
      return super.asMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set replaceValues(Object var1, Iterable var2) {
      return super.replaceValues(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set removeAll(Object var1) {
      return super.removeAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set entries() {
      return super.entries();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set get(Object var1) {
      return super.get(var1);
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
