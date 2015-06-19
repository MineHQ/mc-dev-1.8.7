package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class ImmutableMapValues<K, V> extends ImmutableCollection<V> {
   private final ImmutableMap<K, V> map;

   ImmutableMapValues(ImmutableMap<K, V> var1) {
      this.map = var1;
   }

   public int size() {
      return this.map.size();
   }

   public UnmodifiableIterator<V> iterator() {
      return Maps.valueIterator(this.map.entrySet().iterator());
   }

   public boolean contains(@Nullable Object var1) {
      return var1 != null && Iterators.contains(this.iterator(), var1);
   }

   boolean isPartialView() {
      return true;
   }

   ImmutableList<V> createAsList() {
      final ImmutableList var1 = this.map.entrySet().asList();
      return new ImmutableAsList() {
         public V get(int var1x) {
            return ((Entry)var1.get(var1x)).getValue();
         }

         ImmutableCollection<V> delegateCollection() {
            return ImmutableMapValues.this;
         }
      };
   }

   @GwtIncompatible("serialization")
   Object writeReplace() {
      return new ImmutableMapValues.SerializedForm(this.map);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator iterator() {
      return this.iterator();
   }

   @GwtIncompatible("serialization")
   private static class SerializedForm<V> implements Serializable {
      final ImmutableMap<?, V> map;
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableMap<?, V> var1) {
         this.map = var1;
      }

      Object readResolve() {
         return this.map.values();
      }
   }
}
