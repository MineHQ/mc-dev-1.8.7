package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.SortedSetMultimap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

@Beta
@GwtCompatible
public abstract class MultimapBuilder<K0, V0> {
   private static final int DEFAULT_EXPECTED_KEYS = 8;

   private MultimapBuilder() {
   }

   public static MultimapBuilder.MultimapBuilderWithKeys<Object> hashKeys() {
      return hashKeys(8);
   }

   public static MultimapBuilder.MultimapBuilderWithKeys<Object> hashKeys(final int var0) {
      CollectPreconditions.checkNonnegative(var0, "expectedKeys");
      return new MultimapBuilder.MultimapBuilderWithKeys() {
         <K, V> Map<K, Collection<V>> createMap() {
            return new HashMap(var0);
         }
      };
   }

   public static MultimapBuilder.MultimapBuilderWithKeys<Object> linkedHashKeys() {
      return linkedHashKeys(8);
   }

   public static MultimapBuilder.MultimapBuilderWithKeys<Object> linkedHashKeys(final int var0) {
      CollectPreconditions.checkNonnegative(var0, "expectedKeys");
      return new MultimapBuilder.MultimapBuilderWithKeys() {
         <K, V> Map<K, Collection<V>> createMap() {
            return new LinkedHashMap(var0);
         }
      };
   }

   public static MultimapBuilder.MultimapBuilderWithKeys<Comparable> treeKeys() {
      return treeKeys(Ordering.natural());
   }

   public static <K0> MultimapBuilder.MultimapBuilderWithKeys<K0> treeKeys(final Comparator<K0> var0) {
      Preconditions.checkNotNull(var0);
      return new MultimapBuilder.MultimapBuilderWithKeys() {
         <K extends K0, V> Map<K, Collection<V>> createMap() {
            return new TreeMap(var0);
         }
      };
   }

   public static <K0 extends Enum<K0>> MultimapBuilder.MultimapBuilderWithKeys<K0> enumKeys(final Class<K0> var0) {
      Preconditions.checkNotNull(var0);
      return new MultimapBuilder.MultimapBuilderWithKeys() {
         <K extends K0, V> Map<K, Collection<V>> createMap() {
            return new EnumMap(var0);
         }
      };
   }

   public abstract <K extends K0, V extends V0> Multimap<K, V> build();

   public <K extends K0, V extends V0> Multimap<K, V> build(Multimap<? extends K, ? extends V> var1) {
      Multimap var2 = this.build();
      var2.putAll(var1);
      return var2;
   }

   // $FF: synthetic method
   MultimapBuilder(Object var1) {
      this();
   }

   public abstract static class SortedSetMultimapBuilder<K0, V0> extends MultimapBuilder.SetMultimapBuilder<K0, V0> {
      SortedSetMultimapBuilder() {
      }

      public abstract <K extends K0, V extends V0> SortedSetMultimap<K, V> build();

      public <K extends K0, V extends V0> SortedSetMultimap<K, V> build(Multimap<? extends K, ? extends V> var1) {
         return (SortedSetMultimap)super.build(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public SetMultimap build(Multimap var1) {
         return this.build(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public SetMultimap build() {
         return this.build();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Multimap build(Multimap var1) {
         return this.build(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Multimap build() {
         return this.build();
      }
   }

   public abstract static class SetMultimapBuilder<K0, V0> extends MultimapBuilder<K0, V0> {
      SetMultimapBuilder() {
         super(null);
      }

      public abstract <K extends K0, V extends V0> SetMultimap<K, V> build();

      public <K extends K0, V extends V0> SetMultimap<K, V> build(Multimap<? extends K, ? extends V> var1) {
         return (SetMultimap)super.build(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Multimap build(Multimap var1) {
         return this.build(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Multimap build() {
         return this.build();
      }
   }

   public abstract static class ListMultimapBuilder<K0, V0> extends MultimapBuilder<K0, V0> {
      ListMultimapBuilder() {
         super(null);
      }

      public abstract <K extends K0, V extends V0> ListMultimap<K, V> build();

      public <K extends K0, V extends V0> ListMultimap<K, V> build(Multimap<? extends K, ? extends V> var1) {
         return (ListMultimap)super.build(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Multimap build(Multimap var1) {
         return this.build(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Multimap build() {
         return this.build();
      }
   }

   public abstract static class MultimapBuilderWithKeys<K0> {
      private static final int DEFAULT_EXPECTED_VALUES_PER_KEY = 2;

      MultimapBuilderWithKeys() {
      }

      abstract <K extends K0, V> Map<K, Collection<V>> createMap();

      public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues() {
         return this.arrayListValues(2);
      }

      public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues(final int var1) {
         CollectPreconditions.checkNonnegative(var1, "expectedValuesPerKey");
         return new MultimapBuilder.ListMultimapBuilder() {
            public <K extends K0, V> ListMultimap<K, V> build() {
               return Multimaps.newListMultimap(MultimapBuilderWithKeys.this.createMap(), new MultimapBuilder.ArrayListSupplier(var1));
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Multimap build() {
               return this.build();
            }
         };
      }

      public MultimapBuilder.ListMultimapBuilder<K0, Object> linkedListValues() {
         return new MultimapBuilder.ListMultimapBuilder() {
            public <K extends K0, V> ListMultimap<K, V> build() {
               return Multimaps.newListMultimap(MultimapBuilderWithKeys.this.createMap(), MultimapBuilder.LinkedListSupplier.instance());
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Multimap build() {
               return this.build();
            }
         };
      }

      public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues() {
         return this.hashSetValues(2);
      }

      public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues(final int var1) {
         CollectPreconditions.checkNonnegative(var1, "expectedValuesPerKey");
         return new MultimapBuilder.SetMultimapBuilder() {
            public <K extends K0, V> SetMultimap<K, V> build() {
               return Multimaps.newSetMultimap(MultimapBuilderWithKeys.this.createMap(), new MultimapBuilder.HashSetSupplier(var1));
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Multimap build() {
               return this.build();
            }
         };
      }

      public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues() {
         return this.linkedHashSetValues(2);
      }

      public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues(final int var1) {
         CollectPreconditions.checkNonnegative(var1, "expectedValuesPerKey");
         return new MultimapBuilder.SetMultimapBuilder() {
            public <K extends K0, V> SetMultimap<K, V> build() {
               return Multimaps.newSetMultimap(MultimapBuilderWithKeys.this.createMap(), new MultimapBuilder.LinkedHashSetSupplier(var1));
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Multimap build() {
               return this.build();
            }
         };
      }

      public MultimapBuilder.SortedSetMultimapBuilder<K0, Comparable> treeSetValues() {
         return this.treeSetValues(Ordering.natural());
      }

      public <V0> MultimapBuilder.SortedSetMultimapBuilder<K0, V0> treeSetValues(final Comparator<V0> var1) {
         Preconditions.checkNotNull(var1, "comparator");
         return new MultimapBuilder.SortedSetMultimapBuilder() {
            public <K extends K0, V extends V0> SortedSetMultimap<K, V> build() {
               return Multimaps.newSortedSetMultimap(MultimapBuilderWithKeys.this.createMap(), new MultimapBuilder.TreeSetSupplier(var1));
            }

            // $FF: synthetic method
            // $FF: bridge method
            public SetMultimap build() {
               return this.build();
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Multimap build() {
               return this.build();
            }
         };
      }

      public <V0 extends Enum<V0>> MultimapBuilder.SetMultimapBuilder<K0, V0> enumSetValues(final Class<V0> var1) {
         Preconditions.checkNotNull(var1, "valueClass");
         return new MultimapBuilder.SetMultimapBuilder() {
            public <K extends K0, V extends V0> SetMultimap<K, V> build() {
               MultimapBuilder.EnumSetSupplier var1x = new MultimapBuilder.EnumSetSupplier(var1);
               return Multimaps.newSetMultimap(MultimapBuilderWithKeys.this.createMap(), var1x);
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Multimap build() {
               return this.build();
            }
         };
      }
   }

   private static final class EnumSetSupplier<V extends Enum<V>> implements Supplier<Set<V>>, Serializable {
      private final Class<V> clazz;

      EnumSetSupplier(Class<V> var1) {
         this.clazz = (Class)Preconditions.checkNotNull(var1);
      }

      public Set<V> get() {
         return EnumSet.noneOf(this.clazz);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get() {
         return this.get();
      }
   }

   private static final class TreeSetSupplier<V> implements Supplier<SortedSet<V>>, Serializable {
      private final Comparator<? super V> comparator;

      TreeSetSupplier(Comparator<? super V> var1) {
         this.comparator = (Comparator)Preconditions.checkNotNull(var1);
      }

      public SortedSet<V> get() {
         return new TreeSet(this.comparator);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get() {
         return this.get();
      }
   }

   private static final class LinkedHashSetSupplier<V> implements Supplier<Set<V>>, Serializable {
      private final int expectedValuesPerKey;

      LinkedHashSetSupplier(int var1) {
         this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(var1, "expectedValuesPerKey");
      }

      public Set<V> get() {
         return new LinkedHashSet(this.expectedValuesPerKey);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get() {
         return this.get();
      }
   }

   private static final class HashSetSupplier<V> implements Supplier<Set<V>>, Serializable {
      private final int expectedValuesPerKey;

      HashSetSupplier(int var1) {
         this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(var1, "expectedValuesPerKey");
      }

      public Set<V> get() {
         return new HashSet(this.expectedValuesPerKey);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get() {
         return this.get();
      }
   }

   private static enum LinkedListSupplier implements Supplier<List<Object>> {
      INSTANCE;

      private LinkedListSupplier() {
      }

      public static <V> Supplier<List<V>> instance() {
         MultimapBuilder.LinkedListSupplier var0 = INSTANCE;
         return var0;
      }

      public List<Object> get() {
         return new LinkedList();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get() {
         return this.get();
      }
   }

   private static final class ArrayListSupplier<V> implements Supplier<List<V>>, Serializable {
      private final int expectedValuesPerKey;

      ArrayListSupplier(int var1) {
         this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(var1, "expectedValuesPerKey");
      }

      public List<V> get() {
         return new ArrayList(this.expectedValuesPerKey);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get() {
         return this.get();
      }
   }
}
