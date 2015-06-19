package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.IRecipe;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ShapelessRecipes implements IRecipe {
   private final ItemStack result;
   private final List<ItemStack> ingredients;

   public ShapelessRecipes(ItemStack var1, List<ItemStack> var2) {
      this.result = var1;
      this.ingredients = var2;
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
      ArrayList var3 = Lists.newArrayList((Iterable)this.ingredients);

      for(int var4 = 0; var4 < var1.h(); ++var4) {
         for(int var5 = 0; var5 < var1.i(); ++var5) {
            ItemStack var6 = var1.c(var5, var4);
            if(var6 != null) {
               boolean var7 = false;
               Iterator var8 = var3.iterator();

               while(var8.hasNext()) {
                  ItemStack var9 = (ItemStack)var8.next();
                  if(var6.getItem() == var9.getItem() && (var9.getData() == 32767 || var6.getData() == var9.getData())) {
                     var7 = true;
                     var3.remove(var9);
                     break;
                  }
               }

               if(!var7) {
                  return false;
               }
            }
         }
      }

      return var3.isEmpty();
   }

   public ItemStack a(InventoryCrafting var1) {
      return this.result.cloneItemStack();
   }

   public int a() {
      return this.ingredients.size();
   }
}
