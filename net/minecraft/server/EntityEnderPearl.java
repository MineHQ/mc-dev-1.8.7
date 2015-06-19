package net.minecraft.server;

import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityEndermite;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityProjectile;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.World;

public class EntityEnderPearl extends EntityProjectile {
   private EntityLiving c;

   public EntityEnderPearl(World var1) {
      super(var1);
   }

   public EntityEnderPearl(World var1, EntityLiving var2) {
      super(var1, var2);
      this.c = var2;
   }

   protected void a(MovingObjectPosition var1) {
      EntityLiving var2 = this.getShooter();
      if(var1.entity != null) {
         if(var1.entity == this.c) {
            return;
         }

         var1.entity.damageEntity(DamageSource.projectile(this, var2), 0.0F);
      }

      for(int var3 = 0; var3 < 32; ++var3) {
         this.world.addParticle(EnumParticle.PORTAL, this.locX, this.locY + this.random.nextDouble() * 2.0D, this.locZ, this.random.nextGaussian(), 0.0D, this.random.nextGaussian(), new int[0]);
      }

      if(!this.world.isClientSide) {
         if(var2 instanceof EntityPlayer) {
            EntityPlayer var5 = (EntityPlayer)var2;
            if(var5.playerConnection.a().g() && var5.world == this.world && !var5.isSleeping()) {
               if(this.random.nextFloat() < 0.05F && this.world.getGameRules().getBoolean("doMobSpawning")) {
                  EntityEndermite var4 = new EntityEndermite(this.world);
                  var4.a(true);
                  var4.setPositionRotation(var2.locX, var2.locY, var2.locZ, var2.yaw, var2.pitch);
                  this.world.addEntity(var4);
               }

               if(var2.au()) {
                  var2.mount((Entity)null);
               }

               var2.enderTeleportTo(this.locX, this.locY, this.locZ);
               var2.fallDistance = 0.0F;
               var2.damageEntity(DamageSource.FALL, 5.0F);
            }
         } else if(var2 != null) {
            var2.enderTeleportTo(this.locX, this.locY, this.locZ);
            var2.fallDistance = 0.0F;
         }

         this.die();
      }

   }

   public void t_() {
      EntityLiving var1 = this.getShooter();
      if(var1 != null && var1 instanceof EntityHuman && !var1.isAlive()) {
         this.die();
      } else {
         super.t_();
      }

   }
}
