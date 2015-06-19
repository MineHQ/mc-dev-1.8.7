package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockSand;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Material;

public class BlockRedSandstone extends Block {
   public static final BlockStateEnum<BlockRedSandstone.EnumRedSandstoneVariant> TYPE = BlockStateEnum.of("type", BlockRedSandstone.EnumRedSandstoneVariant.class);

   public BlockRedSandstone() {
      super(Material.STONE, BlockSand.EnumSandVariant.RED_SAND.c());
      this.j(this.blockStateList.getBlockData().set(TYPE, BlockRedSandstone.EnumRedSandstoneVariant.DEFAULT));
      this.a(CreativeModeTab.b);
   }

   public int getDropData(IBlockData var1) {
      return ((BlockRedSandstone.EnumRedSandstoneVariant)var1.get(TYPE)).a();
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(TYPE, BlockRedSandstone.EnumRedSandstoneVariant.a(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockRedSandstone.EnumRedSandstoneVariant)var1.get(TYPE)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{TYPE});
   }

   public static enum EnumRedSandstoneVariant implements INamable {
      DEFAULT(0, "red_sandstone", "default"),
      CHISELED(1, "chiseled_red_sandstone", "chiseled"),
      SMOOTH(2, "smooth_red_sandstone", "smooth");

      private static final BlockRedSandstone.EnumRedSandstoneVariant[] d;
      private final int e;
      private final String f;
      private final String g;

      private EnumRedSandstoneVariant(int var3, String var4, String var5) {
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

      public static BlockRedSandstone.EnumRedSandstoneVariant a(int var0) {
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
         d = new BlockRedSandstone.EnumRedSandstoneVariant[values().length];
         BlockRedSandstone.EnumRedSandstoneVariant[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockRedSandstone.EnumRedSandstoneVariant var3 = var0[var2];
            d[var3.a()] = var3;
         }

      }
   }
}
