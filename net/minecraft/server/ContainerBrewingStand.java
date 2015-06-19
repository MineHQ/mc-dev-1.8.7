package net.minecraft.server;

import net.minecraft.server.AchievementList;
import net.minecraft.server.Container;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ICrafting;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.Slot;
import net.minecraft.server.Statistic;

public class ContainerBrewingStand extends Container {
   private IInventory brewingStand;
   private final Slot f;
   private int g;

   public ContainerBrewingStand(PlayerInventory var1, IInventory var2) {
      this.brewingStand = var2;
      this.a(new ContainerBrewingStand.SlotPotionBottle(var1.player, var2, 0, 56, 46));
      this.a(new ContainerBrewingStand.SlotPotionBottle(var1.player, var2, 1, 79, 53));
      this.a(new ContainerBrewingStand.SlotPotionBottle(var1.player, var2, 2, 102, 46));
      this.f = this.a(new ContainerBrewingStand.SlotBrewing(var2, 3, 79, 17));

      int var3;
      for(var3 = 0; var3 < 3; ++var3) {
         for(int var4 = 0; var4 < 9; ++var4) {
            this.a(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
         }
      }

      for(var3 = 0; var3 < 9; ++var3) {
         this.a(new Slot(var1, var3, 8 + var3 * 18, 142));
      }

   }

   public void addSlotListener(ICrafting var1) {
      super.addSlotListener(var1);
      var1.setContainerData(this, this.brewingStand);
   }

   public void b() {
      super.b();

      for(int var1 = 0; var1 < this.listeners.size(); ++var1) {
         ICrafting var2 = (ICrafting)this.listeners.get(var1);
         if(this.g != this.brewingStand.getProperty(0)) {
            var2.setContainerData(this, 0, this.brewingStand.getProperty(0));
         }
      }

      this.g = this.brewingStand.getProperty(0);
   }

   public boolean a(EntityHuman var1) {
      return this.brewingStand.a(var1);
   }

   public ItemStack b(EntityHuman var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.c.get(var2);
      if(var4 != null && var4.hasItem()) {
         ItemStack var5 = var4.getItem();
         var3 = var5.cloneItemStack();
         if((var2 < 0 || var2 > 2) && var2 != 3) {
            if(!this.f.hasItem() && this.f.isAllowed(var5)) {
               if(!this.a(var5, 3, 4, false)) {
                  return null;
               }
            } else if(ContainerBrewingStand.SlotPotionBottle.b_(var3)) {
               if(!this.a(var5, 0, 3, false)) {
                  return null;
               }
            } else if(var2 >= 4 && var2 < 31) {
               if(!this.a(var5, 31, 40, false)) {
                  return null;
               }
            } else if(var2 >= 31 && var2 < 40) {
               if(!this.a(var5, 4, 31, false)) {
                  return null;
               }
            } else if(!this.a(var5, 4, 40, false)) {
               return null;
            }
         } else {
            if(!this.a(var5, 4, 40, true)) {
               return null;
            }

            var4.a(var5, var3);
         }

         if(var5.count == 0) {
            var4.set((ItemStack)null);
         } else {
            var4.f();
         }

         if(var5.count == var3.count) {
            return null;
         }

         var4.a(var1, var5);
      }

      return var3;
   }

   class SlotBrewing extends Slot {
      public SlotBrewing(IInventory var2, int var3, int var4, int var5) {
         super(var2, var3, var4, var5);
      }

      public boolean isAllowed(ItemStack var1) {
         return var1 != null?var1.getItem().l(var1):false;
      }

      public int getMaxStackSize() {
         return 64;
      }
   }

   static class SlotPotionBottle extends Slot {
      private EntityHuman a;

      public SlotPotionBottle(EntityHuman var1, IInventory var2, int var3, int var4, int var5) {
         super(var2, var3, var4, var5);
         this.a = var1;
      }

      public boolean isAllowed(ItemStack var1) {
         return b_(var1);
      }

      public int getMaxStackSize() {
         return 1;
      }

      public void a(EntityHuman var1, ItemStack var2) {
         if(var2.getItem() == Items.POTION && var2.getData() > 0) {
            this.a.b((Statistic)AchievementList.B);
         }

         super.a(var1, var2);
      }

      public static boolean b_(ItemStack var0) {
         return var0 != null && (var0.getItem() == Items.POTION || var0.getItem() == Items.GLASS_BOTTLE);
      }
   }
}
