package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
final class NullsFirstOrdering<T> extends Ordering<T> implements Serializable {
   final Ordering<? super T> ordering;
   private static final long serialVersionUID = 0L;

   NullsFirstOrdering(Ordering<? super T> var1) {
      this.ordering = var1;
   }

   public int compare(@Nullable T var1, @Nullable T var2) {
      return var1 == var2?0:(var1 == null?-1:(var2 == null?1:this.ordering.compare(var1, var2)));
   }

   public <S extends T> Ordering<S> reverse() {
      return this.ordering.reverse().nullsLast();
   }

   public <S extends T> Ordering<S> nullsFirst() {
      return this;
   }

   public <S extends T> Ordering<S> nullsLast() {
      return this.ordering.nullsLast();
   }

   public boolean equals(@Nullable Object var1) {
      if(var1 == this) {
         return true;
      } else if(var1 instanceof NullsFirstOrdering) {
         NullsFirstOrdering var2 = (NullsFirstOrdering)var1;
         return this.ordering.equals(var2.ordering);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.ordering.hashCode() ^ 957692532;
   }

   public String toString() {
      return this.ordering + ".nullsFirst()";
   }
}
