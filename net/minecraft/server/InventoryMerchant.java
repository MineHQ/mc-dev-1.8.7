package net.minecraft.server;

import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IInventory;
import net.minecraft.server.IMerchant;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MerchantRecipe;
import net.minecraft.server.MerchantRecipeList;

public class InventoryMerchant implements IInventory {
   private final IMerchant merchant;
   private ItemStack[] itemsInSlots = new ItemStack[3];
   private final EntityHuman player;
   private MerchantRecipe recipe;
   private int e;

   public InventoryMerchant(EntityHuman var1, IMerchant var2) {
      this.player = var1;
      this.merchant = var2;
   }

   public int getSize() {
      return this.itemsInSlots.length;
   }

   public ItemStack getItem(int var1) {
      return this.itemsInSlots[var1];
   }

   public ItemStack splitStack(int var1, int var2) {
      if(this.itemsInSlots[var1] != null) {
         ItemStack var3;
         if(var1 == 2) {
            var3 = this.itemsInSlots[var1];
            this.itemsInSlots[var1] = null;
            return var3;
         } else if(this.itemsInSlots[var1].count <= var2) {
            var3 = this.itemsInSlots[var1];
            this.itemsInSlots[var1] = null;
            if(this.e(var1)) {
               this.h();
            }

            return var3;
         } else {
            var3 = this.itemsInSlots[var1].a(var2);
            if(this.itemsInSlots[var1].count == 0) {
               this.itemsInSlots[var1] = null;
            }

            if(this.e(var1)) {
               this.h();
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   private boolean e(int var1) {
      return var1 == 0 || var1 == 1;
   }

   public ItemStack splitWithoutUpdate(int var1) {
      if(this.itemsInSlots[var1] != null) {
         ItemStack var2 = this.itemsInSlots[var1];
         this.itemsInSlots[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void setItem(int var1, ItemStack var2) {
      this.itemsInSlots[var1] = var2;
      if(var2 != null && var2.count > this.getMaxStackSize()) {
         var2.count = this.getMaxStackSize();
      }

      if(this.e(var1)) {
         this.h();
      }

   }

   public String getName() {
      return "mob.villager";
   }

   public boolean hasCustomName() {
      return false;
   }

   public IChatBaseComponent getScoreboardDisplayName() {
      return (IChatBaseComponent)(this.hasCustomName()?new ChatComponentText(this.getName()):new ChatMessage(this.getName(), new Object[0]));
   }

   public int getMaxStackSize() {
      return 64;
   }

   public boolean a(EntityHuman var1) {
      return this.merchant.v_() == var1;
   }

   public void startOpen(EntityHuman var1) {
   }

   public void closeContainer(EntityHuman var1) {
   }

   public boolean b(int var1, ItemStack var2) {
      return true;
   }

   public void update() {
      this.h();
   }

   public void h() {
      this.recipe = null;
      ItemStack var1 = this.itemsInSlots[0];
      ItemStack var2 = this.itemsInSlots[1];
      if(var1 == null) {
         var1 = var2;
         var2 = null;
      }

      if(var1 == null) {
         this.setItem(2, (ItemStack)null);
      } else {
         MerchantRecipeList var3 = this.merchant.getOffers(this.player);
         if(var3 != null) {
            MerchantRecipe var4 = var3.a(var1, var2, this.e);
            if(var4 != null && !var4.h()) {
               this.recipe = var4;
               this.setItem(2, var4.getBuyItem3().cloneItemStack());
            } else if(var2 != null) {
               var4 = var3.a(var2, var1, this.e);
               if(var4 != null && !var4.h()) {
                  this.recipe = var4;
                  this.setItem(2, var4.getBuyItem3().cloneItemStack());
               } else {
                  this.setItem(2, (ItemStack)null);
               }
            } else {
               this.setItem(2, (ItemStack)null);
            }
         }
      }

      this.merchant.a_(this.getItem(2));
   }

   public MerchantRecipe getRecipe() {
      return this.recipe;
   }

   public void d(int var1) {
      this.e = var1;
      this.h();
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
      for(int var1 = 0; var1 < this.itemsInSlots.length; ++var1) {
         this.itemsInSlots[var1] = null;
      }

   }
}
