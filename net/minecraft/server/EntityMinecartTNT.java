package net.minecraft.server;

import net.minecraft.server.BlockMinecartTrackAbstract;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.Explosion;
import net.minecraft.server.IBlockData;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityMinecartTNT extends EntityMinecartAbstract {
   private int a = -1;

   public EntityMinecartTNT(World var1) {
      super(var1);
   }

   public EntityMinecartTNT(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   public EntityMinecartAbstract.EnumMinecartType s() {
      return EntityMinecartAbstract.EnumMinecartType.TNT;
   }

   public IBlockData u() {
      return Blocks.TNT.getBlockData();
   }

   public void t_() {
      super.t_();
      if(this.a > 0) {
         --this.a;
         this.world.addParticle(EnumParticle.SMOKE_NORMAL, this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D, new int[0]);
      } else if(this.a == 0) {
         this.b(this.motX * this.motX + this.motZ * this.motZ);
      }

      if(this.positionChanged) {
         double var1 = this.motX * this.motX + this.motZ * this.motZ;
         if(var1 >= 0.009999999776482582D) {
            this.b(var1);
         }
      }

   }

   public boolean damageEntity(DamageSource var1, float var2) {
      Entity var3 = var1.i();
      if(var3 instanceof EntityArrow) {
         EntityArrow var4 = (EntityArrow)var3;
         if(var4.isBurning()) {
            this.b(var4.motX * var4.motX + var4.motY * var4.motY + var4.motZ * var4.motZ);
         }
      }

      return super.damageEntity(var1, var2);
   }

   public void a(DamageSource var1) {
      super.a(var1);
      double var2 = this.motX * this.motX + this.motZ * this.motZ;
      if(!var1.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
         this.a(new ItemStack(Blocks.TNT, 1), 0.0F);
      }

      if(var1.o() || var1.isExplosion() || var2 >= 0.009999999776482582D) {
         this.b(var2);
      }

   }

   protected void b(double var1) {
      if(!this.world.isClientSide) {
         double var3 = Math.sqrt(var1);
         if(var3 > 5.0D) {
            var3 = 5.0D;
         }

         this.world.explode(this, this.locX, this.locY, this.locZ, (float)(4.0D + this.random.nextDouble() * 1.5D * var3), true);
         this.die();
      }

   }

   public void e(float var1, float var2) {
      if(var1 >= 3.0F) {
         float var3 = var1 / 10.0F;
         this.b((double)(var3 * var3));
      }

      super.e(var1, var2);
   }

   public void a(int var1, int var2, int var3, boolean var4) {
      if(var4 && this.a < 0) {
         this.j();
      }

   }

   public void j() {
      this.a = 80;
      if(!this.world.isClientSide) {
         this.world.broadcastEntityEffect(this, (byte)10);
         if(!this.R()) {
            this.world.makeSound(this, "game.tnt.primed", 1.0F, 1.0F);
         }
      }

   }

   public boolean y() {
      return this.a > -1;
   }

   public float a(Explosion var1, World var2, BlockPosition var3, IBlockData var4) {
      return !this.y() || !BlockMinecartTrackAbstract.d(var4) && !BlockMinecartTrackAbstract.e(var2, var3.up())?super.a(var1, var2, var3, var4):0.0F;
   }

   public boolean a(Explosion var1, World var2, BlockPosition var3, IBlockData var4, float var5) {
      return !this.y() || !BlockMinecartTrackAbstract.d(var4) && !BlockMinecartTrackAbstract.e(var2, var3.up())?super.a(var1, var2, var3, var4, var5):false;
   }

   protected void a(NBTTagCompound var1) {
      super.a(var1);
      if(var1.hasKeyOfType("TNTFuse", 99)) {
         this.a = var1.getInt("TNTFuse");
      }

   }

   protected void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("TNTFuse", this.a);
   }
}
