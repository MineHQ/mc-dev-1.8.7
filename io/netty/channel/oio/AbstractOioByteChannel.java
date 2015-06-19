package io.netty.channel.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FileRegion;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.oio.AbstractOioChannel;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;

public abstract class AbstractOioByteChannel extends AbstractOioChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(FileRegion.class) + ')';
   private RecvByteBufAllocator.Handle allocHandle;
   private volatile boolean inputShutdown;

   protected AbstractOioByteChannel(Channel var1) {
      super(var1);
   }

   protected boolean isInputShutdown() {
      return this.inputShutdown;
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   protected boolean checkInputShutdown() {
      if(this.inputShutdown) {
         try {
            Thread.sleep(1000L);
         } catch (InterruptedException var2) {
            ;
         }

         return true;
      } else {
         return false;
      }
   }

   protected void doRead() {
      if(!this.checkInputShutdown()) {
         ChannelConfig var1 = this.config();
         ChannelPipeline var2 = this.pipeline();
         RecvByteBufAllocator.Handle var3 = this.allocHandle;
         if(var3 == null) {
            this.allocHandle = var3 = var1.getRecvByteBufAllocator().newHandle();
         }

         ByteBuf var4 = var3.allocate(this.alloc());
         boolean var5 = false;
         boolean var6 = false;
         Throwable var7 = null;
         int var8 = 0;

         try {
            int var9 = 0;

            do {
               var8 = this.doReadBytes(var4);
               if(var8 > 0) {
                  var6 = true;
               } else if(var8 < 0) {
                  var5 = true;
               }

               int var10 = this.available();
               if(var10 <= 0) {
                  break;
               }

               if(!var4.isWritable()) {
                  int var11 = var4.capacity();
                  int var12 = var4.maxCapacity();
                  if(var11 == var12) {
                     if(var6) {
                        var6 = false;
                        var2.fireChannelRead(var4);
                        var4 = this.alloc().buffer();
                     }
                  } else {
                     int var13 = var4.writerIndex();
                     if(var13 + var10 > var12) {
                        var4.capacity(var12);
                     } else {
                        var4.ensureWritable(var10);
                     }
                  }
               }

               if(var9 >= Integer.MAX_VALUE - var8) {
                  var9 = Integer.MAX_VALUE;
                  break;
               }

               var9 += var8;
            } while(var1.isAutoRead());

            var3.record(var9);
         } catch (Throwable var17) {
            var7 = var17;
         } finally {
            if(var6) {
               var2.fireChannelRead(var4);
            } else {
               var4.release();
            }

            var2.fireChannelReadComplete();
            if(var7 != null) {
               if(var7 instanceof IOException) {
                  var5 = true;
                  this.pipeline().fireExceptionCaught(var7);
               } else {
                  var2.fireExceptionCaught(var7);
                  this.unsafe().close(this.voidPromise());
               }
            }

            if(var5) {
               this.inputShutdown = true;
               if(this.isOpen()) {
                  if(Boolean.TRUE.equals(this.config().getOption(ChannelOption.ALLOW_HALF_CLOSURE))) {
                     var2.fireUserEventTriggered(ChannelInputShutdownEvent.INSTANCE);
                  } else {
                     this.unsafe().close(this.unsafe().voidPromise());
                  }
               }
            }

            if(var8 == 0 && this.isActive()) {
               this.read();
            }

         }

      }
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      while(true) {
         Object var2 = var1.current();
         if(var2 == null) {
            return;
         }

         if(!(var2 instanceof ByteBuf)) {
            if(var2 instanceof FileRegion) {
               FileRegion var6 = (FileRegion)var2;
               long var7 = var6.transfered();
               this.doWriteFileRegion(var6);
               var1.progress(var6.transfered() - var7);
               var1.remove();
            } else {
               var1.remove(new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(var2)));
            }
         } else {
            ByteBuf var3 = (ByteBuf)var2;

            int var5;
            for(int var4 = var3.readableBytes(); var4 > 0; var4 = var5) {
               this.doWriteBytes(var3);
               var5 = var3.readableBytes();
               var1.progress((long)(var4 - var5));
            }

            var1.remove();
         }
      }
   }

   protected final Object filterOutboundMessage(Object var1) throws Exception {
      if(!(var1 instanceof ByteBuf) && !(var1 instanceof FileRegion)) {
         throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(var1) + EXPECTED_TYPES);
      } else {
         return var1;
      }
   }

   protected abstract int available();

   protected abstract int doReadBytes(ByteBuf var1) throws Exception;

   protected abstract void doWriteBytes(ByteBuf var1) throws Exception;

   protected abstract void doWriteFileRegion(FileRegion var1) throws Exception;
}
