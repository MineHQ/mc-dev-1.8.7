package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.BiomeBaseSub;
import net.minecraft.server.BlockFlowers;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockTallPlant;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.MathHelper;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenForest;
import net.minecraft.server.WorldGenForestTree;
import net.minecraft.server.WorldGenHugeMushroom;
import net.minecraft.server.WorldGenTreeAbstract;

public class BiomeForest extends BiomeBase {
   private int aG;
   protected static final WorldGenForest aD = new WorldGenForest(false, true);
   protected static final WorldGenForest aE = new WorldGenForest(false, false);
   protected static final WorldGenForestTree aF = new WorldGenForestTree(false);

   public BiomeForest(int var1, int var2) {
      super(var1);
      this.aG = var2;
      this.as.A = 10;
      this.as.C = 2;
      if(this.aG == 1) {
         this.as.A = 6;
         this.as.B = 100;
         this.as.C = 1;
      }

      this.a(5159473);
      this.a(0.7F, 0.8F);
      if(this.aG == 2) {
         this.aj = 353825;
         this.ai = 3175492;
         this.a(0.6F, 0.6F);
      }

      if(this.aG == 0) {
         this.au.add(new BiomeBase.BiomeMeta(EntityWolf.class, 5, 4, 4));
      }

      if(this.aG == 3) {
         this.as.A = -999;
      }

   }

   protected BiomeBase a(int var1, boolean var2) {
      if(this.aG == 2) {
         this.aj = 353825;
         this.ai = var1;
         if(var2) {
            this.aj = (this.aj & 16711422) >> 1;
         }

         return this;
      } else {
         return super.a(var1, var2);
      }
   }

   public WorldGenTreeAbstract a(Random var1) {
      return (WorldGenTreeAbstract)(this.aG == 3 && var1.nextInt(3) > 0?aF:(this.aG != 2 && var1.nextInt(5) != 0?this.aA:aE));
   }

   public BlockFlowers.EnumFlowerVarient a(Random var1, BlockPosition var2) {
      if(this.aG == 1) {
         double var3 = MathHelper.a((1.0D + af.a((double)var2.getX() / 48.0D, (double)var2.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
         BlockFlowers.EnumFlowerVarient var5 = BlockFlowers.EnumFlowerVarient.values()[(int)(var3 * (double)BlockFlowers.EnumFlowerVarient.values().length)];
         return var5 == BlockFlowers.EnumFlowerVarient.BLUE_ORCHID?BlockFlowers.EnumFlowerVarient.POPPY:var5;
      } else {
         return super.a(var1, var2);
      }
   }

   public void a(World var1, Random var2, BlockPosition var3) {
      int var4;
      int var5;
      int var6;
      int var7;
      if(this.aG == 3) {
         for(var4 = 0; var4 < 4; ++var4) {
            for(var5 = 0; var5 < 4; ++var5) {
               var6 = var4 * 4 + 1 + 8 + var2.nextInt(3);
               var7 = var5 * 4 + 1 + 8 + var2.nextInt(3);
               BlockPosition var8 = var1.getHighestBlockYAt(var3.a(var6, 0, var7));
               if(var2.nextInt(20) == 0) {
                  WorldGenHugeMushroom var9 = new WorldGenHugeMushroom();
                  var9.generate(var1, var2, var8);
               } else {
                  WorldGenTreeAbstract var12 = this.a(var2);
                  var12.e();
                  if(var12.generate(var1, var2, var8)) {
                     var12.a(var1, var2, var8);
                  }
               }
            }
         }
      }

      var4 = var2.nextInt(5) - 3;
      if(this.aG == 1) {
         var4 += 2;
      }

      for(var5 = 0; var5 < var4; ++var5) {
         var6 = var2.nextInt(3);
         if(var6 == 0) {
            ag.a(BlockTallPlant.EnumTallFlowerVariants.SYRINGA);
         } else if(var6 == 1) {
            ag.a(BlockTallPlant.EnumTallFlowerVariants.ROSE);
         } else if(var6 == 2) {
            ag.a(BlockTallPlant.EnumTallFlowerVariants.PAEONIA);
         }

         for(var7 = 0; var7 < 5; ++var7) {
            int var11 = var2.nextInt(16) + 8;
            int var13 = var2.nextInt(16) + 8;
            int var10 = var2.nextInt(var1.getHighestBlockYAt(var3.a(var11, 0, var13)).getY() + 32);
            if(ag.generate(var1, var2, new BlockPosition(var3.getX() + var11, var10, var3.getZ() + var13))) {
               break;
            }
         }
      }

      super.a(var1, var2, var3);
   }

   protected BiomeBase d(final int var1) {
      if(this.id == BiomeBase.FOREST.id) {
         BiomeForest var2 = new BiomeForest(var1, 1);
         var2.a(new BiomeBase.BiomeTemperature(this.an, this.ao + 0.2F));
         var2.a("Flower Forest");
         var2.a(6976549, true);
         var2.a(8233509);
         return var2;
      } else {
         return this.id != BiomeBase.BIRCH_FOREST.id && this.id != BiomeBase.BIRCH_FOREST_HILLS.id?new BiomeBaseSub(var1, this) {
            public void a(World var1, Random var2, BlockPosition var3) {
               this.aE.a(var1, var2, var3);
            }
         }:new BiomeBaseSub(var1, this) {
            public WorldGenTreeAbstract a(Random var1) {
               return var1.nextBoolean()?BiomeForest.aD:BiomeForest.aE;
            }
         };
      }
   }
}
