package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockTorch;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityComplexPart;
import net.minecraft.server.EntityDamageSource;
import net.minecraft.server.EntityEnderCrystal;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.Explosion;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IComplex;
import net.minecraft.server.IMonster;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class EntityEnderDragon extends EntityInsentient implements IComplex, IMonster {
   public double a;
   public double b;
   public double c;
   public double[][] bk = new double[64][3];
   public int bl = -1;
   public EntityComplexPart[] children;
   public EntityComplexPart bn;
   public EntityComplexPart bo;
   public EntityComplexPart bp;
   public EntityComplexPart bq;
   public EntityComplexPart br;
   public EntityComplexPart bs;
   public EntityComplexPart bt;
   public float bu;
   public float bv;
   public boolean bw;
   public boolean bx;
   private Entity bA;
   public int by;
   public EntityEnderCrystal bz;

   public EntityEnderDragon(World var1) {
      super(var1);
      this.children = new EntityComplexPart[]{this.bn = new EntityComplexPart(this, "head", 6.0F, 6.0F), this.bo = new EntityComplexPart(this, "body", 8.0F, 8.0F), this.bp = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.bq = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.br = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.bs = new EntityComplexPart(this, "wing", 4.0F, 4.0F), this.bt = new EntityComplexPart(this, "wing", 4.0F, 4.0F)};
      this.setHealth(this.getMaxHealth());
      this.setSize(16.0F, 8.0F);
      this.noclip = true;
      this.fireProof = true;
      this.b = 100.0D;
      this.ah = true;
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(200.0D);
   }

   protected void h() {
      super.h();
   }

   public double[] b(int var1, float var2) {
      if(this.getHealth() <= 0.0F) {
         var2 = 0.0F;
      }

      var2 = 1.0F - var2;
      int var3 = this.bl - var1 * 1 & 63;
      int var4 = this.bl - var1 * 1 - 1 & 63;
      double[] var5 = new double[3];
      double var6 = this.bk[var3][0];
      double var8 = MathHelper.g(this.bk[var4][0] - var6);
      var5[0] = var6 + var8 * (double)var2;
      var6 = this.bk[var3][1];
      var8 = this.bk[var4][1] - var6;
      var5[1] = var6 + var8 * (double)var2;
      var5[2] = this.bk[var3][2] + (this.bk[var4][2] - this.bk[var3][2]) * (double)var2;
      return var5;
   }

   public void m() {
      float var1;
      float var2;
      if(this.world.isClientSide) {
         var1 = MathHelper.cos(this.bv * 3.1415927F * 2.0F);
         var2 = MathHelper.cos(this.bu * 3.1415927F * 2.0F);
         if(var2 <= -0.3F && var1 >= -0.3F && !this.R()) {
            this.world.a(this.locX, this.locY, this.locZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
         }
      }

      this.bu = this.bv;
      float var3;
      if(this.getHealth() <= 0.0F) {
         var1 = (this.random.nextFloat() - 0.5F) * 8.0F;
         var2 = (this.random.nextFloat() - 0.5F) * 4.0F;
         var3 = (this.random.nextFloat() - 0.5F) * 8.0F;
         this.world.addParticle(EnumParticle.EXPLOSION_LARGE, this.locX + (double)var1, this.locY + 2.0D + (double)var2, this.locZ + (double)var3, 0.0D, 0.0D, 0.0D, new int[0]);
      } else {
         this.n();
         var1 = 0.2F / (MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 10.0F + 1.0F);
         var1 *= (float)Math.pow(2.0D, this.motY);
         if(this.bx) {
            this.bv += var1 * 0.5F;
         } else {
            this.bv += var1;
         }

         this.yaw = MathHelper.g(this.yaw);
         if(this.ce()) {
            this.bv = 0.5F;
         } else {
            if(this.bl < 0) {
               for(int var27 = 0; var27 < this.bk.length; ++var27) {
                  this.bk[var27][0] = (double)this.yaw;
                  this.bk[var27][1] = this.locY;
               }
            }

            if(++this.bl == this.bk.length) {
               this.bl = 0;
            }

            this.bk[this.bl][0] = (double)this.yaw;
            this.bk[this.bl][1] = this.locY;
            double var4;
            double var6;
            double var8;
            double var28;
            float var33;
            if(this.world.isClientSide) {
               if(this.bc > 0) {
                  var28 = this.locX + (this.bd - this.locX) / (double)this.bc;
                  var4 = this.locY + (this.be - this.locY) / (double)this.bc;
                  var6 = this.locZ + (this.bf - this.locZ) / (double)this.bc;
                  var8 = MathHelper.g(this.bg - (double)this.yaw);
                  this.yaw = (float)((double)this.yaw + var8 / (double)this.bc);
                  this.pitch = (float)((double)this.pitch + (this.bh - (double)this.pitch) / (double)this.bc);
                  --this.bc;
                  this.setPosition(var28, var4, var6);
                  this.setYawPitch(this.yaw, this.pitch);
               }
            } else {
               var28 = this.a - this.locX;
               var4 = this.b - this.locY;
               var6 = this.c - this.locZ;
               var8 = var28 * var28 + var4 * var4 + var6 * var6;
               double var16;
               if(this.bA != null) {
                  this.a = this.bA.locX;
                  this.c = this.bA.locZ;
                  double var10 = this.a - this.locX;
                  double var12 = this.c - this.locZ;
                  double var14 = Math.sqrt(var10 * var10 + var12 * var12);
                  var16 = 0.4000000059604645D + var14 / 80.0D - 1.0D;
                  if(var16 > 10.0D) {
                     var16 = 10.0D;
                  }

                  this.b = this.bA.getBoundingBox().b + var16;
               } else {
                  this.a += this.random.nextGaussian() * 2.0D;
                  this.c += this.random.nextGaussian() * 2.0D;
               }

               if(this.bw || var8 < 100.0D || var8 > 22500.0D || this.positionChanged || this.E) {
                  this.cf();
               }

               var4 /= (double)MathHelper.sqrt(var28 * var28 + var6 * var6);
               var33 = 0.6F;
               var4 = MathHelper.a(var4, (double)(-var33), (double)var33);
               this.motY += var4 * 0.10000000149011612D;
               this.yaw = MathHelper.g(this.yaw);
               double var11 = 180.0D - MathHelper.b(var28, var6) * 180.0D / 3.1415927410125732D;
               double var13 = MathHelper.g(var11 - (double)this.yaw);
               if(var13 > 50.0D) {
                  var13 = 50.0D;
               }

               if(var13 < -50.0D) {
                  var13 = -50.0D;
               }

               Vec3D var15 = (new Vec3D(this.a - this.locX, this.b - this.locY, this.c - this.locZ)).a();
               var16 = (double)(-MathHelper.cos(this.yaw * 3.1415927F / 180.0F));
               Vec3D var18 = (new Vec3D((double)MathHelper.sin(this.yaw * 3.1415927F / 180.0F), this.motY, var16)).a();
               float var19 = ((float)var18.b(var15) + 0.5F) / 1.5F;
               if(var19 < 0.0F) {
                  var19 = 0.0F;
               }

               this.bb *= 0.8F;
               float var20 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0F + 1.0F;
               double var21 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0D + 1.0D;
               if(var21 > 40.0D) {
                  var21 = 40.0D;
               }

               this.bb = (float)((double)this.bb + var13 * (0.699999988079071D / var21 / (double)var20));
               this.yaw += this.bb * 0.1F;
               float var23 = (float)(2.0D / (var21 + 1.0D));
               float var24 = 0.06F;
               this.a(0.0F, -1.0F, var24 * (var19 * var23 + (1.0F - var23)));
               if(this.bx) {
                  this.move(this.motX * 0.800000011920929D, this.motY * 0.800000011920929D, this.motZ * 0.800000011920929D);
               } else {
                  this.move(this.motX, this.motY, this.motZ);
               }

               Vec3D var25 = (new Vec3D(this.motX, this.motY, this.motZ)).a();
               float var26 = ((float)var25.b(var18) + 1.0F) / 2.0F;
               var26 = 0.8F + 0.15F * var26;
               this.motX *= (double)var26;
               this.motZ *= (double)var26;
               this.motY *= 0.9100000262260437D;
            }

            this.aI = this.yaw;
            this.bn.width = this.bn.length = 3.0F;
            this.bp.width = this.bp.length = 2.0F;
            this.bq.width = this.bq.length = 2.0F;
            this.br.width = this.br.length = 2.0F;
            this.bo.length = 3.0F;
            this.bo.width = 5.0F;
            this.bs.length = 2.0F;
            this.bs.width = 4.0F;
            this.bt.length = 3.0F;
            this.bt.width = 4.0F;
            var2 = (float)(this.b(5, 1.0F)[1] - this.b(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
            var3 = MathHelper.cos(var2);
            float var29 = -MathHelper.sin(var2);
            float var5 = this.yaw * 3.1415927F / 180.0F;
            float var30 = MathHelper.sin(var5);
            float var7 = MathHelper.cos(var5);
            this.bo.t_();
            this.bo.setPositionRotation(this.locX + (double)(var30 * 0.5F), this.locY, this.locZ - (double)(var7 * 0.5F), 0.0F, 0.0F);
            this.bs.t_();
            this.bs.setPositionRotation(this.locX + (double)(var7 * 4.5F), this.locY + 2.0D, this.locZ + (double)(var30 * 4.5F), 0.0F, 0.0F);
            this.bt.t_();
            this.bt.setPositionRotation(this.locX - (double)(var7 * 4.5F), this.locY + 2.0D, this.locZ - (double)(var30 * 4.5F), 0.0F, 0.0F);
            if(!this.world.isClientSide && this.hurtTicks == 0) {
               this.a(this.world.getEntities(this, this.bs.getBoundingBox().grow(4.0D, 2.0D, 4.0D).c(0.0D, -2.0D, 0.0D)));
               this.a(this.world.getEntities(this, this.bt.getBoundingBox().grow(4.0D, 2.0D, 4.0D).c(0.0D, -2.0D, 0.0D)));
               this.b(this.world.getEntities(this, this.bn.getBoundingBox().grow(1.0D, 1.0D, 1.0D)));
            }

            double[] var31 = this.b(5, 1.0F);
            double[] var9 = this.b(0, 1.0F);
            var33 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F - this.bb * 0.01F);
            float var35 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F - this.bb * 0.01F);
            this.bn.t_();
            this.bn.setPositionRotation(this.locX + (double)(var33 * 5.5F * var3), this.locY + (var9[1] - var31[1]) * 1.0D + (double)(var29 * 5.5F), this.locZ - (double)(var35 * 5.5F * var3), 0.0F, 0.0F);

            for(int var32 = 0; var32 < 3; ++var32) {
               EntityComplexPart var34 = null;
               if(var32 == 0) {
                  var34 = this.bp;
               }

               if(var32 == 1) {
                  var34 = this.bq;
               }

               if(var32 == 2) {
                  var34 = this.br;
               }

               double[] var36 = this.b(12 + var32 * 2, 1.0F);
               float var37 = this.yaw * 3.1415927F / 180.0F + this.b(var36[0] - var31[0]) * 3.1415927F / 180.0F * 1.0F;
               float var39 = MathHelper.sin(var37);
               float var38 = MathHelper.cos(var37);
               float var40 = 1.5F;
               float var41 = (float)(var32 + 1) * 2.0F;
               var34.t_();
               var34.setPositionRotation(this.locX - (double)((var30 * var40 + var39 * var41) * var3), this.locY + (var36[1] - var31[1]) * 1.0D - (double)((var41 + var40) * var29) + 1.5D, this.locZ + (double)((var7 * var40 + var38 * var41) * var3), 0.0F, 0.0F);
            }

            if(!this.world.isClientSide) {
               this.bx = this.b(this.bn.getBoundingBox()) | this.b(this.bo.getBoundingBox());
            }

         }
      }
   }

   private void n() {
      if(this.bz != null) {
         if(this.bz.dead) {
            if(!this.world.isClientSide) {
               this.a(this.bn, DamageSource.explosion((Explosion)null), 10.0F);
            }

            this.bz = null;
         } else if(this.ticksLived % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
            this.setHealth(this.getHealth() + 1.0F);
         }
      }

      if(this.random.nextInt(10) == 0) {
         float var1 = 32.0F;
         List var2 = this.world.a(EntityEnderCrystal.class, this.getBoundingBox().grow((double)var1, (double)var1, (double)var1));
         EntityEnderCrystal var3 = null;
         double var4 = Double.MAX_VALUE;
         Iterator var6 = var2.iterator();

         while(var6.hasNext()) {
            EntityEnderCrystal var7 = (EntityEnderCrystal)var6.next();
            double var8 = var7.h(this);
            if(var8 < var4) {
               var4 = var8;
               var3 = var7;
            }
         }

         this.bz = var3;
      }

   }

   private void a(List<Entity> var1) {
      double var2 = (this.bo.getBoundingBox().a + this.bo.getBoundingBox().d) / 2.0D;
      double var4 = (this.bo.getBoundingBox().c + this.bo.getBoundingBox().f) / 2.0D;
      Iterator var6 = var1.iterator();

      while(var6.hasNext()) {
         Entity var7 = (Entity)var6.next();
         if(var7 instanceof EntityLiving) {
            double var8 = var7.locX - var2;
            double var10 = var7.locZ - var4;
            double var12 = var8 * var8 + var10 * var10;
            var7.g(var8 / var12 * 4.0D, 0.20000000298023224D, var10 / var12 * 4.0D);
         }
      }

   }

   private void b(List<Entity> var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         Entity var3 = (Entity)var1.get(var2);
         if(var3 instanceof EntityLiving) {
            var3.damageEntity(DamageSource.mobAttack(this), 10.0F);
            this.a(this, var3);
         }
      }

   }

   private void cf() {
      this.bw = false;
      ArrayList var1 = Lists.newArrayList((Iterable)this.world.players);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         if(((EntityHuman)var2.next()).isSpectator()) {
            var2.remove();
         }
      }

      if(this.random.nextInt(2) == 0 && !var1.isEmpty()) {
         this.bA = (Entity)var1.get(this.random.nextInt(var1.size()));
      } else {
         boolean var3;
         do {
            this.a = 0.0D;
            this.b = (double)(70.0F + this.random.nextFloat() * 50.0F);
            this.c = 0.0D;
            this.a += (double)(this.random.nextFloat() * 120.0F - 60.0F);
            this.c += (double)(this.random.nextFloat() * 120.0F - 60.0F);
            double var4 = this.locX - this.a;
            double var6 = this.locY - this.b;
            double var8 = this.locZ - this.c;
            var3 = var4 * var4 + var6 * var6 + var8 * var8 > 100.0D;
         } while(!var3);

         this.bA = null;
      }

   }

   private float b(double var1) {
      return (float)MathHelper.g(var1);
   }

   private boolean b(AxisAlignedBB var1) {
      int var2 = MathHelper.floor(var1.a);
      int var3 = MathHelper.floor(var1.b);
      int var4 = MathHelper.floor(var1.c);
      int var5 = MathHelper.floor(var1.d);
      int var6 = MathHelper.floor(var1.e);
      int var7 = MathHelper.floor(var1.f);
      boolean var8 = false;
      boolean var9 = false;

      for(int var10 = var2; var10 <= var5; ++var10) {
         for(int var11 = var3; var11 <= var6; ++var11) {
            for(int var12 = var4; var12 <= var7; ++var12) {
               BlockPosition var13 = new BlockPosition(var10, var11, var12);
               Block var14 = this.world.getType(var13).getBlock();
               if(var14.getMaterial() != Material.AIR) {
                  if(var14 != Blocks.BARRIER && var14 != Blocks.OBSIDIAN && var14 != Blocks.END_STONE && var14 != Blocks.BEDROCK && var14 != Blocks.COMMAND_BLOCK && this.world.getGameRules().getBoolean("mobGriefing")) {
                     var9 = this.world.setAir(var13) || var9;
                  } else {
                     var8 = true;
                  }
               }
            }
         }
      }

      if(var9) {
         double var16 = var1.a + (var1.d - var1.a) * (double)this.random.nextFloat();
         double var17 = var1.b + (var1.e - var1.b) * (double)this.random.nextFloat();
         double var18 = var1.c + (var1.f - var1.c) * (double)this.random.nextFloat();
         this.world.addParticle(EnumParticle.EXPLOSION_LARGE, var16, var17, var18, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      return var8;
   }

   public boolean a(EntityComplexPart var1, DamageSource var2, float var3) {
      if(var1 != this.bn) {
         var3 = var3 / 4.0F + 1.0F;
      }

      float var4 = this.yaw * 3.1415927F / 180.0F;
      float var5 = MathHelper.sin(var4);
      float var6 = MathHelper.cos(var4);
      this.a = this.locX + (double)(var5 * 5.0F) + (double)((this.random.nextFloat() - 0.5F) * 2.0F);
      this.b = this.locY + (double)(this.random.nextFloat() * 3.0F) + 1.0D;
      this.c = this.locZ - (double)(var6 * 5.0F) + (double)((this.random.nextFloat() - 0.5F) * 2.0F);
      this.bA = null;
      if(var2.getEntity() instanceof EntityHuman || var2.isExplosion()) {
         this.dealDamage(var2, var3);
      }

      return true;
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(var1 instanceof EntityDamageSource && ((EntityDamageSource)var1).w()) {
         this.dealDamage(var1, var2);
      }

      return false;
   }

   protected boolean dealDamage(DamageSource var1, float var2) {
      return super.damageEntity(var1, var2);
   }

   public void G() {
      this.die();
   }

   protected void aZ() {
      ++this.by;
      if(this.by >= 180 && this.by <= 200) {
         float var1 = (this.random.nextFloat() - 0.5F) * 8.0F;
         float var2 = (this.random.nextFloat() - 0.5F) * 4.0F;
         float var3 = (this.random.nextFloat() - 0.5F) * 8.0F;
         this.world.addParticle(EnumParticle.EXPLOSION_HUGE, this.locX + (double)var1, this.locY + 2.0D + (double)var2, this.locZ + (double)var3, 0.0D, 0.0D, 0.0D, new int[0]);
      }

      boolean var4 = this.world.getGameRules().getBoolean("doMobLoot");
      int var5;
      int var6;
      if(!this.world.isClientSide) {
         if(this.by > 150 && this.by % 5 == 0 && var4) {
            var5 = 1000;

            while(var5 > 0) {
               var6 = EntityExperienceOrb.getOrbValue(var5);
               var5 -= var6;
               this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, var6));
            }
         }

         if(this.by == 1) {
            this.world.a(1018, new BlockPosition(this), 0);
         }
      }

      this.move(0.0D, 0.10000000149011612D, 0.0D);
      this.aI = this.yaw += 20.0F;
      if(this.by == 200 && !this.world.isClientSide) {
         if(var4) {
            var5 = 2000;

            while(var5 > 0) {
               var6 = EntityExperienceOrb.getOrbValue(var5);
               var5 -= var6;
               this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, var6));
            }
         }

         this.a(new BlockPosition(this.locX, 64.0D, this.locZ));
         this.die();
      }

   }

   private void a(BlockPosition var1) {
      boolean var2 = true;
      double var3 = 12.25D;
      double var5 = 6.25D;

      for(int var7 = -1; var7 <= 32; ++var7) {
         for(int var8 = -4; var8 <= 4; ++var8) {
            for(int var9 = -4; var9 <= 4; ++var9) {
               double var10 = (double)(var8 * var8 + var9 * var9);
               if(var10 <= 12.25D) {
                  BlockPosition var12 = var1.a(var8, var7, var9);
                  if(var7 < 0) {
                     if(var10 <= 6.25D) {
                        this.world.setTypeUpdate(var12, Blocks.BEDROCK.getBlockData());
                     }
                  } else if(var7 > 0) {
                     this.world.setTypeUpdate(var12, Blocks.AIR.getBlockData());
                  } else if(var10 > 6.25D) {
                     this.world.setTypeUpdate(var12, Blocks.BEDROCK.getBlockData());
                  } else {
                     this.world.setTypeUpdate(var12, Blocks.END_PORTAL.getBlockData());
                  }
               }
            }
         }
      }

      this.world.setTypeUpdate(var1, Blocks.BEDROCK.getBlockData());
      this.world.setTypeUpdate(var1.up(), Blocks.BEDROCK.getBlockData());
      BlockPosition var13 = var1.up(2);
      this.world.setTypeUpdate(var13, Blocks.BEDROCK.getBlockData());
      this.world.setTypeUpdate(var13.west(), Blocks.TORCH.getBlockData().set(BlockTorch.FACING, EnumDirection.EAST));
      this.world.setTypeUpdate(var13.east(), Blocks.TORCH.getBlockData().set(BlockTorch.FACING, EnumDirection.WEST));
      this.world.setTypeUpdate(var13.north(), Blocks.TORCH.getBlockData().set(BlockTorch.FACING, EnumDirection.SOUTH));
      this.world.setTypeUpdate(var13.south(), Blocks.TORCH.getBlockData().set(BlockTorch.FACING, EnumDirection.NORTH));
      this.world.setTypeUpdate(var1.up(3), Blocks.BEDROCK.getBlockData());
      this.world.setTypeUpdate(var1.up(4), Blocks.DRAGON_EGG.getBlockData());
   }

   protected void D() {
   }

   public Entity[] aB() {
      return this.children;
   }

   public boolean ad() {
      return false;
   }

   public World a() {
      return this.world;
   }

   protected String z() {
      return "mob.enderdragon.growl";
   }

   protected String bo() {
      return "mob.enderdragon.hit";
   }

   protected float bB() {
      return 5.0F;
   }
}
