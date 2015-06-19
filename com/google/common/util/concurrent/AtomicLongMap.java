package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@GwtCompatible
public final class AtomicLongMap<K> {
   private final ConcurrentHashMap<K, AtomicLong> map;
   private transient Map<K, Long> asMap;

   private AtomicLongMap(ConcurrentHashMap<K, AtomicLong> var1) {
      this.map = (ConcurrentHashMap)Preconditions.checkNotNull(var1);
   }

   public static <K> AtomicLongMap<K> create() {
      return new AtomicLongMap(new ConcurrentHashMap());
   }

   public static <K> AtomicLongMap<K> create(Map<? extends K, ? extends Long> var0) {
      AtomicLongMap var1 = create();
      var1.putAll(var0);
      return var1;
   }

   public long get(K var1) {
      AtomicLong var2 = (AtomicLong)this.map.get(var1);
      return var2 == null?0L:var2.get();
   }

   public long incrementAndGet(K var1) {
      return this.addAndGet(var1, 1L);
   }

   public long decrementAndGet(K var1) {
      return this.addAndGet(var1, -1L);
   }

   public long addAndGet(K var1, long var2) {
      AtomicLong var4;
      label23:
      do {
         var4 = (AtomicLong)this.map.get(var1);
         if(var4 == null) {
            var4 = (AtomicLong)this.map.putIfAbsent(var1, new AtomicLong(var2));
            if(var4 == null) {
               return var2;
            }
         }

         long var5;
         long var7;
         do {
            var5 = var4.get();
            if(var5 == 0L) {
               continue label23;
            }

            var7 = var5 + var2;
         } while(!var4.compareAndSet(var5, var7));

         return var7;
      } while(!this.map.replace(var1, var4, new AtomicLong(var2)));

      return var2;
   }

   public long getAndIncrement(K var1) {
      return this.getAndAdd(var1, 1L);
   }

   public long getAndDecrement(K var1) {
      return this.getAndAdd(var1, -1L);
   }

   public long getAndAdd(K var1, long var2) {
      AtomicLong var4;
      label23:
      do {
         var4 = (AtomicLong)this.map.get(var1);
         if(var4 == null) {
            var4 = (AtomicLong)this.map.putIfAbsent(var1, new AtomicLong(var2));
            if(var4 == null) {
               return 0L;
            }
         }

         long var5;
         long var7;
         do {
            var5 = var4.get();
            if(var5 == 0L) {
               continue label23;
            }

            var7 = var5 + var2;
         } while(!var4.compareAndSet(var5, var7));

         return var5;
      } while(!this.map.replace(var1, var4, new AtomicLong(var2)));

      return 0L;
   }

   public long put(K var1, long var2) {
      AtomicLong var4;
      label23:
      do {
         var4 = (AtomicLong)this.map.get(var1);
         if(var4 == null) {
            var4 = (AtomicLong)this.map.putIfAbsent(var1, new AtomicLong(var2));
            if(var4 == null) {
               return 0L;
            }
         }

         long var5;
         do {
            var5 = var4.get();
            if(var5 == 0L) {
               continue label23;
            }
         } while(!var4.compareAndSet(var5, var2));

         return var5;
      } while(!this.map.replace(var1, var4, new AtomicLong(var2)));

      return 0L;
   }

   public void putAll(Map<? extends K, ? extends Long> var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put(var3.getKey(), ((Long)var3.getValue()).longValue());
      }

   }

   public long remove(K var1) {
      AtomicLong var2 = (AtomicLong)this.map.get(var1);
      if(var2 == null) {
         return 0L;
      } else {
         long var3;
         do {
            var3 = var2.get();
         } while(var3 != 0L && !var2.compareAndSet(var3, 0L));

         this.map.remove(var1, var2);
         return var3;
      }
   }

   public void removeAllZeros() {
      Iterator var1 = this.map.keySet().iterator();

      while(var1.hasNext()) {
         Object var2 = var1.next();
         AtomicLong var3 = (AtomicLong)this.map.get(var2);
         if(var3 != null && var3.get() == 0L) {
            this.map.remove(var2, var3);
         }
      }

   }

   public long sum() {
      long var1 = 0L;

      AtomicLong var4;
      for(Iterator var3 = this.map.values().iterator(); var3.hasNext(); var1 += var4.get()) {
         var4 = (AtomicLong)var3.next();
      }

      return var1;
   }

   public Map<K, Long> asMap() {
      Map var1 = this.asMap;
      return var1 == null?(this.asMap = this.createAsMap()):var1;
   }

   private Map<K, Long> createAsMap() {
      return Collections.unmodifiableMap(Maps.transformValues((Map)this.map, new Function() {
         public Long apply(AtomicLong var1) {
            return Long.valueOf(var1.get());
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object apply(Object var1) {
            return this.apply((AtomicLong)var1);
         }
      }));
   }

   public boolean containsKey(Object var1) {
      return this.map.containsKey(var1);
   }

   public int size() {
      return this.map.size();
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }

   public void clear() {
      this.map.clear();
   }

   public String toString() {
      return this.map.toString();
   }

   long putIfAbsent(K var1, long var2) {
      while(true) {
         AtomicLong var4 = (AtomicLong)this.map.get(var1);
         if(var4 == null) {
            var4 = (AtomicLong)this.map.putIfAbsent(var1, new AtomicLong(var2));
            if(var4 == null) {
               return 0L;
            }
         }

         long var5 = var4.get();
         if(var5 == 0L) {
            if(!this.map.replace(var1, var4, new AtomicLong(var2))) {
               continue;
            }

            return 0L;
         }

         return var5;
      }
   }

   boolean replace(K var1, long var2, long var4) {
      if(var2 == 0L) {
         return this.putIfAbsent(var1, var4) == 0L;
      } else {
         AtomicLong var6 = (AtomicLong)this.map.get(var1);
         return var6 == null?false:var6.compareAndSet(var2, var4);
      }
   }

   boolean remove(K var1, long var2) {
      AtomicLong var4 = (AtomicLong)this.map.get(var1);
      if(var4 == null) {
         return false;
      } else {
         long var5 = var4.get();
         if(var5 != var2) {
            return false;
         } else if(var5 != 0L && !var4.compareAndSet(var5, 0L)) {
            return false;
         } else {
            this.map.remove(var1, var4);
            return true;
         }
      }
   }
}
