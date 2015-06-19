package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.RegularImmutableAsList;
import com.google.common.collect.RegularImmutableSortedSet;
import com.google.common.collect.SortedIterable;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class ImmutableSortedAsList<E> extends RegularImmutableAsList<E> implements SortedIterable<E> {
   ImmutableSortedAsList(ImmutableSortedSet<E> var1, ImmutableList<E> var2) {
      super(var1, (ImmutableList)var2);
   }

   ImmutableSortedSet<E> delegateCollection() {
      return (ImmutableSortedSet)super.delegateCollection();
   }

   public Comparator<? super E> comparator() {
      return this.delegateCollection().comparator();
   }

   @GwtIncompatible("ImmutableSortedSet.indexOf")
   public int indexOf(@Nullable Object var1) {
      int var2 = this.delegateCollection().indexOf(var1);
      return var2 >= 0 && this.get(var2).equals(var1)?var2:-1;
   }

   @GwtIncompatible("ImmutableSortedSet.indexOf")
   public int lastIndexOf(@Nullable Object var1) {
      return this.indexOf(var1);
   }

   public boolean contains(Object var1) {
      return this.indexOf(var1) >= 0;
   }

   @GwtIncompatible("super.subListUnchecked does not exist; inherited subList is valid if slow")
   ImmutableList<E> subListUnchecked(int var1, int var2) {
      return (new RegularImmutableSortedSet(super.subListUnchecked(var1, var2), this.comparator())).asList();
   }

   // $FF: synthetic method
   // $FF: bridge method
   ImmutableCollection delegateCollection() {
      return this.delegateCollection();
   }
}
