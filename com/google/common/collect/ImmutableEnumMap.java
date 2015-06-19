package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMapEntrySet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
final class ImmutableEnumMap<K extends Enum<K>, V> extends ImmutableMap<K, V> {
   private final transient EnumMap<K, V> delegate;

   static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(EnumMap<K, V> var0) {
      switch(var0.size()) {
      case 0:
         return ImmutableMap.of();
      case 1:
         Entry var1 = (Entry)Iterables.getOnlyElement(var0.entrySet());
         return ImmutableMap.of(var1.getKey(), var1.getValue());
      default:
         return new ImmutableEnumMap(var0);
      }
   }

   private ImmutableEnumMap(EnumMap<K, V> var1) {
      this.delegate = var1;
      Preconditions.checkArgument(!var1.isEmpty());
   }

   ImmutableSet<K> createKeySet() {
      return new ImmutableSet() {
         public boolean contains(Object var1) {
            return ImmutableEnumMap.this.delegate.containsKey(var1);
         }

         public int size() {
            return ImmutableEnumMap.this.size();
         }

         public UnmodifiableIterator<K> iterator() {
            return Iterators.unmodifiableIterator(ImmutableEnumMap.this.delegate.keySet().iterator());
         }

         boolean isPartialView() {
            return true;
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Iterator iterator() {
            return this.iterator();
         }
      };
   }

   public int size() {
      return this.delegate.size();
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.delegate.containsKey(var1);
   }

   public V get(Object var1) {
      return this.delegate.get(var1);
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      return new ImmutableMapEntrySet() {
         ImmutableMap<K, V> map() {
            return ImmutableEnumMap.this;
         }

         public UnmodifiableIterator<Entry<K, V>> iterator() {
            return new UnmodifiableIterator() {
               private final Iterator<Entry<K, V>> backingIterator;

               {
                  this.backingIterator = ImmutableEnumMap.this.delegate.entrySet().iterator();
               }

               public boolean hasNext() {
                  return this.backingIterator.hasNext();
               }

               public Entry<K, V> next() {
                  Entry var1 = (Entry)this.backingIterator.next();
                  return Maps.immutableEntry(var1.getKey(), var1.getValue());
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

   boolean isPartialView() {
      return false;
   }

   Object writeReplace() {
      return new ImmutableEnumMap.EnumSerializedForm(this.delegate);
   }

   // $FF: synthetic method
   ImmutableEnumMap(EnumMap var1, Object var2) {
      this(var1);
   }

   private static class EnumSerializedForm<K extends Enum<K>, V> implements Serializable {
      final EnumMap<K, V> delegate;
      private static final long serialVersionUID = 0L;

      EnumSerializedForm(EnumMap<K, V> var1) {
         this.delegate = var1;
      }

      Object readResolve() {
         return new ImmutableEnumMap(this.delegate, null);
      }
   }
}
