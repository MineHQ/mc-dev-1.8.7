package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.Item;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.World;

public class EntityEndermite extends EntityMonster {
   private int a = 0;
   private boolean b = false;

   public EntityEndermite(World var1) {
      super(var1);
      this.b_ = 3;
      this.setSize(0.4F, 0.3F);
      this.goalSelector.a(1, new PathfinderGoalFloat(this));
      this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
      this.goalSelector.a(3, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
      this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
      this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
      this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
   }

   public float getHeadHeight() {
      return 0.1F;
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(8.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
      this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(2.0D);
   }

   protected boolean s_() {
      return false;
   }

   protected String z() {
      return "mob.silverfish.say";
   }

   protected String bo() {
      return "mob.silverfish.hit";
   }

   protected String bp() {
      return "mob.silverfish.kill";
   }

   protected void a(BlockPosition var1, Block var2) {
      this.makeSound("mob.silverfish.step", 0.15F, 1.0F);
   }

   protected Item getLoot() {
      return null;
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.a = var1.getInt("Lifetime");
      this.b = var1.getBoolean("PlayerSpawned");
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("Lifetime", this.a);
      var1.setBoolean("PlayerSpawned", this.b);
   }

   public void t_() {
      this.aI = this.yaw;
      super.t_();
   }

   public boolean n() {
      return this.b;
   }

   public void a(boolean var1) {
      this.b = var1;
   }

   public void m() {
      super.m();
      if(this.world.isClientSide) {
         for(int var1 = 0; var1 < 2; ++var1) {
            this.world.addParticle(EnumParticle.PORTAL, this.locX + (this.random.nextDouble() - 0.5D) * (double)this.width, this.locY + this.random.nextDouble() * (double)this.length, this.locZ + (this.random.nextDouble() - 0.5D) * (double)this.width, (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D, new int[0]);
         }
      } else {
         if(!this.isPersistent()) {
            ++this.a;
         }

         if(this.a >= 2400) {
            this.die();
         }
      }

   }

   protected boolean n_() {
      return true;
   }

   public boolean bR() {
      if(super.bR()) {
         EntityHuman var1 = this.world.findNearbyPlayer(this, 5.0D);
         return var1 == null;
      } else {
         return false;
      }
   }

   public EnumMonsterType getMonsterType() {
      return EnumMonsterType.ARTHROPOD;
   }
}
