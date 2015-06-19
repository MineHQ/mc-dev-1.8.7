package com.sun.jna.win32;

import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;
import java.lang.reflect.Method;

public class W32APIFunctionMapper implements FunctionMapper {
   public static final FunctionMapper UNICODE = new W32APIFunctionMapper(true);
   public static final FunctionMapper ASCII = new W32APIFunctionMapper(false);
   private final String suffix;

   protected W32APIFunctionMapper(boolean var1) {
      this.suffix = var1?"W":"A";
   }

   public String getFunctionName(NativeLibrary var1, Method var2) {
      String var3 = var2.getName();
      if(!var3.endsWith("W") && !var3.endsWith("A")) {
         try {
            var3 = var1.getFunction(var3 + this.suffix, 1).getName();
         } catch (UnsatisfiedLinkError var5) {
            ;
         }
      }

      return var3;
   }
}
