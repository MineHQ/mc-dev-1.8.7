package net.minecraft.server;

public class LongHashMap<V> {
   private transient LongHashMap.LongHashMapEntry<V>[] entries = new LongHashMap.LongHashMapEntry[4096];
   private transient int count;
   private int c;
   private int d = 3072;
   private final float e = 0.75F;
   private transient volatile int f;

   public LongHashMap() {
      this.c = this.entries.length - 1;
   }

   private static int g(long var0) {
      return a((int)(var0 ^ var0 >>> 32));
   }

   private static int a(int var0) {
      var0 ^= var0 >>> 20 ^ var0 >>> 12;
      return var0 ^ var0 >>> 7 ^ var0 >>> 4;
   }

   private static int a(int var0, int var1) {
      return var0 & var1;
   }

   public int count() {
      return this.count;
   }

   public V getEntry(long var1) {
      int var3 = g(var1);

      for(LongHashMap.LongHashMapEntry var4 = this.entries[a(var3, this.c)]; var4 != null; var4 = var4.c) {
         if(var4.a == var1) {
            return var4.b;
         }
      }

      return null;
   }

   public boolean contains(long var1) {
      return this.c(var1) != null;
   }

   final LongHashMap.LongHashMapEntry<V> c(long var1) {
      int var3 = g(var1);

      for(LongHashMap.LongHashMapEntry var4 = this.entries[a(var3, this.c)]; var4 != null; var4 = var4.c) {
         if(var4.a == var1) {
            return var4;
         }
      }

      return null;
   }

   public void put(long var1, V var3) {
      int var4 = g(var1);
      int var5 = a(var4, this.c);

      for(LongHashMap.LongHashMapEntry var6 = this.entries[var5]; var6 != null; var6 = var6.c) {
         if(var6.a == var1) {
            var6.b = var3;
            return;
         }
      }

      ++this.f;
      this.a(var4, var1, var3, var5);
   }

   private void b(int var1) {
      LongHashMap.LongHashMapEntry[] var2 = this.entries;
      int var3 = var2.length;
      if(var3 == 1073741824) {
         this.d = Integer.MAX_VALUE;
      } else {
         LongHashMap.LongHashMapEntry[] var4 = new LongHashMap.LongHashMapEntry[var1];
         this.a(var4);
         this.entries = var4;
         this.c = this.entries.length - 1;
         this.d = (int)((float)var1 * this.e);
      }
   }

   private void a(LongHashMap.LongHashMapEntry<V>[] var1) {
      LongHashMap.LongHashMapEntry[] var2 = this.entries;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         LongHashMap.LongHashMapEntry var5 = var2[var4];
         if(var5 != null) {
            var2[var4] = null;

            LongHashMap.LongHashMapEntry var6;
            do {
               var6 = var5.c;
               int var7 = a(var5.d, var3 - 1);
               var5.c = var1[var7];
               var1[var7] = var5;
               var5 = var6;
            } while(var6 != null);
         }
      }

   }

   public V remove(long var1) {
      LongHashMap.LongHashMapEntry var3 = this.e(var1);
      return var3 == null?null:var3.b;
   }

   final LongHashMap.LongHashMapEntry<V> e(long var1) {
      int var3 = g(var1);
      int var4 = a(var3, this.c);
      LongHashMap.LongHashMapEntry var5 = this.entries[var4];

      LongHashMap.LongHashMapEntry var6;
      LongHashMap.LongHashMapEntry var7;
      for(var6 = var5; var6 != null; var6 = var7) {
         var7 = var6.c;
         if(var6.a == var1) {
            ++this.f;
            --this.count;
            if(var5 == var6) {
               this.entries[var4] = var7;
            } else {
               var5.c = var7;
            }

            return var6;
         }

         var5 = var6;
      }

      return var6;
   }

   private void a(int var1, long var2, V var4, int var5) {
      LongHashMap.LongHashMapEntry var6 = this.entries[var5];
      this.entries[var5] = new LongHashMap.LongHashMapEntry(var1, var2, var4, var6);
      if(this.count++ >= this.d) {
         this.b(2 * this.entries.length);
      }

   }

   static class LongHashMapEntry<V> {
      final long a;
      V b;
      LongHashMap.LongHashMapEntry<V> c;
      final int d;

      LongHashMapEntry(int var1, long var2, V var4, LongHashMap.LongHashMapEntry<V> var5) {
         this.b = var4;
         this.c = var5;
         this.a = var2;
         this.d = var1;
      }

      public final long a() {
         return this.a;
      }

      public final V b() {
         return this.b;
      }

      public final boolean equals(Object var1) {
         if(!(var1 instanceof LongHashMap.LongHashMapEntry)) {
            return false;
         } else {
            LongHashMap.LongHashMapEntry var2 = (LongHashMap.LongHashMapEntry)var1;
            Long var3 = Long.valueOf(this.a());
            Long var4 = Long.valueOf(var2.a());
            if(var3 == var4 || var3 != null && var3.equals(var4)) {
               Object var5 = this.b();
               Object var6 = var2.b();
               if(var5 == var6 || var5 != null && var5.equals(var6)) {
                  return true;
               }
            }

            return false;
         }
      }

      public final int hashCode() {
         return LongHashMap.g(this.a);
      }

      public final String toString() {
         return this.a() + "=" + this.b();
      }
   }
}
