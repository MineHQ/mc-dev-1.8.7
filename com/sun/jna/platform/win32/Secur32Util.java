package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Secur32;
import com.sun.jna.platform.win32.Sspi;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;

public abstract class Secur32Util {
   public Secur32Util() {
   }

   public static String getUserNameEx(int var0) {
      char[] var1 = new char[128];
      IntByReference var2 = new IntByReference(var1.length);
      boolean var3 = Secur32.INSTANCE.GetUserNameEx(var0, var1, var2);
      if(!var3) {
         int var4 = Kernel32.INSTANCE.GetLastError();
         switch(var4) {
         case 234:
            var1 = new char[var2.getValue() + 1];
            var3 = Secur32.INSTANCE.GetUserNameEx(var0, var1, var2);
            break;
         default:
            throw new Win32Exception(Native.getLastError());
         }
      }

      if(!var3) {
         throw new Win32Exception(Native.getLastError());
      } else {
         return Native.toString(var1);
      }
   }

   public static Secur32Util.SecurityPackage[] getSecurityPackages() {
      IntByReference var0 = new IntByReference();
      Sspi.PSecPkgInfo.PSecPkgInfo$ByReference var1 = new Sspi.PSecPkgInfo.PSecPkgInfo$ByReference();
      int var2 = Secur32.INSTANCE.EnumerateSecurityPackages(var0, var1);
      if(0 != var2) {
         throw new Win32Exception(var2);
      } else {
         Sspi.SecPkgInfo.SecPkgInfo$ByReference[] var3 = var1.toArray(var0.getValue());
         ArrayList var4 = new ArrayList(var0.getValue());
         Sspi.SecPkgInfo.SecPkgInfo$ByReference[] var5 = var3;
         int var6 = var3.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Sspi.SecPkgInfo.SecPkgInfo$ByReference var8 = var5[var7];
            Secur32Util.SecurityPackage var9 = new Secur32Util.SecurityPackage();
            var9.name = var8.Name.toString();
            var9.comment = var8.Comment.toString();
            var4.add(var9);
         }

         var2 = Secur32.INSTANCE.FreeContextBuffer(var1.pPkgInfo.getPointer());
         if(0 != var2) {
            throw new Win32Exception(var2);
         } else {
            return (Secur32Util.SecurityPackage[])var4.toArray(new Secur32Util.SecurityPackage[0]);
         }
      }
   }

   public static class SecurityPackage {
      public String name;
      public String comment;

      public SecurityPackage() {
      }
   }
}
