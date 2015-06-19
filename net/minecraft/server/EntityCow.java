package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityAgeable;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoalBreed;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalFollowParent;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalPanic;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalTempt;
import net.minecraft.server.World;

public class EntityCow extends EntityAnimal {
   public EntityCow(World var1) {
      super(var1);
      this.setSize(0.9F, 1.3F);
      ((Navigation)this.getNavigation()).a(true);
      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(1, new PathfinderGoalPanic(this, 2.0D));
      this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
      this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.25D, Items.WHEAT, false));
      this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.25D));
      this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
      this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(10.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.20000000298023224D);
   }

   protected String z() {
      return "mob.cow.say";
   }

   protected String bo() {
      return "mob.cow.hurt";
   }

   protected String bp() {
      return "mob.cow.hurt";
   }

   protected void a(BlockPosition var1, Block var2) {
      this.makeSound("mob.cow.step", 0.15F, 1.0F);
   }

   protected float bB() {
      return 0.4F;
   }

   protected Item getLoot() {
      return Items.LEATHER;
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      int var3 = this.random.nextInt(3) + this.random.nextInt(1 + var2);

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         this.a(Items.LEATHER, 1);
      }

      var3 = this.random.nextInt(3) + 1 + this.random.nextInt(1 + var2);

      for(var4 = 0; var4 < var3; ++var4) {
         if(this.isBurning()) {
            this.a(Items.COOKED_BEEF, 1);
         } else {
            this.a(Items.BEEF, 1);
         }
      }

   }

   public boolean a(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      if(var2 != null && var2.getItem() == Items.BUCKET && !var1.abilities.canInstantlyBuild && !this.isBaby()) {
         if(var2.count-- == 1) {
            var1.inventory.setItem(var1.inventory.itemInHandIndex, new ItemStack(Items.MILK_BUCKET));
         } else if(!var1.inventory.pickup(new ItemStack(Items.MILK_BUCKET))) {
            var1.drop(new ItemStack(Items.MILK_BUCKET, 1, 0), false);
         }

         return true;
      } else {
         return super.a(var1);
      }
   }

   public EntityCow b(EntityAgeable var1) {
      return new EntityCow(this.world);
   }

   public float getHeadHeight() {
      return this.length;
   }

   // $FF: synthetic method
   public EntityAgeable createChild(EntityAgeable var1) {
      return this.b(var1);
   }
}
