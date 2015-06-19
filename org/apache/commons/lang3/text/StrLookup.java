package org.apache.commons.lang3.text;

import java.util.Map;
import java.util.Properties;

public abstract class StrLookup<V> {
   private static final StrLookup<String> NONE_LOOKUP = new StrLookup.MapStrLookup((Map)null);
   private static final StrLookup<String> SYSTEM_PROPERTIES_LOOKUP;

   public static StrLookup<?> noneLookup() {
      return NONE_LOOKUP;
   }

   public static StrLookup<String> systemPropertiesLookup() {
      return SYSTEM_PROPERTIES_LOOKUP;
   }

   public static <V> StrLookup<V> mapLookup(Map<String, V> var0) {
      return new StrLookup.MapStrLookup(var0);
   }

   protected StrLookup() {
   }

   public abstract String lookup(String var1);

   static {
      Object var0 = null;

      try {
         Properties var1 = System.getProperties();
         var0 = new StrLookup.MapStrLookup(var1);
      } catch (SecurityException var3) {
         var0 = NONE_LOOKUP;
      }

      SYSTEM_PROPERTIES_LOOKUP = (StrLookup)var0;
   }

   static class MapStrLookup<V> extends StrLookup<V> {
      private final Map<String, V> map;

      MapStrLookup(Map<String, V> var1) {
         this.map = var1;
      }

      public String lookup(String var1) {
         if(this.map == null) {
            return null;
         } else {
            Object var2 = this.map.get(var1);
            return var2 == null?null:var2.toString();
         }
      }
   }
}
