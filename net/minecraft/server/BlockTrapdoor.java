package net.minecraft.server;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStairs;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockStepAbstract;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class BlockTrapdoor extends Block {
   public static final BlockStateDirection FACING;
   public static final BlockStateBoolean OPEN;
   public static final BlockStateEnum<BlockTrapdoor.EnumTrapdoorHalf> HALF;

   protected BlockTrapdoor(Material var1) {
      super(var1);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(OPEN, Boolean.valueOf(false)).set(HALF, BlockTrapdoor.EnumTrapdoorHalf.BOTTOM));
      float var2 = 0.5F;
      float var3 = 1.0F;
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.a(CreativeModeTab.d);
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public boolean b(IBlockAccess var1, BlockPosition var2) {
      return !((Boolean)var1.getType(var2).get(OPEN)).booleanValue();
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      this.updateShape(var1, var2);
      return super.a(var1, var2, var3);
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      this.d(var1.getType(var2));
   }

   public void j() {
      float var1 = 0.1875F;
      this.a(0.0F, 0.40625F, 0.0F, 1.0F, 0.59375F, 1.0F);
   }

   public void d(IBlockData var1) {
      if(var1.getBlock() == this) {
         boolean var2 = var1.get(HALF) == BlockTrapdoor.EnumTrapdoorHalf.TOP;
         Boolean var3 = (Boolean)var1.get(OPEN);
         EnumDirection var4 = (EnumDirection)var1.get(FACING);
         float var5 = 0.1875F;
         if(var2) {
            this.a(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
         } else {
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
         }

         if(var3.booleanValue()) {
            if(var4 == EnumDirection.NORTH) {
               this.a(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
            }

            if(var4 == EnumDirection.SOUTH) {
               this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
            }

            if(var4 == EnumDirection.WEST) {
               this.a(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }

            if(var4 == EnumDirection.EAST) {
               this.a(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
            }
         }

      }
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(this.material == Material.ORE) {
         return true;
      } else {
         var3 = var3.a(OPEN);
         var1.setTypeAndData(var2, var3, 2);
         var1.a(var4, ((Boolean)var3.get(OPEN)).booleanValue()?1003:1006, var2, 0);
         return true;
      }
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!var1.isClientSide) {
         BlockPosition var5 = var2.shift(((EnumDirection)var3.get(FACING)).opposite());
         if(!c(var1.getType(var5).getBlock())) {
            var1.setAir(var2);
            this.b(var1, var2, var3, 0);
         } else {
            boolean var6 = var1.isBlockIndirectlyPowered(var2);
            if(var6 || var4.isPowerSource()) {
               boolean var7 = ((Boolean)var3.get(OPEN)).booleanValue();
               if(var7 != var6) {
                  var1.setTypeAndData(var2, var3.set(OPEN, Boolean.valueOf(var6)), 2);
                  var1.a((EntityHuman)null, var6?1003:1006, var2, 0);
               }
            }

         }
      }
   }

   public MovingObjectPosition a(World var1, BlockPosition var2, Vec3D var3, Vec3D var4) {
      this.updateShape(var1, var2);
      return super.a(var1, var2, var3, var4);
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      IBlockData var9 = this.getBlockData();
      if(var3.k().c()) {
         var9 = var9.set(FACING, var3).set(OPEN, Boolean.valueOf(false));
         var9 = var9.set(HALF, var5 > 0.5F?BlockTrapdoor.EnumTrapdoorHalf.TOP:BlockTrapdoor.EnumTrapdoorHalf.BOTTOM);
      }

      return var9;
   }

   public boolean canPlace(World var1, BlockPosition var2, EnumDirection var3) {
      return !var3.k().b() && c(var1.getType(var2.shift(var3.opposite())).getBlock());
   }

   protected static EnumDirection b(int var0) {
      switch(var0 & 3) {
      case 0:
         return EnumDirection.NORTH;
      case 1:
         return EnumDirection.SOUTH;
      case 2:
         return EnumDirection.WEST;
      case 3:
      default:
         return EnumDirection.EAST;
      }
   }

   protected static int a(EnumDirection var0) {
      switch(BlockTrapdoor.SyntheticClass_1.a[var0.ordinal()]) {
      case 1:
         return 0;
      case 2:
         return 1;
      case 3:
         return 2;
      case 4:
      default:
         return 3;
      }
   }

   private static boolean c(Block var0) {
      return var0.material.k() && var0.d() || var0 == Blocks.GLOWSTONE || var0 instanceof BlockStepAbstract || var0 instanceof BlockStairs;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(FACING, b(var1)).set(OPEN, Boolean.valueOf((var1 & 4) != 0)).set(HALF, (var1 & 8) == 0?BlockTrapdoor.EnumTrapdoorHalf.BOTTOM:BlockTrapdoor.EnumTrapdoorHalf.TOP);
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | a((EnumDirection)var1.get(FACING));
      if(((Boolean)var1.get(OPEN)).booleanValue()) {
         var3 |= 4;
      }

      if(var1.get(HALF) == BlockTrapdoor.EnumTrapdoorHalf.TOP) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, OPEN, HALF});
   }

   static {
      FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
      OPEN = BlockStateBoolean.of("open");
      HALF = BlockStateEnum.of("half", BlockTrapdoor.EnumTrapdoorHalf.class);
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

   public static enum EnumTrapdoorHalf implements INamable {
      TOP("top"),
      BOTTOM("bottom");

      private final String c;

      private EnumTrapdoorHalf(String var3) {
         this.c = var3;
      }

      public String toString() {
         return this.c;
      }

      public String getName() {
         return this.c;
      }
   }
}
