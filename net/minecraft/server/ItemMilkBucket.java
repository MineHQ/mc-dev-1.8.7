package net.minecraft.server;

import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumAnimation;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class ItemMilkBucket extends Item {
   public ItemMilkBucket() {
      this.c(1);
      this.a(CreativeModeTab.f);
   }

   public ItemStack b(ItemStack var1, World var2, EntityHuman var3) {
      if(!var3.abilities.canInstantlyBuild) {
         --var1.count;
      }

      if(!var2.isClientSide) {
         var3.removeAllEffects();
      }

      var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
      return var1.count <= 0?new ItemStack(Items.BUCKET):var1;
   }

   public int d(ItemStack var1) {
      return 32;
   }

   public EnumAnimation e(ItemStack var1) {
      return EnumAnimation.DRINK;
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      var3.a(var1, this.d(var1));
      return var1;
   }
}
