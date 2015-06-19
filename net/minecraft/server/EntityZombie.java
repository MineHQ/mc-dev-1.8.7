package net.minecraft.server;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.AttributeInstance;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.AttributeRanged;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityChicken;
import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntityPigZombie;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.IAttribute;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IEntitySelector;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalBreakDoor;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.World;

public class EntityZombie extends EntityMonster {
   protected static final IAttribute a = (new AttributeRanged((IAttribute)null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D)).a("Spawn Reinforcements Chance");
   private static final UUID b = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
   private static final AttributeModifier c;
   private final PathfinderGoalBreakDoor bm = new PathfinderGoalBreakDoor(this);
   private int bn;
   private boolean bo = false;
   private float bp = -1.0F;
   private float bq;

   public EntityZombie(World var1) {
      super(var1);
      ((Navigation)this.getNavigation()).b(true);
      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
      this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
      this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
      this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
      this.n();
      this.setSize(0.6F, 1.95F);
   }

   protected void n() {
      this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityVillager.class, 1.0D, true));
      this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityIronGolem.class, 1.0D, true));
      this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
      this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[]{EntityPigZombie.class}));
      this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
      this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, false));
      this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityIronGolem.class, true));
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(35.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.23000000417232513D);
      this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(3.0D);
      this.getAttributeMap().b(a).setValue(this.random.nextDouble() * 0.10000000149011612D);
   }

   protected void h() {
      super.h();
      this.getDataWatcher().a(12, Byte.valueOf((byte)0));
      this.getDataWatcher().a(13, Byte.valueOf((byte)0));
      this.getDataWatcher().a(14, Byte.valueOf((byte)0));
   }

   public int br() {
      int var1 = super.br() + 2;
      if(var1 > 20) {
         var1 = 20;
      }

      return var1;
   }

   public boolean cn() {
      return this.bo;
   }

   public void a(boolean var1) {
      if(this.bo != var1) {
         this.bo = var1;
         if(var1) {
            this.goalSelector.a(1, this.bm);
         } else {
            this.goalSelector.a((PathfinderGoal)this.bm);
         }
      }

   }

   public boolean isBaby() {
      return this.getDataWatcher().getByte(12) == 1;
   }

   protected int getExpValue(EntityHuman var1) {
      if(this.isBaby()) {
         this.b_ = (int)((float)this.b_ * 2.5F);
      }

      return super.getExpValue(var1);
   }

   public void setBaby(boolean var1) {
      this.getDataWatcher().watch(12, Byte.valueOf((byte)(var1?1:0)));
      if(this.world != null && !this.world.isClientSide) {
         AttributeInstance var2 = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
         var2.c(c);
         if(var1) {
            var2.b(c);
         }
      }

      this.n(var1);
   }

   public boolean isVillager() {
      return this.getDataWatcher().getByte(13) == 1;
   }

   public void setVillager(boolean var1) {
      this.getDataWatcher().watch(13, Byte.valueOf((byte)(var1?1:0)));
   }

   public void m() {
      if(this.world.w() && !this.world.isClientSide && !this.isBaby()) {
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

      if(this.au() && this.getGoalTarget() != null && this.vehicle instanceof EntityChicken) {
         ((EntityInsentient)this.vehicle).getNavigation().a(this.getNavigation().j(), 1.5D);
      }

      super.m();
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(super.damageEntity(var1, var2)) {
         EntityLiving var3 = this.getGoalTarget();
         if(var3 == null && var1.getEntity() instanceof EntityLiving) {
            var3 = (EntityLiving)var1.getEntity();
         }

         if(var3 != null && this.world.getDifficulty() == EnumDifficulty.HARD && (double)this.random.nextFloat() < this.getAttributeInstance(a).getValue()) {
            int var4 = MathHelper.floor(this.locX);
            int var5 = MathHelper.floor(this.locY);
            int var6 = MathHelper.floor(this.locZ);
            EntityZombie var7 = new EntityZombie(this.world);

            for(int var8 = 0; var8 < 50; ++var8) {
               int var9 = var4 + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
               int var10 = var5 + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
               int var11 = var6 + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
               if(World.a((IBlockAccess)this.world, (BlockPosition)(new BlockPosition(var9, var10 - 1, var11))) && this.world.getLightLevel(new BlockPosition(var9, var10, var11)) < 10) {
                  var7.setPosition((double)var9, (double)var10, (double)var11);
                  if(!this.world.isPlayerNearby((double)var9, (double)var10, (double)var11, 7.0D) && this.world.a((AxisAlignedBB)var7.getBoundingBox(), (Entity)var7) && this.world.getCubes(var7, var7.getBoundingBox()).isEmpty() && !this.world.containsLiquid(var7.getBoundingBox())) {
                     this.world.addEntity(var7);
                     var7.setGoalTarget(var3);
                     var7.prepare(this.world.E(new BlockPosition(var7)), (GroupDataEntity)null);
                     this.getAttributeInstance(a).b(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, 0));
                     var7.getAttributeInstance(a).b(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, 0));
                     break;
                  }
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void t_() {
      if(!this.world.isClientSide && this.cp()) {
         int var1 = this.cr();
         this.bn -= var1;
         if(this.bn <= 0) {
            this.cq();
         }
      }

      super.t_();
   }

   public boolean r(Entity var1) {
      boolean var2 = super.r(var1);
      if(var2) {
         int var3 = this.world.getDifficulty().a();
         if(this.bA() == null && this.isBurning() && this.random.nextFloat() < (float)var3 * 0.3F) {
            var1.setOnFire(2 * var3);
         }
      }

      return var2;
   }

   protected String z() {
      return "mob.zombie.say";
   }

   protected String bo() {
      return "mob.zombie.hurt";
   }

   protected String bp() {
      return "mob.zombie.death";
   }

   protected void a(BlockPosition var1, Block var2) {
      this.makeSound("mob.zombie.step", 0.15F, 1.0F);
   }

   protected Item getLoot() {
      return Items.ROTTEN_FLESH;
   }

   public EnumMonsterType getMonsterType() {
      return EnumMonsterType.UNDEAD;
   }

   protected void getRareDrop() {
      switch(this.random.nextInt(3)) {
      case 0:
         this.a(Items.IRON_INGOT, 1);
         break;
      case 1:
         this.a(Items.CARROT, 1);
         break;
      case 2:
         this.a(Items.POTATO, 1);
      }

   }

   protected void a(DifficultyDamageScaler var1) {
      super.a(var1);
      if(this.random.nextFloat() < (this.world.getDifficulty() == EnumDifficulty.HARD?0.05F:0.01F)) {
         int var2 = this.random.nextInt(3);
         if(var2 == 0) {
            this.setEquipment(0, new ItemStack(Items.IRON_SWORD));
         } else {
            this.setEquipment(0, new ItemStack(Items.IRON_SHOVEL));
         }
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      if(this.isBaby()) {
         var1.setBoolean("IsBaby", true);
      }

      if(this.isVillager()) {
         var1.setBoolean("IsVillager", true);
      }

      var1.setInt("ConversionTime", this.cp()?this.bn:-1);
      var1.setBoolean("CanBreakDoors", this.cn());
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      if(var1.getBoolean("IsBaby")) {
         this.setBaby(true);
      }

      if(var1.getBoolean("IsVillager")) {
         this.setVillager(true);
      }

      if(var1.hasKeyOfType("ConversionTime", 99) && var1.getInt("ConversionTime") > -1) {
         this.a(var1.getInt("ConversionTime"));
      }

      this.a(var1.getBoolean("CanBreakDoors"));
   }

   public void a(EntityLiving var1) {
      super.a(var1);
      if((this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) && var1 instanceof EntityVillager) {
         if(this.world.getDifficulty() != EnumDifficulty.HARD && this.random.nextBoolean()) {
            return;
         }

         EntityInsentient var2 = (EntityInsentient)var1;
         EntityZombie var3 = new EntityZombie(this.world);
         var3.m(var1);
         this.world.kill(var1);
         var3.prepare(this.world.E(new BlockPosition(var3)), (GroupDataEntity)null);
         var3.setVillager(true);
         if(var1.isBaby()) {
            var3.setBaby(true);
         }

         var3.k(var2.ce());
         if(var2.hasCustomName()) {
            var3.setCustomName(var2.getCustomName());
            var3.setCustomNameVisible(var2.getCustomNameVisible());
         }

         this.world.addEntity(var3);
         this.world.a((EntityHuman)null, 1016, new BlockPosition((int)this.locX, (int)this.locY, (int)this.locZ), 0);
      }

   }

   public float getHeadHeight() {
      float var1 = 1.74F;
      if(this.isBaby()) {
         var1 = (float)((double)var1 - 0.81D);
      }

      return var1;
   }

   protected boolean a(ItemStack var1) {
      return var1.getItem() == Items.EGG && this.isBaby() && this.au()?false:super.a(var1);
   }

   public GroupDataEntity prepare(DifficultyDamageScaler var1, GroupDataEntity var2) {
      Object var7 = super.prepare(var1, var2);
      float var3 = var1.c();
      this.j(this.random.nextFloat() < 0.55F * var3);
      if(var7 == null) {
         var7 = new EntityZombie.GroupDataZombie(this.world.random.nextFloat() < 0.05F, this.world.random.nextFloat() < 0.05F);
      }

      if(var7 instanceof EntityZombie.GroupDataZombie) {
         EntityZombie.GroupDataZombie var4 = (EntityZombie.GroupDataZombie)var7;
         if(var4.b) {
            this.setVillager(true);
         }

         if(var4.a) {
            this.setBaby(true);
            if((double)this.world.random.nextFloat() < 0.05D) {
               List var5 = this.world.a(EntityChicken.class, this.getBoundingBox().grow(5.0D, 3.0D, 5.0D), IEntitySelector.b);
               if(!var5.isEmpty()) {
                  EntityChicken var6 = (EntityChicken)var5.get(0);
                  var6.l(true);
                  this.mount(var6);
               }
            } else if((double)this.world.random.nextFloat() < 0.05D) {
               EntityChicken var10 = new EntityChicken(this.world);
               var10.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, 0.0F);
               var10.prepare(var1, (GroupDataEntity)null);
               var10.l(true);
               this.world.addEntity(var10);
               this.mount(var10);
            }
         }
      }

      this.a(this.random.nextFloat() < var3 * 0.1F);
      this.a(var1);
      this.b(var1);
      if(this.getEquipment(4) == null) {
         Calendar var8 = this.world.Y();
         if(var8.get(2) + 1 == 10 && var8.get(5) == 31 && this.random.nextFloat() < 0.25F) {
            this.setEquipment(4, new ItemStack(this.random.nextFloat() < 0.1F?Blocks.LIT_PUMPKIN:Blocks.PUMPKIN));
            this.dropChances[4] = 0.0F;
         }
      }

      this.getAttributeInstance(GenericAttributes.c).b(new AttributeModifier("Random spawn bonus", this.random.nextDouble() * 0.05000000074505806D, 0));
      double var9 = this.random.nextDouble() * 1.5D * (double)var3;
      if(var9 > 1.0D) {
         this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).b(new AttributeModifier("Random zombie-spawn bonus", var9, 2));
      }

      if(this.random.nextFloat() < var3 * 0.05F) {
         this.getAttributeInstance(a).b(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 0.25D + 0.5D, 0));
         this.getAttributeInstance(GenericAttributes.maxHealth).b(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 3.0D + 1.0D, 2));
         this.a(true);
      }

      return (GroupDataEntity)var7;
   }

   public boolean a(EntityHuman var1) {
      ItemStack var2 = var1.bZ();
      if(var2 != null && var2.getItem() == Items.GOLDEN_APPLE && var2.getData() == 0 && this.isVillager() && this.hasEffect(MobEffectList.WEAKNESS)) {
         if(!var1.abilities.canInstantlyBuild) {
            --var2.count;
         }

         if(var2.count <= 0) {
            var1.inventory.setItem(var1.inventory.itemInHandIndex, (ItemStack)null);
         }

         if(!this.world.isClientSide) {
            this.a(this.random.nextInt(2401) + 3600);
         }

         return true;
      } else {
         return false;
      }
   }

   protected void a(int var1) {
      this.bn = var1;
      this.getDataWatcher().watch(14, Byte.valueOf((byte)1));
      this.removeEffect(MobEffectList.WEAKNESS.id);
      this.addEffect(new MobEffect(MobEffectList.INCREASE_DAMAGE.id, var1, Math.min(this.world.getDifficulty().a() - 1, 0)));
      this.world.broadcastEntityEffect(this, (byte)16);
   }

   protected boolean isTypeNotPersistent() {
      return !this.cp();
   }

   public boolean cp() {
      return this.getDataWatcher().getByte(14) == 1;
   }

   protected void cq() {
      EntityVillager var1 = new EntityVillager(this.world);
      var1.m(this);
      var1.prepare(this.world.E(new BlockPosition(var1)), (GroupDataEntity)null);
      var1.cp();
      if(this.isBaby()) {
         var1.setAgeRaw(-24000);
      }

      this.world.kill(this);
      var1.k(this.ce());
      if(this.hasCustomName()) {
         var1.setCustomName(this.getCustomName());
         var1.setCustomNameVisible(this.getCustomNameVisible());
      }

      this.world.addEntity(var1);
      var1.addEffect(new MobEffect(MobEffectList.CONFUSION.id, 200, 0));
      this.world.a((EntityHuman)null, 1017, new BlockPosition((int)this.locX, (int)this.locY, (int)this.locZ), 0);
   }

   protected int cr() {
      int var1 = 1;
      if(this.random.nextFloat() < 0.01F) {
         int var2 = 0;
         BlockPosition.MutableBlockPosition var3 = new BlockPosition.MutableBlockPosition();

         for(int var4 = (int)this.locX - 4; var4 < (int)this.locX + 4 && var2 < 14; ++var4) {
            for(int var5 = (int)this.locY - 4; var5 < (int)this.locY + 4 && var2 < 14; ++var5) {
               for(int var6 = (int)this.locZ - 4; var6 < (int)this.locZ + 4 && var2 < 14; ++var6) {
                  Block var7 = this.world.getType(var3.c(var4, var5, var6)).getBlock();
                  if(var7 == Blocks.IRON_BARS || var7 == Blocks.BED) {
                     if(this.random.nextFloat() < 0.3F) {
                        ++var1;
                     }

                     ++var2;
                  }
               }
            }
         }
      }

      return var1;
   }

   public void n(boolean var1) {
      this.a(var1?0.5F:1.0F);
   }

   protected final void setSize(float var1, float var2) {
      boolean var3 = this.bp > 0.0F && this.bq > 0.0F;
      this.bp = var1;
      this.bq = var2;
      if(!var3) {
         this.a(1.0F);
      }

   }

   protected final void a(float var1) {
      super.setSize(this.bp * var1, this.bq * var1);
   }

   public double am() {
      return this.isBaby()?0.0D:-0.35D;
   }

   public void die(DamageSource var1) {
      super.die(var1);
      if(var1.getEntity() instanceof EntityCreeper && !(this instanceof EntityPigZombie) && ((EntityCreeper)var1.getEntity()).isPowered() && ((EntityCreeper)var1.getEntity()).cp()) {
         ((EntityCreeper)var1.getEntity()).cq();
         this.a(new ItemStack(Items.SKULL, 1, 2), 0.0F);
      }

   }

   static {
      c = new AttributeModifier(b, "Baby speed boost", 0.5D, 1);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   class GroupDataZombie implements GroupDataEntity {
      public boolean a;
      public boolean b;

      private GroupDataZombie(boolean var2, boolean var3) {
         this.a = false;
         this.b = false;
         this.a = var2;
         this.b = var3;
      }

      // $FF: synthetic method
      GroupDataZombie(boolean var2, boolean var3, EntityZombie.SyntheticClass_1 var4) {
         this();
      }
   }
}
