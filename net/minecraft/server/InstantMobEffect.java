package net.minecraft.server;

import net.minecraft.server.MinecraftKey;
import net.minecraft.server.MobEffectList;

public class InstantMobEffect extends MobEffectList {
   public InstantMobEffect(int var1, MinecraftKey var2, boolean var3, int var4) {
      super(var1, var2, var3, var4);
   }

   public boolean isInstant() {
      return true;
   }

   public boolean a(int var1, int var2) {
      return var1 >= 1;
   }
}
