package net.minecraft.server;

import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.PathfinderGoal;

public class PathfinderGoalSwell extends PathfinderGoal {
   EntityCreeper a;
   EntityLiving b;

   public PathfinderGoalSwell(EntityCreeper var1) {
      this.a = var1;
      this.a(1);
   }

   public boolean a() {
      EntityLiving var1 = this.a.getGoalTarget();
      return this.a.cm() > 0 || var1 != null && this.a.h(var1) < 9.0D;
   }

   public void c() {
      this.a.getNavigation().n();
      this.b = this.a.getGoalTarget();
   }

   public void d() {
      this.b = null;
   }

   public void e() {
      if(this.b == null) {
         this.a.a(-1);
      } else if(this.a.h(this.b) > 49.0D) {
         this.a.a(-1);
      } else if(!this.a.getEntitySenses().a(this.b)) {
         this.a.a(-1);
      } else {
         this.a.a(1);
      }
   }
}
