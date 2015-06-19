package net.minecraft.server;

import net.minecraft.server.DamageSource;
import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.EnchantmentSlotType;
import net.minecraft.server.Entity;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftKey;

public class EnchantmentProtection extends Enchantment {
   private static final String[] E = new String[]{"all", "fire", "fall", "explosion", "projectile"};
   private static final int[] F = new int[]{1, 10, 5, 5, 3};
   private static final int[] G = new int[]{11, 8, 6, 8, 6};
   private static final int[] H = new int[]{20, 12, 10, 12, 15};
   public final int a;

   public EnchantmentProtection(int var1, MinecraftKey var2, int var3, int var4) {
      super(var1, var2, var3, EnchantmentSlotType.ARMOR);
      this.a = var4;
      if(var4 == 2) {
         this.slot = EnchantmentSlotType.ARMOR_FEET;
      }

   }

   public int a(int var1) {
      return F[this.a] + (var1 - 1) * G[this.a];
   }

   public int b(int var1) {
      return this.a(var1) + H[this.a];
   }

   public int getMaxLevel() {
      return 4;
   }

   public int a(int var1, DamageSource var2) {
      if(var2.ignoresInvulnerability()) {
         return 0;
      } else {
         float var3 = (float)(6 + var1 * var1) / 3.0F;
         return this.a == 0?MathHelper.d(var3 * 0.75F):(this.a == 1 && var2.o()?MathHelper.d(var3 * 1.25F):(this.a == 2 && var2 == DamageSource.FALL?MathHelper.d(var3 * 2.5F):(this.a == 3 && var2.isExplosion()?MathHelper.d(var3 * 1.5F):(this.a == 4 && var2.a()?MathHelper.d(var3 * 1.5F):0))));
      }
   }

   public String a() {
      return "enchantment.protect." + E[this.a];
   }

   public boolean a(Enchantment var1) {
      if(var1 instanceof EnchantmentProtection) {
         EnchantmentProtection var2 = (EnchantmentProtection)var1;
         return var2.a == this.a?false:this.a == 2 || var2.a == 2;
      } else {
         return super.a(var1);
      }
   }

   public static int a(Entity var0, int var1) {
      int var2 = EnchantmentManager.a(Enchantment.PROTECTION_FIRE.id, var0.getEquipment());
      if(var2 > 0) {
         var1 -= MathHelper.d((float)var1 * (float)var2 * 0.15F);
      }

      return var1;
   }

   public static double a(Entity var0, double var1) {
      int var3 = EnchantmentManager.a(Enchantment.PROTECTION_EXPLOSIONS.id, var0.getEquipment());
      if(var3 > 0) {
         var1 -= (double)MathHelper.floor(var1 * (double)((float)var3 * 0.15F));
      }

      return var1;
   }
}
