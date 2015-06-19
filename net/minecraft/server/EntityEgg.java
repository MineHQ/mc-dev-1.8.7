package net.minecraft.server;

import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityChicken;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityProjectile;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.World;

public class EntityEgg extends EntityProjectile {
   public EntityEgg(World var1) {
      super(var1);
   }

   public EntityEgg(World var1, EntityLiving var2) {
      super(var1, var2);
   }

   public EntityEgg(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   protected void a(MovingObjectPosition var1) {
      if(var1.entity != null) {
         var1.entity.damageEntity(DamageSource.projectile(this, this.getShooter()), 0.0F);
      }

      if(!this.world.isClientSide && this.random.nextInt(8) == 0) {
         byte var2 = 1;
         if(this.random.nextInt(32) == 0) {
            var2 = 4;
         }

         for(int var3 = 0; var3 < var2; ++var3) {
            EntityChicken var4 = new EntityChicken(this.world);
            var4.setAgeRaw(-24000);
            var4.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, 0.0F);
            this.world.addEntity(var4);
         }
      }

      double var5 = 0.08D;

      for(int var6 = 0; var6 < 8; ++var6) {
         this.world.addParticle(EnumParticle.ITEM_CRACK, this.locX, this.locY, this.locZ, ((double)this.random.nextFloat() - 0.5D) * 0.08D, ((double)this.random.nextFloat() - 0.5D) * 0.08D, ((double)this.random.nextFloat() - 0.5D) * 0.08D, new int[]{Item.getId(Items.EGG)});
      }

      if(!this.world.isClientSide) {
         this.die();
      }

   }
}
