package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public class BlockMelon extends Block {
   protected BlockMelon() {
      super(Material.PUMPKIN, MaterialMapColor.u);
      this.a(CreativeModeTab.b);
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.MELON;
   }

   public int a(Random var1) {
      return 3 + var1.nextInt(5);
   }

   public int getDropCount(int var1, Random var2) {
      return Math.min(9, this.a(var2) + var2.nextInt(1 + var1));
   }
}
