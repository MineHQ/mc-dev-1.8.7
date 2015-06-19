package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockCarrots;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.ControllerJump;
import net.minecraft.server.ControllerMove;
import net.minecraft.server.DamageSource;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAgeable;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathEntity;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalAvoidTarget;
import net.minecraft.server.PathfinderGoalBreed;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalGotoTarget;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalPanic;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalTempt;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class EntityRabbit extends EntityAnimal {
   private EntityRabbit.PathfinderGoalRabbitAvoidTarget<EntityWolf> bm;
   private int bo = 0;
   private int bp = 0;
   private boolean bq = false;
   private boolean br = false;
   private int bs = 0;
   private EntityRabbit.EnumRabbitState bt;
   private int bu;
   private EntityHuman bv;

   public EntityRabbit(World var1) {
      super(var1);
      this.bt = EntityRabbit.EnumRabbitState.HOP;
      this.bu = 0;
      this.bv = null;
      this.setSize(0.6F, 0.7F);
      this.g = new EntityRabbit.ControllerJumpRabbit(this);
      this.moveController = new EntityRabbit.ControllerMoveRabbit(this);
      ((Navigation)this.getNavigation()).a(true);
      this.navigation.a(2.5F);
      this.goalSelector.a(1, new PathfinderGoalFloat(this));
      this.goalSelector.a(1, new EntityRabbit.PathfinderGoalRabbitPanic(this, 1.33D));
      this.goalSelector.a(2, new PathfinderGoalTempt(this, 1.0D, Items.CARROT, false));
      this.goalSelector.a(2, new PathfinderGoalTempt(this, 1.0D, Items.GOLDEN_CARROT, false));
      this.goalSelector.a(2, new PathfinderGoalTempt(this, 1.0D, Item.getItemOf(Blocks.YELLOW_FLOWER), false));
      this.goalSelector.a(3, new PathfinderGoalBreed(this, 0.8D));
      this.goalSelector.a(5, new EntityRabbit.PathfinderGoalEatCarrots(this));
      this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.6D));
      this.goalSelector.a(11, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 10.0F));
      this.bm = new EntityRabbit.PathfinderGoalRabbitAvoidTarget(this, EntityWolf.class, 16.0F, 1.33D, 1.33D);
      this.goalSelector.a(4, this.bm);
      this.b(0.0D);
   }

   protected float bE() {
      return this.moveController.a() && this.moveController.e() > this.locY + 0.5D?0.5F:this.bt.b();
   }

   public void a(EntityRabbit.EnumRabbitState var1) {
      this.bt = var1;
   }

   public void b(double var1) {
      this.getNavigation().a(var1);
      this.moveController.a(this.moveController.d(), this.moveController.e(), this.moveController.f(), var1);
   }

   public void a(boolean var1, EntityRabbit.EnumRabbitState var2) {
      super.i(var1);
      if(!var1) {
         if(this.bt == EntityRabbit.EnumRabbitState.ATTACK) {
            this.bt = EntityRabbit.EnumRabbitState.HOP;
         }
      } else {
         this.b(1.5D * (double)var2.a());
         this.makeSound(this.cm(), this.bB(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
      }

      this.bq = var1;
   }

   public void b(EntityRabbit.EnumRabbitState var1) {
      this.a(true, var1);
      this.bp = var1.d();
      this.bo = 0;
   }

   public boolean cl() {
      return this.bq;
   }

   protected void h() {
      super.h();
      this.datawatcher.a(18, Byte.valueOf((byte)0));
   }

   public void E() {
      if(this.moveController.b() > 0.8D) {
         this.a(EntityRabbit.EnumRabbitState.SPRINT);
      } else if(this.bt != EntityRabbit.EnumRabbitState.ATTACK) {
         this.a(EntityRabbit.EnumRabbitState.HOP);
      }

      if(this.bs > 0) {
         --this.bs;
      }

      if(this.bu > 0) {
         this.bu -= this.random.nextInt(3);
         if(this.bu < 0) {
            this.bu = 0;
         }
      }

      if(this.onGround) {
         if(!this.br) {
            this.a(false, EntityRabbit.EnumRabbitState.NONE);
            this.cw();
         }

         if(this.getRabbitType() == 99 && this.bs == 0) {
            EntityLiving var1 = this.getGoalTarget();
            if(var1 != null && this.h(var1) < 16.0D) {
               this.a(var1.locX, var1.locZ);
               this.moveController.a(var1.locX, var1.locY, var1.locZ, this.moveController.b());
               this.b(EntityRabbit.EnumRabbitState.ATTACK);
               this.br = true;
            }
         }

         EntityRabbit.ControllerJumpRabbit var4 = (EntityRabbit.ControllerJumpRabbit)this.g;
         if(!var4.c()) {
            if(this.moveController.a() && this.bs == 0) {
               PathEntity var2 = this.navigation.j();
               Vec3D var3 = new Vec3D(this.moveController.d(), this.moveController.e(), this.moveController.f());
               if(var2 != null && var2.e() < var2.d()) {
                  var3 = var2.a((Entity)this);
               }

               this.a(var3.a, var3.c);
               this.b(this.bt);
            }
         } else if(!var4.d()) {
            this.ct();
         }
      }

      this.br = this.onGround;
   }

   public void Y() {
   }

   private void a(double var1, double var3) {
      this.yaw = (float)(MathHelper.b(var3 - this.locZ, var1 - this.locX) * 180.0D / 3.1415927410125732D) - 90.0F;
   }

   private void ct() {
      ((EntityRabbit.ControllerJumpRabbit)this.g).a(true);
   }

   private void cu() {
      ((EntityRabbit.ControllerJumpRabbit)this.g).a(false);
   }

   private void cv() {
      this.bs = this.co();
   }

   private void cw() {
      this.cv();
      this.cu();
   }

   public void m() {
      super.m();
      if(this.bo != this.bp) {
         if(this.bo == 0 && !this.world.isClientSide) {
            this.world.broadcastEntityEffect(this, (byte)1);
         }

         ++this.bo;
      } else if(this.bp != 0) {
         this.bo = 0;
         this.bp = 0;
      }

   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(10.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.30000001192092896D);
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("RabbitType", this.getRabbitType());
      var1.setInt("MoreCarrotTicks", this.bu);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.setRabbitType(var1.getInt("RabbitType"));
      this.bu = var1.getInt("MoreCarrotTicks");
   }

   protected String cm() {
      return "mob.rabbit.hop";
   }

   protected String z() {
      return "mob.rabbit.idle";
   }

   protected String bo() {
      return "mob.rabbit.hurt";
   }

   protected String bp() {
      return "mob.rabbit.death";
   }

   public boolean r(Entity var1) {
      if(this.getRabbitType() == 99) {
         this.makeSound("mob.attack", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
         return var1.damageEntity(DamageSource.mobAttack(this), 8.0F);
      } else {
         return var1.damageEntity(DamageSource.mobAttack(this), 3.0F);
      }
   }

   public int br() {
      return this.getRabbitType() == 99?8:super.br();
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      return this.isInvulnerable(var1)?false:super.damageEntity(var1, var2);
   }

   protected void getRareDrop() {
      this.a(new ItemStack(Items.RABBIT_FOOT, 1), 0.0F);
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      int var3 = this.random.nextInt(2) + this.random.nextInt(1 + var2);

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         this.a(Items.RABBIT_HIDE, 1);
      }

      var3 = this.random.nextInt(2);

      for(var4 = 0; var4 < var3; ++var4) {
         if(this.isBurning()) {
            this.a(Items.COOKED_RABBIT, 1);
         } else {
            this.a(Items.RABBIT, 1);
         }
      }

   }

   private boolean a(Item var1) {
      return var1 == Items.CARROT || var1 == Items.GOLDEN_CARROT || var1 == Item.getItemOf(Blocks.YELLOW_FLOWER);
   }

   public EntityRabbit b(EntityAgeable var1) {
      EntityRabbit var2 = new EntityRabbit(this.world);
      if(var1 instanceof EntityRabbit) {
         var2.setRabbitType(this.random.nextBoolean()?this.getRabbitType():((EntityRabbit)var1).getRabbitType());
      }

      return var2;
   }

   public boolean d(ItemStack var1) {
      return var1 != null && this.a(var1.getItem());
   }

   public int getRabbitType() {
      return this.datawatcher.getByte(18);
   }

   public void setRabbitType(int var1) {
      if(var1 == 99) {
         this.goalSelector.a((PathfinderGoal)this.bm);
         this.goalSelector.a(4, new EntityRabbit.PathfinderGoalKillerRabbitMeleeAttack(this));
         this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
         this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
         this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityWolf.class, true));
         if(!this.hasCustomName()) {
            this.setCustomName(LocaleI18n.get("entity.KillerBunny.name"));
         }
      }

      this.datawatcher.watch(18, Byte.valueOf((byte)var1));
   }

   public GroupDataEntity prepare(DifficultyDamageScaler var1, GroupDataEntity var2) {
      Object var5 = super.prepare(var1, var2);
      int var3 = this.random.nextInt(6);
      boolean var4 = false;
      if(var5 instanceof EntityRabbit.GroupDataRabbit) {
         var3 = ((EntityRabbit.GroupDataRabbit)var5).a;
         var4 = true;
      } else {
         var5 = new EntityRabbit.GroupDataRabbit(var3);
      }

      this.setRabbitType(var3);
      if(var4) {
         this.setAgeRaw(-24000);
      }

      return (GroupDataEntity)var5;
   }

   private boolean cx() {
      return this.bu == 0;
   }

   protected int co() {
      return this.bt.c();
   }

   protected void cp() {
      this.world.addParticle(EnumParticle.BLOCK_DUST, this.locX + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, this.locY + 0.5D + (double)(this.random.nextFloat() * this.length), this.locZ + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, 0.0D, 0.0D, 0.0D, new int[]{Block.getCombinedId(Blocks.CARROTS.fromLegacyData(7))});
      this.bu = 100;
   }

   // $FF: synthetic method
   public EntityAgeable createChild(EntityAgeable var1) {
      return this.b(var1);
   }

   static enum EnumRabbitState {
      NONE(0.0F, 0.0F, 30, 1),
      HOP(0.8F, 0.2F, 20, 10),
      STEP(1.0F, 0.45F, 14, 14),
      SPRINT(1.75F, 0.4F, 1, 8),
      ATTACK(2.0F, 0.7F, 7, 8);

      private final float f;
      private final float g;
      private final int h;
      private final int i;

      private EnumRabbitState(float var3, float var4, int var5, int var6) {
         this.f = var3;
         this.g = var4;
         this.h = var5;
         this.i = var6;
      }

      public float a() {
         return this.f;
      }

      public float b() {
         return this.g;
      }

      public int c() {
         return this.h;
      }

      public int d() {
         return this.i;
      }
   }

   static class PathfinderGoalKillerRabbitMeleeAttack extends PathfinderGoalMeleeAttack {
      public PathfinderGoalKillerRabbitMeleeAttack(EntityRabbit var1) {
         super(var1, EntityLiving.class, 1.4D, true);
      }

      protected double a(EntityLiving var1) {
         return (double)(4.0F + var1.width);
      }
   }

   static class PathfinderGoalRabbitPanic extends PathfinderGoalPanic {
      private EntityRabbit b;

      public PathfinderGoalRabbitPanic(EntityRabbit var1, double var2) {
         super(var1, var2);
         this.b = var1;
      }

      public void e() {
         super.e();
         this.b.b(this.a);
      }
   }

   static class PathfinderGoalEatCarrots extends PathfinderGoalGotoTarget {
      private final EntityRabbit c;
      private boolean d;
      private boolean e = false;

      public PathfinderGoalEatCarrots(EntityRabbit var1) {
         super(var1, 0.699999988079071D, 16);
         this.c = var1;
      }

      public boolean a() {
         if(this.a <= 0) {
            if(!this.c.world.getGameRules().getBoolean("mobGriefing")) {
               return false;
            }

            this.e = false;
            this.d = this.c.cx();
         }

         return super.a();
      }

      public boolean b() {
         return this.e && super.b();
      }

      public void c() {
         super.c();
      }

      public void d() {
         super.d();
      }

      public void e() {
         super.e();
         this.c.getControllerLook().a((double)this.b.getX() + 0.5D, (double)(this.b.getY() + 1), (double)this.b.getZ() + 0.5D, 10.0F, (float)this.c.bQ());
         if(this.f()) {
            World var1 = this.c.world;
            BlockPosition var2 = this.b.up();
            IBlockData var3 = var1.getType(var2);
            Block var4 = var3.getBlock();
            if(this.e && var4 instanceof BlockCarrots && ((Integer)var3.get(BlockCarrots.AGE)).intValue() == 7) {
               var1.setTypeAndData(var2, Blocks.AIR.getBlockData(), 2);
               var1.setAir(var2, true);
               this.c.cp();
            }

            this.e = false;
            this.a = 10;
         }

      }

      protected boolean a(World var1, BlockPosition var2) {
         Block var3 = var1.getType(var2).getBlock();
         if(var3 == Blocks.FARMLAND) {
            var2 = var2.up();
            IBlockData var4 = var1.getType(var2);
            var3 = var4.getBlock();
            if(var3 instanceof BlockCarrots && ((Integer)var4.get(BlockCarrots.AGE)).intValue() == 7 && this.d && !this.e) {
               this.e = true;
               return true;
            }
         }

         return false;
      }
   }

   static class PathfinderGoalRabbitAvoidTarget<T extends Entity> extends PathfinderGoalAvoidTarget<T> {
      private EntityRabbit c;

      public PathfinderGoalRabbitAvoidTarget(EntityRabbit var1, Class<T> var2, float var3, double var4, double var6) {
         super(var1, var2, var3, var4, var6);
         this.c = var1;
      }

      public void e() {
         super.e();
      }
   }

   static class ControllerMoveRabbit extends ControllerMove {
      private EntityRabbit g;

      public ControllerMoveRabbit(EntityRabbit var1) {
         super(var1);
         this.g = var1;
      }

      public void c() {
         if(this.g.onGround && !this.g.cl()) {
            this.g.b(0.0D);
         }

         super.c();
      }
   }

   public class ControllerJumpRabbit extends ControllerJump {
      private EntityRabbit c;
      private boolean d = false;

      public ControllerJumpRabbit(EntityRabbit var2) {
         super(var2);
         this.c = var2;
      }

      public boolean c() {
         return this.a;
      }

      public boolean d() {
         return this.d;
      }

      public void a(boolean var1) {
         this.d = var1;
      }

      public void b() {
         if(this.a) {
            this.c.b(EntityRabbit.EnumRabbitState.STEP);
            this.a = false;
         }

      }
   }

   public static class GroupDataRabbit implements GroupDataEntity {
      public int a;

      public GroupDataRabbit(int var1) {
         this.a = var1;
      }
   }
}
