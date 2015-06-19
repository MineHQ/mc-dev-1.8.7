package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.AbstractMultiset;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.Serialization;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

public final class ConcurrentHashMultiset<E> extends AbstractMultiset<E> implements Serializable {
   private final transient ConcurrentMap<E, AtomicInteger> countMap;
   private transient ConcurrentHashMultiset<E>.EntrySet entrySet;
   private static final long serialVersionUID = 1L;

   public static <E> ConcurrentHashMultiset<E> create() {
      return new ConcurrentHashMultiset(new ConcurrentHashMap());
   }

   public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> var0) {
      ConcurrentHashMultiset var1 = create();
      Iterables.addAll(var1, var0);
      return var1;
   }

   @Beta
   public static <E> ConcurrentHashMultiset<E> create(MapMaker var0) {
      return new ConcurrentHashMultiset(var0.makeMap());
   }

   @VisibleForTesting
   ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> var1) {
      Preconditions.checkArgument(var1.isEmpty());
      this.countMap = var1;
   }

   public int count(@Nullable Object var1) {
      AtomicInteger var2 = (AtomicInteger)Maps.safeGet(this.countMap, var1);
      return var2 == null?0:var2.get();
   }

   public int size() {
      long var1 = 0L;

      AtomicInteger var4;
      for(Iterator var3 = this.countMap.values().iterator(); var3.hasNext(); var1 += (long)var4.get()) {
         var4 = (AtomicInteger)var3.next();
      }

      return Ints.saturatedCast(var1);
   }

   public Object[] toArray() {
      return this.snapshot().toArray();
   }

   public <T> T[] toArray(T[] var1) {
      return this.snapshot().toArray(var1);
   }

   private List<E> snapshot() {
      ArrayList var1 = Lists.newArrayListWithExpectedSize(this.size());
      Iterator var2 = this.entrySet().iterator();

      while(var2.hasNext()) {
         Multiset.Entry var3 = (Multiset.Entry)var2.next();
         Object var4 = var3.getElement();

         for(int var5 = var3.getCount(); var5 > 0; --var5) {
            var1.add(var4);
         }
      }

      return var1;
   }

   public int add(E var1, int var2) {
      Preconditions.checkNotNull(var1);
      if(var2 == 0) {
         return this.count(var1);
      } else {
         Preconditions.checkArgument(var2 > 0, "Invalid occurrences: %s", new Object[]{Integer.valueOf(var2)});

         AtomicInteger var3;
         AtomicInteger var7;
         do {
            var3 = (AtomicInteger)Maps.safeGet(this.countMap, var1);
            if(var3 == null) {
               var3 = (AtomicInteger)this.countMap.putIfAbsent(var1, new AtomicInteger(var2));
               if(var3 == null) {
                  return 0;
               }
            }

            while(true) {
               int var4 = var3.get();
               if(var4 == 0) {
                  var7 = new AtomicInteger(var2);
                  break;
               }

               try {
                  int var5 = IntMath.checkedAdd(var4, var2);
                  if(var3.compareAndSet(var4, var5)) {
                     return var4;
                  }
               } catch (ArithmeticException var6) {
                  throw new IllegalArgumentException("Overflow adding " + var2 + " occurrences to a count of " + var4);
               }
            }
         } while(this.countMap.putIfAbsent(var1, var7) != null && !this.countMap.replace(var1, var3, var7));

         return 0;
      }
   }

   public int remove(@Nullable Object var1, int var2) {
      if(var2 == 0) {
         return this.count(var1);
      } else {
         Preconditions.checkArgument(var2 > 0, "Invalid occurrences: %s", new Object[]{Integer.valueOf(var2)});
         AtomicInteger var3 = (AtomicInteger)Maps.safeGet(this.countMap, var1);
         if(var3 == null) {
            return 0;
         } else {
            int var4;
            int var5;
            do {
               var4 = var3.get();
               if(var4 == 0) {
                  return 0;
               }

               var5 = Math.max(0, var4 - var2);
            } while(!var3.compareAndSet(var4, var5));

            if(var5 == 0) {
               this.countMap.remove(var1, var3);
            }

            return var4;
         }
      }
   }

   public boolean removeExactly(@Nullable Object var1, int var2) {
      if(var2 == 0) {
         return true;
      } else {
         Preconditions.checkArgument(var2 > 0, "Invalid occurrences: %s", new Object[]{Integer.valueOf(var2)});
         AtomicInteger var3 = (AtomicInteger)Maps.safeGet(this.countMap, var1);
         if(var3 == null) {
            return false;
         } else {
            int var4;
            int var5;
            do {
               var4 = var3.get();
               if(var4 < var2) {
                  return false;
               }

               var5 = var4 - var2;
            } while(!var3.compareAndSet(var4, var5));

            if(var5 == 0) {
               this.countMap.remove(var1, var3);
            }

            return true;
         }
      }
   }

   public int setCount(E var1, int var2) {
      Preconditions.checkNotNull(var1);
      CollectPreconditions.checkNonnegative(var2, "count");

      AtomicInteger var3;
      AtomicInteger var5;
      label40:
      do {
         var3 = (AtomicInteger)Maps.safeGet(this.countMap, var1);
         if(var3 == null) {
            if(var2 == 0) {
               return 0;
            }

            var3 = (AtomicInteger)this.countMap.putIfAbsent(var1, new AtomicInteger(var2));
            if(var3 == null) {
               return 0;
            }
         }

         int var4;
         do {
            var4 = var3.get();
            if(var4 == 0) {
               if(var2 == 0) {
                  return 0;
               }

               var5 = new AtomicInteger(var2);
               continue label40;
            }
         } while(!var3.compareAndSet(var4, var2));

         if(var2 == 0) {
            this.countMap.remove(var1, var3);
         }

         return var4;
      } while(this.countMap.putIfAbsent(var1, var5) != null && !this.countMap.replace(var1, var3, var5));

      return 0;
   }

   public boolean setCount(E var1, int var2, int var3) {
      Preconditions.checkNotNull(var1);
      CollectPreconditions.checkNonnegative(var2, "oldCount");
      CollectPreconditions.checkNonnegative(var3, "newCount");
      AtomicInteger var4 = (AtomicInteger)Maps.safeGet(this.countMap, var1);
      if(var4 == null) {
         return var2 != 0?false:(var3 == 0?true:this.countMap.putIfAbsent(var1, new AtomicInteger(var3)) == null);
      } else {
         int var5 = var4.get();
         if(var5 == var2) {
            if(var5 == 0) {
               if(var3 == 0) {
                  this.countMap.remove(var1, var4);
                  return true;
               }

               AtomicInteger var6 = new AtomicInteger(var3);
               return this.countMap.putIfAbsent(var1, var6) == null || this.countMap.replace(var1, var4, var6);
            }

            if(var4.compareAndSet(var5, var3)) {
               if(var3 == 0) {
                  this.countMap.remove(var1, var4);
               }

               return true;
            }
         }

         return false;
      }
   }

   Set<E> createElementSet() {
      final Set var1 = this.countMap.keySet();
      return new ForwardingSet() {
         protected Set<E> delegate() {
            return var1;
         }

         public boolean contains(@Nullable Object var1x) {
            return var1x != null && Collections2.safeContains(var1, var1x);
         }

         public boolean containsAll(Collection<?> var1x) {
            return this.standardContainsAll(var1x);
         }

         public boolean remove(Object var1x) {
            return var1x != null && Collections2.safeRemove(var1, var1x);
         }

         public boolean removeAll(Collection<?> var1x) {
            return this.standardRemoveAll(var1x);
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Collection delegate() {
            return this.delegate();
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   public Set<Multiset.Entry<E>> entrySet() {
      ConcurrentHashMultiset.EntrySet var1 = this.entrySet;
      if(var1 == null) {
         this.entrySet = var1 = new ConcurrentHashMultiset.EntrySet(null);
      }

      return var1;
   }

   int distinctElements() {
      return this.countMap.size();
   }

   public boolean isEmpty() {
      return this.countMap.isEmpty();
   }

   Iterator<Multiset.Entry<E>> entryIterator() {
      final AbstractIterator var1 = new AbstractIterator() {
         private Iterator<Entry<E, AtomicInteger>> mapEntries;

         {
            this.mapEntries = ConcurrentHashMultiset.this.countMap.entrySet().iterator();
         }

         protected Multiset.Entry<E> computeNext() {
            Entry var1;
            int var2;
            do {
               if(!this.mapEntries.hasNext()) {
                  return (Multiset.Entry)this.endOfData();
               }

               var1 = (Entry)this.mapEntries.next();
               var2 = ((AtomicInteger)var1.getValue()).get();
            } while(var2 == 0);

            return Multisets.immutableEntry(var1.getKey(), var2);
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object computeNext() {
            return this.computeNext();
         }
      };
      return new ForwardingIterator() {
         private Multiset.Entry<E> last;

         protected Iterator<Multiset.Entry<E>> delegate() {
            return var1;
         }

         public Multiset.Entry<E> next() {
            this.last = (Multiset.Entry)super.next();
            return this.last;
         }

         public void remove() {
            CollectPreconditions.checkRemove(this.last != null);
            ConcurrentHashMultiset.this.setCount(this.last.getElement(), 0);
            this.last = null;
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object next() {
            return this.next();
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   public void clear() {
      this.countMap.clear();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.countMap);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      ConcurrentMap var2 = (ConcurrentMap)var1.readObject();
      ConcurrentHashMultiset.FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set(this, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public String toString() {
      return super.toString();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set elementSet() {
      return super.elementSet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean retainAll(Collection var1) {
      return super.retainAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean removeAll(Collection var1) {
      return super.removeAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean addAll(Collection var1) {
      return super.addAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean remove(Object var1) {
      return super.remove(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean add(Object var1) {
      return super.add(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator iterator() {
      return super.iterator();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean contains(Object var1) {
      return super.contains(var1);
   }

   private class EntrySet extends AbstractMultiset<E>.EntrySet {
      private EntrySet() {
         super();
      }

      ConcurrentHashMultiset<E> multiset() {
         return ConcurrentHashMultiset.this;
      }

      public Object[] toArray() {
         return this.snapshot().toArray();
      }

      public <T> T[] toArray(T[] var1) {
         return this.snapshot().toArray(var1);
      }

      private List<Multiset.Entry<E>> snapshot() {
         ArrayList var1 = Lists.newArrayListWithExpectedSize(this.size());
         Iterators.addAll(var1, this.iterator());
         return var1;
      }

      // $FF: synthetic method
      // $FF: bridge method
      Multiset multiset() {
         return this.multiset();
      }

      // $FF: synthetic method
      EntrySet(Object var2) {
         this();
      }
   }

   private static class FieldSettersHolder {
      static final Serialization.FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");

      private FieldSettersHolder() {
      }
   }
}
