package io.netty.channel.nio;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.AbstractNioChannel;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNioMessageChannel extends AbstractNioChannel {
   protected AbstractNioMessageChannel(Channel var1, SelectableChannel var2, int var3) {
      super(var1, var2, var3);
   }

   protected AbstractNioChannel.AbstractNioUnsafe newUnsafe() {
      return new AbstractNioMessageChannel.NioMessageUnsafe();
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      SelectionKey var2 = this.selectionKey();
      int var3 = var2.interestOps();

      while(true) {
         Object var4 = var1.current();
         if(var4 == null) {
            if((var3 & 4) != 0) {
               var2.interestOps(var3 & -5);
            }
            break;
         }

         try {
            boolean var5 = false;

            for(int var6 = this.config().getWriteSpinCount() - 1; var6 >= 0; --var6) {
               if(this.doWriteMessage(var4, var1)) {
                  var5 = true;
                  break;
               }
            }

            if(!var5) {
               if((var3 & 4) == 0) {
                  var2.interestOps(var3 | 4);
               }
               break;
            }

            var1.remove();
         } catch (IOException var7) {
            if(!this.continueOnWriteError()) {
               throw var7;
            }

            var1.remove(var7);
         }
      }

   }

   protected boolean continueOnWriteError() {
      return false;
   }

   protected abstract int doReadMessages(List<Object> var1) throws Exception;

   protected abstract boolean doWriteMessage(Object var1, ChannelOutboundBuffer var2) throws Exception;

   // $FF: synthetic method
   // $FF: bridge method
   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return this.newUnsafe();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private final class NioMessageUnsafe extends AbstractNioChannel.AbstractNioUnsafe {
      private final List<Object> readBuf;

      private NioMessageUnsafe() {
         super();
         this.readBuf = new ArrayList();
      }

      public void read() {
         assert AbstractNioMessageChannel.this.eventLoop().inEventLoop();

         ChannelConfig var1 = AbstractNioMessageChannel.this.config();
         if(!var1.isAutoRead() && !AbstractNioMessageChannel.this.isReadPending()) {
            this.removeReadOp();
         } else {
            int var2 = var1.getMaxMessagesPerRead();
            ChannelPipeline var3 = AbstractNioMessageChannel.this.pipeline();
            boolean var4 = false;
            Throwable var5 = null;

            try {
               int var6;
               try {
                  do {
                     var6 = AbstractNioMessageChannel.this.doReadMessages(this.readBuf);
                     if(var6 == 0) {
                        break;
                     }

                     if(var6 < 0) {
                        var4 = true;
                        break;
                     }
                  } while(var1.isAutoRead() && this.readBuf.size() < var2);
               } catch (Throwable var11) {
                  var5 = var11;
               }

               AbstractNioMessageChannel.this.setReadPending(false);
               var6 = this.readBuf.size();

               for(int var7 = 0; var7 < var6; ++var7) {
                  var3.fireChannelRead(this.readBuf.get(var7));
               }

               this.readBuf.clear();
               var3.fireChannelReadComplete();
               if(var5 != null) {
                  if(var5 instanceof IOException) {
                     var4 = !(AbstractNioMessageChannel.this instanceof ServerChannel);
                  }

                  var3.fireExceptionCaught(var5);
               }

               if(var4 && AbstractNioMessageChannel.this.isOpen()) {
                  this.close(this.voidPromise());
               }
            } finally {
               if(!var1.isAutoRead() && !AbstractNioMessageChannel.this.isReadPending()) {
                  this.removeReadOp();
               }

            }

         }
      }

      // $FF: synthetic method
      NioMessageUnsafe(AbstractNioMessageChannel.SyntheticClass_1 var2) {
         this();
      }
   }
}
