package com.sun.jna.ptr;

import com.sun.jna.ptr.ByReference;

public class ByteByReference extends ByReference {
   public ByteByReference() {
      this((byte)0);
   }

   public ByteByReference(byte var1) {
      super(1);
      this.setValue(var1);
   }

   public void setValue(byte var1) {
      this.getPointer().setByte(0L, var1);
   }

   public byte getValue() {
      return this.getPointer().getByte(0L);
   }
}
