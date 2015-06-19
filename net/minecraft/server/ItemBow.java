package net.minecraft.server;

import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumAnimation;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class ItemBow extends Item {
   public static final String[] a = new String[]{"pulling_0", "pulling_1", "pulling_2"};

   public ItemBow() {
      this.maxStackSize = 1;
      this.setMaxDurability(384);
      this.a(CreativeModeTab.j);
   }

   public void a(ItemStack var1, World var2, EntityHuman var3, int var4) {
      boolean var5 = var3.abilities.canInstantlyBuild || EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_INFINITE.id, var1) > 0;
      if(var5 || var3.inventory.b(Items.ARROW)) {
         int var6 = this.d(var1) - var4;
         float var7 = (float)var6 / 20.0F;
         var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;
         if((double)var7 < 0.1D) {
            return;
         }

         if(var7 > 1.0F) {
            var7 = 1.0F;
         }

         EntityArrow var8 = new EntityArrow(var2, var3, var7 * 2.0F);
         if(var7 == 1.0F) {
            var8.setCritical(true);
         }

         int var9 = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, var1);
         if(var9 > 0) {
            var8.b(var8.j() + (double)var9 * 0.5D + 0.5D);
         }

         int var10 = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, var1);
         if(var10 > 0) {
            var8.setKnockbackStrength(var10);
         }

         if(EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, var1) > 0) {
            var8.setOnFire(100);
         }

         var1.damage(1, var3);
         var2.makeSound(var3, "random.bow", 1.0F, 1.0F / (g.nextFloat() * 0.4F + 1.2F) + var7 * 0.5F);
         if(var5) {
            var8.fromPlayer = 2;
         } else {
            var3.inventory.a(Items.ARROW);
         }

         var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
         if(!var2.isClientSide) {
            var2.addEntity(var8);
         }
      }

   }

   public ItemStack b(ItemStack var1, World var2, EntityHuman var3) {
      return var1;
   }

   public int d(ItemStack var1) {
      return 72000;
   }

   public EnumAnimation e(ItemStack var1) {
      return EnumAnimation.BOW;
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      if(var3.abilities.canInstantlyBuild || var3.inventory.b(Items.ARROW)) {
         var3.a(var1, this.d(var1));
      }

      return var1;
   }

   public int b() {
      return 1;
   }
}
