package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IEntitySelector;
import net.minecraft.server.PathfinderGoalTarget;

public class PathfinderGoalNearestAttackableTarget<T extends EntityLiving> extends PathfinderGoalTarget {
   protected final Class<T> a;
   private final int g;
   protected final PathfinderGoalNearestAttackableTarget.DistanceComparator b;
   protected Predicate<? super T> c;
   protected EntityLiving d;

   public PathfinderGoalNearestAttackableTarget(EntityCreature var1, Class<T> var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   public PathfinderGoalNearestAttackableTarget(EntityCreature var1, Class<T> var2, boolean var3, boolean var4) {
      this(var1, var2, 10, var3, var4, (Predicate)null);
   }

   public PathfinderGoalNearestAttackableTarget(EntityCreature var1, Class<T> var2, int var3, boolean var4, boolean var5, final Predicate<? super T> var6) {
      super(var1, var4, var5);
      this.a = var2;
      this.g = var3;
      this.b = new PathfinderGoalNearestAttackableTarget.DistanceComparator(var1);
      this.a(1);
      this.c = new Predicate() {
         public boolean a(T var1) {
            if(var6 != null && !var6.apply(var1)) {
               return false;
            } else {
               if(var1 instanceof EntityHuman) {
                  double var2 = PathfinderGoalNearestAttackableTarget.this.f();
                  if(var1.isSneaking()) {
                     var2 *= 0.800000011920929D;
                  }

                  if(var1.isInvisible()) {
                     float var4 = ((EntityHuman)var1).bY();
                     if(var4 < 0.1F) {
                        var4 = 0.1F;
                     }

                     var2 *= (double)(0.7F * var4);
                  }

                  if((double)var1.g(PathfinderGoalNearestAttackableTarget.this.e) > var2) {
                     return false;
                  }
               }

               return PathfinderGoalNearestAttackableTarget.this.a(var1, false);
            }
         }

         // $FF: synthetic method
         public boolean apply(Object var1) {
            return this.a((EntityLiving)var1);
         }
      };
   }

   public boolean a() {
      if(this.g > 0 && this.e.bc().nextInt(this.g) != 0) {
         return false;
      } else {
         double var1 = this.f();
         List var3 = this.e.world.a(this.a, this.e.getBoundingBox().grow(var1, 4.0D, var1), Predicates.and(this.c, IEntitySelector.d));
         Collections.sort(var3, this.b);
         if(var3.isEmpty()) {
            return false;
         } else {
            this.d = (EntityLiving)var3.get(0);
            return true;
         }
      }
   }

   public void c() {
      this.e.setGoalTarget(this.d);
      super.c();
   }

   public static class DistanceComparator implements Comparator<Entity> {
      private final Entity a;

      public DistanceComparator(Entity var1) {
         this.a = var1;
      }

      public int a(Entity var1, Entity var2) {
         double var3 = this.a.h(var1);
         double var5 = this.a.h(var2);
         return var3 < var5?-1:(var3 > var5?1:0);
      }

      // $FF: synthetic method
      public int compare(Object var1, Object var2) {
         return this.a((Entity)var1, (Entity)var2);
      }
   }
}
