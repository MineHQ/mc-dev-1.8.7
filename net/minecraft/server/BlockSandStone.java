package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public class BlockSandStone extends Block {
   public static final BlockStateEnum<BlockSandStone.EnumSandstoneVariant> TYPE = BlockStateEnum.of("type", BlockSandStone.EnumSandstoneVariant.class);

   public BlockSandStone() {
      super(Material.STONE);
      this.j(this.blockStateList.getBlockData().set(TYPE, BlockSandStone.EnumSandstoneVariant.DEFAULT));
      this.a(CreativeModeTab.b);
   }

   public int getDropData(IBlockData var1) {
      return ((BlockSandStone.EnumSandstoneVariant)var1.get(TYPE)).a();
   }

   public MaterialMapColor g(IBlockData var1) {
      return MaterialMapColor.d;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(TYPE, BlockSandStone.EnumSandstoneVariant.a(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockSandStone.EnumSandstoneVariant)var1.get(TYPE)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{TYPE});
   }

   public static enum EnumSandstoneVariant implements INamable {
      DEFAULT(0, "sandstone", "default"),
      CHISELED(1, "chiseled_sandstone", "chiseled"),
      SMOOTH(2, "smooth_sandstone", "smooth");

      private static final BlockSandStone.EnumSandstoneVariant[] d;
      private final int e;
      private final String f;
      private final String g;

      private EnumSandstoneVariant(int var3, String var4, String var5) {
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

      public static BlockSandStone.EnumSandstoneVariant a(int var0) {
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
         d = new BlockSandStone.EnumSandstoneVariant[values().length];
         BlockSandStone.EnumSandstoneVariant[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockSandStone.EnumSandstoneVariant var3 = var0[var2];
            d[var3.a()] = var3;
         }

      }
   }
}
