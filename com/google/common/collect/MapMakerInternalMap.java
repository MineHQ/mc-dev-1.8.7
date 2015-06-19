package com.google.common.collect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import com.google.common.collect.AbstractMapEntry;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ForwardingConcurrentMap;
import com.google.common.collect.GenericMapMaker;
import com.google.common.collect.Iterators;
import com.google.common.collect.MapMaker;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

class MapMakerInternalMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
   static final int MAXIMUM_CAPACITY = 1073741824;
   static final int MAX_SEGMENTS = 65536;
   static final int CONTAINS_VALUE_RETRIES = 3;
   static final int DRAIN_THRESHOLD = 63;
   static final int DRAIN_MAX = 16;
   static final long CLEANUP_EXECUTOR_DELAY_SECS = 60L;
   private static final Logger logger = Logger.getLogger(MapMakerInternalMap.class.getName());
   final transient int segmentMask;
   final transient int segmentShift;
   final transient MapMakerInternalMap.Segment<K, V>[] segments;
   final int concurrencyLevel;
   final Equivalence<Object> keyEquivalence;
   final Equivalence<Object> valueEquivalence;
   final MapMakerInternalMap.Strength keyStrength;
   final MapMakerInternalMap.Strength valueStrength;
   final int maximumSize;
   final long expireAfterAccessNanos;
   final long expireAfterWriteNanos;
   final Queue<MapMaker.RemovalNotification<K, V>> removalNotificationQueue;
   final MapMaker.RemovalListener<K, V> removalListener;
   final transient MapMakerInternalMap.EntryFactory entryFactory;
   final Ticker ticker;
   static final MapMakerInternalMap.ValueReference<Object, Object> UNSET = new MapMakerInternalMap.ValueReference() {
      public Object get() {
         return null;
      }

      public MapMakerInternalMap.ReferenceEntry<Object, Object> getEntry() {
         return null;
      }

      public MapMakerInternalMap.ValueReference<Object, Object> copyFor(ReferenceQueue<Object> var1, @Nullable Object var2, MapMakerInternalMap.ReferenceEntry<Object, Object> var3) {
         return this;
      }

      public boolean isComputingReference() {
         return false;
      }

      public Object waitForValue() {
         return null;
      }

      public void clear(MapMakerInternalMap.ValueReference<Object, Object> var1) {
      }
   };
   static final Queue<? extends Object> DISCARDING_QUEUE = new AbstractQueue() {
      public boolean offer(Object var1) {
         return true;
      }

      public Object peek() {
         return null;
      }

      public Object poll() {
         return null;
      }

      public int size() {
         return 0;
      }

      public Iterator<Object> iterator() {
         return Iterators.emptyIterator();
      }
   };
   transient Set<K> keySet;
   transient Collection<V> values;
   transient Set<Entry<K, V>> entrySet;
   private static final long serialVersionUID = 5L;

   MapMakerInternalMap(MapMaker var1) {
      this.concurrencyLevel = Math.min(var1.getConcurrencyLevel(), 65536);
      this.keyStrength = var1.getKeyStrength();
      this.valueStrength = var1.getValueStrength();
      this.keyEquivalence = var1.getKeyEquivalence();
      this.valueEquivalence = this.valueStrength.defaultEquivalence();
      this.maximumSize = var1.maximumSize;
      this.expireAfterAccessNanos = var1.getExpireAfterAccessNanos();
      this.expireAfterWriteNanos = var1.getExpireAfterWriteNanos();
      this.entryFactory = MapMakerInternalMap.EntryFactory.getFactory(this.keyStrength, this.expires(), this.evictsBySize());
      this.ticker = var1.getTicker();
      this.removalListener = var1.getRemovalListener();
      this.removalNotificationQueue = (Queue)(this.removalListener == GenericMapMaker.NullListener.INSTANCE?discardingQueue():new ConcurrentLinkedQueue());
      int var2 = Math.min(var1.getInitialCapacity(), 1073741824);
      if(this.evictsBySize()) {
         var2 = Math.min(var2, this.maximumSize);
      }

      int var3 = 0;

      int var4;
      for(var4 = 1; var4 < this.concurrencyLevel && (!this.evictsBySize() || var4 * 2 <= this.maximumSize); var4 <<= 1) {
         ++var3;
      }

      this.segmentShift = 32 - var3;
      this.segmentMask = var4 - 1;
      this.segments = this.newSegmentArray(var4);
      int var5 = var2 / var4;
      if(var5 * var4 < var2) {
         ++var5;
      }

      int var6;
      for(var6 = 1; var6 < var5; var6 <<= 1) {
         ;
      }

      int var7;
      if(this.evictsBySize()) {
         var7 = this.maximumSize / var4 + 1;
         int var8 = this.maximumSize % var4;

         for(int var9 = 0; var9 < this.segments.length; ++var9) {
            if(var9 == var8) {
               --var7;
            }

            this.segments[var9] = this.createSegment(var6, var7);
         }
      } else {
         for(var7 = 0; var7 < this.segments.length; ++var7) {
            this.segments[var7] = this.createSegment(var6, -1);
         }
      }

   }

   boolean evictsBySize() {
      return this.maximumSize != -1;
   }

   boolean expires() {
      return this.expiresAfterWrite() || this.expiresAfterAccess();
   }

   boolean expiresAfterWrite() {
      return this.expireAfterWriteNanos > 0L;
   }

   boolean expiresAfterAccess() {
      return this.expireAfterAccessNanos > 0L;
   }

   boolean usesKeyReferences() {
      return this.keyStrength != MapMakerInternalMap.Strength.STRONG;
   }

   boolean usesValueReferences() {
      return this.valueStrength != MapMakerInternalMap.Strength.STRONG;
   }

   static <K, V> MapMakerInternalMap.ValueReference<K, V> unset() {
      return UNSET;
   }

   static <K, V> MapMakerInternalMap.ReferenceEntry<K, V> nullEntry() {
      return MapMakerInternalMap.NullEntry.INSTANCE;
   }

   static <E> Queue<E> discardingQueue() {
      return DISCARDING_QUEUE;
   }

   static int rehash(int var0) {
      var0 += var0 << 15 ^ -12931;
      var0 ^= var0 >>> 10;
      var0 += var0 << 3;
      var0 ^= var0 >>> 6;
      var0 += (var0 << 2) + (var0 << 14);
      return var0 ^ var0 >>> 16;
   }

   @GuardedBy("Segment.this")
   @VisibleForTesting
   MapMakerInternalMap.ReferenceEntry<K, V> newEntry(K var1, int var2, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var3) {
      return this.segmentFor(var2).newEntry(var1, var2, var3);
   }

   @GuardedBy("Segment.this")
   @VisibleForTesting
   MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.ReferenceEntry<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2) {
      int var3 = var1.getHash();
      return this.segmentFor(var3).copyEntry(var1, var2);
   }

   @GuardedBy("Segment.this")
   @VisibleForTesting
   MapMakerInternalMap.ValueReference<K, V> newValueReference(MapMakerInternalMap.ReferenceEntry<K, V> var1, V var2) {
      int var3 = var1.getHash();
      return this.valueStrength.referenceValue(this.segmentFor(var3), var1, var2);
   }

   int hash(Object var1) {
      int var2 = this.keyEquivalence.hash(var1);
      return rehash(var2);
   }

   void reclaimValue(MapMakerInternalMap.ValueReference<K, V> var1) {
      MapMakerInternalMap.ReferenceEntry var2 = var1.getEntry();
      int var3 = var2.getHash();
      this.segmentFor(var3).reclaimValue(var2.getKey(), var3, var1);
   }

   void reclaimKey(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
      int var2 = var1.getHash();
      this.segmentFor(var2).reclaimKey(var1, var2);
   }

   @VisibleForTesting
   boolean isLive(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
      return this.segmentFor(var1.getHash()).getLiveValue(var1) != null;
   }

   MapMakerInternalMap.Segment<K, V> segmentFor(int var1) {
      return this.segments[var1 >>> this.segmentShift & this.segmentMask];
   }

   MapMakerInternalMap.Segment<K, V> createSegment(int var1, int var2) {
      return new MapMakerInternalMap.Segment(this, var1, var2);
   }

   V getLiveValue(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
      if(var1.getKey() == null) {
         return null;
      } else {
         Object var2 = var1.getValueReference().get();
         return var2 == null?null:(this.expires() && this.isExpired(var1)?null:var2);
      }
   }

   boolean isExpired(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
      return this.isExpired(var1, this.ticker.read());
   }

   boolean isExpired(MapMakerInternalMap.ReferenceEntry<K, V> var1, long var2) {
      return var2 - var1.getExpirationTime() > 0L;
   }

   @GuardedBy("Segment.this")
   static <K, V> void connectExpirables(MapMakerInternalMap.ReferenceEntry<K, V> var0, MapMakerInternalMap.ReferenceEntry<K, V> var1) {
      var0.setNextExpirable(var1);
      var1.setPreviousExpirable(var0);
   }

   @GuardedBy("Segment.this")
   static <K, V> void nullifyExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var0) {
      MapMakerInternalMap.ReferenceEntry var1 = nullEntry();
      var0.setNextExpirable(var1);
      var0.setPreviousExpirable(var1);
   }

   void processPendingNotifications() {
      MapMaker.RemovalNotification var1;
      while((var1 = (MapMaker.RemovalNotification)this.removalNotificationQueue.poll()) != null) {
         try {
            this.removalListener.onRemoval(var1);
         } catch (Exception var3) {
            logger.log(Level.WARNING, "Exception thrown by removal listener", var3);
         }
      }

   }

   @GuardedBy("Segment.this")
   static <K, V> void connectEvictables(MapMakerInternalMap.ReferenceEntry<K, V> var0, MapMakerInternalMap.ReferenceEntry<K, V> var1) {
      var0.setNextEvictable(var1);
      var1.setPreviousEvictable(var0);
   }

   @GuardedBy("Segment.this")
   static <K, V> void nullifyEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var0) {
      MapMakerInternalMap.ReferenceEntry var1 = nullEntry();
      var0.setNextEvictable(var1);
      var0.setPreviousEvictable(var1);
   }

   final MapMakerInternalMap.Segment<K, V>[] newSegmentArray(int var1) {
      return new MapMakerInternalMap.Segment[var1];
   }

   public boolean isEmpty() {
      long var1 = 0L;
      MapMakerInternalMap.Segment[] var3 = this.segments;

      int var4;
      for(var4 = 0; var4 < var3.length; ++var4) {
         if(var3[var4].count != 0) {
            return false;
         }

         var1 += (long)var3[var4].modCount;
      }

      if(var1 != 0L) {
         for(var4 = 0; var4 < var3.length; ++var4) {
            if(var3[var4].count != 0) {
               return false;
            }

            var1 -= (long)var3[var4].modCount;
         }

         if(var1 != 0L) {
            return false;
         }
      }

      return true;
   }

   public int size() {
      MapMakerInternalMap.Segment[] var1 = this.segments;
      long var2 = 0L;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var2 += (long)var1[var4].count;
      }

      return Ints.saturatedCast(var2);
   }

   public V get(@Nullable Object var1) {
      if(var1 == null) {
         return null;
      } else {
         int var2 = this.hash(var1);
         return this.segmentFor(var2).get(var1, var2);
      }
   }

   MapMakerInternalMap.ReferenceEntry<K, V> getEntry(@Nullable Object var1) {
      if(var1 == null) {
         return null;
      } else {
         int var2 = this.hash(var1);
         return this.segmentFor(var2).getEntry(var1, var2);
      }
   }

   public boolean containsKey(@Nullable Object var1) {
      if(var1 == null) {
         return false;
      } else {
         int var2 = this.hash(var1);
         return this.segmentFor(var2).containsKey(var1, var2);
      }
   }

   public boolean containsValue(@Nullable Object var1) {
      if(var1 == null) {
         return false;
      } else {
         MapMakerInternalMap.Segment[] var2 = this.segments;
         long var3 = -1L;

         for(int var5 = 0; var5 < 3; ++var5) {
            long var6 = 0L;
            MapMakerInternalMap.Segment[] var8 = var2;
            int var9 = var2.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               MapMakerInternalMap.Segment var11 = var8[var10];
               int var12 = var11.count;
               AtomicReferenceArray var13 = var11.table;

               for(int var14 = 0; var14 < var13.length(); ++var14) {
                  for(MapMakerInternalMap.ReferenceEntry var15 = (MapMakerInternalMap.ReferenceEntry)var13.get(var14); var15 != null; var15 = var15.getNext()) {
                     Object var16 = var11.getLiveValue(var15);
                     if(var16 != null && this.valueEquivalence.equivalent(var1, var16)) {
                        return true;
                     }
                  }
               }

               var6 += (long)var11.modCount;
            }

            if(var6 == var3) {
               break;
            }

            var3 = var6;
         }

         return false;
      }
   }

   public V put(K var1, V var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      int var3 = this.hash(var1);
      return this.segmentFor(var3).put(var1, var3, var2, false);
   }

   public V putIfAbsent(K var1, V var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      int var3 = this.hash(var1);
      return this.segmentFor(var3).put(var1, var3, var2, true);
   }

   public void putAll(Map<? extends K, ? extends V> var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put(var3.getKey(), var3.getValue());
      }

   }

   public V remove(@Nullable Object var1) {
      if(var1 == null) {
         return null;
      } else {
         int var2 = this.hash(var1);
         return this.segmentFor(var2).remove(var1, var2);
      }
   }

   public boolean remove(@Nullable Object var1, @Nullable Object var2) {
      if(var1 != null && var2 != null) {
         int var3 = this.hash(var1);
         return this.segmentFor(var3).remove(var1, var3, var2);
      } else {
         return false;
      }
   }

   public boolean replace(K var1, @Nullable V var2, V var3) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var3);
      if(var2 == null) {
         return false;
      } else {
         int var4 = this.hash(var1);
         return this.segmentFor(var4).replace(var1, var4, var2, var3);
      }
   }

   public V replace(K var1, V var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      int var3 = this.hash(var1);
      return this.segmentFor(var3).replace(var1, var3, var2);
   }

   public void clear() {
      MapMakerInternalMap.Segment[] var1 = this.segments;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         MapMakerInternalMap.Segment var4 = var1[var3];
         var4.clear();
      }

   }

   public Set<K> keySet() {
      Set var1 = this.keySet;
      return var1 != null?var1:(this.keySet = new MapMakerInternalMap.KeySet());
   }

   public Collection<V> values() {
      Collection var1 = this.values;
      return var1 != null?var1:(this.values = new MapMakerInternalMap.Values());
   }

   public Set<Entry<K, V>> entrySet() {
      Set var1 = this.entrySet;
      return var1 != null?var1:(this.entrySet = new MapMakerInternalMap.EntrySet());
   }

   Object writeReplace() {
      return new MapMakerInternalMap.SerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this);
   }

   private static final class SerializationProxy<K, V> extends MapMakerInternalMap.AbstractSerializationProxy<K, V> {
      private static final long serialVersionUID = 3L;

      SerializationProxy(MapMakerInternalMap.Strength var1, MapMakerInternalMap.Strength var2, Equivalence<Object> var3, Equivalence<Object> var4, long var5, long var7, int var9, int var10, MapMaker.RemovalListener<? super K, ? super V> var11, ConcurrentMap<K, V> var12) {
         super(var1, var2, var3, var4, var5, var7, var9, var10, var11, var12);
      }

      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
         this.writeMapTo(var1);
      }

      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         MapMaker var2 = this.readMapMaker(var1);
         this.delegate = var2.makeMap();
         this.readEntries(var1);
      }

      private Object readResolve() {
         return this.delegate;
      }
   }

   abstract static class AbstractSerializationProxy<K, V> extends ForwardingConcurrentMap<K, V> implements Serializable {
      private static final long serialVersionUID = 3L;
      final MapMakerInternalMap.Strength keyStrength;
      final MapMakerInternalMap.Strength valueStrength;
      final Equivalence<Object> keyEquivalence;
      final Equivalence<Object> valueEquivalence;
      final long expireAfterWriteNanos;
      final long expireAfterAccessNanos;
      final int maximumSize;
      final int concurrencyLevel;
      final MapMaker.RemovalListener<? super K, ? super V> removalListener;
      transient ConcurrentMap<K, V> delegate;

      AbstractSerializationProxy(MapMakerInternalMap.Strength var1, MapMakerInternalMap.Strength var2, Equivalence<Object> var3, Equivalence<Object> var4, long var5, long var7, int var9, int var10, MapMaker.RemovalListener<? super K, ? super V> var11, ConcurrentMap<K, V> var12) {
         this.keyStrength = var1;
         this.valueStrength = var2;
         this.keyEquivalence = var3;
         this.valueEquivalence = var4;
         this.expireAfterWriteNanos = var5;
         this.expireAfterAccessNanos = var7;
         this.maximumSize = var9;
         this.concurrencyLevel = var10;
         this.removalListener = var11;
         this.delegate = var12;
      }

      protected ConcurrentMap<K, V> delegate() {
         return this.delegate;
      }

      void writeMapTo(ObjectOutputStream var1) throws IOException {
         var1.writeInt(this.delegate.size());
         Iterator var2 = this.delegate.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            var1.writeObject(var3.getKey());
            var1.writeObject(var3.getValue());
         }

         var1.writeObject((Object)null);
      }

      MapMaker readMapMaker(ObjectInputStream var1) throws IOException {
         int var2 = var1.readInt();
         MapMaker var3 = (new MapMaker()).initialCapacity(var2).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).concurrencyLevel(this.concurrencyLevel);
         var3.removalListener(this.removalListener);
         if(this.expireAfterWriteNanos > 0L) {
            var3.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
         }

         if(this.expireAfterAccessNanos > 0L) {
            var3.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
         }

         if(this.maximumSize != -1) {
            var3.maximumSize(this.maximumSize);
         }

         return var3;
      }

      void readEntries(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         while(true) {
            Object var2 = var1.readObject();
            if(var2 == null) {
               return;
            }

            Object var3 = var1.readObject();
            this.delegate.put(var2, var3);
         }
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Map delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   final class EntrySet extends AbstractSet<Entry<K, V>> {
      EntrySet() {
      }

      public Iterator<Entry<K, V>> iterator() {
         return MapMakerInternalMap.this.new EntryIterator();
      }

      public boolean contains(Object var1) {
         if(!(var1 instanceof Entry)) {
            return false;
         } else {
            Entry var2 = (Entry)var1;
            Object var3 = var2.getKey();
            if(var3 == null) {
               return false;
            } else {
               Object var4 = MapMakerInternalMap.this.get(var3);
               return var4 != null && MapMakerInternalMap.this.valueEquivalence.equivalent(var2.getValue(), var4);
            }
         }
      }

      public boolean remove(Object var1) {
         if(!(var1 instanceof Entry)) {
            return false;
         } else {
            Entry var2 = (Entry)var1;
            Object var3 = var2.getKey();
            return var3 != null && MapMakerInternalMap.this.remove(var3, var2.getValue());
         }
      }

      public int size() {
         return MapMakerInternalMap.this.size();
      }

      public boolean isEmpty() {
         return MapMakerInternalMap.this.isEmpty();
      }

      public void clear() {
         MapMakerInternalMap.this.clear();
      }
   }

   final class Values extends AbstractCollection<V> {
      Values() {
      }

      public Iterator<V> iterator() {
         return MapMakerInternalMap.this.new ValueIterator();
      }

      public int size() {
         return MapMakerInternalMap.this.size();
      }

      public boolean isEmpty() {
         return MapMakerInternalMap.this.isEmpty();
      }

      public boolean contains(Object var1) {
         return MapMakerInternalMap.this.containsValue(var1);
      }

      public void clear() {
         MapMakerInternalMap.this.clear();
      }
   }

   final class KeySet extends AbstractSet<K> {
      KeySet() {
      }

      public Iterator<K> iterator() {
         return MapMakerInternalMap.this.new KeyIterator();
      }

      public int size() {
         return MapMakerInternalMap.this.size();
      }

      public boolean isEmpty() {
         return MapMakerInternalMap.this.isEmpty();
      }

      public boolean contains(Object var1) {
         return MapMakerInternalMap.this.containsKey(var1);
      }

      public boolean remove(Object var1) {
         return MapMakerInternalMap.this.remove(var1) != null;
      }

      public void clear() {
         MapMakerInternalMap.this.clear();
      }
   }

   final class EntryIterator extends MapMakerInternalMap<K, V>.HashIterator<Entry<K, V>> {
      EntryIterator() {
         super();
      }

      public Entry<K, V> next() {
         return this.nextEntry();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object next() {
         return this.next();
      }
   }

   final class WriteThroughEntry extends AbstractMapEntry<K, V> {
      final K key;
      V value;

      WriteThroughEntry(K var1, V var2) {
         this.key = var2;
         this.value = var3;
      }

      public K getKey() {
         return this.key;
      }

      public V getValue() {
         return this.value;
      }

      public boolean equals(@Nullable Object var1) {
         if(!(var1 instanceof Entry)) {
            return false;
         } else {
            Entry var2 = (Entry)var1;
            return this.key.equals(var2.getKey()) && this.value.equals(var2.getValue());
         }
      }

      public int hashCode() {
         return this.key.hashCode() ^ this.value.hashCode();
      }

      public V setValue(V var1) {
         Object var2 = MapMakerInternalMap.this.put(this.key, var1);
         this.value = var1;
         return var2;
      }
   }

   final class ValueIterator extends MapMakerInternalMap<K, V>.HashIterator<V> {
      ValueIterator() {
         super();
      }

      public V next() {
         return this.nextEntry().getValue();
      }
   }

   final class KeyIterator extends MapMakerInternalMap<K, V>.HashIterator<K> {
      KeyIterator() {
         super();
      }

      public K next() {
         return this.nextEntry().getKey();
      }
   }

   abstract class HashIterator<E> implements Iterator<E> {
      int nextSegmentIndex;
      int nextTableIndex;
      MapMakerInternalMap.Segment<K, V> currentSegment;
      AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> currentTable;
      MapMakerInternalMap.ReferenceEntry<K, V> nextEntry;
      MapMakerInternalMap<K, V>.WriteThroughEntry nextExternal;
      MapMakerInternalMap<K, V>.WriteThroughEntry lastReturned;

      HashIterator() {
         this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
         this.nextTableIndex = -1;
         this.advance();
      }

      public abstract E next();

      final void advance() {
         this.nextExternal = null;
         if(!this.nextInChain()) {
            if(!this.nextInTable()) {
               while(this.nextSegmentIndex >= 0) {
                  this.currentSegment = MapMakerInternalMap.this.segments[this.nextSegmentIndex--];
                  if(this.currentSegment.count != 0) {
                     this.currentTable = this.currentSegment.table;
                     this.nextTableIndex = this.currentTable.length() - 1;
                     if(this.nextInTable()) {
                        return;
                     }
                  }
               }

            }
         }
      }

      boolean nextInChain() {
         if(this.nextEntry != null) {
            for(this.nextEntry = this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = this.nextEntry.getNext()) {
               if(this.advanceTo(this.nextEntry)) {
                  return true;
               }
            }
         }

         return false;
      }

      boolean nextInTable() {
         while(true) {
            if(this.nextTableIndex >= 0) {
               if((this.nextEntry = (MapMakerInternalMap.ReferenceEntry)this.currentTable.get(this.nextTableIndex--)) == null || !this.advanceTo(this.nextEntry) && !this.nextInChain()) {
                  continue;
               }

               return true;
            }

            return false;
         }
      }

      boolean advanceTo(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         boolean var4;
         try {
            Object var2 = var1.getKey();
            Object var3 = MapMakerInternalMap.this.getLiveValue(var1);
            if(var3 == null) {
               var4 = false;
               return var4;
            }

            this.nextExternal = MapMakerInternalMap.this.new WriteThroughEntry(var2, var3);
            var4 = true;
         } finally {
            this.currentSegment.postReadCleanup();
         }

         return var4;
      }

      public boolean hasNext() {
         return this.nextExternal != null;
      }

      MapMakerInternalMap<K, V>.WriteThroughEntry nextEntry() {
         if(this.nextExternal == null) {
            throw new NoSuchElementException();
         } else {
            this.lastReturned = this.nextExternal;
            this.advance();
            return this.lastReturned;
         }
      }

      public void remove() {
         CollectPreconditions.checkRemove(this.lastReturned != null);
         MapMakerInternalMap.this.remove(this.lastReturned.getKey());
         this.lastReturned = null;
      }
   }

   static final class CleanupMapTask implements Runnable {
      final WeakReference<MapMakerInternalMap<?, ?>> mapReference;

      public CleanupMapTask(MapMakerInternalMap<?, ?> var1) {
         this.mapReference = new WeakReference(var1);
      }

      public void run() {
         MapMakerInternalMap var1 = (MapMakerInternalMap)this.mapReference.get();
         if(var1 == null) {
            throw new CancellationException();
         } else {
            MapMakerInternalMap.Segment[] var2 = var1.segments;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               MapMakerInternalMap.Segment var5 = var2[var4];
               var5.runCleanup();
            }

         }
      }
   }

   static final class ExpirationQueue<K, V> extends AbstractQueue<MapMakerInternalMap.ReferenceEntry<K, V>> {
      final MapMakerInternalMap.ReferenceEntry<K, V> head = new MapMakerInternalMap.AbstractReferenceEntry() {
         MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable = this;
         MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable = this;

         public long getExpirationTime() {
            return Long.MAX_VALUE;
         }

         public void setExpirationTime(long var1) {
         }

         public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
         }

         public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
            this.nextExpirable = var1;
         }

         public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
         }

         public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
            this.previousExpirable = var1;
         }
      };

      ExpirationQueue() {
      }

      public boolean offer(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         MapMakerInternalMap.connectExpirables(var1.getPreviousExpirable(), var1.getNextExpirable());
         MapMakerInternalMap.connectExpirables(this.head.getPreviousExpirable(), var1);
         MapMakerInternalMap.connectExpirables(var1, this.head);
         return true;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> peek() {
         MapMakerInternalMap.ReferenceEntry var1 = this.head.getNextExpirable();
         return var1 == this.head?null:var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> poll() {
         MapMakerInternalMap.ReferenceEntry var1 = this.head.getNextExpirable();
         if(var1 == this.head) {
            return null;
         } else {
            this.remove(var1);
            return var1;
         }
      }

      public boolean remove(Object var1) {
         MapMakerInternalMap.ReferenceEntry var2 = (MapMakerInternalMap.ReferenceEntry)var1;
         MapMakerInternalMap.ReferenceEntry var3 = var2.getPreviousExpirable();
         MapMakerInternalMap.ReferenceEntry var4 = var2.getNextExpirable();
         MapMakerInternalMap.connectExpirables(var3, var4);
         MapMakerInternalMap.nullifyExpirable(var2);
         return var4 != MapMakerInternalMap.NullEntry.INSTANCE;
      }

      public boolean contains(Object var1) {
         MapMakerInternalMap.ReferenceEntry var2 = (MapMakerInternalMap.ReferenceEntry)var1;
         return var2.getNextExpirable() != MapMakerInternalMap.NullEntry.INSTANCE;
      }

      public boolean isEmpty() {
         return this.head.getNextExpirable() == this.head;
      }

      public int size() {
         int var1 = 0;

         for(MapMakerInternalMap.ReferenceEntry var2 = this.head.getNextExpirable(); var2 != this.head; var2 = var2.getNextExpirable()) {
            ++var1;
         }

         return var1;
      }

      public void clear() {
         MapMakerInternalMap.ReferenceEntry var2;
         for(MapMakerInternalMap.ReferenceEntry var1 = this.head.getNextExpirable(); var1 != this.head; var1 = var2) {
            var2 = var1.getNextExpirable();
            MapMakerInternalMap.nullifyExpirable(var1);
         }

         this.head.setNextExpirable(this.head);
         this.head.setPreviousExpirable(this.head);
      }

      public Iterator<MapMakerInternalMap.ReferenceEntry<K, V>> iterator() {
         return new AbstractSequentialIterator(this.peek()) {
            protected MapMakerInternalMap.ReferenceEntry<K, V> computeNext(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
               MapMakerInternalMap.ReferenceEntry var2 = var1.getNextExpirable();
               return var2 == ExpirationQueue.this.head?null:var2;
            }

            // $FF: synthetic method
            // $FF: bridge method
            protected Object computeNext(Object var1) {
               return this.computeNext((MapMakerInternalMap.ReferenceEntry)var1);
            }
         };
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object peek() {
         return this.peek();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object poll() {
         return this.poll();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public boolean offer(Object var1) {
         return this.offer((MapMakerInternalMap.ReferenceEntry)var1);
      }
   }

   static final class EvictionQueue<K, V> extends AbstractQueue<MapMakerInternalMap.ReferenceEntry<K, V>> {
      final MapMakerInternalMap.ReferenceEntry<K, V> head = new MapMakerInternalMap.AbstractReferenceEntry() {
         MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable = this;
         MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable = this;

         public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
         }

         public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
            this.nextEvictable = var1;
         }

         public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
         }

         public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
            this.previousEvictable = var1;
         }
      };

      EvictionQueue() {
      }

      public boolean offer(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         MapMakerInternalMap.connectEvictables(var1.getPreviousEvictable(), var1.getNextEvictable());
         MapMakerInternalMap.connectEvictables(this.head.getPreviousEvictable(), var1);
         MapMakerInternalMap.connectEvictables(var1, this.head);
         return true;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> peek() {
         MapMakerInternalMap.ReferenceEntry var1 = this.head.getNextEvictable();
         return var1 == this.head?null:var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> poll() {
         MapMakerInternalMap.ReferenceEntry var1 = this.head.getNextEvictable();
         if(var1 == this.head) {
            return null;
         } else {
            this.remove(var1);
            return var1;
         }
      }

      public boolean remove(Object var1) {
         MapMakerInternalMap.ReferenceEntry var2 = (MapMakerInternalMap.ReferenceEntry)var1;
         MapMakerInternalMap.ReferenceEntry var3 = var2.getPreviousEvictable();
         MapMakerInternalMap.ReferenceEntry var4 = var2.getNextEvictable();
         MapMakerInternalMap.connectEvictables(var3, var4);
         MapMakerInternalMap.nullifyEvictable(var2);
         return var4 != MapMakerInternalMap.NullEntry.INSTANCE;
      }

      public boolean contains(Object var1) {
         MapMakerInternalMap.ReferenceEntry var2 = (MapMakerInternalMap.ReferenceEntry)var1;
         return var2.getNextEvictable() != MapMakerInternalMap.NullEntry.INSTANCE;
      }

      public boolean isEmpty() {
         return this.head.getNextEvictable() == this.head;
      }

      public int size() {
         int var1 = 0;

         for(MapMakerInternalMap.ReferenceEntry var2 = this.head.getNextEvictable(); var2 != this.head; var2 = var2.getNextEvictable()) {
            ++var1;
         }

         return var1;
      }

      public void clear() {
         MapMakerInternalMap.ReferenceEntry var2;
         for(MapMakerInternalMap.ReferenceEntry var1 = this.head.getNextEvictable(); var1 != this.head; var1 = var2) {
            var2 = var1.getNextEvictable();
            MapMakerInternalMap.nullifyEvictable(var1);
         }

         this.head.setNextEvictable(this.head);
         this.head.setPreviousEvictable(this.head);
      }

      public Iterator<MapMakerInternalMap.ReferenceEntry<K, V>> iterator() {
         return new AbstractSequentialIterator(this.peek()) {
            protected MapMakerInternalMap.ReferenceEntry<K, V> computeNext(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
               MapMakerInternalMap.ReferenceEntry var2 = var1.getNextEvictable();
               return var2 == EvictionQueue.this.head?null:var2;
            }

            // $FF: synthetic method
            // $FF: bridge method
            protected Object computeNext(Object var1) {
               return this.computeNext((MapMakerInternalMap.ReferenceEntry)var1);
            }
         };
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object peek() {
         return this.peek();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object poll() {
         return this.poll();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public boolean offer(Object var1) {
         return this.offer((MapMakerInternalMap.ReferenceEntry)var1);
      }
   }

   static class Segment<K, V> extends ReentrantLock {
      final MapMakerInternalMap<K, V> map;
      volatile int count;
      int modCount;
      int threshold;
      volatile AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table;
      final int maxSegmentSize;
      final ReferenceQueue<K> keyReferenceQueue;
      final ReferenceQueue<V> valueReferenceQueue;
      final Queue<MapMakerInternalMap.ReferenceEntry<K, V>> recencyQueue;
      final AtomicInteger readCount = new AtomicInteger();
      @GuardedBy("Segment.this")
      final Queue<MapMakerInternalMap.ReferenceEntry<K, V>> evictionQueue;
      @GuardedBy("Segment.this")
      final Queue<MapMakerInternalMap.ReferenceEntry<K, V>> expirationQueue;

      Segment(MapMakerInternalMap<K, V> var1, int var2, int var3) {
         this.map = var1;
         this.maxSegmentSize = var3;
         this.initTable(this.newEntryArray(var2));
         this.keyReferenceQueue = var1.usesKeyReferences()?new ReferenceQueue():null;
         this.valueReferenceQueue = var1.usesValueReferences()?new ReferenceQueue():null;
         this.recencyQueue = (Queue)(!var1.evictsBySize() && !var1.expiresAfterAccess()?MapMakerInternalMap.DISCARDING_QUEUE:new ConcurrentLinkedQueue());
         this.evictionQueue = (Queue)(var1.evictsBySize()?new MapMakerInternalMap.EvictionQueue():MapMakerInternalMap.DISCARDING_QUEUE);
         this.expirationQueue = (Queue)(var1.expires()?new MapMakerInternalMap.ExpirationQueue():MapMakerInternalMap.DISCARDING_QUEUE);
      }

      AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> newEntryArray(int var1) {
         return new AtomicReferenceArray(var1);
      }

      void initTable(AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> var1) {
         this.threshold = var1.length() * 3 / 4;
         if(this.threshold == this.maxSegmentSize) {
            ++this.threshold;
         }

         this.table = var1;
      }

      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> newEntry(K var1, int var2, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var3) {
         return this.map.entryFactory.newEntry(this, var1, var2, var3);
      }

      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.ReferenceEntry<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2) {
         if(var1.getKey() == null) {
            return null;
         } else {
            MapMakerInternalMap.ValueReference var3 = var1.getValueReference();
            Object var4 = var3.get();
            if(var4 == null && !var3.isComputingReference()) {
               return null;
            } else {
               MapMakerInternalMap.ReferenceEntry var5 = this.map.entryFactory.copyEntry(this, var1, var2);
               var5.setValueReference(var3.copyFor(this.valueReferenceQueue, var4, var5));
               return var5;
            }
         }
      }

      @GuardedBy("Segment.this")
      void setValue(MapMakerInternalMap.ReferenceEntry<K, V> var1, V var2) {
         MapMakerInternalMap.ValueReference var3 = this.map.valueStrength.referenceValue(this, var1, var2);
         var1.setValueReference(var3);
         this.recordWrite(var1);
      }

      void tryDrainReferenceQueues() {
         if(this.tryLock()) {
            try {
               this.drainReferenceQueues();
            } finally {
               this.unlock();
            }
         }

      }

      @GuardedBy("Segment.this")
      void drainReferenceQueues() {
         if(this.map.usesKeyReferences()) {
            this.drainKeyReferenceQueue();
         }

         if(this.map.usesValueReferences()) {
            this.drainValueReferenceQueue();
         }

      }

      @GuardedBy("Segment.this")
      void drainKeyReferenceQueue() {
         int var2 = 0;

         Reference var1;
         while((var1 = this.keyReferenceQueue.poll()) != null) {
            MapMakerInternalMap.ReferenceEntry var3 = (MapMakerInternalMap.ReferenceEntry)var1;
            this.map.reclaimKey(var3);
            ++var2;
            if(var2 == 16) {
               break;
            }
         }

      }

      @GuardedBy("Segment.this")
      void drainValueReferenceQueue() {
         int var2 = 0;

         Reference var1;
         while((var1 = this.valueReferenceQueue.poll()) != null) {
            MapMakerInternalMap.ValueReference var3 = (MapMakerInternalMap.ValueReference)var1;
            this.map.reclaimValue(var3);
            ++var2;
            if(var2 == 16) {
               break;
            }
         }

      }

      void clearReferenceQueues() {
         if(this.map.usesKeyReferences()) {
            this.clearKeyReferenceQueue();
         }

         if(this.map.usesValueReferences()) {
            this.clearValueReferenceQueue();
         }

      }

      void clearKeyReferenceQueue() {
         while(this.keyReferenceQueue.poll() != null) {
            ;
         }

      }

      void clearValueReferenceQueue() {
         while(this.valueReferenceQueue.poll() != null) {
            ;
         }

      }

      void recordRead(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         if(this.map.expiresAfterAccess()) {
            this.recordExpirationTime(var1, this.map.expireAfterAccessNanos);
         }

         this.recencyQueue.add(var1);
      }

      @GuardedBy("Segment.this")
      void recordLockedRead(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.evictionQueue.add(var1);
         if(this.map.expiresAfterAccess()) {
            this.recordExpirationTime(var1, this.map.expireAfterAccessNanos);
            this.expirationQueue.add(var1);
         }

      }

      @GuardedBy("Segment.this")
      void recordWrite(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.drainRecencyQueue();
         this.evictionQueue.add(var1);
         if(this.map.expires()) {
            long var2 = this.map.expiresAfterAccess()?this.map.expireAfterAccessNanos:this.map.expireAfterWriteNanos;
            this.recordExpirationTime(var1, var2);
            this.expirationQueue.add(var1);
         }

      }

      @GuardedBy("Segment.this")
      void drainRecencyQueue() {
         MapMakerInternalMap.ReferenceEntry var1;
         while((var1 = (MapMakerInternalMap.ReferenceEntry)this.recencyQueue.poll()) != null) {
            if(this.evictionQueue.contains(var1)) {
               this.evictionQueue.add(var1);
            }

            if(this.map.expiresAfterAccess() && this.expirationQueue.contains(var1)) {
               this.expirationQueue.add(var1);
            }
         }

      }

      void recordExpirationTime(MapMakerInternalMap.ReferenceEntry<K, V> var1, long var2) {
         var1.setExpirationTime(this.map.ticker.read() + var2);
      }

      void tryExpireEntries() {
         if(this.tryLock()) {
            try {
               this.expireEntries();
            } finally {
               this.unlock();
            }
         }

      }

      @GuardedBy("Segment.this")
      void expireEntries() {
         this.drainRecencyQueue();
         if(!this.expirationQueue.isEmpty()) {
            long var1 = this.map.ticker.read();

            MapMakerInternalMap.ReferenceEntry var3;
            while((var3 = (MapMakerInternalMap.ReferenceEntry)this.expirationQueue.peek()) != null && this.map.isExpired(var3, var1)) {
               if(!this.removeEntry(var3, var3.getHash(), MapMaker.RemovalCause.EXPIRED)) {
                  throw new AssertionError();
               }
            }

         }
      }

      void enqueueNotification(MapMakerInternalMap.ReferenceEntry<K, V> var1, MapMaker.RemovalCause var2) {
         this.enqueueNotification(var1.getKey(), var1.getHash(), var1.getValueReference().get(), var2);
      }

      void enqueueNotification(@Nullable K var1, int var2, @Nullable V var3, MapMaker.RemovalCause var4) {
         if(this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
            MapMaker.RemovalNotification var5 = new MapMaker.RemovalNotification(var1, var3, var4);
            this.map.removalNotificationQueue.offer(var5);
         }

      }

      @GuardedBy("Segment.this")
      boolean evictEntries() {
         if(this.map.evictsBySize() && this.count >= this.maxSegmentSize) {
            this.drainRecencyQueue();
            MapMakerInternalMap.ReferenceEntry var1 = (MapMakerInternalMap.ReferenceEntry)this.evictionQueue.remove();
            if(!this.removeEntry(var1, var1.getHash(), MapMaker.RemovalCause.SIZE)) {
               throw new AssertionError();
            } else {
               return true;
            }
         } else {
            return false;
         }
      }

      MapMakerInternalMap.ReferenceEntry<K, V> getFirst(int var1) {
         AtomicReferenceArray var2 = this.table;
         return (MapMakerInternalMap.ReferenceEntry)var2.get(var1 & var2.length() - 1);
      }

      MapMakerInternalMap.ReferenceEntry<K, V> getEntry(Object var1, int var2) {
         if(this.count != 0) {
            for(MapMakerInternalMap.ReferenceEntry var3 = this.getFirst(var2); var3 != null; var3 = var3.getNext()) {
               if(var3.getHash() == var2) {
                  Object var4 = var3.getKey();
                  if(var4 == null) {
                     this.tryDrainReferenceQueues();
                  } else if(this.map.keyEquivalence.equivalent(var1, var4)) {
                     return var3;
                  }
               }
            }
         }

         return null;
      }

      MapMakerInternalMap.ReferenceEntry<K, V> getLiveEntry(Object var1, int var2) {
         MapMakerInternalMap.ReferenceEntry var3 = this.getEntry(var1, var2);
         if(var3 == null) {
            return null;
         } else if(this.map.expires() && this.map.isExpired(var3)) {
            this.tryExpireEntries();
            return null;
         } else {
            return var3;
         }
      }

      V get(Object var1, int var2) {
         Object var5;
         try {
            MapMakerInternalMap.ReferenceEntry var3 = this.getLiveEntry(var1, var2);
            Object var4;
            if(var3 == null) {
               var4 = null;
               return var4;
            }

            var4 = var3.getValueReference().get();
            if(var4 != null) {
               this.recordRead(var3);
            } else {
               this.tryDrainReferenceQueues();
            }

            var5 = var4;
         } finally {
            this.postReadCleanup();
         }

         return var5;
      }

      boolean containsKey(Object var1, int var2) {
         boolean var3;
         try {
            if(this.count != 0) {
               MapMakerInternalMap.ReferenceEntry var8 = this.getLiveEntry(var1, var2);
               boolean var4;
               if(var8 == null) {
                  var4 = false;
                  return var4;
               }

               var4 = var8.getValueReference().get() != null;
               return var4;
            }

            var3 = false;
         } finally {
            this.postReadCleanup();
         }

         return var3;
      }

      @VisibleForTesting
      boolean containsValue(Object var1) {
         try {
            if(this.count != 0) {
               AtomicReferenceArray var2 = this.table;
               int var3 = var2.length();

               for(int var4 = 0; var4 < var3; ++var4) {
                  for(MapMakerInternalMap.ReferenceEntry var5 = (MapMakerInternalMap.ReferenceEntry)var2.get(var4); var5 != null; var5 = var5.getNext()) {
                     Object var6 = this.getLiveValue(var5);
                     if(var6 != null && this.map.valueEquivalence.equivalent(var1, var6)) {
                        boolean var7 = true;
                        return var7;
                     }
                  }
               }
            }

            boolean var11 = false;
            return var11;
         } finally {
            this.postReadCleanup();
         }
      }

      V put(K var1, int var2, V var3, boolean var4) {
         this.lock();

         Object var10;
         try {
            this.preWriteCleanup();
            int var5 = this.count + 1;
            if(var5 > this.threshold) {
               this.expand();
               var5 = this.count + 1;
            }

            AtomicReferenceArray var6 = this.table;
            int var7 = var2 & var6.length() - 1;
            MapMakerInternalMap.ReferenceEntry var8 = (MapMakerInternalMap.ReferenceEntry)var6.get(var7);

            MapMakerInternalMap.ReferenceEntry var9;
            for(var9 = var8; var9 != null; var9 = var9.getNext()) {
               var10 = var9.getKey();
               if(var9.getHash() == var2 && var10 != null && this.map.keyEquivalence.equivalent(var1, var10)) {
                  MapMakerInternalMap.ValueReference var11 = var9.getValueReference();
                  Object var12 = var11.get();
                  Object var13;
                  if(var12 == null) {
                     ++this.modCount;
                     this.setValue(var9, var3);
                     if(!var11.isComputingReference()) {
                        this.enqueueNotification(var1, var2, var12, MapMaker.RemovalCause.COLLECTED);
                        var5 = this.count;
                     } else if(this.evictEntries()) {
                        var5 = this.count + 1;
                     }

                     this.count = var5;
                     var13 = null;
                     return var13;
                  }

                  if(var4) {
                     this.recordLockedRead(var9);
                     var13 = var12;
                     return var13;
                  }

                  ++this.modCount;
                  this.enqueueNotification(var1, var2, var12, MapMaker.RemovalCause.REPLACED);
                  this.setValue(var9, var3);
                  var13 = var12;
                  return var13;
               }
            }

            ++this.modCount;
            var9 = this.newEntry(var1, var2, var8);
            this.setValue(var9, var3);
            var6.set(var7, var9);
            if(this.evictEntries()) {
               var5 = this.count + 1;
            }

            this.count = var5;
            var10 = null;
         } finally {
            this.unlock();
            this.postWriteCleanup();
         }

         return var10;
      }

      @GuardedBy("Segment.this")
      void expand() {
         AtomicReferenceArray var1 = this.table;
         int var2 = var1.length();
         if(var2 < 1073741824) {
            int var3 = this.count;
            AtomicReferenceArray var4 = this.newEntryArray(var2 << 1);
            this.threshold = var4.length() * 3 / 4;
            int var5 = var4.length() - 1;

            for(int var6 = 0; var6 < var2; ++var6) {
               MapMakerInternalMap.ReferenceEntry var7 = (MapMakerInternalMap.ReferenceEntry)var1.get(var6);
               if(var7 != null) {
                  MapMakerInternalMap.ReferenceEntry var8 = var7.getNext();
                  int var9 = var7.getHash() & var5;
                  if(var8 == null) {
                     var4.set(var9, var7);
                  } else {
                     MapMakerInternalMap.ReferenceEntry var10 = var7;
                     int var11 = var9;

                     MapMakerInternalMap.ReferenceEntry var12;
                     int var13;
                     for(var12 = var8; var12 != null; var12 = var12.getNext()) {
                        var13 = var12.getHash() & var5;
                        if(var13 != var11) {
                           var11 = var13;
                           var10 = var12;
                        }
                     }

                     var4.set(var11, var10);

                     for(var12 = var7; var12 != var10; var12 = var12.getNext()) {
                        var13 = var12.getHash() & var5;
                        MapMakerInternalMap.ReferenceEntry var14 = (MapMakerInternalMap.ReferenceEntry)var4.get(var13);
                        MapMakerInternalMap.ReferenceEntry var15 = this.copyEntry(var12, var14);
                        if(var15 != null) {
                           var4.set(var13, var15);
                        } else {
                           this.removeCollectedEntry(var12);
                           --var3;
                        }
                     }
                  }
               }
            }

            this.table = var4;
            this.count = var3;
         }
      }

      boolean replace(K var1, int var2, V var3, V var4) {
         this.lock();

         try {
            this.preWriteCleanup();
            AtomicReferenceArray var5 = this.table;
            int var6 = var2 & var5.length() - 1;
            MapMakerInternalMap.ReferenceEntry var7 = (MapMakerInternalMap.ReferenceEntry)var5.get(var6);

            for(MapMakerInternalMap.ReferenceEntry var8 = var7; var8 != null; var8 = var8.getNext()) {
               Object var9 = var8.getKey();
               if(var8.getHash() == var2 && var9 != null && this.map.keyEquivalence.equivalent(var1, var9)) {
                  MapMakerInternalMap.ValueReference var10 = var8.getValueReference();
                  Object var11 = var10.get();
                  boolean var18;
                  if(var11 != null) {
                     if(this.map.valueEquivalence.equivalent(var3, var11)) {
                        ++this.modCount;
                        this.enqueueNotification(var1, var2, var11, MapMaker.RemovalCause.REPLACED);
                        this.setValue(var8, var4);
                        var18 = true;
                        return var18;
                     }

                     this.recordLockedRead(var8);
                     var18 = false;
                     return var18;
                  }

                  if(this.isCollected(var10)) {
                     int var12 = this.count - 1;
                     ++this.modCount;
                     this.enqueueNotification(var9, var2, var11, MapMaker.RemovalCause.COLLECTED);
                     MapMakerInternalMap.ReferenceEntry var13 = this.removeFromChain(var7, var8);
                     var12 = this.count - 1;
                     var5.set(var6, var13);
                     this.count = var12;
                  }

                  var18 = false;
                  return var18;
               }
            }

            boolean var17 = false;
            return var17;
         } finally {
            this.unlock();
            this.postWriteCleanup();
         }
      }

      V replace(K var1, int var2, V var3) {
         this.lock();

         MapMakerInternalMap.ReferenceEntry var7;
         try {
            this.preWriteCleanup();
            AtomicReferenceArray var4 = this.table;
            int var5 = var2 & var4.length() - 1;
            MapMakerInternalMap.ReferenceEntry var6 = (MapMakerInternalMap.ReferenceEntry)var4.get(var5);

            for(var7 = var6; var7 != null; var7 = var7.getNext()) {
               Object var8 = var7.getKey();
               if(var7.getHash() == var2 && var8 != null && this.map.keyEquivalence.equivalent(var1, var8)) {
                  MapMakerInternalMap.ValueReference var9 = var7.getValueReference();
                  Object var10 = var9.get();
                  Object var11;
                  if(var10 == null) {
                     if(this.isCollected(var9)) {
                        int var16 = this.count - 1;
                        ++this.modCount;
                        this.enqueueNotification(var8, var2, var10, MapMaker.RemovalCause.COLLECTED);
                        MapMakerInternalMap.ReferenceEntry var12 = this.removeFromChain(var6, var7);
                        var16 = this.count - 1;
                        var4.set(var5, var12);
                        this.count = var16;
                     }

                     var11 = null;
                     return var11;
                  }

                  ++this.modCount;
                  this.enqueueNotification(var1, var2, var10, MapMaker.RemovalCause.REPLACED);
                  this.setValue(var7, var3);
                  var11 = var10;
                  return var11;
               }
            }

            var7 = null;
         } finally {
            this.unlock();
            this.postWriteCleanup();
         }

         return var7;
      }

      V remove(Object var1, int var2) {
         this.lock();

         try {
            this.preWriteCleanup();
            int var3 = this.count - 1;
            AtomicReferenceArray var4 = this.table;
            int var5 = var2 & var4.length() - 1;
            MapMakerInternalMap.ReferenceEntry var6 = (MapMakerInternalMap.ReferenceEntry)var4.get(var5);

            MapMakerInternalMap.ReferenceEntry var7;
            for(var7 = var6; var7 != null; var7 = var7.getNext()) {
               Object var8 = var7.getKey();
               if(var7.getHash() == var2 && var8 != null && this.map.keyEquivalence.equivalent(var1, var8)) {
                  MapMakerInternalMap.ValueReference var9 = var7.getValueReference();
                  Object var10 = var9.get();
                  MapMaker.RemovalCause var11;
                  MapMakerInternalMap.ReferenceEntry var12;
                  if(var10 != null) {
                     var11 = MapMaker.RemovalCause.EXPLICIT;
                  } else {
                     if(!this.isCollected(var9)) {
                        var12 = null;
                        return var12;
                     }

                     var11 = MapMaker.RemovalCause.COLLECTED;
                  }

                  ++this.modCount;
                  this.enqueueNotification(var8, var2, var10, var11);
                  var12 = this.removeFromChain(var6, var7);
                  var3 = this.count - 1;
                  var4.set(var5, var12);
                  this.count = var3;
                  Object var13 = var10;
                  return var13;
               }
            }

            var7 = null;
            return var7;
         } finally {
            this.unlock();
            this.postWriteCleanup();
         }
      }

      boolean remove(Object var1, int var2, Object var3) {
         this.lock();

         try {
            this.preWriteCleanup();
            int var4 = this.count - 1;
            AtomicReferenceArray var5 = this.table;
            int var6 = var2 & var5.length() - 1;
            MapMakerInternalMap.ReferenceEntry var7 = (MapMakerInternalMap.ReferenceEntry)var5.get(var6);

            for(MapMakerInternalMap.ReferenceEntry var8 = var7; var8 != null; var8 = var8.getNext()) {
               Object var9 = var8.getKey();
               if(var8.getHash() == var2 && var9 != null && this.map.keyEquivalence.equivalent(var1, var9)) {
                  MapMakerInternalMap.ValueReference var10 = var8.getValueReference();
                  Object var11 = var10.get();
                  MapMaker.RemovalCause var12;
                  if(this.map.valueEquivalence.equivalent(var3, var11)) {
                     var12 = MapMaker.RemovalCause.EXPLICIT;
                  } else {
                     if(!this.isCollected(var10)) {
                        boolean var19 = false;
                        return var19;
                     }

                     var12 = MapMaker.RemovalCause.COLLECTED;
                  }

                  ++this.modCount;
                  this.enqueueNotification(var9, var2, var11, var12);
                  MapMakerInternalMap.ReferenceEntry var13 = this.removeFromChain(var7, var8);
                  var4 = this.count - 1;
                  var5.set(var6, var13);
                  this.count = var4;
                  boolean var14 = var12 == MapMaker.RemovalCause.EXPLICIT;
                  return var14;
               }
            }

            boolean var18 = false;
            return var18;
         } finally {
            this.unlock();
            this.postWriteCleanup();
         }
      }

      void clear() {
         if(this.count != 0) {
            this.lock();

            try {
               AtomicReferenceArray var1 = this.table;
               int var2;
               if(this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
                  for(var2 = 0; var2 < var1.length(); ++var2) {
                     for(MapMakerInternalMap.ReferenceEntry var3 = (MapMakerInternalMap.ReferenceEntry)var1.get(var2); var3 != null; var3 = var3.getNext()) {
                        if(!var3.getValueReference().isComputingReference()) {
                           this.enqueueNotification(var3, MapMaker.RemovalCause.EXPLICIT);
                        }
                     }
                  }
               }

               for(var2 = 0; var2 < var1.length(); ++var2) {
                  var1.set(var2, (Object)null);
               }

               this.clearReferenceQueues();
               this.evictionQueue.clear();
               this.expirationQueue.clear();
               this.readCount.set(0);
               ++this.modCount;
               this.count = 0;
            } finally {
               this.unlock();
               this.postWriteCleanup();
            }
         }

      }

      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> removeFromChain(MapMakerInternalMap.ReferenceEntry<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2) {
         this.evictionQueue.remove(var2);
         this.expirationQueue.remove(var2);
         int var3 = this.count;
         MapMakerInternalMap.ReferenceEntry var4 = var2.getNext();

         for(MapMakerInternalMap.ReferenceEntry var5 = var1; var5 != var2; var5 = var5.getNext()) {
            MapMakerInternalMap.ReferenceEntry var6 = this.copyEntry(var5, var4);
            if(var6 != null) {
               var4 = var6;
            } else {
               this.removeCollectedEntry(var5);
               --var3;
            }
         }

         this.count = var3;
         return var4;
      }

      void removeCollectedEntry(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.enqueueNotification(var1, MapMaker.RemovalCause.COLLECTED);
         this.evictionQueue.remove(var1);
         this.expirationQueue.remove(var1);
      }

      boolean reclaimKey(MapMakerInternalMap.ReferenceEntry<K, V> var1, int var2) {
         this.lock();

         try {
            int var3 = this.count - 1;
            AtomicReferenceArray var4 = this.table;
            int var5 = var2 & var4.length() - 1;
            MapMakerInternalMap.ReferenceEntry var6 = (MapMakerInternalMap.ReferenceEntry)var4.get(var5);

            for(MapMakerInternalMap.ReferenceEntry var7 = var6; var7 != null; var7 = var7.getNext()) {
               if(var7 == var1) {
                  ++this.modCount;
                  this.enqueueNotification(var7.getKey(), var2, var7.getValueReference().get(), MapMaker.RemovalCause.COLLECTED);
                  MapMakerInternalMap.ReferenceEntry var8 = this.removeFromChain(var6, var7);
                  var3 = this.count - 1;
                  var4.set(var5, var8);
                  this.count = var3;
                  boolean var9 = true;
                  return var9;
               }
            }

            boolean var13 = false;
            return var13;
         } finally {
            this.unlock();
            this.postWriteCleanup();
         }
      }

      boolean reclaimValue(K var1, int var2, MapMakerInternalMap.ValueReference<K, V> var3) {
         this.lock();

         try {
            int var4 = this.count - 1;
            AtomicReferenceArray var5 = this.table;
            int var6 = var2 & var5.length() - 1;
            MapMakerInternalMap.ReferenceEntry var7 = (MapMakerInternalMap.ReferenceEntry)var5.get(var6);

            for(MapMakerInternalMap.ReferenceEntry var8 = var7; var8 != null; var8 = var8.getNext()) {
               Object var9 = var8.getKey();
               if(var8.getHash() == var2 && var9 != null && this.map.keyEquivalence.equivalent(var1, var9)) {
                  MapMakerInternalMap.ValueReference var10 = var8.getValueReference();
                  if(var10 != var3) {
                     boolean var17 = false;
                     return var17;
                  }

                  ++this.modCount;
                  this.enqueueNotification(var1, var2, var3.get(), MapMaker.RemovalCause.COLLECTED);
                  MapMakerInternalMap.ReferenceEntry var11 = this.removeFromChain(var7, var8);
                  var4 = this.count - 1;
                  var5.set(var6, var11);
                  this.count = var4;
                  boolean var12 = true;
                  return var12;
               }
            }

            boolean var16 = false;
            return var16;
         } finally {
            this.unlock();
            if(!this.isHeldByCurrentThread()) {
               this.postWriteCleanup();
            }

         }
      }

      boolean clearValue(K var1, int var2, MapMakerInternalMap.ValueReference<K, V> var3) {
         this.lock();

         try {
            AtomicReferenceArray var4 = this.table;
            int var5 = var2 & var4.length() - 1;
            MapMakerInternalMap.ReferenceEntry var6 = (MapMakerInternalMap.ReferenceEntry)var4.get(var5);

            for(MapMakerInternalMap.ReferenceEntry var7 = var6; var7 != null; var7 = var7.getNext()) {
               Object var8 = var7.getKey();
               if(var7.getHash() == var2 && var8 != null && this.map.keyEquivalence.equivalent(var1, var8)) {
                  MapMakerInternalMap.ValueReference var9 = var7.getValueReference();
                  if(var9 == var3) {
                     MapMakerInternalMap.ReferenceEntry var15 = this.removeFromChain(var6, var7);
                     var4.set(var5, var15);
                     boolean var11 = true;
                     return var11;
                  }

                  boolean var10 = false;
                  return var10;
               }
            }

            boolean var16 = false;
            return var16;
         } finally {
            this.unlock();
            this.postWriteCleanup();
         }
      }

      @GuardedBy("Segment.this")
      boolean removeEntry(MapMakerInternalMap.ReferenceEntry<K, V> var1, int var2, MapMaker.RemovalCause var3) {
         int var4 = this.count - 1;
         AtomicReferenceArray var5 = this.table;
         int var6 = var2 & var5.length() - 1;
         MapMakerInternalMap.ReferenceEntry var7 = (MapMakerInternalMap.ReferenceEntry)var5.get(var6);

         for(MapMakerInternalMap.ReferenceEntry var8 = var7; var8 != null; var8 = var8.getNext()) {
            if(var8 == var1) {
               ++this.modCount;
               this.enqueueNotification(var8.getKey(), var2, var8.getValueReference().get(), var3);
               MapMakerInternalMap.ReferenceEntry var9 = this.removeFromChain(var7, var8);
               var4 = this.count - 1;
               var5.set(var6, var9);
               this.count = var4;
               return true;
            }
         }

         return false;
      }

      boolean isCollected(MapMakerInternalMap.ValueReference<K, V> var1) {
         return var1.isComputingReference()?false:var1.get() == null;
      }

      V getLiveValue(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         if(var1.getKey() == null) {
            this.tryDrainReferenceQueues();
            return null;
         } else {
            Object var2 = var1.getValueReference().get();
            if(var2 == null) {
               this.tryDrainReferenceQueues();
               return null;
            } else if(this.map.expires() && this.map.isExpired(var1)) {
               this.tryExpireEntries();
               return null;
            } else {
               return var2;
            }
         }
      }

      void postReadCleanup() {
         if((this.readCount.incrementAndGet() & 63) == 0) {
            this.runCleanup();
         }

      }

      @GuardedBy("Segment.this")
      void preWriteCleanup() {
         this.runLockedCleanup();
      }

      void postWriteCleanup() {
         this.runUnlockedCleanup();
      }

      void runCleanup() {
         this.runLockedCleanup();
         this.runUnlockedCleanup();
      }

      void runLockedCleanup() {
         if(this.tryLock()) {
            try {
               this.drainReferenceQueues();
               this.expireEntries();
               this.readCount.set(0);
            } finally {
               this.unlock();
            }
         }

      }

      void runUnlockedCleanup() {
         if(!this.isHeldByCurrentThread()) {
            this.map.processPendingNotifications();
         }

      }
   }

   static final class StrongValueReference<K, V> implements MapMakerInternalMap.ValueReference<K, V> {
      final V referent;

      StrongValueReference(V var1) {
         this.referent = var1;
      }

      public V get() {
         return this.referent;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
         return null;
      }

      public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> var1, V var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
         return this;
      }

      public boolean isComputingReference() {
         return false;
      }

      public V waitForValue() {
         return this.get();
      }

      public void clear(MapMakerInternalMap.ValueReference<K, V> var1) {
      }
   }

   static final class SoftValueReference<K, V> extends SoftReference<V> implements MapMakerInternalMap.ValueReference<K, V> {
      final MapMakerInternalMap.ReferenceEntry<K, V> entry;

      SoftValueReference(ReferenceQueue<V> var1, V var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
         super(var2, var1);
         this.entry = var3;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
         return this.entry;
      }

      public void clear(MapMakerInternalMap.ValueReference<K, V> var1) {
         this.clear();
      }

      public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> var1, V var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
         return new MapMakerInternalMap.SoftValueReference(var1, var2, var3);
      }

      public boolean isComputingReference() {
         return false;
      }

      public V waitForValue() {
         return this.get();
      }
   }

   static final class WeakValueReference<K, V> extends WeakReference<V> implements MapMakerInternalMap.ValueReference<K, V> {
      final MapMakerInternalMap.ReferenceEntry<K, V> entry;

      WeakValueReference(ReferenceQueue<V> var1, V var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
         super(var2, var1);
         this.entry = var3;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
         return this.entry;
      }

      public void clear(MapMakerInternalMap.ValueReference<K, V> var1) {
         this.clear();
      }

      public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> var1, V var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
         return new MapMakerInternalMap.WeakValueReference(var1, var2, var3);
      }

      public boolean isComputingReference() {
         return false;
      }

      public V waitForValue() {
         return this.get();
      }
   }

   static final class WeakExpirableEvictableEntry<K, V> extends MapMakerInternalMap.WeakEntry<K, V> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      volatile long time = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

      WeakExpirableEvictableEntry(ReferenceQueue<K> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
         super(var1, var2, var3, var4);
      }

      public long getExpirationTime() {
         return this.time;
      }

      public void setExpirationTime(long var1) {
         this.time = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
         return this.nextExpirable;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.nextExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
         return this.previousExpirable;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.previousExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
         return this.nextEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.nextEvictable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
         return this.previousEvictable;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.previousEvictable = var1;
      }
   }

   static final class WeakEvictableEntry<K, V> extends MapMakerInternalMap.WeakEntry<K, V> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

      WeakEvictableEntry(ReferenceQueue<K> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
         super(var1, var2, var3, var4);
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
         return this.nextEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.nextEvictable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
         return this.previousEvictable;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.previousEvictable = var1;
      }
   }

   static final class WeakExpirableEntry<K, V> extends MapMakerInternalMap.WeakEntry<K, V> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      volatile long time = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();

      WeakExpirableEntry(ReferenceQueue<K> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
         super(var1, var2, var3, var4);
      }

      public long getExpirationTime() {
         return this.time;
      }

      public void setExpirationTime(long var1) {
         this.time = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
         return this.nextExpirable;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.nextExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
         return this.previousExpirable;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.previousExpirable = var1;
      }
   }

   static class WeakEntry<K, V> extends WeakReference<K> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      final int hash;
      final MapMakerInternalMap.ReferenceEntry<K, V> next;
      volatile MapMakerInternalMap.ValueReference<K, V> valueReference;

      WeakEntry(ReferenceQueue<K> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
         super(var2, var1);
         this.valueReference = MapMakerInternalMap.UNSET;
         this.hash = var3;
         this.next = var4;
      }

      public K getKey() {
         return this.get();
      }

      public long getExpirationTime() {
         throw new UnsupportedOperationException();
      }

      public void setExpirationTime(long var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ValueReference<K, V> getValueReference() {
         return this.valueReference;
      }

      public void setValueReference(MapMakerInternalMap.ValueReference<K, V> var1) {
         MapMakerInternalMap.ValueReference var2 = this.valueReference;
         this.valueReference = var1;
         var2.clear(var1);
      }

      public int getHash() {
         return this.hash;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNext() {
         return this.next;
      }
   }

   static final class SoftExpirableEvictableEntry<K, V> extends MapMakerInternalMap.SoftEntry<K, V> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      volatile long time = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

      SoftExpirableEvictableEntry(ReferenceQueue<K> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
         super(var1, var2, var3, var4);
      }

      public long getExpirationTime() {
         return this.time;
      }

      public void setExpirationTime(long var1) {
         this.time = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
         return this.nextExpirable;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.nextExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
         return this.previousExpirable;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.previousExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
         return this.nextEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.nextEvictable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
         return this.previousEvictable;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.previousEvictable = var1;
      }
   }

   static final class SoftEvictableEntry<K, V> extends MapMakerInternalMap.SoftEntry<K, V> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

      SoftEvictableEntry(ReferenceQueue<K> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
         super(var1, var2, var3, var4);
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
         return this.nextEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.nextEvictable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
         return this.previousEvictable;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.previousEvictable = var1;
      }
   }

   static final class SoftExpirableEntry<K, V> extends MapMakerInternalMap.SoftEntry<K, V> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      volatile long time = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();

      SoftExpirableEntry(ReferenceQueue<K> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
         super(var1, var2, var3, var4);
      }

      public long getExpirationTime() {
         return this.time;
      }

      public void setExpirationTime(long var1) {
         this.time = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
         return this.nextExpirable;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.nextExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
         return this.previousExpirable;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.previousExpirable = var1;
      }
   }

   static class SoftEntry<K, V> extends SoftReference<K> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      final int hash;
      final MapMakerInternalMap.ReferenceEntry<K, V> next;
      volatile MapMakerInternalMap.ValueReference<K, V> valueReference;

      SoftEntry(ReferenceQueue<K> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
         super(var2, var1);
         this.valueReference = MapMakerInternalMap.UNSET;
         this.hash = var3;
         this.next = var4;
      }

      public K getKey() {
         return this.get();
      }

      public long getExpirationTime() {
         throw new UnsupportedOperationException();
      }

      public void setExpirationTime(long var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ValueReference<K, V> getValueReference() {
         return this.valueReference;
      }

      public void setValueReference(MapMakerInternalMap.ValueReference<K, V> var1) {
         MapMakerInternalMap.ValueReference var2 = this.valueReference;
         this.valueReference = var1;
         var2.clear(var1);
      }

      public int getHash() {
         return this.hash;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNext() {
         return this.next;
      }
   }

   static final class StrongExpirableEvictableEntry<K, V> extends MapMakerInternalMap.StrongEntry<K, V> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      volatile long time = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

      StrongExpirableEvictableEntry(K var1, int var2, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var3) {
         super(var1, var2, var3);
      }

      public long getExpirationTime() {
         return this.time;
      }

      public void setExpirationTime(long var1) {
         this.time = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
         return this.nextExpirable;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.nextExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
         return this.previousExpirable;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.previousExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
         return this.nextEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.nextEvictable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
         return this.previousEvictable;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.previousEvictable = var1;
      }
   }

   static final class StrongEvictableEntry<K, V> extends MapMakerInternalMap.StrongEntry<K, V> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

      StrongEvictableEntry(K var1, int var2, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var3) {
         super(var1, var2, var3);
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
         return this.nextEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.nextEvictable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
         return this.previousEvictable;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.previousEvictable = var1;
      }
   }

   static final class StrongExpirableEntry<K, V> extends MapMakerInternalMap.StrongEntry<K, V> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      volatile long time = Long.MAX_VALUE;
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
      @GuardedBy("Segment.this")
      MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();

      StrongExpirableEntry(K var1, int var2, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var3) {
         super(var1, var2, var3);
      }

      public long getExpirationTime() {
         return this.time;
      }

      public void setExpirationTime(long var1) {
         this.time = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
         return this.nextExpirable;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.nextExpirable = var1;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
         return this.previousExpirable;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         this.previousExpirable = var1;
      }
   }

   static class StrongEntry<K, V> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      final K key;
      final int hash;
      final MapMakerInternalMap.ReferenceEntry<K, V> next;
      volatile MapMakerInternalMap.ValueReference<K, V> valueReference;

      StrongEntry(K var1, int var2, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var3) {
         this.valueReference = MapMakerInternalMap.UNSET;
         this.key = var1;
         this.hash = var2;
         this.next = var3;
      }

      public K getKey() {
         return this.key;
      }

      public long getExpirationTime() {
         throw new UnsupportedOperationException();
      }

      public void setExpirationTime(long var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ValueReference<K, V> getValueReference() {
         return this.valueReference;
      }

      public void setValueReference(MapMakerInternalMap.ValueReference<K, V> var1) {
         MapMakerInternalMap.ValueReference var2 = this.valueReference;
         this.valueReference = var1;
         var2.clear(var1);
      }

      public int getHash() {
         return this.hash;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNext() {
         return this.next;
      }
   }

   abstract static class AbstractReferenceEntry<K, V> implements MapMakerInternalMap.ReferenceEntry<K, V> {
      AbstractReferenceEntry() {
      }

      public MapMakerInternalMap.ValueReference<K, V> getValueReference() {
         throw new UnsupportedOperationException();
      }

      public void setValueReference(MapMakerInternalMap.ValueReference<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNext() {
         throw new UnsupportedOperationException();
      }

      public int getHash() {
         throw new UnsupportedOperationException();
      }

      public K getKey() {
         throw new UnsupportedOperationException();
      }

      public long getExpirationTime() {
         throw new UnsupportedOperationException();
      }

      public void setExpirationTime(long var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
         throw new UnsupportedOperationException();
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1) {
         throw new UnsupportedOperationException();
      }
   }

   private static enum NullEntry implements MapMakerInternalMap.ReferenceEntry<Object, Object> {
      INSTANCE;

      private NullEntry() {
      }

      public MapMakerInternalMap.ValueReference<Object, Object> getValueReference() {
         return null;
      }

      public void setValueReference(MapMakerInternalMap.ValueReference<Object, Object> var1) {
      }

      public MapMakerInternalMap.ReferenceEntry<Object, Object> getNext() {
         return null;
      }

      public int getHash() {
         return 0;
      }

      public Object getKey() {
         return null;
      }

      public long getExpirationTime() {
         return 0L;
      }

      public void setExpirationTime(long var1) {
      }

      public MapMakerInternalMap.ReferenceEntry<Object, Object> getNextExpirable() {
         return this;
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<Object, Object> var1) {
      }

      public MapMakerInternalMap.ReferenceEntry<Object, Object> getPreviousExpirable() {
         return this;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<Object, Object> var1) {
      }

      public MapMakerInternalMap.ReferenceEntry<Object, Object> getNextEvictable() {
         return this;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<Object, Object> var1) {
      }

      public MapMakerInternalMap.ReferenceEntry<Object, Object> getPreviousEvictable() {
         return this;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<Object, Object> var1) {
      }
   }

   interface ReferenceEntry<K, V> {
      MapMakerInternalMap.ValueReference<K, V> getValueReference();

      void setValueReference(MapMakerInternalMap.ValueReference<K, V> var1);

      MapMakerInternalMap.ReferenceEntry<K, V> getNext();

      int getHash();

      K getKey();

      long getExpirationTime();

      void setExpirationTime(long var1);

      MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable();

      void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1);

      MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable();

      void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> var1);

      MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable();

      void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1);

      MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable();

      void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> var1);
   }

   interface ValueReference<K, V> {
      V get();

      V waitForValue() throws ExecutionException;

      MapMakerInternalMap.ReferenceEntry<K, V> getEntry();

      MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> var1, @Nullable V var2, MapMakerInternalMap.ReferenceEntry<K, V> var3);

      void clear(@Nullable MapMakerInternalMap.ValueReference<K, V> var1);

      boolean isComputingReference();
   }

   static enum EntryFactory {
      STRONG {
         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
            return new MapMakerInternalMap.StrongEntry(var2, var3, var4);
         }
      },
      STRONG_EXPIRABLE {
         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
            return new MapMakerInternalMap.StrongExpirableEntry(var2, var3, var4);
         }

         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
            MapMakerInternalMap.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyExpirableEntry(var2, var4);
            return var4;
         }
      },
      STRONG_EVICTABLE {
         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
            return new MapMakerInternalMap.StrongEvictableEntry(var2, var3, var4);
         }

         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
            MapMakerInternalMap.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyEvictableEntry(var2, var4);
            return var4;
         }
      },
      STRONG_EXPIRABLE_EVICTABLE {
         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
            return new MapMakerInternalMap.StrongExpirableEvictableEntry(var2, var3, var4);
         }

         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
            MapMakerInternalMap.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyExpirableEntry(var2, var4);
            this.copyEvictableEntry(var2, var4);
            return var4;
         }
      },
      WEAK {
         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
            return new MapMakerInternalMap.WeakEntry(var1.keyReferenceQueue, var2, var3, var4);
         }
      },
      WEAK_EXPIRABLE {
         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
            return new MapMakerInternalMap.WeakExpirableEntry(var1.keyReferenceQueue, var2, var3, var4);
         }

         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
            MapMakerInternalMap.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyExpirableEntry(var2, var4);
            return var4;
         }
      },
      WEAK_EVICTABLE {
         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
            return new MapMakerInternalMap.WeakEvictableEntry(var1.keyReferenceQueue, var2, var3, var4);
         }

         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
            MapMakerInternalMap.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyEvictableEntry(var2, var4);
            return var4;
         }
      },
      WEAK_EXPIRABLE_EVICTABLE {
         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4) {
            return new MapMakerInternalMap.WeakExpirableEvictableEntry(var1.keyReferenceQueue, var2, var3, var4);
         }

         <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
            MapMakerInternalMap.ReferenceEntry var4 = super.copyEntry(var1, var2, var3);
            this.copyExpirableEntry(var2, var4);
            this.copyEvictableEntry(var2, var4);
            return var4;
         }
      };

      static final int EXPIRABLE_MASK = 1;
      static final int EVICTABLE_MASK = 2;
      static final MapMakerInternalMap.EntryFactory[][] factories;

      private EntryFactory() {
      }

      static MapMakerInternalMap.EntryFactory getFactory(MapMakerInternalMap.Strength var0, boolean var1, boolean var2) {
         int var3 = (var1?1:0) | (var2?2:0);
         return factories[var0.ordinal()][var3];
      }

      abstract <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> var1, K var2, int var3, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> var4);

      @GuardedBy("Segment.this")
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
         return this.newEntry(var1, var2.getKey(), var2.getHash(), var3);
      }

      @GuardedBy("Segment.this")
      <K, V> void copyExpirableEntry(MapMakerInternalMap.ReferenceEntry<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2) {
         var2.setExpirationTime(var1.getExpirationTime());
         MapMakerInternalMap.connectExpirables(var1.getPreviousExpirable(), var2);
         MapMakerInternalMap.connectExpirables(var2, var1.getNextExpirable());
         MapMakerInternalMap.nullifyExpirable(var1);
      }

      @GuardedBy("Segment.this")
      <K, V> void copyEvictableEntry(MapMakerInternalMap.ReferenceEntry<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2) {
         MapMakerInternalMap.connectEvictables(var1.getPreviousEvictable(), var2);
         MapMakerInternalMap.connectEvictables(var2, var1.getNextEvictable());
         MapMakerInternalMap.nullifyEvictable(var1);
      }

      // $FF: synthetic method
      EntryFactory(Object var3) {
         this();
      }

      static {
         factories = new MapMakerInternalMap.EntryFactory[][]{{STRONG, STRONG_EXPIRABLE, STRONG_EVICTABLE, STRONG_EXPIRABLE_EVICTABLE}, new MapMakerInternalMap.EntryFactory[0], {WEAK, WEAK_EXPIRABLE, WEAK_EVICTABLE, WEAK_EXPIRABLE_EVICTABLE}};
      }
   }

   static enum Strength {
      STRONG {
         <K, V> MapMakerInternalMap.ValueReference<K, V> referenceValue(MapMakerInternalMap.Segment<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2, V var3) {
            return new MapMakerInternalMap.StrongValueReference(var3);
         }

         Equivalence<Object> defaultEquivalence() {
            return Equivalence.equals();
         }
      },
      SOFT {
         <K, V> MapMakerInternalMap.ValueReference<K, V> referenceValue(MapMakerInternalMap.Segment<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2, V var3) {
            return new MapMakerInternalMap.SoftValueReference(var1.valueReferenceQueue, var3, var2);
         }

         Equivalence<Object> defaultEquivalence() {
            return Equivalence.identity();
         }
      },
      WEAK {
         <K, V> MapMakerInternalMap.ValueReference<K, V> referenceValue(MapMakerInternalMap.Segment<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2, V var3) {
            return new MapMakerInternalMap.WeakValueReference(var1.valueReferenceQueue, var3, var2);
         }

         Equivalence<Object> defaultEquivalence() {
            return Equivalence.identity();
         }
      };

      private Strength() {
      }

      abstract <K, V> MapMakerInternalMap.ValueReference<K, V> referenceValue(MapMakerInternalMap.Segment<K, V> var1, MapMakerInternalMap.ReferenceEntry<K, V> var2, V var3);

      abstract Equivalence<Object> defaultEquivalence();

      // $FF: synthetic method
      Strength(Object var3) {
         this();
      }
   }
}
