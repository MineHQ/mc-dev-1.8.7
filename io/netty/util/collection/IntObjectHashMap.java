package io.netty.util.collection;

import io.netty.util.collection.IntObjectMap;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IntObjectHashMap<V> implements IntObjectMap<V>, Iterable<IntObjectMap.Entry<V>> {
   private static final int DEFAULT_CAPACITY = 11;
   private static final float DEFAULT_LOAD_FACTOR = 0.5F;
   private static final Object NULL_VALUE = new Object();
   private int maxSize;
   private final float loadFactor;
   private int[] keys;
   private V[] values;
   private int size;

   public IntObjectHashMap() {
      this(11, 0.5F);
   }

   public IntObjectHashMap(int var1) {
      this(var1, 0.5F);
   }

   public IntObjectHashMap(int var1, float var2) {
      if(var1 < 1) {
         throw new IllegalArgumentException("initialCapacity must be >= 1");
      } else if(var2 > 0.0F && var2 <= 1.0F) {
         this.loadFactor = var2;
         int var3 = adjustCapacity(var1);
         this.keys = new int[var3];
         Object[] var4 = (Object[])(new Object[var3]);
         this.values = var4;
         this.maxSize = this.calcMaxSize(var3);
      } else {
         throw new IllegalArgumentException("loadFactor must be > 0 and <= 1");
      }
   }

   private static <T> T toExternal(T var0) {
      return var0 == NULL_VALUE?null:var0;
   }

   private static <T> T toInternal(T var0) {
      return var0 == null?NULL_VALUE:var0;
   }

   public V get(int var1) {
      int var2 = this.indexOf(var1);
      return var2 == -1?null:toExternal(this.values[var2]);
   }

   public V put(int var1, V var2) {
      int var3 = this.hashIndex(var1);
      int var4 = var3;

      while(this.values[var4] != null) {
         if(this.keys[var4] == var1) {
            Object var5 = this.values[var4];
            this.values[var4] = toInternal(var2);
            return toExternal(var5);
         }

         if((var4 = this.probeNext(var4)) == var3) {
            throw new IllegalStateException("Unable to insert");
         }
      }

      this.keys[var4] = var1;
      this.values[var4] = toInternal(var2);
      this.growSize();
      return null;
   }

   private int probeNext(int var1) {
      return var1 == this.values.length - 1?0:var1 + 1;
   }

   public void putAll(IntObjectMap<V> var1) {
      if(var1 instanceof IntObjectHashMap) {
         IntObjectHashMap var5 = (IntObjectHashMap)var1;

         for(int var6 = 0; var6 < var5.values.length; ++var6) {
            Object var4 = var5.values[var6];
            if(var4 != null) {
               this.put(var5.keys[var6], var4);
            }
         }

      } else {
         Iterator var2 = var1.entries().iterator();

         while(var2.hasNext()) {
            IntObjectMap.Entry var3 = (IntObjectMap.Entry)var2.next();
            this.put(var3.key(), var3.value());
         }

      }
   }

   public V remove(int var1) {
      int var2 = this.indexOf(var1);
      if(var2 == -1) {
         return null;
      } else {
         Object var3 = this.values[var2];
         this.removeAt(var2);
         return toExternal(var3);
      }
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public void clear() {
      Arrays.fill(this.keys, 0);
      Arrays.fill(this.values, (Object)null);
      this.size = 0;
   }

   public boolean containsKey(int var1) {
      return this.indexOf(var1) >= 0;
   }

   public boolean containsValue(V var1) {
      Object var2 = toInternal(var1);

      for(int var3 = 0; var3 < this.values.length; ++var3) {
         if(this.values[var3] != null && this.values[var3].equals(var2)) {
            return true;
         }
      }

      return false;
   }

   public Iterable<IntObjectMap.Entry<V>> entries() {
      return this;
   }

   public Iterator<IntObjectMap.Entry<V>> iterator() {
      return new IntObjectHashMap.IteratorImpl();
   }

   public int[] keys() {
      int[] var1 = new int[this.size()];
      int var2 = 0;

      for(int var3 = 0; var3 < this.values.length; ++var3) {
         if(this.values[var3] != null) {
            var1[var2++] = this.keys[var3];
         }
      }

      return var1;
   }

   public V[] values(Class<V> var1) {
      Object[] var2 = (Object[])((Object[])Array.newInstance(var1, this.size()));
      int var3 = 0;

      for(int var4 = 0; var4 < this.values.length; ++var4) {
         if(this.values[var4] != null) {
            var2[var3++] = this.values[var4];
         }
      }

      return var2;
   }

   public int hashCode() {
      int var1 = this.size;

      for(int var2 = 0; var2 < this.keys.length; ++var2) {
         var1 ^= this.keys[var2];
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if(this == var1) {
         return true;
      } else if(!(var1 instanceof IntObjectMap)) {
         return false;
      } else {
         IntObjectMap var2 = (IntObjectMap)var1;
         if(this.size != var2.size()) {
            return false;
         } else {
            for(int var3 = 0; var3 < this.values.length; ++var3) {
               Object var4 = this.values[var3];
               if(var4 != null) {
                  int var5 = this.keys[var3];
                  Object var6 = var2.get(var5);
                  if(var4 == NULL_VALUE) {
                     if(var6 != null) {
                        return false;
                     }
                  } else if(!var4.equals(var6)) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   private int indexOf(int var1) {
      int var2 = this.hashIndex(var1);
      int var3 = var2;

      while(this.values[var3] != null) {
         if(var1 == this.keys[var3]) {
            return var3;
         }

         if((var3 = this.probeNext(var3)) == var2) {
            return -1;
         }
      }

      return -1;
   }

   private int hashIndex(int var1) {
      return var1 % this.keys.length;
   }

   private void growSize() {
      ++this.size;
      if(this.size > this.maxSize) {
         this.rehash(adjustCapacity((int)Math.min((double)this.keys.length * 2.0D, 2.147483639E9D)));
      } else if(this.size == this.keys.length) {
         this.rehash(this.keys.length);
      }

   }

   private static int adjustCapacity(int var0) {
      return var0 | 1;
   }

   private void removeAt(int var1) {
      --this.size;
      this.keys[var1] = 0;
      this.values[var1] = null;
      int var2 = var1;

      for(int var3 = this.probeNext(var1); this.values[var3] != null; var3 = this.probeNext(var3)) {
         int var4 = this.hashIndex(this.keys[var3]);
         if(var3 < var4 && (var4 <= var2 || var2 <= var3) || var4 <= var2 && var2 <= var3) {
            this.keys[var2] = this.keys[var3];
            this.values[var2] = this.values[var3];
            this.keys[var3] = 0;
            this.values[var3] = null;
            var2 = var3;
         }
      }

   }

   private int calcMaxSize(int var1) {
      int var2 = var1 - 1;
      return Math.min(var2, (int)((float)var1 * this.loadFactor));
   }

   private void rehash(int var1) {
      int[] var2 = this.keys;
      Object[] var3 = this.values;
      this.keys = new int[var1];
      Object[] var4 = (Object[])(new Object[var1]);
      this.values = var4;
      this.maxSize = this.calcMaxSize(var1);

      for(int var5 = 0; var5 < var3.length; ++var5) {
         Object var6 = var3[var5];
         if(var6 != null) {
            int var7 = var2[var5];
            int var8 = this.hashIndex(var7);

            int var9;
            for(var9 = var8; this.values[var9] != null; var9 = this.probeNext(var9)) {
               ;
            }

            this.keys[var9] = var7;
            this.values[var9] = toInternal(var6);
         }
      }

   }

   public String toString() {
      if(this.size == 0) {
         return "{}";
      } else {
         StringBuilder var1 = new StringBuilder(4 * this.size);

         for(int var2 = 0; var2 < this.values.length; ++var2) {
            Object var3 = this.values[var2];
            if(var3 != null) {
               var1.append(var1.length() == 0?"{":", ");
               var1.append(this.keys[var2]).append('=').append(var3 == this?"(this Map)":var3);
            }
         }

         return var1.append('}').toString();
      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private final class IteratorImpl implements Iterator<IntObjectMap.Entry<V>>, IntObjectMap.Entry<V> {
      private int prevIndex;
      private int nextIndex;
      private int entryIndex;

      private IteratorImpl() {
         this.prevIndex = -1;
         this.nextIndex = -1;
         this.entryIndex = -1;
      }

      private void scanNext() {
         while(++this.nextIndex != IntObjectHashMap.this.values.length && IntObjectHashMap.this.values[this.nextIndex] == null) {
            ;
         }

      }

      public boolean hasNext() {
         if(this.nextIndex == -1) {
            this.scanNext();
         }

         return this.nextIndex < IntObjectHashMap.this.keys.length;
      }

      public IntObjectMap.Entry<V> next() {
         if(!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            this.prevIndex = this.nextIndex;
            this.scanNext();
            this.entryIndex = this.prevIndex;
            return this;
         }
      }

      public void remove() {
         if(this.prevIndex < 0) {
            throw new IllegalStateException("next must be called before each remove.");
         } else {
            IntObjectHashMap.this.removeAt(this.prevIndex);
            this.prevIndex = -1;
         }
      }

      public int key() {
         return IntObjectHashMap.this.keys[this.entryIndex];
      }

      public V value() {
         return IntObjectHashMap.toExternal(IntObjectHashMap.this.values[this.entryIndex]);
      }

      public void setValue(V var1) {
         IntObjectHashMap.this.values[this.entryIndex] = IntObjectHashMap.toInternal(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object next() {
         return this.next();
      }

      // $FF: synthetic method
      IteratorImpl(IntObjectHashMap.SyntheticClass_1 var2) {
         this();
      }
   }
}
