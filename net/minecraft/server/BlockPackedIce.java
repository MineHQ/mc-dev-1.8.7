package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Material;

public class BlockPackedIce extends Block {
   public BlockPackedIce() {
      super(Material.SNOW_LAYER);
      this.frictionFactor = 0.98F;
      this.a(CreativeModeTab.b);
   }

   public int a(Random var1) {
      return 0;
   }
}
