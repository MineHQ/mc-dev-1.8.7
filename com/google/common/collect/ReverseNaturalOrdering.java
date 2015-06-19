package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.NaturalOrdering;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.Iterator;

@GwtCompatible(
   serializable = true
)
final class ReverseNaturalOrdering extends Ordering<Comparable> implements Serializable {
   static final ReverseNaturalOrdering INSTANCE = new ReverseNaturalOrdering();
   private static final long serialVersionUID = 0L;

   public int compare(Comparable var1, Comparable var2) {
      Preconditions.checkNotNull(var1);
      return var1 == var2?0:var2.compareTo(var1);
   }

   public <S extends Comparable> Ordering<S> reverse() {
      return Ordering.natural();
   }

   public <E extends Comparable> E min(E var1, E var2) {
      return (Comparable)NaturalOrdering.INSTANCE.max(var1, var2);
   }

   public <E extends Comparable> E min(E var1, E var2, E var3, E... var4) {
      return (Comparable)NaturalOrdering.INSTANCE.max(var1, var2, var3, var4);
   }

   public <E extends Comparable> E min(Iterator<E> var1) {
      return (Comparable)NaturalOrdering.INSTANCE.max(var1);
   }

   public <E extends Comparable> E min(Iterable<E> var1) {
      return (Comparable)NaturalOrdering.INSTANCE.max(var1);
   }

   public <E extends Comparable> E max(E var1, E var2) {
      return (Comparable)NaturalOrdering.INSTANCE.min(var1, var2);
   }

   public <E extends Comparable> E max(E var1, E var2, E var3, E... var4) {
      return (Comparable)NaturalOrdering.INSTANCE.min(var1, var2, var3, var4);
   }

   public <E extends Comparable> E max(Iterator<E> var1) {
      return (Comparable)NaturalOrdering.INSTANCE.min(var1);
   }

   public <E extends Comparable> E max(Iterable<E> var1) {
      return (Comparable)NaturalOrdering.INSTANCE.min(var1);
   }

   private Object readResolve() {
      return INSTANCE;
   }

   public String toString() {
      return "Ordering.natural().reverse()";
   }

   private ReverseNaturalOrdering() {
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object max(Object var1, Object var2, Object var3, Object[] var4) {
      return this.max((Comparable)var1, (Comparable)var2, (Comparable)var3, (Comparable[])var4);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object max(Object var1, Object var2) {
      return this.max((Comparable)var1, (Comparable)var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object max(Iterable var1) {
      return this.max(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object max(Iterator var1) {
      return this.max(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object min(Object var1, Object var2, Object var3, Object[] var4) {
      return this.min((Comparable)var1, (Comparable)var2, (Comparable)var3, (Comparable[])var4);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object min(Object var1, Object var2) {
      return this.min((Comparable)var1, (Comparable)var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object min(Iterable var1) {
      return this.min(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object min(Iterator var1) {
      return this.min(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int compare(Object var1, Object var2) {
      return this.compare((Comparable)var1, (Comparable)var2);
   }
}
