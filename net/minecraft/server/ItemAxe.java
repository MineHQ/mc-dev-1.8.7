package net.minecraft.server;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.server.Block;
import net.minecraft.server.Blocks;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemTool;
import net.minecraft.server.Material;

public class ItemAxe extends ItemTool {
   private static final Set<Block> c;

   protected ItemAxe(Item.EnumToolMaterial var1) {
      super(3.0F, var1, c);
   }

   public float getDestroySpeed(ItemStack var1, Block var2) {
      return var2.getMaterial() != Material.WOOD && var2.getMaterial() != Material.PLANT && var2.getMaterial() != Material.REPLACEABLE_PLANT?super.getDestroySpeed(var1, var2):this.a;
   }

   static {
      c = Sets.newHashSet((Object[])(new Block[]{Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER}));
   }
}
