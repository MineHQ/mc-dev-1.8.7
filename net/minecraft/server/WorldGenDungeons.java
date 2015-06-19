package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IInventory;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.StructurePieceTreasure;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityChest;
import net.minecraft.server.TileEntityMobSpawner;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldGenDungeons extends WorldGenerator {
   private static final Logger a = LogManager.getLogger();
   private static final String[] b = new String[]{"Skeleton", "Zombie", "Zombie", "Spider"};
   private static final List<StructurePieceTreasure> c;

   public WorldGenDungeons() {
   }

   public boolean generate(World var1, Random var2, BlockPosition var3) {
      boolean var4 = true;
      int var5 = var2.nextInt(2) + 2;
      int var6 = -var5 - 1;
      int var7 = var5 + 1;
      boolean var8 = true;
      boolean var9 = true;
      int var10 = var2.nextInt(2) + 2;
      int var11 = -var10 - 1;
      int var12 = var10 + 1;
      int var13 = 0;

      int var14;
      int var15;
      int var16;
      BlockPosition var17;
      for(var14 = var6; var14 <= var7; ++var14) {
         for(var15 = -1; var15 <= 4; ++var15) {
            for(var16 = var11; var16 <= var12; ++var16) {
               var17 = var3.a(var14, var15, var16);
               Material var18 = var1.getType(var17).getBlock().getMaterial();
               boolean var19 = var18.isBuildable();
               if(var15 == -1 && !var19) {
                  return false;
               }

               if(var15 == 4 && !var19) {
                  return false;
               }

               if((var14 == var6 || var14 == var7 || var16 == var11 || var16 == var12) && var15 == 0 && var1.isEmpty(var17) && var1.isEmpty(var17.up())) {
                  ++var13;
               }
            }
         }
      }

      if(var13 >= 1 && var13 <= 5) {
         for(var14 = var6; var14 <= var7; ++var14) {
            for(var15 = 3; var15 >= -1; --var15) {
               for(var16 = var11; var16 <= var12; ++var16) {
                  var17 = var3.a(var14, var15, var16);
                  if(var14 != var6 && var15 != -1 && var16 != var11 && var14 != var7 && var15 != 4 && var16 != var12) {
                     if(var1.getType(var17).getBlock() != Blocks.CHEST) {
                        var1.setAir(var17);
                     }
                  } else if(var17.getY() >= 0 && !var1.getType(var17.down()).getBlock().getMaterial().isBuildable()) {
                     var1.setAir(var17);
                  } else if(var1.getType(var17).getBlock().getMaterial().isBuildable() && var1.getType(var17).getBlock() != Blocks.CHEST) {
                     if(var15 == -1 && var2.nextInt(4) != 0) {
                        var1.setTypeAndData(var17, Blocks.MOSSY_COBBLESTONE.getBlockData(), 2);
                     } else {
                        var1.setTypeAndData(var17, Blocks.COBBLESTONE.getBlockData(), 2);
                     }
                  }
               }
            }
         }

         for(var14 = 0; var14 < 2; ++var14) {
            for(var15 = 0; var15 < 3; ++var15) {
               var16 = var3.getX() + var2.nextInt(var5 * 2 + 1) - var5;
               int var24 = var3.getY();
               int var25 = var3.getZ() + var2.nextInt(var10 * 2 + 1) - var10;
               BlockPosition var26 = new BlockPosition(var16, var24, var25);
               if(var1.isEmpty(var26)) {
                  int var20 = 0;
                  Iterator var21 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

                  while(var21.hasNext()) {
                     EnumDirection var22 = (EnumDirection)var21.next();
                     if(var1.getType(var26.shift(var22)).getBlock().getMaterial().isBuildable()) {
                        ++var20;
                     }
                  }

                  if(var20 == 1) {
                     var1.setTypeAndData(var26, Blocks.CHEST.f(var1, var26, Blocks.CHEST.getBlockData()), 2);
                     List var27 = StructurePieceTreasure.a(c, new StructurePieceTreasure[]{Items.ENCHANTED_BOOK.b(var2)});
                     TileEntity var28 = var1.getTileEntity(var26);
                     if(var28 instanceof TileEntityChest) {
                        StructurePieceTreasure.a(var2, var27, (IInventory)((TileEntityChest)var28), 8);
                     }
                     break;
                  }
               }
            }
         }

         var1.setTypeAndData(var3, Blocks.MOB_SPAWNER.getBlockData(), 2);
         TileEntity var23 = var1.getTileEntity(var3);
         if(var23 instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner)var23).getSpawner().setMobName(this.a(var2));
         } else {
            a.error("Failed to fetch mob spawner entity at (" + var3.getX() + ", " + var3.getY() + ", " + var3.getZ() + ")");
         }

         return true;
      } else {
         return false;
      }
   }

   private String a(Random var1) {
      return b[var1.nextInt(b.length)];
   }

   static {
      c = Lists.newArrayList((Object[])(new StructurePieceTreasure[]{new StructurePieceTreasure(Items.SADDLE, 0, 1, 1, 10), new StructurePieceTreasure(Items.IRON_INGOT, 0, 1, 4, 10), new StructurePieceTreasure(Items.BREAD, 0, 1, 1, 10), new StructurePieceTreasure(Items.WHEAT, 0, 1, 4, 10), new StructurePieceTreasure(Items.GUNPOWDER, 0, 1, 4, 10), new StructurePieceTreasure(Items.STRING, 0, 1, 4, 10), new StructurePieceTreasure(Items.BUCKET, 0, 1, 1, 10), new StructurePieceTreasure(Items.GOLDEN_APPLE, 0, 1, 1, 1), new StructurePieceTreasure(Items.REDSTONE, 0, 1, 4, 10), new StructurePieceTreasure(Items.RECORD_13, 0, 1, 1, 4), new StructurePieceTreasure(Items.RECORD_CAT, 0, 1, 1, 4), new StructurePieceTreasure(Items.NAME_TAG, 0, 1, 1, 10), new StructurePieceTreasure(Items.GOLDEN_HORSE_ARMOR, 0, 1, 1, 2), new StructurePieceTreasure(Items.IRON_HORSE_ARMOR, 0, 1, 1, 5), new StructurePieceTreasure(Items.DIAMOND_HORSE_ARMOR, 0, 1, 1, 1)}));
   }
}
