package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.server.BlockPlant;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;

public abstract class BlockFlowers extends BlockPlant {
   protected BlockStateEnum<BlockFlowers.EnumFlowerVarient> TYPE;

   protected BlockFlowers() {
      this.j(this.blockStateList.getBlockData().set(this.n(), this.l() == BlockFlowers.EnumFlowerType.RED?BlockFlowers.EnumFlowerVarient.POPPY:BlockFlowers.EnumFlowerVarient.DANDELION));
   }

   public int getDropData(IBlockData var1) {
      return ((BlockFlowers.EnumFlowerVarient)var1.get(this.n())).b();
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(this.n(), BlockFlowers.EnumFlowerVarient.a(this.l(), var1));
   }

   public abstract BlockFlowers.EnumFlowerType l();

   public IBlockState<BlockFlowers.EnumFlowerVarient> n() {
      if(this.TYPE == null) {
         this.TYPE = BlockStateEnum.a("type", BlockFlowers.EnumFlowerVarient.class, new Predicate() {
            public boolean a(BlockFlowers.EnumFlowerVarient var1) {
               return var1.a() == BlockFlowers.this.l();
            }

            // $FF: synthetic method
            public boolean apply(Object var1) {
               return this.a((BlockFlowers.EnumFlowerVarient)var1);
            }
         });
      }

      return this.TYPE;
   }

   public int toLegacyData(IBlockData var1) {
      return ((BlockFlowers.EnumFlowerVarient)var1.get(this.n())).b();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{this.n()});
   }

   public static enum EnumFlowerVarient implements INamable {
      DANDELION,
      POPPY,
      BLUE_ORCHID,
      ALLIUM,
      HOUSTONIA,
      RED_TULIP,
      ORANGE_TULIP,
      WHITE_TULIP,
      PINK_TULIP,
      OXEYE_DAISY;

      private static final BlockFlowers.EnumFlowerVarient[][] k;
      private final BlockFlowers.EnumFlowerType l;
      private final int m;
      private final String n;
      private final String o;

      private EnumFlowerVarient(BlockFlowers.EnumFlowerType var3, int var4, String var5) {
         this(var3, var4, var5, var5);
      }

      private EnumFlowerVarient(BlockFlowers.EnumFlowerType var3, int var4, String var5, String var6) {
         this.l = var3;
         this.m = var4;
         this.n = var5;
         this.o = var6;
      }

      public BlockFlowers.EnumFlowerType a() {
         return this.l;
      }

      public int b() {
         return this.m;
      }

      public static BlockFlowers.EnumFlowerVarient a(BlockFlowers.EnumFlowerType var0, int var1) {
         BlockFlowers.EnumFlowerVarient[] var2 = k[var0.ordinal()];
         if(var1 < 0 || var1 >= var2.length) {
            var1 = 0;
         }

         return var2[var1];
      }

      public String toString() {
         return this.n;
      }

      public String getName() {
         return this.n;
      }

      public String d() {
         return this.o;
      }

      static {
         DANDELION = new BlockFlowers.EnumFlowerVarient("DANDELION", 0, BlockFlowers.EnumFlowerType.YELLOW, 0, "dandelion");
         POPPY = new BlockFlowers.EnumFlowerVarient("POPPY", 1, BlockFlowers.EnumFlowerType.RED, 0, "poppy");
         BLUE_ORCHID = new BlockFlowers.EnumFlowerVarient("BLUE_ORCHID", 2, BlockFlowers.EnumFlowerType.RED, 1, "blue_orchid", "blueOrchid");
         ALLIUM = new BlockFlowers.EnumFlowerVarient("ALLIUM", 3, BlockFlowers.EnumFlowerType.RED, 2, "allium");
         HOUSTONIA = new BlockFlowers.EnumFlowerVarient("HOUSTONIA", 4, BlockFlowers.EnumFlowerType.RED, 3, "houstonia");
         RED_TULIP = new BlockFlowers.EnumFlowerVarient("RED_TULIP", 5, BlockFlowers.EnumFlowerType.RED, 4, "red_tulip", "tulipRed");
         ORANGE_TULIP = new BlockFlowers.EnumFlowerVarient("ORANGE_TULIP", 6, BlockFlowers.EnumFlowerType.RED, 5, "orange_tulip", "tulipOrange");
         WHITE_TULIP = new BlockFlowers.EnumFlowerVarient("WHITE_TULIP", 7, BlockFlowers.EnumFlowerType.RED, 6, "white_tulip", "tulipWhite");
         PINK_TULIP = new BlockFlowers.EnumFlowerVarient("PINK_TULIP", 8, BlockFlowers.EnumFlowerType.RED, 7, "pink_tulip", "tulipPink");
         OXEYE_DAISY = new BlockFlowers.EnumFlowerVarient("OXEYE_DAISY", 9, BlockFlowers.EnumFlowerType.RED, 8, "oxeye_daisy", "oxeyeDaisy");
         k = new BlockFlowers.EnumFlowerVarient[BlockFlowers.EnumFlowerType.values().length][];
         BlockFlowers.EnumFlowerType[] var0 = BlockFlowers.EnumFlowerType.values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            final BlockFlowers.EnumFlowerType var3 = var0[var2];
            Collection var4 = Collections2.filter(Lists.newArrayList((Object[])values()), new Predicate() {
               public boolean a(BlockFlowers.EnumFlowerVarient var1) {
                  return var1.a() == var3;
               }

               // $FF: synthetic method
               public boolean apply(Object var1) {
                  return this.a((BlockFlowers.EnumFlowerVarient)var1);
               }
            });
            k[var3.ordinal()] = (BlockFlowers.EnumFlowerVarient[])var4.toArray(new BlockFlowers.EnumFlowerVarient[var4.size()]);
         }

      }
   }

   public static enum EnumFlowerType {
      YELLOW,
      RED;

      private EnumFlowerType() {
      }

      public BlockFlowers a() {
         return this == YELLOW?Blocks.YELLOW_FLOWER:Blocks.RED_FLOWER;
      }
   }
}
