package net.minecraft.server;

import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Container;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.InventoryUtils;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityHopper;
import net.minecraft.server.World;

public class BlockHopper extends BlockContainer {
   public static final BlockStateDirection FACING = BlockStateDirection.of("facing", new Predicate() {
      public boolean a(EnumDirection var1) {
         return var1 != EnumDirection.UP;
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((EnumDirection)var1);
      }
   });
   public static final BlockStateBoolean ENABLED = BlockStateBoolean.of("enabled");

   public BlockHopper() {
      super(Material.ORE, MaterialMapColor.m);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.DOWN).set(ENABLED, Boolean.valueOf(true)));
      this.a(CreativeModeTab.d);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, Entity var6) {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
      super.a(var1, var2, var3, var4, var5, var6);
      float var7 = 0.125F;
      this.a(0.0F, 0.0F, 0.0F, var7, 1.0F, 1.0F);
      super.a(var1, var2, var3, var4, var5, var6);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var7);
      super.a(var1, var2, var3, var4, var5, var6);
      this.a(1.0F - var7, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.a(var1, var2, var3, var4, var5, var6);
      this.a(0.0F, 0.0F, 1.0F - var7, 1.0F, 1.0F, 1.0F);
      super.a(var1, var2, var3, var4, var5, var6);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      EnumDirection var9 = var3.opposite();
      if(var9 == EnumDirection.UP) {
         var9 = EnumDirection.DOWN;
      }

      return this.getBlockData().set(FACING, var9).set(ENABLED, Boolean.valueOf(true));
   }

   public TileEntity a(World var1, int var2) {
      return new TileEntityHopper();
   }

   public void postPlace(World var1, BlockPosition var2, IBlockData var3, EntityLiving var4, ItemStack var5) {
      super.postPlace(var1, var2, var3, var4, var5);
      if(var5.hasName()) {
         TileEntity var6 = var1.getTileEntity(var2);
         if(var6 instanceof TileEntityHopper) {
            ((TileEntityHopper)var6).a(var5.getName());
         }
      }

   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      this.e(var1, var2, var3);
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.isClientSide) {
         return true;
      } else {
         TileEntity var9 = var1.getTileEntity(var2);
         if(var9 instanceof TileEntityHopper) {
            var4.openContainer((TileEntityHopper)var9);
            var4.b(StatisticList.P);
         }

         return true;
      }
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      this.e(var1, var2, var3);
   }

   private void e(World var1, BlockPosition var2, IBlockData var3) {
      boolean var4 = !var1.isBlockIndirectlyPowered(var2);
      if(var4 != ((Boolean)var3.get(ENABLED)).booleanValue()) {
         var1.setTypeAndData(var2, var3.set(ENABLED, Boolean.valueOf(var4)), 4);
      }

   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      TileEntity var4 = var1.getTileEntity(var2);
      if(var4 instanceof TileEntityHopper) {
         InventoryUtils.dropInventory(var1, var2, (TileEntityHopper)var4);
         var1.updateAdjacentComparators(var2, this);
      }

      super.remove(var1, var2, var3);
   }

   public int b() {
      return 3;
   }

   public boolean d() {
      return false;
   }

   public boolean c() {
      return false;
   }

   public static EnumDirection b(int var0) {
      return EnumDirection.fromType1(var0 & 7);
   }

   public static boolean f(int var0) {
      return (var0 & 8) != 8;
   }

   public boolean isComplexRedstone() {
      return true;
   }

   public int l(World var1, BlockPosition var2) {
      return Container.a(var1.getTileEntity(var2));
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(FACING, b(var1)).set(ENABLED, Boolean.valueOf(f(var1)));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumDirection)var1.get(FACING)).a();
      if(!((Boolean)var1.get(ENABLED)).booleanValue()) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, ENABLED});
   }
}
