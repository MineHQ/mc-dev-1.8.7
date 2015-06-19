package net.minecraft.server;

import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentSlotType;
import net.minecraft.server.MinecraftKey;

public class EnchantmentFire extends Enchantment {
   protected EnchantmentFire(int var1, MinecraftKey var2, int var3) {
      super(var1, var2, var3, EnchantmentSlotType.WEAPON);
      this.c("fire");
   }

   public int a(int var1) {
      return 10 + 20 * (var1 - 1);
   }

   public int b(int var1) {
      return super.a(var1) + 50;
   }

   public int getMaxLevel() {
      return 2;
   }
}
