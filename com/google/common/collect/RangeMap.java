package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.collect.Range;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
public interface RangeMap<K extends Comparable, V> {
   @Nullable
   V get(K var1);

   @Nullable
   Entry<Range<K>, V> getEntry(K var1);

   Range<K> span();

   void put(Range<K> var1, V var2);

   void putAll(RangeMap<K, V> var1);

   void clear();

   void remove(Range<K> var1);

   Map<Range<K>, V> asMapOfRanges();

   RangeMap<K, V> subRangeMap(Range<K> var1);

   boolean equals(@Nullable Object var1);

   int hashCode();

   String toString();
}
