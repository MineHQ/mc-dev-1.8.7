package com.sun.jna;

public class FromNativeContext {
   private Class type;

   FromNativeContext(Class var1) {
      this.type = var1;
   }

   public Class getTargetType() {
      return this.type;
   }
}
