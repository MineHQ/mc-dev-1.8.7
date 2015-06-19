package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.MathHelper;

public class BlockLightStone extends Block {
   public BlockLightStone(Material var1) {
      super(var1);
      this.a(CreativeModeTab.b);
   }

   public int getDropCount(int var1, Random var2) {
      return MathHelper.clamp(this.a(var2) + var2.nextInt(var1 + 1), 1, 4);
   }

   public int a(Random var1) {
      return 2 + var1.nextInt(3);
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.GLOWSTONE_DUST;
   }

   public MaterialMapColor g(IBlockData var1) {
      return MaterialMapColor.d;
   }
}
