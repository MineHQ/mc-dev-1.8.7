package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMapEntry<K, V> implements Entry<K, V> {
   AbstractMapEntry() {
   }

   public abstract K getKey();

   public abstract V getValue();

   public V setValue(V var1) {
      throw new UnsupportedOperationException();
   }

   public boolean equals(@Nullable Object var1) {
      if(!(var1 instanceof Entry)) {
         return false;
      } else {
         Entry var2 = (Entry)var1;
         return Objects.equal(this.getKey(), var2.getKey()) && Objects.equal(this.getValue(), var2.getValue());
      }
   }

   public int hashCode() {
      Object var1 = this.getKey();
      Object var2 = this.getValue();
      return (var1 == null?0:var1.hashCode()) ^ (var2 == null?0:var2.hashCode());
   }

   public String toString() {
      return this.getKey() + "=" + this.getValue();
   }
}
