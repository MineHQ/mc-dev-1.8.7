package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.StandardRowSortedTable;
import com.google.common.collect.StandardTable;
import com.google.common.collect.Table;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
@Beta
public class TreeBasedTable<R, C, V> extends StandardRowSortedTable<R, C, V> {
   private final Comparator<? super C> columnComparator;
   private static final long serialVersionUID = 0L;

   public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
      return new TreeBasedTable(Ordering.natural(), Ordering.natural());
   }

   public static <R, C, V> TreeBasedTable<R, C, V> create(Comparator<? super R> var0, Comparator<? super C> var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new TreeBasedTable(var0, var1);
   }

   public static <R, C, V> TreeBasedTable<R, C, V> create(TreeBasedTable<R, C, ? extends V> var0) {
      TreeBasedTable var1 = new TreeBasedTable(var0.rowComparator(), var0.columnComparator());
      var1.putAll(var0);
      return var1;
   }

   TreeBasedTable(Comparator<? super R> var1, Comparator<? super C> var2) {
      super(new TreeMap(var1), new TreeBasedTable.Factory(var2));
      this.columnComparator = var2;
   }

   public Comparator<? super R> rowComparator() {
      return this.rowKeySet().comparator();
   }

   public Comparator<? super C> columnComparator() {
      return this.columnComparator;
   }

   public SortedMap<C, V> row(R var1) {
      return new TreeBasedTable.TreeRow(var1);
   }

   public SortedSet<R> rowKeySet() {
      return super.rowKeySet();
   }

   public SortedMap<R, Map<C, V>> rowMap() {
      return super.rowMap();
   }

   Iterator<C> createColumnKeyIterator() {
      final Comparator var1 = this.columnComparator();
      final UnmodifiableIterator var2 = Iterators.mergeSorted(Iterables.transform(this.backingMap.values(), new Function() {
         public Iterator<C> apply(Map<C, V> var1) {
            return var1.keySet().iterator();
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object apply(Object var1) {
            return this.apply((Map)var1);
         }
      }), var1);
      return new AbstractIterator() {
         C lastValue;

         protected C computeNext() {
            Object var1x;
            boolean var2x;
            do {
               if(!var2.hasNext()) {
                  this.lastValue = null;
                  return this.endOfData();
               }

               var1x = var2.next();
               var2x = this.lastValue != null && var1.compare(var1x, this.lastValue) == 0;
            } while(var2x);

            this.lastValue = var1x;
            return this.lastValue;
         }
      };
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map rowMap() {
      return this.rowMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set rowKeySet() {
      return this.rowKeySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map row(Object var1) {
      return this.row(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map columnMap() {
      return super.columnMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection values() {
      return super.values();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set columnKeySet() {
      return super.columnKeySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map column(Object var1) {
      return super.column(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set cellSet() {
      return super.cellSet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object remove(Object var1, Object var2) {
      return super.remove(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object put(Object var1, Object var2, Object var3) {
      return super.put(var1, var2, var3);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void clear() {
      super.clear();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int size() {
      return super.size();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean isEmpty() {
      return super.isEmpty();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object get(Object var1, Object var2) {
      return super.get(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsValue(Object var1) {
      return super.containsValue(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsRow(Object var1) {
      return super.containsRow(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsColumn(Object var1) {
      return super.containsColumn(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean contains(Object var1, Object var2) {
      return super.contains(var1, var2);
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
   public void putAll(Table var1) {
      super.putAll(var1);
   }

   private class TreeRow extends StandardTable<R, C, V>.Row implements SortedMap<C, V> {
      @Nullable
      final C lowerBound;
      @Nullable
      final C upperBound;
      transient SortedMap<C, V> wholeRow;

      TreeRow(R var1) {
         this();
      }

      TreeRow(R var1, @Nullable C var2, @Nullable C var3) {
         super();
         this.lowerBound = var3;
         this.upperBound = var4;
         Preconditions.checkArgument(var3 == null || var4 == null || this.compare(var3, var4) <= 0);
      }

      public SortedSet<C> keySet() {
         return new Maps.SortedKeySet(this);
      }

      public Comparator<? super C> comparator() {
         return TreeBasedTable.this.columnComparator();
      }

      int compare(Object var1, Object var2) {
         Comparator var3 = this.comparator();
         return var3.compare(var1, var2);
      }

      boolean rangeContains(@Nullable Object var1) {
         return var1 != null && (this.lowerBound == null || this.compare(this.lowerBound, var1) <= 0) && (this.upperBound == null || this.compare(this.upperBound, var1) > 0);
      }

      public SortedMap<C, V> subMap(C var1, C var2) {
         Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(var1)) && this.rangeContains(Preconditions.checkNotNull(var2)));
         return TreeBasedTable.this.new TreeRow(this.rowKey, var1, var2);
      }

      public SortedMap<C, V> headMap(C var1) {
         Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(var1)));
         return TreeBasedTable.this.new TreeRow(this.rowKey, this.lowerBound, var1);
      }

      public SortedMap<C, V> tailMap(C var1) {
         Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(var1)));
         return TreeBasedTable.this.new TreeRow(this.rowKey, var1, this.upperBound);
      }

      public C firstKey() {
         SortedMap var1 = this.backingRowMap();
         if(var1 == null) {
            throw new NoSuchElementException();
         } else {
            return this.backingRowMap().firstKey();
         }
      }

      public C lastKey() {
         SortedMap var1 = this.backingRowMap();
         if(var1 == null) {
            throw new NoSuchElementException();
         } else {
            return this.backingRowMap().lastKey();
         }
      }

      SortedMap<C, V> wholeRow() {
         if(this.wholeRow == null || this.wholeRow.isEmpty() && TreeBasedTable.this.backingMap.containsKey(this.rowKey)) {
            this.wholeRow = (SortedMap)TreeBasedTable.this.backingMap.get(this.rowKey);
         }

         return this.wholeRow;
      }

      SortedMap<C, V> backingRowMap() {
         return (SortedMap)super.backingRowMap();
      }

      SortedMap<C, V> computeBackingRowMap() {
         SortedMap var1 = this.wholeRow();
         if(var1 != null) {
            if(this.lowerBound != null) {
               var1 = var1.tailMap(this.lowerBound);
            }

            if(this.upperBound != null) {
               var1 = var1.headMap(this.upperBound);
            }

            return var1;
         } else {
            return null;
         }
      }

      void maintainEmptyInvariant() {
         if(this.wholeRow() != null && this.wholeRow.isEmpty()) {
            TreeBasedTable.this.backingMap.remove(this.rowKey);
            this.wholeRow = null;
            this.backingRowMap = null;
         }

      }

      public boolean containsKey(Object var1) {
         return this.rangeContains(var1) && super.containsKey(var1);
      }

      public V put(C var1, V var2) {
         Preconditions.checkArgument(this.rangeContains(Preconditions.checkNotNull(var1)));
         return super.put(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      Map computeBackingRowMap() {
         return this.computeBackingRowMap();
      }

      // $FF: synthetic method
      // $FF: bridge method
      Map backingRowMap() {
         return this.backingRowMap();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Set keySet() {
         return this.keySet();
      }
   }

   private static class Factory<C, V> implements Supplier<TreeMap<C, V>>, Serializable {
      final Comparator<? super C> comparator;
      private static final long serialVersionUID = 0L;

      Factory(Comparator<? super C> var1) {
         this.comparator = var1;
      }

      public TreeMap<C, V> get() {
         return new TreeMap(this.comparator);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get() {
         return this.get();
      }
   }
}
