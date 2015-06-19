package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.BiMap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
final class SingletonImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
   final transient K singleKey;
   final transient V singleValue;
   transient ImmutableBiMap<V, K> inverse;

   SingletonImmutableBiMap(K var1, V var2) {
      CollectPreconditions.checkEntryNotNull(var1, var2);
      this.singleKey = var1;
      this.singleValue = var2;
   }

   private SingletonImmutableBiMap(K var1, V var2, ImmutableBiMap<V, K> var3) {
      this.singleKey = var1;
      this.singleValue = var2;
      this.inverse = var3;
   }

   SingletonImmutableBiMap(Entry<? extends K, ? extends V> var1) {
      this(var1.getKey(), var1.getValue());
   }

   public V get(@Nullable Object var1) {
      return this.singleKey.equals(var1)?this.singleValue:null;
   }

   public int size() {
      return 1;
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.singleKey.equals(var1);
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.singleValue.equals(var1);
   }

   boolean isPartialView() {
      return false;
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
   }

   ImmutableSet<K> createKeySet() {
      return ImmutableSet.of(this.singleKey);
   }

   public ImmutableBiMap<V, K> inverse() {
      ImmutableBiMap var1 = this.inverse;
      return var1 == null?(this.inverse = new SingletonImmutableBiMap(this.singleValue, this.singleKey, this)):var1;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public BiMap inverse() {
      return this.inverse();
   }
}
