package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapEntry;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableEnumMap;
import com.google.common.collect.ImmutableMapEntry;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableMapKeySet;
import com.google.common.collect.ImmutableMapValues;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.RegularImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
   private static final Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Entry[0];
   private transient ImmutableSet<Entry<K, V>> entrySet;
   private transient ImmutableSet<K> keySet;
   private transient ImmutableCollection<V> values;
   private transient ImmutableSetMultimap<K, V> multimapView;

   public static <K, V> ImmutableMap<K, V> of() {
      return ImmutableBiMap.of();
   }

   public static <K, V> ImmutableMap<K, V> of(K var0, V var1) {
      return ImmutableBiMap.of(var0, var1);
   }

   public static <K, V> ImmutableMap<K, V> of(K var0, V var1, K var2, V var3) {
      return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3)});
   }

   public static <K, V> ImmutableMap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5) {
      return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5)});
   }

   public static <K, V> ImmutableMap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5, K var6, V var7) {
      return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5), entryOf(var6, var7)});
   }

   public static <K, V> ImmutableMap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5, K var6, V var7, K var8, V var9) {
      return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[]{entryOf(var0, var1), entryOf(var2, var3), entryOf(var4, var5), entryOf(var6, var7), entryOf(var8, var9)});
   }

   static <K, V> ImmutableMapEntry.TerminalEntry<K, V> entryOf(K var0, V var1) {
      CollectPreconditions.checkEntryNotNull(var0, var1);
      return new ImmutableMapEntry.TerminalEntry(var0, var1);
   }

   public static <K, V> ImmutableMap.Builder<K, V> builder() {
      return new ImmutableMap.Builder();
   }

   static void checkNoConflict(boolean var0, String var1, Entry<?, ?> var2, Entry<?, ?> var3) {
      if(!var0) {
         throw new IllegalArgumentException("Multiple entries with same " + var1 + ": " + var2 + " and " + var3);
      }
   }

   public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> var0) {
      if(var0 instanceof ImmutableMap && !(var0 instanceof ImmutableSortedMap)) {
         ImmutableMap var1 = (ImmutableMap)var0;
         if(!var1.isPartialView()) {
            return var1;
         }
      } else if(var0 instanceof EnumMap) {
         return copyOfEnumMapUnsafe(var0);
      }

      Entry[] var3 = (Entry[])var0.entrySet().toArray(EMPTY_ENTRY_ARRAY);
      switch(var3.length) {
      case 0:
         return of();
      case 1:
         Entry var2 = var3[0];
         return of(var2.getKey(), var2.getValue());
      default:
         return new RegularImmutableMap(var3);
      }
   }

   private static <K, V> ImmutableMap<K, V> copyOfEnumMapUnsafe(Map<? extends K, ? extends V> var0) {
      return copyOfEnumMap((EnumMap)var0);
   }

   private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(Map<K, ? extends V> var0) {
      EnumMap var1 = new EnumMap(var0);
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         CollectPreconditions.checkEntryNotNull(var3.getKey(), var3.getValue());
      }

      return ImmutableEnumMap.asImmutable(var1);
   }

   ImmutableMap() {
   }

   /** @deprecated */
   @Deprecated
   public final V put(K var1, V var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final V remove(Object var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final void putAll(Map<? extends K, ? extends V> var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public final void clear() {
      throw new UnsupportedOperationException();
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.get(var1) != null;
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.values().contains(var1);
   }

   public abstract V get(@Nullable Object var1);

   public ImmutableSet<Entry<K, V>> entrySet() {
      ImmutableSet var1 = this.entrySet;
      return var1 == null?(this.entrySet = this.createEntrySet()):var1;
   }

   abstract ImmutableSet<Entry<K, V>> createEntrySet();

   public ImmutableSet<K> keySet() {
      ImmutableSet var1 = this.keySet;
      return var1 == null?(this.keySet = this.createKeySet()):var1;
   }

   ImmutableSet<K> createKeySet() {
      return new ImmutableMapKeySet(this);
   }

   public ImmutableCollection<V> values() {
      ImmutableCollection var1 = this.values;
      return var1 == null?(this.values = new ImmutableMapValues(this)):var1;
   }

   @Beta
   public ImmutableSetMultimap<K, V> asMultimap() {
      ImmutableSetMultimap var1 = this.multimapView;
      return var1 == null?(this.multimapView = this.createMultimapView()):var1;
   }

   private ImmutableSetMultimap<K, V> createMultimapView() {
      ImmutableMap var1 = this.viewMapValuesAsSingletonSets();
      return new ImmutableSetMultimap(var1, var1.size(), (Comparator)null);
   }

   private ImmutableMap<K, ImmutableSet<V>> viewMapValuesAsSingletonSets() {
      return new ImmutableMap.MapViewOfValuesAsSingletonSets(this);
   }

   public boolean equals(@Nullable Object var1) {
      return Maps.equalsImpl(this, var1);
   }

   abstract boolean isPartialView();

   public int hashCode() {
      return this.entrySet().hashCode();
   }

   public String toString() {
      return Maps.toStringImpl(this);
   }

   Object writeReplace() {
      return new ImmutableMap.SerializedForm(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set entrySet() {
      return this.entrySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection values() {
      return this.values();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set keySet() {
      return this.keySet();
   }

   static class SerializedForm implements Serializable {
      private final Object[] keys;
      private final Object[] values;
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableMap<?, ?> var1) {
         this.keys = new Object[var1.size()];
         this.values = new Object[var1.size()];
         int var2 = 0;

         for(Iterator var3 = var1.entrySet().iterator(); var3.hasNext(); ++var2) {
            Entry var4 = (Entry)var3.next();
            this.keys[var2] = var4.getKey();
            this.values[var2] = var4.getValue();
         }

      }

      Object readResolve() {
         ImmutableMap.Builder var1 = new ImmutableMap.Builder();
         return this.createMap(var1);
      }

      Object createMap(ImmutableMap.Builder<Object, Object> var1) {
         for(int var2 = 0; var2 < this.keys.length; ++var2) {
            var1.put(this.keys[var2], this.values[var2]);
         }

         return var1.build();
      }
   }

   private static final class MapViewOfValuesAsSingletonSets<K, V> extends ImmutableMap<K, ImmutableSet<V>> {
      private final ImmutableMap<K, V> delegate;

      MapViewOfValuesAsSingletonSets(ImmutableMap<K, V> var1) {
         this.delegate = (ImmutableMap)Preconditions.checkNotNull(var1);
      }

      public int size() {
         return this.delegate.size();
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.delegate.containsKey(var1);
      }

      public ImmutableSet<V> get(@Nullable Object var1) {
         Object var2 = this.delegate.get(var1);
         return var2 == null?null:ImmutableSet.of(var2);
      }

      boolean isPartialView() {
         return false;
      }

      ImmutableSet<Entry<K, ImmutableSet<V>>> createEntrySet() {
         return new ImmutableMapEntrySet() {
            ImmutableMap<K, ImmutableSet<V>> map() {
               return MapViewOfValuesAsSingletonSets.this;
            }

            public UnmodifiableIterator<Entry<K, ImmutableSet<V>>> iterator() {
               final UnmodifiableIterator var1 = MapViewOfValuesAsSingletonSets.this.delegate.entrySet().iterator();
               return new UnmodifiableIterator() {
                  public boolean hasNext() {
                     return var1.hasNext();
                  }

                  public Entry<K, ImmutableSet<V>> next() {
                     final Entry var1x = (Entry)var1.next();
                     return new AbstractMapEntry() {
                        public K getKey() {
                           return var1x.getKey();
                        }

                        public ImmutableSet<V> getValue() {
                           return ImmutableSet.of(var1x.getValue());
                        }

                        // $FF: synthetic method
                        // $FF: bridge method
                        public Object getValue() {
                           return this.getValue();
                        }
                     };
                  }

                  // $FF: synthetic method
                  // $FF: bridge method
                  public Object next() {
                     return this.next();
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

      // $FF: synthetic method
      // $FF: bridge method
      public Object get(Object var1) {
         return this.get(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Set entrySet() {
         return super.entrySet();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection values() {
         return super.values();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Set keySet() {
         return super.keySet();
      }
   }

   public static class Builder<K, V> {
      ImmutableMapEntry.TerminalEntry<K, V>[] entries;
      int size;

      public Builder() {
         this(4);
      }

      Builder(int var1) {
         this.entries = new ImmutableMapEntry.TerminalEntry[var1];
         this.size = 0;
      }

      private void ensureCapacity(int var1) {
         if(var1 > this.entries.length) {
            this.entries = (ImmutableMapEntry.TerminalEntry[])ObjectArrays.arraysCopyOf(this.entries, ImmutableCollection.Builder.expandedCapacity(this.entries.length, var1));
         }

      }

      public ImmutableMap.Builder<K, V> put(K var1, V var2) {
         this.ensureCapacity(this.size + 1);
         ImmutableMapEntry.TerminalEntry var3 = ImmutableMap.entryOf(var1, var2);
         this.entries[this.size++] = var3;
         return this;
      }

      public ImmutableMap.Builder<K, V> put(Entry<? extends K, ? extends V> var1) {
         return this.put(var1.getKey(), var1.getValue());
      }

      public ImmutableMap.Builder<K, V> putAll(Map<? extends K, ? extends V> var1) {
         this.ensureCapacity(this.size + var1.size());
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            this.put(var3);
         }

         return this;
      }

      public ImmutableMap<K, V> build() {
         switch(this.size) {
         case 0:
            return ImmutableBiMap.of();
         case 1:
            return ImmutableBiMap.of(this.entries[0].getKey(), this.entries[0].getValue());
         default:
            return new RegularImmutableMap(this.size, this.entries);
         }
      }
   }
}
