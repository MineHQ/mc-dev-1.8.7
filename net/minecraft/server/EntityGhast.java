package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.AchievementList;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ControllerMove;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityFlying;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLargeFireball;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IMonster;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalTargetNearestPlayer;
import net.minecraft.server.Statistic;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class EntityGhast extends EntityFlying implements IMonster {
   private int a = 1;

   public EntityGhast(World var1) {
      super(var1);
      this.setSize(4.0F, 4.0F);
      this.fireProof = true;
      this.b_ = 5;
      this.moveController = new EntityGhast.ControllerGhast(this);
      this.goalSelector.a(5, new EntityGhast.PathfinderGoalGhastIdleMove(this));
      this.goalSelector.a(7, new EntityGhast.PathfinderGoalGhastMoveTowardsTarget(this));
      this.goalSelector.a(7, new EntityGhast.PathfinderGoalGhastAttackTarget(this));
      this.targetSelector.a(1, new PathfinderGoalTargetNearestPlayer(this));
   }

   public void a(boolean var1) {
      this.datawatcher.watch(16, Byte.valueOf((byte)(var1?1:0)));
   }

   public int cf() {
      return this.a;
   }

   public void t_() {
      super.t_();
      if(!this.world.isClientSide && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
         this.die();
      }

   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else if("fireball".equals(var1.p()) && var1.getEntity() instanceof EntityHuman) {
         super.damageEntity(var1, 1000.0F);
         ((EntityHuman)var1.getEntity()).b((Statistic)AchievementList.z);
         return true;
      } else {
         return super.damageEntity(var1, var2);
      }
   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, Byte.valueOf((byte)0));
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(10.0D);
      this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(100.0D);
   }

   protected String z() {
      return "mob.ghast.moan";
   }

   protected String bo() {
      return "mob.ghast.scream";
   }

   protected String bp() {
      return "mob.ghast.death";
   }

   protected Item getLoot() {
      return Items.GUNPOWDER;
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      int var3 = this.random.nextInt(2) + this.random.nextInt(1 + var2);

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         this.a(Items.GHAST_TEAR, 1);
      }

      var3 = this.random.nextInt(3) + this.random.nextInt(1 + var2);

      for(var4 = 0; var4 < var3; ++var4) {
         this.a(Items.GUNPOWDER, 1);
      }

   }

   protected float bB() {
      return 10.0F;
   }

   public boolean bR() {
      return this.random.nextInt(20) == 0 && super.bR() && this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
   }

   public int bV() {
      return 1;
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("ExplosionPower", this.a);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      if(var1.hasKeyOfType("ExplosionPower", 99)) {
         this.a = var1.getInt("ExplosionPower");
      }

   }

   public float getHeadHeight() {
      return 2.6F;
   }

   static class PathfinderGoalGhastAttackTarget extends PathfinderGoal {
      private EntityGhast b;
      public int a;

      public PathfinderGoalGhastAttackTarget(EntityGhast var1) {
         this.b = var1;
      }

      public boolean a() {
         return this.b.getGoalTarget() != null;
      }

      public void c() {
         this.a = 0;
      }

      public void d() {
         this.b.a(false);
      }

      public void e() {
         EntityLiving var1 = this.b.getGoalTarget();
         double var2 = 64.0D;
         if(var1.h(this.b) < var2 * var2 && this.b.hasLineOfSight(var1)) {
            World var4 = this.b.world;
            ++this.a;
            if(this.a == 10) {
               var4.a((EntityHuman)null, 1007, new BlockPosition(this.b), 0);
            }

            if(this.a == 20) {
               double var5 = 4.0D;
               Vec3D var7 = this.b.d(1.0F);
               double var8 = var1.locX - (this.b.locX + var7.a * var5);
               double var10 = var1.getBoundingBox().b + (double)(var1.length / 2.0F) - (0.5D + this.b.locY + (double)(this.b.length / 2.0F));
               double var12 = var1.locZ - (this.b.locZ + var7.c * var5);
               var4.a((EntityHuman)null, 1008, new BlockPosition(this.b), 0);
               EntityLargeFireball var14 = new EntityLargeFireball(var4, this.b, var8, var10, var12);
               var14.yield = this.b.cf();
               var14.locX = this.b.locX + var7.a * var5;
               var14.locY = this.b.locY + (double)(this.b.length / 2.0F) + 0.5D;
               var14.locZ = this.b.locZ + var7.c * var5;
               var4.addEntity(var14);
               this.a = -40;
            }
         } else if(this.a > 0) {
            --this.a;
         }

         this.b.a(this.a > 10);
      }
   }

   static class PathfinderGoalGhastMoveTowardsTarget extends PathfinderGoal {
      private EntityGhast a;

      public PathfinderGoalGhastMoveTowardsTarget(EntityGhast var1) {
         this.a = var1;
         this.a(2);
      }

      public boolean a() {
         return true;
      }

      public void e() {
         if(this.a.getGoalTarget() == null) {
            this.a.aI = this.a.yaw = -((float)MathHelper.b(this.a.motX, this.a.motZ)) * 180.0F / 3.1415927F;
         } else {
            EntityLiving var1 = this.a.getGoalTarget();
            double var2 = 64.0D;
            if(var1.h(this.a) < var2 * var2) {
               double var4 = var1.locX - this.a.locX;
               double var6 = var1.locZ - this.a.locZ;
               this.a.aI = this.a.yaw = -((float)MathHelper.b(var4, var6)) * 180.0F / 3.1415927F;
            }
         }

      }
   }

   static class PathfinderGoalGhastIdleMove extends PathfinderGoal {
      private EntityGhast a;

      public PathfinderGoalGhastIdleMove(EntityGhast var1) {
         this.a = var1;
         this.a(1);
      }

      public boolean a() {
         ControllerMove var1 = this.a.getControllerMove();
         if(!var1.a()) {
            return true;
         } else {
            double var2 = var1.d() - this.a.locX;
            double var4 = var1.e() - this.a.locY;
            double var6 = var1.f() - this.a.locZ;
            double var8 = var2 * var2 + var4 * var4 + var6 * var6;
            return var8 < 1.0D || var8 > 3600.0D;
         }
      }

      public boolean b() {
         return false;
      }

      public void c() {
         Random var1 = this.a.bc();
         double var2 = this.a.locX + (double)((var1.nextFloat() * 2.0F - 1.0F) * 16.0F);
         double var4 = this.a.locY + (double)((var1.nextFloat() * 2.0F - 1.0F) * 16.0F);
         double var6 = this.a.locZ + (double)((var1.nextFloat() * 2.0F - 1.0F) * 16.0F);
         this.a.getControllerMove().a(var2, var4, var6, 1.0D);
      }
   }

   static class ControllerGhast extends ControllerMove {
      private EntityGhast g;
      private int h;

      public ControllerGhast(EntityGhast var1) {
         super(var1);
         this.g = var1;
      }

      public void c() {
         if(this.f) {
            double var1 = this.b - this.g.locX;
            double var3 = this.c - this.g.locY;
            double var5 = this.d - this.g.locZ;
            double var7 = var1 * var1 + var3 * var3 + var5 * var5;
            if(this.h-- <= 0) {
               this.h += this.g.bc().nextInt(5) + 2;
               var7 = (double)MathHelper.sqrt(var7);
               if(this.b(this.b, this.c, this.d, var7)) {
                  this.g.motX += var1 / var7 * 0.1D;
                  this.g.motY += var3 / var7 * 0.1D;
                  this.g.motZ += var5 / var7 * 0.1D;
               } else {
                  this.f = false;
               }
            }

         }
      }

      private boolean b(double var1, double var3, double var5, double var7) {
         double var9 = (var1 - this.g.locX) / var7;
         double var11 = (var3 - this.g.locY) / var7;
         double var13 = (var5 - this.g.locZ) / var7;
         AxisAlignedBB var15 = this.g.getBoundingBox();

         for(int var16 = 1; (double)var16 < var7; ++var16) {
            var15 = var15.c(var9, var11, var13);
            if(!this.g.world.getCubes(this.g, var15).isEmpty()) {
               return false;
            }
         }

         return true;
      }
   }
}
