package org.apache.logging.log4j.core.helpers;

public final class Assert {
   private Assert() {
   }

   public static <T> T isNotNull(T var0, String var1) {
      if(var0 == null) {
         throw new NullPointerException(var1 + " is null");
      } else {
         return var0;
      }
   }
}
