package net.minecraft.server;

import net.minecraft.server.Container;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ICrafting;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Slot;

public class ContainerBeacon extends Container {
   private IInventory a;
   private final ContainerBeacon.SlotBeacon f;

   public ContainerBeacon(IInventory var1, IInventory var2) {
      this.a = var2;
      this.a(this.f = new ContainerBeacon.SlotBeacon(var2, 0, 136, 110));
      byte var3 = 36;
      short var4 = 137;

      int var5;
      for(var5 = 0; var5 < 3; ++var5) {
         for(int var6 = 0; var6 < 9; ++var6) {
            this.a(new Slot(var1, var6 + var5 * 9 + 9, var3 + var6 * 18, var4 + var5 * 18));
         }
      }

      for(var5 = 0; var5 < 9; ++var5) {
         this.a(new Slot(var1, var5, var3 + var5 * 18, 58 + var4));
      }

   }

   public void addSlotListener(ICrafting var1) {
      super.addSlotListener(var1);
      var1.setContainerData(this, this.a);
   }

   public IInventory e() {
      return this.a;
   }

   public void b(EntityHuman var1) {
      super.b(var1);
      if(var1 != null && !var1.world.isClientSide) {
         ItemStack var2 = this.f.a(this.f.getMaxStackSize());
         if(var2 != null) {
            var1.drop(var2, false);
         }

      }
   }

   public boolean a(EntityHuman var1) {
      return this.a.a(var1);
   }

   public ItemStack b(EntityHuman var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.c.get(var2);
      if(var4 != null && var4.hasItem()) {
         ItemStack var5 = var4.getItem();
         var3 = var5.cloneItemStack();
         if(var2 == 0) {
            if(!this.a(var5, 1, 37, true)) {
               return null;
            }

            var4.a(var5, var3);
         } else if(!this.f.hasItem() && this.f.isAllowed(var5) && var5.count == 1) {
            if(!this.a(var5, 0, 1, false)) {
               return null;
            }
         } else if(var2 >= 1 && var2 < 28) {
            if(!this.a(var5, 28, 37, false)) {
               return null;
            }
         } else if(var2 >= 28 && var2 < 37) {
            if(!this.a(var5, 1, 28, false)) {
               return null;
            }
         } else if(!this.a(var5, 1, 37, false)) {
            return null;
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

   class SlotBeacon extends Slot {
      public SlotBeacon(IInventory var2, int var3, int var4, int var5) {
         super(var2, var3, var4, var5);
      }

      public boolean isAllowed(ItemStack var1) {
         return var1 == null?false:var1.getItem() == Items.EMERALD || var1.getItem() == Items.DIAMOND || var1.getItem() == Items.GOLD_INGOT || var1.getItem() == Items.IRON_INGOT;
      }

      public int getMaxStackSize() {
         return 1;
      }
   }
}
