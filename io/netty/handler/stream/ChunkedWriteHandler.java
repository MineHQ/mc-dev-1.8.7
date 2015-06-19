package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.handler.stream.ChunkedInput;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.Queue;

public class ChunkedWriteHandler extends ChannelDuplexHandler {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChunkedWriteHandler.class);
   private final Queue<ChunkedWriteHandler.PendingWrite> queue = new ArrayDeque();
   private volatile ChannelHandlerContext ctx;
   private ChunkedWriteHandler.PendingWrite currentWrite;

   public ChunkedWriteHandler() {
   }

   /** @deprecated */
   @Deprecated
   public ChunkedWriteHandler(int var1) {
      if(var1 <= 0) {
         throw new IllegalArgumentException("maxPendingWrites: " + var1 + " (expected: > 0)");
      }
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      this.ctx = var1;
   }

   public void resumeTransfer() {
      final ChannelHandlerContext var1 = this.ctx;
      if(var1 != null) {
         if(var1.executor().inEventLoop()) {
            try {
               this.doFlush(var1);
            } catch (Exception var3) {
               if(logger.isWarnEnabled()) {
                  logger.warn("Unexpected exception while sending chunks.", (Throwable)var3);
               }
            }
         } else {
            var1.executor().execute(new Runnable() {
               public void run() {
                  try {
                     ChunkedWriteHandler.this.doFlush(var1);
                  } catch (Exception var2) {
                     if(ChunkedWriteHandler.logger.isWarnEnabled()) {
                        ChunkedWriteHandler.logger.warn("Unexpected exception while sending chunks.", (Throwable)var2);
                     }
                  }

               }
            });
         }

      }
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      this.queue.add(new ChunkedWriteHandler.PendingWrite(var2, var3));
   }

   public void flush(ChannelHandlerContext var1) throws Exception {
      Channel var2 = var1.channel();
      if(var2.isWritable() || !var2.isActive()) {
         this.doFlush(var1);
      }

   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      this.doFlush(var1);
      super.channelInactive(var1);
   }

   public void channelWritabilityChanged(ChannelHandlerContext var1) throws Exception {
      if(var1.channel().isWritable()) {
         this.doFlush(var1);
      }

      var1.fireChannelWritabilityChanged();
   }

   private void discard(Throwable var1) {
      while(true) {
         ChunkedWriteHandler.PendingWrite var2 = this.currentWrite;
         if(this.currentWrite == null) {
            var2 = (ChunkedWriteHandler.PendingWrite)this.queue.poll();
         } else {
            this.currentWrite = null;
         }

         if(var2 == null) {
            return;
         }

         Object var3 = var2.msg;
         if(var3 instanceof ChunkedInput) {
            ChunkedInput var4 = (ChunkedInput)var3;

            try {
               if(!var4.isEndOfInput()) {
                  if(var1 == null) {
                     var1 = new ClosedChannelException();
                  }

                  var2.fail((Throwable)var1);
               } else {
                  var2.success();
               }

               closeInput(var4);
            } catch (Exception var6) {
               var2.fail(var6);
               logger.warn(ChunkedInput.class.getSimpleName() + ".isEndOfInput() failed", (Throwable)var6);
               closeInput(var4);
            }
         } else {
            if(var1 == null) {
               var1 = new ClosedChannelException();
            }

            var2.fail((Throwable)var1);
         }
      }
   }

   private void doFlush(ChannelHandlerContext var1) throws Exception {
      final Channel var2 = var1.channel();
      if(!var2.isActive()) {
         this.discard((Throwable)null);
      } else {
         while(var2.isWritable()) {
            if(this.currentWrite == null) {
               this.currentWrite = (ChunkedWriteHandler.PendingWrite)this.queue.poll();
            }

            if(this.currentWrite == null) {
               break;
            }

            final ChunkedWriteHandler.PendingWrite var3 = this.currentWrite;
            final Object var4 = var3.msg;
            if(var4 instanceof ChunkedInput) {
               final ChunkedInput var5 = (ChunkedInput)var4;
               Object var8 = null;

               boolean var6;
               boolean var7;
               try {
                  var8 = var5.readChunk(var1);
                  var6 = var5.isEndOfInput();
                  if(var8 == null) {
                     var7 = !var6;
                  } else {
                     var7 = false;
                  }
               } catch (Throwable var11) {
                  this.currentWrite = null;
                  if(var8 != null) {
                     ReferenceCountUtil.release(var8);
                  }

                  var3.fail(var11);
                  closeInput(var5);
                  break;
               }

               if(var7) {
                  break;
               }

               if(var8 == null) {
                  var8 = Unpooled.EMPTY_BUFFER;
               }

               final int var9 = amount(var8);
               ChannelFuture var10 = var1.write(var8);
               if(var6) {
                  this.currentWrite = null;
                  var10.addListener(new ChannelFutureListener() {
                     public void operationComplete(ChannelFuture var1) throws Exception {
                        var3.progress(var9);
                        var3.success();
                        ChunkedWriteHandler.closeInput(var5);
                     }

                     // $FF: synthetic method
                     // $FF: bridge method
                     public void operationComplete(Future var1) throws Exception {
                        this.operationComplete((ChannelFuture)var1);
                     }
                  });
               } else if(var2.isWritable()) {
                  var10.addListener(new ChannelFutureListener() {
                     public void operationComplete(ChannelFuture var1) throws Exception {
                        if(!var1.isSuccess()) {
                           ChunkedWriteHandler.closeInput((ChunkedInput)var4);
                           var3.fail(var1.cause());
                        } else {
                           var3.progress(var9);
                        }

                     }

                     // $FF: synthetic method
                     // $FF: bridge method
                     public void operationComplete(Future var1) throws Exception {
                        this.operationComplete((ChannelFuture)var1);
                     }
                  });
               } else {
                  var10.addListener(new ChannelFutureListener() {
                     public void operationComplete(ChannelFuture var1) throws Exception {
                        if(!var1.isSuccess()) {
                           ChunkedWriteHandler.closeInput((ChunkedInput)var4);
                           var3.fail(var1.cause());
                        } else {
                           var3.progress(var9);
                           if(var2.isWritable()) {
                              ChunkedWriteHandler.this.resumeTransfer();
                           }
                        }

                     }

                     // $FF: synthetic method
                     // $FF: bridge method
                     public void operationComplete(Future var1) throws Exception {
                        this.operationComplete((ChannelFuture)var1);
                     }
                  });
               }
            } else {
               var1.write(var4, var3.promise);
               this.currentWrite = null;
            }

            var1.flush();
            if(!var2.isActive()) {
               this.discard(new ClosedChannelException());
               return;
            }
         }

      }
   }

   static void closeInput(ChunkedInput<?> var0) {
      try {
         var0.close();
      } catch (Throwable var2) {
         if(logger.isWarnEnabled()) {
            logger.warn("Failed to close a chunked input.", var2);
         }
      }

   }

   private static int amount(Object var0) {
      return var0 instanceof ByteBuf?((ByteBuf)var0).readableBytes():(var0 instanceof ByteBufHolder?((ByteBufHolder)var0).content().readableBytes():1);
   }

   private static final class PendingWrite {
      final Object msg;
      final ChannelPromise promise;
      private long progress;

      PendingWrite(Object var1, ChannelPromise var2) {
         this.msg = var1;
         this.promise = var2;
      }

      void fail(Throwable var1) {
         ReferenceCountUtil.release(this.msg);
         this.promise.tryFailure(var1);
      }

      void success() {
         if(!this.promise.isDone()) {
            if(this.promise instanceof ChannelProgressivePromise) {
               ((ChannelProgressivePromise)this.promise).tryProgress(this.progress, this.progress);
            }

            this.promise.trySuccess();
         }
      }

      void progress(int var1) {
         this.progress += (long)var1;
         if(this.promise instanceof ChannelProgressivePromise) {
            ((ChannelProgressivePromise)this.promise).tryProgress(this.progress, -1L);
         }

      }
   }
}
