package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public abstract class BlockMinecartTrackAbstract extends Block {
   protected final boolean a;

   public static boolean e(World var0, BlockPosition var1) {
      return d(var0.getType(var1));
   }

   public static boolean d(IBlockData var0) {
      Block var1 = var0.getBlock();
      return var1 == Blocks.RAIL || var1 == Blocks.GOLDEN_RAIL || var1 == Blocks.DETECTOR_RAIL || var1 == Blocks.ACTIVATOR_RAIL;
   }

   protected BlockMinecartTrackAbstract(boolean var1) {
      super(Material.ORIENTABLE);
      this.a = var1;
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
      this.a(CreativeModeTab.e);
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return null;
   }

   public boolean c() {
      return false;
   }

   public MovingObjectPosition a(World var1, BlockPosition var2, Vec3D var3, Vec3D var4) {
      this.updateShape(var1, var2);
      return super.a(var1, var2, var3, var4);
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      BlockMinecartTrackAbstract.EnumTrackPosition var4 = var3.getBlock() == this?(BlockMinecartTrackAbstract.EnumTrackPosition)var3.get(this.n()):null;
      if(var4 != null && var4.c()) {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
      } else {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
      }

   }

   public boolean d() {
      return false;
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return World.a((IBlockAccess)var1, (BlockPosition)var2.down());
   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      if(!var1.isClientSide) {
         var3 = this.a(var1, var2, var3, true);
         if(this.a) {
            this.doPhysics(var1, var2, var3, this);
         }
      }

   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!var1.isClientSide) {
         BlockMinecartTrackAbstract.EnumTrackPosition var5 = (BlockMinecartTrackAbstract.EnumTrackPosition)var3.get(this.n());
         boolean var6 = false;
         if(!World.a((IBlockAccess)var1, (BlockPosition)var2.down())) {
            var6 = true;
         }

         if(var5 == BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST && !World.a((IBlockAccess)var1, (BlockPosition)var2.east())) {
            var6 = true;
         } else if(var5 == BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST && !World.a((IBlockAccess)var1, (BlockPosition)var2.west())) {
            var6 = true;
         } else if(var5 == BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH && !World.a((IBlockAccess)var1, (BlockPosition)var2.north())) {
            var6 = true;
         } else if(var5 == BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH && !World.a((IBlockAccess)var1, (BlockPosition)var2.south())) {
            var6 = true;
         }

         if(var6) {
            this.b(var1, var2, var3, 0);
            var1.setAir(var2);
         } else {
            this.b(var1, var2, var3, var4);
         }

      }
   }

   protected void b(World var1, BlockPosition var2, IBlockData var3, Block var4) {
   }

   protected IBlockData a(World var1, BlockPosition var2, IBlockData var3, boolean var4) {
      return var1.isClientSide?var3:(new BlockMinecartTrackAbstract.MinecartTrackLogic(var1, var2, var3)).a(var1.isBlockIndirectlyPowered(var2), var4).b();
   }

   public int k() {
      return 0;
   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      super.remove(var1, var2, var3);
      if(((BlockMinecartTrackAbstract.EnumTrackPosition)var3.get(this.n())).c()) {
         var1.applyPhysics(var2.up(), this);
      }

      if(this.a) {
         var1.applyPhysics(var2, this);
         var1.applyPhysics(var2.down(), this);
      }

   }

   public abstract IBlockState<BlockMinecartTrackAbstract.EnumTrackPosition> n();

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[BlockMinecartTrackAbstract.EnumTrackPosition.values().length];

      static {
         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH.ordinal()] = 1;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST.ordinal()] = 2;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST.ordinal()] = 3;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST.ordinal()] = 4;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH.ordinal()] = 5;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH.ordinal()] = 6;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST.ordinal()] = 7;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST.ordinal()] = 8;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST.ordinal()] = 9;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST.ordinal()] = 10;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumTrackPosition implements INamable {
      NORTH_SOUTH(0, "north_south"),
      EAST_WEST(1, "east_west"),
      ASCENDING_EAST(2, "ascending_east"),
      ASCENDING_WEST(3, "ascending_west"),
      ASCENDING_NORTH(4, "ascending_north"),
      ASCENDING_SOUTH(5, "ascending_south"),
      SOUTH_EAST(6, "south_east"),
      SOUTH_WEST(7, "south_west"),
      NORTH_WEST(8, "north_west"),
      NORTH_EAST(9, "north_east");

      private static final BlockMinecartTrackAbstract.EnumTrackPosition[] k;
      private final int l;
      private final String m;

      private EnumTrackPosition(int var3, String var4) {
         this.l = var3;
         this.m = var4;
      }

      public int a() {
         return this.l;
      }

      public String toString() {
         return this.m;
      }

      public boolean c() {
         return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
      }

      public static BlockMinecartTrackAbstract.EnumTrackPosition a(int var0) {
         if(var0 < 0 || var0 >= k.length) {
            var0 = 0;
         }

         return k[var0];
      }

      public String getName() {
         return this.m;
      }

      static {
         k = new BlockMinecartTrackAbstract.EnumTrackPosition[values().length];
         BlockMinecartTrackAbstract.EnumTrackPosition[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockMinecartTrackAbstract.EnumTrackPosition var3 = var0[var2];
            k[var3.a()] = var3;
         }

      }
   }

   public class MinecartTrackLogic {
      private final World b;
      private final BlockPosition c;
      private final BlockMinecartTrackAbstract d;
      private IBlockData e;
      private final boolean f;
      private final List<BlockPosition> g = Lists.newArrayList();

      public MinecartTrackLogic(World var2, BlockPosition var3, IBlockData var4) {
         this.b = var2;
         this.c = var3;
         this.e = var4;
         this.d = (BlockMinecartTrackAbstract)var4.getBlock();
         BlockMinecartTrackAbstract.EnumTrackPosition var5 = (BlockMinecartTrackAbstract.EnumTrackPosition)var4.get(BlockMinecartTrackAbstract.this.n());
         this.f = this.d.a;
         this.a(var5);
      }

      private void a(BlockMinecartTrackAbstract.EnumTrackPosition var1) {
         this.g.clear();
         switch(BlockMinecartTrackAbstract.SyntheticClass_1.a[var1.ordinal()]) {
         case 1:
            this.g.add(this.c.north());
            this.g.add(this.c.south());
            break;
         case 2:
            this.g.add(this.c.west());
            this.g.add(this.c.east());
            break;
         case 3:
            this.g.add(this.c.west());
            this.g.add(this.c.east().up());
            break;
         case 4:
            this.g.add(this.c.west().up());
            this.g.add(this.c.east());
            break;
         case 5:
            this.g.add(this.c.north().up());
            this.g.add(this.c.south());
            break;
         case 6:
            this.g.add(this.c.north());
            this.g.add(this.c.south().up());
            break;
         case 7:
            this.g.add(this.c.east());
            this.g.add(this.c.south());
            break;
         case 8:
            this.g.add(this.c.west());
            this.g.add(this.c.south());
            break;
         case 9:
            this.g.add(this.c.west());
            this.g.add(this.c.north());
            break;
         case 10:
            this.g.add(this.c.east());
            this.g.add(this.c.north());
         }

      }

      private void c() {
         for(int var1 = 0; var1 < this.g.size(); ++var1) {
            BlockMinecartTrackAbstract.MinecartTrackLogic var2 = this.b((BlockPosition)this.g.get(var1));
            if(var2 != null && var2.a(this)) {
               this.g.set(var1, var2.c);
            } else {
               this.g.remove(var1--);
            }
         }

      }

      private boolean a(BlockPosition var1) {
         return BlockMinecartTrackAbstract.e(this.b, var1) || BlockMinecartTrackAbstract.e(this.b, var1.up()) || BlockMinecartTrackAbstract.e(this.b, var1.down());
      }

      private BlockMinecartTrackAbstract.MinecartTrackLogic b(BlockPosition var1) {
         IBlockData var3 = this.b.getType(var1);
         if(BlockMinecartTrackAbstract.d(var3)) {
            return BlockMinecartTrackAbstract.this.new MinecartTrackLogic(this.b, var1, var3);
         } else {
            BlockPosition var2 = var1.up();
            var3 = this.b.getType(var2);
            if(BlockMinecartTrackAbstract.d(var3)) {
               return BlockMinecartTrackAbstract.this.new MinecartTrackLogic(this.b, var2, var3);
            } else {
               var2 = var1.down();
               var3 = this.b.getType(var2);
               return BlockMinecartTrackAbstract.d(var3)?BlockMinecartTrackAbstract.this.new MinecartTrackLogic(this.b, var2, var3):null;
            }
         }
      }

      private boolean a(BlockMinecartTrackAbstract.MinecartTrackLogic var1) {
         return this.c(var1.c);
      }

      private boolean c(BlockPosition var1) {
         for(int var2 = 0; var2 < this.g.size(); ++var2) {
            BlockPosition var3 = (BlockPosition)this.g.get(var2);
            if(var3.getX() == var1.getX() && var3.getZ() == var1.getZ()) {
               return true;
            }
         }

         return false;
      }

      protected int a() {
         int var1 = 0;
         Iterator var2 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

         while(var2.hasNext()) {
            EnumDirection var3 = (EnumDirection)var2.next();
            if(this.a(this.c.shift(var3))) {
               ++var1;
            }
         }

         return var1;
      }

      private boolean b(BlockMinecartTrackAbstract.MinecartTrackLogic var1) {
         return this.a(var1) || this.g.size() != 2;
      }

      private void c(BlockMinecartTrackAbstract.MinecartTrackLogic var1) {
         this.g.add(var1.c);
         BlockPosition var2 = this.c.north();
         BlockPosition var3 = this.c.south();
         BlockPosition var4 = this.c.west();
         BlockPosition var5 = this.c.east();
         boolean var6 = this.c(var2);
         boolean var7 = this.c(var3);
         boolean var8 = this.c(var4);
         boolean var9 = this.c(var5);
         BlockMinecartTrackAbstract.EnumTrackPosition var10 = null;
         if(var6 || var7) {
            var10 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
         }

         if(var8 || var9) {
            var10 = BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST;
         }

         if(!this.f) {
            if(var7 && var9 && !var6 && !var8) {
               var10 = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST;
            }

            if(var7 && var8 && !var6 && !var9) {
               var10 = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST;
            }

            if(var6 && var8 && !var7 && !var9) {
               var10 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST;
            }

            if(var6 && var9 && !var7 && !var8) {
               var10 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST;
            }
         }

         if(var10 == BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH) {
            if(BlockMinecartTrackAbstract.e(this.b, var2.up())) {
               var10 = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH;
            }

            if(BlockMinecartTrackAbstract.e(this.b, var3.up())) {
               var10 = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH;
            }
         }

         if(var10 == BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST) {
            if(BlockMinecartTrackAbstract.e(this.b, var5.up())) {
               var10 = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST;
            }

            if(BlockMinecartTrackAbstract.e(this.b, var4.up())) {
               var10 = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST;
            }
         }

         if(var10 == null) {
            var10 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
         }

         this.e = this.e.set(this.d.n(), var10);
         this.b.setTypeAndData(this.c, this.e, 3);
      }

      private boolean d(BlockPosition var1) {
         BlockMinecartTrackAbstract.MinecartTrackLogic var2 = this.b(var1);
         if(var2 == null) {
            return false;
         } else {
            var2.c();
            return var2.b(this);
         }
      }

      public BlockMinecartTrackAbstract.MinecartTrackLogic a(boolean var1, boolean var2) {
         BlockPosition var3 = this.c.north();
         BlockPosition var4 = this.c.south();
         BlockPosition var5 = this.c.west();
         BlockPosition var6 = this.c.east();
         boolean var7 = this.d(var3);
         boolean var8 = this.d(var4);
         boolean var9 = this.d(var5);
         boolean var10 = this.d(var6);
         BlockMinecartTrackAbstract.EnumTrackPosition var11 = null;
         if((var7 || var8) && !var9 && !var10) {
            var11 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
         }

         if((var9 || var10) && !var7 && !var8) {
            var11 = BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST;
         }

         if(!this.f) {
            if(var8 && var10 && !var7 && !var9) {
               var11 = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST;
            }

            if(var8 && var9 && !var7 && !var10) {
               var11 = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST;
            }

            if(var7 && var9 && !var8 && !var10) {
               var11 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST;
            }

            if(var7 && var10 && !var8 && !var9) {
               var11 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST;
            }
         }

         if(var11 == null) {
            if(var7 || var8) {
               var11 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
            }

            if(var9 || var10) {
               var11 = BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST;
            }

            if(!this.f) {
               if(var1) {
                  if(var8 && var10) {
                     var11 = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST;
                  }

                  if(var9 && var8) {
                     var11 = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST;
                  }

                  if(var10 && var7) {
                     var11 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST;
                  }

                  if(var7 && var9) {
                     var11 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST;
                  }
               } else {
                  if(var7 && var9) {
                     var11 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST;
                  }

                  if(var10 && var7) {
                     var11 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST;
                  }

                  if(var9 && var8) {
                     var11 = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST;
                  }

                  if(var8 && var10) {
                     var11 = BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST;
                  }
               }
            }
         }

         if(var11 == BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH) {
            if(BlockMinecartTrackAbstract.e(this.b, var3.up())) {
               var11 = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH;
            }

            if(BlockMinecartTrackAbstract.e(this.b, var4.up())) {
               var11 = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH;
            }
         }

         if(var11 == BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST) {
            if(BlockMinecartTrackAbstract.e(this.b, var6.up())) {
               var11 = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST;
            }

            if(BlockMinecartTrackAbstract.e(this.b, var5.up())) {
               var11 = BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST;
            }
         }

         if(var11 == null) {
            var11 = BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
         }

         this.a(var11);
         this.e = this.e.set(this.d.n(), var11);
         if(var2 || this.b.getType(this.c) != this.e) {
            this.b.setTypeAndData(this.c, this.e, 3);

            for(int var12 = 0; var12 < this.g.size(); ++var12) {
               BlockMinecartTrackAbstract.MinecartTrackLogic var13 = this.b((BlockPosition)this.g.get(var12));
               if(var13 != null) {
                  var13.c();
                  if(var13.b(this)) {
                     var13.c(this);
                  }
               }
            }
         }

         return this;
      }

      public IBlockData b() {
         return this.e;
      }
   }
}
