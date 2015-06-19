package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemSeeds extends Item {
   private Block a;
   private Block b;

   public ItemSeeds(Block var1, Block var2) {
      this.a = var1;
      this.b = var2;
      this.a(CreativeModeTab.l);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var5 != EnumDirection.UP) {
         return false;
      } else if(!var2.a(var4.shift(var5), var5, var1)) {
         return false;
      } else if(var3.getType(var4).getBlock() == this.b && var3.isEmpty(var4.up())) {
         var3.setTypeUpdate(var4.up(), this.a.getBlockData());
         --var1.count;
         return true;
      } else {
         return false;
      }
   }
}
