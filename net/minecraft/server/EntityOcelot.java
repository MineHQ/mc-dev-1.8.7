package net.minecraft.server;

import com.google.common.base.Predicate;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAgeable;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityChicken;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityTameableAnimal;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalAvoidTarget;
import net.minecraft.server.PathfinderGoalBreed;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalFollowOwner;
import net.minecraft.server.PathfinderGoalJumpOnBlock;
import net.minecraft.server.PathfinderGoalLeapAtTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalOcelotAttack;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalRandomTargetNonTamed;
import net.minecraft.server.PathfinderGoalTempt;
import net.minecraft.server.World;

public class EntityOcelot extends EntityTameableAnimal {
   private PathfinderGoalAvoidTarget<EntityHuman> bo;
   private PathfinderGoalTempt bp;

   public EntityOcelot(World var1) {
      super(var1);
      this.setSize(0.6F, 0.7F);
      ((Navigation)this.getNavigation()).a(true);
      this.goalSelector.a(1, new PathfinderGoalFloat(this));
      this.goalSelector.a(2, this.bm);
      this.goalSelector.a(3, this.bp = new PathfinderGoalTempt(this, 0.6D, Items.FISH, true));
      this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 5.0F));
      this.goalSelector.a(6, new PathfinderGoalJumpOnBlock(this, 0.8D));
      this.goalSelector.a(7, new PathfinderGoalLeapAtTarget(this, 0.3F));
      this.goalSelector.a(8, new PathfinderGoalOcelotAttack(this));
      this.goalSelector.a(9, new PathfinderGoalBreed(this, 0.8D));
      this.goalSelector.a(10, new PathfinderGoalRandomStroll(this, 0.8D));
      this.goalSelector.a(11, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 10.0F));
      this.targetSelector.a(1, new PathfinderGoalRandomTargetNonTamed(this, EntityChicken.class, false, (Predicate)null));
   }

   protected void h() {
      super.h();
      this.datawatcher.a(18, Byte.valueOf((byte)0));
   }

   public void E() {
      if(this.getControllerMove().a()) {
         double var1 = this.getControllerMove().b();
         if(var1 == 0.6D) {
            this.setSneaking(true);
            this.setSprinting(false);
         } else if(var1 == 1.33D) {
            this.setSneaking(false);
            this.setSprinting(true);
         } else {
            this.setSneaking(false);
            this.setSprinting(false);
         }
      } else {
         this.setSneaking(false);
         this.setSprinting(false);
      }

   }

   protected boolean isTypeNotPersistent() {
      return !this.isTamed() && this.ticksLived > 2400;
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(10.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.30000001192092896D);
   }

   public void e(float var1, float var2) {
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("CatType", this.getCatType());
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.setCatType(var1.getInt("CatType"));
   }

   protected String z() {
      return this.isTamed()?(this.isInLove()?"mob.cat.purr":(this.random.nextInt(4) == 0?"mob.cat.purreow":"mob.cat.meow")):"";
   }

   protected String bo() {
      return "mob.cat.hitt";
   }

   protected String bp() {
      return "mob.cat.hitt";
   }

   protected float bB() {
      return 0.4F;
   }

   protected Item getLoot() {
      return Items.LEATHER;
   }

   public boolean r(Entity var1) {
      return var1.damageEntity(DamageSource.mobAttack(this), 3.0F);
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else {
         this.bm.setSitting(false);
         return super.damageEntity(var1, var2);
      }
   }

   protected void dropDeathLoot(boolean var1, int var2) {
   }

   public boolean a(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      if(this.isTamed()) {
         if(this.e(var1) && !this.world.isClientSide && !this.d(var2)) {
            this.bm.setSitting(!this.isSitting());
         }
      } else if(this.bp.f() && var2 != null && var2.getItem() == Items.FISH && var1.h(this) < 9.0D) {
         if(!var1.abilities.canInstantlyBuild) {
            --var2.count;
         }

         if(var2.count <= 0) {
            var1.inventory.setItem(var1.inventory.itemInHandIndex, (ItemStack)null);
         }

         if(!this.world.isClientSide) {
            if(this.random.nextInt(3) == 0) {
               this.setTamed(true);
               this.setCatType(1 + this.world.random.nextInt(3));
               this.setOwnerUUID(var1.getUniqueID().toString());
               this.l(true);
               this.bm.setSitting(true);
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

   public EntityOcelot b(EntityAgeable var1) {
      EntityOcelot var2 = new EntityOcelot(this.world);
      if(this.isTamed()) {
         var2.setOwnerUUID(this.getOwnerUUID());
         var2.setTamed(true);
         var2.setCatType(this.getCatType());
      }

      return var2;
   }

   public boolean d(ItemStack var1) {
      return var1 != null && var1.getItem() == Items.FISH;
   }

   public boolean mate(EntityAnimal var1) {
      if(var1 == this) {
         return false;
      } else if(!this.isTamed()) {
         return false;
      } else if(!(var1 instanceof EntityOcelot)) {
         return false;
      } else {
         EntityOcelot var2 = (EntityOcelot)var1;
         return !var2.isTamed()?false:this.isInLove() && var2.isInLove();
      }
   }

   public int getCatType() {
      return this.datawatcher.getByte(18);
   }

   public void setCatType(int var1) {
      this.datawatcher.watch(18, Byte.valueOf((byte)var1));
   }

   public boolean bR() {
      return this.world.random.nextInt(3) != 0;
   }

   public boolean canSpawn() {
      if(this.world.a((AxisAlignedBB)this.getBoundingBox(), (Entity)this) && this.world.getCubes(this, this.getBoundingBox()).isEmpty() && !this.world.containsLiquid(this.getBoundingBox())) {
         BlockPosition var1 = new BlockPosition(this.locX, this.getBoundingBox().b, this.locZ);
         if(var1.getY() < this.world.F()) {
            return false;
         }

         Block var2 = this.world.getType(var1.down()).getBlock();
         if(var2 == Blocks.GRASS || var2.getMaterial() == Material.LEAVES) {
            return true;
         }
      }

      return false;
   }

   public String getName() {
      return this.hasCustomName()?this.getCustomName():(this.isTamed()?LocaleI18n.get("entity.Cat.name"):super.getName());
   }

   public void setTamed(boolean var1) {
      super.setTamed(var1);
   }

   protected void cm() {
      if(this.bo == null) {
         this.bo = new PathfinderGoalAvoidTarget(this, EntityHuman.class, 16.0F, 0.8D, 1.33D);
      }

      this.goalSelector.a((PathfinderGoal)this.bo);
      if(!this.isTamed()) {
         this.goalSelector.a(4, this.bo);
      }

   }

   public GroupDataEntity prepare(DifficultyDamageScaler var1, GroupDataEntity var2) {
      var2 = super.prepare(var1, var2);
      if(this.world.random.nextInt(7) == 0) {
         for(int var3 = 0; var3 < 2; ++var3) {
            EntityOcelot var4 = new EntityOcelot(this.world);
            var4.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, 0.0F);
            var4.setAgeRaw(-24000);
            this.world.addEntity(var4);
         }
      }

      return var2;
   }

   // $FF: synthetic method
   public EntityAgeable createChild(EntityAgeable var1) {
      return this.b(var1);
   }
}
