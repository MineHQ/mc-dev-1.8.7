package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockVine;
import net.minecraft.server.Blocks;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenerator;

public class WorldGenVines extends WorldGenerator {
   public WorldGenVines() {
   }

   public boolean generate(World var1, Random var2, BlockPosition var3) {
      for(; var3.getY() < 128; var3 = var3.up()) {
         if(var1.isEmpty(var3)) {
            EnumDirection[] var4 = EnumDirection.EnumDirectionLimit.HORIZONTAL.a();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               EnumDirection var7 = var4[var6];
               if(Blocks.VINE.canPlace(var1, var3, var7)) {
                  IBlockData var8 = Blocks.VINE.getBlockData().set(BlockVine.NORTH, Boolean.valueOf(var7 == EnumDirection.NORTH)).set(BlockVine.EAST, Boolean.valueOf(var7 == EnumDirection.EAST)).set(BlockVine.SOUTH, Boolean.valueOf(var7 == EnumDirection.SOUTH)).set(BlockVine.WEST, Boolean.valueOf(var7 == EnumDirection.WEST));
                  var1.setTypeAndData(var3, var8, 2);
                  break;
               }
            }
         } else {
            var3 = var3.a(var2.nextInt(4) - var2.nextInt(4), 0, var2.nextInt(4) - var2.nextInt(4));
         }
      }

      return true;
   }
}
