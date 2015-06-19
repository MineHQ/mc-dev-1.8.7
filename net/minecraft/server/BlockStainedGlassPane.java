package net.minecraft.server;

import net.minecraft.server.BlockBeacon;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockThin;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EnumColor;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.World;

public class BlockStainedGlassPane extends BlockThin {
   public static final BlockStateEnum<EnumColor> COLOR = BlockStateEnum.of("color", EnumColor.class);

   public BlockStainedGlassPane() {
      super(Material.SHATTERABLE, false);
      this.j(this.blockStateList.getBlockData().set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)).set(COLOR, EnumColor.WHITE));
      this.a(CreativeModeTab.c);
   }

   public int getDropData(IBlockData var1) {
      return ((EnumColor)var1.get(COLOR)).getColorIndex();
   }

   public MaterialMapColor g(IBlockData var1) {
      return ((EnumColor)var1.get(COLOR)).e();
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(COLOR, EnumColor.fromColorIndex(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((EnumColor)var1.get(COLOR)).getColorIndex();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{NORTH, EAST, WEST, SOUTH, COLOR});
   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      if(!var1.isClientSide) {
         BlockBeacon.f(var1, var2);
      }

   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      if(!var1.isClientSide) {
         BlockBeacon.f(var1, var2);
      }

   }
}
