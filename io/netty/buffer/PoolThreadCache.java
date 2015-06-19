package io.netty.buffer;

import io.netty.buffer.PoolArena;
import io.netty.buffer.PoolChunk;
import io.netty.buffer.PooledByteBuf;
import io.netty.util.ThreadDeathWatcher;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;

final class PoolThreadCache {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PoolThreadCache.class);
   final PoolArena<byte[]> heapArena;
   final PoolArena<ByteBuffer> directArena;
   private final PoolThreadCache.MemoryRegionCache<byte[]>[] tinySubPageHeapCaches;
   private final PoolThreadCache.MemoryRegionCache<byte[]>[] smallSubPageHeapCaches;
   private final PoolThreadCache.MemoryRegionCache<ByteBuffer>[] tinySubPageDirectCaches;
   private final PoolThreadCache.MemoryRegionCache<ByteBuffer>[] smallSubPageDirectCaches;
   private final PoolThreadCache.MemoryRegionCache<byte[]>[] normalHeapCaches;
   private final PoolThreadCache.MemoryRegionCache<ByteBuffer>[] normalDirectCaches;
   private final int numShiftsNormalDirect;
   private final int numShiftsNormalHeap;
   private final int freeSweepAllocationThreshold;
   private int allocations;
   private final Thread thread = Thread.currentThread();
   private final Runnable freeTask = new Runnable() {
      public void run() {
         PoolThreadCache.this.free0();
      }
   };

   PoolThreadCache(PoolArena<byte[]> var1, PoolArena<ByteBuffer> var2, int var3, int var4, int var5, int var6, int var7) {
      if(var6 < 0) {
         throw new IllegalArgumentException("maxCachedBufferCapacity: " + var6 + " (expected: >= 0)");
      } else if(var7 < 1) {
         throw new IllegalArgumentException("freeSweepAllocationThreshold: " + var6 + " (expected: > 0)");
      } else {
         this.freeSweepAllocationThreshold = var7;
         this.heapArena = var1;
         this.directArena = var2;
         if(var2 != null) {
            this.tinySubPageDirectCaches = createSubPageCaches(var3, 32);
            this.smallSubPageDirectCaches = createSubPageCaches(var4, var2.numSmallSubpagePools);
            this.numShiftsNormalDirect = log2(var2.pageSize);
            this.normalDirectCaches = createNormalCaches(var5, var6, var2);
         } else {
            this.tinySubPageDirectCaches = null;
            this.smallSubPageDirectCaches = null;
            this.normalDirectCaches = null;
            this.numShiftsNormalDirect = -1;
         }

         if(var1 != null) {
            this.tinySubPageHeapCaches = createSubPageCaches(var3, 32);
            this.smallSubPageHeapCaches = createSubPageCaches(var4, var1.numSmallSubpagePools);
            this.numShiftsNormalHeap = log2(var1.pageSize);
            this.normalHeapCaches = createNormalCaches(var5, var6, var1);
         } else {
            this.tinySubPageHeapCaches = null;
            this.smallSubPageHeapCaches = null;
            this.normalHeapCaches = null;
            this.numShiftsNormalHeap = -1;
         }

         ThreadDeathWatcher.watch(this.thread, this.freeTask);
      }
   }

   private static <T> PoolThreadCache.SubPageMemoryRegionCache<T>[] createSubPageCaches(int var0, int var1) {
      if(var0 <= 0) {
         return null;
      } else {
         PoolThreadCache.SubPageMemoryRegionCache[] var2 = new PoolThreadCache.SubPageMemoryRegionCache[var1];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = new PoolThreadCache.SubPageMemoryRegionCache(var0);
         }

         return var2;
      }
   }

   private static <T> PoolThreadCache.NormalMemoryRegionCache<T>[] createNormalCaches(int var0, int var1, PoolArena<T> var2) {
      if(var0 <= 0) {
         return null;
      } else {
         int var3 = Math.min(var2.chunkSize, var1);
         int var4 = Math.max(1, var3 / var2.pageSize);
         PoolThreadCache.NormalMemoryRegionCache[] var5 = new PoolThreadCache.NormalMemoryRegionCache[var4];

         for(int var6 = 0; var6 < var5.length; ++var6) {
            var5[var6] = new PoolThreadCache.NormalMemoryRegionCache(var0);
         }

         return var5;
      }
   }

   private static int log2(int var0) {
      int var1;
      for(var1 = 0; var0 > 1; ++var1) {
         var0 >>= 1;
      }

      return var1;
   }

   boolean allocateTiny(PoolArena<?> var1, PooledByteBuf<?> var2, int var3, int var4) {
      return this.allocate(this.cacheForTiny(var1, var4), var2, var3);
   }

   boolean allocateSmall(PoolArena<?> var1, PooledByteBuf<?> var2, int var3, int var4) {
      return this.allocate(this.cacheForSmall(var1, var4), var2, var3);
   }

   boolean allocateNormal(PoolArena<?> var1, PooledByteBuf<?> var2, int var3, int var4) {
      return this.allocate(this.cacheForNormal(var1, var4), var2, var3);
   }

   private boolean allocate(PoolThreadCache.MemoryRegionCache<?> var1, PooledByteBuf var2, int var3) {
      if(var1 == null) {
         return false;
      } else {
         boolean var4 = var1.allocate(var2, var3);
         if(++this.allocations >= this.freeSweepAllocationThreshold) {
            this.allocations = 0;
            this.trim();
         }

         return var4;
      }
   }

   boolean add(PoolArena<?> var1, PoolChunk var2, long var3, int var5) {
      PoolThreadCache.MemoryRegionCache var6;
      if(var1.isTinyOrSmall(var5)) {
         if(PoolArena.isTiny(var5)) {
            var6 = this.cacheForTiny(var1, var5);
         } else {
            var6 = this.cacheForSmall(var1, var5);
         }
      } else {
         var6 = this.cacheForNormal(var1, var5);
      }

      return var6 == null?false:var6.add(var2, var3);
   }

   void free() {
      ThreadDeathWatcher.unwatch(this.thread, this.freeTask);
      this.free0();
   }

   private void free0() {
      int var1 = free(this.tinySubPageDirectCaches) + free(this.smallSubPageDirectCaches) + free(this.normalDirectCaches) + free(this.tinySubPageHeapCaches) + free(this.smallSubPageHeapCaches) + free(this.normalHeapCaches);
      if(var1 > 0 && logger.isDebugEnabled()) {
         logger.debug("Freed {} thread-local buffer(s) from thread: {}", Integer.valueOf(var1), this.thread.getName());
      }

   }

   private static int free(PoolThreadCache.MemoryRegionCache<?>[] var0) {
      if(var0 == null) {
         return 0;
      } else {
         int var1 = 0;
         PoolThreadCache.MemoryRegionCache[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            PoolThreadCache.MemoryRegionCache var5 = var2[var4];
            var1 += free(var5);
         }

         return var1;
      }
   }

   private static int free(PoolThreadCache.MemoryRegionCache<?> var0) {
      return var0 == null?0:var0.free();
   }

   void trim() {
      trim(this.tinySubPageDirectCaches);
      trim(this.smallSubPageDirectCaches);
      trim(this.normalDirectCaches);
      trim(this.tinySubPageHeapCaches);
      trim(this.smallSubPageHeapCaches);
      trim(this.normalHeapCaches);
   }

   private static void trim(PoolThreadCache.MemoryRegionCache<?>[] var0) {
      if(var0 != null) {
         PoolThreadCache.MemoryRegionCache[] var1 = var0;
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            PoolThreadCache.MemoryRegionCache var4 = var1[var3];
            trim(var4);
         }

      }
   }

   private static void trim(PoolThreadCache.MemoryRegionCache<?> var0) {
      if(var0 != null) {
         var0.trim();
      }
   }

   private PoolThreadCache.MemoryRegionCache<?> cacheForTiny(PoolArena<?> var1, int var2) {
      int var3 = PoolArena.tinyIdx(var2);
      return var1.isDirect()?cache(this.tinySubPageDirectCaches, var3):cache(this.tinySubPageHeapCaches, var3);
   }

   private PoolThreadCache.MemoryRegionCache<?> cacheForSmall(PoolArena<?> var1, int var2) {
      int var3 = PoolArena.smallIdx(var2);
      return var1.isDirect()?cache(this.smallSubPageDirectCaches, var3):cache(this.smallSubPageHeapCaches, var3);
   }

   private PoolThreadCache.MemoryRegionCache<?> cacheForNormal(PoolArena<?> var1, int var2) {
      int var3;
      if(var1.isDirect()) {
         var3 = log2(var2 >> this.numShiftsNormalDirect);
         return cache(this.normalDirectCaches, var3);
      } else {
         var3 = log2(var2 >> this.numShiftsNormalHeap);
         return cache(this.normalHeapCaches, var3);
      }
   }

   private static <T> PoolThreadCache.MemoryRegionCache<T> cache(PoolThreadCache.MemoryRegionCache<T>[] var0, int var1) {
      return var0 != null && var1 <= var0.length - 1?var0[var1]:null;
   }

   private abstract static class MemoryRegionCache<T> {
      private final PoolThreadCache.MemoryRegionCache.MemoryRegionCache$Entry<T>[] entries;
      private final int maxUnusedCached;
      private int head;
      private int tail;
      private int maxEntriesInUse;
      private int entriesInUse;

      MemoryRegionCache(int var1) {
         this.entries = new PoolThreadCache.MemoryRegionCache.MemoryRegionCache$Entry[powerOfTwo(var1)];

         for(int var2 = 0; var2 < this.entries.length; ++var2) {
            this.entries[var2] = new PoolThreadCache.MemoryRegionCache.MemoryRegionCache$Entry(null);
         }

         this.maxUnusedCached = var1 / 2;
      }

      private static int powerOfTwo(int var0) {
         if(var0 <= 2) {
            return 2;
         } else {
            --var0;
            var0 |= var0 >> 1;
            var0 |= var0 >> 2;
            var0 |= var0 >> 4;
            var0 |= var0 >> 8;
            var0 |= var0 >> 16;
            ++var0;
            return var0;
         }
      }

      protected abstract void initBuf(PoolChunk<T> var1, long var2, PooledByteBuf<T> var4, int var5);

      public boolean add(PoolChunk<T> var1, long var2) {
         PoolThreadCache.MemoryRegionCache.MemoryRegionCache$Entry var4 = this.entries[this.tail];
         if(var4.chunk != null) {
            return false;
         } else {
            --this.entriesInUse;
            var4.chunk = var1;
            var4.handle = var2;
            this.tail = this.nextIdx(this.tail);
            return true;
         }
      }

      public boolean allocate(PooledByteBuf<T> var1, int var2) {
         PoolThreadCache.MemoryRegionCache.MemoryRegionCache$Entry var3 = this.entries[this.head];
         if(var3.chunk == null) {
            return false;
         } else {
            ++this.entriesInUse;
            if(this.maxEntriesInUse < this.entriesInUse) {
               this.maxEntriesInUse = this.entriesInUse;
            }

            this.initBuf(var3.chunk, var3.handle, var1, var2);
            var3.chunk = null;
            this.head = this.nextIdx(this.head);
            return true;
         }
      }

      public int free() {
         int var1 = 0;
         this.entriesInUse = 0;
         this.maxEntriesInUse = 0;

         for(int var2 = this.head; freeEntry(this.entries[var2]); var2 = this.nextIdx(var2)) {
            ++var1;
         }

         return var1;
      }

      private void trim() {
         int var1 = this.size() - this.maxEntriesInUse;
         this.entriesInUse = 0;
         this.maxEntriesInUse = 0;
         if(var1 > this.maxUnusedCached) {
            for(int var2 = this.head; var1 > 0; --var1) {
               if(!freeEntry(this.entries[var2])) {
                  return;
               }

               var2 = this.nextIdx(var2);
            }

         }
      }

      private static boolean freeEntry(PoolThreadCache.MemoryRegionCache.MemoryRegionCache$Entry var0) {
         PoolChunk var1 = var0.chunk;
         if(var1 == null) {
            return false;
         } else {
            PoolArena var2 = var1.arena;
            synchronized(var1.arena) {
               var1.parent.free(var1, var0.handle);
            }

            var0.chunk = null;
            return true;
         }
      }

      private int size() {
         return this.tail - this.head & this.entries.length - 1;
      }

      private int nextIdx(int var1) {
         return var1 + 1 & this.entries.length - 1;
      }

      private static final class MemoryRegionCache$Entry<T> {
         PoolChunk<T> chunk;
         long handle;

         private MemoryRegionCache$Entry() {
         }

         // $FF: synthetic method
         MemoryRegionCache$Entry(Object var1) {
            this();
         }
      }
   }

   private static final class NormalMemoryRegionCache<T> extends PoolThreadCache.MemoryRegionCache<T> {
      NormalMemoryRegionCache(int var1) {
         super(var1);
      }

      protected void initBuf(PoolChunk<T> var1, long var2, PooledByteBuf<T> var4, int var5) {
         var1.initBuf(var4, var2, var5);
      }
   }

   private static final class SubPageMemoryRegionCache<T> extends PoolThreadCache.MemoryRegionCache<T> {
      SubPageMemoryRegionCache(int var1) {
         super(var1);
      }

      protected void initBuf(PoolChunk<T> var1, long var2, PooledByteBuf<T> var4, int var5) {
         var1.initBufWithSubpage(var4, var2, var5);
      }
   }
}
