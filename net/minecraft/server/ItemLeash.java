package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockFence;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLeash;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemLeash extends Item {
   public ItemLeash() {
      this.a(CreativeModeTab.i);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      Block var9 = var3.getType(var4).getBlock();
      if(var9 instanceof BlockFence) {
         if(var3.isClientSide) {
            return true;
         } else {
            a(var2, var3, var4);
            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean a(EntityHuman var0, World var1, BlockPosition var2) {
      EntityLeash var3 = EntityLeash.b(var1, var2);
      boolean var4 = false;
      double var5 = 7.0D;
      int var7 = var2.getX();
      int var8 = var2.getY();
      int var9 = var2.getZ();
      List var10 = var1.a(EntityInsentient.class, new AxisAlignedBB((double)var7 - var5, (double)var8 - var5, (double)var9 - var5, (double)var7 + var5, (double)var8 + var5, (double)var9 + var5));
      Iterator var11 = var10.iterator();

      while(var11.hasNext()) {
         EntityInsentient var12 = (EntityInsentient)var11.next();
         if(var12.cc() && var12.getLeashHolder() == var0) {
            if(var3 == null) {
               var3 = EntityLeash.a(var1, var2);
            }

            var12.setLeashHolder(var3, true);
            var4 = true;
         }
      }

      return var4;
   }
}
