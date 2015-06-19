package net.minecraft.server;

import net.minecraft.server.BlockFalling;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.MaterialMapColor;

public class BlockSand extends BlockFalling {
   public static final BlockStateEnum<BlockSand.EnumSandVariant> VARIANT = BlockStateEnum.of("variant", BlockSand.EnumSandVariant.class);

   public BlockSand() {
      this.j(this.blockStateList.getBlockData().set(VARIANT, BlockSand.EnumSandVariant.SAND));
   }

   public int getDropData(IBlockData var1) {
      return ((BlockSand.EnumSandVariant)var1.get(VARIANT)).a();
   }

   public MaterialMapColor g(IBlockData var1) {
      return ((BlockSand.EnumSandVariant)var1.get(VARIANT)).c();
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(VARIANT, BlockSand.EnumSandVariant.a(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockSand.EnumSandVariant)var1.get(VARIANT)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{VARIANT});
   }

   public static enum EnumSandVariant implements INamable {
      SAND,
      RED_SAND;

      private static final BlockSand.EnumSandVariant[] c;
      private final int d;
      private final String e;
      private final MaterialMapColor f;
      private final String g;

      private EnumSandVariant(int var3, String var4, String var5, MaterialMapColor var6) {
         this.d = var3;
         this.e = var4;
         this.f = var6;
         this.g = var5;
      }

      public int a() {
         return this.d;
      }

      public String toString() {
         return this.e;
      }

      public MaterialMapColor c() {
         return this.f;
      }

      public static BlockSand.EnumSandVariant a(int var0) {
         if(var0 < 0 || var0 >= c.length) {
            var0 = 0;
         }

         return c[var0];
      }

      public String getName() {
         return this.e;
      }

      public String d() {
         return this.g;
      }

      static {
         SAND = new BlockSand.EnumSandVariant("SAND", 0, 0, "sand", "default", MaterialMapColor.d);
         RED_SAND = new BlockSand.EnumSandVariant("RED_SAND", 1, 1, "red_sand", "red", MaterialMapColor.q);
         c = new BlockSand.EnumSandVariant[values().length];
         BlockSand.EnumSandVariant[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockSand.EnumSandVariant var3 = var0[var2];
            c[var3.a()] = var3;
         }

      }
   }
}
