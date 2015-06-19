package com.sun.jna;

import com.sun.jna.ToNativeContext;
import java.lang.reflect.Method;

public class CallbackResultContext extends ToNativeContext {
   private Method method;

   CallbackResultContext(Method var1) {
      this.method = var1;
   }

   public Method getMethod() {
      return this.method;
   }
}
