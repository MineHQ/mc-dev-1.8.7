package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.server.BaseBlockPosition;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityMinecartChest;
import net.minecraft.server.EnumColor;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IInventory;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.StructureBoundingBox;
import net.minecraft.server.StructurePiece;
import net.minecraft.server.StructurePieceTreasure;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityMobSpawner;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenFactory;

public class WorldGenMineshaftPieces {
   private static final List<StructurePieceTreasure> a;

   public static void a() {
      WorldGenFactory.a(WorldGenMineshaftPieces.WorldGenMineshaftCorridor.class, "MSCorridor");
      WorldGenFactory.a(WorldGenMineshaftPieces.WorldGenMineshaftCross.class, "MSCrossing");
      WorldGenFactory.a(WorldGenMineshaftPieces.WorldGenMineshaftRoom.class, "MSRoom");
      WorldGenFactory.a(WorldGenMineshaftPieces.WorldGenMineshaftStairs.class, "MSStairs");
   }

   private static StructurePiece a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5, int var6) {
      int var7 = var1.nextInt(100);
      StructureBoundingBox var8;
      if(var7 >= 80) {
         var8 = WorldGenMineshaftPieces.WorldGenMineshaftCross.a(var0, var1, var2, var3, var4, var5);
         if(var8 != null) {
            return new WorldGenMineshaftPieces.WorldGenMineshaftCross(var6, var1, var8, var5);
         }
      } else if(var7 >= 70) {
         var8 = WorldGenMineshaftPieces.WorldGenMineshaftStairs.a(var0, var1, var2, var3, var4, var5);
         if(var8 != null) {
            return new WorldGenMineshaftPieces.WorldGenMineshaftStairs(var6, var1, var8, var5);
         }
      } else {
         var8 = WorldGenMineshaftPieces.WorldGenMineshaftCorridor.a(var0, var1, var2, var3, var4, var5);
         if(var8 != null) {
            return new WorldGenMineshaftPieces.WorldGenMineshaftCorridor(var6, var1, var8, var5);
         }
      }

      return null;
   }

   private static StructurePiece b(StructurePiece var0, List<StructurePiece> var1, Random var2, int var3, int var4, int var5, EnumDirection var6, int var7) {
      if(var7 > 8) {
         return null;
      } else if(Math.abs(var3 - var0.c().a) <= 80 && Math.abs(var5 - var0.c().c) <= 80) {
         StructurePiece var8 = a(var1, var2, var3, var4, var5, var6, var7 + 1);
         if(var8 != null) {
            var1.add(var8);
            var8.a(var0, var1, var2);
         }

         return var8;
      } else {
         return null;
      }
   }

   static {
      a = Lists.newArrayList((Object[])(new StructurePieceTreasure[]{new StructurePieceTreasure(Items.IRON_INGOT, 0, 1, 5, 10), new StructurePieceTreasure(Items.GOLD_INGOT, 0, 1, 3, 5), new StructurePieceTreasure(Items.REDSTONE, 0, 4, 9, 5), new StructurePieceTreasure(Items.DYE, EnumColor.BLUE.getInvColorIndex(), 4, 9, 5), new StructurePieceTreasure(Items.DIAMOND, 0, 1, 2, 3), new StructurePieceTreasure(Items.COAL, 0, 3, 8, 10), new StructurePieceTreasure(Items.BREAD, 0, 1, 3, 15), new StructurePieceTreasure(Items.IRON_PICKAXE, 0, 1, 1, 1), new StructurePieceTreasure(Item.getItemOf(Blocks.RAIL), 0, 4, 8, 1), new StructurePieceTreasure(Items.MELON_SEEDS, 0, 2, 4, 10), new StructurePieceTreasure(Items.PUMPKIN_SEEDS, 0, 2, 4, 10), new StructurePieceTreasure(Items.SADDLE, 0, 1, 1, 3), new StructurePieceTreasure(Items.IRON_HORSE_ARMOR, 0, 1, 1, 1)}));
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumDirection.values().length];

      static {
         try {
            a[EnumDirection.NORTH.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[EnumDirection.SOUTH.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.WEST.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.EAST.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static class WorldGenMineshaftStairs extends StructurePiece {
      public WorldGenMineshaftStairs() {
      }

      public WorldGenMineshaftStairs(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.m = var4;
         this.l = var3;
      }

      protected void a(NBTTagCompound var1) {
      }

      protected void b(NBTTagCompound var1) {
      }

      public static StructureBoundingBox a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5) {
         StructureBoundingBox var6 = new StructureBoundingBox(var2, var3 - 5, var4, var2, var3 + 2, var4);
         switch(WorldGenMineshaftPieces.SyntheticClass_1.a[var5.ordinal()]) {
         case 1:
            var6.d = var2 + 2;
            var6.c = var4 - 8;
            break;
         case 2:
            var6.d = var2 + 2;
            var6.f = var4 + 8;
            break;
         case 3:
            var6.a = var2 - 8;
            var6.f = var4 + 2;
            break;
         case 4:
            var6.d = var2 + 8;
            var6.f = var4 + 2;
         }

         return StructurePiece.a(var0, var6) != null?null:var6;
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         int var4 = this.d();
         if(this.m != null) {
            switch(WorldGenMineshaftPieces.SyntheticClass_1.a[this.m.ordinal()]) {
            case 1:
               WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a, this.l.b, this.l.c - 1, EnumDirection.NORTH, var4);
               break;
            case 2:
               WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a, this.l.b, this.l.f + 1, EnumDirection.SOUTH, var4);
               break;
            case 3:
               WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a - 1, this.l.b, this.l.c, EnumDirection.WEST, var4);
               break;
            case 4:
               WorldGenMineshaftPieces.b(var1, var2, var3, this.l.d + 1, this.l.b, this.l.c, EnumDirection.EAST, var4);
            }
         }

      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            this.a(var1, var3, 0, 5, 0, 2, 7, 1, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            this.a(var1, var3, 0, 0, 7, 2, 2, 8, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);

            for(int var4 = 0; var4 < 5; ++var4) {
               this.a(var1, var3, 0, 5 - var4 - (var4 < 4?1:0), 2 + var4, 2, 7 - var4, 2 + var4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            }

            return true;
         }
      }
   }

   public static class WorldGenMineshaftCross extends StructurePiece {
      private EnumDirection a;
      private boolean b;

      public WorldGenMineshaftCross() {
      }

      protected void a(NBTTagCompound var1) {
         var1.setBoolean("tf", this.b);
         var1.setInt("D", this.a.b());
      }

      protected void b(NBTTagCompound var1) {
         this.b = var1.getBoolean("tf");
         this.a = EnumDirection.fromType2(var1.getInt("D"));
      }

      public WorldGenMineshaftCross(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.a = var4;
         this.l = var3;
         this.b = var3.d() > 3;
      }

      public static StructureBoundingBox a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5) {
         StructureBoundingBox var6 = new StructureBoundingBox(var2, var3, var4, var2, var3 + 2, var4);
         if(var1.nextInt(4) == 0) {
            var6.e += 4;
         }

         switch(WorldGenMineshaftPieces.SyntheticClass_1.a[var5.ordinal()]) {
         case 1:
            var6.a = var2 - 1;
            var6.d = var2 + 3;
            var6.c = var4 - 4;
            break;
         case 2:
            var6.a = var2 - 1;
            var6.d = var2 + 3;
            var6.f = var4 + 4;
            break;
         case 3:
            var6.a = var2 - 4;
            var6.c = var4 - 1;
            var6.f = var4 + 3;
            break;
         case 4:
            var6.d = var2 + 4;
            var6.c = var4 - 1;
            var6.f = var4 + 3;
         }

         return StructurePiece.a(var0, var6) != null?null:var6;
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         int var4 = this.d();
         switch(WorldGenMineshaftPieces.SyntheticClass_1.a[this.a.ordinal()]) {
         case 1:
            WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a + 1, this.l.b, this.l.c - 1, EnumDirection.NORTH, var4);
            WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a - 1, this.l.b, this.l.c + 1, EnumDirection.WEST, var4);
            WorldGenMineshaftPieces.b(var1, var2, var3, this.l.d + 1, this.l.b, this.l.c + 1, EnumDirection.EAST, var4);
            break;
         case 2:
            WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a + 1, this.l.b, this.l.f + 1, EnumDirection.SOUTH, var4);
            WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a - 1, this.l.b, this.l.c + 1, EnumDirection.WEST, var4);
            WorldGenMineshaftPieces.b(var1, var2, var3, this.l.d + 1, this.l.b, this.l.c + 1, EnumDirection.EAST, var4);
            break;
         case 3:
            WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a + 1, this.l.b, this.l.c - 1, EnumDirection.NORTH, var4);
            WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a + 1, this.l.b, this.l.f + 1, EnumDirection.SOUTH, var4);
            WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a - 1, this.l.b, this.l.c + 1, EnumDirection.WEST, var4);
            break;
         case 4:
            WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a + 1, this.l.b, this.l.c - 1, EnumDirection.NORTH, var4);
            WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a + 1, this.l.b, this.l.f + 1, EnumDirection.SOUTH, var4);
            WorldGenMineshaftPieces.b(var1, var2, var3, this.l.d + 1, this.l.b, this.l.c + 1, EnumDirection.EAST, var4);
         }

         if(this.b) {
            if(var3.nextBoolean()) {
               WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a + 1, this.l.b + 3 + 1, this.l.c - 1, EnumDirection.NORTH, var4);
            }

            if(var3.nextBoolean()) {
               WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a - 1, this.l.b + 3 + 1, this.l.c + 1, EnumDirection.WEST, var4);
            }

            if(var3.nextBoolean()) {
               WorldGenMineshaftPieces.b(var1, var2, var3, this.l.d + 1, this.l.b + 3 + 1, this.l.c + 1, EnumDirection.EAST, var4);
            }

            if(var3.nextBoolean()) {
               WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a + 1, this.l.b + 3 + 1, this.l.f + 1, EnumDirection.SOUTH, var4);
            }
         }

      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            if(this.b) {
               this.a(var1, var3, this.l.a + 1, this.l.b, this.l.c, this.l.d - 1, this.l.b + 3 - 1, this.l.f, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
               this.a(var1, var3, this.l.a, this.l.b, this.l.c + 1, this.l.d, this.l.b + 3 - 1, this.l.f - 1, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
               this.a(var1, var3, this.l.a + 1, this.l.e - 2, this.l.c, this.l.d - 1, this.l.e, this.l.f, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
               this.a(var1, var3, this.l.a, this.l.e - 2, this.l.c + 1, this.l.d, this.l.e, this.l.f - 1, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
               this.a(var1, var3, this.l.a + 1, this.l.b + 3, this.l.c + 1, this.l.d - 1, this.l.b + 3, this.l.f - 1, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            } else {
               this.a(var1, var3, this.l.a + 1, this.l.b, this.l.c, this.l.d - 1, this.l.e, this.l.f, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
               this.a(var1, var3, this.l.a, this.l.b, this.l.c + 1, this.l.d, this.l.e, this.l.f - 1, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            }

            this.a(var1, var3, this.l.a + 1, this.l.b, this.l.c + 1, this.l.a + 1, this.l.e, this.l.c + 1, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
            this.a(var1, var3, this.l.a + 1, this.l.b, this.l.f - 1, this.l.a + 1, this.l.e, this.l.f - 1, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
            this.a(var1, var3, this.l.d - 1, this.l.b, this.l.c + 1, this.l.d - 1, this.l.e, this.l.c + 1, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
            this.a(var1, var3, this.l.d - 1, this.l.b, this.l.f - 1, this.l.d - 1, this.l.e, this.l.f - 1, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);

            for(int var4 = this.l.a; var4 <= this.l.d; ++var4) {
               for(int var5 = this.l.c; var5 <= this.l.f; ++var5) {
                  if(this.a(var1, var4, this.l.b - 1, var5, var3).getBlock().getMaterial() == Material.AIR) {
                     this.a(var1, Blocks.PLANKS.getBlockData(), var4, this.l.b - 1, var5, var3);
                  }
               }
            }

            return true;
         }
      }
   }

   public static class WorldGenMineshaftCorridor extends StructurePiece {
      private boolean a;
      private boolean b;
      private boolean c;
      private int d;

      public WorldGenMineshaftCorridor() {
      }

      protected void a(NBTTagCompound var1) {
         var1.setBoolean("hr", this.a);
         var1.setBoolean("sc", this.b);
         var1.setBoolean("hps", this.c);
         var1.setInt("Num", this.d);
      }

      protected void b(NBTTagCompound var1) {
         this.a = var1.getBoolean("hr");
         this.b = var1.getBoolean("sc");
         this.c = var1.getBoolean("hps");
         this.d = var1.getInt("Num");
      }

      public WorldGenMineshaftCorridor(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.m = var4;
         this.l = var3;
         this.a = var2.nextInt(3) == 0;
         this.b = !this.a && var2.nextInt(23) == 0;
         if(this.m != EnumDirection.NORTH && this.m != EnumDirection.SOUTH) {
            this.d = var3.c() / 5;
         } else {
            this.d = var3.e() / 5;
         }

      }

      public static StructureBoundingBox a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5) {
         StructureBoundingBox var6 = new StructureBoundingBox(var2, var3, var4, var2, var3 + 2, var4);

         int var7;
         for(var7 = var1.nextInt(3) + 2; var7 > 0; --var7) {
            int var8 = var7 * 5;
            switch(WorldGenMineshaftPieces.SyntheticClass_1.a[var5.ordinal()]) {
            case 1:
               var6.d = var2 + 2;
               var6.c = var4 - (var8 - 1);
               break;
            case 2:
               var6.d = var2 + 2;
               var6.f = var4 + (var8 - 1);
               break;
            case 3:
               var6.a = var2 - (var8 - 1);
               var6.f = var4 + 2;
               break;
            case 4:
               var6.d = var2 + (var8 - 1);
               var6.f = var4 + 2;
            }

            if(StructurePiece.a(var0, var6) == null) {
               break;
            }
         }

         return var7 > 0?var6:null;
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         int var4 = this.d();
         int var5 = var3.nextInt(4);
         if(this.m != null) {
            switch(WorldGenMineshaftPieces.SyntheticClass_1.a[this.m.ordinal()]) {
            case 1:
               if(var5 <= 1) {
                  WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a, this.l.b - 1 + var3.nextInt(3), this.l.c - 1, this.m, var4);
               } else if(var5 == 2) {
                  WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a - 1, this.l.b - 1 + var3.nextInt(3), this.l.c, EnumDirection.WEST, var4);
               } else {
                  WorldGenMineshaftPieces.b(var1, var2, var3, this.l.d + 1, this.l.b - 1 + var3.nextInt(3), this.l.c, EnumDirection.EAST, var4);
               }
               break;
            case 2:
               if(var5 <= 1) {
                  WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a, this.l.b - 1 + var3.nextInt(3), this.l.f + 1, this.m, var4);
               } else if(var5 == 2) {
                  WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a - 1, this.l.b - 1 + var3.nextInt(3), this.l.f - 3, EnumDirection.WEST, var4);
               } else {
                  WorldGenMineshaftPieces.b(var1, var2, var3, this.l.d + 1, this.l.b - 1 + var3.nextInt(3), this.l.f - 3, EnumDirection.EAST, var4);
               }
               break;
            case 3:
               if(var5 <= 1) {
                  WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a - 1, this.l.b - 1 + var3.nextInt(3), this.l.c, this.m, var4);
               } else if(var5 == 2) {
                  WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a, this.l.b - 1 + var3.nextInt(3), this.l.c - 1, EnumDirection.NORTH, var4);
               } else {
                  WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a, this.l.b - 1 + var3.nextInt(3), this.l.f + 1, EnumDirection.SOUTH, var4);
               }
               break;
            case 4:
               if(var5 <= 1) {
                  WorldGenMineshaftPieces.b(var1, var2, var3, this.l.d + 1, this.l.b - 1 + var3.nextInt(3), this.l.c, this.m, var4);
               } else if(var5 == 2) {
                  WorldGenMineshaftPieces.b(var1, var2, var3, this.l.d - 3, this.l.b - 1 + var3.nextInt(3), this.l.c - 1, EnumDirection.NORTH, var4);
               } else {
                  WorldGenMineshaftPieces.b(var1, var2, var3, this.l.d - 3, this.l.b - 1 + var3.nextInt(3), this.l.f + 1, EnumDirection.SOUTH, var4);
               }
            }
         }

         if(var4 < 8) {
            int var6;
            int var7;
            if(this.m != EnumDirection.NORTH && this.m != EnumDirection.SOUTH) {
               for(var6 = this.l.a + 3; var6 + 3 <= this.l.d; var6 += 5) {
                  var7 = var3.nextInt(5);
                  if(var7 == 0) {
                     WorldGenMineshaftPieces.b(var1, var2, var3, var6, this.l.b, this.l.c - 1, EnumDirection.NORTH, var4 + 1);
                  } else if(var7 == 1) {
                     WorldGenMineshaftPieces.b(var1, var2, var3, var6, this.l.b, this.l.f + 1, EnumDirection.SOUTH, var4 + 1);
                  }
               }
            } else {
               for(var6 = this.l.c + 3; var6 + 3 <= this.l.f; var6 += 5) {
                  var7 = var3.nextInt(5);
                  if(var7 == 0) {
                     WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a - 1, this.l.b, var6, EnumDirection.WEST, var4 + 1);
                  } else if(var7 == 1) {
                     WorldGenMineshaftPieces.b(var1, var2, var3, this.l.d + 1, this.l.b, var6, EnumDirection.EAST, var4 + 1);
                  }
               }
            }
         }

      }

      protected boolean a(World var1, StructureBoundingBox var2, Random var3, int var4, int var5, int var6, List<StructurePieceTreasure> var7, int var8) {
         BlockPosition var9 = new BlockPosition(this.a(var4, var6), this.d(var5), this.b(var4, var6));
         if(var2.b((BaseBlockPosition)var9) && var1.getType(var9).getBlock().getMaterial() == Material.AIR) {
            int var10 = var3.nextBoolean()?1:0;
            var1.setTypeAndData(var9, Blocks.RAIL.fromLegacyData(this.a(Blocks.RAIL, var10)), 2);
            EntityMinecartChest var11 = new EntityMinecartChest(var1, (double)((float)var9.getX() + 0.5F), (double)((float)var9.getY() + 0.5F), (double)((float)var9.getZ() + 0.5F));
            StructurePieceTreasure.a(var3, var7, (IInventory)var11, var8);
            var1.addEntity(var11);
            return true;
         } else {
            return false;
         }
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            boolean var4 = false;
            boolean var5 = true;
            boolean var6 = false;
            boolean var7 = true;
            int var8 = this.d * 5 - 1;
            this.a(var1, var3, 0, 0, 0, 2, 1, var8, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            this.a(var1, var3, var2, 0.8F, 0, 2, 0, 2, 2, var8, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            if(this.b) {
               this.a(var1, var3, var2, 0.6F, 0, 0, 0, 2, 1, var8, Blocks.WEB.getBlockData(), Blocks.AIR.getBlockData(), false);
            }

            int var9;
            int var10;
            for(var9 = 0; var9 < this.d; ++var9) {
               var10 = 2 + var9 * 5;
               this.a(var1, var3, 0, 0, var10, 0, 1, var10, Blocks.FENCE.getBlockData(), Blocks.AIR.getBlockData(), false);
               this.a(var1, var3, 2, 0, var10, 2, 1, var10, Blocks.FENCE.getBlockData(), Blocks.AIR.getBlockData(), false);
               if(var2.nextInt(4) == 0) {
                  this.a(var1, var3, 0, 2, var10, 0, 2, var10, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
                  this.a(var1, var3, 2, 2, var10, 2, 2, var10, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
               } else {
                  this.a(var1, var3, 0, 2, var10, 2, 2, var10, Blocks.PLANKS.getBlockData(), Blocks.AIR.getBlockData(), false);
               }

               this.a(var1, var3, var2, 0.1F, 0, 2, var10 - 1, Blocks.WEB.getBlockData());
               this.a(var1, var3, var2, 0.1F, 2, 2, var10 - 1, Blocks.WEB.getBlockData());
               this.a(var1, var3, var2, 0.1F, 0, 2, var10 + 1, Blocks.WEB.getBlockData());
               this.a(var1, var3, var2, 0.1F, 2, 2, var10 + 1, Blocks.WEB.getBlockData());
               this.a(var1, var3, var2, 0.05F, 0, 2, var10 - 2, Blocks.WEB.getBlockData());
               this.a(var1, var3, var2, 0.05F, 2, 2, var10 - 2, Blocks.WEB.getBlockData());
               this.a(var1, var3, var2, 0.05F, 0, 2, var10 + 2, Blocks.WEB.getBlockData());
               this.a(var1, var3, var2, 0.05F, 2, 2, var10 + 2, Blocks.WEB.getBlockData());
               this.a(var1, var3, var2, 0.05F, 1, 2, var10 - 1, Blocks.TORCH.fromLegacyData(EnumDirection.UP.a()));
               this.a(var1, var3, var2, 0.05F, 1, 2, var10 + 1, Blocks.TORCH.fromLegacyData(EnumDirection.UP.a()));
               if(var2.nextInt(100) == 0) {
                  this.a(var1, var3, var2, 2, 0, var10 - 1, StructurePieceTreasure.a(WorldGenMineshaftPieces.a, new StructurePieceTreasure[]{Items.ENCHANTED_BOOK.b(var2)}), 3 + var2.nextInt(4));
               }

               if(var2.nextInt(100) == 0) {
                  this.a(var1, var3, var2, 0, 0, var10 + 1, StructurePieceTreasure.a(WorldGenMineshaftPieces.a, new StructurePieceTreasure[]{Items.ENCHANTED_BOOK.b(var2)}), 3 + var2.nextInt(4));
               }

               if(this.b && !this.c) {
                  int var11 = this.d(0);
                  int var12 = var10 - 1 + var2.nextInt(3);
                  int var13 = this.a(1, var12);
                  var12 = this.b(1, var12);
                  BlockPosition var14 = new BlockPosition(var13, var11, var12);
                  if(var3.b((BaseBlockPosition)var14)) {
                     this.c = true;
                     var1.setTypeAndData(var14, Blocks.MOB_SPAWNER.getBlockData(), 2);
                     TileEntity var15 = var1.getTileEntity(var14);
                     if(var15 instanceof TileEntityMobSpawner) {
                        ((TileEntityMobSpawner)var15).getSpawner().setMobName("CaveSpider");
                     }
                  }
               }
            }

            for(var9 = 0; var9 <= 2; ++var9) {
               for(var10 = 0; var10 <= var8; ++var10) {
                  byte var17 = -1;
                  IBlockData var18 = this.a(var1, var9, var17, var10, var3);
                  if(var18.getBlock().getMaterial() == Material.AIR) {
                     byte var19 = -1;
                     this.a(var1, Blocks.PLANKS.getBlockData(), var9, var19, var10, var3);
                  }
               }
            }

            if(this.a) {
               for(var9 = 0; var9 <= var8; ++var9) {
                  IBlockData var16 = this.a(var1, 1, -1, var9, var3);
                  if(var16.getBlock().getMaterial() != Material.AIR && var16.getBlock().o()) {
                     this.a(var1, var3, var2, 0.7F, 1, 0, var9, Blocks.RAIL.fromLegacyData(this.a(Blocks.RAIL, 0)));
                  }
               }
            }

            return true;
         }
      }
   }

   public static class WorldGenMineshaftRoom extends StructurePiece {
      private List<StructureBoundingBox> a = Lists.newLinkedList();

      public WorldGenMineshaftRoom() {
      }

      public WorldGenMineshaftRoom(int var1, Random var2, int var3, int var4) {
         super(var1);
         this.l = new StructureBoundingBox(var3, 50, var4, var3 + 7 + var2.nextInt(6), 54 + var2.nextInt(6), var4 + 7 + var2.nextInt(6));
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         int var4 = this.d();
         int var6 = this.l.d() - 3 - 1;
         if(var6 <= 0) {
            var6 = 1;
         }

         int var5;
         StructurePiece var7;
         StructureBoundingBox var8;
         for(var5 = 0; var5 < this.l.c(); var5 += 4) {
            var5 += var3.nextInt(this.l.c());
            if(var5 + 3 > this.l.c()) {
               break;
            }

            var7 = WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a + var5, this.l.b + var3.nextInt(var6) + 1, this.l.c - 1, EnumDirection.NORTH, var4);
            if(var7 != null) {
               var8 = var7.c();
               this.a.add(new StructureBoundingBox(var8.a, var8.b, this.l.c, var8.d, var8.e, this.l.c + 1));
            }
         }

         for(var5 = 0; var5 < this.l.c(); var5 += 4) {
            var5 += var3.nextInt(this.l.c());
            if(var5 + 3 > this.l.c()) {
               break;
            }

            var7 = WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a + var5, this.l.b + var3.nextInt(var6) + 1, this.l.f + 1, EnumDirection.SOUTH, var4);
            if(var7 != null) {
               var8 = var7.c();
               this.a.add(new StructureBoundingBox(var8.a, var8.b, this.l.f - 1, var8.d, var8.e, this.l.f));
            }
         }

         for(var5 = 0; var5 < this.l.e(); var5 += 4) {
            var5 += var3.nextInt(this.l.e());
            if(var5 + 3 > this.l.e()) {
               break;
            }

            var7 = WorldGenMineshaftPieces.b(var1, var2, var3, this.l.a - 1, this.l.b + var3.nextInt(var6) + 1, this.l.c + var5, EnumDirection.WEST, var4);
            if(var7 != null) {
               var8 = var7.c();
               this.a.add(new StructureBoundingBox(this.l.a, var8.b, var8.c, this.l.a + 1, var8.e, var8.f));
            }
         }

         for(var5 = 0; var5 < this.l.e(); var5 += 4) {
            var5 += var3.nextInt(this.l.e());
            if(var5 + 3 > this.l.e()) {
               break;
            }

            var7 = WorldGenMineshaftPieces.b(var1, var2, var3, this.l.d + 1, this.l.b + var3.nextInt(var6) + 1, this.l.c + var5, EnumDirection.EAST, var4);
            if(var7 != null) {
               var8 = var7.c();
               this.a.add(new StructureBoundingBox(this.l.d - 1, var8.b, var8.c, this.l.d, var8.e, var8.f));
            }
         }

      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            this.a(var1, var3, this.l.a, this.l.b, this.l.c, this.l.d, this.l.b, this.l.f, Blocks.DIRT.getBlockData(), Blocks.AIR.getBlockData(), true);
            this.a(var1, var3, this.l.a, this.l.b + 1, this.l.c, this.l.d, Math.min(this.l.b + 3, this.l.e), this.l.f, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            Iterator var4 = this.a.iterator();

            while(var4.hasNext()) {
               StructureBoundingBox var5 = (StructureBoundingBox)var4.next();
               this.a(var1, var3, var5.a, var5.e - 2, var5.c, var5.d, var5.e, var5.f, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            }

            this.a(var1, var3, this.l.a, this.l.b + 4, this.l.c, this.l.d, this.l.e, this.l.f, Blocks.AIR.getBlockData(), false);
            return true;
         }
      }

      public void a(int var1, int var2, int var3) {
         super.a(var1, var2, var3);
         Iterator var4 = this.a.iterator();

         while(var4.hasNext()) {
            StructureBoundingBox var5 = (StructureBoundingBox)var4.next();
            var5.a(var1, var2, var3);
         }

      }

      protected void a(NBTTagCompound var1) {
         NBTTagList var2 = new NBTTagList();
         Iterator var3 = this.a.iterator();

         while(var3.hasNext()) {
            StructureBoundingBox var4 = (StructureBoundingBox)var3.next();
            var2.add(var4.g());
         }

         var1.set("Entrances", var2);
      }

      protected void b(NBTTagCompound var1) {
         NBTTagList var2 = var1.getList("Entrances", 11);

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            this.a.add(new StructureBoundingBox(var2.c(var3)));
         }

      }
   }
}
