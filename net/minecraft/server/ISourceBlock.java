package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.ILocationSource;
import net.minecraft.server.TileEntity;

public interface ISourceBlock extends ILocationSource {
   double getX();

   double getY();

   double getZ();

   BlockPosition getBlockPosition();

   int f();

   <T extends TileEntity> T getTileEntity();
}
