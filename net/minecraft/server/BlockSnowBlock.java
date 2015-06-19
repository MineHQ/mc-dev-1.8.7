package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EnumSkyBlock;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockSnowBlock extends Block {
   protected BlockSnowBlock() {
      super(Material.SNOW_BLOCK);
      this.a(true);
      this.a(CreativeModeTab.b);
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.SNOWBALL;
   }

   public int a(Random var1) {
      return 4;
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(var1.b(EnumSkyBlock.BLOCK, var2) > 11) {
         this.b(var1, var2, var1.getType(var2), 0);
         var1.setAir(var2);
      }

   }
}
