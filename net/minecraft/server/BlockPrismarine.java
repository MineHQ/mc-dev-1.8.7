package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public class BlockPrismarine extends Block {
   public static final BlockStateEnum<BlockPrismarine.EnumPrismarineVariant> VARIANT = BlockStateEnum.of("variant", BlockPrismarine.EnumPrismarineVariant.class);
   public static final int b;
   public static final int N;
   public static final int O;

   public BlockPrismarine() {
      super(Material.STONE);
      this.j(this.blockStateList.getBlockData().set(VARIANT, BlockPrismarine.EnumPrismarineVariant.ROUGH));
      this.a(CreativeModeTab.b);
   }

   public String getName() {
      return LocaleI18n.get(this.a() + "." + BlockPrismarine.EnumPrismarineVariant.ROUGH.c() + ".name");
   }

   public MaterialMapColor g(IBlockData var1) {
      return var1.get(VARIANT) == BlockPrismarine.EnumPrismarineVariant.ROUGH?MaterialMapColor.y:MaterialMapColor.G;
   }

   public int getDropData(IBlockData var1) {
      return ((BlockPrismarine.EnumPrismarineVariant)var1.get(VARIANT)).a();
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockPrismarine.EnumPrismarineVariant)var1.get(VARIANT)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{VARIANT});
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(VARIANT, BlockPrismarine.EnumPrismarineVariant.a(var1));
   }

   static {
      b = BlockPrismarine.EnumPrismarineVariant.ROUGH.a();
      N = BlockPrismarine.EnumPrismarineVariant.BRICKS.a();
      O = BlockPrismarine.EnumPrismarineVariant.DARK.a();
   }

   public static enum EnumPrismarineVariant implements INamable {
      ROUGH(0, "prismarine", "rough"),
      BRICKS(1, "prismarine_bricks", "bricks"),
      DARK(2, "dark_prismarine", "dark");

      private static final BlockPrismarine.EnumPrismarineVariant[] d;
      private final int e;
      private final String f;
      private final String g;

      private EnumPrismarineVariant(int var3, String var4, String var5) {
         this.e = var3;
         this.f = var4;
         this.g = var5;
      }

      public int a() {
         return this.e;
      }

      public String toString() {
         return this.f;
      }

      public static BlockPrismarine.EnumPrismarineVariant a(int var0) {
         if(var0 < 0 || var0 >= d.length) {
            var0 = 0;
         }

         return d[var0];
      }

      public String getName() {
         return this.f;
      }

      public String c() {
         return this.g;
      }

      static {
         d = new BlockPrismarine.EnumPrismarineVariant[values().length];
         BlockPrismarine.EnumPrismarineVariant[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockPrismarine.EnumPrismarineVariant var3 = var0[var2];
            d[var3.a()] = var3;
         }

      }
   }
}
