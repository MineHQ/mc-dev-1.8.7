package net.minecraft.server;

import net.minecraft.server.BlockFloorSign;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockWallSign;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Item;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntitySign;
import net.minecraft.server.World;

public class ItemSign extends Item {
   public ItemSign() {
      this.maxStackSize = 16;
      this.a(CreativeModeTab.c);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var5 == EnumDirection.DOWN) {
         return false;
      } else if(!var3.getType(var4).getBlock().getMaterial().isBuildable()) {
         return false;
      } else {
         var4 = var4.shift(var5);
         if(!var2.a(var4, var5, var1)) {
            return false;
         } else if(!Blocks.STANDING_SIGN.canPlace(var3, var4)) {
            return false;
         } else if(var3.isClientSide) {
            return true;
         } else {
            if(var5 == EnumDirection.UP) {
               int var9 = MathHelper.floor((double)((var2.yaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
               var3.setTypeAndData(var4, Blocks.STANDING_SIGN.getBlockData().set(BlockFloorSign.ROTATION, Integer.valueOf(var9)), 3);
            } else {
               var3.setTypeAndData(var4, Blocks.WALL_SIGN.getBlockData().set(BlockWallSign.FACING, var5), 3);
            }

            --var1.count;
            TileEntity var10 = var3.getTileEntity(var4);
            if(var10 instanceof TileEntitySign && !ItemBlock.a(var3, var2, var4, var1)) {
               var2.openSign((TileEntitySign)var10);
            }

            return true;
         }
      }
   }
}
