package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.LongHashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldChunkManager;

public class BiomeCache {
   private final WorldChunkManager a;
   private long b;
   private LongHashMap<BiomeCache.BiomeCacheBlock> c = new LongHashMap();
   private List<BiomeCache.BiomeCacheBlock> d = Lists.newArrayList();

   public BiomeCache(WorldChunkManager var1) {
      this.a = var1;
   }

   public BiomeCache.BiomeCacheBlock a(int var1, int var2) {
      var1 >>= 4;
      var2 >>= 4;
      long var3 = (long)var1 & 4294967295L | ((long)var2 & 4294967295L) << 32;
      BiomeCache.BiomeCacheBlock var5 = (BiomeCache.BiomeCacheBlock)this.c.getEntry(var3);
      if(var5 == null) {
         var5 = new BiomeCache.BiomeCacheBlock(var1, var2);
         this.c.put(var3, var5);
         this.d.add(var5);
      }

      var5.e = MinecraftServer.az();
      return var5;
   }

   public BiomeBase a(int var1, int var2, BiomeBase var3) {
      BiomeBase var4 = this.a(var1, var2).a(var1, var2);
      return var4 == null?var3:var4;
   }

   public void a() {
      long var1 = MinecraftServer.az();
      long var3 = var1 - this.b;
      if(var3 > 7500L || var3 < 0L) {
         this.b = var1;

         for(int var5 = 0; var5 < this.d.size(); ++var5) {
            BiomeCache.BiomeCacheBlock var6 = (BiomeCache.BiomeCacheBlock)this.d.get(var5);
            long var7 = var1 - var6.e;
            if(var7 > 30000L || var7 < 0L) {
               this.d.remove(var5--);
               long var9 = (long)var6.c & 4294967295L | ((long)var6.d & 4294967295L) << 32;
               this.c.remove(var9);
            }
         }
      }

   }

   public BiomeBase[] c(int var1, int var2) {
      return this.a(var1, var2).b;
   }

   public class BiomeCacheBlock {
      public float[] a = new float[256];
      public BiomeBase[] b = new BiomeBase[256];
      public int c;
      public int d;
      public long e;

      public BiomeCacheBlock(int var2, int var3) {
         this.c = var2;
         this.d = var3;
         BiomeCache.this.a.getWetness(this.a, var2 << 4, var3 << 4, 16, 16);
         BiomeCache.this.a.a(this.b, var2 << 4, var3 << 4, 16, 16, false);
      }

      public BiomeBase a(int var1, int var2) {
         return this.b[var1 & 15 | (var2 & 15) << 4];
      }
   }
}
