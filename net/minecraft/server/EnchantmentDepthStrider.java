package net.minecraft.server;

import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentSlotType;
import net.minecraft.server.MinecraftKey;

public class EnchantmentDepthStrider extends Enchantment {
   public EnchantmentDepthStrider(int var1, MinecraftKey var2, int var3) {
      super(var1, var2, var3, EnchantmentSlotType.ARMOR_FEET);
      this.c("waterWalker");
   }

   public int a(int var1) {
      return var1 * 10;
   }

   public int b(int var1) {
      return this.a(var1) + 15;
   }

   public int getMaxLevel() {
      return 3;
   }
}
