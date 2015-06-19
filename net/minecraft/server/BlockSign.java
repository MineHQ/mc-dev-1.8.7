package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntitySign;
import net.minecraft.server.World;

public class BlockSign extends BlockContainer {
   protected BlockSign() {
      super(Material.WOOD);
      float var1 = 0.25F;
      float var2 = 1.0F;
      this.a(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var2, 0.5F + var1);
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return null;
   }

   public boolean d() {
      return false;
   }

   public boolean b(IBlockAccess var1, BlockPosition var2) {
      return true;
   }

   public boolean c() {
      return false;
   }

   public boolean g() {
      return true;
   }

   public TileEntity a(World var1, int var2) {
      return new TileEntitySign();
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.SIGN;
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.isClientSide) {
         return true;
      } else {
         TileEntity var9 = var1.getTileEntity(var2);
         return var9 instanceof TileEntitySign?((TileEntitySign)var9).b(var4):false;
      }
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return !this.e(var1, var2) && super.canPlace(var1, var2);
   }
}
