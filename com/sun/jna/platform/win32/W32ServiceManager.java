package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.W32Service;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Winsvc;

public class W32ServiceManager {
   Winsvc.SC_HANDLE _handle = null;
   String _machineName = null;
   String _databaseName = null;

   public W32ServiceManager() {
   }

   public W32ServiceManager(String var1, String var2) {
      this._machineName = var1;
      this._databaseName = var2;
   }

   public void open(int var1) {
      this.close();
      this._handle = Advapi32.INSTANCE.OpenSCManager(this._machineName, this._databaseName, var1);
      if(this._handle == null) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
   }

   public void close() {
      if(this._handle != null) {
         if(!Advapi32.INSTANCE.CloseServiceHandle(this._handle)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         }

         this._handle = null;
      }

   }

   public W32Service openService(String var1, int var2) {
      Winsvc.SC_HANDLE var3 = Advapi32.INSTANCE.OpenService(this._handle, var1, var2);
      if(var3 == null) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      } else {
         return new W32Service(var3);
      }
   }

   public Winsvc.SC_HANDLE getHandle() {
      return this._handle;
   }
}
