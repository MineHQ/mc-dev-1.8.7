package net.minecraft.server;

import net.minecraft.server.BlockHalfTransparent;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.World;

public class BlockSlime extends BlockHalfTransparent {
   public BlockSlime() {
      super(Material.CLAY, false, MaterialMapColor.c);
      this.a(CreativeModeTab.c);
      this.frictionFactor = 0.8F;
   }

   public void a(World var1, BlockPosition var2, Entity var3, float var4) {
      if(var3.isSneaking()) {
         super.a(var1, var2, var3, var4);
      } else {
         var3.e(var4, 0.0F);
      }

   }

   public void a(World var1, Entity var2) {
      if(var2.isSneaking()) {
         super.a(var1, var2);
      } else if(var2.motY < 0.0D) {
         var2.motY = -var2.motY;
      }

   }

   public void a(World var1, BlockPosition var2, Entity var3) {
      if(Math.abs(var3.motY) < 0.1D && !var3.isSneaking()) {
         double var4 = 0.4D + Math.abs(var3.motY) * 0.2D;
         var3.motX *= var4;
         var3.motZ *= var4;
      }

      super.a(var1, var2, var3);
   }
}
