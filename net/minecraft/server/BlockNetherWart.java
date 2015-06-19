package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPlant;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.World;

public class BlockNetherWart extends BlockPlant {
   public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 3);

   protected BlockNetherWart() {
      super(Material.PLANT, MaterialMapColor.D);
      this.j(this.blockStateList.getBlockData().set(AGE, Integer.valueOf(0)));
      this.a(true);
      float var1 = 0.5F;
      this.a(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F, 0.5F + var1);
      this.a((CreativeModeTab)null);
   }

   protected boolean c(Block var1) {
      return var1 == Blocks.SOUL_SAND;
   }

   public boolean f(World var1, BlockPosition var2, IBlockData var3) {
      return this.c(var1.getType(var2.down()).getBlock());
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      int var5 = ((Integer)var3.get(AGE)).intValue();
      if(var5 < 3 && var4.nextInt(10) == 0) {
         var3 = var3.set(AGE, Integer.valueOf(var5 + 1));
         var1.setTypeAndData(var2, var3, 2);
      }

      super.b(var1, var2, var3, var4);
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
      if(!var1.isClientSide) {
         int var6 = 1;
         if(((Integer)var3.get(AGE)).intValue() >= 3) {
            var6 = 2 + var1.random.nextInt(3);
            if(var5 > 0) {
               var6 += var1.random.nextInt(var5 + 1);
            }
         }

         for(int var7 = 0; var7 < var6; ++var7) {
            a(var1, var2, new ItemStack(Items.NETHER_WART));
         }

      }
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return null;
   }

   public int a(Random var1) {
      return 0;
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
