package net.minecraft.server;

import net.minecraft.server.EnumChatFormat;

public enum EnumItemRarity {
   COMMON,
   UNCOMMON,
   RARE,
   EPIC;

   public final EnumChatFormat e;
   public final String f;

   private EnumItemRarity(EnumChatFormat var3, String var4) {
      this.e = var3;
      this.f = var4;
   }

   static {
      COMMON = new EnumItemRarity("COMMON", 0, EnumChatFormat.WHITE, "Common");
      UNCOMMON = new EnumItemRarity("UNCOMMON", 1, EnumChatFormat.YELLOW, "Uncommon");
      RARE = new EnumItemRarity("RARE", 2, EnumChatFormat.AQUA, "Rare");
      EPIC = new EnumItemRarity("EPIC", 3, EnumChatFormat.LIGHT_PURPLE, "Epic");
   }
}
