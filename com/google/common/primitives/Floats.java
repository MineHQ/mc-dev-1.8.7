package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class Floats {
   public static final int BYTES = 4;

   private Floats() {
   }

   public static int hashCode(float var0) {
      return Float.valueOf(var0).hashCode();
   }

   public static int compare(float var0, float var1) {
      return Float.compare(var0, var1);
   }

   public static boolean isFinite(float var0) {
      return Float.NEGATIVE_INFINITY < var0 & var0 < Float.POSITIVE_INFINITY;
   }

   public static boolean contains(float[] var0, float var1) {
      float[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         float var5 = var2[var4];
         if(var5 == var1) {
            return true;
         }
      }

      return false;
   }

   public static int indexOf(float[] var0, float var1) {
      return indexOf(var0, var1, 0, var0.length);
   }

   private static int indexOf(float[] var0, float var1, int var2, int var3) {
      for(int var4 = var2; var4 < var3; ++var4) {
         if(var0[var4] == var1) {
            return var4;
         }
      }

      return -1;
   }

   public static int indexOf(float[] var0, float[] var1) {
      Preconditions.checkNotNull(var0, "array");
      Preconditions.checkNotNull(var1, "target");
      if(var1.length == 0) {
         return 0;
      } else {
         label28:
         for(int var2 = 0; var2 < var0.length - var1.length + 1; ++var2) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               if(var0[var2 + var3] != var1[var3]) {
                  continue label28;
               }
            }

            return var2;
         }

         return -1;
      }
   }

   public static int lastIndexOf(float[] var0, float var1) {
      return lastIndexOf(var0, var1, 0, var0.length);
   }

   private static int lastIndexOf(float[] var0, float var1, int var2, int var3) {
      for(int var4 = var3 - 1; var4 >= var2; --var4) {
         if(var0[var4] == var1) {
            return var4;
         }
      }

      return -1;
   }

   public static float min(float... var0) {
      Preconditions.checkArgument(var0.length > 0);
      float var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         var1 = Math.min(var1, var0[var2]);
      }

      return var1;
   }

   public static float max(float... var0) {
      Preconditions.checkArgument(var0.length > 0);
      float var1 = var0[0];

      for(int var2 = 1; var2 < var0.length; ++var2) {
         var1 = Math.max(var1, var0[var2]);
      }

      return var1;
   }

   public static float[] concat(float[]... var0) {
      int var1 = 0;
      float[][] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         float[] var5 = var2[var4];
         var1 += var5.length;
      }

      float[] var8 = new float[var1];
      var3 = 0;
      float[][] var9 = var0;
      int var10 = var0.length;

      for(int var6 = 0; var6 < var10; ++var6) {
         float[] var7 = var9[var6];
         System.arraycopy(var7, 0, var8, var3, var7.length);
         var3 += var7.length;
      }

      return var8;
   }

   @Beta
   public static Converter<String, Float> stringConverter() {
      return Floats.FloatConverter.INSTANCE;
   }

   public static float[] ensureCapacity(float[] var0, int var1, int var2) {
      Preconditions.checkArgument(var1 >= 0, "Invalid minLength: %s", new Object[]{Integer.valueOf(var1)});
      Preconditions.checkArgument(var2 >= 0, "Invalid padding: %s", new Object[]{Integer.valueOf(var2)});
      return var0.length < var1?copyOf(var0, var1 + var2):var0;
   }

   private static float[] copyOf(float[] var0, int var1) {
      float[] var2 = new float[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   public static String join(String var0, float... var1) {
      Preconditions.checkNotNull(var0);
      if(var1.length == 0) {
         return "";
      } else {
         StringBuilder var2 = new StringBuilder(var1.length * 12);
         var2.append(var1[0]);

         for(int var3 = 1; var3 < var1.length; ++var3) {
            var2.append(var0).append(var1[var3]);
         }

         return var2.toString();
      }
   }

   public static Comparator<float[]> lexicographicalComparator() {
      return Floats.LexicographicalComparator.INSTANCE;
   }

   public static float[] toArray(Collection<? extends Number> var0) {
      if(var0 instanceof Floats.FloatArrayAsList) {
         return ((Floats.FloatArrayAsList)var0).toFloatArray();
      } else {
         Object[] var1 = var0.toArray();
         int var2 = var1.length;
         float[] var3 = new float[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = ((Number)Preconditions.checkNotNull(var1[var4])).floatValue();
         }

         return var3;
      }
   }

   public static List<Float> asList(float... var0) {
      return (List)(var0.length == 0?Collections.emptyList():new Floats.FloatArrayAsList(var0));
   }

   @Nullable
   @GwtIncompatible("regular expressions")
   @Beta
   public static Float tryParse(String var0) {
      if(Doubles.FLOATING_POINT_PATTERN.matcher(var0).matches()) {
         try {
            return Float.valueOf(Float.parseFloat(var0));
         } catch (NumberFormatException var2) {
            ;
         }
      }

      return null;
   }

   @GwtCompatible
   private static class FloatArrayAsList extends AbstractList<Float> implements RandomAccess, Serializable {
      final float[] array;
      final int start;
      final int end;
      private static final long serialVersionUID = 0L;

      FloatArrayAsList(float[] var1) {
         this(var1, 0, var1.length);
      }

      FloatArrayAsList(float[] var1, int var2, int var3) {
         this.array = var1;
         this.start = var2;
         this.end = var3;
      }

      public int size() {
         return this.end - this.start;
      }

      public boolean isEmpty() {
         return false;
      }

      public Float get(int var1) {
         Preconditions.checkElementIndex(var1, this.size());
         return Float.valueOf(this.array[this.start + var1]);
      }

      public boolean contains(Object var1) {
         return var1 instanceof Float && Floats.indexOf(this.array, ((Float)var1).floatValue(), this.start, this.end) != -1;
      }

      public int indexOf(Object var1) {
         if(var1 instanceof Float) {
            int var2 = Floats.indexOf(this.array, ((Float)var1).floatValue(), this.start, this.end);
            if(var2 >= 0) {
               return var2 - this.start;
            }
         }

         return -1;
      }

      public int lastIndexOf(Object var1) {
         if(var1 instanceof Float) {
            int var2 = Floats.lastIndexOf(this.array, ((Float)var1).floatValue(), this.start, this.end);
            if(var2 >= 0) {
               return var2 - this.start;
            }
         }

         return -1;
      }

      public Float set(int var1, Float var2) {
         Preconditions.checkElementIndex(var1, this.size());
         float var3 = this.array[this.start + var1];
         this.array[this.start + var1] = ((Float)Preconditions.checkNotNull(var2)).floatValue();
         return Float.valueOf(var3);
      }

      public List<Float> subList(int var1, int var2) {
         int var3 = this.size();
         Preconditions.checkPositionIndexes(var1, var2, var3);
         return (List)(var1 == var2?Collections.emptyList():new Floats.FloatArrayAsList(this.array, this.start + var1, this.start + var2));
      }

      public boolean equals(Object var1) {
         if(var1 == this) {
            return true;
         } else if(var1 instanceof Floats.FloatArrayAsList) {
            Floats.FloatArrayAsList var2 = (Floats.FloatArrayAsList)var1;
            int var3 = this.size();
            if(var2.size() != var3) {
               return false;
            } else {
               for(int var4 = 0; var4 < var3; ++var4) {
                  if(this.array[this.start + var4] != var2.array[var2.start + var4]) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return super.equals(var1);
         }
      }

      public int hashCode() {
         int var1 = 1;

         for(int var2 = this.start; var2 < this.end; ++var2) {
            var1 = 31 * var1 + Floats.hashCode(this.array[var2]);
         }

         return var1;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(this.size() * 12);
         var1.append('[').append(this.array[this.start]);

         for(int var2 = this.start + 1; var2 < this.end; ++var2) {
            var1.append(", ").append(this.array[var2]);
         }

         return var1.append(']').toString();
      }

      float[] toFloatArray() {
         int var1 = this.size();
         float[] var2 = new float[var1];
         System.arraycopy(this.array, this.start, var2, 0, var1);
         return var2;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object set(int var1, Object var2) {
         return this.set(var1, (Float)var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get(int var1) {
         return this.get(var1);
      }
   }

   private static enum LexicographicalComparator implements Comparator<float[]> {
      INSTANCE;

      private LexicographicalComparator() {
      }

      public int compare(float[] var1, float[] var2) {
         int var3 = Math.min(var1.length, var2.length);

         for(int var4 = 0; var4 < var3; ++var4) {
            int var5 = Float.compare(var1[var4], var2[var4]);
            if(var5 != 0) {
               return var5;
            }
         }

         return var1.length - var2.length;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public int compare(Object var1, Object var2) {
         return this.compare((float[])var1, (float[])var2);
      }
   }

   private static final class FloatConverter extends Converter<String, Float> implements Serializable {
      static final Floats.FloatConverter INSTANCE = new Floats.FloatConverter();
      private static final long serialVersionUID = 1L;

      private FloatConverter() {
      }

      protected Float doForward(String var1) {
         return Float.valueOf(var1);
      }

      protected String doBackward(Float var1) {
         return var1.toString();
      }

      public String toString() {
         return "Floats.stringConverter()";
      }

      private Object readResolve() {
         return INSTANCE;
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object doBackward(Object var1) {
         return this.doBackward((Float)var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object doForward(Object var1) {
         return this.doForward((String)var1);
      }
   }
}
