package net.minecraft.server;

import com.google.common.base.Predicate;
import net.minecraft.server.Entity;
import net.minecraft.server.IAnimal;

public interface IMonster extends IAnimal {
   Predicate<Entity> d = new Predicate() {
      public boolean a(Entity var1) {
         return var1 instanceof IMonster;
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((Entity)var1);
      }
   };
   Predicate<Entity> e = new Predicate() {
      public boolean a(Entity var1) {
         return var1 instanceof IMonster && !var1.isInvisible();
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((Entity)var1);
      }
   };
}
