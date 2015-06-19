package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStepAbstract;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemStep extends ItemBlock {
   private final BlockStepAbstract b;
   private final BlockStepAbstract c;

   public ItemStep(Block var1, BlockStepAbstract var2, BlockStepAbstract var3) {
      super(var1);
      this.b = var2;
      this.c = var3;
      this.setMaxDurability(0);
      this.a(true);
   }

   public int filterData(int var1) {
      return var1;
   }

   public String e_(ItemStack var1) {
      return this.b.b(var1.getData());
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.count == 0) {
         return false;
      } else if(!var2.a(var4.shift(var5), var5, var1)) {
         return false;
      } else {
         Object var9 = this.b.a(var1);
         IBlockData var10 = var3.getType(var4);
         if(var10.getBlock() == this.b) {
            IBlockState var11 = this.b.n();
            Comparable var12 = var10.get(var11);
            BlockStepAbstract.EnumSlabHalf var13 = (BlockStepAbstract.EnumSlabHalf)var10.get(BlockStepAbstract.HALF);
            if((var5 == EnumDirection.UP && var13 == BlockStepAbstract.EnumSlabHalf.BOTTOM || var5 == EnumDirection.DOWN && var13 == BlockStepAbstract.EnumSlabHalf.TOP) && var12 == var9) {
               IBlockData var14 = this.c.getBlockData().set(var11, var12);
               if(var3.b(this.c.a(var3, var4, var14)) && var3.setTypeAndData(var4, var14, 3)) {
                  var3.makeSound((double)((float)var4.getX() + 0.5F), (double)((float)var4.getY() + 0.5F), (double)((float)var4.getZ() + 0.5F), this.c.stepSound.getPlaceSound(), (this.c.stepSound.getVolume1() + 1.0F) / 2.0F, this.c.stepSound.getVolume2() * 0.8F);
                  --var1.count;
               }

               return true;
            }
         }

         return this.a(var1, var3, var4.shift(var5), var9)?true:super.interactWith(var1, var2, var3, var4, var5, var6, var7, var8);
      }
   }

   private boolean a(ItemStack var1, World var2, BlockPosition var3, Object var4) {
      IBlockData var5 = var2.getType(var3);
      if(var5.getBlock() == this.b) {
         Comparable var6 = var5.get(this.b.n());
         if(var6 == var4) {
            IBlockData var7 = this.c.getBlockData().set(this.b.n(), var6);
            if(var2.b(this.c.a(var2, var3, var7)) && var2.setTypeAndData(var3, var7, 3)) {
               var2.makeSound((double)((float)var3.getX() + 0.5F), (double)((float)var3.getY() + 0.5F), (double)((float)var3.getZ() + 0.5F), this.c.stepSound.getPlaceSound(), (this.c.stepSound.getVolume1() + 1.0F) / 2.0F, this.c.stepSound.getVolume2() * 0.8F);
               --var1.count;
            }

            return true;
         }
      }

      return false;
   }
}
