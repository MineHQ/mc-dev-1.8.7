package net.minecraft.server;

import java.util.Iterator;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockLadder extends Block {
   public static final BlockStateDirection FACING;

   protected BlockLadder() {
      super(Material.ORIENTABLE);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
      this.a(CreativeModeTab.c);
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      this.updateShape(var1, var2);
      return super.a(var1, var2, var3);
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      if(var3.getBlock() == this) {
         float var4 = 0.125F;
         switch(BlockLadder.SyntheticClass_1.a[((EnumDirection)var3.get(FACING)).ordinal()]) {
         case 1:
            this.a(0.0F, 0.0F, 1.0F - var4, 1.0F, 1.0F, 1.0F);
            break;
         case 2:
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var4);
            break;
         case 3:
            this.a(1.0F - var4, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;
         case 4:
         default:
            this.a(0.0F, 0.0F, 0.0F, var4, 1.0F, 1.0F);
         }

      }
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return var1.getType(var2.west()).getBlock().isOccluding()?true:(var1.getType(var2.east()).getBlock().isOccluding()?true:(var1.getType(var2.north()).getBlock().isOccluding()?true:var1.getType(var2.south()).getBlock().isOccluding()));
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      if(var3.k().c() && this.a(var1, var2, var3)) {
         return this.getBlockData().set(FACING, var3);
      } else {
         Iterator var9 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

         EnumDirection var10;
         do {
            if(!var9.hasNext()) {
               return this.getBlockData();
            }

            var10 = (EnumDirection)var9.next();
         } while(!this.a(var1, var2, var10));

         return this.getBlockData().set(FACING, var10);
      }
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      EnumDirection var5 = (EnumDirection)var3.get(FACING);
      if(!this.a(var1, var2, var5)) {
         this.b(var1, var2, var3, 0);
         var1.setAir(var2);
      }

      super.doPhysics(var1, var2, var3, var4);
   }

   protected boolean a(World var1, BlockPosition var2, EnumDirection var3) {
      return var1.getType(var2.shift(var3.opposite())).getBlock().isOccluding();
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
