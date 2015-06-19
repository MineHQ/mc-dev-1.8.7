package com.sun.jna.ptr;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.ByReference;

public class NativeLongByReference extends ByReference {
   public NativeLongByReference() {
      this(new NativeLong(0L));
   }

   public NativeLongByReference(NativeLong var1) {
      super(NativeLong.SIZE);
      this.setValue(var1);
   }

   public void setValue(NativeLong var1) {
      this.getPointer().setNativeLong(0L, var1);
   }

   public NativeLong getValue() {
      return this.getPointer().getNativeLong(0L);
   }
}
