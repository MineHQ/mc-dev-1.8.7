package net.minecraft.server;

import java.util.List;
import java.util.UUID;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.IProjectile;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;

public abstract class EntityProjectile extends Entity implements IProjectile {
   private int blockX = -1;
   private int blockY = -1;
   private int blockZ = -1;
   private Block inBlockId;
   protected boolean inGround;
   public int shake;
   public EntityLiving shooter;
   public String shooterName;
   private int i;
   private int ar;

   public EntityProjectile(World var1) {
      super(var1);
      this.setSize(0.25F, 0.25F);
   }

   protected void h() {
   }

   public EntityProjectile(World var1, EntityLiving var2) {
      super(var1);
      this.shooter = var2;
      this.setSize(0.25F, 0.25F);
      this.setPositionRotation(var2.locX, var2.locY + (double)var2.getHeadHeight(), var2.locZ, var2.yaw, var2.pitch);
      this.locX -= (double)(MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F);
      this.locY -= 0.10000000149011612D;
      this.locZ -= (double)(MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F);
      this.setPosition(this.locX, this.locY, this.locZ);
      float var3 = 0.4F;
      this.motX = (double)(-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * var3);
      this.motZ = (double)(MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * var3);
      this.motY = (double)(-MathHelper.sin((this.pitch + this.l()) / 180.0F * 3.1415927F) * var3);
      this.shoot(this.motX, this.motY, this.motZ, this.j(), 1.0F);
   }

   public EntityProjectile(World var1, double var2, double var4, double var6) {
      super(var1);
      this.i = 0;
      this.setSize(0.25F, 0.25F);
      this.setPosition(var2, var4, var6);
   }

   protected float j() {
      return 1.5F;
   }

   protected float l() {
      return 0.0F;
   }

   public void shoot(double var1, double var3, double var5, float var7, float var8) {
      float var9 = MathHelper.sqrt(var1 * var1 + var3 * var3 + var5 * var5);
      var1 /= (double)var9;
      var3 /= (double)var9;
      var5 /= (double)var9;
      var1 += this.random.nextGaussian() * 0.007499999832361937D * (double)var8;
      var3 += this.random.nextGaussian() * 0.007499999832361937D * (double)var8;
      var5 += this.random.nextGaussian() * 0.007499999832361937D * (double)var8;
      var1 *= (double)var7;
      var3 *= (double)var7;
      var5 *= (double)var7;
      this.motX = var1;
      this.motY = var3;
      this.motZ = var5;
      float var10 = MathHelper.sqrt(var1 * var1 + var5 * var5);
      this.lastYaw = this.yaw = (float)(MathHelper.b(var1, var5) * 180.0D / 3.1415927410125732D);
      this.lastPitch = this.pitch = (float)(MathHelper.b(var3, (double)var10) * 180.0D / 3.1415927410125732D);
      this.i = 0;
   }

   public void t_() {
      this.P = this.locX;
      this.Q = this.locY;
      this.R = this.locZ;
      super.t_();
      if(this.shake > 0) {
         --this.shake;
      }

      if(this.inGround) {
         if(this.world.getType(new BlockPosition(this.blockX, this.blockY, this.blockZ)).getBlock() == this.inBlockId) {
            ++this.i;
            if(this.i == 1200) {
               this.die();
            }

            return;
         }

         this.inGround = false;
         this.motX *= (double)(this.random.nextFloat() * 0.2F);
         this.motY *= (double)(this.random.nextFloat() * 0.2F);
         this.motZ *= (double)(this.random.nextFloat() * 0.2F);
         this.i = 0;
         this.ar = 0;
      } else {
         ++this.ar;
      }

      Vec3D var1 = new Vec3D(this.locX, this.locY, this.locZ);
      Vec3D var2 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
      MovingObjectPosition var3 = this.world.rayTrace(var1, var2);
      var1 = new Vec3D(this.locX, this.locY, this.locZ);
      var2 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
      if(var3 != null) {
         var2 = new Vec3D(var3.pos.a, var3.pos.b, var3.pos.c);
      }

      if(!this.world.isClientSide) {
         Entity var4 = null;
         List var5 = this.world.getEntities(this, this.getBoundingBox().a(this.motX, this.motY, this.motZ).grow(1.0D, 1.0D, 1.0D));
         double var6 = 0.0D;
         EntityLiving var8 = this.getShooter();

         for(int var9 = 0; var9 < var5.size(); ++var9) {
            Entity var10 = (Entity)var5.get(var9);
            if(var10.ad() && (var10 != var8 || this.ar >= 5)) {
               float var11 = 0.3F;
               AxisAlignedBB var12 = var10.getBoundingBox().grow((double)var11, (double)var11, (double)var11);
               MovingObjectPosition var13 = var12.a(var1, var2);
               if(var13 != null) {
                  double var14 = var1.distanceSquared(var13.pos);
                  if(var14 < var6 || var6 == 0.0D) {
                     var4 = var10;
                     var6 = var14;
                  }
               }
            }
         }

         if(var4 != null) {
            var3 = new MovingObjectPosition(var4);
         }
      }

      if(var3 != null) {
         if(var3.type == MovingObjectPosition.EnumMovingObjectType.BLOCK && this.world.getType(var3.a()).getBlock() == Blocks.PORTAL) {
            this.d(var3.a());
         } else {
            this.a(var3);
         }
      }

      this.locX += this.motX;
      this.locY += this.motY;
      this.locZ += this.motZ;
      float var16 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
      this.yaw = (float)(MathHelper.b(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);

      for(this.pitch = (float)(MathHelper.b(this.motY, (double)var16) * 180.0D / 3.1415927410125732D); this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
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
      float var17 = 0.99F;
      float var18 = this.m();
      if(this.V()) {
         for(int var7 = 0; var7 < 4; ++var7) {
            float var19 = 0.25F;
            this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX - this.motX * (double)var19, this.locY - this.motY * (double)var19, this.locZ - this.motZ * (double)var19, this.motX, this.motY, this.motZ, new int[0]);
         }

         var17 = 0.8F;
      }

      this.motX *= (double)var17;
      this.motY *= (double)var17;
      this.motZ *= (double)var17;
      this.motY -= (double)var18;
      this.setPosition(this.locX, this.locY, this.locZ);
   }

   protected float m() {
      return 0.03F;
   }

   protected abstract void a(MovingObjectPosition var1);

   public void b(NBTTagCompound var1) {
      var1.setShort("xTile", (short)this.blockX);
      var1.setShort("yTile", (short)this.blockY);
      var1.setShort("zTile", (short)this.blockZ);
      MinecraftKey var2 = (MinecraftKey)Block.REGISTRY.c(this.inBlockId);
      var1.setString("inTile", var2 == null?"":var2.toString());
      var1.setByte("shake", (byte)this.shake);
      var1.setByte("inGround", (byte)(this.inGround?1:0));
      if((this.shooterName == null || this.shooterName.length() == 0) && this.shooter instanceof EntityHuman) {
         this.shooterName = this.shooter.getName();
      }

      var1.setString("ownerName", this.shooterName == null?"":this.shooterName);
   }

   public void a(NBTTagCompound var1) {
      this.blockX = var1.getShort("xTile");
      this.blockY = var1.getShort("yTile");
      this.blockZ = var1.getShort("zTile");
      if(var1.hasKeyOfType("inTile", 8)) {
         this.inBlockId = Block.getByName(var1.getString("inTile"));
      } else {
         this.inBlockId = Block.getById(var1.getByte("inTile") & 255);
      }

      this.shake = var1.getByte("shake") & 255;
      this.inGround = var1.getByte("inGround") == 1;
      this.shooter = null;
      this.shooterName = var1.getString("ownerName");
      if(this.shooterName != null && this.shooterName.length() == 0) {
         this.shooterName = null;
      }

      this.shooter = this.getShooter();
   }

   public EntityLiving getShooter() {
      if(this.shooter == null && this.shooterName != null && this.shooterName.length() > 0) {
         this.shooter = this.world.a(this.shooterName);
         if(this.shooter == null && this.world instanceof WorldServer) {
            try {
               Entity var1 = ((WorldServer)this.world).getEntity(UUID.fromString(this.shooterName));
               if(var1 instanceof EntityLiving) {
                  this.shooter = (EntityLiving)var1;
               }
            } catch (Throwable var2) {
               this.shooter = null;
            }
         }
      }

      return this.shooter;
   }
}
