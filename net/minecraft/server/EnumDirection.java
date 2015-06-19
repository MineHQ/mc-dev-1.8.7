package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import net.minecraft.server.BaseBlockPosition;
import net.minecraft.server.INamable;
import net.minecraft.server.MathHelper;

public enum EnumDirection implements INamable {
   DOWN,
   UP,
   NORTH,
   SOUTH,
   WEST,
   EAST;

   private final int g;
   private final int h;
   private final int i;
   private final String j;
   private final EnumDirection.EnumAxis k;
   private final EnumDirection.EnumAxisDirection l;
   private final BaseBlockPosition m;
   private static final EnumDirection[] n;
   private static final EnumDirection[] o;
   private static final Map<String, EnumDirection> p;

   private EnumDirection(int var3, int var4, int var5, String var6, EnumDirection.EnumAxisDirection var7, EnumDirection.EnumAxis var8, BaseBlockPosition var9) {
      this.g = var3;
      this.i = var5;
      this.h = var4;
      this.j = var6;
      this.k = var8;
      this.l = var7;
      this.m = var9;
   }

   public int a() {
      return this.g;
   }

   public int b() {
      return this.i;
   }

   public EnumDirection.EnumAxisDirection c() {
      return this.l;
   }

   public EnumDirection opposite() {
      return fromType1(this.h);
   }

   public EnumDirection e() {
      switch(EnumDirection.SyntheticClass_1.b[this.ordinal()]) {
      case 1:
         return EAST;
      case 2:
         return SOUTH;
      case 3:
         return WEST;
      case 4:
         return NORTH;
      default:
         throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
      }
   }

   public EnumDirection f() {
      switch(EnumDirection.SyntheticClass_1.b[this.ordinal()]) {
      case 1:
         return WEST;
      case 2:
         return NORTH;
      case 3:
         return EAST;
      case 4:
         return SOUTH;
      default:
         throw new IllegalStateException("Unable to get CCW facing of " + this);
      }
   }

   public int getAdjacentX() {
      return this.k == EnumDirection.EnumAxis.X?this.l.a():0;
   }

   public int getAdjacentY() {
      return this.k == EnumDirection.EnumAxis.Y?this.l.a():0;
   }

   public int getAdjacentZ() {
      return this.k == EnumDirection.EnumAxis.Z?this.l.a():0;
   }

   public String j() {
      return this.j;
   }

   public EnumDirection.EnumAxis k() {
      return this.k;
   }

   public static EnumDirection fromType1(int var0) {
      return n[MathHelper.a(var0 % n.length)];
   }

   public static EnumDirection fromType2(int var0) {
      return o[MathHelper.a(var0 % o.length)];
   }

   public static EnumDirection fromAngle(double var0) {
      return fromType2(MathHelper.floor(var0 / 90.0D + 0.5D) & 3);
   }

   public static EnumDirection a(Random var0) {
      return values()[var0.nextInt(values().length)];
   }

   public String toString() {
      return this.j;
   }

   public String getName() {
      return this.j;
   }

   public static EnumDirection a(EnumDirection.EnumAxisDirection var0, EnumDirection.EnumAxis var1) {
      EnumDirection[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumDirection var5 = var2[var4];
         if(var5.c() == var0 && var5.k() == var1) {
            return var5;
         }
      }

      throw new IllegalArgumentException("No such direction: " + var0 + " " + var1);
   }

   static {
      DOWN = new EnumDirection("DOWN", 0, 0, 1, -1, "down", EnumDirection.EnumAxisDirection.NEGATIVE, EnumDirection.EnumAxis.Y, new BaseBlockPosition(0, -1, 0));
      UP = new EnumDirection("UP", 1, 1, 0, -1, "up", EnumDirection.EnumAxisDirection.POSITIVE, EnumDirection.EnumAxis.Y, new BaseBlockPosition(0, 1, 0));
      NORTH = new EnumDirection("NORTH", 2, 2, 3, 2, "north", EnumDirection.EnumAxisDirection.NEGATIVE, EnumDirection.EnumAxis.Z, new BaseBlockPosition(0, 0, -1));
      SOUTH = new EnumDirection("SOUTH", 3, 3, 2, 0, "south", EnumDirection.EnumAxisDirection.POSITIVE, EnumDirection.EnumAxis.Z, new BaseBlockPosition(0, 0, 1));
      WEST = new EnumDirection("WEST", 4, 4, 5, 1, "west", EnumDirection.EnumAxisDirection.NEGATIVE, EnumDirection.EnumAxis.X, new BaseBlockPosition(-1, 0, 0));
      EAST = new EnumDirection("EAST", 5, 5, 4, 3, "east", EnumDirection.EnumAxisDirection.POSITIVE, EnumDirection.EnumAxis.X, new BaseBlockPosition(1, 0, 0));
      n = new EnumDirection[6];
      o = new EnumDirection[4];
      p = Maps.newHashMap();
      EnumDirection[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumDirection var3 = var0[var2];
         n[var3.g] = var3;
         if(var3.k().c()) {
            o[var3.i] = var3;
         }

         p.put(var3.j().toLowerCase(), var3);
      }

   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a;
      // $FF: synthetic field
      static final int[] b;
      // $FF: synthetic field
      static final int[] c = new int[EnumDirection.EnumDirectionLimit.values().length];

      static {
         try {
            c[EnumDirection.EnumDirectionLimit.HORIZONTAL.ordinal()] = 1;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            c[EnumDirection.EnumDirectionLimit.VERTICAL.ordinal()] = 2;
         } catch (NoSuchFieldError var10) {
            ;
         }

         b = new int[EnumDirection.values().length];

         try {
            b[EnumDirection.NORTH.ordinal()] = 1;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            b[EnumDirection.EAST.ordinal()] = 2;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            b[EnumDirection.SOUTH.ordinal()] = 3;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            b[EnumDirection.WEST.ordinal()] = 4;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            b[EnumDirection.UP.ordinal()] = 5;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            b[EnumDirection.DOWN.ordinal()] = 6;
         } catch (NoSuchFieldError var4) {
            ;
         }

         a = new int[EnumDirection.EnumAxis.values().length];

         try {
            a[EnumDirection.EnumAxis.X.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.EnumAxis.Y.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.EnumAxis.Z.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumDirectionLimit implements Predicate<EnumDirection>, Iterable<EnumDirection> {
      HORIZONTAL,
      VERTICAL;

      private EnumDirectionLimit() {
      }

      public EnumDirection[] a() {
         switch(EnumDirection.SyntheticClass_1.c[this.ordinal()]) {
         case 1:
            return new EnumDirection[]{EnumDirection.NORTH, EnumDirection.EAST, EnumDirection.SOUTH, EnumDirection.WEST};
         case 2:
            return new EnumDirection[]{EnumDirection.UP, EnumDirection.DOWN};
         default:
            throw new Error("Someone\'s been tampering with the universe!");
         }
      }

      public EnumDirection a(Random var1) {
         EnumDirection[] var2 = this.a();
         return var2[var1.nextInt(var2.length)];
      }

      public boolean a(EnumDirection var1) {
         return var1 != null && var1.k().d() == this;
      }

      public Iterator<EnumDirection> iterator() {
         return Iterators.forArray(this.a());
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((EnumDirection)var1);
      }
   }

   public static enum EnumAxisDirection {
      POSITIVE(1, "Towards positive"),
      NEGATIVE(-1, "Towards negative");

      private final int c;
      private final String d;

      private EnumAxisDirection(int var3, String var4) {
         this.c = var3;
         this.d = var4;
      }

      public int a() {
         return this.c;
      }

      public String toString() {
         return this.d;
      }
   }

   public static enum EnumAxis implements Predicate<EnumDirection>, INamable {
      X,
      Y,
      Z;

      private static final Map<String, EnumDirection.EnumAxis> d;
      private final String e;
      private final EnumDirection.EnumDirectionLimit f;

      private EnumAxis(String var3, EnumDirection.EnumDirectionLimit var4) {
         this.e = var3;
         this.f = var4;
      }

      public String a() {
         return this.e;
      }

      public boolean b() {
         return this.f == EnumDirection.EnumDirectionLimit.VERTICAL;
      }

      public boolean c() {
         return this.f == EnumDirection.EnumDirectionLimit.HORIZONTAL;
      }

      public String toString() {
         return this.e;
      }

      public boolean a(EnumDirection var1) {
         return var1 != null && var1.k() == this;
      }

      public EnumDirection.EnumDirectionLimit d() {
         return this.f;
      }

      public String getName() {
         return this.e;
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((EnumDirection)var1);
      }

      static {
         X = new EnumDirection.EnumAxis("X", 0, "x", EnumDirection.EnumDirectionLimit.HORIZONTAL);
         Y = new EnumDirection.EnumAxis("Y", 1, "y", EnumDirection.EnumDirectionLimit.VERTICAL);
         Z = new EnumDirection.EnumAxis("Z", 2, "z", EnumDirection.EnumDirectionLimit.HORIZONTAL);
         d = Maps.newHashMap();
         EnumDirection.EnumAxis[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            EnumDirection.EnumAxis var3 = var0[var2];
            d.put(var3.a().toLowerCase(), var3);
         }

      }
   }
}
