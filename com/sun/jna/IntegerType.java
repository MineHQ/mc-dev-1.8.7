package com.sun.jna;

import com.sun.jna.FromNativeContext;
import com.sun.jna.NativeMapped;

public abstract class IntegerType extends Number implements NativeMapped {
   private int size;
   private Number number;
   private boolean unsigned;
   private long value;

   public IntegerType(int var1) {
      this(var1, 0L, false);
   }

   public IntegerType(int var1, boolean var2) {
      this(var1, 0L, var2);
   }

   public IntegerType(int var1, long var2) {
      this(var1, var2, false);
   }

   public IntegerType(int var1, long var2, boolean var4) {
      this.size = var1;
      this.unsigned = var4;
      this.setValue(var2);
   }

   public void setValue(long var1) {
      long var3 = var1;
      this.value = var1;
      switch(this.size) {
      case 1:
         if(this.unsigned) {
            this.value = var1 & 255L;
         }

         var3 = (long)((byte)((int)var1));
         this.number = new Byte((byte)((int)var1));
         break;
      case 2:
         if(this.unsigned) {
            this.value = var1 & 65535L;
         }

         var3 = (long)((short)((int)var1));
         this.number = new Short((short)((int)var1));
         break;
      case 3:
      case 5:
      case 6:
      case 7:
      default:
         throw new IllegalArgumentException("Unsupported size: " + this.size);
      case 4:
         if(this.unsigned) {
            this.value = var1 & 4294967295L;
         }

         var3 = (long)((int)var1);
         this.number = new Integer((int)var1);
         break;
      case 8:
         this.number = new Long(var1);
      }

      if(this.size < 8) {
         long var5 = ~((1L << this.size * 8) - 1L);
         if(var1 < 0L && var3 != var1 || var1 >= 0L && (var5 & var1) != 0L) {
            throw new IllegalArgumentException("Argument value 0x" + Long.toHexString(var1) + " exceeds native capacity (" + this.size + " bytes) mask=0x" + Long.toHexString(var5));
         }
      }

   }

   public Object toNative() {
      return this.number;
   }

   public Object fromNative(Object var1, FromNativeContext var2) {
      long var3 = var1 == null?0L:((Number)var1).longValue();

      try {
         IntegerType var5 = (IntegerType)this.getClass().newInstance();
         var5.setValue(var3);
         return var5;
      } catch (InstantiationException var6) {
         throw new IllegalArgumentException("Can\'t instantiate " + this.getClass());
      } catch (IllegalAccessException var7) {
         throw new IllegalArgumentException("Not allowed to instantiate " + this.getClass());
      }
   }

   public Class nativeType() {
      return this.number.getClass();
   }

   public int intValue() {
      return (int)this.value;
   }

   public long longValue() {
      return this.value;
   }

   public float floatValue() {
      return this.number.floatValue();
   }

   public double doubleValue() {
      return this.number.doubleValue();
   }

   public boolean equals(Object var1) {
      return var1 instanceof IntegerType && this.number.equals(((IntegerType)var1).number);
   }

   public String toString() {
      return this.number.toString();
   }

   public int hashCode() {
      return this.number.hashCode();
   }
}
