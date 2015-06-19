package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Hashing;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.RegularImmutableAsList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;

@GwtCompatible(
   serializable = true,
   emulated = true
)
final class RegularImmutableSet<E> extends ImmutableSet<E> {
   private final Object[] elements;
   @VisibleForTesting
   final transient Object[] table;
   private final transient int mask;
   private final transient int hashCode;

   RegularImmutableSet(Object[] var1, int var2, Object[] var3, int var4) {
      this.elements = var1;
      this.table = var3;
      this.mask = var4;
      this.hashCode = var2;
   }

   public boolean contains(Object var1) {
      if(var1 == null) {
         return false;
      } else {
         int var2 = Hashing.smear(var1.hashCode());

         while(true) {
            Object var3 = this.table[var2 & this.mask];
            if(var3 == null) {
               return false;
            }

            if(var3.equals(var1)) {
               return true;
            }

            ++var2;
         }
      }
   }

   public int size() {
      return this.elements.length;
   }

   public UnmodifiableIterator<E> iterator() {
      return Iterators.forArray(this.elements);
   }

   int copyIntoArray(Object[] var1, int var2) {
      System.arraycopy(this.elements, 0, var1, var2, this.elements.length);
      return var2 + this.elements.length;
   }

   ImmutableList<E> createAsList() {
      return new RegularImmutableAsList(this, this.elements);
   }

   boolean isPartialView() {
      return false;
   }

   public int hashCode() {
      return this.hashCode;
   }

   boolean isHashCodeFast() {
      return true;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator iterator() {
      return this.iterator();
   }
}
