package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockSnow;
import net.minecraft.server.Blocks;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemReed extends Item {
   private Block a;

   public ItemReed(Block var1) {
      this.a = var1;
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      IBlockData var9 = var3.getType(var4);
      Block var10 = var9.getBlock();
      if(var10 == Blocks.SNOW_LAYER && ((Integer)var9.get(BlockSnow.LAYERS)).intValue() < 1) {
         var5 = EnumDirection.UP;
      } else if(!var10.a(var3, var4)) {
         var4 = var4.shift(var5);
      }

      if(!var2.a(var4, var5, var1)) {
         return false;
      } else if(var1.count == 0) {
         return false;
      } else {
         if(var3.a(this.a, var4, false, var5, (Entity)null, var1)) {
            IBlockData var11 = this.a.getPlacedState(var3, var4, var5, var6, var7, var8, 0, var2);
            if(var3.setTypeAndData(var4, var11, 3)) {
               var11 = var3.getType(var4);
               if(var11.getBlock() == this.a) {
                  ItemBlock.a(var3, var2, var4, var1);
                  var11.getBlock().postPlace(var3, var4, var11, var2, var1);
               }

               var3.makeSound((double)((float)var4.getX() + 0.5F), (double)((float)var4.getY() + 0.5F), (double)((float)var4.getZ() + 0.5F), this.a.stepSound.getPlaceSound(), (this.a.stepSound.getVolume1() + 1.0F) / 2.0F, this.a.stepSound.getVolume2() * 0.8F);
               --var1.count;
               return true;
            }
         }

         return false;
      }
   }
}
