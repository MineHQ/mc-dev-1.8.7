package io.netty.buffer;

import io.netty.buffer.PoolChunk;
import io.netty.buffer.PoolChunkList;
import io.netty.buffer.PoolSubpage;
import io.netty.buffer.PoolThreadCache;
import io.netty.buffer.PooledByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.PooledDirectByteBuf;
import io.netty.buffer.PooledHeapByteBuf;
import io.netty.buffer.PooledUnsafeDirectByteBuf;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.nio.ByteBuffer;

abstract class PoolArena<T> {
   static final int numTinySubpagePools = 32;
   final PooledByteBufAllocator parent;
   private final int maxOrder;
   final int pageSize;
   final int pageShifts;
   final int chunkSize;
   final int subpageOverflowMask;
   final int numSmallSubpagePools;
   private final PoolSubpage<T>[] tinySubpagePools;
   private final PoolSubpage<T>[] smallSubpagePools;
   private final PoolChunkList<T> q050;
   private final PoolChunkList<T> q025;
   private final PoolChunkList<T> q000;
   private final PoolChunkList<T> qInit;
   private final PoolChunkList<T> q075;
   private final PoolChunkList<T> q100;

   protected PoolArena(PooledByteBufAllocator var1, int var2, int var3, int var4, int var5) {
      this.parent = var1;
      this.pageSize = var2;
      this.maxOrder = var3;
      this.pageShifts = var4;
      this.chunkSize = var5;
      this.subpageOverflowMask = ~(var2 - 1);
      this.tinySubpagePools = this.newSubpagePoolArray(32);

      int var6;
      for(var6 = 0; var6 < this.tinySubpagePools.length; ++var6) {
         this.tinySubpagePools[var6] = this.newSubpagePoolHead(var2);
      }

      this.numSmallSubpagePools = var4 - 9;
      this.smallSubpagePools = this.newSubpagePoolArray(this.numSmallSubpagePools);

      for(var6 = 0; var6 < this.smallSubpagePools.length; ++var6) {
         this.smallSubpagePools[var6] = this.newSubpagePoolHead(var2);
      }

      this.q100 = new PoolChunkList(this, (PoolChunkList)null, 100, Integer.MAX_VALUE);
      this.q075 = new PoolChunkList(this, this.q100, 75, 100);
      this.q050 = new PoolChunkList(this, this.q075, 50, 100);
      this.q025 = new PoolChunkList(this, this.q050, 25, 75);
      this.q000 = new PoolChunkList(this, this.q025, 1, 50);
      this.qInit = new PoolChunkList(this, this.q000, Integer.MIN_VALUE, 25);
      this.q100.prevList = this.q075;
      this.q075.prevList = this.q050;
      this.q050.prevList = this.q025;
      this.q025.prevList = this.q000;
      this.q000.prevList = null;
      this.qInit.prevList = this.qInit;
   }

   private PoolSubpage<T> newSubpagePoolHead(int var1) {
      PoolSubpage var2 = new PoolSubpage(var1);
      var2.prev = var2;
      var2.next = var2;
      return var2;
   }

   private PoolSubpage<T>[] newSubpagePoolArray(int var1) {
      return new PoolSubpage[var1];
   }

   abstract boolean isDirect();

   PooledByteBuf<T> allocate(PoolThreadCache var1, int var2, int var3) {
      PooledByteBuf var4 = this.newByteBuf(var3);
      this.allocate(var1, var4, var2);
      return var4;
   }

   static int tinyIdx(int var0) {
      return var0 >>> 4;
   }

   static int smallIdx(int var0) {
      int var1 = 0;

      for(int var2 = var0 >>> 10; var2 != 0; ++var1) {
         var2 >>>= 1;
      }

      return var1;
   }

   boolean isTinyOrSmall(int var1) {
      return (var1 & this.subpageOverflowMask) == 0;
   }

   static boolean isTiny(int var0) {
      return (var0 & -512) == 0;
   }

   private void allocate(PoolThreadCache var1, PooledByteBuf<T> var2, int var3) {
      int var4 = this.normalizeCapacity(var3);
      if(this.isTinyOrSmall(var4)) {
         int var5;
         PoolSubpage[] var6;
         if(isTiny(var4)) {
            if(var1.allocateTiny(this, var2, var3, var4)) {
               return;
            }

            var5 = tinyIdx(var4);
            var6 = this.tinySubpagePools;
         } else {
            if(var1.allocateSmall(this, var2, var3, var4)) {
               return;
            }

            var5 = smallIdx(var4);
            var6 = this.smallSubpagePools;
         }

         synchronized(this) {
            PoolSubpage var8 = var6[var5];
            PoolSubpage var9 = var8.next;
            if(var9 != var8) {
               assert var9.doNotDestroy && var9.elemSize == var4;

               long var10 = var9.allocate();

               assert var10 >= 0L;

               var9.chunk.initBufWithSubpage(var2, var10, var3);
               return;
            }
         }
      } else {
         if(var4 > this.chunkSize) {
            this.allocateHuge(var2, var3);
            return;
         }

         if(var1.allocateNormal(this, var2, var3, var4)) {
            return;
         }
      }

      this.allocateNormal(var2, var3, var4);
   }

   private synchronized void allocateNormal(PooledByteBuf<T> var1, int var2, int var3) {
      if(!this.q050.allocate(var1, var2, var3) && !this.q025.allocate(var1, var2, var3) && !this.q000.allocate(var1, var2, var3) && !this.qInit.allocate(var1, var2, var3) && !this.q075.allocate(var1, var2, var3) && !this.q100.allocate(var1, var2, var3)) {
         PoolChunk var4 = this.newChunk(this.pageSize, this.maxOrder, this.pageShifts, this.chunkSize);
         long var5 = var4.allocate(var3);

         assert var5 > 0L;

         var4.initBuf(var1, var5, var2);
         this.qInit.add(var4);
      }
   }

   private void allocateHuge(PooledByteBuf<T> var1, int var2) {
      var1.initUnpooled(this.newUnpooledChunk(var2), var2);
   }

   void free(PoolChunk<T> var1, long var2, int var4) {
      if(var1.unpooled) {
         this.destroyChunk(var1);
      } else {
         PoolThreadCache var5 = (PoolThreadCache)this.parent.threadCache.get();
         if(var5.add(this, var1, var2, var4)) {
            return;
         }

         synchronized(this) {
            var1.parent.free(var1, var2);
         }
      }

   }

   PoolSubpage<T> findSubpagePoolHead(int var1) {
      int var2;
      PoolSubpage[] var3;
      if(isTiny(var1)) {
         var2 = var1 >>> 4;
         var3 = this.tinySubpagePools;
      } else {
         var2 = 0;

         for(var1 >>>= 10; var1 != 0; ++var2) {
            var1 >>>= 1;
         }

         var3 = this.smallSubpagePools;
      }

      return var3[var2];
   }

   int normalizeCapacity(int var1) {
      if(var1 < 0) {
         throw new IllegalArgumentException("capacity: " + var1 + " (expected: 0+)");
      } else if(var1 >= this.chunkSize) {
         return var1;
      } else if(!isTiny(var1)) {
         int var2 = var1 - 1;
         var2 |= var2 >>> 1;
         var2 |= var2 >>> 2;
         var2 |= var2 >>> 4;
         var2 |= var2 >>> 8;
         var2 |= var2 >>> 16;
         ++var2;
         if(var2 < 0) {
            var2 >>>= 1;
         }

         return var2;
      } else {
         return (var1 & 15) == 0?var1:(var1 & -16) + 16;
      }
   }

   void reallocate(PooledByteBuf<T> var1, int var2, boolean var3) {
      if(var2 >= 0 && var2 <= var1.maxCapacity()) {
         int var4 = var1.length;
         if(var4 != var2) {
            PoolChunk var5 = var1.chunk;
            long var6 = var1.handle;
            Object var8 = var1.memory;
            int var9 = var1.offset;
            int var10 = var1.maxLength;
            int var11 = var1.readerIndex();
            int var12 = var1.writerIndex();
            this.allocate((PoolThreadCache)this.parent.threadCache.get(), var1, var2);
            if(var2 > var4) {
               this.memoryCopy(var8, var9, var1.memory, var1.offset, var4);
            } else if(var2 < var4) {
               if(var11 < var2) {
                  if(var12 > var2) {
                     var12 = var2;
                  }

                  this.memoryCopy(var8, var9 + var11, var1.memory, var1.offset + var11, var12 - var11);
               } else {
                  var12 = var2;
                  var11 = var2;
               }
            }

            var1.setIndex(var11, var12);
            if(var3) {
               this.free(var5, var6, var10);
            }

         }
      } else {
         throw new IllegalArgumentException("newCapacity: " + var2);
      }
   }

   protected abstract PoolChunk<T> newChunk(int var1, int var2, int var3, int var4);

   protected abstract PoolChunk<T> newUnpooledChunk(int var1);

   protected abstract PooledByteBuf<T> newByteBuf(int var1);

   protected abstract void memoryCopy(T var1, int var2, T var3, int var4, int var5);

   protected abstract void destroyChunk(PoolChunk<T> var1);

   public synchronized String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Chunk(s) at 0~25%:");
      var1.append(StringUtil.NEWLINE);
      var1.append(this.qInit);
      var1.append(StringUtil.NEWLINE);
      var1.append("Chunk(s) at 0~50%:");
      var1.append(StringUtil.NEWLINE);
      var1.append(this.q000);
      var1.append(StringUtil.NEWLINE);
      var1.append("Chunk(s) at 25~75%:");
      var1.append(StringUtil.NEWLINE);
      var1.append(this.q025);
      var1.append(StringUtil.NEWLINE);
      var1.append("Chunk(s) at 50~100%:");
      var1.append(StringUtil.NEWLINE);
      var1.append(this.q050);
      var1.append(StringUtil.NEWLINE);
      var1.append("Chunk(s) at 75~100%:");
      var1.append(StringUtil.NEWLINE);
      var1.append(this.q075);
      var1.append(StringUtil.NEWLINE);
      var1.append("Chunk(s) at 100%:");
      var1.append(StringUtil.NEWLINE);
      var1.append(this.q100);
      var1.append(StringUtil.NEWLINE);
      var1.append("tiny subpages:");

      int var2;
      PoolSubpage var3;
      PoolSubpage var4;
      for(var2 = 1; var2 < this.tinySubpagePools.length; ++var2) {
         var3 = this.tinySubpagePools[var2];
         if(var3.next != var3) {
            var1.append(StringUtil.NEWLINE);
            var1.append(var2);
            var1.append(": ");
            var4 = var3.next;

            do {
               var1.append(var4);
               var4 = var4.next;
            } while(var4 != var3);
         }
      }

      var1.append(StringUtil.NEWLINE);
      var1.append("small subpages:");

      for(var2 = 1; var2 < this.smallSubpagePools.length; ++var2) {
         var3 = this.smallSubpagePools[var2];
         if(var3.next != var3) {
            var1.append(StringUtil.NEWLINE);
            var1.append(var2);
            var1.append(": ");
            var4 = var3.next;

            do {
               var1.append(var4);
               var4 = var4.next;
            } while(var4 != var3);
         }
      }

      var1.append(StringUtil.NEWLINE);
      return var1.toString();
   }

   static final class DirectArena extends PoolArena<ByteBuffer> {
      private static final boolean HAS_UNSAFE = PlatformDependent.hasUnsafe();

      DirectArena(PooledByteBufAllocator var1, int var2, int var3, int var4, int var5) {
         super(var1, var2, var3, var4, var5);
      }

      boolean isDirect() {
         return true;
      }

      protected PoolChunk<ByteBuffer> newChunk(int var1, int var2, int var3, int var4) {
         return new PoolChunk(this, ByteBuffer.allocateDirect(var4), var1, var2, var3, var4);
      }

      protected PoolChunk<ByteBuffer> newUnpooledChunk(int var1) {
         return new PoolChunk(this, ByteBuffer.allocateDirect(var1), var1);
      }

      protected void destroyChunk(PoolChunk<ByteBuffer> var1) {
         PlatformDependent.freeDirectBuffer((ByteBuffer)var1.memory);
      }

      protected PooledByteBuf<ByteBuffer> newByteBuf(int var1) {
         return (PooledByteBuf)(HAS_UNSAFE?PooledUnsafeDirectByteBuf.newInstance(var1):PooledDirectByteBuf.newInstance(var1));
      }

      protected void memoryCopy(ByteBuffer var1, int var2, ByteBuffer var3, int var4, int var5) {
         if(var5 != 0) {
            if(HAS_UNSAFE) {
               PlatformDependent.copyMemory(PlatformDependent.directBufferAddress(var1) + (long)var2, PlatformDependent.directBufferAddress(var3) + (long)var4, (long)var5);
            } else {
               var1 = var1.duplicate();
               var3 = var3.duplicate();
               var1.position(var2).limit(var2 + var5);
               var3.position(var4);
               var3.put(var1);
            }

         }
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected void memoryCopy(Object var1, int var2, Object var3, int var4, int var5) {
         this.memoryCopy((ByteBuffer)var1, var2, (ByteBuffer)var3, var4, var5);
      }
   }

   static final class HeapArena extends PoolArena<byte[]> {
      HeapArena(PooledByteBufAllocator var1, int var2, int var3, int var4, int var5) {
         super(var1, var2, var3, var4, var5);
      }

      boolean isDirect() {
         return false;
      }

      protected PoolChunk<byte[]> newChunk(int var1, int var2, int var3, int var4) {
         return new PoolChunk(this, new byte[var4], var1, var2, var3, var4);
      }

      protected PoolChunk<byte[]> newUnpooledChunk(int var1) {
         return new PoolChunk(this, new byte[var1], var1);
      }

      protected void destroyChunk(PoolChunk<byte[]> var1) {
      }

      protected PooledByteBuf<byte[]> newByteBuf(int var1) {
         return PooledHeapByteBuf.newInstance(var1);
      }

      protected void memoryCopy(byte[] var1, int var2, byte[] var3, int var4, int var5) {
         if(var5 != 0) {
            System.arraycopy(var1, var2, var3, var4, var5);
         }
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected void memoryCopy(Object var1, int var2, Object var3, int var4, int var5) {
         this.memoryCopy((byte[])var1, var2, (byte[])var3, var4, var5);
      }
   }
}
