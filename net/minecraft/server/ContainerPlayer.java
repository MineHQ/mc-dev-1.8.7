package net.minecraft.server;

import net.minecraft.server.Blocks;
import net.minecraft.server.Container;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryCraftResult;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.Slot;
import net.minecraft.server.SlotResult;

public class ContainerPlayer extends Container {
   public InventoryCrafting craftInventory = new InventoryCrafting(this, 2, 2);
   public IInventory resultInventory = new InventoryCraftResult();
   public boolean g;
   private final EntityHuman h;

   public ContainerPlayer(final PlayerInventory var1, boolean var2, EntityHuman var3) {
      this.g = var2;
      this.h = var3;
      this.a((Slot)(new SlotResult(var1.player, this.craftInventory, this.resultInventory, 0, 144, 36)));

      final int var4;
      int var5;
      for(var4 = 0; var4 < 2; ++var4) {
         for(var5 = 0; var5 < 2; ++var5) {
            this.a((Slot)(new Slot(this.craftInventory, var5 + var4 * 2, 88 + var5 * 18, 26 + var4 * 18)));
         }
      }

      for(var4 = 0; var4 < 4; ++var4) {
         this.a((Slot)(new Slot(var1, var1.getSize() - 1 - var4, 8, 8 + var4 * 18) {
            public int getMaxStackSize() {
               return 1;
            }

            public boolean isAllowed(ItemStack var1) {
               return var1 == null?false:(var1.getItem() instanceof ItemArmor?((ItemArmor)var1.getItem()).b == var4:(var1.getItem() != Item.getItemOf(Blocks.PUMPKIN) && var1.getItem() != Items.SKULL?false:var4 == 0));
            }
         }));
      }

      for(var4 = 0; var4 < 3; ++var4) {
         for(var5 = 0; var5 < 9; ++var5) {
            this.a((Slot)(new Slot(var1, var5 + (var4 + 1) * 9, 8 + var5 * 18, 84 + var4 * 18)));
         }
      }

      for(var4 = 0; var4 < 9; ++var4) {
         this.a((Slot)(new Slot(var1, var4, 8 + var4 * 18, 142)));
      }

      this.a((IInventory)this.craftInventory);
   }

   public void a(IInventory var1) {
      this.resultInventory.setItem(0, CraftingManager.getInstance().craft(this.craftInventory, this.h.world));
   }

   public void b(EntityHuman var1) {
      super.b(var1);

      for(int var2 = 0; var2 < 4; ++var2) {
         ItemStack var3 = this.craftInventory.splitWithoutUpdate(var2);
         if(var3 != null) {
            var1.drop(var3, false);
         }
      }

      this.resultInventory.setItem(0, (ItemStack)null);
   }

   public boolean a(EntityHuman var1) {
      return true;
   }

   public ItemStack b(EntityHuman var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.c.get(var2);
      if(var4 != null && var4.hasItem()) {
         ItemStack var5 = var4.getItem();
         var3 = var5.cloneItemStack();
         if(var2 == 0) {
            if(!this.a(var5, 9, 45, true)) {
               return null;
            }

            var4.a(var5, var3);
         } else if(var2 >= 1 && var2 < 5) {
            if(!this.a(var5, 9, 45, false)) {
               return null;
            }
         } else if(var2 >= 5 && var2 < 9) {
            if(!this.a(var5, 9, 45, false)) {
               return null;
            }
         } else if(var3.getItem() instanceof ItemArmor && !((Slot)this.c.get(5 + ((ItemArmor)var3.getItem()).b)).hasItem()) {
            int var6 = 5 + ((ItemArmor)var3.getItem()).b;
            if(!this.a(var5, var6, var6 + 1, false)) {
               return null;
            }
         } else if(var2 >= 9 && var2 < 36) {
            if(!this.a(var5, 36, 45, false)) {
               return null;
            }
         } else if(var2 >= 36 && var2 < 45) {
            if(!this.a(var5, 9, 36, false)) {
               return null;
            }
         } else if(!this.a(var5, 9, 45, false)) {
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

   public boolean a(ItemStack var1, Slot var2) {
      return var2.inventory != this.resultInventory && super.a(var1, var2);
   }
}
