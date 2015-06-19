package io.netty.channel;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelPromiseAggregator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.VoidChannelPromise;
import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public final class PendingWriteQueue {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PendingWriteQueue.class);
   private final ChannelHandlerContext ctx;
   private final ChannelOutboundBuffer buffer;
   private final MessageSizeEstimator.Handle estimatorHandle;
   private PendingWriteQueue.PendingWrite head;
   private PendingWriteQueue.PendingWrite tail;
   private int size;

   public PendingWriteQueue(ChannelHandlerContext var1) {
      if(var1 == null) {
         throw new NullPointerException("ctx");
      } else {
         this.ctx = var1;
         this.buffer = var1.channel().unsafe().outboundBuffer();
         this.estimatorHandle = var1.channel().config().getMessageSizeEstimator().newHandle();
      }
   }

   public boolean isEmpty() {
      assert this.ctx.executor().inEventLoop();

      return this.head == null;
   }

   public int size() {
      assert this.ctx.executor().inEventLoop();

      return this.size;
   }

   public void add(Object var1, ChannelPromise var2) {
      assert this.ctx.executor().inEventLoop();

      if(var1 == null) {
         throw new NullPointerException("msg");
      } else if(var2 == null) {
         throw new NullPointerException("promise");
      } else {
         int var3 = this.estimatorHandle.size(var1);
         if(var3 < 0) {
            var3 = 0;
         }

         PendingWriteQueue.PendingWrite var4 = PendingWriteQueue.PendingWrite.newInstance(var1, var3, var2);
         PendingWriteQueue.PendingWrite var5 = this.tail;
         if(var5 == null) {
            this.tail = this.head = var4;
         } else {
            var5.next = var4;
            this.tail = var4;
         }

         ++this.size;
         this.buffer.incrementPendingOutboundBytes(var4.size);
      }
   }

   public void removeAndFailAll(Throwable var1) {
      assert this.ctx.executor().inEventLoop();

      if(var1 == null) {
         throw new NullPointerException("cause");
      } else {
         PendingWriteQueue.PendingWrite var3;
         for(PendingWriteQueue.PendingWrite var2 = this.head; var2 != null; var2 = var3) {
            var3 = var2.next;
            ReferenceCountUtil.safeRelease(var2.msg);
            ChannelPromise var4 = var2.promise;
            this.recycle(var2);
            safeFail(var4, var1);
         }

         this.assertEmpty();
      }
   }

   public void removeAndFail(Throwable var1) {
      assert this.ctx.executor().inEventLoop();

      if(var1 == null) {
         throw new NullPointerException("cause");
      } else {
         PendingWriteQueue.PendingWrite var2 = this.head;
         if(var2 != null) {
            ReferenceCountUtil.safeRelease(var2.msg);
            ChannelPromise var3 = var2.promise;
            safeFail(var3, var1);
            this.recycle(var2);
         }
      }
   }

   public ChannelFuture removeAndWriteAll() {
      assert this.ctx.executor().inEventLoop();

      PendingWriteQueue.PendingWrite var1 = this.head;
      if(var1 == null) {
         return null;
      } else if(this.size == 1) {
         return this.removeAndWrite();
      } else {
         ChannelPromise var2 = this.ctx.newPromise();

         PendingWriteQueue.PendingWrite var4;
         for(ChannelPromiseAggregator var3 = new ChannelPromiseAggregator(var2); var1 != null; var1 = var4) {
            var4 = var1.next;
            Object var5 = var1.msg;
            ChannelPromise var6 = var1.promise;
            this.recycle(var1);
            this.ctx.write(var5, var6);
            var3.add(new ChannelPromise[]{var6});
         }

         this.assertEmpty();
         return var2;
      }
   }

   private void assertEmpty() {
      assert this.tail == null && this.head == null && this.size == 0;
   }

   public ChannelFuture removeAndWrite() {
      assert this.ctx.executor().inEventLoop();

      PendingWriteQueue.PendingWrite var1 = this.head;
      if(var1 == null) {
         return null;
      } else {
         Object var2 = var1.msg;
         ChannelPromise var3 = var1.promise;
         this.recycle(var1);
         return this.ctx.write(var2, var3);
      }
   }

   public ChannelPromise remove() {
      assert this.ctx.executor().inEventLoop();

      PendingWriteQueue.PendingWrite var1 = this.head;
      if(var1 == null) {
         return null;
      } else {
         ChannelPromise var2 = var1.promise;
         ReferenceCountUtil.safeRelease(var1.msg);
         this.recycle(var1);
         return var2;
      }
   }

   public Object current() {
      assert this.ctx.executor().inEventLoop();

      PendingWriteQueue.PendingWrite var1 = this.head;
      return var1 == null?null:var1.msg;
   }

   private void recycle(PendingWriteQueue.PendingWrite var1) {
      PendingWriteQueue.PendingWrite var2 = var1.next;
      this.buffer.decrementPendingOutboundBytes(var1.size);
      var1.recycle();
      --this.size;
      if(var2 == null) {
         this.head = this.tail = null;

         assert this.size == 0;
      } else {
         this.head = var2;

         assert this.size > 0;
      }

   }

   private static void safeFail(ChannelPromise var0, Throwable var1) {
      if(!(var0 instanceof VoidChannelPromise) && !var0.tryFailure(var1)) {
         logger.warn("Failed to mark a promise as failure because it\'s done already: {}", var0, var1);
      }

   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   static final class PendingWrite {
      private static final Recycler<PendingWriteQueue.PendingWrite> RECYCLER = new Recycler() {
         protected PendingWriteQueue.PendingWrite newObject(Recycler.Handle var1) {
            return new PendingWriteQueue.PendingWrite(var1);
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object newObject(Recycler.Handle var1) {
            return this.newObject(var1);
         }
      };
      private final Recycler.Handle handle;
      private PendingWriteQueue.PendingWrite next;
      private long size;
      private ChannelPromise promise;
      private Object msg;

      private PendingWrite(Recycler.Handle var1) {
         this.handle = var1;
      }

      static PendingWriteQueue.PendingWrite newInstance(Object var0, int var1, ChannelPromise var2) {
         PendingWriteQueue.PendingWrite var3 = (PendingWriteQueue.PendingWrite)RECYCLER.get();
         var3.size = (long)var1;
         var3.msg = var0;
         var3.promise = var2;
         return var3;
      }

      private void recycle() {
         this.size = 0L;
         this.next = null;
         this.msg = null;
         this.promise = null;
         RECYCLER.recycle(this, this.handle);
      }

      // $FF: synthetic method
      PendingWrite(Recycler.Handle var1, PendingWriteQueue.SyntheticClass_1 var2) {
         this(var1);
      }
   }
}
