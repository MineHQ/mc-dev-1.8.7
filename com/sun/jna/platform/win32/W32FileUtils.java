package com.sun.jna.platform.win32;

import com.sun.jna.WString;
import com.sun.jna.platform.FileUtils;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.ShellAPI;
import java.io.File;
import java.io.IOException;

public class W32FileUtils extends FileUtils {
   public W32FileUtils() {
   }

   public boolean hasTrash() {
      return true;
   }

   public void moveToTrash(File[] var1) throws IOException {
      Shell32 var2 = Shell32.INSTANCE;
      ShellAPI.SHFILEOPSTRUCT var3 = new ShellAPI.SHFILEOPSTRUCT();
      var3.wFunc = 3;
      String[] var4 = new String[var1.length];

      int var5;
      for(var5 = 0; var5 < var4.length; ++var5) {
         var4[var5] = var1[var5].getAbsolutePath();
      }

      var3.pFrom = new WString(var3.encodePaths(var4));
      var3.fFlags = 1620;
      var5 = var2.SHFileOperation(var3);
      if(var5 != 0) {
         throw new IOException("Move to trash failed: " + var3.pFrom + ": " + Kernel32Util.formatMessageFromLastErrorCode(var5));
      } else if(var3.fAnyOperationsAborted) {
         throw new IOException("Move to trash aborted");
      }
   }
}
