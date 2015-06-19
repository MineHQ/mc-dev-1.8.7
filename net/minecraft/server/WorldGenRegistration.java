package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.server.BaseBlockPosition;
import net.minecraft.server.BlockDoubleStepAbstract;
import net.minecraft.server.BlockFlowerPot;
import net.minecraft.server.BlockLever;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockSandStone;
import net.minecraft.server.BlockSmoothBrick;
import net.minecraft.server.BlockTripwire;
import net.minecraft.server.BlockTripwireHook;
import net.minecraft.server.BlockWood;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityWitch;
import net.minecraft.server.EnumColor;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.StructureBoundingBox;
import net.minecraft.server.StructurePiece;
import net.minecraft.server.StructurePieceTreasure;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenFactory;

public class WorldGenRegistration {
   public static void a() {
      WorldGenFactory.a(WorldGenRegistration.WorldGenPyramidPiece.class, "TeDP");
      WorldGenFactory.a(WorldGenRegistration.WorldGenJungleTemple.class, "TeJP");
      WorldGenFactory.a(WorldGenRegistration.WorldGenWitchHut.class, "TeSH");
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumDirection.values().length];

      static {
         try {
            a[EnumDirection.NORTH.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.SOUTH.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static class WorldGenWitchHut extends WorldGenRegistration.WorldGenScatteredPiece {
      private boolean e;

      public WorldGenWitchHut() {
      }

      public WorldGenWitchHut(Random var1, int var2, int var3) {
         super(var1, var2, 64, var3, 7, 7, 9);
      }

      protected void a(NBTTagCompound var1) {
         super.a(var1);
         var1.setBoolean("Witch", this.e);
      }

      protected void b(NBTTagCompound var1) {
         super.b(var1);
         this.e = var1.getBoolean("Witch");
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(!this.a(var1, var3, 0)) {
            return false;
         } else {
            this.a(var1, var3, 1, 1, 1, 5, 1, 7, Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), false);
            this.a(var1, var3, 1, 4, 2, 5, 4, 7, Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), false);
            this.a(var1, var3, 2, 1, 0, 4, 1, 0, Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), false);
            this.a(var1, var3, 2, 2, 2, 3, 3, 2, Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), false);
            this.a(var1, var3, 1, 2, 3, 1, 3, 6, Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), false);
            this.a(var1, var3, 5, 2, 3, 5, 3, 6, Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), false);
            this.a(var1, var3, 2, 2, 7, 4, 3, 7, Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), Blocks.PLANKS.fromLegacyData(BlockWood.EnumLogVariant.SPRUCE.a()), false);
            this.a(var1, var3, 1, 0, 2, 1, 3, 2, Blocks.LOG.getBlockData(), Blocks.LOG.getBlockData(), false);
            this.a(var1, var3, 5, 0, 2, 5, 3, 2, Blocks.LOG.getBlockData(), Blocks.LOG.getBlockData(), false);
            this.a(var1, var3, 1, 0, 7, 1, 3, 7, Blocks.LOG.getBlockData(), Blocks.LOG.getBlockData(), false);
            this.a(var1, var3, 5, 0, 7, 5, 3, 7, Blocks.LOG.getBlockData(), Blocks.LOG.getBlockData(), false);
            this.a(var1, Blocks.FENCE.getBlockData(), 2, 3, 2, var3);
            this.a(var1, Blocks.FENCE.getBlockData(), 3, 3, 7, var3);
            this.a(var1, Blocks.AIR.getBlockData(), 1, 3, 4, var3);
            this.a(var1, Blocks.AIR.getBlockData(), 5, 3, 4, var3);
            this.a(var1, Blocks.AIR.getBlockData(), 5, 3, 5, var3);
            this.a(var1, Blocks.FLOWER_POT.getBlockData().set(BlockFlowerPot.CONTENTS, BlockFlowerPot.EnumFlowerPotContents.MUSHROOM_RED), 1, 3, 5, var3);
            this.a(var1, Blocks.CRAFTING_TABLE.getBlockData(), 3, 2, 6, var3);
            this.a(var1, Blocks.cauldron.getBlockData(), 4, 2, 6, var3);
            this.a(var1, Blocks.FENCE.getBlockData(), 1, 2, 1, var3);
            this.a(var1, Blocks.FENCE.getBlockData(), 5, 2, 1, var3);
            int var4 = this.a(Blocks.OAK_STAIRS, 3);
            int var5 = this.a(Blocks.OAK_STAIRS, 1);
            int var6 = this.a(Blocks.OAK_STAIRS, 0);
            int var7 = this.a(Blocks.OAK_STAIRS, 2);
            this.a(var1, var3, 0, 4, 1, 6, 4, 1, Blocks.SPRUCE_STAIRS.fromLegacyData(var4), Blocks.SPRUCE_STAIRS.fromLegacyData(var4), false);
            this.a(var1, var3, 0, 4, 2, 0, 4, 7, Blocks.SPRUCE_STAIRS.fromLegacyData(var6), Blocks.SPRUCE_STAIRS.fromLegacyData(var6), false);
            this.a(var1, var3, 6, 4, 2, 6, 4, 7, Blocks.SPRUCE_STAIRS.fromLegacyData(var5), Blocks.SPRUCE_STAIRS.fromLegacyData(var5), false);
            this.a(var1, var3, 0, 4, 8, 6, 4, 8, Blocks.SPRUCE_STAIRS.fromLegacyData(var7), Blocks.SPRUCE_STAIRS.fromLegacyData(var7), false);

            int var8;
            int var9;
            for(var8 = 2; var8 <= 7; var8 += 5) {
               for(var9 = 1; var9 <= 5; var9 += 4) {
                  this.b(var1, Blocks.LOG.getBlockData(), var9, -1, var8, var3);
               }
            }

            if(!this.e) {
               var8 = this.a(2, 5);
               var9 = this.d(2);
               int var10 = this.b(2, 5);
               if(var3.b((BaseBlockPosition)(new BlockPosition(var8, var9, var10)))) {
                  this.e = true;
                  EntityWitch var11 = new EntityWitch(var1);
                  var11.setPositionRotation((double)var8 + 0.5D, (double)var9, (double)var10 + 0.5D, 0.0F, 0.0F);
                  var11.prepare(var1.E(new BlockPosition(var8, var9, var10)), (GroupDataEntity)null);
                  var1.addEntity(var11);
               }
            }

            return true;
         }
      }
   }

   public static class WorldGenJungleTemple extends WorldGenRegistration.WorldGenScatteredPiece {
      private boolean e;
      private boolean f;
      private boolean g;
      private boolean h;
      private static final List<StructurePieceTreasure> i;
      private static final List<StructurePieceTreasure> j;
      private static WorldGenRegistration.WorldGenJungleTemple.WorldGenJungleTemple$WorldGenJungleTemplePiece k;

      public WorldGenJungleTemple() {
      }

      public WorldGenJungleTemple(Random var1, int var2, int var3) {
         super(var1, var2, 64, var3, 12, 10, 15);
      }

      protected void a(NBTTagCompound var1) {
         super.a(var1);
         var1.setBoolean("placedMainChest", this.e);
         var1.setBoolean("placedHiddenChest", this.f);
         var1.setBoolean("placedTrap1", this.g);
         var1.setBoolean("placedTrap2", this.h);
      }

      protected void b(NBTTagCompound var1) {
         super.b(var1);
         this.e = var1.getBoolean("placedMainChest");
         this.f = var1.getBoolean("placedHiddenChest");
         this.g = var1.getBoolean("placedTrap1");
         this.h = var1.getBoolean("placedTrap2");
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         if(!this.a(var1, var3, 0)) {
            return false;
         } else {
            int var4 = this.a(Blocks.STONE_STAIRS, 3);
            int var5 = this.a(Blocks.STONE_STAIRS, 2);
            int var6 = this.a(Blocks.STONE_STAIRS, 0);
            int var7 = this.a(Blocks.STONE_STAIRS, 1);
            this.a(var1, var3, 0, -4, 0, this.a - 1, 0, this.c - 1, false, var2, k);
            this.a(var1, var3, 2, 1, 2, 9, 2, 2, false, var2, k);
            this.a(var1, var3, 2, 1, 12, 9, 2, 12, false, var2, k);
            this.a(var1, var3, 2, 1, 3, 2, 2, 11, false, var2, k);
            this.a(var1, var3, 9, 1, 3, 9, 2, 11, false, var2, k);
            this.a(var1, var3, 1, 3, 1, 10, 6, 1, false, var2, k);
            this.a(var1, var3, 1, 3, 13, 10, 6, 13, false, var2, k);
            this.a(var1, var3, 1, 3, 2, 1, 6, 12, false, var2, k);
            this.a(var1, var3, 10, 3, 2, 10, 6, 12, false, var2, k);
            this.a(var1, var3, 2, 3, 2, 9, 3, 12, false, var2, k);
            this.a(var1, var3, 2, 6, 2, 9, 6, 12, false, var2, k);
            this.a(var1, var3, 3, 7, 3, 8, 7, 11, false, var2, k);
            this.a(var1, var3, 4, 8, 4, 7, 8, 10, false, var2, k);
            this.a(var1, var3, 3, 1, 3, 8, 2, 11);
            this.a(var1, var3, 4, 3, 6, 7, 3, 9);
            this.a(var1, var3, 2, 4, 2, 9, 5, 12);
            this.a(var1, var3, 4, 6, 5, 7, 6, 9);
            this.a(var1, var3, 5, 7, 6, 6, 7, 8);
            this.a(var1, var3, 5, 1, 2, 6, 2, 2);
            this.a(var1, var3, 5, 2, 12, 6, 2, 12);
            this.a(var1, var3, 5, 5, 1, 6, 5, 1);
            this.a(var1, var3, 5, 5, 13, 6, 5, 13);
            this.a(var1, Blocks.AIR.getBlockData(), 1, 5, 5, var3);
            this.a(var1, Blocks.AIR.getBlockData(), 10, 5, 5, var3);
            this.a(var1, Blocks.AIR.getBlockData(), 1, 5, 9, var3);
            this.a(var1, Blocks.AIR.getBlockData(), 10, 5, 9, var3);

            int var8;
            for(var8 = 0; var8 <= 14; var8 += 14) {
               this.a(var1, var3, 2, 4, var8, 2, 5, var8, false, var2, k);
               this.a(var1, var3, 4, 4, var8, 4, 5, var8, false, var2, k);
               this.a(var1, var3, 7, 4, var8, 7, 5, var8, false, var2, k);
               this.a(var1, var3, 9, 4, var8, 9, 5, var8, false, var2, k);
            }

            this.a(var1, var3, 5, 6, 0, 6, 6, 0, false, var2, k);

            for(var8 = 0; var8 <= 11; var8 += 11) {
               for(int var9 = 2; var9 <= 12; var9 += 2) {
                  this.a(var1, var3, var8, 4, var9, var8, 5, var9, false, var2, k);
               }

               this.a(var1, var3, var8, 6, 5, var8, 6, 5, false, var2, k);
               this.a(var1, var3, var8, 6, 9, var8, 6, 9, false, var2, k);
            }

            this.a(var1, var3, 2, 7, 2, 2, 9, 2, false, var2, k);
            this.a(var1, var3, 9, 7, 2, 9, 9, 2, false, var2, k);
            this.a(var1, var3, 2, 7, 12, 2, 9, 12, false, var2, k);
            this.a(var1, var3, 9, 7, 12, 9, 9, 12, false, var2, k);
            this.a(var1, var3, 4, 9, 4, 4, 9, 4, false, var2, k);
            this.a(var1, var3, 7, 9, 4, 7, 9, 4, false, var2, k);
            this.a(var1, var3, 4, 9, 10, 4, 9, 10, false, var2, k);
            this.a(var1, var3, 7, 9, 10, 7, 9, 10, false, var2, k);
            this.a(var1, var3, 5, 9, 7, 6, 9, 7, false, var2, k);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 5, 9, 6, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 6, 9, 6, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var5), 5, 9, 8, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var5), 6, 9, 8, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 4, 0, 0, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 5, 0, 0, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 6, 0, 0, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 7, 0, 0, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 4, 1, 8, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 4, 2, 9, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 4, 3, 10, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 7, 1, 8, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 7, 2, 9, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var4), 7, 3, 10, var3);
            this.a(var1, var3, 4, 1, 9, 4, 1, 9, false, var2, k);
            this.a(var1, var3, 7, 1, 9, 7, 1, 9, false, var2, k);
            this.a(var1, var3, 4, 1, 10, 7, 2, 10, false, var2, k);
            this.a(var1, var3, 5, 4, 5, 6, 4, 5, false, var2, k);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var6), 4, 4, 5, var3);
            this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var7), 7, 4, 5, var3);

            for(var8 = 0; var8 < 4; ++var8) {
               this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var5), 5, 0 - var8, 6 + var8, var3);
               this.a(var1, Blocks.STONE_STAIRS.fromLegacyData(var5), 6, 0 - var8, 6 + var8, var3);
               this.a(var1, var3, 5, 0 - var8, 7 + var8, 6, 0 - var8, 9 + var8);
            }

            this.a(var1, var3, 1, -3, 12, 10, -1, 13);
            this.a(var1, var3, 1, -3, 1, 3, -1, 13);
            this.a(var1, var3, 1, -3, 1, 9, -1, 5);

            for(var8 = 1; var8 <= 13; var8 += 2) {
               this.a(var1, var3, 1, -3, var8, 1, -2, var8, false, var2, k);
            }

            for(var8 = 2; var8 <= 12; var8 += 2) {
               this.a(var1, var3, 1, -1, var8, 3, -1, var8, false, var2, k);
            }

            this.a(var1, var3, 2, -2, 1, 5, -2, 1, false, var2, k);
            this.a(var1, var3, 7, -2, 1, 9, -2, 1, false, var2, k);
            this.a(var1, var3, 6, -3, 1, 6, -3, 1, false, var2, k);
            this.a(var1, var3, 6, -1, 1, 6, -1, 1, false, var2, k);
            this.a(var1, Blocks.TRIPWIRE_HOOK.fromLegacyData(this.a(Blocks.TRIPWIRE_HOOK, EnumDirection.EAST.b())).set(BlockTripwireHook.ATTACHED, Boolean.valueOf(true)), 1, -3, 8, var3);
            this.a(var1, Blocks.TRIPWIRE_HOOK.fromLegacyData(this.a(Blocks.TRIPWIRE_HOOK, EnumDirection.WEST.b())).set(BlockTripwireHook.ATTACHED, Boolean.valueOf(true)), 4, -3, 8, var3);
            this.a(var1, Blocks.TRIPWIRE.getBlockData().set(BlockTripwire.ATTACHED, Boolean.valueOf(true)), 2, -3, 8, var3);
            this.a(var1, Blocks.TRIPWIRE.getBlockData().set(BlockTripwire.ATTACHED, Boolean.valueOf(true)), 3, -3, 8, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 5, -3, 7, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 5, -3, 6, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 5, -3, 5, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 5, -3, 4, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 5, -3, 3, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 5, -3, 2, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 5, -3, 1, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 4, -3, 1, var3);
            this.a(var1, Blocks.MOSSY_COBBLESTONE.getBlockData(), 3, -3, 1, var3);
            if(!this.g) {
               this.g = this.a(var1, var3, var2, 3, -2, 1, EnumDirection.NORTH.a(), j, 2);
            }

            this.a(var1, Blocks.VINE.fromLegacyData(15), 3, -2, 2, var3);
            this.a(var1, Blocks.TRIPWIRE_HOOK.fromLegacyData(this.a(Blocks.TRIPWIRE_HOOK, EnumDirection.NORTH.b())).set(BlockTripwireHook.ATTACHED, Boolean.valueOf(true)), 7, -3, 1, var3);
            this.a(var1, Blocks.TRIPWIRE_HOOK.fromLegacyData(this.a(Blocks.TRIPWIRE_HOOK, EnumDirection.SOUTH.b())).set(BlockTripwireHook.ATTACHED, Boolean.valueOf(true)), 7, -3, 5, var3);
            this.a(var1, Blocks.TRIPWIRE.getBlockData().set(BlockTripwire.ATTACHED, Boolean.valueOf(true)), 7, -3, 2, var3);
            this.a(var1, Blocks.TRIPWIRE.getBlockData().set(BlockTripwire.ATTACHED, Boolean.valueOf(true)), 7, -3, 3, var3);
            this.a(var1, Blocks.TRIPWIRE.getBlockData().set(BlockTripwire.ATTACHED, Boolean.valueOf(true)), 7, -3, 4, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 8, -3, 6, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 9, -3, 6, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 9, -3, 5, var3);
            this.a(var1, Blocks.MOSSY_COBBLESTONE.getBlockData(), 9, -3, 4, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 9, -2, 4, var3);
            if(!this.h) {
               this.h = this.a(var1, var3, var2, 9, -2, 3, EnumDirection.WEST.a(), j, 2);
            }

            this.a(var1, Blocks.VINE.fromLegacyData(15), 8, -1, 3, var3);
            this.a(var1, Blocks.VINE.fromLegacyData(15), 8, -2, 3, var3);
            if(!this.e) {
               this.e = this.a(var1, var3, var2, 8, -3, 3, StructurePieceTreasure.a(i, new StructurePieceTreasure[]{Items.ENCHANTED_BOOK.b(var2)}), 2 + var2.nextInt(5));
            }

            this.a(var1, Blocks.MOSSY_COBBLESTONE.getBlockData(), 9, -3, 2, var3);
            this.a(var1, Blocks.MOSSY_COBBLESTONE.getBlockData(), 8, -3, 1, var3);
            this.a(var1, Blocks.MOSSY_COBBLESTONE.getBlockData(), 4, -3, 5, var3);
            this.a(var1, Blocks.MOSSY_COBBLESTONE.getBlockData(), 5, -2, 5, var3);
            this.a(var1, Blocks.MOSSY_COBBLESTONE.getBlockData(), 5, -1, 5, var3);
            this.a(var1, Blocks.MOSSY_COBBLESTONE.getBlockData(), 6, -3, 5, var3);
            this.a(var1, Blocks.MOSSY_COBBLESTONE.getBlockData(), 7, -2, 5, var3);
            this.a(var1, Blocks.MOSSY_COBBLESTONE.getBlockData(), 7, -1, 5, var3);
            this.a(var1, Blocks.MOSSY_COBBLESTONE.getBlockData(), 8, -3, 5, var3);
            this.a(var1, var3, 9, -1, 1, 9, -1, 5, false, var2, k);
            this.a(var1, var3, 8, -3, 8, 10, -1, 10);
            this.a(var1, Blocks.STONEBRICK.fromLegacyData(BlockSmoothBrick.P), 8, -2, 11, var3);
            this.a(var1, Blocks.STONEBRICK.fromLegacyData(BlockSmoothBrick.P), 9, -2, 11, var3);
            this.a(var1, Blocks.STONEBRICK.fromLegacyData(BlockSmoothBrick.P), 10, -2, 11, var3);
            this.a(var1, Blocks.LEVER.fromLegacyData(BlockLever.a(EnumDirection.fromType1(this.a(Blocks.LEVER, EnumDirection.NORTH.a())))), 8, -2, 12, var3);
            this.a(var1, Blocks.LEVER.fromLegacyData(BlockLever.a(EnumDirection.fromType1(this.a(Blocks.LEVER, EnumDirection.NORTH.a())))), 9, -2, 12, var3);
            this.a(var1, Blocks.LEVER.fromLegacyData(BlockLever.a(EnumDirection.fromType1(this.a(Blocks.LEVER, EnumDirection.NORTH.a())))), 10, -2, 12, var3);
            this.a(var1, var3, 8, -3, 8, 8, -3, 10, false, var2, k);
            this.a(var1, var3, 10, -3, 8, 10, -3, 10, false, var2, k);
            this.a(var1, Blocks.MOSSY_COBBLESTONE.getBlockData(), 10, -2, 9, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 8, -2, 9, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 8, -2, 10, var3);
            this.a(var1, Blocks.REDSTONE_WIRE.getBlockData(), 10, -1, 9, var3);
            this.a(var1, Blocks.STICKY_PISTON.fromLegacyData(EnumDirection.UP.a()), 9, -2, 8, var3);
            this.a(var1, Blocks.STICKY_PISTON.fromLegacyData(this.a(Blocks.STICKY_PISTON, EnumDirection.WEST.a())), 10, -2, 8, var3);
            this.a(var1, Blocks.STICKY_PISTON.fromLegacyData(this.a(Blocks.STICKY_PISTON, EnumDirection.WEST.a())), 10, -1, 8, var3);
            this.a(var1, Blocks.UNPOWERED_REPEATER.fromLegacyData(this.a(Blocks.UNPOWERED_REPEATER, EnumDirection.NORTH.b())), 10, -2, 10, var3);
            if(!this.f) {
               this.f = this.a(var1, var3, var2, 9, -3, 10, StructurePieceTreasure.a(i, new StructurePieceTreasure[]{Items.ENCHANTED_BOOK.b(var2)}), 2 + var2.nextInt(5));
            }

            return true;
         }
      }

      static {
         i = Lists.newArrayList((Object[])(new StructurePieceTreasure[]{new StructurePieceTreasure(Items.DIAMOND, 0, 1, 3, 3), new StructurePieceTreasure(Items.IRON_INGOT, 0, 1, 5, 10), new StructurePieceTreasure(Items.GOLD_INGOT, 0, 2, 7, 15), new StructurePieceTreasure(Items.EMERALD, 0, 1, 3, 2), new StructurePieceTreasure(Items.BONE, 0, 4, 6, 20), new StructurePieceTreasure(Items.ROTTEN_FLESH, 0, 3, 7, 16), new StructurePieceTreasure(Items.SADDLE, 0, 1, 1, 3), new StructurePieceTreasure(Items.IRON_HORSE_ARMOR, 0, 1, 1, 1), new StructurePieceTreasure(Items.GOLDEN_HORSE_ARMOR, 0, 1, 1, 1), new StructurePieceTreasure(Items.DIAMOND_HORSE_ARMOR, 0, 1, 1, 1)}));
         j = Lists.newArrayList((Object[])(new StructurePieceTreasure[]{new StructurePieceTreasure(Items.ARROW, 0, 2, 7, 30)}));
         k = new WorldGenRegistration.WorldGenJungleTemple.WorldGenJungleTemple$WorldGenJungleTemplePiece((WorldGenRegistration.SyntheticClass_1)null);
      }

      static class WorldGenJungleTemple$WorldGenJungleTemplePiece extends StructurePiece.StructurePieceBlockSelector {
         private WorldGenJungleTemple$WorldGenJungleTemplePiece() {
         }

         public void a(Random var1, int var2, int var3, int var4, boolean var5) {
            if(var1.nextFloat() < 0.4F) {
               this.a = Blocks.COBBLESTONE.getBlockData();
            } else {
               this.a = Blocks.MOSSY_COBBLESTONE.getBlockData();
            }

         }

         // $FF: synthetic method
         WorldGenJungleTemple$WorldGenJungleTemplePiece(WorldGenRegistration.SyntheticClass_1 var1) {
            this();
         }
      }
   }

   public static class WorldGenPyramidPiece extends WorldGenRegistration.WorldGenScatteredPiece {
      private boolean[] e = new boolean[4];
      private static final List<StructurePieceTreasure> f;

      public WorldGenPyramidPiece() {
      }

      public WorldGenPyramidPiece(Random var1, int var2, int var3) {
         super(var1, var2, 64, var3, 21, 15, 21);
      }

      protected void a(NBTTagCompound var1) {
         super.a(var1);
         var1.setBoolean("hasPlacedChest0", this.e[0]);
         var1.setBoolean("hasPlacedChest1", this.e[1]);
         var1.setBoolean("hasPlacedChest2", this.e[2]);
         var1.setBoolean("hasPlacedChest3", this.e[3]);
      }

      protected void b(NBTTagCompound var1) {
         super.b(var1);
         this.e[0] = var1.getBoolean("hasPlacedChest0");
         this.e[1] = var1.getBoolean("hasPlacedChest1");
         this.e[2] = var1.getBoolean("hasPlacedChest2");
         this.e[3] = var1.getBoolean("hasPlacedChest3");
      }

      public boolean a(World var1, Random var2, StructureBoundingBox var3) {
         this.a(var1, var3, 0, -4, 0, this.a - 1, 0, this.c - 1, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);

         int var4;
         for(var4 = 1; var4 <= 9; ++var4) {
            this.a(var1, var3, var4, var4, var4, this.a - 1 - var4, var4, this.c - 1 - var4, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
            this.a(var1, var3, var4 + 1, var4, var4 + 1, this.a - 2 - var4, var4, this.c - 2 - var4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
         }

         int var5;
         for(var4 = 0; var4 < this.a; ++var4) {
            for(var5 = 0; var5 < this.c; ++var5) {
               byte var6 = -5;
               this.b(var1, Blocks.SANDSTONE.getBlockData(), var4, var6, var5, var3);
            }
         }

         var4 = this.a(Blocks.SANDSTONE_STAIRS, 3);
         var5 = this.a(Blocks.SANDSTONE_STAIRS, 2);
         int var14 = this.a(Blocks.SANDSTONE_STAIRS, 0);
         int var7 = this.a(Blocks.SANDSTONE_STAIRS, 1);
         int var8 = ~EnumColor.ORANGE.getInvColorIndex() & 15;
         int var9 = ~EnumColor.BLUE.getInvColorIndex() & 15;
         this.a(var1, var3, 0, 0, 0, 4, 9, 4, Blocks.SANDSTONE.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, var3, 1, 10, 1, 3, 10, 3, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var4), 2, 10, 0, var3);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var5), 2, 10, 4, var3);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var14), 0, 10, 2, var3);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var7), 4, 10, 2, var3);
         this.a(var1, var3, this.a - 5, 0, 0, this.a - 1, 9, 4, Blocks.SANDSTONE.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, var3, this.a - 4, 10, 1, this.a - 2, 10, 3, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var4), this.a - 3, 10, 0, var3);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var5), this.a - 3, 10, 4, var3);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var14), this.a - 5, 10, 2, var3);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var7), this.a - 1, 10, 2, var3);
         this.a(var1, var3, 8, 0, 0, 12, 4, 4, Blocks.SANDSTONE.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, var3, 9, 1, 0, 11, 3, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), 9, 1, 1, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), 9, 2, 1, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), 9, 3, 1, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), 10, 3, 1, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), 11, 3, 1, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), 11, 2, 1, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), 11, 1, 1, var3);
         this.a(var1, var3, 4, 1, 1, 8, 3, 3, Blocks.SANDSTONE.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, var3, 4, 1, 2, 8, 2, 2, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, var3, 12, 1, 1, 16, 3, 3, Blocks.SANDSTONE.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, var3, 12, 1, 2, 16, 2, 2, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, var3, 5, 4, 5, this.a - 6, 4, this.c - 6, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
         this.a(var1, var3, 9, 4, 9, 11, 4, 11, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, var3, 8, 1, 8, 8, 3, 8, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), false);
         this.a(var1, var3, 12, 1, 8, 12, 3, 8, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), false);
         this.a(var1, var3, 8, 1, 12, 8, 3, 12, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), false);
         this.a(var1, var3, 12, 1, 12, 12, 3, 12, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), false);
         this.a(var1, var3, 1, 1, 5, 4, 4, 11, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
         this.a(var1, var3, this.a - 5, 1, 5, this.a - 2, 4, 11, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
         this.a(var1, var3, 6, 7, 9, 6, 7, 11, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
         this.a(var1, var3, this.a - 7, 7, 9, this.a - 7, 7, 11, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
         this.a(var1, var3, 5, 5, 9, 5, 7, 11, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), false);
         this.a(var1, var3, this.a - 6, 5, 9, this.a - 6, 7, 11, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), false);
         this.a(var1, Blocks.AIR.getBlockData(), 5, 5, 10, var3);
         this.a(var1, Blocks.AIR.getBlockData(), 5, 6, 10, var3);
         this.a(var1, Blocks.AIR.getBlockData(), 6, 6, 10, var3);
         this.a(var1, Blocks.AIR.getBlockData(), this.a - 6, 5, 10, var3);
         this.a(var1, Blocks.AIR.getBlockData(), this.a - 6, 6, 10, var3);
         this.a(var1, Blocks.AIR.getBlockData(), this.a - 7, 6, 10, var3);
         this.a(var1, var3, 2, 4, 4, 2, 6, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, var3, this.a - 3, 4, 4, this.a - 3, 6, 4, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var4), 2, 4, 5, var3);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var4), 2, 3, 4, var3);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var4), this.a - 3, 4, 5, var3);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var4), this.a - 3, 3, 4, var3);
         this.a(var1, var3, 1, 1, 3, 2, 2, 3, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
         this.a(var1, var3, this.a - 3, 1, 3, this.a - 2, 2, 3, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
         this.a(var1, Blocks.SANDSTONE_STAIRS.getBlockData(), 1, 1, 2, var3);
         this.a(var1, Blocks.SANDSTONE_STAIRS.getBlockData(), this.a - 2, 1, 2, var3);
         this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.SAND.a()), 1, 2, 2, var3);
         this.a(var1, Blocks.STONE_SLAB.fromLegacyData(BlockDoubleStepAbstract.EnumStoneSlabVariant.SAND.a()), this.a - 2, 2, 2, var3);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var7), 2, 1, 2, var3);
         this.a(var1, Blocks.SANDSTONE_STAIRS.fromLegacyData(var14), this.a - 3, 1, 2, var3);
         this.a(var1, var3, 4, 3, 5, 4, 3, 18, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
         this.a(var1, var3, this.a - 5, 3, 5, this.a - 5, 3, 17, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
         this.a(var1, var3, 3, 1, 5, 4, 2, 16, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, var3, this.a - 6, 1, 5, this.a - 5, 2, 16, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);

         int var10;
         for(var10 = 5; var10 <= 17; var10 += 2) {
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), 4, 1, var10, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), 4, 2, var10, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), this.a - 5, 1, var10, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), this.a - 5, 2, var10, var3);
         }

         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 10, 0, 7, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 10, 0, 8, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 9, 0, 9, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 11, 0, 9, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 8, 0, 10, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 12, 0, 10, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 7, 0, 10, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 13, 0, 10, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 9, 0, 11, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 11, 0, 11, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 10, 0, 12, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 10, 0, 13, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var9), 10, 0, 10, var3);

         for(var10 = 0; var10 <= this.a - 1; var10 += this.a - 1) {
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10, 2, 1, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 2, 2, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10, 2, 3, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10, 3, 1, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 3, 2, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10, 3, 3, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 4, 1, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), var10, 4, 2, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 4, 3, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10, 5, 1, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 5, 2, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10, 5, 3, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 6, 1, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), var10, 6, 2, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 6, 3, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 7, 1, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 7, 2, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 7, 3, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10, 8, 1, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10, 8, 2, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10, 8, 3, var3);
         }

         for(var10 = 2; var10 <= this.a - 3; var10 += this.a - 3 - 2) {
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10 - 1, 2, 0, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 2, 0, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10 + 1, 2, 0, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10 - 1, 3, 0, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 3, 0, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10 + 1, 3, 0, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10 - 1, 4, 0, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), var10, 4, 0, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10 + 1, 4, 0, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10 - 1, 5, 0, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 5, 0, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10 + 1, 5, 0, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10 - 1, 6, 0, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), var10, 6, 0, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10 + 1, 6, 0, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10 - 1, 7, 0, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10, 7, 0, var3);
            this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), var10 + 1, 7, 0, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10 - 1, 8, 0, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10, 8, 0, var3);
            this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), var10 + 1, 8, 0, var3);
         }

         this.a(var1, var3, 8, 4, 0, 12, 6, 0, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), false);
         this.a(var1, Blocks.AIR.getBlockData(), 8, 6, 0, var3);
         this.a(var1, Blocks.AIR.getBlockData(), 12, 6, 0, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 9, 5, 0, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), 10, 5, 0, var3);
         this.a(var1, Blocks.STAINED_HARDENED_CLAY.fromLegacyData(var8), 11, 5, 0, var3);
         this.a(var1, var3, 8, -14, 8, 12, -11, 12, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), false);
         this.a(var1, var3, 8, -10, 8, 12, -10, 12, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), false);
         this.a(var1, var3, 8, -9, 8, 12, -9, 12, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), false);
         this.a(var1, var3, 8, -8, 8, 12, -1, 12, Blocks.SANDSTONE.getBlockData(), Blocks.SANDSTONE.getBlockData(), false);
         this.a(var1, var3, 9, -11, 9, 11, -1, 11, Blocks.AIR.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, Blocks.STONE_PRESSURE_PLATE.getBlockData(), 10, -11, 10, var3);
         this.a(var1, var3, 9, -13, 9, 11, -13, 11, Blocks.TNT.getBlockData(), Blocks.AIR.getBlockData(), false);
         this.a(var1, Blocks.AIR.getBlockData(), 8, -11, 10, var3);
         this.a(var1, Blocks.AIR.getBlockData(), 8, -10, 10, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), 7, -10, 10, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), 7, -11, 10, var3);
         this.a(var1, Blocks.AIR.getBlockData(), 12, -11, 10, var3);
         this.a(var1, Blocks.AIR.getBlockData(), 12, -10, 10, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), 13, -10, 10, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), 13, -11, 10, var3);
         this.a(var1, Blocks.AIR.getBlockData(), 10, -11, 8, var3);
         this.a(var1, Blocks.AIR.getBlockData(), 10, -10, 8, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), 10, -10, 7, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), 10, -11, 7, var3);
         this.a(var1, Blocks.AIR.getBlockData(), 10, -11, 12, var3);
         this.a(var1, Blocks.AIR.getBlockData(), 10, -10, 12, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.CHISELED.a()), 10, -10, 13, var3);
         this.a(var1, Blocks.SANDSTONE.fromLegacyData(BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), 10, -11, 13, var3);
         Iterator var15 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

         while(var15.hasNext()) {
            EnumDirection var11 = (EnumDirection)var15.next();
            if(!this.e[var11.b()]) {
               int var12 = var11.getAdjacentX() * 2;
               int var13 = var11.getAdjacentZ() * 2;
               this.e[var11.b()] = this.a(var1, var3, var2, 10 + var12, -11, 10 + var13, StructurePieceTreasure.a(f, new StructurePieceTreasure[]{Items.ENCHANTED_BOOK.b(var2)}), 2 + var2.nextInt(5));
            }
         }

         return true;
      }

      static {
         f = Lists.newArrayList((Object[])(new StructurePieceTreasure[]{new StructurePieceTreasure(Items.DIAMOND, 0, 1, 3, 3), new StructurePieceTreasure(Items.IRON_INGOT, 0, 1, 5, 10), new StructurePieceTreasure(Items.GOLD_INGOT, 0, 2, 7, 15), new StructurePieceTreasure(Items.EMERALD, 0, 1, 3, 2), new StructurePieceTreasure(Items.BONE, 0, 4, 6, 20), new StructurePieceTreasure(Items.ROTTEN_FLESH, 0, 3, 7, 16), new StructurePieceTreasure(Items.SADDLE, 0, 1, 1, 3), new StructurePieceTreasure(Items.IRON_HORSE_ARMOR, 0, 1, 1, 1), new StructurePieceTreasure(Items.GOLDEN_HORSE_ARMOR, 0, 1, 1, 1), new StructurePieceTreasure(Items.DIAMOND_HORSE_ARMOR, 0, 1, 1, 1)}));
      }
   }

   abstract static class WorldGenScatteredPiece extends StructurePiece {
      protected int a;
      protected int b;
      protected int c;
      protected int d = -1;

      public WorldGenScatteredPiece() {
      }

      protected WorldGenScatteredPiece(Random var1, int var2, int var3, int var4, int var5, int var6, int var7) {
         super(0);
         this.a = var5;
         this.b = var6;
         this.c = var7;
         this.m = EnumDirection.EnumDirectionLimit.HORIZONTAL.a(var1);
         switch(WorldGenRegistration.SyntheticClass_1.a[this.m.ordinal()]) {
         case 1:
         case 2:
            this.l = new StructureBoundingBox(var2, var3, var4, var2 + var5 - 1, var3 + var6 - 1, var4 + var7 - 1);
            break;
         default:
            this.l = new StructureBoundingBox(var2, var3, var4, var2 + var7 - 1, var3 + var6 - 1, var4 + var5 - 1);
         }

      }

      protected void a(NBTTagCompound var1) {
         var1.setInt("Width", this.a);
         var1.setInt("Height", this.b);
         var1.setInt("Depth", this.c);
         var1.setInt("HPos", this.d);
      }

      protected void b(NBTTagCompound var1) {
         this.a = var1.getInt("Width");
         this.b = var1.getInt("Height");
         this.c = var1.getInt("Depth");
         this.d = var1.getInt("HPos");
      }

      protected boolean a(World var1, StructureBoundingBox var2, int var3) {
         if(this.d >= 0) {
            return true;
         } else {
            int var4 = 0;
            int var5 = 0;
            BlockPosition.MutableBlockPosition var6 = new BlockPosition.MutableBlockPosition();

            for(int var7 = this.l.c; var7 <= this.l.f; ++var7) {
               for(int var8 = this.l.a; var8 <= this.l.d; ++var8) {
                  var6.c(var8, 64, var7);
                  if(var2.b((BaseBlockPosition)var6)) {
                     var4 += Math.max(var1.r(var6).getY(), var1.worldProvider.getSeaLevel());
                     ++var5;
                  }
               }
            }

            if(var5 == 0) {
               return false;
            } else {
               this.d = var4 / var5;
               this.l.a(0, this.d - this.l.b + var3, 0);
               return true;
            }
         }
      }
   }
}
