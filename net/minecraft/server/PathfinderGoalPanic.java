package net.minecraft.server;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.RandomPositionGenerator;
import net.minecraft.server.Vec3D;

public class PathfinderGoalPanic extends PathfinderGoal {
   private EntityCreature b;
   protected double a;
   private double c;
   private double d;
   private double e;

   public PathfinderGoalPanic(EntityCreature var1, double var2) {
      this.b = var1;
      this.a = var2;
      this.a(1);
   }

   public boolean a() {
      if(this.b.getLastDamager() == null && !this.b.isBurning()) {
         return false;
      } else {
         Vec3D var1 = RandomPositionGenerator.a(this.b, 5, 4);
         if(var1 == null) {
            return false;
         } else {
            this.c = var1.a;
            this.d = var1.b;
            this.e = var1.c;
            return true;
         }
      }
   }

   public void c() {
      this.b.getNavigation().a(this.c, this.d, this.e, this.a);
   }

   public boolean b() {
      return !this.b.getNavigation().m();
   }
}
