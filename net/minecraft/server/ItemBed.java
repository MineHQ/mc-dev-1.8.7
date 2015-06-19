package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockBed;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.World;

public class ItemBed extends Item {
   public ItemBed() {
      this.a(CreativeModeTab.c);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var3.isClientSide) {
         return true;
      } else if(var5 != EnumDirection.UP) {
         return false;
      } else {
         IBlockData var9 = var3.getType(var4);
         Block var10 = var9.getBlock();
         boolean var11 = var10.a(var3, var4);
         if(!var11) {
            var4 = var4.up();
         }

         int var12 = MathHelper.floor((double)(var2.yaw * 4.0F / 360.0F) + 0.5D) & 3;
         EnumDirection var13 = EnumDirection.fromType2(var12);
         BlockPosition var14 = var4.shift(var13);
         if(var2.a(var4, var5, var1) && var2.a(var14, var5, var1)) {
            boolean var15 = var3.getType(var14).getBlock().a(var3, var14);
            boolean var16 = var11 || var3.isEmpty(var4);
            boolean var17 = var15 || var3.isEmpty(var14);
            if(var16 && var17 && World.a((IBlockAccess)var3, (BlockPosition)var4.down()) && World.a((IBlockAccess)var3, (BlockPosition)var14.down())) {
               IBlockData var18 = Blocks.BED.getBlockData().set(BlockBed.OCCUPIED, Boolean.valueOf(false)).set(BlockBed.FACING, var13).set(BlockBed.PART, BlockBed.EnumBedPart.FOOT);
               if(var3.setTypeAndData(var4, var18, 3)) {
                  IBlockData var19 = var18.set(BlockBed.PART, BlockBed.EnumBedPart.HEAD);
                  var3.setTypeAndData(var14, var19, 3);
               }

               --var1.count;
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }
}
