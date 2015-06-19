package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockRedstoneOre extends Block {
   private final boolean a;

   public BlockRedstoneOre(boolean var1) {
      super(Material.STONE);
      if(var1) {
         this.a(true);
      }

      this.a = var1;
   }

   public int a(World var1) {
      return 30;
   }

   public void attack(World var1, BlockPosition var2, EntityHuman var3) {
      this.e(var1, var2);
      super.attack(var1, var2, var3);
   }

   public void a(World var1, BlockPosition var2, Entity var3) {
      this.e(var1, var2);
      super.a(var1, var2, var3);
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      this.e(var1, var2);
      return super.interact(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   private void e(World var1, BlockPosition var2) {
      this.f(var1, var2);
      if(this == Blocks.REDSTONE_ORE) {
         var1.setTypeUpdate(var2, Blocks.LIT_REDSTONE_ORE.getBlockData());
      }

   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(this == Blocks.LIT_REDSTONE_ORE) {
         var1.setTypeUpdate(var2, Blocks.REDSTONE_ORE.getBlockData());
      }

   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.REDSTONE;
   }

   public int getDropCount(int var1, Random var2) {
      return this.a(var2) + var2.nextInt(var1 + 1);
   }

   public int a(Random var1) {
      return 4 + var1.nextInt(2);
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
      super.dropNaturally(var1, var2, var3, var4, var5);
      if(this.getDropType(var3, var1.random, var5) != Item.getItemOf(this)) {
         int var6 = 1 + var1.random.nextInt(5);
         this.dropExperience(var1, var2, var6);
      }

   }

   private void f(World var1, BlockPosition var2) {
      Random var3 = var1.random;
      double var4 = 0.0625D;

      for(int var6 = 0; var6 < 6; ++var6) {
         double var7 = (double)((float)var2.getX() + var3.nextFloat());
         double var9 = (double)((float)var2.getY() + var3.nextFloat());
         double var11 = (double)((float)var2.getZ() + var3.nextFloat());
         if(var6 == 0 && !var1.getType(var2.up()).getBlock().c()) {
            var9 = (double)var2.getY() + var4 + 1.0D;
         }

         if(var6 == 1 && !var1.getType(var2.down()).getBlock().c()) {
            var9 = (double)var2.getY() - var4;
         }

         if(var6 == 2 && !var1.getType(var2.south()).getBlock().c()) {
            var11 = (double)var2.getZ() + var4 + 1.0D;
         }

         if(var6 == 3 && !var1.getType(var2.north()).getBlock().c()) {
            var11 = (double)var2.getZ() - var4;
         }

         if(var6 == 4 && !var1.getType(var2.east()).getBlock().c()) {
            var7 = (double)var2.getX() + var4 + 1.0D;
         }

         if(var6 == 5 && !var1.getType(var2.west()).getBlock().c()) {
            var7 = (double)var2.getX() - var4;
         }

         if(var7 < (double)var2.getX() || var7 > (double)(var2.getX() + 1) || var9 < 0.0D || var9 > (double)(var2.getY() + 1) || var11 < (double)var2.getZ() || var11 > (double)(var2.getZ() + 1)) {
            var1.addParticle(EnumParticle.REDSTONE, var7, var9, var11, 0.0D, 0.0D, 0.0D, new int[0]);
         }
      }

   }

   protected ItemStack i(IBlockData var1) {
      return new ItemStack(Blocks.REDSTONE_ORE);
   }
}
