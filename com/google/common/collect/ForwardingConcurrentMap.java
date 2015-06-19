package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ForwardingMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@GwtCompatible
public abstract class ForwardingConcurrentMap<K, V> extends ForwardingMap<K, V> implements ConcurrentMap<K, V> {
   protected ForwardingConcurrentMap() {
   }

   protected abstract ConcurrentMap<K, V> delegate();

   public V putIfAbsent(K var1, V var2) {
      return this.delegate().putIfAbsent(var1, var2);
   }

   public boolean remove(Object var1, Object var2) {
      return this.delegate().remove(var1, var2);
   }

   public V replace(K var1, V var2) {
      return this.delegate().replace(var1, var2);
   }

   public boolean replace(K var1, V var2, V var3) {
      return this.delegate().replace(var1, var2, var3);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Map delegate() {
      return this.delegate();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Object delegate() {
      return this.delegate();
   }
}
