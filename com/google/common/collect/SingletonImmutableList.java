package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
final class SingletonImmutableList<E> extends ImmutableList<E> {
   final transient E element;

   SingletonImmutableList(E var1) {
      this.element = Preconditions.checkNotNull(var1);
   }

   public E get(int var1) {
      Preconditions.checkElementIndex(var1, 1);
      return this.element;
   }

   public int indexOf(@Nullable Object var1) {
      return this.element.equals(var1)?0:-1;
   }

   public UnmodifiableIterator<E> iterator() {
      return Iterators.singletonIterator(this.element);
   }

   public int lastIndexOf(@Nullable Object var1) {
      return this.indexOf(var1);
   }

   public int size() {
      return 1;
   }

   public ImmutableList<E> subList(int var1, int var2) {
      Preconditions.checkPositionIndexes(var1, var2, 1);
      return (ImmutableList)(var1 == var2?ImmutableList.of():this);
   }

   public ImmutableList<E> reverse() {
      return this;
   }

   public boolean contains(@Nullable Object var1) {
      return this.element.equals(var1);
   }

   public boolean equals(@Nullable Object var1) {
      if(var1 == this) {
         return true;
      } else if(!(var1 instanceof List)) {
         return false;
      } else {
         List var2 = (List)var1;
         return var2.size() == 1 && this.element.equals(var2.get(0));
      }
   }

   public int hashCode() {
      return 31 + this.element.hashCode();
   }

   public String toString() {
      String var1 = this.element.toString();
      return (new StringBuilder(var1.length() + 2)).append('[').append(var1).append(']').toString();
   }

   public boolean isEmpty() {
      return false;
   }

   boolean isPartialView() {
      return false;
   }

   int copyIntoArray(Object[] var1, int var2) {
      var1[var2] = this.element;
      return var2 + 1;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public List subList(int var1, int var2) {
      return this.subList(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator iterator() {
      return this.iterator();
   }
}
