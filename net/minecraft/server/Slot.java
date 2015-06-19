package net.minecraft.server;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;

public class Slot {
   public final int index;
   public final IInventory inventory;
   public int rawSlotIndex;
   public int f;
   public int g;

   public Slot(IInventory var1, int var2, int var3, int var4) {
      this.inventory = var1;
      this.index = var2;
      this.f = var3;
      this.g = var4;
   }

   public void a(ItemStack var1, ItemStack var2) {
      if(var1 != null && var2 != null) {
         if(var1.getItem() == var2.getItem()) {
            int var3 = var2.count - var1.count;
            if(var3 > 0) {
               this.a(var1, var3);
            }

         }
      }
   }

   protected void a(ItemStack var1, int var2) {
   }

   protected void c(ItemStack var1) {
   }

   public void a(EntityHuman var1, ItemStack var2) {
      this.f();
   }

   public boolean isAllowed(ItemStack var1) {
      return true;
   }

   public ItemStack getItem() {
      return this.inventory.getItem(this.index);
   }

   public boolean hasItem() {
      return this.getItem() != null;
   }

   public void set(ItemStack var1) {
      this.inventory.setItem(this.index, var1);
      this.f();
   }

   public void f() {
      this.inventory.update();
   }

   public int getMaxStackSize() {
      return this.inventory.getMaxStackSize();
   }

   public int getMaxStackSize(ItemStack var1) {
      return this.getMaxStackSize();
   }

   public ItemStack a(int var1) {
      return this.inventory.splitStack(this.index, var1);
   }

   public boolean a(IInventory var1, int var2) {
      return var1 == this.inventory && var2 == this.index;
   }

   public boolean isAllowed(EntityHuman var1) {
      return true;
   }
}
