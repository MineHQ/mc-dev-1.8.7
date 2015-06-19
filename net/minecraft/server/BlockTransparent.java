package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.Material;

public class BlockTransparent extends Block {
   protected boolean R;

   protected BlockTransparent(Material var1, boolean var2) {
      super(var1);
      this.R = var2;
   }

   public boolean c() {
      return false;
   }
}
