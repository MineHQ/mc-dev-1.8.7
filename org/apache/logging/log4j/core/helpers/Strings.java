package org.apache.logging.log4j.core.helpers;

public class Strings {
   public Strings() {
   }

   public static boolean isEmpty(CharSequence var0) {
      return var0 == null || var0.length() == 0;
   }

   public static boolean isNotEmpty(CharSequence var0) {
      return !isEmpty(var0);
   }
}
