package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractBiMap;
import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import com.google.common.collect.Serialization;
import com.google.common.collect.WellBehavedMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@GwtCompatible(
   emulated = true
)
public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>> extends AbstractBiMap<K, V> {
   private transient Class<K> keyType;
   private transient Class<V> valueType;
   @GwtIncompatible("not needed in emulated source.")
   private static final long serialVersionUID = 0L;

   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Class<K> var0, Class<V> var1) {
      return new EnumBiMap(var0, var1);
   }

   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Map<K, V> var0) {
      EnumBiMap var1 = create(inferKeyType(var0), inferValueType(var0));
      var1.putAll(var0);
      return var1;
   }

   private EnumBiMap(Class<K> var1, Class<V> var2) {
      super(WellBehavedMap.wrap(new EnumMap(var1)), (Map)WellBehavedMap.wrap(new EnumMap(var2)));
      this.keyType = var1;
      this.valueType = var2;
   }

   static <K extends Enum<K>> Class<K> inferKeyType(Map<K, ?> var0) {
      if(var0 instanceof EnumBiMap) {
         return ((EnumBiMap)var0).keyType();
      } else if(var0 instanceof EnumHashBiMap) {
         return ((EnumHashBiMap)var0).keyType();
      } else {
         Preconditions.checkArgument(!var0.isEmpty());
         return ((Enum)var0.keySet().iterator().next()).getDeclaringClass();
      }
   }

   private static <V extends Enum<V>> Class<V> inferValueType(Map<?, V> var0) {
      if(var0 instanceof EnumBiMap) {
         return ((EnumBiMap)var0).valueType;
      } else {
         Preconditions.checkArgument(!var0.isEmpty());
         return ((Enum)var0.values().iterator().next()).getDeclaringClass();
      }
   }

   public Class<K> keyType() {
      return this.keyType;
   }

   public Class<V> valueType() {
      return this.valueType;
   }

   K checkKey(K var1) {
      return (Enum)Preconditions.checkNotNull(var1);
   }

   V checkValue(V var1) {
      return (Enum)Preconditions.checkNotNull(var1);
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.keyType);
      var1.writeObject(this.valueType);
      Serialization.writeMap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.keyType = (Class)var1.readObject();
      this.valueType = (Class)var1.readObject();
      this.setDelegates(WellBehavedMap.wrap(new EnumMap(this.keyType)), WellBehavedMap.wrap(new EnumMap(this.valueType)));
      Serialization.populateMap(this, var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set entrySet() {
      return super.entrySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set values() {
      return super.values();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set keySet() {
      return super.keySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public BiMap inverse() {
      return super.inverse();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void clear() {
      super.clear();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void putAll(Map var1) {
      super.putAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsValue(Object var1) {
      return super.containsValue(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   Object checkValue(Object var1) {
      return this.checkValue((Enum)var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   Object checkKey(Object var1) {
      return this.checkKey((Enum)var1);
   }
}
