package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.RandomPositionGenerator;
import net.minecraft.server.Vec3D;

public class PathfinderGoalMoveTowardsRestriction extends PathfinderGoal {
   private EntityCreature a;
   private double b;
   private double c;
   private double d;
   private double e;

   public PathfinderGoalMoveTowardsRestriction(EntityCreature var1, double var2) {
      this.a = var1;
      this.e = var2;
      this.a(1);
   }

   public boolean a() {
      if(this.a.cg()) {
         return false;
      } else {
         BlockPosition var1 = this.a.ch();
         Vec3D var2 = RandomPositionGenerator.a(this.a, 16, 7, new Vec3D((double)var1.getX(), (double)var1.getY(), (double)var1.getZ()));
         if(var2 == null) {
            return false;
         } else {
            this.b = var2.a;
            this.c = var2.b;
            this.d = var2.c;
            return true;
         }
      }
   }

   public boolean b() {
      return !this.a.getNavigation().m();
   }

   public void c() {
      this.a.getNavigation().a(this.b, this.c, this.d, this.e);
   }
}
