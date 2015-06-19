package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Item;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.World;

public class BlockHugeMushroom extends Block {
   public static final BlockStateEnum<BlockHugeMushroom.EnumHugeMushroomVariant> VARIANT = BlockStateEnum.of("variant", BlockHugeMushroom.EnumHugeMushroomVariant.class);
   private final Block b;

   public BlockHugeMushroom(Material var1, MaterialMapColor var2, Block var3) {
      super(var1, var2);
      this.j(this.blockStateList.getBlockData().set(VARIANT, BlockHugeMushroom.EnumHugeMushroomVariant.ALL_OUTSIDE));
      this.b = var3;
   }

   public int a(Random var1) {
      return Math.max(0, var1.nextInt(10) - 7);
   }

   public MaterialMapColor g(IBlockData var1) {
      switch(BlockHugeMushroom.SyntheticClass_1.a[((BlockHugeMushroom.EnumHugeMushroomVariant)var1.get(VARIANT)).ordinal()]) {
      case 1:
         return MaterialMapColor.e;
      case 2:
         return MaterialMapColor.d;
      case 3:
         return MaterialMapColor.d;
      default:
         return super.g(var1);
      }
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Item.getItemOf(this.b);
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return this.getBlockData();
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(VARIANT, BlockHugeMushroom.EnumHugeMushroomVariant.a(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockHugeMushroom.EnumHugeMushroomVariant)var1.get(VARIANT)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{VARIANT});
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[BlockHugeMushroom.EnumHugeMushroomVariant.values().length];

      static {
         try {
            a[BlockHugeMushroom.EnumHugeMushroomVariant.ALL_STEM.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[BlockHugeMushroom.EnumHugeMushroomVariant.ALL_INSIDE.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[BlockHugeMushroom.EnumHugeMushroomVariant.STEM.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumHugeMushroomVariant implements INamable {
      NORTH_WEST(1, "north_west"),
      NORTH(2, "north"),
      NORTH_EAST(3, "north_east"),
      WEST(4, "west"),
      CENTER(5, "center"),
      EAST(6, "east"),
      SOUTH_WEST(7, "south_west"),
      SOUTH(8, "south"),
      SOUTH_EAST(9, "south_east"),
      STEM(10, "stem"),
      ALL_INSIDE(0, "all_inside"),
      ALL_OUTSIDE(14, "all_outside"),
      ALL_STEM(15, "all_stem");

      private static final BlockHugeMushroom.EnumHugeMushroomVariant[] n;
      private final int o;
      private final String p;

      private EnumHugeMushroomVariant(int var3, String var4) {
         this.o = var3;
         this.p = var4;
      }

      public int a() {
         return this.o;
      }

      public String toString() {
         return this.p;
      }

      public static BlockHugeMushroom.EnumHugeMushroomVariant a(int var0) {
         if(var0 < 0 || var0 >= n.length) {
            var0 = 0;
         }

         BlockHugeMushroom.EnumHugeMushroomVariant var1 = n[var0];
         return var1 == null?n[0]:var1;
      }

      public String getName() {
         return this.p;
      }

      static {
         n = new BlockHugeMushroom.EnumHugeMushroomVariant[16];
         BlockHugeMushroom.EnumHugeMushroomVariant[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockHugeMushroom.EnumHugeMushroomVariant var3 = var0[var2];
            n[var3.a()] = var3;
         }

      }
   }
}
