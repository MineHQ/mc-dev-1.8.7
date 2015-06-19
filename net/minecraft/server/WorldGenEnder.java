package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityEnderCrystal;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenerator;

public class WorldGenEnder extends WorldGenerator {
   private Block a;

   public WorldGenEnder(Block var1) {
      this.a = var1;
   }

   public boolean generate(World var1, Random var2, BlockPosition var3) {
      if(var1.isEmpty(var3) && var1.getType(var3.down()).getBlock() == this.a) {
         int var4 = var2.nextInt(32) + 6;
         int var5 = var2.nextInt(4) + 1;
         BlockPosition.MutableBlockPosition var6 = new BlockPosition.MutableBlockPosition();

         int var7;
         int var8;
         int var9;
         int var10;
         for(var7 = var3.getX() - var5; var7 <= var3.getX() + var5; ++var7) {
            for(var8 = var3.getZ() - var5; var8 <= var3.getZ() + var5; ++var8) {
               var9 = var7 - var3.getX();
               var10 = var8 - var3.getZ();
               if(var9 * var9 + var10 * var10 <= var5 * var5 + 1 && var1.getType(var6.c(var7, var3.getY() - 1, var8)).getBlock() != this.a) {
                  return false;
               }
            }
         }

         for(var7 = var3.getY(); var7 < var3.getY() + var4 && var7 < 256; ++var7) {
            for(var8 = var3.getX() - var5; var8 <= var3.getX() + var5; ++var8) {
               for(var9 = var3.getZ() - var5; var9 <= var3.getZ() + var5; ++var9) {
                  var10 = var8 - var3.getX();
                  int var11 = var9 - var3.getZ();
                  if(var10 * var10 + var11 * var11 <= var5 * var5 + 1) {
                     var1.setTypeAndData(new BlockPosition(var8, var7, var9), Blocks.OBSIDIAN.getBlockData(), 2);
                  }
               }
            }
         }

         EntityEnderCrystal var12 = new EntityEnderCrystal(var1);
         var12.setPositionRotation((double)((float)var3.getX() + 0.5F), (double)(var3.getY() + var4), (double)((float)var3.getZ() + 0.5F), var2.nextFloat() * 360.0F, 0.0F);
         var1.addEntity(var12);
         var1.setTypeAndData(var3.up(var4), Blocks.BEDROCK.getBlockData(), 2);
         return true;
      } else {
         return false;
      }
   }
}
