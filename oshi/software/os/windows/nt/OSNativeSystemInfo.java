package oshi.software.os.windows.nt;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

public class OSNativeSystemInfo {
   private WinBase.SYSTEM_INFO _si = null;

   public OSNativeSystemInfo() {
      WinBase.SYSTEM_INFO var1 = new WinBase.SYSTEM_INFO();
      Kernel32.INSTANCE.GetSystemInfo(var1);

      try {
         IntByReference var2 = new IntByReference();
         WinNT.HANDLE var3 = Kernel32.INSTANCE.GetCurrentProcess();
         if(Kernel32.INSTANCE.IsWow64Process(var3, var2) && var2.getValue() > 0) {
            Kernel32.INSTANCE.GetNativeSystemInfo(var1);
         }
      } catch (UnsatisfiedLinkError var4) {
         ;
      }

      this._si = var1;
   }

   public OSNativeSystemInfo(WinBase.SYSTEM_INFO var1) {
      this._si = var1;
   }

   public int getNumberOfProcessors() {
      return this._si.dwNumberOfProcessors.intValue();
   }
}
