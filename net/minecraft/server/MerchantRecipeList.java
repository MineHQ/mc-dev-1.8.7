package net.minecraft.server;

import java.util.ArrayList;
import net.minecraft.server.GameProfileSerializer;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MerchantRecipe;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.PacketDataSerializer;

public class MerchantRecipeList extends ArrayList<MerchantRecipe> {
   public MerchantRecipeList() {
   }

   public MerchantRecipeList(NBTTagCompound var1) {
      this.a(var1);
   }

   public MerchantRecipe a(ItemStack var1, ItemStack var2, int var3) {
      if(var3 > 0 && var3 < this.size()) {
         MerchantRecipe var6 = (MerchantRecipe)this.get(var3);
         return !this.a(var1, var6.getBuyItem1()) || (var2 != null || var6.hasSecondItem()) && (!var6.hasSecondItem() || !this.a(var2, var6.getBuyItem2())) || var1.count < var6.getBuyItem1().count || var6.hasSecondItem() && var2.count < var6.getBuyItem2().count?null:var6;
      } else {
         for(int var4 = 0; var4 < this.size(); ++var4) {
            MerchantRecipe var5 = (MerchantRecipe)this.get(var4);
            if(this.a(var1, var5.getBuyItem1()) && var1.count >= var5.getBuyItem1().count && (!var5.hasSecondItem() && var2 == null || var5.hasSecondItem() && this.a(var2, var5.getBuyItem2()) && var2.count >= var5.getBuyItem2().count)) {
               return var5;
            }
         }

         return null;
      }
   }

   private boolean a(ItemStack var1, ItemStack var2) {
      return ItemStack.c(var1, var2) && (!var2.hasTag() || var1.hasTag() && GameProfileSerializer.a(var2.getTag(), var1.getTag(), false));
   }

   public void a(PacketDataSerializer var1) {
      var1.writeByte((byte)(this.size() & 255));

      for(int var2 = 0; var2 < this.size(); ++var2) {
         MerchantRecipe var3 = (MerchantRecipe)this.get(var2);
         var1.a(var3.getBuyItem1());
         var1.a(var3.getBuyItem3());
         ItemStack var4 = var3.getBuyItem2();
         var1.writeBoolean(var4 != null);
         if(var4 != null) {
            var1.a(var4);
         }

         var1.writeBoolean(var3.h());
         var1.writeInt(var3.e());
         var1.writeInt(var3.f());
      }

   }

   public void a(NBTTagCompound var1) {
      NBTTagList var2 = var1.getList("Recipes", 10);

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         NBTTagCompound var4 = var2.get(var3);
         this.add(new MerchantRecipe(var4));
      }

   }

   public NBTTagCompound a() {
      NBTTagCompound var1 = new NBTTagCompound();
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.size(); ++var3) {
         MerchantRecipe var4 = (MerchantRecipe)this.get(var3);
         var2.add(var4.k());
      }

      var1.set("Recipes", var2);
      return var1;
   }
}
