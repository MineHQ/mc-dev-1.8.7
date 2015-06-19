package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockSign;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.World;

public class BlockFloorSign extends BlockSign {
   public static final BlockStateInteger ROTATION = BlockStateInteger.of("rotation", 0, 15);

   public BlockFloorSign() {
      this.j(this.blockStateList.getBlockData().set(ROTATION, Integer.valueOf(0)));
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!var1.getType(var2.down()).getBlock().getMaterial().isBuildable()) {
         this.b(var1, var2, var3, 0);
         var1.setAir(var2);
      }

      super.doPhysics(var1, var2, var3, var4);
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(ROTATION, Integer.valueOf(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Integer)var1.get(ROTATION)).intValue();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{ROTATION});
   }
}
