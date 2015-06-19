package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.BoundType;
import com.google.common.collect.DescendingMultiset;
import com.google.common.collect.ForwardingMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.SortedMultisets;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

@Beta
@GwtCompatible(
   emulated = true
)
public abstract class ForwardingSortedMultiset<E> extends ForwardingMultiset<E> implements SortedMultiset<E> {
   protected ForwardingSortedMultiset() {
   }

   protected abstract SortedMultiset<E> delegate();

   public NavigableSet<E> elementSet() {
      return (NavigableSet)super.elementSet();
   }

   public Comparator<? super E> comparator() {
      return this.delegate().comparator();
   }

   public SortedMultiset<E> descendingMultiset() {
      return this.delegate().descendingMultiset();
   }

   public Multiset.Entry<E> firstEntry() {
      return this.delegate().firstEntry();
   }

   protected Multiset.Entry<E> standardFirstEntry() {
      Iterator var1 = this.entrySet().iterator();
      if(!var1.hasNext()) {
         return null;
      } else {
         Multiset.Entry var2 = (Multiset.Entry)var1.next();
         return Multisets.immutableEntry(var2.getElement(), var2.getCount());
      }
   }

   public Multiset.Entry<E> lastEntry() {
      return this.delegate().lastEntry();
   }

   protected Multiset.Entry<E> standardLastEntry() {
      Iterator var1 = this.descendingMultiset().entrySet().iterator();
      if(!var1.hasNext()) {
         return null;
      } else {
         Multiset.Entry var2 = (Multiset.Entry)var1.next();
         return Multisets.immutableEntry(var2.getElement(), var2.getCount());
      }
   }

   public Multiset.Entry<E> pollFirstEntry() {
      return this.delegate().pollFirstEntry();
   }

   protected Multiset.Entry<E> standardPollFirstEntry() {
      Iterator var1 = this.entrySet().iterator();
      if(!var1.hasNext()) {
         return null;
      } else {
         Multiset.Entry var2 = (Multiset.Entry)var1.next();
         var2 = Multisets.immutableEntry(var2.getElement(), var2.getCount());
         var1.remove();
         return var2;
      }
   }

   public Multiset.Entry<E> pollLastEntry() {
      return this.delegate().pollLastEntry();
   }

   protected Multiset.Entry<E> standardPollLastEntry() {
      Iterator var1 = this.descendingMultiset().entrySet().iterator();
      if(!var1.hasNext()) {
         return null;
      } else {
         Multiset.Entry var2 = (Multiset.Entry)var1.next();
         var2 = Multisets.immutableEntry(var2.getElement(), var2.getCount());
         var1.remove();
         return var2;
      }
   }

   public SortedMultiset<E> headMultiset(E var1, BoundType var2) {
      return this.delegate().headMultiset(var1, var2);
   }

   public SortedMultiset<E> subMultiset(E var1, BoundType var2, E var3, BoundType var4) {
      return this.delegate().subMultiset(var1, var2, var3, var4);
   }

   protected SortedMultiset<E> standardSubMultiset(E var1, BoundType var2, E var3, BoundType var4) {
      return this.tailMultiset(var1, var2).headMultiset(var3, var4);
   }

   public SortedMultiset<E> tailMultiset(E var1, BoundType var2) {
      return this.delegate().tailMultiset(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set elementSet() {
      return this.elementSet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Multiset delegate() {
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

   // $FF: synthetic method
   // $FF: bridge method
   public SortedSet elementSet() {
      return this.elementSet();
   }

   protected abstract class StandardDescendingMultiset extends DescendingMultiset<E> {
      public StandardDescendingMultiset() {
      }

      SortedMultiset<E> forwardMultiset() {
         return ForwardingSortedMultiset.this;
      }
   }

   protected class StandardElementSet extends SortedMultisets.NavigableElementSet<E> {
      public StandardElementSet() {
         super(ForwardingSortedMultiset.this);
      }
   }
}
