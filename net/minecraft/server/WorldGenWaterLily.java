package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenerator;

public class WorldGenWaterLily extends WorldGenerator {
   public WorldGenWaterLily() {
   }

   public boolean generate(World var1, Random var2, BlockPosition var3) {
      for(int var4 = 0; var4 < 10; ++var4) {
         int var5 = var3.getX() + var2.nextInt(8) - var2.nextInt(8);
         int var6 = var3.getY() + var2.nextInt(4) - var2.nextInt(4);
         int var7 = var3.getZ() + var2.nextInt(8) - var2.nextInt(8);
         if(var1.isEmpty(new BlockPosition(var5, var6, var7)) && Blocks.WATERLILY.canPlace(var1, new BlockPosition(var5, var6, var7))) {
            var1.setTypeAndData(new BlockPosition(var5, var6, var7), Blocks.WATERLILY.getBlockData(), 2);
         }
      }

      return true;
   }
}
