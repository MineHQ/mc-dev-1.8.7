package net.minecraft.server;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multisets;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDirt;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStone;
import net.minecraft.server.Blocks;
import net.minecraft.server.Chunk;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IBlockData;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemWorldMapBase;
import net.minecraft.server.Items;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Packet;
import net.minecraft.server.PersistentBase;
import net.minecraft.server.World;
import net.minecraft.server.WorldMap;

public class ItemWorldMap extends ItemWorldMapBase {
   protected ItemWorldMap() {
      this.a(true);
   }

   public WorldMap getSavedMap(ItemStack var1, World var2) {
      String var3 = "map_" + var1.getData();
      WorldMap var4 = (WorldMap)var2.a(WorldMap.class, var3);
      if(var4 == null && !var2.isClientSide) {
         var1.setData(var2.b("map"));
         var3 = "map_" + var1.getData();
         var4 = new WorldMap(var3);
         var4.scale = 3;
         var4.a((double)var2.getWorldData().c(), (double)var2.getWorldData().e(), var4.scale);
         var4.map = (byte)var2.worldProvider.getDimension();
         var4.c();
         var2.a((String)var3, (PersistentBase)var4);
      }

      return var4;
   }

   public void a(World var1, Entity var2, WorldMap var3) {
      if(var1.worldProvider.getDimension() == var3.map && var2 instanceof EntityHuman) {
         int var4 = 1 << var3.scale;
         int var5 = var3.centerX;
         int var6 = var3.centerZ;
         int var7 = MathHelper.floor(var2.locX - (double)var5) / var4 + 64;
         int var8 = MathHelper.floor(var2.locZ - (double)var6) / var4 + 64;
         int var9 = 128 / var4;
         if(var1.worldProvider.o()) {
            var9 /= 2;
         }

         WorldMap.WorldMapHumanTracker var10 = var3.a((EntityHuman)var2);
         ++var10.b;
         boolean var11 = false;

         for(int var12 = var7 - var9 + 1; var12 < var7 + var9; ++var12) {
            if((var12 & 15) == (var10.b & 15) || var11) {
               var11 = false;
               double var13 = 0.0D;

               for(int var15 = var8 - var9 - 1; var15 < var8 + var9; ++var15) {
                  if(var12 >= 0 && var15 >= -1 && var12 < 128 && var15 < 128) {
                     int var16 = var12 - var7;
                     int var17 = var15 - var8;
                     boolean var18 = var16 * var16 + var17 * var17 > (var9 - 2) * (var9 - 2);
                     int var19 = (var5 / var4 + var12 - 64) * var4;
                     int var20 = (var6 / var4 + var15 - 64) * var4;
                     HashMultiset var21 = HashMultiset.create();
                     Chunk var22 = var1.getChunkAtWorldCoords(new BlockPosition(var19, 0, var20));
                     if(!var22.isEmpty()) {
                        int var23 = var19 & 15;
                        int var24 = var20 & 15;
                        int var25 = 0;
                        double var26 = 0.0D;
                        if(var1.worldProvider.o()) {
                           int var28 = var19 + var20 * 231871;
                           var28 = var28 * var28 * 31287121 + var28 * 11;
                           if((var28 >> 20 & 1) == 0) {
                              var21.add(Blocks.DIRT.g(Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.DIRT)), 10);
                           } else {
                              var21.add(Blocks.STONE.g(Blocks.STONE.getBlockData().set(BlockStone.VARIANT, BlockStone.EnumStoneVariant.STONE)), 100);
                           }

                           var26 = 100.0D;
                        } else {
                           BlockPosition.MutableBlockPosition var35 = new BlockPosition.MutableBlockPosition();

                           for(int var29 = 0; var29 < var4; ++var29) {
                              for(int var30 = 0; var30 < var4; ++var30) {
                                 int var31 = var22.b(var29 + var23, var30 + var24) + 1;
                                 IBlockData var32 = Blocks.AIR.getBlockData();
                                 if(var31 > 1) {
                                    do {
                                       --var31;
                                       var32 = var22.getBlockData(var35.c(var29 + var23, var31, var30 + var24));
                                    } while(var32.getBlock().g(var32) == MaterialMapColor.b && var31 > 0);

                                    if(var31 > 0 && var32.getBlock().getMaterial().isLiquid()) {
                                       int var33 = var31 - 1;

                                       Block var34;
                                       do {
                                          var34 = var22.getTypeAbs(var29 + var23, var33--, var30 + var24);
                                          ++var25;
                                       } while(var33 > 0 && var34.getMaterial().isLiquid());
                                    }
                                 }

                                 var26 += (double)var31 / (double)(var4 * var4);
                                 var21.add(var32.getBlock().g(var32));
                              }
                           }
                        }

                        var25 /= var4 * var4;
                        double var36 = (var26 - var13) * 4.0D / (double)(var4 + 4) + ((double)(var12 + var15 & 1) - 0.5D) * 0.4D;
                        byte var37 = 1;
                        if(var36 > 0.6D) {
                           var37 = 2;
                        }

                        if(var36 < -0.6D) {
                           var37 = 0;
                        }

                        MaterialMapColor var38 = (MaterialMapColor)Iterables.getFirst(Multisets.copyHighestCountFirst(var21), MaterialMapColor.b);
                        if(var38 == MaterialMapColor.n) {
                           var36 = (double)var25 * 0.1D + (double)(var12 + var15 & 1) * 0.2D;
                           var37 = 1;
                           if(var36 < 0.5D) {
                              var37 = 2;
                           }

                           if(var36 > 0.9D) {
                              var37 = 0;
                           }
                        }

                        var13 = var26;
                        if(var15 >= 0 && var16 * var16 + var17 * var17 < var9 * var9 && (!var18 || (var12 + var15 & 1) != 0)) {
                           byte var39 = var3.colors[var12 + var15 * 128];
                           byte var40 = (byte)(var38.M * 4 + var37);
                           if(var39 != var40) {
                              var3.colors[var12 + var15 * 128] = var40;
                              var3.flagDirty(var12, var15);
                              var11 = true;
                           }
                        }
                     }
                  }
               }
            }
         }

      }
   }

   public void a(ItemStack var1, World var2, Entity var3, int var4, boolean var5) {
      if(!var2.isClientSide) {
         WorldMap var6 = this.getSavedMap(var1, var2);
         if(var3 instanceof EntityHuman) {
            EntityHuman var7 = (EntityHuman)var3;
            var6.a(var7, var1);
         }

         if(var5) {
            this.a(var2, var3, var6);
         }

      }
   }

   public Packet c(ItemStack var1, World var2, EntityHuman var3) {
      return this.getSavedMap(var1, var2).a(var1, var2, var3);
   }

   public void d(ItemStack var1, World var2, EntityHuman var3) {
      if(var1.hasTag() && var1.getTag().getBoolean("map_is_scaling")) {
         WorldMap var4 = Items.FILLED_MAP.getSavedMap(var1, var2);
         var1.setData(var2.b("map"));
         WorldMap var5 = new WorldMap("map_" + var1.getData());
         var5.scale = (byte)(var4.scale + 1);
         if(var5.scale > 4) {
            var5.scale = 4;
         }

         var5.a((double)var4.centerX, (double)var4.centerZ, var5.scale);
         var5.map = var4.map;
         var5.c();
         var2.a((String)("map_" + var1.getData()), (PersistentBase)var5);
      }

   }
}
