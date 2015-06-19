package net.minecraft.server;

public class SharedConstants {
   public static final char[] allowedCharacters = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};

   public static boolean isAllowedChatCharacter(char var0) {
      return var0 != 167 && var0 >= 32 && var0 != 127;
   }

   public static String a(String var0) {
      StringBuilder var1 = new StringBuilder();
      char[] var2 = var0.toCharArray();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char var5 = var2[var4];
         if(isAllowedChatCharacter(var5)) {
            var1.append(var5);
         }
      }

      return var1.toString();
   }
}
