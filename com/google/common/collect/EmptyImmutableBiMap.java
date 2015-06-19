package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class EmptyImmutableBiMap extends ImmutableBiMap<Object, Object> {
   static final EmptyImmutableBiMap INSTANCE = new EmptyImmutableBiMap();

   private EmptyImmutableBiMap() {
   }

   public ImmutableBiMap<Object, Object> inverse() {
      return this;
   }

   public int size() {
      return 0;
   }

   public boolean isEmpty() {
      return true;
   }

   public Object get(@Nullable Object var1) {
      return null;
   }

   public ImmutableSet<Entry<Object, Object>> entrySet() {
      return ImmutableSet.of();
   }

   ImmutableSet<Entry<Object, Object>> createEntrySet() {
      throw new AssertionError("should never be called");
   }

   public ImmutableSetMultimap<Object, Object> asMultimap() {
      return ImmutableSetMultimap.of();
   }

   public ImmutableSet<Object> keySet() {
      return ImmutableSet.of();
   }

   boolean isPartialView() {
      return false;
   }

   Object readResolve() {
      return INSTANCE;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public BiMap inverse() {
      return this.inverse();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set entrySet() {
      return this.entrySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set keySet() {
      return this.keySet();
   }
}
