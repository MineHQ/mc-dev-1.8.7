package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPiston;
import net.minecraft.server.BlockPistonExtension;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityPiston;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class BlockPistonMoving extends BlockContainer {
   public static final BlockStateDirection FACING;
   public static final BlockStateEnum<BlockPistonExtension.EnumPistonType> TYPE;

   public BlockPistonMoving() {
      super(Material.PISTON);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT));
      this.c(-1.0F);
   }

   public TileEntity a(World var1, int var2) {
      return null;
   }

   public static TileEntity a(IBlockData var0, EnumDirection var1, boolean var2, boolean var3) {
      return new TileEntityPiston(var0, var1, var2, var3);
   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      TileEntity var4 = var1.getTileEntity(var2);
      if(var4 instanceof TileEntityPiston) {
         ((TileEntityPiston)var4).h();
      } else {
         super.remove(var1, var2, var3);
      }

   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return false;
   }

   public boolean canPlace(World var1, BlockPosition var2, EnumDirection var3) {
      return false;
   }

   public void postBreak(World var1, BlockPosition var2, IBlockData var3) {
      BlockPosition var4 = var2.shift(((EnumDirection)var3.get(FACING)).opposite());
      IBlockData var5 = var1.getType(var4);
      if(var5.getBlock() instanceof BlockPiston && ((Boolean)var5.get(BlockPiston.EXTENDED)).booleanValue()) {
         var1.setAir(var4);
      }

   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(!var1.isClientSide && var1.getTileEntity(var2) == null) {
         var1.setAir(var2);
         return true;
      } else {
         return false;
      }
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return null;
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
      if(!var1.isClientSide) {
         TileEntityPiston var6 = this.e(var1, var2);
         if(var6 != null) {
            IBlockData var7 = var6.b();
            var7.getBlock().b(var1, var2, var7, 0);
         }
      }
   }

   public MovingObjectPosition a(World var1, BlockPosition var2, Vec3D var3, Vec3D var4) {
      return null;
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!var1.isClientSide) {
         var1.getTileEntity(var2);
      }

   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      TileEntityPiston var4 = this.e(var1, var2);
      if(var4 == null) {
         return null;
      } else {
         float var5 = var4.a(0.0F);
         if(var4.d()) {
            var5 = 1.0F - var5;
         }

         return this.a(var1, var2, var4.b(), var5, var4.e());
      }
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      TileEntityPiston var3 = this.e(var1, var2);
      if(var3 != null) {
         IBlockData var4 = var3.b();
         Block var5 = var4.getBlock();
         if(var5 == this || var5.getMaterial() == Material.AIR) {
            return;
         }

         float var6 = var3.a(0.0F);
         if(var3.d()) {
            var6 = 1.0F - var6;
         }

         var5.updateShape(var1, var2);
         if(var5 == Blocks.PISTON || var5 == Blocks.STICKY_PISTON) {
            var6 = 0.0F;
         }

         EnumDirection var7 = var3.e();
         this.minX = var5.B() - (double)((float)var7.getAdjacentX() * var6);
         this.minY = var5.D() - (double)((float)var7.getAdjacentY() * var6);
         this.minZ = var5.F() - (double)((float)var7.getAdjacentZ() * var6);
         this.maxX = var5.C() - (double)((float)var7.getAdjacentX() * var6);
         this.maxY = var5.E() - (double)((float)var7.getAdjacentY() * var6);
         this.maxZ = var5.G() - (double)((float)var7.getAdjacentZ() * var6);
      }

   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3, float var4, EnumDirection var5) {
      if(var3.getBlock() != this && var3.getBlock().getMaterial() != Material.AIR) {
         AxisAlignedBB var6 = var3.getBlock().a(var1, var2, var3);
         if(var6 == null) {
            return null;
         } else {
            double var7 = var6.a;
            double var9 = var6.b;
            double var11 = var6.c;
            double var13 = var6.d;
            double var15 = var6.e;
            double var17 = var6.f;
            if(var5.getAdjacentX() < 0) {
               var7 -= (double)((float)var5.getAdjacentX() * var4);
            } else {
               var13 -= (double)((float)var5.getAdjacentX() * var4);
            }

            if(var5.getAdjacentY() < 0) {
               var9 -= (double)((float)var5.getAdjacentY() * var4);
            } else {
               var15 -= (double)((float)var5.getAdjacentY() * var4);
            }

            if(var5.getAdjacentZ() < 0) {
               var11 -= (double)((float)var5.getAdjacentZ() * var4);
            } else {
               var17 -= (double)((float)var5.getAdjacentZ() * var4);
            }

            return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
         }
      } else {
         return null;
      }
   }

   private TileEntityPiston e(IBlockAccess var1, BlockPosition var2) {
      TileEntity var3 = var1.getTileEntity(var2);
      return var3 instanceof TileEntityPiston?(TileEntityPiston)var3:null;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(FACING, BlockPistonExtension.b(var1)).set(TYPE, (var1 & 8) > 0?BlockPistonExtension.EnumPistonType.STICKY:BlockPistonExtension.EnumPistonType.DEFAULT);
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumDirection)var1.get(FACING)).a();
      if(var1.get(TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, TYPE});
   }

   static {
      FACING = BlockPistonExtension.FACING;
      TYPE = BlockPistonExtension.TYPE;
   }
}
