package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.ItemBlock;

public class ItemPiston extends ItemBlock {
   public ItemPiston(Block var1) {
      super(var1);
   }

   public int filterData(int var1) {
      return 7;
   }
}
