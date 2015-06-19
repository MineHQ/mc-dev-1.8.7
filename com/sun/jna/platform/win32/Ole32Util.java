package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.WinNT;

public abstract class Ole32Util {
   public Ole32Util() {
   }

   public static Guid.GUID getGUIDFromString(String var0) {
      Guid.GUID.GUID$ByReference var1 = new Guid.GUID.GUID$ByReference();
      WinNT.HRESULT var2 = Ole32.INSTANCE.IIDFromString(var0, var1);
      if(!var2.equals(W32Errors.S_OK)) {
         throw new RuntimeException(var2.toString());
      } else {
         return var1;
      }
   }

   public static String getStringFromGUID(Guid.GUID var0) {
      Guid.GUID.GUID$ByReference var1 = new Guid.GUID.GUID$ByReference(var0.getPointer());
      byte var2 = 39;
      char[] var3 = new char[var2];
      int var4 = Ole32.INSTANCE.StringFromGUID2(var1, var3, var2);
      if(var4 == 0) {
         throw new RuntimeException("StringFromGUID2");
      } else {
         var3[var4 - 1] = 0;
         return Native.toString(var3);
      }
   }

   public static Guid.GUID generateGUID() {
      Guid.GUID.GUID$ByReference var0 = new Guid.GUID.GUID$ByReference();
      WinNT.HRESULT var1 = Ole32.INSTANCE.CoCreateGuid(var0);
      if(!var1.equals(W32Errors.S_OK)) {
         throw new RuntimeException(var1.toString());
      } else {
         return var0;
      }
   }
}
