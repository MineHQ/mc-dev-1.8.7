package net.minecraft.server;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.IAnimal;
import net.minecraft.server.World;

public abstract class EntityWaterAnimal extends EntityInsentient implements IAnimal {
   public EntityWaterAnimal(World var1) {
      super(var1);
   }

   public boolean aY() {
      return true;
   }

   public boolean bR() {
      return true;
   }

   public boolean canSpawn() {
      return this.world.a((AxisAlignedBB)this.getBoundingBox(), (Entity)this);
   }

   public int w() {
      return 120;
   }

   protected boolean isTypeNotPersistent() {
      return true;
   }

   protected int getExpValue(EntityHuman var1) {
      return 1 + this.world.random.nextInt(3);
   }

   public void K() {
      int var1 = this.getAirTicks();
      super.K();
      if(this.isAlive() && !this.V()) {
         --var1;
         this.setAirTicks(var1);
         if(this.getAirTicks() == -20) {
            this.setAirTicks(0);
            this.damageEntity(DamageSource.DROWN, 2.0F);
         }
      } else {
         this.setAirTicks(300);
      }

   }

   public boolean aL() {
      return false;
   }
}
