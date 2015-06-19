package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Cut;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.RangeSet;
import com.google.common.collect.RegularImmutableSortedMap;
import com.google.common.collect.RegularImmutableSortedSet;
import com.google.common.collect.SortedLists;
import com.google.common.collect.TreeRangeMap;
import com.google.common.collect.TreeRangeSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
@GwtIncompatible("NavigableMap")
public class ImmutableRangeMap<K extends Comparable<?>, V> implements RangeMap<K, V> {
   private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap(ImmutableList.of(), ImmutableList.of());
   private final ImmutableList<Range<K>> ranges;
   private final ImmutableList<V> values;

   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() {
      return EMPTY;
   }

   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> var0, V var1) {
      return new ImmutableRangeMap(ImmutableList.of(var0), ImmutableList.of(var1));
   }

   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> var0) {
      if(var0 instanceof ImmutableRangeMap) {
         return (ImmutableRangeMap)var0;
      } else {
         Map var1 = var0.asMapOfRanges();
         ImmutableList.Builder var2 = new ImmutableList.Builder(var1.size());
         ImmutableList.Builder var3 = new ImmutableList.Builder(var1.size());
         Iterator var4 = var1.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            var2.add(var5.getKey());
            var3.add(var5.getValue());
         }

         return new ImmutableRangeMap(var2.build(), var3.build());
      }
   }

   public static <K extends Comparable<?>, V> ImmutableRangeMap.Builder<K, V> builder() {
      return new ImmutableRangeMap.Builder();
   }

   ImmutableRangeMap(ImmutableList<Range<K>> var1, ImmutableList<V> var2) {
      this.ranges = var1;
      this.values = var2;
   }

   @Nullable
   public V get(K var1) {
      int var2 = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), (Comparable)Cut.belowValue(var1), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
      if(var2 == -1) {
         return null;
      } else {
         Range var3 = (Range)this.ranges.get(var2);
         return var3.contains(var1)?this.values.get(var2):null;
      }
   }

   @Nullable
   public Entry<Range<K>, V> getEntry(K var1) {
      int var2 = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), (Comparable)Cut.belowValue(var1), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
      if(var2 == -1) {
         return null;
      } else {
         Range var3 = (Range)this.ranges.get(var2);
         return var3.contains(var1)?Maps.immutableEntry(var3, this.values.get(var2)):null;
      }
   }

   public Range<K> span() {
      if(this.ranges.isEmpty()) {
         throw new NoSuchElementException();
      } else {
         Range var1 = (Range)this.ranges.get(0);
         Range var2 = (Range)this.ranges.get(this.ranges.size() - 1);
         return Range.create(var1.lowerBound, var2.upperBound);
      }
   }

   public void put(Range<K> var1, V var2) {
      throw new UnsupportedOperationException();
   }

   public void putAll(RangeMap<K, V> var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public void remove(Range<K> var1) {
      throw new UnsupportedOperationException();
   }

   public ImmutableMap<Range<K>, V> asMapOfRanges() {
      if(this.ranges.isEmpty()) {
         return ImmutableMap.of();
      } else {
         RegularImmutableSortedSet var1 = new RegularImmutableSortedSet(this.ranges, Range.RANGE_LEX_ORDERING);
         return new RegularImmutableSortedMap(var1, this.values);
      }
   }

   public ImmutableRangeMap<K, V> subRangeMap(final Range<K> var1) {
      if(((Range)Preconditions.checkNotNull(var1)).isEmpty()) {
         return of();
      } else if(!this.ranges.isEmpty() && !var1.encloses(this.span())) {
         final int var2 = SortedLists.binarySearch(this.ranges, (Function)Range.upperBoundFn(), (Comparable)var1.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
         int var3 = SortedLists.binarySearch(this.ranges, (Function)Range.lowerBoundFn(), (Comparable)var1.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
         if(var2 >= var3) {
            return of();
         } else {
            final int var5 = var3 - var2;
            final ImmutableList var6 = new ImmutableList() {
               public int size() {
                  return var5;
               }

               public Range<K> get(int var1x) {
                  Preconditions.checkElementIndex(var1x, var5);
                  return var1x != 0 && var1x != var5 - 1?(Range)ImmutableRangeMap.this.ranges.get(var1x + var2):((Range)ImmutableRangeMap.this.ranges.get(var1x + var2)).intersection(var1);
               }

               boolean isPartialView() {
                  return true;
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object get(int var1x) {
                  return this.get(var1x);
               }
            };
            return new ImmutableRangeMap(var6, this.values.subList(var2, var3)) {
               public ImmutableRangeMap<K, V> subRangeMap(Range<K> var1x) {
                  return var1.isConnected(var1x)?ImmutableRangeMap.this.subRangeMap(var1x.intersection(var1)):ImmutableRangeMap.EMPTY;
               }

               // $FF: synthetic method
               // $FF: bridge method
               public RangeMap subRangeMap(Range var1x) {
                  return this.subRangeMap(var1x);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Map asMapOfRanges() {
                  return super.asMapOfRanges();
               }
            };
         }
      } else {
         return this;
      }
   }

   public int hashCode() {
      return this.asMapOfRanges().hashCode();
   }

   public boolean equals(@Nullable Object var1) {
      if(var1 instanceof RangeMap) {
         RangeMap var2 = (RangeMap)var1;
         return this.asMapOfRanges().equals(var2.asMapOfRanges());
      } else {
         return false;
      }
   }

   public String toString() {
      return this.asMapOfRanges().toString();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public RangeMap subRangeMap(Range var1) {
      return this.subRangeMap(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map asMapOfRanges() {
      return this.asMapOfRanges();
   }

   public static final class Builder<K extends Comparable<?>, V> {
      private final RangeSet<K> keyRanges = TreeRangeSet.create();
      private final RangeMap<K, V> rangeMap = TreeRangeMap.create();

      public Builder() {
      }

      public ImmutableRangeMap.Builder<K, V> put(Range<K> var1, V var2) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
         Preconditions.checkArgument(!var1.isEmpty(), "Range must not be empty, but was %s", new Object[]{var1});
         if(!this.keyRanges.complement().encloses(var1)) {
            Iterator var3 = this.rangeMap.asMapOfRanges().entrySet().iterator();

            while(var3.hasNext()) {
               Entry var4 = (Entry)var3.next();
               Range var5 = (Range)var4.getKey();
               if(var5.isConnected(var1) && !var5.intersection(var1).isEmpty()) {
                  throw new IllegalArgumentException("Overlapping ranges: range " + var1 + " overlaps with entry " + var4);
               }
            }
         }

         this.keyRanges.add(var1);
         this.rangeMap.put(var1, var2);
         return this;
      }

      public ImmutableRangeMap.Builder<K, V> putAll(RangeMap<K, ? extends V> var1) {
         Iterator var2 = var1.asMapOfRanges().entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            this.put((Range)var3.getKey(), var3.getValue());
         }

         return this;
      }

      public ImmutableRangeMap<K, V> build() {
         Map var1 = this.rangeMap.asMapOfRanges();
         ImmutableList.Builder var2 = new ImmutableList.Builder(var1.size());
         ImmutableList.Builder var3 = new ImmutableList.Builder(var1.size());
         Iterator var4 = var1.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            var2.add(var5.getKey());
            var3.add(var5.getValue());
         }

         return new ImmutableRangeMap(var2.build(), var3.build());
      }
   }
}
