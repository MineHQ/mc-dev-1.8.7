package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.EnumSkyBlock;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IMonster;
import net.minecraft.server.MathHelper;
import net.minecraft.server.World;

public abstract class EntityMonster extends EntityCreature implements IMonster {
   public EntityMonster(World var1) {
      super(var1);
      this.b_ = 5;
   }

   public void m() {
      this.bx();
      float var1 = this.c(1.0F);
      if(var1 > 0.5F) {
         this.ticksFarFromPlayer += 2;
      }

      super.m();
   }

   public void t_() {
      super.t_();
      if(!this.world.isClientSide && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
         this.die();
      }

   }

   protected String P() {
      return "game.hostile.swim";
   }

   protected String aa() {
      return "game.hostile.swim.splash";
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else if(super.damageEntity(var1, var2)) {
         Entity var3 = var1.getEntity();
         return this.passenger != var3 && this.vehicle != var3?true:true;
      } else {
         return false;
      }
   }

   protected String bo() {
      return "game.hostile.hurt";
   }

   protected String bp() {
      return "game.hostile.die";
   }

   protected String n(int var1) {
      return var1 > 4?"game.hostile.hurt.fall.big":"game.hostile.hurt.fall.small";
   }

   public boolean r(Entity var1) {
      float var2 = (float)this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
      int var3 = 0;
      if(var1 instanceof EntityLiving) {
         var2 += EnchantmentManager.a(this.bA(), ((EntityLiving)var1).getMonsterType());
         var3 += EnchantmentManager.a((EntityLiving)this);
      }

      boolean var4 = var1.damageEntity(DamageSource.mobAttack(this), var2);
      if(var4) {
         if(var3 > 0) {
            var1.g((double)(-MathHelper.sin(this.yaw * 3.1415927F / 180.0F) * (float)var3 * 0.5F), 0.1D, (double)(MathHelper.cos(this.yaw * 3.1415927F / 180.0F) * (float)var3 * 0.5F));
            this.motX *= 0.6D;
            this.motZ *= 0.6D;
         }

         int var5 = EnchantmentManager.getFireAspectEnchantmentLevel(this);
         if(var5 > 0) {
            var1.setOnFire(var5 * 4);
         }

         this.a(this, var1);
      }

      return var4;
   }

   public float a(BlockPosition var1) {
      return 0.5F - this.world.o(var1);
   }

   protected boolean n_() {
      BlockPosition var1 = new BlockPosition(this.locX, this.getBoundingBox().b, this.locZ);
      if(this.world.b(EnumSkyBlock.SKY, var1) > this.random.nextInt(32)) {
         return false;
      } else {
         int var2 = this.world.getLightLevel(var1);
         if(this.world.R()) {
            int var3 = this.world.ab();
            this.world.c(10);
            var2 = this.world.getLightLevel(var1);
            this.world.c(var3);
         }

         return var2 <= this.random.nextInt(8);
      }
   }

   public boolean bR() {
      return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.n_() && super.bR();
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE);
   }

   protected boolean ba() {
      return true;
   }
}
