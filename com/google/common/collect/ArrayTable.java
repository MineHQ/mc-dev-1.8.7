package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIndexedListIterator;
import com.google.common.collect.AbstractMapEntry;
import com.google.common.collect.AbstractTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
@GwtCompatible(
   emulated = true
)
public final class ArrayTable<R, C, V> extends AbstractTable<R, C, V> implements Serializable {
   private final ImmutableList<R> rowList;
   private final ImmutableList<C> columnList;
   private final ImmutableMap<R, Integer> rowKeyToIndex;
   private final ImmutableMap<C, Integer> columnKeyToIndex;
   private final V[][] array;
   private transient ArrayTable<R, C, V>.ColumnMap columnMap;
   private transient ArrayTable<R, C, V>.RowMap rowMap;
   private static final long serialVersionUID = 0L;

   public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> var0, Iterable<? extends C> var1) {
      return new ArrayTable(var0, var1);
   }

   public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, V> var0) {
      return var0 instanceof ArrayTable?new ArrayTable((ArrayTable)var0):new ArrayTable(var0);
   }

   private ArrayTable(Iterable<? extends R> var1, Iterable<? extends C> var2) {
      this.rowList = ImmutableList.copyOf(var1);
      this.columnList = ImmutableList.copyOf(var2);
      Preconditions.checkArgument(!this.rowList.isEmpty());
      Preconditions.checkArgument(!this.columnList.isEmpty());
      this.rowKeyToIndex = index(this.rowList);
      this.columnKeyToIndex = index(this.columnList);
      Object[][] var3 = (Object[][])(new Object[this.rowList.size()][this.columnList.size()]);
      this.array = var3;
      this.eraseAll();
   }

   private static <E> ImmutableMap<E, Integer> index(List<E> var0) {
      ImmutableMap.Builder var1 = ImmutableMap.builder();

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         var1.put(var0.get(var2), Integer.valueOf(var2));
      }

      return var1.build();
   }

   private ArrayTable(Table<R, C, V> var1) {
      this(var1.rowKeySet(), var1.columnKeySet());
      this.putAll(var1);
   }

   private ArrayTable(ArrayTable<R, C, V> var1) {
      this.rowList = var1.rowList;
      this.columnList = var1.columnList;
      this.rowKeyToIndex = var1.rowKeyToIndex;
      this.columnKeyToIndex = var1.columnKeyToIndex;
      Object[][] var2 = (Object[][])(new Object[this.rowList.size()][this.columnList.size()]);
      this.array = var2;
      this.eraseAll();

      for(int var3 = 0; var3 < this.rowList.size(); ++var3) {
         System.arraycopy(var1.array[var3], 0, var2[var3], 0, var1.array[var3].length);
      }

   }

   public ImmutableList<R> rowKeyList() {
      return this.rowList;
   }

   public ImmutableList<C> columnKeyList() {
      return this.columnList;
   }

   public V at(int var1, int var2) {
      Preconditions.checkElementIndex(var1, this.rowList.size());
      Preconditions.checkElementIndex(var2, this.columnList.size());
      return this.array[var1][var2];
   }

   public V set(int var1, int var2, @Nullable V var3) {
      Preconditions.checkElementIndex(var1, this.rowList.size());
      Preconditions.checkElementIndex(var2, this.columnList.size());
      Object var4 = this.array[var1][var2];
      this.array[var1][var2] = var3;
      return var4;
   }

   @GwtIncompatible("reflection")
   public V[][] toArray(Class<V> var1) {
      Object[][] var2 = (Object[][])((Object[][])Array.newInstance(var1, new int[]{this.rowList.size(), this.columnList.size()}));

      for(int var3 = 0; var3 < this.rowList.size(); ++var3) {
         System.arraycopy(this.array[var3], 0, var2[var3], 0, this.array[var3].length);
      }

      return var2;
   }

   /** @deprecated */
   @Deprecated
   public void clear() {
      throw new UnsupportedOperationException();
   }

   public void eraseAll() {
      Object[][] var1 = this.array;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Object[] var4 = var1[var3];
         Arrays.fill(var4, (Object)null);
      }

   }

   public boolean contains(@Nullable Object var1, @Nullable Object var2) {
      return this.containsRow(var1) && this.containsColumn(var2);
   }

   public boolean containsColumn(@Nullable Object var1) {
      return this.columnKeyToIndex.containsKey(var1);
   }

   public boolean containsRow(@Nullable Object var1) {
      return this.rowKeyToIndex.containsKey(var1);
   }

   public boolean containsValue(@Nullable Object var1) {
      Object[][] var2 = this.array;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object[] var5 = var2[var4];
         Object[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Object var9 = var6[var8];
            if(Objects.equal(var1, var9)) {
               return true;
            }
         }
      }

      return false;
   }

   public V get(@Nullable Object var1, @Nullable Object var2) {
      Integer var3 = (Integer)this.rowKeyToIndex.get(var1);
      Integer var4 = (Integer)this.columnKeyToIndex.get(var2);
      return var3 != null && var4 != null?this.at(var3.intValue(), var4.intValue()):null;
   }

   public boolean isEmpty() {
      return false;
   }

   public V put(R var1, C var2, @Nullable V var3) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      Integer var4 = (Integer)this.rowKeyToIndex.get(var1);
      Preconditions.checkArgument(var4 != null, "Row %s not in %s", new Object[]{var1, this.rowList});
      Integer var5 = (Integer)this.columnKeyToIndex.get(var2);
      Preconditions.checkArgument(var5 != null, "Column %s not in %s", new Object[]{var2, this.columnList});
      return this.set(var4.intValue(), var5.intValue(), var3);
   }

   public void putAll(Table<? extends R, ? extends C, ? extends V> var1) {
      super.putAll(var1);
   }

   /** @deprecated */
   @Deprecated
   public V remove(Object var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public V erase(@Nullable Object var1, @Nullable Object var2) {
      Integer var3 = (Integer)this.rowKeyToIndex.get(var1);
      Integer var4 = (Integer)this.columnKeyToIndex.get(var2);
      return var3 != null && var4 != null?this.set(var3.intValue(), var4.intValue(), (Object)null):null;
   }

   public int size() {
      return this.rowList.size() * this.columnList.size();
   }

   public Set<Table.Cell<R, C, V>> cellSet() {
      return super.cellSet();
   }

   Iterator<Table.Cell<R, C, V>> cellIterator() {
      return new AbstractIndexedListIterator(this.size()) {
         protected Table.Cell<R, C, V> get(final int var1) {
            return new Tables.AbstractCell() {
               final int rowIndex;
               final int columnIndex;

               {
                  this.rowIndex = var1 / ArrayTable.this.columnList.size();
                  this.columnIndex = var1 % ArrayTable.this.columnList.size();
               }

               public R getRowKey() {
                  return ArrayTable.this.rowList.get(this.rowIndex);
               }

               public C getColumnKey() {
                  return ArrayTable.this.columnList.get(this.columnIndex);
               }

               public V getValue() {
                  return ArrayTable.this.at(this.rowIndex, this.columnIndex);
               }
            };
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object get(int var1) {
            return this.get(var1);
         }
      };
   }

   public Map<R, V> column(C var1) {
      Preconditions.checkNotNull(var1);
      Integer var2 = (Integer)this.columnKeyToIndex.get(var1);
      return (Map)(var2 == null?ImmutableMap.of():new ArrayTable.Column(var2.intValue()));
   }

   public ImmutableSet<C> columnKeySet() {
      return this.columnKeyToIndex.keySet();
   }

   public Map<C, Map<R, V>> columnMap() {
      ArrayTable.ColumnMap var1 = this.columnMap;
      return var1 == null?(this.columnMap = new ArrayTable.ColumnMap(null)):var1;
   }

   public Map<C, V> row(R var1) {
      Preconditions.checkNotNull(var1);
      Integer var2 = (Integer)this.rowKeyToIndex.get(var1);
      return (Map)(var2 == null?ImmutableMap.of():new ArrayTable.Row(var2.intValue()));
   }

   public ImmutableSet<R> rowKeySet() {
      return this.rowKeyToIndex.keySet();
   }

   public Map<R, Map<C, V>> rowMap() {
      ArrayTable.RowMap var1 = this.rowMap;
      return var1 == null?(this.rowMap = new ArrayTable.RowMap(null)):var1;
   }

   public Collection<V> values() {
      return super.values();
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
   public Set columnKeySet() {
      return this.columnKeySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set rowKeySet() {
      return this.rowKeySet();
   }

   private class RowMap extends ArrayTable.ArrayMap<R, Map<C, V>> {
      private RowMap() {
         super(ArrayTable.this.rowKeyToIndex, null);
      }

      String getKeyRole() {
         return "Row";
      }

      Map<C, V> getValue(int var1) {
         return ArrayTable.this.new Row(var1);
      }

      Map<C, V> setValue(int var1, Map<C, V> var2) {
         throw new UnsupportedOperationException();
      }

      public Map<C, V> put(R var1, Map<C, V> var2) {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object put(Object var1, Object var2) {
         return this.put(var1, (Map)var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      Object setValue(int var1, Object var2) {
         return this.setValue(var1, (Map)var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      Object getValue(int var1) {
         return this.getValue(var1);
      }

      // $FF: synthetic method
      RowMap(Object var2) {
         this();
      }
   }

   private class Row extends ArrayTable.ArrayMap<C, V> {
      final int rowIndex;

      Row(int var2) {
         super(ArrayTable.this.columnKeyToIndex, null);
         this.rowIndex = var2;
      }

      String getKeyRole() {
         return "Column";
      }

      V getValue(int var1) {
         return ArrayTable.this.at(this.rowIndex, var1);
      }

      V setValue(int var1, V var2) {
         return ArrayTable.this.set(this.rowIndex, var1, var2);
      }
   }

   private class ColumnMap extends ArrayTable.ArrayMap<C, Map<R, V>> {
      private ColumnMap() {
         super(ArrayTable.this.columnKeyToIndex, null);
      }

      String getKeyRole() {
         return "Column";
      }

      Map<R, V> getValue(int var1) {
         return ArrayTable.this.new Column(var1);
      }

      Map<R, V> setValue(int var1, Map<R, V> var2) {
         throw new UnsupportedOperationException();
      }

      public Map<R, V> put(C var1, Map<R, V> var2) {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object put(Object var1, Object var2) {
         return this.put(var1, (Map)var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      Object setValue(int var1, Object var2) {
         return this.setValue(var1, (Map)var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      Object getValue(int var1) {
         return this.getValue(var1);
      }

      // $FF: synthetic method
      ColumnMap(Object var2) {
         this();
      }
   }

   private class Column extends ArrayTable.ArrayMap<R, V> {
      final int columnIndex;

      Column(int var2) {
         super(ArrayTable.this.rowKeyToIndex, null);
         this.columnIndex = var2;
      }

      String getKeyRole() {
         return "Row";
      }

      V getValue(int var1) {
         return ArrayTable.this.at(var1, this.columnIndex);
      }

      V setValue(int var1, V var2) {
         return ArrayTable.this.set(var1, this.columnIndex, var2);
      }
   }

   private abstract static class ArrayMap<K, V> extends Maps.ImprovedAbstractMap<K, V> {
      private final ImmutableMap<K, Integer> keyIndex;

      private ArrayMap(ImmutableMap<K, Integer> var1) {
         this.keyIndex = var1;
      }

      public Set<K> keySet() {
         return this.keyIndex.keySet();
      }

      K getKey(int var1) {
         return this.keyIndex.keySet().asList().get(var1);
      }

      abstract String getKeyRole();

      @Nullable
      abstract V getValue(int var1);

      @Nullable
      abstract V setValue(int var1, V var2);

      public int size() {
         return this.keyIndex.size();
      }

      public boolean isEmpty() {
         return this.keyIndex.isEmpty();
      }

      protected Set<Entry<K, V>> createEntrySet() {
         return new Maps.EntrySet() {
            Map<K, V> map() {
               return ArrayMap.this;
            }

            public Iterator<Entry<K, V>> iterator() {
               return new AbstractIndexedListIterator(this.size()) {
                  protected Entry<K, V> get(final int var1) {
                     return new AbstractMapEntry() {
                        public K getKey() {
                           return ArrayMap.this.getKey(var1);
                        }

                        public V getValue() {
                           return ArrayMap.this.getValue(var1);
                        }

                        public V setValue(V var1x) {
                           return ArrayMap.this.setValue(var1, var1x);
                        }
                     };
                  }

                  // $FF: synthetic method
                  // $FF: bridge method
                  protected Object get(int var1) {
                     return this.get(var1);
                  }
               };
            }
         };
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.keyIndex.containsKey(var1);
      }

      public V get(@Nullable Object var1) {
         Integer var2 = (Integer)this.keyIndex.get(var1);
         return var2 == null?null:this.getValue(var2.intValue());
      }

      public V put(K var1, V var2) {
         Integer var3 = (Integer)this.keyIndex.get(var1);
         if(var3 == null) {
            throw new IllegalArgumentException(this.getKeyRole() + " " + var1 + " not in " + this.keyIndex.keySet());
         } else {
            return this.setValue(var3.intValue(), var2);
         }
      }

      public V remove(Object var1) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      ArrayMap(ImmutableMap var1, Object var2) {
         this(var1);
      }
   }
}
