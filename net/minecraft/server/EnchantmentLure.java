package net.minecraft.server;

import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentSlotType;
import net.minecraft.server.MinecraftKey;

public class EnchantmentLure extends Enchantment {
   protected EnchantmentLure(int var1, MinecraftKey var2, int var3, EnchantmentSlotType var4) {
      super(var1, var2, var3, var4);
      this.c("fishingSpeed");
   }

   public int a(int var1) {
      return 15 + (var1 - 1) * 9;
   }

   public int b(int var1) {
      return super.a(var1) + 50;
   }

   public int getMaxLevel() {
      return 3;
   }
}
