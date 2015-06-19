package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.MapMaker;
import com.google.common.collect.MapMakerInternalMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/** @deprecated */
@Deprecated
@Beta
@GwtCompatible(
   emulated = true
)
abstract class GenericMapMaker<K0, V0> {
   @GwtIncompatible("To be supported")
   MapMaker.RemovalListener<K0, V0> removalListener;

   GenericMapMaker() {
   }

   @GwtIncompatible("To be supported")
   abstract GenericMapMaker<K0, V0> keyEquivalence(Equivalence<Object> var1);

   public abstract GenericMapMaker<K0, V0> initialCapacity(int var1);

   abstract GenericMapMaker<K0, V0> maximumSize(int var1);

   public abstract GenericMapMaker<K0, V0> concurrencyLevel(int var1);

   @GwtIncompatible("java.lang.ref.WeakReference")
   public abstract GenericMapMaker<K0, V0> weakKeys();

   @GwtIncompatible("java.lang.ref.WeakReference")
   public abstract GenericMapMaker<K0, V0> weakValues();

   /** @deprecated */
   @Deprecated
   @GwtIncompatible("java.lang.ref.SoftReference")
   public abstract GenericMapMaker<K0, V0> softValues();

   abstract GenericMapMaker<K0, V0> expireAfterWrite(long var1, TimeUnit var3);

   @GwtIncompatible("To be supported")
   abstract GenericMapMaker<K0, V0> expireAfterAccess(long var1, TimeUnit var3);

   @GwtIncompatible("To be supported")
   <K extends K0, V extends V0> MapMaker.RemovalListener<K, V> getRemovalListener() {
      return (MapMaker.RemovalListener)Objects.firstNonNull(this.removalListener, GenericMapMaker.NullListener.INSTANCE);
   }

   public abstract <K extends K0, V extends V0> ConcurrentMap<K, V> makeMap();

   @GwtIncompatible("MapMakerInternalMap")
   abstract <K, V> MapMakerInternalMap<K, V> makeCustomMap();

   /** @deprecated */
   @Deprecated
   abstract <K extends K0, V extends V0> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> var1);

   @GwtIncompatible("To be supported")
   static enum NullListener implements MapMaker.RemovalListener<Object, Object> {
      INSTANCE;

      private NullListener() {
      }

      public void onRemoval(MapMaker.RemovalNotification<Object, Object> var1) {
      }
   }
}
