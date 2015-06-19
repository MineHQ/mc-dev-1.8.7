package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Winsvc;
import com.sun.jna.ptr.IntByReference;

public class W32Service {
   Winsvc.SC_HANDLE _handle = null;

   public W32Service(Winsvc.SC_HANDLE var1) {
      this._handle = var1;
   }

   public void close() {
      if(this._handle != null) {
         if(!Advapi32.INSTANCE.CloseServiceHandle(this._handle)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         }

         this._handle = null;
      }

   }

   public Winsvc.SERVICE_STATUS_PROCESS queryStatus() {
      IntByReference var1 = new IntByReference();
      Advapi32.INSTANCE.QueryServiceStatusEx(this._handle, 0, (Winsvc.SERVICE_STATUS_PROCESS)null, 0, var1);
      Winsvc.SERVICE_STATUS_PROCESS var2 = new Winsvc.SERVICE_STATUS_PROCESS(var1.getValue());
      if(!Advapi32.INSTANCE.QueryServiceStatusEx(this._handle, 0, var2, var2.size(), var1)) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      } else {
         return var2;
      }
   }

   public void startService() {
      this.waitForNonPendingState();
      if(this.queryStatus().dwCurrentState != 4) {
         if(!Advapi32.INSTANCE.StartService(this._handle, 0, (String[])null)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         } else {
            this.waitForNonPendingState();
            if(this.queryStatus().dwCurrentState != 4) {
               throw new RuntimeException("Unable to start the service");
            }
         }
      }
   }

   public void stopService() {
      this.waitForNonPendingState();
      if(this.queryStatus().dwCurrentState != 1) {
         if(!Advapi32.INSTANCE.ControlService(this._handle, 1, new Winsvc.SERVICE_STATUS())) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         } else {
            this.waitForNonPendingState();
            if(this.queryStatus().dwCurrentState != 1) {
               throw new RuntimeException("Unable to stop the service");
            }
         }
      }
   }

   public void continueService() {
      this.waitForNonPendingState();
      if(this.queryStatus().dwCurrentState != 4) {
         if(!Advapi32.INSTANCE.ControlService(this._handle, 3, new Winsvc.SERVICE_STATUS())) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         } else {
            this.waitForNonPendingState();
            if(this.queryStatus().dwCurrentState != 4) {
               throw new RuntimeException("Unable to continue the service");
            }
         }
      }
   }

   public void pauseService() {
      this.waitForNonPendingState();
      if(this.queryStatus().dwCurrentState != 7) {
         if(!Advapi32.INSTANCE.ControlService(this._handle, 2, new Winsvc.SERVICE_STATUS())) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         } else {
            this.waitForNonPendingState();
            if(this.queryStatus().dwCurrentState != 7) {
               throw new RuntimeException("Unable to pause the service");
            }
         }
      }
   }

   public void waitForNonPendingState() {
      Winsvc.SERVICE_STATUS_PROCESS var1 = this.queryStatus();
      int var2 = var1.dwCheckPoint;

      for(int var3 = Kernel32.INSTANCE.GetTickCount(); this.isPendingState(var1.dwCurrentState); var1 = this.queryStatus()) {
         if(var1.dwCheckPoint != var2) {
            var2 = var1.dwCheckPoint;
            var3 = Kernel32.INSTANCE.GetTickCount();
         }

         if(Kernel32.INSTANCE.GetTickCount() - var3 > var1.dwWaitHint) {
            throw new RuntimeException("Timeout waiting for service to change to a non-pending state.");
         }

         int var4 = var1.dwWaitHint / 10;
         if(var4 < 1000) {
            var4 = 1000;
         } else if(var4 > 10000) {
            var4 = 10000;
         }

         try {
            Thread.sleep((long)var4);
         } catch (InterruptedException var6) {
            throw new RuntimeException(var6);
         }
      }

   }

   private boolean isPendingState(int var1) {
      switch(var1) {
      case 2:
      case 3:
      case 5:
      case 6:
         return true;
      case 4:
      default:
         return false;
      }
   }

   public Winsvc.SC_HANDLE getHandle() {
      return this._handle;
   }
}
