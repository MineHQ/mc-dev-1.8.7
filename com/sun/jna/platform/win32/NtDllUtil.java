package com.sun.jna.platform.win32;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.NtDll;
import com.sun.jna.platform.win32.Wdm;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.ptr.IntByReference;

public abstract class NtDllUtil {
   public NtDllUtil() {
   }

   public static String getKeyName(WinReg.HKEY var0) {
      IntByReference var1 = new IntByReference();
      int var2 = NtDll.INSTANCE.ZwQueryKey(var0, 0, (Structure)null, 0, var1);
      if(var2 == -1073741789 && var1.getValue() > 0) {
         Wdm.KEY_BASIC_INFORMATION var3 = new Wdm.KEY_BASIC_INFORMATION(var1.getValue());
         var2 = NtDll.INSTANCE.ZwQueryKey(var0, 0, var3, var1.getValue(), var1);
         if(var2 != 0) {
            throw new Win32Exception(var2);
         } else {
            return var3.getName();
         }
      } else {
         throw new Win32Exception(var2);
      }
   }
}
