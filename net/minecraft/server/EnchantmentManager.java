package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Enchantment;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.WeightedRandom;
import net.minecraft.server.WeightedRandomEnchant;

public class EnchantmentManager {
   private static final Random a = new Random();
   private static final EnchantmentManager.EnchantmentModifierProtection b = new EnchantmentManager.EnchantmentModifierProtection();
   private static final EnchantmentManager.EnchantmentModifierDamage c = new EnchantmentManager.EnchantmentModifierDamage();
   private static final EnchantmentManager.EnchantmentModifierThorns d = new EnchantmentManager.EnchantmentModifierThorns();
   private static final EnchantmentManager.EnchantmentModifierArthropods e = new EnchantmentManager.EnchantmentModifierArthropods();

   public static int getEnchantmentLevel(int var0, ItemStack var1) {
      if(var1 == null) {
         return 0;
      } else {
         NBTTagList var2 = var1.getEnchantments();
         if(var2 == null) {
            return 0;
         } else {
            for(int var3 = 0; var3 < var2.size(); ++var3) {
               short var4 = var2.get(var3).getShort("id");
               short var5 = var2.get(var3).getShort("lvl");
               if(var4 == var0) {
                  return var5;
               }
            }

            return 0;
         }
      }
   }

   public static Map<Integer, Integer> a(ItemStack var0) {
      LinkedHashMap var1 = Maps.newLinkedHashMap();
      NBTTagList var2 = var0.getItem() == Items.ENCHANTED_BOOK?Items.ENCHANTED_BOOK.h(var0):var0.getEnchantments();
      if(var2 != null) {
         for(int var3 = 0; var3 < var2.size(); ++var3) {
            short var4 = var2.get(var3).getShort("id");
            short var5 = var2.get(var3).getShort("lvl");
            var1.put(Integer.valueOf(var4), Integer.valueOf(var5));
         }
      }

      return var1;
   }

   public static void a(Map<Integer, Integer> var0, ItemStack var1) {
      NBTTagList var2 = new NBTTagList();
      Iterator var3 = var0.keySet().iterator();

      while(var3.hasNext()) {
         int var4 = ((Integer)var3.next()).intValue();
         Enchantment var5 = Enchantment.getById(var4);
         if(var5 != null) {
            NBTTagCompound var6 = new NBTTagCompound();
            var6.setShort("id", (short)var4);
            var6.setShort("lvl", (short)((Integer)var0.get(Integer.valueOf(var4))).intValue());
            var2.add(var6);
            if(var1.getItem() == Items.ENCHANTED_BOOK) {
               Items.ENCHANTED_BOOK.a(var1, new WeightedRandomEnchant(var5, ((Integer)var0.get(Integer.valueOf(var4))).intValue()));
            }
         }
      }

      if(var2.size() > 0) {
         if(var1.getItem() != Items.ENCHANTED_BOOK) {
            var1.a((String)"ench", (NBTBase)var2);
         }
      } else if(var1.hasTag()) {
         var1.getTag().remove("ench");
      }

   }

   public static int a(int var0, ItemStack[] var1) {
      if(var1 == null) {
         return 0;
      } else {
         int var2 = 0;
         ItemStack[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ItemStack var6 = var3[var5];
            int var7 = getEnchantmentLevel(var0, var6);
            if(var7 > var2) {
               var2 = var7;
            }
         }

         return var2;
      }
   }

   private static void a(EnchantmentManager.EnchantmentModifier var0, ItemStack var1) {
      if(var1 != null) {
         NBTTagList var2 = var1.getEnchantments();
         if(var2 != null) {
            for(int var3 = 0; var3 < var2.size(); ++var3) {
               short var4 = var2.get(var3).getShort("id");
               short var5 = var2.get(var3).getShort("lvl");
               if(Enchantment.getById(var4) != null) {
                  var0.a(Enchantment.getById(var4), var5);
               }
            }

         }
      }
   }

   private static void a(EnchantmentManager.EnchantmentModifier var0, ItemStack[] var1) {
      ItemStack[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack var5 = var2[var4];
         a(var0, var5);
      }

   }

   public static int a(ItemStack[] var0, DamageSource var1) {
      b.a = 0;
      b.b = var1;
      a((EnchantmentManager.EnchantmentModifier)b, (ItemStack[])var0);
      if(b.a > 25) {
         b.a = 25;
      } else if(b.a < 0) {
         b.a = 0;
      }

      return (b.a + 1 >> 1) + a.nextInt((b.a >> 1) + 1);
   }

   public static float a(ItemStack var0, EnumMonsterType var1) {
      c.a = 0.0F;
      c.b = var1;
      a((EnchantmentManager.EnchantmentModifier)c, (ItemStack)var0);
      return c.a;
   }

   public static void a(EntityLiving var0, Entity var1) {
      d.b = var1;
      d.a = var0;
      if(var0 != null) {
         a((EnchantmentManager.EnchantmentModifier)d, (ItemStack[])var0.getEquipment());
      }

      if(var1 instanceof EntityHuman) {
         a((EnchantmentManager.EnchantmentModifier)d, (ItemStack)var0.bA());
      }

   }

   public static void b(EntityLiving var0, Entity var1) {
      e.a = var0;
      e.b = var1;
      if(var0 != null) {
         a((EnchantmentManager.EnchantmentModifier)e, (ItemStack[])var0.getEquipment());
      }

      if(var0 instanceof EntityHuman) {
         a((EnchantmentManager.EnchantmentModifier)e, (ItemStack)var0.bA());
      }

   }

   public static int a(EntityLiving var0) {
      return getEnchantmentLevel(Enchantment.KNOCKBACK.id, var0.bA());
   }

   public static int getFireAspectEnchantmentLevel(EntityLiving var0) {
      return getEnchantmentLevel(Enchantment.FIRE_ASPECT.id, var0.bA());
   }

   public static int getOxygenEnchantmentLevel(Entity var0) {
      return a(Enchantment.OXYGEN.id, var0.getEquipment());
   }

   public static int b(Entity var0) {
      return a(Enchantment.DEPTH_STRIDER.id, var0.getEquipment());
   }

   public static int getDigSpeedEnchantmentLevel(EntityLiving var0) {
      return getEnchantmentLevel(Enchantment.DIG_SPEED.id, var0.bA());
   }

   public static boolean hasSilkTouchEnchantment(EntityLiving var0) {
      return getEnchantmentLevel(Enchantment.SILK_TOUCH.id, var0.bA()) > 0;
   }

   public static int getBonusBlockLootEnchantmentLevel(EntityLiving var0) {
      return getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS.id, var0.bA());
   }

   public static int g(EntityLiving var0) {
      return getEnchantmentLevel(Enchantment.LUCK.id, var0.bA());
   }

   public static int h(EntityLiving var0) {
      return getEnchantmentLevel(Enchantment.LURE.id, var0.bA());
   }

   public static int getBonusMonsterLootEnchantmentLevel(EntityLiving var0) {
      return getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS.id, var0.bA());
   }

   public static boolean j(EntityLiving var0) {
      return a(Enchantment.WATER_WORKER.id, var0.getEquipment()) > 0;
   }

   public static ItemStack a(Enchantment var0, EntityLiving var1) {
      ItemStack[] var2 = var1.getEquipment();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack var5 = var2[var4];
         if(var5 != null && getEnchantmentLevel(var0.id, var5) > 0) {
            return var5;
         }
      }

      return null;
   }

   public static int a(Random var0, int var1, int var2, ItemStack var3) {
      Item var4 = var3.getItem();
      int var5 = var4.b();
      if(var5 <= 0) {
         return 0;
      } else {
         if(var2 > 15) {
            var2 = 15;
         }

         int var6 = var0.nextInt(8) + 1 + (var2 >> 1) + var0.nextInt(var2 + 1);
         return var1 == 0?Math.max(var6 / 3, 1):(var1 == 1?var6 * 2 / 3 + 1:Math.max(var6, var2 * 2));
      }
   }

   public static ItemStack a(Random var0, ItemStack var1, int var2) {
      List var3 = b(var0, var1, var2);
      boolean var4 = var1.getItem() == Items.BOOK;
      if(var4) {
         var1.setItem(Items.ENCHANTED_BOOK);
      }

      if(var3 != null) {
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            WeightedRandomEnchant var6 = (WeightedRandomEnchant)var5.next();
            if(var4) {
               Items.ENCHANTED_BOOK.a(var1, var6);
            } else {
               var1.addEnchantment(var6.enchantment, var6.level);
            }
         }
      }

      return var1;
   }

   public static List<WeightedRandomEnchant> b(Random var0, ItemStack var1, int var2) {
      Item var3 = var1.getItem();
      int var4 = var3.b();
      if(var4 <= 0) {
         return null;
      } else {
         var4 /= 2;
         var4 = 1 + var0.nextInt((var4 >> 1) + 1) + var0.nextInt((var4 >> 1) + 1);
         int var5 = var4 + var2;
         float var6 = (var0.nextFloat() + var0.nextFloat() - 1.0F) * 0.15F;
         int var7 = (int)((float)var5 * (1.0F + var6) + 0.5F);
         if(var7 < 1) {
            var7 = 1;
         }

         ArrayList var8 = null;
         Map var9 = b(var7, var1);
         if(var9 != null && !var9.isEmpty()) {
            WeightedRandomEnchant var10 = (WeightedRandomEnchant)WeightedRandom.a(var0, var9.values());
            if(var10 != null) {
               var8 = Lists.newArrayList();
               var8.add(var10);

               for(int var11 = var7; var0.nextInt(50) <= var11; var11 >>= 1) {
                  Iterator var12 = var9.keySet().iterator();

                  while(var12.hasNext()) {
                     Integer var13 = (Integer)var12.next();
                     boolean var14 = true;
                     Iterator var15 = var8.iterator();

                     while(var15.hasNext()) {
                        WeightedRandomEnchant var16 = (WeightedRandomEnchant)var15.next();
                        if(!var16.enchantment.a(Enchantment.getById(var13.intValue()))) {
                           var14 = false;
                           break;
                        }
                     }

                     if(!var14) {
                        var12.remove();
                     }
                  }

                  if(!var9.isEmpty()) {
                     WeightedRandomEnchant var17 = (WeightedRandomEnchant)WeightedRandom.a(var0, var9.values());
                     var8.add(var17);
                  }
               }
            }
         }

         return var8;
      }
   }

   public static Map<Integer, WeightedRandomEnchant> b(int var0, ItemStack var1) {
      Item var2 = var1.getItem();
      HashMap var3 = null;
      boolean var4 = var1.getItem() == Items.BOOK;
      Enchantment[] var5 = Enchantment.b;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Enchantment var8 = var5[var7];
         if(var8 != null && (var8.slot.canEnchant(var2) || var4)) {
            for(int var9 = var8.getStartLevel(); var9 <= var8.getMaxLevel(); ++var9) {
               if(var0 >= var8.a(var9) && var0 <= var8.b(var9)) {
                  if(var3 == null) {
                     var3 = Maps.newHashMap();
                  }

                  var3.put(Integer.valueOf(var8.id), new WeightedRandomEnchant(var8, var9));
               }
            }
         }
      }

      return var3;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   static final class EnchantmentModifierArthropods implements EnchantmentManager.EnchantmentModifier {
      public EntityLiving a;
      public Entity b;

      private EnchantmentModifierArthropods() {
      }

      public void a(Enchantment var1, int var2) {
         var1.a(this.a, this.b, var2);
      }

      // $FF: synthetic method
      EnchantmentModifierArthropods(EnchantmentManager.SyntheticClass_1 var1) {
         this();
      }
   }

   static final class EnchantmentModifierThorns implements EnchantmentManager.EnchantmentModifier {
      public EntityLiving a;
      public Entity b;

      private EnchantmentModifierThorns() {
      }

      public void a(Enchantment var1, int var2) {
         var1.b(this.a, this.b, var2);
      }

      // $FF: synthetic method
      EnchantmentModifierThorns(EnchantmentManager.SyntheticClass_1 var1) {
         this();
      }
   }

   static final class EnchantmentModifierDamage implements EnchantmentManager.EnchantmentModifier {
      public float a;
      public EnumMonsterType b;

      private EnchantmentModifierDamage() {
      }

      public void a(Enchantment var1, int var2) {
         this.a += var1.a(var2, this.b);
      }

      // $FF: synthetic method
      EnchantmentModifierDamage(EnchantmentManager.SyntheticClass_1 var1) {
         this();
      }
   }

   static final class EnchantmentModifierProtection implements EnchantmentManager.EnchantmentModifier {
      public int a;
      public DamageSource b;

      private EnchantmentModifierProtection() {
      }

      public void a(Enchantment var1, int var2) {
         this.a += var1.a(var2, this.b);
      }

      // $FF: synthetic method
      EnchantmentModifierProtection(EnchantmentManager.SyntheticClass_1 var1) {
         this();
      }
   }

   interface EnchantmentModifier {
      void a(Enchantment var1, int var2);
   }
}
