package net.minecraft.server;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHorse;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.RandomPositionGenerator;
import net.minecraft.server.Vec3D;

public class PathfinderGoalTame extends PathfinderGoal {
   private EntityHorse entity;
   private double b;
   private double c;
   private double d;
   private double e;

   public PathfinderGoalTame(EntityHorse var1, double var2) {
      this.entity = var1;
      this.b = var2;
      this.a(1);
   }

   public boolean a() {
      if(!this.entity.isTame() && this.entity.passenger != null) {
         Vec3D var1 = RandomPositionGenerator.a(this.entity, 5, 4);
         if(var1 == null) {
            return false;
         } else {
            this.c = var1.a;
            this.d = var1.b;
            this.e = var1.c;
            return true;
         }
      } else {
         return false;
      }
   }

   public void c() {
      this.entity.getNavigation().a(this.c, this.d, this.e, this.b);
   }

   public boolean b() {
      return !this.entity.getNavigation().m() && this.entity.passenger != null;
   }

   public void e() {
      if(this.entity.bc().nextInt(50) == 0) {
         if(this.entity.passenger instanceof EntityHuman) {
            int var1 = this.entity.getTemper();
            int var2 = this.entity.getMaxDomestication();
            if(var2 > 0 && this.entity.bc().nextInt(var2) < var1) {
               this.entity.h((EntityHuman)this.entity.passenger);
               this.entity.world.broadcastEntityEffect(this.entity, (byte)7);
               return;
            }

            this.entity.u(5);
         }

         this.entity.passenger.mount((Entity)null);
         this.entity.passenger = null;
         this.entity.cW();
         this.entity.world.broadcastEntityEffect(this.entity, (byte)6);
      }

   }
}
