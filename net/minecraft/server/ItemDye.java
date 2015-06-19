package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockWood;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntitySheep;
import net.minecraft.server.EnumColor;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockFragilePlantElement;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemDye extends Item {
   public static final int[] a = new int[]{1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

   public ItemDye() {
      this.a(true);
      this.setMaxDurability(0);
      this.a(CreativeModeTab.l);
   }

   public String e_(ItemStack var1) {
      int var2 = var1.getData();
      return super.getName() + "." + EnumColor.fromInvColorIndex(var2).d();
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(!var2.a(var4.shift(var5), var5, var1)) {
         return false;
      } else {
         EnumColor var9 = EnumColor.fromInvColorIndex(var1.getData());
         if(var9 == EnumColor.WHITE) {
            if(a(var1, var3, var4)) {
               if(!var3.isClientSide) {
                  var3.triggerEffect(2005, var4, 0);
               }

               return true;
            }
         } else if(var9 == EnumColor.BROWN) {
            IBlockData var10 = var3.getType(var4);
            Block var11 = var10.getBlock();
            if(var11 == Blocks.LOG && var10.get(BlockWood.VARIANT) == BlockWood.EnumLogVariant.JUNGLE) {
               if(var5 == EnumDirection.DOWN) {
                  return false;
               }

               if(var5 == EnumDirection.UP) {
                  return false;
               }

               var4 = var4.shift(var5);
               if(var3.isEmpty(var4)) {
                  IBlockData var12 = Blocks.COCOA.getPlacedState(var3, var4, var5, var6, var7, var8, 0, var2);
                  var3.setTypeAndData(var4, var12, 2);
                  if(!var2.abilities.canInstantlyBuild) {
                     --var1.count;
                  }
               }

               return true;
            }
         }

         return false;
      }
   }

   public static boolean a(ItemStack var0, World var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      if(var3.getBlock() instanceof IBlockFragilePlantElement) {
         IBlockFragilePlantElement var4 = (IBlockFragilePlantElement)var3.getBlock();
         if(var4.a(var1, var2, var3, var1.isClientSide)) {
            if(!var1.isClientSide) {
               if(var4.a(var1, var1.random, var2, var3)) {
                  var4.b(var1, var1.random, var2, var3);
               }

               --var0.count;
            }

            return true;
         }
      }

      return false;
   }

   public boolean a(ItemStack var1, EntityHuman var2, EntityLiving var3) {
      if(var3 instanceof EntitySheep) {
         EntitySheep var4 = (EntitySheep)var3;
         EnumColor var5 = EnumColor.fromInvColorIndex(var1.getData());
         if(!var4.isSheared() && var4.getColor() != var5) {
            var4.setColor(var5);
            --var1.count;
         }

         return true;
      } else {
         return false;
      }
   }
}
