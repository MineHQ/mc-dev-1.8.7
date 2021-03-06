package net.minecraft.server;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityExperienceOrb extends Entity {
   public int a;
   public int b;
   public int c;
   private int d = 5;
   public int value;
   private EntityHuman targetPlayer;
   private int targetTime;

   public EntityExperienceOrb(World var1, double var2, double var4, double var6, int var8) {
      super(var1);
      this.setSize(0.5F, 0.5F);
      this.setPosition(var2, var4, var6);
      this.yaw = (float)(Math.random() * 360.0D);
      this.motX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
      this.motY = (double)((float)(Math.random() * 0.2D) * 2.0F);
      this.motZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
      this.value = var8;
   }

   protected boolean s_() {
      return false;
   }

   public EntityExperienceOrb(World var1) {
      super(var1);
      this.setSize(0.25F, 0.25F);
   }

   protected void h() {
   }

   public void t_() {
      super.t_();
      if(this.c > 0) {
         --this.c;
      }

      this.lastX = this.locX;
      this.lastY = this.locY;
      this.lastZ = this.locZ;
      this.motY -= 0.029999999329447746D;
      if(this.world.getType(new BlockPosition(this)).getBlock().getMaterial() == Material.LAVA) {
         this.motY = 0.20000000298023224D;
         this.motX = (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
         this.motZ = (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
         this.makeSound("random.fizz", 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
      }

      this.j(this.locX, (this.getBoundingBox().b + this.getBoundingBox().e) / 2.0D, this.locZ);
      double var1 = 8.0D;
      if(this.targetTime < this.a - 20 + this.getId() % 100) {
         if(this.targetPlayer == null || this.targetPlayer.h(this) > var1 * var1) {
            this.targetPlayer = this.world.findNearbyPlayer(this, var1);
         }

         this.targetTime = this.a;
      }

      if(this.targetPlayer != null && this.targetPlayer.isSpectator()) {
         this.targetPlayer = null;
      }

      if(this.targetPlayer != null) {
         double var3 = (this.targetPlayer.locX - this.locX) / var1;
         double var5 = (this.targetPlayer.locY + (double)this.targetPlayer.getHeadHeight() - this.locY) / var1;
         double var7 = (this.targetPlayer.locZ - this.locZ) / var1;
         double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
         double var11 = 1.0D - var9;
         if(var11 > 0.0D) {
            var11 *= var11;
            this.motX += var3 / var9 * var11 * 0.1D;
            this.motY += var5 / var9 * var11 * 0.1D;
            this.motZ += var7 / var9 * var11 * 0.1D;
         }
      }

      this.move(this.motX, this.motY, this.motZ);
      float var13 = 0.98F;
      if(this.onGround) {
         var13 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(this.getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.98F;
      }

      this.motX *= (double)var13;
      this.motY *= 0.9800000190734863D;
      this.motZ *= (double)var13;
      if(this.onGround) {
         this.motY *= -0.8999999761581421D;
      }

      ++this.a;
      ++this.b;
      if(this.b >= 6000) {
         this.die();
      }

   }

   public boolean W() {
      return this.world.a((AxisAlignedBB)this.getBoundingBox(), (Material)Material.WATER, (Entity)this);
   }

   protected void burn(int var1) {
      this.damageEntity(DamageSource.FIRE, (float)var1);
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else {
         this.ac();
         this.d = (int)((float)this.d - var2);
         if(this.d <= 0) {
            this.die();
         }

         return false;
      }
   }

   public void b(NBTTagCompound var1) {
      var1.setShort("Health", (short)((byte)this.d));
      var1.setShort("Age", (short)this.b);
      var1.setShort("Value", (short)this.value);
   }

   public void a(NBTTagCompound var1) {
      this.d = var1.getShort("Health") & 255;
      this.b = var1.getShort("Age");
      this.value = var1.getShort("Value");
   }

   public void d(EntityHuman var1) {
      if(!this.world.isClientSide) {
         if(this.c == 0 && var1.bp == 0) {
            var1.bp = 2;
            this.world.makeSound(var1, "random.orb", 0.1F, 0.5F * ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.8F));
            var1.receive(this, 1);
            var1.giveExp(this.value);
            this.die();
         }

      }
   }

   public int j() {
      return this.value;
   }

   public static int getOrbValue(int var0) {
      return var0 >= 2477?2477:(var0 >= 1237?1237:(var0 >= 617?617:(var0 >= 307?307:(var0 >= 149?149:(var0 >= 73?73:(var0 >= 37?37:(var0 >= 17?17:(var0 >= 7?7:(var0 >= 3?3:1)))))))));
   }

   public boolean aD() {
      return false;
   }
}
