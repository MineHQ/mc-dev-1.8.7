package net.minecraft.server;

import net.minecraft.server.AchievementList;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityAgeable;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLightning;
import net.minecraft.server.EntityPigZombie;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoalBreed;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalFollowParent;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalPanic;
import net.minecraft.server.PathfinderGoalPassengerCarrotStick;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalTempt;
import net.minecraft.server.Statistic;
import net.minecraft.server.World;

public class EntityPig extends EntityAnimal {
   private final PathfinderGoalPassengerCarrotStick bm;

   public EntityPig(World var1) {
      super(var1);
      this.setSize(0.9F, 0.9F);
      ((Navigation)this.getNavigation()).a(true);
      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.25D));
      this.goalSelector.a(2, this.bm = new PathfinderGoalPassengerCarrotStick(this, 0.3F));
      this.goalSelector.a(3, new PathfinderGoalBreed(this, 1.0D));
      this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.2D, Items.CARROT_ON_A_STICK, false));
      this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.2D, Items.CARROT, false));
      this.goalSelector.a(5, new PathfinderGoalFollowParent(this, 1.1D));
      this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
      this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(10.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
   }

   public boolean bW() {
      ItemStack var1 = ((EntityHuman)this.passenger).bA();
      return var1 != null && var1.getItem() == Items.CARROT_ON_A_STICK;
   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, Byte.valueOf((byte)0));
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setBoolean("Saddle", this.hasSaddle());
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.setSaddle(var1.getBoolean("Saddle"));
   }

   protected String z() {
      return "mob.pig.say";
   }

   protected String bo() {
      return "mob.pig.say";
   }

   protected String bp() {
      return "mob.pig.death";
   }

   protected void a(BlockPosition var1, Block var2) {
      this.makeSound("mob.pig.step", 0.15F, 1.0F);
   }

   public boolean a(EntityHuman var1) {
      if(super.a(var1)) {
         return true;
      } else if(!this.hasSaddle() || this.world.isClientSide || this.passenger != null && this.passenger != var1) {
         return false;
      } else {
         var1.mount(this);
         return true;
      }
   }

   protected Item getLoot() {
      return this.isBurning()?Items.COOKED_PORKCHOP:Items.PORKCHOP;
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      int var3 = this.random.nextInt(3) + 1 + this.random.nextInt(1 + var2);

      for(int var4 = 0; var4 < var3; ++var4) {
         if(this.isBurning()) {
            this.a(Items.COOKED_PORKCHOP, 1);
         } else {
            this.a(Items.PORKCHOP, 1);
         }
      }

      if(this.hasSaddle()) {
         this.a(Items.SADDLE, 1);
      }

   }

   public boolean hasSaddle() {
      return (this.datawatcher.getByte(16) & 1) != 0;
   }

   public void setSaddle(boolean var1) {
      if(var1) {
         this.datawatcher.watch(16, Byte.valueOf((byte)1));
      } else {
         this.datawatcher.watch(16, Byte.valueOf((byte)0));
      }

   }

   public void onLightningStrike(EntityLightning var1) {
      if(!this.world.isClientSide && !this.dead) {
         EntityPigZombie var2 = new EntityPigZombie(this.world);
         var2.setEquipment(0, new ItemStack(Items.GOLDEN_SWORD));
         var2.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
         var2.k(this.ce());
         if(this.hasCustomName()) {
            var2.setCustomName(this.getCustomName());
            var2.setCustomNameVisible(this.getCustomNameVisible());
         }

         this.world.addEntity(var2);
         this.die();
      }
   }

   public void e(float var1, float var2) {
      super.e(var1, var2);
      if(var1 > 5.0F && this.passenger instanceof EntityHuman) {
         ((EntityHuman)this.passenger).b((Statistic)AchievementList.u);
      }

   }

   public EntityPig b(EntityAgeable var1) {
      return new EntityPig(this.world);
   }

   public boolean d(ItemStack var1) {
      return var1 != null && var1.getItem() == Items.CARROT;
   }

   public PathfinderGoalPassengerCarrotStick cm() {
      return this.bm;
   }

   // $FF: synthetic method
   public EntityAgeable createChild(EntityAgeable var1) {
      return this.b(var1);
   }
}
