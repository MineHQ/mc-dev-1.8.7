package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class ItemShears extends Item {
   public ItemShears() {
      this.c(1);
      this.setMaxDurability(238);
      this.a(CreativeModeTab.i);
   }

   public boolean a(ItemStack var1, World var2, Block var3, BlockPosition var4, EntityLiving var5) {
      if(var3.getMaterial() != Material.LEAVES && var3 != Blocks.WEB && var3 != Blocks.TALLGRASS && var3 != Blocks.VINE && var3 != Blocks.TRIPWIRE && var3 != Blocks.WOOL) {
         return super.a(var1, var2, var3, var4, var5);
      } else {
         var1.damage(1, var5);
         return true;
      }
   }

   public boolean canDestroySpecialBlock(Block var1) {
      return var1 == Blocks.WEB || var1 == Blocks.REDSTONE_WIRE || var1 == Blocks.TRIPWIRE;
   }

   public float getDestroySpeed(ItemStack var1, Block var2) {
      return var2 != Blocks.WEB && var2.getMaterial() != Material.LEAVES?(var2 == Blocks.WOOL?5.0F:super.getDestroySpeed(var1, var2)):15.0F;
   }
}
