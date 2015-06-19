package net.minecraft.server;

import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;

public class ItemCoal extends Item {
   public ItemCoal() {
      this.a(true);
      this.setMaxDurability(0);
      this.a(CreativeModeTab.l);
   }

   public String e_(ItemStack var1) {
      return var1.getData() == 1?"item.charcoal":"item.coal";
   }
}
