package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.NoSuchElementException;

@GwtCompatible
@Beta
public abstract class DiscreteDomain<C extends Comparable> {
   public static DiscreteDomain<Integer> integers() {
      return DiscreteDomain.IntegerDomain.INSTANCE;
   }

   public static DiscreteDomain<Long> longs() {
      return DiscreteDomain.LongDomain.INSTANCE;
   }

   public static DiscreteDomain<BigInteger> bigIntegers() {
      return DiscreteDomain.BigIntegerDomain.INSTANCE;
   }

   protected DiscreteDomain() {
   }

   public abstract C next(C var1);

   public abstract C previous(C var1);

   public abstract long distance(C var1, C var2);

   public C minValue() {
      throw new NoSuchElementException();
   }

   public C maxValue() {
      throw new NoSuchElementException();
   }

   private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable {
      private static final DiscreteDomain.BigIntegerDomain INSTANCE = new DiscreteDomain.BigIntegerDomain();
      private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
      private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
      private static final long serialVersionUID = 0L;

      private BigIntegerDomain() {
      }

      public BigInteger next(BigInteger var1) {
         return var1.add(BigInteger.ONE);
      }

      public BigInteger previous(BigInteger var1) {
         return var1.subtract(BigInteger.ONE);
      }

      public long distance(BigInteger var1, BigInteger var2) {
         return var2.subtract(var1).max(MIN_LONG).min(MAX_LONG).longValue();
      }

      private Object readResolve() {
         return INSTANCE;
      }

      public String toString() {
         return "DiscreteDomain.bigIntegers()";
      }

      // $FF: synthetic method
      // $FF: bridge method
      public long distance(Comparable var1, Comparable var2) {
         return this.distance((BigInteger)var1, (BigInteger)var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Comparable previous(Comparable var1) {
         return this.previous((BigInteger)var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Comparable next(Comparable var1) {
         return this.next((BigInteger)var1);
      }
   }

   private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
      private static final DiscreteDomain.LongDomain INSTANCE = new DiscreteDomain.LongDomain();
      private static final long serialVersionUID = 0L;

      private LongDomain() {
      }

      public Long next(Long var1) {
         long var2 = var1.longValue();
         return var2 == Long.MAX_VALUE?null:Long.valueOf(var2 + 1L);
      }

      public Long previous(Long var1) {
         long var2 = var1.longValue();
         return var2 == Long.MIN_VALUE?null:Long.valueOf(var2 - 1L);
      }

      public long distance(Long var1, Long var2) {
         long var3 = var2.longValue() - var1.longValue();
         return var2.longValue() > var1.longValue() && var3 < 0L?Long.MAX_VALUE:(var2.longValue() < var1.longValue() && var3 > 0L?Long.MIN_VALUE:var3);
      }

      public Long minValue() {
         return Long.valueOf(Long.MIN_VALUE);
      }

      public Long maxValue() {
         return Long.valueOf(Long.MAX_VALUE);
      }

      private Object readResolve() {
         return INSTANCE;
      }

      public String toString() {
         return "DiscreteDomain.longs()";
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Comparable maxValue() {
         return this.maxValue();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Comparable minValue() {
         return this.minValue();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public long distance(Comparable var1, Comparable var2) {
         return this.distance((Long)var1, (Long)var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Comparable previous(Comparable var1) {
         return this.previous((Long)var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Comparable next(Comparable var1) {
         return this.next((Long)var1);
      }
   }

   private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
      private static final DiscreteDomain.IntegerDomain INSTANCE = new DiscreteDomain.IntegerDomain();
      private static final long serialVersionUID = 0L;

      private IntegerDomain() {
      }

      public Integer next(Integer var1) {
         int var2 = var1.intValue();
         return var2 == Integer.MAX_VALUE?null:Integer.valueOf(var2 + 1);
      }

      public Integer previous(Integer var1) {
         int var2 = var1.intValue();
         return var2 == Integer.MIN_VALUE?null:Integer.valueOf(var2 - 1);
      }

      public long distance(Integer var1, Integer var2) {
         return (long)var2.intValue() - (long)var1.intValue();
      }

      public Integer minValue() {
         return Integer.valueOf(Integer.MIN_VALUE);
      }

      public Integer maxValue() {
         return Integer.valueOf(Integer.MAX_VALUE);
      }

      private Object readResolve() {
         return INSTANCE;
      }

      public String toString() {
         return "DiscreteDomain.integers()";
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Comparable maxValue() {
         return this.maxValue();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Comparable minValue() {
         return this.minValue();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public long distance(Comparable var1, Comparable var2) {
         return this.distance((Integer)var1, (Integer)var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Comparable previous(Comparable var1) {
         return this.previous((Integer)var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Comparable next(Comparable var1) {
         return this.next((Integer)var1);
      }
   }
}
