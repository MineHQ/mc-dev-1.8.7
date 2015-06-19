package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible
public final class Objects {
   private Objects() {
   }

   @CheckReturnValue
   public static boolean equal(@Nullable Object var0, @Nullable Object var1) {
      return var0 == var1 || var0 != null && var0.equals(var1);
   }

   public static int hashCode(@Nullable Object... var0) {
      return Arrays.hashCode(var0);
   }

   public static Objects.ToStringHelper toStringHelper(Object var0) {
      return new Objects.ToStringHelper(simpleName(var0.getClass()));
   }

   public static Objects.ToStringHelper toStringHelper(Class<?> var0) {
      return new Objects.ToStringHelper(simpleName(var0));
   }

   public static Objects.ToStringHelper toStringHelper(String var0) {
      return new Objects.ToStringHelper(var0);
   }

   private static String simpleName(Class<?> var0) {
      String var1 = var0.getName();
      var1 = var1.replaceAll("\\$[0-9]+", "\\$");
      int var2 = var1.lastIndexOf(36);
      if(var2 == -1) {
         var2 = var1.lastIndexOf(46);
      }

      return var1.substring(var2 + 1);
   }

   public static <T> T firstNonNull(@Nullable T var0, @Nullable T var1) {
      return var0 != null?var0:Preconditions.checkNotNull(var1);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   public static final class ToStringHelper {
      private final String className;
      private Objects.ToStringHelper.ToStringHelper$ValueHolder holderHead;
      private Objects.ToStringHelper.ToStringHelper$ValueHolder holderTail;
      private boolean omitNullValues;

      private ToStringHelper(String var1) {
         this.holderHead = new Objects.ToStringHelper.ToStringHelper$ValueHolder();
         this.holderTail = this.holderHead;
         this.omitNullValues = false;
         this.className = (String)Preconditions.checkNotNull(var1);
      }

      public Objects.ToStringHelper omitNullValues() {
         this.omitNullValues = true;
         return this;
      }

      public Objects.ToStringHelper add(String var1, @Nullable Object var2) {
         return this.addHolder(var1, var2);
      }

      public Objects.ToStringHelper add(String var1, boolean var2) {
         return this.addHolder(var1, String.valueOf(var2));
      }

      public Objects.ToStringHelper add(String var1, char var2) {
         return this.addHolder(var1, String.valueOf(var2));
      }

      public Objects.ToStringHelper add(String var1, double var2) {
         return this.addHolder(var1, String.valueOf(var2));
      }

      public Objects.ToStringHelper add(String var1, float var2) {
         return this.addHolder(var1, String.valueOf(var2));
      }

      public Objects.ToStringHelper add(String var1, int var2) {
         return this.addHolder(var1, String.valueOf(var2));
      }

      public Objects.ToStringHelper add(String var1, long var2) {
         return this.addHolder(var1, String.valueOf(var2));
      }

      public Objects.ToStringHelper addValue(@Nullable Object var1) {
         return this.addHolder(var1);
      }

      public Objects.ToStringHelper addValue(boolean var1) {
         return this.addHolder(String.valueOf(var1));
      }

      public Objects.ToStringHelper addValue(char var1) {
         return this.addHolder(String.valueOf(var1));
      }

      public Objects.ToStringHelper addValue(double var1) {
         return this.addHolder(String.valueOf(var1));
      }

      public Objects.ToStringHelper addValue(float var1) {
         return this.addHolder(String.valueOf(var1));
      }

      public Objects.ToStringHelper addValue(int var1) {
         return this.addHolder(String.valueOf(var1));
      }

      public Objects.ToStringHelper addValue(long var1) {
         return this.addHolder(String.valueOf(var1));
      }

      public String toString() {
         boolean var1 = this.omitNullValues;
         String var2 = "";
         StringBuilder var3 = (new StringBuilder(32)).append(this.className).append('{');

         for(Objects.ToStringHelper.ToStringHelper$ValueHolder var4 = this.holderHead.next; var4 != null; var4 = var4.next) {
            if(!var1 || var4.value != null) {
               var3.append(var2);
               var2 = ", ";
               if(var4.name != null) {
                  var3.append(var4.name).append('=');
               }

               var3.append(var4.value);
            }
         }

         return var3.append('}').toString();
      }

      private Objects.ToStringHelper.ToStringHelper$ValueHolder addHolder() {
         Objects.ToStringHelper.ToStringHelper$ValueHolder var1 = new Objects.ToStringHelper.ToStringHelper$ValueHolder();
         this.holderTail = this.holderTail.next = var1;
         return var1;
      }

      private Objects.ToStringHelper addHolder(@Nullable Object var1) {
         Objects.ToStringHelper.ToStringHelper$ValueHolder var2 = this.addHolder();
         var2.value = var1;
         return this;
      }

      private Objects.ToStringHelper addHolder(String var1, @Nullable Object var2) {
         Objects.ToStringHelper.ToStringHelper$ValueHolder var3 = this.addHolder();
         var3.value = var2;
         var3.name = (String)Preconditions.checkNotNull(var1);
         return this;
      }

      // $FF: synthetic method
      ToStringHelper(String var1, Objects.SyntheticClass_1 var2) {
         this(var1);
      }

      private static final class ToStringHelper$ValueHolder {
         String name;
         Object value;
         Objects.ToStringHelper.ToStringHelper$ValueHolder next;

         private ToStringHelper$ValueHolder() {
         }

         // $FF: synthetic method
         ToStringHelper$ValueHolder(Objects.SyntheticClass_1 var1) {
            this();
         }
      }
   }
}
