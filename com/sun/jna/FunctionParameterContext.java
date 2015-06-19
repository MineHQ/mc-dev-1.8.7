package com.sun.jna;

import com.sun.jna.Function;
import com.sun.jna.ToNativeContext;

public class FunctionParameterContext extends ToNativeContext {
   private Function function;
   private Object[] args;
   private int index;

   FunctionParameterContext(Function var1, Object[] var2, int var3) {
      this.function = var1;
      this.args = var2;
      this.index = var3;
   }

   public Function getFunction() {
      return this.function;
   }

   public Object[] getParameters() {
      return this.args;
   }

   public int getParameterIndex() {
      return this.index;
   }
}
