package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public class BlockHardenedClay extends Block {
   public BlockHardenedClay() {
      super(Material.STONE);
      this.a(CreativeModeTab.b);
   }

   public MaterialMapColor g(IBlockData var1) {
      return MaterialMapColor.q;
   }
}
