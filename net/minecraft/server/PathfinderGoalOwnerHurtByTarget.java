package net.minecraft.server;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTameableAnimal;
import net.minecraft.server.PathfinderGoalTarget;

public class PathfinderGoalOwnerHurtByTarget extends PathfinderGoalTarget {
   EntityTameableAnimal a;
   EntityLiving b;
   private int c;

   public PathfinderGoalOwnerHurtByTarget(EntityTameableAnimal var1) {
      super(var1, false);
      this.a = var1;
      this.a(1);
   }

   public boolean a() {
      if(!this.a.isTamed()) {
         return false;
      } else {
         EntityLiving var1 = this.a.getOwner();
         if(var1 == null) {
            return false;
         } else {
            this.b = var1.getLastDamager();
            int var2 = var1.be();
            return var2 != this.c && this.a(this.b, false) && this.a.a(this.b, var1);
         }
      }
   }

   public void c() {
      this.e.setGoalTarget(this.b);
      EntityLiving var1 = this.a.getOwner();
      if(var1 != null) {
         this.c = var1.be();
      }

      super.c();
   }
}
