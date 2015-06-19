package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.AbstractIndexedListIterator;
import com.google.common.collect.CartesianList;
import com.google.common.collect.Collections2;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ForwardingNavigableSet;
import com.google.common.collect.ForwardingSortedSet;
import com.google.common.collect.ImmutableEnumSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.Platform;
import com.google.common.collect.Synchronized;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class Sets {
   private Sets() {
   }

   @GwtCompatible(
      serializable = true
   )
   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E var0, E... var1) {
      return ImmutableEnumSet.asImmutable(EnumSet.of(var0, var1));
   }

   @GwtCompatible(
      serializable = true
   )
   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> var0) {
      if(var0 instanceof ImmutableEnumSet) {
         return (ImmutableEnumSet)var0;
      } else if(var0 instanceof Collection) {
         Collection var3 = (Collection)var0;
         return var3.isEmpty()?ImmutableSet.of():ImmutableEnumSet.asImmutable(EnumSet.copyOf(var3));
      } else {
         Iterator var1 = var0.iterator();
         if(var1.hasNext()) {
            EnumSet var2 = EnumSet.of((Enum)var1.next());
            Iterators.addAll(var2, var1);
            return ImmutableEnumSet.asImmutable(var2);
         } else {
            return ImmutableSet.of();
         }
      }
   }

   public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> var0, Class<E> var1) {
      EnumSet var2 = EnumSet.noneOf(var1);
      Iterables.addAll(var2, var0);
      return var2;
   }

   public static <E> HashSet<E> newHashSet() {
      return new HashSet();
   }

   public static <E> HashSet<E> newHashSet(E... var0) {
      HashSet var1 = newHashSetWithExpectedSize(var0.length);
      Collections.addAll(var1, var0);
      return var1;
   }

   public static <E> HashSet<E> newHashSetWithExpectedSize(int var0) {
      return new HashSet(Maps.capacity(var0));
   }

   public static <E> HashSet<E> newHashSet(Iterable<? extends E> var0) {
      return var0 instanceof Collection?new HashSet(Collections2.cast(var0)):newHashSet(var0.iterator());
   }

   public static <E> HashSet<E> newHashSet(Iterator<? extends E> var0) {
      HashSet var1 = newHashSet();
      Iterators.addAll(var1, var0);
      return var1;
   }

   public static <E> Set<E> newConcurrentHashSet() {
      return newSetFromMap(new ConcurrentHashMap());
   }

   public static <E> Set<E> newConcurrentHashSet(Iterable<? extends E> var0) {
      Set var1 = newConcurrentHashSet();
      Iterables.addAll(var1, var0);
      return var1;
   }

   public static <E> LinkedHashSet<E> newLinkedHashSet() {
      return new LinkedHashSet();
   }

   public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int var0) {
      return new LinkedHashSet(Maps.capacity(var0));
   }

   public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> var0) {
      if(var0 instanceof Collection) {
         return new LinkedHashSet(Collections2.cast(var0));
      } else {
         LinkedHashSet var1 = newLinkedHashSet();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static <E extends Comparable> TreeSet<E> newTreeSet() {
      return new TreeSet();
   }

   public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> var0) {
      TreeSet var1 = newTreeSet();
      Iterables.addAll(var1, var0);
      return var1;
   }

   public static <E> TreeSet<E> newTreeSet(Comparator<? super E> var0) {
      return new TreeSet((Comparator)Preconditions.checkNotNull(var0));
   }

   public static <E> Set<E> newIdentityHashSet() {
      return newSetFromMap(Maps.newIdentityHashMap());
   }

   @GwtIncompatible("CopyOnWriteArraySet")
   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
      return new CopyOnWriteArraySet();
   }

   @GwtIncompatible("CopyOnWriteArraySet")
   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> var0) {
      Object var1 = var0 instanceof Collection?Collections2.cast(var0):Lists.newArrayList(var0);
      return new CopyOnWriteArraySet((Collection)var1);
   }

   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> var0) {
      if(var0 instanceof EnumSet) {
         return EnumSet.complementOf((EnumSet)var0);
      } else {
         Preconditions.checkArgument(!var0.isEmpty(), "collection is empty; use the other version of this method");
         Class var1 = ((Enum)var0.iterator().next()).getDeclaringClass();
         return makeComplementByHand(var0, var1);
      }
   }

   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> var0, Class<E> var1) {
      Preconditions.checkNotNull(var0);
      return var0 instanceof EnumSet?EnumSet.complementOf((EnumSet)var0):makeComplementByHand(var0, var1);
   }

   private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> var0, Class<E> var1) {
      EnumSet var2 = EnumSet.allOf(var1);
      var2.removeAll(var0);
      return var2;
   }

   public static <E> Set<E> newSetFromMap(Map<E, Boolean> var0) {
      return Platform.newSetFromMap(var0);
   }

   public static <E> Sets.SetView<E> union(final Set<? extends E> var0, final Set<? extends E> var1) {
      Preconditions.checkNotNull(var0, "set1");
      Preconditions.checkNotNull(var1, "set2");
      final Sets.SetView var2 = difference(var1, var0);
      return new Sets.SetView(null) {
         public int size() {
            return var0.size() + var2.size();
         }

         public boolean isEmpty() {
            return var0.isEmpty() && var1.isEmpty();
         }

         public Iterator<E> iterator() {
            return Iterators.unmodifiableIterator(Iterators.concat(var0.iterator(), var2.iterator()));
         }

         public boolean contains(Object var1x) {
            return var0.contains(var1x) || var1.contains(var1x);
         }

         public <S extends Set<E>> S copyInto(S var1x) {
            var1x.addAll(var0);
            var1x.addAll(var1);
            return var1x;
         }

         public ImmutableSet<E> immutableCopy() {
            return (new ImmutableSet.Builder()).addAll((Iterable)var0).addAll((Iterable)var1).build();
         }
      };
   }

   public static <E> Sets.SetView<E> intersection(final Set<E> var0, final Set<?> var1) {
      Preconditions.checkNotNull(var0, "set1");
      Preconditions.checkNotNull(var1, "set2");
      final Predicate var2 = Predicates.in(var1);
      return new Sets.SetView(null) {
         public Iterator<E> iterator() {
            return Iterators.filter(var0.iterator(), var2);
         }

         public int size() {
            return Iterators.size(this.iterator());
         }

         public boolean isEmpty() {
            return !this.iterator().hasNext();
         }

         public boolean contains(Object var1x) {
            return var0.contains(var1x) && var1.contains(var1x);
         }

         public boolean containsAll(Collection<?> var1x) {
            return var0.containsAll(var1x) && var1.containsAll(var1x);
         }
      };
   }

   public static <E> Sets.SetView<E> difference(final Set<E> var0, final Set<?> var1) {
      Preconditions.checkNotNull(var0, "set1");
      Preconditions.checkNotNull(var1, "set2");
      final Predicate var2 = Predicates.not(Predicates.in(var1));
      return new Sets.SetView(null) {
         public Iterator<E> iterator() {
            return Iterators.filter(var0.iterator(), var2);
         }

         public int size() {
            return Iterators.size(this.iterator());
         }

         public boolean isEmpty() {
            return var1.containsAll(var0);
         }

         public boolean contains(Object var1x) {
            return var0.contains(var1x) && !var1.contains(var1x);
         }
      };
   }

   public static <E> Sets.SetView<E> symmetricDifference(Set<? extends E> var0, Set<? extends E> var1) {
      Preconditions.checkNotNull(var0, "set1");
      Preconditions.checkNotNull(var1, "set2");
      return difference(union(var0, var1), intersection(var0, var1));
   }

   public static <E> Set<E> filter(Set<E> var0, Predicate<? super E> var1) {
      if(var0 instanceof SortedSet) {
         return filter((SortedSet)var0, var1);
      } else if(var0 instanceof Sets.FilteredSet) {
         Sets.FilteredSet var2 = (Sets.FilteredSet)var0;
         Predicate var3 = Predicates.and(var2.predicate, var1);
         return new Sets.FilteredSet((Set)var2.unfiltered, var3);
      } else {
         return new Sets.FilteredSet((Set)Preconditions.checkNotNull(var0), (Predicate)Preconditions.checkNotNull(var1));
      }
   }

   public static <E> SortedSet<E> filter(SortedSet<E> var0, Predicate<? super E> var1) {
      return Platform.setsFilterSortedSet(var0, var1);
   }

   static <E> SortedSet<E> filterSortedIgnoreNavigable(SortedSet<E> var0, Predicate<? super E> var1) {
      if(var0 instanceof Sets.FilteredSet) {
         Sets.FilteredSet var2 = (Sets.FilteredSet)var0;
         Predicate var3 = Predicates.and(var2.predicate, var1);
         return new Sets.FilteredSortedSet((SortedSet)var2.unfiltered, var3);
      } else {
         return new Sets.FilteredSortedSet((SortedSet)Preconditions.checkNotNull(var0), (Predicate)Preconditions.checkNotNull(var1));
      }
   }

   @GwtIncompatible("NavigableSet")
   public static <E> NavigableSet<E> filter(NavigableSet<E> var0, Predicate<? super E> var1) {
      if(var0 instanceof Sets.FilteredSet) {
         Sets.FilteredSet var2 = (Sets.FilteredSet)var0;
         Predicate var3 = Predicates.and(var2.predicate, var1);
         return new Sets.FilteredNavigableSet((NavigableSet)var2.unfiltered, var3);
      } else {
         return new Sets.FilteredNavigableSet((NavigableSet)Preconditions.checkNotNull(var0), (Predicate)Preconditions.checkNotNull(var1));
      }
   }

   public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> var0) {
      return Sets.CartesianSet.create(var0);
   }

   public static <B> Set<List<B>> cartesianProduct(Set... var0) {
      return cartesianProduct(Arrays.asList(var0));
   }

   @GwtCompatible(
      serializable = false
   )
   public static <E> Set<Set<E>> powerSet(Set<E> var0) {
      return new Sets.PowerSet(var0);
   }

   static int hashCodeImpl(Set<?> var0) {
      int var1 = 0;

      for(Iterator var2 = var0.iterator(); var2.hasNext(); var1 = ~(~var1)) {
         Object var3 = var2.next();
         var1 += var3 != null?var3.hashCode():0;
      }

      return var1;
   }

   static boolean equalsImpl(Set<?> var0, @Nullable Object var1) {
      if(var0 == var1) {
         return true;
      } else if(var1 instanceof Set) {
         Set var2 = (Set)var1;

         try {
            return var0.size() == var2.size() && var0.containsAll(var2);
         } catch (NullPointerException var4) {
            return false;
         } catch (ClassCastException var5) {
            return false;
         }
      } else {
         return false;
      }
   }

   @GwtIncompatible("NavigableSet")
   public static <E> NavigableSet<E> unmodifiableNavigableSet(NavigableSet<E> var0) {
      return (NavigableSet)(!(var0 instanceof ImmutableSortedSet) && !(var0 instanceof Sets.UnmodifiableNavigableSet)?new Sets.UnmodifiableNavigableSet(var0):var0);
   }

   @GwtIncompatible("NavigableSet")
   public static <E> NavigableSet<E> synchronizedNavigableSet(NavigableSet<E> var0) {
      return Synchronized.navigableSet(var0);
   }

   static boolean removeAllImpl(Set<?> var0, Iterator<?> var1) {
      boolean var2;
      for(var2 = false; var1.hasNext(); var2 |= var0.remove(var1.next())) {
         ;
      }

      return var2;
   }

   static boolean removeAllImpl(Set<?> var0, Collection<?> var1) {
      Preconditions.checkNotNull(var1);
      if(var1 instanceof Multiset) {
         var1 = ((Multiset)var1).elementSet();
      }

      return var1 instanceof Set && ((Collection)var1).size() > var0.size()?Iterators.removeAll(var0.iterator(), (Collection)var1):removeAllImpl(var0, ((Collection)var1).iterator());
   }

   @GwtIncompatible("NavigableSet")
   static class DescendingSet<E> extends ForwardingNavigableSet<E> {
      private final NavigableSet<E> forward;

      DescendingSet(NavigableSet<E> var1) {
         this.forward = var1;
      }

      protected NavigableSet<E> delegate() {
         return this.forward;
      }

      public E lower(E var1) {
         return this.forward.higher(var1);
      }

      public E floor(E var1) {
         return this.forward.ceiling(var1);
      }

      public E ceiling(E var1) {
         return this.forward.floor(var1);
      }

      public E higher(E var1) {
         return this.forward.lower(var1);
      }

      public E pollFirst() {
         return this.forward.pollLast();
      }

      public E pollLast() {
         return this.forward.pollFirst();
      }

      public NavigableSet<E> descendingSet() {
         return this.forward;
      }

      public Iterator<E> descendingIterator() {
         return this.forward.iterator();
      }

      public NavigableSet<E> subSet(E var1, boolean var2, E var3, boolean var4) {
         return this.forward.subSet(var3, var4, var1, var2).descendingSet();
      }

      public NavigableSet<E> headSet(E var1, boolean var2) {
         return this.forward.tailSet(var1, var2).descendingSet();
      }

      public NavigableSet<E> tailSet(E var1, boolean var2) {
         return this.forward.headSet(var1, var2).descendingSet();
      }

      public Comparator<? super E> comparator() {
         Comparator var1 = this.forward.comparator();
         return var1 == null?Ordering.natural().reverse():reverse(var1);
      }

      private static <T> Ordering<T> reverse(Comparator<T> var0) {
         return Ordering.from(var0).reverse();
      }

      public E first() {
         return this.forward.last();
      }

      public SortedSet<E> headSet(E var1) {
         return this.standardHeadSet(var1);
      }

      public E last() {
         return this.forward.first();
      }

      public SortedSet<E> subSet(E var1, E var2) {
         return this.standardSubSet(var1, var2);
      }

      public SortedSet<E> tailSet(E var1) {
         return this.standardTailSet(var1);
      }

      public Iterator<E> iterator() {
         return this.forward.descendingIterator();
      }

      public Object[] toArray() {
         return this.standardToArray();
      }

      public <T> T[] toArray(T[] var1) {
         return this.standardToArray(var1);
      }

      public String toString() {
         return this.standardToString();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected SortedSet delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Set delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Collection delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   @GwtIncompatible("NavigableSet")
   static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable {
      private final NavigableSet<E> delegate;
      private transient Sets.UnmodifiableNavigableSet<E> descendingSet;
      private static final long serialVersionUID = 0L;

      UnmodifiableNavigableSet(NavigableSet<E> var1) {
         this.delegate = (NavigableSet)Preconditions.checkNotNull(var1);
      }

      protected SortedSet<E> delegate() {
         return Collections.unmodifiableSortedSet(this.delegate);
      }

      public E lower(E var1) {
         return this.delegate.lower(var1);
      }

      public E floor(E var1) {
         return this.delegate.floor(var1);
      }

      public E ceiling(E var1) {
         return this.delegate.ceiling(var1);
      }

      public E higher(E var1) {
         return this.delegate.higher(var1);
      }

      public E pollFirst() {
         throw new UnsupportedOperationException();
      }

      public E pollLast() {
         throw new UnsupportedOperationException();
      }

      public NavigableSet<E> descendingSet() {
         Sets.UnmodifiableNavigableSet var1 = this.descendingSet;
         if(var1 == null) {
            var1 = this.descendingSet = new Sets.UnmodifiableNavigableSet(this.delegate.descendingSet());
            var1.descendingSet = this;
         }

         return var1;
      }

      public Iterator<E> descendingIterator() {
         return Iterators.unmodifiableIterator(this.delegate.descendingIterator());
      }

      public NavigableSet<E> subSet(E var1, boolean var2, E var3, boolean var4) {
         return Sets.unmodifiableNavigableSet(this.delegate.subSet(var1, var2, var3, var4));
      }

      public NavigableSet<E> headSet(E var1, boolean var2) {
         return Sets.unmodifiableNavigableSet(this.delegate.headSet(var1, var2));
      }

      public NavigableSet<E> tailSet(E var1, boolean var2) {
         return Sets.unmodifiableNavigableSet(this.delegate.tailSet(var1, var2));
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Set delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Collection delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   private static final class PowerSet<E> extends AbstractSet<Set<E>> {
      final ImmutableMap<E, Integer> inputSet;

      PowerSet(Set<E> var1) {
         ImmutableMap.Builder var2 = ImmutableMap.builder();
         int var3 = 0;
         Iterator var4 = ((Set)Preconditions.checkNotNull(var1)).iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            var2.put(var5, Integer.valueOf(var3++));
         }

         this.inputSet = var2.build();
         Preconditions.checkArgument(this.inputSet.size() <= 30, "Too many elements to create power set: %s > 30", new Object[]{Integer.valueOf(this.inputSet.size())});
      }

      public int size() {
         return 1 << this.inputSet.size();
      }

      public boolean isEmpty() {
         return false;
      }

      public Iterator<Set<E>> iterator() {
         return new AbstractIndexedListIterator(this.size()) {
            protected Set<E> get(int var1) {
               return new Sets.SubSet(PowerSet.this.inputSet, var1);
            }

            // $FF: synthetic method
            // $FF: bridge method
            protected Object get(int var1) {
               return this.get(var1);
            }
         };
      }

      public boolean contains(@Nullable Object var1) {
         if(var1 instanceof Set) {
            Set var2 = (Set)var1;
            return this.inputSet.keySet().containsAll(var2);
         } else {
            return false;
         }
      }

      public boolean equals(@Nullable Object var1) {
         if(var1 instanceof Sets.PowerSet) {
            Sets.PowerSet var2 = (Sets.PowerSet)var1;
            return this.inputSet.equals(var2.inputSet);
         } else {
            return super.equals(var1);
         }
      }

      public int hashCode() {
         return this.inputSet.keySet().hashCode() << this.inputSet.size() - 1;
      }

      public String toString() {
         return "powerSet(" + this.inputSet + ")";
      }
   }

   private static final class SubSet<E> extends AbstractSet<E> {
      private final ImmutableMap<E, Integer> inputSet;
      private final int mask;

      SubSet(ImmutableMap<E, Integer> var1, int var2) {
         this.inputSet = var1;
         this.mask = var2;
      }

      public Iterator<E> iterator() {
         return new UnmodifiableIterator() {
            final ImmutableList<E> elements;
            int remainingSetBits;

            {
               this.elements = SubSet.this.inputSet.keySet().asList();
               this.remainingSetBits = SubSet.this.mask;
            }

            public boolean hasNext() {
               return this.remainingSetBits != 0;
            }

            public E next() {
               int var1 = Integer.numberOfTrailingZeros(this.remainingSetBits);
               if(var1 == 32) {
                  throw new NoSuchElementException();
               } else {
                  this.remainingSetBits &= ~(1 << var1);
                  return this.elements.get(var1);
               }
            }
         };
      }

      public int size() {
         return Integer.bitCount(this.mask);
      }

      public boolean contains(@Nullable Object var1) {
         Integer var2 = (Integer)this.inputSet.get(var1);
         return var2 != null && (this.mask & 1 << var2.intValue()) != 0;
      }
   }

   private static final class CartesianSet<E> extends ForwardingCollection<List<E>> implements Set<List<E>> {
      private final transient ImmutableList<ImmutableSet<E>> axes;
      private final transient CartesianList<E> delegate;

      static <E> Set<List<E>> create(List<? extends Set<? extends E>> var0) {
         ImmutableList.Builder var1 = new ImmutableList.Builder(var0.size());
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Set var3 = (Set)var2.next();
            ImmutableSet var4 = ImmutableSet.copyOf((Collection)var3);
            if(var4.isEmpty()) {
               return ImmutableSet.of();
            }

            var1.add((Object)var4);
         }

         final ImmutableList var5 = var1.build();
         ImmutableList var6 = new ImmutableList() {
            public int size() {
               return var5.size();
            }

            public List<E> get(int var1) {
               return ((ImmutableSet)var5.get(var1)).asList();
            }

            boolean isPartialView() {
               return true;
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object get(int var1) {
               return this.get(var1);
            }
         };
         return new Sets.CartesianSet(var5, new CartesianList(var6));
      }

      private CartesianSet(ImmutableList<ImmutableSet<E>> var1, CartesianList<E> var2) {
         this.axes = var1;
         this.delegate = var2;
      }

      protected Collection<List<E>> delegate() {
         return this.delegate;
      }

      public boolean equals(@Nullable Object var1) {
         if(var1 instanceof Sets.CartesianSet) {
            Sets.CartesianSet var2 = (Sets.CartesianSet)var1;
            return this.axes.equals(var2.axes);
         } else {
            return super.equals(var1);
         }
      }

      public int hashCode() {
         int var1 = this.size() - 1;

         int var2;
         for(var2 = 0; var2 < this.axes.size(); ++var2) {
            var1 *= 31;
            var1 = ~(~var1);
         }

         var2 = 1;

         for(Iterator var3 = this.axes.iterator(); var3.hasNext(); var2 = ~(~var2)) {
            Set var4 = (Set)var3.next();
            var2 = 31 * var2 + this.size() / var4.size() * var4.hashCode();
         }

         var2 += var1;
         return ~(~var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   @GwtIncompatible("NavigableSet")
   private static class FilteredNavigableSet<E> extends Sets.FilteredSortedSet<E> implements NavigableSet<E> {
      FilteredNavigableSet(NavigableSet<E> var1, Predicate<? super E> var2) {
         super(var1, var2);
      }

      NavigableSet<E> unfiltered() {
         return (NavigableSet)this.unfiltered;
      }

      @Nullable
      public E lower(E var1) {
         return Iterators.getNext(this.headSet(var1, false).descendingIterator(), (Object)null);
      }

      @Nullable
      public E floor(E var1) {
         return Iterators.getNext(this.headSet(var1, true).descendingIterator(), (Object)null);
      }

      public E ceiling(E var1) {
         return Iterables.getFirst(this.tailSet(var1, true), (Object)null);
      }

      public E higher(E var1) {
         return Iterables.getFirst(this.tailSet(var1, false), (Object)null);
      }

      public E pollFirst() {
         return Iterables.removeFirstMatching(this.unfiltered(), this.predicate);
      }

      public E pollLast() {
         return Iterables.removeFirstMatching(this.unfiltered().descendingSet(), this.predicate);
      }

      public NavigableSet<E> descendingSet() {
         return Sets.filter(this.unfiltered().descendingSet(), this.predicate);
      }

      public Iterator<E> descendingIterator() {
         return Iterators.filter(this.unfiltered().descendingIterator(), this.predicate);
      }

      public E last() {
         return this.descendingIterator().next();
      }

      public NavigableSet<E> subSet(E var1, boolean var2, E var3, boolean var4) {
         return Sets.filter(this.unfiltered().subSet(var1, var2, var3, var4), this.predicate);
      }

      public NavigableSet<E> headSet(E var1, boolean var2) {
         return Sets.filter(this.unfiltered().headSet(var1, var2), this.predicate);
      }

      public NavigableSet<E> tailSet(E var1, boolean var2) {
         return Sets.filter(this.unfiltered().tailSet(var1, var2), this.predicate);
      }
   }

   private static class FilteredSortedSet<E> extends Sets.FilteredSet<E> implements SortedSet<E> {
      FilteredSortedSet(SortedSet<E> var1, Predicate<? super E> var2) {
         super(var1, var2);
      }

      public Comparator<? super E> comparator() {
         return ((SortedSet)this.unfiltered).comparator();
      }

      public SortedSet<E> subSet(E var1, E var2) {
         return new Sets.FilteredSortedSet(((SortedSet)this.unfiltered).subSet(var1, var2), this.predicate);
      }

      public SortedSet<E> headSet(E var1) {
         return new Sets.FilteredSortedSet(((SortedSet)this.unfiltered).headSet(var1), this.predicate);
      }

      public SortedSet<E> tailSet(E var1) {
         return new Sets.FilteredSortedSet(((SortedSet)this.unfiltered).tailSet(var1), this.predicate);
      }

      public E first() {
         return this.iterator().next();
      }

      public E last() {
         SortedSet var1 = (SortedSet)this.unfiltered;

         while(true) {
            Object var2 = var1.last();
            if(this.predicate.apply(var2)) {
               return var2;
            }

            var1 = var1.headSet(var2);
         }
      }
   }

   private static class FilteredSet<E> extends Collections2.FilteredCollection<E> implements Set<E> {
      FilteredSet(Set<E> var1, Predicate<? super E> var2) {
         super(var1, var2);
      }

      public boolean equals(@Nullable Object var1) {
         return Sets.equalsImpl(this, var1);
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }
   }

   public abstract static class SetView<E> extends AbstractSet<E> {
      private SetView() {
      }

      public ImmutableSet<E> immutableCopy() {
         return ImmutableSet.copyOf((Collection)this);
      }

      public <S extends Set<E>> S copyInto(S var1) {
         var1.addAll(this);
         return var1;
      }

      // $FF: synthetic method
      SetView(Object var1) {
         this();
      }
   }

   abstract static class ImprovedAbstractSet<E> extends AbstractSet<E> {
      ImprovedAbstractSet() {
      }

      public boolean removeAll(Collection<?> var1) {
         return Sets.removeAllImpl(this, (Collection)var1);
      }

      public boolean retainAll(Collection<?> var1) {
         return super.retainAll((Collection)Preconditions.checkNotNull(var1));
      }
   }
}
