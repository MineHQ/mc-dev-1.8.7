package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockDirt;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class ItemHoe extends Item {
   protected Item.EnumToolMaterial a;

   public ItemHoe(Item.EnumToolMaterial var1) {
      this.a = var1;
      this.maxStackSize = 1;
      this.setMaxDurability(var1.a());
      this.a(CreativeModeTab.i);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(!var2.a(var4.shift(var5), var5, var1)) {
         return false;
      } else {
         IBlockData var9 = var3.getType(var4);
         Block var10 = var9.getBlock();
         if(var5 != EnumDirection.DOWN && var3.getType(var4.up()).getBlock().getMaterial() == Material.AIR) {
            if(var10 == Blocks.GRASS) {
               return this.a(var1, var2, var3, var4, Blocks.FARMLAND.getBlockData());
            }

            if(var10 == Blocks.DIRT) {
               switch(ItemHoe.SyntheticClass_1.a[((BlockDirt.EnumDirtVariant)var9.get(BlockDirt.VARIANT)).ordinal()]) {
               case 1:
                  return this.a(var1, var2, var3, var4, Blocks.FARMLAND.getBlockData());
               case 2:
                  return this.a(var1, var2, var3, var4, Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.DIRT));
               }
            }
         }

         return false;
      }
   }

   protected boolean a(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, IBlockData var5) {
      var3.makeSound((double)((float)var4.getX() + 0.5F), (double)((float)var4.getY() + 0.5F), (double)((float)var4.getZ() + 0.5F), var5.getBlock().stepSound.getStepSound(), (var5.getBlock().stepSound.getVolume1() + 1.0F) / 2.0F, var5.getBlock().stepSound.getVolume2() * 0.8F);
      if(var3.isClientSide) {
         return true;
      } else {
         var3.setTypeUpdate(var4, var5);
         var1.damage(1, var2);
         return true;
      }
   }

   public String g() {
      return this.a.toString();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[BlockDirt.EnumDirtVariant.values().length];

      static {
         try {
            a[BlockDirt.EnumDirtVariant.DIRT.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[BlockDirt.EnumDirtVariant.COARSE_DIRT.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
