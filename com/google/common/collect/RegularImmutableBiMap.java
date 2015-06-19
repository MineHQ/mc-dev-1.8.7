package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.BiMap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Hashing;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMapEntry;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.RegularImmutableAsList;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
   static final double MAX_LOAD_FACTOR = 1.2D;
   private final transient ImmutableMapEntry<K, V>[] keyTable;
   private final transient ImmutableMapEntry<K, V>[] valueTable;
   private final transient ImmutableMapEntry<K, V>[] entries;
   private final transient int mask;
   private final transient int hashCode;
   private transient ImmutableBiMap<V, K> inverse;

   RegularImmutableBiMap(ImmutableMapEntry.TerminalEntry... var1) {
      this(var1.length, var1);
   }

   RegularImmutableBiMap(int var1, ImmutableMapEntry.TerminalEntry<?, ?>[] var2) {
      int var3 = Hashing.closedTableSize(var1, 1.2D);
      this.mask = var3 - 1;
      ImmutableMapEntry[] var4 = createEntryArray(var3);
      ImmutableMapEntry[] var5 = createEntryArray(var3);
      ImmutableMapEntry[] var6 = createEntryArray(var1);
      int var7 = 0;

      for(int var8 = 0; var8 < var1; ++var8) {
         ImmutableMapEntry.TerminalEntry var9 = var2[var8];
         Object var10 = var9.getKey();
         Object var11 = var9.getValue();
         int var12 = var10.hashCode();
         int var13 = var11.hashCode();
         int var14 = Hashing.smear(var12) & this.mask;
         int var15 = Hashing.smear(var13) & this.mask;
         ImmutableMapEntry var16 = var4[var14];

         ImmutableMapEntry var17;
         for(var17 = var16; var17 != null; var17 = var17.getNextInKeyBucket()) {
            checkNoConflict(!var10.equals(var17.getKey()), "key", var9, var17);
         }

         var17 = var5[var15];

         for(ImmutableMapEntry var18 = var17; var18 != null; var18 = var18.getNextInValueBucket()) {
            checkNoConflict(!var11.equals(var18.getValue()), "value", var9, var18);
         }

         Object var19 = var16 == null && var17 == null?var9:new RegularImmutableBiMap.NonTerminalBiMapEntry(var9, var16, var17);
         var4[var14] = (ImmutableMapEntry)var19;
         var5[var15] = (ImmutableMapEntry)var19;
         var6[var8] = (ImmutableMapEntry)var19;
         var7 += var12 ^ var13;
      }

      this.keyTable = var4;
      this.valueTable = var5;
      this.entries = var6;
      this.hashCode = var7;
   }

   RegularImmutableBiMap(Entry<?, ?>[] var1) {
      int var2 = var1.length;
      int var3 = Hashing.closedTableSize(var2, 1.2D);
      this.mask = var3 - 1;
      ImmutableMapEntry[] var4 = createEntryArray(var3);
      ImmutableMapEntry[] var5 = createEntryArray(var3);
      ImmutableMapEntry[] var6 = createEntryArray(var2);
      int var7 = 0;

      for(int var8 = 0; var8 < var2; ++var8) {
         Entry var9 = var1[var8];
         Object var10 = var9.getKey();
         Object var11 = var9.getValue();
         CollectPreconditions.checkEntryNotNull(var10, var11);
         int var12 = var10.hashCode();
         int var13 = var11.hashCode();
         int var14 = Hashing.smear(var12) & this.mask;
         int var15 = Hashing.smear(var13) & this.mask;
         ImmutableMapEntry var16 = var4[var14];

         ImmutableMapEntry var17;
         for(var17 = var16; var17 != null; var17 = var17.getNextInKeyBucket()) {
            checkNoConflict(!var10.equals(var17.getKey()), "key", var9, var17);
         }

         var17 = var5[var15];

         for(ImmutableMapEntry var18 = var17; var18 != null; var18 = var18.getNextInValueBucket()) {
            checkNoConflict(!var11.equals(var18.getValue()), "value", var9, var18);
         }

         Object var19 = var16 == null && var17 == null?new ImmutableMapEntry.TerminalEntry(var10, var11):new RegularImmutableBiMap.NonTerminalBiMapEntry(var10, var11, var16, var17);
         var4[var14] = (ImmutableMapEntry)var19;
         var5[var15] = (ImmutableMapEntry)var19;
         var6[var8] = (ImmutableMapEntry)var19;
         var7 += var12 ^ var13;
      }

      this.keyTable = var4;
      this.valueTable = var5;
      this.entries = var6;
      this.hashCode = var7;
   }

   private static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int var0) {
      return new ImmutableMapEntry[var0];
   }

   @Nullable
   public V get(@Nullable Object var1) {
      if(var1 == null) {
         return null;
      } else {
         int var2 = Hashing.smear(var1.hashCode()) & this.mask;

         for(ImmutableMapEntry var3 = this.keyTable[var2]; var3 != null; var3 = var3.getNextInKeyBucket()) {
            if(var1.equals(var3.getKey())) {
               return var3.getValue();
            }
         }

         return null;
      }
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      return new ImmutableMapEntrySet() {
         ImmutableMap<K, V> map() {
            return RegularImmutableBiMap.this;
         }

         public UnmodifiableIterator<Entry<K, V>> iterator() {
            return this.asList().iterator();
         }

         ImmutableList<Entry<K, V>> createAsList() {
            return new RegularImmutableAsList(this, RegularImmutableBiMap.this.entries);
         }

         boolean isHashCodeFast() {
            return true;
         }

         public int hashCode() {
            return RegularImmutableBiMap.this.hashCode;
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Iterator iterator() {
            return this.iterator();
         }
      };
   }

   boolean isPartialView() {
      return false;
   }

   public int size() {
      return this.entries.length;
   }

   public ImmutableBiMap<V, K> inverse() {
      ImmutableBiMap var1 = this.inverse;
      return var1 == null?(this.inverse = new RegularImmutableBiMap.Inverse(null)):var1;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public BiMap inverse() {
      return this.inverse();
   }

   private static class InverseSerializedForm<K, V> implements Serializable {
      private final ImmutableBiMap<K, V> forward;
      private static final long serialVersionUID = 1L;

      InverseSerializedForm(ImmutableBiMap<K, V> var1) {
         this.forward = var1;
      }

      Object readResolve() {
         return this.forward.inverse();
      }
   }

   private final class Inverse extends ImmutableBiMap<V, K> {
      private Inverse() {
      }

      public int size() {
         return this.inverse().size();
      }

      public ImmutableBiMap<K, V> inverse() {
         return RegularImmutableBiMap.this;
      }

      public K get(@Nullable Object var1) {
         if(var1 == null) {
            return null;
         } else {
            int var2 = Hashing.smear(var1.hashCode()) & RegularImmutableBiMap.this.mask;

            for(ImmutableMapEntry var3 = RegularImmutableBiMap.this.valueTable[var2]; var3 != null; var3 = var3.getNextInValueBucket()) {
               if(var1.equals(var3.getValue())) {
                  return var3.getKey();
               }
            }

            return null;
         }
      }

      ImmutableSet<Entry<V, K>> createEntrySet() {
         return new RegularImmutableBiMap.Inverse.Inverse$InverseEntrySet();
      }

      boolean isPartialView() {
         return false;
      }

      Object writeReplace() {
         return new RegularImmutableBiMap.InverseSerializedForm(RegularImmutableBiMap.this);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public BiMap inverse() {
         return this.inverse();
      }

      // $FF: synthetic method
      Inverse(Object var2) {
         this();
      }

      final class Inverse$InverseEntrySet extends ImmutableMapEntrySet<V, K> {
         Inverse$InverseEntrySet() {
         }

         ImmutableMap<V, K> map() {
            return Inverse.this;
         }

         boolean isHashCodeFast() {
            return true;
         }

         public int hashCode() {
            return RegularImmutableBiMap.this.hashCode;
         }

         public UnmodifiableIterator<Entry<V, K>> iterator() {
            return this.asList().iterator();
         }

         ImmutableList<Entry<V, K>> createAsList() {
            return new ImmutableAsList() {
               public Entry<V, K> get(int var1) {
                  ImmutableMapEntry var2 = RegularImmutableBiMap.this.entries[var1];
                  return Maps.immutableEntry(var2.getValue(), var2.getKey());
               }

               ImmutableCollection<Entry<V, K>> delegateCollection() {
                  return Inverse$InverseEntrySet.this;
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object get(int var1) {
                  return this.get(var1);
               }
            };
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Iterator iterator() {
            return this.iterator();
         }
      }
   }

   private static final class NonTerminalBiMapEntry<K, V> extends ImmutableMapEntry<K, V> {
      @Nullable
      private final ImmutableMapEntry<K, V> nextInKeyBucket;
      @Nullable
      private final ImmutableMapEntry<K, V> nextInValueBucket;

      NonTerminalBiMapEntry(K var1, V var2, @Nullable ImmutableMapEntry<K, V> var3, @Nullable ImmutableMapEntry<K, V> var4) {
         super(var1, var2);
         this.nextInKeyBucket = var3;
         this.nextInValueBucket = var4;
      }

      NonTerminalBiMapEntry(ImmutableMapEntry<K, V> var1, @Nullable ImmutableMapEntry<K, V> var2, @Nullable ImmutableMapEntry<K, V> var3) {
         super(var1);
         this.nextInKeyBucket = var2;
         this.nextInValueBucket = var3;
      }

      @Nullable
      ImmutableMapEntry<K, V> getNextInKeyBucket() {
         return this.nextInKeyBucket;
      }

      @Nullable
      ImmutableMapEntry<K, V> getNextInValueBucket() {
         return this.nextInValueBucket;
      }
   }
}
