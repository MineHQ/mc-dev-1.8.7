package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityBanner;
import net.minecraft.server.World;

public class BlockBanner extends BlockContainer {
   public static final BlockStateDirection FACING;
   public static final BlockStateInteger ROTATION;

   protected BlockBanner() {
      super(Material.WOOD);
      float var1 = 0.25F;
      float var2 = 1.0F;
      this.a(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var2, 0.5F + var1);
   }

   public String getName() {
      return LocaleI18n.get("item.banner.white.name");
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return null;
   }

   public boolean d() {
      return false;
   }

   public boolean b(IBlockAccess var1, BlockPosition var2) {
      return true;
   }

   public boolean c() {
      return false;
   }

   public boolean g() {
      return true;
   }

   public TileEntity a(World var1, int var2) {
      return new TileEntityBanner();
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.BANNER;
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
      TileEntity var6 = var1.getTileEntity(var2);
      if(var6 instanceof TileEntityBanner) {
         ItemStack var7 = new ItemStack(Items.BANNER, 1, ((TileEntityBanner)var6).b());
         NBTTagCompound var8 = new NBTTagCompound();
         var6.b(var8);
         var8.remove("x");
         var8.remove("y");
         var8.remove("z");
         var8.remove("id");
         var7.a((String)"BlockEntityTag", (NBTBase)var8);
         a(var1, var2, var7);
      } else {
         super.dropNaturally(var1, var2, var3, var4, var5);
      }

   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return !this.e(var1, var2) && super.canPlace(var1, var2);
   }

   public void a(World var1, EntityHuman var2, BlockPosition var3, IBlockData var4, TileEntity var5) {
      if(var5 instanceof TileEntityBanner) {
         TileEntityBanner var6 = (TileEntityBanner)var5;
         ItemStack var7 = new ItemStack(Items.BANNER, 1, ((TileEntityBanner)var5).b());
         NBTTagCompound var8 = new NBTTagCompound();
         TileEntityBanner.a(var8, var6.b(), var6.d());
         var7.a((String)"BlockEntityTag", (NBTBase)var8);
         a(var1, var3, var7);
      } else {
         super.a(var1, var2, var3, var4, (TileEntity)null);
      }

   }

   static {
      FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
      ROTATION = BlockStateInteger.of("rotation", 0, 15);
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

   public static class BlockStandingBanner extends BlockBanner {
      public BlockStandingBanner() {
         this.j(this.blockStateList.getBlockData().set(ROTATION, Integer.valueOf(0)));
      }

      public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
         if(!var1.getType(var2.down()).getBlock().getMaterial().isBuildable()) {
            this.b(var1, var2, var3, 0);
            var1.setAir(var2);
         }

         super.doPhysics(var1, var2, var3, var4);
      }

      public IBlockData fromLegacyData(int var1) {
         return this.getBlockData().set(ROTATION, Integer.valueOf(var1));
      }

      public int toLegacyData(IBlockData var1) {
         return ((Integer)var1.get(ROTATION)).intValue();
      }

      protected BlockStateList getStateList() {
         return new BlockStateList(this, new IBlockState[]{ROTATION});
      }
   }

   public static class BlockWallBanner extends BlockBanner {
      public BlockWallBanner() {
         this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
      }

      public void updateShape(IBlockAccess var1, BlockPosition var2) {
         EnumDirection var3 = (EnumDirection)var1.getType(var2).get(FACING);
         float var4 = 0.0F;
         float var5 = 0.78125F;
         float var6 = 0.0F;
         float var7 = 1.0F;
         float var8 = 0.125F;
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         switch(BlockBanner.SyntheticClass_1.a[var3.ordinal()]) {
         case 1:
         default:
            this.a(var6, var4, 1.0F - var8, var7, var5, 1.0F);
            break;
         case 2:
            this.a(var6, var4, 0.0F, var7, var5, var8);
            break;
         case 3:
            this.a(1.0F - var8, var4, var6, 1.0F, var5, var7);
            break;
         case 4:
            this.a(0.0F, var4, var6, var8, var5, var7);
         }

      }

      public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
         EnumDirection var5 = (EnumDirection)var3.get(FACING);
         if(!var1.getType(var2.shift(var5.opposite())).getBlock().getMaterial().isBuildable()) {
            this.b(var1, var2, var3, 0);
            var1.setAir(var2);
         }

         super.doPhysics(var1, var2, var3, var4);
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
   }
}
