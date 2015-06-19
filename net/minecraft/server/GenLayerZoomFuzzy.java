package net.minecraft.server;

import net.minecraft.server.GenLayer;
import net.minecraft.server.GenLayerZoom;

public class GenLayerZoomFuzzy extends GenLayerZoom {
   public GenLayerZoomFuzzy(long var1, GenLayer var3) {
      super(var1, var3);
   }

   protected int b(int var1, int var2, int var3, int var4) {
      return this.a(new int[]{var1, var2, var3, var4});
   }
}
