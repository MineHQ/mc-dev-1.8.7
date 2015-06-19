package net.minecraft.server;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTameableAnimal;
import net.minecraft.server.PathfinderGoal;

public class PathfinderGoalSit extends PathfinderGoal {
   private EntityTameableAnimal entity;
   private boolean willSit;

   public PathfinderGoalSit(EntityTameableAnimal var1) {
      this.entity = var1;
      this.a(5);
   }

   public boolean a() {
      if(!this.entity.isTamed()) {
         return false;
      } else if(this.entity.V()) {
         return false;
      } else if(!this.entity.onGround) {
         return false;
      } else {
         EntityLiving var1 = this.entity.getOwner();
         return var1 == null?true:(this.entity.h(var1) < 144.0D && var1.getLastDamager() != null?false:this.willSit);
      }
   }

   public void c() {
      this.entity.getNavigation().n();
      this.entity.setSitting(true);
   }

   public void d() {
      this.entity.setSitting(false);
   }

   public void setSitting(boolean var1) {
      this.willSit = var1;
   }
}
