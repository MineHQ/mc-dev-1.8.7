package com.google.common.reflect;

import com.google.common.annotations.Beta;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractInvocationHandler implements InvocationHandler {
   private static final Object[] NO_ARGS = new Object[0];

   public AbstractInvocationHandler() {
   }

   public final Object invoke(Object var1, Method var2, @Nullable Object[] var3) throws Throwable {
      if(var3 == null) {
         var3 = NO_ARGS;
      }

      if(var3.length == 0 && var2.getName().equals("hashCode")) {
         return Integer.valueOf(this.hashCode());
      } else if(var3.length == 1 && var2.getName().equals("equals") && var2.getParameterTypes()[0] == Object.class) {
         Object var4 = var3[0];
         return var4 == null?Boolean.valueOf(false):(var1 == var4?Boolean.valueOf(true):Boolean.valueOf(isProxyOfSameInterfaces(var4, var1.getClass()) && this.equals(Proxy.getInvocationHandler(var4))));
      } else {
         return var3.length == 0 && var2.getName().equals("toString")?this.toString():this.handleInvocation(var1, var2, var3);
      }
   }

   protected abstract Object handleInvocation(Object var1, Method var2, Object[] var3) throws Throwable;

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public int hashCode() {
      return super.hashCode();
   }

   public String toString() {
      return super.toString();
   }

   private static boolean isProxyOfSameInterfaces(Object var0, Class<?> var1) {
      return var1.isInstance(var0) || Proxy.isProxyClass(var0.getClass()) && Arrays.equals(var0.getClass().getInterfaces(), var1.getInterfaces());
   }
}
