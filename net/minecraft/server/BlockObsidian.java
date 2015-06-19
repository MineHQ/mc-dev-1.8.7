package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public class BlockObsidian extends Block {
   public BlockObsidian() {
      super(Material.STONE);
      this.a(CreativeModeTab.b);
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Item.getItemOf(Blocks.OBSIDIAN);
   }

   public MaterialMapColor g(IBlockData var1) {
      return MaterialMapColor.E;
   }
}
