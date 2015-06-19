package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinNT;

public abstract class W32Errors implements WinError {
   public W32Errors() {
   }

   public static final boolean SUCCEEDED(int var0) {
      return var0 >= 0;
   }

   public static final boolean FAILED(int var0) {
      return var0 < 0;
   }

   public static final int HRESULT_CODE(int var0) {
      return var0 & '\uffff';
   }

   public static final int SCODE_CODE(int var0) {
      return var0 & '\uffff';
   }

   public static final int HRESULT_FACILITY(int var0) {
      return (var0 >>= 16) & 8191;
   }

   public static final int SCODE_FACILITY(short var0) {
      return (var0 = (short)(var0 >> 16)) & 8191;
   }

   public static short HRESULT_SEVERITY(int var0) {
      return (short)((var0 >>= 31) & 1);
   }

   public static short SCODE_SEVERITY(short var0) {
      return (short)((var0 = (short)(var0 >> 31)) & 1);
   }

   public static int MAKE_HRESULT(short var0, short var1, short var2) {
      return var0 << 31 | var1 << 16 | var2;
   }

   public static final int MAKE_SCODE(short var0, short var1, short var2) {
      return var0 << 31 | var1 << 16 | var2;
   }

   public static final WinNT.HRESULT HRESULT_FROM_WIN32(int var0) {
      byte var1 = 7;
      int var2;
      return new WinNT.HRESULT(var0 <= 0?var0:var0 & '\uffff' | (var2 = var1 << 16) | Integer.MIN_VALUE);
   }

   public static final int FILTER_HRESULT_FROM_FLT_NTSTATUS(int var0) {
      byte var1 = 31;
      int var2;
      return var0 & -2147418113 | (var2 = var1 << 16);
   }
}
