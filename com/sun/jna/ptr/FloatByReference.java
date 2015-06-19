package com.sun.jna.ptr;

import com.sun.jna.ptr.ByReference;

public class FloatByReference extends ByReference {
   public FloatByReference() {
      this(0.0F);
   }

   public FloatByReference(float var1) {
      super(4);
      this.setValue(var1);
   }

   public void setValue(float var1) {
      this.getPointer().setFloat(0L, var1);
   }

   public float getValue() {
      return this.getPointer().getFloat(0L);
   }
}
