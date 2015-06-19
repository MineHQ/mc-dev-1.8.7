package net.minecraft.server;

import net.minecraft.server.Blocks;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.TileEntity;

public class TileEntityEnderChest extends TileEntity implements IUpdatePlayerListBox {
   public float a;
   public float f;
   public int g;
   private int h;

   public TileEntityEnderChest() {
   }

   public void c() {
      if(++this.h % 20 * 4 == 0) {
         this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.g);
      }

      this.f = this.a;
      int var1 = this.position.getX();
      int var2 = this.position.getY();
      int var3 = this.position.getZ();
      float var4 = 0.1F;
      double var7;
      if(this.g > 0 && this.a == 0.0F) {
         double var5 = (double)var1 + 0.5D;
         var7 = (double)var3 + 0.5D;
         this.world.makeSound(var5, (double)var2 + 0.5D, var7, "random.chestopen", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
      }

      if(this.g == 0 && this.a > 0.0F || this.g > 0 && this.a < 1.0F) {
         float var11 = this.a;
         if(this.g > 0) {
            this.a += var4;
         } else {
            this.a -= var4;
         }

         if(this.a > 1.0F) {
            this.a = 1.0F;
         }

         float var6 = 0.5F;
         if(this.a < var6 && var11 >= var6) {
            var7 = (double)var1 + 0.5D;
            double var9 = (double)var3 + 0.5D;
            this.world.makeSound(var7, (double)var2 + 0.5D, var9, "random.chestclosed", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
         }

         if(this.a < 0.0F) {
            this.a = 0.0F;
         }
      }

   }

   public boolean c(int var1, int var2) {
      if(var1 == 1) {
         this.g = var2;
         return true;
      } else {
         return super.c(var1, var2);
      }
   }

   public void y() {
      this.E();
      super.y();
   }

   public void b() {
      ++this.g;
      this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.g);
   }

   public void d() {
      --this.g;
      this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.g);
   }

   public boolean a(EntityHuman var1) {
      return this.world.getTileEntity(this.position) != this?false:var1.e((double)this.position.getX() + 0.5D, (double)this.position.getY() + 0.5D, (double)this.position.getZ() + 0.5D) <= 64.0D;
   }
}
