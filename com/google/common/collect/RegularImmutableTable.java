package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.DenseImmutableTable;
import com.google.common.collect.ImmutableAsList;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Lists;
import com.google.common.collect.SparseImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
   RegularImmutableTable() {
   }

   abstract Table.Cell<R, C, V> getCell(int var1);

   final ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
      return (ImmutableSet)(this.isEmpty()?ImmutableSet.of():new RegularImmutableTable.CellSet(null));
   }

   abstract V getValue(int var1);

   final ImmutableCollection<V> createValues() {
      return (ImmutableCollection)(this.isEmpty()?ImmutableList.of():new RegularImmutableTable.Values(null));
   }

   static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> var0, @Nullable final Comparator<? super R> var1, @Nullable final Comparator<? super C> var2) {
      Preconditions.checkNotNull(var0);
      if(var1 != null || var2 != null) {
         Comparator var3 = new Comparator() {
            public int compare(Table.Cell<R, C, V> var1x, Table.Cell<R, C, V> var2x) {
               int var3 = var1 == null?0:var1.compare(var1x.getRowKey(), var2x.getRowKey());
               return var3 != 0?var3:(var2 == null?0:var2.compare(var1x.getColumnKey(), var2x.getColumnKey()));
            }

            // $FF: synthetic method
            // $FF: bridge method
            public int compare(Object var1x, Object var2x) {
               return this.compare((Table.Cell)var1x, (Table.Cell)var2x);
            }
         };
         Collections.sort(var0, var3);
      }

      return forCellsInternal(var0, var1, var2);
   }

   static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> var0) {
      return forCellsInternal(var0, (Comparator)null, (Comparator)null);
   }

   private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> var0, @Nullable Comparator<? super R> var1, @Nullable Comparator<? super C> var2) {
      ImmutableSet.Builder var3 = ImmutableSet.builder();
      ImmutableSet.Builder var4 = ImmutableSet.builder();
      ImmutableList var5 = ImmutableList.copyOf(var0);
      Iterator var6 = var5.iterator();

      while(var6.hasNext()) {
         Table.Cell var7 = (Table.Cell)var6.next();
         var3.add(var7.getRowKey());
         var4.add(var7.getColumnKey());
      }

      ImmutableSet var9 = var3.build();
      if(var1 != null) {
         ArrayList var10 = Lists.newArrayList((Iterable)var9);
         Collections.sort(var10, var1);
         var9 = ImmutableSet.copyOf((Collection)var10);
      }

      ImmutableSet var11 = var4.build();
      if(var2 != null) {
         ArrayList var8 = Lists.newArrayList((Iterable)var11);
         Collections.sort(var8, var2);
         var11 = ImmutableSet.copyOf((Collection)var8);
      }

      return (RegularImmutableTable)((long)var5.size() > (long)var9.size() * (long)var11.size() / 2L?new DenseImmutableTable(var5, var9, var11):new SparseImmutableTable(var5, var9, var11));
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

   private final class Values extends ImmutableList<V> {
      private Values() {
      }

      public int size() {
         return RegularImmutableTable.this.size();
      }

      public V get(int var1) {
         return RegularImmutableTable.this.getValue(var1);
      }

      boolean isPartialView() {
         return true;
      }

      // $FF: synthetic method
      Values(Object var2) {
         this();
      }
   }

   private final class CellSet extends ImmutableSet<Table.Cell<R, C, V>> {
      private CellSet() {
      }

      public int size() {
         return RegularImmutableTable.this.size();
      }

      public UnmodifiableIterator<Table.Cell<R, C, V>> iterator() {
         return this.asList().iterator();
      }

      ImmutableList<Table.Cell<R, C, V>> createAsList() {
         return new ImmutableAsList() {
            public Table.Cell<R, C, V> get(int var1) {
               return RegularImmutableTable.this.getCell(var1);
            }

            ImmutableCollection<Table.Cell<R, C, V>> delegateCollection() {
               return CellSet.this;
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object get(int var1) {
               return this.get(var1);
            }
         };
      }

      public boolean contains(@Nullable Object var1) {
         if(!(var1 instanceof Table.Cell)) {
            return false;
         } else {
            Table.Cell var2 = (Table.Cell)var1;
            Object var3 = RegularImmutableTable.this.get(var2.getRowKey(), var2.getColumnKey());
            return var3 != null && var3.equals(var2.getValue());
         }
      }

      boolean isPartialView() {
         return false;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Iterator iterator() {
         return this.iterator();
      }

      // $FF: synthetic method
      CellSet(Object var2) {
         this();
      }
   }
}
