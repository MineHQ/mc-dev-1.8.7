package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathEntity;
import net.minecraft.server.World;

public class NavigationSpider extends Navigation {
   private BlockPosition f;

   public NavigationSpider(EntityInsentient var1, World var2) {
      super(var1, var2);
   }

   public PathEntity a(BlockPosition var1) {
      this.f = var1;
      return super.a(var1);
   }

   public PathEntity a(Entity var1) {
      this.f = new BlockPosition(var1);
      return super.a(var1);
   }

   public boolean a(Entity var1, double var2) {
      PathEntity var4 = this.a(var1);
      if(var4 != null) {
         return this.a(var4, var2);
      } else {
         this.f = new BlockPosition(var1);
         this.e = var2;
         return true;
      }
   }

   public void k() {
      if(!this.m()) {
         super.k();
      } else {
         if(this.f != null) {
            double var1 = (double)(this.b.width * this.b.width);
            if(this.b.c(this.f) >= var1 && (this.b.locY <= (double)this.f.getY() || this.b.c(new BlockPosition(this.f.getX(), MathHelper.floor(this.b.locY), this.f.getZ())) >= var1)) {
               this.b.getControllerMove().a((double)this.f.getX(), (double)this.f.getY(), (double)this.f.getZ(), this.e);
            } else {
               this.f = null;
            }
         }

      }
   }
}
