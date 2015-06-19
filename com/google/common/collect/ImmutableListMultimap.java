package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.EmptyImmutableListMultimap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Serialization;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public class ImmutableListMultimap<K, V> extends ImmutableMultimap<K, V> implements ListMultimap<K, V> {
   private transient ImmutableListMultimap<V, K> inverse;
   @GwtIncompatible("Not needed in emulated source")
   private static final long serialVersionUID = 0L;

   public static <K, V> ImmutableListMultimap<K, V> of() {
      return EmptyImmutableListMultimap.INSTANCE;
   }

   public static <K, V> ImmutableListMultimap<K, V> of(K var0, V var1) {
      ImmutableListMultimap.Builder var2 = builder();
      var2.put(var0, var1);
      return var2.build();
   }

   public static <K, V> ImmutableListMultimap<K, V> of(K var0, V var1, K var2, V var3) {
      ImmutableListMultimap.Builder var4 = builder();
      var4.put(var0, var1);
      var4.put(var2, var3);
      return var4.build();
   }

   public static <K, V> ImmutableListMultimap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5) {
      ImmutableListMultimap.Builder var6 = builder();
      var6.put(var0, var1);
      var6.put(var2, var3);
      var6.put(var4, var5);
      return var6.build();
   }

   public static <K, V> ImmutableListMultimap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5, K var6, V var7) {
      ImmutableListMultimap.Builder var8 = builder();
      var8.put(var0, var1);
      var8.put(var2, var3);
      var8.put(var4, var5);
      var8.put(var6, var7);
      return var8.build();
   }

   public static <K, V> ImmutableListMultimap<K, V> of(K var0, V var1, K var2, V var3, K var4, V var5, K var6, V var7, K var8, V var9) {
      ImmutableListMultimap.Builder var10 = builder();
      var10.put(var0, var1);
      var10.put(var2, var3);
      var10.put(var4, var5);
      var10.put(var6, var7);
      var10.put(var8, var9);
      return var10.build();
   }

   public static <K, V> ImmutableListMultimap.Builder<K, V> builder() {
      return new ImmutableListMultimap.Builder();
   }

   public static <K, V> ImmutableListMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> var0) {
      if(var0.isEmpty()) {
         return of();
      } else {
         if(var0 instanceof ImmutableListMultimap) {
            ImmutableListMultimap var1 = (ImmutableListMultimap)var0;
            if(!var1.isPartialView()) {
               return var1;
            }
         }

         ImmutableMap.Builder var6 = ImmutableMap.builder();
         int var2 = 0;
         Iterator var3 = var0.asMap().entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            ImmutableList var5 = ImmutableList.copyOf((Collection)var4.getValue());
            if(!var5.isEmpty()) {
               var6.put(var4.getKey(), var5);
               var2 += var5.size();
            }
         }

         return new ImmutableListMultimap(var6.build(), var2);
      }
   }

   ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> var1, int var2) {
      super(var1, var2);
   }

   public ImmutableList<V> get(@Nullable K var1) {
      ImmutableList var2 = (ImmutableList)this.map.get(var1);
      return var2 == null?ImmutableList.of():var2;
   }

   public ImmutableListMultimap<V, K> inverse() {
      ImmutableListMultimap var1 = this.inverse;
      return var1 == null?(this.inverse = this.invert()):var1;
   }

   private ImmutableListMultimap<V, K> invert() {
      ImmutableListMultimap.Builder var1 = builder();
      Iterator var2 = this.entries().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.put(var3.getValue(), var3.getKey());
      }

      ImmutableListMultimap var4 = var1.build();
      var4.inverse = this;
      return var4;
   }

   /** @deprecated */
   @Deprecated
   public ImmutableList<V> removeAll(Object var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public ImmutableList<V> replaceValues(K var1, Iterable<? extends V> var2) {
      throw new UnsupportedOperationException();
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      Serialization.writeMultimap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      int var2 = var1.readInt();
      if(var2 < 0) {
         throw new InvalidObjectException("Invalid key count " + var2);
      } else {
         ImmutableMap.Builder var3 = ImmutableMap.builder();
         int var4 = 0;

         for(int var5 = 0; var5 < var2; ++var5) {
            Object var6 = var1.readObject();
            int var7 = var1.readInt();
            if(var7 <= 0) {
               throw new InvalidObjectException("Invalid value count " + var7);
            }

            Object[] var8 = new Object[var7];

            for(int var9 = 0; var9 < var7; ++var9) {
               var8[var9] = var1.readObject();
            }

            var3.put(var6, ImmutableList.copyOf(var8));
            var4 += var7;
         }

         ImmutableMap var11;
         try {
            var11 = var3.build();
         } catch (IllegalArgumentException var10) {
            throw (InvalidObjectException)(new InvalidObjectException(var10.getMessage())).initCause(var10);
         }

         ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, var11);
         ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, var4);
      }
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ImmutableMultimap inverse() {
      return this.inverse();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ImmutableCollection get(Object var1) {
      return this.get(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ImmutableCollection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ImmutableCollection removeAll(Object var1) {
      return this.removeAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection get(Object var1) {
      return this.get(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection removeAll(Object var1) {
      return this.removeAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public List replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public List removeAll(Object var1) {
      return this.removeAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public List get(Object var1) {
      return this.get(var1);
   }

   public static final class Builder<K, V> extends ImmutableMultimap.Builder<K, V> {
      public Builder() {
      }

      public ImmutableListMultimap.Builder<K, V> put(K var1, V var2) {
         super.put(var1, var2);
         return this;
      }

      public ImmutableListMultimap.Builder<K, V> put(Entry<? extends K, ? extends V> var1) {
         super.put(var1);
         return this;
      }

      public ImmutableListMultimap.Builder<K, V> putAll(K var1, Iterable<? extends V> var2) {
         super.putAll(var1, var2);
         return this;
      }

      public ImmutableListMultimap.Builder<K, V> putAll(K var1, V... var2) {
         super.putAll(var1, var2);
         return this;
      }

      public ImmutableListMultimap.Builder<K, V> putAll(Multimap<? extends K, ? extends V> var1) {
         super.putAll(var1);
         return this;
      }

      public ImmutableListMultimap.Builder<K, V> orderKeysBy(Comparator<? super K> var1) {
         super.orderKeysBy(var1);
         return this;
      }

      public ImmutableListMultimap.Builder<K, V> orderValuesBy(Comparator<? super V> var1) {
         super.orderValuesBy(var1);
         return this;
      }

      public ImmutableListMultimap<K, V> build() {
         return (ImmutableListMultimap)super.build();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultimap build() {
         return this.build();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultimap.Builder orderValuesBy(Comparator var1) {
         return this.orderValuesBy(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultimap.Builder orderKeysBy(Comparator var1) {
         return this.orderKeysBy(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultimap.Builder putAll(Multimap var1) {
         return this.putAll(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultimap.Builder putAll(Object var1, Object[] var2) {
         return this.putAll(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultimap.Builder putAll(Object var1, Iterable var2) {
         return this.putAll(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultimap.Builder put(Entry var1) {
         return this.put(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ImmutableMultimap.Builder put(Object var1, Object var2) {
         return this.put(var1, var2);
      }
   }
}
