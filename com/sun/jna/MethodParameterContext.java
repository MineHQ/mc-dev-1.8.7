package com.sun.jna;

import com.sun.jna.Function;
import com.sun.jna.FunctionParameterContext;
import java.lang.reflect.Method;

public class MethodParameterContext extends FunctionParameterContext {
   private Method method;

   MethodParameterContext(Function var1, Object[] var2, int var3, Method var4) {
      super(var1, var2, var3);
      this.method = var4;
   }

   public Method getMethod() {
      return this.method;
   }
}
