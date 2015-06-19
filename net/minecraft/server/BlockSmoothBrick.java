package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Material;

public class BlockSmoothBrick extends Block {
   public static final BlockStateEnum<BlockSmoothBrick.EnumStonebrickType> VARIANT = BlockStateEnum.of("variant", BlockSmoothBrick.EnumStonebrickType.class);
   public static final int b;
   public static final int N;
   public static final int O;
   public static final int P;

   public BlockSmoothBrick() {
      super(Material.STONE);
      this.j(this.blockStateList.getBlockData().set(VARIANT, BlockSmoothBrick.EnumStonebrickType.DEFAULT));
      this.a(CreativeModeTab.b);
   }

   public int getDropData(IBlockData var1) {
      return ((BlockSmoothBrick.EnumStonebrickType)var1.get(VARIANT)).a();
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(VARIANT, BlockSmoothBrick.EnumStonebrickType.a(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockSmoothBrick.EnumStonebrickType)var1.get(VARIANT)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{VARIANT});
   }

   static {
      b = BlockSmoothBrick.EnumStonebrickType.DEFAULT.a();
      N = BlockSmoothBrick.EnumStonebrickType.MOSSY.a();
      O = BlockSmoothBrick.EnumStonebrickType.CRACKED.a();
      P = BlockSmoothBrick.EnumStonebrickType.CHISELED.a();
   }

   public static enum EnumStonebrickType implements INamable {
      DEFAULT(0, "stonebrick", "default"),
      MOSSY(1, "mossy_stonebrick", "mossy"),
      CRACKED(2, "cracked_stonebrick", "cracked"),
      CHISELED(3, "chiseled_stonebrick", "chiseled");

      private static final BlockSmoothBrick.EnumStonebrickType[] e;
      private final int f;
      private final String g;
      private final String h;

      private EnumStonebrickType(int var3, String var4, String var5) {
         this.f = var3;
         this.g = var4;
         this.h = var5;
      }

      public int a() {
         return this.f;
      }

      public String toString() {
         return this.g;
      }

      public static BlockSmoothBrick.EnumStonebrickType a(int var0) {
         if(var0 < 0 || var0 >= e.length) {
            var0 = 0;
         }

         return e[var0];
      }

      public String getName() {
         return this.g;
      }

      public String c() {
         return this.h;
      }

      static {
         e = new BlockSmoothBrick.EnumStonebrickType[values().length];
         BlockSmoothBrick.EnumStonebrickType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockSmoothBrick.EnumStonebrickType var3 = var0[var2];
            e[var3.a()] = var3;
         }

      }
   }
}
