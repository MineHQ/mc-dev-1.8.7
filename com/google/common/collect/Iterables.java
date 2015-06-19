package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.TransformedIterator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class Iterables {
   private Iterables() {
   }

   public static <T> Iterable<T> unmodifiableIterable(Iterable<T> var0) {
      Preconditions.checkNotNull(var0);
      return (Iterable)(!(var0 instanceof Iterables.UnmodifiableIterable) && !(var0 instanceof ImmutableCollection)?new Iterables.UnmodifiableIterable(var0, null):var0);
   }

   /** @deprecated */
   @Deprecated
   public static <E> Iterable<E> unmodifiableIterable(ImmutableCollection<E> var0) {
      return (Iterable)Preconditions.checkNotNull(var0);
   }

   public static int size(Iterable<?> var0) {
      return var0 instanceof Collection?((Collection)var0).size():Iterators.size(var0.iterator());
   }

   public static boolean contains(Iterable<?> var0, @Nullable Object var1) {
      if(var0 instanceof Collection) {
         Collection var2 = (Collection)var0;
         return Collections2.safeContains(var2, var1);
      } else {
         return Iterators.contains(var0.iterator(), var1);
      }
   }

   public static boolean removeAll(Iterable<?> var0, Collection<?> var1) {
      return var0 instanceof Collection?((Collection)var0).removeAll((Collection)Preconditions.checkNotNull(var1)):Iterators.removeAll(var0.iterator(), var1);
   }

   public static boolean retainAll(Iterable<?> var0, Collection<?> var1) {
      return var0 instanceof Collection?((Collection)var0).retainAll((Collection)Preconditions.checkNotNull(var1)):Iterators.retainAll(var0.iterator(), var1);
   }

   public static <T> boolean removeIf(Iterable<T> var0, Predicate<? super T> var1) {
      return var0 instanceof RandomAccess && var0 instanceof List?removeIfFromRandomAccessList((List)var0, (Predicate)Preconditions.checkNotNull(var1)):Iterators.removeIf(var0.iterator(), var1);
   }

   private static <T> boolean removeIfFromRandomAccessList(List<T> var0, Predicate<? super T> var1) {
      int var2 = 0;

      int var3;
      for(var3 = 0; var2 < var0.size(); ++var2) {
         Object var4 = var0.get(var2);
         if(!var1.apply(var4)) {
            if(var2 > var3) {
               try {
                  var0.set(var3, var4);
               } catch (UnsupportedOperationException var6) {
                  slowRemoveIfForRemainingElements(var0, var1, var3, var2);
                  return true;
               }
            }

            ++var3;
         }
      }

      var0.subList(var3, var0.size()).clear();
      return var2 != var3;
   }

   private static <T> void slowRemoveIfForRemainingElements(List<T> var0, Predicate<? super T> var1, int var2, int var3) {
      int var4;
      for(var4 = var0.size() - 1; var4 > var3; --var4) {
         if(var1.apply(var0.get(var4))) {
            var0.remove(var4);
         }
      }

      for(var4 = var3 - 1; var4 >= var2; --var4) {
         var0.remove(var4);
      }

   }

   @Nullable
   static <T> T removeFirstMatching(Iterable<T> var0, Predicate<? super T> var1) {
      Preconditions.checkNotNull(var1);
      Iterator var2 = var0.iterator();

      Object var3;
      do {
         if(!var2.hasNext()) {
            return null;
         }

         var3 = var2.next();
      } while(!var1.apply(var3));

      var2.remove();
      return var3;
   }

   public static boolean elementsEqual(Iterable<?> var0, Iterable<?> var1) {
      if(var0 instanceof Collection && var1 instanceof Collection) {
         Collection var2 = (Collection)var0;
         Collection var3 = (Collection)var1;
         if(var2.size() != var3.size()) {
            return false;
         }
      }

      return Iterators.elementsEqual(var0.iterator(), var1.iterator());
   }

   public static String toString(Iterable<?> var0) {
      return Iterators.toString(var0.iterator());
   }

   public static <T> T getOnlyElement(Iterable<T> var0) {
      return Iterators.getOnlyElement(var0.iterator());
   }

   @Nullable
   public static <T> T getOnlyElement(Iterable<? extends T> var0, @Nullable T var1) {
      return Iterators.getOnlyElement(var0.iterator(), var1);
   }

   @GwtIncompatible("Array.newInstance(Class, int)")
   public static <T> T[] toArray(Iterable<? extends T> var0, Class<T> var1) {
      Collection var2 = toCollection(var0);
      Object[] var3 = ObjectArrays.newArray(var1, var2.size());
      return var2.toArray(var3);
   }

   static Object[] toArray(Iterable<?> var0) {
      return toCollection(var0).toArray();
   }

   private static <E> Collection<E> toCollection(Iterable<E> var0) {
      return (Collection)(var0 instanceof Collection?(Collection)var0:Lists.newArrayList(var0.iterator()));
   }

   public static <T> boolean addAll(Collection<T> var0, Iterable<? extends T> var1) {
      if(var1 instanceof Collection) {
         Collection var2 = Collections2.cast(var1);
         return var0.addAll(var2);
      } else {
         return Iterators.addAll(var0, ((Iterable)Preconditions.checkNotNull(var1)).iterator());
      }
   }

   public static int frequency(Iterable<?> var0, @Nullable Object var1) {
      return var0 instanceof Multiset?((Multiset)var0).count(var1):(var0 instanceof Set?(((Set)var0).contains(var1)?1:0):Iterators.frequency(var0.iterator(), var1));
   }

   public static <T> Iterable<T> cycle(final Iterable<T> var0) {
      Preconditions.checkNotNull(var0);
      return new FluentIterable() {
         public Iterator<T> iterator() {
            return Iterators.cycle(var0);
         }

         public String toString() {
            return var0.toString() + " (cycled)";
         }
      };
   }

   public static <T> Iterable<T> cycle(T... var0) {
      return cycle((Iterable)Lists.newArrayList(var0));
   }

   public static <T> Iterable<T> concat(Iterable<? extends T> var0, Iterable<? extends T> var1) {
      return concat((Iterable)ImmutableList.of(var0, var1));
   }

   public static <T> Iterable<T> concat(Iterable<? extends T> var0, Iterable<? extends T> var1, Iterable<? extends T> var2) {
      return concat((Iterable)ImmutableList.of(var0, var1, var2));
   }

   public static <T> Iterable<T> concat(Iterable<? extends T> var0, Iterable<? extends T> var1, Iterable<? extends T> var2, Iterable<? extends T> var3) {
      return concat((Iterable)ImmutableList.of(var0, var1, var2, var3));
   }

   public static <T> Iterable<T> concat(Iterable... var0) {
      return concat((Iterable)ImmutableList.copyOf((Object[])var0));
   }

   public static <T> Iterable<T> concat(final Iterable<? extends Iterable<? extends T>> var0) {
      Preconditions.checkNotNull(var0);
      return new FluentIterable() {
         public Iterator<T> iterator() {
            return Iterators.concat(Iterables.iterators(var0));
         }
      };
   }

   private static <T> Iterator<Iterator<? extends T>> iterators(Iterable<? extends Iterable<? extends T>> var0) {
      return new TransformedIterator(var0.iterator()) {
         Iterator<? extends T> transform(Iterable<? extends T> var1) {
            return var1.iterator();
         }

         // $FF: synthetic method
         // $FF: bridge method
         Object transform(Object var1) {
            return this.transform((Iterable)var1);
         }
      };
   }

   public static <T> Iterable<List<T>> partition(final Iterable<T> var0, final int var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 > 0);
      return new FluentIterable() {
         public Iterator<List<T>> iterator() {
            return Iterators.partition(var0.iterator(), var1);
         }
      };
   }

   public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> var0, final int var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 > 0);
      return new FluentIterable() {
         public Iterator<List<T>> iterator() {
            return Iterators.paddedPartition(var0.iterator(), var1);
         }
      };
   }

   public static <T> Iterable<T> filter(final Iterable<T> var0, final Predicate<? super T> var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new FluentIterable() {
         public Iterator<T> iterator() {
            return Iterators.filter(var0.iterator(), var1);
         }
      };
   }

   @GwtIncompatible("Class.isInstance")
   public static <T> Iterable<T> filter(final Iterable<?> var0, final Class<T> var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new FluentIterable() {
         public Iterator<T> iterator() {
            return Iterators.filter(var0.iterator(), var1);
         }
      };
   }

   public static <T> boolean any(Iterable<T> var0, Predicate<? super T> var1) {
      return Iterators.any(var0.iterator(), var1);
   }

   public static <T> boolean all(Iterable<T> var0, Predicate<? super T> var1) {
      return Iterators.all(var0.iterator(), var1);
   }

   public static <T> T find(Iterable<T> var0, Predicate<? super T> var1) {
      return Iterators.find(var0.iterator(), var1);
   }

   @Nullable
   public static <T> T find(Iterable<? extends T> var0, Predicate<? super T> var1, @Nullable T var2) {
      return Iterators.find(var0.iterator(), var1, var2);
   }

   public static <T> Optional<T> tryFind(Iterable<T> var0, Predicate<? super T> var1) {
      return Iterators.tryFind(var0.iterator(), var1);
   }

   public static <T> int indexOf(Iterable<T> var0, Predicate<? super T> var1) {
      return Iterators.indexOf(var0.iterator(), var1);
   }

   public static <F, T> Iterable<T> transform(final Iterable<F> var0, final Function<? super F, ? extends T> var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new FluentIterable() {
         public Iterator<T> iterator() {
            return Iterators.transform(var0.iterator(), var1);
         }
      };
   }

   public static <T> T get(Iterable<T> var0, int var1) {
      Preconditions.checkNotNull(var0);
      return var0 instanceof List?((List)var0).get(var1):Iterators.get(var0.iterator(), var1);
   }

   @Nullable
   public static <T> T get(Iterable<? extends T> var0, int var1, @Nullable T var2) {
      Preconditions.checkNotNull(var0);
      Iterators.checkNonnegative(var1);
      if(var0 instanceof List) {
         List var4 = Lists.cast(var0);
         return var1 < var4.size()?var4.get(var1):var2;
      } else {
         Iterator var3 = var0.iterator();
         Iterators.advance(var3, var1);
         return Iterators.getNext(var3, var2);
      }
   }

   @Nullable
   public static <T> T getFirst(Iterable<? extends T> var0, @Nullable T var1) {
      return Iterators.getNext(var0.iterator(), var1);
   }

   public static <T> T getLast(Iterable<T> var0) {
      if(var0 instanceof List) {
         List var1 = (List)var0;
         if(var1.isEmpty()) {
            throw new NoSuchElementException();
         } else {
            return getLastInNonemptyList(var1);
         }
      } else {
         return Iterators.getLast(var0.iterator());
      }
   }

   @Nullable
   public static <T> T getLast(Iterable<? extends T> var0, @Nullable T var1) {
      if(var0 instanceof Collection) {
         Collection var2 = Collections2.cast(var0);
         if(var2.isEmpty()) {
            return var1;
         }

         if(var0 instanceof List) {
            return getLastInNonemptyList(Lists.cast(var0));
         }
      }

      return Iterators.getLast(var0.iterator(), var1);
   }

   private static <T> T getLastInNonemptyList(List<T> var0) {
      return var0.get(var0.size() - 1);
   }

   public static <T> Iterable<T> skip(final Iterable<T> var0, final int var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 >= 0, "number to skip cannot be negative");
      if(var0 instanceof List) {
         final List var2 = (List)var0;
         return new FluentIterable() {
            public Iterator<T> iterator() {
               int var1x = Math.min(var2.size(), var1);
               return var2.subList(var1x, var2.size()).iterator();
            }
         };
      } else {
         return new FluentIterable() {
            public Iterator<T> iterator() {
               final Iterator var1x = var0.iterator();
               Iterators.advance(var1x, var1);
               return new Iterator() {
                  boolean atStart = true;

                  public boolean hasNext() {
                     return var1x.hasNext();
                  }

                  public T next() {
                     Object var1xx = var1x.next();
                     this.atStart = false;
                     return var1xx;
                  }

                  public void remove() {
                     CollectPreconditions.checkRemove(!this.atStart);
                     var1x.remove();
                  }
               };
            }
         };
      }
   }

   public static <T> Iterable<T> limit(final Iterable<T> var0, final int var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var1 >= 0, "limit is negative");
      return new FluentIterable() {
         public Iterator<T> iterator() {
            return Iterators.limit(var0.iterator(), var1);
         }
      };
   }

   public static <T> Iterable<T> consumingIterable(final Iterable<T> var0) {
      if(var0 instanceof Queue) {
         return new FluentIterable() {
            public Iterator<T> iterator() {
               return new Iterables.ConsumingQueueIterator((Queue)var0, null);
            }

            public String toString() {
               return "Iterables.consumingIterable(...)";
            }
         };
      } else {
         Preconditions.checkNotNull(var0);
         return new FluentIterable() {
            public Iterator<T> iterator() {
               return Iterators.consumingIterator(var0.iterator());
            }

            public String toString() {
               return "Iterables.consumingIterable(...)";
            }
         };
      }
   }

   public static boolean isEmpty(Iterable<?> var0) {
      return var0 instanceof Collection?((Collection)var0).isEmpty():!var0.iterator().hasNext();
   }

   @Beta
   public static <T> Iterable<T> mergeSorted(final Iterable<? extends Iterable<? extends T>> var0, final Comparator<? super T> var1) {
      Preconditions.checkNotNull(var0, "iterables");
      Preconditions.checkNotNull(var1, "comparator");
      FluentIterable var2 = new FluentIterable() {
         public Iterator<T> iterator() {
            return Iterators.mergeSorted(Iterables.transform(var0, Iterables.toIterator()), var1);
         }
      };
      return new Iterables.UnmodifiableIterable(var2, null);
   }

   private static <T> Function<Iterable<? extends T>, Iterator<? extends T>> toIterator() {
      return new Function() {
         public Iterator<? extends T> apply(Iterable<? extends T> var1) {
            return var1.iterator();
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object apply(Object var1) {
            return this.apply((Iterable)var1);
         }
      };
   }

   private static class ConsumingQueueIterator<T> extends AbstractIterator<T> {
      private final Queue<T> queue;

      private ConsumingQueueIterator(Queue<T> var1) {
         this.queue = var1;
      }

      public T computeNext() {
         try {
            return this.queue.remove();
         } catch (NoSuchElementException var2) {
            return this.endOfData();
         }
      }

      // $FF: synthetic method
      ConsumingQueueIterator(Queue var1, Object var2) {
         this(var1);
      }
   }

   private static final class UnmodifiableIterable<T> extends FluentIterable<T> {
      private final Iterable<T> iterable;

      private UnmodifiableIterable(Iterable<T> var1) {
         this.iterable = var1;
      }

      public Iterator<T> iterator() {
         return Iterators.unmodifiableIterator(this.iterable.iterator());
      }

      public String toString() {
         return this.iterable.toString();
      }

      // $FF: synthetic method
      UnmodifiableIterable(Iterable var1, Object var2) {
         this(var1);
      }
   }
}
