package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.BoundType;
import com.google.common.collect.ForwardingMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.Ordering;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.SortedMultisets;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

@GwtCompatible(
   emulated = true
)
abstract class DescendingMultiset<E> extends ForwardingMultiset<E> implements SortedMultiset<E> {
   private transient Comparator<? super E> comparator;
   private transient NavigableSet<E> elementSet;
   private transient Set<Multiset.Entry<E>> entrySet;

   DescendingMultiset() {
   }

   abstract SortedMultiset<E> forwardMultiset();

   public Comparator<? super E> comparator() {
      Comparator var1 = this.comparator;
      return var1 == null?(this.comparator = Ordering.from(this.forwardMultiset().comparator()).reverse()):var1;
   }

   public NavigableSet<E> elementSet() {
      NavigableSet var1 = this.elementSet;
      return var1 == null?(this.elementSet = new SortedMultisets.NavigableElementSet(this)):var1;
   }

   public Multiset.Entry<E> pollFirstEntry() {
      return this.forwardMultiset().pollLastEntry();
   }

   public Multiset.Entry<E> pollLastEntry() {
      return this.forwardMultiset().pollFirstEntry();
   }

   public SortedMultiset<E> headMultiset(E var1, BoundType var2) {
      return this.forwardMultiset().tailMultiset(var1, var2).descendingMultiset();
   }

   public SortedMultiset<E> subMultiset(E var1, BoundType var2, E var3, BoundType var4) {
      return this.forwardMultiset().subMultiset(var3, var4, var1, var2).descendingMultiset();
   }

   public SortedMultiset<E> tailMultiset(E var1, BoundType var2) {
      return this.forwardMultiset().headMultiset(var1, var2).descendingMultiset();
   }

   protected Multiset<E> delegate() {
      return this.forwardMultiset();
   }

   public SortedMultiset<E> descendingMultiset() {
      return this.forwardMultiset();
   }

   public Multiset.Entry<E> firstEntry() {
      return this.forwardMultiset().lastEntry();
   }

   public Multiset.Entry<E> lastEntry() {
      return this.forwardMultiset().firstEntry();
   }

   abstract Iterator<Multiset.Entry<E>> entryIterator();

   public Set<Multiset.Entry<E>> entrySet() {
      Set var1 = this.entrySet;
      return var1 == null?(this.entrySet = this.createEntrySet()):var1;
   }

   Set<Multiset.Entry<E>> createEntrySet() {
      return new Multisets.EntrySet() {
         Multiset<E> multiset() {
            return DescendingMultiset.this;
         }

         public Iterator<Multiset.Entry<E>> iterator() {
            return DescendingMultiset.this.entryIterator();
         }

         public int size() {
            return DescendingMultiset.this.forwardMultiset().entrySet().size();
         }
      };
   }

   public Iterator<E> iterator() {
      return Multisets.iteratorImpl(this);
   }

   public Object[] toArray() {
      return this.standardToArray();
   }

   public <T> T[] toArray(T[] var1) {
      return this.standardToArray(var1);
   }

   public String toString() {
      return this.entrySet().toString();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set elementSet() {
      return this.elementSet();
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

   // $FF: synthetic method
   // $FF: bridge method
   public SortedSet elementSet() {
      return this.elementSet();
   }
}
