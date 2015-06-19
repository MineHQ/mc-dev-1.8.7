package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemRedstone extends Item {
   public ItemRedstone() {
      this.a(CreativeModeTab.d);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      boolean var9 = var3.getType(var4).getBlock().a(var3, var4);
      BlockPosition var10 = var9?var4:var4.shift(var5);
      if(!var2.a(var10, var5, var1)) {
         return false;
      } else {
         Block var11 = var3.getType(var10).getBlock();
         if(!var3.a(var11, var10, false, var5, (Entity)null, var1)) {
            return false;
         } else if(Blocks.REDSTONE_WIRE.canPlace(var3, var10)) {
            --var1.count;
            var3.setTypeUpdate(var10, Blocks.REDSTONE_WIRE.getBlockData());
            return true;
         } else {
            return false;
         }
      }
   }
}
