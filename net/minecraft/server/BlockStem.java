package net.minecraft.server;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockCrops;
import net.minecraft.server.BlockPlant;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockFragilePlantElement;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.World;

public class BlockStem extends BlockPlant implements IBlockFragilePlantElement {
   public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 7);
   public static final BlockStateDirection FACING = BlockStateDirection.of("facing", new Predicate() {
      public boolean a(EnumDirection var1) {
         return var1 != EnumDirection.DOWN;
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((EnumDirection)var1);
      }
   });
   private final Block blockFruit;

   protected BlockStem(Block var1) {
      this.j(this.blockStateList.getBlockData().set(AGE, Integer.valueOf(0)).set(FACING, EnumDirection.UP));
      this.blockFruit = var1;
      this.a(true);
      float var2 = 0.125F;
      this.a(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, 0.25F, 0.5F + var2);
      this.a((CreativeModeTab)null);
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      var1 = var1.set(FACING, EnumDirection.UP);
      Iterator var4 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

      while(var4.hasNext()) {
         EnumDirection var5 = (EnumDirection)var4.next();
         if(var2.getType(var3.shift(var5)).getBlock() == this.blockFruit) {
            var1 = var1.set(FACING, var5);
            break;
         }
      }

      return var1;
   }

   protected boolean c(Block var1) {
      return var1 == Blocks.FARMLAND;
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      super.b(var1, var2, var3, var4);
      if(var1.getLightLevel(var2.up()) >= 9) {
         float var5 = BlockCrops.a(this, var1, var2);
         if(var4.nextInt((int)(25.0F / var5) + 1) == 0) {
            int var6 = ((Integer)var3.get(AGE)).intValue();
            if(var6 < 7) {
               var3 = var3.set(AGE, Integer.valueOf(var6 + 1));
               var1.setTypeAndData(var2, var3, 2);
            } else {
               Iterator var7 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

               while(var7.hasNext()) {
                  EnumDirection var8 = (EnumDirection)var7.next();
                  if(var1.getType(var2.shift(var8)).getBlock() == this.blockFruit) {
                     return;
                  }
               }

               var2 = var2.shift(EnumDirection.EnumDirectionLimit.HORIZONTAL.a(var4));
               Block var9 = var1.getType(var2.down()).getBlock();
               if(var1.getType(var2).getBlock().material == Material.AIR && (var9 == Blocks.FARMLAND || var9 == Blocks.DIRT || var9 == Blocks.GRASS)) {
                  var1.setTypeUpdate(var2, this.blockFruit.getBlockData());
               }
            }
         }

      }
   }

   public void g(World var1, BlockPosition var2, IBlockData var3) {
      int var4 = ((Integer)var3.get(AGE)).intValue() + MathHelper.nextInt(var1.random, 2, 5);
      var1.setTypeAndData(var2, var3.set(AGE, Integer.valueOf(Math.min(7, var4))), 2);
   }

   public void j() {
      float var1 = 0.125F;
      this.a(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F, 0.5F + var1);
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      this.maxY = (double)((float)(((Integer)var1.getType(var2).get(AGE)).intValue() * 2 + 2) / 16.0F);
      float var3 = 0.125F;
      this.a(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, (float)this.maxY, 0.5F + var3);
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
      super.dropNaturally(var1, var2, var3, var4, var5);
      if(!var1.isClientSide) {
         Item var6 = this.l();
         if(var6 != null) {
            int var7 = ((Integer)var3.get(AGE)).intValue();

            for(int var8 = 0; var8 < 3; ++var8) {
               if(var1.random.nextInt(15) <= var7) {
                  a(var1, var2, new ItemStack(var6));
               }
            }

         }
      }
   }

   protected Item l() {
      return this.blockFruit == Blocks.PUMPKIN?Items.PUMPKIN_SEEDS:(this.blockFruit == Blocks.MELON_BLOCK?Items.MELON_SEEDS:null);
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return null;
   }

   public boolean a(World var1, BlockPosition var2, IBlockData var3, boolean var4) {
      return ((Integer)var3.get(AGE)).intValue() != 7;
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
      return new BlockStateList(this, new IBlockState[]{AGE, FACING});
   }
}
