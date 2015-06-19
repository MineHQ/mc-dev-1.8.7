package net.minecraft.server;

import net.minecraft.server.EnumDirection;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;

public interface IWorldInventory extends IInventory {
   int[] getSlotsForFace(EnumDirection var1);

   boolean canPlaceItemThroughFace(int var1, ItemStack var2, EnumDirection var3);

   boolean canTakeItemThroughFace(int var1, ItemStack var2, EnumDirection var3);
}
