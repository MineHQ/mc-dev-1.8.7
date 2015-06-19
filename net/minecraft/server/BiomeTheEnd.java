package net.minecraft.server;

import net.minecraft.server.BiomeBase;
import net.minecraft.server.BiomeTheEndDecorator;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityEnderman;

public class BiomeTheEnd extends BiomeBase {
   public BiomeTheEnd(int var1) {
      super(var1);
      this.at.clear();
      this.au.clear();
      this.av.clear();
      this.aw.clear();
      this.at.add(new BiomeBase.BiomeMeta(EntityEnderman.class, 10, 4, 4));
      this.ak = Blocks.DIRT.getBlockData();
      this.al = Blocks.DIRT.getBlockData();
      this.as = new BiomeTheEndDecorator();
   }
}
