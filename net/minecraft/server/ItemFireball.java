package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class ItemFireball extends Item {
   public ItemFireball() {
      this.a(CreativeModeTab.f);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var3.isClientSide) {
         return true;
      } else {
         var4 = var4.shift(var5);
         if(!var2.a(var4, var5, var1)) {
            return false;
         } else {
            if(var3.getType(var4).getBlock().getMaterial() == Material.AIR) {
               var3.makeSound((double)var4.getX() + 0.5D, (double)var4.getY() + 0.5D, (double)var4.getZ() + 0.5D, "item.fireCharge.use", 1.0F, (g.nextFloat() - g.nextFloat()) * 0.2F + 1.0F);
               var3.setTypeUpdate(var4, Blocks.FIRE.getBlockData());
            }

            if(!var2.abilities.canInstantlyBuild) {
               --var1.count;
            }

            return true;
         }
      }
   }
}
