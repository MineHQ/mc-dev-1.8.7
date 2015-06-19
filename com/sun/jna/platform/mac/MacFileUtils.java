package com.sun.jna.platform.mac;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.FileUtils;
import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MacFileUtils extends FileUtils {
   public MacFileUtils() {
   }

   public boolean hasTrash() {
      return true;
   }

   public void moveToTrash(File[] var1) throws IOException {
      File var2 = new File(System.getProperty("user.home"));
      File var3 = new File(var2, ".Trash");
      if(!var3.exists()) {
         throw new IOException("The Trash was not found in its expected location (" + var3 + ")");
      } else {
         ArrayList var4 = new ArrayList();

         for(int var5 = 0; var5 < var1.length; ++var5) {
            File var6 = var1[var5];
            if(MacFileUtils.FileManager.INSTANCE.FSPathMoveObjectToTrashSync(var6.getAbsolutePath(), (PointerByReference)null, 0) != 0) {
               var4.add(var6);
            }
         }

         if(var4.size() > 0) {
            throw new IOException("The following files could not be trashed: " + var4);
         }
      }
   }

   public interface FileManager extends Library {
      int kFSFileOperationDefaultOptions = 0;
      int kFSFileOperationsOverwrite = 1;
      int kFSFileOperationsSkipSourcePermissionErrors = 2;
      int kFSFileOperationsDoNotMoveAcrossVolumes = 4;
      int kFSFileOperationsSkipPreflight = 8;
      MacFileUtils.FileManager INSTANCE = (MacFileUtils.FileManager)Native.loadLibrary("CoreServices", MacFileUtils.FileManager.class);

      int FSPathMoveObjectToTrashSync(String var1, PointerByReference var2, int var3);
   }
}
