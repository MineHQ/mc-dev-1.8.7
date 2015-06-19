package com.sun.jna;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import java.nio.CharBuffer;

class NativeString implements CharSequence, Comparable {
   private Pointer pointer;
   private boolean wide;

   public NativeString(String var1) {
      this(var1, false);
   }

   public NativeString(String var1, boolean var2) {
      if(var1 == null) {
         throw new NullPointerException("String must not be null");
      } else {
         this.wide = var2;
         if(var2) {
            int var3 = (var1.length() + 1) * Native.WCHAR_SIZE;
            this.pointer = new Memory((long)var3);
            this.pointer.setString(0L, var1, true);
         } else {
            byte[] var4 = Native.getBytes(var1);
            this.pointer = new Memory((long)(var4.length + 1));
            this.pointer.write(0L, (byte[])var4, 0, var4.length);
            this.pointer.setByte((long)var4.length, (byte)0);
         }

      }
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public boolean equals(Object var1) {
      return var1 instanceof CharSequence?this.compareTo(var1) == 0:false;
   }

   public String toString() {
      String var1 = this.wide?"const wchar_t*":"const char*";
      var1 = var1 + "(" + this.pointer.getString(0L, this.wide) + ")";
      return var1;
   }

   public Pointer getPointer() {
      return this.pointer;
   }

   public char charAt(int var1) {
      return this.toString().charAt(var1);
   }

   public int length() {
      return this.toString().length();
   }

   public CharSequence subSequence(int var1, int var2) {
      return CharBuffer.wrap(this.toString()).subSequence(var1, var2);
   }

   public int compareTo(Object var1) {
      return var1 == null?1:this.toString().compareTo(var1.toString());
   }
}
