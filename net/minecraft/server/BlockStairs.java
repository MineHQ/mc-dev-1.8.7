package net.minecraft.server;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Explosion;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class BlockStairs extends Block {
   public static final BlockStateDirection FACING;
   public static final BlockStateEnum<BlockStairs.EnumHalf> HALF;
   public static final BlockStateEnum<BlockStairs.EnumStairShape> SHAPE;
   private static final int[][] O;
   private final Block P;
   private final IBlockData Q;
   private boolean R;
   private int S;

   protected BlockStairs(IBlockData var1) {
      super(var1.getBlock().material);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(HALF, BlockStairs.EnumHalf.BOTTOM).set(SHAPE, BlockStairs.EnumStairShape.STRAIGHT));
      this.P = var1.getBlock();
      this.Q = var1;
      this.c(this.P.strength);
      this.b(this.P.durability / 3.0F);
      this.a((Block.StepSound)this.P.stepSound);
      this.e(255);
      this.a((CreativeModeTab)CreativeModeTab.b);
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      if(this.R) {
         this.a(0.5F * (float)(this.S % 2), 0.5F * (float)(this.S / 4 % 2), 0.5F * (float)(this.S / 2 % 2), 0.5F + 0.5F * (float)(this.S % 2), 0.5F + 0.5F * (float)(this.S / 4 % 2), 0.5F + 0.5F * (float)(this.S / 2 % 2));
      } else {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public void e(IBlockAccess var1, BlockPosition var2) {
      if(var1.getType(var2).get(HALF) == BlockStairs.EnumHalf.TOP) {
         this.a(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
      } else {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
      }

   }

   public static boolean c(Block var0) {
      return var0 instanceof BlockStairs;
   }

   public static boolean a(IBlockAccess var0, BlockPosition var1, IBlockData var2) {
      IBlockData var3 = var0.getType(var1);
      Block var4 = var3.getBlock();
      return c(var4) && var3.get(HALF) == var2.get(HALF) && var3.get(FACING) == var2.get(FACING);
   }

   public int f(IBlockAccess var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      EnumDirection var4 = (EnumDirection)var3.get(FACING);
      BlockStairs.EnumHalf var5 = (BlockStairs.EnumHalf)var3.get(HALF);
      boolean var6 = var5 == BlockStairs.EnumHalf.TOP;
      IBlockData var7;
      Block var8;
      EnumDirection var9;
      if(var4 == EnumDirection.EAST) {
         var7 = var1.getType(var2.east());
         var8 = var7.getBlock();
         if(c(var8) && var5 == var7.get(HALF)) {
            var9 = (EnumDirection)var7.get(FACING);
            if(var9 == EnumDirection.NORTH && !a(var1, var2.south(), var3)) {
               return var6?1:2;
            }

            if(var9 == EnumDirection.SOUTH && !a(var1, var2.north(), var3)) {
               return var6?2:1;
            }
         }
      } else if(var4 == EnumDirection.WEST) {
         var7 = var1.getType(var2.west());
         var8 = var7.getBlock();
         if(c(var8) && var5 == var7.get(HALF)) {
            var9 = (EnumDirection)var7.get(FACING);
            if(var9 == EnumDirection.NORTH && !a(var1, var2.south(), var3)) {
               return var6?2:1;
            }

            if(var9 == EnumDirection.SOUTH && !a(var1, var2.north(), var3)) {
               return var6?1:2;
            }
         }
      } else if(var4 == EnumDirection.SOUTH) {
         var7 = var1.getType(var2.south());
         var8 = var7.getBlock();
         if(c(var8) && var5 == var7.get(HALF)) {
            var9 = (EnumDirection)var7.get(FACING);
            if(var9 == EnumDirection.WEST && !a(var1, var2.east(), var3)) {
               return var6?2:1;
            }

            if(var9 == EnumDirection.EAST && !a(var1, var2.west(), var3)) {
               return var6?1:2;
            }
         }
      } else if(var4 == EnumDirection.NORTH) {
         var7 = var1.getType(var2.north());
         var8 = var7.getBlock();
         if(c(var8) && var5 == var7.get(HALF)) {
            var9 = (EnumDirection)var7.get(FACING);
            if(var9 == EnumDirection.WEST && !a(var1, var2.east(), var3)) {
               return var6?1:2;
            }

            if(var9 == EnumDirection.EAST && !a(var1, var2.west(), var3)) {
               return var6?2:1;
            }
         }
      }

      return 0;
   }

   public int g(IBlockAccess var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      EnumDirection var4 = (EnumDirection)var3.get(FACING);
      BlockStairs.EnumHalf var5 = (BlockStairs.EnumHalf)var3.get(HALF);
      boolean var6 = var5 == BlockStairs.EnumHalf.TOP;
      IBlockData var7;
      Block var8;
      EnumDirection var9;
      if(var4 == EnumDirection.EAST) {
         var7 = var1.getType(var2.west());
         var8 = var7.getBlock();
         if(c(var8) && var5 == var7.get(HALF)) {
            var9 = (EnumDirection)var7.get(FACING);
            if(var9 == EnumDirection.NORTH && !a(var1, var2.north(), var3)) {
               return var6?1:2;
            }

            if(var9 == EnumDirection.SOUTH && !a(var1, var2.south(), var3)) {
               return var6?2:1;
            }
         }
      } else if(var4 == EnumDirection.WEST) {
         var7 = var1.getType(var2.east());
         var8 = var7.getBlock();
         if(c(var8) && var5 == var7.get(HALF)) {
            var9 = (EnumDirection)var7.get(FACING);
            if(var9 == EnumDirection.NORTH && !a(var1, var2.north(), var3)) {
               return var6?2:1;
            }

            if(var9 == EnumDirection.SOUTH && !a(var1, var2.south(), var3)) {
               return var6?1:2;
            }
         }
      } else if(var4 == EnumDirection.SOUTH) {
         var7 = var1.getType(var2.north());
         var8 = var7.getBlock();
         if(c(var8) && var5 == var7.get(HALF)) {
            var9 = (EnumDirection)var7.get(FACING);
            if(var9 == EnumDirection.WEST && !a(var1, var2.west(), var3)) {
               return var6?2:1;
            }

            if(var9 == EnumDirection.EAST && !a(var1, var2.east(), var3)) {
               return var6?1:2;
            }
         }
      } else if(var4 == EnumDirection.NORTH) {
         var7 = var1.getType(var2.south());
         var8 = var7.getBlock();
         if(c(var8) && var5 == var7.get(HALF)) {
            var9 = (EnumDirection)var7.get(FACING);
            if(var9 == EnumDirection.WEST && !a(var1, var2.west(), var3)) {
               return var6?1:2;
            }

            if(var9 == EnumDirection.EAST && !a(var1, var2.east(), var3)) {
               return var6?2:1;
            }
         }
      }

      return 0;
   }

   public boolean h(IBlockAccess var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      EnumDirection var4 = (EnumDirection)var3.get(FACING);
      BlockStairs.EnumHalf var5 = (BlockStairs.EnumHalf)var3.get(HALF);
      boolean var6 = var5 == BlockStairs.EnumHalf.TOP;
      float var7 = 0.5F;
      float var8 = 1.0F;
      if(var6) {
         var7 = 0.0F;
         var8 = 0.5F;
      }

      float var9 = 0.0F;
      float var10 = 1.0F;
      float var11 = 0.0F;
      float var12 = 0.5F;
      boolean var13 = true;
      IBlockData var14;
      Block var15;
      EnumDirection var16;
      if(var4 == EnumDirection.EAST) {
         var9 = 0.5F;
         var12 = 1.0F;
         var14 = var1.getType(var2.east());
         var15 = var14.getBlock();
         if(c(var15) && var5 == var14.get(HALF)) {
            var16 = (EnumDirection)var14.get(FACING);
            if(var16 == EnumDirection.NORTH && !a(var1, var2.south(), var3)) {
               var12 = 0.5F;
               var13 = false;
            } else if(var16 == EnumDirection.SOUTH && !a(var1, var2.north(), var3)) {
               var11 = 0.5F;
               var13 = false;
            }
         }
      } else if(var4 == EnumDirection.WEST) {
         var10 = 0.5F;
         var12 = 1.0F;
         var14 = var1.getType(var2.west());
         var15 = var14.getBlock();
         if(c(var15) && var5 == var14.get(HALF)) {
            var16 = (EnumDirection)var14.get(FACING);
            if(var16 == EnumDirection.NORTH && !a(var1, var2.south(), var3)) {
               var12 = 0.5F;
               var13 = false;
            } else if(var16 == EnumDirection.SOUTH && !a(var1, var2.north(), var3)) {
               var11 = 0.5F;
               var13 = false;
            }
         }
      } else if(var4 == EnumDirection.SOUTH) {
         var11 = 0.5F;
         var12 = 1.0F;
         var14 = var1.getType(var2.south());
         var15 = var14.getBlock();
         if(c(var15) && var5 == var14.get(HALF)) {
            var16 = (EnumDirection)var14.get(FACING);
            if(var16 == EnumDirection.WEST && !a(var1, var2.east(), var3)) {
               var10 = 0.5F;
               var13 = false;
            } else if(var16 == EnumDirection.EAST && !a(var1, var2.west(), var3)) {
               var9 = 0.5F;
               var13 = false;
            }
         }
      } else if(var4 == EnumDirection.NORTH) {
         var14 = var1.getType(var2.north());
         var15 = var14.getBlock();
         if(c(var15) && var5 == var14.get(HALF)) {
            var16 = (EnumDirection)var14.get(FACING);
            if(var16 == EnumDirection.WEST && !a(var1, var2.east(), var3)) {
               var10 = 0.5F;
               var13 = false;
            } else if(var16 == EnumDirection.EAST && !a(var1, var2.west(), var3)) {
               var9 = 0.5F;
               var13 = false;
            }
         }
      }

      this.a(var9, var7, var11, var10, var8, var12);
      return var13;
   }

   public boolean i(IBlockAccess var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      EnumDirection var4 = (EnumDirection)var3.get(FACING);
      BlockStairs.EnumHalf var5 = (BlockStairs.EnumHalf)var3.get(HALF);
      boolean var6 = var5 == BlockStairs.EnumHalf.TOP;
      float var7 = 0.5F;
      float var8 = 1.0F;
      if(var6) {
         var7 = 0.0F;
         var8 = 0.5F;
      }

      float var9 = 0.0F;
      float var10 = 0.5F;
      float var11 = 0.5F;
      float var12 = 1.0F;
      boolean var13 = false;
      IBlockData var14;
      Block var15;
      EnumDirection var16;
      if(var4 == EnumDirection.EAST) {
         var14 = var1.getType(var2.west());
         var15 = var14.getBlock();
         if(c(var15) && var5 == var14.get(HALF)) {
            var16 = (EnumDirection)var14.get(FACING);
            if(var16 == EnumDirection.NORTH && !a(var1, var2.north(), var3)) {
               var11 = 0.0F;
               var12 = 0.5F;
               var13 = true;
            } else if(var16 == EnumDirection.SOUTH && !a(var1, var2.south(), var3)) {
               var11 = 0.5F;
               var12 = 1.0F;
               var13 = true;
            }
         }
      } else if(var4 == EnumDirection.WEST) {
         var14 = var1.getType(var2.east());
         var15 = var14.getBlock();
         if(c(var15) && var5 == var14.get(HALF)) {
            var9 = 0.5F;
            var10 = 1.0F;
            var16 = (EnumDirection)var14.get(FACING);
            if(var16 == EnumDirection.NORTH && !a(var1, var2.north(), var3)) {
               var11 = 0.0F;
               var12 = 0.5F;
               var13 = true;
            } else if(var16 == EnumDirection.SOUTH && !a(var1, var2.south(), var3)) {
               var11 = 0.5F;
               var12 = 1.0F;
               var13 = true;
            }
         }
      } else if(var4 == EnumDirection.SOUTH) {
         var14 = var1.getType(var2.north());
         var15 = var14.getBlock();
         if(c(var15) && var5 == var14.get(HALF)) {
            var11 = 0.0F;
            var12 = 0.5F;
            var16 = (EnumDirection)var14.get(FACING);
            if(var16 == EnumDirection.WEST && !a(var1, var2.west(), var3)) {
               var13 = true;
            } else if(var16 == EnumDirection.EAST && !a(var1, var2.east(), var3)) {
               var9 = 0.5F;
               var10 = 1.0F;
               var13 = true;
            }
         }
      } else if(var4 == EnumDirection.NORTH) {
         var14 = var1.getType(var2.south());
         var15 = var14.getBlock();
         if(c(var15) && var5 == var14.get(HALF)) {
            var16 = (EnumDirection)var14.get(FACING);
            if(var16 == EnumDirection.WEST && !a(var1, var2.west(), var3)) {
               var13 = true;
            } else if(var16 == EnumDirection.EAST && !a(var1, var2.east(), var3)) {
               var9 = 0.5F;
               var10 = 1.0F;
               var13 = true;
            }
         }
      }

      if(var13) {
         this.a(var9, var7, var11, var10, var8, var12);
      }

      return var13;
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, Entity var6) {
      this.e(var1, var2);
      super.a(var1, var2, var3, var4, var5, var6);
      boolean var7 = this.h(var1, var2);
      super.a(var1, var2, var3, var4, var5, var6);
      if(var7 && this.i(var1, var2)) {
         super.a(var1, var2, var3, var4, var5, var6);
      }

      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public void attack(World var1, BlockPosition var2, EntityHuman var3) {
      this.P.attack(var1, var2, var3);
   }

   public void postBreak(World var1, BlockPosition var2, IBlockData var3) {
      this.P.postBreak(var1, var2, var3);
   }

   public float a(Entity var1) {
      return this.P.a(var1);
   }

   public int a(World var1) {
      return this.P.a(var1);
   }

   public Vec3D a(World var1, BlockPosition var2, Entity var3, Vec3D var4) {
      return this.P.a(var1, var2, var3, var4);
   }

   public boolean A() {
      return this.P.A();
   }

   public boolean a(IBlockData var1, boolean var2) {
      return this.P.a(var1, var2);
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return this.P.canPlace(var1, var2);
   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      this.doPhysics(var1, var2, this.Q, Blocks.AIR);
      this.P.onPlace(var1, var2, this.Q);
   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      this.P.remove(var1, var2, this.Q);
   }

   public void a(World var1, BlockPosition var2, Entity var3) {
      this.P.a(var1, var2, var3);
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      this.P.b(var1, var2, var3, var4);
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      return this.P.interact(var1, var2, this.Q, var4, EnumDirection.DOWN, 0.0F, 0.0F, 0.0F);
   }

   public void wasExploded(World var1, BlockPosition var2, Explosion var3) {
      this.P.wasExploded(var1, var2, var3);
   }

   public MaterialMapColor g(IBlockData var1) {
      return this.P.g(this.Q);
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      IBlockData var9 = super.getPlacedState(var1, var2, var3, var4, var5, var6, var7, var8);
      var9 = var9.set(FACING, var8.getDirection()).set(SHAPE, BlockStairs.EnumStairShape.STRAIGHT);
      return var3 != EnumDirection.DOWN && (var3 == EnumDirection.UP || (double)var5 <= 0.5D)?var9.set(HALF, BlockStairs.EnumHalf.BOTTOM):var9.set(HALF, BlockStairs.EnumHalf.TOP);
   }

   public MovingObjectPosition a(World var1, BlockPosition var2, Vec3D var3, Vec3D var4) {
      MovingObjectPosition[] var5 = new MovingObjectPosition[8];
      IBlockData var6 = var1.getType(var2);
      int var7 = ((EnumDirection)var6.get(FACING)).b();
      boolean var8 = var6.get(HALF) == BlockStairs.EnumHalf.TOP;
      int[] var9 = O[var7 + (var8?4:0)];
      this.R = true;

      for(int var10 = 0; var10 < 8; ++var10) {
         this.S = var10;
         if(Arrays.binarySearch(var9, var10) < 0) {
            var5[var10] = super.a(var1, var2, var3, var4);
         }
      }

      int[] var19 = var9;
      int var11 = var9.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         int var13 = var19[var12];
         var5[var13] = null;
      }

      MovingObjectPosition var20 = null;
      double var21 = 0.0D;
      MovingObjectPosition[] var22 = var5;
      int var14 = var5.length;

      for(int var15 = 0; var15 < var14; ++var15) {
         MovingObjectPosition var16 = var22[var15];
         if(var16 != null) {
            double var17 = var16.pos.distanceSquared(var4);
            if(var17 > var21) {
               var20 = var16;
               var21 = var17;
            }
         }
      }

      return var20;
   }

   public IBlockData fromLegacyData(int var1) {
      IBlockData var2 = this.getBlockData().set(HALF, (var1 & 4) > 0?BlockStairs.EnumHalf.TOP:BlockStairs.EnumHalf.BOTTOM);
      var2 = var2.set(FACING, EnumDirection.fromType1(5 - (var1 & 3)));
      return var2;
   }

   public int toLegacyData(IBlockData var1) {
      int var2 = 0;
      if(var1.get(HALF) == BlockStairs.EnumHalf.TOP) {
         var2 |= 4;
      }

      var2 |= 5 - ((EnumDirection)var1.get(FACING)).a();
      return var2;
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      if(this.h(var2, var3)) {
         switch(this.g(var2, var3)) {
         case 0:
            var1 = var1.set(SHAPE, BlockStairs.EnumStairShape.STRAIGHT);
            break;
         case 1:
            var1 = var1.set(SHAPE, BlockStairs.EnumStairShape.INNER_RIGHT);
            break;
         case 2:
            var1 = var1.set(SHAPE, BlockStairs.EnumStairShape.INNER_LEFT);
         }
      } else {
         switch(this.f(var2, var3)) {
         case 0:
            var1 = var1.set(SHAPE, BlockStairs.EnumStairShape.STRAIGHT);
            break;
         case 1:
            var1 = var1.set(SHAPE, BlockStairs.EnumStairShape.OUTER_RIGHT);
            break;
         case 2:
            var1 = var1.set(SHAPE, BlockStairs.EnumStairShape.OUTER_LEFT);
         }
      }

      return var1;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, HALF, SHAPE});
   }

   static {
      FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
      HALF = BlockStateEnum.of("half", BlockStairs.EnumHalf.class);
      SHAPE = BlockStateEnum.of("shape", BlockStairs.EnumStairShape.class);
      O = new int[][]{{4, 5}, {5, 7}, {6, 7}, {4, 6}, {0, 1}, {1, 3}, {2, 3}, {0, 2}};
   }

   public static enum EnumStairShape implements INamable {
      STRAIGHT("straight"),
      INNER_LEFT("inner_left"),
      INNER_RIGHT("inner_right"),
      OUTER_LEFT("outer_left"),
      OUTER_RIGHT("outer_right");

      private final String f;

      private EnumStairShape(String var3) {
         this.f = var3;
      }

      public String toString() {
         return this.f;
      }

      public String getName() {
         return this.f;
      }
   }

   public static enum EnumHalf implements INamable {
      TOP("top"),
      BOTTOM("bottom");

      private final String c;

      private EnumHalf(String var3) {
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
