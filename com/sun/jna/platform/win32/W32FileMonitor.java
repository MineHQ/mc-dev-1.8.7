package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.platform.FileMonitor;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class W32FileMonitor extends FileMonitor {
   private static final int BUFFER_SIZE = 4096;
   private Thread watcher;
   private WinNT.HANDLE port;
   private final Map<File, W32FileMonitor.FileInfo> fileMap = new HashMap();
   private final Map<WinNT.HANDLE, W32FileMonitor.FileInfo> handleMap = new HashMap();
   private boolean disposing = false;
   private static int watcherThreadID;

   public W32FileMonitor() {
   }

   private void handleChanges(W32FileMonitor.FileInfo var1) throws IOException {
      Kernel32 var2 = Kernel32.INSTANCE;
      WinNT.FILE_NOTIFY_INFORMATION var3 = var1.info;
      var3.read();

      do {
         FileMonitor.FileEvent var4 = null;
         File var5 = new File(var1.file, var3.getFilename());
         switch(var3.Action) {
         case 0:
            break;
         case 1:
            var4 = new FileMonitor.FileEvent(var5, 1);
            break;
         case 2:
            var4 = new FileMonitor.FileEvent(var5, 2);
            break;
         case 3:
            var4 = new FileMonitor.FileEvent(var5, 4);
            break;
         case 4:
            var4 = new FileMonitor.FileEvent(var5, 16);
            break;
         case 5:
            var4 = new FileMonitor.FileEvent(var5, 32);
            break;
         default:
            System.err.println("Unrecognized file action \'" + var3.Action + "\'");
         }

         if(var4 != null) {
            this.notify(var4);
         }

         var3 = var3.next();
      } while(var3 != null);

      if(!var1.file.exists()) {
         this.unwatch(var1.file);
      } else if(!var2.ReadDirectoryChangesW(var1.handle, var1.info, var1.info.size(), var1.recursive, var1.notifyMask, var1.infoLength, var1.overlapped, (WinNT.OVERLAPPED_COMPLETION_ROUTINE)null) && !this.disposing) {
         int var6 = var2.GetLastError();
         throw new IOException("ReadDirectoryChangesW failed on " + var1.file + ": \'" + Kernel32Util.formatMessageFromLastErrorCode(var6) + "\' (" + var6 + ")");
      }
   }

   private W32FileMonitor.FileInfo waitForChange() {
      Kernel32 var1 = Kernel32.INSTANCE;
      IntByReference var2 = new IntByReference();
      BaseTSD.ULONG_PTRByReference var3 = new BaseTSD.ULONG_PTRByReference();
      PointerByReference var4 = new PointerByReference();
      var1.GetQueuedCompletionStatus(this.port, var2, var3, var4, -1);
      synchronized(this) {
         return (W32FileMonitor.FileInfo)this.handleMap.get(var3.getValue());
      }
   }

   private int convertMask(int var1) {
      int var2 = 0;
      if((var1 & 1) != 0) {
         var2 |= 64;
      }

      if((var1 & 2) != 0) {
         var2 |= 3;
      }

      if((var1 & 4) != 0) {
         var2 |= 16;
      }

      if((var1 & 48) != 0) {
         var2 |= 3;
      }

      if((var1 & 64) != 0) {
         var2 |= 8;
      }

      if((var1 & 8) != 0) {
         var2 |= 32;
      }

      if((var1 & 128) != 0) {
         var2 |= 4;
      }

      if((var1 & 256) != 0) {
         var2 |= 256;
      }

      return var2;
   }

   protected synchronized void watch(File var1, int var2, boolean var3) throws IOException {
      File var4 = var1;
      if(!var1.isDirectory()) {
         var3 = false;
         var4 = var1.getParentFile();
      }

      while(var4 != null && !var4.exists()) {
         var3 = true;
         var4 = var4.getParentFile();
      }

      if(var4 == null) {
         throw new FileNotFoundException("No ancestor found for " + var1);
      } else {
         Kernel32 var5 = Kernel32.INSTANCE;
         byte var6 = 7;
         int var7 = 1107296256;
         WinNT.HANDLE var8 = var5.CreateFile(var1.getAbsolutePath(), 1, var6, (WinBase.SECURITY_ATTRIBUTES)null, 3, var7, (WinNT.HANDLE)null);
         if(WinBase.INVALID_HANDLE_VALUE.equals(var8)) {
            throw new IOException("Unable to open " + var1 + " (" + var5.GetLastError() + ")");
         } else {
            int var9 = this.convertMask(var2);
            W32FileMonitor.FileInfo var10 = new W32FileMonitor.FileInfo(var1, var8, var9, var3);
            this.fileMap.put(var1, var10);
            this.handleMap.put(var8, var10);
            this.port = var5.CreateIoCompletionPort(var8, this.port, var8.getPointer(), 0);
            if(WinBase.INVALID_HANDLE_VALUE.equals(this.port)) {
               throw new IOException("Unable to create/use I/O Completion port for " + var1 + " (" + var5.GetLastError() + ")");
            } else if(!var5.ReadDirectoryChangesW(var8, var10.info, var10.info.size(), var3, var9, var10.infoLength, var10.overlapped, (WinNT.OVERLAPPED_COMPLETION_ROUTINE)null)) {
               int var11 = var5.GetLastError();
               throw new IOException("ReadDirectoryChangesW failed on " + var10.file + ", handle " + var8 + ": \'" + Kernel32Util.formatMessageFromLastErrorCode(var11) + "\' (" + var11 + ")");
            } else {
               if(this.watcher == null) {
                  this.watcher = new Thread("W32 File Monitor-" + watcherThreadID++) {
                     public void run() {
                        while(true) {
                           W32FileMonitor.FileInfo var1 = W32FileMonitor.this.waitForChange();
                           if(var1 == null) {
                              W32FileMonitor var2 = W32FileMonitor.this;
                              synchronized(W32FileMonitor.this) {
                                 if(W32FileMonitor.this.fileMap.isEmpty()) {
                                    W32FileMonitor.this.watcher = null;
                                    return;
                                 }
                              }
                           } else {
                              try {
                                 W32FileMonitor.this.handleChanges(var1);
                              } catch (IOException var5) {
                                 var5.printStackTrace();
                              }
                           }
                        }
                     }
                  };
                  this.watcher.setDaemon(true);
                  this.watcher.start();
               }

            }
         }
      }
   }

   protected synchronized void unwatch(File var1) {
      W32FileMonitor.FileInfo var2 = (W32FileMonitor.FileInfo)this.fileMap.remove(var1);
      if(var2 != null) {
         this.handleMap.remove(var2.handle);
         Kernel32 var3 = Kernel32.INSTANCE;
         var3.CloseHandle(var2.handle);
      }

   }

   public synchronized void dispose() {
      this.disposing = true;
      int var1 = 0;
      Object[] var2 = this.fileMap.keySet().toArray();

      while(!this.fileMap.isEmpty()) {
         this.unwatch((File)var2[var1++]);
      }

      Kernel32 var3 = Kernel32.INSTANCE;
      var3.PostQueuedCompletionStatus(this.port, 0, (Pointer)null, (WinBase.OVERLAPPED)null);
      var3.CloseHandle(this.port);
      this.port = null;
      this.watcher = null;
   }

   private class FileInfo {
      public final File file;
      public final WinNT.HANDLE handle;
      public final int notifyMask;
      public final boolean recursive;
      public final WinNT.FILE_NOTIFY_INFORMATION info = new WinNT.FILE_NOTIFY_INFORMATION(4096);
      public final IntByReference infoLength = new IntByReference();
      public final WinBase.OVERLAPPED overlapped = new WinBase.OVERLAPPED();

      public FileInfo(File var2, WinNT.HANDLE var3, int var4, boolean var5) {
         this.file = var2;
         this.handle = var3;
         this.notifyMask = var4;
         this.recursive = var5;
      }
   }
}
