package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
final class PairwiseEquivalence<T> extends Equivalence<Iterable<T>> implements Serializable {
   final Equivalence<? super T> elementEquivalence;
   private static final long serialVersionUID = 1L;

   PairwiseEquivalence(Equivalence<? super T> var1) {
      this.elementEquivalence = (Equivalence)Preconditions.checkNotNull(var1);
   }

   protected boolean doEquivalent(Iterable<T> var1, Iterable<T> var2) {
      Iterator var3 = var1.iterator();
      Iterator var4 = var2.iterator();

      while(var3.hasNext() && var4.hasNext()) {
         if(!this.elementEquivalence.equivalent(var3.next(), var4.next())) {
            return false;
         }
      }

      return !var3.hasNext() && !var4.hasNext();
   }

   protected int doHash(Iterable<T> var1) {
      int var2 = 78721;

      Object var4;
      for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 = var2 * 24943 + this.elementEquivalence.hash(var4)) {
         var4 = var3.next();
      }

      return var2;
   }

   public boolean equals(@Nullable Object var1) {
      if(var1 instanceof PairwiseEquivalence) {
         PairwiseEquivalence var2 = (PairwiseEquivalence)var1;
         return this.elementEquivalence.equals(var2.elementEquivalence);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.elementEquivalence.hashCode() ^ 1185147655;
   }

   public String toString() {
      return this.elementEquivalence + ".pairwise()";
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected int doHash(Object var1) {
      return this.doHash((Iterable)var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected boolean doEquivalent(Object var1, Object var2) {
      return this.doEquivalent((Iterable)var1, (Iterable)var2);
   }
}
