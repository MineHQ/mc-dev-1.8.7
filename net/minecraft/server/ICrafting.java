package net.minecraft.server;

import java.util.List;
import net.minecraft.server.Container;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;

public interface ICrafting {
   void a(Container var1, List<ItemStack> var2);

   void a(Container var1, int var2, ItemStack var3);

   void setContainerData(Container var1, int var2, int var3);

   void setContainerData(Container var1, IInventory var2);
}
