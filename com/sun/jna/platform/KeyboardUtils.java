package com.sun.jna.platform;

import com.sun.jna.Platform;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.win32.User32;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;

public class KeyboardUtils {
   static final KeyboardUtils.NativeKeyboardUtils INSTANCE;

   public KeyboardUtils() {
   }

   public static boolean isPressed(int var0, int var1) {
      return INSTANCE.isPressed(var0, var1);
   }

   public static boolean isPressed(int var0) {
      return INSTANCE.isPressed(var0);
   }

   static {
      if(GraphicsEnvironment.isHeadless()) {
         throw new HeadlessException("KeyboardUtils requires a keyboard");
      } else {
         if(Platform.isWindows()) {
            INSTANCE = new KeyboardUtils.W32KeyboardUtils();
         } else {
            if(Platform.isMac()) {
               INSTANCE = new KeyboardUtils.MacKeyboardUtils();
               throw new UnsupportedOperationException("No support (yet) for " + System.getProperty("os.name"));
            }

            INSTANCE = new KeyboardUtils.X11KeyboardUtils();
         }

      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class X11KeyboardUtils extends KeyboardUtils.NativeKeyboardUtils {
      private X11KeyboardUtils() {
         super((KeyboardUtils.SyntheticClass_1)null);
      }

      private int toKeySym(int var1, int var2) {
         return var1 >= 65 && var1 <= 90?97 + (var1 - 65):(var1 >= 48 && var1 <= 57?48 + (var1 - 48):(var1 == 16?((var2 & 3) != 0?'\uffe1':'\uffe1'):(var1 == 17?((var2 & 3) != 0?'\uffe4':'\uffe3'):(var1 == 18?((var2 & 3) != 0?'\uffea':'\uffe9'):(var1 == 157?((var2 & 3) != 0?'\uffe8':'\uffe7'):0)))));
      }

      public boolean isPressed(int var1, int var2) {
         X11 var3 = X11.INSTANCE;
         X11.Display var4 = var3.XOpenDisplay((String)null);
         if(var4 == null) {
            throw new Error("Can\'t open X Display");
         } else {
            try {
               byte[] var5 = new byte[32];
               var3.XQueryKeymap(var4, var5);
               int var6 = this.toKeySym(var1, var2);

               for(int var7 = 5; var7 < 256; ++var7) {
                  int var8 = var7 / 8;
                  int var9 = var7 % 8;
                  if((var5[var8] & 1 << var9) != 0) {
                     int var10 = var3.XKeycodeToKeysym(var4, (byte)var7, 0).intValue();
                     if(var10 == var6) {
                        boolean var11 = true;
                        return var11;
                     }
                  }
               }
            } finally {
               var3.XCloseDisplay(var4);
            }

            return false;
         }
      }

      // $FF: synthetic method
      X11KeyboardUtils(KeyboardUtils.SyntheticClass_1 var1) {
         this();
      }
   }

   private static class MacKeyboardUtils extends KeyboardUtils.NativeKeyboardUtils {
      private MacKeyboardUtils() {
         super((KeyboardUtils.SyntheticClass_1)null);
      }

      public boolean isPressed(int var1, int var2) {
         return false;
      }

      // $FF: synthetic method
      MacKeyboardUtils(KeyboardUtils.SyntheticClass_1 var1) {
         this();
      }
   }

   private static class W32KeyboardUtils extends KeyboardUtils.NativeKeyboardUtils {
      private W32KeyboardUtils() {
         super((KeyboardUtils.SyntheticClass_1)null);
      }

      private int toNative(int var1, int var2) {
         return (var1 < 65 || var1 > 90) && (var1 < 48 || var1 > 57)?(var1 == 16?((var2 & 3) != 0?161:((var2 & 2) != 0?160:16)):(var1 == 17?((var2 & 3) != 0?163:((var2 & 2) != 0?162:17)):(var1 == 18?((var2 & 3) != 0?165:((var2 & 2) != 0?164:18)):0))):var1;
      }

      public boolean isPressed(int var1, int var2) {
         User32 var3 = User32.INSTANCE;
         return (var3.GetAsyncKeyState(this.toNative(var1, var2)) & '\u8000') != 0;
      }

      // $FF: synthetic method
      W32KeyboardUtils(KeyboardUtils.SyntheticClass_1 var1) {
         this();
      }
   }

   private abstract static class NativeKeyboardUtils {
      private NativeKeyboardUtils() {
      }

      public abstract boolean isPressed(int var1, int var2);

      public boolean isPressed(int var1) {
         return this.isPressed(var1, 0);
      }

      // $FF: synthetic method
      NativeKeyboardUtils(KeyboardUtils.SyntheticClass_1 var1) {
         this();
      }
   }
}
