package net.minecraft.server;

import net.minecraft.server.AttributeModifier;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.MobEffectList;

public class MobEffectAttackDamage extends MobEffectList {
   protected MobEffectAttackDamage(int var1, MinecraftKey var2, boolean var3, int var4) {
      super(var1, var2, var3, var4);
   }

   public double a(int var1, AttributeModifier var2) {
      return this.id == MobEffectList.WEAKNESS.id?(double)(-0.5F * (float)(var1 + 1)):1.3D * (double)(var1 + 1);
   }
}
