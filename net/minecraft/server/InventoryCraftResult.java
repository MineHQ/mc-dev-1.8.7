package net.minecraft.server;

import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;

public class InventoryCraftResult implements IInventory {
   private ItemStack[] items = new ItemStack[1];

   public InventoryCraftResult() {
   }

   public int getSize() {
      return 1;
   }

   public ItemStack getItem(int var1) {
      return this.items[0];
   }

   public String getName() {
      return "Result";
   }

   public boolean hasCustomName() {
      return false;
   }

   public IChatBaseComponent getScoreboardDisplayName() {
      return (IChatBaseComponent)(this.hasCustomName()?new ChatComponentText(this.getName()):new ChatMessage(this.getName(), new Object[0]));
   }

   public ItemStack splitStack(int var1, int var2) {
      if(this.items[0] != null) {
         ItemStack var3 = this.items[0];
         this.items[0] = null;
         return var3;
      } else {
         return null;
      }
   }

   public ItemStack splitWithoutUpdate(int var1) {
      if(this.items[0] != null) {
         ItemStack var2 = this.items[0];
         this.items[0] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void setItem(int var1, ItemStack var2) {
      this.items[0] = var2;
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
}
