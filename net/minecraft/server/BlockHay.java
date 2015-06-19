package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockRotatable;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.World;

public class BlockHay extends BlockRotatable {
   public BlockHay() {
      super(Material.GRASS, MaterialMapColor.t);
      this.j(this.blockStateList.getBlockData().set(AXIS, EnumDirection.EnumAxis.Y));
      this.a(CreativeModeTab.b);
   }

   public IBlockData fromLegacyData(int var1) {
      EnumDirection.EnumAxis var2 = EnumDirection.EnumAxis.Y;
      int var3 = var1 & 12;
      if(var3 == 4) {
         var2 = EnumDirection.EnumAxis.X;
      } else if(var3 == 8) {
         var2 = EnumDirection.EnumAxis.Z;
      }

      return this.getBlockData().set(AXIS, var2);
   }

   public int toLegacyData(IBlockData var1) {
      int var2 = 0;
      EnumDirection.EnumAxis var3 = (EnumDirection.EnumAxis)var1.get(AXIS);
      if(var3 == EnumDirection.EnumAxis.X) {
         var2 |= 4;
      } else if(var3 == EnumDirection.EnumAxis.Z) {
         var2 |= 8;
      }

      return var2;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{AXIS});
   }

   protected ItemStack i(IBlockData var1) {
      return new ItemStack(Item.getItemOf(this), 1, 0);
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return super.getPlacedState(var1, var2, var3, var4, var5, var6, var7, var8).set(AXIS, var3.k());
   }
}
