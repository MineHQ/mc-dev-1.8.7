package net.minecraft.server;

import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArmorStand;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Vector3f;
import net.minecraft.server.World;

public class ItemArmorStand extends Item {
   public ItemArmorStand() {
      this.a(CreativeModeTab.c);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var5 == EnumDirection.DOWN) {
         return false;
      } else {
         boolean var9 = var3.getType(var4).getBlock().a(var3, var4);
         BlockPosition var10 = var9?var4:var4.shift(var5);
         if(!var2.a(var10, var5, var1)) {
            return false;
         } else {
            BlockPosition var11 = var10.up();
            boolean var12 = !var3.isEmpty(var10) && !var3.getType(var10).getBlock().a(var3, var10);
            var12 |= !var3.isEmpty(var11) && !var3.getType(var11).getBlock().a(var3, var11);
            if(var12) {
               return false;
            } else {
               double var13 = (double)var10.getX();
               double var15 = (double)var10.getY();
               double var17 = (double)var10.getZ();
               List var19 = var3.getEntities((Entity)null, AxisAlignedBB.a(var13, var15, var17, var13 + 1.0D, var15 + 2.0D, var17 + 1.0D));
               if(var19.size() > 0) {
                  return false;
               } else {
                  if(!var3.isClientSide) {
                     var3.setAir(var10);
                     var3.setAir(var11);
                     EntityArmorStand var20 = new EntityArmorStand(var3, var13 + 0.5D, var15, var17 + 0.5D);
                     float var21 = (float)MathHelper.d((MathHelper.g(var2.yaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                     var20.setPositionRotation(var13 + 0.5D, var15, var17 + 0.5D, var21, 0.0F);
                     this.a(var20, var3.random);
                     NBTTagCompound var22 = var1.getTag();
                     if(var22 != null && var22.hasKeyOfType("EntityTag", 10)) {
                        NBTTagCompound var23 = new NBTTagCompound();
                        var20.d(var23);
                        var23.a(var22.getCompound("EntityTag"));
                        var20.f(var23);
                     }

                     var3.addEntity(var20);
                  }

                  --var1.count;
                  return true;
               }
            }
         }
      }
   }

   private void a(EntityArmorStand var1, Random var2) {
      Vector3f var3 = var1.t();
      float var5 = var2.nextFloat() * 5.0F;
      float var6 = var2.nextFloat() * 20.0F - 10.0F;
      Vector3f var4 = new Vector3f(var3.getX() + var5, var3.getY() + var6, var3.getZ());
      var1.setHeadPose(var4);
      var3 = var1.u();
      var5 = var2.nextFloat() * 10.0F - 5.0F;
      var4 = new Vector3f(var3.getX(), var3.getY() + var5, var3.getZ());
      var1.setBodyPose(var4);
   }
}
