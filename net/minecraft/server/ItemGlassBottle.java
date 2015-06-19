package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class ItemGlassBottle extends Item {
   public ItemGlassBottle() {
      this.a(CreativeModeTab.k);
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      MovingObjectPosition var4 = this.a(var2, var3, true);
      if(var4 == null) {
         return var1;
      } else {
         if(var4.type == MovingObjectPosition.EnumMovingObjectType.BLOCK) {
            BlockPosition var5 = var4.a();
            if(!var2.a(var3, var5)) {
               return var1;
            }

            if(!var3.a(var5.shift(var4.direction), var4.direction, var1)) {
               return var1;
            }

            if(var2.getType(var5).getBlock().getMaterial() == Material.WATER) {
               --var1.count;
               var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
               if(var1.count <= 0) {
                  return new ItemStack(Items.POTION);
               }

               if(!var3.inventory.pickup(new ItemStack(Items.POTION))) {
                  var3.drop(new ItemStack(Items.POTION, 1, 0), false);
               }
            }
         }

         return var1;
      }
   }
}
