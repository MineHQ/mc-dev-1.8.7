package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemFood;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.PotionBrewer;
import net.minecraft.server.World;

public class ItemFish extends ItemFood {
   private final boolean b;

   public ItemFish(boolean var1) {
      super(0, 0.0F, false);
      this.b = var1;
   }

   public int getNutrition(ItemStack var1) {
      ItemFish.EnumFish var2 = ItemFish.EnumFish.a(var1);
      return this.b && var2.g()?var2.e():var2.c();
   }

   public float getSaturationModifier(ItemStack var1) {
      ItemFish.EnumFish var2 = ItemFish.EnumFish.a(var1);
      return this.b && var2.g()?var2.f():var2.d();
   }

   public String j(ItemStack var1) {
      return ItemFish.EnumFish.a(var1) == ItemFish.EnumFish.PUFFERFISH?PotionBrewer.m:null;
   }

   protected void c(ItemStack var1, World var2, EntityHuman var3) {
      ItemFish.EnumFish var4 = ItemFish.EnumFish.a(var1);
      if(var4 == ItemFish.EnumFish.PUFFERFISH) {
         var3.addEffect(new MobEffect(MobEffectList.POISON.id, 1200, 3));
         var3.addEffect(new MobEffect(MobEffectList.HUNGER.id, 300, 2));
         var3.addEffect(new MobEffect(MobEffectList.CONFUSION.id, 300, 1));
      }

      super.c(var1, var2, var3);
   }

   public String e_(ItemStack var1) {
      ItemFish.EnumFish var2 = ItemFish.EnumFish.a(var1);
      return this.getName() + "." + var2.b() + "." + (this.b && var2.g()?"cooked":"raw");
   }

   public static enum EnumFish {
      COD(0, "cod", 2, 0.1F, 5, 0.6F),
      SALMON(1, "salmon", 2, 0.1F, 6, 0.8F),
      CLOWNFISH(2, "clownfish", 1, 0.1F),
      PUFFERFISH(3, "pufferfish", 1, 0.1F);

      private static final Map<Integer, ItemFish.EnumFish> e;
      private final int f;
      private final String g;
      private final int h;
      private final float i;
      private final int j;
      private final float k;
      private boolean l = false;

      private EnumFish(int var3, String var4, int var5, float var6, int var7, float var8) {
         this.f = var3;
         this.g = var4;
         this.h = var5;
         this.i = var6;
         this.j = var7;
         this.k = var8;
         this.l = true;
      }

      private EnumFish(int var3, String var4, int var5, float var6) {
         this.f = var3;
         this.g = var4;
         this.h = var5;
         this.i = var6;
         this.j = 0;
         this.k = 0.0F;
         this.l = false;
      }

      public int a() {
         return this.f;
      }

      public String b() {
         return this.g;
      }

      public int c() {
         return this.h;
      }

      public float d() {
         return this.i;
      }

      public int e() {
         return this.j;
      }

      public float f() {
         return this.k;
      }

      public boolean g() {
         return this.l;
      }

      public static ItemFish.EnumFish a(int var0) {
         ItemFish.EnumFish var1 = (ItemFish.EnumFish)e.get(Integer.valueOf(var0));
         return var1 == null?COD:var1;
      }

      public static ItemFish.EnumFish a(ItemStack var0) {
         return var0.getItem() instanceof ItemFish?a(var0.getData()):COD;
      }

      static {
         e = Maps.newHashMap();
         ItemFish.EnumFish[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            ItemFish.EnumFish var3 = var0[var2];
            e.put(Integer.valueOf(var3.a()), var3);
         }

      }
   }
}
