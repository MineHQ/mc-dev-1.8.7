package net.minecraft.server;

import net.minecraft.server.Entity;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityFireworks extends Entity {
   private int ticksFlown;
   public int expectedLifespan;

   public EntityFireworks(World var1) {
      super(var1);
      this.setSize(0.25F, 0.25F);
   }

   protected void h() {
      this.datawatcher.add(8, 5);
   }

   public EntityFireworks(World var1, double var2, double var4, double var6, ItemStack var8) {
      super(var1);
      this.ticksFlown = 0;
      this.setSize(0.25F, 0.25F);
      this.setPosition(var2, var4, var6);
      int var9 = 1;
      if(var8 != null && var8.hasTag()) {
         this.datawatcher.watch(8, var8);
         NBTTagCompound var10 = var8.getTag();
         NBTTagCompound var11 = var10.getCompound("Fireworks");
         if(var11 != null) {
            var9 += var11.getByte("Flight");
         }
      }

      this.motX = this.random.nextGaussian() * 0.001D;
      this.motZ = this.random.nextGaussian() * 0.001D;
      this.motY = 0.05D;
      this.expectedLifespan = 10 * var9 + this.random.nextInt(6) + this.random.nextInt(7);
   }

   public void t_() {
      this.P = this.locX;
      this.Q = this.locY;
      this.R = this.locZ;
      super.t_();
      this.motX *= 1.15D;
      this.motZ *= 1.15D;
      this.motY += 0.04D;
      this.move(this.motX, this.motY, this.motZ);
      float var1 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
      this.yaw = (float)(MathHelper.b(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);

      for(this.pitch = (float)(MathHelper.b(this.motY, (double)var1) * 180.0D / 3.1415927410125732D); this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
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
      if(this.ticksFlown == 0 && !this.R()) {
         this.world.makeSound(this, "fireworks.launch", 3.0F, 1.0F);
      }

      ++this.ticksFlown;
      if(this.world.isClientSide && this.ticksFlown % 2 < 2) {
         this.world.addParticle(EnumParticle.FIREWORKS_SPARK, this.locX, this.locY - 0.3D, this.locZ, this.random.nextGaussian() * 0.05D, -this.motY * 0.5D, this.random.nextGaussian() * 0.05D, new int[0]);
      }

      if(!this.world.isClientSide && this.ticksFlown > this.expectedLifespan) {
         this.world.broadcastEntityEffect(this, (byte)17);
         this.die();
      }

   }

   public void b(NBTTagCompound var1) {
      var1.setInt("Life", this.ticksFlown);
      var1.setInt("LifeTime", this.expectedLifespan);
      ItemStack var2 = this.datawatcher.getItemStack(8);
      if(var2 != null) {
         NBTTagCompound var3 = new NBTTagCompound();
         var2.save(var3);
         var1.set("FireworksItem", var3);
      }

   }

   public void a(NBTTagCompound var1) {
      this.ticksFlown = var1.getInt("Life");
      this.expectedLifespan = var1.getInt("LifeTime");
      NBTTagCompound var2 = var1.getCompound("FireworksItem");
      if(var2 != null) {
         ItemStack var3 = ItemStack.createStack(var2);
         if(var3 != null) {
            this.datawatcher.watch(8, var3);
         }
      }

   }

   public float c(float var1) {
      return super.c(var1);
   }

   public boolean aD() {
      return false;
   }
}
