package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDirectional;
import net.minecraft.server.BlockLongGrass;
import net.minecraft.server.BlockPlant;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
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

public class BlockTallPlant extends BlockPlant implements IBlockFragilePlantElement {
   public static final BlockStateEnum<BlockTallPlant.EnumTallFlowerVariants> VARIANT = BlockStateEnum.of("variant", BlockTallPlant.EnumTallFlowerVariants.class);
   public static final BlockStateEnum<BlockTallPlant.EnumTallPlantHalf> HALF = BlockStateEnum.of("half", BlockTallPlant.EnumTallPlantHalf.class);
   public static final BlockStateEnum<EnumDirection> N;

   public BlockTallPlant() {
      super(Material.REPLACEABLE_PLANT);
      this.j(this.blockStateList.getBlockData().set(VARIANT, BlockTallPlant.EnumTallFlowerVariants.SUNFLOWER).set(HALF, BlockTallPlant.EnumTallPlantHalf.LOWER).set(N, EnumDirection.NORTH));
      this.c(0.0F);
      this.a(h);
      this.c("doublePlant");
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public BlockTallPlant.EnumTallFlowerVariants e(IBlockAccess var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      if(var3.getBlock() == this) {
         var3 = this.updateState(var3, var1, var2);
         return (BlockTallPlant.EnumTallFlowerVariants)var3.get(VARIANT);
      } else {
         return BlockTallPlant.EnumTallFlowerVariants.FERN;
      }
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return super.canPlace(var1, var2) && var1.isEmpty(var2.up());
   }

   public boolean a(World var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      if(var3.getBlock() != this) {
         return true;
      } else {
         BlockTallPlant.EnumTallFlowerVariants var4 = (BlockTallPlant.EnumTallFlowerVariants)this.updateState(var3, var1, var2).get(VARIANT);
         return var4 == BlockTallPlant.EnumTallFlowerVariants.FERN || var4 == BlockTallPlant.EnumTallFlowerVariants.GRASS;
      }
   }

   protected void e(World var1, BlockPosition var2, IBlockData var3) {
      if(!this.f(var1, var2, var3)) {
         boolean var4 = var3.get(HALF) == BlockTallPlant.EnumTallPlantHalf.UPPER;
         BlockPosition var5 = var4?var2:var2.up();
         BlockPosition var6 = var4?var2.down():var2;
         Object var7 = var4?this:var1.getType(var5).getBlock();
         Object var8 = var4?var1.getType(var6).getBlock():this;
         if(var7 == this) {
            var1.setTypeAndData(var5, Blocks.AIR.getBlockData(), 2);
         }

         if(var8 == this) {
            var1.setTypeAndData(var6, Blocks.AIR.getBlockData(), 3);
            if(!var4) {
               this.b(var1, var6, var3, 0);
            }
         }

      }
   }

   public boolean f(World var1, BlockPosition var2, IBlockData var3) {
      if(var3.get(HALF) == BlockTallPlant.EnumTallPlantHalf.UPPER) {
         return var1.getType(var2.down()).getBlock() == this;
      } else {
         IBlockData var4 = var1.getType(var2.up());
         return var4.getBlock() == this && super.f(var1, var2, var4);
      }
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      if(var1.get(HALF) == BlockTallPlant.EnumTallPlantHalf.UPPER) {
         return null;
      } else {
         BlockTallPlant.EnumTallFlowerVariants var4 = (BlockTallPlant.EnumTallFlowerVariants)var1.get(VARIANT);
         return var4 == BlockTallPlant.EnumTallFlowerVariants.FERN?null:(var4 == BlockTallPlant.EnumTallFlowerVariants.GRASS?(var2.nextInt(8) == 0?Items.WHEAT_SEEDS:null):Item.getItemOf(this));
      }
   }

   public int getDropData(IBlockData var1) {
      return var1.get(HALF) != BlockTallPlant.EnumTallPlantHalf.UPPER && var1.get(VARIANT) != BlockTallPlant.EnumTallFlowerVariants.GRASS?((BlockTallPlant.EnumTallFlowerVariants)var1.get(VARIANT)).a():0;
   }

   public void a(World var1, BlockPosition var2, BlockTallPlant.EnumTallFlowerVariants var3, int var4) {
      var1.setTypeAndData(var2, this.getBlockData().set(HALF, BlockTallPlant.EnumTallPlantHalf.LOWER).set(VARIANT, var3), var4);
      var1.setTypeAndData(var2.up(), this.getBlockData().set(HALF, BlockTallPlant.EnumTallPlantHalf.UPPER), var4);
   }

   public void postPlace(World var1, BlockPosition var2, IBlockData var3, EntityLiving var4, ItemStack var5) {
      var1.setTypeAndData(var2.up(), this.getBlockData().set(HALF, BlockTallPlant.EnumTallPlantHalf.UPPER), 2);
   }

   public void a(World var1, EntityHuman var2, BlockPosition var3, IBlockData var4, TileEntity var5) {
      if(var1.isClientSide || var2.bZ() == null || var2.bZ().getItem() != Items.SHEARS || var4.get(HALF) != BlockTallPlant.EnumTallPlantHalf.LOWER || !this.b(var1, var3, var4, var2)) {
         super.a(var1, var2, var3, var4, var5);
      }
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4) {
      if(var3.get(HALF) == BlockTallPlant.EnumTallPlantHalf.UPPER) {
         if(var1.getType(var2.down()).getBlock() == this) {
            if(!var4.abilities.canInstantlyBuild) {
               IBlockData var5 = var1.getType(var2.down());
               BlockTallPlant.EnumTallFlowerVariants var6 = (BlockTallPlant.EnumTallFlowerVariants)var5.get(VARIANT);
               if(var6 != BlockTallPlant.EnumTallFlowerVariants.FERN && var6 != BlockTallPlant.EnumTallFlowerVariants.GRASS) {
                  var1.setAir(var2.down(), true);
               } else if(!var1.isClientSide) {
                  if(var4.bZ() != null && var4.bZ().getItem() == Items.SHEARS) {
                     this.b(var1, var2, var5, var4);
                     var1.setAir(var2.down());
                  } else {
                     var1.setAir(var2.down(), true);
                  }
               } else {
                  var1.setAir(var2.down());
               }
            } else {
               var1.setAir(var2.down());
            }
         }
      } else if(var4.abilities.canInstantlyBuild && var1.getType(var2.up()).getBlock() == this) {
         var1.setTypeAndData(var2.up(), Blocks.AIR.getBlockData(), 2);
      }

      super.a(var1, var2, var3, var4);
   }

   private boolean b(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4) {
      BlockTallPlant.EnumTallFlowerVariants var5 = (BlockTallPlant.EnumTallFlowerVariants)var3.get(VARIANT);
      if(var5 != BlockTallPlant.EnumTallFlowerVariants.FERN && var5 != BlockTallPlant.EnumTallFlowerVariants.GRASS) {
         return false;
      } else {
         var4.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
         int var6 = (var5 == BlockTallPlant.EnumTallFlowerVariants.GRASS?BlockLongGrass.EnumTallGrassType.GRASS:BlockLongGrass.EnumTallGrassType.FERN).a();
         a(var1, var2, new ItemStack(Blocks.TALLGRASS, 2, var6));
         return true;
      }
   }

   public int getDropData(World var1, BlockPosition var2) {
      return this.e(var1, var2).a();
   }

   public boolean a(World var1, BlockPosition var2, IBlockData var3, boolean var4) {
      BlockTallPlant.EnumTallFlowerVariants var5 = this.e(var1, var2);
      return var5 != BlockTallPlant.EnumTallFlowerVariants.GRASS && var5 != BlockTallPlant.EnumTallFlowerVariants.FERN;
   }

   public boolean a(World var1, Random var2, BlockPosition var3, IBlockData var4) {
      return true;
   }

   public void b(World var1, Random var2, BlockPosition var3, IBlockData var4) {
      a(var1, var3, new ItemStack(this, 1, this.e(var1, var3).a()));
   }

   public IBlockData fromLegacyData(int var1) {
      return (var1 & 8) > 0?this.getBlockData().set(HALF, BlockTallPlant.EnumTallPlantHalf.UPPER):this.getBlockData().set(HALF, BlockTallPlant.EnumTallPlantHalf.LOWER).set(VARIANT, BlockTallPlant.EnumTallFlowerVariants.a(var1 & 7));
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      if(var1.get(HALF) == BlockTallPlant.EnumTallPlantHalf.UPPER) {
         IBlockData var4 = var2.getType(var3.down());
         if(var4.getBlock() == this) {
            var1 = var1.set(VARIANT, var4.get(VARIANT));
         }
      }

      return var1;
   }

   public int toLegacyData(IBlockData var1) {
      return var1.get(HALF) == BlockTallPlant.EnumTallPlantHalf.UPPER?8 | ((EnumDirection)var1.get(N)).b():((BlockTallPlant.EnumTallFlowerVariants)var1.get(VARIANT)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{HALF, VARIANT, N});
   }

   static {
      N = BlockDirectional.FACING;
   }

   public static enum EnumTallPlantHalf implements INamable {
      UPPER,
      LOWER;

      private EnumTallPlantHalf() {
      }

      public String toString() {
         return this.getName();
      }

      public String getName() {
         return this == UPPER?"upper":"lower";
      }
   }

   public static enum EnumTallFlowerVariants implements INamable {
      SUNFLOWER(0, "sunflower"),
      SYRINGA(1, "syringa"),
      GRASS(2, "double_grass", "grass"),
      FERN(3, "double_fern", "fern"),
      ROSE(4, "double_rose", "rose"),
      PAEONIA(5, "paeonia");

      private static final BlockTallPlant.EnumTallFlowerVariants[] g;
      private final int h;
      private final String i;
      private final String j;

      private EnumTallFlowerVariants(int var3, String var4) {
         this(var3, var4, var4);
      }

      private EnumTallFlowerVariants(int var3, String var4, String var5) {
         this.h = var3;
         this.i = var4;
         this.j = var5;
      }

      public int a() {
         return this.h;
      }

      public String toString() {
         return this.i;
      }

      public static BlockTallPlant.EnumTallFlowerVariants a(int var0) {
         if(var0 < 0 || var0 >= g.length) {
            var0 = 0;
         }

         return g[var0];
      }

      public String getName() {
         return this.i;
      }

      public String c() {
         return this.j;
      }

      static {
         g = new BlockTallPlant.EnumTallFlowerVariants[values().length];
         BlockTallPlant.EnumTallFlowerVariants[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockTallPlant.EnumTallFlowerVariants var3 = var0[var2];
            g[var3.a()] = var3;
         }

      }
   }
}
