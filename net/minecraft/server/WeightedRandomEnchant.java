package net.minecraft.server;

import net.minecraft.server.Enchantment;
import net.minecraft.server.WeightedRandom;

public class WeightedRandomEnchant extends WeightedRandom.WeightedRandomChoice {
   public final Enchantment enchantment;
   public final int level;

   public WeightedRandomEnchant(Enchantment var1, int var2) {
      super(var1.getRandomWeight());
      this.enchantment = var1;
      this.level = var2;
   }
}
