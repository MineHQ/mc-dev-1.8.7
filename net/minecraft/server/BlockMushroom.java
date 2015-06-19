package net.minecraft.server;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDirt;
import net.minecraft.server.BlockPlant;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockFragilePlantElement;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenHugeMushroom;

public class BlockMushroom extends BlockPlant implements IBlockFragilePlantElement {
   protected BlockMushroom() {
      float var1 = 0.2F;
      this.a(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var1 * 2.0F, 0.5F + var1);
      this.a(true);
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(var4.nextInt(25) == 0) {
         int var5 = 5;
         boolean var6 = true;
         Iterator var7 = BlockPosition.b(var2.a(-4, -1, -4), var2.a(4, 1, 4)).iterator();

         while(var7.hasNext()) {
            BlockPosition var8 = (BlockPosition)var7.next();
            if(var1.getType(var8).getBlock() == this) {
               --var5;
               if(var5 <= 0) {
                  return;
               }
            }
         }

         BlockPosition var9 = var2.a(var4.nextInt(3) - 1, var4.nextInt(2) - var4.nextInt(2), var4.nextInt(3) - 1);

         for(int var10 = 0; var10 < 4; ++var10) {
            if(var1.isEmpty(var9) && this.f(var1, var9, this.getBlockData())) {
               var2 = var9;
            }

            var9 = var2.a(var4.nextInt(3) - 1, var4.nextInt(2) - var4.nextInt(2), var4.nextInt(3) - 1);
         }

         if(var1.isEmpty(var9) && this.f(var1, var9, this.getBlockData())) {
            var1.setTypeAndData(var9, this.getBlockData(), 2);
         }
      }

   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return super.canPlace(var1, var2) && this.f(var1, var2, this.getBlockData());
   }

   protected boolean c(Block var1) {
      return var1.o();
   }

   public boolean f(World var1, BlockPosition var2, IBlockData var3) {
      if(var2.getY() >= 0 && var2.getY() < 256) {
         IBlockData var4 = var1.getType(var2.down());
         return var4.getBlock() == Blocks.MYCELIUM?true:(var4.getBlock() == Blocks.DIRT && var4.get(BlockDirt.VARIANT) == BlockDirt.EnumDirtVariant.PODZOL?true:var1.k(var2) < 13 && this.c(var4.getBlock()));
      } else {
         return false;
      }
   }

   public boolean d(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      var1.setAir(var2);
      WorldGenHugeMushroom var5 = null;
      if(this == Blocks.BROWN_MUSHROOM) {
         var5 = new WorldGenHugeMushroom(Blocks.BROWN_MUSHROOM_BLOCK);
      } else if(this == Blocks.RED_MUSHROOM) {
         var5 = new WorldGenHugeMushroom(Blocks.RED_MUSHROOM_BLOCK);
      }

      if(var5 != null && var5.generate(var1, var4, var2)) {
         return true;
      } else {
         var1.setTypeAndData(var2, var3, 3);
         return false;
      }
   }

   public boolean a(World var1, BlockPosition var2, IBlockData var3, boolean var4) {
      return true;
   }

   public boolean a(World var1, Random var2, BlockPosition var3, IBlockData var4) {
      return (double)var2.nextFloat() < 0.4D;
   }

   public void b(World var1, Random var2, BlockPosition var3, IBlockData var4) {
      this.d(var1, var3, var4, var2);
   }
}
