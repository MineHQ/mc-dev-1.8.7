package io.netty.buffer;

import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PoolChunk;
import io.netty.util.Recycler;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

abstract class PooledByteBuf<T> extends AbstractReferenceCountedByteBuf {
   private final Recycler.Handle recyclerHandle;
   protected PoolChunk<T> chunk;
   protected long handle;
   protected T memory;
   protected int offset;
   protected int length;
   int maxLength;
   private ByteBuffer tmpNioBuf;

   protected PooledByteBuf(Recycler.Handle var1, int var2) {
      super(var2);
      this.recyclerHandle = var1;
   }

   void init(PoolChunk<T> var1, long var2, int var4, int var5, int var6) {
      assert var2 >= 0L;

      assert var1 != null;

      this.chunk = var1;
      this.handle = var2;
      this.memory = var1.memory;
      this.offset = var4;
      this.length = var5;
      this.maxLength = var6;
      this.setIndex(0, 0);
      this.tmpNioBuf = null;
   }

   void initUnpooled(PoolChunk<T> var1, int var2) {
      assert var1 != null;

      this.chunk = var1;
      this.handle = 0L;
      this.memory = var1.memory;
      this.offset = 0;
      this.length = this.maxLength = var2;
      this.setIndex(0, 0);
      this.tmpNioBuf = null;
   }

   public final int capacity() {
      return this.length;
   }

   public final ByteBuf capacity(int var1) {
      this.ensureAccessible();
      if(this.chunk.unpooled) {
         if(var1 == this.length) {
            return this;
         }
      } else if(var1 > this.length) {
         if(var1 <= this.maxLength) {
            this.length = var1;
            return this;
         }
      } else {
         if(var1 >= this.length) {
            return this;
         }

         if(var1 > this.maxLength >>> 1) {
            if(this.maxLength > 512) {
               this.length = var1;
               this.setIndex(Math.min(this.readerIndex(), var1), Math.min(this.writerIndex(), var1));
               return this;
            }

            if(var1 > this.maxLength - 16) {
               this.length = var1;
               this.setIndex(Math.min(this.readerIndex(), var1), Math.min(this.writerIndex(), var1));
               return this;
            }
         }
      }

      this.chunk.arena.reallocate(this, var1, true);
      return this;
   }

   public final ByteBufAllocator alloc() {
      return this.chunk.arena.parent;
   }

   public final ByteOrder order() {
      return ByteOrder.BIG_ENDIAN;
   }

   public final ByteBuf unwrap() {
      return null;
   }

   protected final ByteBuffer internalNioBuffer() {
      ByteBuffer var1 = this.tmpNioBuf;
      if(var1 == null) {
         this.tmpNioBuf = var1 = this.newInternalNioBuffer(this.memory);
      }

      return var1;
   }

   protected abstract ByteBuffer newInternalNioBuffer(T var1);

   protected final void deallocate() {
      if(this.handle >= 0L) {
         long var1 = this.handle;
         this.handle = -1L;
         this.memory = null;
         this.chunk.arena.free(this.chunk, var1, this.maxLength);
         this.recycle();
      }

   }

   private void recycle() {
      Recycler.Handle var1 = this.recyclerHandle;
      if(var1 != null) {
         this.recycler().recycle(this, var1);
      }

   }

   protected abstract Recycler<?> recycler();

   protected final int idx(int var1) {
      return this.offset + var1;
   }
}
