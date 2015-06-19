package com.sun.jna.platform;

import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class RasterRangesUtils {
   private static final int[] subColMasks = new int[]{128, 64, 32, 16, 8, 4, 2, 1};
   private static final Comparator<Object> COMPARATOR = new Comparator() {
      public int compare(Object var1, Object var2) {
         return ((Rectangle)var1).x - ((Rectangle)var2).x;
      }
   };

   public RasterRangesUtils() {
   }

   public static boolean outputOccupiedRanges(Raster var0, RasterRangesUtils.RangesOutput var1) {
      Rectangle var2 = var0.getBounds();
      SampleModel var3 = var0.getSampleModel();
      boolean var4 = var3.getNumBands() == 4;
      if(var0.getParent() == null && var2.x == 0 && var2.y == 0) {
         DataBuffer var5 = var0.getDataBuffer();
         if(var5.getNumBanks() == 1) {
            if(var3 instanceof MultiPixelPackedSampleModel) {
               MultiPixelPackedSampleModel var6 = (MultiPixelPackedSampleModel)var3;
               if(var6.getPixelBitStride() == 1) {
                  return outputOccupiedRangesOfBinaryPixels(((DataBufferByte)var5).getData(), var2.width, var2.height, var1);
               }
            } else if(var3 instanceof SinglePixelPackedSampleModel && var3.getDataType() == 3) {
               return outputOccupiedRanges(((DataBufferInt)var5).getData(), var2.width, var2.height, var4?-16777216:16777215, var1);
            }
         }
      }

      int[] var7 = var0.getPixels(0, 0, var2.width, var2.height, (int[])null);
      return outputOccupiedRanges(var7, var2.width, var2.height, var4?-16777216:16777215, var1);
   }

   public static boolean outputOccupiedRangesOfBinaryPixels(byte[] var0, int var1, int var2, RasterRangesUtils.RangesOutput var3) {
      HashSet var4 = new HashSet();
      Object var5 = Collections.EMPTY_SET;
      int var6 = var0.length / var2;

      for(int var7 = 0; var7 < var2; ++var7) {
         TreeSet var8 = new TreeSet(COMPARATOR);
         int var9 = var7 * var6;
         int var10 = -1;

         for(int var11 = 0; var11 < var6; ++var11) {
            int var12 = var11 << 3;
            byte var13 = var0[var9 + var11];
            if(var13 == 0) {
               if(var10 >= 0) {
                  var8.add(new Rectangle(var10, var7, var12 - var10, 1));
                  var10 = -1;
               }
            } else if(var13 == 255) {
               if(var10 < 0) {
                  var10 = var12;
               }
            } else {
               for(int var14 = 0; var14 < 8; ++var14) {
                  int var15 = var12 | var14;
                  if((var13 & subColMasks[var14]) != 0) {
                     if(var10 < 0) {
                        var10 = var15;
                     }
                  } else if(var10 >= 0) {
                     var8.add(new Rectangle(var10, var7, var15 - var10, 1));
                     var10 = -1;
                  }
               }
            }
         }

         if(var10 >= 0) {
            var8.add(new Rectangle(var10, var7, var1 - var10, 1));
         }

         Set var18 = mergeRects((Set)var5, var8);
         var4.addAll(var18);
         var5 = var8;
      }

      var4.addAll((Collection)var5);
      Iterator var16 = var4.iterator();

      Rectangle var17;
      do {
         if(!var16.hasNext()) {
            return true;
         }

         var17 = (Rectangle)var16.next();
      } while(var3.outputRange(var17.x, var17.y, var17.width, var17.height));

      return false;
   }

   public static boolean outputOccupiedRanges(int[] var0, int var1, int var2, int var3, RasterRangesUtils.RangesOutput var4) {
      HashSet var5 = new HashSet();
      Object var6 = Collections.EMPTY_SET;

      for(int var7 = 0; var7 < var2; ++var7) {
         TreeSet var8 = new TreeSet(COMPARATOR);
         int var9 = var7 * var1;
         int var10 = -1;

         for(int var11 = 0; var11 < var1; ++var11) {
            if((var0[var9 + var11] & var3) != 0) {
               if(var10 < 0) {
                  var10 = var11;
               }
            } else if(var10 >= 0) {
               var8.add(new Rectangle(var10, var7, var11 - var10, 1));
               var10 = -1;
            }
         }

         if(var10 >= 0) {
            var8.add(new Rectangle(var10, var7, var1 - var10, 1));
         }

         Set var14 = mergeRects((Set)var6, var8);
         var5.addAll(var14);
         var6 = var8;
      }

      var5.addAll((Collection)var6);
      Iterator var12 = var5.iterator();

      Rectangle var13;
      do {
         if(!var12.hasNext()) {
            return true;
         }

         var13 = (Rectangle)var12.next();
      } while(var4.outputRange(var13.x, var13.y, var13.width, var13.height));

      return false;
   }

   private static Set<Rectangle> mergeRects(Set<Rectangle> var0, Set<Rectangle> var1) {
      HashSet var2 = new HashSet(var0);
      if(!var0.isEmpty() && !var1.isEmpty()) {
         Rectangle[] var3 = (Rectangle[])var0.toArray(new Rectangle[var0.size()]);
         Rectangle[] var4 = (Rectangle[])var1.toArray(new Rectangle[var1.size()]);
         int var5 = 0;
         int var6 = 0;

         while(true) {
            while(var5 < var3.length && var6 < var4.length) {
               while(var4[var6].x < var3[var5].x) {
                  ++var6;
                  if(var6 == var4.length) {
                     return var2;
                  }
               }

               if(var4[var6].x == var3[var5].x && var4[var6].width == var3[var5].width) {
                  var2.remove(var3[var5]);
                  var4[var6].y = var3[var5].y;
                  var4[var6].height = var3[var5].height + 1;
                  ++var6;
               } else {
                  ++var5;
               }
            }

            return var2;
         }
      } else {
         return var2;
      }
   }

   public interface RangesOutput {
      boolean outputRange(int var1, int var2, int var3, int var4);
   }
}
