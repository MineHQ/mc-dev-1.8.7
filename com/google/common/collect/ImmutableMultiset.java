package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.RegularImmutableMultiset;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableMultiset<E> extends ImmutableCollection<E> implements Multiset<E> {
   private static final ImmutableMultiset<Object> EMPTY = new RegularImmutableMultiset(ImmutableMap.of(), 0);
   private transient ImmutableSet<Multiset.Entry<E>> entrySet;

   public static <E> ImmutableMultiset<E> of() {
      return EMPTY;
   }

   public static <E> ImmutableMultiset<E> of(E var0) {
      return copyOfInternal(new Object[]{var0});
   }

   public static <E> ImmutableMultiset<E> of(E var0, E var1) {
      return copyOfInternal(new Object[]{var0, var1});
   }

   public static <E> ImmutableMultiset<E> of(E var0, E var1, E var2) {
      return copyOfInternal(new Object[]{var0, var1, var2});
   }

   public static <E> ImmutableMultiset<E> of(E var0, E var1, E var2, E var3) {
      return copyOfInternal(new Object[]{var0, var1, var2, var3});
   }

   public static <E> ImmutableMultiset<E> of(E var0, E var1, E var2, E var3, E var4) {
      return copyOfInternal(new Object[]{var0, var1, var2, var3, var4});
   }

   public static <E> ImmutableMultiset<E> of(E var0, E var1, E var2, E var3, E var4, E var5, E... var6) {
      return (new ImmutableMultiset.Builder()).add(var0).add(var1).add(var2).add(var3).add(var4).add(var5).add(var6).build();
   }

   public static <E> ImmutableMultiset<E> copyOf(E[] var0) {
      return copyOf((Iterable)Arrays.asList(var0));
   }

   public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> var0) {
      if(var0 instanceof ImmutableMultiset) {
         ImmutableMultiset var1 = (ImmutableMultiset)var0;
         if(!var1.isPartialView()) {
            return var1;
         }
      }

      Object var2 = var0 instanceof Multiset?Multisets.cast(var0):LinkedHashMultiset.create(var0);
      return copyOfInternal((Multiset)var2);
   }

   private static <E> ImmutableMultiset<E> copyOfInternal(E... var0) {
      return copyOf((Iterable)Arrays.asList(var0));
   }

   private static <E> ImmutableMultiset<E> copyOfInternal(Multiset<? extends E> var0) {
      return copyFromEntries(var0.entrySet());
   }

   static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Multiset.Entry<? extends E>> var0) {
      long var1 = 0L;
      ImmutableMap.Builder var3 = ImmutableMap.builder();
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         Multiset.Entry var5 = (Multiset.Entry)var4.next();
         int var6 = var5.getCount();
         if(var6 > 0) {
            var3.put(var5.getElement(), Integer.valueOf(var6));
            var1 += (long)var6;
         }
      }

      if(var1 == 0L) {
         return of();
      } else {
         return new RegularImmutableMultiset(var3.build(), Ints.saturatedCast(var1));
      }
   }

   public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> var0) {
      LinkedHashMultiset var1 = LinkedHashMultiset.create();
      Iterators.addAll(var1, var0);
      return copyOfInternal((Multiset)var1);
   }

   ImmutableMultiset() {
   }

   public UnmodifiableIterator<E> iterator() {
      final UnmodifiableIterator var1 = this.entrySet().iterator();
      return new UnmodifiableIterator() {
         int remaining;
         E element;

         public boolean hasNext() {
            return this.remaining > 0 || var1.hasNext();
         }

         public E next() {
            if(this.remaining <= 0) {
               Multiset.Entry var1x = (Multiset.Entry)var1.next();
               this.element = var1x.getElement();
               this.remaining = var1x.getCount();
            }

            --this.remaining;
            return this.element;
         }
      };
   }

   public boolean contains(@Nullable Object var1) {
      return this.count(var1) > 0;
   }

   public boolean containsAll(Collection<?> var1) {
      return this.elementSet().containsAll(var1);
   }

   /** @deprecated */
   @Deprecated
   public final int add(E var1, int var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final int remove(Object var1, int var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final int setCount(E var1, int var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final boolean setCount(E var1, int var2, int var3) {
      throw new UnsupportedOperationException();
   }

   @GwtIncompatible("not present in emulated superclass")
   int copyIntoArray(Object[] var1, int var2) {
      Multiset.Entry var4;
      for(Iterator var3 = this.entrySet().iterator(); var3.hasNext(); var2 += var4.getCount()) {
         var4 = (Multiset.Entry)var3.next();
         Arrays.fill(var1, var2, var2 + var4.getCount(), var4.getElement());
      }

      return var2;
   }

   public boolean equals(@Nullable Object var1) {
      return Multisets.equalsImpl(this, var1);
   }

   public int hashCode() {
      return Sets.hashCodeImpl(this.entrySet());
   }

   public String toString() {
      return this.entrySet().toString();
   }

   public ImmutableSet<Multiset.Entry<E>> entrySet() {
      ImmutableSet var1 = this.entrySet;
      return var1 == null?(this.entrySet = this.createEntrySet()):var1;
   }

   private final ImmutableSet<Multiset.Entry<E>> createEntrySet() {
      return (ImmutableSet)(this.isEmpty()?ImmutableSet.of():new ImmutableMultiset.EntrySet(null));
   }

   abstract Multiset.Entry<E> getEntry(int var1);

   Object writeReplace() {
      return new ImmutableMultiset.SerializedForm(this);
   }

   public static <E> ImmutableMultiset.Builder<E> builder() {
      return new ImmutableMultiset.Builder();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator iterator() {
      return this.iterator();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set entrySet() {
      return this.entrySet();
   }

   public static class Builder<E> extends ImmutableCollection.Builder<E> {
      final Multiset<E> contents;

      public Builder() {
         this(LinkedHashMultiset.create());
      }

      Builder(Multiset<E> var1) {
         this.contents = var1;
      }

      public ImmutableMultiset.Builder<E> add(E var1) {
         this.contents.add(Preconditions.checkNotNull(var1));
         return this;
      }

      public ImmutableMultiset.Builder<E> addCopies(E var1, int var2) {
         this.contents.add(Preconditions.checkNotNull(var1), var2);
         return this;
      }

      public ImmutableMultiset.Builder<E> setCount(E var1, int var2) {
         this.contents.setCount(Preconditions.checkNotNull(var1), var2);
         return this;
      }

      public ImmutableMultiset.Builder<E> add(E... var1) {
         super.add(var1);
         return this;
      }

      public ImmutableMultiset.Builder<E> addAll(Iterable<? extends E> var1) {
         if(var1 instanceof Multiset) {
            Multiset var2 = Multisets.cast(var1);
            Iterator var3 = var2.entrySet().iterator();

            while(var3.hasNext()) {
               Multiset.Entry var4 = (Multiset.Entry)var3.next();
               this.addCopies(var4.getElement(), var4.getCount());
            }
         } else {
            super.addAll(var1);
         }

         return this;
      }

      public ImmutableMultiset.Builder<E> addAll(Iterator<? extends E> var1) {
         super.addAll(var1);
         return this;
      }

      public ImmutableMultiset<E> build() {
         return ImmutableMultiset.copyOf((Iterable)this.contents);
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
      public ImmutableCollection.Builder add(Object var1) {
         return this.add(var1);
      }
   }

   private static class SerializedForm implements Serializable {
      final Object[] elements;
      final int[] counts;
      private static final long serialVersionUID = 0L;

      SerializedForm(Multiset<?> var1) {
         int var2 = var1.entrySet().size();
         this.elements = new Object[var2];
         this.counts = new int[var2];
         int var3 = 0;

         for(Iterator var4 = var1.entrySet().iterator(); var4.hasNext(); ++var3) {
            Multiset.Entry var5 = (Multiset.Entry)var4.next();
            this.elements[var3] = var5.getElement();
            this.counts[var3] = var5.getCount();
         }

      }

      Object readResolve() {
         LinkedHashMultiset var1 = LinkedHashMultiset.create(this.elements.length);

         for(int var2 = 0; var2 < this.elements.length; ++var2) {
            var1.add(this.elements[var2], this.counts[var2]);
         }

         return ImmutableMultiset.copyOf((Iterable)var1);
      }
   }

   static class EntrySetSerializedForm<E> implements Serializable {
      final ImmutableMultiset<E> multiset;

      EntrySetSerializedForm(ImmutableMultiset<E> var1) {
         this.multiset = var1;
      }

      Object readResolve() {
         return this.multiset.entrySet();
      }
   }

   private final class EntrySet extends ImmutableSet<Multiset.Entry<E>> {
      private static final long serialVersionUID = 0L;

      private EntrySet() {
      }

      boolean isPartialView() {
         return ImmutableMultiset.this.isPartialView();
      }

      public UnmodifiableIterator<Multiset.Entry<E>> iterator() {
         return this.asList().iterator();
      }

      ImmutableList<Multiset.Entry<E>> createAsList() {
         return new ImmutableAsList() {
            public Multiset.Entry<E> get(int var1) {
               return ImmutableMultiset.this.getEntry(var1);
            }

            ImmutableCollection<Multiset.Entry<E>> delegateCollection() {
               return EntrySet.this;
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object get(int var1) {
               return this.get(var1);
            }
         };
      }

      public int size() {
         return ImmutableMultiset.this.elementSet().size();
      }

      public boolean contains(Object var1) {
         if(var1 instanceof Multiset.Entry) {
            Multiset.Entry var2 = (Multiset.Entry)var1;
            if(var2.getCount() <= 0) {
               return false;
            } else {
               int var3 = ImmutableMultiset.this.count(var2.getElement());
               return var3 == var2.getCount();
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return ImmutableMultiset.this.hashCode();
      }

      Object writeReplace() {
         return new ImmutableMultiset.EntrySetSerializedForm(ImmutableMultiset.this);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Iterator iterator() {
         return this.iterator();
      }

      // $FF: synthetic method
      EntrySet(Object var2) {
         this();
      }
   }
}
