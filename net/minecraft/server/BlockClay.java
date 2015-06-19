package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;

public class BlockClay extends Block {
   public BlockClay() {
      super(Material.CLAY);
      this.a(CreativeModeTab.b);
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.CLAY_BALL;
   }

   public int a(Random var1) {
      return 4;
   }
}
