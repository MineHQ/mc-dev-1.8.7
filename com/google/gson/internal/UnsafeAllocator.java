package com.google.gson.internal;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class UnsafeAllocator {
   public UnsafeAllocator() {
   }

   public abstract <T> T newInstance(Class<T> var1) throws Exception;

   public static UnsafeAllocator create() {
      try {
         Class var7 = Class.forName("sun.misc.Unsafe");
         Field var8 = var7.getDeclaredField("theUnsafe");
         var8.setAccessible(true);
         final Object var9 = var8.get((Object)null);
         final Method var3 = var7.getMethod("allocateInstance", new Class[]{Class.class});
         return new UnsafeAllocator() {
            public <T> T newInstance(Class<T> var1) throws Exception {
               return var3.invoke(var9, new Object[]{var1});
            }
         };
      } catch (Exception var6) {
         final Method var0;
         try {
            var0 = ObjectInputStream.class.getDeclaredMethod("newInstance", new Class[]{Class.class, Class.class});
            var0.setAccessible(true);
            return new UnsafeAllocator() {
               public <T> T newInstance(Class<T> var1) throws Exception {
                  return var0.invoke((Object)null, new Object[]{var1, Object.class});
               }
            };
         } catch (Exception var5) {
            try {
               var0 = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", new Class[]{Class.class});
               var0.setAccessible(true);
               final int var1 = ((Integer)var0.invoke((Object)null, new Object[]{Object.class})).intValue();
               final Method var2 = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[]{Class.class, Integer.TYPE});
               var2.setAccessible(true);
               return new UnsafeAllocator() {
                  public <T> T newInstance(Class<T> var1x) throws Exception {
                     return var2.invoke((Object)null, new Object[]{var1x, Integer.valueOf(var1)});
                  }
               };
            } catch (Exception var4) {
               return new UnsafeAllocator() {
                  public <T> T newInstance(Class<T> var1) {
                     throw new UnsupportedOperationException("Cannot allocate " + var1);
                  }
               };
            }
         }
      }
   }
}
