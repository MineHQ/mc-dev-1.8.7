package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockSign;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.World;

public class BlockWallSign extends BlockSign {
   public static final BlockStateDirection FACING;

   public BlockWallSign() {
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      EnumDirection var3 = (EnumDirection)var1.getType(var2).get(FACING);
      float var4 = 0.28125F;
      float var5 = 0.78125F;
      float var6 = 0.0F;
      float var7 = 1.0F;
      float var8 = 0.125F;
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      switch(BlockWallSign.SyntheticClass_1.a[var3.ordinal()]) {
      case 1:
         this.a(var6, var4, 1.0F - var8, var7, var5, 1.0F);
         break;
      case 2:
         this.a(var6, var4, 0.0F, var7, var5, var8);
         break;
      case 3:
         this.a(1.0F - var8, var4, var6, 1.0F, var5, var7);
         break;
      case 4:
         this.a(0.0F, var4, var6, var8, var5, var7);
      }

   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      EnumDirection var5 = (EnumDirection)var3.get(FACING);
      if(!var1.getType(var2.shift(var5.opposite())).getBlock().getMaterial().isBuildable()) {
         this.b(var1, var2, var3, 0);
         var1.setAir(var2);
      }

      super.doPhysics(var1, var2, var3, var4);
   }

   public IBlockData fromLegacyData(int var1) {
      EnumDirection var2 = EnumDirection.fromType1(var1);
      if(var2.k() == EnumDirection.EnumAxis.Y) {
         var2 = EnumDirection.NORTH;
      }

      return this.getBlockData().set(FACING, var2);
   }

   public int toLegacyData(IBlockData var1) {
      return ((EnumDirection)var1.get(FACING)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING});
   }

   static {
      FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumDirection.values().length];

      static {
         try {
            a[EnumDirection.NORTH.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[EnumDirection.SOUTH.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.WEST.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.EAST.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
