package net.minecraft.server;

import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityEnchantTable;
import net.minecraft.server.World;

public class BlockEnchantmentTable extends BlockContainer {
   protected BlockEnchantmentTable() {
      super(Material.STONE, MaterialMapColor.D);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
      this.e(0);
      this.a(CreativeModeTab.c);
   }

   public boolean d() {
      return false;
   }

   public boolean c() {
      return false;
   }

   public int b() {
      return 3;
   }

   public TileEntity a(World var1, int var2) {
      return new TileEntityEnchantTable();
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.isClientSide) {
         return true;
      } else {
         TileEntity var9 = var1.getTileEntity(var2);
         if(var9 instanceof TileEntityEnchantTable) {
            var4.openTileEntity((TileEntityEnchantTable)var9);
         }

         return true;
      }
   }

   public void postPlace(World var1, BlockPosition var2, IBlockData var3, EntityLiving var4, ItemStack var5) {
      super.postPlace(var1, var2, var3, var4, var5);
      if(var5.hasName()) {
         TileEntity var6 = var1.getTileEntity(var2);
         if(var6 instanceof TileEntityEnchantTable) {
            ((TileEntityEnchantTable)var6).a(var5.getName());
         }
      }

   }
}
