package net.minecraft.server;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.IMerchant;
import net.minecraft.server.InventoryMerchant;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MerchantRecipe;
import net.minecraft.server.Slot;
import net.minecraft.server.StatisticList;

public class SlotMerchantResult extends Slot {
   private final InventoryMerchant a;
   private EntityHuman b;
   private int c;
   private final IMerchant h;

   public SlotMerchantResult(EntityHuman var1, IMerchant var2, InventoryMerchant var3, int var4, int var5, int var6) {
      super(var3, var4, var5, var6);
      this.b = var1;
      this.h = var2;
      this.a = var3;
   }

   public boolean isAllowed(ItemStack var1) {
      return false;
   }

   public ItemStack a(int var1) {
      if(this.hasItem()) {
         this.c += Math.min(var1, this.getItem().count);
      }

      return super.a(var1);
   }

   protected void a(ItemStack var1, int var2) {
      this.c += var2;
      this.c(var1);
   }

   protected void c(ItemStack var1) {
      var1.a(this.b.world, this.b, this.c);
      this.c = 0;
   }

   public void a(EntityHuman var1, ItemStack var2) {
      this.c(var2);
      MerchantRecipe var3 = this.a.getRecipe();
      if(var3 != null) {
         ItemStack var4 = this.a.getItem(0);
         ItemStack var5 = this.a.getItem(1);
         if(this.a(var3, var4, var5) || this.a(var3, var5, var4)) {
            this.h.a(var3);
            var1.b(StatisticList.G);
            if(var4 != null && var4.count <= 0) {
               var4 = null;
            }

            if(var5 != null && var5.count <= 0) {
               var5 = null;
            }

            this.a.setItem(0, var4);
            this.a.setItem(1, var5);
         }
      }

   }

   private boolean a(MerchantRecipe var1, ItemStack var2, ItemStack var3) {
      ItemStack var4 = var1.getBuyItem1();
      ItemStack var5 = var1.getBuyItem2();
      if(var2 != null && var2.getItem() == var4.getItem()) {
         if(var5 != null && var3 != null && var5.getItem() == var3.getItem()) {
            var2.count -= var4.count;
            var3.count -= var5.count;
            return true;
         }

         if(var5 == null && var3 == null) {
            var2.count -= var4.count;
            return true;
         }
      }

      return false;
   }
}
