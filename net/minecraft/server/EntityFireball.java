package net.minecraft.server;

import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public abstract class EntityFireball extends Entity {
   private int e = -1;
   private int f = -1;
   private int g = -1;
   private Block h;
   private boolean i;
   public EntityLiving shooter;
   private int ar;
   private int as;
   public double dirX;
   public double dirY;
   public double dirZ;

   public EntityFireball(World var1) {
      super(var1);
      this.setSize(1.0F, 1.0F);
   }

   protected void h() {
   }

   public EntityFireball(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(var1);
      this.setSize(1.0F, 1.0F);
      this.setPositionRotation(var2, var4, var6, this.yaw, this.pitch);
      this.setPosition(var2, var4, var6);
      double var14 = (double)MathHelper.sqrt(var8 * var8 + var10 * var10 + var12 * var12);
      this.dirX = var8 / var14 * 0.1D;
      this.dirY = var10 / var14 * 0.1D;
      this.dirZ = var12 / var14 * 0.1D;
   }

   public EntityFireball(World var1, EntityLiving var2, double var3, double var5, double var7) {
      super(var1);
      this.shooter = var2;
      this.setSize(1.0F, 1.0F);
      this.setPositionRotation(var2.locX, var2.locY, var2.locZ, var2.yaw, var2.pitch);
      this.setPosition(this.locX, this.locY, this.locZ);
      this.motX = this.motY = this.motZ = 0.0D;
      var3 += this.random.nextGaussian() * 0.4D;
      var5 += this.random.nextGaussian() * 0.4D;
      var7 += this.random.nextGaussian() * 0.4D;
      double var9 = (double)MathHelper.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
      this.dirX = var3 / var9 * 0.1D;
      this.dirY = var5 / var9 * 0.1D;
      this.dirZ = var7 / var9 * 0.1D;
   }

   public void t_() {
      if(this.world.isClientSide || (this.shooter == null || !this.shooter.dead) && this.world.isLoaded(new BlockPosition(this))) {
         super.t_();
         this.setOnFire(1);
         if(this.i) {
            if(this.world.getType(new BlockPosition(this.e, this.f, this.g)).getBlock() == this.h) {
               ++this.ar;
               if(this.ar == 600) {
                  this.die();
               }

               return;
            }

            this.i = false;
            this.motX *= (double)(this.random.nextFloat() * 0.2F);
            this.motY *= (double)(this.random.nextFloat() * 0.2F);
            this.motZ *= (double)(this.random.nextFloat() * 0.2F);
            this.ar = 0;
            this.as = 0;
         } else {
            ++this.as;
         }

         Vec3D var1 = new Vec3D(this.locX, this.locY, this.locZ);
         Vec3D var2 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
         MovingObjectPosition var3 = this.world.rayTrace(var1, var2);
         var1 = new Vec3D(this.locX, this.locY, this.locZ);
         var2 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
         if(var3 != null) {
            var2 = new Vec3D(var3.pos.a, var3.pos.b, var3.pos.c);
         }

         Entity var4 = null;
         List var5 = this.world.getEntities(this, this.getBoundingBox().a(this.motX, this.motY, this.motZ).grow(1.0D, 1.0D, 1.0D));
         double var6 = 0.0D;

         for(int var8 = 0; var8 < var5.size(); ++var8) {
            Entity var9 = (Entity)var5.get(var8);
            if(var9.ad() && (!var9.k(this.shooter) || this.as >= 25)) {
               float var10 = 0.3F;
               AxisAlignedBB var11 = var9.getBoundingBox().grow((double)var10, (double)var10, (double)var10);
               MovingObjectPosition var12 = var11.a(var1, var2);
               if(var12 != null) {
                  double var13 = var1.distanceSquared(var12.pos);
                  if(var13 < var6 || var6 == 0.0D) {
                     var4 = var9;
                     var6 = var13;
                  }
               }
            }
         }

         if(var4 != null) {
            var3 = new MovingObjectPosition(var4);
         }

         if(var3 != null) {
            this.a(var3);
         }

         this.locX += this.motX;
         this.locY += this.motY;
         this.locZ += this.motZ;
         float var15 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
         this.yaw = (float)(MathHelper.b(this.motZ, this.motX) * 180.0D / 3.1415927410125732D) + 90.0F;

         for(this.pitch = (float)(MathHelper.b((double)var15, this.motY) * 180.0D / 3.1415927410125732D) - 90.0F; this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
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
         float var16 = this.j();
         if(this.V()) {
            for(int var17 = 0; var17 < 4; ++var17) {
               float var18 = 0.25F;
               this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX - this.motX * (double)var18, this.locY - this.motY * (double)var18, this.locZ - this.motZ * (double)var18, this.motX, this.motY, this.motZ, new int[0]);
            }

            var16 = 0.8F;
         }

         this.motX += this.dirX;
         this.motY += this.dirY;
         this.motZ += this.dirZ;
         this.motX *= (double)var16;
         this.motY *= (double)var16;
         this.motZ *= (double)var16;
         this.world.addParticle(EnumParticle.SMOKE_NORMAL, this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D, new int[0]);
         this.setPosition(this.locX, this.locY, this.locZ);
      } else {
         this.die();
      }
   }

   protected float j() {
      return 0.95F;
   }

   protected abstract void a(MovingObjectPosition var1);

   public void b(NBTTagCompound var1) {
      var1.setShort("xTile", (short)this.e);
      var1.setShort("yTile", (short)this.f);
      var1.setShort("zTile", (short)this.g);
      MinecraftKey var2 = (MinecraftKey)Block.REGISTRY.c(this.h);
      var1.setString("inTile", var2 == null?"":var2.toString());
      var1.setByte("inGround", (byte)(this.i?1:0));
      var1.set("direction", this.a((double[])(new double[]{this.motX, this.motY, this.motZ})));
   }

   public void a(NBTTagCompound var1) {
      this.e = var1.getShort("xTile");
      this.f = var1.getShort("yTile");
      this.g = var1.getShort("zTile");
      if(var1.hasKeyOfType("inTile", 8)) {
         this.h = Block.getByName(var1.getString("inTile"));
      } else {
         this.h = Block.getById(var1.getByte("inTile") & 255);
      }

      this.i = var1.getByte("inGround") == 1;
      if(var1.hasKeyOfType("direction", 9)) {
         NBTTagList var2 = var1.getList("direction", 6);
         this.motX = var2.d(0);
         this.motY = var2.d(1);
         this.motZ = var2.d(2);
      } else {
         this.die();
      }

   }

   public boolean ad() {
      return true;
   }

   public float ao() {
      return 1.0F;
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else {
         this.ac();
         if(var1.getEntity() != null) {
            Vec3D var3 = var1.getEntity().ap();
            if(var3 != null) {
               this.motX = var3.a;
               this.motY = var3.b;
               this.motZ = var3.c;
               this.dirX = this.motX * 0.1D;
               this.dirY = this.motY * 0.1D;
               this.dirZ = this.motZ * 0.1D;
            }

            if(var1.getEntity() instanceof EntityLiving) {
               this.shooter = (EntityLiving)var1.getEntity();
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public float c(float var1) {
      return 1.0F;
   }
}
