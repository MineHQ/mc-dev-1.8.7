package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntitySkeleton;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.NavigationAbstract;
import net.minecraft.server.NavigationSpider;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLeapAtTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.World;

public class EntitySpider extends EntityMonster {
   public EntitySpider(World var1) {
      super(var1);
      this.setSize(1.4F, 0.9F);
      this.goalSelector.a(1, new PathfinderGoalFloat(this));
      this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
      this.goalSelector.a(4, new EntitySpider.PathfinderGoalSpiderMeleeAttack(this, EntityHuman.class));
      this.goalSelector.a(4, new EntitySpider.PathfinderGoalSpiderMeleeAttack(this, EntityIronGolem.class));
      this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.8D));
      this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
      this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
      this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
      this.targetSelector.a(2, new EntitySpider.PathfinderGoalSpiderNearestAttackableTarget(this, EntityHuman.class));
      this.targetSelector.a(3, new EntitySpider.PathfinderGoalSpiderNearestAttackableTarget(this, EntityIronGolem.class));
   }

   public double an() {
      return (double)(this.length * 0.5F);
   }

   protected NavigationAbstract b(World var1) {
      return new NavigationSpider(this, var1);
   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, new Byte((byte)0));
   }

   public void t_() {
      super.t_();
      if(!this.world.isClientSide) {
         this.a(this.positionChanged);
      }

   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(16.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.30000001192092896D);
   }

   protected String z() {
      return "mob.spider.say";
   }

   protected String bo() {
      return "mob.spider.say";
   }

   protected String bp() {
      return "mob.spider.death";
   }

   protected void a(BlockPosition var1, Block var2) {
      this.makeSound("mob.spider.step", 0.15F, 1.0F);
   }

   protected Item getLoot() {
      return Items.STRING;
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      super.dropDeathLoot(var1, var2);
      if(var1 && (this.random.nextInt(3) == 0 || this.random.nextInt(1 + var2) > 0)) {
         this.a(Items.SPIDER_EYE, 1);
      }

   }

   public boolean k_() {
      return this.n();
   }

   public void aA() {
   }

   public EnumMonsterType getMonsterType() {
      return EnumMonsterType.ARTHROPOD;
   }

   public boolean d(MobEffect var1) {
      return var1.getEffectId() == MobEffectList.POISON.id?false:super.d(var1);
   }

   public boolean n() {
      return (this.datawatcher.getByte(16) & 1) != 0;
   }

   public void a(boolean var1) {
      byte var2 = this.datawatcher.getByte(16);
      if(var1) {
         var2 = (byte)(var2 | 1);
      } else {
         var2 &= -2;
      }

      this.datawatcher.watch(16, Byte.valueOf(var2));
   }

   public GroupDataEntity prepare(DifficultyDamageScaler var1, GroupDataEntity var2) {
      Object var4 = super.prepare(var1, var2);
      if(this.world.random.nextInt(100) == 0) {
         EntitySkeleton var3 = new EntitySkeleton(this.world);
         var3.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, 0.0F);
         var3.prepare(var1, (GroupDataEntity)null);
         this.world.addEntity(var3);
         var3.mount(this);
      }

      if(var4 == null) {
         var4 = new EntitySpider.GroupDataSpider();
         if(this.world.getDifficulty() == EnumDifficulty.HARD && this.world.random.nextFloat() < 0.1F * var1.c()) {
            ((EntitySpider.GroupDataSpider)var4).a(this.world.random);
         }
      }

      if(var4 instanceof EntitySpider.GroupDataSpider) {
         int var5 = ((EntitySpider.GroupDataSpider)var4).a;
         if(var5 > 0 && MobEffectList.byId[var5] != null) {
            this.addEffect(new MobEffect(var5, Integer.MAX_VALUE));
         }
      }

      return (GroupDataEntity)var4;
   }

   public float getHeadHeight() {
      return 0.65F;
   }

   static class PathfinderGoalSpiderNearestAttackableTarget<T extends EntityLiving> extends PathfinderGoalNearestAttackableTarget {
      public PathfinderGoalSpiderNearestAttackableTarget(EntitySpider var1, Class<T> var2) {
         super(var1, var2, true);
      }

      public boolean a() {
         float var1 = this.e.c(1.0F);
         return var1 >= 0.5F?false:super.a();
      }
   }

   static class PathfinderGoalSpiderMeleeAttack extends PathfinderGoalMeleeAttack {
      public PathfinderGoalSpiderMeleeAttack(EntitySpider var1, Class<? extends Entity> var2) {
         super(var1, var2, 1.0D, true);
      }

      public boolean b() {
         float var1 = this.b.c(1.0F);
         if(var1 >= 0.5F && this.b.bc().nextInt(100) == 0) {
            this.b.setGoalTarget((EntityLiving)null);
            return false;
         } else {
            return super.b();
         }
      }

      protected double a(EntityLiving var1) {
         return (double)(4.0F + var1.width);
      }
   }

   public static class GroupDataSpider implements GroupDataEntity {
      public int a;

      public GroupDataSpider() {
      }

      public void a(Random var1) {
         int var2 = var1.nextInt(5);
         if(var2 <= 1) {
            this.a = MobEffectList.FASTER_MOVEMENT.id;
         } else if(var2 <= 2) {
            this.a = MobEffectList.INCREASE_DAMAGE.id;
         } else if(var2 <= 3) {
            this.a = MobEffectList.REGENERATION.id;
         } else if(var2 <= 4) {
            this.a = MobEffectList.INVISIBILITY.id;
         }

      }
   }
}
