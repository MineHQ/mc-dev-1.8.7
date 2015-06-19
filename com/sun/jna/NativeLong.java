package com.sun.jna;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;

public class NativeLong extends IntegerType {
   public static final int SIZE;

   public NativeLong() {
      this(0L);
   }

   public NativeLong(long var1) {
      super(SIZE, var1);
   }

   static {
      SIZE = Native.LONG_SIZE;
   }
}
