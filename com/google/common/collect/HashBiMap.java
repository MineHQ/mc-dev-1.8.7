package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapEntry;
import com.google.common.collect.BiMap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Hashing;
import com.google.common.collect.ImmutableEntry;
import com.google.common.collect.Maps;
import com.google.common.collect.Serialization;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class HashBiMap<K, V> extends AbstractMap<K, V> implements BiMap<K, V>, Serializable {
   private static final double LOAD_FACTOR = 1.0D;
   private transient HashBiMap.BiEntry<K, V>[] hashTableKToV;
   private transient HashBiMap.BiEntry<K, V>[] hashTableVToK;
   private transient int size;
   private transient int mask;
   private transient int modCount;
   private transient BiMap<V, K> inverse;
   @GwtIncompatible("Not needed in emulated source")
   private static final long serialVersionUID = 0L;

   public static <K, V> HashBiMap<K, V> create() {
      return create(16);
   }

   public static <K, V> HashBiMap<K, V> create(int var0) {
      return new HashBiMap(var0);
   }

   public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> var0) {
      HashBiMap var1 = create(var0.size());
      var1.putAll(var0);
      return var1;
   }

   private HashBiMap(int var1) {
      this.init(var1);
   }

   private void init(int var1) {
      CollectPreconditions.checkNonnegative(var1, "expectedSize");
      int var2 = Hashing.closedTableSize(var1, 1.0D);
      this.hashTableKToV = this.createTable(var2);
      this.hashTableVToK = this.createTable(var2);
      this.mask = var2 - 1;
      this.modCount = 0;
      this.size = 0;
   }

   private void delete(HashBiMap.BiEntry<K, V> var1) {
      int var2 = var1.keyHash & this.mask;
      HashBiMap.BiEntry var3 = null;

      for(HashBiMap.BiEntry var4 = this.hashTableKToV[var2]; var4 != var1; var4 = var4.nextInKToVBucket) {
         var3 = var4;
      }

      if(var3 == null) {
         this.hashTableKToV[var2] = var1.nextInKToVBucket;
      } else {
         var3.nextInKToVBucket = var1.nextInKToVBucket;
      }

      int var6 = var1.valueHash & this.mask;
      var3 = null;

      for(HashBiMap.BiEntry var5 = this.hashTableVToK[var6]; var5 != var1; var5 = var5.nextInVToKBucket) {
         var3 = var5;
      }

      if(var3 == null) {
         this.hashTableVToK[var6] = var1.nextInVToKBucket;
      } else {
         var3.nextInVToKBucket = var1.nextInVToKBucket;
      }

      --this.size;
      ++this.modCount;
   }

   private void insert(HashBiMap.BiEntry<K, V> var1) {
      int var2 = var1.keyHash & this.mask;
      var1.nextInKToVBucket = this.hashTableKToV[var2];
      this.hashTableKToV[var2] = var1;
      int var3 = var1.valueHash & this.mask;
      var1.nextInVToKBucket = this.hashTableVToK[var3];
      this.hashTableVToK[var3] = var1;
      ++this.size;
      ++this.modCount;
   }

   private static int hash(@Nullable Object var0) {
      return Hashing.smear(var0 == null?0:var0.hashCode());
   }

   private HashBiMap.BiEntry<K, V> seekByKey(@Nullable Object var1, int var2) {
      for(HashBiMap.BiEntry var3 = this.hashTableKToV[var2 & this.mask]; var3 != null; var3 = var3.nextInKToVBucket) {
         if(var2 == var3.keyHash && Objects.equal(var1, var3.key)) {
            return var3;
         }
      }

      return null;
   }

   private HashBiMap.BiEntry<K, V> seekByValue(@Nullable Object var1, int var2) {
      for(HashBiMap.BiEntry var3 = this.hashTableVToK[var2 & this.mask]; var3 != null; var3 = var3.nextInVToKBucket) {
         if(var2 == var3.valueHash && Objects.equal(var1, var3.value)) {
            return var3;
         }
      }

      return null;
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.seekByKey(var1, hash(var1)) != null;
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.seekByValue(var1, hash(var1)) != null;
   }

   @Nullable
   public V get(@Nullable Object var1) {
      HashBiMap.BiEntry var2 = this.seekByKey(var1, hash(var1));
      return var2 == null?null:var2.value;
   }

   public V put(@Nullable K var1, @Nullable V var2) {
      return this.put(var1, var2, false);
   }

   public V forcePut(@Nullable K var1, @Nullable V var2) {
      return this.put(var1, var2, true);
   }

   private V put(@Nullable K var1, @Nullable V var2, boolean var3) {
      int var4 = hash(var1);
      int var5 = hash(var2);
      HashBiMap.BiEntry var6 = this.seekByKey(var1, var4);
      if(var6 != null && var5 == var6.valueHash && Objects.equal(var2, var6.value)) {
         return var2;
      } else {
         HashBiMap.BiEntry var7 = this.seekByValue(var2, var5);
         if(var7 != null) {
            if(!var3) {
               throw new IllegalArgumentException("value already present: " + var2);
            }

            this.delete(var7);
         }

         if(var6 != null) {
            this.delete(var6);
         }

         HashBiMap.BiEntry var8 = new HashBiMap.BiEntry(var1, var4, var2, var5);
         this.insert(var8);
         this.rehashIfNecessary();
         return var6 == null?null:var6.value;
      }
   }

   @Nullable
   private K putInverse(@Nullable V var1, @Nullable K var2, boolean var3) {
      int var4 = hash(var1);
      int var5 = hash(var2);
      HashBiMap.BiEntry var6 = this.seekByValue(var1, var4);
      if(var6 != null && var5 == var6.keyHash && Objects.equal(var2, var6.key)) {
         return var2;
      } else {
         HashBiMap.BiEntry var7 = this.seekByKey(var2, var5);
         if(var7 != null) {
            if(!var3) {
               throw new IllegalArgumentException("value already present: " + var2);
            }

            this.delete(var7);
         }

         if(var6 != null) {
            this.delete(var6);
         }

         HashBiMap.BiEntry var8 = new HashBiMap.BiEntry(var2, var5, var1, var4);
         this.insert(var8);
         this.rehashIfNecessary();
         return var6 == null?null:var6.key;
      }
   }

   private void rehashIfNecessary() {
      HashBiMap.BiEntry[] var1 = this.hashTableKToV;
      if(Hashing.needsResizing(this.size, var1.length, 1.0D)) {
         int var2 = var1.length * 2;
         this.hashTableKToV = this.createTable(var2);
         this.hashTableVToK = this.createTable(var2);
         this.mask = var2 - 1;
         this.size = 0;

         HashBiMap.BiEntry var5;
         for(int var3 = 0; var3 < var1.length; ++var3) {
            for(HashBiMap.BiEntry var4 = var1[var3]; var4 != null; var4 = var5) {
               var5 = var4.nextInKToVBucket;
               this.insert(var4);
            }
         }

         ++this.modCount;
      }

   }

   private HashBiMap.BiEntry<K, V>[] createTable(int var1) {
      return new HashBiMap.BiEntry[var1];
   }

   public V remove(@Nullable Object var1) {
      HashBiMap.BiEntry var2 = this.seekByKey(var1, hash(var1));
      if(var2 == null) {
         return null;
      } else {
         this.delete(var2);
         return var2.value;
      }
   }

   public void clear() {
      this.size = 0;
      Arrays.fill(this.hashTableKToV, (Object)null);
      Arrays.fill(this.hashTableVToK, (Object)null);
      ++this.modCount;
   }

   public int size() {
      return this.size;
   }

   public Set<K> keySet() {
      return new HashBiMap.KeySet();
   }

   public Set<V> values() {
      return this.inverse().keySet();
   }

   public Set<Entry<K, V>> entrySet() {
      return new HashBiMap.EntrySet();
   }

   public BiMap<V, K> inverse() {
      return this.inverse == null?(this.inverse = new HashBiMap.Inverse()):this.inverse;
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      Serialization.writeMap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      int var2 = Serialization.readCount(var1);
      this.init(var2);
      Serialization.populateMap(this, var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection values() {
      return this.values();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static final class InverseSerializedForm<K, V> implements Serializable {
      private final HashBiMap<K, V> bimap;

      InverseSerializedForm(HashBiMap<K, V> var1) {
         this.bimap = var1;
      }

      Object readResolve() {
         return this.bimap.inverse();
      }
   }

   private final class Inverse extends AbstractMap<V, K> implements BiMap<V, K>, Serializable {
      private Inverse() {
      }

      BiMap<K, V> forward() {
         return HashBiMap.this;
      }

      public int size() {
         return HashBiMap.this.size;
      }

      public void clear() {
         this.forward().clear();
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.forward().containsValue(var1);
      }

      public K get(@Nullable Object var1) {
         HashBiMap.BiEntry var2 = HashBiMap.this.seekByValue(var1, HashBiMap.hash(var1));
         return var2 == null?null:var2.key;
      }

      public K put(@Nullable V var1, @Nullable K var2) {
         return HashBiMap.this.putInverse(var1, var2, false);
      }

      public K forcePut(@Nullable V var1, @Nullable K var2) {
         return HashBiMap.this.putInverse(var1, var2, true);
      }

      public K remove(@Nullable Object var1) {
         HashBiMap.BiEntry var2 = HashBiMap.this.seekByValue(var1, HashBiMap.hash(var1));
         if(var2 == null) {
            return null;
         } else {
            HashBiMap.this.delete(var2);
            return var2.key;
         }
      }

      public BiMap<K, V> inverse() {
         return this.forward();
      }

      public Set<V> keySet() {
         return new HashBiMap.Inverse.Inverse$InverseKeySet();
      }

      public Set<K> values() {
         return this.forward().keySet();
      }

      public Set<Entry<V, K>> entrySet() {
         return new Maps.EntrySet() {
            Map<V, K> map() {
               return Inverse.this;
            }

            public Iterator<Entry<V, K>> iterator() {
               return new HashBiMap.Itr() {
                  Entry<V, K> output(HashBiMap.BiEntry<K, V> var1) {
                     return new null.Inverse$1$1$InverseEntry(var1);
                  }

                  // $FF: synthetic method
                  // $FF: bridge method
                  Object output(HashBiMap.BiEntry var1) {
                     return this.output(var1);
                  }

                  class Inverse$1$1$InverseEntry extends AbstractMapEntry<V, K> {
                     HashBiMap.BiEntry<K, V> delegate;

                     Inverse$1$1$InverseEntry(HashBiMap.BiEntry<K, V> var1) {
                        this.delegate = var2;
                     }

                     public V getKey() {
                        return this.delegate.value;
                     }

                     public K getValue() {
                        return this.delegate.key;
                     }

                     public K setValue(K var1) {
                        Object var2 = this.delegate.key;
                        int var3 = HashBiMap.hash(var1);
                        if(var3 == this.delegate.keyHash && Objects.equal(var1, var2)) {
                           return var1;
                        } else {
                           Preconditions.checkArgument(HashBiMap.this.seekByKey(var1, var3) == null, "value already present: %s", new Object[]{var1});
                           HashBiMap.this.delete(this.delegate);
                           HashBiMap.BiEntry var4 = new HashBiMap.BiEntry(var1, var3, this.delegate.value, this.delegate.valueHash);
                           HashBiMap.this.insert(var4);
                           expectedModCount = HashBiMap.this.modCount;
                           return var2;
                        }
                     }
                  }
               };
            }
         };
      }

      Object writeReplace() {
         return new HashBiMap.InverseSerializedForm(HashBiMap.this);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection values() {
         return this.values();
      }

      // $FF: synthetic method
      Inverse(HashBiMap.SyntheticClass_1 var2) {
         this();
      }

      private final class Inverse$InverseKeySet extends Maps.KeySet<V, K> {
         Inverse$InverseKeySet() {
            super(Inverse.this);
         }

         public boolean remove(@Nullable Object var1) {
            HashBiMap.BiEntry var2 = HashBiMap.this.seekByValue(var1, HashBiMap.hash(var1));
            if(var2 == null) {
               return false;
            } else {
               HashBiMap.this.delete(var2);
               return true;
            }
         }

         public Iterator<V> iterator() {
            return new HashBiMap.Itr() {
               V output(HashBiMap.BiEntry<K, V> var1) {
                  return var1.value;
               }
            };
         }
      }
   }

   private final class EntrySet extends Maps.EntrySet<K, V> {
      private EntrySet() {
      }

      Map<K, V> map() {
         return HashBiMap.this;
      }

      public Iterator<Entry<K, V>> iterator() {
         return new HashBiMap.Itr() {
            Entry<K, V> output(HashBiMap.BiEntry<K, V> var1) {
               return new null.EntrySet$1$MapEntry(var1);
            }

            // $FF: synthetic method
            // $FF: bridge method
            Object output(HashBiMap.BiEntry var1) {
               return this.output(var1);
            }

            class EntrySet$1$MapEntry extends AbstractMapEntry<K, V> {
               HashBiMap.BiEntry<K, V> delegate;

               EntrySet$1$MapEntry(HashBiMap.BiEntry<K, V> var1) {
                  this.delegate = var2;
               }

               public K getKey() {
                  return this.delegate.key;
               }

               public V getValue() {
                  return this.delegate.value;
               }

               public V setValue(V var1) {
                  Object var2 = this.delegate.value;
                  int var3 = HashBiMap.hash(var1);
                  if(var3 == this.delegate.valueHash && Objects.equal(var1, var2)) {
                     return var1;
                  } else {
                     Preconditions.checkArgument(HashBiMap.this.seekByValue(var1, var3) == null, "value already present: %s", new Object[]{var1});
                     HashBiMap.this.delete(this.delegate);
                     HashBiMap.BiEntry var4 = new HashBiMap.BiEntry(this.delegate.key, this.delegate.keyHash, var1, var3);
                     HashBiMap.this.insert(var4);
                     expectedModCount = HashBiMap.this.modCount;
                     if(toRemove == this.delegate) {
                        toRemove = var4;
                     }

                     this.delegate = var4;
                     return var2;
                  }
               }
            }
         };
      }

      // $FF: synthetic method
      EntrySet(HashBiMap.SyntheticClass_1 var2) {
         this();
      }
   }

   private final class KeySet extends Maps.KeySet<K, V> {
      KeySet() {
         super(HashBiMap.this);
      }

      public Iterator<K> iterator() {
         return new HashBiMap.Itr() {
            K output(HashBiMap.BiEntry<K, V> var1) {
               return var1.key;
            }
         };
      }

      public boolean remove(@Nullable Object var1) {
         HashBiMap.BiEntry var2 = HashBiMap.this.seekByKey(var1, HashBiMap.hash(var1));
         if(var2 == null) {
            return false;
         } else {
            HashBiMap.this.delete(var2);
            return true;
         }
      }
   }

   abstract class Itr<T> implements Iterator<T> {
      int nextBucket = 0;
      HashBiMap.BiEntry<K, V> next = null;
      HashBiMap.BiEntry<K, V> toRemove = null;
      int expectedModCount;

      Itr() {
         this.expectedModCount = HashBiMap.this.modCount;
      }

      private void checkForConcurrentModification() {
         if(HashBiMap.this.modCount != this.expectedModCount) {
            throw new ConcurrentModificationException();
         }
      }

      public boolean hasNext() {
         this.checkForConcurrentModification();
         if(this.next != null) {
            return true;
         } else {
            while(this.nextBucket < HashBiMap.this.hashTableKToV.length) {
               if(HashBiMap.this.hashTableKToV[this.nextBucket] != null) {
                  this.next = HashBiMap.this.hashTableKToV[this.nextBucket++];
                  return true;
               }

               ++this.nextBucket;
            }

            return false;
         }
      }

      public T next() {
         this.checkForConcurrentModification();
         if(!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            HashBiMap.BiEntry var1 = this.next;
            this.next = var1.nextInKToVBucket;
            this.toRemove = var1;
            return this.output(var1);
         }
      }

      public void remove() {
         this.checkForConcurrentModification();
         CollectPreconditions.checkRemove(this.toRemove != null);
         HashBiMap.this.delete(this.toRemove);
         this.expectedModCount = HashBiMap.this.modCount;
         this.toRemove = null;
      }

      abstract T output(HashBiMap.BiEntry<K, V> var1);
   }

   private static final class BiEntry<K, V> extends ImmutableEntry<K, V> {
      final int keyHash;
      final int valueHash;
      @Nullable
      HashBiMap.BiEntry<K, V> nextInKToVBucket;
      @Nullable
      HashBiMap.BiEntry<K, V> nextInVToKBucket;

      BiEntry(K var1, int var2, V var3, int var4) {
         super(var1, var3);
         this.keyHash = var2;
         this.valueHash = var4;
      }
   }
}
