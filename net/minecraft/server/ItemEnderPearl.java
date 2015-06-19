package net.minecraft.server;

import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityEnderPearl;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class ItemEnderPearl extends Item {
   public ItemEnderPearl() {
      this.maxStackSize = 16;
      this.a(CreativeModeTab.f);
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      if(var3.abilities.canInstantlyBuild) {
         return var1;
      } else {
         --var1.count;
         var2.makeSound(var3, "random.bow", 0.5F, 0.4F / (g.nextFloat() * 0.4F + 0.8F));
         if(!var2.isClientSide) {
            var2.addEntity(new EntityEnderPearl(var2, var3));
         }

         var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
         return var1;
      }
   }
}
