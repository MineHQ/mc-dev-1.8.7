package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.BiomeBaseSub;
import net.minecraft.server.BlockDirt;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockTallPlant;
import net.minecraft.server.Blocks;
import net.minecraft.server.ChunkSnapshot;
import net.minecraft.server.EntityHorse;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenAcaciaTree;
import net.minecraft.server.WorldGenTreeAbstract;

public class BiomeSavanna extends BiomeBase {
   private static final WorldGenAcaciaTree aD = new WorldGenAcaciaTree(false);

   protected BiomeSavanna(int var1) {
      super(var1);
      this.au.add(new BiomeBase.BiomeMeta(EntityHorse.class, 1, 2, 6));
      this.as.A = 1;
      this.as.B = 4;
      this.as.C = 20;
   }

   public WorldGenTreeAbstract a(Random var1) {
      return (WorldGenTreeAbstract)(var1.nextInt(5) > 0?aD:this.aA);
   }

   protected BiomeBase d(int var1) {
      BiomeSavanna.BiomeSavannaSub var2 = new BiomeSavanna.BiomeSavannaSub(var1, this);
      var2.temperature = (this.temperature + 1.0F) * 0.5F;
      var2.an = this.an * 0.5F + 0.3F;
      var2.ao = this.ao * 0.5F + 1.2F;
      return var2;
   }

   public void a(World var1, Random var2, BlockPosition var3) {
      ag.a(BlockTallPlant.EnumTallFlowerVariants.GRASS);

      for(int var4 = 0; var4 < 7; ++var4) {
         int var5 = var2.nextInt(16) + 8;
         int var6 = var2.nextInt(16) + 8;
         int var7 = var2.nextInt(var1.getHighestBlockYAt(var3.a(var5, 0, var6)).getY() + 32);
         ag.generate(var1, var2, var3.a(var5, var7, var6));
      }

      super.a(var1, var2, var3);
   }

   public static class BiomeSavannaSub extends BiomeBaseSub {
      public BiomeSavannaSub(int var1, BiomeBase var2) {
         super(var1, var2);
         this.as.A = 2;
         this.as.B = 2;
         this.as.C = 5;
      }

      public void a(World var1, Random var2, ChunkSnapshot var3, int var4, int var5, double var6) {
         this.ak = Blocks.GRASS.getBlockData();
         this.al = Blocks.DIRT.getBlockData();
         if(var6 > 1.75D) {
            this.ak = Blocks.STONE.getBlockData();
            this.al = Blocks.STONE.getBlockData();
         } else if(var6 > -0.5D) {
            this.ak = Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.COARSE_DIRT);
         }

         this.b(var1, var2, var3, var4, var5, var6);
      }

      public void a(World var1, Random var2, BlockPosition var3) {
         this.as.a(var1, var2, this, var3);
      }
   }
}
