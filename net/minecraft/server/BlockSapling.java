package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockLeaves;
import net.minecraft.server.BlockLeaves1;
import net.minecraft.server.BlockLog1;
import net.minecraft.server.BlockPlant;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockWood;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockFragilePlantElement;
import net.minecraft.server.IBlockState;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenAcaciaTree;
import net.minecraft.server.WorldGenBigTree;
import net.minecraft.server.WorldGenForest;
import net.minecraft.server.WorldGenForestTree;
import net.minecraft.server.WorldGenJungleTree;
import net.minecraft.server.WorldGenMegaTree;
import net.minecraft.server.WorldGenTaiga2;
import net.minecraft.server.WorldGenTrees;
import net.minecraft.server.WorldGenerator;

public class BlockSapling extends BlockPlant implements IBlockFragilePlantElement {
   public static final BlockStateEnum<BlockWood.EnumLogVariant> TYPE = BlockStateEnum.of("type", BlockWood.EnumLogVariant.class);
   public static final BlockStateInteger STAGE = BlockStateInteger.of("stage", 0, 1);

   protected BlockSapling() {
      this.j(this.blockStateList.getBlockData().set(TYPE, BlockWood.EnumLogVariant.OAK).set(STAGE, Integer.valueOf(0)));
      float var1 = 0.4F;
      this.a(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var1 * 2.0F, 0.5F + var1);
      this.a(CreativeModeTab.c);
   }

   public String getName() {
      return LocaleI18n.get(this.a() + "." + BlockWood.EnumLogVariant.OAK.d() + ".name");
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(!var1.isClientSide) {
         super.b(var1, var2, var3, var4);
         if(var1.getLightLevel(var2.up()) >= 9 && var4.nextInt(7) == 0) {
            this.grow(var1, var2, var3, var4);
         }

      }
   }

   public void grow(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(((Integer)var3.get(STAGE)).intValue() == 0) {
         var1.setTypeAndData(var2, var3.a(STAGE), 4);
      } else {
         this.e(var1, var2, var3, var4);
      }

   }

   public void e(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      Object var5 = var4.nextInt(10) == 0?new WorldGenBigTree(true):new WorldGenTrees(true);
      int var6 = 0;
      int var7 = 0;
      boolean var8 = false;
      IBlockData var9;
      switch(BlockSapling.SyntheticClass_1.a[((BlockWood.EnumLogVariant)var3.get(TYPE)).ordinal()]) {
      case 1:
         label68:
         for(var6 = 0; var6 >= -1; --var6) {
            for(var7 = 0; var7 >= -1; --var7) {
               if(this.a(var1, var2, var6, var7, BlockWood.EnumLogVariant.SPRUCE)) {
                  var5 = new WorldGenMegaTree(false, var4.nextBoolean());
                  var8 = true;
                  break label68;
               }
            }
         }

         if(!var8) {
            var7 = 0;
            var6 = 0;
            var5 = new WorldGenTaiga2(true);
         }
         break;
      case 2:
         var5 = new WorldGenForest(true, false);
         break;
      case 3:
         var9 = Blocks.LOG.getBlockData().set(BlockLog1.VARIANT, BlockWood.EnumLogVariant.JUNGLE);
         IBlockData var10 = Blocks.LEAVES.getBlockData().set(BlockLeaves1.VARIANT, BlockWood.EnumLogVariant.JUNGLE).set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

         label82:
         for(var6 = 0; var6 >= -1; --var6) {
            for(var7 = 0; var7 >= -1; --var7) {
               if(this.a(var1, var2, var6, var7, BlockWood.EnumLogVariant.JUNGLE)) {
                  var5 = new WorldGenJungleTree(true, 10, 20, var9, var10);
                  var8 = true;
                  break label82;
               }
            }
         }

         if(!var8) {
            var7 = 0;
            var6 = 0;
            var5 = new WorldGenTrees(true, 4 + var4.nextInt(7), var9, var10, false);
         }
         break;
      case 4:
         var5 = new WorldGenAcaciaTree(true);
         break;
      case 5:
         label96:
         for(var6 = 0; var6 >= -1; --var6) {
            for(var7 = 0; var7 >= -1; --var7) {
               if(this.a(var1, var2, var6, var7, BlockWood.EnumLogVariant.DARK_OAK)) {
                  var5 = new WorldGenForestTree(true);
                  var8 = true;
                  break label96;
               }
            }
         }

         if(!var8) {
            return;
         }
      case 6:
      }

      var9 = Blocks.AIR.getBlockData();
      if(var8) {
         var1.setTypeAndData(var2.a(var6, 0, var7), var9, 4);
         var1.setTypeAndData(var2.a(var6 + 1, 0, var7), var9, 4);
         var1.setTypeAndData(var2.a(var6, 0, var7 + 1), var9, 4);
         var1.setTypeAndData(var2.a(var6 + 1, 0, var7 + 1), var9, 4);
      } else {
         var1.setTypeAndData(var2, var9, 4);
      }

      if(!((WorldGenerator)var5).generate(var1, var4, var2.a(var6, 0, var7))) {
         if(var8) {
            var1.setTypeAndData(var2.a(var6, 0, var7), var3, 4);
            var1.setTypeAndData(var2.a(var6 + 1, 0, var7), var3, 4);
            var1.setTypeAndData(var2.a(var6, 0, var7 + 1), var3, 4);
            var1.setTypeAndData(var2.a(var6 + 1, 0, var7 + 1), var3, 4);
         } else {
            var1.setTypeAndData(var2, var3, 4);
         }
      }

   }

   private boolean a(World var1, BlockPosition var2, int var3, int var4, BlockWood.EnumLogVariant var5) {
      return this.a(var1, var2.a(var3, 0, var4), var5) && this.a(var1, var2.a(var3 + 1, 0, var4), var5) && this.a(var1, var2.a(var3, 0, var4 + 1), var5) && this.a(var1, var2.a(var3 + 1, 0, var4 + 1), var5);
   }

   public boolean a(World var1, BlockPosition var2, BlockWood.EnumLogVariant var3) {
      IBlockData var4 = var1.getType(var2);
      return var4.getBlock() == this && var4.get(TYPE) == var3;
   }

   public int getDropData(IBlockData var1) {
      return ((BlockWood.EnumLogVariant)var1.get(TYPE)).a();
   }

   public boolean a(World var1, BlockPosition var2, IBlockData var3, boolean var4) {
      return true;
   }

   public boolean a(World var1, Random var2, BlockPosition var3, IBlockData var4) {
      return (double)var1.random.nextFloat() < 0.45D;
   }

   public void b(World var1, Random var2, BlockPosition var3, IBlockData var4) {
      this.grow(var1, var3, var4, var2);
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(TYPE, BlockWood.EnumLogVariant.a(var1 & 7)).set(STAGE, Integer.valueOf((var1 & 8) >> 3));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockWood.EnumLogVariant)var1.get(TYPE)).a();
      var3 |= ((Integer)var1.get(STAGE)).intValue() << 3;
      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{TYPE, STAGE});
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[BlockWood.EnumLogVariant.values().length];

      static {
         try {
            a[BlockWood.EnumLogVariant.SPRUCE.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.BIRCH.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.JUNGLE.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.ACACIA.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.DARK_OAK.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.OAK.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
