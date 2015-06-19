package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.BiMap;
import com.google.common.collect.EmptyImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMapEntry;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.RegularImmutableBiMap;
import com.google.common.collect.SingletonImmutableBiMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableBiMap<K, V> extends ImmutableMap<K, V> implements BiMap<K, V> {
   private static final Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Entry[0];

   public static <K, V> ImmutableBiMap<K, V> of() {
      return EmptyImmutableBiMap.INSTANCE;
   }

   public static <K, V> ImmutableBiMap<K, V> of(K var0, V var1) {
      return new SingletonImmutableBiMap(var0, var1);
   }

   public static <K, V> ImmutableBiMap<K, V> of(K var0, V var1, K var2, V var3) {
      return new RegularImmutableBiMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3)});
   }

   public static <K, V> ImmutableBiMap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5) {
      return new RegularImmutableBiMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5)});
   }

   public static <K, V> ImmutableBiMap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5, K var6, V var7) {
      return new RegularImmutableBiMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5), entryOf(var6, var7)});
   }

   public static <K, V> ImmutableBiMap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5, K var6, V var7, K var8, V var9) {
      return new RegularImmutableBiMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5), entryOf(var6, var7), entryOf(var8, var9)});
   }

   public static <K, V> ImmutableBiMap.Builder<K, V> builder() {
      return new ImmutableBiMap.Builder();
   }

   public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> var0) {
      if(var0 instanceof ImmutableBiMap) {
         ImmutableBiMap var1 = (ImmutableBiMap)var0;
         if(!var1.isPartialView()) {
            return var1;
         }
      }

      Entry[] var3 = (Entry[])var0.entrySet().toArray(EMPTY_ENTRY_ARRAY);
      switch(var3.length) {
      case 0:
         return of();
      case 1:
         Entry var2 = var3[0];
         return of(var2.getKey(), var2.getValue());
      default:
         return new RegularImmutableBiMap(var3);
      }
   }

   ImmutableBiMap() {
   }

   public abstract ImmutableBiMap<V, K> inverse();

   public ImmutableSet<V> values() {
      return this.inverse().keySet();
   }

   /** @deprecated */
   @Deprecated
   public V forcePut(K var1, V var2) {
      throw new UnsupportedOperationException();
   }

   Object writeReplace() {
      return new ImmutableBiMap.SerializedForm(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ImmutableCollection values() {
      return this.values();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection values() {
      return this.values();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public BiMap inverse() {
      return this.inverse();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set values() {
      return this.values();
   }

   private static class SerializedForm extends ImmutableMap.SerializedForm {
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableBiMap<?, ?> var1) {
         super(var1);
      }

      Object readResolve() {
         ImmutableBiMap.Builder var1 = new ImmutableBiMap.Builder();
         return this.createMap(var1);
      }
   }

   public static final class Builder<K, V> extends ImmutableMap.Builder<K, V> {
      public Builder() {
      }

      public ImmutableBiMap.Builder<K, V> put(K var1, V var2) {
         super.put(var1, var2);
         return this;
      }

      public ImmutableBiMap.Builder<K, V> putAll(Map<? extends K, ? extends V> var1) {
         super.putAll(var1);
         return this;
      }

      public ImmutableBiMap<K, V> build() {
         switch(this.size) {
         case 0:
            return ImmutableBiMap.of();
         case 1:
            return ImmutableBiMap.of(this.entries[0].getKey(), this.entries[0].getValue());
         default:
            return new RegularImmutableBiMap(this.size, this.entries);
         }
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
      public ImmutableMap.Builder put(Object var1, Object var2) {
         return this.put(var1, var2);
      }
   }
}
