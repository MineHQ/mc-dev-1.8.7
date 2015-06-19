package net.minecraft.server;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class ItemBookAndQuill extends Item {
   public ItemBookAndQuill() {
      this.c(1);
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      var3.openBook(var1);
      var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
      return var1;
   }

   public static boolean b(NBTTagCompound var0) {
      if(var0 == null) {
         return false;
      } else if(!var0.hasKeyOfType("pages", 9)) {
         return false;
      } else {
         NBTTagList var1 = var0.getList("pages", 8);

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            String var3 = var1.getString(var2);
            if(var3 == null) {
               return false;
            }

            if(var3.length() > 32767) {
               return false;
            }
         }

         return true;
      }
   }
}
