package net.minecraft.server;

import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PathfinderGoal;

public class PathfinderGoalLeapAtTarget extends PathfinderGoal {
   EntityInsentient a;
   EntityLiving b;
   float c;

   public PathfinderGoalLeapAtTarget(EntityInsentient var1, float var2) {
      this.a = var1;
      this.c = var2;
      this.a(5);
   }

   public boolean a() {
      this.b = this.a.getGoalTarget();
      if(this.b == null) {
         return false;
      } else {
         double var1 = this.a.h(this.b);
         return var1 >= 4.0D && var1 <= 16.0D?(!this.a.onGround?false:this.a.bc().nextInt(5) == 0):false;
      }
   }

   public boolean b() {
      return !this.a.onGround;
   }

   public void c() {
      double var1 = this.b.locX - this.a.locX;
      double var3 = this.b.locZ - this.a.locZ;
      float var5 = MathHelper.sqrt(var1 * var1 + var3 * var3);
      this.a.motX += var1 / (double)var5 * 0.5D * 0.800000011920929D + this.a.motX * 0.20000000298023224D;
      this.a.motZ += var3 / (double)var5 * 0.5D * 0.800000011920929D + this.a.motZ * 0.20000000298023224D;
      this.a.motY = (double)this.c;
   }
}
