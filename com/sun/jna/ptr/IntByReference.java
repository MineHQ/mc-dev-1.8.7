package com.sun.jna.ptr;

import com.sun.jna.ptr.ByReference;

public class IntByReference extends ByReference {
   public IntByReference() {
      this(0);
   }

   public IntByReference(int var1) {
      super(4);
      this.setValue(var1);
   }

   public void setValue(int var1) {
      this.getPointer().setInt(0L, var1);
   }

   public int getValue() {
      return this.getPointer().getInt(0L);
   }
}
