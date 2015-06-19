package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.InventoryEnderChest;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityEnderChest;
import net.minecraft.server.World;

public class BlockEnderChest extends BlockContainer {
   public static final BlockStateDirection FACING;

   protected BlockEnderChest() {
      super(Material.STONE);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
      this.a(CreativeModeTab.c);
      this.a(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public int b() {
      return 2;
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Item.getItemOf(Blocks.OBSIDIAN);
   }

   public int a(Random var1) {
      return 8;
   }

   protected boolean I() {
      return true;
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return this.getBlockData().set(FACING, var8.getDirection().opposite());
   }

   public void postPlace(World var1, BlockPosition var2, IBlockData var3, EntityLiving var4, ItemStack var5) {
      var1.setTypeAndData(var2, var3.set(FACING, var4.getDirection().opposite()), 2);
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      InventoryEnderChest var9 = var4.getEnderChest();
      TileEntity var10 = var1.getTileEntity(var2);
      if(var9 != null && var10 instanceof TileEntityEnderChest) {
         if(var1.getType(var2.up()).getBlock().isOccluding()) {
            return true;
         } else if(var1.isClientSide) {
            return true;
         } else {
            var9.a((TileEntityEnderChest)var10);
            var4.openContainer(var9);
            var4.b(StatisticList.V);
            return true;
         }
      } else {
         return true;
      }
   }

   public TileEntity a(World var1, int var2) {
      return new TileEntityEnderChest();
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
