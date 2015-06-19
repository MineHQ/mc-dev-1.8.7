package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.IEntitySelector;
import net.minecraft.server.NavigationAbstract;
import net.minecraft.server.PathEntity;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.RandomPositionGenerator;
import net.minecraft.server.Vec3D;

public class PathfinderGoalAvoidTarget<T extends Entity> extends PathfinderGoal {
   private final Predicate<Entity> c;
   protected EntityCreature a;
   private double d;
   private double e;
   protected T b;
   private float f;
   private PathEntity g;
   private NavigationAbstract h;
   private Class<T> i;
   private Predicate<? super T> j;

   public PathfinderGoalAvoidTarget(EntityCreature var1, Class<T> var2, float var3, double var4, double var6) {
      this(var1, var2, Predicates.alwaysTrue(), var3, var4, var6);
   }

   public PathfinderGoalAvoidTarget(EntityCreature var1, Class<T> var2, Predicate<? super T> var3, float var4, double var5, double var7) {
      this.c = new Predicate() {
         public boolean a(Entity var1) {
            return var1.isAlive() && PathfinderGoalAvoidTarget.this.a.getEntitySenses().a(var1);
         }

         // $FF: synthetic method
         public boolean apply(Object var1) {
            return this.a((Entity)var1);
         }
      };
      this.a = var1;
      this.i = var2;
      this.j = var3;
      this.f = var4;
      this.d = var5;
      this.e = var7;
      this.h = var1.getNavigation();
      this.a(1);
   }

   public boolean a() {
      List var1 = this.a.world.a(this.i, this.a.getBoundingBox().grow((double)this.f, 3.0D, (double)this.f), Predicates.and(new Predicate[]{IEntitySelector.d, this.c, this.j}));
      if(var1.isEmpty()) {
         return false;
      } else {
         this.b = (Entity)var1.get(0);
         Vec3D var2 = RandomPositionGenerator.b(this.a, 16, 7, new Vec3D(this.b.locX, this.b.locY, this.b.locZ));
         if(var2 == null) {
            return false;
         } else if(this.b.e(var2.a, var2.b, var2.c) < this.b.h(this.a)) {
            return false;
         } else {
            this.g = this.h.a(var2.a, var2.b, var2.c);
            return this.g == null?false:this.g.b(var2);
         }
      }
   }

   public boolean b() {
      return !this.h.m();
   }

   public void c() {
      this.h.a(this.g, this.d);
   }

   public void d() {
      this.b = null;
   }

   public void e() {
      if(this.a.h(this.b) < 49.0D) {
         this.a.getNavigation().a(this.e);
      } else {
         this.a.getNavigation().a(this.d);
      }

   }
}
