package io.netty.buffer;

import io.netty.buffer.PoolChunk;

final class PoolSubpage<T> {
   final PoolChunk<T> chunk;
   private final int memoryMapIdx;
   private final int runOffset;
   private final int pageSize;
   private final long[] bitmap;
   PoolSubpage<T> prev;
   PoolSubpage<T> next;
   boolean doNotDestroy;
   int elemSize;
   private int maxNumElems;
   private int bitmapLength;
   private int nextAvail;
   private int numAvail;

   PoolSubpage(int var1) {
      this.chunk = null;
      this.memoryMapIdx = -1;
      this.runOffset = -1;
      this.elemSize = -1;
      this.pageSize = var1;
      this.bitmap = null;
   }

   PoolSubpage(PoolChunk<T> var1, int var2, int var3, int var4, int var5) {
      this.chunk = var1;
      this.memoryMapIdx = var2;
      this.runOffset = var3;
      this.pageSize = var4;
      this.bitmap = new long[var4 >>> 10];
      this.init(var5);
   }

   void init(int var1) {
      this.doNotDestroy = true;
      this.elemSize = var1;
      if(var1 != 0) {
         this.maxNumElems = this.numAvail = this.pageSize / var1;
         this.nextAvail = 0;
         this.bitmapLength = this.maxNumElems >>> 6;
         if((this.maxNumElems & 63) != 0) {
            ++this.bitmapLength;
         }

         for(int var2 = 0; var2 < this.bitmapLength; ++var2) {
            this.bitmap[var2] = 0L;
         }
      }

      this.addToPool();
   }

   long allocate() {
      if(this.elemSize == 0) {
         return this.toHandle(0);
      } else if(this.numAvail != 0 && this.doNotDestroy) {
         int var1 = this.getNextAvail();
         int var2 = var1 >>> 6;
         int var3 = var1 & 63;

         assert (this.bitmap[var2] >>> var3 & 1L) == 0L;

         this.bitmap[var2] |= 1L << var3;
         if(--this.numAvail == 0) {
            this.removeFromPool();
         }

         return this.toHandle(var1);
      } else {
         return -1L;
      }
   }

   boolean free(int var1) {
      if(this.elemSize == 0) {
         return true;
      } else {
         int var2 = var1 >>> 6;
         int var3 = var1 & 63;

         assert (this.bitmap[var2] >>> var3 & 1L) != 0L;

         this.bitmap[var2] ^= 1L << var3;
         this.setNextAvail(var1);
         if(this.numAvail++ == 0) {
            this.addToPool();
            return true;
         } else if(this.numAvail != this.maxNumElems) {
            return true;
         } else if(this.prev == this.next) {
            return true;
         } else {
            this.doNotDestroy = false;
            this.removeFromPool();
            return false;
         }
      }
   }

   private void addToPool() {
      PoolSubpage var1 = this.chunk.arena.findSubpagePoolHead(this.elemSize);
      if($assertionsDisabled || this.prev == null && this.next == null) {
         this.prev = var1;
         this.next = var1.next;
         this.next.prev = this;
         var1.next = this;
      } else {
         throw new AssertionError();
      }
   }

   private void removeFromPool() {
      if($assertionsDisabled || this.prev != null && this.next != null) {
         this.prev.next = this.next;
         this.next.prev = this.prev;
         this.next = null;
         this.prev = null;
      } else {
         throw new AssertionError();
      }
   }

   private void setNextAvail(int var1) {
      this.nextAvail = var1;
   }

   private int getNextAvail() {
      int var1 = this.nextAvail;
      if(var1 >= 0) {
         this.nextAvail = -1;
         return var1;
      } else {
         return this.findNextAvail();
      }
   }

   private int findNextAvail() {
      long[] var1 = this.bitmap;
      int var2 = this.bitmapLength;

      for(int var3 = 0; var3 < var2; ++var3) {
         long var4 = var1[var3];
         if(~var4 != 0L) {
            return this.findNextAvail0(var3, var4);
         }
      }

      return -1;
   }

   private int findNextAvail0(int var1, long var2) {
      int var4 = this.maxNumElems;
      int var5 = var1 << 6;

      for(int var6 = 0; var6 < 64; ++var6) {
         if((var2 & 1L) == 0L) {
            int var7 = var5 | var6;
            if(var7 < var4) {
               return var7;
            }
            break;
         }

         var2 >>>= 1;
      }

      return -1;
   }

   private long toHandle(int var1) {
      return 4611686018427387904L | (long)var1 << 32 | (long)this.memoryMapIdx;
   }

   public String toString() {
      return !this.doNotDestroy?"(" + this.memoryMapIdx + ": not in use)":String.valueOf('(') + this.memoryMapIdx + ": " + (this.maxNumElems - this.numAvail) + '/' + this.maxNumElems + ", offset: " + this.runOffset + ", length: " + this.pageSize + ", elemSize: " + this.elemSize + ')';
   }
}
