package net.minecraft.server;

import java.util.UUID;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityOwnable;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NameReferencingFileConverter;
import net.minecraft.server.PathfinderGoalSit;
import net.minecraft.server.ScoreboardTeamBase;
import net.minecraft.server.World;

public abstract class EntityTameableAnimal extends EntityAnimal implements EntityOwnable {
   protected PathfinderGoalSit bm = new PathfinderGoalSit(this);

   public EntityTameableAnimal(World var1) {
      super(var1);
      this.cm();
   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, Byte.valueOf((byte)0));
      this.datawatcher.a(17, "");
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      if(this.getOwnerUUID() == null) {
         var1.setString("OwnerUUID", "");
      } else {
         var1.setString("OwnerUUID", this.getOwnerUUID());
      }

      var1.setBoolean("Sitting", this.isSitting());
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      String var2 = "";
      if(var1.hasKeyOfType("OwnerUUID", 8)) {
         var2 = var1.getString("OwnerUUID");
      } else {
         String var3 = var1.getString("Owner");
         var2 = NameReferencingFileConverter.a(var3);
      }

      if(var2.length() > 0) {
         this.setOwnerUUID(var2);
         this.setTamed(true);
      }

      this.bm.setSitting(var1.getBoolean("Sitting"));
      this.setSitting(var1.getBoolean("Sitting"));
   }

   protected void l(boolean var1) {
      EnumParticle var2 = EnumParticle.HEART;
      if(!var1) {
         var2 = EnumParticle.SMOKE_NORMAL;
      }

      for(int var3 = 0; var3 < 7; ++var3) {
         double var4 = this.random.nextGaussian() * 0.02D;
         double var6 = this.random.nextGaussian() * 0.02D;
         double var8 = this.random.nextGaussian() * 0.02D;
         this.world.addParticle(var2, this.locX + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, this.locY + 0.5D + (double)(this.random.nextFloat() * this.length), this.locZ + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, var4, var6, var8, new int[0]);
      }

   }

   public boolean isTamed() {
      return (this.datawatcher.getByte(16) & 4) != 0;
   }

   public void setTamed(boolean var1) {
      byte var2 = this.datawatcher.getByte(16);
      if(var1) {
         this.datawatcher.watch(16, Byte.valueOf((byte)(var2 | 4)));
      } else {
         this.datawatcher.watch(16, Byte.valueOf((byte)(var2 & -5)));
      }

      this.cm();
   }

   protected void cm() {
   }

   public boolean isSitting() {
      return (this.datawatcher.getByte(16) & 1) != 0;
   }

   public void setSitting(boolean var1) {
      byte var2 = this.datawatcher.getByte(16);
      if(var1) {
         this.datawatcher.watch(16, Byte.valueOf((byte)(var2 | 1)));
      } else {
         this.datawatcher.watch(16, Byte.valueOf((byte)(var2 & -2)));
      }

   }

   public String getOwnerUUID() {
      return this.datawatcher.getString(17);
   }

   public void setOwnerUUID(String var1) {
      this.datawatcher.watch(17, var1);
   }

   public EntityLiving getOwner() {
      try {
         UUID var1 = UUID.fromString(this.getOwnerUUID());
         return var1 == null?null:this.world.b(var1);
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   public boolean e(EntityLiving var1) {
      return var1 == this.getOwner();
   }

   public PathfinderGoalSit getGoalSit() {
      return this.bm;
   }

   public boolean a(EntityLiving var1, EntityLiving var2) {
      return true;
   }

   public ScoreboardTeamBase getScoreboardTeam() {
      if(this.isTamed()) {
         EntityLiving var1 = this.getOwner();
         if(var1 != null) {
            return var1.getScoreboardTeam();
         }
      }

      return super.getScoreboardTeam();
   }

   public boolean c(EntityLiving var1) {
      if(this.isTamed()) {
         EntityLiving var2 = this.getOwner();
         if(var1 == var2) {
            return true;
         }

         if(var2 != null) {
            return var2.c(var1);
         }
      }

      return super.c(var1);
   }

   public void die(DamageSource var1) {
      if(!this.world.isClientSide && this.world.getGameRules().getBoolean("showDeathMessages") && this.hasCustomName() && this.getOwner() instanceof EntityPlayer) {
         ((EntityPlayer)this.getOwner()).sendMessage(this.bs().b());
      }

      super.die(var1);
   }

   // $FF: synthetic method
   public Entity getOwner() {
      return this.getOwner();
   }
}
