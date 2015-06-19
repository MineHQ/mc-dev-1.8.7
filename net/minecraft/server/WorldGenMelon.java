package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenerator;

public class WorldGenMelon extends WorldGenerator {
   public WorldGenMelon() {
   }

   public boolean generate(World var1, Random var2, BlockPosition var3) {
      for(int var4 = 0; var4 < 64; ++var4) {
         BlockPosition var5 = var3.a(var2.nextInt(8) - var2.nextInt(8), var2.nextInt(4) - var2.nextInt(4), var2.nextInt(8) - var2.nextInt(8));
         if(Blocks.MELON_BLOCK.canPlace(var1, var5) && var1.getType(var5.down()).getBlock() == Blocks.GRASS) {
            var1.setTypeAndData(var5, Blocks.MELON_BLOCK.getBlockData(), 2);
         }
      }

      return true;
   }
}
