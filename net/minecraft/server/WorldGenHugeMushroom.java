package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockHugeMushroom;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Material;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenerator;

public class WorldGenHugeMushroom extends WorldGenerator {
   private Block a;

   public WorldGenHugeMushroom(Block var1) {
      super(true);
      this.a = var1;
   }

   public WorldGenHugeMushroom() {
      super(false);
   }

   public boolean generate(World var1, Random var2, BlockPosition var3) {
      if(this.a == null) {
         this.a = var2.nextBoolean()?Blocks.BROWN_MUSHROOM_BLOCK:Blocks.RED_MUSHROOM_BLOCK;
      }

      int var4 = var2.nextInt(3) + 4;
      boolean var5 = true;
      if(var3.getY() >= 1 && var3.getY() + var4 + 1 < 256) {
         int var9;
         int var10;
         for(int var6 = var3.getY(); var6 <= var3.getY() + 1 + var4; ++var6) {
            byte var7 = 3;
            if(var6 <= var3.getY() + 3) {
               var7 = 0;
            }

            BlockPosition.MutableBlockPosition var8 = new BlockPosition.MutableBlockPosition();

            for(var9 = var3.getX() - var7; var9 <= var3.getX() + var7 && var5; ++var9) {
               for(var10 = var3.getZ() - var7; var10 <= var3.getZ() + var7 && var5; ++var10) {
                  if(var6 >= 0 && var6 < 256) {
                     Block var11 = var1.getType(var8.c(var9, var6, var10)).getBlock();
                     if(var11.getMaterial() != Material.AIR && var11.getMaterial() != Material.LEAVES) {
                        var5 = false;
                     }
                  } else {
                     var5 = false;
                  }
               }
            }
         }

         if(!var5) {
            return false;
         } else {
            Block var19 = var1.getType(var3.down()).getBlock();
            if(var19 != Blocks.DIRT && var19 != Blocks.GRASS && var19 != Blocks.MYCELIUM) {
               return false;
            } else {
               int var20 = var3.getY() + var4;
               if(this.a == Blocks.RED_MUSHROOM_BLOCK) {
                  var20 = var3.getY() + var4 - 3;
               }

               int var21;
               for(var21 = var20; var21 <= var3.getY() + var4; ++var21) {
                  var9 = 1;
                  if(var21 < var3.getY() + var4) {
                     ++var9;
                  }

                  if(this.a == Blocks.BROWN_MUSHROOM_BLOCK) {
                     var9 = 3;
                  }

                  var10 = var3.getX() - var9;
                  int var23 = var3.getX() + var9;
                  int var12 = var3.getZ() - var9;
                  int var13 = var3.getZ() + var9;

                  for(int var14 = var10; var14 <= var23; ++var14) {
                     for(int var15 = var12; var15 <= var13; ++var15) {
                        int var16 = 5;
                        if(var14 == var10) {
                           --var16;
                        } else if(var14 == var23) {
                           ++var16;
                        }

                        if(var15 == var12) {
                           var16 -= 3;
                        } else if(var15 == var13) {
                           var16 += 3;
                        }

                        BlockHugeMushroom.EnumHugeMushroomVariant var17 = BlockHugeMushroom.EnumHugeMushroomVariant.a(var16);
                        if(this.a == Blocks.BROWN_MUSHROOM_BLOCK || var21 < var3.getY() + var4) {
                           if((var14 == var10 || var14 == var23) && (var15 == var12 || var15 == var13)) {
                              continue;
                           }

                           if(var14 == var3.getX() - (var9 - 1) && var15 == var12) {
                              var17 = BlockHugeMushroom.EnumHugeMushroomVariant.NORTH_WEST;
                           }

                           if(var14 == var10 && var15 == var3.getZ() - (var9 - 1)) {
                              var17 = BlockHugeMushroom.EnumHugeMushroomVariant.NORTH_WEST;
                           }

                           if(var14 == var3.getX() + (var9 - 1) && var15 == var12) {
                              var17 = BlockHugeMushroom.EnumHugeMushroomVariant.NORTH_EAST;
                           }

                           if(var14 == var23 && var15 == var3.getZ() - (var9 - 1)) {
                              var17 = BlockHugeMushroom.EnumHugeMushroomVariant.NORTH_EAST;
                           }

                           if(var14 == var3.getX() - (var9 - 1) && var15 == var13) {
                              var17 = BlockHugeMushroom.EnumHugeMushroomVariant.SOUTH_WEST;
                           }

                           if(var14 == var10 && var15 == var3.getZ() + (var9 - 1)) {
                              var17 = BlockHugeMushroom.EnumHugeMushroomVariant.SOUTH_WEST;
                           }

                           if(var14 == var3.getX() + (var9 - 1) && var15 == var13) {
                              var17 = BlockHugeMushroom.EnumHugeMushroomVariant.SOUTH_EAST;
                           }

                           if(var14 == var23 && var15 == var3.getZ() + (var9 - 1)) {
                              var17 = BlockHugeMushroom.EnumHugeMushroomVariant.SOUTH_EAST;
                           }
                        }

                        if(var17 == BlockHugeMushroom.EnumHugeMushroomVariant.CENTER && var21 < var3.getY() + var4) {
                           var17 = BlockHugeMushroom.EnumHugeMushroomVariant.ALL_INSIDE;
                        }

                        if(var3.getY() >= var3.getY() + var4 - 1 || var17 != BlockHugeMushroom.EnumHugeMushroomVariant.ALL_INSIDE) {
                           BlockPosition var18 = new BlockPosition(var14, var21, var15);
                           if(!var1.getType(var18).getBlock().o()) {
                              this.a(var1, var18, this.a.getBlockData().set(BlockHugeMushroom.VARIANT, var17));
                           }
                        }
                     }
                  }
               }

               for(var21 = 0; var21 < var4; ++var21) {
                  Block var22 = var1.getType(var3.up(var21)).getBlock();
                  if(!var22.o()) {
                     this.a(var1, var3.up(var21), this.a.getBlockData().set(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumHugeMushroomVariant.STEM));
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }
}
