package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableListIterator;
import java.util.ListIterator;

@GwtCompatible(
   emulated = true
)
class RegularImmutableAsList<E> extends ImmutableAsList<E> {
   private final ImmutableCollection<E> delegate;
   private final ImmutableList<? extends E> delegateList;

   RegularImmutableAsList(ImmutableCollection<E> var1, ImmutableList<? extends E> var2) {
      this.delegate = var1;
      this.delegateList = var2;
   }

   RegularImmutableAsList(ImmutableCollection<E> var1, Object[] var2) {
      this(var1, ImmutableList.asImmutableList(var2));
   }

   ImmutableCollection<E> delegateCollection() {
      return this.delegate;
   }

   ImmutableList<? extends E> delegateList() {
      return this.delegateList;
   }

   public UnmodifiableListIterator<E> listIterator(int var1) {
      return this.delegateList.listIterator(var1);
   }

   @GwtIncompatible("not present in emulated superclass")
   int copyIntoArray(Object[] var1, int var2) {
      return this.delegateList.copyIntoArray(var1, var2);
   }

   public E get(int var1) {
      return this.delegateList.get(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ListIterator listIterator(int var1) {
      return this.listIterator(var1);
   }
}
