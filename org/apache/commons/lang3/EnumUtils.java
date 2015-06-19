package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

public class EnumUtils {
   private static final String NULL_ELEMENTS_NOT_PERMITTED = "null elements not permitted";
   private static final String CANNOT_STORE_S_S_VALUES_IN_S_BITS = "Cannot store %s %s values in %s bits";
   private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";
   private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";

   public EnumUtils() {
   }

   public static <E extends Enum<E>> Map<String, E> getEnumMap(Class<E> var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      Enum[] var2 = (Enum[])var0.getEnumConstants();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Enum var5 = var2[var4];
         var1.put(var5.name(), var5);
      }

      return var1;
   }

   public static <E extends Enum<E>> List<E> getEnumList(Class<E> var0) {
      return new ArrayList(Arrays.asList(var0.getEnumConstants()));
   }

   public static <E extends Enum<E>> boolean isValidEnum(Class<E> var0, String var1) {
      if(var1 == null) {
         return false;
      } else {
         try {
            Enum.valueOf(var0, var1);
            return true;
         } catch (IllegalArgumentException var3) {
            return false;
         }
      }
   }

   public static <E extends Enum<E>> E getEnum(Class<E> var0, String var1) {
      if(var1 == null) {
         return null;
      } else {
         try {
            return Enum.valueOf(var0, var1);
         } catch (IllegalArgumentException var3) {
            return null;
         }
      }
   }

   public static <E extends Enum<E>> long generateBitVector(Class<E> var0, Iterable<? extends E> var1) {
      checkBitVectorable(var0);
      Validate.notNull(var1);
      long var2 = 0L;

      Enum var5;
      for(Iterator var4 = var1.iterator(); var4.hasNext(); var2 |= (long)(1 << var5.ordinal())) {
         var5 = (Enum)var4.next();
         Validate.isTrue(var5 != null, "null elements not permitted", new Object[0]);
      }

      return var2;
   }

   public static <E extends Enum<E>> long[] generateBitVectors(Class<E> var0, Iterable<? extends E> var1) {
      asEnum(var0);
      Validate.notNull(var1);
      EnumSet var2 = EnumSet.noneOf(var0);
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Enum var4 = (Enum)var3.next();
         Validate.isTrue(var4 != null, "null elements not permitted", new Object[0]);
         var2.add(var4);
      }

      long[] var6 = new long[(((Enum[])var0.getEnumConstants()).length - 1) / 64 + 1];

      int var10001;
      Enum var5;
      for(Iterator var7 = var2.iterator(); var7.hasNext(); var6[var10001] |= (long)(1 << var5.ordinal() % 64)) {
         var5 = (Enum)var7.next();
         var10001 = var5.ordinal() / 64;
      }

      ArrayUtils.reverse(var6);
      return var6;
   }

   public static <E extends Enum<E>> long generateBitVector(Class<E> var0, E... var1) {
      Validate.noNullElements((Object[])var1);
      return generateBitVector(var0, (Iterable)Arrays.asList(var1));
   }

   public static <E extends Enum<E>> long[] generateBitVectors(Class<E> var0, E... var1) {
      asEnum(var0);
      Validate.noNullElements((Object[])var1);
      EnumSet var2 = EnumSet.noneOf(var0);
      Collections.addAll(var2, var1);
      long[] var3 = new long[(((Enum[])var0.getEnumConstants()).length - 1) / 64 + 1];

      int var10001;
      Enum var5;
      for(Iterator var4 = var2.iterator(); var4.hasNext(); var3[var10001] |= (long)(1 << var5.ordinal() % 64)) {
         var5 = (Enum)var4.next();
         var10001 = var5.ordinal() / 64;
      }

      ArrayUtils.reverse(var3);
      return var3;
   }

   public static <E extends Enum<E>> EnumSet<E> processBitVector(Class<E> var0, long var1) {
      checkBitVectorable(var0).getEnumConstants();
      return processBitVectors(var0, new long[]{var1});
   }

   public static <E extends Enum<E>> EnumSet<E> processBitVectors(Class<E> var0, long... var1) {
      EnumSet var2 = EnumSet.noneOf(asEnum(var0));
      long[] var3 = ArrayUtils.clone((long[])Validate.notNull(var1));
      ArrayUtils.reverse(var3);
      Enum[] var4 = (Enum[])var0.getEnumConstants();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Enum var7 = var4[var6];
         int var8 = var7.ordinal() / 64;
         if(var8 < var3.length && (var3[var8] & (long)(1 << var7.ordinal() % 64)) != 0L) {
            var2.add(var7);
         }
      }

      return var2;
   }

   private static <E extends Enum<E>> Class<E> checkBitVectorable(Class<E> var0) {
      Enum[] var1 = (Enum[])asEnum(var0).getEnumConstants();
      Validate.isTrue(var1.length <= 64, "Cannot store %s %s values in %s bits", new Object[]{Integer.valueOf(var1.length), var0.getSimpleName(), Integer.valueOf(64)});
      return var0;
   }

   private static <E extends Enum<E>> Class<E> asEnum(Class<E> var0) {
      Validate.notNull(var0, "EnumClass must be defined.", new Object[0]);
      Validate.isTrue(var0.isEnum(), "%s does not seem to be an Enum type", new Object[]{var0});
      return var0;
   }
}
