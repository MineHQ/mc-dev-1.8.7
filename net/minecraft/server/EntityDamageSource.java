package net.minecraft.server;

import net.minecraft.server.ChatMessage;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ItemStack;
import net.minecraft.server.LocaleI18n;

public class EntityDamageSource extends DamageSource {
   protected Entity q;
   private boolean r = false;

   public EntityDamageSource(String var1, Entity var2) {
      super(var1);
      this.q = var2;
   }

   public EntityDamageSource v() {
      this.r = true;
      return this;
   }

   public boolean w() {
      return this.r;
   }

   public Entity getEntity() {
      return this.q;
   }

   public IChatBaseComponent getLocalizedDeathMessage(EntityLiving var1) {
      ItemStack var2 = this.q instanceof EntityLiving?((EntityLiving)this.q).bA():null;
      String var3 = "death.attack." + this.translationIndex;
      String var4 = var3 + ".item";
      return var2 != null && var2.hasName() && LocaleI18n.c(var4)?new ChatMessage(var4, new Object[]{var1.getScoreboardDisplayName(), this.q.getScoreboardDisplayName(), var2.C()}):new ChatMessage(var3, new Object[]{var1.getScoreboardDisplayName(), this.q.getScoreboardDisplayName()});
   }

   public boolean r() {
      return this.q != null && this.q instanceof EntityLiving && !(this.q instanceof EntityHuman);
   }
}
