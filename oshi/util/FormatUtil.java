package oshi.util;

import java.math.BigDecimal;

public abstract class FormatUtil {
   private static final long kibiByte = 1024L;
   private static final long mebiByte = 1048576L;
   private static final long gibiByte = 1073741824L;
   private static final long tebiByte = 1099511627776L;
   private static final long pebiByte = 1125899906842624L;

   public FormatUtil() {
   }

   public static String formatBytes(long var0) {
      return var0 == 1L?String.format("%d byte", new Object[]{Long.valueOf(var0)}):(var0 < 1024L?String.format("%d bytes", new Object[]{Long.valueOf(var0)}):(var0 < 1048576L && var0 % 1024L == 0L?String.format("%.0f KB", new Object[]{Double.valueOf((double)var0 / 1024.0D)}):(var0 < 1048576L?String.format("%.1f KB", new Object[]{Double.valueOf((double)var0 / 1024.0D)}):(var0 < 1073741824L && var0 % 1048576L == 0L?String.format("%.0f MB", new Object[]{Double.valueOf((double)var0 / 1048576.0D)}):(var0 < 1073741824L?String.format("%.1f MB", new Object[]{Double.valueOf((double)var0 / 1048576.0D)}):(var0 % 1073741824L == 0L && var0 < 1099511627776L?String.format("%.0f GB", new Object[]{Double.valueOf((double)var0 / 1.073741824E9D)}):(var0 < 1099511627776L?String.format("%.1f GB", new Object[]{Double.valueOf((double)var0 / 1.073741824E9D)}):(var0 % 1099511627776L == 0L && var0 < 1125899906842624L?String.format("%.0f TiB", new Object[]{Double.valueOf((double)var0 / 1.099511627776E12D)}):(var0 < 1125899906842624L?String.format("%.1f TiB", new Object[]{Double.valueOf((double)var0 / 1.099511627776E12D)}):String.format("%d bytes", new Object[]{Long.valueOf(var0)}))))))))));
   }

   public static float round(float var0, int var1) {
      BigDecimal var2 = new BigDecimal(Float.toString(var0));
      var2 = var2.setScale(var1, 4);
      return var2.floatValue();
   }
}
