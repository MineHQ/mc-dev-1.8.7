package io.netty.util.internal;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

public final class NativeLibraryLoader {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NativeLibraryLoader.class);
   private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
   private static final String OSNAME;
   private static final File WORKDIR;

   private static File tmpdir() {
      File var0;
      try {
         var0 = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
         if(var0 != null) {
            logger.debug("-Dio.netty.tmpdir: " + var0);
            return var0;
         }

         var0 = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
         if(var0 != null) {
            logger.debug("-Dio.netty.tmpdir: " + var0 + " (java.io.tmpdir)");
            return var0;
         }

         if(isWindows()) {
            var0 = toDirectory(System.getenv("TEMP"));
            if(var0 != null) {
               logger.debug("-Dio.netty.tmpdir: " + var0 + " (%TEMP%)");
               return var0;
            }

            String var1 = System.getenv("USERPROFILE");
            if(var1 != null) {
               var0 = toDirectory(var1 + "\\AppData\\Local\\Temp");
               if(var0 != null) {
                  logger.debug("-Dio.netty.tmpdir: " + var0 + " (%USERPROFILE%\\AppData\\Local\\Temp)");
                  return var0;
               }

               var0 = toDirectory(var1 + "\\Local Settings\\Temp");
               if(var0 != null) {
                  logger.debug("-Dio.netty.tmpdir: " + var0 + " (%USERPROFILE%\\Local Settings\\Temp)");
                  return var0;
               }
            }
         } else {
            var0 = toDirectory(System.getenv("TMPDIR"));
            if(var0 != null) {
               logger.debug("-Dio.netty.tmpdir: " + var0 + " ($TMPDIR)");
               return var0;
            }
         }
      } catch (Exception var2) {
         ;
      }

      if(isWindows()) {
         var0 = new File("C:\\Windows\\Temp");
      } else {
         var0 = new File("/tmp");
      }

      logger.warn("Failed to get the temporary directory; falling back to: " + var0);
      return var0;
   }

   private static File toDirectory(String var0) {
      if(var0 == null) {
         return null;
      } else {
         File var1 = new File(var0);
         var1.mkdirs();
         if(!var1.isDirectory()) {
            return null;
         } else {
            try {
               return var1.getAbsoluteFile();
            } catch (Exception var3) {
               return var1;
            }
         }
      }
   }

   private static boolean isWindows() {
      return OSNAME.startsWith("windows");
   }

   private static boolean isOSX() {
      return OSNAME.startsWith("macosx") || OSNAME.startsWith("osx");
   }

   public static void load(String var0, ClassLoader var1) {
      String var2 = System.mapLibraryName(var0);
      String var3 = "META-INF/native/" + var2;
      URL var4 = var1.getResource(var3);
      if(var4 == null && isOSX()) {
         if(var3.endsWith(".jnilib")) {
            var4 = var1.getResource("META-INF/native/lib" + var0 + ".dynlib");
         } else {
            var4 = var1.getResource("META-INF/native/lib" + var0 + ".jnilib");
         }
      }

      if(var4 == null) {
         System.loadLibrary(var0);
      } else {
         int var5 = var2.lastIndexOf(46);
         String var6 = var2.substring(0, var5);
         String var7 = var2.substring(var5, var2.length());
         InputStream var8 = null;
         FileOutputStream var9 = null;
         File var10 = null;
         boolean var11 = false;

         try {
            var10 = File.createTempFile(var6, var7, WORKDIR);
            var8 = var4.openStream();
            var9 = new FileOutputStream(var10);
            byte[] var12 = new byte[8192];

            int var13;
            while((var13 = var8.read(var12)) > 0) {
               var9.write(var12, 0, var13);
            }

            var9.flush();
            var9.close();
            var9 = null;
            System.load(var10.getPath());
            var11 = true;
         } catch (Exception var24) {
            throw (UnsatisfiedLinkError)(new UnsatisfiedLinkError("could not load a native library: " + var0)).initCause(var24);
         } finally {
            if(var8 != null) {
               try {
                  var8.close();
               } catch (IOException var23) {
                  ;
               }
            }

            if(var9 != null) {
               try {
                  var9.close();
               } catch (IOException var22) {
                  ;
               }
            }

            if(var10 != null) {
               if(var11) {
                  var10.deleteOnExit();
               } else if(!var10.delete()) {
                  var10.deleteOnExit();
               }
            }

         }
      }
   }

   private NativeLibraryLoader() {
   }

   static {
      OSNAME = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
      String var0 = SystemPropertyUtil.get("io.netty.native.workdir");
      if(var0 != null) {
         File var1 = new File(var0);
         var1.mkdirs();

         try {
            var1 = var1.getAbsoluteFile();
         } catch (Exception var3) {
            ;
         }

         WORKDIR = var1;
         logger.debug("-Dio.netty.netty.workdir: " + WORKDIR);
      } else {
         WORKDIR = tmpdir();
         logger.debug("-Dio.netty.netty.workdir: " + WORKDIR + " (io.netty.tmpdir)");
      }

   }
}
