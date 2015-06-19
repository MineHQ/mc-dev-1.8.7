package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityFireworks;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemFireworks extends Item {
   public ItemFireworks() {
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(!var3.isClientSide) {
         EntityFireworks var9 = new EntityFireworks(var3, (double)((float)var4.getX() + var6), (double)((float)var4.getY() + var7), (double)((float)var4.getZ() + var8), var1);
         var3.addEntity(var9);
         if(!var2.abilities.canInstantlyBuild) {
            --var1.count;
         }

         return true;
      } else {
         return false;
      }
   }
}
