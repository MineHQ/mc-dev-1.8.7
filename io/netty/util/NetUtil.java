package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;

public final class NetUtil {
   public static final Inet4Address LOCALHOST4;
   public static final Inet6Address LOCALHOST6;
   public static final InetAddress LOCALHOST;
   public static final NetworkInterface LOOPBACK_IF;
   public static final int SOMAXCONN;
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NetUtil.class);

   public static byte[] createByteArrayFromIpAddressString(String var0) {
      StringTokenizer var1;
      if(isValidIpV4Address(var0)) {
         var1 = new StringTokenizer(var0, ".");
         byte[] var12 = new byte[4];

         for(int var13 = 0; var13 < 4; ++var13) {
            String var10 = var1.nextToken();
            int var11 = Integer.parseInt(var10);
            var12[var13] = (byte)var11;
         }

         return var12;
      } else if(!isValidIpV6Address(var0)) {
         return null;
      } else {
         if(var0.charAt(0) == 91) {
            var0 = var0.substring(1, var0.length() - 1);
         }

         var1 = new StringTokenizer(var0, ":.", true);
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         String var4 = "";
         String var5 = "";
         int var6 = -1;

         while(var1.hasMoreTokens()) {
            var5 = var4;
            var4 = var1.nextToken();
            if(":".equals(var4)) {
               if(":".equals(var5)) {
                  var6 = var2.size();
               } else if(!var5.isEmpty()) {
                  var2.add(var5);
               }
            } else if(".".equals(var4)) {
               var3.add(var5);
            }
         }

         if(":".equals(var5)) {
            if(":".equals(var4)) {
               var6 = var2.size();
            } else {
               var2.add(var4);
            }
         } else if(".".equals(var5)) {
            var3.add(var4);
         }

         int var7 = 8;
         if(!var3.isEmpty()) {
            var7 -= 2;
         }

         int var9;
         if(var6 != -1) {
            int var8 = var7 - var2.size();

            for(var9 = 0; var9 < var8; ++var9) {
               var2.add(var6, "0");
            }
         }

         byte[] var14 = new byte[16];

         for(var9 = 0; var9 < var2.size(); ++var9) {
            convertToBytes((String)var2.get(var9), var14, var9 * 2);
         }

         for(var9 = 0; var9 < var3.size(); ++var9) {
            var14[var9 + 12] = (byte)(Integer.parseInt((String)var3.get(var9)) & 255);
         }

         return var14;
      }
   }

   private static void convertToBytes(String var0, byte[] var1, int var2) {
      int var3 = var0.length();
      int var4 = 0;
      var1[var2] = 0;
      var1[var2 + 1] = 0;
      int var5;
      if(var3 > 3) {
         var5 = getIntValue(var0.charAt(var4++));
         var1[var2] = (byte)(var1[var2] | var5 << 4);
      }

      if(var3 > 2) {
         var5 = getIntValue(var0.charAt(var4++));
         var1[var2] = (byte)(var1[var2] | var5);
      }

      if(var3 > 1) {
         var5 = getIntValue(var0.charAt(var4++));
         var1[var2 + 1] = (byte)(var1[var2 + 1] | var5 << 4);
      }

      var5 = getIntValue(var0.charAt(var4));
      var1[var2 + 1] = (byte)(var1[var2 + 1] | var5 & 15);
   }

   static int getIntValue(char var0) {
      switch(var0) {
      case '0':
         return 0;
      case '1':
         return 1;
      case '2':
         return 2;
      case '3':
         return 3;
      case '4':
         return 4;
      case '5':
         return 5;
      case '6':
         return 6;
      case '7':
         return 7;
      case '8':
         return 8;
      case '9':
         return 9;
      default:
         var0 = Character.toLowerCase(var0);
         switch(var0) {
         case 'a':
            return 10;
         case 'b':
            return 11;
         case 'c':
            return 12;
         case 'd':
            return 13;
         case 'e':
            return 14;
         case 'f':
            return 15;
         default:
            return 0;
         }
      }
   }

   public static boolean isValidIpV6Address(String var0) {
      int var1 = var0.length();
      boolean var2 = false;
      int var3 = 0;
      int var4 = 0;
      int var5 = 0;
      StringBuilder var6 = new StringBuilder();
      char var7 = 0;
      byte var9 = 0;
      if(var1 < 2) {
         return false;
      } else {
         for(int var10 = 0; var10 < var1; ++var10) {
            char var8 = var7;
            var7 = var0.charAt(var10);
            switch(var7) {
            case '%':
               if(var3 == 0) {
                  return false;
               }

               ++var5;
               if(var10 + 1 >= var1) {
                  return false;
               }

               try {
                  if(Integer.parseInt(var0.substring(var10 + 1)) < 0) {
                     return false;
                  }
                  break;
               } catch (NumberFormatException var12) {
                  return false;
               }
            case '.':
               ++var4;
               if(var4 > 3) {
                  return false;
               }

               if(!isValidIp4Word(var6.toString())) {
                  return false;
               }

               if(var3 != 6 && !var2) {
                  return false;
               }

               if(var3 == 7 && var0.charAt(var9) != 58 && var0.charAt(1 + var9) != 58) {
                  return false;
               }

               var6.delete(0, var6.length());
               break;
            case ':':
               if(var10 == var9 && (var0.length() <= var10 || var0.charAt(var10 + 1) != 58)) {
                  return false;
               }

               ++var3;
               if(var3 > 7) {
                  return false;
               }

               if(var4 > 0) {
                  return false;
               }

               if(var8 == 58) {
                  if(var2) {
                     return false;
                  }

                  var2 = true;
               }

               var6.delete(0, var6.length());
               break;
            case '[':
               if(var10 != 0) {
                  return false;
               }

               if(var0.charAt(var1 - 1) != 93) {
                  return false;
               }

               var9 = 1;
               if(var1 < 4) {
                  return false;
               }
               break;
            case ']':
               if(var10 != var1 - 1) {
                  return false;
               }

               if(var0.charAt(0) != 91) {
                  return false;
               }
               break;
            default:
               if(var5 == 0) {
                  if(var6 != null && var6.length() > 3) {
                     return false;
                  }

                  if(!isValidHexChar(var7)) {
                     return false;
                  }
               }

               var6.append(var7);
            }
         }

         if(var4 > 0) {
            if(var4 != 3 || !isValidIp4Word(var6.toString()) || var3 >= 7) {
               return false;
            }
         } else {
            if(var3 != 7 && !var2) {
               return false;
            }

            if(var5 == 0 && var6.length() == 0 && var0.charAt(var1 - 1 - var9) == 58 && var0.charAt(var1 - 2 - var9) != 58) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isValidIp4Word(String var0) {
      if(var0.length() >= 1 && var0.length() <= 3) {
         for(int var2 = 0; var2 < var0.length(); ++var2) {
            char var1 = var0.charAt(var2);
            if(var1 < 48 || var1 > 57) {
               return false;
            }
         }

         return Integer.parseInt(var0) <= 255;
      } else {
         return false;
      }
   }

   static boolean isValidHexChar(char var0) {
      return var0 >= 48 && var0 <= 57 || var0 >= 65 && var0 <= 70 || var0 >= 97 && var0 <= 102;
   }

   public static boolean isValidIpV4Address(String var0) {
      int var1 = 0;
      int var3 = var0.length();
      if(var3 > 15) {
         return false;
      } else {
         StringBuilder var5 = new StringBuilder();

         for(int var2 = 0; var2 < var3; ++var2) {
            char var4 = var0.charAt(var2);
            if(var4 == 46) {
               ++var1;
               if(var1 > 3) {
                  return false;
               }

               if(var5.length() == 0) {
                  return false;
               }

               if(Integer.parseInt(var5.toString()) > 255) {
                  return false;
               }

               var5.delete(0, var5.length());
            } else {
               if(!Character.isDigit(var4)) {
                  return false;
               }

               if(var5.length() > 2) {
                  return false;
               }

               var5.append(var4);
            }
         }

         if(var5.length() != 0 && Integer.parseInt(var5.toString()) <= 255) {
            return var1 == 3;
         } else {
            return false;
         }
      }
   }

   private NetUtil() {
   }

   static {
      byte[] var0 = new byte[]{(byte)127, (byte)0, (byte)0, (byte)1};
      byte[] var1 = new byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)1};
      Inet4Address var2 = null;

      try {
         var2 = (Inet4Address)InetAddress.getByAddress(var0);
      } catch (Exception var38) {
         PlatformDependent.throwException(var38);
      }

      LOCALHOST4 = var2;
      Inet6Address var3 = null;

      try {
         var3 = (Inet6Address)InetAddress.getByAddress(var1);
      } catch (Exception var37) {
         PlatformDependent.throwException(var37);
      }

      LOCALHOST6 = var3;
      ArrayList var4 = new ArrayList();

      try {
         Enumeration var5 = NetworkInterface.getNetworkInterfaces();

         while(var5.hasMoreElements()) {
            NetworkInterface var6 = (NetworkInterface)var5.nextElement();
            if(var6.getInetAddresses().hasMoreElements()) {
               var4.add(var6);
            }
         }
      } catch (SocketException var42) {
         logger.warn("Failed to retrieve the list of available network interfaces", (Throwable)var42);
      }

      NetworkInterface var43 = null;
      Object var44 = null;
      Iterator var7 = var4.iterator();

      NetworkInterface var8;
      Enumeration var9;
      label416:
      while(var7.hasNext()) {
         var8 = (NetworkInterface)var7.next();
         var9 = var8.getInetAddresses();

         while(var9.hasMoreElements()) {
            InetAddress var10 = (InetAddress)var9.nextElement();
            if(var10.isLoopbackAddress()) {
               var43 = var8;
               var44 = var10;
               break label416;
            }
         }
      }

      if(var43 == null) {
         try {
            var7 = var4.iterator();

            while(var7.hasNext()) {
               var8 = (NetworkInterface)var7.next();
               if(var8.isLoopback()) {
                  var9 = var8.getInetAddresses();
                  if(var9.hasMoreElements()) {
                     var43 = var8;
                     var44 = (InetAddress)var9.nextElement();
                     break;
                  }
               }
            }

            if(var43 == null) {
               logger.warn("Failed to find the loopback interface");
            }
         } catch (SocketException var41) {
            logger.warn("Failed to find the loopback interface", (Throwable)var41);
         }
      }

      if(var43 != null) {
         logger.debug("Loopback interface: {} ({}, {})", new Object[]{var43.getName(), var43.getDisplayName(), ((InetAddress)var44).getHostAddress()});
      } else if(var44 == null) {
         try {
            if(NetworkInterface.getByInetAddress(LOCALHOST6) != null) {
               logger.debug("Using hard-coded IPv6 localhost address: {}", (Object)var3);
               var44 = var3;
            }
         } catch (Exception var36) {
            ;
         } finally {
            if(var44 == null) {
               logger.debug("Using hard-coded IPv4 localhost address: {}", (Object)var2);
               var44 = var2;
            }

         }
      }

      LOOPBACK_IF = var43;
      LOCALHOST = (InetAddress)var44;
      int var45 = PlatformDependent.isWindows()?200:128;
      File var46 = new File("/proc/sys/net/core/somaxconn");
      if(var46.exists()) {
         BufferedReader var47 = null;

         try {
            var47 = new BufferedReader(new FileReader(var46));
            var45 = Integer.parseInt(var47.readLine());
            if(logger.isDebugEnabled()) {
               logger.debug("{}: {}", var46, Integer.valueOf(var45));
            }
         } catch (Exception var35) {
            logger.debug("Failed to get SOMAXCONN from: {}", var46, var35);
         } finally {
            if(var47 != null) {
               try {
                  var47.close();
               } catch (Exception var34) {
                  ;
               }
            }

         }
      } else if(logger.isDebugEnabled()) {
         logger.debug("{}: {} (non-existent)", var46, Integer.valueOf(var45));
      }

      SOMAXCONN = var45;
   }
}
