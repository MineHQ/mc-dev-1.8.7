package net.minecraft.server;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDirectional;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.INamable;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockBed extends BlockDirectional {
   public static final BlockStateEnum<BlockBed.EnumBedPart> PART = BlockStateEnum.of("part", BlockBed.EnumBedPart.class);
   public static final BlockStateBoolean OCCUPIED = BlockStateBoolean.of("occupied");

   public BlockBed() {
      super(Material.CLOTH);
      this.j(this.blockStateList.getBlockData().set(PART, BlockBed.EnumBedPart.FOOT).set(OCCUPIED, Boolean.valueOf(false)));
      this.l();
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.isClientSide) {
         return true;
      } else {
         if(var3.get(PART) != BlockBed.EnumBedPart.HEAD) {
            var2 = var2.shift((EnumDirection)var3.get(FACING));
            var3 = var1.getType(var2);
            if(var3.getBlock() != this) {
               return true;
            }
         }

         if(var1.worldProvider.e() && var1.getBiome(var2) != BiomeBase.HELL) {
            if(((Boolean)var3.get(OCCUPIED)).booleanValue()) {
               EntityHuman var10 = this.f(var1, var2);
               if(var10 != null) {
                  var4.b((IChatBaseComponent)(new ChatMessage("tile.bed.occupied", new Object[0])));
                  return true;
               }

               var3 = var3.set(OCCUPIED, Boolean.valueOf(false));
               var1.setTypeAndData(var2, var3, 4);
            }

            EntityHuman.EnumBedResult var11 = var4.a(var2);
            if(var11 == EntityHuman.EnumBedResult.OK) {
               var3 = var3.set(OCCUPIED, Boolean.valueOf(true));
               var1.setTypeAndData(var2, var3, 4);
               return true;
            } else {
               if(var11 == EntityHuman.EnumBedResult.NOT_POSSIBLE_NOW) {
                  var4.b((IChatBaseComponent)(new ChatMessage("tile.bed.noSleep", new Object[0])));
               } else if(var11 == EntityHuman.EnumBedResult.NOT_SAFE) {
                  var4.b((IChatBaseComponent)(new ChatMessage("tile.bed.notSafe", new Object[0])));
               }

               return true;
            }
         } else {
            var1.setAir(var2);
            BlockPosition var9 = var2.shift(((EnumDirection)var3.get(FACING)).opposite());
            if(var1.getType(var9).getBlock() == this) {
               var1.setAir(var9);
            }

            var1.createExplosion((Entity)null, (double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, 5.0F, true, true);
            return true;
         }
      }
   }

   private EntityHuman f(World var1, BlockPosition var2) {
      Iterator var3 = var1.players.iterator();

      EntityHuman var4;
      do {
         if(!var3.hasNext()) {
            return null;
         }

         var4 = (EntityHuman)var3.next();
      } while(!var4.isSleeping() || !var4.bx.equals(var2));

      return var4;
   }

   public boolean d() {
      return false;
   }

   public boolean c() {
      return false;
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      this.l();
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      EnumDirection var5 = (EnumDirection)var3.get(FACING);
      if(var3.get(PART) == BlockBed.EnumBedPart.HEAD) {
         if(var1.getType(var2.shift(var5.opposite())).getBlock() != this) {
            var1.setAir(var2);
         }
      } else if(var1.getType(var2.shift(var5)).getBlock() != this) {
         var1.setAir(var2);
         if(!var1.isClientSide) {
            this.b(var1, var2, var3, 0);
         }
      }

   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return var1.get(PART) == BlockBed.EnumBedPart.HEAD?null:Items.BED;
   }

   private void l() {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
   }

   public static BlockPosition a(World var0, BlockPosition var1, int var2) {
      EnumDirection var3 = (EnumDirection)var0.getType(var1).get(FACING);
      int var4 = var1.getX();
      int var5 = var1.getY();
      int var6 = var1.getZ();

      for(int var7 = 0; var7 <= 1; ++var7) {
         int var8 = var4 - var3.getAdjacentX() * var7 - 1;
         int var9 = var6 - var3.getAdjacentZ() * var7 - 1;
         int var10 = var8 + 2;
         int var11 = var9 + 2;

         for(int var12 = var8; var12 <= var10; ++var12) {
            for(int var13 = var9; var13 <= var11; ++var13) {
               BlockPosition var14 = new BlockPosition(var12, var5, var13);
               if(e(var0, var14)) {
                  if(var2 <= 0) {
                     return var14;
                  }

                  --var2;
               }
            }
         }
      }

      return null;
   }

   protected static boolean e(World var0, BlockPosition var1) {
      return World.a((IBlockAccess)var0, (BlockPosition)var1.down()) && !var0.getType(var1).getBlock().getMaterial().isBuildable() && !var0.getType(var1.up()).getBlock().getMaterial().isBuildable();
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
      if(var3.get(PART) == BlockBed.EnumBedPart.FOOT) {
         super.dropNaturally(var1, var2, var3, var4, 0);
      }

   }

   public int k() {
      return 1;
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4) {
      if(var4.abilities.canInstantlyBuild && var3.get(PART) == BlockBed.EnumBedPart.HEAD) {
         BlockPosition var5 = var2.shift(((EnumDirection)var3.get(FACING)).opposite());
         if(var1.getType(var5).getBlock() == this) {
            var1.setAir(var5);
         }
      }

   }

   public IBlockData fromLegacyData(int var1) {
      EnumDirection var2 = EnumDirection.fromType2(var1);
      return (var1 & 8) > 0?this.getBlockData().set(PART, BlockBed.EnumBedPart.HEAD).set(FACING, var2).set(OCCUPIED, Boolean.valueOf((var1 & 4) > 0)):this.getBlockData().set(PART, BlockBed.EnumBedPart.FOOT).set(FACING, var2);
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      if(var1.get(PART) == BlockBed.EnumBedPart.FOOT) {
         IBlockData var4 = var2.getType(var3.shift((EnumDirection)var1.get(FACING)));
         if(var4.getBlock() == this) {
            var1 = var1.set(OCCUPIED, var4.get(OCCUPIED));
         }
      }

      return var1;
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumDirection)var1.get(FACING)).b();
      if(var1.get(PART) == BlockBed.EnumBedPart.HEAD) {
         var3 |= 8;
         if(((Boolean)var1.get(OCCUPIED)).booleanValue()) {
            var3 |= 4;
         }
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, PART, OCCUPIED});
   }

   public static enum EnumBedPart implements INamable {
      HEAD("head"),
      FOOT("foot");

      private final String c;

      private EnumBedPart(String var3) {
         this.c = var3;
      }

      public String toString() {
         return this.c;
      }

      public String getName() {
         return this.c;
      }
   }
}
