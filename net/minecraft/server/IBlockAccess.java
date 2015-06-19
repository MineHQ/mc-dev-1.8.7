package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.TileEntity;

public interface IBlockAccess {
   TileEntity getTileEntity(BlockPosition var1);

   IBlockData getType(BlockPosition var1);

   boolean isEmpty(BlockPosition var1);

   int getBlockPower(BlockPosition var1, EnumDirection var2);
}
