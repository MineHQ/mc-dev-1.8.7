package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDirt;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.World;

public class BlockMycel extends Block {
   public static final BlockStateBoolean SNOWY = BlockStateBoolean.of("snowy");

   protected BlockMycel() {
      super(Material.GRASS, MaterialMapColor.z);
      this.j(this.blockStateList.getBlockData().set(SNOWY, Boolean.valueOf(false)));
      this.a(true);
      this.a(CreativeModeTab.b);
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      Block var4 = var2.getType(var3.up()).getBlock();
      return var1.set(SNOWY, Boolean.valueOf(var4 == Blocks.SNOW || var4 == Blocks.SNOW_LAYER));
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(!var1.isClientSide) {
         if(var1.getLightLevel(var2.up()) < 4 && var1.getType(var2.up()).getBlock().p() > 2) {
            var1.setTypeUpdate(var2, Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.DIRT));
         } else {
            if(var1.getLightLevel(var2.up()) >= 9) {
               for(int var5 = 0; var5 < 4; ++var5) {
                  BlockPosition var6 = var2.a(var4.nextInt(3) - 1, var4.nextInt(5) - 3, var4.nextInt(3) - 1);
                  IBlockData var7 = var1.getType(var6);
                  Block var8 = var1.getType(var6.up()).getBlock();
                  if(var7.getBlock() == Blocks.DIRT && var7.get(BlockDirt.VARIANT) == BlockDirt.EnumDirtVariant.DIRT && var1.getLightLevel(var6.up()) >= 4 && var8.p() <= 2) {
                     var1.setTypeUpdate(var6, this.getBlockData());
                  }
               }
            }

         }
      }
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Blocks.DIRT.getDropType(Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.DIRT), var2, var3);
   }

   public int toLegacyData(IBlockData var1) {
      return 0;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{SNOWY});
   }
}
