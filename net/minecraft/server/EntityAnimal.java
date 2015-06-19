package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityAgeable;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.IAnimal;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public abstract class EntityAnimal extends EntityAgeable implements IAnimal {
   protected Block bn;
   private int bm;
   private EntityHuman bo;

   public EntityAnimal(World var1) {
      super(var1);
      this.bn = Blocks.GRASS;
   }

   protected void E() {
      if(this.getAge() != 0) {
         this.bm = 0;
      }

      super.E();
   }

   public void m() {
      super.m();
      if(this.getAge() != 0) {
         this.bm = 0;
      }

      if(this.bm > 0) {
         --this.bm;
         if(this.bm % 10 == 0) {
            double var1 = this.random.nextGaussian() * 0.02D;
            double var3 = this.random.nextGaussian() * 0.02D;
            double var5 = this.random.nextGaussian() * 0.02D;
            this.world.addParticle(EnumParticle.HEART, this.locX + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, this.locY + 0.5D + (double)(this.random.nextFloat() * this.length), this.locZ + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, var1, var3, var5, new int[0]);
         }
      }

   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else {
         this.bm = 0;
         return super.damageEntity(var1, var2);
      }
   }

   public float a(BlockPosition var1) {
      return this.world.getType(var1.down()).getBlock() == Blocks.GRASS?10.0F:this.world.o(var1) - 0.5F;
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("InLove", this.bm);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.bm = var1.getInt("InLove");
   }

   public boolean bR() {
      int var1 = MathHelper.floor(this.locX);
      int var2 = MathHelper.floor(this.getBoundingBox().b);
      int var3 = MathHelper.floor(this.locZ);
      BlockPosition var4 = new BlockPosition(var1, var2, var3);
      return this.world.getType(var4.down()).getBlock() == this.bn && this.world.k(var4) > 8 && super.bR();
   }

   public int w() {
      return 120;
   }

   protected boolean isTypeNotPersistent() {
      return false;
   }

   protected int getExpValue(EntityHuman var1) {
      return 1 + this.world.random.nextInt(3);
   }

   public boolean d(ItemStack var1) {
      return var1 == null?false:var1.getItem() == Items.WHEAT;
   }

   public boolean a(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      if(var2 != null) {
         if(this.d(var2) && this.getAge() == 0 && this.bm <= 0) {
            this.a(var1, var2);
            this.c(var1);
            return true;
         }

         if(this.isBaby() && this.d(var2)) {
            this.a(var1, var2);
            this.setAge((int)((float)(-this.getAge() / 20) * 0.1F), true);
            return true;
         }
      }

      return super.a(var1);
   }

   protected void a(EntityHuman var1, ItemStack var2) {
      if(!var1.abilities.canInstantlyBuild) {
         --var2.count;
         if(var2.count <= 0) {
            var1.inventory.setItem(var1.inventory.itemInHandIndex, (ItemStack)null);
         }
      }

   }

   public void c(EntityHuman var1) {
      this.bm = 600;
      this.bo = var1;
      this.world.broadcastEntityEffect(this, (byte)18);
   }

   public EntityHuman cq() {
      return this.bo;
   }

   public boolean isInLove() {
      return this.bm > 0;
   }

   public void cs() {
      this.bm = 0;
   }

   public boolean mate(EntityAnimal var1) {
      return var1 == this?false:(var1.getClass() != this.getClass()?false:this.isInLove() && var1.isInLove());
   }
}
