package net.minecraft.server;

import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentSlotType;
import net.minecraft.server.MinecraftKey;

public class EnchantmentWaterWorker extends Enchantment {
   public EnchantmentWaterWorker(int var1, MinecraftKey var2, int var3) {
      super(var1, var2, var3, EnchantmentSlotType.ARMOR_HEAD);
      this.c("waterWorker");
   }

   public int a(int var1) {
      return 1;
   }

   public int b(int var1) {
      return this.a(var1) + 40;
   }

   public int getMaxLevel() {
      return 1;
   }
}
