package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDiodeAbstract;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockRepeater;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockRedstoneWire extends Block {
   public static final BlockStateEnum<BlockRedstoneWire.EnumRedstoneWireConnection> NORTH = BlockStateEnum.of("north", BlockRedstoneWire.EnumRedstoneWireConnection.class);
   public static final BlockStateEnum<BlockRedstoneWire.EnumRedstoneWireConnection> EAST = BlockStateEnum.of("east", BlockRedstoneWire.EnumRedstoneWireConnection.class);
   public static final BlockStateEnum<BlockRedstoneWire.EnumRedstoneWireConnection> SOUTH = BlockStateEnum.of("south", BlockRedstoneWire.EnumRedstoneWireConnection.class);
   public static final BlockStateEnum<BlockRedstoneWire.EnumRedstoneWireConnection> WEST = BlockStateEnum.of("west", BlockRedstoneWire.EnumRedstoneWireConnection.class);
   public static final BlockStateInteger POWER = BlockStateInteger.of("power", 0, 15);
   private boolean Q = true;
   private final Set<BlockPosition> R = Sets.newHashSet();

   public BlockRedstoneWire() {
      super(Material.ORIENTABLE);
      this.j(this.blockStateList.getBlockData().set(NORTH, BlockRedstoneWire.EnumRedstoneWireConnection.NONE).set(EAST, BlockRedstoneWire.EnumRedstoneWireConnection.NONE).set(SOUTH, BlockRedstoneWire.EnumRedstoneWireConnection.NONE).set(WEST, BlockRedstoneWire.EnumRedstoneWireConnection.NONE).set(POWER, Integer.valueOf(0)));
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      var1 = var1.set(WEST, this.c(var2, var3, EnumDirection.WEST));
      var1 = var1.set(EAST, this.c(var2, var3, EnumDirection.EAST));
      var1 = var1.set(NORTH, this.c(var2, var3, EnumDirection.NORTH));
      var1 = var1.set(SOUTH, this.c(var2, var3, EnumDirection.SOUTH));
      return var1;
   }

   private BlockRedstoneWire.EnumRedstoneWireConnection c(IBlockAccess var1, BlockPosition var2, EnumDirection var3) {
      BlockPosition var4 = var2.shift(var3);
      Block var5 = var1.getType(var2.shift(var3)).getBlock();
      if(a(var1.getType(var4), var3) || !var5.u() && d(var1.getType(var4.down()))) {
         return BlockRedstoneWire.EnumRedstoneWireConnection.SIDE;
      } else {
         Block var6 = var1.getType(var2.up()).getBlock();
         return !var6.u() && var5.u() && d(var1.getType(var4.up()))?BlockRedstoneWire.EnumRedstoneWireConnection.UP:BlockRedstoneWire.EnumRedstoneWireConnection.NONE;
      }
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

   public boolean canPlace(World var1, BlockPosition var2) {
      return World.a((IBlockAccess)var1, (BlockPosition)var2.down()) || var1.getType(var2.down()).getBlock() == Blocks.GLOWSTONE;
   }

   private IBlockData e(World var1, BlockPosition var2, IBlockData var3) {
      var3 = this.a(var1, var2, var2, var3);
      ArrayList var4 = Lists.newArrayList((Iterable)this.R);
      this.R.clear();
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         BlockPosition var6 = (BlockPosition)var5.next();
         var1.applyPhysics(var6, this);
      }

      return var3;
   }

   private IBlockData a(World var1, BlockPosition var2, BlockPosition var3, IBlockData var4) {
      IBlockData var5 = var4;
      int var6 = ((Integer)var4.get(POWER)).intValue();
      byte var7 = 0;
      int var14 = this.getPower(var1, var3, var7);
      this.Q = false;
      int var8 = var1.A(var2);
      this.Q = true;
      if(var8 > 0 && var8 > var14 - 1) {
         var14 = var8;
      }

      int var9 = 0;
      Iterator var10 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

      while(true) {
         while(var10.hasNext()) {
            EnumDirection var11 = (EnumDirection)var10.next();
            BlockPosition var12 = var2.shift(var11);
            boolean var13 = var12.getX() != var3.getX() || var12.getZ() != var3.getZ();
            if(var13) {
               var9 = this.getPower(var1, var12, var9);
            }

            if(var1.getType(var12).getBlock().isOccluding() && !var1.getType(var2.up()).getBlock().isOccluding()) {
               if(var13 && var2.getY() >= var3.getY()) {
                  var9 = this.getPower(var1, var12.up(), var9);
               }
            } else if(!var1.getType(var12).getBlock().isOccluding() && var13 && var2.getY() <= var3.getY()) {
               var9 = this.getPower(var1, var12.down(), var9);
            }
         }

         if(var9 > var14) {
            var14 = var9 - 1;
         } else if(var14 > 0) {
            --var14;
         } else {
            var14 = 0;
         }

         if(var8 > var14 - 1) {
            var14 = var8;
         }

         if(var6 != var14) {
            var4 = var4.set(POWER, Integer.valueOf(var14));
            if(var1.getType(var2) == var5) {
               var1.setTypeAndData(var2, var4, 2);
            }

            this.R.add(var2);
            EnumDirection[] var15 = EnumDirection.values();
            int var16 = var15.length;

            for(int var17 = 0; var17 < var16; ++var17) {
               EnumDirection var18 = var15[var17];
               this.R.add(var2.shift(var18));
            }
         }

         return var4;
      }
   }

   private void e(World var1, BlockPosition var2) {
      if(var1.getType(var2).getBlock() == this) {
         var1.applyPhysics(var2, this);
         EnumDirection[] var3 = EnumDirection.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            EnumDirection var6 = var3[var5];
            var1.applyPhysics(var2.shift(var6), this);
         }

      }
   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      if(!var1.isClientSide) {
         this.e(var1, var2, var3);
         Iterator var4 = EnumDirection.EnumDirectionLimit.VERTICAL.iterator();

         EnumDirection var5;
         while(var4.hasNext()) {
            var5 = (EnumDirection)var4.next();
            var1.applyPhysics(var2.shift(var5), this);
         }

         var4 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

         while(var4.hasNext()) {
            var5 = (EnumDirection)var4.next();
            this.e(var1, var2.shift(var5));
         }

         var4 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

         while(var4.hasNext()) {
            var5 = (EnumDirection)var4.next();
            BlockPosition var6 = var2.shift(var5);
            if(var1.getType(var6).getBlock().isOccluding()) {
               this.e(var1, var6.up());
            } else {
               this.e(var1, var6.down());
            }
         }

      }
   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      super.remove(var1, var2, var3);
      if(!var1.isClientSide) {
         EnumDirection[] var4 = EnumDirection.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            EnumDirection var7 = var4[var6];
            var1.applyPhysics(var2.shift(var7), this);
         }

         this.e(var1, var2, var3);
         Iterator var8 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

         EnumDirection var9;
         while(var8.hasNext()) {
            var9 = (EnumDirection)var8.next();
            this.e(var1, var2.shift(var9));
         }

         var8 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

         while(var8.hasNext()) {
            var9 = (EnumDirection)var8.next();
            BlockPosition var10 = var2.shift(var9);
            if(var1.getType(var10).getBlock().isOccluding()) {
               this.e(var1, var10.up());
            } else {
               this.e(var1, var10.down());
            }
         }

      }
   }

   public int getPower(World var1, BlockPosition var2, int var3) {
      if(var1.getType(var2).getBlock() != this) {
         return var3;
      } else {
         int var4 = ((Integer)var1.getType(var2).get(POWER)).intValue();
         return var4 > var3?var4:var3;
      }
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!var1.isClientSide) {
         if(this.canPlace(var1, var2)) {
            this.e(var1, var2, var3);
         } else {
            this.b(var1, var2, var3, 0);
            var1.setAir(var2);
         }

      }
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.REDSTONE;
   }

   public int b(IBlockAccess var1, BlockPosition var2, IBlockData var3, EnumDirection var4) {
      return !this.Q?0:this.a(var1, var2, var3, var4);
   }

   public int a(IBlockAccess var1, BlockPosition var2, IBlockData var3, EnumDirection var4) {
      if(!this.Q) {
         return 0;
      } else {
         int var5 = ((Integer)var3.get(POWER)).intValue();
         if(var5 == 0) {
            return 0;
         } else if(var4 == EnumDirection.UP) {
            return var5;
         } else {
            EnumSet var6 = EnumSet.noneOf(EnumDirection.class);
            Iterator var7 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

            while(var7.hasNext()) {
               EnumDirection var8 = (EnumDirection)var7.next();
               if(this.d(var1, var2, var8)) {
                  var6.add(var8);
               }
            }

            if(var4.k().c() && var6.isEmpty()) {
               return var5;
            } else if(var6.contains(var4) && !var6.contains(var4.f()) && !var6.contains(var4.e())) {
               return var5;
            } else {
               return 0;
            }
         }
      }
   }

   private boolean d(IBlockAccess var1, BlockPosition var2, EnumDirection var3) {
      BlockPosition var4 = var2.shift(var3);
      IBlockData var5 = var1.getType(var4);
      Block var6 = var5.getBlock();
      boolean var7 = var6.isOccluding();
      boolean var8 = var1.getType(var2.up()).getBlock().isOccluding();
      return !var8 && var7 && e(var1, var4.up())?true:(a(var5, var3)?true:(var6 == Blocks.POWERED_REPEATER && var5.get(BlockDiodeAbstract.FACING) == var3?true:!var7 && e(var1, var4.down())));
   }

   protected static boolean e(IBlockAccess var0, BlockPosition var1) {
      return d(var0.getType(var1));
   }

   protected static boolean d(IBlockData var0) {
      return a(var0, (EnumDirection)null);
   }

   protected static boolean a(IBlockData var0, EnumDirection var1) {
      Block var2 = var0.getBlock();
      if(var2 == Blocks.REDSTONE_WIRE) {
         return true;
      } else if(Blocks.UNPOWERED_REPEATER.e(var2)) {
         EnumDirection var3 = (EnumDirection)var0.get(BlockRepeater.FACING);
         return var3 == var1 || var3.opposite() == var1;
      } else {
         return var2.isPowerSource() && var1 != null;
      }
   }

   public boolean isPowerSource() {
      return this.Q;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(POWER, Integer.valueOf(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Integer)var1.get(POWER)).intValue();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{NORTH, EAST, SOUTH, WEST, POWER});
   }

   static enum EnumRedstoneWireConnection implements INamable {
      UP("up"),
      SIDE("side"),
      NONE("none");

      private final String d;

      private EnumRedstoneWireConnection(String var3) {
         this.d = var3;
      }

      public String toString() {
         return this.getName();
      }

      public String getName() {
         return this.d;
      }
   }
}
