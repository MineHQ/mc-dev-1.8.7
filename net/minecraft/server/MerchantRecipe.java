package net.minecraft.server;

import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;

public class MerchantRecipe {
   private ItemStack buyingItem1;
   private ItemStack buyingItem2;
   private ItemStack sellingItem;
   private int uses;
   private int maxUses;
   private boolean rewardExp;

   public MerchantRecipe(NBTTagCompound var1) {
      this.a(var1);
   }

   public MerchantRecipe(ItemStack var1, ItemStack var2, ItemStack var3) {
      this(var1, var2, var3, 0, 7);
   }

   public MerchantRecipe(ItemStack var1, ItemStack var2, ItemStack var3, int var4, int var5) {
      this.buyingItem1 = var1;
      this.buyingItem2 = var2;
      this.sellingItem = var3;
      this.uses = var4;
      this.maxUses = var5;
      this.rewardExp = true;
   }

   public MerchantRecipe(ItemStack var1, ItemStack var2) {
      this(var1, (ItemStack)null, var2);
   }

   public MerchantRecipe(ItemStack var1, Item var2) {
      this(var1, new ItemStack(var2));
   }

   public ItemStack getBuyItem1() {
      return this.buyingItem1;
   }

   public ItemStack getBuyItem2() {
      return this.buyingItem2;
   }

   public boolean hasSecondItem() {
      return this.buyingItem2 != null;
   }

   public ItemStack getBuyItem3() {
      return this.sellingItem;
   }

   public int e() {
      return this.uses;
   }

   public int f() {
      return this.maxUses;
   }

   public void g() {
      ++this.uses;
   }

   public void a(int var1) {
      this.maxUses += var1;
   }

   public boolean h() {
      return this.uses >= this.maxUses;
   }

   public boolean j() {
      return this.rewardExp;
   }

   public void a(NBTTagCompound var1) {
      NBTTagCompound var2 = var1.getCompound("buy");
      this.buyingItem1 = ItemStack.createStack(var2);
      NBTTagCompound var3 = var1.getCompound("sell");
      this.sellingItem = ItemStack.createStack(var3);
      if(var1.hasKeyOfType("buyB", 10)) {
         this.buyingItem2 = ItemStack.createStack(var1.getCompound("buyB"));
      }

      if(var1.hasKeyOfType("uses", 99)) {
         this.uses = var1.getInt("uses");
      }

      if(var1.hasKeyOfType("maxUses", 99)) {
         this.maxUses = var1.getInt("maxUses");
      } else {
         this.maxUses = 7;
      }

      if(var1.hasKeyOfType("rewardExp", 1)) {
         this.rewardExp = var1.getBoolean("rewardExp");
      } else {
         this.rewardExp = true;
      }

   }

   public NBTTagCompound k() {
      NBTTagCompound var1 = new NBTTagCompound();
      var1.set("buy", this.buyingItem1.save(new NBTTagCompound()));
      var1.set("sell", this.sellingItem.save(new NBTTagCompound()));
      if(this.buyingItem2 != null) {
         var1.set("buyB", this.buyingItem2.save(new NBTTagCompound()));
      }

      var1.setInt("uses", this.uses);
      var1.setInt("maxUses", this.maxUses);
      var1.setBoolean("rewardExp", this.rewardExp);
      return var1;
   }
}
