package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.EnchantmentSlotType;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MinecraftKey;

public class EnchantmentThorns extends Enchantment {
   public EnchantmentThorns(int var1, MinecraftKey var2, int var3) {
      super(var1, var2, var3, EnchantmentSlotType.ARMOR_TORSO);
      this.c("thorns");
   }

   public int a(int var1) {
      return 10 + 20 * (var1 - 1);
   }

   public int b(int var1) {
      return super.a(var1) + 50;
   }

   public int getMaxLevel() {
      return 3;
   }

   public boolean canEnchant(ItemStack var1) {
      return var1.getItem() instanceof ItemArmor?true:super.canEnchant(var1);
   }

   public void b(EntityLiving var1, Entity var2, int var3) {
      Random var4 = var1.bc();
      ItemStack var5 = EnchantmentManager.a(Enchantment.THORNS, var1);
      if(a(var3, var4)) {
         if(var2 != null) {
            var2.damageEntity(DamageSource.a(var1), (float)b(var3, var4));
            var2.makeSound("damage.thorns", 0.5F, 1.0F);
         }

         if(var5 != null) {
            var5.damage(3, var1);
         }
      } else if(var5 != null) {
         var5.damage(1, var1);
      }

   }

   public static boolean a(int var0, Random var1) {
      return var0 <= 0?false:var1.nextFloat() < 0.15F * (float)var0;
   }

   public static int b(int var0, Random var1) {
      return var0 > 10?var0 - 10:1 + var1.nextInt(4);
   }
}
