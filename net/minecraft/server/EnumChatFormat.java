package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public enum EnumChatFormat {
   BLACK("BLACK", '0', 0),
   DARK_BLUE("DARK_BLUE", '1', 1),
   DARK_GREEN("DARK_GREEN", '2', 2),
   DARK_AQUA("DARK_AQUA", '3', 3),
   DARK_RED("DARK_RED", '4', 4),
   DARK_PURPLE("DARK_PURPLE", '5', 5),
   GOLD("GOLD", '6', 6),
   GRAY("GRAY", '7', 7),
   DARK_GRAY("DARK_GRAY", '8', 8),
   BLUE("BLUE", '9', 9),
   GREEN("GREEN", 'a', 10),
   AQUA("AQUA", 'b', 11),
   RED("RED", 'c', 12),
   LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13),
   YELLOW("YELLOW", 'e', 14),
   WHITE("WHITE", 'f', 15),
   OBFUSCATED("OBFUSCATED", 'k', true),
   BOLD("BOLD", 'l', true),
   STRIKETHROUGH("STRIKETHROUGH", 'm', true),
   UNDERLINE("UNDERLINE", 'n', true),
   ITALIC("ITALIC", 'o', true),
   RESET("RESET", 'r', -1);

   private static final Map<String, EnumChatFormat> w;
   private static final Pattern x;
   private final String y;
   private final char z;
   private final boolean A;
   private final String B;
   private final int C;

   private static String c(String var0) {
      return var0.toLowerCase().replaceAll("[^a-z]", "");
   }

   private EnumChatFormat(String var3, char var4, int var5) {
      this(var3, var4, false, var5);
   }

   private EnumChatFormat(String var3, char var4, boolean var5) {
      this(var3, var4, var5, -1);
   }

   private EnumChatFormat(String var3, char var4, boolean var5, int var6) {
      this.y = var3;
      this.z = var4;
      this.A = var5;
      this.C = var6;
      this.B = "\u00a7" + var4;
   }

   public int b() {
      return this.C;
   }

   public boolean isFormat() {
      return this.A;
   }

   public boolean d() {
      return !this.A && this != RESET;
   }

   public String e() {
      return this.name().toLowerCase();
   }

   public String toString() {
      return this.B;
   }

   public static String a(String var0) {
      return var0 == null?null:x.matcher(var0).replaceAll("");
   }

   public static EnumChatFormat b(String var0) {
      return var0 == null?null:(EnumChatFormat)w.get(c(var0));
   }

   public static EnumChatFormat a(int var0) {
      if(var0 < 0) {
         return RESET;
      } else {
         EnumChatFormat[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            EnumChatFormat var4 = var1[var3];
            if(var4.b() == var0) {
               return var4;
            }
         }

         return null;
      }
   }

   public static Collection<String> a(boolean var0, boolean var1) {
      ArrayList var2 = Lists.newArrayList();
      EnumChatFormat[] var3 = values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumChatFormat var6 = var3[var5];
         if((!var6.d() || var0) && (!var6.isFormat() || var1)) {
            var2.add(var6.e());
         }
      }

      return var2;
   }

   static {
      w = Maps.newHashMap();
      x = Pattern.compile("(?i)" + String.valueOf('\u00a7') + "[0-9A-FK-OR]");
      EnumChatFormat[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumChatFormat var3 = var0[var2];
         w.put(c(var3.y), var3);
      }

   }
}
