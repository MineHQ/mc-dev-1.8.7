package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockFluids;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemWithAuxData;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class ItemWaterLily extends ItemWithAuxData {
   public ItemWaterLily(Block var1) {
      super(var1, false);
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

            BlockPosition var6 = var5.up();
            IBlockData var7 = var2.getType(var5);
            if(var7.getBlock().getMaterial() == Material.WATER && ((Integer)var7.get(BlockFluids.LEVEL)).intValue() == 0 && var2.isEmpty(var6)) {
               var2.setTypeUpdate(var6, Blocks.WATERLILY.getBlockData());
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
