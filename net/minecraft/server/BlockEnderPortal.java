package net.minecraft.server;

import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Entity;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityEnderPortal;
import net.minecraft.server.World;

public class BlockEnderPortal extends BlockContainer {
   protected BlockEnderPortal(Material var1) {
      super(var1);
      this.a(1.0F);
   }

   public TileEntity a(World var1, int var2) {
      return new TileEntityEnderPortal();
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      float var3 = 0.0625F;
      this.a(0.0F, 0.0F, 0.0F, 1.0F, var3, 1.0F);
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, Entity var6) {
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public int a(Random var1) {
      return 0;
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, Entity var4) {
      if(var4.vehicle == null && var4.passenger == null && !var1.isClientSide) {
         var4.c(1);
      }

   }

   public MaterialMapColor g(IBlockData var1) {
      return MaterialMapColor.E;
   }
}
