package net.minecraft.server;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemFood;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.World;

public class ItemSoup extends ItemFood {
   public ItemSoup(int var1) {
      super(var1, false);
      this.c(1);
   }

   public ItemStack b(ItemStack var1, World var2, EntityHuman var3) {
      super.b(var1, var2, var3);
      return new ItemStack(Items.BOWL);
   }
}
