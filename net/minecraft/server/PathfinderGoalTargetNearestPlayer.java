package net.minecraft.server;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import net.minecraft.server.AttributeInstance;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalTarget;
import net.minecraft.server.ScoreboardTeamBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PathfinderGoalTargetNearestPlayer extends PathfinderGoal {
   private static final Logger a = LogManager.getLogger();
   private EntityInsentient b;
   private final Predicate<Entity> c;
   private final PathfinderGoalNearestAttackableTarget.DistanceComparator d;
   private EntityLiving e;

   public PathfinderGoalTargetNearestPlayer(EntityInsentient var1) {
      this.b = var1;
      if(var1 instanceof EntityCreature) {
         a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
      }

      this.c = new Predicate() {
         public boolean a(Entity var1) {
            if(!(var1 instanceof EntityHuman)) {
               return false;
            } else if(((EntityHuman)var1).abilities.isInvulnerable) {
               return false;
            } else {
               double var2 = PathfinderGoalTargetNearestPlayer.this.f();
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

               return (double)var1.g(PathfinderGoalTargetNearestPlayer.this.b) > var2?false:PathfinderGoalTarget.a(PathfinderGoalTargetNearestPlayer.this.b, (EntityLiving)var1, false, true);
            }
         }

         // $FF: synthetic method
         public boolean apply(Object var1) {
            return this.a((Entity)var1);
         }
      };
      this.d = new PathfinderGoalNearestAttackableTarget.DistanceComparator(var1);
   }

   public boolean a() {
      double var1 = this.f();
      List var3 = this.b.world.a(EntityHuman.class, this.b.getBoundingBox().grow(var1, 4.0D, var1), this.c);
      Collections.sort(var3, this.d);
      if(var3.isEmpty()) {
         return false;
      } else {
         this.e = (EntityLiving)var3.get(0);
         return true;
      }
   }

   public boolean b() {
      EntityLiving var1 = this.b.getGoalTarget();
      if(var1 == null) {
         return false;
      } else if(!var1.isAlive()) {
         return false;
      } else if(var1 instanceof EntityHuman && ((EntityHuman)var1).abilities.isInvulnerable) {
         return false;
      } else {
         ScoreboardTeamBase var2 = this.b.getScoreboardTeam();
         ScoreboardTeamBase var3 = var1.getScoreboardTeam();
         if(var2 != null && var3 == var2) {
            return false;
         } else {
            double var4 = this.f();
            return this.b.h(var1) > var4 * var4?false:!(var1 instanceof EntityPlayer) || !((EntityPlayer)var1).playerInteractManager.isCreative();
         }
      }
   }

   public void c() {
      this.b.setGoalTarget(this.e);
      super.c();
   }

   public void d() {
      this.b.setGoalTarget((EntityLiving)null);
      super.c();
   }

   protected double f() {
      AttributeInstance var1 = this.b.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
      return var1 == null?16.0D:var1.getValue();
   }
}
