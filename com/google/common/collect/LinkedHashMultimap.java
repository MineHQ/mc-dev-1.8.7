package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.collect.AbstractSetMultimap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.Hashing;
import com.google.common.collect.ImmutableEntry;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public final class LinkedHashMultimap<K, V> extends AbstractSetMultimap<K, V> {
   private static final int DEFAULT_KEY_CAPACITY = 16;
   private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
   @VisibleForTesting
   static final double VALUE_SET_LOAD_FACTOR = 1.0D;
   @VisibleForTesting
   transient int valueSetCapacity = 2;
   private transient LinkedHashMultimap.ValueEntry<K, V> multimapHeaderEntry;
   @GwtIncompatible("java serialization not supported")
   private static final long serialVersionUID = 1L;

   public static <K, V> LinkedHashMultimap<K, V> create() {
      return new LinkedHashMultimap(16, 2);
   }

   public static <K, V> LinkedHashMultimap<K, V> create(int var0, int var1) {
      return new LinkedHashMultimap(Maps.capacity(var0), Maps.capacity(var1));
   }

   public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> var0) {
      LinkedHashMultimap var1 = create(var0.keySet().size(), 2);
      var1.putAll(var0);
      return var1;
   }

   private static <K, V> void succeedsInValueSet(LinkedHashMultimap.ValueSetLink<K, V> var0, LinkedHashMultimap.ValueSetLink<K, V> var1) {
      var0.setSuccessorInValueSet(var1);
      var1.setPredecessorInValueSet(var0);
   }

   private static <K, V> void succeedsInMultimap(LinkedHashMultimap.ValueEntry<K, V> var0, LinkedHashMultimap.ValueEntry<K, V> var1) {
      var0.setSuccessorInMultimap(var1);
      var1.setPredecessorInMultimap(var0);
   }

   private static <K, V> void deleteFromValueSet(LinkedHashMultimap.ValueSetLink<K, V> var0) {
      succeedsInValueSet(var0.getPredecessorInValueSet(), var0.getSuccessorInValueSet());
   }

   private static <K, V> void deleteFromMultimap(LinkedHashMultimap.ValueEntry<K, V> var0) {
      succeedsInMultimap(var0.getPredecessorInMultimap(), var0.getSuccessorInMultimap());
   }

   private LinkedHashMultimap(int var1, int var2) {
      super(new LinkedHashMap(var1));
      CollectPreconditions.checkNonnegative(var2, "expectedValuesPerKey");
      this.valueSetCapacity = var2;
      this.multimapHeaderEntry = new LinkedHashMultimap.ValueEntry((Object)null, (Object)null, 0, (LinkedHashMultimap.ValueEntry)null);
      succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
   }

   Set<V> createCollection() {
      return new LinkedHashSet(this.valueSetCapacity);
   }

   Collection<V> createCollection(K var1) {
      return new LinkedHashMultimap.ValueSet(var1, this.valueSetCapacity);
   }

   public Set<V> replaceValues(@Nullable K var1, Iterable<? extends V> var2) {
      return super.replaceValues(var1, var2);
   }

   public Set<Entry<K, V>> entries() {
      return super.entries();
   }

   public Collection<V> values() {
      return super.values();
   }

   Iterator<Entry<K, V>> entryIterator() {
      return new Iterator() {
         LinkedHashMultimap.ValueEntry<K, V> nextEntry;
         LinkedHashMultimap.ValueEntry<K, V> toRemove;

         {
            this.nextEntry = LinkedHashMultimap.this.multimapHeaderEntry.successorInMultimap;
         }

         public boolean hasNext() {
            return this.nextEntry != LinkedHashMultimap.this.multimapHeaderEntry;
         }

         public Entry<K, V> next() {
            if(!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               LinkedHashMultimap.ValueEntry var1 = this.nextEntry;
               this.toRemove = var1;
               this.nextEntry = this.nextEntry.successorInMultimap;
               return var1;
            }
         }

         public void remove() {
            CollectPreconditions.checkRemove(this.toRemove != null);
            LinkedHashMultimap.this.remove(this.toRemove.getKey(), this.toRemove.getValue());
            this.toRemove = null;
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object next() {
            return this.next();
         }
      };
   }

   Iterator<V> valueIterator() {
      return Maps.valueIterator(this.entryIterator());
   }

   public void clear() {
      super.clear();
      succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeInt(this.valueSetCapacity);
      var1.writeInt(this.keySet().size());
      Iterator var2 = this.keySet().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         var1.writeObject(var3);
      }

      var1.writeInt(this.size());
      var2 = this.entries().iterator();

      while(var2.hasNext()) {
         Entry var4 = (Entry)var2.next();
         var1.writeObject(var4.getKey());
         var1.writeObject(var4.getValue());
      }

   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.multimapHeaderEntry = new LinkedHashMultimap.ValueEntry((Object)null, (Object)null, 0, (LinkedHashMultimap.ValueEntry)null);
      succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
      this.valueSetCapacity = var1.readInt();
      int var2 = var1.readInt();
      LinkedHashMap var3 = new LinkedHashMap(Maps.capacity(var2));

      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         Object var5 = var1.readObject();
         var3.put(var5, this.createCollection(var5));
      }

      var4 = var1.readInt();

      for(int var8 = 0; var8 < var4; ++var8) {
         Object var6 = var1.readObject();
         Object var7 = var1.readObject();
         ((Collection)var3.get(var6)).add(var7);
      }

      this.setMap(var3);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean put(Object var1, Object var2) {
      return super.put(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Map asMap() {
      return super.asMap();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set removeAll(Object var1) {
      return super.removeAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set get(Object var1) {
      return super.get(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection entries() {
      return this.entries();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsKey(Object var1) {
      return super.containsKey(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int size() {
      return super.size();
   }

   // $FF: synthetic method
   // $FF: bridge method
   Collection createCollection() {
      return this.createCollection();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public String toString() {
      return super.toString();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Multiset keys() {
      return super.keys();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set keySet() {
      return super.keySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean putAll(Multimap var1) {
      return super.putAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean putAll(Object var1, Iterable var2) {
      return super.putAll(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean remove(Object var1, Object var2) {
      return super.remove(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsEntry(Object var1, Object var2) {
      return super.containsEntry(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean containsValue(Object var1) {
      return super.containsValue(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean isEmpty() {
      return super.isEmpty();
   }

   @VisibleForTesting
   final class ValueSet extends Sets.ImprovedAbstractSet<V> implements LinkedHashMultimap.ValueSetLink<K, V> {
      private final K key;
      @VisibleForTesting
      LinkedHashMultimap.ValueEntry<K, V>[] hashTable;
      private int size = 0;
      private int modCount = 0;
      private LinkedHashMultimap.ValueSetLink<K, V> firstEntry;
      private LinkedHashMultimap.ValueSetLink<K, V> lastEntry;

      ValueSet(K var1, int var2) {
         this.key = var2;
         this.firstEntry = this;
         this.lastEntry = this;
         int var4 = Hashing.closedTableSize(var3, 1.0D);
         LinkedHashMultimap.ValueEntry[] var5 = new LinkedHashMultimap.ValueEntry[var4];
         this.hashTable = var5;
      }

      private int mask() {
         return this.hashTable.length - 1;
      }

      public LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet() {
         return this.lastEntry;
      }

      public LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet() {
         return this.firstEntry;
      }

      public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> var1) {
         this.lastEntry = var1;
      }

      public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> var1) {
         this.firstEntry = var1;
      }

      public Iterator<V> iterator() {
         return new Iterator() {
            LinkedHashMultimap.ValueSetLink<K, V> nextEntry;
            LinkedHashMultimap.ValueEntry<K, V> toRemove;
            int expectedModCount;

            {
               this.nextEntry = ValueSet.this.firstEntry;
               this.expectedModCount = ValueSet.this.modCount;
            }

            private void checkForComodification() {
               if(ValueSet.this.modCount != this.expectedModCount) {
                  throw new ConcurrentModificationException();
               }
            }

            public boolean hasNext() {
               this.checkForComodification();
               return this.nextEntry != ValueSet.this;
            }

            public V next() {
               if(!this.hasNext()) {
                  throw new NoSuchElementException();
               } else {
                  LinkedHashMultimap.ValueEntry var1 = (LinkedHashMultimap.ValueEntry)this.nextEntry;
                  Object var2 = var1.getValue();
                  this.toRemove = var1;
                  this.nextEntry = var1.getSuccessorInValueSet();
                  return var2;
               }
            }

            public void remove() {
               this.checkForComodification();
               CollectPreconditions.checkRemove(this.toRemove != null);
               ValueSet.this.remove(this.toRemove.getValue());
               this.expectedModCount = ValueSet.this.modCount;
               this.toRemove = null;
            }
         };
      }

      public int size() {
         return this.size;
      }

      public boolean contains(@Nullable Object var1) {
         int var2 = Hashing.smearedHash(var1);

         for(LinkedHashMultimap.ValueEntry var3 = this.hashTable[var2 & this.mask()]; var3 != null; var3 = var3.nextInValueBucket) {
            if(var3.matchesValue(var1, var2)) {
               return true;
            }
         }

         return false;
      }

      public boolean add(@Nullable V var1) {
         int var2 = Hashing.smearedHash(var1);
         int var3 = var2 & this.mask();
         LinkedHashMultimap.ValueEntry var4 = this.hashTable[var3];

         LinkedHashMultimap.ValueEntry var5;
         for(var5 = var4; var5 != null; var5 = var5.nextInValueBucket) {
            if(var5.matchesValue(var1, var2)) {
               return false;
            }
         }

         var5 = new LinkedHashMultimap.ValueEntry(this.key, var1, var2, var4);
         LinkedHashMultimap.succeedsInValueSet(this.lastEntry, var5);
         LinkedHashMultimap.succeedsInValueSet(var5, this);
         LinkedHashMultimap.succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), var5);
         LinkedHashMultimap.succeedsInMultimap(var5, LinkedHashMultimap.this.multimapHeaderEntry);
         this.hashTable[var3] = var5;
         ++this.size;
         ++this.modCount;
         this.rehashIfNecessary();
         return true;
      }

      private void rehashIfNecessary() {
         if(Hashing.needsResizing(this.size, this.hashTable.length, 1.0D)) {
            LinkedHashMultimap.ValueEntry[] var1 = new LinkedHashMultimap.ValueEntry[this.hashTable.length * 2];
            this.hashTable = var1;
            int var2 = var1.length - 1;

            for(LinkedHashMultimap.ValueSetLink var3 = this.firstEntry; var3 != this; var3 = var3.getSuccessorInValueSet()) {
               LinkedHashMultimap.ValueEntry var4 = (LinkedHashMultimap.ValueEntry)var3;
               int var5 = var4.smearedValueHash & var2;
               var4.nextInValueBucket = var1[var5];
               var1[var5] = var4;
            }
         }

      }

      public boolean remove(@Nullable Object var1) {
         int var2 = Hashing.smearedHash(var1);
         int var3 = var2 & this.mask();
         LinkedHashMultimap.ValueEntry var4 = null;

         for(LinkedHashMultimap.ValueEntry var5 = this.hashTable[var3]; var5 != null; var5 = var5.nextInValueBucket) {
            if(var5.matchesValue(var1, var2)) {
               if(var4 == null) {
                  this.hashTable[var3] = var5.nextInValueBucket;
               } else {
                  var4.nextInValueBucket = var5.nextInValueBucket;
               }

               LinkedHashMultimap.deleteFromValueSet(var5);
               LinkedHashMultimap.deleteFromMultimap(var5);
               --this.size;
               ++this.modCount;
               return true;
            }

            var4 = var5;
         }

         return false;
      }

      public void clear() {
         Arrays.fill(this.hashTable, (Object)null);
         this.size = 0;

         for(LinkedHashMultimap.ValueSetLink var1 = this.firstEntry; var1 != this; var1 = var1.getSuccessorInValueSet()) {
            LinkedHashMultimap.ValueEntry var2 = (LinkedHashMultimap.ValueEntry)var1;
            LinkedHashMultimap.deleteFromMultimap(var2);
         }

         LinkedHashMultimap.succeedsInValueSet(this, this);
         ++this.modCount;
      }
   }

   @VisibleForTesting
   static final class ValueEntry<K, V> extends ImmutableEntry<K, V> implements LinkedHashMultimap.ValueSetLink<K, V> {
      final int smearedValueHash;
      @Nullable
      LinkedHashMultimap.ValueEntry<K, V> nextInValueBucket;
      LinkedHashMultimap.ValueSetLink<K, V> predecessorInValueSet;
      LinkedHashMultimap.ValueSetLink<K, V> successorInValueSet;
      LinkedHashMultimap.ValueEntry<K, V> predecessorInMultimap;
      LinkedHashMultimap.ValueEntry<K, V> successorInMultimap;

      ValueEntry(@Nullable K var1, @Nullable V var2, int var3, @Nullable LinkedHashMultimap.ValueEntry<K, V> var4) {
         super(var1, var2);
         this.smearedValueHash = var3;
         this.nextInValueBucket = var4;
      }

      boolean matchesValue(@Nullable Object var1, int var2) {
         return this.smearedValueHash == var2 && Objects.equal(this.getValue(), var1);
      }

      public LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet() {
         return this.predecessorInValueSet;
      }

      public LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet() {
         return this.successorInValueSet;
      }

      public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> var1) {
         this.predecessorInValueSet = var1;
      }

      public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> var1) {
         this.successorInValueSet = var1;
      }

      public LinkedHashMultimap.ValueEntry<K, V> getPredecessorInMultimap() {
         return this.predecessorInMultimap;
      }

      public LinkedHashMultimap.ValueEntry<K, V> getSuccessorInMultimap() {
         return this.successorInMultimap;
      }

      public void setSuccessorInMultimap(LinkedHashMultimap.ValueEntry<K, V> var1) {
         this.successorInMultimap = var1;
      }

      public void setPredecessorInMultimap(LinkedHashMultimap.ValueEntry<K, V> var1) {
         this.predecessorInMultimap = var1;
      }
   }

   private interface ValueSetLink<K, V> {
      LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet();

      LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet();

      void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> var1);

      void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> var1);
   }
}
