package net.minecraft.server;

import net.minecraft.server.BiomeBase;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Chunk;
import net.minecraft.server.ControllerMove;
import net.minecraft.server.DamageSource;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.IMonster;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalNearestAttackableTargetInsentient;
import net.minecraft.server.PathfinderGoalTargetNearestPlayer;
import net.minecraft.server.World;
import net.minecraft.server.WorldType;

public class EntitySlime extends EntityInsentient implements IMonster {
   public float a;
   public float b;
   public float c;
   private boolean bk;

   public EntitySlime(World var1) {
      super(var1);
      this.moveController = new EntitySlime.ControllerMoveSlime(this);
      this.goalSelector.a(1, new EntitySlime.PathfinderGoalSlimeRandomJump(this));
      this.goalSelector.a(2, new EntitySlime.PathfinderGoalSlimeNearestPlayer(this));
      this.goalSelector.a(3, new EntitySlime.PathfinderGoalSlimeRandomDirection(this));
      this.goalSelector.a(5, new EntitySlime.PathfinderGoalSlimeIdle(this));
      this.targetSelector.a(1, new PathfinderGoalTargetNearestPlayer(this));
      this.targetSelector.a(3, new PathfinderGoalNearestAttackableTargetInsentient(this, EntityIronGolem.class));
   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, Byte.valueOf((byte)1));
   }

   public void setSize(int var1) {
      this.datawatcher.watch(16, Byte.valueOf((byte)var1));
      this.setSize(0.51000005F * (float)var1, 0.51000005F * (float)var1);
      this.setPosition(this.locX, this.locY, this.locZ);
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue((double)(var1 * var1));
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue((double)(0.2F + 0.1F * (float)var1));
      this.setHealth(this.getMaxHealth());
      this.b_ = var1;
   }

   public int getSize() {
      return this.datawatcher.getByte(16);
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("Size", this.getSize() - 1);
      var1.setBoolean("wasOnGround", this.bk);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      int var2 = var1.getInt("Size");
      if(var2 < 0) {
         var2 = 0;
      }

      this.setSize(var2 + 1);
      this.bk = var1.getBoolean("wasOnGround");
   }

   protected EnumParticle n() {
      return EnumParticle.SLIME;
   }

   protected String ck() {
      return "mob.slime." + (this.getSize() > 1?"big":"small");
   }

   public void t_() {
      if(!this.world.isClientSide && this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSize() > 0) {
         this.dead = true;
      }

      this.b += (this.a - this.b) * 0.5F;
      this.c = this.b;
      super.t_();
      if(this.onGround && !this.bk) {
         int var1 = this.getSize();

         for(int var2 = 0; var2 < var1 * 8; ++var2) {
            float var3 = this.random.nextFloat() * 3.1415927F * 2.0F;
            float var4 = this.random.nextFloat() * 0.5F + 0.5F;
            float var5 = MathHelper.sin(var3) * (float)var1 * 0.5F * var4;
            float var6 = MathHelper.cos(var3) * (float)var1 * 0.5F * var4;
            World var10000 = this.world;
            EnumParticle var10001 = this.n();
            double var10002 = this.locX + (double)var5;
            double var10004 = this.locZ + (double)var6;
            var10000.addParticle(var10001, var10002, this.getBoundingBox().b, var10004, 0.0D, 0.0D, 0.0D, new int[0]);
         }

         if(this.cl()) {
            this.makeSound(this.ck(), this.bB(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
         }

         this.a = -0.5F;
      } else if(!this.onGround && this.bk) {
         this.a = 1.0F;
      }

      this.bk = this.onGround;
      this.ch();
   }

   protected void ch() {
      this.a *= 0.6F;
   }

   protected int cg() {
      return this.random.nextInt(20) + 10;
   }

   protected EntitySlime cf() {
      return new EntitySlime(this.world);
   }

   public void i(int var1) {
      if(var1 == 16) {
         int var2 = this.getSize();
         this.setSize(0.51000005F * (float)var2, 0.51000005F * (float)var2);
         this.yaw = this.aK;
         this.aI = this.aK;
         if(this.V() && this.random.nextInt(20) == 0) {
            this.X();
         }
      }

      super.i(var1);
   }

   public void die() {
      int var1 = this.getSize();
      if(!this.world.isClientSide && var1 > 1 && this.getHealth() <= 0.0F) {
         int var2 = 2 + this.random.nextInt(3);

         for(int var3 = 0; var3 < var2; ++var3) {
            float var4 = ((float)(var3 % 2) - 0.5F) * (float)var1 / 4.0F;
            float var5 = ((float)(var3 / 2) - 0.5F) * (float)var1 / 4.0F;
            EntitySlime var6 = this.cf();
            if(this.hasCustomName()) {
               var6.setCustomName(this.getCustomName());
            }

            if(this.isPersistent()) {
               var6.bX();
            }

            var6.setSize(var1 / 2);
            var6.setPositionRotation(this.locX + (double)var4, this.locY + 0.5D, this.locZ + (double)var5, this.random.nextFloat() * 360.0F, 0.0F);
            this.world.addEntity(var6);
         }
      }

      super.die();
   }

   public void collide(Entity var1) {
      super.collide(var1);
      if(var1 instanceof EntityIronGolem && this.ci()) {
         this.e((EntityLiving)var1);
      }

   }

   public void d(EntityHuman var1) {
      if(this.ci()) {
         this.e(var1);
      }

   }

   protected void e(EntityLiving var1) {
      int var2 = this.getSize();
      if(this.hasLineOfSight(var1) && this.h(var1) < 0.6D * (double)var2 * 0.6D * (double)var2 && var1.damageEntity(DamageSource.mobAttack(this), (float)this.cj())) {
         this.makeSound("mob.attack", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
         this.a(this, var1);
      }

   }

   public float getHeadHeight() {
      return 0.625F * this.length;
   }

   protected boolean ci() {
      return this.getSize() > 1;
   }

   protected int cj() {
      return this.getSize();
   }

   protected String bo() {
      return "mob.slime." + (this.getSize() > 1?"big":"small");
   }

   protected String bp() {
      return "mob.slime." + (this.getSize() > 1?"big":"small");
   }

   protected Item getLoot() {
      return this.getSize() == 1?Items.SLIME:null;
   }

   public boolean bR() {
      BlockPosition var1 = new BlockPosition(MathHelper.floor(this.locX), 0, MathHelper.floor(this.locZ));
      Chunk var2 = this.world.getChunkAtWorldCoords(var1);
      if(this.world.getWorldData().getType() == WorldType.FLAT && this.random.nextInt(4) != 1) {
         return false;
      } else {
         if(this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
            BiomeBase var3 = this.world.getBiome(var1);
            if(var3 == BiomeBase.SWAMPLAND && this.locY > 50.0D && this.locY < 70.0D && this.random.nextFloat() < 0.5F && this.random.nextFloat() < this.world.y() && this.world.getLightLevel(new BlockPosition(this)) <= this.random.nextInt(8)) {
               return super.bR();
            }

            if(this.random.nextInt(10) == 0 && var2.a(987234911L).nextInt(10) == 0 && this.locY < 40.0D) {
               return super.bR();
            }
         }

         return false;
      }
   }

   protected float bB() {
      return 0.4F * (float)this.getSize();
   }

   public int bQ() {
      return 0;
   }

   protected boolean cn() {
      return this.getSize() > 0;
   }

   protected boolean cl() {
      return this.getSize() > 2;
   }

   protected void bF() {
      this.motY = 0.41999998688697815D;
      this.ai = true;
   }

   public GroupDataEntity prepare(DifficultyDamageScaler var1, GroupDataEntity var2) {
      int var3 = this.random.nextInt(3);
      if(var3 < 2 && this.random.nextFloat() < 0.5F * var1.c()) {
         ++var3;
      }

      int var4 = 1 << var3;
      this.setSize(var4);
      return super.prepare(var1, var2);
   }

   static class PathfinderGoalSlimeIdle extends PathfinderGoal {
      private EntitySlime a;

      public PathfinderGoalSlimeIdle(EntitySlime var1) {
         this.a = var1;
         this.a(5);
      }

      public boolean a() {
         return true;
      }

      public void e() {
         ((EntitySlime.ControllerMoveSlime)this.a.getControllerMove()).a(1.0D);
      }
   }

   static class PathfinderGoalSlimeRandomJump extends PathfinderGoal {
      private EntitySlime a;

      public PathfinderGoalSlimeRandomJump(EntitySlime var1) {
         this.a = var1;
         this.a(5);
         ((Navigation)var1.getNavigation()).d(true);
      }

      public boolean a() {
         return this.a.V() || this.a.ab();
      }

      public void e() {
         if(this.a.bc().nextFloat() < 0.8F) {
            this.a.getControllerJump().a();
         }

         ((EntitySlime.ControllerMoveSlime)this.a.getControllerMove()).a(1.2D);
      }
   }

   static class PathfinderGoalSlimeRandomDirection extends PathfinderGoal {
      private EntitySlime a;
      private float b;
      private int c;

      public PathfinderGoalSlimeRandomDirection(EntitySlime var1) {
         this.a = var1;
         this.a(2);
      }

      public boolean a() {
         return this.a.getGoalTarget() == null && (this.a.onGround || this.a.V() || this.a.ab());
      }

      public void e() {
         if(--this.c <= 0) {
            this.c = 40 + this.a.bc().nextInt(60);
            this.b = (float)this.a.bc().nextInt(360);
         }

         ((EntitySlime.ControllerMoveSlime)this.a.getControllerMove()).a(this.b, false);
      }
   }

   static class PathfinderGoalSlimeNearestPlayer extends PathfinderGoal {
      private EntitySlime a;
      private int b;

      public PathfinderGoalSlimeNearestPlayer(EntitySlime var1) {
         this.a = var1;
         this.a(2);
      }

      public boolean a() {
         EntityLiving var1 = this.a.getGoalTarget();
         return var1 == null?false:(!var1.isAlive()?false:!(var1 instanceof EntityHuman) || !((EntityHuman)var1).abilities.isInvulnerable);
      }

      public void c() {
         this.b = 300;
         super.c();
      }

      public boolean b() {
         EntityLiving var1 = this.a.getGoalTarget();
         return var1 == null?false:(!var1.isAlive()?false:(var1 instanceof EntityHuman && ((EntityHuman)var1).abilities.isInvulnerable?false:--this.b > 0));
      }

      public void e() {
         this.a.a(this.a.getGoalTarget(), 10.0F, 10.0F);
         ((EntitySlime.ControllerMoveSlime)this.a.getControllerMove()).a(this.a.yaw, this.a.ci());
      }
   }

   static class ControllerMoveSlime extends ControllerMove {
      private float g;
      private int h;
      private EntitySlime i;
      private boolean j;

      public ControllerMoveSlime(EntitySlime var1) {
         super(var1);
         this.i = var1;
      }

      public void a(float var1, boolean var2) {
         this.g = var1;
         this.j = var2;
      }

      public void a(double var1) {
         this.e = var1;
         this.f = true;
      }

      public void c() {
         this.a.yaw = this.a(this.a.yaw, this.g, 30.0F);
         this.a.aK = this.a.yaw;
         this.a.aI = this.a.yaw;
         if(!this.f) {
            this.a.n(0.0F);
         } else {
            this.f = false;
            if(this.a.onGround) {
               this.a.k((float)(this.e * this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
               if(this.h-- <= 0) {
                  this.h = this.i.cg();
                  if(this.j) {
                     this.h /= 3;
                  }

                  this.i.getControllerJump().a();
                  if(this.i.cn()) {
                     this.i.makeSound(this.i.ck(), this.i.bB(), ((this.i.bc().nextFloat() - this.i.bc().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                  }
               } else {
                  this.i.aZ = this.i.ba = 0.0F;
                  this.a.k(0.0F);
               }
            } else {
               this.a.k((float)(this.e * this.a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
            }

         }
      }
   }
}
