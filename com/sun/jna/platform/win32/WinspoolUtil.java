package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Winspool;
import com.sun.jna.ptr.IntByReference;

public abstract class WinspoolUtil {
   public WinspoolUtil() {
   }

   public static Winspool.PRINTER_INFO_1[] getPrinterInfo1() {
      IntByReference var0 = new IntByReference();
      IntByReference var1 = new IntByReference();
      Winspool.INSTANCE.EnumPrinters(2, (String)null, 1, (Pointer)null, 0, var0, var1);
      if(var0.getValue() <= 0) {
         return new Winspool.PRINTER_INFO_1[0];
      } else {
         Winspool.PRINTER_INFO_1 var2 = new Winspool.PRINTER_INFO_1(var0.getValue());
         if(!Winspool.INSTANCE.EnumPrinters(2, (String)null, 1, var2.getPointer(), var0.getValue(), var0, var1)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         } else {
            return (Winspool.PRINTER_INFO_1[])((Winspool.PRINTER_INFO_1[])var2.toArray(var1.getValue()));
         }
      }
   }

   public static Winspool.PRINTER_INFO_4[] getPrinterInfo4() {
      IntByReference var0 = new IntByReference();
      IntByReference var1 = new IntByReference();
      Winspool.INSTANCE.EnumPrinters(2, (String)null, 4, (Pointer)null, 0, var0, var1);
      if(var0.getValue() <= 0) {
         return new Winspool.PRINTER_INFO_4[0];
      } else {
         Winspool.PRINTER_INFO_4 var2 = new Winspool.PRINTER_INFO_4(var0.getValue());
         if(!Winspool.INSTANCE.EnumPrinters(2, (String)null, 4, var2.getPointer(), var0.getValue(), var0, var1)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         } else {
            return (Winspool.PRINTER_INFO_4[])((Winspool.PRINTER_INFO_4[])var2.toArray(var1.getValue()));
         }
      }
   }
}
