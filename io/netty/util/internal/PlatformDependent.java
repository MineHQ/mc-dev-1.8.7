package io.netty.util.internal;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.JavassistTypeParameterMatcherGenerator;
import io.netty.util.internal.MpscLinkedQueue;
import io.netty.util.internal.PlatformDependent0;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PlatformDependent {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PlatformDependent.class);
   private static final Pattern MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN = Pattern.compile("\\s*-XX:MaxDirectMemorySize\\s*=\\s*([0-9]+)\\s*([kKmMgG]?)\\s*$");
   private static final boolean IS_ANDROID = isAndroid0();
   private static final boolean IS_WINDOWS = isWindows0();
   private static final boolean IS_ROOT = isRoot0();
   private static final int JAVA_VERSION = javaVersion0();
   private static final boolean CAN_ENABLE_TCP_NODELAY_BY_DEFAULT = !isAndroid();
   private static final boolean HAS_UNSAFE = hasUnsafe0();
   private static final boolean CAN_USE_CHM_V8;
   private static final boolean DIRECT_BUFFER_PREFERRED;
   private static final long MAX_DIRECT_MEMORY;
   private static final long ARRAY_BASE_OFFSET;
   private static final boolean HAS_JAVASSIST;
   private static final File TMPDIR;
   private static final int BIT_MODE;
   private static final int ADDRESS_SIZE;

   public static boolean isAndroid() {
      return IS_ANDROID;
   }

   public static boolean isWindows() {
      return IS_WINDOWS;
   }

   public static boolean isRoot() {
      return IS_ROOT;
   }

   public static int javaVersion() {
      return JAVA_VERSION;
   }

   public static boolean canEnableTcpNoDelayByDefault() {
      return CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
   }

   public static boolean hasUnsafe() {
      return HAS_UNSAFE;
   }

   public static boolean directBufferPreferred() {
      return DIRECT_BUFFER_PREFERRED;
   }

   public static long maxDirectMemory() {
      return MAX_DIRECT_MEMORY;
   }

   public static boolean hasJavassist() {
      return HAS_JAVASSIST;
   }

   public static File tmpdir() {
      return TMPDIR;
   }

   public static int bitMode() {
      return BIT_MODE;
   }

   public static int addressSize() {
      return ADDRESS_SIZE;
   }

   public static long allocateMemory(long var0) {
      return PlatformDependent0.allocateMemory(var0);
   }

   public static void freeMemory(long var0) {
      PlatformDependent0.freeMemory(var0);
   }

   public static void throwException(Throwable var0) {
      if(hasUnsafe()) {
         PlatformDependent0.throwException(var0);
      } else {
         throwException0(var0);
      }

   }

   private static <E extends Throwable> void throwException0(Throwable var0) throws E {
      throw var0;
   }

   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap() {
      return (ConcurrentMap)(CAN_USE_CHM_V8?new ConcurrentHashMapV8():new ConcurrentHashMap());
   }

   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int var0) {
      return (ConcurrentMap)(CAN_USE_CHM_V8?new ConcurrentHashMapV8(var0):new ConcurrentHashMap(var0));
   }

   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int var0, float var1) {
      return (ConcurrentMap)(CAN_USE_CHM_V8?new ConcurrentHashMapV8(var0, var1):new ConcurrentHashMap(var0, var1));
   }

   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int var0, float var1, int var2) {
      return (ConcurrentMap)(CAN_USE_CHM_V8?new ConcurrentHashMapV8(var0, var1, var2):new ConcurrentHashMap(var0, var1, var2));
   }

   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(Map<? extends K, ? extends V> var0) {
      return (ConcurrentMap)(CAN_USE_CHM_V8?new ConcurrentHashMapV8(var0):new ConcurrentHashMap(var0));
   }

   public static void freeDirectBuffer(ByteBuffer var0) {
      if(hasUnsafe() && !isAndroid()) {
         PlatformDependent0.freeDirectBuffer(var0);
      }

   }

   public static long directBufferAddress(ByteBuffer var0) {
      return PlatformDependent0.directBufferAddress(var0);
   }

   public static Object getObject(Object var0, long var1) {
      return PlatformDependent0.getObject(var0, var1);
   }

   public static Object getObjectVolatile(Object var0, long var1) {
      return PlatformDependent0.getObjectVolatile(var0, var1);
   }

   public static int getInt(Object var0, long var1) {
      return PlatformDependent0.getInt(var0, var1);
   }

   public static long objectFieldOffset(Field var0) {
      return PlatformDependent0.objectFieldOffset(var0);
   }

   public static byte getByte(long var0) {
      return PlatformDependent0.getByte(var0);
   }

   public static short getShort(long var0) {
      return PlatformDependent0.getShort(var0);
   }

   public static int getInt(long var0) {
      return PlatformDependent0.getInt(var0);
   }

   public static long getLong(long var0) {
      return PlatformDependent0.getLong(var0);
   }

   public static void putOrderedObject(Object var0, long var1, Object var3) {
      PlatformDependent0.putOrderedObject(var0, var1, var3);
   }

   public static void putByte(long var0, byte var2) {
      PlatformDependent0.putByte(var0, var2);
   }

   public static void putShort(long var0, short var2) {
      PlatformDependent0.putShort(var0, var2);
   }

   public static void putInt(long var0, int var2) {
      PlatformDependent0.putInt(var0, var2);
   }

   public static void putLong(long var0, long var2) {
      PlatformDependent0.putLong(var0, var2);
   }

   public static void copyMemory(long var0, long var2, long var4) {
      PlatformDependent0.copyMemory(var0, var2, var4);
   }

   public static void copyMemory(byte[] var0, int var1, long var2, long var4) {
      PlatformDependent0.copyMemory(var0, ARRAY_BASE_OFFSET + (long)var1, (Object)null, var2, var4);
   }

   public static void copyMemory(long var0, byte[] var2, int var3, long var4) {
      PlatformDependent0.copyMemory((Object)null, var0, var2, ARRAY_BASE_OFFSET + (long)var3, var4);
   }

   public static <U, W> AtomicReferenceFieldUpdater<U, W> newAtomicReferenceFieldUpdater(Class<U> var0, String var1) {
      if(hasUnsafe()) {
         try {
            return PlatformDependent0.newAtomicReferenceFieldUpdater(var0, var1);
         } catch (Throwable var3) {
            ;
         }
      }

      return null;
   }

   public static <T> AtomicIntegerFieldUpdater<T> newAtomicIntegerFieldUpdater(Class<?> var0, String var1) {
      if(hasUnsafe()) {
         try {
            return PlatformDependent0.newAtomicIntegerFieldUpdater(var0, var1);
         } catch (Throwable var3) {
            ;
         }
      }

      return null;
   }

   public static <T> AtomicLongFieldUpdater<T> newAtomicLongFieldUpdater(Class<?> var0, String var1) {
      if(hasUnsafe()) {
         try {
            return PlatformDependent0.newAtomicLongFieldUpdater(var0, var1);
         } catch (Throwable var3) {
            ;
         }
      }

      return null;
   }

   public static <T> Queue<T> newMpscQueue() {
      return new MpscLinkedQueue();
   }

   public static ClassLoader getClassLoader(Class<?> var0) {
      return PlatformDependent0.getClassLoader(var0);
   }

   public static ClassLoader getContextClassLoader() {
      return PlatformDependent0.getContextClassLoader();
   }

   public static ClassLoader getSystemClassLoader() {
      return PlatformDependent0.getSystemClassLoader();
   }

   private static boolean isAndroid0() {
      boolean var0;
      try {
         Class.forName("android.app.Application", false, getSystemClassLoader());
         var0 = true;
      } catch (Exception var2) {
         var0 = false;
      }

      if(var0) {
         logger.debug("Platform: Android");
      }

      return var0;
   }

   private static boolean isWindows0() {
      boolean var0 = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).contains("win");
      if(var0) {
         logger.debug("Platform: Windows");
      }

      return var0;
   }

   private static boolean isRoot0() {
      if(isWindows()) {
         return false;
      } else {
         String[] var0 = new String[]{"/usr/bin/id", "/bin/id", "id", "/usr/xpg4/bin/id"};
         Pattern var1 = Pattern.compile("^(?:0|[1-9][0-9]*)$");
         String[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            Process var6 = null;
            BufferedReader var7 = null;
            String var8 = null;

            try {
               var6 = Runtime.getRuntime().exec(new String[]{var5, "-u"});
               var7 = new BufferedReader(new InputStreamReader(var6.getInputStream(), CharsetUtil.US_ASCII));
               var8 = var7.readLine();
               var7.close();

               while(true) {
                  try {
                     int var9 = var6.waitFor();
                     if(var9 != 0) {
                        var8 = null;
                     }
                     break;
                  } catch (InterruptedException var46) {
                     ;
                  }
               }
            } catch (Exception var47) {
               var8 = null;
            } finally {
               if(var7 != null) {
                  try {
                     var7.close();
                  } catch (IOException var43) {
                     ;
                  }
               }

               if(var6 != null) {
                  try {
                     var6.destroy();
                  } catch (Exception var42) {
                     ;
                  }
               }

            }

            if(var8 != null && var1.matcher(var8).matches()) {
               logger.debug("UID: {}", (Object)var8);
               return "0".equals(var8);
            }
         }

         logger.debug("Could not determine the current UID using /usr/bin/id; attempting to bind at privileged ports.");
         Pattern var49 = Pattern.compile(".*(?:denied|not.*permitted).*");
         var3 = 1023;

         while(true) {
            if(var3 > 0) {
               ServerSocket var50 = null;

               try {
                  var50 = new ServerSocket();
                  var50.setReuseAddress(true);
                  var50.bind(new InetSocketAddress(var3));
                  if(logger.isDebugEnabled()) {
                     logger.debug("UID: 0 (succeded to bind at port {})", (Object)Integer.valueOf(var3));
                  }

                  boolean var51 = true;
                  return var51;
               } catch (Exception var44) {
                  String var52 = var44.getMessage();
                  if(var52 == null) {
                     var52 = "";
                  }

                  var52 = var52.toLowerCase();
                  if(var49.matcher(var52).matches()) {
                     break label91;
                  }
               } finally {
                  if(var50 != null) {
                     try {
                        var50.close();
                     } catch (Exception var41) {
                        ;
                     }
                  }

               }

               --var3;
               continue;
            }

            logger.debug("UID: non-root (failed to bind at any privileged ports)");
            return false;
         }
      }
   }

   private static int javaVersion0() {
      byte var0;
      if(isAndroid()) {
         var0 = 6;
      } else {
         try {
            Class.forName("java.time.Clock", false, getClassLoader(Object.class));
            var0 = 8;
         } catch (Exception var3) {
            try {
               Class.forName("java.util.concurrent.LinkedTransferQueue", false, getClassLoader(BlockingQueue.class));
               var0 = 7;
            } catch (Exception var2) {
               var0 = 6;
            }
         }
      }

      if(logger.isDebugEnabled()) {
         logger.debug("Java version: {}", (Object)Integer.valueOf(var0));
      }

      return var0;
   }

   private static boolean hasUnsafe0() {
      boolean var0 = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
      logger.debug("-Dio.netty.noUnsafe: {}", (Object)Boolean.valueOf(var0));
      if(isAndroid()) {
         logger.debug("sun.misc.Unsafe: unavailable (Android)");
         return false;
      } else if(var0) {
         logger.debug("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
         return false;
      } else {
         boolean var1;
         if(SystemPropertyUtil.contains("io.netty.tryUnsafe")) {
            var1 = SystemPropertyUtil.getBoolean("io.netty.tryUnsafe", true);
         } else {
            var1 = SystemPropertyUtil.getBoolean("org.jboss.netty.tryUnsafe", true);
         }

         if(!var1) {
            logger.debug("sun.misc.Unsafe: unavailable (io.netty.tryUnsafe/org.jboss.netty.tryUnsafe)");
            return false;
         } else {
            try {
               boolean var2 = PlatformDependent0.hasUnsafe();
               logger.debug("sun.misc.Unsafe: {}", (Object)(var2?"available":"unavailable"));
               return var2;
            } catch (Throwable var3) {
               return false;
            }
         }
      }
   }

   private static long arrayBaseOffset0() {
      return !hasUnsafe()?-1L:PlatformDependent0.arrayBaseOffset();
   }

   private static long maxDirectMemory0() {
      long var0 = 0L;

      Class var2;
      try {
         var2 = Class.forName("sun.misc.VM", true, getSystemClassLoader());
         Method var3 = var2.getDeclaredMethod("maxDirectMemory", new Class[0]);
         var0 = ((Number)var3.invoke((Object)null, new Object[0])).longValue();
      } catch (Throwable var8) {
         ;
      }

      if(var0 > 0L) {
         return var0;
      } else {
         try {
            var2 = Class.forName("java.lang.management.ManagementFactory", true, getSystemClassLoader());
            Class var10 = Class.forName("java.lang.management.RuntimeMXBean", true, getSystemClassLoader());
            Object var4 = var2.getDeclaredMethod("getRuntimeMXBean", new Class[0]).invoke((Object)null, new Object[0]);
            List var5 = (List)var10.getDeclaredMethod("getInputArguments", new Class[0]).invoke(var4, new Object[0]);

            label41:
            for(int var6 = var5.size() - 1; var6 >= 0; --var6) {
               Matcher var7 = MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN.matcher((CharSequence)var5.get(var6));
               if(var7.matches()) {
                  var0 = Long.parseLong(var7.group(1));
                  switch(var7.group(2).charAt(0)) {
                  case 'G':
                  case 'g':
                     var0 *= 1073741824L;
                     break label41;
                  case 'K':
                  case 'k':
                     var0 *= 1024L;
                     break label41;
                  case 'M':
                  case 'm':
                     var0 *= 1048576L;
                  default:
                     break label41;
                  }
               }
            }
         } catch (Throwable var9) {
            ;
         }

         if(var0 <= 0L) {
            var0 = Runtime.getRuntime().maxMemory();
            logger.debug("maxDirectMemory: {} bytes (maybe)", (Object)Long.valueOf(var0));
         } else {
            logger.debug("maxDirectMemory: {} bytes", (Object)Long.valueOf(var0));
         }

         return var0;
      }
   }

   private static boolean hasJavassist0() {
      if(isAndroid()) {
         return false;
      } else {
         boolean var0 = SystemPropertyUtil.getBoolean("io.netty.noJavassist", false);
         logger.debug("-Dio.netty.noJavassist: {}", (Object)Boolean.valueOf(var0));
         if(var0) {
            logger.debug("Javassist: unavailable (io.netty.noJavassist)");
            return false;
         } else {
            try {
               JavassistTypeParameterMatcherGenerator.generate(Object.class, getClassLoader(PlatformDependent.class));
               logger.debug("Javassist: available");
               return true;
            } catch (Throwable var2) {
               logger.debug("Javassist: unavailable");
               logger.debug("You don\'t have Javassist in your class path or you don\'t have enough permission to load dynamically generated classes.  Please check the configuration for better performance.");
               return false;
            }
         }
      }
   }

   private static File tmpdir0() {
      File var0;
      try {
         var0 = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
         if(var0 != null) {
            logger.debug("-Dio.netty.tmpdir: {}", (Object)var0);
            return var0;
         }

         var0 = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
         if(var0 != null) {
            logger.debug("-Dio.netty.tmpdir: {} (java.io.tmpdir)", (Object)var0);
            return var0;
         }

         if(isWindows()) {
            var0 = toDirectory(System.getenv("TEMP"));
            if(var0 != null) {
               logger.debug("-Dio.netty.tmpdir: {} (%TEMP%)", (Object)var0);
               return var0;
            }

            String var1 = System.getenv("USERPROFILE");
            if(var1 != null) {
               var0 = toDirectory(var1 + "\\AppData\\Local\\Temp");
               if(var0 != null) {
                  logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\AppData\\Local\\Temp)", (Object)var0);
                  return var0;
               }

               var0 = toDirectory(var1 + "\\Local Settings\\Temp");
               if(var0 != null) {
                  logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\Local Settings\\Temp)", (Object)var0);
                  return var0;
               }
            }
         } else {
            var0 = toDirectory(System.getenv("TMPDIR"));
            if(var0 != null) {
               logger.debug("-Dio.netty.tmpdir: {} ($TMPDIR)", (Object)var0);
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

      logger.warn("Failed to get the temporary directory; falling back to: {}", (Object)var0);
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

   private static int bitMode0() {
      int var0 = SystemPropertyUtil.getInt("io.netty.bitMode", 0);
      if(var0 > 0) {
         logger.debug("-Dio.netty.bitMode: {}", (Object)Integer.valueOf(var0));
         return var0;
      } else {
         var0 = SystemPropertyUtil.getInt("sun.arch.data.model", 0);
         if(var0 > 0) {
            logger.debug("-Dio.netty.bitMode: {} (sun.arch.data.model)", (Object)Integer.valueOf(var0));
            return var0;
         } else {
            var0 = SystemPropertyUtil.getInt("com.ibm.vm.bitmode", 0);
            if(var0 > 0) {
               logger.debug("-Dio.netty.bitMode: {} (com.ibm.vm.bitmode)", (Object)Integer.valueOf(var0));
               return var0;
            } else {
               String var1 = SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim();
               if(!"amd64".equals(var1) && !"x86_64".equals(var1)) {
                  if("i386".equals(var1) || "i486".equals(var1) || "i586".equals(var1) || "i686".equals(var1)) {
                     var0 = 32;
                  }
               } else {
                  var0 = 64;
               }

               if(var0 > 0) {
                  logger.debug("-Dio.netty.bitMode: {} (os.arch: {})", Integer.valueOf(var0), var1);
               }

               String var2 = SystemPropertyUtil.get("java.vm.name", "").toLowerCase(Locale.US);
               Pattern var3 = Pattern.compile("([1-9][0-9]+)-?bit");
               Matcher var4 = var3.matcher(var2);
               return var4.find()?Integer.parseInt(var4.group(1)):64;
            }
         }
      }
   }

   private static int addressSize0() {
      return !hasUnsafe()?-1:PlatformDependent0.addressSize();
   }

   private PlatformDependent() {
   }

   static {
      CAN_USE_CHM_V8 = HAS_UNSAFE && JAVA_VERSION < 8;
      DIRECT_BUFFER_PREFERRED = HAS_UNSAFE && !SystemPropertyUtil.getBoolean("io.netty.noPreferDirect", false);
      MAX_DIRECT_MEMORY = maxDirectMemory0();
      ARRAY_BASE_OFFSET = arrayBaseOffset0();
      HAS_JAVASSIST = hasJavassist0();
      TMPDIR = tmpdir0();
      BIT_MODE = bitMode0();
      ADDRESS_SIZE = addressSize0();
      if(logger.isDebugEnabled()) {
         logger.debug("-Dio.netty.noPreferDirect: {}", (Object)Boolean.valueOf(!DIRECT_BUFFER_PREFERRED));
      }

      if(!hasUnsafe() && !isAndroid()) {
         logger.info("Your platform does not provide complete low-level API for accessing direct buffers reliably. Unless explicitly requested, heap buffer will always be preferred to avoid potential system unstability.");
      }

   }
}
