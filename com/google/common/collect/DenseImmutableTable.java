package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.RegularImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
@Immutable
final class DenseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
   private final ImmutableMap<R, Integer> rowKeyToIndex;
   private final ImmutableMap<C, Integer> columnKeyToIndex;
   private final ImmutableMap<R, Map<C, V>> rowMap;
   private final ImmutableMap<C, Map<R, V>> columnMap;
   private final int[] rowCounts;
   private final int[] columnCounts;
   private final V[][] values;
   private final int[] iterationOrderRow;
   private final int[] iterationOrderColumn;

   private static <E> ImmutableMap<E, Integer> makeIndex(ImmutableSet<E> var0) {
      ImmutableMap.Builder var1 = ImmutableMap.builder();
      int var2 = 0;

      for(Iterator var3 = var0.iterator(); var3.hasNext(); ++var2) {
         Object var4 = var3.next();
         var1.put(var4, Integer.valueOf(var2));
      }

      return var1.build();
   }

   DenseImmutableTable(ImmutableList<Table.Cell<R, C, V>> var1, ImmutableSet<R> var2, ImmutableSet<C> var3) {
      Object[][] var4 = (Object[][])(new Object[var2.size()][var3.size()]);
      this.values = var4;
      this.rowKeyToIndex = makeIndex(var2);
      this.columnKeyToIndex = makeIndex(var3);
      this.rowCounts = new int[this.rowKeyToIndex.size()];
      this.columnCounts = new int[this.columnKeyToIndex.size()];
      int[] var5 = new int[var1.size()];
      int[] var6 = new int[var1.size()];

      for(int var7 = 0; var7 < var1.size(); ++var7) {
         Table.Cell var8 = (Table.Cell)var1.get(var7);
         Object var9 = var8.getRowKey();
         Object var10 = var8.getColumnKey();
         int var11 = ((Integer)this.rowKeyToIndex.get(var9)).intValue();
         int var12 = ((Integer)this.columnKeyToIndex.get(var10)).intValue();
         Object var13 = this.values[var11][var12];
         Preconditions.checkArgument(var13 == null, "duplicate key: (%s, %s)", new Object[]{var9, var10});
         this.values[var11][var12] = var8.getValue();
         ++this.rowCounts[var11];
         ++this.columnCounts[var12];
         var5[var7] = var11;
         var6[var7] = var12;
      }

      this.iterationOrderRow = var5;
      this.iterationOrderColumn = var6;
      this.rowMap = new DenseImmutableTable.RowMap();
      this.columnMap = new DenseImmutableTable.ColumnMap();
   }

   public ImmutableMap<C, Map<R, V>> columnMap() {
      return this.columnMap;
   }

   public ImmutableMap<R, Map<C, V>> rowMap() {
      return this.rowMap;
   }

   public V get(@Nullable Object var1, @Nullable Object var2) {
      Integer var3 = (Integer)this.rowKeyToIndex.get(var1);
      Integer var4 = (Integer)this.columnKeyToIndex.get(var2);
      return var3 != null && var4 != null?this.values[var3.intValue()][var4.intValue()]:null;
   }

   public int size() {
      return this.iterationOrderRow.length;
   }

   Table.Cell<R, C, V> getCell(int var1) {
      int var2 = this.iterationOrderRow[var1];
      int var3 = this.iterationOrderColumn[var1];
      Object var4 = this.rowKeySet().asList().get(var2);
      Object var5 = this.columnKeySet().asList().get(var3);
      Object var6 = this.values[var2][var3];
      return cellOf(var4, var5, var6);
   }

   V getValue(int var1) {
      return this.values[this.iterationOrderRow[var1]][this.iterationOrderColumn[var1]];
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map columnMap() {
      return this.columnMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map rowMap() {
      return this.rowMap();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private final class ColumnMap extends DenseImmutableTable.ImmutableArrayMap<C, Map<R, V>> {
      private ColumnMap() {
         super(DenseImmutableTable.this.columnCounts.length);
      }

      ImmutableMap<C, Integer> keyToIndex() {
         return DenseImmutableTable.this.columnKeyToIndex;
      }

      Map<R, V> getValue(int var1) {
         return DenseImmutableTable.this.new Column(var1);
      }

      boolean isPartialView() {
         return false;
      }

      // $FF: synthetic method
      // $FF: bridge method
      Object getValue(int var1) {
         return this.getValue(var1);
      }

      // $FF: synthetic method
      ColumnMap(DenseImmutableTable.SyntheticClass_1 var2) {
         this();
      }
   }

   private final class RowMap extends DenseImmutableTable.ImmutableArrayMap<R, Map<C, V>> {
      private RowMap() {
         super(DenseImmutableTable.this.rowCounts.length);
      }

      ImmutableMap<R, Integer> keyToIndex() {
         return DenseImmutableTable.this.rowKeyToIndex;
      }

      Map<C, V> getValue(int var1) {
         return DenseImmutableTable.this.new Row(var1);
      }

      boolean isPartialView() {
         return false;
      }

      // $FF: synthetic method
      // $FF: bridge method
      Object getValue(int var1) {
         return this.getValue(var1);
      }

      // $FF: synthetic method
      RowMap(DenseImmutableTable.SyntheticClass_1 var2) {
         this();
      }
   }

   private final class Column extends DenseImmutableTable.ImmutableArrayMap<R, V> {
      private final int columnIndex;

      Column(int var2) {
         super(DenseImmutableTable.this.columnCounts[var2]);
         this.columnIndex = var2;
      }

      ImmutableMap<R, Integer> keyToIndex() {
         return DenseImmutableTable.this.rowKeyToIndex;
      }

      V getValue(int var1) {
         return DenseImmutableTable.this.values[var1][this.columnIndex];
      }

      boolean isPartialView() {
         return true;
      }
   }

   private final class Row extends DenseImmutableTable.ImmutableArrayMap<C, V> {
      private final int rowIndex;

      Row(int var2) {
         super(DenseImmutableTable.this.rowCounts[var2]);
         this.rowIndex = var2;
      }

      ImmutableMap<C, Integer> keyToIndex() {
         return DenseImmutableTable.this.columnKeyToIndex;
      }

      V getValue(int var1) {
         return DenseImmutableTable.this.values[this.rowIndex][var1];
      }

      boolean isPartialView() {
         return true;
      }
   }

   private abstract static class ImmutableArrayMap<K, V> extends ImmutableMap<K, V> {
      private final int size;

      ImmutableArrayMap(int var1) {
         this.size = var1;
      }

      abstract ImmutableMap<K, Integer> keyToIndex();

      private boolean isFull() {
         return this.size == this.keyToIndex().size();
      }

      K getKey(int var1) {
         return this.keyToIndex().keySet().asList().get(var1);
      }

      @Nullable
      abstract V getValue(int var1);

      ImmutableSet<K> createKeySet() {
         return this.isFull()?this.keyToIndex().keySet():super.createKeySet();
      }

      public int size() {
         return this.size;
      }

      public V get(@Nullable Object var1) {
         Integer var2 = (Integer)this.keyToIndex().get(var1);
         return var2 == null?null:this.getValue(var2.intValue());
      }

      ImmutableSet<Entry<K, V>> createEntrySet() {
         return new ImmutableMapEntrySet() {
            ImmutableMap<K, V> map() {
               return ImmutableArrayMap.this;
            }

            public UnmodifiableIterator<Entry<K, V>> iterator() {
               return new AbstractIterator() {
                  private int index = -1;
                  private final int maxIndex = ImmutableArrayMap.this.keyToIndex().size();

                  protected Entry<K, V> computeNext() {
                     ++this.index;

                     while(this.index < this.maxIndex) {
                        Object var1 = ImmutableArrayMap.this.getValue(this.index);
                        if(var1 != null) {
                           return Maps.immutableEntry(ImmutableArrayMap.this.getKey(this.index), var1);
                        }

                        ++this.index;
                     }

                     return (Entry)this.endOfData();
                  }

                  // $FF: synthetic method
                  // $FF: bridge method
                  protected Object computeNext() {
                     return this.computeNext();
                  }
               };
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Iterator iterator() {
               return this.iterator();
            }
         };
      }
   }
}
