package com.sun.jna;

import com.sun.jna.FromNativeContext;
import java.lang.reflect.Method;

public class CallbackParameterContext extends FromNativeContext {
   private Method method;
   private Object[] args;
   private int index;

   CallbackParameterContext(Class var1, Method var2, Object[] var3, int var4) {
      super(var1);
      this.method = var2;
      this.args = var3;
      this.index = var4;
   }

   public Method getMethod() {
      return this.method;
   }

   public Object[] getArguments() {
      return this.args;
   }

   public int getIndex() {
      return this.index;
   }
}
