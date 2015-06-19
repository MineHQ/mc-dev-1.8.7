package net.minecraft.server;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockSnow;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemSnow extends ItemBlock {
   public ItemSnow(Block var1) {
      super(var1);
      this.setMaxDurability(0);
      this.a(true);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.count == 0) {
         return false;
      } else if(!var2.a(var4, var5, var1)) {
         return false;
      } else {
         IBlockData var9 = var3.getType(var4);
         Block var10 = var9.getBlock();
         BlockPosition var11 = var4;
         if((var5 != EnumDirection.UP || var10 != this.a) && !var10.a(var3, var4)) {
            var11 = var4.shift(var5);
            var9 = var3.getType(var11);
            var10 = var9.getBlock();
         }

         if(var10 == this.a) {
            int var12 = ((Integer)var9.get(BlockSnow.LAYERS)).intValue();
            if(var12 <= 7) {
               IBlockData var13 = var9.set(BlockSnow.LAYERS, Integer.valueOf(var12 + 1));
               AxisAlignedBB var14 = this.a.a(var3, var11, var13);
               if(var14 != null && var3.b(var14) && var3.setTypeAndData(var11, var13, 2)) {
                  var3.makeSound((double)((float)var11.getX() + 0.5F), (double)((float)var11.getY() + 0.5F), (double)((float)var11.getZ() + 0.5F), this.a.stepSound.getPlaceSound(), (this.a.stepSound.getVolume1() + 1.0F) / 2.0F, this.a.stepSound.getVolume2() * 0.8F);
                  --var1.count;
                  return true;
               }
            }
         }

         return super.interactWith(var1, var2, var3, var11, var5, var6, var7, var8);
      }
   }

   public int filterData(int var1) {
      return var1;
   }
}
