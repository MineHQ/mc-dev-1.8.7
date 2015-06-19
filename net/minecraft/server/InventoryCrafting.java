package net.minecraft.server;

import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.Container;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;

public class InventoryCrafting implements IInventory {
   private final ItemStack[] items;
   private final int b;
   private final int c;
   private final Container d;

   public InventoryCrafting(Container var1, int var2, int var3) {
      int var4 = var2 * var3;
      this.items = new ItemStack[var4];
      this.d = var1;
      this.b = var2;
      this.c = var3;
   }

   public int getSize() {
      return this.items.length;
   }

   public ItemStack getItem(int var1) {
      return var1 >= this.getSize()?null:this.items[var1];
   }

   public ItemStack c(int var1, int var2) {
      return var1 >= 0 && var1 < this.b && var2 >= 0 && var2 <= this.c?this.getItem(var1 + var2 * this.b):null;
   }

   public String getName() {
      return "container.crafting";
   }

   public boolean hasCustomName() {
      return false;
   }

   public IChatBaseComponent getScoreboardDisplayName() {
      return (IChatBaseComponent)(this.hasCustomName()?new ChatComponentText(this.getName()):new ChatMessage(this.getName(), new Object[0]));
   }

   public ItemStack splitWithoutUpdate(int var1) {
      if(this.items[var1] != null) {
         ItemStack var2 = this.items[var1];
         this.items[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public ItemStack splitStack(int var1, int var2) {
      if(this.items[var1] != null) {
         ItemStack var3;
         if(this.items[var1].count <= var2) {
            var3 = this.items[var1];
            this.items[var1] = null;
            this.d.a((IInventory)this);
            return var3;
         } else {
            var3 = this.items[var1].a(var2);
            if(this.items[var1].count == 0) {
               this.items[var1] = null;
            }

            this.d.a((IInventory)this);
            return var3;
         }
      } else {
         return null;
      }
   }

   public void setItem(int var1, ItemStack var2) {
      this.items[var1] = var2;
      this.d.a((IInventory)this);
   }

   public int getMaxStackSize() {
      return 64;
   }

   public void update() {
   }

   public boolean a(EntityHuman var1) {
      return true;
   }

   public void startOpen(EntityHuman var1) {
   }

   public void closeContainer(EntityHuman var1) {
   }

   public boolean b(int var1, ItemStack var2) {
      return true;
   }

   public int getProperty(int var1) {
      return 0;
   }

   public void b(int var1, int var2) {
   }

   public int g() {
      return 0;
   }

   public void l() {
      for(int var1 = 0; var1 < this.items.length; ++var1) {
         this.items[var1] = null;
      }

   }

   public int h() {
      return this.c;
   }

   public int i() {
      return this.b;
   }
}
