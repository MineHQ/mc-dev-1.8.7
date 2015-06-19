package io.netty.buffer;

import io.netty.buffer.AbstractByteBufAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PoolArena;
import io.netty.buffer.PoolThreadCache;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class PooledByteBufAllocator extends AbstractByteBufAllocator {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PooledByteBufAllocator.class);
   private static final int DEFAULT_NUM_HEAP_ARENA;
   private static final int DEFAULT_NUM_DIRECT_ARENA;
   private static final int DEFAULT_PAGE_SIZE;
   private static final int DEFAULT_MAX_ORDER;
   private static final int DEFAULT_TINY_CACHE_SIZE;
   private static final int DEFAULT_SMALL_CACHE_SIZE;
   private static final int DEFAULT_NORMAL_CACHE_SIZE;
   private static final int DEFAULT_MAX_CACHED_BUFFER_CAPACITY;
   private static final int DEFAULT_CACHE_TRIM_INTERVAL;
   private static final int MIN_PAGE_SIZE = 4096;
   private static final int MAX_CHUNK_SIZE = 1073741824;
   public static final PooledByteBufAllocator DEFAULT;
   private final PoolArena<byte[]>[] heapArenas;
   private final PoolArena<ByteBuffer>[] directArenas;
   private final int tinyCacheSize;
   private final int smallCacheSize;
   private final int normalCacheSize;
   final PooledByteBufAllocator.PoolThreadLocalCache threadCache;

   public PooledByteBufAllocator() {
      this(false);
   }

   public PooledByteBufAllocator(boolean var1) {
      this(var1, DEFAULT_NUM_HEAP_ARENA, DEFAULT_NUM_DIRECT_ARENA, DEFAULT_PAGE_SIZE, DEFAULT_MAX_ORDER);
   }

   public PooledByteBufAllocator(int var1, int var2, int var3, int var4) {
      this(false, var1, var2, var3, var4);
   }

   public PooledByteBufAllocator(boolean var1, int var2, int var3, int var4, int var5) {
      this(var1, var2, var3, var4, var5, DEFAULT_TINY_CACHE_SIZE, DEFAULT_SMALL_CACHE_SIZE, DEFAULT_NORMAL_CACHE_SIZE);
   }

   public PooledByteBufAllocator(boolean var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      super(var1);
      this.threadCache = new PooledByteBufAllocator.PoolThreadLocalCache();
      this.tinyCacheSize = var6;
      this.smallCacheSize = var7;
      this.normalCacheSize = var8;
      int var9 = validateAndCalculateChunkSize(var4, var5);
      if(var2 < 0) {
         throw new IllegalArgumentException("nHeapArena: " + var2 + " (expected: >= 0)");
      } else if(var3 < 0) {
         throw new IllegalArgumentException("nDirectArea: " + var3 + " (expected: >= 0)");
      } else {
         int var10 = validateAndCalculatePageShifts(var4);
         int var11;
         if(var2 > 0) {
            this.heapArenas = newArenaArray(var2);

            for(var11 = 0; var11 < this.heapArenas.length; ++var11) {
               this.heapArenas[var11] = new PoolArena.HeapArena(this, var4, var5, var10, var9);
            }
         } else {
            this.heapArenas = null;
         }

         if(var3 > 0) {
            this.directArenas = newArenaArray(var3);

            for(var11 = 0; var11 < this.directArenas.length; ++var11) {
               this.directArenas[var11] = new PoolArena.DirectArena(this, var4, var5, var10, var9);
            }
         } else {
            this.directArenas = null;
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public PooledByteBufAllocator(boolean var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9) {
      this(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   private static <T> PoolArena<T>[] newArenaArray(int var0) {
      return new PoolArena[var0];
   }

   private static int validateAndCalculatePageShifts(int var0) {
      if(var0 < 4096) {
         throw new IllegalArgumentException("pageSize: " + var0 + " (expected: " + 4096 + "+)");
      } else if((var0 & var0 - 1) != 0) {
         throw new IllegalArgumentException("pageSize: " + var0 + " (expected: power of 2)");
      } else {
         return 31 - Integer.numberOfLeadingZeros(var0);
      }
   }

   private static int validateAndCalculateChunkSize(int var0, int var1) {
      if(var1 > 14) {
         throw new IllegalArgumentException("maxOrder: " + var1 + " (expected: 0-14)");
      } else {
         int var2 = var0;

         for(int var3 = var1; var3 > 0; --var3) {
            if(var2 > 536870912) {
               throw new IllegalArgumentException(String.format("pageSize (%d) << maxOrder (%d) must not exceed %d", new Object[]{Integer.valueOf(var0), Integer.valueOf(var1), Integer.valueOf(1073741824)}));
            }

            var2 <<= 1;
         }

         return var2;
      }
   }

   protected ByteBuf newHeapBuffer(int var1, int var2) {
      PoolThreadCache var3 = (PoolThreadCache)this.threadCache.get();
      PoolArena var4 = var3.heapArena;
      Object var5;
      if(var4 != null) {
         var5 = var4.allocate(var3, var1, var2);
      } else {
         var5 = new UnpooledHeapByteBuf(this, var1, var2);
      }

      return toLeakAwareBuffer((ByteBuf)var5);
   }

   protected ByteBuf newDirectBuffer(int var1, int var2) {
      PoolThreadCache var3 = (PoolThreadCache)this.threadCache.get();
      PoolArena var4 = var3.directArena;
      Object var5;
      if(var4 != null) {
         var5 = var4.allocate(var3, var1, var2);
      } else if(PlatformDependent.hasUnsafe()) {
         var5 = new UnpooledUnsafeDirectByteBuf(this, var1, var2);
      } else {
         var5 = new UnpooledDirectByteBuf(this, var1, var2);
      }

      return toLeakAwareBuffer((ByteBuf)var5);
   }

   public boolean isDirectBufferPooled() {
      return this.directArenas != null;
   }

   /** @deprecated */
   @Deprecated
   public boolean hasThreadLocalCache() {
      return this.threadCache.isSet();
   }

   /** @deprecated */
   @Deprecated
   public void freeThreadLocalCache() {
      this.threadCache.remove();
   }

   static {
      int var0 = SystemPropertyUtil.getInt("io.netty.allocator.pageSize", 8192);
      Throwable var1 = null;

      try {
         validateAndCalculatePageShifts(var0);
      } catch (Throwable var7) {
         var1 = var7;
         var0 = 8192;
      }

      DEFAULT_PAGE_SIZE = var0;
      int var2 = SystemPropertyUtil.getInt("io.netty.allocator.maxOrder", 11);
      Throwable var3 = null;

      try {
         validateAndCalculateChunkSize(DEFAULT_PAGE_SIZE, var2);
      } catch (Throwable var6) {
         var3 = var6;
         var2 = 11;
      }

      DEFAULT_MAX_ORDER = var2;
      Runtime var4 = Runtime.getRuntime();
      int var5 = DEFAULT_PAGE_SIZE << DEFAULT_MAX_ORDER;
      DEFAULT_NUM_HEAP_ARENA = Math.max(0, SystemPropertyUtil.getInt("io.netty.allocator.numHeapArenas", (int)Math.min((long)var4.availableProcessors(), Runtime.getRuntime().maxMemory() / (long)var5 / 2L / 3L)));
      DEFAULT_NUM_DIRECT_ARENA = Math.max(0, SystemPropertyUtil.getInt("io.netty.allocator.numDirectArenas", (int)Math.min((long)var4.availableProcessors(), PlatformDependent.maxDirectMemory() / (long)var5 / 2L / 3L)));
      DEFAULT_TINY_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.tinyCacheSize", 512);
      DEFAULT_SMALL_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.smallCacheSize", 256);
      DEFAULT_NORMAL_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.normalCacheSize", 64);
      DEFAULT_MAX_CACHED_BUFFER_CAPACITY = SystemPropertyUtil.getInt("io.netty.allocator.maxCachedBufferCapacity", '\u8000');
      DEFAULT_CACHE_TRIM_INTERVAL = SystemPropertyUtil.getInt("io.netty.allocator.cacheTrimInterval", 8192);
      if(logger.isDebugEnabled()) {
         logger.debug("-Dio.netty.allocator.numHeapArenas: {}", (Object)Integer.valueOf(DEFAULT_NUM_HEAP_ARENA));
         logger.debug("-Dio.netty.allocator.numDirectArenas: {}", (Object)Integer.valueOf(DEFAULT_NUM_DIRECT_ARENA));
         if(var1 == null) {
            logger.debug("-Dio.netty.allocator.pageSize: {}", (Object)Integer.valueOf(DEFAULT_PAGE_SIZE));
         } else {
            logger.debug("-Dio.netty.allocator.pageSize: {}", Integer.valueOf(DEFAULT_PAGE_SIZE), var1);
         }

         if(var3 == null) {
            logger.debug("-Dio.netty.allocator.maxOrder: {}", (Object)Integer.valueOf(DEFAULT_MAX_ORDER));
         } else {
            logger.debug("-Dio.netty.allocator.maxOrder: {}", Integer.valueOf(DEFAULT_MAX_ORDER), var3);
         }

         logger.debug("-Dio.netty.allocator.chunkSize: {}", (Object)Integer.valueOf(DEFAULT_PAGE_SIZE << DEFAULT_MAX_ORDER));
         logger.debug("-Dio.netty.allocator.tinyCacheSize: {}", (Object)Integer.valueOf(DEFAULT_TINY_CACHE_SIZE));
         logger.debug("-Dio.netty.allocator.smallCacheSize: {}", (Object)Integer.valueOf(DEFAULT_SMALL_CACHE_SIZE));
         logger.debug("-Dio.netty.allocator.normalCacheSize: {}", (Object)Integer.valueOf(DEFAULT_NORMAL_CACHE_SIZE));
         logger.debug("-Dio.netty.allocator.maxCachedBufferCapacity: {}", (Object)Integer.valueOf(DEFAULT_MAX_CACHED_BUFFER_CAPACITY));
         logger.debug("-Dio.netty.allocator.cacheTrimInterval: {}", (Object)Integer.valueOf(DEFAULT_CACHE_TRIM_INTERVAL));
      }

      DEFAULT = new PooledByteBufAllocator(PlatformDependent.directBufferPreferred());
   }

   final class PoolThreadLocalCache extends FastThreadLocal<PoolThreadCache> {
      private final AtomicInteger index = new AtomicInteger();

      PoolThreadLocalCache() {
      }

      protected PoolThreadCache initialValue() {
         int var1 = this.index.getAndIncrement();
         PoolArena var2;
         if(PooledByteBufAllocator.this.heapArenas != null) {
            var2 = PooledByteBufAllocator.this.heapArenas[Math.abs(var1 % PooledByteBufAllocator.this.heapArenas.length)];
         } else {
            var2 = null;
         }

         PoolArena var3;
         if(PooledByteBufAllocator.this.directArenas != null) {
            var3 = PooledByteBufAllocator.this.directArenas[Math.abs(var1 % PooledByteBufAllocator.this.directArenas.length)];
         } else {
            var3 = null;
         }

         return new PoolThreadCache(var2, var3, PooledByteBufAllocator.this.tinyCacheSize, PooledByteBufAllocator.this.smallCacheSize, PooledByteBufAllocator.this.normalCacheSize, PooledByteBufAllocator.DEFAULT_MAX_CACHED_BUFFER_CAPACITY, PooledByteBufAllocator.DEFAULT_CACHE_TRIM_INTERVAL);
      }

      protected void onRemoval(PoolThreadCache var1) {
         var1.free();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected void onRemoval(Object var1) throws Exception {
         this.onRemoval((PoolThreadCache)var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object initialValue() throws Exception {
         return this.initialValue();
      }
   }
}
