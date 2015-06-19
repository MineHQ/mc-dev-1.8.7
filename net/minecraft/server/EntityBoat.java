package net.minecraft.server;

import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityDamageSourceIndirect;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityBoat extends Entity {
   private boolean a;
   private double b;
   private int c;
   private double d;
   private double e;
   private double f;
   private double g;
   private double h;

   public EntityBoat(World var1) {
      super(var1);
      this.a = true;
      this.b = 0.07D;
      this.k = true;
      this.setSize(1.5F, 0.6F);
   }

   protected boolean s_() {
      return false;
   }

   protected void h() {
      this.datawatcher.a(17, new Integer(0));
      this.datawatcher.a(18, new Integer(1));
      this.datawatcher.a(19, new Float(0.0F));
   }

   public AxisAlignedBB j(Entity var1) {
      return var1.getBoundingBox();
   }

   public AxisAlignedBB S() {
      return this.getBoundingBox();
   }

   public boolean ae() {
      return true;
   }

   public EntityBoat(World var1, double var2, double var4, double var6) {
      this(var1);
      this.setPosition(var2, var4, var6);
      this.motX = 0.0D;
      this.motY = 0.0D;
      this.motZ = 0.0D;
      this.lastX = var2;
      this.lastY = var4;
      this.lastZ = var6;
   }

   public double an() {
      return -0.3D;
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else if(!this.world.isClientSide && !this.dead) {
         if(this.passenger != null && this.passenger == var1.getEntity() && var1 instanceof EntityDamageSourceIndirect) {
            return false;
         } else {
            this.b(-this.m());
            this.a(10);
            this.setDamage(this.j() + var2 * 10.0F);
            this.ac();
            boolean var3 = var1.getEntity() instanceof EntityHuman && ((EntityHuman)var1.getEntity()).abilities.canInstantlyBuild;
            if(var3 || this.j() > 40.0F) {
               if(this.passenger != null) {
                  this.passenger.mount(this);
               }

               if(!var3 && this.world.getGameRules().getBoolean("doEntityDrops")) {
                  this.a(Items.BOAT, 1, 0.0F);
               }

               this.die();
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public boolean ad() {
      return !this.dead;
   }

   public void t_() {
      super.t_();
      if(this.l() > 0) {
         this.a(this.l() - 1);
      }

      if(this.j() > 0.0F) {
         this.setDamage(this.j() - 1.0F);
      }

      this.lastX = this.locX;
      this.lastY = this.locY;
      this.lastZ = this.locZ;
      byte var1 = 5;
      double var2 = 0.0D;

      for(int var4 = 0; var4 < var1; ++var4) {
         double var5 = this.getBoundingBox().b + (this.getBoundingBox().e - this.getBoundingBox().b) * (double)(var4 + 0) / (double)var1 - 0.125D;
         double var7 = this.getBoundingBox().b + (this.getBoundingBox().e - this.getBoundingBox().b) * (double)(var4 + 1) / (double)var1 - 0.125D;
         AxisAlignedBB var9 = new AxisAlignedBB(this.getBoundingBox().a, var5, this.getBoundingBox().c, this.getBoundingBox().d, var7, this.getBoundingBox().f);
         if(this.world.b(var9, Material.WATER)) {
            var2 += 1.0D / (double)var1;
         }
      }

      double var19 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
      double var6;
      double var8;
      int var10;
      if(var19 > 0.2975D) {
         var6 = Math.cos((double)this.yaw * 3.141592653589793D / 180.0D);
         var8 = Math.sin((double)this.yaw * 3.141592653589793D / 180.0D);

         for(var10 = 0; (double)var10 < 1.0D + var19 * 60.0D; ++var10) {
            double var11 = (double)(this.random.nextFloat() * 2.0F - 1.0F);
            double var13 = (double)(this.random.nextInt(2) * 2 - 1) * 0.7D;
            double var15;
            double var17;
            if(this.random.nextBoolean()) {
               var15 = this.locX - var6 * var11 * 0.8D + var8 * var13;
               var17 = this.locZ - var8 * var11 * 0.8D - var6 * var13;
               this.world.addParticle(EnumParticle.WATER_SPLASH, var15, this.locY - 0.125D, var17, this.motX, this.motY, this.motZ, new int[0]);
            } else {
               var15 = this.locX + var6 + var8 * var11 * 0.7D;
               var17 = this.locZ + var8 - var6 * var11 * 0.7D;
               this.world.addParticle(EnumParticle.WATER_SPLASH, var15, this.locY - 0.125D, var17, this.motX, this.motY, this.motZ, new int[0]);
            }
         }
      }

      double var24;
      double var26;
      if(this.world.isClientSide && this.a) {
         if(this.c > 0) {
            var6 = this.locX + (this.d - this.locX) / (double)this.c;
            var8 = this.locY + (this.e - this.locY) / (double)this.c;
            var24 = this.locZ + (this.f - this.locZ) / (double)this.c;
            var26 = MathHelper.g(this.g - (double)this.yaw);
            this.yaw = (float)((double)this.yaw + var26 / (double)this.c);
            this.pitch = (float)((double)this.pitch + (this.h - (double)this.pitch) / (double)this.c);
            --this.c;
            this.setPosition(var6, var8, var24);
            this.setYawPitch(this.yaw, this.pitch);
         } else {
            var6 = this.locX + this.motX;
            var8 = this.locY + this.motY;
            var24 = this.locZ + this.motZ;
            this.setPosition(var6, var8, var24);
            if(this.onGround) {
               this.motX *= 0.5D;
               this.motY *= 0.5D;
               this.motZ *= 0.5D;
            }

            this.motX *= 0.9900000095367432D;
            this.motY *= 0.949999988079071D;
            this.motZ *= 0.9900000095367432D;
         }

      } else {
         if(var2 < 1.0D) {
            var6 = var2 * 2.0D - 1.0D;
            this.motY += 0.03999999910593033D * var6;
         } else {
            if(this.motY < 0.0D) {
               this.motY /= 2.0D;
            }

            this.motY += 0.007000000216066837D;
         }

         if(this.passenger instanceof EntityLiving) {
            EntityLiving var20 = (EntityLiving)this.passenger;
            float var21 = this.passenger.yaw + -var20.aZ * 90.0F;
            this.motX += -Math.sin((double)(var21 * 3.1415927F / 180.0F)) * this.b * (double)var20.ba * 0.05000000074505806D;
            this.motZ += Math.cos((double)(var21 * 3.1415927F / 180.0F)) * this.b * (double)var20.ba * 0.05000000074505806D;
         }

         var6 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
         if(var6 > 0.35D) {
            var8 = 0.35D / var6;
            this.motX *= var8;
            this.motZ *= var8;
            var6 = 0.35D;
         }

         if(var6 > var19 && this.b < 0.35D) {
            this.b += (0.35D - this.b) / 35.0D;
            if(this.b > 0.35D) {
               this.b = 0.35D;
            }
         } else {
            this.b -= (this.b - 0.07D) / 35.0D;
            if(this.b < 0.07D) {
               this.b = 0.07D;
            }
         }

         int var22;
         for(var22 = 0; var22 < 4; ++var22) {
            int var23 = MathHelper.floor(this.locX + ((double)(var22 % 2) - 0.5D) * 0.8D);
            var10 = MathHelper.floor(this.locZ + ((double)(var22 / 2) - 0.5D) * 0.8D);

            for(int var25 = 0; var25 < 2; ++var25) {
               int var12 = MathHelper.floor(this.locY) + var25;
               BlockPosition var27 = new BlockPosition(var23, var12, var10);
               Block var14 = this.world.getType(var27).getBlock();
               if(var14 == Blocks.SNOW_LAYER) {
                  this.world.setAir(var27);
                  this.positionChanged = false;
               } else if(var14 == Blocks.WATERLILY) {
                  this.world.setAir(var27, true);
                  this.positionChanged = false;
               }
            }
         }

         if(this.onGround) {
            this.motX *= 0.5D;
            this.motY *= 0.5D;
            this.motZ *= 0.5D;
         }

         this.move(this.motX, this.motY, this.motZ);
         if(this.positionChanged && var19 > 0.2975D) {
            if(!this.world.isClientSide && !this.dead) {
               this.die();
               if(this.world.getGameRules().getBoolean("doEntityDrops")) {
                  for(var22 = 0; var22 < 3; ++var22) {
                     this.a(Item.getItemOf(Blocks.PLANKS), 1, 0.0F);
                  }

                  for(var22 = 0; var22 < 2; ++var22) {
                     this.a(Items.STICK, 1, 0.0F);
                  }
               }
            }
         } else {
            this.motX *= 0.9900000095367432D;
            this.motY *= 0.949999988079071D;
            this.motZ *= 0.9900000095367432D;
         }

         this.pitch = 0.0F;
         var8 = (double)this.yaw;
         var24 = this.lastX - this.locX;
         var26 = this.lastZ - this.locZ;
         if(var24 * var24 + var26 * var26 > 0.001D) {
            var8 = (double)((float)(MathHelper.b(var26, var24) * 180.0D / 3.141592653589793D));
         }

         double var28 = MathHelper.g(var8 - (double)this.yaw);
         if(var28 > 20.0D) {
            var28 = 20.0D;
         }

         if(var28 < -20.0D) {
            var28 = -20.0D;
         }

         this.yaw = (float)((double)this.yaw + var28);
         this.setYawPitch(this.yaw, this.pitch);
         if(!this.world.isClientSide) {
            List var16 = this.world.getEntities(this, this.getBoundingBox().grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));
            if(var16 != null && !var16.isEmpty()) {
               for(int var29 = 0; var29 < var16.size(); ++var29) {
                  Entity var18 = (Entity)var16.get(var29);
                  if(var18 != this.passenger && var18.ae() && var18 instanceof EntityBoat) {
                     var18.collide(this);
                  }
               }
            }

            if(this.passenger != null && this.passenger.dead) {
               this.passenger = null;
            }

         }
      }
   }

   public void al() {
      if(this.passenger != null) {
         double var1 = Math.cos((double)this.yaw * 3.141592653589793D / 180.0D) * 0.4D;
         double var3 = Math.sin((double)this.yaw * 3.141592653589793D / 180.0D) * 0.4D;
         this.passenger.setPosition(this.locX + var1, this.locY + this.an() + this.passenger.am(), this.locZ + var3);
      }
   }

   protected void b(NBTTagCompound var1) {
   }

   protected void a(NBTTagCompound var1) {
   }

   public boolean e(EntityHuman var1) {
      if(this.passenger != null && this.passenger instanceof EntityHuman && this.passenger != var1) {
         return true;
      } else {
         if(!this.world.isClientSide) {
            var1.mount(this);
         }

         return true;
      }
   }

   protected void a(double var1, boolean var3, Block var4, BlockPosition var5) {
      if(var3) {
         if(this.fallDistance > 3.0F) {
            this.e(this.fallDistance, 1.0F);
            if(!this.world.isClientSide && !this.dead) {
               this.die();
               if(this.world.getGameRules().getBoolean("doEntityDrops")) {
                  int var6;
                  for(var6 = 0; var6 < 3; ++var6) {
                     this.a(Item.getItemOf(Blocks.PLANKS), 1, 0.0F);
                  }

                  for(var6 = 0; var6 < 2; ++var6) {
                     this.a(Items.STICK, 1, 0.0F);
                  }
               }
            }

            this.fallDistance = 0.0F;
         }
      } else if(this.world.getType((new BlockPosition(this)).down()).getBlock().getMaterial() != Material.WATER && var1 < 0.0D) {
         this.fallDistance = (float)((double)this.fallDistance - var1);
      }

   }

   public void setDamage(float var1) {
      this.datawatcher.watch(19, Float.valueOf(var1));
   }

   public float j() {
      return this.datawatcher.getFloat(19);
   }

   public void a(int var1) {
      this.datawatcher.watch(17, Integer.valueOf(var1));
   }

   public int l() {
      return this.datawatcher.getInt(17);
   }

   public void b(int var1) {
      this.datawatcher.watch(18, Integer.valueOf(var1));
   }

   public int m() {
      return this.datawatcher.getInt(18);
   }
}
