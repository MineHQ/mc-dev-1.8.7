package net.minecraft.server;

import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLightning;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntityOcelot;
import net.minecraft.server.EntitySkeleton;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PathfinderGoalAvoidTarget;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalSwell;
import net.minecraft.server.World;

public class EntityCreeper extends EntityMonster {
   private int a;
   private int fuseTicks;
   private int maxFuseTicks = 30;
   private int explosionRadius = 3;
   private int bn = 0;

   public EntityCreeper(World var1) {
      super(var1);
      this.goalSelector.a(1, new PathfinderGoalFloat(this));
      this.goalSelector.a(2, new PathfinderGoalSwell(this));
      this.goalSelector.a(3, new PathfinderGoalAvoidTarget(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
      this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, false));
      this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.8D));
      this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
      this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
      this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
      this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
   }

   public int aE() {
      return this.getGoalTarget() == null?3:3 + (int)(this.getHealth() - 1.0F);
   }

   public void e(float var1, float var2) {
      super.e(var1, var2);
      this.fuseTicks = (int)((float)this.fuseTicks + var1 * 1.5F);
      if(this.fuseTicks > this.maxFuseTicks - 5) {
         this.fuseTicks = this.maxFuseTicks - 5;
      }

   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, Byte.valueOf((byte)-1));
      this.datawatcher.a(17, Byte.valueOf((byte)0));
      this.datawatcher.a(18, Byte.valueOf((byte)0));
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      if(this.datawatcher.getByte(17) == 1) {
         var1.setBoolean("powered", true);
      }

      var1.setShort("Fuse", (short)this.maxFuseTicks);
      var1.setByte("ExplosionRadius", (byte)this.explosionRadius);
      var1.setBoolean("ignited", this.cn());
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.datawatcher.watch(17, Byte.valueOf((byte)(var1.getBoolean("powered")?1:0)));
      if(var1.hasKeyOfType("Fuse", 99)) {
         this.maxFuseTicks = var1.getShort("Fuse");
      }

      if(var1.hasKeyOfType("ExplosionRadius", 99)) {
         this.explosionRadius = var1.getByte("ExplosionRadius");
      }

      if(var1.getBoolean("ignited")) {
         this.co();
      }

   }

   public void t_() {
      if(this.isAlive()) {
         this.a = this.fuseTicks;
         if(this.cn()) {
            this.a(1);
         }

         int var1 = this.cm();
         if(var1 > 0 && this.fuseTicks == 0) {
            this.makeSound("creeper.primed", 1.0F, 0.5F);
         }

         this.fuseTicks += var1;
         if(this.fuseTicks < 0) {
            this.fuseTicks = 0;
         }

         if(this.fuseTicks >= this.maxFuseTicks) {
            this.fuseTicks = this.maxFuseTicks;
            this.cr();
         }
      }

      super.t_();
   }

   protected String bo() {
      return "mob.creeper.say";
   }

   protected String bp() {
      return "mob.creeper.death";
   }

   public void die(DamageSource var1) {
      super.die(var1);
      if(var1.getEntity() instanceof EntitySkeleton) {
         int var2 = Item.getId(Items.RECORD_13);
         int var3 = Item.getId(Items.RECORD_WAIT);
         int var4 = var2 + this.random.nextInt(var3 - var2 + 1);
         this.a(Item.getById(var4), 1);
      } else if(var1.getEntity() instanceof EntityCreeper && var1.getEntity() != this && ((EntityCreeper)var1.getEntity()).isPowered() && ((EntityCreeper)var1.getEntity()).cp()) {
         ((EntityCreeper)var1.getEntity()).cq();
         this.a(new ItemStack(Items.SKULL, 1, 4), 0.0F);
      }

   }

   public boolean r(Entity var1) {
      return true;
   }

   public boolean isPowered() {
      return this.datawatcher.getByte(17) == 1;
   }

   protected Item getLoot() {
      return Items.GUNPOWDER;
   }

   public int cm() {
      return this.datawatcher.getByte(16);
   }

   public void a(int var1) {
      this.datawatcher.watch(16, Byte.valueOf((byte)var1));
   }

   public void onLightningStrike(EntityLightning var1) {
      super.onLightningStrike(var1);
      this.datawatcher.watch(17, Byte.valueOf((byte)1));
   }

   protected boolean a(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      if(var2 != null && var2.getItem() == Items.FLINT_AND_STEEL) {
         this.world.makeSound(this.locX + 0.5D, this.locY + 0.5D, this.locZ + 0.5D, "fire.ignite", 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
         var1.bw();
         if(!this.world.isClientSide) {
            this.co();
            var2.damage(1, var1);
            return true;
         }
      }

      return super.a(var1);
   }

   private void cr() {
      if(!this.world.isClientSide) {
         boolean var1 = this.world.getGameRules().getBoolean("mobGriefing");
         float var2 = this.isPowered()?2.0F:1.0F;
         this.world.explode(this, this.locX, this.locY, this.locZ, (float)this.explosionRadius * var2, var1);
         this.die();
      }

   }

   public boolean cn() {
      return this.datawatcher.getByte(18) != 0;
   }

   public void co() {
      this.datawatcher.watch(18, Byte.valueOf((byte)1));
   }

   public boolean cp() {
      return this.bn < 1 && this.world.getGameRules().getBoolean("doMobLoot");
   }

   public void cq() {
      ++this.bn;
   }
}
