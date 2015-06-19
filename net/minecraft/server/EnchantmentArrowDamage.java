package net.minecraft.server;

import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentSlotType;
import net.minecraft.server.MinecraftKey;

public class EnchantmentArrowDamage extends Enchantment {
   public EnchantmentArrowDamage(int var1, MinecraftKey var2, int var3) {
      super(var1, var2, var3, EnchantmentSlotType.BOW);
      this.c("arrowDamage");
   }

   public int a(int var1) {
      return 1 + (var1 - 1) * 10;
   }

   public int b(int var1) {
      return this.a(var1) + 15;
   }

   public int getMaxLevel() {
      return 5;
   }
}
