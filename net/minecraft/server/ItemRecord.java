package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.server.BlockJukeBox;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumItemRarity;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class ItemRecord extends Item {
   private static final Map<String, ItemRecord> b = Maps.newHashMap();
   public final String a;

   protected ItemRecord(String var1) {
      this.a = var1;
      this.maxStackSize = 1;
      this.a(CreativeModeTab.f);
      b.put("records." + var1, this);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      IBlockData var9 = var3.getType(var4);
      if(var9.getBlock() == Blocks.JUKEBOX && !((Boolean)var9.get(BlockJukeBox.HAS_RECORD)).booleanValue()) {
         if(var3.isClientSide) {
            return true;
         } else {
            ((BlockJukeBox)Blocks.JUKEBOX).a(var3, var4, var9, var1);
            var3.a((EntityHuman)null, 1005, var4, Item.getId(this));
            --var1.count;
            var2.b(StatisticList.X);
            return true;
         }
      } else {
         return false;
      }
   }

   public EnumItemRarity g(ItemStack var1) {
      return EnumItemRarity.RARE;
   }
}
