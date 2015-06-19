package net.minecraft.server;

import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.INamable;
import net.minecraft.server.MaterialMapColor;

public enum EnumColor implements INamable {
   WHITE,
   ORANGE,
   MAGENTA,
   LIGHT_BLUE,
   YELLOW,
   LIME,
   PINK,
   GRAY,
   SILVER,
   CYAN,
   PURPLE,
   BLUE,
   BROWN,
   GREEN,
   RED,
   BLACK;

   private static final EnumColor[] q;
   private static final EnumColor[] r;
   private final int s;
   private final int t;
   private final String u;
   private final String v;
   private final MaterialMapColor w;
   private final EnumChatFormat x;

   private EnumColor(int var3, int var4, String var5, String var6, MaterialMapColor var7, EnumChatFormat var8) {
      this.s = var3;
      this.t = var4;
      this.u = var5;
      this.v = var6;
      this.w = var7;
      this.x = var8;
   }

   public int getColorIndex() {
      return this.s;
   }

   public int getInvColorIndex() {
      return this.t;
   }

   public String d() {
      return this.v;
   }

   public MaterialMapColor e() {
      return this.w;
   }

   public static EnumColor fromInvColorIndex(int var0) {
      if(var0 < 0 || var0 >= r.length) {
         var0 = 0;
      }

      return r[var0];
   }

   public static EnumColor fromColorIndex(int var0) {
      if(var0 < 0 || var0 >= q.length) {
         var0 = 0;
      }

      return q[var0];
   }

   public String toString() {
      return this.v;
   }

   public String getName() {
      return this.u;
   }

   static {
      WHITE = new EnumColor("WHITE", 0, 0, 15, "white", "white", MaterialMapColor.j, EnumChatFormat.WHITE);
      ORANGE = new EnumColor("ORANGE", 1, 1, 14, "orange", "orange", MaterialMapColor.q, EnumChatFormat.GOLD);
      MAGENTA = new EnumColor("MAGENTA", 2, 2, 13, "magenta", "magenta", MaterialMapColor.r, EnumChatFormat.AQUA);
      LIGHT_BLUE = new EnumColor("LIGHT_BLUE", 3, 3, 12, "light_blue", "lightBlue", MaterialMapColor.s, EnumChatFormat.BLUE);
      YELLOW = new EnumColor("YELLOW", 4, 4, 11, "yellow", "yellow", MaterialMapColor.t, EnumChatFormat.YELLOW);
      LIME = new EnumColor("LIME", 5, 5, 10, "lime", "lime", MaterialMapColor.u, EnumChatFormat.GREEN);
      PINK = new EnumColor("PINK", 6, 6, 9, "pink", "pink", MaterialMapColor.v, EnumChatFormat.LIGHT_PURPLE);
      GRAY = new EnumColor("GRAY", 7, 7, 8, "gray", "gray", MaterialMapColor.w, EnumChatFormat.DARK_GRAY);
      SILVER = new EnumColor("SILVER", 8, 8, 7, "silver", "silver", MaterialMapColor.x, EnumChatFormat.GRAY);
      CYAN = new EnumColor("CYAN", 9, 9, 6, "cyan", "cyan", MaterialMapColor.y, EnumChatFormat.DARK_AQUA);
      PURPLE = new EnumColor("PURPLE", 10, 10, 5, "purple", "purple", MaterialMapColor.z, EnumChatFormat.DARK_PURPLE);
      BLUE = new EnumColor("BLUE", 11, 11, 4, "blue", "blue", MaterialMapColor.A, EnumChatFormat.DARK_BLUE);
      BROWN = new EnumColor("BROWN", 12, 12, 3, "brown", "brown", MaterialMapColor.B, EnumChatFormat.GOLD);
      GREEN = new EnumColor("GREEN", 13, 13, 2, "green", "green", MaterialMapColor.C, EnumChatFormat.DARK_GREEN);
      RED = new EnumColor("RED", 14, 14, 1, "red", "red", MaterialMapColor.D, EnumChatFormat.DARK_RED);
      BLACK = new EnumColor("BLACK", 15, 15, 0, "black", "black", MaterialMapColor.E, EnumChatFormat.BLACK);
      q = new EnumColor[values().length];
      r = new EnumColor[values().length];
      EnumColor[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumColor var3 = var0[var2];
         q[var3.getColorIndex()] = var3;
         r[var3.getInvColorIndex()] = var3;
      }

   }
}
