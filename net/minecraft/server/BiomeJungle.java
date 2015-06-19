package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.BlockLeaves;
import net.minecraft.server.BlockLeaves1;
import net.minecraft.server.BlockLog1;
import net.minecraft.server.BlockLongGrass;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockWood;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityChicken;
import net.minecraft.server.EntityOcelot;
import net.minecraft.server.IBlockData;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenGrass;
import net.minecraft.server.WorldGenGroundBush;
import net.minecraft.server.WorldGenJungleTree;
import net.minecraft.server.WorldGenMelon;
import net.minecraft.server.WorldGenTreeAbstract;
import net.minecraft.server.WorldGenTrees;
import net.minecraft.server.WorldGenVines;
import net.minecraft.server.WorldGenerator;

public class BiomeJungle extends BiomeBase {
   private boolean aD;
   private static final IBlockData aE;
   private static final IBlockData aF;
   private static final IBlockData aG;

   public BiomeJungle(int var1, boolean var2) {
      super(var1);
      this.aD = var2;
      if(var2) {
         this.as.A = 2;
      } else {
         this.as.A = 50;
      }

      this.as.C = 25;
      this.as.B = 4;
      if(!var2) {
         this.at.add(new BiomeBase.BiomeMeta(EntityOcelot.class, 2, 1, 1));
      }

      this.au.add(new BiomeBase.BiomeMeta(EntityChicken.class, 10, 4, 4));
   }

   public WorldGenTreeAbstract a(Random var1) {
      return (WorldGenTreeAbstract)(var1.nextInt(10) == 0?this.aB:(var1.nextInt(2) == 0?new WorldGenGroundBush(aE, aG):(!this.aD && var1.nextInt(3) == 0?new WorldGenJungleTree(false, 10, 20, aE, aF):new WorldGenTrees(false, 4 + var1.nextInt(7), aE, aF, true))));
   }

   public WorldGenerator b(Random var1) {
      return var1.nextInt(4) == 0?new WorldGenGrass(BlockLongGrass.EnumTallGrassType.FERN):new WorldGenGrass(BlockLongGrass.EnumTallGrassType.GRASS);
   }

   public void a(World var1, Random var2, BlockPosition var3) {
      super.a(var1, var2, var3);
      int var4 = var2.nextInt(16) + 8;
      int var5 = var2.nextInt(16) + 8;
      int var6 = var2.nextInt(var1.getHighestBlockYAt(var3.a(var4, 0, var5)).getY() * 2);
      (new WorldGenMelon()).generate(var1, var2, var3.a(var4, var6, var5));
      WorldGenVines var9 = new WorldGenVines();

      for(var5 = 0; var5 < 50; ++var5) {
         var6 = var2.nextInt(16) + 8;
         boolean var7 = true;
         int var8 = var2.nextInt(16) + 8;
         var9.generate(var1, var2, var3.a(var6, 128, var8));
      }

   }

   static {
      aE = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
      aF = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
      aG = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.OAK).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
   }
}
