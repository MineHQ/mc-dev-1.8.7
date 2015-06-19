package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToInstanceMap;
import com.google.common.reflect.TypeToken;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
public final class MutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B> implements TypeToInstanceMap<B> {
   private final Map<TypeToken<? extends B>, B> backingMap = Maps.newHashMap();

   public MutableTypeToInstanceMap() {
   }

   @Nullable
   public <T extends B> T getInstance(Class<T> var1) {
      return this.trustedGet(TypeToken.of(var1));
   }

   @Nullable
   public <T extends B> T putInstance(Class<T> var1, @Nullable T var2) {
      return this.trustedPut(TypeToken.of(var1), var2);
   }

   @Nullable
   public <T extends B> T getInstance(TypeToken<T> var1) {
      return this.trustedGet(var1.rejectTypeVariables());
   }

   @Nullable
   public <T extends B> T putInstance(TypeToken<T> var1, @Nullable T var2) {
      return this.trustedPut(var1.rejectTypeVariables(), var2);
   }

   public B put(TypeToken<? extends B> var1, B var2) {
      throw new UnsupportedOperationException("Please use putInstance() instead.");
   }

   public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> var1) {
      throw new UnsupportedOperationException("Please use putInstance() instead.");
   }

   public Set<Entry<TypeToken<? extends B>, B>> entrySet() {
      return MutableTypeToInstanceMap.UnmodifiableEntry.transformEntries(super.entrySet());
   }

   protected Map<TypeToken<? extends B>, B> delegate() {
      return this.backingMap;
   }

   @Nullable
   private <T extends B> T trustedPut(TypeToken<T> var1, @Nullable T var2) {
      return this.backingMap.put(var1, var2);
   }

   @Nullable
   private <T extends B> T trustedGet(TypeToken<T> var1) {
      return this.backingMap.get(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object put(Object var1, Object var2) {
      return this.put((TypeToken)var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Object delegate() {
      return this.delegate();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static final class UnmodifiableEntry<K, V> extends ForwardingMapEntry<K, V> {
      private final Entry<K, V> delegate;

      static <K, V> Set<Entry<K, V>> transformEntries(final Set<Entry<K, V>> var0) {
         return new ForwardingSet() {
            protected Set<Entry<K, V>> delegate() {
               return var0;
            }

            public Iterator<Entry<K, V>> iterator() {
               return MutableTypeToInstanceMap.UnmodifiableEntry.transformEntries(super.iterator());
            }

            public Object[] toArray() {
               return this.standardToArray();
            }

            public <T> T[] toArray(T[] var1) {
               return this.standardToArray(var1);
            }

            // $FF: synthetic method
            // $FF: bridge method
            protected Collection delegate() {
               return this.delegate();
            }

            // $FF: synthetic method
            // $FF: bridge method
            protected Object delegate() {
               return this.delegate();
            }
         };
      }

      private static <K, V> Iterator<Entry<K, V>> transformEntries(Iterator<Entry<K, V>> var0) {
         return Iterators.transform(var0, new Function() {
            public Entry<K, V> apply(Entry<K, V> var1) {
               return new MutableTypeToInstanceMap.UnmodifiableEntry(var1);
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object apply(Object var1) {
               return this.apply((Entry)var1);
            }
         });
      }

      private UnmodifiableEntry(Entry<K, V> var1) {
         this.delegate = (Entry)Preconditions.checkNotNull(var1);
      }

      protected Entry<K, V> delegate() {
         return this.delegate;
      }

      public V setValue(V var1) {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      UnmodifiableEntry(Entry var1, MutableTypeToInstanceMap.SyntheticClass_1 var2) {
         this(var1);
      }
   }
}
