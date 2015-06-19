package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.server.AchievementList;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAgeable;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityCow;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.Statistic;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class PathfinderGoalBreed extends PathfinderGoal {
   private EntityAnimal d;
   World a;
   private EntityAnimal e;
   int b;
   double c;

   public PathfinderGoalBreed(EntityAnimal var1, double var2) {
      this.d = var1;
      this.a = var1.world;
      this.c = var2;
      this.a(3);
   }

   public boolean a() {
      if(!this.d.isInLove()) {
         return false;
      } else {
         this.e = this.f();
         return this.e != null;
      }
   }

   public boolean b() {
      return this.e.isAlive() && this.e.isInLove() && this.b < 60;
   }

   public void d() {
      this.e = null;
      this.b = 0;
   }

   public void e() {
      this.d.getControllerLook().a(this.e, 10.0F, (float)this.d.bQ());
      this.d.getNavigation().a((Entity)this.e, this.c);
      ++this.b;
      if(this.b >= 60 && this.d.h(this.e) < 9.0D) {
         this.g();
      }

   }

   private EntityAnimal f() {
      float var1 = 8.0F;
      List var2 = this.a.a(this.d.getClass(), this.d.getBoundingBox().grow((double)var1, (double)var1, (double)var1));
      double var3 = Double.MAX_VALUE;
      EntityAnimal var5 = null;
      Iterator var6 = var2.iterator();

      while(var6.hasNext()) {
         EntityAnimal var7 = (EntityAnimal)var6.next();
         if(this.d.mate(var7) && this.d.h(var7) < var3) {
            var5 = var7;
            var3 = this.d.h(var7);
         }
      }

      return var5;
   }

   private void g() {
      EntityAgeable var1 = this.d.createChild(this.e);
      if(var1 != null) {
         EntityHuman var2 = this.d.cq();
         if(var2 == null && this.e.cq() != null) {
            var2 = this.e.cq();
         }

         if(var2 != null) {
            var2.b(StatisticList.A);
            if(this.d instanceof EntityCow) {
               var2.b((Statistic)AchievementList.H);
            }
         }

         this.d.setAgeRaw(6000);
         this.e.setAgeRaw(6000);
         this.d.cs();
         this.e.cs();
         var1.setAgeRaw(-24000);
         var1.setPositionRotation(this.d.locX, this.d.locY, this.d.locZ, 0.0F, 0.0F);
         this.a.addEntity(var1);
         Random var3 = this.d.bc();

         for(int var4 = 0; var4 < 7; ++var4) {
            double var5 = var3.nextGaussian() * 0.02D;
            double var7 = var3.nextGaussian() * 0.02D;
            double var9 = var3.nextGaussian() * 0.02D;
            double var11 = var3.nextDouble() * (double)this.d.width * 2.0D - (double)this.d.width;
            double var13 = 0.5D + var3.nextDouble() * (double)this.d.length;
            double var15 = var3.nextDouble() * (double)this.d.width * 2.0D - (double)this.d.width;
            this.a.addParticle(EnumParticle.HEART, this.d.locX + var11, this.d.locY + var13, this.d.locZ + var15, var5, var7, var9, new int[0]);
         }

         if(this.a.getGameRules().getBoolean("doMobLoot")) {
            this.a.addEntity(new EntityExperienceOrb(this.a, this.d.locX, this.d.locY, this.d.locZ, var3.nextInt(7) + 1));
         }

      }
   }
}
