package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.util.List;

public class WebSocket08FrameEncoder extends MessageToMessageEncoder<WebSocketFrame> implements WebSocketFrameEncoder {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocket08FrameEncoder.class);
   private static final byte OPCODE_CONT = 0;
   private static final byte OPCODE_TEXT = 1;
   private static final byte OPCODE_BINARY = 2;
   private static final byte OPCODE_CLOSE = 8;
   private static final byte OPCODE_PING = 9;
   private static final byte OPCODE_PONG = 10;
   private final boolean maskPayload;

   public WebSocket08FrameEncoder(boolean var1) {
      this.maskPayload = var1;
   }

   protected void encode(ChannelHandlerContext var1, WebSocketFrame var2, List<Object> var3) throws Exception {
      ByteBuf var4 = var2.content();
      byte var6;
      if(var2 instanceof TextWebSocketFrame) {
         var6 = 1;
      } else if(var2 instanceof PingWebSocketFrame) {
         var6 = 9;
      } else if(var2 instanceof PongWebSocketFrame) {
         var6 = 10;
      } else if(var2 instanceof CloseWebSocketFrame) {
         var6 = 8;
      } else if(var2 instanceof BinaryWebSocketFrame) {
         var6 = 2;
      } else {
         if(!(var2 instanceof ContinuationWebSocketFrame)) {
            throw new UnsupportedOperationException("Cannot encode frame of type: " + var2.getClass().getName());
         }

         var6 = 0;
      }

      int var7 = var4.readableBytes();
      if(logger.isDebugEnabled()) {
         logger.debug("Encoding WebSocket Frame opCode=" + var6 + " length=" + var7);
      }

      int var8 = 0;
      if(var2.isFinalFragment()) {
         var8 |= 128;
      }

      var8 |= var2.rsv() % 8 << 4;
      var8 |= var6 % 128;
      if(var6 == 9 && var7 > 125) {
         throw new TooLongFrameException("invalid payload for PING (payload length must be <= 125, was " + var7);
      } else {
         boolean var9 = true;
         ByteBuf var10 = null;

         try {
            int var11 = this.maskPayload?4:0;
            int var12;
            if(var7 <= 125) {
               var12 = 2 + var11;
               if(this.maskPayload) {
                  var12 += var7;
               }

               var10 = var1.alloc().buffer(var12);
               var10.writeByte(var8);
               byte var13 = (byte)(this.maskPayload?128 | (byte)var7:(byte)var7);
               var10.writeByte(var13);
            } else if(var7 <= '\uffff') {
               var12 = 4 + var11;
               if(this.maskPayload) {
                  var12 += var7;
               }

               var10 = var1.alloc().buffer(var12);
               var10.writeByte(var8);
               var10.writeByte(this.maskPayload?254:126);
               var10.writeByte(var7 >>> 8 & 255);
               var10.writeByte(var7 & 255);
            } else {
               var12 = 10 + var11;
               if(this.maskPayload) {
                  var12 += var7;
               }

               var10 = var1.alloc().buffer(var12);
               var10.writeByte(var8);
               var10.writeByte(this.maskPayload?255:127);
               var10.writeLong((long)var7);
            }

            if(!this.maskPayload) {
               if(var10.writableBytes() >= var4.readableBytes()) {
                  var10.writeBytes(var4);
                  var3.add(var10);
               } else {
                  var3.add(var10);
                  var3.add(var4.retain());
               }
            } else {
               var12 = (int)(Math.random() * 2.147483647E9D);
               byte[] var5 = ByteBuffer.allocate(4).putInt(var12).array();
               var10.writeBytes(var5);
               int var19 = 0;

               for(int var14 = var4.readerIndex(); var14 < var4.writerIndex(); ++var14) {
                  byte var15 = var4.getByte(var14);
                  var10.writeByte(var15 ^ var5[var19++ % 4]);
               }

               var3.add(var10);
            }

            var9 = false;
         } finally {
            if(var9 && var10 != null) {
               var10.release();
            }

         }

      }
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.encode(var1, (WebSocketFrame)var2, var3);
   }
}
