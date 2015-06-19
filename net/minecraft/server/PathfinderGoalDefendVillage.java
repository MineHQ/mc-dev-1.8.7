package net.minecraft.server;

import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.PathfinderGoalTarget;
import net.minecraft.server.Village;

public class PathfinderGoalDefendVillage extends PathfinderGoalTarget {
   EntityIronGolem a;
   EntityLiving b;

   public PathfinderGoalDefendVillage(EntityIronGolem var1) {
      super(var1, false, true);
      this.a = var1;
      this.a(1);
   }

   public boolean a() {
      Village var1 = this.a.n();
      if(var1 == null) {
         return false;
      } else {
         this.b = var1.b((EntityLiving)this.a);
         if(this.b instanceof EntityCreeper) {
            return false;
         } else if(!this.a(this.b, false)) {
            if(this.e.bc().nextInt(20) == 0) {
               this.b = var1.c((EntityLiving)this.a);
               return this.a(this.b, false);
            } else {
               return false;
            }
         } else {
            return true;
         }
      }
   }

   public void c() {
      this.a.setGoalTarget(this.b);
      super.c();
   }
}
