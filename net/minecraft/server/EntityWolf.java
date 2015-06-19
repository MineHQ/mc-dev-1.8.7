package net.minecraft.server;

import com.google.common.base.Predicate;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAgeable;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityGhast;
import net.minecraft.server.EntityHorse;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityRabbit;
import net.minecraft.server.EntitySheep;
import net.minecraft.server.EntitySkeleton;
import net.minecraft.server.EntityTameableAnimal;
import net.minecraft.server.EnumColor;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.Item;
import net.minecraft.server.ItemFood;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoalBeg;
import net.minecraft.server.PathfinderGoalBreed;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalFollowOwner;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLeapAtTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalOwnerHurtByTarget;
import net.minecraft.server.PathfinderGoalOwnerHurtTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalRandomTargetNonTamed;
import net.minecraft.server.World;

public class EntityWolf extends EntityTameableAnimal {
   private float bo;
   private float bp;
   private boolean bq;
   private boolean br;
   private float bs;
   private float bt;

   public EntityWolf(World var1) {
      super(var1);
      this.setSize(0.6F, 0.8F);
      ((Navigation)this.getNavigation()).a(true);
      this.goalSelector.a(1, new PathfinderGoalFloat(this));
      this.goalSelector.a(2, this.bm);
      this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
      this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, true));
      this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 2.0F));
      this.goalSelector.a(6, new PathfinderGoalBreed(this, 1.0D));
      this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(8, new PathfinderGoalBeg(this, 8.0F));
      this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
      this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
      this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
      this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
      this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
      this.targetSelector.a(4, new PathfinderGoalRandomTargetNonTamed(this, EntityAnimal.class, false, new Predicate() {
         public boolean a(Entity var1) {
            return var1 instanceof EntitySheep || var1 instanceof EntityRabbit;
         }

         // $FF: synthetic method
         public boolean apply(Object var1) {
            return this.a((Entity)var1);
         }
      }));
      this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget(this, EntitySkeleton.class, false));
      this.setTamed(false);
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.30000001192092896D);
      if(this.isTamed()) {
         this.getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
      } else {
         this.getAttributeInstance(GenericAttributes.maxHealth).setValue(8.0D);
      }

      this.getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE);
      this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(2.0D);
   }

   public void setGoalTarget(EntityLiving var1) {
      super.setGoalTarget(var1);
      if(var1 == null) {
         this.setAngry(false);
      } else if(!this.isTamed()) {
         this.setAngry(true);
      }

   }

   protected void E() {
      this.datawatcher.watch(18, Float.valueOf(this.getHealth()));
   }

   protected void h() {
      super.h();
      this.datawatcher.a(18, new Float(this.getHealth()));
      this.datawatcher.a(19, new Byte((byte)0));
      this.datawatcher.a(20, new Byte((byte)EnumColor.RED.getColorIndex()));
   }

   protected void a(BlockPosition var1, Block var2) {
      this.makeSound("mob.wolf.step", 0.15F, 1.0F);
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setBoolean("Angry", this.isAngry());
      var1.setByte("CollarColor", (byte)this.getCollarColor().getInvColorIndex());
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.setAngry(var1.getBoolean("Angry"));
      if(var1.hasKeyOfType("CollarColor", 99)) {
         this.setCollarColor(EnumColor.fromInvColorIndex(var1.getByte("CollarColor")));
      }

   }

   protected String z() {
      return this.isAngry()?"mob.wolf.growl":(this.random.nextInt(3) == 0?(this.isTamed() && this.datawatcher.getFloat(18) < 10.0F?"mob.wolf.whine":"mob.wolf.panting"):"mob.wolf.bark");
   }

   protected String bo() {
      return "mob.wolf.hurt";
   }

   protected String bp() {
      return "mob.wolf.death";
   }

   protected float bB() {
      return 0.4F;
   }

   protected Item getLoot() {
      return Item.getById(-1);
   }

   public void m() {
      super.m();
      if(!this.world.isClientSide && this.bq && !this.br && !this.cf() && this.onGround) {
         this.br = true;
         this.bs = 0.0F;
         this.bt = 0.0F;
         this.world.broadcastEntityEffect(this, (byte)8);
      }

      if(!this.world.isClientSide && this.getGoalTarget() == null && this.isAngry()) {
         this.setAngry(false);
      }

   }

   public void t_() {
      super.t_();
      this.bp = this.bo;
      if(this.cx()) {
         this.bo += (1.0F - this.bo) * 0.4F;
      } else {
         this.bo += (0.0F - this.bo) * 0.4F;
      }

      if(this.U()) {
         this.bq = true;
         this.br = false;
         this.bs = 0.0F;
         this.bt = 0.0F;
      } else if((this.bq || this.br) && this.br) {
         if(this.bs == 0.0F) {
            this.makeSound("mob.wolf.shake", this.bB(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
         }

         this.bt = this.bs;
         this.bs += 0.05F;
         if(this.bt >= 2.0F) {
            this.bq = false;
            this.br = false;
            this.bt = 0.0F;
            this.bs = 0.0F;
         }

         if(this.bs > 0.4F) {
            float var1 = (float)this.getBoundingBox().b;
            int var2 = (int)(MathHelper.sin((this.bs - 0.4F) * 3.1415927F) * 7.0F);

            for(int var3 = 0; var3 < var2; ++var3) {
               float var4 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
               float var5 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
               this.world.addParticle(EnumParticle.WATER_SPLASH, this.locX + (double)var4, (double)(var1 + 0.8F), this.locZ + (double)var5, this.motX, this.motY, this.motZ, new int[0]);
            }
         }
      }

   }

   public float getHeadHeight() {
      return this.length * 0.8F;
   }

   public int bQ() {
      return this.isSitting()?20:super.bQ();
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else {
         Entity var3 = var1.getEntity();
         this.bm.setSitting(false);
         if(var3 != null && !(var3 instanceof EntityHuman) && !(var3 instanceof EntityArrow)) {
            var2 = (var2 + 1.0F) / 2.0F;
         }

         return super.damageEntity(var1, var2);
      }
   }

   public boolean r(Entity var1) {
      boolean var2 = var1.damageEntity(DamageSource.mobAttack(this), (float)((int)this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue()));
      if(var2) {
         this.a((EntityLiving)this, (Entity)var1);
      }

      return var2;
   }

   public void setTamed(boolean var1) {
      super.setTamed(var1);
      if(var1) {
         this.getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
      } else {
         this.getAttributeInstance(GenericAttributes.maxHealth).setValue(8.0D);
      }

      this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(4.0D);
   }

   public boolean a(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      if(this.isTamed()) {
         if(var2 != null) {
            if(var2.getItem() instanceof ItemFood) {
               ItemFood var3 = (ItemFood)var2.getItem();
               if(var3.g() && this.datawatcher.getFloat(18) < 20.0F) {
                  if(!var1.abilities.canInstantlyBuild) {
                     --var2.count;
                  }

                  this.heal((float)var3.getNutrition(var2));
                  if(var2.count <= 0) {
                     var1.inventory.setItem(var1.inventory.itemInHandIndex, (ItemStack)null);
                  }

                  return true;
               }
            } else if(var2.getItem() == Items.DYE) {
               EnumColor var4 = EnumColor.fromInvColorIndex(var2.getData());
               if(var4 != this.getCollarColor()) {
                  this.setCollarColor(var4);
                  if(!var1.abilities.canInstantlyBuild && --var2.count <= 0) {
                     var1.inventory.setItem(var1.inventory.itemInHandIndex, (ItemStack)null);
                  }

                  return true;
               }
            }
         }

         if(this.e(var1) && !this.world.isClientSide && !this.d(var2)) {
            this.bm.setSitting(!this.isSitting());
            this.aY = false;
            this.navigation.n();
            this.setGoalTarget((EntityLiving)null);
         }
      } else if(var2 != null && var2.getItem() == Items.BONE && !this.isAngry()) {
         if(!var1.abilities.canInstantlyBuild) {
            --var2.count;
         }

         if(var2.count <= 0) {
            var1.inventory.setItem(var1.inventory.itemInHandIndex, (ItemStack)null);
         }

         if(!this.world.isClientSide) {
            if(this.random.nextInt(3) == 0) {
               this.setTamed(true);
               this.navigation.n();
               this.setGoalTarget((EntityLiving)null);
               this.bm.setSitting(true);
               this.setHealth(20.0F);
               this.setOwnerUUID(var1.getUniqueID().toString());
               this.l(true);
               this.world.broadcastEntityEffect(this, (byte)7);
            } else {
               this.l(false);
               this.world.broadcastEntityEffect(this, (byte)6);
            }
         }

         return true;
      }

      return super.a(var1);
   }

   public boolean d(ItemStack var1) {
      return var1 == null?false:(!(var1.getItem() instanceof ItemFood)?false:((ItemFood)var1.getItem()).g());
   }

   public int bV() {
      return 8;
   }

   public boolean isAngry() {
      return (this.datawatcher.getByte(16) & 2) != 0;
   }

   public void setAngry(boolean var1) {
      byte var2 = this.datawatcher.getByte(16);
      if(var1) {
         this.datawatcher.watch(16, Byte.valueOf((byte)(var2 | 2)));
      } else {
         this.datawatcher.watch(16, Byte.valueOf((byte)(var2 & -3)));
      }

   }

   public EnumColor getCollarColor() {
      return EnumColor.fromInvColorIndex(this.datawatcher.getByte(20) & 15);
   }

   public void setCollarColor(EnumColor var1) {
      this.datawatcher.watch(20, Byte.valueOf((byte)(var1.getInvColorIndex() & 15)));
   }

   public EntityWolf b(EntityAgeable var1) {
      EntityWolf var2 = new EntityWolf(this.world);
      String var3 = this.getOwnerUUID();
      if(var3 != null && var3.trim().length() > 0) {
         var2.setOwnerUUID(var3);
         var2.setTamed(true);
      }

      return var2;
   }

   public void p(boolean var1) {
      if(var1) {
         this.datawatcher.watch(19, Byte.valueOf((byte)1));
      } else {
         this.datawatcher.watch(19, Byte.valueOf((byte)0));
      }

   }

   public boolean mate(EntityAnimal var1) {
      if(var1 == this) {
         return false;
      } else if(!this.isTamed()) {
         return false;
      } else if(!(var1 instanceof EntityWolf)) {
         return false;
      } else {
         EntityWolf var2 = (EntityWolf)var1;
         return !var2.isTamed()?false:(var2.isSitting()?false:this.isInLove() && var2.isInLove());
      }
   }

   public boolean cx() {
      return this.datawatcher.getByte(19) == 1;
   }

   protected boolean isTypeNotPersistent() {
      return !this.isTamed() && this.ticksLived > 2400;
   }

   public boolean a(EntityLiving var1, EntityLiving var2) {
      if(!(var1 instanceof EntityCreeper) && !(var1 instanceof EntityGhast)) {
         if(var1 instanceof EntityWolf) {
            EntityWolf var3 = (EntityWolf)var1;
            if(var3.isTamed() && var3.getOwner() == var2) {
               return false;
            }
         }

         return var1 instanceof EntityHuman && var2 instanceof EntityHuman && !((EntityHuman)var2).a((EntityHuman)var1)?false:!(var1 instanceof EntityHorse) || !((EntityHorse)var1).isTame();
      } else {
         return false;
      }
   }

   public boolean cb() {
      return !this.isAngry() && super.cb();
   }

   // $FF: synthetic method
   public EntityAgeable createChild(EntityAgeable var1) {
      return this.b(var1);
   }
}
