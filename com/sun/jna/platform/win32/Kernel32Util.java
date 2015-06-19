package com.sun.jna.platform.win32;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;

public abstract class Kernel32Util implements WinDef {
   public Kernel32Util() {
   }

   public static String getComputerName() {
      char[] var0 = new char[WinBase.MAX_COMPUTERNAME_LENGTH + 1];
      IntByReference var1 = new IntByReference(var0.length);
      if(!Kernel32.INSTANCE.GetComputerName(var0, var1)) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      } else {
         return Native.toString(var0);
      }
   }

   public static String formatMessageFromHR(WinNT.HRESULT var0) {
      PointerByReference var1 = new PointerByReference();
      if(0 == Kernel32.INSTANCE.FormatMessage(4864, (Pointer)null, var0.intValue(), 0, var1, 0, (Pointer)null)) {
         throw new LastErrorException(Kernel32.INSTANCE.GetLastError());
      } else {
         String var2 = var1.getValue().getString(0L, !Boolean.getBoolean("w32.ascii"));
         Kernel32.INSTANCE.LocalFree(var1.getValue());
         return var2.trim();
      }
   }

   public static String formatMessageFromLastErrorCode(int var0) {
      return formatMessageFromHR(W32Errors.HRESULT_FROM_WIN32(var0));
   }

   public static String getTempPath() {
      WinDef.DWORD var0 = new WinDef.DWORD(260L);
      char[] var1 = new char[var0.intValue()];
      if(Kernel32.INSTANCE.GetTempPath(var0, var1).intValue() == 0) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      } else {
         return Native.toString(var1);
      }
   }

   public static void deleteFile(String var0) {
      if(!Kernel32.INSTANCE.DeleteFile(var0)) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
   }

   public static String[] getLogicalDriveStrings() {
      WinDef.DWORD var0 = Kernel32.INSTANCE.GetLogicalDriveStrings(new WinDef.DWORD(0L), (char[])null);
      if(var0.intValue() <= 0) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      } else {
         char[] var1 = new char[var0.intValue()];
         var0 = Kernel32.INSTANCE.GetLogicalDriveStrings(var0, var1);
         if(var0.intValue() <= 0) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         } else {
            ArrayList var2 = new ArrayList();
            String var3 = "";

            for(int var4 = 0; var4 < var1.length - 1; ++var4) {
               if(var1[var4] == 0) {
                  var2.add(var3);
                  var3 = "";
               } else {
                  var3 = var3 + var1[var4];
               }
            }

            return (String[])var2.toArray(new String[0]);
         }
      }
   }

   public static int getFileAttributes(String var0) {
      int var1 = Kernel32.INSTANCE.GetFileAttributes(var0);
      if(var1 == -1) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      } else {
         return var1;
      }
   }
}
