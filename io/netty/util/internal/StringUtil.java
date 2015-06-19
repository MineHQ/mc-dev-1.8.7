package io.netty.util.internal;

import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;

public final class StringUtil {
   public static final String NEWLINE;
   private static final String[] BYTE2HEX_PAD = new String[256];
   private static final String[] BYTE2HEX_NOPAD = new String[256];
   private static final String EMPTY_STRING = "";

   public static String[] split(String var0, char var1) {
      int var2 = var0.length();
      ArrayList var3 = new ArrayList();
      int var4 = 0;

      int var5;
      for(var5 = 0; var5 < var2; ++var5) {
         if(var0.charAt(var5) == var1) {
            if(var4 == var5) {
               var3.add("");
            } else {
               var3.add(var0.substring(var4, var5));
            }

            var4 = var5 + 1;
         }
      }

      if(var4 == 0) {
         var3.add(var0);
      } else if(var4 != var2) {
         var3.add(var0.substring(var4, var2));
      } else {
         for(var5 = var3.size() - 1; var5 >= 0 && ((String)var3.get(var5)).isEmpty(); --var5) {
            var3.remove(var5);
         }
      }

      return (String[])var3.toArray(new String[var3.size()]);
   }

   public static String byteToHexStringPadded(int var0) {
      return BYTE2HEX_PAD[var0 & 255];
   }

   public static <T extends Appendable> T byteToHexStringPadded(T var0, int var1) {
      try {
         var0.append(byteToHexStringPadded(var1));
      } catch (IOException var3) {
         PlatformDependent.throwException(var3);
      }

      return var0;
   }

   public static String toHexStringPadded(byte[] var0) {
      return toHexStringPadded(var0, 0, var0.length);
   }

   public static String toHexStringPadded(byte[] var0, int var1, int var2) {
      return ((StringBuilder)toHexStringPadded(new StringBuilder(var2 << 1), var0, var1, var2)).toString();
   }

   public static <T extends Appendable> T toHexStringPadded(T var0, byte[] var1) {
      return toHexStringPadded(var0, var1, 0, var1.length);
   }

   public static <T extends Appendable> T toHexStringPadded(T var0, byte[] var1, int var2, int var3) {
      int var4 = var2 + var3;

      for(int var5 = var2; var5 < var4; ++var5) {
         byteToHexStringPadded(var0, var1[var5]);
      }

      return var0;
   }

   public static String byteToHexString(int var0) {
      return BYTE2HEX_NOPAD[var0 & 255];
   }

   public static <T extends Appendable> T byteToHexString(T var0, int var1) {
      try {
         var0.append(byteToHexString(var1));
      } catch (IOException var3) {
         PlatformDependent.throwException(var3);
      }

      return var0;
   }

   public static String toHexString(byte[] var0) {
      return toHexString(var0, 0, var0.length);
   }

   public static String toHexString(byte[] var0, int var1, int var2) {
      return ((StringBuilder)toHexString(new StringBuilder(var2 << 1), var0, var1, var2)).toString();
   }

   public static <T extends Appendable> T toHexString(T var0, byte[] var1) {
      return toHexString(var0, var1, 0, var1.length);
   }

   public static <T extends Appendable> T toHexString(T var0, byte[] var1, int var2, int var3) {
      assert var3 >= 0;

      if(var3 == 0) {
         return var0;
      } else {
         int var4 = var2 + var3;
         int var5 = var4 - 1;

         int var6;
         for(var6 = var2; var6 < var5 && var1[var6] == 0; ++var6) {
            ;
         }

         byteToHexString(var0, var1[var6++]);
         int var7 = var4 - var6;
         toHexStringPadded(var0, var1, var6, var7);
         return var0;
      }
   }

   public static String simpleClassName(Object var0) {
      return var0 == null?"null_object":simpleClassName(var0.getClass());
   }

   public static String simpleClassName(Class<?> var0) {
      if(var0 == null) {
         return "null_class";
      } else {
         Package var1 = var0.getPackage();
         return var1 != null?var0.getName().substring(var1.getName().length() + 1):var0.getName();
      }
   }

   private StringUtil() {
   }

   static {
      String var0;
      try {
         var0 = (new Formatter()).format("%n", new Object[0]).toString();
      } catch (Exception var4) {
         var0 = "\n";
      }

      NEWLINE = var0;

      int var1;
      StringBuilder var2;
      for(var1 = 0; var1 < 10; ++var1) {
         var2 = new StringBuilder(2);
         var2.append('0');
         var2.append(var1);
         BYTE2HEX_PAD[var1] = var2.toString();
         BYTE2HEX_NOPAD[var1] = String.valueOf(var1);
      }

      while(var1 < 16) {
         var2 = new StringBuilder(2);
         char var3 = (char)(97 + var1 - 10);
         var2.append('0');
         var2.append(var3);
         BYTE2HEX_PAD[var1] = var2.toString();
         BYTE2HEX_NOPAD[var1] = String.valueOf(var3);
         ++var1;
      }

      while(var1 < BYTE2HEX_PAD.length) {
         var2 = new StringBuilder(2);
         var2.append(Integer.toHexString(var1));
         String var5 = var2.toString();
         BYTE2HEX_PAD[var1] = var5;
         BYTE2HEX_NOPAD[var1] = var5;
         ++var1;
      }

   }
}
