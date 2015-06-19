package net.minecraft.server;

import com.google.common.base.Predicate;
import net.minecraft.server.Block;
import net.minecraft.server.BlockMinecartTrackAbstract;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.World;

public class BlockPoweredRail extends BlockMinecartTrackAbstract {
   public static final BlockStateEnum<BlockMinecartTrackAbstract.EnumTrackPosition> SHAPE = BlockStateEnum.a("shape", BlockMinecartTrackAbstract.EnumTrackPosition.class, new Predicate() {
      public boolean a(BlockMinecartTrackAbstract.EnumTrackPosition var1) {
         return var1 != BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST && var1 != BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST && var1 != BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST && var1 != BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST;
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((BlockMinecartTrackAbstract.EnumTrackPosition)var1);
      }
   });
   public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");

   protected BlockPoweredRail() {
      super(true);
      this.j(this.blockStateList.getBlockData().set(SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH).set(POWERED, Boolean.valueOf(false)));
   }

   protected boolean a(World var1, BlockPosition var2, IBlockData var3, boolean var4, int var5) {
      if(var5 >= 8) {
         return false;
      } else {
         int var6 = var2.getX();
         int var7 = var2.getY();
         int var8 = var2.getZ();
         boolean var9 = true;
         BlockMinecartTrackAbstract.EnumTrackPosition var10 = (BlockMinecartTrackAbstract.EnumTrackPosition)var3.get(SHAPE);
         switch(BlockPoweredRail.SyntheticClass_1.a[var10.ordinal()]) {
         case 1:
            if(var4) {
               ++var8;
            } else {
               --var8;
            }
            break;
         case 2:
            if(var4) {
               --var6;
            } else {
               ++var6;
            }
            break;
         case 3:
            if(var4) {
               --var6;
            } else {
               ++var6;
               ++var7;
               var9 = false;
            }

            var10 = BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST;
            break;
         case 4:
            if(var4) {
               --var6;
               ++var7;
               var9 = false;
            } else {
               ++var6;
            }

            var10 = BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST;
            break;
         case 5:
            if(var4) {
               ++var8;
            } else {
               --var8;
               ++var7;
               var9 = false;
            }

            var10 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
            break;
         case 6:
            if(var4) {
               ++var8;
               ++var7;
               var9 = false;
            } else {
               --var8;
            }

            var10 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
         }

         return this.a(var1, new BlockPosition(var6, var7, var8), var4, var5, var10)?true:var9 && this.a(var1, new BlockPosition(var6, var7 - 1, var8), var4, var5, var10);
      }
   }

   protected boolean a(World var1, BlockPosition var2, boolean var3, int var4, BlockMinecartTrackAbstract.EnumTrackPosition var5) {
      IBlockData var6 = var1.getType(var2);
      if(var6.getBlock() != this) {
         return false;
      } else {
         BlockMinecartTrackAbstract.EnumTrackPosition var7 = (BlockMinecartTrackAbstract.EnumTrackPosition)var6.get(SHAPE);
         return var5 == BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST && (var7 == BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH || var7 == BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH || var7 == BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH)?false:(var5 == BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH && (var7 == BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST || var7 == BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST || var7 == BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST)?false:(((Boolean)var6.get(POWERED)).booleanValue()?(var1.isBlockIndirectlyPowered(var2)?true:this.a(var1, var2, var6, var3, var4 + 1)):false));
      }
   }

   protected void b(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      boolean var5 = ((Boolean)var3.get(POWERED)).booleanValue();
      boolean var6 = var1.isBlockIndirectlyPowered(var2) || this.a(var1, var2, var3, true, 0) || this.a(var1, var2, var3, false, 0);
      if(var6 != var5) {
         var1.setTypeAndData(var2, var3.set(POWERED, Boolean.valueOf(var6)), 3);
         var1.applyPhysics(var2.down(), this);
         if(((BlockMinecartTrackAbstract.EnumTrackPosition)var3.get(SHAPE)).c()) {
            var1.applyPhysics(var2.up(), this);
         }
      }

   }

   public IBlockState<BlockMinecartTrackAbstract.EnumTrackPosition> n() {
      return SHAPE;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.a(var1 & 7)).set(POWERED, Boolean.valueOf((var1 & 8) > 0));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockMinecartTrackAbstract.EnumTrackPosition)var1.get(SHAPE)).a();
      if(((Boolean)var1.get(POWERED)).booleanValue()) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{SHAPE, POWERED});
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[BlockMinecartTrackAbstract.EnumTrackPosition.values().length];

      static {
         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
