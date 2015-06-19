package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.server.BaseBlockPosition;
import net.minecraft.server.BlockDoubleStepAbstract;
import net.minecraft.server.BlockEnderPortalFrame;
import net.minecraft.server.BlockMonsterEggs;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockSmoothBrick;
import net.minecraft.server.Blocks;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.StructureBoundingBox;
import net.minecraft.server.StructurePiece;
import net.minecraft.server.StructurePieceTreasure;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityMobSpawner;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenFactory;

public class WorldGenStrongholdPieces {
   private static final WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight[] b = new WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight[]{new WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight(WorldGenStrongholdPieces.WorldGenStrongholdStairs.class, 40, 0), new WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight(WorldGenStrongholdPieces.WorldGenStrongholdPrison.class, 5, 5), new WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight(WorldGenStrongholdPieces.WorldGenStrongholdLeftTurn.class, 20, 0), new WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight(WorldGenStrongholdPieces.WorldGenStrongholdRightTurn.class, 20, 0), new WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight(WorldGenStrongholdPieces.WorldGenStrongholdRoomCrossing.class, 10, 6), new WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight(WorldGenStrongholdPieces.WorldGenStrongholdStairsStraight.class, 5, 5), new WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight(WorldGenStrongholdPieces.WorldGenStrongholdStairs2.class, 5, 5), new WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight(WorldGenStrongholdPieces.WorldGenStrongholdCrossing.class, 5, 4), new WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight(WorldGenStrongholdPieces.WorldGenStrongholdChestCorridor.class, 5, 4), new WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight(WorldGenStrongholdPieces.WorldGenStrongholdLibrary.class, 10, 2) {
      public boolean a(int var1) {
         return super.a(var1) && var1 > 4;
      }
   }, new WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight(WorldGenStrongholdPieces.WorldGenStrongholdPortalRoom.class, 20, 1) {
      public boolean a(int var1) {
         return super.a(var1) && var1 > 5;
      }
   }};
   private static List<WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight> c;
   private static Class<? extends WorldGenStrongholdPieces.WorldGenStrongholdPiece> d;
   static int a;
   private static final WorldGenStrongholdPieces.WorldGenStrongholdStones e = new WorldGenStrongholdPieces.WorldGenStrongholdStones(null);

   public static void a() {
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdChestCorridor.class, "SHCC");
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdCorridor.class, "SHFC");
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdCrossing.class, "SH5C");
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdLeftTurn.class, "SHLT");
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdLibrary.class, "SHLi");
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdPortalRoom.class, "SHPR");
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdPrison.class, "SHPH");
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdRightTurn.class, "SHRT");
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdRoomCrossing.class, "SHRC");
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdStairs2.class, "SHSD");
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdStart.class, "SHStart");
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdStairs.class, "SHS");
      WorldGenFactory.a(WorldGenStrongholdPieces.WorldGenStrongholdStairsStraight.class, "SHSSD");
   }

   public static void b() {
      c = Lists.newArrayList();
      WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight[] var0 = b;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight var3 = var0[var2];
         var3.c = 0;
         c.add(var3);
      }

      d = null;
   }

   private static boolean d() {
      boolean var0 = false;
      a = 0;

      WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight var2;
      for(Iterator var1 = c.iterator(); var1.hasNext(); a += var2.b) {
         var2 = (WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight)var1.next();
         if(var2.d > 0 && var2.c < var2.d) {
            var0 = true;
         }
      }

      return var0;
   }

   private static WorldGenStrongholdPieces.WorldGenStrongholdPiece a(Class<? extends WorldGenStrongholdPieces.WorldGenStrongholdPiece> var0, List<StructurePiece> var1, Random var2, int var3, int var4, int var5, EnumDirection var6, int var7) {
      Object var8 = null;
      if(var0 == WorldGenStrongholdPieces.WorldGenStrongholdStairs.class) {
         var8 = WorldGenStrongholdPieces.WorldGenStrongholdStairs.a(var1, var2, var3, var4, var5, var6, var7);
      } else if(var0 == WorldGenStrongholdPieces.WorldGenStrongholdPrison.class) {
         var8 = WorldGenStrongholdPieces.WorldGenStrongholdPrison.a(var1, var2, var3, var4, var5, var6, var7);
      } else if(var0 == WorldGenStrongholdPieces.WorldGenStrongholdLeftTurn.class) {
         var8 = WorldGenStrongholdPieces.WorldGenStrongholdLeftTurn.a(var1, var2, var3, var4, var5, var6, var7);
      } else if(var0 == WorldGenStrongholdPieces.WorldGenStrongholdRightTurn.class) {
         var8 = WorldGenStrongholdPieces.WorldGenStrongholdRightTurn.a(var1, var2, var3, var4, var5, var6, var7);
      } else if(var0 == WorldGenStrongholdPieces.WorldGenStrongholdRoomCrossing.class) {
         var8 = WorldGenStrongholdPieces.WorldGenStrongholdRoomCrossing.a(var1, var2, var3, var4, var5, var6, var7);
      } else if(var0 == WorldGenStrongholdPieces.WorldGenStrongholdStairsStraight.class) {
         var8 = WorldGenStrongholdPieces.WorldGenStrongholdStairsStraight.a(var1, var2, var3, var4, var5, var6, var7);
      } else if(var0 == WorldGenStrongholdPieces.WorldGenStrongholdStairs2.class) {
         var8 = WorldGenStrongholdPieces.WorldGenStrongholdStairs2.a(var1, var2, var3, var4, var5, var6, var7);
      } else if(var0 == WorldGenStrongholdPieces.WorldGenStrongholdCrossing.class) {
         var8 = WorldGenStrongholdPieces.WorldGenStrongholdCrossing.a(var1, var2, var3, var4, var5, var6, var7);
      } else if(var0 == WorldGenStrongholdPieces.WorldGenStrongholdChestCorridor.class) {
         var8 = WorldGenStrongholdPieces.WorldGenStrongholdChestCorridor.a(var1, var2, var3, var4, var5, var6, var7);
      } else if(var0 == WorldGenStrongholdPieces.WorldGenStrongholdLibrary.class) {
         var8 = WorldGenStrongholdPieces.WorldGenStrongholdLibrary.a(var1, var2, var3, var4, var5, var6, var7);
      } else if(var0 == WorldGenStrongholdPieces.WorldGenStrongholdPortalRoom.class) {
         var8 = WorldGenStrongholdPieces.WorldGenStrongholdPortalRoom.a(var1, var2, var3, var4, var5, var6, var7);
      }

      return (WorldGenStrongholdPieces.WorldGenStrongholdPiece)var8;
   }

   private static WorldGenStrongholdPieces.WorldGenStrongholdPiece b(WorldGenStrongholdPieces.WorldGenStrongholdStart var0, List<StructurePiece> var1, Random var2, int var3, int var4, int var5, EnumDirection var6, int var7) {
      if(!d()) {
         return null;
      } else {
         if(d != null) {
            WorldGenStrongholdPieces.WorldGenStrongholdPiece var8 = a(d, var1, var2, var3, var4, var5, var6, var7);
            d = null;
            if(var8 != null) {
               return var8;
            }
         }

         int var13 = 0;

         while(var13 < 5) {
            ++var13;
            int var9 = var2.nextInt(a);
            Iterator var10 = c.iterator();

            while(var10.hasNext()) {
               WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight var11 = (WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight)var10.next();
               var9 -= var11.b;
               if(var9 < 0) {
                  if(!var11.a(var7) || var11 == var0.a) {
                     break;
                  }

                  WorldGenStrongholdPieces.WorldGenStrongholdPiece var12 = a(var11.a, var1, var2, var3, var4, var5, var6, var7);
                  if(var12 != null) {
                     ++var11.c;
                     var0.a = var11;
                     if(!var11.a()) {
                        c.remove(var11);
                     }

                     return var12;
                  }
               }
            }
         }

         StructureBoundingBox var14 = WorldGenStrongholdPieces.WorldGenStrongholdCorridor.a(var1, var2, var3, var4, var5, var6);
         if(var14 != null && var14.b > 1) {
            return new WorldGenStrongholdPieces.WorldGenStrongholdCorridor(var7, var2, var14, var6);
         } else {
            return null;
         }
      }
   }

   private static StructurePiece c(WorldGenStrongholdPieces.WorldGenStrongholdStart var0, List<StructurePiece> var1, Random var2, int var3, int var4, int var5, EnumDirection var6, int var7) {
      if(var7 > 50) {
         return null;
      } else if(Math.abs(var3 - var0.c().a) <= 112 && Math.abs(var5 - var0.c().c) <= 112) {
         WorldGenStrongholdPieces.WorldGenStrongholdPiece var8 = b(var0, var1, var2, var3, var4, var5, var6, var7 + 1);
         if(var8 != null) {
            var1.add(var8);
            var0.c.add(var8);
         }

         return var8;
      } else {
         return null;
      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a;
      // $FF: synthetic field
      static final int[] b = new int[EnumDirection.values().length];

      static {
         try {
            b[EnumDirection.NORTH.ordinal()] = 1;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            b[EnumDirection.SOUTH.ordinal()] = 2;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            b[EnumDirection.WEST.ordinal()] = 3;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            b[EnumDirection.EAST.ordinal()] = 4;
         } catch (NoSuchFieldError var5) {
            ;
         }

         a = new int[WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.values().length];

         try {
            a[WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.OPENING.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.WOOD_DOOR.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.GRATES.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.IRON_DOOR.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   static class WorldGenStrongholdStones extends StructurePiece.StructurePieceBlockSelector {
      private WorldGenStrongholdStones() {
      }

      public void a(Random var1, int var2, int var3, int var4, boolean var5) {
         if(var5) {
            float var6 = var1.nextFloat();
            if(var6 < 0.2F) {
               this.a = Blocks.STONEBRICK.fromLegacyData(BlockSmoothBrick.O);
            } else if(var6 < 0.5F) {
               this.a = Blocks.STONEBRICK.fromLegacyData(BlockSmoothBrick.N);
            } else if(var6 < 0.55F) {
               this.a = Blocks.MONSTER_EGG.fromLegacyData(BlockMonsterEggs.EnumMonsterEggVarient.STONEBRICK.a());
            } else {
               this.a = Blocks.STONEBRICK.getBlockData();
            }
         } else {
            this.a = Blocks.AIR.getBlockData();
         }

      }

      // $FF: synthetic method
      WorldGenStrongholdStones(Object var1) {
         this();
      }
   }

   public static class WorldGenStrongholdPortalRoom extends WorldGenStrongholdPieces.WorldGenStrongholdPiece {
      private boolean a;

      public WorldGenStrongholdPortalRoom() {
      }

      public WorldGenStrongholdPortalRoom(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.m = var4;
         this.l = var3;
      }

      protected void a(NBTTagCompound var1) {
         super.a(var1);
         var1.setBoolean("Mob", this.a);
      }

      protected void b(NBTTagCompound var1) {
         super.b(var1);
         this.a = var1.getBoolean("Mob");
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         if(var1 != null) {
            ((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1).b = this;
         }

      }

      public static WorldGenStrongholdPieces.WorldGenStrongholdPortalRoom a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.a(var2, var3, var4, -4, -1, 0, 11, 8, 16, var5);
         return a(var7) && StructurePiece.a(var0, var7) == null?new WorldGenStrongholdPieces.WorldGenStrongholdPortalRoom(var6, var1, var7, var5):null;
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         this.a(var1, var3, 0, 0, 0, 10, 7, 15, false, var2, WorldGenStrongholdPieces.e);
         this.a(var1, var2, var3, WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.GRATES, 4, 1, 0);
         byte var4 = 6;
         this.a(var1, var3, 1, var4, 1, 1, var4, 14, false, var2, WorldGenStrongholdPieces.e);
         this.a(var1, var3, 9, var4, 1, 9, var4, 14, false, var2, WorldGenStrongholdPieces.e);
         this.a(var1, var3, 2, var4, 1, 8, var4, 2, false, var2, WorldGenStrongholdPieces.e);
         this.a(var1, var3, 2, var4, 14, 8, var4, 14, false, var2, WorldGenStrongholdPieces.e);
         this.a(var1, var3, 1, 1, 1, 2, 1, 4, false, var2, WorldGenStrongholdPieces.e);
         this.a(var1, var3, 8, 1, 1, 9, 1, 4, false, var2, WorldGenStrongholdPieces.e);
         this.a(var1, var3, 1, 1, 1, 1, 1, 3, Blocks.FLOWING_LAVA.getBlockData(), Blocks.FLOWING_LAVA.getBlockData(), false);
         this.a(var1, var3, 9, 1, 1, 9, 1, 3, Blocks.FLOWING_LAVA.getBlockData(), Blocks.FLOWING_LAVA.getBlockData(), false);
         this.a(var1, var3, 3, 1, 8, 7, 1, 12, false, var2, WorldGenStrongholdPieces.e);
         this.a(var1, var3, 4, 1, 9, 6, 1, 11, Blocks.FLOWING_LAVA.getBlockData(), Blocks.FLOWING_LAVA.getBlockData(), false);

         int var5;
         for(var5 = 3; var5 < 14; var5 += 2) {
            this.a(var1, var3, 0, 3, var5, 0, 4, var5, Blocks.IRON_BARS.getBlockData(), Blocks.IRON_BARS.getBlockData(), false);
            this.a(var1, var3, 10, 3, var5, 10, 4, var5, Blocks.IRON_BARS.getBlockData(), Blocks.IRON_BARS.getBlockData(), false);
         }

         for(var5 = 2; var5 < 9; var5 += 2) {
            this.a(var1, var3, var5, 3, 15, var5, 4, 15, Blocks.IRON_BARS.getBlockData(), Blocks.IRON_BARS.getBlockData(), false);
         }

         var5 = this.a(Blocks.STONE_BRICK_STAIRS, 3);
         this.a(var1, var3, 4, 1, 5, 6, 1, 7, false, var2, WorldGenStrongholdPieces.e);
         this.a(var1, var3, 4, 2, 6, 6, 2, 7, false, var2, WorldGenStrongholdPieces.e);
         this.a(var1, var3, 4, 3, 7, 6, 3, 7, false, var2, WorldGenStrongholdPieces.e);

         int var6;
         for(var6 = 4; var6 <= 6; ++var6) {
            this.a(var1, Blocks.STONE_BRICK_STAIRS.fromLegacyData(var5), var6, 1, 4, var3);
            this.a(var1, Blocks.STONE_BRICK_STAIRS.fromLegacyData(var5), var6, 2, 5, var3);
            this.a(var1, Blocks.STONE_BRICK_STAIRS.fromLegacyData(var5), var6, 3, 6, var3);
         }

         var6 = EnumDirection.NORTH.b();
         int var7 = EnumDirection.SOUTH.b();
         int var8 = EnumDirection.EAST.b();
         int var9 = EnumDirection.WEST.b();
         if(this.m != null) {
            switch(WorldGenStrongholdPieces.SyntheticClass_1.b[this.m.ordinal()]) {
            case 2:
               var6 = EnumDirection.SOUTH.b();
               var7 = EnumDirection.NORTH.b();
               break;
            case 3:
               var6 = EnumDirection.WEST.b();
               var7 = EnumDirection.EAST.b();
               var8 = EnumDirection.SOUTH.b();
               var9 = EnumDirection.NORTH.b();
               break;
            case 4:
               var6 = EnumDirection.EAST.b();
               var7 = EnumDirection.WEST.b();
               var8 = EnumDirection.SOUTH.b();
               var9 = EnumDirection.NORTH.b();
            }
         }

         this.a(var1, Blocks.END_PORTAL_FRAME.fromLegacyData(var6).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(var2.nextFloat() > 0.9F)), 4, 3, 8, var3);
         this.a(var1, Blocks.END_PORTAL_FRAME.fromLegacyData(var6).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(var2.nextFloat() > 0.9F)), 5, 3, 8, var3);
         this.a(var1, Blocks.END_PORTAL_FRAME.fromLegacyData(var6).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(var2.nextFloat() > 0.9F)), 6, 3, 8, var3);
         this.a(var1, Blocks.END_PORTAL_FRAME.fromLegacyData(var7).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(var2.nextFloat() > 0.9F)), 4, 3, 12, var3);
         this.a(var1, Blocks.END_PORTAL_FRAME.fromLegacyData(var7).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(var2.nextFloat() > 0.9F)), 5, 3, 12, var3);
         this.a(var1, Blocks.END_PORTAL_FRAME.fromLegacyData(var7).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(var2.nextFloat() > 0.9F)), 6, 3, 12, var3);
         this.a(var1, Blocks.END_PORTAL_FRAME.fromLegacyData(var8).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(var2.nextFloat() > 0.9F)), 3, 3, 9, var3);
         this.a(var1, Blocks.END_PORTAL_FRAME.fromLegacyData(var8).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(var2.nextFloat() > 0.9F)), 3, 3, 10, var3);
         this.a(var1, Blocks.END_PORTAL_FRAME.fromLegacyData(var8).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(var2.nextFloat() > 0.9F)), 3, 3, 11, var3);
         this.a(var1, Blocks.END_PORTAL_FRAME.fromLegacyData(var9).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(var2.nextFloat() > 0.9F)), 7, 3, 9, var3);
         this.a(var1, Blocks.END_PORTAL_FRAME.fromLegacyData(var9).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(var2.nextFloat() > 0.9F)), 7, 3, 10, var3);
         this.a(var1, Blocks.END_PORTAL_FRAME.fromLegacyData(var9).set(BlockEnderPortalFrame.EYE, Boolean.valueOf(var2.nextFloat() > 0.9F)), 7, 3, 11, var3);
         if(!this.a) {
            int var12 = this.d(3);
            BlockPosition var10 = new BlockPosition(this.a(5, 6), var12, this.b(5, 6));
            if(var3.b((BaseBlockPosition)var10)) {
               this.a = true;
               var1.setTypeAndData(var10, Blocks.MOB_SPAWNER.getBlockData(), 2);
               TileEntity var11 = var1.getTileEntity(var10);
               if(var11 instanceof TileEntityMobSpawner) {
                  ((TileEntityMobSpawner)var11).getSpawner().setMobName("Silverfish");
               }
            }
         }

         return true;
      }
   }

   public static class WorldGenStrongholdCrossing extends WorldGenStrongholdPieces.WorldGenStrongholdPiece {
      private boolean a;
      private boolean b;
      private boolean c;
      private boolean e;

      public WorldGenStrongholdCrossing() {
      }

      public WorldGenStrongholdCrossing(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.m = var4;
         this.d = this.a(var2);
         this.l = var3;
         this.a = var2.nextBoolean();
         this.b = var2.nextBoolean();
         this.c = var2.nextBoolean();
         this.e = var2.nextInt(3) > 0;
      }

      protected void a(NBTTagCompound var1) {
         super.a(var1);
         var1.setBoolean("leftLow", this.a);
         var1.setBoolean("leftHigh", this.b);
         var1.setBoolean("rightLow", this.c);
         var1.setBoolean("rightHigh", this.e);
      }

      protected void b(NBTTagCompound var1) {
         super.b(var1);
         this.a = var1.getBoolean("leftLow");
         this.b = var1.getBoolean("leftHigh");
         this.c = var1.getBoolean("rightLow");
         this.e = var1.getBoolean("rightHigh");
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         int var4 = 3;
         int var5 = 5;
         if(this.m == EnumDirection.WEST || this.m == EnumDirection.NORTH) {
            var4 = 8 - var4;
            var5 = 8 - var5;
         }

         this.a((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 5, 1);
         if(this.a) {
            this.b((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, var4, 1);
         }

         if(this.b) {
            this.b((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, var5, 7);
         }

         if(this.c) {
            this.c((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, var4, 1);
         }

         if(this.e) {
            this.c((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, var5, 7);
         }

      }

      public static WorldGenStrongholdPieces.WorldGenStrongholdCrossing a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.a(var2, var3, var4, -4, -3, 0, 10, 9, 11, var5);
         return a(var7) && StructurePiece.a(var0, var7) == null?new WorldGenStrongholdPieces.WorldGenStrongholdCrossing(var6, var1, var7, var5):null;
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            this.a(var1, var3, 0, 0, 0, 9, 8, 10, true, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var2, var3, this.d, 4, 3, 0);
            if(this.a) {
               this.a(var1, var3, 0, 3, 1, 0, 5, 3, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            }

            if(this.c) {
               this.a(var1, var3, 9, 3, 1, 9, 5, 3, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            }

            if(this.b) {
               this.a(var1, var3, 0, 5, 7, 0, 7, 9, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            }

            if(this.e) {
               this.a(var1, var3, 9, 5, 7, 9, 7, 9, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            }

            this.a(var1, var3, 5, 1, 10, 7, 3, 10, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            this.a(var1, var3, 1, 2, 1, 8, 2, 6, false, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var3, 4, 1, 5, 4, 4, 9, false, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var3, 8, 1, 5, 8, 4, 9, false, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var3, 1, 4, 7, 3, 4, 9, false, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var3, 1, 3, 5, 3, 3, 6, false, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var3, 1, 3, 4, 3, 3, 4, Blocks.STONE_SLAB.getBlockData(), Blocks.STONE_SLAB.getBlockData(), false);
            this.a(var1, var3, 1, 4, 6, 3, 4, 6, Blocks.STONE_SLAB.getBlockData(), Blocks.STONE_SLAB.getBlockData(), false);
            this.a(var1, var3, 5, 1, 7, 7, 1, 8, false, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var3, 5, 1, 9, 7, 1, 9, Blocks.STONE_SLAB.getBlockData(), Blocks.STONE_SLAB.getBlockData(), false);
            this.a(var1, var3, 5, 2, 7, 7, 2, 7, Blocks.STONE_SLAB.getBlockData(), Blocks.STONE_SLAB.getBlockData(), false);
            this.a(var1, var3, 4, 5, 7, 4, 5, 9, Blocks.STONE_SLAB.getBlockData(), Blocks.STONE_SLAB.getBlockData(), false);
            this.a(var1, var3, 8, 5, 7, 8, 5, 9, Blocks.STONE_SLAB.getBlockData(), Blocks.STONE_SLAB.getBlockData(), false);
            this.a(var1, var3, 5, 5, 7, 7, 5, 9, Blocks.DOUBLE_STONE_SLAB.getBlockData(), Blocks.DOUBLE_STONE_SLAB.getBlockData(), false);
            this.a(var1, Blocks.TORCH.getBlockData(), 6, 5, 6, var3);
            return true;
         }
      }
   }

   public static class WorldGenStrongholdLibrary extends WorldGenStrongholdPieces.WorldGenStrongholdPiece {
      private static final List<StructurePieceTreasure> a;
      private boolean b;

      public WorldGenStrongholdLibrary() {
      }

      public WorldGenStrongholdLibrary(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.m = var4;
         this.d = this.a(var2);
         this.l = var3;
         this.b = var3.d() > 6;
      }

      protected void a(NBTTagCompound var1) {
         super.a(var1);
         var1.setBoolean("Tall", this.b);
      }

      protected void b(NBTTagCompound var1) {
         super.b(var1);
         this.b = var1.getBoolean("Tall");
      }

      public static WorldGenStrongholdPieces.WorldGenStrongholdLibrary a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.a(var2, var3, var4, -4, -1, 0, 14, 11, 15, var5);
         if(!a(var7) || StructurePiece.a(var0, var7) != null) {
            var7 = StructureBoundingBox.a(var2, var3, var4, -4, -1, 0, 14, 6, 15, var5);
            if(!a(var7) || StructurePiece.a(var0, var7) != null) {
               return null;
            }
         }

         return new WorldGenStrongholdPieces.WorldGenStrongholdLibrary(var6, var1, var7, var5);
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            byte var4 = 11;
            if(!this.b) {
               var4 = 6;
            }

            this.a(var1, var3, 0, 0, 0, 13, var4 - 1, 14, true, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var2, var3, this.d, 4, 1, 0);
            this.a(var1, var3, var2, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.WEB.getBlockData(), Blocks.WEB.getBlockData(), false);
            boolean var5 = true;
            boolean var6 = true;

            int var7;
            for(var7 = 1; var7 <= 13; ++var7) {
               if((var7 - 1) % 4 == 0) {
                  this.a(var1, var3, 1, 1, var7, 1, 4, var7, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
                  this.a(var1, var3, 12, 1, var7, 12, 4, var7, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
                  this.a(var1, Blocks.TORCH.getBlockData(), 2, 3, var7, var3);
                  this.a(var1, Blocks.TORCH.getBlockData(), 11, 3, var7, var3);
                  if(this.b) {
                     this.a(var1, var3, 1, 6, var7, 1, 9, var7, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
                     this.a(var1, var3, 12, 6, var7, 12, 9, var7, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
                  }
               } else {
                  this.a(var1, var3, 1, 1, var7, 1, 4, var7, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
                  this.a(var1, var3, 12, 1, var7, 12, 4, var7, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
                  if(this.b) {
                     this.a(var1, var3, 1, 6, var7, 1, 9, var7, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
                     this.a(var1, var3, 12, 6, var7, 12, 9, var7, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
                  }
               }
            }

            for(var7 = 3; var7 < 12; var7 += 2) {
               this.a(var1, var3, 3, 1, var7, 4, 3, var7, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
               this.a(var1, var3, 6, 1, var7, 7, 3, var7, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
               this.a(var1, var3, 9, 1, var7, 10, 3, var7, Blocks.BOOKSHELF.getBlockData(), Blocks.BOOKSHELF.getBlockData(), false);
            }

            if(this.b) {
               this.a(var1, var3, 1, 5, 1, 3, 5, 13, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
               this.a(var1, var3, 10, 5, 1, 12, 5, 13, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
               this.a(var1, var3, 4, 5, 1, 9, 5, 2, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
               this.a(var1, var3, 4, 5, 12, 9, 5, 13, Blocks.PLANKS.getBlockData(), Blocks.PLANKS.getBlockData(), false);
               this.a(var1, Blocks.PLANKS.getBlockData(), 9, 5, 11, var3);
               this.a(var1, Blocks.PLANKS.getBlockData(), 8, 5, 11, var3);
               this.a(var1, Blocks.PLANKS.getBlockData(), 9, 5, 10, var3);
               this.a(var1, var3, 3, 6, 2, 3, 6, 12, Blocks.FENCE.getBlockData(), Blocks.FENCE.getBlockData(), false);
               this.a(var1, var3, 10, 6, 2, 10, 6, 10, Blocks.FENCE.getBlockData(), Blocks.FENCE.getBlockData(), false);
               this.a(var1, var3, 4, 6, 2, 9, 6, 2, Blocks.FENCE.getBlockData(), Blocks.FENCE.getBlockData(), false);
               this.a(var1, var3, 4, 6, 12, 8, 6, 12, Blocks.FENCE.getBlockData(), Blocks.FENCE.getBlockData(), false);
               this.a(var1, Blocks.FENCE.getBlockData(), 9, 6, 11, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), 8, 6, 11, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), 9, 6, 10, var3);
               var7 = this.a(Blocks.LADDER, 3);
               this.a(var1, Blocks.LADDER.fromLegacyData(var7), 10, 1, 13, var3);
               this.a(var1, Blocks.LADDER.fromLegacyData(var7), 10, 2, 13, var3);
               this.a(var1, Blocks.LADDER.fromLegacyData(var7), 10, 3, 13, var3);
               this.a(var1, Blocks.LADDER.fromLegacyData(var7), 10, 4, 13, var3);
               this.a(var1, Blocks.LADDER.fromLegacyData(var7), 10, 5, 13, var3);
               this.a(var1, Blocks.LADDER.fromLegacyData(var7), 10, 6, 13, var3);
               this.a(var1, Blocks.LADDER.fromLegacyData(var7), 10, 7, 13, var3);
               byte var8 = 7;
               byte var9 = 7;
               this.a(var1, Blocks.FENCE.getBlockData(), var8 - 1, 9, var9, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), var8, 9, var9, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), var8 - 1, 8, var9, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), var8, 8, var9, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), var8 - 1, 7, var9, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), var8, 7, var9, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), var8 - 2, 7, var9, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), var8 + 1, 7, var9, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), var8 - 1, 7, var9 - 1, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), var8 - 1, 7, var9 + 1, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), var8, 7, var9 - 1, var3);
               this.a(var1, Blocks.FENCE.getBlockData(), var8, 7, var9 + 1, var3);
               this.a(var1, Blocks.TORCH.getBlockData(), var8 - 2, 8, var9, var3);
               this.a(var1, Blocks.TORCH.getBlockData(), var8 + 1, 8, var9, var3);
               this.a(var1, Blocks.TORCH.getBlockData(), var8 - 1, 8, var9 - 1, var3);
               this.a(var1, Blocks.TORCH.getBlockData(), var8 - 1, 8, var9 + 1, var3);
               this.a(var1, Blocks.TORCH.getBlockData(), var8, 8, var9 - 1, var3);
               this.a(var1, Blocks.TORCH.getBlockData(), var8, 8, var9 + 1, var3);
            }

            this.a(var1, var3, var2, 3, 3, 5, StructurePieceTreasure.a(a, new StructurePieceTreasure[]{Items.ENCHANTED_BOOK.a(var2, 1, 5, 2)}), 1 + var2.nextInt(4));
            if(this.b) {
               this.a(var1, Blocks.AIR.getBlockData(), 12, 9, 1, var3);
               this.a(var1, var3, var2, 12, 8, 1, StructurePieceTreasure.a(a, new StructurePieceTreasure[]{Items.ENCHANTED_BOOK.a(var2, 1, 5, 2)}), 1 + var2.nextInt(4));
            }

            return true;
         }
      }

      static {
         a = Lists.newArrayList((Object[])(new StructurePieceTreasure[]{new StructurePieceTreasure(Items.BOOK, 0, 1, 3, 20), new StructurePieceTreasure(Items.PAPER, 0, 2, 7, 20), new StructurePieceTreasure(Items.MAP, 0, 1, 1, 1), new StructurePieceTreasure(Items.COMPASS, 0, 1, 1, 1)}));
      }
   }

   public static class WorldGenStrongholdPrison extends WorldGenStrongholdPieces.WorldGenStrongholdPiece {
      public WorldGenStrongholdPrison() {
      }

      public WorldGenStrongholdPrison(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.m = var4;
         this.d = this.a(var2);
         this.l = var3;
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         this.a((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 1);
      }

      public static WorldGenStrongholdPieces.WorldGenStrongholdPrison a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.a(var2, var3, var4, -1, -1, 0, 9, 5, 11, var5);
         return a(var7) && StructurePiece.a(var0, var7) == null?new WorldGenStrongholdPieces.WorldGenStrongholdPrison(var6, var1, var7, var5):null;
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            this.a(var1, var3, 0, 0, 0, 8, 4, 10, true, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var2, var3, this.d, 1, 1, 0);
            this.a(var1, var3, 1, 1, 10, 3, 3, 10, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            this.a(var1, var3, 4, 1, 1, 4, 3, 1, false, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var3, 4, 1, 3, 4, 3, 3, false, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var3, 4, 1, 7, 4, 3, 7, false, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var3, 4, 1, 9, 4, 3, 9, false, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var3, 4, 1, 4, 4, 3, 6, Blocks.IRON_BARS.getBlockData(), Blocks.IRON_BARS.getBlockData(), false);
            this.a(var1, var3, 5, 1, 5, 7, 3, 5, Blocks.IRON_BARS.getBlockData(), Blocks.IRON_BARS.getBlockData(), false);
            this.a(var1, Blocks.IRON_BARS.getBlockData(), 4, 3, 2, var3);
            this.a(var1, Blocks.IRON_BARS.getBlockData(), 4, 3, 8, var3);
            this.a(var1, Blocks.IRON_DOOR.fromLegacyData(this.a(Blocks.IRON_DOOR, 3)), 4, 1, 2, var3);
            this.a(var1, Blocks.IRON_DOOR.fromLegacyData(this.a(Blocks.IRON_DOOR, 3) + 8), 4, 2, 2, var3);
            this.a(var1, Blocks.IRON_DOOR.fromLegacyData(this.a(Blocks.IRON_DOOR, 3)), 4, 1, 8, var3);
            this.a(var1, Blocks.IRON_DOOR.fromLegacyData(this.a(Blocks.IRON_DOOR, 3) + 8), 4, 2, 8, var3);
            return true;
         }
      }
   }

   public static class WorldGenStrongholdRoomCrossing extends WorldGenStrongholdPieces.WorldGenStrongholdPiece {
      private static final List<StructurePieceTreasure> b;
      protected int a;

      public WorldGenStrongholdRoomCrossing() {
      }

      public WorldGenStrongholdRoomCrossing(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.m = var4;
         this.d = this.a(var2);
         this.l = var3;
         this.a = var2.nextInt(5);
      }

      protected void a(NBTTagCompound var1) {
         super.a(var1);
         var1.setInt("Type", this.a);
      }

      protected void b(NBTTagCompound var1) {
         super.b(var1);
         this.a = var1.getInt("Type");
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         this.a((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 4, 1);
         this.b((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 4);
         this.c((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 4);
      }

      public static WorldGenStrongholdPieces.WorldGenStrongholdRoomCrossing a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.a(var2, var3, var4, -4, -1, 0, 11, 7, 11, var5);
         return a(var7) && StructurePiece.a(var0, var7) == null?new WorldGenStrongholdPieces.WorldGenStrongholdRoomCrossing(var6, var1, var7, var5):null;
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            this.a(var1, var3, 0, 0, 0, 10, 6, 10, true, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var2, var3, this.d, 4, 1, 0);
            this.a(var1, var3, 4, 1, 10, 6, 3, 10, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            this.a(var1, var3, 0, 1, 4, 0, 3, 6, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            this.a(var1, var3, 10, 1, 4, 10, 3, 6, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            int var4;
            switch(this.a) {
            case 0:
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 5, 1, 5, var3);
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 5, 2, 5, var3);
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 5, 3, 5, var3);
               this.a(var1, Blocks.TORCH.getBlockData(), 4, 3, 5, var3);
               this.a(var1, Blocks.TORCH.getBlockData(), 6, 3, 5, var3);
               this.a(var1, Blocks.TORCH.getBlockData(), 5, 3, 4, var3);
               this.a(var1, Blocks.TORCH.getBlockData(), 5, 3, 6, var3);
               this.a(var1, Blocks.STONE_SLAB.getBlockData(), 4, 1, 4, var3);
               this.a(var1, Blocks.STONE_SLAB.getBlockData(), 4, 1, 5, var3);
               this.a(var1, Blocks.STONE_SLAB.getBlockData(), 4, 1, 6, var3);
               this.a(var1, Blocks.STONE_SLAB.getBlockData(), 6, 1, 4, var3);
               this.a(var1, Blocks.STONE_SLAB.getBlockData(), 6, 1, 5, var3);
               this.a(var1, Blocks.STONE_SLAB.getBlockData(), 6, 1, 6, var3);
               this.a(var1, Blocks.STONE_SLAB.getBlockData(), 5, 1, 4, var3);
               this.a(var1, Blocks.STONE_SLAB.getBlockData(), 5, 1, 6, var3);
               break;
            case 1:
               for(var4 = 0; var4 < 5; ++var4) {
                  this.a(var1, Blocks.STONEBRICK.getBlockData(), 3, 1, 3 + var4, var3);
                  this.a(var1, Blocks.STONEBRICK.getBlockData(), 7, 1, 3 + var4, var3);
                  this.a(var1, Blocks.STONEBRICK.getBlockData(), 3 + var4, 1, 3, var3);
                  this.a(var1, Blocks.STONEBRICK.getBlockData(), 3 + var4, 1, 7, var3);
               }

               this.a(var1, Blocks.STONEBRICK.getBlockData(), 5, 1, 5, var3);
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 5, 2, 5, var3);
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 5, 3, 5, var3);
               this.a(var1, Blocks.FLOWING_WATER.getBlockData(), 5, 4, 5, var3);
               break;
            case 2:
               for(var4 = 1; var4 <= 9; ++var4) {
                  this.a(var1, Blocks.COBBLESTONE.getBlockData(), 1, 3, var4, var3);
                  this.a(var1, Blocks.COBBLESTONE.getBlockData(), 9, 3, var4, var3);
               }

               for(var4 = 1; var4 <= 9; ++var4) {
                  this.a(var1, Blocks.COBBLESTONE.getBlockData(), var4, 3, 1, var3);
                  this.a(var1, Blocks.COBBLESTONE.getBlockData(), var4, 3, 9, var3);
               }

               this.a(var1, Blocks.COBBLESTONE.getBlockData(), 5, 1, 4, var3);
               this.a(var1, Blocks.COBBLESTONE.getBlockData(), 5, 1, 6, var3);
               this.a(var1, Blocks.COBBLESTONE.getBlockData(), 5, 3, 4, var3);
               this.a(var1, Blocks.COBBLESTONE.getBlockData(), 5, 3, 6, var3);
               this.a(var1, Blocks.COBBLESTONE.getBlockData(), 4, 1, 5, var3);
               this.a(var1, Blocks.COBBLESTONE.getBlockData(), 6, 1, 5, var3);
               this.a(var1, Blocks.COBBLESTONE.getBlockData(), 4, 3, 5, var3);
               this.a(var1, Blocks.COBBLESTONE.getBlockData(), 6, 3, 5, var3);

               for(var4 = 1; var4 <= 3; ++var4) {
                  this.a(var1, Blocks.COBBLESTONE.getBlockData(), 4, var4, 4, var3);
                  this.a(var1, Blocks.COBBLESTONE.getBlockData(), 6, var4, 4, var3);
                  this.a(var1, Blocks.COBBLESTONE.getBlockData(), 4, var4, 6, var3);
                  this.a(var1, Blocks.COBBLESTONE.getBlockData(), 6, var4, 6, var3);
               }

               this.a(var1, Blocks.TORCH.getBlockData(), 5, 3, 5, var3);

               for(var4 = 2; var4 <= 8; ++var4) {
                  this.a(var1, Blocks.PLANKS.getBlockData(), 2, 3, var4, var3);
                  this.a(var1, Blocks.PLANKS.getBlockData(), 3, 3, var4, var3);
                  if(var4 <= 3 || var4 >= 7) {
                     this.a(var1, Blocks.PLANKS.getBlockData(), 4, 3, var4, var3);
                     this.a(var1, Blocks.PLANKS.getBlockData(), 5, 3, var4, var3);
                     this.a(var1, Blocks.PLANKS.getBlockData(), 6, 3, var4, var3);
                  }

                  this.a(var1, Blocks.PLANKS.getBlockData(), 7, 3, var4, var3);
                  this.a(var1, Blocks.PLANKS.getBlockData(), 8, 3, var4, var3);
               }

               this.a(var1, Blocks.LADDER.fromLegacyData(this.a(Blocks.LADDER, EnumDirection.WEST.a())), 9, 1, 3, var3);
               this.a(var1, Blocks.LADDER.fromLegacyData(this.a(Blocks.LADDER, EnumDirection.WEST.a())), 9, 2, 3, var3);
               this.a(var1, Blocks.LADDER.fromLegacyData(this.a(Blocks.LADDER, EnumDirection.WEST.a())), 9, 3, 3, var3);
               this.a(var1, var3, var2, 3, 4, 8, StructurePieceTreasure.a(b, new StructurePieceTreasure[]{Items.ENCHANTED_BOOK.b(var2)}), 1 + var2.nextInt(4));
            }

            return true;
         }
      }

      static {
         b = Lists.newArrayList((Object[])(new StructurePieceTreasure[]{new StructurePieceTreasure(Items.IRON_INGOT, 0, 1, 5, 10), new StructurePieceTreasure(Items.GOLD_INGOT, 0, 1, 3, 5), new StructurePieceTreasure(Items.REDSTONE, 0, 4, 9, 5), new StructurePieceTreasure(Items.COAL, 0, 3, 8, 10), new StructurePieceTreasure(Items.BREAD, 0, 1, 3, 15), new StructurePieceTreasure(Items.APPLE, 0, 1, 3, 15), new StructurePieceTreasure(Items.IRON_PICKAXE, 0, 1, 1, 1)}));
      }
   }

   public static class WorldGenStrongholdRightTurn extends WorldGenStrongholdPieces.WorldGenStrongholdLeftTurn {
      public WorldGenStrongholdRightTurn() {
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         if(this.m != EnumDirection.NORTH && this.m != EnumDirection.EAST) {
            this.b((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 1);
         } else {
            this.c((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 1);
         }

      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            this.a(var1, var3, 0, 0, 0, 4, 4, 4, true, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var2, var3, this.d, 1, 1, 0);
            if(this.m != EnumDirection.NORTH && this.m != EnumDirection.EAST) {
               this.a(var1, var3, 0, 1, 1, 0, 3, 3, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            } else {
               this.a(var1, var3, 4, 1, 1, 4, 3, 3, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            }

            return true;
         }
      }
   }

   public static class WorldGenStrongholdLeftTurn extends WorldGenStrongholdPieces.WorldGenStrongholdPiece {
      public WorldGenStrongholdLeftTurn() {
      }

      public WorldGenStrongholdLeftTurn(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.m = var4;
         this.d = this.a(var2);
         this.l = var3;
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         if(this.m != EnumDirection.NORTH && this.m != EnumDirection.EAST) {
            this.c((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 1);
         } else {
            this.b((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 1);
         }

      }

      public static WorldGenStrongholdPieces.WorldGenStrongholdLeftTurn a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.a(var2, var3, var4, -1, -1, 0, 5, 5, 5, var5);
         return a(var7) && StructurePiece.a(var0, var7) == null?new WorldGenStrongholdPieces.WorldGenStrongholdLeftTurn(var6, var1, var7, var5):null;
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            this.a(var1, var3, 0, 0, 0, 4, 4, 4, true, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var2, var3, this.d, 1, 1, 0);
            if(this.m != EnumDirection.NORTH && this.m != EnumDirection.EAST) {
               this.a(var1, var3, 4, 1, 1, 4, 3, 3, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            } else {
               this.a(var1, var3, 0, 1, 1, 0, 3, 3, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            }

            return true;
         }
      }
   }

   public static class WorldGenStrongholdStairsStraight extends WorldGenStrongholdPieces.WorldGenStrongholdPiece {
      public WorldGenStrongholdStairsStraight() {
      }

      public WorldGenStrongholdStairsStraight(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.m = var4;
         this.d = this.a(var2);
         this.l = var3;
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         this.a((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 1);
      }

      public static WorldGenStrongholdPieces.WorldGenStrongholdStairsStraight a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.a(var2, var3, var4, -1, -7, 0, 5, 11, 8, var5);
         return a(var7) && StructurePiece.a(var0, var7) == null?new WorldGenStrongholdPieces.WorldGenStrongholdStairsStraight(var6, var1, var7, var5):null;
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            this.a(var1, var3, 0, 0, 0, 4, 10, 7, true, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var2, var3, this.d, 1, 7, 0);
            this.a(var1, var2, var3, WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.OPENING, 1, 1, 7);
            int var4 = this.a(Blocks.STONE_STAIRS, 2);

            for(int var5 = 0; var5 < 6; ++var5) {
               this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 1, 6 - var5, 1 + var5, var3);
               this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 2, 6 - var5, 1 + var5, var3);
               this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 3, 6 - var5, 1 + var5, var3);
               if(var5 < 5) {
                  this.a(var1, Blocks.STONEBRICK.getBlockData(), 1, 5 - var5, 1 + var5, var3);
                  this.a(var1, Blocks.STONEBRICK.getBlockData(), 2, 5 - var5, 1 + var5, var3);
                  this.a(var1, Blocks.STONEBRICK.getBlockData(), 3, 5 - var5, 1 + var5, var3);
               }
            }

            return true;
         }
      }
   }

   public static class WorldGenStrongholdChestCorridor extends WorldGenStrongholdPieces.WorldGenStrongholdPiece {
      private static final List<StructurePieceTreasure> a;
      private boolean b;

      public WorldGenStrongholdChestCorridor() {
      }

      public WorldGenStrongholdChestCorridor(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.m = var4;
         this.d = this.a(var2);
         this.l = var3;
      }

      protected void a(NBTTagCompound var1) {
         super.a(var1);
         var1.setBoolean("Chest", this.b);
      }

      protected void b(NBTTagCompound var1) {
         super.b(var1);
         this.b = var1.getBoolean("Chest");
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         this.a((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 1);
      }

      public static WorldGenStrongholdPieces.WorldGenStrongholdChestCorridor a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.a(var2, var3, var4, -1, -1, 0, 5, 5, 7, var5);
         return a(var7) && StructurePiece.a(var0, var7) == null?new WorldGenStrongholdPieces.WorldGenStrongholdChestCorridor(var6, var1, var7, var5):null;
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            this.a(var1, var3, 0, 0, 0, 4, 4, 6, true, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var2, var3, this.d, 1, 1, 0);
            this.a(var1, var2, var3, WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.OPENING, 1, 1, 6);
            this.a(var1, var3, 3, 1, 2, 3, 1, 4, Blocks.STONEBRICK.getBlockData(), Blocks.STONEBRICK.getBlockData(), false);
            this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.SMOOTHBRICK.a()), 3, 1, 1, var3);
            this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.SMOOTHBRICK.a()), 3, 1, 5, var3);
            this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.SMOOTHBRICK.a()), 3, 2, 2, var3);
            this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.SMOOTHBRICK.a()), 3, 2, 4, var3);

            for(int var4 = 2; var4 <= 4; ++var4) {
               this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.SMOOTHBRICK.a()), 2, 1, var4, var3);
            }

            if(!this.b && var3.b((BaseBlockPosition)(new BlockPosition(this.a(3, 3), this.d(2), this.b(3, 3))))) {
               this.b = true;
               this.a(var1, var3, var2, 3, 2, 3, StructurePieceTreasure.a(a, new StructurePieceTreasure[]{Items.ENCHANTED_BOOK.b(var2)}), 2 + var2.nextInt(2));
            }

            return true;
         }
      }

      static {
         a = Lists.newArrayList((Object[])(new StructurePieceTreasure[]{new StructurePieceTreasure(Items.ENDER_PEARL, 0, 1, 1, 10), new StructurePieceTreasure(Items.DIAMOND, 0, 1, 3, 3), new StructurePieceTreasure(Items.IRON_INGOT, 0, 1, 5, 10), new StructurePieceTreasure(Items.GOLD_INGOT, 0, 1, 3, 5), new StructurePieceTreasure(Items.REDSTONE, 0, 4, 9, 5), new StructurePieceTreasure(Items.BREAD, 0, 1, 3, 15), new StructurePieceTreasure(Items.APPLE, 0, 1, 3, 15), new StructurePieceTreasure(Items.IRON_PICKAXE, 0, 1, 1, 5), new StructurePieceTreasure(Items.IRON_SWORD, 0, 1, 1, 5), new StructurePieceTreasure(Items.IRON_CHESTPLATE, 0, 1, 1, 5), new StructurePieceTreasure(Items.IRON_HELMET, 0, 1, 1, 5), new StructurePieceTreasure(Items.IRON_LEGGINGS, 0, 1, 1, 5), new StructurePieceTreasure(Items.IRON_BOOTS, 0, 1, 1, 5), new StructurePieceTreasure(Items.GOLDEN_APPLE, 0, 1, 1, 1), new StructurePieceTreasure(Items.SADDLE, 0, 1, 1, 1), new StructurePieceTreasure(Items.IRON_HORSE_ARMOR, 0, 1, 1, 1), new StructurePieceTreasure(Items.GOLDEN_HORSE_ARMOR, 0, 1, 1, 1), new StructurePieceTreasure(Items.DIAMOND_HORSE_ARMOR, 0, 1, 1, 1)}));
      }
   }

   public static class WorldGenStrongholdStairs extends WorldGenStrongholdPieces.WorldGenStrongholdPiece {
      private boolean a;
      private boolean b;

      public WorldGenStrongholdStairs() {
      }

      public WorldGenStrongholdStairs(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.m = var4;
         this.d = this.a(var2);
         this.l = var3;
         this.a = var2.nextInt(2) == 0;
         this.b = var2.nextInt(2) == 0;
      }

      protected void a(NBTTagCompound var1) {
         super.a(var1);
         var1.setBoolean("Left", this.a);
         var1.setBoolean("Right", this.b);
      }

      protected void b(NBTTagCompound var1) {
         super.b(var1);
         this.a = var1.getBoolean("Left");
         this.b = var1.getBoolean("Right");
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         this.a((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 1);
         if(this.a) {
            this.b((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 2);
         }

         if(this.b) {
            this.c((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 2);
         }

      }

      public static WorldGenStrongholdPieces.WorldGenStrongholdStairs a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.a(var2, var3, var4, -1, -1, 0, 5, 5, 7, var5);
         return a(var7) && StructurePiece.a(var0, var7) == null?new WorldGenStrongholdPieces.WorldGenStrongholdStairs(var6, var1, var7, var5):null;
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            this.a(var1, var3, 0, 0, 0, 4, 4, 6, true, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var2, var3, this.d, 1, 1, 0);
            this.a(var1, var2, var3, WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.OPENING, 1, 1, 6);
            this.a(var1, var3, var2, 0.1F, 1, 2, 1, Blocks.TORCH.getBlockData());
            this.a(var1, var3, var2, 0.1F, 3, 2, 1, Blocks.TORCH.getBlockData());
            this.a(var1, var3, var2, 0.1F, 1, 2, 5, Blocks.TORCH.getBlockData());
            this.a(var1, var3, var2, 0.1F, 3, 2, 5, Blocks.TORCH.getBlockData());
            if(this.a) {
               this.a(var1, var3, 0, 1, 2, 0, 3, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            }

            if(this.b) {
               this.a(var1, var3, 4, 1, 2, 4, 3, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            }

            return true;
         }
      }
   }

   public static class WorldGenStrongholdStart extends WorldGenStrongholdPieces.WorldGenStrongholdStairs2 {
      public WorldGenStrongholdPieces.WorldGenStrongholdPieceWeight a;
      public WorldGenStrongholdPieces.WorldGenStrongholdPortalRoom b;
      public List<StructurePiece> c = Lists.newArrayList();

      public WorldGenStrongholdStart() {
      }

      public WorldGenStrongholdStart(int var1, Random var2, int var3, int var4) {
         super(0, var2, var3, var4);
      }

      public BlockPosition a() {
         return this.b != null?this.b.a():super.a();
      }
   }

   public static class WorldGenStrongholdStairs2 extends WorldGenStrongholdPieces.WorldGenStrongholdPiece {
      private boolean a;

      public WorldGenStrongholdStairs2() {
      }

      public WorldGenStrongholdStairs2(int var1, Random var2, int var3, int var4) {
         super(var1);
         this.a = true;
         this.m = EnumDirection.EnumDirectionLimit.HORIZONTAL.a(var2);
         this.d = WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.OPENING;
         switch(WorldGenStrongholdPieces.SyntheticClass_1.b[this.m.ordinal()]) {
         case 1:
         case 2:
            this.l = new StructureBoundingBox(var3, 64, var4, var3 + 5 - 1, 74, var4 + 5 - 1);
            break;
         default:
            this.l = new StructureBoundingBox(var3, 64, var4, var3 + 5 - 1, 74, var4 + 5 - 1);
         }

      }

      public WorldGenStrongholdStairs2(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.a = false;
         this.m = var4;
         this.d = this.a(var2);
         this.l = var3;
      }

      protected void a(NBTTagCompound var1) {
         super.a(var1);
         var1.setBoolean("Source", this.a);
      }

      protected void b(NBTTagCompound var1) {
         super.b(var1);
         this.a = var1.getBoolean("Source");
      }

      public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
         if(this.a) {
            WorldGenStrongholdPieces.d = WorldGenStrongholdPieces.WorldGenStrongholdCrossing.class;
         }

         this.a((WorldGenStrongholdPieces.WorldGenStrongholdStart)var1, var2, var3, 1, 1);
      }

      public static WorldGenStrongholdPieces.WorldGenStrongholdStairs2 a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.a(var2, var3, var4, -1, -7, 0, 5, 11, 5, var5);
         return a(var7) && StructurePiece.a(var0, var7) == null?new WorldGenStrongholdPieces.WorldGenStrongholdStairs2(var6, var1, var7, var5):null;
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            this.a(var1, var3, 0, 0, 0, 4, 10, 4, true, var2, WorldGenStrongholdPieces.e);
            this.a(var1, var2, var3, this.d, 1, 7, 0);
            this.a(var1, var2, var3, WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.OPENING, 1, 1, 4);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), 2, 6, 1, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), 1, 5, 1, var3);
            this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE.a()), 1, 6, 1, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), 1, 5, 2, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), 1, 4, 3, var3);
            this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE.a()), 1, 5, 3, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), 2, 4, 3, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), 3, 3, 3, var3);
            this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE.a()), 3, 4, 3, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), 3, 3, 2, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), 3, 2, 1, var3);
            this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE.a()), 3, 3, 1, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), 2, 2, 1, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), 1, 1, 1, var3);
            this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE.a()), 1, 2, 1, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), 1, 1, 2, var3);
            this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE.a()), 1, 1, 3, var3);
            return true;
         }
      }
   }

   public static class WorldGenStrongholdCorridor extends WorldGenStrongholdPieces.WorldGenStrongholdPiece {
      private int a;

      public WorldGenStrongholdCorridor() {
      }

      public WorldGenStrongholdCorridor(int var1, Random var2, StructureBoundingBox var3, EnumDirection var4) {
         super(var1);
         this.m = var4;
         this.l = var3;
         this.a = var4 != EnumDirection.NORTH && var4 != EnumDirection.SOUTH?var3.c():var3.e();
      }

      protected void a(NBTTagCompound var1) {
         super.a(var1);
         var1.setInt("Steps", this.a);
      }

      protected void b(NBTTagCompound var1) {
         super.b(var1);
         this.a = var1.getInt("Steps");
      }

      public static StructureBoundingBox a(List<StructurePiece> var0, Random var1, int var2, int var3, int var4, EnumDirection var5) {
         boolean var6 = true;
         StructureBoundingBox var7 = StructureBoundingBox.a(var2, var3, var4, -1, -1, 0, 5, 5, 4, var5);
         StructurePiece var8 = StructurePiece.a(var0, var7);
         if(var8 == null) {
            return null;
         } else {
            if(var8.c().b == var7.b) {
               for(int var9 = 3; var9 >= 1; --var9) {
                  var7 = StructureBoundingBox.a(var2, var3, var4, -1, -1, 0, 5, 5, var9 - 1, var5);
                  if(!var8.c().a(var7)) {
                     return StructureBoundingBox.a(var2, var3, var4, -1, -1, 0, 5, 5, var9, var5);
                  }
               }
            }

            return null;
         }
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(this.a(var1, var3)) {
            return false;
         } else {
            for(int var4 = 0; var4 < this.a; ++var4) {
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 0, 0, var4, var3);
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 1, 0, var4, var3);
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 2, 0, var4, var3);
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 3, 0, var4, var3);
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 4, 0, var4, var3);

               for(int var5 = 1; var5 <= 3; ++var5) {
                  this.a(var1, Blocks.STONEBRICK.getBlockData(), 0, var5, var4, var3);
                  this.a(var1, Blocks.AIR.getBlockData(), 1, var5, var4, var3);
                  this.a(var1, Blocks.AIR.getBlockData(), 2, var5, var4, var3);
                  this.a(var1, Blocks.AIR.getBlockData(), 3, var5, var4, var3);
                  this.a(var1, Blocks.STONEBRICK.getBlockData(), 4, var5, var4, var3);
               }

               this.a(var1, Blocks.STONEBRICK.getBlockData(), 0, 4, var4, var3);
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 1, 4, var4, var3);
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 2, 4, var4, var3);
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 3, 4, var4, var3);
               this.a(var1, Blocks.STONEBRICK.getBlockData(), 4, 4, var4, var3);
            }

            return true;
         }
      }
   }

   abstract static class WorldGenStrongholdPiece extends StructurePiece {
      protected WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType d;

      public WorldGenStrongholdPiece() {
         this.d = WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.OPENING;
      }

      protected WorldGenStrongholdPiece(int var1) {
         super(var1);
         this.d = WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.OPENING;
      }

      protected void a(NBTTagCompound var1) {
         var1.setString("EntryDoor", this.d.name());
      }

      protected void b(NBTTagCompound var1) {
         this.d = WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.valueOf(var1.getString("EntryDoor"));
      }

      protected void a(World var1, Random var2, StructureBoundingBox var3, WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType var4, int var5, int var6, int var7) {
         switch(WorldGenStrongholdPieces.SyntheticClass_1.a[var4.ordinal()]) {
         case 1:
         default:
            this.a(var1, var3, var5, var6, var7, var5 + 3 - 1, var6 + 3 - 1, var7, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
            break;
         case 2:
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5, var6, var7, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5, var6 + 1, var7, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5, var6 + 2, var7, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5 + 1, var6 + 2, var7, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5 + 2, var6 + 2, var7, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5 + 2, var6 + 1, var7, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5 + 2, var6, var7, var3);
            this.a(var1, Blocks.WOODEN_DOOR.getBlockData(), var5 + 1, var6, var7, var3);
            this.a(var1, Blocks.WOODEN_DOOR.fromLegacyData(8), var5 + 1, var6 + 1, var7, var3);
            break;
         case 3:
            this.a(var1, Blocks.AIR.getBlockData(), var5 + 1, var6, var7, var3);
            this.a(var1, Blocks.AIR.getBlockData(), var5 + 1, var6 + 1, var7, var3);
            this.a(var1, Blocks.IRON_BARS.getBlockData(), var5, var6, var7, var3);
            this.a(var1, Blocks.IRON_BARS.getBlockData(), var5, var6 + 1, var7, var3);
            this.a(var1, Blocks.IRON_BARS.getBlockData(), var5, var6 + 2, var7, var3);
            this.a(var1, Blocks.IRON_BARS.getBlockData(), var5 + 1, var6 + 2, var7, var3);
            this.a(var1, Blocks.IRON_BARS.getBlockData(), var5 + 2, var6 + 2, var7, var3);
            this.a(var1, Blocks.IRON_BARS.getBlockData(), var5 + 2, var6 + 1, var7, var3);
            this.a(var1, Blocks.IRON_BARS.getBlockData(), var5 + 2, var6, var7, var3);
            break;
         case 4:
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5, var6, var7, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5, var6 + 1, var7, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5, var6 + 2, var7, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5 + 1, var6 + 2, var7, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5 + 2, var6 + 2, var7, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5 + 2, var6 + 1, var7, var3);
            this.a(var1, Blocks.STONEBRICK.getBlockData(), var5 + 2, var6, var7, var3);
            this.a(var1, Blocks.IRON_DOOR.getBlockData(), var5 + 1, var6, var7, var3);
            this.a(var1, Blocks.IRON_DOOR.fromLegacyData(8), var5 + 1, var6 + 1, var7, var3);
            this.a(var1, Blocks.STONE_BUTTON.fromLegacyData(this.a(Blocks.STONE_BUTTON, 4)), var5 + 2, var6 + 1, var7 + 1, var3);
            this.a(var1, Blocks.STONE_BUTTON.fromLegacyData(this.a(Blocks.STONE_BUTTON, 3)), var5 + 2, var6 + 1, var7 - 1, var3);
         }

      }

      protected WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType a(Random var1) {
         int var2 = var1.nextInt(5);
         switch(var2) {
         case 0:
         case 1:
         default:
            return WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.OPENING;
         case 2:
            return WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.WOOD_DOOR;
         case 3:
            return WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.GRATES;
         case 4:
            return WorldGenStrongholdPieces.WorldGenStrongholdPiece.WorldGenStrongholdPiece$WorldGenStrongholdDoorType.IRON_DOOR;
         }
      }

      protected StructurePiece a(WorldGenStrongholdPieces.WorldGenStrongholdStart var1, List<StructurePiece> var2, Random var3, int var4, int var5) {
         if(this.m != null) {
            switch(WorldGenStrongholdPieces.SyntheticClass_1.b[this.m.ordinal()]) {
            case 1:
               return WorldGenStrongholdPieces.c(var1, var2, var3, this.l.a + var4, this.l.b + var5, this.l.c - 1, this.m, this.d());
            case 2:
               return WorldGenStrongholdPieces.c(var1, var2, var3, this.l.a + var4, this.l.b + var5, this.l.f + 1, this.m, this.d());
            case 3:
               return WorldGenStrongholdPieces.c(var1, var2, var3, this.l.a - 1, this.l.b + var5, this.l.c + var4, this.m, this.d());
            case 4:
               return WorldGenStrongholdPieces.c(var1, var2, var3, this.l.d + 1, this.l.b + var5, this.l.c + var4, this.m, this.d());
            }
         }

         return null;
      }

      protected StructurePiece b(WorldGenStrongholdPieces.WorldGenStrongholdStart var1, List<StructurePiece> var2, Random var3, int var4, int var5) {
         if(this.m != null) {
            switch(WorldGenStrongholdPieces.SyntheticClass_1.b[this.m.ordinal()]) {
            case 1:
               return WorldGenStrongholdPieces.c(var1, var2, var3, this.l.a - 1, this.l.b + var4, this.l.c + var5, EnumDirection.WEST, this.d());
            case 2:
               return WorldGenStrongholdPieces.c(var1, var2, var3, this.l.a - 1, this.l.b + var4, this.l.c + var5, EnumDirection.WEST, this.d());
            case 3:
               return WorldGenStrongholdPieces.c(var1, var2, var3, this.l.a + var5, this.l.b + var4, this.l.c - 1, EnumDirection.NORTH, this.d());
            case 4:
               return WorldGenStrongholdPieces.c(var1, var2, var3, this.l.a + var5, this.l.b + var4, this.l.c - 1, EnumDirection.NORTH, this.d());
            }
         }

         return null;
      }

      protected StructurePiece c(WorldGenStrongholdPieces.WorldGenStrongholdStart var1, List<StructurePiece> var2, Random var3, int var4, int var5) {
         if(this.m != null) {
            switch(WorldGenStrongholdPieces.SyntheticClass_1.b[this.m.ordinal()]) {
            case 1:
               return WorldGenStrongholdPieces.c(var1, var2, var3, this.l.d + 1, this.l.b + var4, this.l.c + var5, EnumDirection.EAST, this.d());
            case 2:
               return WorldGenStrongholdPieces.c(var1, var2, var3, this.l.d + 1, this.l.b + var4, this.l.c + var5, EnumDirection.EAST, this.d());
            case 3:
               return WorldGenStrongholdPieces.c(var1, var2, var3, this.l.a + var5, this.l.b + var4, this.l.f + 1, EnumDirection.SOUTH, this.d());
            case 4:
               return WorldGenStrongholdPieces.c(var1, var2, var3, this.l.a + var5, this.l.b + var4, this.l.f + 1, EnumDirection.SOUTH, this.d());
            }
         }

         return null;
      }

      protected static boolean a(StructureBoundingBox var0) {
         return var0 != null && var0.b > 10;
      }

      public static enum WorldGenStrongholdPiece$WorldGenStrongholdDoorType {
         OPENING,
         WOOD_DOOR,
         GRATES,
         IRON_DOOR;

         private WorldGenStrongholdPiece$WorldGenStrongholdDoorType() {
         }
      }
   }

   static class WorldGenStrongholdPieceWeight {
      public Class<? extends WorldGenStrongholdPieces.WorldGenStrongholdPiece> a;
      public final int b;
      public int c;
      public int d;

      public WorldGenStrongholdPieceWeight(Class<? extends WorldGenStrongholdPieces.WorldGenStrongholdPiece> var1, int var2, int var3) {
         this.a = var1;
         this.b = var2;
         this.d = var3;
      }

      public boolean a(int var1) {
         return this.d == 0 || this.c < this.d;
      }

      public boolean a() {
         return this.d == 0 || this.c < this.d;
      }
   }
}
