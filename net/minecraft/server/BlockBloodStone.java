package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public class BlockBloodStone extends Block {
   public BlockBloodStone() {
      super(Material.STONE);
      this.a(CreativeModeTab.b);
   }

   public MaterialMapColor g(IBlockData var1) {
      return MaterialMapColor.K;
   }
}
