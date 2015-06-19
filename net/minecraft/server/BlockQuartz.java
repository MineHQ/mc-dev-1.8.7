package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.World;

public class BlockQuartz extends Block {
   public static final BlockStateEnum<BlockQuartz.EnumQuartzVariant> VARIANT = BlockStateEnum.of("variant", BlockQuartz.EnumQuartzVariant.class);

   public BlockQuartz() {
      super(Material.STONE);
      this.j(this.blockStateList.getBlockData().set(VARIANT, BlockQuartz.EnumQuartzVariant.DEFAULT));
      this.a(CreativeModeTab.b);
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      if(var7 == BlockQuartz.EnumQuartzVariant.LINES_Y.a()) {
         switch(BlockQuartz.SyntheticClass_1.a[var3.k().ordinal()]) {
         case 1:
            return this.getBlockData().set(VARIANT, BlockQuartz.EnumQuartzVariant.LINES_Z);
         case 2:
            return this.getBlockData().set(VARIANT, BlockQuartz.EnumQuartzVariant.LINES_X);
         case 3:
         default:
            return this.getBlockData().set(VARIANT, BlockQuartz.EnumQuartzVariant.LINES_Y);
         }
      } else {
         return var7 == BlockQuartz.EnumQuartzVariant.CHISELED.a()?this.getBlockData().set(VARIANT, BlockQuartz.EnumQuartzVariant.CHISELED):this.getBlockData().set(VARIANT, BlockQuartz.EnumQuartzVariant.DEFAULT);
      }
   }

   public int getDropData(IBlockData var1) {
      BlockQuartz.EnumQuartzVariant var2 = (BlockQuartz.EnumQuartzVariant)var1.get(VARIANT);
      return var2 != BlockQuartz.EnumQuartzVariant.LINES_X && var2 != BlockQuartz.EnumQuartzVariant.LINES_Z?var2.a():BlockQuartz.EnumQuartzVariant.LINES_Y.a();
   }

   protected ItemStack i(IBlockData var1) {
      BlockQuartz.EnumQuartzVariant var2 = (BlockQuartz.EnumQuartzVariant)var1.get(VARIANT);
      return var2 != BlockQuartz.EnumQuartzVariant.LINES_X && var2 != BlockQuartz.EnumQuartzVariant.LINES_Z?super.i(var1):new ItemStack(Item.getItemOf(this), 1, BlockQuartz.EnumQuartzVariant.LINES_Y.a());
   }

   public MaterialMapColor g(IBlockData var1) {
      return MaterialMapColor.p;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(VARIANT, BlockQuartz.EnumQuartzVariant.a(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockQuartz.EnumQuartzVariant)var1.get(VARIANT)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{VARIANT});
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumDirection.EnumAxis.values().length];

      static {
         try {
            a[EnumDirection.EnumAxis.Z.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.EnumAxis.X.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.EnumAxis.Y.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumQuartzVariant implements INamable {
      DEFAULT(0, "default", "default"),
      CHISELED(1, "chiseled", "chiseled"),
      LINES_Y(2, "lines_y", "lines"),
      LINES_X(3, "lines_x", "lines"),
      LINES_Z(4, "lines_z", "lines");

      private static final BlockQuartz.EnumQuartzVariant[] f;
      private final int g;
      private final String h;
      private final String i;

      private EnumQuartzVariant(int var3, String var4, String var5) {
         this.g = var3;
         this.h = var4;
         this.i = var5;
      }

      public int a() {
         return this.g;
      }

      public String toString() {
         return this.i;
      }

      public static BlockQuartz.EnumQuartzVariant a(int var0) {
         if(var0 < 0 || var0 >= f.length) {
            var0 = 0;
         }

         return f[var0];
      }

      public String getName() {
         return this.h;
      }

      static {
         f = new BlockQuartz.EnumQuartzVariant[values().length];
         BlockQuartz.EnumQuartzVariant[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockQuartz.EnumQuartzVariant var3 = var0[var2];
            f[var3.a()] = var3;
         }

      }
   }
}
