package com.sun.jna.platform.win32;

import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.WinNT;

public class Win32Exception extends RuntimeException {
   private static final long serialVersionUID = 1L;
   private WinNT.HRESULT _hr;

   public WinNT.HRESULT getHR() {
      return this._hr;
   }

   public Win32Exception(WinNT.HRESULT var1) {
      super(Kernel32Util.formatMessageFromHR(var1));
      this._hr = var1;
   }

   public Win32Exception(int var1) {
      this(W32Errors.HRESULT_FROM_WIN32(var1));
   }
}
