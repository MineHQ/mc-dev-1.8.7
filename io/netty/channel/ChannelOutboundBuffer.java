package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.channel.FileRegion;
import io.netty.channel.VoidChannelPromise;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public final class ChannelOutboundBuffer {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelOutboundBuffer.class);
   private static final FastThreadLocal<ByteBuffer[]> NIO_BUFFERS = new FastThreadLocal() {
      protected ByteBuffer[] initialValue() throws Exception {
         return new ByteBuffer[1024];
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object initialValue() throws Exception {
         return this.initialValue();
      }
   };
   private final Channel channel;
   private ChannelOutboundBuffer.Entry flushedEntry;
   private ChannelOutboundBuffer.Entry unflushedEntry;
   private ChannelOutboundBuffer.Entry tailEntry;
   private int flushed;
   private int nioBufferCount;
   private long nioBufferSize;
   private boolean inFail;
   private static final AtomicLongFieldUpdater<ChannelOutboundBuffer> TOTAL_PENDING_SIZE_UPDATER;
   private volatile long totalPendingSize;
   private static final AtomicIntegerFieldUpdater<ChannelOutboundBuffer> WRITABLE_UPDATER;
   private volatile int writable = 1;

   ChannelOutboundBuffer(AbstractChannel var1) {
      this.channel = var1;
   }

   public void addMessage(Object var1, int var2, ChannelPromise var3) {
      ChannelOutboundBuffer.Entry var4 = ChannelOutboundBuffer.Entry.newInstance(var1, var2, total(var1), var3);
      if(this.tailEntry == null) {
         this.flushedEntry = null;
         this.tailEntry = var4;
      } else {
         ChannelOutboundBuffer.Entry var5 = this.tailEntry;
         var5.next = var4;
         this.tailEntry = var4;
      }

      if(this.unflushedEntry == null) {
         this.unflushedEntry = var4;
      }

      this.incrementPendingOutboundBytes((long)var2);
   }

   public void addFlush() {
      ChannelOutboundBuffer.Entry var1 = this.unflushedEntry;
      if(var1 != null) {
         if(this.flushedEntry == null) {
            this.flushedEntry = var1;
         }

         do {
            ++this.flushed;
            if(!var1.promise.setUncancellable()) {
               int var2 = var1.cancel();
               this.decrementPendingOutboundBytes((long)var2);
            }

            var1 = var1.next;
         } while(var1 != null);

         this.unflushedEntry = null;
      }

   }

   void incrementPendingOutboundBytes(long var1) {
      if(var1 != 0L) {
         long var3 = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, var1);
         if(var3 > (long)this.channel.config().getWriteBufferHighWaterMark() && WRITABLE_UPDATER.compareAndSet(this, 1, 0)) {
            this.channel.pipeline().fireChannelWritabilityChanged();
         }

      }
   }

   void decrementPendingOutboundBytes(long var1) {
      if(var1 != 0L) {
         long var3 = TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, -var1);
         if((var3 == 0L || var3 < (long)this.channel.config().getWriteBufferLowWaterMark()) && WRITABLE_UPDATER.compareAndSet(this, 0, 1)) {
            this.channel.pipeline().fireChannelWritabilityChanged();
         }

      }
   }

   private static long total(Object var0) {
      return var0 instanceof ByteBuf?(long)((ByteBuf)var0).readableBytes():(var0 instanceof FileRegion?((FileRegion)var0).count():(var0 instanceof ByteBufHolder?(long)((ByteBufHolder)var0).content().readableBytes():-1L));
   }

   public Object current() {
      ChannelOutboundBuffer.Entry var1 = this.flushedEntry;
      return var1 == null?null:var1.msg;
   }

   public void progress(long var1) {
      ChannelOutboundBuffer.Entry var3 = this.flushedEntry;

      assert var3 != null;

      ChannelPromise var4 = var3.promise;
      if(var4 instanceof ChannelProgressivePromise) {
         long var5 = var3.progress + var1;
         var3.progress = var5;
         ((ChannelProgressivePromise)var4).tryProgress(var5, var3.total);
      }

   }

   public boolean remove() {
      ChannelOutboundBuffer.Entry var1 = this.flushedEntry;
      if(var1 == null) {
         return false;
      } else {
         Object var2 = var1.msg;
         ChannelPromise var3 = var1.promise;
         int var4 = var1.pendingSize;
         this.removeEntry(var1);
         if(!var1.cancelled) {
            ReferenceCountUtil.safeRelease(var2);
            safeSuccess(var3);
            this.decrementPendingOutboundBytes((long)var4);
         }

         var1.recycle();
         return true;
      }
   }

   public boolean remove(Throwable var1) {
      ChannelOutboundBuffer.Entry var2 = this.flushedEntry;
      if(var2 == null) {
         return false;
      } else {
         Object var3 = var2.msg;
         ChannelPromise var4 = var2.promise;
         int var5 = var2.pendingSize;
         this.removeEntry(var2);
         if(!var2.cancelled) {
            ReferenceCountUtil.safeRelease(var3);
            safeFail(var4, var1);
            this.decrementPendingOutboundBytes((long)var5);
         }

         var2.recycle();
         return true;
      }
   }

   private void removeEntry(ChannelOutboundBuffer.Entry var1) {
      if(--this.flushed == 0) {
         this.flushedEntry = null;
         if(var1 == this.tailEntry) {
            this.tailEntry = null;
            this.unflushedEntry = null;
         }
      } else {
         this.flushedEntry = var1.next;
      }

   }

   public void removeBytes(long var1) {
      while(true) {
         Object var3 = this.current();
         if(!(var3 instanceof ByteBuf)) {
            assert var1 == 0L;
         } else {
            ByteBuf var4 = (ByteBuf)var3;
            int var5 = var4.readerIndex();
            int var6 = var4.writerIndex() - var5;
            if((long)var6 <= var1) {
               if(var1 != 0L) {
                  this.progress((long)var6);
                  var1 -= (long)var6;
               }

               this.remove();
               continue;
            }

            if(var1 != 0L) {
               var4.readerIndex(var5 + (int)var1);
               this.progress(var1);
            }
         }

         return;
      }
   }

   public ByteBuffer[] nioBuffers() {
      long var1 = 0L;
      int var3 = 0;
      InternalThreadLocalMap var4 = InternalThreadLocalMap.get();
      ByteBuffer[] var5 = (ByteBuffer[])NIO_BUFFERS.get(var4);

      for(ChannelOutboundBuffer.Entry var6 = this.flushedEntry; this.isFlushedEntry(var6) && var6.msg instanceof ByteBuf; var6 = var6.next) {
         if(!var6.cancelled) {
            ByteBuf var7 = (ByteBuf)var6.msg;
            int var8 = var7.readerIndex();
            int var9 = var7.writerIndex() - var8;
            if(var9 > 0) {
               var1 += (long)var9;
               int var10 = var6.count;
               if(var10 == -1) {
                  var6.count = var10 = var7.nioBufferCount();
               }

               int var11 = var3 + var10;
               if(var11 > var5.length) {
                  var5 = expandNioBufferArray(var5, var11, var3);
                  NIO_BUFFERS.set(var4, var5);
               }

               if(var10 == 1) {
                  ByteBuffer var12 = var6.buf;
                  if(var12 == null) {
                     var6.buf = var12 = var7.internalNioBuffer(var8, var9);
                  }

                  var5[var3++] = var12;
               } else {
                  ByteBuffer[] var13 = var6.bufs;
                  if(var13 == null) {
                     var6.bufs = var13 = var7.nioBuffers();
                  }

                  var3 = fillBufferArray(var13, var5, var3);
               }
            }
         }
      }

      this.nioBufferCount = var3;
      this.nioBufferSize = var1;
      return var5;
   }

   private static int fillBufferArray(ByteBuffer[] var0, ByteBuffer[] var1, int var2) {
      ByteBuffer[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ByteBuffer var6 = var3[var5];
         if(var6 == null) {
            break;
         }

         var1[var2++] = var6;
      }

      return var2;
   }

   private static ByteBuffer[] expandNioBufferArray(ByteBuffer[] var0, int var1, int var2) {
      int var3 = var0.length;

      do {
         var3 <<= 1;
         if(var3 < 0) {
            throw new IllegalStateException();
         }
      } while(var1 > var3);

      ByteBuffer[] var4 = new ByteBuffer[var3];
      System.arraycopy(var0, 0, var4, 0, var2);
      return var4;
   }

   public int nioBufferCount() {
      return this.nioBufferCount;
   }

   public long nioBufferSize() {
      return this.nioBufferSize;
   }

   boolean isWritable() {
      return this.writable != 0;
   }

   public int size() {
      return this.flushed;
   }

   public boolean isEmpty() {
      return this.flushed == 0;
   }

   void failFlushed(Throwable var1) {
      if(!this.inFail) {
         try {
            this.inFail = true;

            while(this.remove(var1)) {
               ;
            }
         } finally {
            this.inFail = false;
         }

      }
   }

   void close(final ClosedChannelException var1) {
      if(this.inFail) {
         this.channel.eventLoop().execute(new Runnable() {
            public void run() {
               ChannelOutboundBuffer.this.close(var1);
            }
         });
      } else {
         this.inFail = true;
         if(this.channel.isOpen()) {
            throw new IllegalStateException("close() must be invoked after the channel is closed.");
         } else if(!this.isEmpty()) {
            throw new IllegalStateException("close() must be invoked after all flushed writes are handled.");
         } else {
            try {
               for(ChannelOutboundBuffer.Entry var2 = this.unflushedEntry; var2 != null; var2 = var2.recycleAndGetNext()) {
                  int var3 = var2.pendingSize;
                  TOTAL_PENDING_SIZE_UPDATER.addAndGet(this, (long)(-var3));
                  if(!var2.cancelled) {
                     ReferenceCountUtil.safeRelease(var2.msg);
                     safeFail(var2.promise, var1);
                  }
               }
            } finally {
               this.inFail = false;
            }

         }
      }
   }

   private static void safeSuccess(ChannelPromise var0) {
      if(!(var0 instanceof VoidChannelPromise) && !var0.trySuccess()) {
         logger.warn("Failed to mark a promise as success because it is done already: {}", (Object)var0);
      }

   }

   private static void safeFail(ChannelPromise var0, Throwable var1) {
      if(!(var0 instanceof VoidChannelPromise) && !var0.tryFailure(var1)) {
         logger.warn("Failed to mark a promise as failure because it\'s done already: {}", var0, var1);
      }

   }

   /** @deprecated */
   @Deprecated
   public void recycle() {
   }

   public long totalPendingWriteBytes() {
      return this.totalPendingSize;
   }

   public void forEachFlushedMessage(ChannelOutboundBuffer.MessageProcessor var1) throws Exception {
      if(var1 == null) {
         throw new NullPointerException("processor");
      } else {
         ChannelOutboundBuffer.Entry var2 = this.flushedEntry;
         if(var2 != null) {
            do {
               if(!var2.cancelled && !var1.processMessage(var2.msg)) {
                  return;
               }

               var2 = var2.next;
            } while(this.isFlushedEntry(var2));

         }
      }
   }

   private boolean isFlushedEntry(ChannelOutboundBuffer.Entry var1) {
      return var1 != null && var1 != this.unflushedEntry;
   }

   static {
      AtomicIntegerFieldUpdater var0 = PlatformDependent.newAtomicIntegerFieldUpdater(ChannelOutboundBuffer.class, "writable");
      if(var0 == null) {
         var0 = AtomicIntegerFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "writable");
      }

      WRITABLE_UPDATER = var0;
      AtomicLongFieldUpdater var1 = PlatformDependent.newAtomicLongFieldUpdater(ChannelOutboundBuffer.class, "totalPendingSize");
      if(var1 == null) {
         var1 = AtomicLongFieldUpdater.newUpdater(ChannelOutboundBuffer.class, "totalPendingSize");
      }

      TOTAL_PENDING_SIZE_UPDATER = var1;
   }

   static final class Entry {
      private static final Recycler<ChannelOutboundBuffer.Entry> RECYCLER = new Recycler() {
         protected ChannelOutboundBuffer.Entry newObject(Recycler.Handle var1) {
            return new ChannelOutboundBuffer.Entry(var1, null);
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object newObject(Recycler.Handle var1) {
            return this.newObject(var1);
         }
      };
      private final Recycler.Handle handle;
      ChannelOutboundBuffer.Entry next;
      Object msg;
      ByteBuffer[] bufs;
      ByteBuffer buf;
      ChannelPromise promise;
      long progress;
      long total;
      int pendingSize;
      int count;
      boolean cancelled;

      private Entry(Recycler.Handle var1) {
         this.count = -1;
         this.handle = var1;
      }

      static ChannelOutboundBuffer.Entry newInstance(Object var0, int var1, long var2, ChannelPromise var4) {
         ChannelOutboundBuffer.Entry var5 = (ChannelOutboundBuffer.Entry)RECYCLER.get();
         var5.msg = var0;
         var5.pendingSize = var1;
         var5.total = var2;
         var5.promise = var4;
         return var5;
      }

      int cancel() {
         if(!this.cancelled) {
            this.cancelled = true;
            int var1 = this.pendingSize;
            ReferenceCountUtil.safeRelease(this.msg);
            this.msg = Unpooled.EMPTY_BUFFER;
            this.pendingSize = 0;
            this.total = 0L;
            this.progress = 0L;
            this.bufs = null;
            this.buf = null;
            return var1;
         } else {
            return 0;
         }
      }

      void recycle() {
         this.next = null;
         this.bufs = null;
         this.buf = null;
         this.msg = null;
         this.promise = null;
         this.progress = 0L;
         this.total = 0L;
         this.pendingSize = 0;
         this.count = -1;
         this.cancelled = false;
         RECYCLER.recycle(this, this.handle);
      }

      ChannelOutboundBuffer.Entry recycleAndGetNext() {
         ChannelOutboundBuffer.Entry var1 = this.next;
         this.recycle();
         return var1;
      }

      // $FF: synthetic method
      Entry(Recycler.Handle var1, Object var2) {
         this(var1);
      }
   }

   public interface MessageProcessor {
      boolean processMessage(Object var1) throws Exception;
   }
}
