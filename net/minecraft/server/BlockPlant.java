package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.World;

public class BlockPlant extends Block {
   protected BlockPlant() {
      this(Material.PLANT);
   }

   protected BlockPlant(Material var1) {
      this(var1, var1.r());
   }

   protected BlockPlant(Material var1, MaterialMapColor var2) {
      super(var1, var2);
      this.a(true);
      float var3 = 0.2F;
      this.a(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 3.0F, 0.5F + var3);
      this.a(CreativeModeTab.c);
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return super.canPlace(var1, var2) && this.c(var1.getType(var2.down()).getBlock());
   }

   protected boolean c(Block var1) {
      return var1 == Blocks.GRASS || var1 == Blocks.DIRT || var1 == Blocks.FARMLAND;
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      super.doPhysics(var1, var2, var3, var4);
      this.e(var1, var2, var3);
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      this.e(var1, var2, var3);
   }

   protected void e(World var1, BlockPosition var2, IBlockData var3) {
      if(!this.f(var1, var2, var3)) {
         this.b(var1, var2, var3, 0);
         var1.setTypeAndData(var2, Blocks.AIR.getBlockData(), 3);
      }

   }

   public boolean f(World var1, BlockPosition var2, IBlockData var3) {
      return this.c(var1.getType(var2.down()).getBlock());
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return null;
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }
}
