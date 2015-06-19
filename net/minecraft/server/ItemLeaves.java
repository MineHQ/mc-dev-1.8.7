package net.minecraft.server;

import net.minecraft.server.BlockLeaves;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;

public class ItemLeaves extends ItemBlock {
   private final BlockLeaves b;

   public ItemLeaves(BlockLeaves var1) {
      super(var1);
      this.b = var1;
      this.setMaxDurability(0);
      this.a(true);
   }

   public int filterData(int var1) {
      return var1 | 4;
   }

   public String e_(ItemStack var1) {
      return super.getName() + "." + this.b.b(var1.getData()).d();
   }
}
