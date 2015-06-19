package com.sun.jna.platform;

import com.sun.jna.platform.mac.MacFileUtils;
import com.sun.jna.platform.win32.W32FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class FileUtils {
   public FileUtils() {
   }

   public boolean hasTrash() {
      return false;
   }

   public abstract void moveToTrash(File[] var1) throws IOException;

   public static FileUtils getInstance() {
      return FileUtils.Holder.INSTANCE;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class DefaultFileUtils extends FileUtils {
      private DefaultFileUtils() {
      }

      private File getTrashDirectory() {
         File var1 = new File(System.getProperty("user.home"));
         File var2 = new File(var1, ".Trash");
         if(!var2.exists()) {
            var2 = new File(var1, "Trash");
            if(!var2.exists()) {
               File var3 = new File(var1, "Desktop");
               if(var3.exists()) {
                  var2 = new File(var3, ".Trash");
                  if(!var2.exists()) {
                     var2 = new File(var3, "Trash");
                     if(!var2.exists()) {
                        var2 = new File(System.getProperty("fileutils.trash", "Trash"));
                     }
                  }
               }
            }
         }

         return var2;
      }

      public boolean hasTrash() {
         return this.getTrashDirectory().exists();
      }

      public void moveToTrash(File[] var1) throws IOException {
         File var2 = this.getTrashDirectory();
         if(!var2.exists()) {
            throw new IOException("No trash location found (define fileutils.trash to be the path to the trash)");
         } else {
            ArrayList var3 = new ArrayList();

            for(int var4 = 0; var4 < var1.length; ++var4) {
               File var5 = var1[var4];
               File var6 = new File(var2, var5.getName());
               if(!var5.renameTo(var6)) {
                  var3.add(var5);
               }
            }

            if(var3.size() > 0) {
               throw new IOException("The following files could not be trashed: " + var3);
            }
         }
      }

      // $FF: synthetic method
      DefaultFileUtils(FileUtils.SyntheticClass_1 var1) {
         this();
      }
   }

   private static class Holder {
      public static final FileUtils INSTANCE;

      private Holder() {
      }

      static {
         String var0 = System.getProperty("os.name");
         if(var0.startsWith("Windows")) {
            INSTANCE = new W32FileUtils();
         } else if(var0.startsWith("Mac")) {
            INSTANCE = new MacFileUtils();
         } else {
            INSTANCE = new FileUtils.DefaultFileUtils();
         }

      }
   }
}
