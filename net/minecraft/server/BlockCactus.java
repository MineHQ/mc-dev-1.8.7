package net.minecraft.server;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockCactus extends Block {
   public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 15);

   protected BlockCactus() {
      super(Material.CACTUS);
      this.j(this.blockStateList.getBlockData().set(AGE, Integer.valueOf(0)));
      this.a(true);
      this.a(CreativeModeTab.c);
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      BlockPosition var5 = var2.up();
      if(var1.isEmpty(var5)) {
         int var6;
         for(var6 = 1; var1.getType(var2.down(var6)).getBlock() == this; ++var6) {
            ;
         }

         if(var6 < 3) {
            int var7 = ((Integer)var3.get(AGE)).intValue();
            if(var7 == 15) {
               var1.setTypeUpdate(var5, this.getBlockData());
               IBlockData var8 = var3.set(AGE, Integer.valueOf(0));
               var1.setTypeAndData(var2, var8, 4);
               this.doPhysics(var1, var5, var8, this);
            } else {
               var1.setTypeAndData(var2, var3.set(AGE, Integer.valueOf(var7 + 1)), 4);
            }

         }
      }
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      float var4 = 0.0625F;
      return new AxisAlignedBB((double)((float)var2.getX() + var4), (double)var2.getY(), (double)((float)var2.getZ() + var4), (double)((float)(var2.getX() + 1) - var4), (double)((float)(var2.getY() + 1) - var4), (double)((float)(var2.getZ() + 1) - var4));
   }

   public boolean d() {
      return false;
   }

   public boolean c() {
      return false;
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return super.canPlace(var1, var2)?this.e(var1, var2):false;
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!this.e(var1, var2)) {
         var1.setAir(var2, true);
      }

   }

   public boolean e(World var1, BlockPosition var2) {
      Iterator var3 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

      while(var3.hasNext()) {
         EnumDirection var4 = (EnumDirection)var3.next();
         if(var1.getType(var2.shift(var4)).getBlock().getMaterial().isBuildable()) {
            return false;
         }
      }

      Block var5 = var1.getType(var2.down()).getBlock();
      return var5 == Blocks.CACTUS || var5 == Blocks.SAND;
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, Entity var4) {
      var4.damageEntity(DamageSource.CACTUS, 1.0F);
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(AGE, Integer.valueOf(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Integer)var1.get(AGE)).intValue();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{AGE});
   }
}
