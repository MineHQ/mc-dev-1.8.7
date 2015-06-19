package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Crypt32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinCrypt;
import com.sun.jna.ptr.PointerByReference;

public abstract class Crypt32Util {
   public Crypt32Util() {
   }

   public static byte[] cryptProtectData(byte[] var0) {
      return cryptProtectData(var0, 0);
   }

   public static byte[] cryptProtectData(byte[] var0, int var1) {
      return cryptProtectData(var0, (byte[])null, var1, "", (WinCrypt.CRYPTPROTECT_PROMPTSTRUCT)null);
   }

   public static byte[] cryptProtectData(byte[] var0, byte[] var1, int var2, String var3, WinCrypt.CRYPTPROTECT_PROMPTSTRUCT var4) {
      WinCrypt.DATA_BLOB var5 = new WinCrypt.DATA_BLOB(var0);
      WinCrypt.DATA_BLOB var6 = new WinCrypt.DATA_BLOB();
      WinCrypt.DATA_BLOB var7 = var1 == null?null:new WinCrypt.DATA_BLOB(var1);

      byte[] var8;
      try {
         if(!Crypt32.INSTANCE.CryptProtectData(var5, var3, var7, (Pointer)null, var4, var2, var6)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         }

         var8 = var6.getData();
      } finally {
         if(var6.pbData != null) {
            Kernel32.INSTANCE.LocalFree(var6.pbData);
         }

      }

      return var8;
   }

   public static byte[] cryptUnprotectData(byte[] var0) {
      return cryptUnprotectData(var0, 0);
   }

   public static byte[] cryptUnprotectData(byte[] var0, int var1) {
      return cryptUnprotectData(var0, (byte[])null, var1, (WinCrypt.CRYPTPROTECT_PROMPTSTRUCT)null);
   }

   public static byte[] cryptUnprotectData(byte[] var0, byte[] var1, int var2, WinCrypt.CRYPTPROTECT_PROMPTSTRUCT var3) {
      WinCrypt.DATA_BLOB var4 = new WinCrypt.DATA_BLOB(var0);
      WinCrypt.DATA_BLOB var5 = new WinCrypt.DATA_BLOB();
      WinCrypt.DATA_BLOB var6 = var1 == null?null:new WinCrypt.DATA_BLOB(var1);
      PointerByReference var7 = new PointerByReference();

      byte[] var8;
      try {
         if(!Crypt32.INSTANCE.CryptUnprotectData(var4, var7, var6, (Pointer)null, var3, var2, var5)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         }

         var8 = var5.getData();
      } finally {
         if(var5.pbData != null) {
            Kernel32.INSTANCE.LocalFree(var5.pbData);
         }

         if(var7.getValue() != null) {
            Kernel32.INSTANCE.LocalFree(var7.getValue());
         }

      }

      return var8;
   }
}
