package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;
import com.google.common.collect.MapMakerInternalMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReferenceArray;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

class ComputingConcurrentHashMap<K, V> extends MapMakerInternalMap<K, V> {
   final Function<? super K, ? extends V> computingFunction;
   private static final long serialVersionUID = 4L;

   ComputingConcurrentHashMap(MapMaker var1, Function<? super K, ? extends V> var2) {
      super(var1);
      this.computingFunction = (Function)Preconditions.checkNotNull(var2);
   }

   MapMakerInternalMap.Segment<K, V> createSegment(int var1, int var2) {
      return new ComputingConcurrentHashMap.ComputingSegment(this, var1, var2);
   }

   ComputingConcurrentHashMap.ComputingSegment<K, V> segmentFor(int var1) {
      return (ComputingConcurrentHashMap.ComputingSegment)super.segmentFor(var1);
   }

   V getOrCompute(K var1) throws ExecutionException {
      int var2 = this.hash(Preconditions.checkNotNull(var1));
      return this.segmentFor(var2).getOrCompute(var1, var2, this.computingFunction);
   }

   Object writeReplace() {
      return new ComputingConcurrentHashMap.ComputingSerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this, this.computingFunction);
   }

   // $FF: synthetic method
   // $FF: bridge method
   MapMakerInternalMap.Segment segmentFor(int var1) {
      return this.segmentFor(var1);
   }

   static final class ComputingSerializationProxy<K, V> extends MapMakerInternalMap.AbstractSerializationProxy<K, V> {
      final Function<? super K, ? extends V> computingFunction;
      private static final long serialVersionUID = 4L;

      ComputingSerializationProxy(MapMakerInternalMap.Strength var1, MapMakerInternalMap.Strength var2, Equivalence<Object> var3, Equivalence<Object> var4, long var5, long var7, int var9, int var10, MapMaker.RemovalListener<? super K, ? super V> var11, ConcurrentMap<K, V> var12, Function<? super K, ? extends V> var13) {
         super(var1, var2, var3, var4, var5, var7, var9, var10, var11, var12);
         this.computingFunction = var13;
      }

      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
         this.writeMapTo(var1);
      }

      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         MapMaker var2 = this.readMapMaker(var1);
         this.delegate = var2.makeComputingMap(this.computingFunction);
         this.readEntries(var1);
      }

      Object readResolve() {
         return this.delegate;
      }
   }

   private static final class ComputingValueReference<K, V> implements MapMakerInternalMap.ValueReference<K, V> {
      final Function<? super K, ? extends V> computingFunction;
      @GuardedBy("ComputingValueReference.this")
      volatile MapMakerInternalMap.ValueReference<K, V> computedReference = MapMakerInternalMap.unset();

      public ComputingValueReference(Function<? super K, ? extends V> var1) {
         this.computingFunction = var1;
      }

      public V get() {
         return null;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
         return null;
      }

      public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> var1, @Nullable V var2, MapMakerInternalMap.ReferenceEntry<K, V> var3) {
         return this;
      }

      public boolean isComputingReference() {
         return true;
      }

      public V waitForValue() throws ExecutionException {
         if(this.computedReference == MapMakerInternalMap.UNSET) {
            boolean var1 = false;

            try {
               synchronized(this) {
                  while(this.computedReference == MapMakerInternalMap.UNSET) {
                     try {
                        this.wait();
                     } catch (InterruptedException var9) {
                        var1 = true;
                     }
                  }
               }
            } finally {
               if(var1) {
                  Thread.currentThread().interrupt();
               }

            }
         }

         return this.computedReference.waitForValue();
      }

      public void clear(MapMakerInternalMap.ValueReference<K, V> var1) {
         this.setValueReference(var1);
      }

      V compute(K var1, int var2) throws ExecutionException {
         Object var3;
         try {
            var3 = this.computingFunction.apply(var1);
         } catch (Throwable var5) {
            this.setValueReference(new ComputingConcurrentHashMap.ComputationExceptionReference(var5));
            throw new ExecutionException(var5);
         }

         this.setValueReference(new ComputingConcurrentHashMap.ComputedReference(var3));
         return var3;
      }

      void setValueReference(MapMakerInternalMap.ValueReference<K, V> var1) {
         synchronized(this) {
            if(this.computedReference == MapMakerInternalMap.UNSET) {
               this.computedReference = var1;
               this.notifyAll();
            }

         }
      }
   }

   private static final class ComputedReference<K, V> implements MapMakerInternalMap.ValueReference<K, V> {
      final V value;

      ComputedReference(@Nullable V var1) {
         this.value = var1;
      }

      public V get() {
         return this.value;
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

   private static final class ComputationExceptionReference<K, V> implements MapMakerInternalMap.ValueReference<K, V> {
      final Throwable t;

      ComputationExceptionReference(Throwable var1) {
         this.t = var1;
      }

      public V get() {
         return null;
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

      public V waitForValue() throws ExecutionException {
         throw new ExecutionException(this.t);
      }

      public void clear(MapMakerInternalMap.ValueReference<K, V> var1) {
      }
   }

   static final class ComputingSegment<K, V> extends MapMakerInternalMap.Segment<K, V> {
      ComputingSegment(MapMakerInternalMap<K, V> var1, int var2, int var3) {
         super(var1, var2, var3);
      }

      V getOrCompute(K var1, int var2, Function<? super K, ? extends V> var3) throws ExecutionException {
         try {
            MapMakerInternalMap.ReferenceEntry var4;
            Object var5;
            Object var24;
            do {
               var4 = this.getEntry(var1, var2);
               if(var4 != null) {
                  var5 = this.getLiveValue(var4);
                  if(var5 != null) {
                     this.recordRead(var4);
                     var24 = var5;
                     return var24;
                  }
               }

               if(var4 == null || !var4.getValueReference().isComputingReference()) {
                  boolean var23 = true;
                  ComputingConcurrentHashMap.ComputingValueReference var6 = null;
                  this.lock();

                  try {
                     this.preWriteCleanup();
                     int var7 = this.count - 1;
                     AtomicReferenceArray var8 = this.table;
                     int var9 = var2 & var8.length() - 1;
                     MapMakerInternalMap.ReferenceEntry var10 = (MapMakerInternalMap.ReferenceEntry)var8.get(var9);

                     for(var4 = var10; var4 != null; var4 = var4.getNext()) {
                        Object var11 = var4.getKey();
                        if(var4.getHash() == var2 && var11 != null && this.map.keyEquivalence.equivalent(var1, var11)) {
                           MapMakerInternalMap.ValueReference var12 = var4.getValueReference();
                           if(var12.isComputingReference()) {
                              var23 = false;
                              break;
                           }

                           Object var13 = var4.getValueReference().get();
                           if(var13 == null) {
                              this.enqueueNotification(var11, var2, var13, MapMaker.RemovalCause.COLLECTED);
                           } else {
                              if(!this.map.expires() || !this.map.isExpired(var4)) {
                                 this.recordLockedRead(var4);
                                 Object var14 = var13;
                                 return var14;
                              }

                              this.enqueueNotification(var11, var2, var13, MapMaker.RemovalCause.EXPIRED);
                           }

                           this.evictionQueue.remove(var4);
                           this.expirationQueue.remove(var4);
                           this.count = var7;
                           break;
                        }
                     }

                     if(var23) {
                        var6 = new ComputingConcurrentHashMap.ComputingValueReference(var3);
                        if(var4 == null) {
                           var4 = this.newEntry(var1, var2, var10);
                           var4.setValueReference(var6);
                           var8.set(var9, var4);
                        } else {
                           var4.setValueReference(var6);
                        }
                     }
                  } finally {
                     this.unlock();
                     this.postWriteCleanup();
                  }

                  if(var23) {
                     Object var25 = this.compute(var1, var2, var4, var6);
                     return var25;
                  }
               }

               Preconditions.checkState(!Thread.holdsLock(var4), "Recursive computation");
               var5 = var4.getValueReference().waitForValue();
            } while(var5 == null);

            this.recordRead(var4);
            var24 = var5;
            return var24;
         } finally {
            this.postReadCleanup();
         }
      }

      V compute(K var1, int var2, MapMakerInternalMap.ReferenceEntry<K, V> var3, ComputingConcurrentHashMap.ComputingValueReference<K, V> var4) throws ExecutionException {
         Object var5 = null;
         long var6 = System.nanoTime();
         long var8 = 0L;

         Object var10;
         try {
            synchronized(var3) {
               var5 = var4.compute(var1, var2);
               var8 = System.nanoTime();
            }

            if(var5 != null) {
               var10 = this.put(var1, var2, var5, true);
               if(var10 != null) {
                  this.enqueueNotification(var1, var2, var5, MapMaker.RemovalCause.REPLACED);
               }
            }

            var10 = var5;
         } finally {
            if(var8 == 0L) {
               var8 = System.nanoTime();
            }

            if(var5 == null) {
               this.clearValue(var1, var2, var4);
            }

         }

         return var10;
      }
   }
}
