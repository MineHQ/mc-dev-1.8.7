package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.IBlockData;
import net.minecraft.server.ISourceBlock;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class SourceBlock implements ISourceBlock {
   private final World a;
   private final BlockPosition b;

   public SourceBlock(World var1, BlockPosition var2) {
      this.a = var1;
      this.b = var2;
   }

   public World i() {
      return this.a;
   }

   public double getX() {
      return (double)this.b.getX() + 0.5D;
   }

   public double getY() {
      return (double)this.b.getY() + 0.5D;
   }

   public double getZ() {
      return (double)this.b.getZ() + 0.5D;
   }

   public BlockPosition getBlockPosition() {
      return this.b;
   }

   public int f() {
      IBlockData var1 = this.a.getType(this.b);
      return var1.getBlock().toLegacyData(var1);
   }

   public <T extends TileEntity> T getTileEntity() {
      return this.a.getTileEntity(this.b);
   }
}
