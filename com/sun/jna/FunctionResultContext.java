package com.sun.jna;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Function;

public class FunctionResultContext extends FromNativeContext {
   private Function function;
   private Object[] args;

   FunctionResultContext(Class var1, Function var2, Object[] var3) {
      super(var1);
      this.function = var2;
      this.args = var3;
   }

   public Function getFunction() {
      return this.function;
   }

   public Object[] getArguments() {
      return this.args;
   }
}
