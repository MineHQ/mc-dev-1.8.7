package net.minecraft.server;

import net.minecraft.server.Blocks;
import net.minecraft.server.EntityAgeable;
import net.minecraft.server.EntityCow;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.World;

public class EntityMushroomCow extends EntityCow {
   public EntityMushroomCow(World var1) {
      super(var1);
      this.setSize(0.9F, 1.3F);
      this.bn = Blocks.MYCELIUM;
   }

   public boolean a(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      if(var2 != null && var2.getItem() == Items.BOWL && this.getAge() >= 0) {
         if(var2.count == 1) {
            var1.inventory.setItem(var1.inventory.itemInHandIndex, new ItemStack(Items.MUSHROOM_STEW));
            return true;
         }

         if(var1.inventory.pickup(new ItemStack(Items.MUSHROOM_STEW)) && !var1.abilities.canInstantlyBuild) {
            var1.inventory.splitStack(var1.inventory.itemInHandIndex, 1);
            return true;
         }
      }

      if(var2 != null && var2.getItem() == Items.SHEARS && this.getAge() >= 0) {
         this.die();
         this.world.addParticle(EnumParticle.EXPLOSION_LARGE, this.locX, this.locY + (double)(this.length / 2.0F), this.locZ, 0.0D, 0.0D, 0.0D, new int[0]);
         if(!this.world.isClientSide) {
            EntityCow var3 = new EntityCow(this.world);
            var3.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            var3.setHealth(this.getHealth());
            var3.aI = this.aI;
            if(this.hasCustomName()) {
               var3.setCustomName(this.getCustomName());
            }

            this.world.addEntity(var3);

            for(int var4 = 0; var4 < 5; ++var4) {
               this.world.addEntity(new EntityItem(this.world, this.locX, this.locY + (double)this.length, this.locZ, new ItemStack(Blocks.RED_MUSHROOM)));
            }

            var2.damage(1, var1);
            this.makeSound("mob.sheep.shear", 1.0F, 1.0F);
         }

         return true;
      } else {
         return super.a(var1);
      }
   }

   public EntityMushroomCow c(EntityAgeable var1) {
      return new EntityMushroomCow(this.world);
   }

   // $FF: synthetic method
   public EntityCow b(EntityAgeable var1) {
      return this.c(var1);
   }

   // $FF: synthetic method
   public EntityAgeable createChild(EntityAgeable var1) {
      return this.c(var1);
   }
}
