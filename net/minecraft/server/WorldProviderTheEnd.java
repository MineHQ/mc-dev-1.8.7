package net.minecraft.server;

import net.minecraft.server.BiomeBase;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChunkProviderTheEnd;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.WorldChunkManagerHell;
import net.minecraft.server.WorldProvider;

public class WorldProviderTheEnd extends WorldProvider {
   public WorldProviderTheEnd() {
   }

   public void b() {
      this.c = new WorldChunkManagerHell(BiomeBase.SKY, 0.0F);
      this.dimension = 1;
      this.e = true;
   }

   public IChunkProvider getChunkProvider() {
      return new ChunkProviderTheEnd(this.b, this.b.getSeed());
   }

   public float a(long var1, float var3) {
      return 0.0F;
   }

   public boolean e() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public boolean canSpawn(int var1, int var2) {
      return this.b.c(new BlockPosition(var1, 0, var2)).getMaterial().isSolid();
   }

   public BlockPosition h() {
      return new BlockPosition(100, 50, 0);
   }

   public int getSeaLevel() {
      return 50;
   }

   public String getName() {
      return "The End";
   }

   public String getSuffix() {
      return "_end";
   }
}
