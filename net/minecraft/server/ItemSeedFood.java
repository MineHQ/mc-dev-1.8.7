package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.ItemFood;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemSeedFood extends ItemFood {
   private Block b;
   private Block c;

   public ItemSeedFood(int var1, float var2, Block var3, Block var4) {
      super(var1, var2, false);
      this.b = var3;
      this.c = var4;
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var5 != EnumDirection.UP) {
         return false;
      } else if(!var2.a(var4.shift(var5), var5, var1)) {
         return false;
      } else if(var3.getType(var4).getBlock() == this.c && var3.isEmpty(var4.up())) {
         var3.setTypeUpdate(var4.up(), this.b.getBlockData());
         --var1.count;
         return true;
      } else {
         return false;
      }
   }
}
