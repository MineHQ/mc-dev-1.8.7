package com.sun.jna;

import com.sun.jna.AltCallingConvention;
import com.sun.jna.Function;
import com.sun.jna.FunctionMapper;
import com.sun.jna.InvocationMapper;
import com.sun.jna.NativeLibrary;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public interface Library {
   String OPTION_TYPE_MAPPER = "type-mapper";
   String OPTION_FUNCTION_MAPPER = "function-mapper";
   String OPTION_INVOCATION_MAPPER = "invocation-mapper";
   String OPTION_STRUCTURE_ALIGNMENT = "structure-alignment";
   String OPTION_ALLOW_OBJECTS = "allow-objects";
   String OPTION_CALLING_CONVENTION = "calling-convention";

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   public static class Handler implements InvocationHandler {
      static final Method OBJECT_TOSTRING;
      static final Method OBJECT_HASHCODE;
      static final Method OBJECT_EQUALS;
      private final NativeLibrary nativeLibrary;
      private final Class interfaceClass;
      private final Map options;
      private FunctionMapper functionMapper;
      private final InvocationMapper invocationMapper;
      private final Map functions = new WeakHashMap();

      public Handler(String var1, Class var2, Map var3) {
         if(var1 != null && "".equals(var1.trim())) {
            throw new IllegalArgumentException("Invalid library name \"" + var1 + "\"");
         } else {
            this.interfaceClass = var2;
            HashMap var5 = new HashMap(var3);
            int var4 = AltCallingConvention.class.isAssignableFrom(var2)?1:0;
            if(var5.get("calling-convention") == null) {
               var5.put("calling-convention", new Integer(var4));
            }

            this.options = var5;
            this.nativeLibrary = NativeLibrary.getInstance(var1, var5);
            this.functionMapper = (FunctionMapper)var5.get("function-mapper");
            if(this.functionMapper == null) {
               this.functionMapper = new Library.Handler.Handler$FunctionNameMap(var5);
            }

            this.invocationMapper = (InvocationMapper)var5.get("invocation-mapper");
         }
      }

      public NativeLibrary getNativeLibrary() {
         return this.nativeLibrary;
      }

      public String getLibraryName() {
         return this.nativeLibrary.getName();
      }

      public Class getInterfaceClass() {
         return this.interfaceClass;
      }

      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         if(OBJECT_TOSTRING.equals(var2)) {
            return "Proxy interface to " + this.nativeLibrary;
         } else if(OBJECT_HASHCODE.equals(var2)) {
            return new Integer(this.hashCode());
         } else if(OBJECT_EQUALS.equals(var2)) {
            Object var9 = var3[0];
            return var9 != null && Proxy.isProxyClass(var9.getClass())?Function.valueOf(Proxy.getInvocationHandler(var9) == this):Boolean.FALSE;
         } else {
            Library.Handler.Handler$FunctionInfo var4 = null;
            Map var5 = this.functions;
            synchronized(this.functions) {
               var4 = (Library.Handler.Handler$FunctionInfo)this.functions.get(var2);
               if(var4 == null) {
                  var4 = new Library.Handler.Handler$FunctionInfo((Library.SyntheticClass_1)null);
                  var4.isVarArgs = Function.isVarArgs(var2);
                  if(this.invocationMapper != null) {
                     var4.handler = this.invocationMapper.getInvocationHandler(this.nativeLibrary, var2);
                  }

                  if(var4.handler == null) {
                     String var6 = this.functionMapper.getFunctionName(this.nativeLibrary, var2);
                     if(var6 == null) {
                        var6 = var2.getName();
                     }

                     var4.function = this.nativeLibrary.getFunction(var6, var2);
                     var4.options = new HashMap(this.options);
                     var4.options.put("invoking-method", var2);
                  }

                  this.functions.put(var2, var4);
               }
            }

            if(var4.isVarArgs) {
               var3 = Function.concatenateVarArgs(var3);
            }

            return var4.handler != null?var4.handler.invoke(var1, var2, var3):var4.function.invoke(var2.getReturnType(), var3, var4.options);
         }
      }

      static {
         try {
            OBJECT_TOSTRING = Object.class.getMethod("toString", new Class[0]);
            OBJECT_HASHCODE = Object.class.getMethod("hashCode", new Class[0]);
            OBJECT_EQUALS = Object.class.getMethod("equals", new Class[]{Object.class});
         } catch (Exception var1) {
            throw new Error("Error retrieving Object.toString() method");
         }
      }

      private static class Handler$FunctionInfo {
         InvocationHandler handler;
         Function function;
         boolean isVarArgs;
         Map options;

         private Handler$FunctionInfo() {
         }

         // $FF: synthetic method
         Handler$FunctionInfo(Library.SyntheticClass_1 var1) {
            this();
         }
      }

      private static class Handler$FunctionNameMap implements FunctionMapper {
         private final Map map;

         public Handler$FunctionNameMap(Map var1) {
            this.map = new HashMap(var1);
         }

         public String getFunctionName(NativeLibrary var1, Method var2) {
            String var3 = var2.getName();
            return this.map.containsKey(var3)?(String)this.map.get(var3):var3;
         }
      }
   }
}
