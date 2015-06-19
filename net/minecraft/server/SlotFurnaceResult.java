package net.minecraft.server;

import net.minecraft.server.AchievementList;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.RecipesFurnace;
import net.minecraft.server.Slot;
import net.minecraft.server.Statistic;

public class SlotFurnaceResult extends Slot {
   private EntityHuman a;
   private int b;

   public SlotFurnaceResult(EntityHuman var1, IInventory var2, int var3, int var4, int var5) {
      super(var2, var3, var4, var5);
      this.a = var1;
   }

   public boolean isAllowed(ItemStack var1) {
      return false;
   }

   public ItemStack a(int var1) {
      if(this.hasItem()) {
         this.b += Math.min(var1, this.getItem().count);
      }

      return super.a(var1);
   }

   public void a(EntityHuman var1, ItemStack var2) {
      this.c(var2);
      super.a(var1, var2);
   }

   protected void a(ItemStack var1, int var2) {
      this.b += var2;
      this.c(var1);
   }

   protected void c(ItemStack var1) {
      var1.a(this.a.world, this.a, this.b);
      if(!this.a.world.isClientSide) {
         int var2 = this.b;
         float var3 = RecipesFurnace.getInstance().b(var1);
         int var4;
         if(var3 == 0.0F) {
            var2 = 0;
         } else if(var3 < 1.0F) {
            var4 = MathHelper.d((float)var2 * var3);
            if(var4 < MathHelper.f((float)var2 * var3) && Math.random() < (double)((float)var2 * var3 - (float)var4)) {
               ++var4;
            }

            var2 = var4;
         }

         while(var2 > 0) {
            var4 = EntityExperienceOrb.getOrbValue(var2);
            var2 -= var4;
            this.a.world.addEntity(new EntityExperienceOrb(this.a.world, this.a.locX, this.a.locY + 0.5D, this.a.locZ + 0.5D, var4));
         }
      }

      this.b = 0;
      if(var1.getItem() == Items.IRON_INGOT) {
         this.a.b((Statistic)AchievementList.k);
      }

      if(var1.getItem() == Items.COOKED_FISH) {
         this.a.b((Statistic)AchievementList.p);
      }

   }
}
