package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockFlowing;
import net.minecraft.server.BlockFluids;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockStationary extends BlockFluids {
   protected BlockStationary(Material var1) {
      super(var1);
      this.a(false);
      if(var1 == Material.LAVA) {
         this.a(true);
      }

   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!this.e(var1, var2, var3)) {
         this.f(var1, var2, var3);
      }

   }

   private void f(World var1, BlockPosition var2, IBlockData var3) {
      BlockFlowing var4 = a(this.material);
      var1.setTypeAndData(var2, var4.getBlockData().set(LEVEL, var3.get(LEVEL)), 2);
      var1.a((BlockPosition)var2, (Block)var4, this.a(var1));
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(this.material == Material.LAVA) {
         if(var1.getGameRules().getBoolean("doFireTick")) {
            int var5 = var4.nextInt(3);
            if(var5 > 0) {
               BlockPosition var6 = var2;

               for(int var7 = 0; var7 < var5; ++var7) {
                  var6 = var6.a(var4.nextInt(3) - 1, 1, var4.nextInt(3) - 1);
                  Block var8 = var1.getType(var6).getBlock();
                  if(var8.material == Material.AIR) {
                     if(this.f(var1, var6)) {
                        var1.setTypeUpdate(var6, Blocks.FIRE.getBlockData());
                        return;
                     }
                  } else if(var8.material.isSolid()) {
                     return;
                  }
               }
            } else {
               for(int var9 = 0; var9 < 3; ++var9) {
                  BlockPosition var10 = var2.a(var4.nextInt(3) - 1, 0, var4.nextInt(3) - 1);
                  if(var1.isEmpty(var10.up()) && this.m(var1, var10)) {
                     var1.setTypeUpdate(var10.up(), Blocks.FIRE.getBlockData());
                  }
               }
            }

         }
      }
   }

   protected boolean f(World var1, BlockPosition var2) {
      EnumDirection[] var3 = EnumDirection.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumDirection var6 = var3[var5];
         if(this.m(var1, var2.shift(var6))) {
            return true;
         }
      }

      return false;
   }

   private boolean m(World var1, BlockPosition var2) {
      return var1.getType(var2).getBlock().getMaterial().isBurnable();
   }
}
