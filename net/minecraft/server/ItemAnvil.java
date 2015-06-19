package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.ItemMultiTexture;

public class ItemAnvil extends ItemMultiTexture {
   public ItemAnvil(Block var1) {
      super(var1, var1, new String[]{"intact", "slightlyDamaged", "veryDamaged"});
   }

   public int filterData(int var1) {
      return var1 << 2;
   }
}
