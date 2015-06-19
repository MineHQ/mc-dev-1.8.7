package net.minecraft.server;

import net.minecraft.server.BlockEnderPortalFrame;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityEnderSignal;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class ItemEnderEye extends Item {
   public ItemEnderEye() {
      this.a(CreativeModeTab.f);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      IBlockData var9 = var3.getType(var4);
      if(var2.a(var4.shift(var5), var5, var1) && var9.getBlock() == Blocks.END_PORTAL_FRAME && !((Boolean)var9.get(BlockEnderPortalFrame.EYE)).booleanValue()) {
         if(var3.isClientSide) {
            return true;
         } else {
            var3.setTypeAndData(var4, var9.set(BlockEnderPortalFrame.EYE, Boolean.valueOf(true)), 2);
            var3.updateAdjacentComparators(var4, Blocks.END_PORTAL_FRAME);
            --var1.count;

            for(int var10 = 0; var10 < 16; ++var10) {
               double var11 = (double)((float)var4.getX() + (5.0F + g.nextFloat() * 6.0F) / 16.0F);
               double var13 = (double)((float)var4.getY() + 0.8125F);
               double var15 = (double)((float)var4.getZ() + (5.0F + g.nextFloat() * 6.0F) / 16.0F);
               double var17 = 0.0D;
               double var19 = 0.0D;
               double var21 = 0.0D;
               var3.addParticle(EnumParticle.SMOKE_NORMAL, var11, var13, var15, var17, var19, var21, new int[0]);
            }

            EnumDirection var23 = (EnumDirection)var9.get(BlockEnderPortalFrame.FACING);
            int var24 = 0;
            int var12 = 0;
            boolean var25 = false;
            boolean var14 = true;
            EnumDirection var26 = var23.e();

            for(int var16 = -2; var16 <= 2; ++var16) {
               BlockPosition var28 = var4.shift(var26, var16);
               IBlockData var18 = var3.getType(var28);
               if(var18.getBlock() == Blocks.END_PORTAL_FRAME) {
                  if(!((Boolean)var18.get(BlockEnderPortalFrame.EYE)).booleanValue()) {
                     var14 = false;
                     break;
                  }

                  var12 = var16;
                  if(!var25) {
                     var24 = var16;
                     var25 = true;
                  }
               }
            }

            if(var14 && var12 == var24 + 2) {
               BlockPosition var27 = var4.shift(var23, 4);

               int var29;
               for(var29 = var24; var29 <= var12; ++var29) {
                  BlockPosition var30 = var27.shift(var26, var29);
                  IBlockData var32 = var3.getType(var30);
                  if(var32.getBlock() != Blocks.END_PORTAL_FRAME || !((Boolean)var32.get(BlockEnderPortalFrame.EYE)).booleanValue()) {
                     var14 = false;
                     break;
                  }
               }

               int var31;
               BlockPosition var33;
               for(var29 = var24 - 1; var29 <= var12 + 1; var29 += 4) {
                  var27 = var4.shift(var26, var29);

                  for(var31 = 1; var31 <= 3; ++var31) {
                     var33 = var27.shift(var23, var31);
                     IBlockData var20 = var3.getType(var33);
                     if(var20.getBlock() != Blocks.END_PORTAL_FRAME || !((Boolean)var20.get(BlockEnderPortalFrame.EYE)).booleanValue()) {
                        var14 = false;
                        break;
                     }
                  }
               }

               if(var14) {
                  for(var29 = var24; var29 <= var12; ++var29) {
                     var27 = var4.shift(var26, var29);

                     for(var31 = 1; var31 <= 3; ++var31) {
                        var33 = var27.shift(var23, var31);
                        var3.setTypeAndData(var33, Blocks.END_PORTAL.getBlockData(), 2);
                     }
                  }
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      MovingObjectPosition var4 = this.a(var2, var3, false);
      if(var4 != null && var4.type == MovingObjectPosition.EnumMovingObjectType.BLOCK && var2.getType(var4.a()).getBlock() == Blocks.END_PORTAL_FRAME) {
         return var1;
      } else {
         if(!var2.isClientSide) {
            BlockPosition var5 = var2.a("Stronghold", new BlockPosition(var3));
            if(var5 != null) {
               EntityEnderSignal var6 = new EntityEnderSignal(var2, var3.locX, var3.locY, var3.locZ);
               var6.a(var5);
               var2.addEntity(var6);
               var2.makeSound(var3, "random.bow", 0.5F, 0.4F / (g.nextFloat() * 0.4F + 0.8F));
               var2.a((EntityHuman)null, 1002, new BlockPosition(var3), 0);
               if(!var3.abilities.canInstantlyBuild) {
                  --var1.count;
               }

               var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
            }
         }

         return var1;
      }
   }
}
