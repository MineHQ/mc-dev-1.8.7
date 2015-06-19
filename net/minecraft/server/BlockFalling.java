package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityFallingBlock;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockFalling extends Block {
   public static boolean instaFall;

   public BlockFalling() {
      super(Material.SAND);
      this.a((CreativeModeTab)CreativeModeTab.b);
   }

   public BlockFalling(Material var1) {
      super(var1);
   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      var1.a((BlockPosition)var2, (Block)this, this.a(var1));
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      var1.a((BlockPosition)var2, (Block)this, this.a(var1));
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(!var1.isClientSide) {
         this.f(var1, var2);
      }

   }

   private void f(World var1, BlockPosition var2) {
      if(canFall(var1, var2.down()) && var2.getY() >= 0) {
         byte var3 = 32;
         if(!instaFall && var1.areChunksLoadedBetween(var2.a(-var3, -var3, -var3), var2.a(var3, var3, var3))) {
            if(!var1.isClientSide) {
               EntityFallingBlock var5 = new EntityFallingBlock(var1, (double)var2.getX() + 0.5D, (double)var2.getY(), (double)var2.getZ() + 0.5D, var1.getType(var2));
               this.a(var5);
               var1.addEntity(var5);
            }
         } else {
            var1.setAir(var2);

            BlockPosition var4;
            for(var4 = var2.down(); canFall(var1, var4) && var4.getY() > 0; var4 = var4.down()) {
               ;
            }

            if(var4.getY() > 0) {
               var1.setTypeUpdate(var4.up(), this.getBlockData());
            }
         }

      }
   }

   protected void a(EntityFallingBlock var1) {
   }

   public int a(World var1) {
      return 2;
   }

   public static boolean canFall(World var0, BlockPosition var1) {
      Block var2 = var0.getType(var1).getBlock();
      Material var3 = var2.material;
      return var2 == Blocks.FIRE || var3 == Material.AIR || var3 == Material.WATER || var3 == Material.LAVA;
   }

   public void a_(World var1, BlockPosition var2) {
   }
}
