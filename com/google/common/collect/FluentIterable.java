package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public abstract class FluentIterable<E> implements Iterable<E> {
   private final Iterable<E> iterable;

   protected FluentIterable() {
      this.iterable = this;
   }

   FluentIterable(Iterable<E> var1) {
      this.iterable = (Iterable)Preconditions.checkNotNull(var1);
   }

   public static <E> FluentIterable<E> from(final Iterable<E> var0) {
      return var0 instanceof FluentIterable?(FluentIterable)var0:new FluentIterable(var0) {
         public Iterator<E> iterator() {
            return var0.iterator();
         }
      };
   }

   /** @deprecated */
   @Deprecated
   public static <E> FluentIterable<E> from(FluentIterable<E> var0) {
      return (FluentIterable)Preconditions.checkNotNull(var0);
   }

   public String toString() {
      return Iterables.toString(this.iterable);
   }

   public final int size() {
      return Iterables.size(this.iterable);
   }

   public final boolean contains(@Nullable Object var1) {
      return Iterables.contains(this.iterable, var1);
   }

   @CheckReturnValue
   public final FluentIterable<E> cycle() {
      return from(Iterables.cycle(this.iterable));
   }

   @CheckReturnValue
   public final FluentIterable<E> filter(Predicate<? super E> var1) {
      return from(Iterables.filter(this.iterable, var1));
   }

   @CheckReturnValue
   @GwtIncompatible("Class.isInstance")
   public final <T> FluentIterable<T> filter(Class<T> var1) {
      return from(Iterables.filter(this.iterable, var1));
   }

   public final boolean anyMatch(Predicate<? super E> var1) {
      return Iterables.any(this.iterable, var1);
   }

   public final boolean allMatch(Predicate<? super E> var1) {
      return Iterables.all(this.iterable, var1);
   }

   public final Optional<E> firstMatch(Predicate<? super E> var1) {
      return Iterables.tryFind(this.iterable, var1);
   }

   public final <T> FluentIterable<T> transform(Function<? super E, T> var1) {
      return from(Iterables.transform(this.iterable, var1));
   }

   public <T> FluentIterable<T> transformAndConcat(Function<? super E, ? extends Iterable<? extends T>> var1) {
      return from(Iterables.concat((Iterable)this.transform(var1)));
   }

   public final Optional<E> first() {
      Iterator var1 = this.iterable.iterator();
      return var1.hasNext()?Optional.of(var1.next()):Optional.absent();
   }

   public final Optional<E> last() {
      if(this.iterable instanceof List) {
         List var3 = (List)this.iterable;
         return var3.isEmpty()?Optional.absent():Optional.of(var3.get(var3.size() - 1));
      } else {
         Iterator var1 = this.iterable.iterator();
         if(!var1.hasNext()) {
            return Optional.absent();
         } else if(this.iterable instanceof SortedSet) {
            SortedSet var4 = (SortedSet)this.iterable;
            return Optional.of(var4.last());
         } else {
            Object var2;
            do {
               var2 = var1.next();
            } while(var1.hasNext());

            return Optional.of(var2);
         }
      }
   }

   @CheckReturnValue
   public final FluentIterable<E> skip(int var1) {
      return from(Iterables.skip(this.iterable, var1));
   }

   @CheckReturnValue
   public final FluentIterable<E> limit(int var1) {
      return from(Iterables.limit(this.iterable, var1));
   }

   public final boolean isEmpty() {
      return !this.iterable.iterator().hasNext();
   }

   public final ImmutableList<E> toList() {
      return ImmutableList.copyOf(this.iterable);
   }

   @Beta
   public final ImmutableList<E> toSortedList(Comparator<? super E> var1) {
      return Ordering.from(var1).immutableSortedCopy(this.iterable);
   }

   public final ImmutableSet<E> toSet() {
      return ImmutableSet.copyOf(this.iterable);
   }

   public final ImmutableSortedSet<E> toSortedSet(Comparator<? super E> var1) {
      return ImmutableSortedSet.copyOf(var1, this.iterable);
   }

   public final <V> ImmutableMap<E, V> toMap(Function<? super E, V> var1) {
      return Maps.toMap(this.iterable, var1);
   }

   public final <K> ImmutableListMultimap<K, E> index(Function<? super E, K> var1) {
      return Multimaps.index(this.iterable, var1);
   }

   public final <K> ImmutableMap<K, E> uniqueIndex(Function<? super E, K> var1) {
      return Maps.uniqueIndex(this.iterable, var1);
   }

   @GwtIncompatible("Array.newArray(Class, int)")
   public final E[] toArray(Class<E> var1) {
      return Iterables.toArray(this.iterable, var1);
   }

   public final <C extends Collection<? super E>> C copyInto(C var1) {
      Preconditions.checkNotNull(var1);
      if(this.iterable instanceof Collection) {
         var1.addAll(Collections2.cast(this.iterable));
      } else {
         Iterator var2 = this.iterable.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            var1.add(var3);
         }
      }

      return var1;
   }

   public final E get(int var1) {
      return Iterables.get(this.iterable, var1);
   }

   private static class FromIterableFunction<E> implements Function<Iterable<E>, FluentIterable<E>> {
      private FromIterableFunction() {
      }

      public FluentIterable<E> apply(Iterable<E> var1) {
         return FluentIterable.from(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object apply(Object var1) {
         return this.apply((Iterable)var1);
      }
   }
}
