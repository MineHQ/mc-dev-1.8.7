package net.minecraft.server;

import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Container;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.InventoryUtils;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityBrewingStand;
import net.minecraft.server.World;

public class BlockBrewingStand extends BlockContainer {
   public static final BlockStateBoolean[] HAS_BOTTLE = new BlockStateBoolean[]{BlockStateBoolean.of("has_bottle_0"), BlockStateBoolean.of("has_bottle_1"), BlockStateBoolean.of("has_bottle_2")};

   public BlockBrewingStand() {
      super(Material.ORE);
      this.j(this.blockStateList.getBlockData().set(HAS_BOTTLE[0], Boolean.valueOf(false)).set(HAS_BOTTLE[1], Boolean.valueOf(false)).set(HAS_BOTTLE[2], Boolean.valueOf(false)));
   }

   public String getName() {
      return LocaleI18n.get("item.brewingStand.name");
   }

   public boolean c() {
      return false;
   }

   public int b() {
      return 3;
   }

   public TileEntity a(World var1, int var2) {
      return new TileEntityBrewingStand();
   }

   public boolean d() {
      return false;
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, Entity var6) {
      this.a(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
      super.a(var1, var2, var3, var4, var5, var6);
      this.j();
      super.a(var1, var2, var3, var4, var5, var6);
   }

   public void j() {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.isClientSide) {
         return true;
      } else {
         TileEntity var9 = var1.getTileEntity(var2);
         if(var9 instanceof TileEntityBrewingStand) {
            var4.openContainer((TileEntityBrewingStand)var9);
            var4.b(StatisticList.M);
         }

         return true;
      }
   }

   public void postPlace(World var1, BlockPosition var2, IBlockData var3, EntityLiving var4, ItemStack var5) {
      if(var5.hasName()) {
         TileEntity var6 = var1.getTileEntity(var2);
         if(var6 instanceof TileEntityBrewingStand) {
            ((TileEntityBrewingStand)var6).a(var5.getName());
         }
      }

   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      TileEntity var4 = var1.getTileEntity(var2);
      if(var4 instanceof TileEntityBrewingStand) {
         InventoryUtils.dropInventory(var1, var2, (TileEntityBrewingStand)var4);
      }

      super.remove(var1, var2, var3);
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.BREWING_STAND;
   }

   public boolean isComplexRedstone() {
      return true;
   }

   public int l(World var1, BlockPosition var2) {
      return Container.a(var1.getTileEntity(var2));
   }

   public IBlockData fromLegacyData(int var1) {
      IBlockData var2 = this.getBlockData();

      for(int var3 = 0; var3 < 3; ++var3) {
         var2 = var2.set(HAS_BOTTLE[var3], Boolean.valueOf((var1 & 1 << var3) > 0));
      }

      return var2;
   }

   public int toLegacyData(IBlockData var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < 3; ++var3) {
         if(((Boolean)var1.get(HAS_BOTTLE[var3])).booleanValue()) {
            var2 |= 1 << var3;
         }
      }

      return var2;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{HAS_BOTTLE[0], HAS_BOTTLE[1], HAS_BOTTLE[2]});
   }
}
