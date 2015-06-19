package org.apache.commons.lang3;

import org.apache.commons.lang3.StringUtils;

public class CharUtils {
   private static final String[] CHAR_STRING_ARRAY = new String[128];
   public static final char LF = '\n';
   public static final char CR = '\r';

   public CharUtils() {
   }

   /** @deprecated */
   @Deprecated
   public static Character toCharacterObject(char var0) {
      return Character.valueOf(var0);
   }

   public static Character toCharacterObject(String var0) {
      return StringUtils.isEmpty(var0)?null:Character.valueOf(var0.charAt(0));
   }

   public static char toChar(Character var0) {
      if(var0 == null) {
         throw new IllegalArgumentException("The Character must not be null");
      } else {
         return var0.charValue();
      }
   }

   public static char toChar(Character var0, char var1) {
      return var0 == null?var1:var0.charValue();
   }

   public static char toChar(String var0) {
      if(StringUtils.isEmpty(var0)) {
         throw new IllegalArgumentException("The String must not be empty");
      } else {
         return var0.charAt(0);
      }
   }

   public static char toChar(String var0, char var1) {
      return StringUtils.isEmpty(var0)?var1:var0.charAt(0);
   }

   public static int toIntValue(char var0) {
      if(!isAsciiNumeric(var0)) {
         throw new IllegalArgumentException("The character " + var0 + " is not in the range \'0\' - \'9\'");
      } else {
         return var0 - 48;
      }
   }

   public static int toIntValue(char var0, int var1) {
      return !isAsciiNumeric(var0)?var1:var0 - 48;
   }

   public static int toIntValue(Character var0) {
      if(var0 == null) {
         throw new IllegalArgumentException("The character must not be null");
      } else {
         return toIntValue(var0.charValue());
      }
   }

   public static int toIntValue(Character var0, int var1) {
      return var0 == null?var1:toIntValue(var0.charValue(), var1);
   }

   public static String toString(char var0) {
      return var0 < 128?CHAR_STRING_ARRAY[var0]:new String(new char[]{var0});
   }

   public static String toString(Character var0) {
      return var0 == null?null:toString(var0.charValue());
   }

   public static String unicodeEscaped(char var0) {
      return var0 < 16?"\\u000" + Integer.toHexString(var0):(var0 < 256?"\\u00" + Integer.toHexString(var0):(var0 < 4096?"\\u0" + Integer.toHexString(var0):"\\u" + Integer.toHexString(var0)));
   }

   public static String unicodeEscaped(Character var0) {
      return var0 == null?null:unicodeEscaped(var0.charValue());
   }

   public static boolean isAscii(char var0) {
      return var0 < 128;
   }

   public static boolean isAsciiPrintable(char var0) {
      return var0 >= 32 && var0 < 127;
   }

   public static boolean isAsciiControl(char var0) {
      return var0 < 32 || var0 == 127;
   }

   public static boolean isAsciiAlpha(char var0) {
      return isAsciiAlphaUpper(var0) || isAsciiAlphaLower(var0);
   }

   public static boolean isAsciiAlphaUpper(char var0) {
      return var0 >= 65 && var0 <= 90;
   }

   public static boolean isAsciiAlphaLower(char var0) {
      return var0 >= 97 && var0 <= 122;
   }

   public static boolean isAsciiNumeric(char var0) {
      return var0 >= 48 && var0 <= 57;
   }

   public static boolean isAsciiAlphanumeric(char var0) {
      return isAsciiAlpha(var0) || isAsciiNumeric(var0);
   }

   static {
      for(char var0 = 0; var0 < CHAR_STRING_ARRAY.length; ++var0) {
         CHAR_STRING_ARRAY[var0] = String.valueOf(var0);
      }

   }
}
