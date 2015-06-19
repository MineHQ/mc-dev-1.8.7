package com.sun.jna.ptr;

import com.sun.jna.ptr.ByReference;

public class ShortByReference extends ByReference {
   public ShortByReference() {
      this((short)0);
   }

   public ShortByReference(short var1) {
      super(2);
      this.setValue(var1);
   }

   public void setValue(short var1) {
      this.getPointer().setShort(0L, var1);
   }

   public short getValue() {
      return this.getPointer().getShort(0L);
   }
}
