package net.minecraft.server;

import java.util.UUID;
import net.minecraft.server.AttributeInstance;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.DamageSource;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.World;

public class EntityPigZombie extends EntityZombie {
   private static final UUID b = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
   private static final AttributeModifier c;
   public int angerLevel;
   private int soundDelay;
   private UUID hurtBy;

   public EntityPigZombie(World var1) {
      super(var1);
      this.fireProof = true;
   }

   public void b(EntityLiving var1) {
      super.b(var1);
      if(var1 != null) {
         this.hurtBy = var1.getUniqueID();
      }

   }

   protected void n() {
      this.targetSelector.a(1, new EntityPigZombie.PathfinderGoalAngerOther(this));
      this.targetSelector.a(2, new EntityPigZombie.PathfinderGoalAnger(this));
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(a).setValue(0.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.23000000417232513D);
      this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(5.0D);
   }

   public void t_() {
      super.t_();
   }

   protected void E() {
      AttributeInstance var1 = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
      if(this.cm()) {
         if(!this.isBaby() && !var1.a(c)) {
            var1.b(c);
         }

         --this.angerLevel;
      } else if(var1.a(c)) {
         var1.c(c);
      }

      if(this.soundDelay > 0 && --this.soundDelay == 0) {
         this.makeSound("mob.zombiepig.zpigangry", this.bB() * 2.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 1.8F);
      }

      if(this.angerLevel > 0 && this.hurtBy != null && this.getLastDamager() == null) {
         EntityHuman var2 = this.world.b(this.hurtBy);
         this.b((EntityLiving)var2);
         this.killer = var2;
         this.lastDamageByPlayerTime = this.be();
      }

      super.E();
   }

   public boolean bR() {
      return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
   }

   public boolean canSpawn() {
      return this.world.a((AxisAlignedBB)this.getBoundingBox(), (Entity)this) && this.world.getCubes(this, this.getBoundingBox()).isEmpty() && !this.world.containsLiquid(this.getBoundingBox());
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setShort("Anger", (short)this.angerLevel);
      if(this.hurtBy != null) {
         var1.setString("HurtBy", this.hurtBy.toString());
      } else {
         var1.setString("HurtBy", "");
      }

   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.angerLevel = var1.getShort("Anger");
      String var2 = var1.getString("HurtBy");
      if(var2.length() > 0) {
         this.hurtBy = UUID.fromString(var2);
         EntityHuman var3 = this.world.b(this.hurtBy);
         this.b((EntityLiving)var3);
         if(var3 != null) {
            this.killer = var3;
            this.lastDamageByPlayerTime = this.be();
         }
      }

   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else {
         Entity var3 = var1.getEntity();
         if(var3 instanceof EntityHuman) {
            this.b(var3);
         }

         return super.damageEntity(var1, var2);
      }
   }

   private void b(Entity var1) {
      this.angerLevel = 400 + this.random.nextInt(400);
      this.soundDelay = this.random.nextInt(40);
      if(var1 instanceof EntityLiving) {
         this.b((EntityLiving)var1);
      }

   }

   public boolean cm() {
      return this.angerLevel > 0;
   }

   protected String z() {
      return "mob.zombiepig.zpig";
   }

   protected String bo() {
      return "mob.zombiepig.zpighurt";
   }

   protected String bp() {
      return "mob.zombiepig.zpigdeath";
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      int var3 = this.random.nextInt(2 + var2);

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         this.a(Items.ROTTEN_FLESH, 1);
      }

      var3 = this.random.nextInt(2 + var2);

      for(var4 = 0; var4 < var3; ++var4) {
         this.a(Items.GOLD_NUGGET, 1);
      }

   }

   public boolean a(EntityHuman var1) {
      return false;
   }

   protected void getRareDrop() {
      this.a(Items.GOLD_INGOT, 1);
   }

   protected void a(DifficultyDamageScaler var1) {
      this.setEquipment(0, new ItemStack(Items.GOLDEN_SWORD));
   }

   public GroupDataEntity prepare(DifficultyDamageScaler var1, GroupDataEntity var2) {
      super.prepare(var1, var2);
      this.setVillager(false);
      return var2;
   }

   static {
      c = (new AttributeModifier(b, "Attacking speed boost", 0.05D, 0)).a(false);
   }

   static class PathfinderGoalAnger extends PathfinderGoalNearestAttackableTarget<EntityHuman> {
      public PathfinderGoalAnger(EntityPigZombie var1) {
         super(var1, EntityHuman.class, true);
      }

      public boolean a() {
         return ((EntityPigZombie)this.e).cm() && super.a();
      }
   }

   static class PathfinderGoalAngerOther extends PathfinderGoalHurtByTarget {
      public PathfinderGoalAngerOther(EntityPigZombie var1) {
         super(var1, true, new Class[0]);
      }

      protected void a(EntityCreature var1, EntityLiving var2) {
         super.a(var1, var2);
         if(var1 instanceof EntityPigZombie) {
            ((EntityPigZombie)var1).b((Entity)var2);
         }

      }
   }
}
