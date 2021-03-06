package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.Comparator;

@GwtCompatible(
   serializable = true
)
final class CompoundOrdering<T> extends Ordering<T> implements Serializable {
   final ImmutableList<Comparator<? super T>> comparators;
   private static final long serialVersionUID = 0L;

   CompoundOrdering(Comparator<? super T> var1, Comparator<? super T> var2) {
      this.comparators = ImmutableList.of(var1, var2);
   }

   CompoundOrdering(Iterable<? extends Comparator<? super T>> var1) {
      this.comparators = ImmutableList.copyOf(var1);
   }

   public int compare(T var1, T var2) {
      int var3 = this.comparators.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = ((Comparator)this.comparators.get(var4)).compare(var1, var2);
         if(var5 != 0) {
            return var5;
         }
      }

      return 0;
   }

   public boolean equals(Object var1) {
      if(var1 == this) {
         return true;
      } else if(var1 instanceof CompoundOrdering) {
         CompoundOrdering var2 = (CompoundOrdering)var1;
         return this.comparators.equals(var2.comparators);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.comparators.hashCode();
   }

   public String toString() {
      return "Ordering.compound(" + this.comparators + ")";
   }
}
