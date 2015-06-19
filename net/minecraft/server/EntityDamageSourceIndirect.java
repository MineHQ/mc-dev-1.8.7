package net.minecraft.server;

import net.minecraft.server.ChatMessage;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityDamageSource;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ItemStack;
import net.minecraft.server.LocaleI18n;

public class EntityDamageSourceIndirect extends EntityDamageSource {
   private Entity owner;

   public EntityDamageSourceIndirect(String var1, Entity var2, Entity var3) {
      super(var1, var2);
      this.owner = var3;
   }

   public Entity i() {
      return this.q;
   }

   public Entity getEntity() {
      return this.owner;
   }

   public IChatBaseComponent getLocalizedDeathMessage(EntityLiving var1) {
      IChatBaseComponent var2 = this.owner == null?this.q.getScoreboardDisplayName():this.owner.getScoreboardDisplayName();
      ItemStack var3 = this.owner instanceof EntityLiving?((EntityLiving)this.owner).bA():null;
      String var4 = "death.attack." + this.translationIndex;
      String var5 = var4 + ".item";
      return var3 != null && var3.hasName() && LocaleI18n.c(var5)?new ChatMessage(var5, new Object[]{var1.getScoreboardDisplayName(), var2, var3.C()}):new ChatMessage(var4, new Object[]{var1.getScoreboardDisplayName(), var2});
   }
}
