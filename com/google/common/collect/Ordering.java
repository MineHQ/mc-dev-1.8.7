package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.AllEqualOrdering;
import com.google.common.collect.ByFunctionOrdering;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ComparatorOrdering;
import com.google.common.collect.CompoundOrdering;
import com.google.common.collect.ExplicitOrdering;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.LexicographicalOrdering;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.NaturalOrdering;
import com.google.common.collect.NullsFirstOrdering;
import com.google.common.collect.NullsLastOrdering;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Platform;
import com.google.common.collect.ReverseOrdering;
import com.google.common.collect.UsingToStringOrdering;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class Ordering<T> implements Comparator<T> {
   static final int LEFT_IS_GREATER = 1;
   static final int RIGHT_IS_GREATER = -1;

   @GwtCompatible(
      serializable = true
   )
   public static <C extends Comparable> Ordering<C> natural() {
      return NaturalOrdering.INSTANCE;
   }

   @GwtCompatible(
      serializable = true
   )
   public static <T> Ordering<T> from(Comparator<T> var0) {
      return (Ordering)(var0 instanceof Ordering?(Ordering)var0:new ComparatorOrdering(var0));
   }

   /** @deprecated */
   @Deprecated
   @GwtCompatible(
      serializable = true
   )
   public static <T> Ordering<T> from(Ordering<T> var0) {
      return (Ordering)Preconditions.checkNotNull(var0);
   }

   @GwtCompatible(
      serializable = true
   )
   public static <T> Ordering<T> explicit(List<T> var0) {
      return new ExplicitOrdering(var0);
   }

   @GwtCompatible(
      serializable = true
   )
   public static <T> Ordering<T> explicit(T var0, T... var1) {
      return explicit(Lists.asList(var0, var1));
   }

   @GwtCompatible(
      serializable = true
   )
   public static Ordering<Object> allEqual() {
      return AllEqualOrdering.INSTANCE;
   }

   @GwtCompatible(
      serializable = true
   )
   public static Ordering<Object> usingToString() {
      return UsingToStringOrdering.INSTANCE;
   }

   public static Ordering<Object> arbitrary() {
      return Ordering.ArbitraryOrderingHolder.ARBITRARY_ORDERING;
   }

   protected Ordering() {
   }

   @GwtCompatible(
      serializable = true
   )
   public <S extends T> Ordering<S> reverse() {
      return new ReverseOrdering(this);
   }

   @GwtCompatible(
      serializable = true
   )
   public <S extends T> Ordering<S> nullsFirst() {
      return new NullsFirstOrdering(this);
   }

   @GwtCompatible(
      serializable = true
   )
   public <S extends T> Ordering<S> nullsLast() {
      return new NullsLastOrdering(this);
   }

   @GwtCompatible(
      serializable = true
   )
   public <F> Ordering<F> onResultOf(Function<F, ? extends T> var1) {
      return new ByFunctionOrdering(var1, this);
   }

   <T2 extends T> Ordering<Entry<T2, ?>> onKeys() {
      return this.onResultOf(Maps.keyFunction());
   }

   @GwtCompatible(
      serializable = true
   )
   public <U extends T> Ordering<U> compound(Comparator<? super U> var1) {
      return new CompoundOrdering(this, (Comparator)Preconditions.checkNotNull(var1));
   }

   @GwtCompatible(
      serializable = true
   )
   public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> var0) {
      return new CompoundOrdering(var0);
   }

   @GwtCompatible(
      serializable = true
   )
   public <S extends T> Ordering<Iterable<S>> lexicographical() {
      return new LexicographicalOrdering(this);
   }

   public abstract int compare(@Nullable T var1, @Nullable T var2);

   public <E extends T> E min(Iterator<E> var1) {
      Object var2;
      for(var2 = var1.next(); var1.hasNext(); var2 = this.min(var2, var1.next())) {
         ;
      }

      return var2;
   }

   public <E extends T> E min(Iterable<E> var1) {
      return this.min(var1.iterator());
   }

   public <E extends T> E min(@Nullable E var1, @Nullable E var2) {
      return this.compare(var1, var2) <= 0?var1:var2;
   }

   public <E extends T> E min(@Nullable E var1, @Nullable E var2, @Nullable E var3, E... var4) {
      Object var5 = this.min(this.min(var1, var2), var3);
      Object[] var6 = var4;
      int var7 = var4.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Object var9 = var6[var8];
         var5 = this.min(var5, var9);
      }

      return var5;
   }

   public <E extends T> E max(Iterator<E> var1) {
      Object var2;
      for(var2 = var1.next(); var1.hasNext(); var2 = this.max(var2, var1.next())) {
         ;
      }

      return var2;
   }

   public <E extends T> E max(Iterable<E> var1) {
      return this.max(var1.iterator());
   }

   public <E extends T> E max(@Nullable E var1, @Nullable E var2) {
      return this.compare(var1, var2) >= 0?var1:var2;
   }

   public <E extends T> E max(@Nullable E var1, @Nullable E var2, @Nullable E var3, E... var4) {
      Object var5 = this.max(this.max(var1, var2), var3);
      Object[] var6 = var4;
      int var7 = var4.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Object var9 = var6[var8];
         var5 = this.max(var5, var9);
      }

      return var5;
   }

   public <E extends T> List<E> leastOf(Iterable<E> var1, int var2) {
      if(var1 instanceof Collection) {
         Collection var3 = (Collection)var1;
         if((long)var3.size() <= 2L * (long)var2) {
            Object[] var4 = (Object[])var3.toArray();
            Arrays.sort(var4, this);
            if(var4.length > var2) {
               var4 = ObjectArrays.arraysCopyOf(var4, var2);
            }

            return Collections.unmodifiableList(Arrays.asList(var4));
         }
      }

      return this.leastOf(var1.iterator(), var2);
   }

   public <E extends T> List<E> leastOf(Iterator<E> var1, int var2) {
      Preconditions.checkNotNull(var1);
      CollectPreconditions.checkNonnegative(var2, "k");
      if(var2 != 0 && var1.hasNext()) {
         if(var2 >= 1073741823) {
            ArrayList var13 = Lists.newArrayList(var1);
            Collections.sort(var13, this);
            if(var13.size() > var2) {
               var13.subList(var2, var13.size()).clear();
            }

            var13.trimToSize();
            return Collections.unmodifiableList(var13);
         } else {
            int var3 = var2 * 2;
            Object[] var4 = (Object[])(new Object[var3]);
            Object var5 = var1.next();
            var4[0] = var5;

            int var6;
            Object var7;
            for(var6 = 1; var6 < var2 && var1.hasNext(); var5 = this.max(var5, var7)) {
               var7 = var1.next();
               var4[var6++] = var7;
            }

            while(true) {
               do {
                  do {
                     if(!var1.hasNext()) {
                        Arrays.sort(var4, 0, var6, this);
                        var6 = Math.min(var6, var2);
                        return Collections.unmodifiableList(Arrays.asList(ObjectArrays.arraysCopyOf(var4, var6)));
                     }

                     var7 = var1.next();
                  } while(this.compare(var7, var5) >= 0);

                  var4[var6++] = var7;
               } while(var6 != var3);

               int var8 = 0;
               int var9 = var3 - 1;
               int var10 = 0;

               int var11;
               while(var8 < var9) {
                  var11 = var8 + var9 + 1 >>> 1;
                  int var12 = this.partition(var4, var8, var9, var11);
                  if(var12 > var2) {
                     var9 = var12 - 1;
                  } else {
                     if(var12 >= var2) {
                        break;
                     }

                     var8 = Math.max(var12, var8 + 1);
                     var10 = var12;
                  }
               }

               var6 = var2;
               var5 = var4[var10];

               for(var11 = var10 + 1; var11 < var6; ++var11) {
                  var5 = this.max(var5, var4[var11]);
               }
            }
         }
      } else {
         return ImmutableList.of();
      }
   }

   private <E extends T> int partition(E[] var1, int var2, int var3, int var4) {
      Object var5 = var1[var4];
      var1[var4] = var1[var3];
      var1[var3] = var5;
      int var6 = var2;

      for(int var7 = var2; var7 < var3; ++var7) {
         if(this.compare(var1[var7], var5) < 0) {
            ObjectArrays.swap(var1, var6, var7);
            ++var6;
         }
      }

      ObjectArrays.swap(var1, var3, var6);
      return var6;
   }

   public <E extends T> List<E> greatestOf(Iterable<E> var1, int var2) {
      return this.reverse().leastOf(var1, var2);
   }

   public <E extends T> List<E> greatestOf(Iterator<E> var1, int var2) {
      return this.reverse().leastOf(var1, var2);
   }

   public <E extends T> List<E> sortedCopy(Iterable<E> var1) {
      Object[] var2 = (Object[])Iterables.toArray(var1);
      Arrays.sort(var2, this);
      return Lists.newArrayList((Iterable)Arrays.asList(var2));
   }

   public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> var1) {
      Object[] var2 = (Object[])Iterables.toArray(var1);
      Object[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         Preconditions.checkNotNull(var6);
      }

      Arrays.sort(var2, this);
      return ImmutableList.asImmutableList(var2);
   }

   public boolean isOrdered(Iterable<? extends T> var1) {
      Iterator var2 = var1.iterator();
      Object var4;
      if(var2.hasNext()) {
         for(Object var3 = var2.next(); var2.hasNext(); var3 = var4) {
            var4 = var2.next();
            if(this.compare(var3, var4) > 0) {
               return false;
            }
         }
      }

      return true;
   }

   public boolean isStrictlyOrdered(Iterable<? extends T> var1) {
      Iterator var2 = var1.iterator();
      Object var4;
      if(var2.hasNext()) {
         for(Object var3 = var2.next(); var2.hasNext(); var3 = var4) {
            var4 = var2.next();
            if(this.compare(var3, var4) >= 0) {
               return false;
            }
         }
      }

      return true;
   }

   public int binarySearch(List<? extends T> var1, @Nullable T var2) {
      return Collections.binarySearch(var1, var2, this);
   }

   @VisibleForTesting
   static class IncomparableValueException extends ClassCastException {
      final Object value;
      private static final long serialVersionUID = 0L;

      IncomparableValueException(Object var1) {
         super("Cannot compare value: " + var1);
         this.value = var1;
      }
   }

   @VisibleForTesting
   static class ArbitraryOrdering extends Ordering<Object> {
      private Map<Object, Integer> uids = Platform.tryWeakKeys(new MapMaker()).makeComputingMap(new Function() {
         final AtomicInteger counter = new AtomicInteger(0);

         public Integer apply(Object var1) {
            return Integer.valueOf(this.counter.getAndIncrement());
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object apply(Object var1) {
            return this.apply(var1);
         }
      });

      ArbitraryOrdering() {
      }

      public int compare(Object var1, Object var2) {
         if(var1 == var2) {
            return 0;
         } else if(var1 == null) {
            return -1;
         } else if(var2 == null) {
            return 1;
         } else {
            int var3 = this.identityHashCode(var1);
            int var4 = this.identityHashCode(var2);
            if(var3 != var4) {
               return var3 < var4?-1:1;
            } else {
               int var5 = ((Integer)this.uids.get(var1)).compareTo((Integer)this.uids.get(var2));
               if(var5 == 0) {
                  throw new AssertionError();
               } else {
                  return var5;
               }
            }
         }
      }

      public String toString() {
         return "Ordering.arbitrary()";
      }

      int identityHashCode(Object var1) {
         return System.identityHashCode(var1);
      }
   }

   private static class ArbitraryOrderingHolder {
      static final Ordering<Object> ARBITRARY_ORDERING = new Ordering.ArbitraryOrdering();

      private ArbitraryOrderingHolder() {
      }
   }
}
