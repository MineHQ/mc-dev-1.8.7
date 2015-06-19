package net.minecraft.server;

import com.google.common.base.Predicate;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArmorStand;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;

public final class IEntitySelector {
   public static final Predicate<Entity> a = new Predicate() {
      public boolean a(Entity var1) {
         return var1.isAlive();
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((Entity)var1);
      }
   };
   public static final Predicate<Entity> b = new Predicate() {
      public boolean a(Entity var1) {
         return var1.isAlive() && var1.passenger == null && var1.vehicle == null;
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((Entity)var1);
      }
   };
   public static final Predicate<Entity> c = new Predicate() {
      public boolean a(Entity var1) {
         return var1 instanceof IInventory && var1.isAlive();
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((Entity)var1);
      }
   };
   public static final Predicate<Entity> d = new Predicate() {
      public boolean a(Entity var1) {
         return !(var1 instanceof EntityHuman) || !((EntityHuman)var1).isSpectator();
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((Entity)var1);
      }
   };

   public static class EntitySelectorEquipable implements Predicate<Entity> {
      private final ItemStack a;

      public EntitySelectorEquipable(ItemStack var1) {
         this.a = var1;
      }

      public boolean a(Entity var1) {
         if(!var1.isAlive()) {
            return false;
         } else if(!(var1 instanceof EntityLiving)) {
            return false;
         } else {
            EntityLiving var2 = (EntityLiving)var1;
            return var2.getEquipment(EntityInsentient.c(this.a)) != null?false:(var2 instanceof EntityInsentient?((EntityInsentient)var2).bY():(var2 instanceof EntityArmorStand?true:var2 instanceof EntityHuman));
         }
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((Entity)var1);
      }
   }
}
