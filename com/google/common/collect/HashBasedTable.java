package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Supplier;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.StandardTable;
import com.google.common.collect.Table;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
public class HashBasedTable<R, C, V> extends StandardTable<R, C, V> {
   private static final long serialVersionUID = 0L;

   public static <R, C, V> HashBasedTable<R, C, V> create() {
      return new HashBasedTable(new HashMap(), new HashBasedTable.Factory(0));
   }

   public static <R, C, V> HashBasedTable<R, C, V> create(int var0, int var1) {
      CollectPreconditions.checkNonnegative(var1, "expectedCellsPerRow");
      HashMap var2 = Maps.newHashMapWithExpectedSize(var0);
      return new HashBasedTable(var2, new HashBasedTable.Factory(var1));
   }

   public static <R, C, V> HashBasedTable<R, C, V> create(Table<? extends R, ? extends C, ? extends V> var0) {
      HashBasedTable var1 = create();
      var1.putAll(var0);
      return var1;
   }

   HashBasedTable(Map<R, Map<C, V>> var1, HashBasedTable.Factory<C, V> var2) {
      super(var1, var2);
   }

   public boolean contains(@Nullable Object var1, @Nullable Object var2) {
      return super.contains(var1, var2);
   }

   public boolean containsColumn(@Nullable Object var1) {
      return super.containsColumn(var1);
   }

   public boolean containsRow(@Nullable Object var1) {
      return super.containsRow(var1);
   }

   public boolean containsValue(@Nullable Object var1) {
      return super.containsValue(var1);
   }

   public V get(@Nullable Object var1, @Nullable Object var2) {
      return super.get(var1, var2);
   }

   public boolean equals(@Nullable Object var1) {
      return super.equals(var1);
   }

   public V remove(@Nullable Object var1, @Nullable Object var2) {
      return super.remove(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map columnMap() {
      return super.columnMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map rowMap() {
      return super.rowMap();
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
   public Set rowKeySet() {
      return super.rowKeySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map column(Object var1) {
      return super.column(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map row(Object var1) {
      return super.row(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set cellSet() {
      return super.cellSet();
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
   public void putAll(Table var1) {
      super.putAll(var1);
   }

   private static class Factory<C, V> implements Supplier<Map<C, V>>, Serializable {
      final int expectedSize;
      private static final long serialVersionUID = 0L;

      Factory(int var1) {
         this.expectedSize = var1;
      }

      public Map<C, V> get() {
         return Maps.newHashMapWithExpectedSize(this.expectedSize);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get() {
         return this.get();
      }
   }
}
