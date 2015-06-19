package net.minecraft.server;

import net.minecraft.server.BiomeBase;
import net.minecraft.server.ChunkProviderHell;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.WorldBorder;
import net.minecraft.server.WorldChunkManagerHell;
import net.minecraft.server.WorldProvider;

public class WorldProviderHell extends WorldProvider {
   public WorldProviderHell() {
   }

   public void b() {
      this.c = new WorldChunkManagerHell(BiomeBase.HELL, 0.0F);
      this.d = true;
      this.e = true;
      this.dimension = -1;
   }

   protected void a() {
      float var1 = 0.1F;

      for(int var2 = 0; var2 <= 15; ++var2) {
         float var3 = 1.0F - (float)var2 / 15.0F;
         this.f[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
      }

   }

   public IChunkProvider getChunkProvider() {
      return new ChunkProviderHell(this.b, this.b.getWorldData().shouldGenerateMapFeatures(), this.b.getSeed());
   }

   public boolean d() {
      return false;
   }

   public boolean canSpawn(int var1, int var2) {
      return false;
   }

   public float a(long var1, float var3) {
      return 0.5F;
   }

   public boolean e() {
      return false;
   }

   public String getName() {
      return "Nether";
   }

   public String getSuffix() {
      return "_nether";
   }

   public WorldBorder getWorldBorder() {
      return new WorldBorder() {
         public double getCenterX() {
            return super.getCenterX() / 8.0D;
         }

         public double getCenterZ() {
            return super.getCenterZ() / 8.0D;
         }
      };
   }
}
