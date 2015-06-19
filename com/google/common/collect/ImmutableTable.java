package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractTable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.RegularImmutableTable;
import com.google.common.collect.SingletonImmutableTable;
import com.google.common.collect.SparseImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ImmutableTable<R, C, V> extends AbstractTable<R, C, V> {
   private static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable(ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());

   public static <R, C, V> ImmutableTable<R, C, V> of() {
      return EMPTY;
   }

   public static <R, C, V> ImmutableTable<R, C, V> of(R var0, C var1, V var2) {
      return new SingletonImmutableTable(var0, var1, var2);
   }

   public static <R, C, V> ImmutableTable<R, C, V> copyOf(Table<? extends R, ? extends C, ? extends V> var0) {
      if(var0 instanceof ImmutableTable) {
         ImmutableTable var6 = (ImmutableTable)var0;
         return var6;
      } else {
         int var1 = var0.size();
         switch(var1) {
         case 0:
            return of();
         case 1:
            Table.Cell var2 = (Table.Cell)Iterables.getOnlyElement(var0.cellSet());
            return of(var2.getRowKey(), var2.getColumnKey(), var2.getValue());
         default:
            ImmutableSet.Builder var3 = ImmutableSet.builder();
            Iterator var4 = var0.cellSet().iterator();

            while(var4.hasNext()) {
               Table.Cell var5 = (Table.Cell)var4.next();
               var3.add((Object)cellOf(var5.getRowKey(), var5.getColumnKey(), var5.getValue()));
            }

            return RegularImmutableTable.forCells(var3.build());
         }
      }
   }

   public static <R, C, V> ImmutableTable.Builder<R, C, V> builder() {
      return new ImmutableTable.Builder();
   }

   static <R, C, V> Table.Cell<R, C, V> cellOf(R var0, C var1, V var2) {
      return Tables.immutableCell(Preconditions.checkNotNull(var0), Preconditions.checkNotNull(var1), Preconditions.checkNotNull(var2));
   }

   ImmutableTable() {
   }

   public ImmutableSet<Table.Cell<R, C, V>> cellSet() {
      return (ImmutableSet)super.cellSet();
   }

   abstract ImmutableSet<Table.Cell<R, C, V>> createCellSet();

   final UnmodifiableIterator<Table.Cell<R, C, V>> cellIterator() {
      throw new AssertionError("should never be called");
   }

   public ImmutableCollection<V> values() {
      return (ImmutableCollection)super.values();
   }

   abstract ImmutableCollection<V> createValues();

   final Iterator<V> valuesIterator() {
      throw new AssertionError("should never be called");
   }

   public ImmutableMap<R, V> column(C var1) {
      Preconditions.checkNotNull(var1);
      return (ImmutableMap)Objects.firstNonNull((ImmutableMap)this.columnMap().get(var1), ImmutableMap.of());
   }

   public ImmutableSet<C> columnKeySet() {
      return this.columnMap().keySet();
   }

   public abstract ImmutableMap<C, Map<R, V>> columnMap();

   public ImmutableMap<C, V> row(R var1) {
      Preconditions.checkNotNull(var1);
      return (ImmutableMap)Objects.firstNonNull((ImmutableMap)this.rowMap().get(var1), ImmutableMap.of());
   }

   public ImmutableSet<R> rowKeySet() {
      return this.rowMap().keySet();
   }

   public abstract ImmutableMap<R, Map<C, V>> rowMap();

   public boolean contains(@Nullable Object var1, @Nullable Object var2) {
      return this.get(var1, var2) != null;
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.values().contains(var1);
   }

   /** @deprecated */
   @Deprecated
   public final void clear() {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final V put(R var1, C var2, V var3) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final void putAll(Table<? extends R, ? extends C, ? extends V> var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final V remove(Object var1, Object var2) {
      throw new UnsupportedOperationException();
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
   Collection createValues() {
      return this.createValues();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection values() {
      return this.values();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Iterator cellIterator() {
      return this.cellIterator();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Set createCellSet() {
      return this.createCellSet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set cellSet() {
      return this.cellSet();
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
   public Set columnKeySet() {
      return this.columnKeySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set rowKeySet() {
      return this.rowKeySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsColumn(Object var1) {
      return super.containsColumn(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsRow(Object var1) {
      return super.containsRow(var1);
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

   // $FF: synthetic method
   // $FF: bridge method
   public Map row(Object var1) {
      return this.row(var1);
   }

   public static final class Builder<R, C, V> {
      private final List<Table.Cell<R, C, V>> cells = Lists.newArrayList();
      private Comparator<? super R> rowComparator;
      private Comparator<? super C> columnComparator;

      public Builder() {
      }

      public ImmutableTable.Builder<R, C, V> orderRowsBy(Comparator<? super R> var1) {
         this.rowComparator = (Comparator)Preconditions.checkNotNull(var1);
         return this;
      }

      public ImmutableTable.Builder<R, C, V> orderColumnsBy(Comparator<? super C> var1) {
         this.columnComparator = (Comparator)Preconditions.checkNotNull(var1);
         return this;
      }

      public ImmutableTable.Builder<R, C, V> put(R var1, C var2, V var3) {
         this.cells.add(ImmutableTable.cellOf(var1, var2, var3));
         return this;
      }

      public ImmutableTable.Builder<R, C, V> put(Table.Cell<? extends R, ? extends C, ? extends V> var1) {
         if(var1 instanceof Tables.ImmutableCell) {
            Preconditions.checkNotNull(var1.getRowKey());
            Preconditions.checkNotNull(var1.getColumnKey());
            Preconditions.checkNotNull(var1.getValue());
            this.cells.add(var1);
         } else {
            this.put(var1.getRowKey(), var1.getColumnKey(), var1.getValue());
         }

         return this;
      }

      public ImmutableTable.Builder<R, C, V> putAll(Table<? extends R, ? extends C, ? extends V> var1) {
         Iterator var2 = var1.cellSet().iterator();

         while(var2.hasNext()) {
            Table.Cell var3 = (Table.Cell)var2.next();
            this.put(var3);
         }

         return this;
      }

      public ImmutableTable<R, C, V> build() {
         int var1 = this.cells.size();
         switch(var1) {
         case 0:
            return ImmutableTable.EMPTY;
         case 1:
            return new SingletonImmutableTable((Table.Cell)Iterables.getOnlyElement(this.cells));
         default:
            return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
         }
      }
   }
}
