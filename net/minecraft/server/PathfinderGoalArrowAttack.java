package net.minecraft.server;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IRangedEntity;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PathfinderGoal;

public class PathfinderGoalArrowAttack extends PathfinderGoal {
   private final EntityInsentient a;
   private final IRangedEntity b;
   private EntityLiving c;
   private int d;
   private double e;
   private int f;
   private int g;
   private int h;
   private float i;
   private float j;

   public PathfinderGoalArrowAttack(IRangedEntity var1, double var2, int var4, float var5) {
      this(var1, var2, var4, var4, var5);
   }

   public PathfinderGoalArrowAttack(IRangedEntity var1, double var2, int var4, int var5, float var6) {
      this.d = -1;
      if(!(var1 instanceof EntityLiving)) {
         throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
      } else {
         this.b = var1;
         this.a = (EntityInsentient)var1;
         this.e = var2;
         this.g = var4;
         this.h = var5;
         this.i = var6;
         this.j = var6 * var6;
         this.a(3);
      }
   }

   public boolean a() {
      EntityLiving var1 = this.a.getGoalTarget();
      if(var1 == null) {
         return false;
      } else {
         this.c = var1;
         return true;
      }
   }

   public boolean b() {
      return this.a() || !this.a.getNavigation().m();
   }

   public void d() {
      this.c = null;
      this.f = 0;
      this.d = -1;
   }

   public void e() {
      double var1 = this.a.e(this.c.locX, this.c.getBoundingBox().b, this.c.locZ);
      boolean var3 = this.a.getEntitySenses().a(this.c);
      if(var3) {
         ++this.f;
      } else {
         this.f = 0;
      }

      if(var1 <= (double)this.j && this.f >= 20) {
         this.a.getNavigation().n();
      } else {
         this.a.getNavigation().a((Entity)this.c, this.e);
      }

      this.a.getControllerLook().a(this.c, 30.0F, 30.0F);
      float var4;
      if(--this.d == 0) {
         if(var1 > (double)this.j || !var3) {
            return;
         }

         var4 = MathHelper.sqrt(var1) / this.i;
         float var5 = MathHelper.a(var4, 0.1F, 1.0F);
         this.b.a(this.c, var5);
         this.d = MathHelper.d(var4 * (float)(this.h - this.g) + (float)this.g);
      } else if(this.d < 0) {
         var4 = MathHelper.sqrt(var1) / this.i;
         this.d = MathHelper.d(var4 * (float)(this.h - this.g) + (float)this.g);
      }

   }
}
