package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.ShlObj;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

public abstract class Shell32Util {
   public Shell32Util() {
   }

   public static String getFolderPath(WinDef.HWND var0, int var1, WinDef.DWORD var2) {
      char[] var3 = new char[260];
      WinNT.HRESULT var4 = Shell32.INSTANCE.SHGetFolderPath(var0, var1, (WinNT.HANDLE)null, var2, var3);
      if(!var4.equals(W32Errors.S_OK)) {
         throw new Win32Exception(var4);
      } else {
         return Native.toString(var3);
      }
   }

   public static String getFolderPath(int var0) {
      return getFolderPath((WinDef.HWND)null, var0, ShlObj.SHGFP_TYPE_CURRENT);
   }
}
