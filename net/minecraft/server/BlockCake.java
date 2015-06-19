package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.Material;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class BlockCake extends Block {
   public static final BlockStateInteger BITES = BlockStateInteger.of("bites", 0, 6);

   protected BlockCake() {
      super(Material.CAKE);
      this.j(this.blockStateList.getBlockData().set(BITES, Integer.valueOf(0)));
      this.a(true);
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      float var3 = 0.0625F;
      float var4 = (float)(1 + ((Integer)var1.getType(var2).get(BITES)).intValue() * 2) / 16.0F;
      float var5 = 0.5F;
      this.a(var4, 0.0F, var3, 1.0F - var3, var5, 1.0F - var3);
   }

   public void j() {
      float var1 = 0.0625F;
      float var2 = 0.5F;
      this.a(var1, 0.0F, var1, 1.0F - var1, var2, 1.0F - var1);
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      float var4 = 0.0625F;
      float var5 = (float)(1 + ((Integer)var3.get(BITES)).intValue() * 2) / 16.0F;
      float var6 = 0.5F;
      return new AxisAlignedBB((double)((float)var2.getX() + var5), (double)var2.getY(), (double)((float)var2.getZ() + var4), (double)((float)(var2.getX() + 1) - var4), (double)((float)var2.getY() + var6), (double)((float)(var2.getZ() + 1) - var4));
   }

   public boolean d() {
      return false;
   }

   public boolean c() {
      return false;
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      this.b(var1, var2, var3, var4);
      return true;
   }

   public void attack(World var1, BlockPosition var2, EntityHuman var3) {
      this.b(var1, var2, var1.getType(var2), var3);
   }

   private void b(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4) {
      if(var4.j(false)) {
         var4.b(StatisticList.H);
         var4.getFoodData().eat(2, 0.1F);
         int var5 = ((Integer)var3.get(BITES)).intValue();
         if(var5 < 6) {
            var1.setTypeAndData(var2, var3.set(BITES, Integer.valueOf(var5 + 1)), 3);
         } else {
            var1.setAir(var2);
         }

      }
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return super.canPlace(var1, var2)?this.e(var1, var2):false;
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!this.e(var1, var2)) {
         var1.setAir(var2);
      }

   }

   private boolean e(World var1, BlockPosition var2) {
      return var1.getType(var2.down()).getBlock().getMaterial().isBuildable();
   }

   public int a(Random var1) {
      return 0;
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return null;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(BITES, Integer.valueOf(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Integer)var1.get(BITES)).intValue();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{BITES});
   }

   public int l(World var1, BlockPosition var2) {
      return (7 - ((Integer)var1.getType(var2).get(BITES)).intValue()) * 2;
   }

   public boolean isComplexRedstone() {
      return true;
   }
}
