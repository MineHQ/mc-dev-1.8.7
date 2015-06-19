package com.sun.jna;

import com.sun.jna.Native;

public final class Platform {
   public static final int UNSPECIFIED = -1;
   public static final int MAC = 0;
   public static final int LINUX = 1;
   public static final int WINDOWS = 2;
   public static final int SOLARIS = 3;
   public static final int FREEBSD = 4;
   public static final int OPENBSD = 5;
   public static final int WINDOWSCE = 6;
   public static final boolean RO_FIELDS;
   public static final boolean HAS_BUFFERS;
   public static final boolean HAS_AWT;
   public static final String MATH_LIBRARY_NAME;
   public static final String C_LIBRARY_NAME;
   private static final int osType;

   private Platform() {
   }

   public static final int getOSType() {
      return osType;
   }

   public static final boolean isMac() {
      return osType == 0;
   }

   public static final boolean isLinux() {
      return osType == 1;
   }

   public static final boolean isWindowsCE() {
      return osType == 6;
   }

   public static final boolean isWindows() {
      return osType == 2 || osType == 6;
   }

   public static final boolean isSolaris() {
      return osType == 3;
   }

   public static final boolean isFreeBSD() {
      return osType == 4;
   }

   public static final boolean isOpenBSD() {
      return osType == 5;
   }

   public static final boolean isX11() {
      return !isWindows() && !isMac();
   }

   public static final boolean hasRuntimeExec() {
      return !isWindowsCE() || !"J9".equals(System.getProperty("java.vm.name"));
   }

   public static final boolean is64Bit() {
      String var0 = System.getProperty("sun.arch.data.model", System.getProperty("com.ibm.vm.bitmode"));
      if(var0 != null) {
         return "64".equals(var0);
      } else {
         String var1 = System.getProperty("os.arch").toLowerCase();
         return !"x86_64".equals(var1) && !"ia64".equals(var1) && !"ppc64".equals(var1) && !"sparcv9".equals(var1) && !"amd64".equals(var1)?Native.POINTER_SIZE == 8:true;
      }
   }

   public static final boolean isIntel() {
      String var0 = System.getProperty("os.arch").toLowerCase().trim();
      return var0.equals("i386") || var0.equals("x86") || var0.equals("x86_64") || var0.equals("amd64");
   }

   public static final boolean isPPC() {
      String var0 = System.getProperty("os.arch").toLowerCase().trim();
      return var0.equals("ppc") || var0.equals("ppc64") || var0.equals("powerpc") || var0.equals("powerpc64");
   }

   public static final boolean isARM() {
      String var0 = System.getProperty("os.arch").toLowerCase().trim();
      return var0.equals("arm");
   }

   static {
      String var0 = System.getProperty("os.name");
      if(var0.startsWith("Linux")) {
         osType = 1;
      } else if(!var0.startsWith("Mac") && !var0.startsWith("Darwin")) {
         if(var0.startsWith("Windows CE")) {
            osType = 6;
         } else if(var0.startsWith("Windows")) {
            osType = 2;
         } else if(!var0.startsWith("Solaris") && !var0.startsWith("SunOS")) {
            if(var0.startsWith("FreeBSD")) {
               osType = 4;
            } else if(var0.startsWith("OpenBSD")) {
               osType = 5;
            } else {
               osType = -1;
            }
         } else {
            osType = 3;
         }
      } else {
         osType = 0;
      }

      boolean var1 = false;

      try {
         Class.forName("java.awt.Component");
         var1 = true;
      } catch (ClassNotFoundException var5) {
         ;
      }

      HAS_AWT = var1;
      boolean var2 = false;

      try {
         Class.forName("java.nio.Buffer");
         var2 = true;
      } catch (ClassNotFoundException var4) {
         ;
      }

      HAS_BUFFERS = var2;
      RO_FIELDS = osType != 6;
      C_LIBRARY_NAME = osType == 2?"msvcrt":(osType == 6?"coredll":"c");
      MATH_LIBRARY_NAME = osType == 2?"msvcrt":(osType == 6?"coredll":"m");
   }
}
