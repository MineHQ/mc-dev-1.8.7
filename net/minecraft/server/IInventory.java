package net.minecraft.server;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.INamableTileEntity;
import net.minecraft.server.ItemStack;

public interface IInventory extends INamableTileEntity {
   int getSize();

   ItemStack getItem(int var1);

   ItemStack splitStack(int var1, int var2);

   ItemStack splitWithoutUpdate(int var1);

   void setItem(int var1, ItemStack var2);

   int getMaxStackSize();

   void update();

   boolean a(EntityHuman var1);

   void startOpen(EntityHuman var1);

   void closeContainer(EntityHuman var1);

   boolean b(int var1, ItemStack var2);

   int getProperty(int var1);

   void b(int var1, int var2);

   int g();

   void l();
}
