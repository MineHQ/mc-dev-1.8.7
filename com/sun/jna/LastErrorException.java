package com.sun.jna;

import com.sun.jna.Platform;

public class LastErrorException extends RuntimeException {
   private int errorCode;

   private static String formatMessage(int var0) {
      return Platform.isWindows()?"GetLastError() returned " + var0:"errno was " + var0;
   }

   private static String parseMessage(String var0) {
      try {
         return formatMessage(Integer.parseInt(var0));
      } catch (NumberFormatException var2) {
         return var0;
      }
   }

   public LastErrorException(String var1) {
      super(parseMessage(var1.trim()));

      try {
         if(var1.startsWith("[")) {
            var1 = var1.substring(1, var1.indexOf("]"));
         }

         this.errorCode = Integer.parseInt(var1);
      } catch (NumberFormatException var3) {
         this.errorCode = -1;
      }

   }

   public int getErrorCode() {
      return this.errorCode;
   }

   public LastErrorException(int var1) {
      super(formatMessage(var1));
      this.errorCode = var1;
   }
}
