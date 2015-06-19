package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EnchantmentArrowDamage;
import net.minecraft.server.EnchantmentArrowKnockback;
import net.minecraft.server.EnchantmentDepthStrider;
import net.minecraft.server.EnchantmentDigging;
import net.minecraft.server.EnchantmentDurability;
import net.minecraft.server.EnchantmentFire;
import net.minecraft.server.EnchantmentFlameArrows;
import net.minecraft.server.EnchantmentInfiniteArrows;
import net.minecraft.server.EnchantmentKnockback;
import net.minecraft.server.EnchantmentLootBonus;
import net.minecraft.server.EnchantmentLure;
import net.minecraft.server.EnchantmentOxygen;
import net.minecraft.server.EnchantmentProtection;
import net.minecraft.server.EnchantmentSilkTouch;
import net.minecraft.server.EnchantmentSlotType;
import net.minecraft.server.EnchantmentThorns;
import net.minecraft.server.EnchantmentWaterWorker;
import net.minecraft.server.EnchantmentWeaponDamage;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.ItemStack;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.MinecraftKey;

public abstract class Enchantment {
   private static final Enchantment[] byId = new Enchantment[256];
   public static final Enchantment[] b;
   private static final Map<MinecraftKey, Enchantment> E = Maps.newHashMap();
   public static final Enchantment PROTECTION_ENVIRONMENTAL = new EnchantmentProtection(0, new MinecraftKey("protection"), 10, 0);
   public static final Enchantment PROTECTION_FIRE = new EnchantmentProtection(1, new MinecraftKey("fire_protection"), 5, 1);
   public static final Enchantment PROTECTION_FALL = new EnchantmentProtection(2, new MinecraftKey("feather_falling"), 5, 2);
   public static final Enchantment PROTECTION_EXPLOSIONS = new EnchantmentProtection(3, new MinecraftKey("blast_protection"), 2, 3);
   public static final Enchantment PROTECTION_PROJECTILE = new EnchantmentProtection(4, new MinecraftKey("projectile_protection"), 5, 4);
   public static final Enchantment OXYGEN = new EnchantmentOxygen(5, new MinecraftKey("respiration"), 2);
   public static final Enchantment WATER_WORKER = new EnchantmentWaterWorker(6, new MinecraftKey("aqua_affinity"), 2);
   public static final Enchantment THORNS = new EnchantmentThorns(7, new MinecraftKey("thorns"), 1);
   public static final Enchantment DEPTH_STRIDER = new EnchantmentDepthStrider(8, new MinecraftKey("depth_strider"), 2);
   public static final Enchantment DAMAGE_ALL = new EnchantmentWeaponDamage(16, new MinecraftKey("sharpness"), 10, 0);
   public static final Enchantment DAMAGE_UNDEAD = new EnchantmentWeaponDamage(17, new MinecraftKey("smite"), 5, 1);
   public static final Enchantment DAMAGE_ARTHROPODS = new EnchantmentWeaponDamage(18, new MinecraftKey("bane_of_arthropods"), 5, 2);
   public static final Enchantment KNOCKBACK = new EnchantmentKnockback(19, new MinecraftKey("knockback"), 5);
   public static final Enchantment FIRE_ASPECT = new EnchantmentFire(20, new MinecraftKey("fire_aspect"), 2);
   public static final Enchantment LOOT_BONUS_MOBS;
   public static final Enchantment DIG_SPEED;
   public static final Enchantment SILK_TOUCH;
   public static final Enchantment DURABILITY;
   public static final Enchantment LOOT_BONUS_BLOCKS;
   public static final Enchantment ARROW_DAMAGE;
   public static final Enchantment ARROW_KNOCKBACK;
   public static final Enchantment ARROW_FIRE;
   public static final Enchantment ARROW_INFINITE;
   public static final Enchantment LUCK;
   public static final Enchantment LURE;
   public final int id;
   private final int weight;
   public EnchantmentSlotType slot;
   protected String name;

   public static Enchantment getById(int var0) {
      return var0 >= 0 && var0 < byId.length?byId[var0]:null;
   }

   protected Enchantment(int var1, MinecraftKey var2, int var3, EnchantmentSlotType var4) {
      this.id = var1;
      this.weight = var3;
      this.slot = var4;
      if(byId[var1] != null) {
         throw new IllegalArgumentException("Duplicate enchantment id!");
      } else {
         byId[var1] = this;
         E.put(var2, this);
      }
   }

   public static Enchantment getByName(String var0) {
      return (Enchantment)E.get(new MinecraftKey(var0));
   }

   public static Set<MinecraftKey> getEffects() {
      return E.keySet();
   }

   public int getRandomWeight() {
      return this.weight;
   }

   public int getStartLevel() {
      return 1;
   }

   public int getMaxLevel() {
      return 1;
   }

   public int a(int var1) {
      return 1 + var1 * 10;
   }

   public int b(int var1) {
      return this.a(var1) + 5;
   }

   public int a(int var1, DamageSource var2) {
      return 0;
   }

   public float a(int var1, EnumMonsterType var2) {
      return 0.0F;
   }

   public boolean a(Enchantment var1) {
      return this != var1;
   }

   public Enchantment c(String var1) {
      this.name = var1;
      return this;
   }

   public String a() {
      return "enchantment." + this.name;
   }

   public String d(int var1) {
      String var2 = LocaleI18n.get(this.a());
      return var2 + " " + LocaleI18n.get("enchantment.level." + var1);
   }

   public boolean canEnchant(ItemStack var1) {
      return this.slot.canEnchant(var1.getItem());
   }

   public void a(EntityLiving var1, Entity var2, int var3) {
   }

   public void b(EntityLiving var1, Entity var2, int var3) {
   }

   static {
      LOOT_BONUS_MOBS = new EnchantmentLootBonus(21, new MinecraftKey("looting"), 2, EnchantmentSlotType.WEAPON);
      DIG_SPEED = new EnchantmentDigging(32, new MinecraftKey("efficiency"), 10);
      SILK_TOUCH = new EnchantmentSilkTouch(33, new MinecraftKey("silk_touch"), 1);
      DURABILITY = new EnchantmentDurability(34, new MinecraftKey("unbreaking"), 5);
      LOOT_BONUS_BLOCKS = new EnchantmentLootBonus(35, new MinecraftKey("fortune"), 2, EnchantmentSlotType.DIGGER);
      ARROW_DAMAGE = new EnchantmentArrowDamage(48, new MinecraftKey("power"), 10);
      ARROW_KNOCKBACK = new EnchantmentArrowKnockback(49, new MinecraftKey("punch"), 2);
      ARROW_FIRE = new EnchantmentFlameArrows(50, new MinecraftKey("flame"), 2);
      ARROW_INFINITE = new EnchantmentInfiniteArrows(51, new MinecraftKey("infinity"), 1);
      LUCK = new EnchantmentLootBonus(61, new MinecraftKey("luck_of_the_sea"), 2, EnchantmentSlotType.FISHING_ROD);
      LURE = new EnchantmentLure(62, new MinecraftKey("lure"), 2, EnchantmentSlotType.FISHING_ROD);
      ArrayList var0 = Lists.newArrayList();
      Enchantment[] var1 = byId;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Enchantment var4 = var1[var3];
         if(var4 != null) {
            var0.add(var4);
         }
      }

      b = (Enchantment[])var0.toArray(new Enchantment[var0.size()]);
   }
}
