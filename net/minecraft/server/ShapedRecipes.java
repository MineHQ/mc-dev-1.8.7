package net.minecraft.server;

import net.minecraft.server.IRecipe;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class ShapedRecipes implements IRecipe {
   private final int width;
   private final int height;
   private final ItemStack[] items;
   private final ItemStack result;
   private boolean e;

   public ShapedRecipes(int var1, int var2, ItemStack[] var3, ItemStack var4) {
      this.width = var1;
      this.height = var2;
      this.items = var3;
      this.result = var4;
   }

   public ItemStack b() {
      return this.result;
   }

   public ItemStack[] b(InventoryCrafting var1) {
      ItemStack[] var2 = new ItemStack[var1.getSize()];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         ItemStack var4 = var1.getItem(var3);
         if(var4 != null && var4.getItem().r()) {
            var2[var3] = new ItemStack(var4.getItem().q());
         }
      }

      return var2;
   }

   public boolean a(InventoryCrafting var1, World var2) {
      for(int var3 = 0; var3 <= 3 - this.width; ++var3) {
         for(int var4 = 0; var4 <= 3 - this.height; ++var4) {
            if(this.a(var1, var3, var4, true)) {
               return true;
            }

            if(this.a(var1, var3, var4, false)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean a(InventoryCrafting var1, int var2, int var3, boolean var4) {
      for(int var5 = 0; var5 < 3; ++var5) {
         for(int var6 = 0; var6 < 3; ++var6) {
            int var7 = var5 - var2;
            int var8 = var6 - var3;
            ItemStack var9 = null;
            if(var7 >= 0 && var8 >= 0 && var7 < this.width && var8 < this.height) {
               if(var4) {
                  var9 = this.items[this.width - var7 - 1 + var8 * this.width];
               } else {
                  var9 = this.items[var7 + var8 * this.width];
               }
            }

            ItemStack var10 = var1.c(var5, var6);
            if(var10 != null || var9 != null) {
               if(var10 == null && var9 != null || var10 != null && var9 == null) {
                  return false;
               }

               if(var9.getItem() != var10.getItem()) {
                  return false;
               }

               if(var9.getData() != 32767 && var9.getData() != var10.getData()) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public ItemStack a(InventoryCrafting var1) {
      ItemStack var2 = this.b().cloneItemStack();
      if(this.e) {
         for(int var3 = 0; var3 < var1.getSize(); ++var3) {
            ItemStack var4 = var1.getItem(var3);
            if(var4 != null && var4.hasTag()) {
               var2.setTag((NBTTagCompound)var4.getTag().clone());
            }
         }
      }

      return var2;
   }

   public int a() {
      return this.width * this.height;
   }
}
