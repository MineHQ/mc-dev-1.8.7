package net.minecraft.server;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockFenceGate;
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
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockCobbleWall extends Block {
   public static final BlockStateBoolean UP = BlockStateBoolean.of("up");
   public static final BlockStateBoolean NORTH = BlockStateBoolean.of("north");
   public static final BlockStateBoolean EAST = BlockStateBoolean.of("east");
   public static final BlockStateBoolean SOUTH = BlockStateBoolean.of("south");
   public static final BlockStateBoolean WEST = BlockStateBoolean.of("west");
   public static final BlockStateEnum<BlockCobbleWall.EnumCobbleVariant> VARIANT = BlockStateEnum.of("variant", BlockCobbleWall.EnumCobbleVariant.class);

   public BlockCobbleWall(Block var1) {
      super(var1.material);
      this.j(this.blockStateList.getBlockData().set(UP, Boolean.valueOf(false)).set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)).set(VARIANT, BlockCobbleWall.EnumCobbleVariant.NORMAL));
      this.c(var1.strength);
      this.b(var1.durability / 3.0F);
      this.a(var1.stepSound);
      this.a(CreativeModeTab.b);
   }

   public String getName() {
      return LocaleI18n.get(this.a() + "." + BlockCobbleWall.EnumCobbleVariant.NORMAL.c() + ".name");
   }

   public boolean d() {
      return false;
   }

   public boolean b(IBlockAccess var1, BlockPosition var2) {
      return false;
   }

   public boolean c() {
      return false;
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      boolean var3 = this.e(var1, var2.north());
      boolean var4 = this.e(var1, var2.south());
      boolean var5 = this.e(var1, var2.west());
      boolean var6 = this.e(var1, var2.east());
      float var7 = 0.25F;
      float var8 = 0.75F;
      float var9 = 0.25F;
      float var10 = 0.75F;
      float var11 = 1.0F;
      if(var3) {
         var9 = 0.0F;
      }

      if(var4) {
         var10 = 1.0F;
      }

      if(var5) {
         var7 = 0.0F;
      }

      if(var6) {
         var8 = 1.0F;
      }

      if(var3 && var4 && !var5 && !var6) {
         var11 = 0.8125F;
         var7 = 0.3125F;
         var8 = 0.6875F;
      } else if(!var3 && !var4 && var5 && var6) {
         var11 = 0.8125F;
         var9 = 0.3125F;
         var10 = 0.6875F;
      }

      this.a(var7, 0.0F, var9, var8, var11, var10);
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      this.updateShape(var1, var2);
      this.maxY = 1.5D;
      return super.a(var1, var2, var3);
   }

   public boolean e(IBlockAccess var1, BlockPosition var2) {
      Block var3 = var1.getType(var2).getBlock();
      return var3 == Blocks.BARRIER?false:(var3 != this && !(var3 instanceof BlockFenceGate)?(var3.material.k() && var3.d()?var3.material != Material.PUMPKIN:false):true);
   }

   public int getDropData(IBlockData var1) {
      return ((BlockCobbleWall.EnumCobbleVariant)var1.get(VARIANT)).a();
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(VARIANT, BlockCobbleWall.EnumCobbleVariant.a(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockCobbleWall.EnumCobbleVariant)var1.get(VARIANT)).a();
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      return var1.set(UP, Boolean.valueOf(!var2.isEmpty(var3.up()))).set(NORTH, Boolean.valueOf(this.e(var2, var3.north()))).set(EAST, Boolean.valueOf(this.e(var2, var3.east()))).set(SOUTH, Boolean.valueOf(this.e(var2, var3.south()))).set(WEST, Boolean.valueOf(this.e(var2, var3.west())));
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{UP, NORTH, EAST, WEST, SOUTH, VARIANT});
   }

   public static enum EnumCobbleVariant implements INamable {
      NORMAL(0, "cobblestone", "normal"),
      MOSSY(1, "mossy_cobblestone", "mossy");

      private static final BlockCobbleWall.EnumCobbleVariant[] c;
      private final int d;
      private final String e;
      private String f;

      private EnumCobbleVariant(int var3, String var4, String var5) {
         this.d = var3;
         this.e = var4;
         this.f = var5;
      }

      public int a() {
         return this.d;
      }

      public String toString() {
         return this.e;
      }

      public static BlockCobbleWall.EnumCobbleVariant a(int var0) {
         if(var0 < 0 || var0 >= c.length) {
            var0 = 0;
         }

         return c[var0];
      }

      public String getName() {
         return this.e;
      }

      public String c() {
         return this.f;
      }

      static {
         c = new BlockCobbleWall.EnumCobbleVariant[values().length];
         BlockCobbleWall.EnumCobbleVariant[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockCobbleWall.EnumCobbleVariant var3 = var0[var2];
            c[var3.a()] = var3;
         }

      }
   }
}
