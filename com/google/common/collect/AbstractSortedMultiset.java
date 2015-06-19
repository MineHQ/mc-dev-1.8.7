package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMultiset;
import com.google.common.collect.BoundType;
import com.google.common.collect.DescendingMultiset;
import com.google.common.collect.GwtTransient;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.Ordering;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.SortedMultisets;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
abstract class AbstractSortedMultiset<E> extends AbstractMultiset<E> implements SortedMultiset<E> {
   @GwtTransient
   final Comparator<? super E> comparator;
   private transient SortedMultiset<E> descendingMultiset;

   AbstractSortedMultiset() {
      this(Ordering.natural());
   }

   AbstractSortedMultiset(Comparator<? super E> var1) {
      this.comparator = (Comparator)Preconditions.checkNotNull(var1);
   }

   public NavigableSet<E> elementSet() {
      return (NavigableSet)super.elementSet();
   }

   NavigableSet<E> createElementSet() {
      return new SortedMultisets.NavigableElementSet(this);
   }

   public Comparator<? super E> comparator() {
      return this.comparator;
   }

   public Multiset.Entry<E> firstEntry() {
      Iterator var1 = this.entryIterator();
      return var1.hasNext()?(Multiset.Entry)var1.next():null;
   }

   public Multiset.Entry<E> lastEntry() {
      Iterator var1 = this.descendingEntryIterator();
      return var1.hasNext()?(Multiset.Entry)var1.next():null;
   }

   public Multiset.Entry<E> pollFirstEntry() {
      Iterator var1 = this.entryIterator();
      if(var1.hasNext()) {
         Multiset.Entry var2 = (Multiset.Entry)var1.next();
         var2 = Multisets.immutableEntry(var2.getElement(), var2.getCount());
         var1.remove();
         return var2;
      } else {
         return null;
      }
   }

   public Multiset.Entry<E> pollLastEntry() {
      Iterator var1 = this.descendingEntryIterator();
      if(var1.hasNext()) {
         Multiset.Entry var2 = (Multiset.Entry)var1.next();
         var2 = Multisets.immutableEntry(var2.getElement(), var2.getCount());
         var1.remove();
         return var2;
      } else {
         return null;
      }
   }

   public SortedMultiset<E> subMultiset(@Nullable E var1, BoundType var2, @Nullable E var3, BoundType var4) {
      Preconditions.checkNotNull(var2);
      Preconditions.checkNotNull(var4);
      return this.tailMultiset(var1, var2).headMultiset(var3, var4);
   }

   abstract Iterator<Multiset.Entry<E>> descendingEntryIterator();

   Iterator<E> descendingIterator() {
      return Multisets.iteratorImpl(this.descendingMultiset());
   }

   public SortedMultiset<E> descendingMultiset() {
      SortedMultiset var1 = this.descendingMultiset;
      return var1 == null?(this.descendingMultiset = this.createDescendingMultiset()):var1;
   }

   SortedMultiset<E> createDescendingMultiset() {
      return new DescendingMultiset() {
         SortedMultiset<E> forwardMultiset() {
            return AbstractSortedMultiset.this;
         }

         Iterator<Multiset.Entry<E>> entryIterator() {
            return AbstractSortedMultiset.this.descendingEntryIterator();
         }

         public Iterator<E> iterator() {
            return AbstractSortedMultiset.this.descendingIterator();
         }
      };
   }

   // $FF: synthetic method
   // $FF: bridge method
   Set createElementSet() {
      return this.createElementSet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set elementSet() {
      return this.elementSet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SortedSet elementSet() {
      return this.elementSet();
   }
}
