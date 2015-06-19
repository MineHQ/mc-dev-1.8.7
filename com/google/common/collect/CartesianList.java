package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.math.IntMath;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible
final class CartesianList<E> extends AbstractList<List<E>> implements RandomAccess {
   private final transient ImmutableList<List<E>> axes;
   private final transient int[] axesSizeProduct;

   static <E> List<List<E>> create(List<? extends List<? extends E>> var0) {
      ImmutableList.Builder var1 = new ImmutableList.Builder(var0.size());
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         List var3 = (List)var2.next();
         ImmutableList var4 = ImmutableList.copyOf((Collection)var3);
         if(var4.isEmpty()) {
            return ImmutableList.of();
         }

         var1.add((Object)var4);
      }

      return new CartesianList(var1.build());
   }

   CartesianList(ImmutableList<List<E>> var1) {
      this.axes = var1;
      int[] var2 = new int[var1.size() + 1];
      var2[var1.size()] = 1;

      try {
         for(int var3 = var1.size() - 1; var3 >= 0; --var3) {
            var2[var3] = IntMath.checkedMultiply(var2[var3 + 1], ((List)var1.get(var3)).size());
         }
      } catch (ArithmeticException var4) {
         throw new IllegalArgumentException("Cartesian product too large; must have size at most Integer.MAX_VALUE");
      }

      this.axesSizeProduct = var2;
   }

   private int getAxisIndexForProductIndex(int var1, int var2) {
      return var1 / this.axesSizeProduct[var2 + 1] % ((List)this.axes.get(var2)).size();
   }

   public ImmutableList<E> get(final int var1) {
      Preconditions.checkElementIndex(var1, this.size());
      return new ImmutableList() {
         public int size() {
            return CartesianList.this.axes.size();
         }

         public E get(int var1x) {
            Preconditions.checkElementIndex(var1x, this.size());
            int var2 = CartesianList.this.getAxisIndexForProductIndex(var1, var1x);
            return ((List)CartesianList.this.axes.get(var1x)).get(var2);
         }

         boolean isPartialView() {
            return true;
         }
      };
   }

   public int size() {
      return this.axesSizeProduct[0];
   }

   public boolean contains(@Nullable Object var1) {
      if(!(var1 instanceof List)) {
         return false;
      } else {
         List var2 = (List)var1;
         if(var2.size() != this.axes.size()) {
            return false;
         } else {
            ListIterator var3 = var2.listIterator();

            int var4;
            do {
               if(!var3.hasNext()) {
                  return true;
               }

               var4 = var3.nextIndex();
            } while(((List)this.axes.get(var4)).contains(var3.next()));

            return false;
         }
      }
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object get(int var1) {
      return this.get(var1);
   }
}
