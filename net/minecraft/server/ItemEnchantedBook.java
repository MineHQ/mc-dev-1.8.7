package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.EnumItemRarity;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.StructurePieceTreasure;
import net.minecraft.server.WeightedRandomEnchant;

public class ItemEnchantedBook extends Item {
   public ItemEnchantedBook() {
   }

   public boolean f_(ItemStack var1) {
      return false;
   }

   public EnumItemRarity g(ItemStack var1) {
      return this.h(var1).size() > 0?EnumItemRarity.UNCOMMON:super.g(var1);
   }

   public NBTTagList h(ItemStack var1) {
      NBTTagCompound var2 = var1.getTag();
      return var2 != null && var2.hasKeyOfType("StoredEnchantments", 9)?(NBTTagList)var2.get("StoredEnchantments"):new NBTTagList();
   }

   public void a(ItemStack var1, WeightedRandomEnchant var2) {
      NBTTagList var3 = this.h(var1);
      boolean var4 = true;

      for(int var5 = 0; var5 < var3.size(); ++var5) {
         NBTTagCompound var6 = var3.get(var5);
         if(var6.getShort("id") == var2.enchantment.id) {
            if(var6.getShort("lvl") < var2.level) {
               var6.setShort("lvl", (short)var2.level);
            }

            var4 = false;
            break;
         }
      }

      if(var4) {
         NBTTagCompound var7 = new NBTTagCompound();
         var7.setShort("id", (short)var2.enchantment.id);
         var7.setShort("lvl", (short)var2.level);
         var3.add(var7);
      }

      if(!var1.hasTag()) {
         var1.setTag(new NBTTagCompound());
      }

      var1.getTag().set("StoredEnchantments", var3);
   }

   public ItemStack a(WeightedRandomEnchant var1) {
      ItemStack var2 = new ItemStack(this);
      this.a(var2, var1);
      return var2;
   }

   public StructurePieceTreasure b(Random var1) {
      return this.a(var1, 1, 1, 1);
   }

   public StructurePieceTreasure a(Random var1, int var2, int var3, int var4) {
      ItemStack var5 = new ItemStack(Items.BOOK, 1, 0);
      EnchantmentManager.a(var1, var5, 30);
      return new StructurePieceTreasure(var5, var2, var3, var4);
   }
}
