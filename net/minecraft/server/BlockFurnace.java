package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.Container;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.InventoryUtils;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityFurnace;
import net.minecraft.server.World;

public class BlockFurnace extends BlockContainer {
   public static final BlockStateDirection FACING;
   private final boolean b;
   private static boolean N;

   protected BlockFurnace(boolean var1) {
      super(Material.STONE);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
      this.b = var1;
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Item.getItemOf(Blocks.FURNACE);
   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      this.e(var1, var2, var3);
   }

   private void e(World var1, BlockPosition var2, IBlockData var3) {
      if(!var1.isClientSide) {
         Block var4 = var1.getType(var2.north()).getBlock();
         Block var5 = var1.getType(var2.south()).getBlock();
         Block var6 = var1.getType(var2.west()).getBlock();
         Block var7 = var1.getType(var2.east()).getBlock();
         EnumDirection var8 = (EnumDirection)var3.get(FACING);
         if(var8 == EnumDirection.NORTH && var4.o() && !var5.o()) {
            var8 = EnumDirection.SOUTH;
         } else if(var8 == EnumDirection.SOUTH && var5.o() && !var4.o()) {
            var8 = EnumDirection.NORTH;
         } else if(var8 == EnumDirection.WEST && var6.o() && !var7.o()) {
            var8 = EnumDirection.EAST;
         } else if(var8 == EnumDirection.EAST && var7.o() && !var6.o()) {
            var8 = EnumDirection.WEST;
         }

         var1.setTypeAndData(var2, var3.set(FACING, var8), 2);
      }
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.isClientSide) {
         return true;
      } else {
         TileEntity var9 = var1.getTileEntity(var2);
         if(var9 instanceof TileEntityFurnace) {
            var4.openContainer((TileEntityFurnace)var9);
            var4.b(StatisticList.Y);
         }

         return true;
      }
   }

   public static void a(boolean var0, World var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      TileEntity var4 = var1.getTileEntity(var2);
      N = true;
      if(var0) {
         var1.setTypeAndData(var2, Blocks.LIT_FURNACE.getBlockData().set(FACING, var3.get(FACING)), 3);
         var1.setTypeAndData(var2, Blocks.LIT_FURNACE.getBlockData().set(FACING, var3.get(FACING)), 3);
      } else {
         var1.setTypeAndData(var2, Blocks.FURNACE.getBlockData().set(FACING, var3.get(FACING)), 3);
         var1.setTypeAndData(var2, Blocks.FURNACE.getBlockData().set(FACING, var3.get(FACING)), 3);
      }

      N = false;
      if(var4 != null) {
         var4.D();
         var1.setTileEntity(var2, var4);
      }

   }

   public TileEntity a(World var1, int var2) {
      return new TileEntityFurnace();
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return this.getBlockData().set(FACING, var8.getDirection().opposite());
   }

   public void postPlace(World var1, BlockPosition var2, IBlockData var3, EntityLiving var4, ItemStack var5) {
      var1.setTypeAndData(var2, var3.set(FACING, var4.getDirection().opposite()), 2);
      if(var5.hasName()) {
         TileEntity var6 = var1.getTileEntity(var2);
         if(var6 instanceof TileEntityFurnace) {
            ((TileEntityFurnace)var6).a(var5.getName());
         }
      }

   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      if(!N) {
         TileEntity var4 = var1.getTileEntity(var2);
         if(var4 instanceof TileEntityFurnace) {
            InventoryUtils.dropInventory(var1, var2, (TileEntityFurnace)var4);
            var1.updateAdjacentComparators(var2, this);
         }
      }

      super.remove(var1, var2, var3);
   }

   public boolean isComplexRedstone() {
      return true;
   }

   public int l(World var1, BlockPosition var2) {
      return Container.a(var1.getTileEntity(var2));
   }

   public int b() {
      return 3;
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
}
