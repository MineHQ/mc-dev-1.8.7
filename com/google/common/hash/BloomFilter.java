package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.hash.BloomFilterStrategies;
import com.google.common.hash.Funnel;
import java.io.Serializable;
import javax.annotation.Nullable;

@Beta
public final class BloomFilter<T> implements Predicate<T>, Serializable {
   private final BloomFilterStrategies.BitArray bits;
   private final int numHashFunctions;
   private final Funnel<T> funnel;
   private final BloomFilter.Strategy strategy;
   private static final BloomFilter.Strategy DEFAULT_STRATEGY = getDefaultStrategyFromSystemProperty();
   @VisibleForTesting
   static final String USE_MITZ32_PROPERTY = "com.google.common.hash.BloomFilter.useMitz32";

   private BloomFilter(BloomFilterStrategies.BitArray var1, int var2, Funnel<T> var3, BloomFilter.Strategy var4) {
      Preconditions.checkArgument(var2 > 0, "numHashFunctions (%s) must be > 0", new Object[]{Integer.valueOf(var2)});
      Preconditions.checkArgument(var2 <= 255, "numHashFunctions (%s) must be <= 255", new Object[]{Integer.valueOf(var2)});
      this.bits = (BloomFilterStrategies.BitArray)Preconditions.checkNotNull(var1);
      this.numHashFunctions = var2;
      this.funnel = (Funnel)Preconditions.checkNotNull(var3);
      this.strategy = (BloomFilter.Strategy)Preconditions.checkNotNull(var4);
   }

   public BloomFilter<T> copy() {
      return new BloomFilter(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
   }

   public boolean mightContain(T var1) {
      return this.strategy.mightContain(var1, this.funnel, this.numHashFunctions, this.bits);
   }

   /** @deprecated */
   @Deprecated
   public boolean apply(T var1) {
      return this.mightContain(var1);
   }

   public boolean put(T var1) {
      return this.strategy.put(var1, this.funnel, this.numHashFunctions, this.bits);
   }

   public double expectedFpp() {
      return Math.pow((double)this.bits.bitCount() / (double)this.bitSize(), (double)this.numHashFunctions);
   }

   @VisibleForTesting
   long bitSize() {
      return this.bits.bitSize();
   }

   public boolean isCompatible(BloomFilter<T> var1) {
      Preconditions.checkNotNull(var1);
      return this != var1 && this.numHashFunctions == var1.numHashFunctions && this.bitSize() == var1.bitSize() && this.strategy.equals(var1.strategy) && this.funnel.equals(var1.funnel);
   }

   public void putAll(BloomFilter<T> var1) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkArgument(this != var1, "Cannot combine a BloomFilter with itself.");
      Preconditions.checkArgument(this.numHashFunctions == var1.numHashFunctions, "BloomFilters must have the same number of hash functions (%s != %s)", new Object[]{Integer.valueOf(this.numHashFunctions), Integer.valueOf(var1.numHashFunctions)});
      Preconditions.checkArgument(this.bitSize() == var1.bitSize(), "BloomFilters must have the same size underlying bit arrays (%s != %s)", new Object[]{Long.valueOf(this.bitSize()), Long.valueOf(var1.bitSize())});
      Preconditions.checkArgument(this.strategy.equals(var1.strategy), "BloomFilters must have equal strategies (%s != %s)", new Object[]{this.strategy, var1.strategy});
      Preconditions.checkArgument(this.funnel.equals(var1.funnel), "BloomFilters must have equal funnels (%s != %s)", new Object[]{this.funnel, var1.funnel});
      this.bits.putAll(var1.bits);
   }

   public boolean equals(@Nullable Object var1) {
      if(var1 == this) {
         return true;
      } else if(!(var1 instanceof BloomFilter)) {
         return false;
      } else {
         BloomFilter var2 = (BloomFilter)var1;
         return this.numHashFunctions == var2.numHashFunctions && this.funnel.equals(var2.funnel) && this.bits.equals(var2.bits) && this.strategy.equals(var2.strategy);
      }
   }

   public int hashCode() {
      return Objects.hashCode(new Object[]{Integer.valueOf(this.numHashFunctions), this.funnel, this.strategy, this.bits});
   }

   @VisibleForTesting
   static BloomFilter.Strategy getDefaultStrategyFromSystemProperty() {
      return Boolean.parseBoolean(System.getProperty("com.google.common.hash.BloomFilter.useMitz32"))?BloomFilterStrategies.MURMUR128_MITZ_32:BloomFilterStrategies.MURMUR128_MITZ_64;
   }

   public static <T> BloomFilter<T> create(Funnel<T> var0, int var1, double var2) {
      return create(var0, var1, var2, DEFAULT_STRATEGY);
   }

   @VisibleForTesting
   static <T> BloomFilter<T> create(Funnel<T> var0, int var1, double var2, BloomFilter.Strategy var4) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 >= 0, "Expected insertions (%s) must be >= 0", new Object[]{Integer.valueOf(var1)});
      Preconditions.checkArgument(var2 > 0.0D, "False positive probability (%s) must be > 0.0", new Object[]{Double.valueOf(var2)});
      Preconditions.checkArgument(var2 < 1.0D, "False positive probability (%s) must be < 1.0", new Object[]{Double.valueOf(var2)});
      Preconditions.checkNotNull(var4);
      if(var1 == 0) {
         var1 = 1;
      }

      long var5 = optimalNumOfBits((long)var1, var2);
      int var7 = optimalNumOfHashFunctions((long)var1, var5);

      try {
         return new BloomFilter(new BloomFilterStrategies.BitArray(var5), var7, var0, var4);
      } catch (IllegalArgumentException var9) {
         throw new IllegalArgumentException("Could not create BloomFilter of " + var5 + " bits", var9);
      }
   }

   public static <T> BloomFilter<T> create(Funnel<T> var0, int var1) {
      return create(var0, var1, 0.03D);
   }

   @VisibleForTesting
   static int optimalNumOfHashFunctions(long var0, long var2) {
      return Math.max(1, (int)Math.round((double)(var2 / var0) * Math.log(2.0D)));
   }

   @VisibleForTesting
   static long optimalNumOfBits(long var0, double var2) {
      if(var2 == 0.0D) {
         var2 = Double.MIN_VALUE;
      }

      return (long)((double)(-var0) * Math.log(var2) / (Math.log(2.0D) * Math.log(2.0D)));
   }

   private Object writeReplace() {
      return new BloomFilter.SerialForm(this);
   }

   // $FF: synthetic method
   BloomFilter(BloomFilterStrategies.BitArray var1, int var2, Funnel var3, BloomFilter.Strategy var4, BloomFilter.SyntheticClass_1 var5) {
      this(var1, var2, var3, var4);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class SerialForm<T> implements Serializable {
      final long[] data;
      final int numHashFunctions;
      final Funnel<T> funnel;
      final BloomFilter.Strategy strategy;
      private static final long serialVersionUID = 1L;

      SerialForm(BloomFilter<T> var1) {
         this.data = var1.bits.data;
         this.numHashFunctions = var1.numHashFunctions;
         this.funnel = var1.funnel;
         this.strategy = var1.strategy;
      }

      Object readResolve() {
         return new BloomFilter(new BloomFilterStrategies.BitArray(this.data), this.numHashFunctions, this.funnel, this.strategy);
      }
   }

   interface Strategy extends Serializable {
      <T> boolean put(T var1, Funnel<? super T> var2, int var3, BloomFilterStrategies.BitArray var4);

      <T> boolean mightContain(T var1, Funnel<? super T> var2, int var3, BloomFilterStrategies.BitArray var4);

      int ordinal();
   }
}
