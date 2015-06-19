package net.minecraft.server;

import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPig;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;

public class ItemSaddle extends Item {
   public ItemSaddle() {
      this.maxStackSize = 1;
      this.a(CreativeModeTab.e);
   }

   public boolean a(ItemStack var1, EntityHuman var2, EntityLiving var3) {
      if(var3 instanceof EntityPig) {
         EntityPig var4 = (EntityPig)var3;
         if(!var4.hasSaddle() && !var4.isBaby()) {
            var4.setSaddle(true);
            var4.world.makeSound(var4, "mob.horse.leather", 0.5F, 1.0F);
            --var1.count;
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean a(ItemStack var1, EntityLiving var2, EntityLiving var3) {
      this.a(var1, (EntityHuman)null, var2);
      return true;
   }
}
