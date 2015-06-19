package com.sun.jna.platform.win32;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public abstract class Advapi32Util {
   public Advapi32Util() {
   }

   public static String getUserName() {
      char[] var0 = new char[128];
      IntByReference var1 = new IntByReference(var0.length);
      boolean var2 = Advapi32.INSTANCE.GetUserNameW(var0, var1);
      if(!var2) {
         switch(Kernel32.INSTANCE.GetLastError()) {
         case 122:
            var0 = new char[var1.getValue()];
            var2 = Advapi32.INSTANCE.GetUserNameW(var0, var1);
            break;
         default:
            throw new Win32Exception(Native.getLastError());
         }
      }

      if(!var2) {
         throw new Win32Exception(Native.getLastError());
      } else {
         return Native.toString(var0);
      }
   }

   public static Advapi32Util.Account getAccountByName(String var0) {
      return getAccountByName((String)null, var0);
   }

   public static Advapi32Util.Account getAccountByName(String var0, String var1) {
      IntByReference var2 = new IntByReference(0);
      IntByReference var3 = new IntByReference(0);
      PointerByReference var4 = new PointerByReference();
      if(Advapi32.INSTANCE.LookupAccountName(var0, var1, (WinNT.PSID)null, var2, (char[])null, var3, var4)) {
         throw new RuntimeException("LookupAccountNameW was expected to fail with ERROR_INSUFFICIENT_BUFFER");
      } else {
         int var5 = Kernel32.INSTANCE.GetLastError();
         if(var2.getValue() != 0 && var5 == 122) {
            Memory var6 = new Memory((long)var2.getValue());
            WinNT.PSID var7 = new WinNT.PSID(var6);
            char[] var8 = new char[var3.getValue() + 1];
            if(!Advapi32.INSTANCE.LookupAccountName(var0, var1, var7, var2, var8, var3, var4)) {
               throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            } else {
               Advapi32Util.Account var9 = new Advapi32Util.Account();
               var9.accountType = var4.getPointer().getInt(0L);
               var9.name = var1;
               String[] var10 = var1.split("\\\\", 2);
               String[] var11 = var1.split("@", 2);
               if(var10.length == 2) {
                  var9.name = var10[1];
               } else if(var11.length == 2) {
                  var9.name = var11[0];
               } else {
                  var9.name = var1;
               }

               if(var3.getValue() > 0) {
                  var9.domain = Native.toString(var8);
                  var9.fqn = var9.domain + "\\" + var9.name;
               } else {
                  var9.fqn = var9.name;
               }

               var9.sid = var7.getBytes();
               var9.sidString = convertSidToStringSid(new WinNT.PSID(var9.sid));
               return var9;
            }
         } else {
            throw new Win32Exception(var5);
         }
      }
   }

   public static Advapi32Util.Account getAccountBySid(WinNT.PSID var0) {
      return getAccountBySid((String)null, (WinNT.PSID)var0);
   }

   public static Advapi32Util.Account getAccountBySid(String var0, WinNT.PSID var1) {
      IntByReference var2 = new IntByReference();
      IntByReference var3 = new IntByReference();
      PointerByReference var4 = new PointerByReference();
      if(Advapi32.INSTANCE.LookupAccountSid((String)null, var1, (char[])null, var2, (char[])null, var3, var4)) {
         throw new RuntimeException("LookupAccountSidW was expected to fail with ERROR_INSUFFICIENT_BUFFER");
      } else {
         int var5 = Kernel32.INSTANCE.GetLastError();
         if(var2.getValue() != 0 && var5 == 122) {
            char[] var6 = new char[var3.getValue()];
            char[] var7 = new char[var2.getValue()];
            if(!Advapi32.INSTANCE.LookupAccountSid((String)null, var1, var7, var2, var6, var3, var4)) {
               throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            } else {
               Advapi32Util.Account var8 = new Advapi32Util.Account();
               var8.accountType = var4.getPointer().getInt(0L);
               var8.name = Native.toString(var7);
               if(var3.getValue() > 0) {
                  var8.domain = Native.toString(var6);
                  var8.fqn = var8.domain + "\\" + var8.name;
               } else {
                  var8.fqn = var8.name;
               }

               var8.sid = var1.getBytes();
               var8.sidString = convertSidToStringSid(var1);
               return var8;
            }
         } else {
            throw new Win32Exception(var5);
         }
      }
   }

   public static String convertSidToStringSid(WinNT.PSID var0) {
      PointerByReference var1 = new PointerByReference();
      if(!Advapi32.INSTANCE.ConvertSidToStringSid(var0, var1)) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      } else {
         String var2 = var1.getValue().getString(0L, true);
         Kernel32.INSTANCE.LocalFree(var1.getValue());
         return var2;
      }
   }

   public static byte[] convertStringSidToSid(String var0) {
      WinNT.PSIDByReference var1 = new WinNT.PSIDByReference();
      if(!Advapi32.INSTANCE.ConvertStringSidToSid(var0, var1)) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      } else {
         return var1.getValue().getBytes();
      }
   }

   public static boolean isWellKnownSid(String var0, int var1) {
      WinNT.PSIDByReference var2 = new WinNT.PSIDByReference();
      if(!Advapi32.INSTANCE.ConvertStringSidToSid(var0, var2)) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      } else {
         return Advapi32.INSTANCE.IsWellKnownSid(var2.getValue(), var1);
      }
   }

   public static boolean isWellKnownSid(byte[] var0, int var1) {
      WinNT.PSID var2 = new WinNT.PSID(var0);
      return Advapi32.INSTANCE.IsWellKnownSid(var2, var1);
   }

   public static Advapi32Util.Account getAccountBySid(String var0) {
      return getAccountBySid((String)null, (String)var0);
   }

   public static Advapi32Util.Account getAccountBySid(String var0, String var1) {
      return getAccountBySid(var0, new WinNT.PSID(convertStringSidToSid(var1)));
   }

   public static Advapi32Util.Account[] getTokenGroups(WinNT.HANDLE var0) {
      IntByReference var1 = new IntByReference();
      if(Advapi32.INSTANCE.GetTokenInformation(var0, 2, (Structure)null, 0, var1)) {
         throw new RuntimeException("Expected GetTokenInformation to fail with ERROR_INSUFFICIENT_BUFFER");
      } else {
         int var2 = Kernel32.INSTANCE.GetLastError();
         if(var2 != 122) {
            throw new Win32Exception(var2);
         } else {
            WinNT.TOKEN_GROUPS var3 = new WinNT.TOKEN_GROUPS(var1.getValue());
            if(!Advapi32.INSTANCE.GetTokenInformation(var0, 2, var3, var1.getValue(), var1)) {
               throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            } else {
               ArrayList var4 = new ArrayList();
               WinNT.SID_AND_ATTRIBUTES[] var5 = var3.getGroups();
               int var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  WinNT.SID_AND_ATTRIBUTES var8 = var5[var7];
                  Advapi32Util.Account var9 = null;

                  try {
                     var9 = getAccountBySid((WinNT.PSID)var8.Sid);
                  } catch (Exception var11) {
                     var9 = new Advapi32Util.Account();
                     var9.sid = var8.Sid.getBytes();
                     var9.sidString = convertSidToStringSid(var8.Sid);
                     var9.name = var9.sidString;
                     var9.fqn = var9.sidString;
                     var9.accountType = 2;
                  }

                  var4.add(var9);
               }

               return (Advapi32Util.Account[])var4.toArray(new Advapi32Util.Account[0]);
            }
         }
      }
   }

   public static Advapi32Util.Account getTokenAccount(WinNT.HANDLE var0) {
      IntByReference var1 = new IntByReference();
      if(Advapi32.INSTANCE.GetTokenInformation(var0, 1, (Structure)null, 0, var1)) {
         throw new RuntimeException("Expected GetTokenInformation to fail with ERROR_INSUFFICIENT_BUFFER");
      } else {
         int var2 = Kernel32.INSTANCE.GetLastError();
         if(var2 != 122) {
            throw new Win32Exception(var2);
         } else {
            WinNT.TOKEN_USER var3 = new WinNT.TOKEN_USER(var1.getValue());
            if(!Advapi32.INSTANCE.GetTokenInformation(var0, 1, var3, var1.getValue(), var1)) {
               throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            } else {
               return getAccountBySid((WinNT.PSID)var3.User.Sid);
            }
         }
      }
   }

   public static Advapi32Util.Account[] getCurrentUserGroups() {
      WinNT.HANDLEByReference var0 = new WinNT.HANDLEByReference();

      Advapi32Util.Account[] var6;
      try {
         WinNT.HANDLE var1 = Kernel32.INSTANCE.GetCurrentThread();
         if(!Advapi32.INSTANCE.OpenThreadToken(var1, 10, true, var0)) {
            if(1008 != Kernel32.INSTANCE.GetLastError()) {
               throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }

            WinNT.HANDLE var2 = Kernel32.INSTANCE.GetCurrentProcess();
            if(!Advapi32.INSTANCE.OpenProcessToken(var2, 10, var0)) {
               throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
         }

         var6 = getTokenGroups(var0.getValue());
      } finally {
         if(var0.getValue() != WinBase.INVALID_HANDLE_VALUE && !Kernel32.INSTANCE.CloseHandle(var0.getValue())) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         }

      }

      return var6;
   }

   public static boolean registryKeyExists(WinReg.HKEY var0, String var1) {
      WinReg.HKEYByReference var2 = new WinReg.HKEYByReference();
      int var3 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131097, var2);
      switch(var3) {
      case 0:
         Advapi32.INSTANCE.RegCloseKey(var2.getValue());
         return true;
      case 2:
         return false;
      default:
         throw new Win32Exception(var3);
      }
   }

   public static boolean registryValueExists(WinReg.HKEY var0, String var1, String var2) {
      WinReg.HKEYByReference var3 = new WinReg.HKEYByReference();
      int var4 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131097, var3);

      try {
         switch(var4) {
         case 0:
            IntByReference var11 = new IntByReference();
            IntByReference var6 = new IntByReference();
            var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (char[])((char[])null), var11);
            boolean var7;
            switch(var4) {
            case 0:
            case 122:
               var7 = true;
               return var7;
            case 2:
               var7 = false;
               return var7;
            default:
               throw new Win32Exception(var4);
            }
         case 2:
            boolean var5 = false;
            return var5;
         default:
            throw new Win32Exception(var4);
         }
      } finally {
         if(var3.getValue() != WinBase.INVALID_HANDLE_VALUE) {
            var4 = Advapi32.INSTANCE.RegCloseKey(var3.getValue());
            if(var4 != 0) {
               throw new Win32Exception(var4);
            }
         }

      }
   }

   public static String registryGetStringValue(WinReg.HKEY var0, String var1, String var2) {
      WinReg.HKEYByReference var3 = new WinReg.HKEYByReference();
      int var4 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131097, var3);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      } else {
         String var8;
         try {
            IntByReference var5 = new IntByReference();
            IntByReference var6 = new IntByReference();
            var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (char[])((char[])null), var5);
            if(var4 != 0 && var4 != 122) {
               throw new Win32Exception(var4);
            }

            if(var6.getValue() != 1 && var6.getValue() != 2) {
               throw new RuntimeException("Unexpected registry type " + var6.getValue() + ", expected REG_SZ or REG_EXPAND_SZ");
            }

            char[] var7 = new char[var5.getValue()];
            var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (char[])var7, var5);
            if(var4 != 0 && var4 != 122) {
               throw new Win32Exception(var4);
            }

            var8 = Native.toString(var7);
         } finally {
            var4 = Advapi32.INSTANCE.RegCloseKey(var3.getValue());
            if(var4 != 0) {
               throw new Win32Exception(var4);
            }

         }

         return var8;
      }
   }

   public static String registryGetExpandableStringValue(WinReg.HKEY var0, String var1, String var2) {
      WinReg.HKEYByReference var3 = new WinReg.HKEYByReference();
      int var4 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131097, var3);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      } else {
         String var8;
         try {
            IntByReference var5 = new IntByReference();
            IntByReference var6 = new IntByReference();
            var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (char[])((char[])null), var5);
            if(var4 != 0 && var4 != 122) {
               throw new Win32Exception(var4);
            }

            if(var6.getValue() != 2) {
               throw new RuntimeException("Unexpected registry type " + var6.getValue() + ", expected REG_SZ");
            }

            char[] var7 = new char[var5.getValue()];
            var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (char[])var7, var5);
            if(var4 != 0 && var4 != 122) {
               throw new Win32Exception(var4);
            }

            var8 = Native.toString(var7);
         } finally {
            var4 = Advapi32.INSTANCE.RegCloseKey(var3.getValue());
            if(var4 != 0) {
               throw new Win32Exception(var4);
            }

         }

         return var8;
      }
   }

   public static String[] registryGetStringArray(WinReg.HKEY var0, String var1, String var2) {
      WinReg.HKEYByReference var3 = new WinReg.HKEYByReference();
      int var4 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131097, var3);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      } else {
         try {
            IntByReference var5 = new IntByReference();
            IntByReference var6 = new IntByReference();
            var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (char[])((char[])null), var5);
            if(var4 != 0 && var4 != 122) {
               throw new Win32Exception(var4);
            } else if(var6.getValue() != 7) {
               throw new RuntimeException("Unexpected registry type " + var6.getValue() + ", expected REG_SZ");
            } else {
               Memory var7 = new Memory((long)var5.getValue());
               var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (Pointer)var7, var5);
               if(var4 != 0 && var4 != 122) {
                  throw new Win32Exception(var4);
               } else {
                  ArrayList var8 = new ArrayList();
                  int var9 = 0;

                  while((long)var9 < var7.size()) {
                     String var10 = var7.getString((long)var9, true);
                     var9 += var10.length() * Native.WCHAR_SIZE;
                     var9 += Native.WCHAR_SIZE;
                     var8.add(var10);
                  }

                  String[] var14 = (String[])var8.toArray(new String[0]);
                  return var14;
               }
            }
         } finally {
            var4 = Advapi32.INSTANCE.RegCloseKey(var3.getValue());
            if(var4 != 0) {
               throw new Win32Exception(var4);
            }
         }
      }
   }

   public static byte[] registryGetBinaryValue(WinReg.HKEY var0, String var1, String var2) {
      WinReg.HKEYByReference var3 = new WinReg.HKEYByReference();
      int var4 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131097, var3);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      } else {
         byte[] var8;
         try {
            IntByReference var5 = new IntByReference();
            IntByReference var6 = new IntByReference();
            var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (char[])((char[])null), var5);
            if(var4 != 0 && var4 != 122) {
               throw new Win32Exception(var4);
            }

            if(var6.getValue() != 3) {
               throw new RuntimeException("Unexpected registry type " + var6.getValue() + ", expected REG_BINARY");
            }

            byte[] var7 = new byte[var5.getValue()];
            var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (byte[])var7, var5);
            if(var4 != 0 && var4 != 122) {
               throw new Win32Exception(var4);
            }

            var8 = var7;
         } finally {
            var4 = Advapi32.INSTANCE.RegCloseKey(var3.getValue());
            if(var4 != 0) {
               throw new Win32Exception(var4);
            }

         }

         return var8;
      }
   }

   public static int registryGetIntValue(WinReg.HKEY var0, String var1, String var2) {
      WinReg.HKEYByReference var3 = new WinReg.HKEYByReference();
      int var4 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131097, var3);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      } else {
         int var8;
         try {
            IntByReference var5 = new IntByReference();
            IntByReference var6 = new IntByReference();
            var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (char[])((char[])null), var5);
            if(var4 != 0 && var4 != 122) {
               throw new Win32Exception(var4);
            }

            if(var6.getValue() != 4) {
               throw new RuntimeException("Unexpected registry type " + var6.getValue() + ", expected REG_DWORD");
            }

            IntByReference var7 = new IntByReference();
            var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (IntByReference)var7, var5);
            if(var4 != 0 && var4 != 122) {
               throw new Win32Exception(var4);
            }

            var8 = var7.getValue();
         } finally {
            var4 = Advapi32.INSTANCE.RegCloseKey(var3.getValue());
            if(var4 != 0) {
               throw new Win32Exception(var4);
            }

         }

         return var8;
      }
   }

   public static long registryGetLongValue(WinReg.HKEY var0, String var1, String var2) {
      WinReg.HKEYByReference var3 = new WinReg.HKEYByReference();
      int var4 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131097, var3);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      } else {
         long var8;
         try {
            IntByReference var5 = new IntByReference();
            IntByReference var6 = new IntByReference();
            var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (char[])((char[])null), var5);
            if(var4 != 0 && var4 != 122) {
               throw new Win32Exception(var4);
            }

            if(var6.getValue() != 11) {
               throw new RuntimeException("Unexpected registry type " + var6.getValue() + ", expected REG_QWORD");
            }

            LongByReference var7 = new LongByReference();
            var4 = Advapi32.INSTANCE.RegQueryValueEx(var3.getValue(), var2, 0, var6, (LongByReference)var7, var5);
            if(var4 != 0 && var4 != 122) {
               throw new Win32Exception(var4);
            }

            var8 = var7.getValue();
         } finally {
            var4 = Advapi32.INSTANCE.RegCloseKey(var3.getValue());
            if(var4 != 0) {
               throw new Win32Exception(var4);
            }

         }

         return var8;
      }
   }

   public static boolean registryCreateKey(WinReg.HKEY var0, String var1) {
      WinReg.HKEYByReference var2 = new WinReg.HKEYByReference();
      IntByReference var3 = new IntByReference();
      int var4 = Advapi32.INSTANCE.RegCreateKeyEx(var0, var1, 0, (String)null, 0, 131097, (WinBase.SECURITY_ATTRIBUTES)null, var2, var3);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      } else {
         var4 = Advapi32.INSTANCE.RegCloseKey(var2.getValue());
         if(var4 != 0) {
            throw new Win32Exception(var4);
         } else {
            return 1 == var3.getValue();
         }
      }
   }

   public static boolean registryCreateKey(WinReg.HKEY var0, String var1, String var2) {
      WinReg.HKEYByReference var3 = new WinReg.HKEYByReference();
      int var4 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 4, var3);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      } else {
         boolean var5;
         try {
            var5 = registryCreateKey(var3.getValue(), var2);
         } finally {
            var4 = Advapi32.INSTANCE.RegCloseKey(var3.getValue());
            if(var4 != 0) {
               throw new Win32Exception(var4);
            }

         }

         return var5;
      }
   }

   public static void registrySetIntValue(WinReg.HKEY var0, String var1, int var2) {
      byte[] var3 = new byte[]{(byte)(var2 & 255), (byte)(var2 >> 8 & 255), (byte)(var2 >> 16 & 255), (byte)(var2 >> 24 & 255)};
      int var4 = Advapi32.INSTANCE.RegSetValueEx(var0, var1, 0, 4, (byte[])var3, 4);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      }
   }

   public static void registrySetIntValue(WinReg.HKEY var0, String var1, String var2, int var3) {
      WinReg.HKEYByReference var4 = new WinReg.HKEYByReference();
      int var5 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131103, var4);
      if(var5 != 0) {
         throw new Win32Exception(var5);
      } else {
         try {
            registrySetIntValue(var4.getValue(), var2, var3);
         } finally {
            var5 = Advapi32.INSTANCE.RegCloseKey(var4.getValue());
            if(var5 != 0) {
               throw new Win32Exception(var5);
            }

         }

      }
   }

   public static void registrySetLongValue(WinReg.HKEY var0, String var1, long var2) {
      byte[] var4 = new byte[]{(byte)((int)(var2 & 255L)), (byte)((int)(var2 >> 8 & 255L)), (byte)((int)(var2 >> 16 & 255L)), (byte)((int)(var2 >> 24 & 255L)), (byte)((int)(var2 >> 32 & 255L)), (byte)((int)(var2 >> 40 & 255L)), (byte)((int)(var2 >> 48 & 255L)), (byte)((int)(var2 >> 56 & 255L))};
      int var5 = Advapi32.INSTANCE.RegSetValueEx(var0, var1, 0, 11, (byte[])var4, 8);
      if(var5 != 0) {
         throw new Win32Exception(var5);
      }
   }

   public static void registrySetLongValue(WinReg.HKEY var0, String var1, String var2, long var3) {
      WinReg.HKEYByReference var5 = new WinReg.HKEYByReference();
      int var6 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131103, var5);
      if(var6 != 0) {
         throw new Win32Exception(var6);
      } else {
         try {
            registrySetLongValue(var5.getValue(), var2, var3);
         } finally {
            var6 = Advapi32.INSTANCE.RegCloseKey(var5.getValue());
            if(var6 != 0) {
               throw new Win32Exception(var6);
            }

         }

      }
   }

   public static void registrySetStringValue(WinReg.HKEY var0, String var1, String var2) {
      char[] var3 = Native.toCharArray(var2);
      int var4 = Advapi32.INSTANCE.RegSetValueEx(var0, var1, 0, 1, (char[])var3, var3.length * Native.WCHAR_SIZE);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      }
   }

   public static void registrySetStringValue(WinReg.HKEY var0, String var1, String var2, String var3) {
      WinReg.HKEYByReference var4 = new WinReg.HKEYByReference();
      int var5 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131103, var4);
      if(var5 != 0) {
         throw new Win32Exception(var5);
      } else {
         try {
            registrySetStringValue(var4.getValue(), var2, var3);
         } finally {
            var5 = Advapi32.INSTANCE.RegCloseKey(var4.getValue());
            if(var5 != 0) {
               throw new Win32Exception(var5);
            }

         }

      }
   }

   public static void registrySetExpandableStringValue(WinReg.HKEY var0, String var1, String var2) {
      char[] var3 = Native.toCharArray(var2);
      int var4 = Advapi32.INSTANCE.RegSetValueEx(var0, var1, 0, 2, (char[])var3, var3.length * Native.WCHAR_SIZE);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      }
   }

   public static void registrySetExpandableStringValue(WinReg.HKEY var0, String var1, String var2, String var3) {
      WinReg.HKEYByReference var4 = new WinReg.HKEYByReference();
      int var5 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131103, var4);
      if(var5 != 0) {
         throw new Win32Exception(var5);
      } else {
         try {
            registrySetExpandableStringValue(var4.getValue(), var2, var3);
         } finally {
            var5 = Advapi32.INSTANCE.RegCloseKey(var4.getValue());
            if(var5 != 0) {
               throw new Win32Exception(var5);
            }

         }

      }
   }

   public static void registrySetStringArray(WinReg.HKEY var0, String var1, String[] var2) {
      int var3 = 0;
      String[] var4 = var2;
      int var5 = var2.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         var3 += var7.length() * Native.WCHAR_SIZE;
         var3 += Native.WCHAR_SIZE;
      }

      int var10 = 0;
      Memory var11 = new Memory((long)var3);
      String[] var12 = var2;
      int var13 = var2.length;

      for(int var8 = 0; var8 < var13; ++var8) {
         String var9 = var12[var8];
         var11.setString((long)var10, var9, true);
         var10 += var9.length() * Native.WCHAR_SIZE;
         var10 += Native.WCHAR_SIZE;
      }

      var6 = Advapi32.INSTANCE.RegSetValueEx(var0, var1, 0, 7, (byte[])var11.getByteArray(0L, var3), var3);
      if(var6 != 0) {
         throw new Win32Exception(var6);
      }
   }

   public static void registrySetStringArray(WinReg.HKEY var0, String var1, String var2, String[] var3) {
      WinReg.HKEYByReference var4 = new WinReg.HKEYByReference();
      int var5 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131103, var4);
      if(var5 != 0) {
         throw new Win32Exception(var5);
      } else {
         try {
            registrySetStringArray(var4.getValue(), var2, var3);
         } finally {
            var5 = Advapi32.INSTANCE.RegCloseKey(var4.getValue());
            if(var5 != 0) {
               throw new Win32Exception(var5);
            }

         }

      }
   }

   public static void registrySetBinaryValue(WinReg.HKEY var0, String var1, byte[] var2) {
      int var3 = Advapi32.INSTANCE.RegSetValueEx(var0, var1, 0, 3, (byte[])var2, var2.length);
      if(var3 != 0) {
         throw new Win32Exception(var3);
      }
   }

   public static void registrySetBinaryValue(WinReg.HKEY var0, String var1, String var2, byte[] var3) {
      WinReg.HKEYByReference var4 = new WinReg.HKEYByReference();
      int var5 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131103, var4);
      if(var5 != 0) {
         throw new Win32Exception(var5);
      } else {
         try {
            registrySetBinaryValue(var4.getValue(), var2, var3);
         } finally {
            var5 = Advapi32.INSTANCE.RegCloseKey(var4.getValue());
            if(var5 != 0) {
               throw new Win32Exception(var5);
            }

         }

      }
   }

   public static void registryDeleteKey(WinReg.HKEY var0, String var1) {
      int var2 = Advapi32.INSTANCE.RegDeleteKey(var0, var1);
      if(var2 != 0) {
         throw new Win32Exception(var2);
      }
   }

   public static void registryDeleteKey(WinReg.HKEY var0, String var1, String var2) {
      WinReg.HKEYByReference var3 = new WinReg.HKEYByReference();
      int var4 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131103, var3);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      } else {
         try {
            registryDeleteKey(var3.getValue(), var2);
         } finally {
            var4 = Advapi32.INSTANCE.RegCloseKey(var3.getValue());
            if(var4 != 0) {
               throw new Win32Exception(var4);
            }

         }

      }
   }

   public static void registryDeleteValue(WinReg.HKEY var0, String var1) {
      int var2 = Advapi32.INSTANCE.RegDeleteValue(var0, var1);
      if(var2 != 0) {
         throw new Win32Exception(var2);
      }
   }

   public static void registryDeleteValue(WinReg.HKEY var0, String var1, String var2) {
      WinReg.HKEYByReference var3 = new WinReg.HKEYByReference();
      int var4 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131103, var3);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      } else {
         try {
            registryDeleteValue(var3.getValue(), var2);
         } finally {
            var4 = Advapi32.INSTANCE.RegCloseKey(var3.getValue());
            if(var4 != 0) {
               throw new Win32Exception(var4);
            }

         }

      }
   }

   public static String[] registryGetKeys(WinReg.HKEY var0) {
      IntByReference var1 = new IntByReference();
      IntByReference var2 = new IntByReference();
      int var3 = Advapi32.INSTANCE.RegQueryInfoKey(var0, (char[])null, (IntByReference)null, (IntByReference)null, var1, var2, (IntByReference)null, (IntByReference)null, (IntByReference)null, (IntByReference)null, (IntByReference)null, (WinBase.FILETIME)null);
      if(var3 != 0) {
         throw new Win32Exception(var3);
      } else {
         ArrayList var4 = new ArrayList(var1.getValue());
         char[] var5 = new char[var2.getValue() + 1];

         for(int var6 = 0; var6 < var1.getValue(); ++var6) {
            IntByReference var7 = new IntByReference(var2.getValue() + 1);
            var3 = Advapi32.INSTANCE.RegEnumKeyEx(var0, var6, var5, var7, (IntByReference)null, (char[])null, (IntByReference)null, (WinBase.FILETIME)null);
            if(var3 != 0) {
               throw new Win32Exception(var3);
            }

            var4.add(Native.toString(var5));
         }

         return (String[])var4.toArray(new String[0]);
      }
   }

   public static String[] registryGetKeys(WinReg.HKEY var0, String var1) {
      WinReg.HKEYByReference var2 = new WinReg.HKEYByReference();
      int var3 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131097, var2);
      if(var3 != 0) {
         throw new Win32Exception(var3);
      } else {
         String[] var4;
         try {
            var4 = registryGetKeys(var2.getValue());
         } finally {
            var3 = Advapi32.INSTANCE.RegCloseKey(var2.getValue());
            if(var3 != 0) {
               throw new Win32Exception(var3);
            }

         }

         return var4;
      }
   }

   public static TreeMap<String, Object> registryGetValues(WinReg.HKEY var0) {
      IntByReference var1 = new IntByReference();
      IntByReference var2 = new IntByReference();
      IntByReference var3 = new IntByReference();
      int var4 = Advapi32.INSTANCE.RegQueryInfoKey(var0, (char[])null, (IntByReference)null, (IntByReference)null, (IntByReference)null, (IntByReference)null, (IntByReference)null, var1, var2, var3, (IntByReference)null, (WinBase.FILETIME)null);
      if(var4 != 0) {
         throw new Win32Exception(var4);
      } else {
         TreeMap var5 = new TreeMap();
         char[] var6 = new char[var2.getValue() + 1];
         byte[] var7 = new byte[var3.getValue()];

         for(int var8 = 0; var8 < var1.getValue(); ++var8) {
            IntByReference var9 = new IntByReference(var2.getValue() + 1);
            IntByReference var10 = new IntByReference(var3.getValue());
            IntByReference var11 = new IntByReference();
            var4 = Advapi32.INSTANCE.RegEnumValue(var0, var8, var6, var9, (IntByReference)null, var11, var7, var10);
            if(var4 != 0) {
               throw new Win32Exception(var4);
            }

            String var12 = Native.toString(var6);
            Memory var13 = new Memory((long)var10.getValue());
            var13.write(0L, (byte[])var7, 0, var10.getValue());
            switch(var11.getValue()) {
            case 1:
            case 2:
               var5.put(var12, var13.getString(0L, true));
               break;
            case 3:
               var5.put(var12, var13.getByteArray(0L, var10.getValue()));
               break;
            case 4:
               var5.put(var12, Integer.valueOf(var13.getInt(0L)));
               break;
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            default:
               throw new RuntimeException("Unsupported type: " + var11.getValue());
            case 7:
               Memory var14 = new Memory((long)var10.getValue());
               var14.write(0L, (byte[])var7, 0, var10.getValue());
               ArrayList var15 = new ArrayList();
               int var16 = 0;

               while((long)var16 < var14.size()) {
                  String var17 = var14.getString((long)var16, true);
                  var16 += var17.length() * Native.WCHAR_SIZE;
                  var16 += Native.WCHAR_SIZE;
                  var15.add(var17);
               }

               var5.put(var12, var15.toArray(new String[0]));
               break;
            case 11:
               var5.put(var12, Long.valueOf(var13.getLong(0L)));
            }
         }

         return var5;
      }
   }

   public static TreeMap<String, Object> registryGetValues(WinReg.HKEY var0, String var1) {
      WinReg.HKEYByReference var2 = new WinReg.HKEYByReference();
      int var3 = Advapi32.INSTANCE.RegOpenKeyEx(var0, var1, 0, 131097, var2);
      if(var3 != 0) {
         throw new Win32Exception(var3);
      } else {
         TreeMap var4;
         try {
            var4 = registryGetValues(var2.getValue());
         } finally {
            var3 = Advapi32.INSTANCE.RegCloseKey(var2.getValue());
            if(var3 != 0) {
               throw new Win32Exception(var3);
            }

         }

         return var4;
      }
   }

   public static String getEnvironmentBlock(Map<String, String> var0) {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = var0.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if(var3.getValue() != null) {
            var1.append((String)var3.getKey() + "=" + (String)var3.getValue() + "\u0000");
         }
      }

      return var1.toString() + "\u0000";
   }

   public static WinNT.ACCESS_ACEStructure[] getFileSecurity(String var0, boolean var1) {
      byte var2 = 4;
      int var3 = 1024;
      boolean var4 = false;
      Memory var5 = null;

      do {
         var4 = false;
         var5 = new Memory((long)var3);
         IntByReference var6 = new IntByReference();
         boolean var7 = Advapi32.INSTANCE.GetFileSecurity(new WString(var0), var2, var5, var3, var6);
         int var8;
         if(!var7) {
            var8 = Kernel32.INSTANCE.GetLastError();
            var5.clear();
            if(122 != var8) {
               throw new Win32Exception(var8);
            }
         }

         var8 = var6.getValue();
         if(var3 < var8) {
            var4 = true;
            var3 = var8;
            var5.clear();
         }
      } while(var4);

      WinNT.SECURITY_DESCRIPTOR_RELATIVE var18 = new WinNT.SECURITY_DESCRIPTOR_RELATIVE(var5);
      var5.clear();
      WinNT.ACL var19 = var18.getDiscretionaryACL();
      WinNT.ACCESS_ACEStructure[] var20 = var19.getACEStructures();
      if(var1) {
         HashMap var9 = new HashMap();
         WinNT.ACCESS_ACEStructure[] var10 = var20;
         int var11 = var20.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            WinNT.ACCESS_ACEStructure var13 = var10[var12];
            boolean var14 = (var13.AceFlags & 31) != 0;
            String var15 = var13.getSidString() + "/" + var14 + "/" + var13.getClass().getName();
            WinNT.ACCESS_ACEStructure var16 = (WinNT.ACCESS_ACEStructure)var9.get(var15);
            if(var16 != null) {
               int var17 = var16.Mask;
               var17 |= var13.Mask;
               var16.Mask = var17;
            } else {
               var9.put(var15, var13);
            }
         }

         return (WinNT.ACCESS_ACEStructure[])var9.values().toArray(new WinNT.ACCESS_ACEStructure[var9.size()]);
      } else {
         return var20;
      }
   }

   public static class EventLogIterator implements Iterable<Advapi32Util.EventLogRecord>, Iterator<Advapi32Util.EventLogRecord> {
      private WinNT.HANDLE _h;
      private Memory _buffer;
      private boolean _done;
      private int _dwRead;
      private Pointer _pevlr;
      private int _flags;

      public EventLogIterator(String var1) {
         this((String)null, var1, 4);
      }

      public EventLogIterator(String var1, String var2, int var3) {
         this._h = null;
         this._buffer = new Memory(65536L);
         this._done = false;
         this._dwRead = 0;
         this._pevlr = null;
         this._flags = 4;
         this._flags = var3;
         this._h = Advapi32.INSTANCE.OpenEventLog(var1, var2);
         if(this._h == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
         }
      }

      private boolean read() {
         if(!this._done && this._dwRead <= 0) {
            IntByReference var1 = new IntByReference();
            IntByReference var2 = new IntByReference();
            if(!Advapi32.INSTANCE.ReadEventLog(this._h, 1 | this._flags, 0, this._buffer, (int)this._buffer.size(), var1, var2)) {
               int var3 = Kernel32.INSTANCE.GetLastError();
               if(var3 != 122) {
                  this.close();
                  if(var3 != 38) {
                     throw new Win32Exception(var3);
                  }

                  return false;
               }

               this._buffer = new Memory((long)var2.getValue());
               if(!Advapi32.INSTANCE.ReadEventLog(this._h, 1 | this._flags, 0, this._buffer, (int)this._buffer.size(), var1, var2)) {
                  throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
               }
            }

            this._dwRead = var1.getValue();
            this._pevlr = this._buffer;
            return true;
         } else {
            return false;
         }
      }

      public void close() {
         this._done = true;
         if(this._h != null) {
            if(!Advapi32.INSTANCE.CloseEventLog(this._h)) {
               throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }

            this._h = null;
         }

      }

      public Iterator<Advapi32Util.EventLogRecord> iterator() {
         return this;
      }

      public boolean hasNext() {
         this.read();
         return !this._done;
      }

      public Advapi32Util.EventLogRecord next() {
         this.read();
         Advapi32Util.EventLogRecord var1 = new Advapi32Util.EventLogRecord(this._pevlr);
         this._dwRead -= var1.getLength();
         this._pevlr = this._pevlr.share((long)var1.getLength());
         return var1;
      }

      public void remove() {
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object next() {
         return this.next();
      }
   }

   public static class EventLogRecord {
      private WinNT.EVENTLOGRECORD _record = null;
      private String _source;
      private byte[] _data;
      private String[] _strings;

      public WinNT.EVENTLOGRECORD getRecord() {
         return this._record;
      }

      public int getEventId() {
         return this._record.EventID.intValue();
      }

      public String getSource() {
         return this._source;
      }

      public int getStatusCode() {
         return this._record.EventID.intValue() & '\uffff';
      }

      public int getRecordNumber() {
         return this._record.RecordNumber.intValue();
      }

      public int getLength() {
         return this._record.Length.intValue();
      }

      public String[] getStrings() {
         return this._strings;
      }

      public Advapi32Util.EventLogType getType() {
         switch(this._record.EventType.intValue()) {
         case 0:
         case 4:
            return Advapi32Util.EventLogType.Informational;
         case 1:
            return Advapi32Util.EventLogType.Error;
         case 2:
            return Advapi32Util.EventLogType.Warning;
         case 3:
         case 5:
         case 6:
         case 7:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         default:
            throw new RuntimeException("Invalid type: " + this._record.EventType.intValue());
         case 8:
            return Advapi32Util.EventLogType.AuditSuccess;
         case 16:
            return Advapi32Util.EventLogType.AuditFailure;
         }
      }

      public byte[] getData() {
         return this._data;
      }

      public EventLogRecord(Pointer var1) {
         this._record = new WinNT.EVENTLOGRECORD(var1);
         this._source = var1.getString((long)this._record.size(), true);
         if(this._record.DataLength.intValue() > 0) {
            this._data = var1.getByteArray((long)this._record.DataOffset.intValue(), this._record.DataLength.intValue());
         }

         if(this._record.NumStrings.intValue() > 0) {
            ArrayList var2 = new ArrayList();
            int var3 = this._record.NumStrings.intValue();

            for(long var4 = (long)this._record.StringOffset.intValue(); var3 > 0; --var3) {
               String var6 = var1.getString(var4, true);
               var2.add(var6);
               var4 += (long)(var6.length() * Native.WCHAR_SIZE);
               var4 += (long)Native.WCHAR_SIZE;
            }

            this._strings = (String[])var2.toArray(new String[0]);
         }

      }
   }

   public static enum EventLogType {
      Error,
      Warning,
      Informational,
      AuditSuccess,
      AuditFailure;

      private EventLogType() {
      }
   }

   public static class Account {
      public String name;
      public String domain;
      public byte[] sid;
      public String sidString;
      public int accountType;
      public String fqn;

      public Account() {
      }
   }
}
