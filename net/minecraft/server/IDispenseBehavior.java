package net.minecraft.server;

import net.minecraft.server.ISourceBlock;
import net.minecraft.server.ItemStack;

public interface IDispenseBehavior {
   IDispenseBehavior a = new IDispenseBehavior() {
      public ItemStack a(ISourceBlock var1, ItemStack var2) {
         return var2;
      }
   };

   ItemStack a(ISourceBlock var1, ItemStack var2);
}
