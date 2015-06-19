package net.minecraft.server;

import net.minecraft.server.EntityInsentient;
import net.minecraft.server.PathfinderGoal;

public class PathfinderGoalRandomLookaround extends PathfinderGoal {
   private EntityInsentient a;
   private double b;
   private double c;
   private int d;

   public PathfinderGoalRandomLookaround(EntityInsentient var1) {
      this.a = var1;
      this.a(3);
   }

   public boolean a() {
      return this.a.bc().nextFloat() < 0.02F;
   }

   public boolean b() {
      return this.d >= 0;
   }

   public void c() {
      double var1 = 6.283185307179586D * this.a.bc().nextDouble();
      this.b = Math.cos(var1);
      this.c = Math.sin(var1);
      this.d = 20 + this.a.bc().nextInt(20);
   }

   public void e() {
      --this.d;
      this.a.getControllerLook().a(this.a.locX + this.b, this.a.locY + (double)this.a.getHeadHeight(), this.a.locZ + this.c, 10.0F, (float)this.a.bQ());
   }
}
