package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMultiset;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Count;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.primitives.Ints;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
abstract class AbstractMapBasedMultiset<E> extends AbstractMultiset<E> implements Serializable {
   private transient Map<E, Count> backingMap;
   private transient long size;
   @GwtIncompatible("not needed in emulated source.")
   private static final long serialVersionUID = -2250766705698539974L;

   protected AbstractMapBasedMultiset(Map<E, Count> var1) {
      this.backingMap = (Map)Preconditions.checkNotNull(var1);
      this.size = (long)super.size();
   }

   void setBackingMap(Map<E, Count> var1) {
      this.backingMap = var1;
   }

   public Set<Multiset.Entry<E>> entrySet() {
      return super.entrySet();
   }

   Iterator<Multiset.Entry<E>> entryIterator() {
      final Iterator var1 = this.backingMap.entrySet().iterator();
      return new Iterator() {
         Entry<E, Count> toRemove;

         public boolean hasNext() {
            return var1.hasNext();
         }

         public Multiset.Entry<E> next() {
            final Entry var1x = (Entry)var1.next();
            this.toRemove = var1x;
            return new Multisets.AbstractEntry() {
               public E getElement() {
                  return var1x.getKey();
               }

               public int getCount() {
                  Count var1xx = (Count)var1x.getValue();
                  if(var1xx == null || var1xx.get() == 0) {
                     Count var2 = (Count)AbstractMapBasedMultiset.this.backingMap.get(this.getElement());
                     if(var2 != null) {
                        return var2.get();
                     }
                  }

                  return var1xx == null?0:var1xx.get();
               }
            };
         }

         public void remove() {
            CollectPreconditions.checkRemove(this.toRemove != null);
            AbstractMapBasedMultiset.this.size = (long)((Count)this.toRemove.getValue()).getAndSet(0);
            var1.remove();
            this.toRemove = null;
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object next() {
            return this.next();
         }
      };
   }

   public void clear() {
      Iterator var1 = this.backingMap.values().iterator();

      while(var1.hasNext()) {
         Count var2 = (Count)var1.next();
         var2.set(0);
      }

      this.backingMap.clear();
      this.size = 0L;
   }

   int distinctElements() {
      return this.backingMap.size();
   }

   public int size() {
      return Ints.saturatedCast(this.size);
   }

   public Iterator<E> iterator() {
      return new AbstractMapBasedMultiset.MapBasedMultisetIterator();
   }

   public int count(@Nullable Object var1) {
      Count var2 = (Count)Maps.safeGet(this.backingMap, var1);
      return var2 == null?0:var2.get();
   }

   public int add(@Nullable E var1, int var2) {
      if(var2 == 0) {
         return this.count(var1);
      } else {
         Preconditions.checkArgument(var2 > 0, "occurrences cannot be negative: %s", new Object[]{Integer.valueOf(var2)});
         Count var3 = (Count)this.backingMap.get(var1);
         int var4;
         if(var3 == null) {
            var4 = 0;
            this.backingMap.put(var1, new Count(var2));
         } else {
            var4 = var3.get();
            long var5 = (long)var4 + (long)var2;
            Preconditions.checkArgument(var5 <= 2147483647L, "too many occurrences: %s", new Object[]{Long.valueOf(var5)});
            var3.getAndAdd(var2);
         }

         this.size += (long)var2;
         return var4;
      }
   }

   public int remove(@Nullable Object var1, int var2) {
      if(var2 == 0) {
         return this.count(var1);
      } else {
         Preconditions.checkArgument(var2 > 0, "occurrences cannot be negative: %s", new Object[]{Integer.valueOf(var2)});
         Count var3 = (Count)this.backingMap.get(var1);
         if(var3 == null) {
            return 0;
         } else {
            int var4 = var3.get();
            int var5;
            if(var4 > var2) {
               var5 = var2;
            } else {
               var5 = var4;
               this.backingMap.remove(var1);
            }

            var3.addAndGet(-var5);
            this.size -= (long)var5;
            return var4;
         }
      }
   }

   public int setCount(@Nullable E var1, int var2) {
      CollectPreconditions.checkNonnegative(var2, "count");
      Count var3;
      int var4;
      if(var2 == 0) {
         var3 = (Count)this.backingMap.remove(var1);
         var4 = getAndSet(var3, var2);
      } else {
         var3 = (Count)this.backingMap.get(var1);
         var4 = getAndSet(var3, var2);
         if(var3 == null) {
            this.backingMap.put(var1, new Count(var2));
         }
      }

      this.size += (long)(var2 - var4);
      return var4;
   }

   private static int getAndSet(Count var0, int var1) {
      return var0 == null?0:var0.getAndSet(var1);
   }

   @GwtIncompatible("java.io.ObjectStreamException")
   private void readObjectNoData() throws ObjectStreamException {
      throw new InvalidObjectException("Stream data required");
   }

   // $FF: synthetic method
   static long access$110(AbstractMapBasedMultiset var0) {
      return (long)(var0.size--);
   }

   private class MapBasedMultisetIterator implements Iterator<E> {
      final Iterator<Entry<E, Count>> entryIterator;
      Entry<E, Count> currentEntry;
      int occurrencesLeft;
      boolean canRemove;

      MapBasedMultisetIterator() {
         this.entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
      }

      public boolean hasNext() {
         return this.occurrencesLeft > 0 || this.entryIterator.hasNext();
      }

      public E next() {
         if(this.occurrencesLeft == 0) {
            this.currentEntry = (Entry)this.entryIterator.next();
            this.occurrencesLeft = ((Count)this.currentEntry.getValue()).get();
         }

         --this.occurrencesLeft;
         this.canRemove = true;
         return this.currentEntry.getKey();
      }

      public void remove() {
         CollectPreconditions.checkRemove(this.canRemove);
         int var1 = ((Count)this.currentEntry.getValue()).get();
         if(var1 <= 0) {
            throw new ConcurrentModificationException();
         } else {
            if(((Count)this.currentEntry.getValue()).addAndGet(-1) == 0) {
               this.entryIterator.remove();
            }

            AbstractMapBasedMultiset.access$110(AbstractMapBasedMultiset.this);
            this.canRemove = false;
         }
      }
   }
}
