package net.minecraft.server;

import com.google.common.base.Predicate;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTameableAnimal;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;

public class PathfinderGoalRandomTargetNonTamed<T extends EntityLiving> extends PathfinderGoalNearestAttackableTarget {
   private EntityTameableAnimal g;

   public PathfinderGoalRandomTargetNonTamed(EntityTameableAnimal var1, Class<T> var2, boolean var3, Predicate<? super T> var4) {
      super(var1, var2, 10, var3, false, var4);
      this.g = var1;
   }

   public boolean a() {
      return !this.g.isTamed() && super.a();
   }
}
