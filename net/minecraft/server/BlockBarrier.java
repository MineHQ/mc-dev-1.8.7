package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockBarrier extends Block {
   protected BlockBarrier() {
      super(Material.BANNER);
      this.x();
      this.b(6000001.0F);
      this.K();
      this.t = true;
   }

   public int b() {
      return -1;
   }

   public boolean c() {
      return false;
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
   }
}
