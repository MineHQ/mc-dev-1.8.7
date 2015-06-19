package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
final class AllEqualOrdering extends Ordering<Object> implements Serializable {
   static final AllEqualOrdering INSTANCE = new AllEqualOrdering();
   private static final long serialVersionUID = 0L;

   AllEqualOrdering() {
   }

   public int compare(@Nullable Object var1, @Nullable Object var2) {
      return 0;
   }

   public <E> List<E> sortedCopy(Iterable<E> var1) {
      return Lists.newArrayList(var1);
   }

   public <E> ImmutableList<E> immutableSortedCopy(Iterable<E> var1) {
      return ImmutableList.copyOf(var1);
   }

   public <S> Ordering<S> reverse() {
      return this;
   }

   private Object readResolve() {
      return INSTANCE;
   }

   public String toString() {
      return "Ordering.allEqual()";
   }
}
