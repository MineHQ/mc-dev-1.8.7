package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIndexedListIterator;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.RegularImmutableList;
import com.google.common.collect.SingletonImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.UnmodifiableListIterator;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess {
   private static final ImmutableList<Object> EMPTY;

   public static <E> ImmutableList<E> of() {
      return EMPTY;
   }

   public static <E> ImmutableList<E> of(E var0) {
      return new SingletonImmutableList(var0);
   }

   public static <E> ImmutableList<E> of(E var0, E var1) {
      return construct(new Object[]{var0, var1});
   }

   public static <E> ImmutableList<E> of(E var0, E var1, E var2) {
      return construct(new Object[]{var0, var1, var2});
   }

   public static <E> ImmutableList<E> of(E var0, E var1, E var2, E var3) {
      return construct(new Object[]{var0, var1, var2, var3});
   }

   public static <E> ImmutableList<E> of(E var0, E var1, E var2, E var3, E var4) {
      return construct(new Object[]{var0, var1, var2, var3, var4});
   }

   public static <E> ImmutableList<E> of(E var0, E var1, E var2, E var3, E var4, E var5) {
      return construct(new Object[]{var0, var1, var2, var3, var4, var5});
   }

   public static <E> ImmutableList<E> of(E var0, E var1, E var2, E var3, E var4, E var5, E var6) {
      return construct(new Object[]{var0, var1, var2, var3, var4, var5, var6});
   }

   public static <E> ImmutableList<E> of(E var0, E var1, E var2, E var3, E var4, E var5, E var6, E var7) {
      return construct(new Object[]{var0, var1, var2, var3, var4, var5, var6, var7});
   }

   public static <E> ImmutableList<E> of(E var0, E var1, E var2, E var3, E var4, E var5, E var6, E var7, E var8) {
      return construct(new Object[]{var0, var1, var2, var3, var4, var5, var6, var7, var8});
   }

   public static <E> ImmutableList<E> of(E var0, E var1, E var2, E var3, E var4, E var5, E var6, E var7, E var8, E var9) {
      return construct(new Object[]{var0, var1, var2, var3, var4, var5, var6, var7, var8, var9});
   }

   public static <E> ImmutableList<E> of(E var0, E var1, E var2, E var3, E var4, E var5, E var6, E var7, E var8, E var9, E var10) {
      return construct(new Object[]{var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10});
   }

   public static <E> ImmutableList<E> of(E var0, E var1, E var2, E var3, E var4, E var5, E var6, E var7, E var8, E var9, E var10, E var11, E... var12) {
      Object[] var13 = new Object[12 + var12.length];
      var13[0] = var0;
      var13[1] = var1;
      var13[2] = var2;
      var13[3] = var3;
      var13[4] = var4;
      var13[5] = var5;
      var13[6] = var6;
      var13[7] = var7;
      var13[8] = var8;
      var13[9] = var9;
      var13[10] = var10;
      var13[11] = var11;
      System.arraycopy(var12, 0, var13, 12, var12.length);
      return construct(var13);
   }

   public static <E> ImmutableList<E> copyOf(Iterable<? extends E> var0) {
      Preconditions.checkNotNull(var0);
      return var0 instanceof Collection?copyOf(Collections2.cast(var0)):copyOf(var0.iterator());
   }

   public static <E> ImmutableList<E> copyOf(Collection<? extends E> var0) {
      if(var0 instanceof ImmutableCollection) {
         ImmutableList var1 = ((ImmutableCollection)var0).asList();
         return var1.isPartialView()?asImmutableList(var1.toArray()):var1;
      } else {
         return construct(var0.toArray());
      }
   }

   public static <E> ImmutableList<E> copyOf(Iterator<? extends E> var0) {
      if(!var0.hasNext()) {
         return of();
      } else {
         Object var1 = var0.next();
         return !var0.hasNext()?of(var1):(new ImmutableList.Builder()).add(var1).addAll(var0).build();
      }
   }

   public static <E> ImmutableList<E> copyOf(E[] var0) {
      switch(var0.length) {
      case 0:
         return of();
      case 1:
         return new SingletonImmutableList(var0[0]);
      default:
         return new RegularImmutableList(ObjectArrays.checkElementsNotNull((Object[])var0.clone()));
      }
   }

   private static <E> ImmutableList<E> construct(Object... var0) {
      return asImmutableList(ObjectArrays.checkElementsNotNull(var0));
   }

   static <E> ImmutableList<E> asImmutableList(Object[] var0) {
      return asImmutableList(var0, var0.length);
   }

   static <E> ImmutableList<E> asImmutableList(Object[] var0, int var1) {
      switch(var1) {
      case 0:
         return of();
      case 1:
         SingletonImmutableList var2 = new SingletonImmutableList(var0[0]);
         return var2;
      default:
         if(var1 < var0.length) {
            var0 = ObjectArrays.arraysCopyOf(var0, var1);
         }

         return new RegularImmutableList(var0);
      }
   }

   ImmutableList() {
   }

   public UnmodifiableIterator<E> iterator() {
      return this.listIterator();
   }

   public UnmodifiableListIterator<E> listIterator() {
      return this.listIterator(0);
   }

   public UnmodifiableListIterator<E> listIterator(final int var1) {
      return new AbstractIndexedListIterator(this.size(), var1) {
         protected E get(int var1) {
            return ImmutableList.this.get(var1);
         }
      };
   }

   public int indexOf(@Nullable Object var1) {
      return var1 == null?-1:Lists.indexOfImpl(this, var1);
   }

   public int lastIndexOf(@Nullable Object var1) {
      return var1 == null?-1:Lists.lastIndexOfImpl(this, var1);
   }

   public boolean contains(@Nullable Object var1) {
      return this.indexOf(var1) >= 0;
   }

   public ImmutableList<E> subList(int var1, int var2) {
      Preconditions.checkPositionIndexes(var1, var2, this.size());
      int var3 = var2 - var1;
      switch(var3) {
      case 0:
         return of();
      case 1:
         return of(this.get(var1));
      default:
         return this.subListUnchecked(var1, var2);
      }
   }

   ImmutableList<E> subListUnchecked(int var1, int var2) {
      return new ImmutableList.SubList(var1, var2 - var1);
   }

   /** @deprecated */
   @Deprecated
   public final boolean addAll(int var1, Collection<? extends E> var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final E set(int var1, E var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final void add(int var1, E var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final E remove(int var1) {
      throw new UnsupportedOperationException();
   }

   public final ImmutableList<E> asList() {
      return this;
   }

   int copyIntoArray(Object[] var1, int var2) {
      int var3 = this.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         var1[var2 + var4] = this.get(var4);
      }

      return var2 + var3;
   }

   public ImmutableList<E> reverse() {
      return new ImmutableList.ReverseImmutableList(this);
   }

   public boolean equals(@Nullable Object var1) {
      return Lists.equalsImpl(this, var1);
   }

   public int hashCode() {
      int var1 = 1;
      int var2 = this.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         var1 = 31 * var1 + this.get(var3).hashCode();
         var1 = ~(~var1);
      }

      return var1;
   }

   private void readObject(ObjectInputStream var1) throws InvalidObjectException {
      throw new InvalidObjectException("Use SerializedForm");
   }

   Object writeReplace() {
      return new ImmutableList.SerializedForm(this.toArray());
   }

   public static <E> ImmutableList.Builder<E> builder() {
      return new ImmutableList.Builder();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator iterator() {
      return this.iterator();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public List subList(int var1, int var2) {
      return this.subList(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ListIterator listIterator(int var1) {
      return this.listIterator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ListIterator listIterator() {
      return this.listIterator();
   }

   static {
      EMPTY = new RegularImmutableList(ObjectArrays.EMPTY_ARRAY);
   }

   public static final class Builder<E> extends ImmutableCollection.ArrayBasedBuilder<E> {
      public Builder() {
         this(4);
      }

      Builder(int var1) {
         super(var1);
      }

      public ImmutableList.Builder<E> add(E var1) {
         super.add(var1);
         return this;
      }

      public ImmutableList.Builder<E> addAll(Iterable<? extends E> var1) {
         super.addAll(var1);
         return this;
      }

      public ImmutableList.Builder<E> add(E... var1) {
         super.add(var1);
         return this;
      }

      public ImmutableList.Builder<E> addAll(Iterator<? extends E> var1) {
         super.addAll(var1);
         return this;
      }

      public ImmutableList<E> build() {
         return ImmutableList.asImmutableList(this.contents, this.size);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableCollection.Builder addAll(Iterable var1) {
         return this.addAll(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableCollection.Builder add(Object[] var1) {
         return this.add(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableCollection.ArrayBasedBuilder add(Object var1) {
         return this.add(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableCollection build() {
         return this.build();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableCollection.Builder addAll(Iterator var1) {
         return this.addAll(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableCollection.Builder add(Object var1) {
         return this.add(var1);
      }
   }

   static class SerializedForm implements Serializable {
      final Object[] elements;
      private static final long serialVersionUID = 0L;

      SerializedForm(Object[] var1) {
         this.elements = var1;
      }

      Object readResolve() {
         return ImmutableList.copyOf(this.elements);
      }
   }

   private static class ReverseImmutableList<E> extends ImmutableList<E> {
      private final transient ImmutableList<E> forwardList;

      ReverseImmutableList(ImmutableList<E> var1) {
         this.forwardList = var1;
      }

      private int reverseIndex(int var1) {
         return this.size() - 1 - var1;
      }

      private int reversePosition(int var1) {
         return this.size() - var1;
      }

      public ImmutableList<E> reverse() {
         return this.forwardList;
      }

      public boolean contains(@Nullable Object var1) {
         return this.forwardList.contains(var1);
      }

      public int indexOf(@Nullable Object var1) {
         int var2 = this.forwardList.lastIndexOf(var1);
         return var2 >= 0?this.reverseIndex(var2):-1;
      }

      public int lastIndexOf(@Nullable Object var1) {
         int var2 = this.forwardList.indexOf(var1);
         return var2 >= 0?this.reverseIndex(var2):-1;
      }

      public ImmutableList<E> subList(int var1, int var2) {
         Preconditions.checkPositionIndexes(var1, var2, this.size());
         return this.forwardList.subList(this.reversePosition(var2), this.reversePosition(var1)).reverse();
      }

      public E get(int var1) {
         Preconditions.checkElementIndex(var1, this.size());
         return this.forwardList.get(this.reverseIndex(var1));
      }

      public int size() {
         return this.forwardList.size();
      }

      boolean isPartialView() {
         return this.forwardList.isPartialView();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public List subList(int var1, int var2) {
         return this.subList(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ListIterator listIterator(int var1) {
         return super.listIterator(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ListIterator listIterator() {
         return super.listIterator();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Iterator iterator() {
         return super.iterator();
      }
   }

   class SubList extends ImmutableList<E> {
      final transient int offset;
      final transient int length;

      SubList(int var2, int var3) {
         this.offset = var2;
         this.length = var3;
      }

      public int size() {
         return this.length;
      }

      public E get(int var1) {
         Preconditions.checkElementIndex(var1, this.length);
         return ImmutableList.this.get(var1 + this.offset);
      }

      public ImmutableList<E> subList(int var1, int var2) {
         Preconditions.checkPositionIndexes(var1, var2, this.length);
         return ImmutableList.this.subList(var1 + this.offset, var2 + this.offset);
      }

      boolean isPartialView() {
         return true;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public List subList(int var1, int var2) {
         return this.subList(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ListIterator listIterator(int var1) {
         return super.listIterator(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ListIterator listIterator() {
         return super.listIterator();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Iterator iterator() {
         return super.iterator();
      }
   }
}
