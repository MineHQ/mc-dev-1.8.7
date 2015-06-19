package org.apache.commons.lang3.time;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

abstract class FormatCache<F extends Format> {
   static final int NONE = -1;
   private final ConcurrentMap<FormatCache.MultipartKey, F> cInstanceCache = new ConcurrentHashMap(7);
   private static final ConcurrentMap<FormatCache.MultipartKey, String> cDateTimeInstanceCache = new ConcurrentHashMap(7);

   FormatCache() {
   }

   public F getInstance() {
      return this.getDateTimeInstance(3, 3, TimeZone.getDefault(), Locale.getDefault());
   }

   public F getInstance(String var1, TimeZone var2, Locale var3) {
      if(var1 == null) {
         throw new NullPointerException("pattern must not be null");
      } else {
         if(var2 == null) {
            var2 = TimeZone.getDefault();
         }

         if(var3 == null) {
            var3 = Locale.getDefault();
         }

         FormatCache.MultipartKey var4 = new FormatCache.MultipartKey(new Object[]{var1, var2, var3});
         Format var5 = (Format)this.cInstanceCache.get(var4);
         if(var5 == null) {
            var5 = this.createInstance(var1, var2, var3);
            Format var6 = (Format)this.cInstanceCache.putIfAbsent(var4, var5);
            if(var6 != null) {
               var5 = var6;
            }
         }

         return var5;
      }
   }

   protected abstract F createInstance(String var1, TimeZone var2, Locale var3);

   private F getDateTimeInstance(Integer var1, Integer var2, TimeZone var3, Locale var4) {
      if(var4 == null) {
         var4 = Locale.getDefault();
      }

      String var5 = getPatternForStyle(var1, var2, var4);
      return this.getInstance(var5, var3, var4);
   }

   F getDateTimeInstance(int var1, int var2, TimeZone var3, Locale var4) {
      return this.getDateTimeInstance(Integer.valueOf(var1), Integer.valueOf(var2), var3, var4);
   }

   F getDateInstance(int var1, TimeZone var2, Locale var3) {
      return this.getDateTimeInstance(Integer.valueOf(var1), (Integer)null, var2, var3);
   }

   F getTimeInstance(int var1, TimeZone var2, Locale var3) {
      return this.getDateTimeInstance((Integer)null, Integer.valueOf(var1), var2, var3);
   }

   static String getPatternForStyle(Integer var0, Integer var1, Locale var2) {
      FormatCache.MultipartKey var3 = new FormatCache.MultipartKey(new Object[]{var0, var1, var2});
      String var4 = (String)cDateTimeInstanceCache.get(var3);
      if(var4 == null) {
         try {
            DateFormat var5;
            if(var0 == null) {
               var5 = DateFormat.getTimeInstance(var1.intValue(), var2);
            } else if(var1 == null) {
               var5 = DateFormat.getDateInstance(var0.intValue(), var2);
            } else {
               var5 = DateFormat.getDateTimeInstance(var0.intValue(), var1.intValue(), var2);
            }

            var4 = ((SimpleDateFormat)var5).toPattern();
            String var6 = (String)cDateTimeInstanceCache.putIfAbsent(var3, var4);
            if(var6 != null) {
               var4 = var6;
            }
         } catch (ClassCastException var7) {
            throw new IllegalArgumentException("No date time pattern for locale: " + var2);
         }
      }

      return var4;
   }

   private static class MultipartKey {
      private final Object[] keys;
      private int hashCode;

      public MultipartKey(Object... var1) {
         this.keys = var1;
      }

      public boolean equals(Object var1) {
         return Arrays.equals(this.keys, ((FormatCache.MultipartKey)var1).keys);
      }

      public int hashCode() {
         if(this.hashCode == 0) {
            int var1 = 0;
            Object[] var2 = this.keys;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Object var5 = var2[var4];
               if(var5 != null) {
                  var1 = var1 * 7 + var5.hashCode();
               }
            }

            this.hashCode = var1;
         }

         return this.hashCode;
      }
   }
}
