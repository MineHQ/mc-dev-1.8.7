package net.minecraft.server;

import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityEnderman;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IProjectile;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PacketPlayOutGameStateChange;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class EntityArrow extends Entity implements IProjectile {
   private int d = -1;
   private int e = -1;
   private int f = -1;
   private Block g;
   private int h;
   private boolean inGround;
   public int fromPlayer;
   public int shake;
   public Entity shooter;
   private int ar;
   private int as;
   private double damage = 2.0D;
   public int knockbackStrength;

   public EntityArrow(World var1) {
      super(var1);
      this.j = 10.0D;
      this.setSize(0.5F, 0.5F);
   }

   public EntityArrow(World var1, double var2, double var4, double var6) {
      super(var1);
      this.j = 10.0D;
      this.setSize(0.5F, 0.5F);
      this.setPosition(var2, var4, var6);
   }

   public EntityArrow(World var1, EntityLiving var2, EntityLiving var3, float var4, float var5) {
      super(var1);
      this.j = 10.0D;
      this.shooter = var2;
      if(var2 instanceof EntityHuman) {
         this.fromPlayer = 1;
      }

      this.locY = var2.locY + (double)var2.getHeadHeight() - 0.10000000149011612D;
      double var6 = var3.locX - var2.locX;
      double var8 = var3.getBoundingBox().b + (double)(var3.length / 3.0F) - this.locY;
      double var10 = var3.locZ - var2.locZ;
      double var12 = (double)MathHelper.sqrt(var6 * var6 + var10 * var10);
      if(var12 >= 1.0E-7D) {
         float var14 = (float)(MathHelper.b(var10, var6) * 180.0D / 3.1415927410125732D) - 90.0F;
         float var15 = (float)(-(MathHelper.b(var8, var12) * 180.0D / 3.1415927410125732D));
         double var16 = var6 / var12;
         double var18 = var10 / var12;
         this.setPositionRotation(var2.locX + var16, this.locY, var2.locZ + var18, var14, var15);
         float var20 = (float)(var12 * 0.20000000298023224D);
         this.shoot(var6, var8 + (double)var20, var10, var4, var5);
      }
   }

   public EntityArrow(World var1, EntityLiving var2, float var3) {
      super(var1);
      this.j = 10.0D;
      this.shooter = var2;
      if(var2 instanceof EntityHuman) {
         this.fromPlayer = 1;
      }

      this.setSize(0.5F, 0.5F);
      this.setPositionRotation(var2.locX, var2.locY + (double)var2.getHeadHeight(), var2.locZ, var2.yaw, var2.pitch);
      this.locX -= (double)(MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F);
      this.locY -= 0.10000000149011612D;
      this.locZ -= (double)(MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F);
      this.setPosition(this.locX, this.locY, this.locZ);
      this.motX = (double)(-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F));
      this.motZ = (double)(MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F));
      this.motY = (double)(-MathHelper.sin(this.pitch / 180.0F * 3.1415927F));
      this.shoot(this.motX, this.motY, this.motZ, var3 * 1.5F, 1.0F);
   }

   protected void h() {
      this.datawatcher.a(16, Byte.valueOf((byte)0));
   }

   public void shoot(double var1, double var3, double var5, float var7, float var8) {
      float var9 = MathHelper.sqrt(var1 * var1 + var3 * var3 + var5 * var5);
      var1 /= (double)var9;
      var3 /= (double)var9;
      var5 /= (double)var9;
      var1 += this.random.nextGaussian() * (double)(this.random.nextBoolean()?-1:1) * 0.007499999832361937D * (double)var8;
      var3 += this.random.nextGaussian() * (double)(this.random.nextBoolean()?-1:1) * 0.007499999832361937D * (double)var8;
      var5 += this.random.nextGaussian() * (double)(this.random.nextBoolean()?-1:1) * 0.007499999832361937D * (double)var8;
      var1 *= (double)var7;
      var3 *= (double)var7;
      var5 *= (double)var7;
      this.motX = var1;
      this.motY = var3;
      this.motZ = var5;
      float var10 = MathHelper.sqrt(var1 * var1 + var5 * var5);
      this.lastYaw = this.yaw = (float)(MathHelper.b(var1, var5) * 180.0D / 3.1415927410125732D);
      this.lastPitch = this.pitch = (float)(MathHelper.b(var3, (double)var10) * 180.0D / 3.1415927410125732D);
      this.ar = 0;
   }

   public void t_() {
      super.t_();
      if(this.lastPitch == 0.0F && this.lastYaw == 0.0F) {
         float var1 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
         this.lastYaw = this.yaw = (float)(MathHelper.b(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);
         this.lastPitch = this.pitch = (float)(MathHelper.b(this.motY, (double)var1) * 180.0D / 3.1415927410125732D);
      }

      BlockPosition var18 = new BlockPosition(this.d, this.e, this.f);
      IBlockData var2 = this.world.getType(var18);
      Block var3 = var2.getBlock();
      if(var3.getMaterial() != Material.AIR) {
         var3.updateShape(this.world, var18);
         AxisAlignedBB var4 = var3.a(this.world, var18, var2);
         if(var4 != null && var4.a(new Vec3D(this.locX, this.locY, this.locZ))) {
            this.inGround = true;
         }
      }

      if(this.shake > 0) {
         --this.shake;
      }

      if(this.inGround) {
         int var20 = var3.toLegacyData(var2);
         if(var3 == this.g && var20 == this.h) {
            ++this.ar;
            if(this.ar >= 1200) {
               this.die();
            }
         } else {
            this.inGround = false;
            this.motX *= (double)(this.random.nextFloat() * 0.2F);
            this.motY *= (double)(this.random.nextFloat() * 0.2F);
            this.motZ *= (double)(this.random.nextFloat() * 0.2F);
            this.ar = 0;
            this.as = 0;
         }

      } else {
         ++this.as;
         Vec3D var19 = new Vec3D(this.locX, this.locY, this.locZ);
         Vec3D var5 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
         MovingObjectPosition var6 = this.world.rayTrace(var19, var5, false, true, false);
         var19 = new Vec3D(this.locX, this.locY, this.locZ);
         var5 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
         if(var6 != null) {
            var5 = new Vec3D(var6.pos.a, var6.pos.b, var6.pos.c);
         }

         Entity var7 = null;
         List var8 = this.world.getEntities(this, this.getBoundingBox().a(this.motX, this.motY, this.motZ).grow(1.0D, 1.0D, 1.0D));
         double var9 = 0.0D;

         int var11;
         float var13;
         for(var11 = 0; var11 < var8.size(); ++var11) {
            Entity var12 = (Entity)var8.get(var11);
            if(var12.ad() && (var12 != this.shooter || this.as >= 5)) {
               var13 = 0.3F;
               AxisAlignedBB var14 = var12.getBoundingBox().grow((double)var13, (double)var13, (double)var13);
               MovingObjectPosition var15 = var14.a(var19, var5);
               if(var15 != null) {
                  double var16 = var19.distanceSquared(var15.pos);
                  if(var16 < var9 || var9 == 0.0D) {
                     var7 = var12;
                     var9 = var16;
                  }
               }
            }
         }

         if(var7 != null) {
            var6 = new MovingObjectPosition(var7);
         }

         if(var6 != null && var6.entity != null && var6.entity instanceof EntityHuman) {
            EntityHuman var21 = (EntityHuman)var6.entity;
            if(var21.abilities.isInvulnerable || this.shooter instanceof EntityHuman && !((EntityHuman)this.shooter).a(var21)) {
               var6 = null;
            }
         }

         float var22;
         float var30;
         if(var6 != null) {
            if(var6.entity != null) {
               var22 = MathHelper.sqrt(this.motX * this.motX + this.motY * this.motY + this.motZ * this.motZ);
               int var24 = MathHelper.f((double)var22 * this.damage);
               if(this.isCritical()) {
                  var24 += this.random.nextInt(var24 / 2 + 2);
               }

               DamageSource var26;
               if(this.shooter == null) {
                  var26 = DamageSource.arrow(this, this);
               } else {
                  var26 = DamageSource.arrow(this, this.shooter);
               }

               if(this.isBurning() && !(var6.entity instanceof EntityEnderman)) {
                  var6.entity.setOnFire(5);
               }

               if(var6.entity.damageEntity(var26, (float)var24)) {
                  if(var6.entity instanceof EntityLiving) {
                     EntityLiving var28 = (EntityLiving)var6.entity;
                     if(!this.world.isClientSide) {
                        var28.o(var28.bv() + 1);
                     }

                     if(this.knockbackStrength > 0) {
                        var30 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
                        if(var30 > 0.0F) {
                           var6.entity.g(this.motX * (double)this.knockbackStrength * 0.6000000238418579D / (double)var30, 0.1D, this.motZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)var30);
                        }
                     }

                     if(this.shooter instanceof EntityLiving) {
                        EnchantmentManager.a(var28, this.shooter);
                        EnchantmentManager.b((EntityLiving)this.shooter, var28);
                     }

                     if(this.shooter != null && var6.entity != this.shooter && var6.entity instanceof EntityHuman && this.shooter instanceof EntityPlayer) {
                        ((EntityPlayer)this.shooter).playerConnection.sendPacket(new PacketPlayOutGameStateChange(6, 0.0F));
                     }
                  }

                  this.makeSound("random.bowhit", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                  if(!(var6.entity instanceof EntityEnderman)) {
                     this.die();
                  }
               } else {
                  this.motX *= -0.10000000149011612D;
                  this.motY *= -0.10000000149011612D;
                  this.motZ *= -0.10000000149011612D;
                  this.yaw += 180.0F;
                  this.lastYaw += 180.0F;
                  this.as = 0;
               }
            } else {
               BlockPosition var23 = var6.a();
               this.d = var23.getX();
               this.e = var23.getY();
               this.f = var23.getZ();
               IBlockData var25 = this.world.getType(var23);
               this.g = var25.getBlock();
               this.h = this.g.toLegacyData(var25);
               this.motX = (double)((float)(var6.pos.a - this.locX));
               this.motY = (double)((float)(var6.pos.b - this.locY));
               this.motZ = (double)((float)(var6.pos.c - this.locZ));
               var13 = MathHelper.sqrt(this.motX * this.motX + this.motY * this.motY + this.motZ * this.motZ);
               this.locX -= this.motX / (double)var13 * 0.05000000074505806D;
               this.locY -= this.motY / (double)var13 * 0.05000000074505806D;
               this.locZ -= this.motZ / (double)var13 * 0.05000000074505806D;
               this.makeSound("random.bowhit", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
               this.inGround = true;
               this.shake = 7;
               this.setCritical(false);
               if(this.g.getMaterial() != Material.AIR) {
                  this.g.a((World)this.world, var23, (IBlockData)var25, (Entity)this);
               }
            }
         }

         if(this.isCritical()) {
            for(var11 = 0; var11 < 4; ++var11) {
               this.world.addParticle(EnumParticle.CRIT, this.locX + this.motX * (double)var11 / 4.0D, this.locY + this.motY * (double)var11 / 4.0D, this.locZ + this.motZ * (double)var11 / 4.0D, -this.motX, -this.motY + 0.2D, -this.motZ, new int[0]);
            }
         }

         this.locX += this.motX;
         this.locY += this.motY;
         this.locZ += this.motZ;
         var22 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
         this.yaw = (float)(MathHelper.b(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);

         for(this.pitch = (float)(MathHelper.b(this.motY, (double)var22) * 180.0D / 3.1415927410125732D); this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
            ;
         }

         while(this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
         }

         while(this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
         }

         while(this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
         }

         this.pitch = this.lastPitch + (this.pitch - this.lastPitch) * 0.2F;
         this.yaw = this.lastYaw + (this.yaw - this.lastYaw) * 0.2F;
         float var27 = 0.99F;
         var13 = 0.05F;
         if(this.V()) {
            for(int var29 = 0; var29 < 4; ++var29) {
               var30 = 0.25F;
               this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX - this.motX * (double)var30, this.locY - this.motY * (double)var30, this.locZ - this.motZ * (double)var30, this.motX, this.motY, this.motZ, new int[0]);
            }

            var27 = 0.6F;
         }

         if(this.U()) {
            this.extinguish();
         }

         this.motX *= (double)var27;
         this.motY *= (double)var27;
         this.motZ *= (double)var27;
         this.motY -= (double)var13;
         this.setPosition(this.locX, this.locY, this.locZ);
         this.checkBlockCollisions();
      }
   }

   public void b(NBTTagCompound var1) {
      var1.setShort("xTile", (short)this.d);
      var1.setShort("yTile", (short)this.e);
      var1.setShort("zTile", (short)this.f);
      var1.setShort("life", (short)this.ar);
      MinecraftKey var2 = (MinecraftKey)Block.REGISTRY.c(this.g);
      var1.setString("inTile", var2 == null?"":var2.toString());
      var1.setByte("inData", (byte)this.h);
      var1.setByte("shake", (byte)this.shake);
      var1.setByte("inGround", (byte)(this.inGround?1:0));
      var1.setByte("pickup", (byte)this.fromPlayer);
      var1.setDouble("damage", this.damage);
   }

   public void a(NBTTagCompound var1) {
      this.d = var1.getShort("xTile");
      this.e = var1.getShort("yTile");
      this.f = var1.getShort("zTile");
      this.ar = var1.getShort("life");
      if(var1.hasKeyOfType("inTile", 8)) {
         this.g = Block.getByName(var1.getString("inTile"));
      } else {
         this.g = Block.getById(var1.getByte("inTile") & 255);
      }

      this.h = var1.getByte("inData") & 255;
      this.shake = var1.getByte("shake") & 255;
      this.inGround = var1.getByte("inGround") == 1;
      if(var1.hasKeyOfType("damage", 99)) {
         this.damage = var1.getDouble("damage");
      }

      if(var1.hasKeyOfType("pickup", 99)) {
         this.fromPlayer = var1.getByte("pickup");
      } else if(var1.hasKeyOfType("player", 99)) {
         this.fromPlayer = var1.getBoolean("player")?1:0;
      }

   }

   public void d(EntityHuman var1) {
      if(!this.world.isClientSide && this.inGround && this.shake <= 0) {
         boolean var2 = this.fromPlayer == 1 || this.fromPlayer == 2 && var1.abilities.canInstantlyBuild;
         if(this.fromPlayer == 1 && !var1.inventory.pickup(new ItemStack(Items.ARROW, 1))) {
            var2 = false;
         }

         if(var2) {
            this.makeSound("random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            var1.receive(this, 1);
            this.die();
         }

      }
   }

   protected boolean s_() {
      return false;
   }

   public void b(double var1) {
      this.damage = var1;
   }

   public double j() {
      return this.damage;
   }

   public void setKnockbackStrength(int var1) {
      this.knockbackStrength = var1;
   }

   public boolean aD() {
      return false;
   }

   public float getHeadHeight() {
      return 0.0F;
   }

   public void setCritical(boolean var1) {
      byte var2 = this.datawatcher.getByte(16);
      if(var1) {
         this.datawatcher.watch(16, Byte.valueOf((byte)(var2 | 1)));
      } else {
         this.datawatcher.watch(16, Byte.valueOf((byte)(var2 & -2)));
      }

   }

   public boolean isCritical() {
      byte var1 = this.datawatcher.getByte(16);
      return (var1 & 1) != 0;
   }
}
