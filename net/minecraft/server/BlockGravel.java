package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockFalling;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.MaterialMapColor;

public class BlockGravel extends BlockFalling {
   public BlockGravel() {
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      if(var3 > 3) {
         var3 = 3;
      }

      return var2.nextInt(10 - var3 * 3) == 0?Items.FLINT:Item.getItemOf(this);
   }

   public MaterialMapColor g(IBlockData var1) {
      return MaterialMapColor.m;
   }
}
