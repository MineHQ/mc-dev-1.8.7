package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoderBuffer;
import io.netty.util.Signal;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.StringUtil;
import java.util.List;

public abstract class ReplayingDecoder<S> extends ByteToMessageDecoder {
   static final Signal REPLAY = Signal.valueOf(ReplayingDecoder.class.getName() + ".REPLAY");
   private final ReplayingDecoderBuffer replayable;
   private S state;
   private int checkpoint;

   protected ReplayingDecoder() {
      this((Object)null);
   }

   protected ReplayingDecoder(S var1) {
      this.replayable = new ReplayingDecoderBuffer();
      this.checkpoint = -1;
      this.state = var1;
   }

   protected void checkpoint() {
      this.checkpoint = this.internalBuffer().readerIndex();
   }

   protected void checkpoint(S var1) {
      this.checkpoint();
      this.state(var1);
   }

   protected S state() {
      return this.state;
   }

   protected S state(S var1) {
      Object var2 = this.state;
      this.state = var1;
      return var2;
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      RecyclableArrayList var2 = RecyclableArrayList.newInstance();
      boolean var39 = false;

      int var3;
      int var4;
      label457: {
         try {
            var39 = true;
            this.replayable.terminate();
            this.callDecode(var1, this.internalBuffer(), var2);
            this.decodeLast(var1, this.replayable, var2);
            var39 = false;
            break label457;
         } catch (Signal var43) {
            var43.expect(REPLAY);
            var39 = false;
         } catch (DecoderException var44) {
            throw var44;
         } catch (Exception var45) {
            throw new DecoderException(var45);
         } finally {
            if(var39) {
               try {
                  if(this.cumulation != null) {
                     this.cumulation.release();
                     this.cumulation = null;
                  }

                  int var8 = var2.size();

                  for(int var9 = 0; var9 < var8; ++var9) {
                     var1.fireChannelRead(var2.get(var9));
                  }

                  if(var8 > 0) {
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

            var3 = var2.size();

            for(var4 = 0; var4 < var3; ++var4) {
               var1.fireChannelRead(var2.get(var4));
            }

            if(var3 > 0) {
               var1.fireChannelReadComplete();
            }

            var1.fireChannelInactive();
            return;
         } finally {
            var2.recycle();
         }
      }

      try {
         if(this.cumulation != null) {
            this.cumulation.release();
            this.cumulation = null;
         }

         var3 = var2.size();

         for(var4 = 0; var4 < var3; ++var4) {
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
      this.replayable.setCumulation(var2);

      try {
         while(var2.isReadable()) {
            int var4 = this.checkpoint = var2.readerIndex();
            int var5 = var3.size();
            Object var6 = this.state;
            int var7 = var2.readableBytes();

            try {
               this.decode(var1, this.replayable, var3);
               if(var1.isRemoved()) {
                  break;
               }

               if(var5 == var3.size()) {
                  if(var7 == var2.readableBytes() && var6 == this.state) {
                     throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() must consume the inbound " + "data or change its state if it did not decode anything.");
                  }
                  continue;
               }
            } catch (Signal var10) {
               var10.expect(REPLAY);
               if(!var1.isRemoved()) {
                  int var9 = this.checkpoint;
                  if(var9 >= 0) {
                     var2.readerIndex(var9);
                  }
               }
               break;
            }

            if(var4 == var2.readerIndex() && var6 == this.state) {
               throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() method must consume the inbound data " + "or change its state if it decoded something.");
            }

            if(this.isSingleDecode()) {
               break;
            }
         }

      } catch (DecoderException var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new DecoderException(var12);
      }
   }
}
