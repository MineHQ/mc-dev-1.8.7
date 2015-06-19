package net.minecraft.server;

import net.minecraft.server.BlockFurnace;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.IBlockData;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityMinecartFurnace extends EntityMinecartAbstract {
   private int c;
   public double a;
   public double b;

   public EntityMinecartFurnace(World var1) {
      super(var1);
   }

   public EntityMinecartFurnace(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   public EntityMinecartAbstract.EnumMinecartType s() {
      return EntityMinecartAbstract.EnumMinecartType.FURNACE;
   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, new Byte((byte)0));
   }

   public void t_() {
      super.t_();
      if(this.c > 0) {
         --this.c;
      }

      if(this.c <= 0) {
         this.a = this.b = 0.0D;
      }

      this.i(this.c > 0);
      if(this.j() && this.random.nextInt(4) == 0) {
         this.world.addParticle(EnumParticle.SMOKE_LARGE, this.locX, this.locY + 0.8D, this.locZ, 0.0D, 0.0D, 0.0D, new int[0]);
      }

   }

   protected double m() {
      return 0.2D;
   }

   public void a(DamageSource var1) {
      super.a(var1);
      if(!var1.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
         this.a(new ItemStack(Blocks.FURNACE, 1), 0.0F);
      }

   }

   protected void a(BlockPosition var1, IBlockData var2) {
      super.a(var1, var2);
      double var3 = this.a * this.a + this.b * this.b;
      if(var3 > 1.0E-4D && this.motX * this.motX + this.motZ * this.motZ > 0.001D) {
         var3 = (double)MathHelper.sqrt(var3);
         this.a /= var3;
         this.b /= var3;
         if(this.a * this.motX + this.b * this.motZ < 0.0D) {
            this.a = 0.0D;
            this.b = 0.0D;
         } else {
            double var5 = var3 / this.m();
            this.a *= var5;
            this.b *= var5;
         }
      }

   }

   protected void o() {
      double var1 = this.a * this.a + this.b * this.b;
      if(var1 > 1.0E-4D) {
         var1 = (double)MathHelper.sqrt(var1);
         this.a /= var1;
         this.b /= var1;
         double var3 = 1.0D;
         this.motX *= 0.800000011920929D;
         this.motY *= 0.0D;
         this.motZ *= 0.800000011920929D;
         this.motX += this.a * var3;
         this.motZ += this.b * var3;
      } else {
         this.motX *= 0.9800000190734863D;
         this.motY *= 0.0D;
         this.motZ *= 0.9800000190734863D;
      }

      super.o();
   }

   public boolean e(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      if(var2 != null && var2.getItem() == Items.COAL) {
         if(!var1.abilities.canInstantlyBuild && --var2.count == 0) {
            var1.inventory.setItem(var1.inventory.itemInHandIndex, (ItemStack)null);
         }

         this.c += 3600;
      }

      this.a = this.locX - var1.locX;
      this.b = this.locZ - var1.locZ;
      return true;
   }

   protected void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setDouble("PushX", this.a);
      var1.setDouble("PushZ", this.b);
      var1.setShort("Fuel", (short)this.c);
   }

   protected void a(NBTTagCompound var1) {
      super.a(var1);
      this.a = var1.getDouble("PushX");
      this.b = var1.getDouble("PushZ");
      this.c = var1.getShort("Fuel");
   }

   protected boolean j() {
      return (this.datawatcher.getByte(16) & 1) != 0;
   }

   protected void i(boolean var1) {
      if(var1) {
         this.datawatcher.watch(16, Byte.valueOf((byte)(this.datawatcher.getByte(16) | 1)));
      } else {
         this.datawatcher.watch(16, Byte.valueOf((byte)(this.datawatcher.getByte(16) & -2)));
      }

   }

   public IBlockData u() {
      return (this.j()?Blocks.LIT_FURNACE:Blocks.FURNACE).getBlockData().set(BlockFurnace.FACING, EnumDirection.NORTH);
   }
}
