package net.minecraft.server;

import com.google.common.base.Function;
import net.minecraft.server.Block;
import net.minecraft.server.ItemMultiTexture;
import net.minecraft.server.ItemStack;

public class ItemTallPlant extends ItemMultiTexture {
   public ItemTallPlant(Block var1, Block var2, Function<ItemStack, String> var3) {
      super(var1, var2, var3);
   }
}
