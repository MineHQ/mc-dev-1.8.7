package net.minecraft.server;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockAir extends Block {
   protected BlockAir() {
      super(Material.AIR);
   }

   public int b() {
      return -1;
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return null;
   }

   public boolean c() {
      return false;
   }

   public boolean a(IBlockData var1, boolean var2) {
      return false;
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
   }

   public boolean a(World var1, BlockPosition var2) {
      return true;
   }
}
