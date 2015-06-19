package com.sun.jna;

import com.sun.jna.Callback;
import com.sun.jna.CallbackReference;
import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.FromNativeContext;
import com.sun.jna.FromNativeConverter;
import com.sun.jna.Function;
import com.sun.jna.FunctionMapper;
import com.sun.jna.IntegerType;
import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.ToNativeContext;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import java.util.Map.Entry;

public final class Native {
   private static final String VERSION = "3.4.0";
   private static final String VERSION_NATIVE = "3.4.0";
   private static String nativeLibraryPath = null;
   private static Map typeMappers = new WeakHashMap();
   private static Map alignments = new WeakHashMap();
   private static Map options = new WeakHashMap();
   private static Map libraries = new WeakHashMap();
   private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler() {
      public void uncaughtException(Callback var1, Throwable var2) {
         System.err.println("JNA: Callback " + var1 + " threw the following exception:");
         var2.printStackTrace();
      }
   };
   private static Callback.UncaughtExceptionHandler callbackExceptionHandler;
   public static final int POINTER_SIZE;
   public static final int LONG_SIZE;
   public static final int WCHAR_SIZE;
   public static final int SIZE_T_SIZE;
   private static final int TYPE_VOIDP = 0;
   private static final int TYPE_LONG = 1;
   private static final int TYPE_WCHAR_T = 2;
   private static final int TYPE_SIZE_T = 3;
   private static final int THREAD_NOCHANGE = 0;
   private static final int THREAD_DETACH = -1;
   private static final int THREAD_LEAVE_ATTACHED = -2;
   private static final Object finalizer;
   private static final ThreadLocal lastError;
   private static Map registeredClasses;
   private static Map registeredLibraries;
   private static Object unloader;
   static final int CB_HAS_INITIALIZER = 1;
   private static final int CVT_UNSUPPORTED = -1;
   private static final int CVT_DEFAULT = 0;
   private static final int CVT_POINTER = 1;
   private static final int CVT_STRING = 2;
   private static final int CVT_STRUCTURE = 3;
   private static final int CVT_STRUCTURE_BYVAL = 4;
   private static final int CVT_BUFFER = 5;
   private static final int CVT_ARRAY_BYTE = 6;
   private static final int CVT_ARRAY_SHORT = 7;
   private static final int CVT_ARRAY_CHAR = 8;
   private static final int CVT_ARRAY_INT = 9;
   private static final int CVT_ARRAY_LONG = 10;
   private static final int CVT_ARRAY_FLOAT = 11;
   private static final int CVT_ARRAY_DOUBLE = 12;
   private static final int CVT_ARRAY_BOOLEAN = 13;
   private static final int CVT_BOOLEAN = 14;
   private static final int CVT_CALLBACK = 15;
   private static final int CVT_FLOAT = 16;
   private static final int CVT_NATIVE_MAPPED = 17;
   private static final int CVT_WSTRING = 18;
   private static final int CVT_INTEGER_TYPE = 19;
   private static final int CVT_POINTER_TYPE = 20;
   private static final int CVT_TYPE_MAPPER = 21;

   private static void dispose() {
      NativeLibrary.disposeAll();
      nativeLibraryPath = null;
   }

   private static boolean deleteNativeLibrary(String var0) {
      File var1 = new File(var0);
      if(var1.delete()) {
         return true;
      } else {
         markTemporaryFile(var1);
         return false;
      }
   }

   private Native() {
   }

   private static native void initIDs();

   public static synchronized native void setProtected(boolean var0);

   public static synchronized native boolean isProtected();

   /** @deprecated */
   public static synchronized native void setPreserveLastError(boolean var0);

   /** @deprecated */
   public static synchronized native boolean getPreserveLastError();

   public static long getWindowID(Window var0) throws HeadlessException {
      return Native.AWT.getComponentID(var0);
   }

   public static long getComponentID(Component var0) throws HeadlessException {
      return Native.AWT.getComponentID(var0);
   }

   public static Pointer getWindowPointer(Window var0) throws HeadlessException {
      return new Pointer(Native.AWT.getComponentID(var0));
   }

   public static Pointer getComponentPointer(Component var0) throws HeadlessException {
      return new Pointer(Native.AWT.getComponentID(var0));
   }

   static native long getWindowHandle0(Component var0);

   public static Pointer getDirectBufferPointer(Buffer var0) {
      long var1 = _getDirectBufferPointer(var0);
      return var1 == 0L?null:new Pointer(var1);
   }

   private static native long _getDirectBufferPointer(Buffer var0);

   public static String toString(byte[] var0) {
      return toString(var0, System.getProperty("jna.encoding"));
   }

   public static String toString(byte[] var0, String var1) {
      String var2 = null;
      if(var1 != null) {
         try {
            var2 = new String(var0, var1);
         } catch (UnsupportedEncodingException var4) {
            ;
         }
      }

      if(var2 == null) {
         var2 = new String(var0);
      }

      int var3 = var2.indexOf(0);
      if(var3 != -1) {
         var2 = var2.substring(0, var3);
      }

      return var2;
   }

   public static String toString(char[] var0) {
      String var1 = new String(var0);
      int var2 = var1.indexOf(0);
      if(var2 != -1) {
         var1 = var1.substring(0, var2);
      }

      return var1;
   }

   public static Object loadLibrary(Class var0) {
      return loadLibrary((String)null, (Class)var0);
   }

   public static Object loadLibrary(Class var0, Map var1) {
      return loadLibrary((String)null, var0, var1);
   }

   public static Object loadLibrary(String var0, Class var1) {
      return loadLibrary(var0, var1, Collections.EMPTY_MAP);
   }

   public static Object loadLibrary(String var0, Class var1, Map var2) {
      Library.Handler var3 = new Library.Handler(var0, var1, var2);
      ClassLoader var4 = var1.getClassLoader();
      Library var5 = (Library)Proxy.newProxyInstance(var4, new Class[]{var1}, var3);
      cacheOptions(var1, var2, var5);
      return var5;
   }

   private static void loadLibraryInstance(Class var0) {
      if(var0 != null && !libraries.containsKey(var0)) {
         try {
            Field[] var1 = var0.getFields();

            for(int var2 = 0; var2 < var1.length; ++var2) {
               Field var3 = var1[var2];
               if(var3.getType() == var0 && Modifier.isStatic(var3.getModifiers())) {
                  libraries.put(var0, new WeakReference(var3.get((Object)null)));
                  break;
               }
            }
         } catch (Exception var4) {
            throw new IllegalArgumentException("Could not access instance of " + var0 + " (" + var4 + ")");
         }
      }

   }

   static Class findEnclosingLibraryClass(Class var0) {
      if(var0 == null) {
         return null;
      } else {
         Map var1 = libraries;
         synchronized(libraries) {
            if(options.containsKey(var0)) {
               return var0;
            }
         }

         if(Library.class.isAssignableFrom(var0)) {
            return var0;
         } else {
            if(Callback.class.isAssignableFrom(var0)) {
               var0 = CallbackReference.findCallbackClass(var0);
            }

            Class var4 = var0.getDeclaringClass();
            Class var2 = findEnclosingLibraryClass(var4);
            return var2 != null?var2:findEnclosingLibraryClass(var0.getSuperclass());
         }
      }
   }

   public static Map getLibraryOptions(Class var0) {
      Map var1 = libraries;
      synchronized(libraries) {
         Class var2 = findEnclosingLibraryClass(var0);
         if(var2 != null) {
            loadLibraryInstance(var2);
         } else {
            var2 = var0;
         }

         if(!options.containsKey(var2)) {
            try {
               Field var3 = var2.getField("OPTIONS");
               var3.setAccessible(true);
               options.put(var2, var3.get((Object)null));
            } catch (NoSuchFieldException var5) {
               ;
            } catch (Exception var6) {
               throw new IllegalArgumentException("OPTIONS must be a public field of type java.util.Map (" + var6 + "): " + var2);
            }
         }

         return (Map)options.get(var2);
      }
   }

   public static TypeMapper getTypeMapper(Class var0) {
      Map var1 = libraries;
      synchronized(libraries) {
         Class var2 = findEnclosingLibraryClass(var0);
         if(var2 != null) {
            loadLibraryInstance(var2);
         } else {
            var2 = var0;
         }

         if(!typeMappers.containsKey(var2)) {
            try {
               Field var3 = var2.getField("TYPE_MAPPER");
               var3.setAccessible(true);
               typeMappers.put(var2, var3.get((Object)null));
            } catch (NoSuchFieldException var6) {
               Map var4 = getLibraryOptions(var0);
               if(var4 != null && var4.containsKey("type-mapper")) {
                  typeMappers.put(var2, var4.get("type-mapper"));
               }
            } catch (Exception var7) {
               throw new IllegalArgumentException("TYPE_MAPPER must be a public field of type " + TypeMapper.class.getName() + " (" + var7 + "): " + var2);
            }
         }

         return (TypeMapper)typeMappers.get(var2);
      }
   }

   public static int getStructureAlignment(Class var0) {
      Map var1 = libraries;
      synchronized(libraries) {
         Class var2 = findEnclosingLibraryClass(var0);
         if(var2 != null) {
            loadLibraryInstance(var2);
         } else {
            var2 = var0;
         }

         if(!alignments.containsKey(var2)) {
            try {
               Field var3 = var2.getField("STRUCTURE_ALIGNMENT");
               var3.setAccessible(true);
               alignments.put(var2, var3.get((Object)null));
            } catch (NoSuchFieldException var6) {
               Map var4 = getLibraryOptions(var2);
               if(var4 != null && var4.containsKey("structure-alignment")) {
                  alignments.put(var2, var4.get("structure-alignment"));
               }
            } catch (Exception var7) {
               throw new IllegalArgumentException("STRUCTURE_ALIGNMENT must be a public field of type int (" + var7 + "): " + var2);
            }
         }

         Integer var9 = (Integer)alignments.get(var2);
         return var9 != null?var9.intValue():0;
      }
   }

   static byte[] getBytes(String var0) {
      try {
         return getBytes(var0, System.getProperty("jna.encoding"));
      } catch (UnsupportedEncodingException var2) {
         return var0.getBytes();
      }
   }

   static byte[] getBytes(String var0, String var1) throws UnsupportedEncodingException {
      return var1 != null?var0.getBytes(var1):var0.getBytes();
   }

   public static byte[] toByteArray(String var0) {
      byte[] var1 = getBytes(var0);
      byte[] var2 = new byte[var1.length + 1];
      System.arraycopy(var1, 0, var2, 0, var1.length);
      return var2;
   }

   public static byte[] toByteArray(String var0, String var1) throws UnsupportedEncodingException {
      byte[] var2 = getBytes(var0, var1);
      byte[] var3 = new byte[var2.length + 1];
      System.arraycopy(var2, 0, var3, 0, var2.length);
      return var3;
   }

   public static char[] toCharArray(String var0) {
      char[] var1 = var0.toCharArray();
      char[] var2 = new char[var1.length + 1];
      System.arraycopy(var1, 0, var2, 0, var1.length);
      return var2;
   }

   static String getNativeLibraryResourcePath(int var0, String var1, String var2) {
      var1 = var1.toLowerCase();
      if("powerpc".equals(var1)) {
         var1 = "ppc";
      } else if("powerpc64".equals(var1)) {
         var1 = "ppc64";
      }

      String var3;
      switch(var0) {
      case 0:
         var3 = "darwin";
         break;
      case 1:
         if("x86".equals(var1)) {
            var1 = "i386";
         } else if("x86_64".equals(var1)) {
            var1 = "amd64";
         }

         var3 = "linux-" + var1;
         break;
      case 2:
         if("i386".equals(var1)) {
            var1 = "x86";
         }

         var3 = "win32-" + var1;
         break;
      case 3:
         var3 = "sunos-" + var1;
         break;
      case 4:
      case 5:
      default:
         var3 = var2.toLowerCase();
         if("x86".equals(var1)) {
            var1 = "i386";
         }

         if("x86_64".equals(var1)) {
            var1 = "amd64";
         }

         int var4 = var3.indexOf(" ");
         if(var4 != -1) {
            var3 = var3.substring(0, var4);
         }

         var3 = var3 + "-" + var1;
         break;
      case 6:
         var3 = "w32ce-" + var1;
      }

      return "/com/sun/jna/" + var3;
   }

   private static void loadNativeLibrary() {
      removeTemporaryFiles();
      String var0 = System.getProperty("jna.boot.library.name", "jnidispatch");
      String var1 = System.getProperty("jna.boot.library.path");
      if(var1 != null) {
         StringTokenizer var2 = new StringTokenizer(var1, File.pathSeparator);

         while(var2.hasMoreTokens()) {
            String var3 = var2.nextToken();
            File var4 = new File(new File(var3), System.mapLibraryName(var0));
            String var5 = var4.getAbsolutePath();
            if(var4.exists()) {
               try {
                  System.load(var5);
                  nativeLibraryPath = var5;
                  return;
               } catch (UnsatisfiedLinkError var11) {
                  ;
               }
            }

            if(Platform.isMac()) {
               String var6;
               String var7;
               if(var5.endsWith("dylib")) {
                  var6 = "dylib";
                  var7 = "jnilib";
               } else {
                  var6 = "jnilib";
                  var7 = "dylib";
               }

               var5 = var5.substring(0, var5.lastIndexOf(var6)) + var7;
               if((new File(var5)).exists()) {
                  try {
                     System.load(var5);
                     nativeLibraryPath = var5;
                     return;
                  } catch (UnsatisfiedLinkError var10) {
                     System.err.println("File found at " + var5 + " but not loadable: " + var10.getMessage());
                  }
               }
            }
         }
      }

      try {
         if(!Boolean.getBoolean("jna.nosys")) {
            System.loadLibrary(var0);
            return;
         }
      } catch (UnsatisfiedLinkError var9) {
         if(Boolean.getBoolean("jna.nounpack")) {
            throw var9;
         }
      }

      if(!Boolean.getBoolean("jna.nounpack")) {
         loadNativeLibraryFromJar();
      } else {
         throw new UnsatisfiedLinkError("Native jnidispatch library not found");
      }
   }

   private static void loadNativeLibraryFromJar() {
      String var0 = System.mapLibraryName("jnidispatch");
      String var1 = System.getProperty("os.arch");
      String var2 = System.getProperty("os.name");
      String var3 = getNativeLibraryResourcePath(Platform.getOSType(), var1, var2) + "/" + var0;
      URL var4 = Native.class.getResource(var3);
      boolean var5 = false;
      if(var4 == null && Platform.isMac() && var3.endsWith(".dylib")) {
         var3 = var3.substring(0, var3.lastIndexOf(".dylib")) + ".jnilib";
         var4 = Native.class.getResource(var3);
      }

      if(var4 == null) {
         throw new UnsatisfiedLinkError("jnidispatch (" + var3 + ") not found in resource path");
      } else {
         File var6 = null;
         if(var4.getProtocol().toLowerCase().equals("file")) {
            try {
               var6 = new File(new URI(var4.toString()));
            } catch (URISyntaxException var23) {
               var6 = new File(var4.getPath());
            }

            if(!var6.exists()) {
               throw new Error("File URL " + var4 + " could not be properly decoded");
            }
         } else {
            InputStream var7 = Native.class.getResourceAsStream(var3);
            if(var7 == null) {
               throw new Error("Can\'t obtain jnidispatch InputStream");
            }

            FileOutputStream var8 = null;

            try {
               File var9 = getTempDir();
               var6 = File.createTempFile("jna", Platform.isWindows()?".dll":null, var9);
               var6.deleteOnExit();
               var8 = new FileOutputStream(var6);
               byte[] var11 = new byte[1024];

               int var10;
               while((var10 = var7.read(var11, 0, var11.length)) > 0) {
                  var8.write(var11, 0, var10);
               }

               var5 = true;
            } catch (IOException var24) {
               throw new Error("Failed to create temporary file for jnidispatch library: " + var24);
            } finally {
               try {
                  var7.close();
               } catch (IOException var22) {
                  ;
               }

               if(var8 != null) {
                  try {
                     var8.close();
                  } catch (IOException var21) {
                     ;
                  }
               }

            }
         }

         System.load(var6.getAbsolutePath());
         nativeLibraryPath = var6.getAbsolutePath();
         if(var5) {
            deleteNativeLibrary(var6.getAbsolutePath());
         }

      }
   }

   private static native int sizeof(int var0);

   private static native String getNativeVersion();

   private static native String getAPIChecksum();

   public static int getLastError() {
      return ((Integer)lastError.get()).intValue();
   }

   public static native void setLastError(int var0);

   static void updateLastError(int var0) {
      lastError.set(new Integer(var0));
   }

   public static Library synchronizedLibrary(final Library var0) {
      Class var1 = var0.getClass();
      if(!Proxy.isProxyClass(var1)) {
         throw new IllegalArgumentException("Library must be a proxy class");
      } else {
         InvocationHandler var2 = Proxy.getInvocationHandler(var0);
         if(!(var2 instanceof Library.Handler)) {
            throw new IllegalArgumentException("Unrecognized proxy handler: " + var2);
         } else {
            final Library.Handler var3 = (Library.Handler)var2;
            InvocationHandler var4 = new InvocationHandler() {
               public Object invoke(Object var1, Method var2, Object[] var3x) throws Throwable {
                  synchronized(var3.getNativeLibrary()) {
                     return var3.invoke(var0, var2, var3x);
                  }
               }
            };
            return (Library)Proxy.newProxyInstance(var1.getClassLoader(), var1.getInterfaces(), var4);
         }
      }
   }

   public static String getWebStartLibraryPath(String var0) {
      if(System.getProperty("javawebstart.version") == null) {
         return null;
      } else {
         try {
            ClassLoader var1 = Native.class.getClassLoader();
            Method var2 = (Method)AccessController.doPrivileged(new PrivilegedAction() {
               public Object run() {
                  try {
                     Method var1 = ClassLoader.class.getDeclaredMethod("findLibrary", new Class[]{String.class});
                     var1.setAccessible(true);
                     return var1;
                  } catch (Exception var2) {
                     return null;
                  }
               }
            });
            String var3 = (String)var2.invoke(var1, new Object[]{var0});
            return var3 != null?(new File(var3)).getParent():null;
         } catch (Exception var4) {
            return null;
         }
      }
   }

   static void markTemporaryFile(File var0) {
      try {
         File var1 = new File(var0.getParentFile(), var0.getName() + ".x");
         var1.createNewFile();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   static File getTempDir() {
      File var0 = new File(System.getProperty("java.io.tmpdir"));
      File var1 = new File(var0, "jna");
      var1.mkdirs();
      return var1.exists()?var1:var0;
   }

   static void removeTemporaryFiles() {
      File var0 = getTempDir();
      FilenameFilter var1 = new FilenameFilter() {
         public boolean accept(File var1, String var2) {
            return var2.endsWith(".x") && var2.indexOf("jna") != -1;
         }
      };
      File[] var2 = var0.listFiles(var1);

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         File var4 = var2[var3];
         String var5 = var4.getName();
         var5 = var5.substring(0, var5.length() - 2);
         File var6 = new File(var4.getParentFile(), var5);
         if(!var6.exists() || var6.delete()) {
            var4.delete();
         }
      }

   }

   public static int getNativeSize(Class var0, Object var1) {
      if(var0.isArray()) {
         int var2 = Array.getLength(var1);
         if(var2 > 0) {
            Object var3 = Array.get(var1, 0);
            return var2 * getNativeSize(var0.getComponentType(), var3);
         } else {
            throw new IllegalArgumentException("Arrays of length zero not allowed: " + var0);
         }
      } else if(Structure.class.isAssignableFrom(var0) && !Structure.ByReference.class.isAssignableFrom(var0)) {
         if(var1 == null) {
            var1 = Structure.newInstance(var0);
         }

         return ((Structure)var1).size();
      } else {
         try {
            return getNativeSize(var0);
         } catch (IllegalArgumentException var4) {
            throw new IllegalArgumentException("The type \"" + var0.getName() + "\" is not supported: " + var4.getMessage());
         }
      }
   }

   public static int getNativeSize(Class var0) {
      if(NativeMapped.class.isAssignableFrom(var0)) {
         var0 = NativeMappedConverter.getInstance(var0).nativeType();
      }

      if(var0 != Boolean.TYPE && var0 != Boolean.class) {
         if(var0 != Byte.TYPE && var0 != Byte.class) {
            if(var0 != Short.TYPE && var0 != Short.class) {
               if(var0 != Character.TYPE && var0 != Character.class) {
                  if(var0 != Integer.TYPE && var0 != Integer.class) {
                     if(var0 != Long.TYPE && var0 != Long.class) {
                        if(var0 != Float.TYPE && var0 != Float.class) {
                           if(var0 != Double.TYPE && var0 != Double.class) {
                              if(Structure.class.isAssignableFrom(var0)) {
                                 return Structure.ByValue.class.isAssignableFrom(var0)?Structure.newInstance(var0).size():POINTER_SIZE;
                              } else if(!Pointer.class.isAssignableFrom(var0) && (!Platform.HAS_BUFFERS || !Native.Buffers.isBuffer(var0)) && !Callback.class.isAssignableFrom(var0) && String.class != var0 && WString.class != var0) {
                                 throw new IllegalArgumentException("Native size for type \"" + var0.getName() + "\" is unknown");
                              } else {
                                 return POINTER_SIZE;
                              }
                           } else {
                              return 8;
                           }
                        } else {
                           return 4;
                        }
                     } else {
                        return 8;
                     }
                  } else {
                     return 4;
                  }
               } else {
                  return WCHAR_SIZE;
               }
            } else {
               return 2;
            }
         } else {
            return 1;
         }
      } else {
         return 4;
      }
   }

   public static boolean isSupportedNativeType(Class var0) {
      if(Structure.class.isAssignableFrom(var0)) {
         return true;
      } else {
         try {
            return getNativeSize(var0) != 0;
         } catch (IllegalArgumentException var2) {
            return false;
         }
      }
   }

   public static void setCallbackExceptionHandler(Callback.UncaughtExceptionHandler var0) {
      callbackExceptionHandler = var0 == null?DEFAULT_HANDLER:var0;
   }

   public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler() {
      return callbackExceptionHandler;
   }

   public static void register(String var0) {
      register(getNativeClass(getCallingClass()), NativeLibrary.getInstance(var0));
   }

   public static void register(NativeLibrary var0) {
      register(getNativeClass(getCallingClass()), var0);
   }

   static Class getNativeClass(Class var0) {
      Method[] var1 = var0.getDeclaredMethods();

      int var2;
      for(var2 = 0; var2 < var1.length; ++var2) {
         if((var1[var2].getModifiers() & 256) != 0) {
            return var0;
         }
      }

      var2 = var0.getName().lastIndexOf("$");
      if(var2 != -1) {
         String var3 = var0.getName().substring(0, var2);

         try {
            return getNativeClass(Class.forName(var3, true, var0.getClassLoader()));
         } catch (ClassNotFoundException var5) {
            ;
         }
      }

      throw new IllegalArgumentException("Can\'t determine class with native methods from the current context (" + var0 + ")");
   }

   static Class getCallingClass() {
      Class[] var0 = (new SecurityManager() {
         public Class[] getClassContext() {
            return super.getClassContext();
         }
      }).getClassContext();
      if(var0.length < 4) {
         throw new IllegalStateException("This method must be called from the static initializer of a class");
      } else {
         return var0[3];
      }
   }

   public static void setCallbackThreadInitializer(Callback var0, CallbackThreadInitializer var1) {
      CallbackReference.setCallbackThreadInitializer(var0, var1);
   }

   public static void unregister() {
      unregister(getNativeClass(getCallingClass()));
   }

   public static void unregister(Class var0) {
      Map var1 = registeredClasses;
      synchronized(registeredClasses) {
         if(registeredClasses.containsKey(var0)) {
            unregister(var0, (long[])((long[])registeredClasses.get(var0)));
            registeredClasses.remove(var0);
            registeredLibraries.remove(var0);
         }

      }
   }

   private static native void unregister(Class var0, long[] var1);

   private static String getSignature(Class var0) {
      if(var0.isArray()) {
         return "[" + getSignature(var0.getComponentType());
      } else {
         if(var0.isPrimitive()) {
            if(var0 == Void.TYPE) {
               return "V";
            }

            if(var0 == Boolean.TYPE) {
               return "Z";
            }

            if(var0 == Byte.TYPE) {
               return "B";
            }

            if(var0 == Short.TYPE) {
               return "S";
            }

            if(var0 == Character.TYPE) {
               return "C";
            }

            if(var0 == Integer.TYPE) {
               return "I";
            }

            if(var0 == Long.TYPE) {
               return "J";
            }

            if(var0 == Float.TYPE) {
               return "F";
            }

            if(var0 == Double.TYPE) {
               return "D";
            }
         }

         return "L" + replace(".", "/", var0.getName()) + ";";
      }
   }

   static String replace(String var0, String var1, String var2) {
      StringBuffer var3 = new StringBuffer();

      while(true) {
         int var4 = var2.indexOf(var0);
         if(var4 == -1) {
            var3.append(var2);
            return var3.toString();
         }

         var3.append(var2.substring(0, var4));
         var3.append(var1);
         var2 = var2.substring(var4 + var0.length());
      }
   }

   private static int getConversion(Class var0, TypeMapper var1) {
      if(var0 == Boolean.class) {
         var0 = Boolean.TYPE;
      } else if(var0 == Byte.class) {
         var0 = Byte.TYPE;
      } else if(var0 == Short.class) {
         var0 = Short.TYPE;
      } else if(var0 == Character.class) {
         var0 = Character.TYPE;
      } else if(var0 == Integer.class) {
         var0 = Integer.TYPE;
      } else if(var0 == Long.class) {
         var0 = Long.TYPE;
      } else if(var0 == Float.class) {
         var0 = Float.TYPE;
      } else if(var0 == Double.class) {
         var0 = Double.TYPE;
      } else if(var0 == Void.class) {
         var0 = Void.TYPE;
      }

      if(var1 == null || var1.getFromNativeConverter(var0) == null && var1.getToNativeConverter(var0) == null) {
         if(Pointer.class.isAssignableFrom(var0)) {
            return 1;
         } else if(String.class == var0) {
            return 2;
         } else if(WString.class.isAssignableFrom(var0)) {
            return 18;
         } else if(Platform.HAS_BUFFERS && Native.Buffers.isBuffer(var0)) {
            return 5;
         } else if(Structure.class.isAssignableFrom(var0)) {
            return Structure.ByValue.class.isAssignableFrom(var0)?4:3;
         } else {
            if(var0.isArray()) {
               switch(var0.getName().charAt(1)) {
               case 'B':
                  return 6;
               case 'C':
                  return 8;
               case 'D':
                  return 12;
               case 'E':
               case 'G':
               case 'H':
               case 'K':
               case 'L':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'R':
               case 'T':
               case 'U':
               case 'V':
               case 'W':
               case 'X':
               case 'Y':
               default:
                  break;
               case 'F':
                  return 11;
               case 'I':
                  return 9;
               case 'J':
                  return 10;
               case 'S':
                  return 7;
               case 'Z':
                  return 13;
               }
            }

            return var0.isPrimitive()?(var0 == Boolean.TYPE?14:0):(Callback.class.isAssignableFrom(var0)?15:(IntegerType.class.isAssignableFrom(var0)?19:(PointerType.class.isAssignableFrom(var0)?20:(NativeMapped.class.isAssignableFrom(var0)?17:-1))));
         }
      } else {
         return 21;
      }
   }

   public static void register(Class var0, NativeLibrary var1) {
      Method[] var2 = var0.getDeclaredMethods();
      ArrayList var3 = new ArrayList();
      TypeMapper var4 = (TypeMapper)var1.getOptions().get("type-mapper");

      for(int var5 = 0; var5 < var2.length; ++var5) {
         if((var2[var5].getModifiers() & 256) != 0) {
            var3.add(var2[var5]);
         }
      }

      long[] var30 = new long[var3.size()];

      for(int var6 = 0; var6 < var30.length; ++var6) {
         Method var7 = (Method)var3.get(var6);
         String var8 = "(";
         Class var9 = var7.getReturnType();
         Class[] var14 = var7.getParameterTypes();
         long[] var15 = new long[var14.length];
         long[] var16 = new long[var14.length];
         int[] var17 = new int[var14.length];
         ToNativeConverter[] var18 = new ToNativeConverter[var14.length];
         FromNativeConverter var19 = null;
         int var20 = getConversion(var9, var4);
         boolean var21 = false;
         long var10;
         long var12;
         switch(var20) {
         case -1:
            throw new IllegalArgumentException(var9 + " is not a supported return type (in method " + var7.getName() + " in " + var0 + ")");
         case 0:
         case 1:
         case 2:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 18:
         default:
            var12 = var10 = Structure.FFIType.get(var9).peer;
            break;
         case 3:
            var12 = var10 = Structure.FFIType.get(Pointer.class).peer;
            break;
         case 4:
            var12 = Structure.FFIType.get(Pointer.class).peer;
            var10 = Structure.FFIType.get(var9).peer;
            break;
         case 17:
         case 19:
         case 20:
            var12 = Structure.FFIType.get(Pointer.class).peer;
            var10 = Structure.FFIType.get(NativeMappedConverter.getInstance(var9).nativeType()).peer;
            break;
         case 21:
            var19 = var4.getFromNativeConverter(var9);
            var12 = Structure.FFIType.get(var9).peer;
            var10 = Structure.FFIType.get(var19.nativeType()).peer;
         }

         for(int var22 = 0; var22 < var14.length; ++var22) {
            Class var23 = var14[var22];
            var8 = var8 + getSignature(var23);
            var17[var22] = getConversion(var23, var4);
            if(var17[var22] == -1) {
               throw new IllegalArgumentException(var23 + " is not a supported argument type (in method " + var7.getName() + " in " + var0 + ")");
            }

            if(var17[var22] != 17 && var17[var22] != 19) {
               if(var17[var22] == 21) {
                  var18[var22] = var4.getToNativeConverter(var23);
               }
            } else {
               var23 = NativeMappedConverter.getInstance(var23).nativeType();
            }

            switch(var17[var22]) {
            case 0:
               var16[var22] = var15[var22] = Structure.FFIType.get(var23).peer;
               break;
            case 4:
            case 17:
            case 19:
            case 20:
               var15[var22] = Structure.FFIType.get(var23).peer;
               var16[var22] = Structure.FFIType.get(Pointer.class).peer;
               break;
            case 21:
               if(var23.isPrimitive()) {
                  var16[var22] = Structure.FFIType.get(var23).peer;
               } else {
                  var16[var22] = Structure.FFIType.get(Pointer.class).peer;
               }

               var15[var22] = Structure.FFIType.get(var18[var22].nativeType()).peer;
               break;
            default:
               var16[var22] = var15[var22] = Structure.FFIType.get(Pointer.class).peer;
            }
         }

         var8 = var8 + ")";
         var8 = var8 + getSignature(var9);
         Class[] var32 = var7.getExceptionTypes();

         for(int var33 = 0; var33 < var32.length; ++var33) {
            if(LastErrorException.class.isAssignableFrom(var32[var33])) {
               var21 = true;
               break;
            }
         }

         String var34 = var7.getName();
         FunctionMapper var24 = (FunctionMapper)var1.getOptions().get("function-mapper");
         if(var24 != null) {
            var34 = var24.getFunctionName(var1, var7);
         }

         Function var25 = var1.getFunction(var34, var7);

         try {
            var30[var6] = registerMethod(var0, var7.getName(), var8, var17, var16, var15, var20, var12, var10, var9, var25.peer, var25.getCallingConvention(), var21, var18, var19);
         } catch (NoSuchMethodError var29) {
            throw new UnsatisfiedLinkError("No method " + var7.getName() + " with signature " + var8 + " in " + var0);
         }
      }

      Map var31 = registeredClasses;
      synchronized(registeredClasses) {
         registeredClasses.put(var0, var30);
         registeredLibraries.put(var0, var1);
      }

      cacheOptions(var0, var1.getOptions(), (Object)null);
   }

   private static void cacheOptions(Class var0, Map var1, Object var2) {
      Map var3 = libraries;
      synchronized(libraries) {
         if(!var1.isEmpty()) {
            options.put(var0, var1);
         }

         if(var1.containsKey("type-mapper")) {
            typeMappers.put(var0, var1.get("type-mapper"));
         }

         if(var1.containsKey("structure-alignment")) {
            alignments.put(var0, var1.get("structure-alignment"));
         }

         if(var2 != null) {
            libraries.put(var0, new WeakReference(var2));
         }

         if(!var0.isInterface() && Library.class.isAssignableFrom(var0)) {
            Class[] var4 = var0.getInterfaces();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               if(Library.class.isAssignableFrom(var4[var5])) {
                  cacheOptions(var4[var5], var1, var2);
                  break;
               }
            }
         }

      }
   }

   private static native long registerMethod(Class var0, String var1, String var2, int[] var3, long[] var4, long[] var5, int var6, long var7, long var9, Class var11, long var12, int var14, boolean var15, ToNativeConverter[] var16, FromNativeConverter var17);

   private static NativeMapped fromNative(Class var0, Object var1) {
      return (NativeMapped)NativeMappedConverter.getInstance(var0).fromNative(var1, new FromNativeContext(var0));
   }

   private static Class nativeType(Class var0) {
      return NativeMappedConverter.getInstance(var0).nativeType();
   }

   private static Object toNative(ToNativeConverter var0, Object var1) {
      return var0.toNative(var1, new ToNativeContext());
   }

   private static Object fromNative(FromNativeConverter var0, Object var1, Class var2) {
      return var0.fromNative(var1, new FromNativeContext(var2));
   }

   public static native long ffi_prep_cif(int var0, int var1, long var2, long var4);

   public static native void ffi_call(long var0, long var2, long var4, long var6);

   public static native long ffi_prep_closure(long var0, Native.ffi_callback var2);

   public static native void ffi_free_closure(long var0);

   static native int initialize_ffi_type(long var0);

   public static void main(String[] var0) {
      String var1 = "Java Native Access (JNA)";
      String var2 = "3.4.0";
      String var3 = "3.4.0 (package information missing)";
      Package var4 = Native.class.getPackage();
      String var5 = var4 != null?var4.getSpecificationTitle():"Java Native Access (JNA)";
      if(var5 == null) {
         var5 = "Java Native Access (JNA)";
      }

      String var6 = var4 != null?var4.getSpecificationVersion():"3.4.0";
      if(var6 == null) {
         var6 = "3.4.0";
      }

      var5 = var5 + " API Version " + var6;
      System.out.println(var5);
      var6 = var4 != null?var4.getImplementationVersion():"3.4.0 (package information missing)";
      if(var6 == null) {
         var6 = "3.4.0 (package information missing)";
      }

      System.out.println("Version: " + var6);
      System.out.println(" Native: " + getNativeVersion() + " (" + getAPIChecksum() + ")");
      System.exit(0);
   }

   static synchronized native void freeNativeCallback(long var0);

   static synchronized native long createNativeCallback(Callback var0, Method var1, Class[] var2, Class var3, int var4, boolean var5);

   static native int invokeInt(long var0, int var2, Object[] var3);

   static native long invokeLong(long var0, int var2, Object[] var3);

   static native void invokeVoid(long var0, int var2, Object[] var3);

   static native float invokeFloat(long var0, int var2, Object[] var3);

   static native double invokeDouble(long var0, int var2, Object[] var3);

   static native long invokePointer(long var0, int var2, Object[] var3);

   private static native void invokeStructure(long var0, int var2, Object[] var3, long var4, long var6);

   static Structure invokeStructure(long var0, int var2, Object[] var3, Structure var4) {
      invokeStructure(var0, var2, var3, var4.getPointer().peer, var4.getTypeInfo().peer);
      return var4;
   }

   static native Object invokeObject(long var0, int var2, Object[] var3);

   static native long open(String var0);

   static native void close(long var0);

   static native long findSymbol(long var0, String var2);

   static native long indexOf(long var0, byte var2);

   static native void read(long var0, byte[] var2, int var3, int var4);

   static native void read(long var0, short[] var2, int var3, int var4);

   static native void read(long var0, char[] var2, int var3, int var4);

   static native void read(long var0, int[] var2, int var3, int var4);

   static native void read(long var0, long[] var2, int var3, int var4);

   static native void read(long var0, float[] var2, int var3, int var4);

   static native void read(long var0, double[] var2, int var3, int var4);

   static native void write(long var0, byte[] var2, int var3, int var4);

   static native void write(long var0, short[] var2, int var3, int var4);

   static native void write(long var0, char[] var2, int var3, int var4);

   static native void write(long var0, int[] var2, int var3, int var4);

   static native void write(long var0, long[] var2, int var3, int var4);

   static native void write(long var0, float[] var2, int var3, int var4);

   static native void write(long var0, double[] var2, int var3, int var4);

   static native byte getByte(long var0);

   static native char getChar(long var0);

   static native short getShort(long var0);

   static native int getInt(long var0);

   static native long getLong(long var0);

   static native float getFloat(long var0);

   static native double getDouble(long var0);

   static Pointer getPointer(long var0) {
      long var2 = _getPointer(var0);
      return var2 == 0L?null:new Pointer(var2);
   }

   private static native long _getPointer(long var0);

   static native String getString(long var0, boolean var2);

   static native void setMemory(long var0, long var2, byte var4);

   static native void setByte(long var0, byte var2);

   static native void setShort(long var0, short var2);

   static native void setChar(long var0, char var2);

   static native void setInt(long var0, int var2);

   static native void setLong(long var0, long var2);

   static native void setFloat(long var0, float var2);

   static native void setDouble(long var0, double var2);

   static native void setPointer(long var0, long var2);

   static native void setString(long var0, String var2, boolean var3);

   public static native long malloc(long var0);

   public static native void free(long var0);

   public static native ByteBuffer getDirectByteBuffer(long var0, long var2);

   public static void detach(boolean var0) {
      setLastError(var0?-1:-2);
   }

   static {
      callbackExceptionHandler = DEFAULT_HANDLER;
      loadNativeLibrary();
      POINTER_SIZE = sizeof(0);
      LONG_SIZE = sizeof(1);
      WCHAR_SIZE = sizeof(2);
      SIZE_T_SIZE = sizeof(3);
      initIDs();
      if(Boolean.getBoolean("jna.protected")) {
         setProtected(true);
      }

      String var0 = getNativeVersion();
      if(!"3.4.0".equals(var0)) {
         String var1 = System.getProperty("line.separator");
         throw new Error(var1 + var1 + "There is an incompatible JNA native library installed on this system." + var1 + "To resolve this issue you may do one of the following:" + var1 + " - remove or uninstall the offending library" + var1 + " - set the system property jna.nosys=true" + var1 + " - set jna.boot.library.path to include the path to the version of the " + var1 + "   jnidispatch library included with the JNA jar file you are using" + var1);
      } else {
         setPreserveLastError("true".equalsIgnoreCase(System.getProperty("jna.preserve_last_error", "true")));
         finalizer = new Object() {
            protected void finalize() {
               Native.dispose();
            }
         };
         lastError = new ThreadLocal() {
            protected synchronized Object initialValue() {
               return new Integer(0);
            }
         };
         registeredClasses = new HashMap();
         registeredLibraries = new HashMap();
         unloader = new Object() {
            protected void finalize() {
               synchronized(Native.registeredClasses) {
                  Iterator var2 = Native.registeredClasses.entrySet().iterator();

                  while(var2.hasNext()) {
                     Entry var3 = (Entry)var2.next();
                     Native.unregister((Class)var3.getKey(), (long[])((long[])var3.getValue()));
                     var2.remove();
                  }

               }
            }
         };
      }
   }

   private static class AWT {
      private AWT() {
      }

      static long getWindowID(Window var0) throws HeadlessException {
         return getComponentID(var0);
      }

      static long getComponentID(Object var0) throws HeadlessException {
         if(GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException("No native windows when headless");
         } else {
            Component var1 = (Component)var0;
            if(var1.isLightweight()) {
               throw new IllegalArgumentException("Component must be heavyweight");
            } else if(!var1.isDisplayable()) {
               throw new IllegalStateException("Component must be displayable");
            } else if(Platform.isX11() && System.getProperty("java.version").startsWith("1.4") && !var1.isVisible()) {
               throw new IllegalStateException("Component must be visible");
            } else {
               return Native.getWindowHandle0(var1);
            }
         }
      }
   }

   private static class Buffers {
      private Buffers() {
      }

      static boolean isBuffer(Class var0) {
         return Buffer.class.isAssignableFrom(var0);
      }
   }

   public interface ffi_callback {
      void invoke(long var1, long var3, long var5);
   }
}
