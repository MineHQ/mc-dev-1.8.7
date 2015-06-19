package net.minecraft.server;

import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;

public class ItemBook extends Item {
   public ItemBook() {
   }

   public boolean f_(ItemStack var1) {
      return var1.count == 1;
   }

   public int b() {
      return 1;
   }
}
