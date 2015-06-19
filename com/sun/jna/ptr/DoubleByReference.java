package com.sun.jna.ptr;

import com.sun.jna.ptr.ByReference;

public class DoubleByReference extends ByReference {
   public DoubleByReference() {
      this(0.0D);
   }

   public DoubleByReference(double var1) {
      super(8);
      this.setValue(var1);
   }

   public void setValue(double var1) {
      this.getPointer().setDouble(0L, var1);
   }

   public double getValue() {
      return this.getPointer().getDouble(0L);
   }
}
