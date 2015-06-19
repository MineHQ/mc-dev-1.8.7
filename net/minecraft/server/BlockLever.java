package net.minecraft.server;

import java.util.Iterator;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockButtonAbstract;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockLever extends Block {
   public static final BlockStateEnum<BlockLever.EnumLeverPosition> FACING = BlockStateEnum.of("facing", BlockLever.EnumLeverPosition.class);
   public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");

   protected BlockLever() {
      super(Material.ORIENTABLE);
      this.j(this.blockStateList.getBlockData().set(FACING, BlockLever.EnumLeverPosition.NORTH).set(POWERED, Boolean.valueOf(false)));
      this.a(CreativeModeTab.d);
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return null;
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public boolean canPlace(World var1, BlockPosition var2, EnumDirection var3) {
      return a(var1, var2, var3.opposite());
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      EnumDirection[] var3 = EnumDirection.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumDirection var6 = var3[var5];
         if(a(var1, var2, var6)) {
            return true;
         }
      }

      return false;
   }

   protected static boolean a(World var0, BlockPosition var1, EnumDirection var2) {
      return BlockButtonAbstract.a(var0, var1, var2);
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      IBlockData var9 = this.getBlockData().set(POWERED, Boolean.valueOf(false));
      if(a(var1, var2, var3.opposite())) {
         return var9.set(FACING, BlockLever.EnumLeverPosition.a(var3, var8.getDirection()));
      } else {
         Iterator var10 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

         EnumDirection var11;
         do {
            if(!var10.hasNext()) {
               if(World.a((IBlockAccess)var1, (BlockPosition)var2.down())) {
                  return var9.set(FACING, BlockLever.EnumLeverPosition.a(EnumDirection.UP, var8.getDirection()));
               }

               return var9;
            }

            var11 = (EnumDirection)var10.next();
         } while(var11 == var3 || !a(var1, var2, var11.opposite()));

         return var9.set(FACING, BlockLever.EnumLeverPosition.a(var11, var8.getDirection()));
      }
   }

   public static int a(EnumDirection var0) {
      switch(BlockLever.SyntheticClass_1.a[var0.ordinal()]) {
      case 1:
         return 0;
      case 2:
         return 5;
      case 3:
         return 4;
      case 4:
         return 3;
      case 5:
         return 2;
      case 6:
         return 1;
      default:
         return -1;
      }
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(this.e(var1, var2, var3) && !a(var1, var2, ((BlockLever.EnumLeverPosition)var3.get(FACING)).c().opposite())) {
         this.b(var1, var2, var3, 0);
         var1.setAir(var2);
      }

   }

   private boolean e(World var1, BlockPosition var2, IBlockData var3) {
      if(this.canPlace(var1, var2)) {
         return true;
      } else {
         this.b(var1, var2, var3, 0);
         var1.setAir(var2);
         return false;
      }
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      float var3 = 0.1875F;
      switch(BlockLever.SyntheticClass_1.b[((BlockLever.EnumLeverPosition)var1.getType(var2).get(FACING)).ordinal()]) {
      case 1:
         this.a(0.0F, 0.2F, 0.5F - var3, var3 * 2.0F, 0.8F, 0.5F + var3);
         break;
      case 2:
         this.a(1.0F - var3 * 2.0F, 0.2F, 0.5F - var3, 1.0F, 0.8F, 0.5F + var3);
         break;
      case 3:
         this.a(0.5F - var3, 0.2F, 0.0F, 0.5F + var3, 0.8F, var3 * 2.0F);
         break;
      case 4:
         this.a(0.5F - var3, 0.2F, 1.0F - var3 * 2.0F, 0.5F + var3, 0.8F, 1.0F);
         break;
      case 5:
      case 6:
         var3 = 0.25F;
         this.a(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.6F, 0.5F + var3);
         break;
      case 7:
      case 8:
         var3 = 0.25F;
         this.a(0.5F - var3, 0.4F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
      }

   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.isClientSide) {
         return true;
      } else {
         var3 = var3.a(POWERED);
         var1.setTypeAndData(var2, var3, 3);
         var1.makeSound((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, "random.click", 0.3F, ((Boolean)var3.get(POWERED)).booleanValue()?0.6F:0.5F);
         var1.applyPhysics(var2, this);
         EnumDirection var9 = ((BlockLever.EnumLeverPosition)var3.get(FACING)).c();
         var1.applyPhysics(var2.shift(var9.opposite()), this);
         return true;
      }
   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      if(((Boolean)var3.get(POWERED)).booleanValue()) {
         var1.applyPhysics(var2, this);
         EnumDirection var4 = ((BlockLever.EnumLeverPosition)var3.get(FACING)).c();
         var1.applyPhysics(var2.shift(var4.opposite()), this);
      }

      super.remove(var1, var2, var3);
   }

   public int a(IBlockAccess var1, BlockPosition var2, IBlockData var3, EnumDirection var4) {
      return ((Boolean)var3.get(POWERED)).booleanValue()?15:0;
   }

   public int b(IBlockAccess var1, BlockPosition var2, IBlockData var3, EnumDirection var4) {
      return !((Boolean)var3.get(POWERED)).booleanValue()?0:(((BlockLever.EnumLeverPosition)var3.get(FACING)).c() == var4?15:0);
   }

   public boolean isPowerSource() {
      return true;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(FACING, BlockLever.EnumLeverPosition.a(var1 & 7)).set(POWERED, Boolean.valueOf((var1 & 8) > 0));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockLever.EnumLeverPosition)var1.get(FACING)).a();
      if(((Boolean)var1.get(POWERED)).booleanValue()) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, POWERED});
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a;
      // $FF: synthetic field
      static final int[] b;
      // $FF: synthetic field
      static final int[] c = new int[EnumDirection.EnumAxis.values().length];

      static {
         try {
            c[EnumDirection.EnumAxis.X.ordinal()] = 1;
         } catch (NoSuchFieldError var16) {
            ;
         }

         try {
            c[EnumDirection.EnumAxis.Z.ordinal()] = 2;
         } catch (NoSuchFieldError var15) {
            ;
         }

         b = new int[BlockLever.EnumLeverPosition.values().length];

         try {
            b[BlockLever.EnumLeverPosition.EAST.ordinal()] = 1;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            b[BlockLever.EnumLeverPosition.WEST.ordinal()] = 2;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            b[BlockLever.EnumLeverPosition.SOUTH.ordinal()] = 3;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            b[BlockLever.EnumLeverPosition.NORTH.ordinal()] = 4;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            b[BlockLever.EnumLeverPosition.UP_Z.ordinal()] = 5;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            b[BlockLever.EnumLeverPosition.UP_X.ordinal()] = 6;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            b[BlockLever.EnumLeverPosition.DOWN_X.ordinal()] = 7;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            b[BlockLever.EnumLeverPosition.DOWN_Z.ordinal()] = 8;
         } catch (NoSuchFieldError var7) {
            ;
         }

         a = new int[EnumDirection.values().length];

         try {
            a[EnumDirection.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            a[EnumDirection.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            a[EnumDirection.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[EnumDirection.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumLeverPosition implements INamable {
      DOWN_X,
      EAST,
      WEST,
      SOUTH,
      NORTH,
      UP_Z,
      UP_X,
      DOWN_Z;

      private static final BlockLever.EnumLeverPosition[] i;
      private final int j;
      private final String k;
      private final EnumDirection l;

      private EnumLeverPosition(int var3, String var4, EnumDirection var5) {
         this.j = var3;
         this.k = var4;
         this.l = var5;
      }

      public int a() {
         return this.j;
      }

      public EnumDirection c() {
         return this.l;
      }

      public String toString() {
         return this.k;
      }

      public static BlockLever.EnumLeverPosition a(int var0) {
         if(var0 < 0 || var0 >= i.length) {
            var0 = 0;
         }

         return i[var0];
      }

      public static BlockLever.EnumLeverPosition a(EnumDirection var0, EnumDirection var1) {
         switch(BlockLever.SyntheticClass_1.a[var0.ordinal()]) {
         case 1:
            switch(BlockLever.SyntheticClass_1.c[var1.k().ordinal()]) {
            case 1:
               return DOWN_X;
            case 2:
               return DOWN_Z;
            default:
               throw new IllegalArgumentException("Invalid entityFacing " + var1 + " for facing " + var0);
            }
         case 2:
            switch(BlockLever.SyntheticClass_1.c[var1.k().ordinal()]) {
            case 1:
               return UP_X;
            case 2:
               return UP_Z;
            default:
               throw new IllegalArgumentException("Invalid entityFacing " + var1 + " for facing " + var0);
            }
         case 3:
            return NORTH;
         case 4:
            return SOUTH;
         case 5:
            return WEST;
         case 6:
            return EAST;
         default:
            throw new IllegalArgumentException("Invalid facing: " + var0);
         }
      }

      public String getName() {
         return this.k;
      }

      static {
         DOWN_X = new BlockLever.EnumLeverPosition("DOWN_X", 0, 0, "down_x", EnumDirection.DOWN);
         EAST = new BlockLever.EnumLeverPosition("EAST", 1, 1, "east", EnumDirection.EAST);
         WEST = new BlockLever.EnumLeverPosition("WEST", 2, 2, "west", EnumDirection.WEST);
         SOUTH = new BlockLever.EnumLeverPosition("SOUTH", 3, 3, "south", EnumDirection.SOUTH);
         NORTH = new BlockLever.EnumLeverPosition("NORTH", 4, 4, "north", EnumDirection.NORTH);
         UP_Z = new BlockLever.EnumLeverPosition("UP_Z", 5, 5, "up_z", EnumDirection.UP);
         UP_X = new BlockLever.EnumLeverPosition("UP_X", 6, 6, "up_x", EnumDirection.UP);
         DOWN_Z = new BlockLever.EnumLeverPosition("DOWN_Z", 7, 7, "down_z", EnumDirection.DOWN);
         i = new BlockLever.EnumLeverPosition[values().length];
         BlockLever.EnumLeverPosition[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockLever.EnumLeverPosition var3 = var0[var2];
            i[var3.a()] = var3;
         }

      }
   }
}
