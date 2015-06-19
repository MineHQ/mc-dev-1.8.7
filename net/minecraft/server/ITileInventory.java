package net.minecraft.server;

import net.minecraft.server.ChestLock;
import net.minecraft.server.IInventory;
import net.minecraft.server.ITileEntityContainer;

public interface ITileInventory extends IInventory, ITileEntityContainer {
   boolean r_();

   void a(ChestLock var1);

   ChestLock i();
}
