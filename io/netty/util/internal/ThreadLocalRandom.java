package io.netty.util.internal;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.Thread.UncaughtExceptionHandler;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadLocalRandom extends Random {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ThreadLocalRandom.class);
   private static final AtomicLong seedUniquifier = new AtomicLong();
   private static volatile long initialSeedUniquifier;
   private static final long multiplier = 25214903917L;
   private static final long addend = 11L;
   private static final long mask = 281474976710655L;
   private long rnd;
   boolean initialized = true;
   private long pad0;
   private long pad1;
   private long pad2;
   private long pad3;
   private long pad4;
   private long pad5;
   private long pad6;
   private long pad7;
   private static final long serialVersionUID = -5851777807851030925L;

   public static void setInitialSeedUniquifier(long var0) {
      initialSeedUniquifier = var0;
   }

   public static synchronized long getInitialSeedUniquifier() {
      long var0 = initialSeedUniquifier;
      if(var0 == 0L) {
         initialSeedUniquifier = var0 = SystemPropertyUtil.getLong("io.netty.initialSeedUniquifier", 0L);
      }

      if(var0 == 0L) {
         final LinkedBlockingQueue var2 = new LinkedBlockingQueue();
         Thread var3 = new Thread("initialSeedUniquifierGenerator") {
            public void run() {
               SecureRandom var1 = new SecureRandom();
               var2.add(var1.generateSeed(8));
            }
         };
         var3.setDaemon(true);
         var3.start();
         var3.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void uncaughtException(Thread var1, Throwable var2) {
               ThreadLocalRandom.logger.debug("An exception has been raised by {}", var1.getName(), var2);
            }
         });
         long var4 = 3L;
         long var6 = System.nanoTime() + TimeUnit.SECONDS.toNanos(3L);
         boolean var8 = false;

         while(true) {
            long var9 = var6 - System.nanoTime();
            if(var9 <= 0L) {
               var3.interrupt();
               logger.warn("Failed to generate a seed from SecureRandom within {} seconds. Not enough entrophy?", (Object)Long.valueOf(3L));
               break;
            }

            try {
               byte[] var11 = (byte[])var2.poll(var9, TimeUnit.NANOSECONDS);
               if(var11 != null) {
                  var0 = ((long)var11[0] & 255L) << 56 | ((long)var11[1] & 255L) << 48 | ((long)var11[2] & 255L) << 40 | ((long)var11[3] & 255L) << 32 | ((long)var11[4] & 255L) << 24 | ((long)var11[5] & 255L) << 16 | ((long)var11[6] & 255L) << 8 | (long)var11[7] & 255L;
                  break;
               }
            } catch (InterruptedException var12) {
               var8 = true;
               logger.warn("Failed to generate a seed from SecureRandom due to an InterruptedException.");
               break;
            }
         }

         var0 ^= 3627065505421648153L;
         var0 ^= Long.reverse(System.nanoTime());
         initialSeedUniquifier = var0;
         if(var8) {
            Thread.currentThread().interrupt();
            var3.interrupt();
         }
      }

      return var0;
   }

   private static long newSeed() {
      long var0 = System.nanoTime();

      long var2;
      long var4;
      long var6;
      do {
         var2 = seedUniquifier.get();
         var4 = var2 != 0L?var2:getInitialSeedUniquifier();
         var6 = var4 * 181783497276652981L;
      } while(!seedUniquifier.compareAndSet(var2, var6));

      if(var2 == 0L && logger.isDebugEnabled()) {
         logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x (took %d ms)", new Object[]{Long.valueOf(var4), Long.valueOf(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - var0))}));
      }

      return var6 ^ System.nanoTime();
   }

   ThreadLocalRandom() {
      super(newSeed());
   }

   public static ThreadLocalRandom current() {
      return InternalThreadLocalMap.get().random();
   }

   public void setSeed(long var1) {
      if(this.initialized) {
         throw new UnsupportedOperationException();
      } else {
         this.rnd = (var1 ^ 25214903917L) & 281474976710655L;
      }
   }

   protected int next(int var1) {
      this.rnd = this.rnd * 25214903917L + 11L & 281474976710655L;
      return (int)(this.rnd >>> 48 - var1);
   }

   public int nextInt(int var1, int var2) {
      if(var1 >= var2) {
         throw new IllegalArgumentException();
      } else {
         return this.nextInt(var2 - var1) + var1;
      }
   }

   public long nextLong(long var1) {
      if(var1 <= 0L) {
         throw new IllegalArgumentException("n must be positive");
      } else {
         long var3;
         long var8;
         for(var3 = 0L; var1 >= 2147483647L; var1 = var8) {
            int var5 = this.next(2);
            long var6 = var1 >>> 1;
            var8 = (var5 & 2) == 0?var6:var1 - var6;
            if((var5 & 1) == 0) {
               var3 += var1 - var8;
            }
         }

         return var3 + (long)this.nextInt((int)var1);
      }
   }

   public long nextLong(long var1, long var3) {
      if(var1 >= var3) {
         throw new IllegalArgumentException();
      } else {
         return this.nextLong(var3 - var1) + var1;
      }
   }

   public double nextDouble(double var1) {
      if(var1 <= 0.0D) {
         throw new IllegalArgumentException("n must be positive");
      } else {
         return this.nextDouble() * var1;
      }
   }

   public double nextDouble(double var1, double var3) {
      if(var1 >= var3) {
         throw new IllegalArgumentException();
      } else {
         return this.nextDouble() * (var3 - var1) + var1;
      }
   }
}
