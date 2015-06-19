package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AchievementList;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntityWitherSkull;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IEntitySelector;
import net.minecraft.server.IRangedEntity;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MobEffect;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoalArrowAttack;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.Statistic;
import net.minecraft.server.World;

public class EntityWither extends EntityMonster implements IRangedEntity {
   private float[] a = new float[2];
   private float[] b = new float[2];
   private float[] c = new float[2];
   private float[] bm = new float[2];
   private int[] bn = new int[2];
   private int[] bo = new int[2];
   private int bp;
   private static final Predicate<Entity> bq = new Predicate() {
      public boolean a(Entity var1) {
         return var1 instanceof EntityLiving && ((EntityLiving)var1).getMonsterType() != EnumMonsterType.UNDEAD;
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((Entity)var1);
      }
   };

   public EntityWither(World var1) {
      super(var1);
      this.setHealth(this.getMaxHealth());
      this.setSize(0.9F, 3.5F);
      this.fireProof = true;
      ((Navigation)this.getNavigation()).d(true);
      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(2, new PathfinderGoalArrowAttack(this, 1.0D, 40, 20.0F));
      this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
      this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
      this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
      this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityInsentient.class, 0, false, false, bq));
      this.b_ = 50;
   }

   protected void h() {
      super.h();
      this.datawatcher.a(17, new Integer(0));
      this.datawatcher.a(18, new Integer(0));
      this.datawatcher.a(19, new Integer(0));
      this.datawatcher.a(20, new Integer(0));
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("Invul", this.cl());
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.r(var1.getInt("Invul"));
   }

   protected String z() {
      return "mob.wither.idle";
   }

   protected String bo() {
      return "mob.wither.hurt";
   }

   protected String bp() {
      return "mob.wither.death";
   }

   public void m() {
      this.motY *= 0.6000000238418579D;
      double var4;
      double var6;
      double var8;
      if(!this.world.isClientSide && this.s(0) > 0) {
         Entity var1 = this.world.a(this.s(0));
         if(var1 != null) {
            if(this.locY < var1.locY || !this.cm() && this.locY < var1.locY + 5.0D) {
               if(this.motY < 0.0D) {
                  this.motY = 0.0D;
               }

               this.motY += (0.5D - this.motY) * 0.6000000238418579D;
            }

            double var2 = var1.locX - this.locX;
            var4 = var1.locZ - this.locZ;
            var6 = var2 * var2 + var4 * var4;
            if(var6 > 9.0D) {
               var8 = (double)MathHelper.sqrt(var6);
               this.motX += (var2 / var8 * 0.5D - this.motX) * 0.6000000238418579D;
               this.motZ += (var4 / var8 * 0.5D - this.motZ) * 0.6000000238418579D;
            }
         }
      }

      if(this.motX * this.motX + this.motZ * this.motZ > 0.05000000074505806D) {
         this.yaw = (float)MathHelper.b(this.motZ, this.motX) * 57.295776F - 90.0F;
      }

      super.m();

      int var20;
      for(var20 = 0; var20 < 2; ++var20) {
         this.bm[var20] = this.b[var20];
         this.c[var20] = this.a[var20];
      }

      int var21;
      for(var20 = 0; var20 < 2; ++var20) {
         var21 = this.s(var20 + 1);
         Entity var3 = null;
         if(var21 > 0) {
            var3 = this.world.a(var21);
         }

         if(var3 != null) {
            var4 = this.t(var20 + 1);
            var6 = this.u(var20 + 1);
            var8 = this.v(var20 + 1);
            double var10 = var3.locX - var4;
            double var12 = var3.locY + (double)var3.getHeadHeight() - var6;
            double var14 = var3.locZ - var8;
            double var16 = (double)MathHelper.sqrt(var10 * var10 + var14 * var14);
            float var18 = (float)(MathHelper.b(var14, var10) * 180.0D / 3.1415927410125732D) - 90.0F;
            float var19 = (float)(-(MathHelper.b(var12, var16) * 180.0D / 3.1415927410125732D));
            this.a[var20] = this.b(this.a[var20], var19, 40.0F);
            this.b[var20] = this.b(this.b[var20], var18, 10.0F);
         } else {
            this.b[var20] = this.b(this.b[var20], this.aI, 10.0F);
         }
      }

      boolean var22 = this.cm();

      for(var21 = 0; var21 < 3; ++var21) {
         double var23 = this.t(var21);
         double var5 = this.u(var21);
         double var7 = this.v(var21);
         this.world.addParticle(EnumParticle.SMOKE_NORMAL, var23 + this.random.nextGaussian() * 0.30000001192092896D, var5 + this.random.nextGaussian() * 0.30000001192092896D, var7 + this.random.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D, new int[0]);
         if(var22 && this.world.random.nextInt(4) == 0) {
            this.world.addParticle(EnumParticle.SPELL_MOB, var23 + this.random.nextGaussian() * 0.30000001192092896D, var5 + this.random.nextGaussian() * 0.30000001192092896D, var7 + this.random.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D, new int[0]);
         }
      }

      if(this.cl() > 0) {
         for(var21 = 0; var21 < 3; ++var21) {
            this.world.addParticle(EnumParticle.SPELL_MOB, this.locX + this.random.nextGaussian() * 1.0D, this.locY + (double)(this.random.nextFloat() * 3.3F), this.locZ + this.random.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D, new int[0]);
         }
      }

   }

   protected void E() {
      int var1;
      if(this.cl() > 0) {
         var1 = this.cl() - 1;
         if(var1 <= 0) {
            this.world.createExplosion(this, this.locX, this.locY + (double)this.getHeadHeight(), this.locZ, 7.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
            this.world.a(1013, new BlockPosition(this), 0);
         }

         this.r(var1);
         if(this.ticksLived % 10 == 0) {
            this.heal(10.0F);
         }

      } else {
         super.E();

         int var13;
         for(var1 = 1; var1 < 3; ++var1) {
            if(this.ticksLived >= this.bn[var1 - 1]) {
               this.bn[var1 - 1] = this.ticksLived + 10 + this.random.nextInt(10);
               if(this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) {
                  int var10001 = var1 - 1;
                  int var10003 = this.bo[var1 - 1];
                  this.bo[var10001] = this.bo[var1 - 1] + 1;
                  if(var10003 > 15) {
                     float var2 = 10.0F;
                     float var3 = 5.0F;
                     double var4 = MathHelper.a(this.random, this.locX - (double)var2, this.locX + (double)var2);
                     double var6 = MathHelper.a(this.random, this.locY - (double)var3, this.locY + (double)var3);
                     double var8 = MathHelper.a(this.random, this.locZ - (double)var2, this.locZ + (double)var2);
                     this.a(var1 + 1, var4, var6, var8, true);
                     this.bo[var1 - 1] = 0;
                  }
               }

               var13 = this.s(var1);
               if(var13 > 0) {
                  Entity var15 = this.world.a(var13);
                  if(var15 != null && var15.isAlive() && this.h(var15) <= 900.0D && this.hasLineOfSight(var15)) {
                     if(var15 instanceof EntityHuman && ((EntityHuman)var15).abilities.isInvulnerable) {
                        this.b(var1, 0);
                     } else {
                        this.a(var1 + 1, (EntityLiving)var15);
                        this.bn[var1 - 1] = this.ticksLived + 40 + this.random.nextInt(20);
                        this.bo[var1 - 1] = 0;
                     }
                  } else {
                     this.b(var1, 0);
                  }
               } else {
                  List var14 = this.world.a(EntityLiving.class, this.getBoundingBox().grow(20.0D, 8.0D, 20.0D), Predicates.and(bq, IEntitySelector.d));

                  for(int var17 = 0; var17 < 10 && !var14.isEmpty(); ++var17) {
                     EntityLiving var5 = (EntityLiving)var14.get(this.random.nextInt(var14.size()));
                     if(var5 != this && var5.isAlive() && this.hasLineOfSight(var5)) {
                        if(var5 instanceof EntityHuman) {
                           if(!((EntityHuman)var5).abilities.isInvulnerable) {
                              this.b(var1, var5.getId());
                           }
                        } else {
                           this.b(var1, var5.getId());
                        }
                        break;
                     }

                     var14.remove(var5);
                  }
               }
            }
         }

         if(this.getGoalTarget() != null) {
            this.b(0, this.getGoalTarget().getId());
         } else {
            this.b(0, 0);
         }

         if(this.bp > 0) {
            --this.bp;
            if(this.bp == 0 && this.world.getGameRules().getBoolean("mobGriefing")) {
               var1 = MathHelper.floor(this.locY);
               var13 = MathHelper.floor(this.locX);
               int var16 = MathHelper.floor(this.locZ);
               boolean var18 = false;

               for(int var19 = -1; var19 <= 1; ++var19) {
                  for(int var20 = -1; var20 <= 1; ++var20) {
                     for(int var7 = 0; var7 <= 3; ++var7) {
                        int var21 = var13 + var19;
                        int var9 = var1 + var7;
                        int var10 = var16 + var20;
                        BlockPosition var11 = new BlockPosition(var21, var9, var10);
                        Block var12 = this.world.getType(var11).getBlock();
                        if(var12.getMaterial() != Material.AIR && a(var12)) {
                           var18 = this.world.setAir(var11, true) || var18;
                        }
                     }
                  }
               }

               if(var18) {
                  this.world.a((EntityHuman)null, 1012, new BlockPosition(this), 0);
               }
            }
         }

         if(this.ticksLived % 20 == 0) {
            this.heal(1.0F);
         }

      }
   }

   public static boolean a(Block var0) {
      return var0 != Blocks.BEDROCK && var0 != Blocks.END_PORTAL && var0 != Blocks.END_PORTAL_FRAME && var0 != Blocks.COMMAND_BLOCK && var0 != Blocks.BARRIER;
   }

   public void n() {
      this.r(220);
      this.setHealth(this.getMaxHealth() / 3.0F);
   }

   public void aA() {
   }

   public int br() {
      return 4;
   }

   private double t(int var1) {
      if(var1 <= 0) {
         return this.locX;
      } else {
         float var2 = (this.aI + (float)(180 * (var1 - 1))) / 180.0F * 3.1415927F;
         float var3 = MathHelper.cos(var2);
         return this.locX + (double)var3 * 1.3D;
      }
   }

   private double u(int var1) {
      return var1 <= 0?this.locY + 3.0D:this.locY + 2.2D;
   }

   private double v(int var1) {
      if(var1 <= 0) {
         return this.locZ;
      } else {
         float var2 = (this.aI + (float)(180 * (var1 - 1))) / 180.0F * 3.1415927F;
         float var3 = MathHelper.sin(var2);
         return this.locZ + (double)var3 * 1.3D;
      }
   }

   private float b(float var1, float var2, float var3) {
      float var4 = MathHelper.g(var2 - var1);
      if(var4 > var3) {
         var4 = var3;
      }

      if(var4 < -var3) {
         var4 = -var3;
      }

      return var1 + var4;
   }

   private void a(int var1, EntityLiving var2) {
      this.a(var1, var2.locX, var2.locY + (double)var2.getHeadHeight() * 0.5D, var2.locZ, var1 == 0 && this.random.nextFloat() < 0.001F);
   }

   private void a(int var1, double var2, double var4, double var6, boolean var8) {
      this.world.a((EntityHuman)null, 1014, new BlockPosition(this), 0);
      double var9 = this.t(var1);
      double var11 = this.u(var1);
      double var13 = this.v(var1);
      double var15 = var2 - var9;
      double var17 = var4 - var11;
      double var19 = var6 - var13;
      EntityWitherSkull var21 = new EntityWitherSkull(this.world, this, var15, var17, var19);
      if(var8) {
         var21.setCharged(true);
      }

      var21.locY = var11;
      var21.locX = var9;
      var21.locZ = var13;
      this.world.addEntity(var21);
   }

   public void a(EntityLiving var1, float var2) {
      this.a(0, var1);
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else if(var1 != DamageSource.DROWN && !(var1.getEntity() instanceof EntityWither)) {
         if(this.cl() > 0 && var1 != DamageSource.OUT_OF_WORLD) {
            return false;
         } else {
            Entity var3;
            if(this.cm()) {
               var3 = var1.i();
               if(var3 instanceof EntityArrow) {
                  return false;
               }
            }

            var3 = var1.getEntity();
            if(var3 != null && !(var3 instanceof EntityHuman) && var3 instanceof EntityLiving && ((EntityLiving)var3).getMonsterType() == this.getMonsterType()) {
               return false;
            } else {
               if(this.bp <= 0) {
                  this.bp = 20;
               }

               for(int var4 = 0; var4 < this.bo.length; ++var4) {
                  this.bo[var4] += 3;
               }

               return super.damageEntity(var1, var2);
            }
         }
      } else {
         return false;
      }
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      EntityItem var3 = this.a(Items.NETHER_STAR, 1);
      if(var3 != null) {
         var3.u();
      }

      if(!this.world.isClientSide) {
         Iterator var4 = this.world.a(EntityHuman.class, this.getBoundingBox().grow(50.0D, 100.0D, 50.0D)).iterator();

         while(var4.hasNext()) {
            EntityHuman var5 = (EntityHuman)var4.next();
            var5.b((Statistic)AchievementList.J);
         }
      }

   }

   protected void D() {
      this.ticksFarFromPlayer = 0;
   }

   public void e(float var1, float var2) {
   }

   public void addEffect(MobEffect var1) {
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(300.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.6000000238418579D);
      this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(40.0D);
   }

   public int cl() {
      return this.datawatcher.getInt(20);
   }

   public void r(int var1) {
      this.datawatcher.watch(20, Integer.valueOf(var1));
   }

   public int s(int var1) {
      return this.datawatcher.getInt(17 + var1);
   }

   public void b(int var1, int var2) {
      this.datawatcher.watch(17 + var1, Integer.valueOf(var2));
   }

   public boolean cm() {
      return this.getHealth() <= this.getMaxHealth() / 2.0F;
   }

   public EnumMonsterType getMonsterType() {
      return EnumMonsterType.UNDEAD;
   }

   public void mount(Entity var1) {
      this.vehicle = null;
   }
}
