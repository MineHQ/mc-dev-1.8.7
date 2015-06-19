package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPlant;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockDeadBush extends BlockPlant {
   protected BlockDeadBush() {
      super(Material.REPLACEABLE_PLANT);
      float var1 = 0.4F;
      this.a(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.8F, 0.5F + var1);
   }

   public MaterialMapColor g(IBlockData var1) {
      return MaterialMapColor.o;
   }

   protected boolean c(Block var1) {
      return var1 == Blocks.SAND || var1 == Blocks.HARDENED_CLAY || var1 == Blocks.STAINED_HARDENED_CLAY || var1 == Blocks.DIRT;
   }

   public boolean a(World var1, BlockPosition var2) {
      return true;
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return null;
   }

   public void a(World var1, EntityHuman var2, BlockPosition var3, IBlockData var4, TileEntity var5) {
      if(!var1.isClientSide && var2.bZ() != null && var2.bZ().getItem() == Items.SHEARS) {
         var2.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
         a(var1, var3, new ItemStack(Blocks.DEADBUSH, 1, 0));
      } else {
         super.a(var1, var2, var3, var4, var5);
      }

   }
}
