package net.minecraft.server;

import net.minecraft.server.BlockCrops;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.World;

public class BlockPotatoes extends BlockCrops {
   public BlockPotatoes() {
   }

   protected Item l() {
      return Items.POTATO;
   }

   protected Item n() {
      return Items.POTATO;
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
      super.dropNaturally(var1, var2, var3, var4, var5);
      if(!var1.isClientSide) {
         if(((Integer)var3.get(AGE)).intValue() >= 7 && var1.random.nextInt(50) == 0) {
            a(var1, var2, new ItemStack(Items.POISONOUS_POTATO));
         }

      }
   }
}
