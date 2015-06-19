package net.minecraft.server;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.PathfinderGoal;

public class PathfinderGoalOfferFlower extends PathfinderGoal {
   private EntityIronGolem a;
   private EntityVillager b;
   private int c;

   public PathfinderGoalOfferFlower(EntityIronGolem var1) {
      this.a = var1;
      this.a(3);
   }

   public boolean a() {
      if(!this.a.world.w()) {
         return false;
      } else if(this.a.bc().nextInt(8000) != 0) {
         return false;
      } else {
         this.b = (EntityVillager)this.a.world.a((Class)EntityVillager.class, (AxisAlignedBB)this.a.getBoundingBox().grow(6.0D, 2.0D, 6.0D), (Entity)this.a);
         return this.b != null;
      }
   }

   public boolean b() {
      return this.c > 0;
   }

   public void c() {
      this.c = 400;
      this.a.a(true);
   }

   public void d() {
      this.a.a(false);
      this.b = null;
   }

   public void e() {
      this.a.getControllerLook().a(this.b, 30.0F, 30.0F);
      --this.c;
   }
}
