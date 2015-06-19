package com.sun.jna;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class WeakIdentityHashMap implements Map {
   private final ReferenceQueue queue = new ReferenceQueue();
   private Map backingStore = new HashMap();

   public WeakIdentityHashMap() {
   }

   public void clear() {
      this.backingStore.clear();
      this.reap();
   }

   public boolean containsKey(Object var1) {
      this.reap();
      return this.backingStore.containsKey(new WeakIdentityHashMap.IdentityWeakReference(var1));
   }

   public boolean containsValue(Object var1) {
      this.reap();
      return this.backingStore.containsValue(var1);
   }

   public Set entrySet() {
      this.reap();
      HashSet var1 = new HashSet();
      Iterator var2 = this.backingStore.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         final Object var4 = ((WeakIdentityHashMap.IdentityWeakReference)var3.getKey()).get();
         final Object var5 = var3.getValue();
         Entry var6 = new Entry() {
            public Object getKey() {
               return var4;
            }

            public Object getValue() {
               return var5;
            }

            public Object setValue(Object var1) {
               throw new UnsupportedOperationException();
            }
         };
         var1.add(var6);
      }

      return Collections.unmodifiableSet(var1);
   }

   public Set keySet() {
      this.reap();
      HashSet var1 = new HashSet();
      Iterator var2 = this.backingStore.keySet().iterator();

      while(var2.hasNext()) {
         WeakIdentityHashMap.IdentityWeakReference var3 = (WeakIdentityHashMap.IdentityWeakReference)var2.next();
         var1.add(var3.get());
      }

      return Collections.unmodifiableSet(var1);
   }

   public boolean equals(Object var1) {
      return this.backingStore.equals(((WeakIdentityHashMap)var1).backingStore);
   }

   public Object get(Object var1) {
      this.reap();
      return this.backingStore.get(new WeakIdentityHashMap.IdentityWeakReference(var1));
   }

   public Object put(Object var1, Object var2) {
      this.reap();
      return this.backingStore.put(new WeakIdentityHashMap.IdentityWeakReference(var1), var2);
   }

   public int hashCode() {
      this.reap();
      return this.backingStore.hashCode();
   }

   public boolean isEmpty() {
      this.reap();
      return this.backingStore.isEmpty();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public Object remove(Object var1) {
      this.reap();
      return this.backingStore.remove(new WeakIdentityHashMap.IdentityWeakReference(var1));
   }

   public int size() {
      this.reap();
      return this.backingStore.size();
   }

   public Collection values() {
      this.reap();
      return this.backingStore.values();
   }

   private synchronized void reap() {
      for(Reference var1 = this.queue.poll(); var1 != null; var1 = this.queue.poll()) {
         WeakIdentityHashMap.IdentityWeakReference var2 = (WeakIdentityHashMap.IdentityWeakReference)var1;
         this.backingStore.remove(var2);
      }

   }

   class IdentityWeakReference extends WeakReference {
      int hash;

      IdentityWeakReference(Object var2) {
         super(var2, WeakIdentityHashMap.this.queue);
         this.hash = System.identityHashCode(var2);
      }

      public int hashCode() {
         return this.hash;
      }

      public boolean equals(Object var1) {
         if(this == var1) {
            return true;
         } else {
            WeakIdentityHashMap.IdentityWeakReference var2 = (WeakIdentityHashMap.IdentityWeakReference)var1;
            return this.get() == var2.get();
         }
      }
   }
}
