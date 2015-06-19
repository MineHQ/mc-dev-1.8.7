package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.Utf8Validator;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.List;

public class WebSocket08FrameDecoder extends ReplayingDecoder<WebSocket08FrameDecoder.State> implements WebSocketFrameDecoder {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocket08FrameDecoder.class);
   private static final byte OPCODE_CONT = 0;
   private static final byte OPCODE_TEXT = 1;
   private static final byte OPCODE_BINARY = 2;
   private static final byte OPCODE_CLOSE = 8;
   private static final byte OPCODE_PING = 9;
   private static final byte OPCODE_PONG = 10;
   private int fragmentedFramesCount;
   private final long maxFramePayloadLength;
   private boolean frameFinalFlag;
   private int frameRsv;
   private int frameOpcode;
   private long framePayloadLength;
   private ByteBuf framePayload;
   private int framePayloadBytesRead;
   private byte[] maskingKey;
   private ByteBuf payloadBuffer;
   private final boolean allowExtensions;
   private final boolean maskedPayload;
   private boolean receivedClosingHandshake;
   private Utf8Validator utf8Validator;

   public WebSocket08FrameDecoder(boolean var1, boolean var2, int var3) {
      super(WebSocket08FrameDecoder.State.FRAME_START);
      this.maskedPayload = var1;
      this.allowExtensions = var2;
      this.maxFramePayloadLength = (long)var3;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      if(this.receivedClosingHandshake) {
         var2.skipBytes(this.actualReadableBytes());
      } else {
         try {
            switch(WebSocket08FrameDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$http$websocketx$WebSocket08FrameDecoder$State[((WebSocket08FrameDecoder.State)this.state()).ordinal()]) {
            case 1:
               this.framePayloadBytesRead = 0;
               this.framePayloadLength = -1L;
               this.framePayload = null;
               this.payloadBuffer = null;
               byte var4 = var2.readByte();
               this.frameFinalFlag = (var4 & 128) != 0;
               this.frameRsv = (var4 & 112) >> 4;
               this.frameOpcode = var4 & 15;
               if(logger.isDebugEnabled()) {
                  logger.debug("Decoding WebSocket Frame opCode={}", (Object)Integer.valueOf(this.frameOpcode));
               }

               var4 = var2.readByte();
               boolean var5 = (var4 & 128) != 0;
               int var6 = var4 & 127;
               if(this.frameRsv != 0 && !this.allowExtensions) {
                  this.protocolViolation(var1, "RSV != 0 and no extension negotiated, RSV:" + this.frameRsv);
                  return;
               } else if(this.maskedPayload && !var5) {
                  this.protocolViolation(var1, "unmasked client to server frame");
                  return;
               } else {
                  if(this.frameOpcode > 7) {
                     if(!this.frameFinalFlag) {
                        this.protocolViolation(var1, "fragmented control frame");
                        return;
                     }

                     if(var6 > 125) {
                        this.protocolViolation(var1, "control frame with payload length > 125 octets");
                        return;
                     }

                     if(this.frameOpcode != 8 && this.frameOpcode != 9 && this.frameOpcode != 10) {
                        this.protocolViolation(var1, "control frame using reserved opcode " + this.frameOpcode);
                        return;
                     }

                     if(this.frameOpcode == 8 && var6 == 1) {
                        this.protocolViolation(var1, "received close control frame with payload len 1");
                        return;
                     }
                  } else {
                     if(this.frameOpcode != 0 && this.frameOpcode != 1 && this.frameOpcode != 2) {
                        this.protocolViolation(var1, "data frame using reserved opcode " + this.frameOpcode);
                        return;
                     }

                     if(this.fragmentedFramesCount == 0 && this.frameOpcode == 0) {
                        this.protocolViolation(var1, "received continuation data frame outside fragmented message");
                        return;
                     }

                     if(this.fragmentedFramesCount != 0 && this.frameOpcode != 0 && this.frameOpcode != 9) {
                        this.protocolViolation(var1, "received non-continuation data frame while inside fragmented message");
                        return;
                     }
                  }

                  if(var6 == 126) {
                     this.framePayloadLength = (long)var2.readUnsignedShort();
                     if(this.framePayloadLength < 126L) {
                        this.protocolViolation(var1, "invalid data frame length (not using minimal length encoding)");
                        return;
                     }
                  } else if(var6 == 127) {
                     this.framePayloadLength = var2.readLong();
                     if(this.framePayloadLength < 65536L) {
                        this.protocolViolation(var1, "invalid data frame length (not using minimal length encoding)");
                        return;
                     }
                  } else {
                     this.framePayloadLength = (long)var6;
                  }

                  if(this.framePayloadLength > this.maxFramePayloadLength) {
                     this.protocolViolation(var1, "Max frame length of " + this.maxFramePayloadLength + " has been exceeded.");
                     return;
                  } else {
                     if(logger.isDebugEnabled()) {
                        logger.debug("Decoding WebSocket Frame length={}", (Object)Long.valueOf(this.framePayloadLength));
                     }

                     this.checkpoint(WebSocket08FrameDecoder.State.MASKING_KEY);
                  }
               }
            case 2:
               if(this.maskedPayload) {
                  if(this.maskingKey == null) {
                     this.maskingKey = new byte[4];
                  }

                  var2.readBytes(this.maskingKey);
               }

               this.checkpoint(WebSocket08FrameDecoder.State.PAYLOAD);
            case 3:
               int var7 = this.actualReadableBytes();
               long var8 = (long)(this.framePayloadBytesRead + var7);
               if(var8 == this.framePayloadLength) {
                  this.payloadBuffer = var1.alloc().buffer(var7);
                  this.payloadBuffer.writeBytes(var2, var7);
               } else {
                  if(var8 < this.framePayloadLength) {
                     if(this.framePayload == null) {
                        this.framePayload = var1.alloc().buffer(toFrameLength(this.framePayloadLength));
                     }

                     this.framePayload.writeBytes(var2, var7);
                     this.framePayloadBytesRead += var7;
                     return;
                  }

                  if(var8 > this.framePayloadLength) {
                     if(this.framePayload == null) {
                        this.framePayload = var1.alloc().buffer(toFrameLength(this.framePayloadLength));
                     }

                     this.framePayload.writeBytes(var2, toFrameLength(this.framePayloadLength - (long)this.framePayloadBytesRead));
                  }
               }

               this.checkpoint(WebSocket08FrameDecoder.State.FRAME_START);
               if(this.framePayload == null) {
                  this.framePayload = this.payloadBuffer;
                  this.payloadBuffer = null;
               } else if(this.payloadBuffer != null) {
                  this.framePayload.writeBytes(this.payloadBuffer);
                  this.payloadBuffer.release();
                  this.payloadBuffer = null;
               }

               if(this.maskedPayload) {
                  this.unmask(this.framePayload);
               }

               if(this.frameOpcode == 9) {
                  var3.add(new PingWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
                  this.framePayload = null;
                  return;
               } else if(this.frameOpcode == 10) {
                  var3.add(new PongWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
                  this.framePayload = null;
                  return;
               } else if(this.frameOpcode == 8) {
                  this.checkCloseFrameBody(var1, this.framePayload);
                  this.receivedClosingHandshake = true;
                  var3.add(new CloseWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
                  this.framePayload = null;
                  return;
               } else {
                  if(this.frameFinalFlag) {
                     if(this.frameOpcode != 9) {
                        this.fragmentedFramesCount = 0;
                        if(this.frameOpcode == 1 || this.utf8Validator != null && this.utf8Validator.isChecking()) {
                           this.checkUTF8String(var1, this.framePayload);
                           this.utf8Validator.finish();
                        }
                     }
                  } else {
                     if(this.fragmentedFramesCount == 0) {
                        if(this.frameOpcode == 1) {
                           this.checkUTF8String(var1, this.framePayload);
                        }
                     } else if(this.utf8Validator != null && this.utf8Validator.isChecking()) {
                        this.checkUTF8String(var1, this.framePayload);
                     }

                     ++this.fragmentedFramesCount;
                  }

                  if(this.frameOpcode == 1) {
                     var3.add(new TextWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
                     this.framePayload = null;
                     return;
                  } else if(this.frameOpcode == 2) {
                     var3.add(new BinaryWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
                     this.framePayload = null;
                     return;
                  } else {
                     if(this.frameOpcode == 0) {
                        var3.add(new ContinuationWebSocketFrame(this.frameFinalFlag, this.frameRsv, this.framePayload));
                        this.framePayload = null;
                        return;
                     }

                     throw new UnsupportedOperationException("Cannot decode web socket frame with opcode: " + this.frameOpcode);
                  }
               }
            case 4:
               var2.readByte();
               return;
            default:
               throw new Error("Shouldn\'t reach here.");
            }
         } catch (Exception var10) {
            if(this.payloadBuffer != null) {
               if(this.payloadBuffer.refCnt() > 0) {
                  this.payloadBuffer.release();
               }

               this.payloadBuffer = null;
            }

            if(this.framePayload != null) {
               if(this.framePayload.refCnt() > 0) {
                  this.framePayload.release();
               }

               this.framePayload = null;
            }

            throw var10;
         }
      }
   }

   private void unmask(ByteBuf var1) {
      for(int var2 = var1.readerIndex(); var2 < var1.writerIndex(); ++var2) {
         var1.setByte(var2, var1.getByte(var2) ^ this.maskingKey[var2 % 4]);
      }

   }

   private void protocolViolation(ChannelHandlerContext var1, String var2) {
      this.protocolViolation(var1, new CorruptedFrameException(var2));
   }

   private void protocolViolation(ChannelHandlerContext var1, CorruptedFrameException var2) {
      this.checkpoint(WebSocket08FrameDecoder.State.CORRUPT);
      if(var1.channel().isActive()) {
         var1.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
      }

      throw var2;
   }

   private static int toFrameLength(long var0) {
      if(var0 > 2147483647L) {
         throw new TooLongFrameException("Length:" + var0);
      } else {
         return (int)var0;
      }
   }

   private void checkUTF8String(ChannelHandlerContext var1, ByteBuf var2) {
      try {
         if(this.utf8Validator == null) {
            this.utf8Validator = new Utf8Validator();
         }

         this.utf8Validator.check(var2);
      } catch (CorruptedFrameException var4) {
         this.protocolViolation(var1, var4);
      }

   }

   protected void checkCloseFrameBody(ChannelHandlerContext var1, ByteBuf var2) {
      if(var2 != null && var2.isReadable()) {
         if(var2.readableBytes() == 1) {
            this.protocolViolation(var1, "Invalid close frame body");
         }

         int var3 = var2.readerIndex();
         var2.readerIndex(0);
         short var4 = var2.readShort();
         if(var4 >= 0 && var4 <= 999 || var4 >= 1004 && var4 <= 1006 || var4 >= 1012 && var4 <= 2999) {
            this.protocolViolation(var1, "Invalid close frame getStatus code: " + var4);
         }

         if(var2.isReadable()) {
            try {
               (new Utf8Validator()).check(var2);
            } catch (CorruptedFrameException var6) {
               this.protocolViolation(var1, var6);
            }
         }

         var2.readerIndex(var3);
      }
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      super.channelInactive(var1);
      if(this.framePayload != null) {
         this.framePayload.release();
      }

      if(this.payloadBuffer != null) {
         this.payloadBuffer.release();
      }

   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$http$websocketx$WebSocket08FrameDecoder$State = new int[WebSocket08FrameDecoder.State.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$http$websocketx$WebSocket08FrameDecoder$State[WebSocket08FrameDecoder.State.FRAME_START.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$websocketx$WebSocket08FrameDecoder$State[WebSocket08FrameDecoder.State.MASKING_KEY.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$websocketx$WebSocket08FrameDecoder$State[WebSocket08FrameDecoder.State.PAYLOAD.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$websocketx$WebSocket08FrameDecoder$State[WebSocket08FrameDecoder.State.CORRUPT.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   static enum State {
      FRAME_START,
      MASKING_KEY,
      PAYLOAD,
      CORRUPT;

      private State() {
      }
   }
}
