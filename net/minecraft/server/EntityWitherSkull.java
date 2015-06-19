package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityWither;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.Explosion;
import net.minecraft.server.IBlockData;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.World;

public class EntityWitherSkull extends EntityFireball {
   public EntityWitherSkull(World var1) {
      super(var1);
      this.setSize(0.3125F, 0.3125F);
   }

   public EntityWitherSkull(World var1, EntityLiving var2, double var3, double var5, double var7) {
      super(var1, var2, var3, var5, var7);
      this.setSize(0.3125F, 0.3125F);
   }

   protected float j() {
      return this.isCharged()?0.73F:super.j();
   }

   public boolean isBurning() {
      return false;
   }

   public float a(Explosion var1, World var2, BlockPosition var3, IBlockData var4) {
      float var5 = super.a(var1, var2, var3, var4);
      Block var6 = var4.getBlock();
      if(this.isCharged() && EntityWither.a(var6)) {
         var5 = Math.min(0.8F, var5);
      }

      return var5;
   }

   protected void a(MovingObjectPosition var1) {
      if(!this.world.isClientSide) {
         if(var1.entity != null) {
            if(this.shooter != null) {
               if(var1.entity.damageEntity(DamageSource.mobAttack(this.shooter), 8.0F)) {
                  if(!var1.entity.isAlive()) {
                     this.shooter.heal(5.0F);
                  } else {
                     this.a(this.shooter, var1.entity);
                  }
               }
            } else {
               var1.entity.damageEntity(DamageSource.MAGIC, 5.0F);
            }

            if(var1.entity instanceof EntityLiving) {
               byte var2 = 0;
               if(this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                  var2 = 10;
               } else if(this.world.getDifficulty() == EnumDifficulty.HARD) {
                  var2 = 40;
               }

               if(var2 > 0) {
                  ((EntityLiving)var1.entity).addEffect(new MobEffect(MobEffectList.WITHER.id, 20 * var2, 1));
               }
            }
         }

         this.world.createExplosion(this, this.locX, this.locY, this.locZ, 1.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
         this.die();
      }

   }

   public boolean ad() {
      return false;
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      return false;
   }

   protected void h() {
      this.datawatcher.a(10, Byte.valueOf((byte)0));
   }

   public boolean isCharged() {
      return this.datawatcher.getByte(10) == 1;
   }

   public void setCharged(boolean var1) {
      this.datawatcher.watch(10, Byte.valueOf((byte)(var1?1:0)));
   }
}
