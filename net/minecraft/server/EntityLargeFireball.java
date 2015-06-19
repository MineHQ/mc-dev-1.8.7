package net.minecraft.server;

import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityLargeFireball extends EntityFireball {
   public int yield = 1;

   public EntityLargeFireball(World var1) {
      super(var1);
   }

   public EntityLargeFireball(World var1, EntityLiving var2, double var3, double var5, double var7) {
      super(var1, var2, var3, var5, var7);
   }

   protected void a(MovingObjectPosition var1) {
      if(!this.world.isClientSide) {
         if(var1.entity != null) {
            var1.entity.damageEntity(DamageSource.fireball(this, this.shooter), 6.0F);
            this.a(this.shooter, var1.entity);
         }

         boolean var2 = this.world.getGameRules().getBoolean("mobGriefing");
         this.world.createExplosion((Entity)null, this.locX, this.locY, this.locZ, (float)this.yield, var2, var2);
         this.die();
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("ExplosionPower", this.yield);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      if(var1.hasKeyOfType("ExplosionPower", 99)) {
         this.yield = var1.getInt("ExplosionPower");
      }

   }
}
