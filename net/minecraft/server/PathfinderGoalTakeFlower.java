package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.PathfinderGoal;

public class PathfinderGoalTakeFlower extends PathfinderGoal {
   private EntityVillager a;
   private EntityIronGolem b;
   private int c;
   private boolean d;

   public PathfinderGoalTakeFlower(EntityVillager var1) {
      this.a = var1;
      this.a(3);
   }

   public boolean a() {
      if(this.a.getAge() >= 0) {
         return false;
      } else if(!this.a.world.w()) {
         return false;
      } else {
         List var1 = this.a.world.a(EntityIronGolem.class, this.a.getBoundingBox().grow(6.0D, 2.0D, 6.0D));
         if(var1.isEmpty()) {
            return false;
         } else {
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               EntityIronGolem var3 = (EntityIronGolem)var2.next();
               if(var3.cm() > 0) {
                  this.b = var3;
                  break;
               }
            }

            return this.b != null;
         }
      }
   }

   public boolean b() {
      return this.b.cm() > 0;
   }

   public void c() {
      this.c = this.a.bc().nextInt(320);
      this.d = false;
      this.b.getNavigation().n();
   }

   public void d() {
      this.b = null;
      this.a.getNavigation().n();
   }

   public void e() {
      this.a.getControllerLook().a(this.b, 30.0F, 30.0F);
      if(this.b.cm() == this.c) {
         this.a.getNavigation().a((Entity)this.b, 0.5D);
         this.d = true;
      }

      if(this.d && this.a.h(this.b) < 4.0D) {
         this.b.a(false);
         this.a.getNavigation().n();
      }

   }
}
