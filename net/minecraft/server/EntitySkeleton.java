package net.minecraft.server;

import java.util.Calendar;
import net.minecraft.server.AchievementList;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.IRangedEntity;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalArrowAttack;
import net.minecraft.server.PathfinderGoalAvoidTarget;
import net.minecraft.server.PathfinderGoalFleeSun;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalRestrictSun;
import net.minecraft.server.Statistic;
import net.minecraft.server.World;
import net.minecraft.server.WorldProviderHell;

public class EntitySkeleton extends EntityMonster implements IRangedEntity {
   private PathfinderGoalArrowAttack a = new PathfinderGoalArrowAttack(this, 1.0D, 20, 60, 15.0F);
   private PathfinderGoalMeleeAttack b = new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.2D, false);

   public EntitySkeleton(World var1) {
      super(var1);
      this.goalSelector.a(1, new PathfinderGoalFloat(this));
      this.goalSelector.a(2, new PathfinderGoalRestrictSun(this));
      this.goalSelector.a(3, new PathfinderGoalFleeSun(this, 1.0D));
      this.goalSelector.a(3, new PathfinderGoalAvoidTarget(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
      this.goalSelector.a(4, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
      this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
      this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
      this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
      this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, EntityIronGolem.class, true));
      if(var1 != null && !var1.isClientSide) {
         this.n();
      }

   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
   }

   protected void h() {
      super.h();
      this.datawatcher.a(13, new Byte((byte)0));
   }

   protected String z() {
      return "mob.skeleton.say";
   }

   protected String bo() {
      return "mob.skeleton.hurt";
   }

   protected String bp() {
      return "mob.skeleton.death";
   }

   protected void a(BlockPosition var1, Block var2) {
      this.makeSound("mob.skeleton.step", 0.15F, 1.0F);
   }

   public boolean r(Entity var1) {
      if(super.r(var1)) {
         if(this.getSkeletonType() == 1 && var1 instanceof EntityLiving) {
            ((EntityLiving)var1).addEffect(new MobEffect(MobEffectList.WITHER.id, 200));
         }

         return true;
      } else {
         return false;
      }
   }

   public EnumMonsterType getMonsterType() {
      return EnumMonsterType.UNDEAD;
   }

   public void m() {
      if(this.world.w() && !this.world.isClientSide) {
         float var1 = this.c(1.0F);
         BlockPosition var2 = new BlockPosition(this.locX, (double)Math.round(this.locY), this.locZ);
         if(var1 > 0.5F && this.random.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F && this.world.i(var2)) {
            boolean var3 = true;
            ItemStack var4 = this.getEquipment(4);
            if(var4 != null) {
               if(var4.e()) {
                  var4.setData(var4.h() + this.random.nextInt(2));
                  if(var4.h() >= var4.j()) {
                     this.b(var4);
                     this.setEquipment(4, (ItemStack)null);
                  }
               }

               var3 = false;
            }

            if(var3) {
               this.setOnFire(8);
            }
         }
      }

      if(this.world.isClientSide && this.getSkeletonType() == 1) {
         this.setSize(0.72F, 2.535F);
      }

      super.m();
   }

   public void ak() {
      super.ak();
      if(this.vehicle instanceof EntityCreature) {
         EntityCreature var1 = (EntityCreature)this.vehicle;
         this.aI = var1.aI;
      }

   }

   public void die(DamageSource var1) {
      super.die(var1);
      if(var1.i() instanceof EntityArrow && var1.getEntity() instanceof EntityHuman) {
         EntityHuman var2 = (EntityHuman)var1.getEntity();
         double var3 = var2.locX - this.locX;
         double var5 = var2.locZ - this.locZ;
         if(var3 * var3 + var5 * var5 >= 2500.0D) {
            var2.b((Statistic)AchievementList.v);
         }
      } else if(var1.getEntity() instanceof EntityCreeper && ((EntityCreeper)var1.getEntity()).isPowered() && ((EntityCreeper)var1.getEntity()).cp()) {
         ((EntityCreeper)var1.getEntity()).cq();
         this.a(new ItemStack(Items.SKULL, 1, this.getSkeletonType() == 1?1:0), 0.0F);
      }

   }

   protected Item getLoot() {
      return Items.ARROW;
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      int var3;
      int var4;
      if(this.getSkeletonType() == 1) {
         var3 = this.random.nextInt(3 + var2) - 1;

         for(var4 = 0; var4 < var3; ++var4) {
            this.a(Items.COAL, 1);
         }
      } else {
         var3 = this.random.nextInt(3 + var2);

         for(var4 = 0; var4 < var3; ++var4) {
            this.a(Items.ARROW, 1);
         }
      }

      var3 = this.random.nextInt(3 + var2);

      for(var4 = 0; var4 < var3; ++var4) {
         this.a(Items.BONE, 1);
      }

   }

   protected void getRareDrop() {
      if(this.getSkeletonType() == 1) {
         this.a(new ItemStack(Items.SKULL, 1, 1), 0.0F);
      }

   }

   protected void a(DifficultyDamageScaler var1) {
      super.a(var1);
      this.setEquipment(0, new ItemStack(Items.BOW));
   }

   public GroupDataEntity prepare(DifficultyDamageScaler var1, GroupDataEntity var2) {
      var2 = super.prepare(var1, var2);
      if(this.world.worldProvider instanceof WorldProviderHell && this.bc().nextInt(5) > 0) {
         this.goalSelector.a(4, this.b);
         this.setSkeletonType(1);
         this.setEquipment(0, new ItemStack(Items.STONE_SWORD));
         this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(4.0D);
      } else {
         this.goalSelector.a(4, this.a);
         this.a(var1);
         this.b(var1);
      }

      this.j(this.random.nextFloat() < 0.55F * var1.c());
      if(this.getEquipment(4) == null) {
         Calendar var3 = this.world.Y();
         if(var3.get(2) + 1 == 10 && var3.get(5) == 31 && this.random.nextFloat() < 0.25F) {
            this.setEquipment(4, new ItemStack(this.random.nextFloat() < 0.1F?Blocks.LIT_PUMPKIN:Blocks.PUMPKIN));
            this.dropChances[4] = 0.0F;
         }
      }

      return var2;
   }

   public void n() {
      this.goalSelector.a((PathfinderGoal)this.b);
      this.goalSelector.a((PathfinderGoal)this.a);
      ItemStack var1 = this.bA();
      if(var1 != null && var1.getItem() == Items.BOW) {
         this.goalSelector.a(4, this.a);
      } else {
         this.goalSelector.a(4, this.b);
      }

   }

   public void a(EntityLiving var1, float var2) {
      EntityArrow var3 = new EntityArrow(this.world, this, var1, 1.6F, (float)(14 - this.world.getDifficulty().a() * 4));
      int var4 = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, this.bA());
      int var5 = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, this.bA());
      var3.b((double)(var2 * 2.0F) + this.random.nextGaussian() * 0.25D + (double)((float)this.world.getDifficulty().a() * 0.11F));
      if(var4 > 0) {
         var3.b(var3.j() + (double)var4 * 0.5D + 0.5D);
      }

      if(var5 > 0) {
         var3.setKnockbackStrength(var5);
      }

      if(EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, this.bA()) > 0 || this.getSkeletonType() == 1) {
         var3.setOnFire(100);
      }

      this.makeSound("random.bow", 1.0F, 1.0F / (this.bc().nextFloat() * 0.4F + 0.8F));
      this.world.addEntity(var3);
   }

   public int getSkeletonType() {
      return this.datawatcher.getByte(13);
   }

   public void setSkeletonType(int var1) {
      this.datawatcher.watch(13, Byte.valueOf((byte)var1));
      this.fireProof = var1 == 1;
      if(var1 == 1) {
         this.setSize(0.72F, 2.535F);
      } else {
         this.setSize(0.6F, 1.95F);
      }

   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      if(var1.hasKeyOfType("SkeletonType", 99)) {
         byte var2 = var1.getByte("SkeletonType");
         this.setSkeletonType(var2);
      }

      this.n();
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setByte("SkeletonType", (byte)this.getSkeletonType());
   }

   public void setEquipment(int var1, ItemStack var2) {
      super.setEquipment(var1, var2);
      if(!this.world.isClientSide && var1 == 0) {
         this.n();
      }

   }

   public float getHeadHeight() {
      return this.getSkeletonType() == 1?super.getHeadHeight():1.74F;
   }

   public double am() {
      return this.isBaby()?0.0D:-0.35D;
   }
}
