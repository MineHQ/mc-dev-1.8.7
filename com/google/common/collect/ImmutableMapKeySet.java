package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class ImmutableMapKeySet<K, V> extends ImmutableSet<K> {
   private final ImmutableMap<K, V> map;

   ImmutableMapKeySet(ImmutableMap<K, V> var1) {
      this.map = var1;
   }

   public int size() {
      return this.map.size();
   }

   public UnmodifiableIterator<K> iterator() {
      return this.asList().iterator();
   }

   public boolean contains(@Nullable Object var1) {
      return this.map.containsKey(var1);
   }

   ImmutableList<K> createAsList() {
      final ImmutableList var1 = this.map.entrySet().asList();
      return new ImmutableAsList() {
         public K get(int var1x) {
            return ((Entry)var1.get(var1x)).getKey();
         }

         ImmutableCollection<K> delegateCollection() {
            return ImmutableMapKeySet.this;
         }
      };
   }

   boolean isPartialView() {
      return true;
   }

   @GwtIncompatible("serialization")
   Object writeReplace() {
      return new ImmutableMapKeySet.KeySetSerializedForm(this.map);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator iterator() {
      return this.iterator();
   }

   @GwtIncompatible("serialization")
   private static class KeySetSerializedForm<K> implements Serializable {
      final ImmutableMap<K, ?> map;
      private static final long serialVersionUID = 0L;

      KeySetSerializedForm(ImmutableMap<K, ?> var1) {
         this.map = var1;
      }

      Object readResolve() {
         return this.map.keySet();
      }
   }
}
