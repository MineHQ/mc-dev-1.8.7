package com.sun.jna;

import com.sun.jna.AltCallingConvention;
import com.sun.jna.Callback;
import com.sun.jna.CallbackParameterContext;
import com.sun.jna.CallbackProxy;
import com.sun.jna.CallbackResultContext;
import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.FromNativeConverter;
import com.sun.jna.Function;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.NativeString;
import com.sun.jna.Pointer;
import com.sun.jna.StringArray;
import com.sun.jna.Structure;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

class CallbackReference extends WeakReference {
   static final Map callbackMap = new WeakHashMap();
   static final Map allocations = new WeakHashMap();
   private static final Method PROXY_CALLBACK_METHOD;
   private static final Map initializers;
   Pointer cbstruct;
   CallbackProxy proxy;
   Method method;

   static void setCallbackThreadInitializer(Callback var0, CallbackThreadInitializer var1) {
      Map var2 = callbackMap;
      synchronized(callbackMap) {
         if(var1 != null) {
            initializers.put(var0, var1);
         } else {
            initializers.remove(var0);
         }

      }
   }

   private static ThreadGroup initializeThread(Callback var0, CallbackReference.AttachOptions var1) {
      CallbackThreadInitializer var2 = null;
      if(var0 instanceof CallbackReference.DefaultCallbackProxy) {
         var0 = ((CallbackReference.DefaultCallbackProxy)var0).getCallback();
      }

      Map var3 = initializers;
      synchronized(initializers) {
         var2 = (CallbackThreadInitializer)initializers.get(var0);
      }

      ThreadGroup var6 = null;
      if(var2 != null) {
         var6 = var2.getThreadGroup(var0);
         var1.name = var2.getName(var0);
         var1.daemon = var2.isDaemon(var0);
         var1.detach = var2.detach(var0);
         var1.write();
      }

      return var6;
   }

   public static Callback getCallback(Class var0, Pointer var1) {
      return getCallback(var0, var1, false);
   }

   private static Callback getCallback(Class var0, Pointer var1, boolean var2) {
      if(var1 == null) {
         return null;
      } else if(!var0.isInterface()) {
         throw new IllegalArgumentException("Callback type must be an interface");
      } else {
         Map var3 = callbackMap;
         synchronized(var3) {
            Iterator var5 = var3.keySet().iterator();

            while(var5.hasNext()) {
               Callback var6 = (Callback)var5.next();
               if(var0.isAssignableFrom(var6.getClass())) {
                  CallbackReference var7 = (CallbackReference)var3.get(var6);
                  Pointer var8 = var7 != null?var7.getTrampoline():getNativeFunctionPointer(var6);
                  if(var1.equals(var8)) {
                     return var6;
                  }
               }
            }

            int var12 = AltCallingConvention.class.isAssignableFrom(var0)?1:0;
            HashMap var13 = new HashMap();
            Map var14 = Native.getLibraryOptions(var0);
            if(var14 != null) {
               var13.putAll(var14);
            }

            var13.put("invoking-method", getCallbackMethod(var0));
            CallbackReference.NativeFunctionHandler var15 = new CallbackReference.NativeFunctionHandler(var1, var12, var13);
            Callback var9 = (Callback)Proxy.newProxyInstance(var0.getClassLoader(), new Class[]{var0}, var15);
            var3.put(var9, (Object)null);
            return var9;
         }
      }
   }

   private CallbackReference(Callback var1, int var2, boolean var3) {
      super(var1);
      TypeMapper var4 = Native.getTypeMapper(var1.getClass());
      String var7 = System.getProperty("os.arch").toLowerCase();
      boolean var8 = "ppc".equals(var7) || "powerpc".equals(var7);
      if(var3) {
         Method var9 = getCallbackMethod(var1);
         Class[] var10 = var9.getParameterTypes();

         for(int var11 = 0; var11 < var10.length; ++var11) {
            if(var8 && (var10[var11] == Float.TYPE || var10[var11] == Double.TYPE)) {
               var3 = false;
               break;
            }

            if(var4 != null && var4.getFromNativeConverter(var10[var11]) != null) {
               var3 = false;
               break;
            }
         }

         if(var4 != null && var4.getToNativeConverter(var9.getReturnType()) != null) {
            var3 = false;
         }
      }

      Class[] var5;
      Class var6;
      long var12;
      if(var3) {
         this.method = getCallbackMethod(var1);
         var5 = this.method.getParameterTypes();
         var6 = this.method.getReturnType();
         var12 = Native.createNativeCallback(var1, this.method, var5, var6, var2, true);
         this.cbstruct = var12 != 0L?new Pointer(var12):null;
      } else {
         if(var1 instanceof CallbackProxy) {
            this.proxy = (CallbackProxy)var1;
         } else {
            this.proxy = new CallbackReference.DefaultCallbackProxy(getCallbackMethod(var1), var4);
         }

         var5 = this.proxy.getParameterTypes();
         var6 = this.proxy.getReturnType();
         int var13;
         if(var4 != null) {
            for(var13 = 0; var13 < var5.length; ++var13) {
               FromNativeConverter var15 = var4.getFromNativeConverter(var5[var13]);
               if(var15 != null) {
                  var5[var13] = var15.nativeType();
               }
            }

            ToNativeConverter var14 = var4.getToNativeConverter(var6);
            if(var14 != null) {
               var6 = var14.nativeType();
            }
         }

         for(var13 = 0; var13 < var5.length; ++var13) {
            var5[var13] = this.getNativeType(var5[var13]);
            if(!isAllowableNativeType(var5[var13])) {
               String var16 = "Callback argument " + var5[var13] + " requires custom type conversion";
               throw new IllegalArgumentException(var16);
            }
         }

         var6 = this.getNativeType(var6);
         if(!isAllowableNativeType(var6)) {
            String var17 = "Callback return type " + var6 + " requires custom type conversion";
            throw new IllegalArgumentException(var17);
         }

         var12 = Native.createNativeCallback(this.proxy, PROXY_CALLBACK_METHOD, var5, var6, var2, false);
         this.cbstruct = var12 != 0L?new Pointer(var12):null;
      }

   }

   private Class getNativeType(Class var1) {
      if(Structure.class.isAssignableFrom(var1)) {
         Structure.newInstance(var1);
         if(!Structure.ByValue.class.isAssignableFrom(var1)) {
            return Pointer.class;
         }
      } else {
         if(NativeMapped.class.isAssignableFrom(var1)) {
            return NativeMappedConverter.getInstance(var1).nativeType();
         }

         if(var1 == String.class || var1 == WString.class || var1 == String[].class || var1 == WString[].class || Callback.class.isAssignableFrom(var1)) {
            return Pointer.class;
         }
      }

      return var1;
   }

   private static Method checkMethod(Method var0) {
      if(var0.getParameterTypes().length > 256) {
         String var1 = "Method signature exceeds the maximum parameter count: " + var0;
         throw new UnsupportedOperationException(var1);
      } else {
         return var0;
      }
   }

   static Class findCallbackClass(Class var0) {
      if(!Callback.class.isAssignableFrom(var0)) {
         throw new IllegalArgumentException(var0.getName() + " is not derived from com.sun.jna.Callback");
      } else if(var0.isInterface()) {
         return var0;
      } else {
         Class[] var1 = var0.getInterfaces();
         int var2 = 0;

         while(true) {
            if(var2 < var1.length) {
               if(!Callback.class.isAssignableFrom(var1[var2])) {
                  ++var2;
                  continue;
               }

               try {
                  getCallbackMethod(var1[var2]);
                  return var1[var2];
               } catch (IllegalArgumentException var4) {
                  ;
               }
            }

            if(Callback.class.isAssignableFrom(var0.getSuperclass())) {
               return findCallbackClass(var0.getSuperclass());
            }

            return var0;
         }
      }
   }

   private static Method getCallbackMethod(Callback var0) {
      return getCallbackMethod(findCallbackClass(var0.getClass()));
   }

   private static Method getCallbackMethod(Class var0) {
      Method[] var1 = var0.getDeclaredMethods();
      Method[] var2 = var0.getMethods();
      HashSet var3 = new HashSet(Arrays.asList(var1));
      var3.retainAll(Arrays.asList(var2));
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Method var5 = (Method)var4.next();
         if(Callback.FORBIDDEN_NAMES.contains(var5.getName())) {
            var4.remove();
         }
      }

      Method[] var7 = (Method[])((Method[])var3.toArray(new Method[var3.size()]));
      if(var7.length == 1) {
         return checkMethod(var7[0]);
      } else {
         for(int var8 = 0; var8 < var7.length; ++var8) {
            Method var6 = var7[var8];
            if("callback".equals(var6.getName())) {
               return checkMethod(var6);
            }
         }

         String var9 = "Callback must implement a single public method, or one public method named \'callback\'";
         throw new IllegalArgumentException(var9);
      }
   }

   private void setCallbackOptions(int var1) {
      this.cbstruct.setInt((long)Pointer.SIZE, var1);
   }

   public Pointer getTrampoline() {
      return this.cbstruct.getPointer(0L);
   }

   protected void finalize() {
      this.dispose();
   }

   protected synchronized void dispose() {
      if(this.cbstruct != null) {
         Native.freeNativeCallback(this.cbstruct.peer);
         this.cbstruct.peer = 0L;
         this.cbstruct = null;
      }

   }

   private Callback getCallback() {
      return (Callback)this.get();
   }

   private static Pointer getNativeFunctionPointer(Callback var0) {
      if(Proxy.isProxyClass(var0.getClass())) {
         InvocationHandler var1 = Proxy.getInvocationHandler(var0);
         if(var1 instanceof CallbackReference.NativeFunctionHandler) {
            return ((CallbackReference.NativeFunctionHandler)var1).getPointer();
         }
      }

      return null;
   }

   public static Pointer getFunctionPointer(Callback var0) {
      return getFunctionPointer(var0, false);
   }

   private static Pointer getFunctionPointer(Callback var0, boolean var1) {
      Pointer var2 = null;
      if(var0 == null) {
         return null;
      } else if((var2 = getNativeFunctionPointer(var0)) != null) {
         return var2;
      } else {
         int var3 = var0 instanceof AltCallingConvention?1:0;
         Map var4 = callbackMap;
         synchronized(var4) {
            CallbackReference var6 = (CallbackReference)var4.get(var0);
            if(var6 == null) {
               var6 = new CallbackReference(var0, var3, var1);
               var4.put(var0, var6);
               if(initializers.containsKey(var0)) {
                  var6.setCallbackOptions(1);
               }
            }

            return var6.getTrampoline();
         }
      }
   }

   private static boolean isAllowableNativeType(Class var0) {
      return var0 == Void.TYPE || var0 == Void.class || var0 == Boolean.TYPE || var0 == Boolean.class || var0 == Byte.TYPE || var0 == Byte.class || var0 == Short.TYPE || var0 == Short.class || var0 == Character.TYPE || var0 == Character.class || var0 == Integer.TYPE || var0 == Integer.class || var0 == Long.TYPE || var0 == Long.class || var0 == Float.TYPE || var0 == Float.class || var0 == Double.TYPE || var0 == Double.class || Structure.ByValue.class.isAssignableFrom(var0) && Structure.class.isAssignableFrom(var0) || Pointer.class.isAssignableFrom(var0);
   }

   private static Pointer getNativeString(Object var0, boolean var1) {
      if(var0 != null) {
         NativeString var2 = new NativeString(var0.toString(), var1);
         allocations.put(var0, var2);
         return var2.getPointer();
      } else {
         return null;
      }
   }

   static {
      try {
         PROXY_CALLBACK_METHOD = CallbackProxy.class.getMethod("callback", new Class[]{Object[].class});
      } catch (Exception var1) {
         throw new Error("Error looking up CallbackProxy.callback() method");
      }

      initializers = new WeakHashMap();
   }

   private static class NativeFunctionHandler implements InvocationHandler {
      private Function function;
      private Map options;

      public NativeFunctionHandler(Pointer var1, int var2, Map var3) {
         this.function = new Function(var1, var2);
         this.options = var3;
      }

      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         if(Library.Handler.OBJECT_TOSTRING.equals(var2)) {
            String var7 = "Proxy interface to " + this.function;
            Method var5 = (Method)this.options.get("invoking-method");
            Class var6 = CallbackReference.findCallbackClass(var5.getDeclaringClass());
            var7 = var7 + " (" + var6.getName() + ")";
            return var7;
         } else if(Library.Handler.OBJECT_HASHCODE.equals(var2)) {
            return new Integer(this.hashCode());
         } else if(Library.Handler.OBJECT_EQUALS.equals(var2)) {
            Object var4 = var3[0];
            return var4 != null && Proxy.isProxyClass(var4.getClass())?Function.valueOf(Proxy.getInvocationHandler(var4) == this):Boolean.FALSE;
         } else {
            if(Function.isVarArgs(var2)) {
               var3 = Function.concatenateVarArgs(var3);
            }

            return this.function.invoke(var2.getReturnType(), var3, this.options);
         }
      }

      public Pointer getPointer() {
         return this.function;
      }
   }

   private class DefaultCallbackProxy implements CallbackProxy {
      private Method callbackMethod;
      private ToNativeConverter toNative;
      private FromNativeConverter[] fromNative;

      public DefaultCallbackProxy(Method var2, TypeMapper var3) {
         this.callbackMethod = var2;
         Class[] var4 = var2.getParameterTypes();
         Class var5 = var2.getReturnType();
         this.fromNative = new FromNativeConverter[var4.length];
         if(NativeMapped.class.isAssignableFrom(var5)) {
            this.toNative = NativeMappedConverter.getInstance(var5);
         } else if(var3 != null) {
            this.toNative = var3.getToNativeConverter(var5);
         }

         for(int var6 = 0; var6 < this.fromNative.length; ++var6) {
            if(NativeMapped.class.isAssignableFrom(var4[var6])) {
               this.fromNative[var6] = new NativeMappedConverter(var4[var6]);
            } else if(var3 != null) {
               this.fromNative[var6] = var3.getFromNativeConverter(var4[var6]);
            }
         }

         if(!var2.isAccessible()) {
            try {
               var2.setAccessible(true);
            } catch (SecurityException var7) {
               throw new IllegalArgumentException("Callback method is inaccessible, make sure the interface is public: " + var2);
            }
         }

      }

      public Callback getCallback() {
         return CallbackReference.this.getCallback();
      }

      private Object invokeCallback(Object[] var1) {
         Class[] var2 = this.callbackMethod.getParameterTypes();
         Object[] var3 = new Object[var1.length];

         for(int var4 = 0; var4 < var1.length; ++var4) {
            Class var5 = var2[var4];
            Object var6 = var1[var4];
            if(this.fromNative[var4] != null) {
               CallbackParameterContext var7 = new CallbackParameterContext(var5, this.callbackMethod, var1, var4);
               var3[var4] = this.fromNative[var4].fromNative(var6, var7);
            } else {
               var3[var4] = this.convertArgument(var6, var5);
            }
         }

         Object var11 = null;
         Callback var12 = this.getCallback();
         if(var12 != null) {
            try {
               var11 = this.convertResult(this.callbackMethod.invoke(var12, var3));
            } catch (IllegalArgumentException var8) {
               Native.getCallbackExceptionHandler().uncaughtException(var12, var8);
            } catch (IllegalAccessException var9) {
               Native.getCallbackExceptionHandler().uncaughtException(var12, var9);
            } catch (InvocationTargetException var10) {
               Native.getCallbackExceptionHandler().uncaughtException(var12, var10.getTargetException());
            }
         }

         for(int var13 = 0; var13 < var3.length; ++var13) {
            if(var3[var13] instanceof Structure && !(var3[var13] instanceof Structure.ByValue)) {
               ((Structure)var3[var13]).autoWrite();
            }
         }

         return var11;
      }

      public Object callback(Object[] var1) {
         try {
            return this.invokeCallback(var1);
         } catch (Throwable var3) {
            Native.getCallbackExceptionHandler().uncaughtException(this.getCallback(), var3);
            return null;
         }
      }

      private Object convertArgument(Object var1, Class var2) {
         if(var1 instanceof Pointer) {
            if(var2 == String.class) {
               var1 = ((Pointer)var1).getString(0L);
            } else if(var2 == WString.class) {
               var1 = new WString(((Pointer)var1).getString(0L, true));
            } else if(var2 != String[].class && var2 != WString[].class) {
               if(Callback.class.isAssignableFrom(var2)) {
                  CallbackReference var10000 = CallbackReference.this;
                  var1 = CallbackReference.getCallback(var2, (Pointer)var1);
               } else if(Structure.class.isAssignableFrom(var2)) {
                  Structure var3 = Structure.newInstance(var2);
                  if(Structure.ByValue.class.isAssignableFrom(var2)) {
                     byte[] var4 = new byte[var3.size()];
                     ((Pointer)var1).read(0L, (byte[])var4, 0, var4.length);
                     var3.getPointer().write(0L, (byte[])var4, 0, var4.length);
                  } else {
                     var3.useMemory((Pointer)var1);
                  }

                  var3.read();
                  var1 = var3;
               }
            } else {
               var1 = ((Pointer)var1).getStringArray(0L, var2 == WString[].class);
            }
         } else if((Boolean.TYPE == var2 || Boolean.class == var2) && var1 instanceof Number) {
            var1 = Function.valueOf(((Number)var1).intValue() != 0);
         }

         return var1;
      }

      private Object convertResult(Object var1) {
         if(this.toNative != null) {
            var1 = this.toNative.toNative(var1, new CallbackResultContext(this.callbackMethod));
         }

         if(var1 == null) {
            return null;
         } else {
            Class var2 = var1.getClass();
            if(Structure.class.isAssignableFrom(var2)) {
               return Structure.ByValue.class.isAssignableFrom(var2)?var1:((Structure)var1).getPointer();
            } else if(var2 != Boolean.TYPE && var2 != Boolean.class) {
               if(var2 != String.class && var2 != WString.class) {
                  if(var2 != String[].class && var2 != WString.class) {
                     return Callback.class.isAssignableFrom(var2)?CallbackReference.getFunctionPointer((Callback)var1):var1;
                  } else {
                     StringArray var3 = var2 == String[].class?new StringArray((String[])((String[])var1)):new StringArray((WString[])((WString[])var1));
                     CallbackReference.allocations.put(var1, var3);
                     return var3;
                  }
               } else {
                  return CallbackReference.getNativeString(var1, var2 == WString.class);
               }
            } else {
               return Boolean.TRUE.equals(var1)?Function.INTEGER_TRUE:Function.INTEGER_FALSE;
            }
         }
      }

      public Class[] getParameterTypes() {
         return this.callbackMethod.getParameterTypes();
      }

      public Class getReturnType() {
         return this.callbackMethod.getReturnType();
      }
   }

   static class AttachOptions extends Structure {
      public boolean daemon;
      public boolean detach;
      public String name;

      AttachOptions() {
      }
   }
}
