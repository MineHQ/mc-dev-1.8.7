package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.IBlockData;
import net.minecraft.server.World;

public abstract class WorldGenerator {
   private final boolean a;

   public WorldGenerator() {
      this(false);
   }

   public WorldGenerator(boolean var1) {
      this.a = var1;
   }

   public abstract boolean generate(World var1, Random var2, BlockPosition var3);

   public void e() {
   }

   protected void a(World var1, BlockPosition var2, IBlockData var3) {
      if(this.a) {
         var1.setTypeAndData(var2, var3, 3);
      } else {
         var1.setTypeAndData(var2, var3, 2);
      }

   }
}
