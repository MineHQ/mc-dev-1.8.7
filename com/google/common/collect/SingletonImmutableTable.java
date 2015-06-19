package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@GwtCompatible
class SingletonImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
   final R singleRowKey;
   final C singleColumnKey;
   final V singleValue;

   SingletonImmutableTable(R var1, C var2, V var3) {
      this.singleRowKey = Preconditions.checkNotNull(var1);
      this.singleColumnKey = Preconditions.checkNotNull(var2);
      this.singleValue = Preconditions.checkNotNull(var3);
   }

   SingletonImmutableTable(Table.Cell<R, C, V> var1) {
      this(var1.getRowKey(), var1.getColumnKey(), var1.getValue());
   }

   public ImmutableMap<R, V> column(C var1) {
      Preconditions.checkNotNull(var1);
      return this.containsColumn(var1)?ImmutableMap.of(this.singleRowKey, this.singleValue):ImmutableMap.of();
   }

   public ImmutableMap<C, Map<R, V>> columnMap() {
      return ImmutableMap.of(this.singleColumnKey, ImmutableMap.of(this.singleRowKey, this.singleValue));
   }

   public ImmutableMap<R, Map<C, V>> rowMap() {
      return ImmutableMap.of(this.singleRowKey, ImmutableMap.of(this.singleColumnKey, this.singleValue));
   }

   public int size() {
      return 1;
   }

   ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
      return ImmutableSet.of(cellOf(this.singleRowKey, this.singleColumnKey, this.singleValue));
   }

   ImmutableCollection<V> createValues() {
      return ImmutableSet.of(this.singleValue);
   }

   // $FF: synthetic method
   // $FF: bridge method
   Collection createValues() {
      return this.createValues();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Set createCellSet() {
      return this.createCellSet();
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

   // $FF: synthetic method
   // $FF: bridge method
   public Map column(Object var1) {
      return this.column(var1);
   }
}
