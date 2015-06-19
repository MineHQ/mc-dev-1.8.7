package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IInventory;
import net.minecraft.server.IInventoryListener;
import net.minecraft.server.ItemStack;

public class InventorySubcontainer implements IInventory {
   private String a;
   private int b;
   public ItemStack[] items;
   private List<IInventoryListener> d;
   private boolean e;

   public InventorySubcontainer(String var1, boolean var2, int var3) {
      this.a = var1;
      this.e = var2;
      this.b = var3;
      this.items = new ItemStack[var3];
   }

   public void a(IInventoryListener var1) {
      if(this.d == null) {
         this.d = Lists.newArrayList();
      }

      this.d.add(var1);
   }

   public void b(IInventoryListener var1) {
      this.d.remove(var1);
   }

   public ItemStack getItem(int var1) {
      return var1 >= 0 && var1 < this.items.length?this.items[var1]:null;
   }

   public ItemStack splitStack(int var1, int var2) {
      if(this.items[var1] != null) {
         ItemStack var3;
         if(this.items[var1].count <= var2) {
            var3 = this.items[var1];
            this.items[var1] = null;
            this.update();
            return var3;
         } else {
            var3 = this.items[var1].a(var2);
            if(this.items[var1].count == 0) {
               this.items[var1] = null;
            }

            this.update();
            return var3;
         }
      } else {
         return null;
      }
   }

   public ItemStack a(ItemStack var1) {
      ItemStack var2 = var1.cloneItemStack();

      for(int var3 = 0; var3 < this.b; ++var3) {
         ItemStack var4 = this.getItem(var3);
         if(var4 == null) {
            this.setItem(var3, var2);
            this.update();
            return null;
         }

         if(ItemStack.c(var4, var2)) {
            int var5 = Math.min(this.getMaxStackSize(), var4.getMaxStackSize());
            int var6 = Math.min(var2.count, var5 - var4.count);
            if(var6 > 0) {
               var4.count += var6;
               var2.count -= var6;
               if(var2.count <= 0) {
                  this.update();
                  return null;
               }
            }
         }
      }

      if(var2.count != var1.count) {
         this.update();
      }

      return var2;
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

   public void setItem(int var1, ItemStack var2) {
      this.items[var1] = var2;
      if(var2 != null && var2.count > this.getMaxStackSize()) {
         var2.count = this.getMaxStackSize();
      }

      this.update();
   }

   public int getSize() {
      return this.b;
   }

   public String getName() {
      return this.a;
   }

   public boolean hasCustomName() {
      return this.e;
   }

   public void a(String var1) {
      this.e = true;
      this.a = var1;
   }

   public IChatBaseComponent getScoreboardDisplayName() {
      return (IChatBaseComponent)(this.hasCustomName()?new ChatComponentText(this.getName()):new ChatMessage(this.getName(), new Object[0]));
   }

   public int getMaxStackSize() {
      return 64;
   }

   public void update() {
      if(this.d != null) {
         for(int var1 = 0; var1 < this.d.size(); ++var1) {
            ((IInventoryListener)this.d.get(var1)).a(this);
         }
      }

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
