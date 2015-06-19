package net.minecraft.server;

import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public interface IRecipe {
   boolean a(InventoryCrafting var1, World var2);

   ItemStack a(InventoryCrafting var1);

   int a();

   ItemStack b();

   ItemStack[] b(InventoryCrafting var1);
}
