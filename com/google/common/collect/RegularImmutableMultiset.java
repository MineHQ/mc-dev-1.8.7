package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
class RegularImmutableMultiset<E> extends ImmutableMultiset<E> {
   private final transient ImmutableMap<E, Integer> map;
   private final transient int size;

   RegularImmutableMultiset(ImmutableMap<E, Integer> var1, int var2) {
      this.map = var1;
      this.size = var2;
   }

   boolean isPartialView() {
      return this.map.isPartialView();
   }

   public int count(@Nullable Object var1) {
      Integer var2 = (Integer)this.map.get(var1);
      return var2 == null?0:var2.intValue();
   }

   public int size() {
      return this.size;
   }

   public boolean contains(@Nullable Object var1) {
      return this.map.containsKey(var1);
   }

   public ImmutableSet<E> elementSet() {
      return this.map.keySet();
   }

   Multiset.Entry<E> getEntry(int var1) {
      Entry var2 = (Entry)this.map.entrySet().asList().get(var1);
      return Multisets.immutableEntry(var2.getKey(), ((Integer)var2.getValue()).intValue());
   }

   public int hashCode() {
      return this.map.hashCode();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set elementSet() {
      return this.elementSet();
   }
}
