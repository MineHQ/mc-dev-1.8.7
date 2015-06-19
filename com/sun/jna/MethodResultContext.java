package com.sun.jna;

import com.sun.jna.Function;
import com.sun.jna.FunctionResultContext;
import java.lang.reflect.Method;

public class MethodResultContext extends FunctionResultContext {
   private final Method method;

   MethodResultContext(Class var1, Function var2, Object[] var3, Method var4) {
      super(var1, var2, var3);
      this.method = var4;
   }

   public Method getMethod() {
      return this.method;
   }
}
