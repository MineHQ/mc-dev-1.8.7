package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.BlockDirt;
import net.minecraft.server.BlockLongGrass;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockTallPlant;
import net.minecraft.server.Blocks;
import net.minecraft.server.ChunkSnapshot;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenGrass;
import net.minecraft.server.WorldGenMegaTree;
import net.minecraft.server.WorldGenTaiga1;
import net.minecraft.server.WorldGenTaiga2;
import net.minecraft.server.WorldGenTaigaStructure;
import net.minecraft.server.WorldGenTreeAbstract;
import net.minecraft.server.WorldGenerator;

public class BiomeTaiga extends BiomeBase {
   private static final WorldGenTaiga1 aD = new WorldGenTaiga1();
   private static final WorldGenTaiga2 aE = new WorldGenTaiga2(false);
   private static final WorldGenMegaTree aF = new WorldGenMegaTree(false, false);
   private static final WorldGenMegaTree aG = new WorldGenMegaTree(false, true);
   private static final WorldGenTaigaStructure aH;
   private int aI;

   public BiomeTaiga(int var1, int var2) {
      super(var1);
      this.aI = var2;
      this.au.add(new BiomeBase.BiomeMeta(EntityWolf.class, 8, 4, 4));
      this.as.A = 10;
      if(var2 != 1 && var2 != 2) {
         this.as.C = 1;
         this.as.E = 1;
      } else {
         this.as.C = 7;
         this.as.D = 1;
         this.as.E = 3;
      }

   }

   public WorldGenTreeAbstract a(Random var1) {
      return (WorldGenTreeAbstract)((this.aI == 1 || this.aI == 2) && var1.nextInt(3) == 0?(this.aI != 2 && var1.nextInt(13) != 0?aF:aG):(var1.nextInt(3) == 0?aD:aE));
   }

   public WorldGenerator b(Random var1) {
      return var1.nextInt(5) > 0?new WorldGenGrass(BlockLongGrass.EnumTallGrassType.FERN):new WorldGenGrass(BlockLongGrass.EnumTallGrassType.GRASS);
   }

   public void a(World var1, Random var2, BlockPosition var3) {
      int var4;
      int var5;
      int var6;
      int var7;
      if(this.aI == 1 || this.aI == 2) {
         var4 = var2.nextInt(3);

         for(var5 = 0; var5 < var4; ++var5) {
            var6 = var2.nextInt(16) + 8;
            var7 = var2.nextInt(16) + 8;
            BlockPosition var8 = var1.getHighestBlockYAt(var3.a(var6, 0, var7));
            aH.generate(var1, var2, var8);
         }
      }

      ag.a(BlockTallPlant.EnumTallFlowerVariants.FERN);

      for(var4 = 0; var4 < 7; ++var4) {
         var5 = var2.nextInt(16) + 8;
         var6 = var2.nextInt(16) + 8;
         var7 = var2.nextInt(var1.getHighestBlockYAt(var3.a(var5, 0, var6)).getY() + 32);
         ag.generate(var1, var2, var3.a(var5, var7, var6));
      }

      super.a(var1, var2, var3);
   }

   public void a(World var1, Random var2, ChunkSnapshot var3, int var4, int var5, double var6) {
      if(this.aI == 1 || this.aI == 2) {
         this.ak = Blocks.GRASS.getBlockData();
         this.al = Blocks.DIRT.getBlockData();
         if(var6 > 1.75D) {
            this.ak = Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.COARSE_DIRT);
         } else if(var6 > -0.95D) {
            this.ak = Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.PODZOL);
         }
      }

      this.b(var1, var2, var3, var4, var5, var6);
   }

   protected BiomeBase d(int var1) {
      return this.id == BiomeBase.MEGA_TAIGA.id?(new BiomeTaiga(var1, 2)).a(5858897, true).a("Mega Spruce Taiga").a(5159473).a(0.25F, 0.8F).a(new BiomeBase.BiomeTemperature(this.an, this.ao)):super.d(var1);
   }

   static {
      aH = new WorldGenTaigaStructure(Blocks.MOSSY_COBBLESTONE, 0);
   }
}
