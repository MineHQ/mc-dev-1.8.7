package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHanging;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItemFrame;
import net.minecraft.server.EntityPainting;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemHanging extends Item {
   private final Class<? extends EntityHanging> a;

   public ItemHanging(Class<? extends EntityHanging> var1) {
      this.a = var1;
      this.a(CreativeModeTab.c);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var5 == EnumDirection.DOWN) {
         return false;
      } else if(var5 == EnumDirection.UP) {
         return false;
      } else {
         BlockPosition var9 = var4.shift(var5);
         if(!var2.a(var9, var5, var1)) {
            return false;
         } else {
            EntityHanging var10 = this.a(var3, var9, var5);
            if(var10 != null && var10.survives()) {
               if(!var3.isClientSide) {
                  var3.addEntity(var10);
               }

               --var1.count;
            }

            return true;
         }
      }
   }

   private EntityHanging a(World var1, BlockPosition var2, EnumDirection var3) {
      return (EntityHanging)(this.a == EntityPainting.class?new EntityPainting(var1, var2, var3):(this.a == EntityItemFrame.class?new EntityItemFrame(var1, var2, var3):null));
   }
}
