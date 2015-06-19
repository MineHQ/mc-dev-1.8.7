package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityAgeable;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PathfinderGoalBreed;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalFollowParent;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalPanic;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalTempt;
import net.minecraft.server.World;

public class EntityChicken extends EntityAnimal {
   public float bm;
   public float bo;
   public float bp;
   public float bq;
   public float br = 1.0F;
   public int bs;
   public boolean bt;

   public EntityChicken(World var1) {
      super(var1);
      this.setSize(0.4F, 0.7F);
      this.bs = this.random.nextInt(6000) + 6000;
      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.4D));
      this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
      this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.0D, Items.WHEAT_SEEDS, false));
      this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.1D));
      this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
      this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
   }

   public float getHeadHeight() {
      return this.length;
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(4.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
   }

   public void m() {
      super.m();
      this.bq = this.bm;
      this.bp = this.bo;
      this.bo = (float)((double)this.bo + (double)(this.onGround?-1:4) * 0.3D);
      this.bo = MathHelper.a(this.bo, 0.0F, 1.0F);
      if(!this.onGround && this.br < 1.0F) {
         this.br = 1.0F;
      }

      this.br = (float)((double)this.br * 0.9D);
      if(!this.onGround && this.motY < 0.0D) {
         this.motY *= 0.6D;
      }

      this.bm += this.br * 2.0F;
      if(!this.world.isClientSide && !this.isBaby() && !this.isChickenJockey() && --this.bs <= 0) {
         this.makeSound("mob.chicken.plop", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
         this.a(Items.EGG, 1);
         this.bs = this.random.nextInt(6000) + 6000;
      }

   }

   public void e(float var1, float var2) {
   }

   protected String z() {
      return "mob.chicken.say";
   }

   protected String bo() {
      return "mob.chicken.hurt";
   }

   protected String bp() {
      return "mob.chicken.hurt";
   }

   protected void a(BlockPosition var1, Block var2) {
      this.makeSound("mob.chicken.step", 0.15F, 1.0F);
   }

   protected Item getLoot() {
      return Items.FEATHER;
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      int var3 = this.random.nextInt(3) + this.random.nextInt(1 + var2);

      for(int var4 = 0; var4 < var3; ++var4) {
         this.a(Items.FEATHER, 1);
      }

      if(this.isBurning()) {
         this.a(Items.COOKED_CHICKEN, 1);
      } else {
         this.a(Items.CHICKEN, 1);
      }

   }

   public EntityChicken b(EntityAgeable var1) {
      return new EntityChicken(this.world);
   }

   public boolean d(ItemStack var1) {
      return var1 != null && var1.getItem() == Items.WHEAT_SEEDS;
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.bt = var1.getBoolean("IsChickenJockey");
      if(var1.hasKey("EggLayTime")) {
         this.bs = var1.getInt("EggLayTime");
      }

   }

   protected int getExpValue(EntityHuman var1) {
      return this.isChickenJockey()?10:super.getExpValue(var1);
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setBoolean("IsChickenJockey", this.bt);
      var1.setInt("EggLayTime", this.bs);
   }

   protected boolean isTypeNotPersistent() {
      return this.isChickenJockey() && this.passenger == null;
   }

   public void al() {
      super.al();
      float var1 = MathHelper.sin(this.aI * 3.1415927F / 180.0F);
      float var2 = MathHelper.cos(this.aI * 3.1415927F / 180.0F);
      float var3 = 0.1F;
      float var4 = 0.0F;
      this.passenger.setPosition(this.locX + (double)(var3 * var1), this.locY + (double)(this.length * 0.5F) + this.passenger.am() + (double)var4, this.locZ - (double)(var3 * var2));
      if(this.passenger instanceof EntityLiving) {
         ((EntityLiving)this.passenger).aI = this.aI;
      }

   }

   public boolean isChickenJockey() {
      return this.bt;
   }

   public void l(boolean var1) {
      this.bt = var1;
   }

   // $FF: synthetic method
   public EntityAgeable createChild(EntityAgeable var1) {
      return this.b(var1);
   }
}
