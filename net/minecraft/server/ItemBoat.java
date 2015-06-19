package net.minecraft.server;

import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityBoat;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.StatisticList;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class ItemBoat extends Item {
   public ItemBoat() {
      this.maxStackSize = 1;
      this.a(CreativeModeTab.e);
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      float var4 = 1.0F;
      float var5 = var3.lastPitch + (var3.pitch - var3.lastPitch) * var4;
      float var6 = var3.lastYaw + (var3.yaw - var3.lastYaw) * var4;
      double var7 = var3.lastX + (var3.locX - var3.lastX) * (double)var4;
      double var9 = var3.lastY + (var3.locY - var3.lastY) * (double)var4 + (double)var3.getHeadHeight();
      double var11 = var3.lastZ + (var3.locZ - var3.lastZ) * (double)var4;
      Vec3D var13 = new Vec3D(var7, var9, var11);
      float var14 = MathHelper.cos(-var6 * 0.017453292F - 3.1415927F);
      float var15 = MathHelper.sin(-var6 * 0.017453292F - 3.1415927F);
      float var16 = -MathHelper.cos(-var5 * 0.017453292F);
      float var17 = MathHelper.sin(-var5 * 0.017453292F);
      float var18 = var15 * var16;
      float var20 = var14 * var16;
      double var21 = 5.0D;
      Vec3D var23 = var13.add((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
      MovingObjectPosition var24 = var2.rayTrace(var13, var23, true);
      if(var24 == null) {
         return var1;
      } else {
         Vec3D var25 = var3.d(var4);
         boolean var26 = false;
         float var27 = 1.0F;
         List var28 = var2.getEntities(var3, var3.getBoundingBox().a(var25.a * var21, var25.b * var21, var25.c * var21).grow((double)var27, (double)var27, (double)var27));

         for(int var29 = 0; var29 < var28.size(); ++var29) {
            Entity var30 = (Entity)var28.get(var29);
            if(var30.ad()) {
               float var31 = var30.ao();
               AxisAlignedBB var32 = var30.getBoundingBox().grow((double)var31, (double)var31, (double)var31);
               if(var32.a(var13)) {
                  var26 = true;
               }
            }
         }

         if(var26) {
            return var1;
         } else {
            if(var24.type == MovingObjectPosition.EnumMovingObjectType.BLOCK) {
               BlockPosition var33 = var24.a();
               if(var2.getType(var33).getBlock() == Blocks.SNOW_LAYER) {
                  var33 = var33.down();
               }

               EntityBoat var34 = new EntityBoat(var2, (double)((float)var33.getX() + 0.5F), (double)((float)var33.getY() + 1.0F), (double)((float)var33.getZ() + 0.5F));
               var34.yaw = (float)(((MathHelper.floor((double)(var3.yaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);
               if(!var2.getCubes(var34, var34.getBoundingBox().grow(-0.1D, -0.1D, -0.1D)).isEmpty()) {
                  return var1;
               }

               if(!var2.isClientSide) {
                  var2.addEntity(var34);
               }

               if(!var3.abilities.canInstantlyBuild) {
                  --var1.count;
               }

               var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
            }

            return var1;
         }
      }
   }
}
