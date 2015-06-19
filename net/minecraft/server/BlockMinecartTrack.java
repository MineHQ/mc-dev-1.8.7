package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockMinecartTrackAbstract;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.World;

public class BlockMinecartTrack extends BlockMinecartTrackAbstract {
   public static final BlockStateEnum<BlockMinecartTrackAbstract.EnumTrackPosition> SHAPE = BlockStateEnum.of("shape", BlockMinecartTrackAbstract.EnumTrackPosition.class);

   protected BlockMinecartTrack() {
      super(false);
      this.j(this.blockStateList.getBlockData().set(SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH));
   }

   protected void b(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(var4.isPowerSource() && (new BlockMinecartTrackAbstract.MinecartTrackLogic(var1, var2, var3)).a() == 3) {
         this.a(var1, var2, var3, false);
      }

   }

   public IBlockState<BlockMinecartTrackAbstract.EnumTrackPosition> n() {
      return SHAPE;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.a(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockMinecartTrackAbstract.EnumTrackPosition)var1.get(SHAPE)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{SHAPE});
   }
}
