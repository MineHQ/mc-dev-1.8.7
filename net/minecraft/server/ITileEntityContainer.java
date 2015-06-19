package net.minecraft.server;

import net.minecraft.server.Container;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.INamableTileEntity;
import net.minecraft.server.PlayerInventory;

public interface ITileEntityContainer extends INamableTileEntity {
   Container createContainer(PlayerInventory var1, EntityHuman var2);

   String getContainerName();
}
