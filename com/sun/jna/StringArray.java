package com.sun.jna;

import com.sun.jna.Function;
import com.sun.jna.Memory;
import com.sun.jna.NativeString;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringArray extends Memory implements Function.PostCallRead {
   private boolean wide;
   private List natives;
   private Object[] original;

   public StringArray(String[] var1) {
      this(var1, false);
   }

   public StringArray(String[] var1, boolean var2) {
      this((Object[])var1, var2);
   }

   public StringArray(WString[] var1) {
      this((Object[])var1, true);
   }

   private StringArray(Object[] var1, boolean var2) {
      super((long)((var1.length + 1) * Pointer.SIZE));
      this.natives = new ArrayList();
      this.original = var1;
      this.wide = var2;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Pointer var4 = null;
         if(var1[var3] != null) {
            NativeString var5 = new NativeString(var1[var3].toString(), var2);
            this.natives.add(var5);
            var4 = var5.getPointer();
         }

         this.setPointer((long)(Pointer.SIZE * var3), var4);
      }

      this.setPointer((long)(Pointer.SIZE * var1.length), (Pointer)null);
   }

   public void read() {
      boolean var1 = this.original instanceof WString[];

      for(int var2 = 0; var2 < this.original.length; ++var2) {
         Pointer var3 = this.getPointer((long)(var2 * Pointer.SIZE));
         Object var4 = null;
         if(var3 != null) {
            var4 = var3.getString(0L, this.wide);
            if(var1) {
               var4 = new WString((String)var4);
            }
         }

         this.original[var2] = var4;
      }

   }

   public String toString() {
      String var1 = this.wide?"const wchar_t*[]":"const char*[]";
      var1 = var1 + Arrays.asList(this.original);
      return var1;
   }
}
