package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;

public interface WinCrypt extends StdCallLibrary {
   int CRYPTPROTECT_PROMPT_ON_UNPROTECT = 1;
   int CRYPTPROTECT_PROMPT_ON_PROTECT = 2;
   int CRYPTPROTECT_PROMPT_RESERVED = 4;
   int CRYPTPROTECT_PROMPT_STRONG = 8;
   int CRYPTPROTECT_PROMPT_REQUIRE_STRONG = 16;
   int CRYPTPROTECT_UI_FORBIDDEN = 1;
   int CRYPTPROTECT_LOCAL_MACHINE = 4;
   int CRYPTPROTECT_CRED_SYNC = 8;
   int CRYPTPROTECT_AUDIT = 16;
   int CRYPTPROTECT_NO_RECOVERY = 32;
   int CRYPTPROTECT_VERIFY_PROTECTION = 64;
   int CRYPTPROTECT_CRED_REGENERATE = 128;

   public static class CRYPTPROTECT_PROMPTSTRUCT extends Structure {
      public int cbSize;
      public int dwPromptFlags;
      public WinDef.HWND hwndApp;
      public String szPrompt;

      public CRYPTPROTECT_PROMPTSTRUCT() {
      }

      public CRYPTPROTECT_PROMPTSTRUCT(Pointer var1) {
         super(var1);
         this.read();
      }
   }

   public static class DATA_BLOB extends Structure {
      public int cbData;
      public Pointer pbData;

      public DATA_BLOB() {
      }

      public DATA_BLOB(Pointer var1) {
         super(var1);
         this.read();
      }

      public DATA_BLOB(byte[] var1) {
         this.pbData = new Memory((long)var1.length);
         this.pbData.write(0L, (byte[])var1, 0, var1.length);
         this.cbData = var1.length;
         this.allocateMemory();
      }

      public DATA_BLOB(String var1) {
         this(Native.toByteArray(var1));
      }

      public byte[] getData() {
         return this.pbData == null?null:this.pbData.getByteArray(0L, this.cbData);
      }
   }
}
