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

public class BlockWood extends Block {
   public static final BlockStateEnum<BlockWood.EnumLogVariant> VARIANT = BlockStateEnum.of("variant", BlockWood.EnumLogVariant.class);

   public BlockWood() {
      super(Material.WOOD);
      this.j(this.blockStateList.getBlockData().set(VARIANT, BlockWood.EnumLogVariant.OAK));
      this.a(CreativeModeTab.b);
   }

   public int getDropData(IBlockData var1) {
      return ((BlockWood.EnumLogVariant)var1.get(VARIANT)).a();
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(VARIANT, BlockWood.EnumLogVariant.a(var1));
   }

   public MaterialMapColor g(IBlockData var1) {
      return ((BlockWood.EnumLogVariant)var1.get(VARIANT)).c();
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockWood.EnumLogVariant)var1.get(VARIANT)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{VARIANT});
   }

   public static enum EnumLogVariant implements INamable {
      OAK,
      SPRUCE,
      BIRCH,
      JUNGLE,
      ACACIA,
      DARK_OAK;

      private static final BlockWood.EnumLogVariant[] g;
      private final int h;
      private final String i;
      private final String j;
      private final MaterialMapColor k;

      private EnumLogVariant(int var3, String var4, MaterialMapColor var5) {
         this(var3, var4, var4, var5);
      }

      private EnumLogVariant(int var3, String var4, String var5, MaterialMapColor var6) {
         this.h = var3;
         this.i = var4;
         this.j = var5;
         this.k = var6;
      }

      public int a() {
         return this.h;
      }

      public MaterialMapColor c() {
         return this.k;
      }

      public String toString() {
         return this.i;
      }

      public static BlockWood.EnumLogVariant a(int var0) {
         if(var0 < 0 || var0 >= g.length) {
            var0 = 0;
         }

         return g[var0];
      }

      public String getName() {
         return this.i;
      }

      public String d() {
         return this.j;
      }

      static {
         OAK = new BlockWood.EnumLogVariant("OAK", 0, 0, "oak", MaterialMapColor.o);
         SPRUCE = new BlockWood.EnumLogVariant("SPRUCE", 1, 1, "spruce", MaterialMapColor.J);
         BIRCH = new BlockWood.EnumLogVariant("BIRCH", 2, 2, "birch", MaterialMapColor.d);
         JUNGLE = new BlockWood.EnumLogVariant("JUNGLE", 3, 3, "jungle", MaterialMapColor.l);
         ACACIA = new BlockWood.EnumLogVariant("ACACIA", 4, 4, "acacia", MaterialMapColor.q);
         DARK_OAK = new BlockWood.EnumLogVariant("DARK_OAK", 5, 5, "dark_oak", "big_oak", MaterialMapColor.B);
         g = new BlockWood.EnumLogVariant[values().length];
         BlockWood.EnumLogVariant[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockWood.EnumLogVariant var3 = var0[var2];
            g[var3.a()] = var3;
         }

      }
   }
}
