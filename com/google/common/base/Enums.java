package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Platform;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
@Beta
public final class Enums {
   @GwtIncompatible("java.lang.ref.WeakReference")
   private static final Map<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>> enumConstantCache = new WeakHashMap();

   private Enums() {
   }

   @GwtIncompatible("reflection")
   public static Field getField(Enum<?> var0) {
      Class var1 = var0.getDeclaringClass();

      try {
         return var1.getDeclaredField(var0.name());
      } catch (NoSuchFieldException var3) {
         throw new AssertionError(var3);
      }
   }

   /** @deprecated */
   @Deprecated
   public static <T extends Enum<T>> Function<String, T> valueOfFunction(Class<T> var0) {
      return new Enums.ValueOfFunction(var0);
   }

   public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> var0, String var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return Platform.getEnumIfPresent(var0, var1);
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   private static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> populateCache(Class<T> var0) {
      HashMap var1 = new HashMap();
      Iterator var2 = EnumSet.allOf(var0).iterator();

      while(var2.hasNext()) {
         Enum var3 = (Enum)var2.next();
         var1.put(var3.name(), new WeakReference(var3));
      }

      enumConstantCache.put(var0, var1);
      return var1;
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> getEnumConstants(Class<T> var0) {
      Map var1 = enumConstantCache;
      synchronized(enumConstantCache) {
         Map var2 = (Map)enumConstantCache.get(var0);
         if(var2 == null) {
            var2 = populateCache(var0);
         }

         return var2;
      }
   }

   public static <T extends Enum<T>> Converter<String, T> stringConverter(Class<T> var0) {
      return new Enums.StringConverter(var0);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static final class StringConverter<T extends Enum<T>> extends Converter<String, T> implements Serializable {
      private final Class<T> enumClass;
      private static final long serialVersionUID = 0L;

      StringConverter(Class<T> var1) {
         this.enumClass = (Class)Preconditions.checkNotNull(var1);
      }

      protected T doForward(String var1) {
         return Enum.valueOf(this.enumClass, var1);
      }

      protected String doBackward(T var1) {
         return var1.name();
      }

      public boolean equals(@Nullable Object var1) {
         if(var1 instanceof Enums.StringConverter) {
            Enums.StringConverter var2 = (Enums.StringConverter)var1;
            return this.enumClass.equals(var2.enumClass);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.enumClass.hashCode();
      }

      public String toString() {
         return "Enums.stringConverter(" + this.enumClass.getName() + ".class)";
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object doBackward(Object var1) {
         return this.doBackward((Enum)var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object doForward(Object var1) {
         return this.doForward((String)var1);
      }
   }

   private static final class ValueOfFunction<T extends Enum<T>> implements Function<String, T>, Serializable {
      private final Class<T> enumClass;
      private static final long serialVersionUID = 0L;

      private ValueOfFunction(Class<T> var1) {
         this.enumClass = (Class)Preconditions.checkNotNull(var1);
      }

      public T apply(String var1) {
         try {
            return Enum.valueOf(this.enumClass, var1);
         } catch (IllegalArgumentException var3) {
            return null;
         }
      }

      public boolean equals(@Nullable Object var1) {
         return var1 instanceof Enums.ValueOfFunction && this.enumClass.equals(((Enums.ValueOfFunction)var1).enumClass);
      }

      public int hashCode() {
         return this.enumClass.hashCode();
      }

      public String toString() {
         return "Enums.valueOf(" + this.enumClass + ")";
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object apply(Object var1) {
         return this.apply((String)var1);
      }

      // $FF: synthetic method
      ValueOfFunction(Class var1, Enums.SyntheticClass_1 var2) {
         this(var1);
      }
   }
}
