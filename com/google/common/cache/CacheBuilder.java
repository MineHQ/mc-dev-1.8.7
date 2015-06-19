package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Ticker;
import com.google.common.cache.AbstractCache;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.LocalCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.cache.Weigher;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckReturnValue;

@GwtCompatible(
   emulated = true
)
public final class CacheBuilder<K, V> {
   private static final int DEFAULT_INITIAL_CAPACITY = 16;
   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
   private static final int DEFAULT_EXPIRATION_NANOS = 0;
   private static final int DEFAULT_REFRESH_NANOS = 0;
   static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter() {
      public void recordHits(int var1) {
      }

      public void recordMisses(int var1) {
      }

      public void recordLoadSuccess(long var1) {
      }

      public void recordLoadException(long var1) {
      }

      public void recordEviction() {
      }

      public CacheStats snapshot() {
         return CacheBuilder.EMPTY_STATS;
      }
   });
   static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
   static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier() {
      public AbstractCache.StatsCounter get() {
         return new AbstractCache.SimpleStatsCounter();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get() {
         return this.get();
      }
   };
   static final Ticker NULL_TICKER = new Ticker() {
      public long read() {
         return 0L;
      }
   };
   private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
   static final int UNSET_INT = -1;
   boolean strictParsing = true;
   int initialCapacity = -1;
   int concurrencyLevel = -1;
   long maximumSize = -1L;
   long maximumWeight = -1L;
   Weigher<? super K, ? super V> weigher;
   LocalCache.Strength keyStrength;
   LocalCache.Strength valueStrength;
   long expireAfterWriteNanos = -1L;
   long expireAfterAccessNanos = -1L;
   long refreshNanos = -1L;
   Equivalence<Object> keyEquivalence;
   Equivalence<Object> valueEquivalence;
   RemovalListener<? super K, ? super V> removalListener;
   Ticker ticker;
   Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier;

   CacheBuilder() {
      this.statsCounterSupplier = NULL_STATS_COUNTER;
   }

   public static CacheBuilder<Object, Object> newBuilder() {
      return new CacheBuilder();
   }

   @Beta
   @GwtIncompatible("To be supported")
   public static CacheBuilder<Object, Object> from(CacheBuilderSpec var0) {
      return var0.toCacheBuilder().lenientParsing();
   }

   @Beta
   @GwtIncompatible("To be supported")
   public static CacheBuilder<Object, Object> from(String var0) {
      return from(CacheBuilderSpec.parse(var0));
   }

   @GwtIncompatible("To be supported")
   CacheBuilder<K, V> lenientParsing() {
      this.strictParsing = false;
      return this;
   }

   @GwtIncompatible("To be supported")
   CacheBuilder<K, V> keyEquivalence(Equivalence<Object> var1) {
      Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", new Object[]{this.keyEquivalence});
      this.keyEquivalence = (Equivalence)Preconditions.checkNotNull(var1);
      return this;
   }

   Equivalence<Object> getKeyEquivalence() {
      return (Equivalence)Objects.firstNonNull(this.keyEquivalence, this.getKeyStrength().defaultEquivalence());
   }

   @GwtIncompatible("To be supported")
   CacheBuilder<K, V> valueEquivalence(Equivalence<Object> var1) {
      Preconditions.checkState(this.valueEquivalence == null, "value equivalence was already set to %s", new Object[]{this.valueEquivalence});
      this.valueEquivalence = (Equivalence)Preconditions.checkNotNull(var1);
      return this;
   }

   Equivalence<Object> getValueEquivalence() {
      return (Equivalence)Objects.firstNonNull(this.valueEquivalence, this.getValueStrength().defaultEquivalence());
   }

   public CacheBuilder<K, V> initialCapacity(int var1) {
      Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", new Object[]{Integer.valueOf(this.initialCapacity)});
      Preconditions.checkArgument(var1 >= 0);
      this.initialCapacity = var1;
      return this;
   }

   int getInitialCapacity() {
      return this.initialCapacity == -1?16:this.initialCapacity;
   }

   public CacheBuilder<K, V> concurrencyLevel(int var1) {
      Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", new Object[]{Integer.valueOf(this.concurrencyLevel)});
      Preconditions.checkArgument(var1 > 0);
      this.concurrencyLevel = var1;
      return this;
   }

   int getConcurrencyLevel() {
      return this.concurrencyLevel == -1?4:this.concurrencyLevel;
   }

   public CacheBuilder<K, V> maximumSize(long var1) {
      Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", new Object[]{Long.valueOf(this.maximumSize)});
      Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", new Object[]{Long.valueOf(this.maximumWeight)});
      Preconditions.checkState(this.weigher == null, "maximum size can not be combined with weigher");
      Preconditions.checkArgument(var1 >= 0L, "maximum size must not be negative");
      this.maximumSize = var1;
      return this;
   }

   @GwtIncompatible("To be supported")
   public CacheBuilder<K, V> maximumWeight(long var1) {
      Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", new Object[]{Long.valueOf(this.maximumWeight)});
      Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", new Object[]{Long.valueOf(this.maximumSize)});
      this.maximumWeight = var1;
      Preconditions.checkArgument(var1 >= 0L, "maximum weight must not be negative");
      return this;
   }

   @GwtIncompatible("To be supported")
   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> var1) {
      Preconditions.checkState(this.weigher == null);
      if(this.strictParsing) {
         Preconditions.checkState(this.maximumSize == -1L, "weigher can not be combined with maximum size", new Object[]{Long.valueOf(this.maximumSize)});
      }

      this.weigher = (Weigher)Preconditions.checkNotNull(var1);
      return this;
   }

   long getMaximumWeight() {
      return this.expireAfterWriteNanos != 0L && this.expireAfterAccessNanos != 0L?(this.weigher == null?this.maximumSize:this.maximumWeight):0L;
   }

   <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
      return (Weigher)Objects.firstNonNull(this.weigher, CacheBuilder.OneWeigher.INSTANCE);
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   public CacheBuilder<K, V> weakKeys() {
      return this.setKeyStrength(LocalCache.Strength.WEAK);
   }

   CacheBuilder<K, V> setKeyStrength(LocalCache.Strength var1) {
      Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", new Object[]{this.keyStrength});
      this.keyStrength = (LocalCache.Strength)Preconditions.checkNotNull(var1);
      return this;
   }

   LocalCache.Strength getKeyStrength() {
      return (LocalCache.Strength)Objects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   public CacheBuilder<K, V> weakValues() {
      return this.setValueStrength(LocalCache.Strength.WEAK);
   }

   @GwtIncompatible("java.lang.ref.SoftReference")
   public CacheBuilder<K, V> softValues() {
      return this.setValueStrength(LocalCache.Strength.SOFT);
   }

   CacheBuilder<K, V> setValueStrength(LocalCache.Strength var1) {
      Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", new Object[]{this.valueStrength});
      this.valueStrength = (LocalCache.Strength)Preconditions.checkNotNull(var1);
      return this;
   }

   LocalCache.Strength getValueStrength() {
      return (LocalCache.Strength)Objects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
   }

   public CacheBuilder<K, V> expireAfterWrite(long var1, TimeUnit var3) {
      Preconditions.checkState(this.expireAfterWriteNanos == -1L, "expireAfterWrite was already set to %s ns", new Object[]{Long.valueOf(this.expireAfterWriteNanos)});
      Preconditions.checkArgument(var1 >= 0L, "duration cannot be negative: %s %s", new Object[]{Long.valueOf(var1), var3});
      this.expireAfterWriteNanos = var3.toNanos(var1);
      return this;
   }

   long getExpireAfterWriteNanos() {
      return this.expireAfterWriteNanos == -1L?0L:this.expireAfterWriteNanos;
   }

   public CacheBuilder<K, V> expireAfterAccess(long var1, TimeUnit var3) {
      Preconditions.checkState(this.expireAfterAccessNanos == -1L, "expireAfterAccess was already set to %s ns", new Object[]{Long.valueOf(this.expireAfterAccessNanos)});
      Preconditions.checkArgument(var1 >= 0L, "duration cannot be negative: %s %s", new Object[]{Long.valueOf(var1), var3});
      this.expireAfterAccessNanos = var3.toNanos(var1);
      return this;
   }

   long getExpireAfterAccessNanos() {
      return this.expireAfterAccessNanos == -1L?0L:this.expireAfterAccessNanos;
   }

   @Beta
   @GwtIncompatible("To be supported (synchronously).")
   public CacheBuilder<K, V> refreshAfterWrite(long var1, TimeUnit var3) {
      Preconditions.checkNotNull(var3);
      Preconditions.checkState(this.refreshNanos == -1L, "refresh was already set to %s ns", new Object[]{Long.valueOf(this.refreshNanos)});
      Preconditions.checkArgument(var1 > 0L, "duration must be positive: %s %s", new Object[]{Long.valueOf(var1), var3});
      this.refreshNanos = var3.toNanos(var1);
      return this;
   }

   long getRefreshNanos() {
      return this.refreshNanos == -1L?0L:this.refreshNanos;
   }

   public CacheBuilder<K, V> ticker(Ticker var1) {
      Preconditions.checkState(this.ticker == null);
      this.ticker = (Ticker)Preconditions.checkNotNull(var1);
      return this;
   }

   Ticker getTicker(boolean var1) {
      return this.ticker != null?this.ticker:(var1?Ticker.systemTicker():NULL_TICKER);
   }

   @CheckReturnValue
   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> var1) {
      Preconditions.checkState(this.removalListener == null);
      this.removalListener = (RemovalListener)Preconditions.checkNotNull(var1);
      return this;
   }

   <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
      return (RemovalListener)Objects.firstNonNull(this.removalListener, CacheBuilder.NullListener.INSTANCE);
   }

   public CacheBuilder<K, V> recordStats() {
      this.statsCounterSupplier = CACHE_STATS_COUNTER;
      return this;
   }

   boolean isRecordingStats() {
      return this.statsCounterSupplier == CACHE_STATS_COUNTER;
   }

   Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
      return this.statsCounterSupplier;
   }

   public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> var1) {
      this.checkWeightWithWeigher();
      return new LocalCache.LocalLoadingCache(this, var1);
   }

   public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
      this.checkWeightWithWeigher();
      this.checkNonLoadingCache();
      return new LocalCache.LocalManualCache(this);
   }

   private void checkNonLoadingCache() {
      Preconditions.checkState(this.refreshNanos == -1L, "refreshAfterWrite requires a LoadingCache");
   }

   private void checkWeightWithWeigher() {
      if(this.weigher == null) {
         Preconditions.checkState(this.maximumWeight == -1L, "maximumWeight requires weigher");
      } else if(this.strictParsing) {
         Preconditions.checkState(this.maximumWeight != -1L, "weigher requires maximumWeight");
      } else if(this.maximumWeight == -1L) {
         logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
      }

   }

   public String toString() {
      Objects.ToStringHelper var1 = Objects.toStringHelper((Object)this);
      if(this.initialCapacity != -1) {
         var1.add("initialCapacity", this.initialCapacity);
      }

      if(this.concurrencyLevel != -1) {
         var1.add("concurrencyLevel", this.concurrencyLevel);
      }

      if(this.maximumSize != -1L) {
         var1.add("maximumSize", this.maximumSize);
      }

      if(this.maximumWeight != -1L) {
         var1.add("maximumWeight", this.maximumWeight);
      }

      if(this.expireAfterWriteNanos != -1L) {
         var1.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
      }

      if(this.expireAfterAccessNanos != -1L) {
         var1.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
      }

      if(this.keyStrength != null) {
         var1.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
      }

      if(this.valueStrength != null) {
         var1.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
      }

      if(this.keyEquivalence != null) {
         var1.addValue("keyEquivalence");
      }

      if(this.valueEquivalence != null) {
         var1.addValue("valueEquivalence");
      }

      if(this.removalListener != null) {
         var1.addValue("removalListener");
      }

      return var1.toString();
   }

   static enum OneWeigher implements Weigher<Object, Object> {
      INSTANCE;

      private OneWeigher() {
      }

      public int weigh(Object var1, Object var2) {
         return 1;
      }
   }

   static enum NullListener implements RemovalListener<Object, Object> {
      INSTANCE;

      private NullListener() {
      }

      public void onRemoval(RemovalNotification<Object, Object> var1) {
      }
   }
}
