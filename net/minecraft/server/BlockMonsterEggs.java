package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockSmoothBrick;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockStone;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntitySilverfish;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockMonsterEggs extends Block {
   public static final BlockStateEnum<BlockMonsterEggs.EnumMonsterEggVarient> VARIANT = BlockStateEnum.of("variant", BlockMonsterEggs.EnumMonsterEggVarient.class);

   public BlockMonsterEggs() {
      super(Material.CLAY);
      this.j(this.blockStateList.getBlockData().set(VARIANT, BlockMonsterEggs.EnumMonsterEggVarient.STONE));
      this.c(0.0F);
      this.a(CreativeModeTab.c);
   }

   public int a(Random var1) {
      return 0;
   }

   public static boolean d(IBlockData var0) {
      Block var1 = var0.getBlock();
      return var0 == Blocks.STONE.getBlockData().set(BlockStone.VARIANT, BlockStone.EnumStoneVariant.STONE) || var1 == Blocks.COBBLESTONE || var1 == Blocks.STONEBRICK;
   }

   protected ItemStack i(IBlockData var1) {
      switch(BlockMonsterEggs.SyntheticClass_1.a[((BlockMonsterEggs.EnumMonsterEggVarient)var1.get(VARIANT)).ordinal()]) {
      case 1:
         return new ItemStack(Blocks.COBBLESTONE);
      case 2:
         return new ItemStack(Blocks.STONEBRICK);
      case 3:
         return new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.EnumStonebrickType.MOSSY.a());
      case 4:
         return new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.EnumStonebrickType.CRACKED.a());
      case 5:
         return new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.EnumStonebrickType.CHISELED.a());
      default:
         return new ItemStack(Blocks.STONE);
      }
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
      if(!var1.isClientSide && var1.getGameRules().getBoolean("doTileDrops")) {
         EntitySilverfish var6 = new EntitySilverfish(var1);
         var6.setPositionRotation((double)var2.getX() + 0.5D, (double)var2.getY(), (double)var2.getZ() + 0.5D, 0.0F, 0.0F);
         var1.addEntity(var6);
         var6.y();
      }

   }

   public int getDropData(World var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      return var3.getBlock().toLegacyData(var3);
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(VARIANT, BlockMonsterEggs.EnumMonsterEggVarient.a(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockMonsterEggs.EnumMonsterEggVarient)var1.get(VARIANT)).a();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{VARIANT});
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[BlockMonsterEggs.EnumMonsterEggVarient.values().length];

      static {
         try {
            a[BlockMonsterEggs.EnumMonsterEggVarient.COBBLESTONE.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            a[BlockMonsterEggs.EnumMonsterEggVarient.STONEBRICK.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[BlockMonsterEggs.EnumMonsterEggVarient.MOSSY_STONEBRICK.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[BlockMonsterEggs.EnumMonsterEggVarient.CRACKED_STONEBRICK.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[BlockMonsterEggs.EnumMonsterEggVarient.CHISELED_STONEBRICK.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumMonsterEggVarient implements INamable {
      STONE(0, "stone") {
         public IBlockData d() {
            return Blocks.STONE.getBlockData().set(BlockStone.VARIANT, BlockStone.EnumStoneVariant.STONE);
         }
      },
      COBBLESTONE(1, "cobblestone", "cobble") {
         public IBlockData d() {
            return Blocks.COBBLESTONE.getBlockData();
         }
      },
      STONEBRICK(2, "stone_brick", "brick") {
         public IBlockData d() {
            return Blocks.STONEBRICK.getBlockData().set(BlockSmoothBrick.VARIANT, BlockSmoothBrick.EnumStonebrickType.DEFAULT);
         }
      },
      MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick") {
         public IBlockData d() {
            return Blocks.STONEBRICK.getBlockData().set(BlockSmoothBrick.VARIANT, BlockSmoothBrick.EnumStonebrickType.MOSSY);
         }
      },
      CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick") {
         public IBlockData d() {
            return Blocks.STONEBRICK.getBlockData().set(BlockSmoothBrick.VARIANT, BlockSmoothBrick.EnumStonebrickType.CRACKED);
         }
      },
      CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick") {
         public IBlockData d() {
            return Blocks.STONEBRICK.getBlockData().set(BlockSmoothBrick.VARIANT, BlockSmoothBrick.EnumStonebrickType.CHISELED);
         }
      };

      private static final BlockMonsterEggs.EnumMonsterEggVarient[] g;
      private final int h;
      private final String i;
      private final String j;

      private EnumMonsterEggVarient(int var3, String var4) {
         this(var3, var4, var4);
      }

      private EnumMonsterEggVarient(int var3, String var4, String var5) {
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

      public static BlockMonsterEggs.EnumMonsterEggVarient a(int var0) {
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

      public abstract IBlockData d();

      public static BlockMonsterEggs.EnumMonsterEggVarient a(IBlockData var0) {
         BlockMonsterEggs.EnumMonsterEggVarient[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            BlockMonsterEggs.EnumMonsterEggVarient var4 = var1[var3];
            if(var0 == var4.d()) {
               return var4;
            }
         }

         return STONE;
      }

      // $FF: synthetic method
      EnumMonsterEggVarient(int var3, String var4, BlockMonsterEggs.SyntheticClass_1 var5) {
         this(var3, var4);
      }

      // $FF: synthetic method
      EnumMonsterEggVarient(int var3, String var4, String var5, BlockMonsterEggs.SyntheticClass_1 var6) {
         this(var3, var4, var5);
      }

      static {
         g = new BlockMonsterEggs.EnumMonsterEggVarient[values().length];
         BlockMonsterEggs.EnumMonsterEggVarient[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockMonsterEggs.EnumMonsterEggVarient var3 = var0[var2];
            g[var3.a()] = var3;
         }

      }
   }
}
