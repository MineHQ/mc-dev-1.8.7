package org.apache.logging.log4j.core.helpers;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class UUIDUtil {
   public static final String UUID_SEQUENCE = "org.apache.logging.log4j.uuidSequence";
   private static final String ASSIGNED_SEQUENCES = "org.apache.logging.log4j.assignedSequences";
   private static AtomicInteger count = new AtomicInteger(0);
   private static final long TYPE1 = 4096L;
   private static final byte VARIANT = -128;
   private static final int SEQUENCE_MASK = 16383;
   private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
   private static long uuidSequence = PropertiesUtil.getProperties().getLongProperty("org.apache.logging.log4j.uuidSequence", 0L);
   private static long least;
   private static final long LOW_MASK = 4294967295L;
   private static final long MID_MASK = 281470681743360L;
   private static final long HIGH_MASK = 1152640029630136320L;
   private static final int NODE_SIZE = 8;
   private static final int SHIFT_2 = 16;
   private static final int SHIFT_4 = 32;
   private static final int SHIFT_6 = 48;
   private static final int HUNDRED_NANOS_PER_MILLI = 10000;

   private UUIDUtil() {
   }

   public static UUID getTimeBasedUUID() {
      long var0 = System.currentTimeMillis() * 10000L + 122192928000000000L + (long)(count.incrementAndGet() % 10000);
      long var2 = (var0 & 4294967295L) << 32;
      long var4 = (var0 & 281470681743360L) >> 16;
      long var6 = (var0 & 1152640029630136320L) >> 48;
      long var8 = var2 | var4 | 4096L | var6;
      return new UUID(var8, least);
   }

   static {
      byte[] var0 = null;

      try {
         InetAddress var1 = InetAddress.getLocalHost();

         try {
            NetworkInterface var2 = NetworkInterface.getByInetAddress(var1);
            if(var2 != null && !var2.isLoopback() && var2.isUp()) {
               Method var3 = var2.getClass().getMethod("getHardwareAddress", new Class[0]);
               if(var3 != null) {
                  var0 = (byte[])((byte[])var3.invoke(var2, new Object[0]));
               }
            }

            if(var0 == null) {
               Enumeration var24 = NetworkInterface.getNetworkInterfaces();

               while(var24.hasMoreElements() && var0 == null) {
                  var2 = (NetworkInterface)var24.nextElement();
                  if(var2 != null && var2.isUp() && !var2.isLoopback()) {
                     Method var4 = var2.getClass().getMethod("getHardwareAddress", new Class[0]);
                     if(var4 != null) {
                        var0 = (byte[])((byte[])var4.invoke(var2, new Object[0]));
                     }
                  }
               }
            }
         } catch (Exception var20) {
            var20.printStackTrace();
         }

         if(var0 == null || var0.length == 0) {
            var0 = var1.getAddress();
         }
      } catch (UnknownHostException var21) {
         ;
      }

      SecureRandom var22 = new SecureRandom();
      if(var0 == null || var0.length == 0) {
         var0 = new byte[6];
         var22.nextBytes(var0);
      }

      int var23 = var0.length >= 6?6:var0.length;
      int var25 = var0.length >= 6?var0.length - 6:0;
      byte[] var26 = new byte[8];
      var26[0] = -128;
      var26[1] = 0;

      for(int var5 = 2; var5 < 8; ++var5) {
         var26[var5] = 0;
      }

      System.arraycopy(var0, var25, var26, var25 + 2, var23);
      ByteBuffer var27 = ByteBuffer.wrap(var26);
      long var6 = uuidSequence;
      Runtime var8 = Runtime.getRuntime();
      synchronized(var8) {
         String var10 = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.assignedSequences");
         long[] var11;
         int var15;
         if(var10 == null) {
            var11 = new long[0];
         } else {
            String[] var12 = var10.split(",");
            var11 = new long[var12.length];
            int var13 = 0;
            String[] var14 = var12;
            var15 = var12.length;

            for(int var16 = 0; var16 < var15; ++var16) {
               String var17 = var14[var16];
               var11[var13] = Long.parseLong(var17);
               ++var13;
            }
         }

         if(var6 == 0L) {
            var6 = var22.nextLong();
         }

         var6 &= 16383L;

         while(true) {
            boolean var28 = false;
            long[] var29 = var11;
            int var30 = var11.length;

            for(var15 = 0; var15 < var30; ++var15) {
               long var31 = var29[var15];
               if(var31 == var6) {
                  var28 = true;
                  break;
               }
            }

            if(var28) {
               var6 = var6 + 1L & 16383L;
            }

            if(!var28) {
               var10 = var10 == null?Long.toString(var6):var10 + "," + Long.toString(var6);
               System.setProperty("org.apache.logging.log4j.assignedSequences", var10);
               break;
            }
         }
      }

      least = var27.getLong() | var6 << 48;
   }
}
