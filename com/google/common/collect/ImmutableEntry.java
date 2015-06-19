package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.AbstractMapEntry;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
class ImmutableEntry<K, V> extends AbstractMapEntry<K, V> implements Serializable {
   final K key;
   final V value;
   private static final long serialVersionUID = 0L;

   ImmutableEntry(@Nullable K var1, @Nullable V var2) {
      this.key = var1;
      this.value = var2;
   }

   @Nullable
   public final K getKey() {
      return this.key;
   }

   @Nullable
   public final V getValue() {
      return this.value;
   }

   public final V setValue(V var1) {
      throw new UnsupportedOperationException();
   }
}
