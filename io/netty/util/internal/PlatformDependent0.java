package io.netty.util.internal;

import io.netty.util.internal.Cleaner0;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.UnsafeAtomicIntegerFieldUpdater;
import io.netty.util.internal.UnsafeAtomicLongFieldUpdater;
import io.netty.util.internal.UnsafeAtomicReferenceFieldUpdater;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import sun.misc.Unsafe;

final class PlatformDependent0 {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PlatformDependent0.class);
   private static final Unsafe UNSAFE;
   private static final boolean BIG_ENDIAN;
   private static final long ADDRESS_FIELD_OFFSET;
   private static final long UNSAFE_COPY_THRESHOLD = 1048576L;
   private static final boolean UNALIGNED;

   static boolean hasUnsafe() {
      return UNSAFE != null;
   }

   static void throwException(Throwable var0) {
      UNSAFE.throwException(var0);
   }

   static void freeDirectBuffer(ByteBuffer var0) {
      Cleaner0.freeDirectBuffer(var0);
   }

   static long directBufferAddress(ByteBuffer var0) {
      return getLong(var0, ADDRESS_FIELD_OFFSET);
   }

   static long arrayBaseOffset() {
      return (long)UNSAFE.arrayBaseOffset(byte[].class);
   }

   static Object getObject(Object var0, long var1) {
      return UNSAFE.getObject(var0, var1);
   }

   static Object getObjectVolatile(Object var0, long var1) {
      return UNSAFE.getObjectVolatile(var0, var1);
   }

   static int getInt(Object var0, long var1) {
      return UNSAFE.getInt(var0, var1);
   }

   private static long getLong(Object var0, long var1) {
      return UNSAFE.getLong(var0, var1);
   }

   static long objectFieldOffset(Field var0) {
      return UNSAFE.objectFieldOffset(var0);
   }

   static byte getByte(long var0) {
      return UNSAFE.getByte(var0);
   }

   static short getShort(long var0) {
      return UNALIGNED?UNSAFE.getShort(var0):(BIG_ENDIAN?(short)(getByte(var0) << 8 | getByte(var0 + 1L) & 255):(short)(getByte(var0 + 1L) << 8 | getByte(var0) & 255));
   }

   static int getInt(long var0) {
      return UNALIGNED?UNSAFE.getInt(var0):(BIG_ENDIAN?getByte(var0) << 24 | (getByte(var0 + 1L) & 255) << 16 | (getByte(var0 + 2L) & 255) << 8 | getByte(var0 + 3L) & 255:getByte(var0 + 3L) << 24 | (getByte(var0 + 2L) & 255) << 16 | (getByte(var0 + 1L) & 255) << 8 | getByte(var0) & 255);
   }

   static long getLong(long var0) {
      return UNALIGNED?UNSAFE.getLong(var0):(BIG_ENDIAN?(long)getByte(var0) << 56 | ((long)getByte(var0 + 1L) & 255L) << 48 | ((long)getByte(var0 + 2L) & 255L) << 40 | ((long)getByte(var0 + 3L) & 255L) << 32 | ((long)getByte(var0 + 4L) & 255L) << 24 | ((long)getByte(var0 + 5L) & 255L) << 16 | ((long)getByte(var0 + 6L) & 255L) << 8 | (long)getByte(var0 + 7L) & 255L:(long)getByte(var0 + 7L) << 56 | ((long)getByte(var0 + 6L) & 255L) << 48 | ((long)getByte(var0 + 5L) & 255L) << 40 | ((long)getByte(var0 + 4L) & 255L) << 32 | ((long)getByte(var0 + 3L) & 255L) << 24 | ((long)getByte(var0 + 2L) & 255L) << 16 | ((long)getByte(var0 + 1L) & 255L) << 8 | (long)getByte(var0) & 255L);
   }

   static void putOrderedObject(Object var0, long var1, Object var3) {
      UNSAFE.putOrderedObject(var0, var1, var3);
   }

   static void putByte(long var0, byte var2) {
      UNSAFE.putByte(var0, var2);
   }

   static void putShort(long var0, short var2) {
      if(UNALIGNED) {
         UNSAFE.putShort(var0, var2);
      } else if(BIG_ENDIAN) {
         putByte(var0, (byte)(var2 >>> 8));
         putByte(var0 + 1L, (byte)var2);
      } else {
         putByte(var0 + 1L, (byte)(var2 >>> 8));
         putByte(var0, (byte)var2);
      }

   }

   static void putInt(long var0, int var2) {
      if(UNALIGNED) {
         UNSAFE.putInt(var0, var2);
      } else if(BIG_ENDIAN) {
         putByte(var0, (byte)(var2 >>> 24));
         putByte(var0 + 1L, (byte)(var2 >>> 16));
         putByte(var0 + 2L, (byte)(var2 >>> 8));
         putByte(var0 + 3L, (byte)var2);
      } else {
         putByte(var0 + 3L, (byte)(var2 >>> 24));
         putByte(var0 + 2L, (byte)(var2 >>> 16));
         putByte(var0 + 1L, (byte)(var2 >>> 8));
         putByte(var0, (byte)var2);
      }

   }

   static void putLong(long var0, long var2) {
      if(UNALIGNED) {
         UNSAFE.putLong(var0, var2);
      } else if(BIG_ENDIAN) {
         putByte(var0, (byte)((int)(var2 >>> 56)));
         putByte(var0 + 1L, (byte)((int)(var2 >>> 48)));
         putByte(var0 + 2L, (byte)((int)(var2 >>> 40)));
         putByte(var0 + 3L, (byte)((int)(var2 >>> 32)));
         putByte(var0 + 4L, (byte)((int)(var2 >>> 24)));
         putByte(var0 + 5L, (byte)((int)(var2 >>> 16)));
         putByte(var0 + 6L, (byte)((int)(var2 >>> 8)));
         putByte(var0 + 7L, (byte)((int)var2));
      } else {
         putByte(var0 + 7L, (byte)((int)(var2 >>> 56)));
         putByte(var0 + 6L, (byte)((int)(var2 >>> 48)));
         putByte(var0 + 5L, (byte)((int)(var2 >>> 40)));
         putByte(var0 + 4L, (byte)((int)(var2 >>> 32)));
         putByte(var0 + 3L, (byte)((int)(var2 >>> 24)));
         putByte(var0 + 2L, (byte)((int)(var2 >>> 16)));
         putByte(var0 + 1L, (byte)((int)(var2 >>> 8)));
         putByte(var0, (byte)((int)var2));
      }

   }

   static void copyMemory(long var0, long var2, long var4) {
      while(var4 > 0L) {
         long var6 = Math.min(var4, 1048576L);
         UNSAFE.copyMemory(var0, var2, var6);
         var4 -= var6;
         var0 += var6;
         var2 += var6;
      }

   }

   static void copyMemory(Object var0, long var1, Object var3, long var4, long var6) {
      while(var6 > 0L) {
         long var8 = Math.min(var6, 1048576L);
         UNSAFE.copyMemory(var0, var1, var3, var4, var8);
         var6 -= var8;
         var1 += var8;
         var4 += var8;
      }

   }

   static <U, W> AtomicReferenceFieldUpdater<U, W> newAtomicReferenceFieldUpdater(Class<U> var0, String var1) throws Exception {
      return new UnsafeAtomicReferenceFieldUpdater(UNSAFE, var0, var1);
   }

   static <T> AtomicIntegerFieldUpdater<T> newAtomicIntegerFieldUpdater(Class<?> var0, String var1) throws Exception {
      return new UnsafeAtomicIntegerFieldUpdater(UNSAFE, var0, var1);
   }

   static <T> AtomicLongFieldUpdater<T> newAtomicLongFieldUpdater(Class<?> var0, String var1) throws Exception {
      return new UnsafeAtomicLongFieldUpdater(UNSAFE, var0, var1);
   }

   static ClassLoader getClassLoader(final Class<?> var0) {
      return System.getSecurityManager() == null?var0.getClassLoader():(ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public ClassLoader run() {
            return var0.getClassLoader();
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object run() {
            return this.run();
         }
      });
   }

   static ClassLoader getContextClassLoader() {
      return System.getSecurityManager() == null?Thread.currentThread().getContextClassLoader():(ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object run() {
            return this.run();
         }
      });
   }

   static ClassLoader getSystemClassLoader() {
      return System.getSecurityManager() == null?ClassLoader.getSystemClassLoader():(ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public ClassLoader run() {
            return ClassLoader.getSystemClassLoader();
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object run() {
            return this.run();
         }
      });
   }

   static int addressSize() {
      return UNSAFE.addressSize();
   }

   static long allocateMemory(long var0) {
      return UNSAFE.allocateMemory(var0);
   }

   static void freeMemory(long var0) {
      UNSAFE.freeMemory(var0);
   }

   private PlatformDependent0() {
   }

   static {
      BIG_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
      ByteBuffer var0 = ByteBuffer.allocateDirect(1);

      Field var1;
      try {
         var1 = Buffer.class.getDeclaredField("address");
         var1.setAccessible(true);
         if(var1.getLong(ByteBuffer.allocate(1)) != 0L) {
            var1 = null;
         } else if(var1.getLong(var0) == 0L) {
            var1 = null;
         }
      } catch (Throwable var10) {
         var1 = null;
      }

      logger.debug("java.nio.Buffer.address: {}", (Object)(var1 != null?"available":"unavailable"));
      Unsafe var2;
      if(var1 != null) {
         try {
            Field var3 = Unsafe.class.getDeclaredField("theUnsafe");
            var3.setAccessible(true);
            var2 = (Unsafe)var3.get((Object)null);
            logger.debug("sun.misc.Unsafe.theUnsafe: {}", (Object)(var2 != null?"available":"unavailable"));

            try {
               if(var2 != null) {
                  var2.getClass().getDeclaredMethod("copyMemory", new Class[]{Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE});
                  logger.debug("sun.misc.Unsafe.copyMemory: available");
               }
            } catch (NoSuchMethodError var7) {
               logger.debug("sun.misc.Unsafe.copyMemory: unavailable");
               throw var7;
            } catch (NoSuchMethodException var8) {
               logger.debug("sun.misc.Unsafe.copyMemory: unavailable");
               throw var8;
            }
         } catch (Throwable var9) {
            var2 = null;
         }
      } else {
         var2 = null;
      }

      UNSAFE = var2;
      if(var2 == null) {
         ADDRESS_FIELD_OFFSET = -1L;
         UNALIGNED = false;
      } else {
         ADDRESS_FIELD_OFFSET = objectFieldOffset(var1);

         boolean var11;
         try {
            Class var4 = Class.forName("java.nio.Bits", false, ClassLoader.getSystemClassLoader());
            Method var12 = var4.getDeclaredMethod("unaligned", new Class[0]);
            var12.setAccessible(true);
            var11 = Boolean.TRUE.equals(var12.invoke((Object)null, new Object[0]));
         } catch (Throwable var6) {
            String var5 = SystemPropertyUtil.get("os.arch", "");
            var11 = var5.matches("^(i[3-6]86|x86(_64)?|x64|amd64)$");
         }

         UNALIGNED = var11;
         logger.debug("java.nio.Bits.unaligned: {}", (Object)Boolean.valueOf(UNALIGNED));
      }

   }
}
