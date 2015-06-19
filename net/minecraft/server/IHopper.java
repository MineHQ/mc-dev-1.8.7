package net.minecraft.server;

import net.minecraft.server.IInventory;
import net.minecraft.server.World;

public interface IHopper extends IInventory {
   World getWorld();

   double A();

   double B();

   double C();
}
