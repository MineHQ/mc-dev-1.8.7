package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.EnumColor;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;

public class ItemCloth extends ItemBlock {
   public ItemCloth(Block var1) {
      super(var1);
      this.setMaxDurability(0);
      this.a(true);
   }

   public int filterData(int var1) {
      return var1;
   }

   public String e_(ItemStack var1) {
      return super.getName() + "." + EnumColor.fromColorIndex(var1.getData()).d();
   }
}
