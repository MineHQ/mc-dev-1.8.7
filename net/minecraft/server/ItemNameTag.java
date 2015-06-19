package net.minecraft.server;

import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;

public class ItemNameTag extends Item {
   public ItemNameTag() {
      this.a(CreativeModeTab.i);
   }

   public boolean a(ItemStack var1, EntityHuman var2, EntityLiving var3) {
      if(!var1.hasName()) {
         return false;
      } else if(var3 instanceof EntityInsentient) {
         EntityInsentient var4 = (EntityInsentient)var3;
         var4.setCustomName(var1.getName());
         var4.bX();
         --var1.count;
         return true;
      } else {
         return super.a(var1, var2, var3);
      }
   }
}
