package net.minecraft.server;

import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemWorldMapBase;
import net.minecraft.server.Items;
import net.minecraft.server.PersistentBase;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;
import net.minecraft.server.WorldMap;

public class ItemMapEmpty extends ItemWorldMapBase {
   protected ItemMapEmpty() {
      this.a(CreativeModeTab.f);
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      ItemStack var4 = new ItemStack(Items.FILLED_MAP, 1, var2.b("map"));
      String var5 = "map_" + var4.getData();
      WorldMap var6 = new WorldMap(var5);
      var2.a((String)var5, (PersistentBase)var6);
      var6.scale = 0;
      var6.a(var3.locX, var3.locZ, var6.scale);
      var6.map = (byte)var2.worldProvider.getDimension();
      var6.c();
      --var1.count;
      if(var1.count <= 0) {
         return var4;
      } else {
         if(!var3.inventory.pickup(var4.cloneItemStack())) {
            var3.drop(var4, false);
         }

         var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
         return var1;
      }
   }
}
