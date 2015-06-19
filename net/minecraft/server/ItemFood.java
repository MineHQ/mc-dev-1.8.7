package net.minecraft.server;

import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumAnimation;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MobEffect;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class ItemFood extends Item {
   public final int a;
   private final int b;
   private final float c;
   private final boolean d;
   private boolean k;
   private int l;
   private int m;
   private int n;
   private float o;

   public ItemFood(int var1, float var2, boolean var3) {
      this.a = 32;
      this.b = var1;
      this.d = var3;
      this.c = var2;
      this.a(CreativeModeTab.h);
   }

   public ItemFood(int var1, boolean var2) {
      this(var1, 0.6F, var2);
   }

   public ItemStack b(ItemStack var1, World var2, EntityHuman var3) {
      --var1.count;
      var3.getFoodData().a(this, var1);
      var2.makeSound(var3, "random.burp", 0.5F, var2.random.nextFloat() * 0.1F + 0.9F);
      this.c(var1, var2, var3);
      var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
      return var1;
   }

   protected void c(ItemStack var1, World var2, EntityHuman var3) {
      if(!var2.isClientSide && this.l > 0 && var2.random.nextFloat() < this.o) {
         var3.addEffect(new MobEffect(this.l, this.m * 20, this.n));
      }

   }

   public int d(ItemStack var1) {
      return 32;
   }

   public EnumAnimation e(ItemStack var1) {
      return EnumAnimation.EAT;
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      if(var3.j(this.k)) {
         var3.a(var1, this.d(var1));
      }

      return var1;
   }

   public int getNutrition(ItemStack var1) {
      return this.b;
   }

   public float getSaturationModifier(ItemStack var1) {
      return this.c;
   }

   public boolean g() {
      return this.d;
   }

   public ItemFood a(int var1, int var2, int var3, float var4) {
      this.l = var1;
      this.m = var2;
      this.n = var3;
      this.o = var4;
      return this;
   }

   public ItemFood h() {
      this.k = true;
      return this;
   }
}
