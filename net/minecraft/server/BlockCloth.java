package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EnumColor;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public class BlockCloth extends Block {
   public static final BlockStateEnum<EnumColor> COLOR = BlockStateEnum.of("color", EnumColor.class);

   public BlockCloth(Material var1) {
      super(var1);
      this.j(this.blockStateList.getBlockData().set(COLOR, EnumColor.WHITE));
      this.a(CreativeModeTab.b);
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
      return new BlockStateList(this, new IBlockState[]{COLOR});
   }
}
