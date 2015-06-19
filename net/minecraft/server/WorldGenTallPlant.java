package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockTallPlant;
import net.minecraft.server.Blocks;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenerator;

public class WorldGenTallPlant extends WorldGenerator {
   private BlockTallPlant.EnumTallFlowerVariants a;

   public WorldGenTallPlant() {
   }

   public void a(BlockTallPlant.EnumTallFlowerVariants var1) {
      this.a = var1;
   }

   public boolean generate(World var1, Random var2, BlockPosition var3) {
      boolean var4 = false;

      for(int var5 = 0; var5 < 64; ++var5) {
         BlockPosition var6 = var3.a(var2.nextInt(8) - var2.nextInt(8), var2.nextInt(4) - var2.nextInt(4), var2.nextInt(8) - var2.nextInt(8));
         if(var1.isEmpty(var6) && (!var1.worldProvider.o() || var6.getY() < 254) && Blocks.DOUBLE_PLANT.canPlace(var1, var6)) {
            Blocks.DOUBLE_PLANT.a(var1, var6, this.a, 2);
            var4 = true;
         }
      }

      return var4;
   }
}
