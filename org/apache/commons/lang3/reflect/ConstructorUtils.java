package org.apache.commons.lang3.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MemberUtils;

public class ConstructorUtils {
   public ConstructorUtils() {
   }

   public static <T> T invokeConstructor(Class<T> var0, Object... var1) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      var1 = ArrayUtils.nullToEmpty(var1);
      Class[] var2 = ClassUtils.toClass(var1);
      return invokeConstructor(var0, var1, var2);
   }

   public static <T> T invokeConstructor(Class<T> var0, Object[] var1, Class<?>[] var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      var1 = ArrayUtils.nullToEmpty(var1);
      var2 = ArrayUtils.nullToEmpty(var2);
      Constructor var3 = getMatchingAccessibleConstructor(var0, var2);
      if(var3 == null) {
         throw new NoSuchMethodException("No such accessible constructor on object: " + var0.getName());
      } else {
         return var3.newInstance(var1);
      }
   }

   public static <T> T invokeExactConstructor(Class<T> var0, Object... var1) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      var1 = ArrayUtils.nullToEmpty(var1);
      Class[] var2 = ClassUtils.toClass(var1);
      return invokeExactConstructor(var0, var1, var2);
   }

   public static <T> T invokeExactConstructor(Class<T> var0, Object[] var1, Class<?>[] var2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
      var1 = ArrayUtils.nullToEmpty(var1);
      var2 = ArrayUtils.nullToEmpty(var2);
      Constructor var3 = getAccessibleConstructor(var0, var2);
      if(var3 == null) {
         throw new NoSuchMethodException("No such accessible constructor on object: " + var0.getName());
      } else {
         return var3.newInstance(var1);
      }
   }

   public static <T> Constructor<T> getAccessibleConstructor(Class<T> var0, Class... var1) {
      Validate.notNull(var0, "class cannot be null", new Object[0]);

      try {
         return getAccessibleConstructor(var0.getConstructor(var1));
      } catch (NoSuchMethodException var3) {
         return null;
      }
   }

   public static <T> Constructor<T> getAccessibleConstructor(Constructor<T> var0) {
      Validate.notNull(var0, "constructor cannot be null", new Object[0]);
      return MemberUtils.isAccessible(var0) && isAccessible(var0.getDeclaringClass())?var0:null;
   }

   public static <T> Constructor<T> getMatchingAccessibleConstructor(Class<T> var0, Class... var1) {
      Validate.notNull(var0, "class cannot be null", new Object[0]);

      Constructor var2;
      try {
         var2 = var0.getConstructor(var1);
         MemberUtils.setAccessibleWorkaround(var2);
         return var2;
      } catch (NoSuchMethodException var9) {
         var2 = null;
         Constructor[] var3 = var0.getConstructors();
         Constructor[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Constructor var7 = var4[var6];
            if(ClassUtils.isAssignable(var1, var7.getParameterTypes(), true)) {
               var7 = getAccessibleConstructor(var7);
               if(var7 != null) {
                  MemberUtils.setAccessibleWorkaround(var7);
                  if(var2 == null || MemberUtils.compareParameterTypes(var7.getParameterTypes(), var2.getParameterTypes(), var1) < 0) {
                     var2 = var7;
                  }
               }
            }
         }

         return var2;
      }
   }

   private static boolean isAccessible(Class<?> var0) {
      for(Class var1 = var0; var1 != null; var1 = var1.getEnclosingClass()) {
         if(!Modifier.isPublic(var1.getModifiers())) {
            return false;
         }
      }

      return true;
   }
}
