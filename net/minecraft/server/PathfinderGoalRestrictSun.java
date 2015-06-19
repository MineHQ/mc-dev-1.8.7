package net.minecraft.server;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoal;

public class PathfinderGoalRestrictSun extends PathfinderGoal {
   private EntityCreature a;

   public PathfinderGoalRestrictSun(EntityCreature var1) {
      this.a = var1;
   }

   public boolean a() {
      return this.a.world.w();
   }

   public void c() {
      ((Navigation)this.a.getNavigation()).e(true);
   }

   public void d() {
      ((Navigation)this.a.getNavigation()).e(false);
   }
}
