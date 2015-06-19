package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Interner;
import com.google.common.collect.MapMaker;
import com.google.common.collect.MapMakerInternalMap;
import java.util.concurrent.ConcurrentMap;

@Beta
public final class Interners {
   private Interners() {
   }

   public static <E> Interner<E> newStrongInterner() {
      final ConcurrentMap var0 = (new MapMaker()).makeMap();
      return new Interner() {
         public E intern(E var1) {
            Object var2 = var0.putIfAbsent(Preconditions.checkNotNull(var1), var1);
            return var2 == null?var1:var2;
         }
      };
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   public static <E> Interner<E> newWeakInterner() {
      return new Interners.WeakInterner(null);
   }

   public static <E> Function<E, E> asFunction(Interner<E> var0) {
      return new Interners.InternerFunction((Interner)Preconditions.checkNotNull(var0));
   }

   private static class InternerFunction<E> implements Function<E, E> {
      private final Interner<E> interner;

      public InternerFunction(Interner<E> var1) {
         this.interner = var1;
      }

      public E apply(E var1) {
         return this.interner.intern(var1);
      }

      public int hashCode() {
         return this.interner.hashCode();
      }

      public boolean equals(Object var1) {
         if(var1 instanceof Interners.InternerFunction) {
            Interners.InternerFunction var2 = (Interners.InternerFunction)var1;
            return this.interner.equals(var2.interner);
         } else {
            return false;
         }
      }
   }

   private static class WeakInterner<E> implements Interner<E> {
      private final MapMakerInternalMap<E, Interners.WeakInterner.WeakInterner$Dummy> map;

      private WeakInterner() {
         this.map = (new MapMaker()).weakKeys().keyEquivalence(Equivalence.equals()).makeCustomMap();
      }

      public E intern(E var1) {
         Interners.WeakInterner.WeakInterner$Dummy var4;
         do {
            MapMakerInternalMap.ReferenceEntry var2 = this.map.getEntry(var1);
            if(var2 != null) {
               Object var3 = var2.getKey();
               if(var3 != null) {
                  return var3;
               }
            }

            var4 = (Interners.WeakInterner.WeakInterner$Dummy)this.map.putIfAbsent(var1, Interners.WeakInterner.WeakInterner$Dummy.VALUE);
         } while(var4 != null);

         return var1;
      }

      // $FF: synthetic method
      WeakInterner(Object var1) {
         this();
      }

      private static enum WeakInterner$Dummy {
         VALUE;

         private WeakInterner$Dummy() {
         }
      }
   }
}
