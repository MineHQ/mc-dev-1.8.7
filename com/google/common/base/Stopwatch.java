package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import java.util.concurrent.TimeUnit;

@Beta
@GwtCompatible(
   emulated = true
)
public final class Stopwatch {
   private final Ticker ticker;
   private boolean isRunning;
   private long elapsedNanos;
   private long startTick;

   public static Stopwatch createUnstarted() {
      return new Stopwatch();
   }

   public static Stopwatch createUnstarted(Ticker var0) {
      return new Stopwatch(var0);
   }

   public static Stopwatch createStarted() {
      return (new Stopwatch()).start();
   }

   public static Stopwatch createStarted(Ticker var0) {
      return (new Stopwatch(var0)).start();
   }

   /** @deprecated */
   @Deprecated
   Stopwatch() {
      this(Ticker.systemTicker());
   }

   /** @deprecated */
   @Deprecated
   Stopwatch(Ticker var1) {
      this.ticker = (Ticker)Preconditions.checkNotNull(var1, "ticker");
   }

   public boolean isRunning() {
      return this.isRunning;
   }

   public Stopwatch start() {
      Preconditions.checkState(!this.isRunning, "This stopwatch is already running.");
      this.isRunning = true;
      this.startTick = this.ticker.read();
      return this;
   }

   public Stopwatch stop() {
      long var1 = this.ticker.read();
      Preconditions.checkState(this.isRunning, "This stopwatch is already stopped.");
      this.isRunning = false;
      this.elapsedNanos += var1 - this.startTick;
      return this;
   }

   public Stopwatch reset() {
      this.elapsedNanos = 0L;
      this.isRunning = false;
      return this;
   }

   private long elapsedNanos() {
      return this.isRunning?this.ticker.read() - this.startTick + this.elapsedNanos:this.elapsedNanos;
   }

   public long elapsed(TimeUnit var1) {
      return var1.convert(this.elapsedNanos(), TimeUnit.NANOSECONDS);
   }

   @GwtIncompatible("String.format()")
   public String toString() {
      long var1 = this.elapsedNanos();
      TimeUnit var3 = chooseUnit(var1);
      double var4 = (double)var1 / (double)TimeUnit.NANOSECONDS.convert(1L, var3);
      return String.format("%.4g %s", new Object[]{Double.valueOf(var4), abbreviate(var3)});
   }

   private static TimeUnit chooseUnit(long var0) {
      return TimeUnit.DAYS.convert(var0, TimeUnit.NANOSECONDS) > 0L?TimeUnit.DAYS:(TimeUnit.HOURS.convert(var0, TimeUnit.NANOSECONDS) > 0L?TimeUnit.HOURS:(TimeUnit.MINUTES.convert(var0, TimeUnit.NANOSECONDS) > 0L?TimeUnit.MINUTES:(TimeUnit.SECONDS.convert(var0, TimeUnit.NANOSECONDS) > 0L?TimeUnit.SECONDS:(TimeUnit.MILLISECONDS.convert(var0, TimeUnit.NANOSECONDS) > 0L?TimeUnit.MILLISECONDS:(TimeUnit.MICROSECONDS.convert(var0, TimeUnit.NANOSECONDS) > 0L?TimeUnit.MICROSECONDS:TimeUnit.NANOSECONDS)))));
   }

   private static String abbreviate(TimeUnit var0) {
      switch(Stopwatch.SyntheticClass_1.$SwitchMap$java$util$concurrent$TimeUnit[var0.ordinal()]) {
      case 1:
         return "ns";
      case 2:
         return "\u03bcs";
      case 3:
         return "ms";
      case 4:
         return "s";
      case 5:
         return "min";
      case 6:
         return "h";
      case 7:
         return "d";
      default:
         throw new AssertionError();
      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$java$util$concurrent$TimeUnit = new int[TimeUnit.values().length];

      static {
         try {
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.NANOSECONDS.ordinal()] = 1;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MICROSECONDS.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MILLISECONDS.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.SECONDS.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MINUTES.ordinal()] = 5;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.HOURS.ordinal()] = 6;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.DAYS.ordinal()] = 7;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
