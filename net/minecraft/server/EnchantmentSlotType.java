package net.minecraft.server;

import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.ItemBow;
import net.minecraft.server.ItemFishingRod;
import net.minecraft.server.ItemSword;
import net.minecraft.server.ItemTool;

public enum EnchantmentSlotType {
   ALL,
   ARMOR,
   ARMOR_FEET,
   ARMOR_LEGS,
   ARMOR_TORSO,
   ARMOR_HEAD,
   WEAPON,
   DIGGER,
   FISHING_ROD,
   BREAKABLE,
   BOW;

   private EnchantmentSlotType() {
   }

   public boolean canEnchant(Item var1) {
      if(this == ALL) {
         return true;
      } else if(this == BREAKABLE && var1.usesDurability()) {
         return true;
      } else if(var1 instanceof ItemArmor) {
         if(this == ARMOR) {
            return true;
         } else {
            ItemArmor var2 = (ItemArmor)var1;
            return var2.b == 0?this == ARMOR_HEAD:(var2.b == 2?this == ARMOR_LEGS:(var2.b == 1?this == ARMOR_TORSO:(var2.b == 3?this == ARMOR_FEET:false)));
         }
      } else {
         return var1 instanceof ItemSword?this == WEAPON:(var1 instanceof ItemTool?this == DIGGER:(var1 instanceof ItemBow?this == BOW:(var1 instanceof ItemFishingRod?this == FISHING_ROD:false)));
      }
   }
}
