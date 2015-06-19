package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;

public class ItemWithAuxData extends ItemBlock {
   private final Block b;
   private String[] c;

   public ItemWithAuxData(Block var1, boolean var2) {
      super(var1);
      this.b = var1;
      if(var2) {
         this.setMaxDurability(0);
         this.a(true);
      }

   }

   public int filterData(int var1) {
      return var1;
   }

   public ItemWithAuxData a(String[] var1) {
      this.c = var1;
      return this;
   }

   public String e_(ItemStack var1) {
      if(this.c == null) {
         return super.e_(var1);
      } else {
         int var2 = var1.getData();
         return var2 >= 0 && var2 < this.c.length?super.e_(var1) + "." + this.c[var2]:super.e_(var1);
      }
   }
}
