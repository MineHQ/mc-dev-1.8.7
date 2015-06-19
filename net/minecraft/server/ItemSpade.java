package net.minecraft.server;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.server.Block;
import net.minecraft.server.Blocks;
import net.minecraft.server.Item;
import net.minecraft.server.ItemTool;

public class ItemSpade extends ItemTool {
   private static final Set<Block> c;

   public ItemSpade(Item.EnumToolMaterial var1) {
      super(1.0F, var1, c);
   }

   public boolean canDestroySpecialBlock(Block var1) {
      return var1 == Blocks.SNOW_LAYER?true:var1 == Blocks.SNOW;
   }

   static {
      c = Sets.newHashSet((Object[])(new Block[]{Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND}));
   }
}
