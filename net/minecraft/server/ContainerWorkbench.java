package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Container;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryCraftResult;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.ItemStack;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.Slot;
import net.minecraft.server.SlotResult;
import net.minecraft.server.World;

public class ContainerWorkbench extends Container {
   public InventoryCrafting craftInventory = new InventoryCrafting(this, 3, 3);
   public IInventory resultInventory = new InventoryCraftResult();
   private World g;
   private BlockPosition h;

   public ContainerWorkbench(PlayerInventory var1, World var2, BlockPosition var3) {
      this.g = var2;
      this.h = var3;
      this.a((Slot)(new SlotResult(var1.player, this.craftInventory, this.resultInventory, 0, 124, 35)));

      int var4;
      int var5;
      for(var4 = 0; var4 < 3; ++var4) {
         for(var5 = 0; var5 < 3; ++var5) {
            this.a((Slot)(new Slot(this.craftInventory, var5 + var4 * 3, 30 + var5 * 18, 17 + var4 * 18)));
         }
      }

      for(var4 = 0; var4 < 3; ++var4) {
         for(var5 = 0; var5 < 9; ++var5) {
            this.a((Slot)(new Slot(var1, var5 + var4 * 9 + 9, 8 + var5 * 18, 84 + var4 * 18)));
         }
      }

      for(var4 = 0; var4 < 9; ++var4) {
         this.a((Slot)(new Slot(var1, var4, 8 + var4 * 18, 142)));
      }

      this.a((IInventory)this.craftInventory);
   }

   public void a(IInventory var1) {
      this.resultInventory.setItem(0, CraftingManager.getInstance().craft(this.craftInventory, this.g));
   }

   public void b(EntityHuman var1) {
      super.b(var1);
      if(!this.g.isClientSide) {
         for(int var2 = 0; var2 < 9; ++var2) {
            ItemStack var3 = this.craftInventory.splitWithoutUpdate(var2);
            if(var3 != null) {
               var1.drop(var3, false);
            }
         }

      }
   }

   public boolean a(EntityHuman var1) {
      return this.g.getType(this.h).getBlock() != Blocks.CRAFTING_TABLE?false:var1.e((double)this.h.getX() + 0.5D, (double)this.h.getY() + 0.5D, (double)this.h.getZ() + 0.5D) <= 64.0D;
   }

   public ItemStack b(EntityHuman var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.c.get(var2);
      if(var4 != null && var4.hasItem()) {
         ItemStack var5 = var4.getItem();
         var3 = var5.cloneItemStack();
         if(var2 == 0) {
            if(!this.a(var5, 10, 46, true)) {
               return null;
            }

            var4.a(var5, var3);
         } else if(var2 >= 10 && var2 < 37) {
            if(!this.a(var5, 37, 46, false)) {
               return null;
            }
         } else if(var2 >= 37 && var2 < 46) {
            if(!this.a(var5, 10, 37, false)) {
               return null;
            }
         } else if(!this.a(var5, 10, 46, false)) {
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
