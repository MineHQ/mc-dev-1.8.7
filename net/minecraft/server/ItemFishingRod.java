package net.minecraft.server;

import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityFishingHook;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class ItemFishingRod extends Item {
   public ItemFishingRod() {
      this.setMaxDurability(64);
      this.c(1);
      this.a(CreativeModeTab.i);
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      if(var3.hookedFish != null) {
         int var4 = var3.hookedFish.l();
         var1.damage(var4, var3);
         var3.bw();
      } else {
         var2.makeSound(var3, "random.bow", 0.5F, 0.4F / (g.nextFloat() * 0.4F + 0.8F));
         if(!var2.isClientSide) {
            var2.addEntity(new EntityFishingHook(var2, var3));
         }

         var3.bw();
         var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
      }

      return var1;
   }

   public boolean f_(ItemStack var1) {
      return super.f_(var1);
   }

   public int b() {
      return 1;
   }
}
