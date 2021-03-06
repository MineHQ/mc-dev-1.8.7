package com.google.common.base;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Defaults {
   private static final Map<Class<?>, Object> DEFAULTS;

   private Defaults() {
   }

   private static <T> void put(Map<Class<?>, Object> var0, Class<T> var1, T var2) {
      var0.put(var1, var2);
   }

   public static <T> T defaultValue(Class<T> var0) {
      Object var1 = DEFAULTS.get(Preconditions.checkNotNull(var0));
      return var1;
   }

   static {
      HashMap var0 = new HashMap();
      put(var0, Boolean.TYPE, Boolean.valueOf(false));
      put(var0, Character.TYPE, Character.valueOf('\u0000'));
      put(var0, Byte.TYPE, Byte.valueOf((byte)0));
      put(var0, Short.TYPE, Short.valueOf((short)0));
      put(var0, Integer.TYPE, Integer.valueOf(0));
      put(var0, Long.TYPE, Long.valueOf(0L));
      put(var0, Float.TYPE, Float.valueOf(0.0F));
      put(var0, Double.TYPE, Double.valueOf(0.0D));
      DEFAULTS = Collections.unmodifiableMap(var0);
   }
}
