package net.minecraft.server;

import net.minecraft.server.BlockDispenser;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IDispenseBehavior;
import net.minecraft.server.IPosition;
import net.minecraft.server.ISourceBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class DispenseBehaviorItem implements IDispenseBehavior {
   public DispenseBehaviorItem() {
   }

   public final ItemStack a(ISourceBlock var1, ItemStack var2) {
      ItemStack var3 = this.b(var1, var2);
      this.a(var1);
      this.a(var1, BlockDispenser.b(var1.f()));
      return var3;
   }

   protected ItemStack b(ISourceBlock var1, ItemStack var2) {
      EnumDirection var3 = BlockDispenser.b(var1.f());
      IPosition var4 = BlockDispenser.a(var1);
      ItemStack var5 = var2.a(1);
      a(var1.i(), var5, 6, var3, var4);
      return var2;
   }

   public static void a(World var0, ItemStack var1, int var2, EnumDirection var3, IPosition var4) {
      double var5 = var4.getX();
      double var7 = var4.getY();
      double var9 = var4.getZ();
      if(var3.k() == EnumDirection.EnumAxis.Y) {
         var7 -= 0.125D;
      } else {
         var7 -= 0.15625D;
      }

      EntityItem var11 = new EntityItem(var0, var5, var7, var9, var1);
      double var12 = var0.random.nextDouble() * 0.1D + 0.2D;
      var11.motX = (double)var3.getAdjacentX() * var12;
      var11.motY = 0.20000000298023224D;
      var11.motZ = (double)var3.getAdjacentZ() * var12;
      var11.motX += var0.random.nextGaussian() * 0.007499999832361937D * (double)var2;
      var11.motY += var0.random.nextGaussian() * 0.007499999832361937D * (double)var2;
      var11.motZ += var0.random.nextGaussian() * 0.007499999832361937D * (double)var2;
      var0.addEntity(var11);
   }

   protected void a(ISourceBlock var1) {
      var1.i().triggerEffect(1000, var1.getBlockPosition(), 0);
   }

   protected void a(ISourceBlock var1, EnumDirection var2) {
      var1.i().triggerEffect(2000, var1.getBlockPosition(), this.a(var2));
   }

   private int a(EnumDirection var1) {
      return var1.getAdjacentX() + 1 + (var1.getAdjacentZ() + 1) * 3;
   }
}
