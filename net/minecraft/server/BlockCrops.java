package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPlant;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockSoil;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockFragilePlantElement;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.World;

public class BlockCrops extends BlockPlant implements IBlockFragilePlantElement {
   public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 7);

   protected BlockCrops() {
      this.j(this.blockStateList.getBlockData().set(AGE, Integer.valueOf(0)));
      this.a(true);
      float var1 = 0.5F;
      this.a(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F, 0.5F + var1);
      this.a((CreativeModeTab)null);
      this.c(0.0F);
      this.a(h);
      this.K();
   }

   protected boolean c(Block var1) {
      return var1 == Blocks.FARMLAND;
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      super.b(var1, var2, var3, var4);
      if(var1.getLightLevel(var2.up()) >= 9) {
         int var5 = ((Integer)var3.get(AGE)).intValue();
         if(var5 < 7) {
            float var6 = a(this, var1, var2);
            if(var4.nextInt((int)(25.0F / var6) + 1) == 0) {
               var1.setTypeAndData(var2, var3.set(AGE, Integer.valueOf(var5 + 1)), 2);
            }
         }
      }

   }

   public void g(World var1, BlockPosition var2, IBlockData var3) {
      int var4 = ((Integer)var3.get(AGE)).intValue() + MathHelper.nextInt(var1.random, 2, 5);
      if(var4 > 7) {
         var4 = 7;
      }

      var1.setTypeAndData(var2, var3.set(AGE, Integer.valueOf(var4)), 2);
   }

   protected static float a(Block var0, World var1, BlockPosition var2) {
      float var3 = 1.0F;
      BlockPosition var4 = var2.down();

      for(int var5 = -1; var5 <= 1; ++var5) {
         for(int var6 = -1; var6 <= 1; ++var6) {
            float var7 = 0.0F;
            IBlockData var8 = var1.getType(var4.a(var5, 0, var6));
            if(var8.getBlock() == Blocks.FARMLAND) {
               var7 = 1.0F;
               if(((Integer)var8.get(BlockSoil.MOISTURE)).intValue() > 0) {
                  var7 = 3.0F;
               }
            }

            if(var5 != 0 || var6 != 0) {
               var7 /= 4.0F;
            }

            var3 += var7;
         }
      }

      BlockPosition var12 = var2.north();
      BlockPosition var13 = var2.south();
      BlockPosition var15 = var2.west();
      BlockPosition var14 = var2.east();
      boolean var9 = var0 == var1.getType(var15).getBlock() || var0 == var1.getType(var14).getBlock();
      boolean var10 = var0 == var1.getType(var12).getBlock() || var0 == var1.getType(var13).getBlock();
      if(var9 && var10) {
         var3 /= 2.0F;
      } else {
         boolean var11 = var0 == var1.getType(var15.north()).getBlock() || var0 == var1.getType(var14.north()).getBlock() || var0 == var1.getType(var14.south()).getBlock() || var0 == var1.getType(var15.south()).getBlock();
         if(var11) {
            var3 /= 2.0F;
         }
      }

      return var3;
   }

   public boolean f(World var1, BlockPosition var2, IBlockData var3) {
      return (var1.k(var2) >= 8 || var1.i(var2)) && this.c(var1.getType(var2.down()).getBlock());
   }

   protected Item l() {
      return Items.WHEAT_SEEDS;
   }

   protected Item n() {
      return Items.WHEAT;
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
      super.dropNaturally(var1, var2, var3, var4, 0);
      if(!var1.isClientSide) {
         int var6 = ((Integer)var3.get(AGE)).intValue();
         if(var6 >= 7) {
            int var7 = 3 + var5;

            for(int var8 = 0; var8 < var7; ++var8) {
               if(var1.random.nextInt(15) <= var6) {
                  a(var1, var2, new ItemStack(this.l(), 1, 0));
               }
            }
         }

      }
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return ((Integer)var1.get(AGE)).intValue() == 7?this.n():this.l();
   }

   public boolean a(World var1, BlockPosition var2, IBlockData var3, boolean var4) {
      return ((Integer)var3.get(AGE)).intValue() < 7;
   }

   public boolean a(World var1, Random var2, BlockPosition var3, IBlockData var4) {
      return true;
   }

   public void b(World var1, Random var2, BlockPosition var3, IBlockData var4) {
      this.g(var1, var3, var4);
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(AGE, Integer.valueOf(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Integer)var1.get(AGE)).intValue();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{AGE});
   }
}
