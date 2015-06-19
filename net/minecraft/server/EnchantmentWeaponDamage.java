package net.minecraft.server;

import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentSlotType;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.ItemAxe;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;

public class EnchantmentWeaponDamage extends Enchantment {
   private static final String[] E = new String[]{"all", "undead", "arthropods"};
   private static final int[] F = new int[]{1, 5, 5};
   private static final int[] G = new int[]{11, 8, 8};
   private static final int[] H = new int[]{20, 20, 20};
   public final int a;

   public EnchantmentWeaponDamage(int var1, MinecraftKey var2, int var3, int var4) {
      super(var1, var2, var3, EnchantmentSlotType.WEAPON);
      this.a = var4;
   }

   public int a(int var1) {
      return F[this.a] + (var1 - 1) * G[this.a];
   }

   public int b(int var1) {
      return this.a(var1) + H[this.a];
   }

   public int getMaxLevel() {
      return 5;
   }

   public float a(int var1, EnumMonsterType var2) {
      return this.a == 0?(float)var1 * 1.25F:(this.a == 1 && var2 == EnumMonsterType.UNDEAD?(float)var1 * 2.5F:(this.a == 2 && var2 == EnumMonsterType.ARTHROPOD?(float)var1 * 2.5F:0.0F));
   }

   public String a() {
      return "enchantment.damage." + E[this.a];
   }

   public boolean a(Enchantment var1) {
      return !(var1 instanceof EnchantmentWeaponDamage);
   }

   public boolean canEnchant(ItemStack var1) {
      return var1.getItem() instanceof ItemAxe?true:super.canEnchant(var1);
   }

   public void a(EntityLiving var1, Entity var2, int var3) {
      if(var2 instanceof EntityLiving) {
         EntityLiving var4 = (EntityLiving)var2;
         if(this.a == 2 && var4.getMonsterType() == EnumMonsterType.ARTHROPOD) {
            int var5 = 20 + var1.bc().nextInt(10 * var3);
            var4.addEffect(new MobEffect(MobEffectList.SLOWER_MOVEMENT.id, var5, 3));
         }
      }

   }
}
