package com.sun.jna;

import com.sun.jna.Function;
import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class NativeLibrary {
   private long handle;
   private final String libraryName;
   private final String libraryPath;
   private final Map functions = new HashMap();
   final int callFlags;
   final Map options;
   private static final Map libraries = new HashMap();
   private static final Map searchPaths = Collections.synchronizedMap(new HashMap());
   private static final List librarySearchPath = new LinkedList();

   private static String functionKey(String var0, int var1) {
      return var0 + "|" + var1;
   }

   private NativeLibrary(String var1, String var2, long var3, Map var5) {
      this.libraryName = this.getLibraryName(var1);
      this.libraryPath = var2;
      this.handle = var3;
      Object var6 = var5.get("calling-convention");
      int var7 = var6 instanceof Integer?((Integer)var6).intValue():0;
      this.callFlags = var7;
      this.options = var5;
      if(Platform.isWindows() && "kernel32".equals(this.libraryName.toLowerCase())) {
         Map var8 = this.functions;
         synchronized(this.functions) {
            Function var9 = new Function(this, "GetLastError", 1) {
               Object invoke(Object[] var1, Class var2, boolean var3) {
                  return new Integer(Native.getLastError());
               }
            };
            this.functions.put(functionKey("GetLastError", this.callFlags), var9);
         }
      }

   }

   private static NativeLibrary loadLibrary(String var0, Map var1) {
      LinkedList var2 = new LinkedList();
      String var3 = Native.getWebStartLibraryPath(var0);
      if(var3 != null) {
         var2.add(var3);
      }

      List var4 = (List)searchPaths.get(var0);
      if(var4 != null) {
         synchronized(var4) {
            var2.addAll(0, var4);
         }
      }

      var2.addAll(initPaths("jna.library.path"));
      String var5 = findLibraryPath(var0, var2);
      long var6 = 0L;

      try {
         var6 = Native.open(var5);
      } catch (UnsatisfiedLinkError var13) {
         var2.addAll(librarySearchPath);
      }

      try {
         if(var6 == 0L) {
            var5 = findLibraryPath(var0, var2);
            var6 = Native.open(var5);
            if(var6 == 0L) {
               throw new UnsatisfiedLinkError("Failed to load library \'" + var0 + "\'");
            }
         }
      } catch (UnsatisfiedLinkError var15) {
         UnsatisfiedLinkError var8 = var15;
         if(Platform.isLinux()) {
            var5 = matchLibrary(var0, var2);
            if(var5 != null) {
               try {
                  var6 = Native.open(var5);
               } catch (UnsatisfiedLinkError var12) {
                  var8 = var12;
               }
            }
         } else if(Platform.isMac() && !var0.endsWith(".dylib")) {
            var5 = "/System/Library/Frameworks/" + var0 + ".framework/" + var0;
            if((new File(var5)).exists()) {
               try {
                  var6 = Native.open(var5);
               } catch (UnsatisfiedLinkError var11) {
                  var8 = var11;
               }
            }
         } else if(Platform.isWindows()) {
            var5 = findLibraryPath("lib" + var0, var2);

            try {
               var6 = Native.open(var5);
            } catch (UnsatisfiedLinkError var10) {
               var8 = var10;
            }
         }

         if(var6 == 0L) {
            throw new UnsatisfiedLinkError("Unable to load library \'" + var0 + "\': " + var8.getMessage());
         }
      }

      return new NativeLibrary(var0, var5, var6, var1);
   }

   private String getLibraryName(String var1) {
      String var2 = var1;
      String var3 = "---";
      String var4 = mapLibraryName("---");
      int var5 = var4.indexOf("---");
      if(var5 > 0 && var1.startsWith(var4.substring(0, var5))) {
         var2 = var1.substring(var5);
      }

      String var6 = var4.substring(var5 + "---".length());
      int var7 = var2.indexOf(var6);
      if(var7 != -1) {
         var2 = var2.substring(0, var7);
      }

      return var2;
   }

   public static final NativeLibrary getInstance(String var0) {
      return getInstance(var0, Collections.EMPTY_MAP);
   }

   public static final NativeLibrary getInstance(String var0, Map var1) {
      HashMap var8 = new HashMap(var1);
      if(var8.get("calling-convention") == null) {
         var8.put("calling-convention", new Integer(0));
      }

      if(Platform.isLinux() && "c".equals(var0)) {
         var0 = null;
      }

      Map var2 = libraries;
      synchronized(libraries) {
         WeakReference var3 = (WeakReference)libraries.get(var0 + var8);
         NativeLibrary var4 = var3 != null?(NativeLibrary)var3.get():null;
         if(var4 == null) {
            if(var0 == null) {
               var4 = new NativeLibrary("<process>", (String)null, Native.open((String)null), var8);
            } else {
               var4 = loadLibrary(var0, var8);
            }

            var3 = new WeakReference(var4);
            libraries.put(var4.getName() + var8, var3);
            File var5 = var4.getFile();
            if(var5 != null) {
               libraries.put(var5.getAbsolutePath() + var8, var3);
               libraries.put(var5.getName() + var8, var3);
            }
         }

         return var4;
      }
   }

   public static final synchronized NativeLibrary getProcess() {
      return getInstance((String)null);
   }

   public static final synchronized NativeLibrary getProcess(Map var0) {
      return getInstance((String)null, var0);
   }

   public static final void addSearchPath(String var0, String var1) {
      Map var2 = searchPaths;
      synchronized(searchPaths) {
         List var3 = (List)searchPaths.get(var0);
         if(var3 == null) {
            var3 = Collections.synchronizedList(new LinkedList());
            searchPaths.put(var0, var3);
         }

         var3.add(var1);
      }
   }

   public Function getFunction(String var1) {
      return this.getFunction(var1, this.callFlags);
   }

   Function getFunction(String var1, Method var2) {
      int var3 = this.callFlags;
      Class[] var4 = var2.getExceptionTypes();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if(LastErrorException.class.isAssignableFrom(var4[var5])) {
            var3 |= 4;
         }
      }

      return this.getFunction(var1, var3);
   }

   public Function getFunction(String var1, int var2) {
      if(var1 == null) {
         throw new NullPointerException("Function name may not be null");
      } else {
         Map var3 = this.functions;
         synchronized(this.functions) {
            String var4 = functionKey(var1, var2);
            Function var5 = (Function)this.functions.get(var4);
            if(var5 == null) {
               var5 = new Function(this, var1, var2);
               this.functions.put(var4, var5);
            }

            return var5;
         }
      }
   }

   public Map getOptions() {
      return this.options;
   }

   public Pointer getGlobalVariableAddress(String var1) {
      try {
         return new Pointer(this.getSymbolAddress(var1));
      } catch (UnsatisfiedLinkError var3) {
         throw new UnsatisfiedLinkError("Error looking up \'" + var1 + "\': " + var3.getMessage());
      }
   }

   long getSymbolAddress(String var1) {
      if(this.handle == 0L) {
         throw new UnsatisfiedLinkError("Library has been unloaded");
      } else {
         return Native.findSymbol(this.handle, var1);
      }
   }

   public String toString() {
      return "Native Library <" + this.libraryPath + "@" + this.handle + ">";
   }

   public String getName() {
      return this.libraryName;
   }

   public File getFile() {
      return this.libraryPath == null?null:new File(this.libraryPath);
   }

   protected void finalize() {
      this.dispose();
   }

   static void disposeAll() {
      Map var1 = libraries;
      HashSet var0;
      synchronized(libraries) {
         var0 = new HashSet(libraries.values());
      }

      Iterator var5 = var0.iterator();

      while(var5.hasNext()) {
         WeakReference var2 = (WeakReference)var5.next();
         NativeLibrary var3 = (NativeLibrary)var2.get();
         if(var3 != null) {
            var3.dispose();
         }
      }

   }

   public void dispose() {
      Map var1 = libraries;
      synchronized(libraries) {
         Iterator var2 = libraries.values().iterator();

         while(var2.hasNext()) {
            WeakReference var3 = (WeakReference)var2.next();
            if(var3.get() == this) {
               var2.remove();
            }
         }
      }

      synchronized(this) {
         if(this.handle != 0L) {
            Native.close(this.handle);
            this.handle = 0L;
         }

      }
   }

   private static List initPaths(String var0) {
      String var1 = System.getProperty(var0, "");
      if("".equals(var1)) {
         return Collections.EMPTY_LIST;
      } else {
         StringTokenizer var2 = new StringTokenizer(var1, File.pathSeparator);
         ArrayList var3 = new ArrayList();

         while(var2.hasMoreTokens()) {
            String var4 = var2.nextToken();
            if(!"".equals(var4)) {
               var3.add(var4);
            }
         }

         return var3;
      }
   }

   private static String findLibraryPath(String var0, List var1) {
      if((new File(var0)).isAbsolute()) {
         return var0;
      } else {
         String var2 = mapLibraryName(var0);
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            File var5 = new File(var4, var2);
            if(var5.exists()) {
               return var5.getAbsolutePath();
            }

            if(Platform.isMac() && var2.endsWith(".dylib")) {
               var5 = new File(var4, var2.substring(0, var2.lastIndexOf(".dylib")) + ".jnilib");
               if(var5.exists()) {
                  return var5.getAbsolutePath();
               }
            }
         }

         return var2;
      }
   }

   private static String mapLibraryName(String var0) {
      if(Platform.isMac()) {
         if(!var0.startsWith("lib") || !var0.endsWith(".dylib") && !var0.endsWith(".jnilib")) {
            String var1 = System.mapLibraryName(var0);
            return var1.endsWith(".jnilib")?var1.substring(0, var1.lastIndexOf(".jnilib")) + ".dylib":var1;
         } else {
            return var0;
         }
      } else {
         if(Platform.isLinux()) {
            if(isVersionedName(var0) || var0.endsWith(".so")) {
               return var0;
            }
         } else if(Platform.isWindows() && (var0.endsWith(".drv") || var0.endsWith(".dll"))) {
            return var0;
         }

         return System.mapLibraryName(var0);
      }
   }

   private static boolean isVersionedName(String var0) {
      if(var0.startsWith("lib")) {
         int var1 = var0.lastIndexOf(".so.");
         if(var1 != -1 && var1 + 4 < var0.length()) {
            for(int var2 = var1 + 4; var2 < var0.length(); ++var2) {
               char var3 = var0.charAt(var2);
               if(!Character.isDigit(var3) && var3 != 46) {
                  return false;
               }
            }

            return true;
         }
      }

      return false;
   }

   static String matchLibrary(final String var0, List var1) {
      File var2 = new File(var0);
      if(var2.isAbsolute()) {
         var1 = Arrays.asList(new String[]{var2.getParent()});
      }

      FilenameFilter var3 = new FilenameFilter() {
         public boolean accept(File var1, String var2) {
            return (var2.startsWith("lib" + var0 + ".so") || var2.startsWith(var0 + ".so") && var0.startsWith("lib")) && NativeLibrary.isVersionedName(var2);
         }
      };
      LinkedList var4 = new LinkedList();
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         File[] var6 = (new File((String)var5.next())).listFiles(var3);
         if(var6 != null && var6.length > 0) {
            var4.addAll(Arrays.asList(var6));
         }
      }

      double var13 = -1.0D;
      String var7 = null;
      Iterator var8 = var4.iterator();

      while(var8.hasNext()) {
         String var9 = ((File)var8.next()).getAbsolutePath();
         String var10 = var9.substring(var9.lastIndexOf(".so.") + 4);
         double var11 = parseVersion(var10);
         if(var11 > var13) {
            var13 = var11;
            var7 = var9;
         }
      }

      return var7;
   }

   static double parseVersion(String var0) {
      double var1 = 0.0D;
      double var3 = 1.0D;

      for(int var5 = var0.indexOf("."); var0 != null; var3 *= 100.0D) {
         String var6;
         if(var5 != -1) {
            var6 = var0.substring(0, var5);
            var0 = var0.substring(var5 + 1);
            var5 = var0.indexOf(".");
         } else {
            var6 = var0;
            var0 = null;
         }

         try {
            var1 += (double)Integer.parseInt(var6) / var3;
         } catch (NumberFormatException var8) {
            return 0.0D;
         }
      }

      return var1;
   }

   static {
      if(Native.POINTER_SIZE == 0) {
         throw new Error("Native library not initialized");
      } else {
         String var0 = Native.getWebStartLibraryPath("jnidispatch");
         if(var0 != null) {
            librarySearchPath.add(var0);
         }

         if(System.getProperty("jna.platform.library.path") == null && !Platform.isWindows()) {
            String var1 = "";
            String var2 = "";
            String var3 = "";
            if(Platform.isLinux() || Platform.isSolaris() || Platform.isFreeBSD()) {
               var3 = (Platform.isSolaris()?"/":"") + Pointer.SIZE * 8;
            }

            String[] var4 = new String[]{"/usr/lib" + var3, "/lib" + var3, "/usr/lib", "/lib"};
            if(Platform.isLinux()) {
               String var5 = "";
               String var6 = "linux";
               String var7 = "gnu";
               if(Platform.isIntel()) {
                  var5 = Platform.is64Bit()?"x86_64":"i386";
               } else if(Platform.isPPC()) {
                  var5 = Platform.is64Bit()?"powerpc64":"powerpc";
               } else if(Platform.isARM()) {
                  var5 = "arm";
                  var7 = "gnueabi";
               }

               String var8 = var5 + "-" + var6 + "-" + var7;
               var4 = new String[]{"/usr/lib/" + var8, "/lib/" + var8, "/usr/lib" + var3, "/lib" + var3, "/usr/lib", "/lib"};
            }

            for(int var10 = 0; var10 < var4.length; ++var10) {
               File var9 = new File(var4[var10]);
               if(var9.exists() && var9.isDirectory()) {
                  var1 = var1 + var2 + var4[var10];
                  var2 = File.pathSeparator;
               }
            }

            if(!"".equals(var1)) {
               System.setProperty("jna.platform.library.path", var1);
            }
         }

         librarySearchPath.addAll(initPaths("jna.platform.library.path"));
      }
   }
}
