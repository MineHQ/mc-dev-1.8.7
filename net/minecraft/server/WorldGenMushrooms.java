package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockPlant;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenerator;

public class WorldGenMushrooms extends WorldGenerator {
   private BlockPlant a;

   public WorldGenMushrooms(BlockPlant var1) {
      this.a = var1;
   }

   public boolean generate(World var1, Random var2, BlockPosition var3) {
      for(int var4 = 0; var4 < 64; ++var4) {
         BlockPosition var5 = var3.a(var2.nextInt(8) - var2.nextInt(8), var2.nextInt(4) - var2.nextInt(4), var2.nextInt(8) - var2.nextInt(8));
         if(var1.isEmpty(var5) && (!var1.worldProvider.o() || var5.getY() < 255) && this.a.f(var1, var5, this.a.getBlockData())) {
            var1.setTypeAndData(var5, this.a.getBlockData(), 2);
         }
      }

      return true;
   }
}
