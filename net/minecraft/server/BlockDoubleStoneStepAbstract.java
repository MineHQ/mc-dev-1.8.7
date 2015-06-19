package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockSand;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockStepAbstract;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public abstract class BlockDoubleStoneStepAbstract extends BlockStepAbstract {
   public static final BlockStateBoolean SEAMLESS = BlockStateBoolean.of("seamless");
   public static final BlockStateEnum<BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant> VARIANT = BlockStateEnum.of("variant", BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant.class);

   public BlockDoubleStoneStepAbstract() {
      super(Material.STONE);
      IBlockData var1 = this.blockStateList.getBlockData();
      if(this.l()) {
         var1 = var1.set(SEAMLESS, Boolean.valueOf(false));
      } else {
         var1 = var1.set(HALF, BlockStepAbstract.EnumSlabHalf.BOTTOM);
      }

      this.j(var1.set(VARIANT, BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant.RED_SANDSTONE));
      this.a(CreativeModeTab.b);
   }

   public String getName() {
      return LocaleI18n.get(this.a() + ".red_sandstone.name");
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Item.getItemOf(Blocks.STONE_SLAB2);
   }

   public String b(int var1) {
      return super.a() + "." + BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant.a(var1).d();
   }

   public IBlockState<?> n() {
      return VARIANT;
   }

   public Object a(ItemStack var1) {
      return BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant.a(var1.getData() & 7);
   }

   public IBlockData fromLegacyData(int var1) {
      IBlockData var2 = this.getBlockData().set(VARIANT, BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant.a(var1 & 7));
      if(this.l()) {
         var2 = var2.set(SEAMLESS, Boolean.valueOf((var1 & 8) != 0));
      } else {
         var2 = var2.set(HALF, (var1 & 8) == 0?BlockStepAbstract.EnumSlabHalf.BOTTOM:BlockStepAbstract.EnumSlabHalf.TOP);
      }

      return var2;
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant)var1.get(VARIANT)).a();
      if(this.l()) {
         if(((Boolean)var1.get(SEAMLESS)).booleanValue()) {
            var3 |= 8;
         }
      } else if(var1.get(HALF) == BlockStepAbstract.EnumSlabHalf.TOP) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return this.l()?new BlockStateList(this, new IBlockState[]{SEAMLESS, VARIANT}):new BlockStateList(this, new IBlockState[]{HALF, VARIANT});
   }

   public MaterialMapColor g(IBlockData var1) {
      return ((BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant)var1.get(VARIANT)).c();
   }

   public int getDropData(IBlockData var1) {
      return ((BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant)var1.get(VARIANT)).a();
   }

   public static enum EnumStoneSlab2Variant implements INamable {
      RED_SANDSTONE;

      private static final BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant[] b;
      private final int c;
      private final String d;
      private final MaterialMapColor e;

      private EnumStoneSlab2Variant(int var3, String var4, MaterialMapColor var5) {
         this.c = var3;
         this.d = var4;
         this.e = var5;
      }

      public int a() {
         return this.c;
      }

      public MaterialMapColor c() {
         return this.e;
      }

      public String toString() {
         return this.d;
      }

      public static BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant a(int var0) {
         if(var0 < 0 || var0 >= b.length) {
            var0 = 0;
         }

         return b[var0];
      }

      public String getName() {
         return this.d;
      }

      public String d() {
         return this.d;
      }

      static {
         RED_SANDSTONE = new BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant("RED_SANDSTONE", 0, 0, "red_sandstone", BlockSand.EnumSandVariant.RED_SAND.c());
         b = new BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant[values().length];
         BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant var3 = var0[var2];
            b[var3.a()] = var3;
         }

      }
   }
}
