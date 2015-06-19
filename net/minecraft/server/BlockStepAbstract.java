package net.minecraft.server;

import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public abstract class BlockStepAbstract extends Block {
   public static final BlockStateEnum<BlockStepAbstract.EnumSlabHalf> HALF = BlockStateEnum.of("half", BlockStepAbstract.EnumSlabHalf.class);

   public BlockStepAbstract(Material var1) {
      super(var1);
      if(this.l()) {
         this.r = true;
      } else {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
      }

      this.e(255);
   }

   protected boolean I() {
      return false;
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      if(this.l()) {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      } else {
         IBlockData var3 = var1.getType(var2);
         if(var3.getBlock() == this) {
            if(var3.get(HALF) == BlockStepAbstract.EnumSlabHalf.TOP) {
               this.a(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
            } else {
               this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            }
         }

      }
   }

   public void j() {
      if(this.l()) {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      } else {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
      }

   }

   public void a(World var1, BlockPosition var2, IBlockData var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, Entity var6) {
      this.updateShape(var1, var2);
      super.a(var1, var2, var3, var4, var5, var6);
   }

   public boolean c() {
      return this.l();
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      IBlockData var9 = super.getPlacedState(var1, var2, var3, var4, var5, var6, var7, var8).set(HALF, BlockStepAbstract.EnumSlabHalf.BOTTOM);
      return this.l()?var9:(var3 != EnumDirection.DOWN && (var3 == EnumDirection.UP || (double)var5 <= 0.5D)?var9:var9.set(HALF, BlockStepAbstract.EnumSlabHalf.TOP));
   }

   public int a(Random var1) {
      return this.l()?2:1;
   }

   public boolean d() {
      return this.l();
   }

   public abstract String b(int var1);

   public int getDropData(World var1, BlockPosition var2) {
      return super.getDropData(var1, var2) & 7;
   }

   public abstract boolean l();

   public abstract IBlockState<?> n();

   public abstract Object a(ItemStack var1);

   public static enum EnumSlabHalf implements INamable {
      TOP("top"),
      BOTTOM("bottom");

      private final String c;

      private EnumSlabHalf(String var3) {
         this.c = var3;
      }

      public String toString() {
         return this.c;
      }

      public String getName() {
         return this.c;
      }
   }
}
