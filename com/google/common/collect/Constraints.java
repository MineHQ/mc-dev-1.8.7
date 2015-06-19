package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Constraint;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.ForwardingListIterator;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ForwardingSortedSet;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedSet;

@GwtCompatible
final class Constraints {
   private Constraints() {
   }

   public static <E> Collection<E> constrainedCollection(Collection<E> var0, Constraint<? super E> var1) {
      return new Constraints.ConstrainedCollection(var0, var1);
   }

   public static <E> Set<E> constrainedSet(Set<E> var0, Constraint<? super E> var1) {
      return new Constraints.ConstrainedSet(var0, var1);
   }

   public static <E> SortedSet<E> constrainedSortedSet(SortedSet<E> var0, Constraint<? super E> var1) {
      return new Constraints.ConstrainedSortedSet(var0, var1);
   }

   public static <E> List<E> constrainedList(List<E> var0, Constraint<? super E> var1) {
      return (List)(var0 instanceof RandomAccess?new Constraints.ConstrainedRandomAccessList(var0, var1):new Constraints.ConstrainedList(var0, var1));
   }

   private static <E> ListIterator<E> constrainedListIterator(ListIterator<E> var0, Constraint<? super E> var1) {
      return new Constraints.ConstrainedListIterator(var0, var1);
   }

   static <E> Collection<E> constrainedTypePreservingCollection(Collection<E> var0, Constraint<E> var1) {
      return (Collection)(var0 instanceof SortedSet?constrainedSortedSet((SortedSet)var0, var1):(var0 instanceof Set?constrainedSet((Set)var0, var1):(var0 instanceof List?constrainedList((List)var0, var1):constrainedCollection(var0, var1))));
   }

   private static <E> Collection<E> checkElements(Collection<E> var0, Constraint<? super E> var1) {
      ArrayList var2 = Lists.newArrayList((Iterable)var0);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         var1.checkElement(var4);
      }

      return var2;
   }

   static class ConstrainedListIterator<E> extends ForwardingListIterator<E> {
      private final ListIterator<E> delegate;
      private final Constraint<? super E> constraint;

      public ConstrainedListIterator(ListIterator<E> var1, Constraint<? super E> var2) {
         this.delegate = var1;
         this.constraint = var2;
      }

      protected ListIterator<E> delegate() {
         return this.delegate;
      }

      public void add(E var1) {
         this.constraint.checkElement(var1);
         this.delegate.add(var1);
      }

      public void set(E var1) {
         this.constraint.checkElement(var1);
         this.delegate.set(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Iterator delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   static class ConstrainedRandomAccessList<E> extends Constraints.ConstrainedList<E> implements RandomAccess {
      ConstrainedRandomAccessList(List<E> var1, Constraint<? super E> var2) {
         super(var1, var2);
      }
   }

   @GwtCompatible
   private static class ConstrainedList<E> extends ForwardingList<E> {
      final List<E> delegate;
      final Constraint<? super E> constraint;

      ConstrainedList(List<E> var1, Constraint<? super E> var2) {
         this.delegate = (List)Preconditions.checkNotNull(var1);
         this.constraint = (Constraint)Preconditions.checkNotNull(var2);
      }

      protected List<E> delegate() {
         return this.delegate;
      }

      public boolean add(E var1) {
         this.constraint.checkElement(var1);
         return this.delegate.add(var1);
      }

      public void add(int var1, E var2) {
         this.constraint.checkElement(var2);
         this.delegate.add(var1, var2);
      }

      public boolean addAll(Collection<? extends E> var1) {
         return this.delegate.addAll(Constraints.checkElements(var1, this.constraint));
      }

      public boolean addAll(int var1, Collection<? extends E> var2) {
         return this.delegate.addAll(var1, Constraints.checkElements(var2, this.constraint));
      }

      public ListIterator<E> listIterator() {
         return Constraints.constrainedListIterator(this.delegate.listIterator(), this.constraint);
      }

      public ListIterator<E> listIterator(int var1) {
         return Constraints.constrainedListIterator(this.delegate.listIterator(var1), this.constraint);
      }

      public E set(int var1, E var2) {
         this.constraint.checkElement(var2);
         return this.delegate.set(var1, var2);
      }

      public List<E> subList(int var1, int var2) {
         return Constraints.constrainedList(this.delegate.subList(var1, var2), this.constraint);
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

   private static class ConstrainedSortedSet<E> extends ForwardingSortedSet<E> {
      final SortedSet<E> delegate;
      final Constraint<? super E> constraint;

      ConstrainedSortedSet(SortedSet<E> var1, Constraint<? super E> var2) {
         this.delegate = (SortedSet)Preconditions.checkNotNull(var1);
         this.constraint = (Constraint)Preconditions.checkNotNull(var2);
      }

      protected SortedSet<E> delegate() {
         return this.delegate;
      }

      public SortedSet<E> headSet(E var1) {
         return Constraints.constrainedSortedSet(this.delegate.headSet(var1), this.constraint);
      }

      public SortedSet<E> subSet(E var1, E var2) {
         return Constraints.constrainedSortedSet(this.delegate.subSet(var1, var2), this.constraint);
      }

      public SortedSet<E> tailSet(E var1) {
         return Constraints.constrainedSortedSet(this.delegate.tailSet(var1), this.constraint);
      }

      public boolean add(E var1) {
         this.constraint.checkElement(var1);
         return this.delegate.add(var1);
      }

      public boolean addAll(Collection<? extends E> var1) {
         return this.delegate.addAll(Constraints.checkElements(var1, this.constraint));
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

   static class ConstrainedSet<E> extends ForwardingSet<E> {
      private final Set<E> delegate;
      private final Constraint<? super E> constraint;

      public ConstrainedSet(Set<E> var1, Constraint<? super E> var2) {
         this.delegate = (Set)Preconditions.checkNotNull(var1);
         this.constraint = (Constraint)Preconditions.checkNotNull(var2);
      }

      protected Set<E> delegate() {
         return this.delegate;
      }

      public boolean add(E var1) {
         this.constraint.checkElement(var1);
         return this.delegate.add(var1);
      }

      public boolean addAll(Collection<? extends E> var1) {
         return this.delegate.addAll(Constraints.checkElements(var1, this.constraint));
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

   static class ConstrainedCollection<E> extends ForwardingCollection<E> {
      private final Collection<E> delegate;
      private final Constraint<? super E> constraint;

      public ConstrainedCollection(Collection<E> var1, Constraint<? super E> var2) {
         this.delegate = (Collection)Preconditions.checkNotNull(var1);
         this.constraint = (Constraint)Preconditions.checkNotNull(var2);
      }

      protected Collection<E> delegate() {
         return this.delegate;
      }

      public boolean add(E var1) {
         this.constraint.checkElement(var1);
         return this.delegate.add(var1);
      }

      public boolean addAll(Collection<? extends E> var1) {
         return this.delegate.addAll(Constraints.checkElements(var1, this.constraint));
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }
}
