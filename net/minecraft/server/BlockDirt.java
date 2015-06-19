package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.World;

public class BlockDirt extends Block {
   public static final BlockStateEnum<BlockDirt.EnumDirtVariant> VARIANT = BlockStateEnum.of("variant", BlockDirt.EnumDirtVariant.class);
   public static final BlockStateBoolean SNOWY = BlockStateBoolean.of("snowy");

   protected BlockDirt() {
      super(Material.EARTH);
      this.j(this.blockStateList.getBlockData().set(VARIANT, BlockDirt.EnumDirtVariant.DIRT).set(SNOWY, Boolean.valueOf(false)));
      this.a(CreativeModeTab.b);
   }

   public MaterialMapColor g(IBlockData var1) {
      return ((BlockDirt.EnumDirtVariant)var1.get(VARIANT)).d();
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      if(var1.get(VARIANT) == BlockDirt.EnumDirtVariant.PODZOL) {
         Block var4 = var2.getType(var3.up()).getBlock();
         var1 = var1.set(SNOWY, Boolean.valueOf(var4 == Blocks.SNOW || var4 == Blocks.SNOW_LAYER));
      }

      return var1;
   }

   public int getDropData(World var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      return var3.getBlock() != this?0:((BlockDirt.EnumDirtVariant)var3.get(VARIANT)).a();
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(VARIANT, BlockDirt.EnumDirtVariant.a(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockDirt.EnumDirtVariant)var1.get(VARIANT)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{VARIANT, SNOWY});
   }

   public int getDropData(IBlockData var1) {
      BlockDirt.EnumDirtVariant var2 = (BlockDirt.EnumDirtVariant)var1.get(VARIANT);
      if(var2 == BlockDirt.EnumDirtVariant.PODZOL) {
         var2 = BlockDirt.EnumDirtVariant.DIRT;
      }

      return var2.a();
   }

   public static enum EnumDirtVariant implements INamable {
      DIRT,
      COARSE_DIRT,
      PODZOL;

      private static final BlockDirt.EnumDirtVariant[] d;
      private final int e;
      private final String f;
      private final String g;
      private final MaterialMapColor h;

      private EnumDirtVariant(int var3, String var4, MaterialMapColor var5) {
         this(var3, var4, var4, var5);
      }

      private EnumDirtVariant(int var3, String var4, String var5, MaterialMapColor var6) {
         this.e = var3;
         this.f = var4;
         this.g = var5;
         this.h = var6;
      }

      public int a() {
         return this.e;
      }

      public String c() {
         return this.g;
      }

      public MaterialMapColor d() {
         return this.h;
      }

      public String toString() {
         return this.f;
      }

      public static BlockDirt.EnumDirtVariant a(int var0) {
         if(var0 < 0 || var0 >= d.length) {
            var0 = 0;
         }

         return d[var0];
      }

      public String getName() {
         return this.f;
      }

      static {
         DIRT = new BlockDirt.EnumDirtVariant("DIRT", 0, 0, "dirt", "default", MaterialMapColor.l);
         COARSE_DIRT = new BlockDirt.EnumDirtVariant("COARSE_DIRT", 1, 1, "coarse_dirt", "coarse", MaterialMapColor.l);
         PODZOL = new BlockDirt.EnumDirtVariant("PODZOL", 2, 2, "podzol", MaterialMapColor.J);
         d = new BlockDirt.EnumDirtVariant[values().length];
         BlockDirt.EnumDirtVariant[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockDirt.EnumDirtVariant var3 = var0[var2];
            d[var3.a()] = var3;
         }

      }
   }
}
