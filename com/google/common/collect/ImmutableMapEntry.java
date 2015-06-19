package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ImmutableEntry;
import javax.annotation.Nullable;

@GwtIncompatible("unnecessary")
abstract class ImmutableMapEntry<K, V> extends ImmutableEntry<K, V> {
   ImmutableMapEntry(K var1, V var2) {
      super(var1, var2);
      CollectPreconditions.checkEntryNotNull(var1, var2);
   }

   ImmutableMapEntry(ImmutableMapEntry<K, V> var1) {
      super(var1.getKey(), var1.getValue());
   }

   @Nullable
   abstract ImmutableMapEntry<K, V> getNextInKeyBucket();

   @Nullable
   abstract ImmutableMapEntry<K, V> getNextInValueBucket();

   static final class TerminalEntry<K, V> extends ImmutableMapEntry<K, V> {
      TerminalEntry(ImmutableMapEntry<K, V> var1) {
         super(var1);
      }

      TerminalEntry(K var1, V var2) {
         super(var1, var2);
      }

      @Nullable
      ImmutableMapEntry<K, V> getNextInKeyBucket() {
         return null;
      }

      @Nullable
      ImmutableMapEntry<K, V> getNextInValueBucket() {
         return null;
      }
   }
}
