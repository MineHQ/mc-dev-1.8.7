package net.minecraft.server;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.World;

public class PathfinderGoalOcelotAttack extends PathfinderGoal {
   World a;
   EntityInsentient b;
   EntityLiving c;
   int d;

   public PathfinderGoalOcelotAttack(EntityInsentient var1) {
      this.b = var1;
      this.a = var1.world;
      this.a(3);
   }

   public boolean a() {
      EntityLiving var1 = this.b.getGoalTarget();
      if(var1 == null) {
         return false;
      } else {
         this.c = var1;
         return true;
      }
   }

   public boolean b() {
      return !this.c.isAlive()?false:(this.b.h(this.c) > 225.0D?false:!this.b.getNavigation().m() || this.a());
   }

   public void d() {
      this.c = null;
      this.b.getNavigation().n();
   }

   public void e() {
      this.b.getControllerLook().a(this.c, 30.0F, 30.0F);
      double var1 = (double)(this.b.width * 2.0F * this.b.width * 2.0F);
      double var3 = this.b.e(this.c.locX, this.c.getBoundingBox().b, this.c.locZ);
      double var5 = 0.8D;
      if(var3 > var1 && var3 < 16.0D) {
         var5 = 1.33D;
      } else if(var3 < 225.0D) {
         var5 = 0.6D;
      }

      this.b.getNavigation().a((Entity)this.c, var5);
      this.d = Math.max(this.d - 1, 0);
      if(var3 <= var1) {
         if(this.d <= 0) {
            this.d = 20;
            this.b.r(this.c);
         }
      }
   }
}
