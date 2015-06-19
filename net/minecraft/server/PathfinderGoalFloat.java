package net.minecraft.server;

import net.minecraft.server.EntityInsentient;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoal;

public class PathfinderGoalFloat extends PathfinderGoal {
   private EntityInsentient a;

   public PathfinderGoalFloat(EntityInsentient var1) {
      this.a = var1;
      this.a(4);
      ((Navigation)var1.getNavigation()).d(true);
   }

   public boolean a() {
      return this.a.V() || this.a.ab();
   }

   public void e() {
      if(this.a.bc().nextFloat() < 0.8F) {
         this.a.getControllerJump().a();
      }

   }
}
