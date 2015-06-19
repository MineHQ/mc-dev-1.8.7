package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockHalfTransparent;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Material;

public class BlockGlass extends BlockHalfTransparent {
   public BlockGlass(Material var1, boolean var2) {
      super(var1, var2);
      this.a(CreativeModeTab.b);
   }

   public int a(Random var1) {
      return 0;
   }

   public boolean d() {
      return false;
   }

   protected boolean I() {
      return true;
   }
}
