package com.sun.jna.ptr;

import com.sun.jna.ptr.ByReference;

public class LongByReference extends ByReference {
   public LongByReference() {
      this(0L);
   }

   public LongByReference(long var1) {
      super(8);
      this.setValue(var1);
   }

   public void setValue(long var1) {
      this.getPointer().setLong(0L, var1);
   }

   public long getValue() {
      return this.getPointer().getLong(0L);
   }
}
