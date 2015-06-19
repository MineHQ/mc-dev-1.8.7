package net.minecraft.server;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDirectional;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockWood;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockFenceGate extends BlockDirectional {
   public static final BlockStateBoolean OPEN = BlockStateBoolean.of("open");
   public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
   public static final BlockStateBoolean IN_WALL = BlockStateBoolean.of("in_wall");

   public BlockFenceGate(BlockWood.EnumLogVariant var1) {
      super(Material.WOOD, var1.c());
      this.j(this.blockStateList.getBlockData().set(OPEN, Boolean.valueOf(false)).set(POWERED, Boolean.valueOf(false)).set(IN_WALL, Boolean.valueOf(false)));
      this.a(CreativeModeTab.d);
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      EnumDirection.EnumAxis var4 = ((EnumDirection)var1.get(FACING)).k();
      if(var4 == EnumDirection.EnumAxis.Z && (var2.getType(var3.west()).getBlock() == Blocks.COBBLESTONE_WALL || var2.getType(var3.east()).getBlock() == Blocks.COBBLESTONE_WALL) || var4 == EnumDirection.EnumAxis.X && (var2.getType(var3.north()).getBlock() == Blocks.COBBLESTONE_WALL || var2.getType(var3.south()).getBlock() == Blocks.COBBLESTONE_WALL)) {
         var1 = var1.set(IN_WALL, Boolean.valueOf(true));
      }

      return var1;
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return var1.getType(var2.down()).getBlock().getMaterial().isBuildable()?super.canPlace(var1, var2):false;
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      if(((Boolean)var3.get(OPEN)).booleanValue()) {
         return null;
      } else {
         EnumDirection.EnumAxis var4 = ((EnumDirection)var3.get(FACING)).k();
         return var4 == EnumDirection.EnumAxis.Z?new AxisAlignedBB((double)var2.getX(), (double)var2.getY(), (double)((float)var2.getZ() + 0.375F), (double)(var2.getX() + 1), (double)((float)var2.getY() + 1.5F), (double)((float)var2.getZ() + 0.625F)):new AxisAlignedBB((double)((float)var2.getX() + 0.375F), (double)var2.getY(), (double)var2.getZ(), (double)((float)var2.getX() + 0.625F), (double)((float)var2.getY() + 1.5F), (double)(var2.getZ() + 1));
      }
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      EnumDirection.EnumAxis var3 = ((EnumDirection)var1.getType(var2).get(FACING)).k();
      if(var3 == EnumDirection.EnumAxis.Z) {
         this.a(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
      } else {
         this.a(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
      }

   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public boolean b(IBlockAccess var1, BlockPosition var2) {
      return ((Boolean)var1.getType(var2).get(OPEN)).booleanValue();
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return this.getBlockData().set(FACING, var8.getDirection()).set(OPEN, Boolean.valueOf(false)).set(POWERED, Boolean.valueOf(false)).set(IN_WALL, Boolean.valueOf(false));
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(((Boolean)var3.get(OPEN)).booleanValue()) {
         var3 = var3.set(OPEN, Boolean.valueOf(false));
         var1.setTypeAndData(var2, var3, 2);
      } else {
         EnumDirection var9 = EnumDirection.fromAngle((double)var4.yaw);
         if(var3.get(FACING) == var9.opposite()) {
            var3 = var3.set(FACING, var9);
         }

         var3 = var3.set(OPEN, Boolean.valueOf(true));
         var1.setTypeAndData(var2, var3, 2);
      }

      var1.a(var4, ((Boolean)var3.get(OPEN)).booleanValue()?1003:1006, var2, 0);
      return true;
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!var1.isClientSide) {
         boolean var5 = var1.isBlockIndirectlyPowered(var2);
         if(var5 || var4.isPowerSource()) {
            if(var5 && !((Boolean)var3.get(OPEN)).booleanValue() && !((Boolean)var3.get(POWERED)).booleanValue()) {
               var1.setTypeAndData(var2, var3.set(OPEN, Boolean.valueOf(true)).set(POWERED, Boolean.valueOf(true)), 2);
               var1.a((EntityHuman)null, 1003, var2, 0);
            } else if(!var5 && ((Boolean)var3.get(OPEN)).booleanValue() && ((Boolean)var3.get(POWERED)).booleanValue()) {
               var1.setTypeAndData(var2, var3.set(OPEN, Boolean.valueOf(false)).set(POWERED, Boolean.valueOf(false)), 2);
               var1.a((EntityHuman)null, 1006, var2, 0);
            } else if(var5 != ((Boolean)var3.get(POWERED)).booleanValue()) {
               var1.setTypeAndData(var2, var3.set(POWERED, Boolean.valueOf(var5)), 2);
            }
         }

      }
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(FACING, EnumDirection.fromType2(var1)).set(OPEN, Boolean.valueOf((var1 & 4) != 0)).set(POWERED, Boolean.valueOf((var1 & 8) != 0));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumDirection)var1.get(FACING)).b();
      if(((Boolean)var1.get(POWERED)).booleanValue()) {
         var3 |= 8;
      }

      if(((Boolean)var1.get(OPEN)).booleanValue()) {
         var3 |= 4;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, OPEN, POWERED, IN_WALL});
   }
}
