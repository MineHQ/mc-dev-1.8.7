package net.minecraft.server;

import net.minecraft.server.WorldProvider;

public class WorldProviderNormal extends WorldProvider {
   public WorldProviderNormal() {
   }

   public String getName() {
      return "Overworld";
   }

   public String getSuffix() {
      return "";
   }
}
