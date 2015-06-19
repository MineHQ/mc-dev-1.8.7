package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPlant;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockTallPlant;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockFragilePlantElement;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockLongGrass extends BlockPlant implements IBlockFragilePlantElement {
   public static final BlockStateEnum<BlockLongGrass.EnumTallGrassType> TYPE = BlockStateEnum.of("type", BlockLongGrass.EnumTallGrassType.class);

   protected BlockLongGrass() {
      super(Material.REPLACEABLE_PLANT);
      this.j(this.blockStateList.getBlockData().set(TYPE, BlockLongGrass.EnumTallGrassType.DEAD_BUSH));
      float var1 = 0.4F;
      this.a(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.8F, 0.5F + var1);
   }

   public boolean f(World var1, BlockPosition var2, IBlockData var3) {
      return this.c(var1.getType(var2.down()).getBlock());
   }

   public boolean a(World var1, BlockPosition var2) {
      return true;
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return var2.nextInt(8) == 0?Items.WHEAT_SEEDS:null;
   }

   public int getDropCount(int var1, Random var2) {
      return 1 + var2.nextInt(var1 * 2 + 1);
   }

   public void a(World var1, EntityHuman var2, BlockPosition var3, IBlockData var4, TileEntity var5) {
      if(!var1.isClientSide && var2.bZ() != null && var2.bZ().getItem() == Items.SHEARS) {
         var2.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
         a(var1, var3, new ItemStack(Blocks.TALLGRASS, 1, ((BlockLongGrass.EnumTallGrassType)var4.get(TYPE)).a()));
      } else {
         super.a(var1, var2, var3, var4, var5);
      }

   }

   public int getDropData(World var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      return var3.getBlock().toLegacyData(var3);
   }

   public boolean a(World var1, BlockPosition var2, IBlockData var3, boolean var4) {
      return var3.get(TYPE) != BlockLongGrass.EnumTallGrassType.DEAD_BUSH;
   }

   public boolean a(World var1, Random var2, BlockPosition var3, IBlockData var4) {
      return true;
   }

   public void b(World var1, Random var2, BlockPosition var3, IBlockData var4) {
      BlockTallPlant.EnumTallFlowerVariants var5 = BlockTallPlant.EnumTallFlowerVariants.GRASS;
      if(var4.get(TYPE) == BlockLongGrass.EnumTallGrassType.FERN) {
         var5 = BlockTallPlant.EnumTallFlowerVariants.FERN;
      }

      if(Blocks.DOUBLE_PLANT.canPlace(var1, var3)) {
         Blocks.DOUBLE_PLANT.a(var1, var3, var5, 2);
      }

   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(TYPE, BlockLongGrass.EnumTallGrassType.a(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockLongGrass.EnumTallGrassType)var1.get(TYPE)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{TYPE});
   }

   public static enum EnumTallGrassType implements INamable {
      DEAD_BUSH(0, "dead_bush"),
      GRASS(1, "tall_grass"),
      FERN(2, "fern");

      private static final BlockLongGrass.EnumTallGrassType[] d;
      private final int e;
      private final String f;

      private EnumTallGrassType(int var3, String var4) {
         this.e = var3;
         this.f = var4;
      }

      public int a() {
         return this.e;
      }

      public String toString() {
         return this.f;
      }

      public static BlockLongGrass.EnumTallGrassType a(int var0) {
         if(var0 < 0 || var0 >= d.length) {
            var0 = 0;
         }

         return d[var0];
      }

      public String getName() {
         return this.f;
      }

      static {
         d = new BlockLongGrass.EnumTallGrassType[values().length];
         BlockLongGrass.EnumTallGrassType[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockLongGrass.EnumTallGrassType var3 = var0[var2];
            d[var3.a()] = var3;
         }

      }
   }
}
