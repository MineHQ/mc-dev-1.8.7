package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import java.util.List;

public abstract class ByteToMessageDecoder extends ChannelInboundHandlerAdapter {
   ByteBuf cumulation;
   private boolean singleDecode;
   private boolean decodeWasNull;
   private boolean first;

   protected ByteToMessageDecoder() {
      if(this.isSharable()) {
         throw new IllegalStateException("@Sharable annotation is not allowed");
      }
   }

   public void setSingleDecode(boolean var1) {
      this.singleDecode = var1;
   }

   public boolean isSingleDecode() {
      return this.singleDecode;
   }

   protected int actualReadableBytes() {
      return this.internalBuffer().readableBytes();
   }

   protected ByteBuf internalBuffer() {
      return this.cumulation != null?this.cumulation:Unpooled.EMPTY_BUFFER;
   }

   public final void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      ByteBuf var2 = this.internalBuffer();
      int var3 = var2.readableBytes();
      if(var2.isReadable()) {
         ByteBuf var4 = var2.readBytes(var3);
         var2.release();
         var1.fireChannelRead(var4);
      } else {
         var2.release();
      }

      this.cumulation = null;
      var1.fireChannelReadComplete();
      this.handlerRemoved0(var1);
   }

   protected void handlerRemoved0(ChannelHandlerContext var1) throws Exception {
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      if(var2 instanceof ByteBuf) {
         RecyclableArrayList var3 = RecyclableArrayList.newInstance();
         boolean var12 = false;

         try {
            var12 = true;
            ByteBuf var4 = (ByteBuf)var2;
            this.first = this.cumulation == null;
            if(this.first) {
               this.cumulation = var4;
            } else {
               if(this.cumulation.writerIndex() > this.cumulation.maxCapacity() - var4.readableBytes() || this.cumulation.refCnt() > 1) {
                  this.expandCumulation(var1, var4.readableBytes());
               }

               this.cumulation.writeBytes(var4);
               var4.release();
            }

            this.callDecode(var1, this.cumulation, var3);
            var12 = false;
         } catch (DecoderException var13) {
            throw var13;
         } catch (Throwable var14) {
            throw new DecoderException(var14);
         } finally {
            if(var12) {
               if(this.cumulation != null && !this.cumulation.isReadable()) {
                  this.cumulation.release();
                  this.cumulation = null;
               }

               int var7 = var3.size();
               this.decodeWasNull = var7 == 0;

               for(int var8 = 0; var8 < var7; ++var8) {
                  var1.fireChannelRead(var3.get(var8));
               }

               var3.recycle();
            }
         }

         if(this.cumulation != null && !this.cumulation.isReadable()) {
            this.cumulation.release();
            this.cumulation = null;
         }

         int var16 = var3.size();
         this.decodeWasNull = var16 == 0;

         for(int var5 = 0; var5 < var16; ++var5) {
            var1.fireChannelRead(var3.get(var5));
         }

         var3.recycle();
      } else {
         var1.fireChannelRead(var2);
      }

   }

   private void expandCumulation(ChannelHandlerContext var1, int var2) {
      ByteBuf var3 = this.cumulation;
      this.cumulation = var1.alloc().buffer(var3.readableBytes() + var2);
      this.cumulation.writeBytes(var3);
      var3.release();
   }

   public void channelReadComplete(ChannelHandlerContext var1) throws Exception {
      if(this.cumulation != null && !this.first && this.cumulation.refCnt() == 1) {
         this.cumulation.discardSomeReadBytes();
      }

      if(this.decodeWasNull) {
         this.decodeWasNull = false;
         if(!var1.channel().config().isAutoRead()) {
            var1.read();
         }
      }

      var1.fireChannelReadComplete();
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      RecyclableArrayList var2 = RecyclableArrayList.newInstance();
      boolean var25 = false;

      try {
         var25 = true;
         if(this.cumulation != null) {
            this.callDecode(var1, this.cumulation, var2);
            this.decodeLast(var1, this.cumulation, var2);
            var25 = false;
         } else {
            this.decodeLast(var1, Unpooled.EMPTY_BUFFER, var2);
            var25 = false;
         }
      } catch (DecoderException var26) {
         throw var26;
      } catch (Exception var27) {
         throw new DecoderException(var27);
      } finally {
         if(var25) {
            try {
               if(this.cumulation != null) {
                  this.cumulation.release();
                  this.cumulation = null;
               }

               int var7 = var2.size();

               for(int var8 = 0; var8 < var7; ++var8) {
                  var1.fireChannelRead(var2.get(var8));
               }

               if(var7 > 0) {
                  var1.fireChannelReadComplete();
               }

               var1.fireChannelInactive();
            } finally {
               var2.recycle();
            }
         }
      }

      try {
         if(this.cumulation != null) {
            this.cumulation.release();
            this.cumulation = null;
         }

         int var3 = var2.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            var1.fireChannelRead(var2.get(var4));
         }

         if(var3 > 0) {
            var1.fireChannelReadComplete();
         }

         var1.fireChannelInactive();
      } finally {
         var2.recycle();
      }

   }

   protected void callDecode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) {
      try {
         while(true) {
            if(var2.isReadable()) {
               int var4 = var3.size();
               int var5 = var2.readableBytes();
               this.decode(var1, var2, var3);
               if(!var1.isRemoved()) {
                  if(var4 == var3.size()) {
                     if(var5 != var2.readableBytes()) {
                        continue;
                     }
                  } else {
                     if(var5 == var2.readableBytes()) {
                        throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() did not read anything but decoded a message.");
                     }

                     if(!this.isSingleDecode()) {
                        continue;
                     }
                  }
               }
            }

            return;
         }
      } catch (DecoderException var6) {
         throw var6;
      } catch (Throwable var7) {
         throw new DecoderException(var7);
      }
   }

   protected abstract void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception;

   protected void decodeLast(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      this.decode(var1, var2, var3);
   }
}
