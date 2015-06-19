package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

public abstract class ByteToMessageCodec<I> extends ChannelDuplexHandler {
   private final TypeParameterMatcher outboundMsgMatcher;
   private final MessageToByteEncoder<I> encoder;
   private final ByteToMessageDecoder decoder;

   protected ByteToMessageCodec() {
      this(true);
   }

   protected ByteToMessageCodec(Class<? extends I> var1) {
      this(var1, true);
   }

   protected ByteToMessageCodec(boolean var1) {
      this.decoder = new ByteToMessageDecoder() {
         public void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
            ByteToMessageCodec.this.decode(var1, var2, var3);
         }

         protected void decodeLast(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
            ByteToMessageCodec.this.decodeLast(var1, var2, var3);
         }
      };
      this.outboundMsgMatcher = TypeParameterMatcher.find(this, ByteToMessageCodec.class, "I");
      this.encoder = new ByteToMessageCodec.Encoder(var1);
   }

   protected ByteToMessageCodec(Class<? extends I> var1, boolean var2) {
      this.decoder = new ByteToMessageDecoder() {
         public void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
            ByteToMessageCodec.this.decode(var1, var2, var3);
         }

         protected void decodeLast(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
            ByteToMessageCodec.this.decodeLast(var1, var2, var3);
         }
      };
      this.checkForSharableAnnotation();
      this.outboundMsgMatcher = TypeParameterMatcher.get(var1);
      this.encoder = new ByteToMessageCodec.Encoder(var2);
   }

   private void checkForSharableAnnotation() {
      if(this.isSharable()) {
         throw new IllegalStateException("@Sharable annotation is not allowed");
      }
   }

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return this.outboundMsgMatcher.match(var1);
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      this.decoder.channelRead(var1, var2);
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      this.encoder.write(var1, var2, var3);
   }

   protected abstract void encode(ChannelHandlerContext var1, I var2, ByteBuf var3) throws Exception;

   protected abstract void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception;

   protected void decodeLast(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      this.decode(var1, var2, var3);
   }

   private final class Encoder extends MessageToByteEncoder<I> {
      Encoder(boolean var2) {
         super(var2);
      }

      public boolean acceptOutboundMessage(Object var1) throws Exception {
         return ByteToMessageCodec.this.acceptOutboundMessage(var1);
      }

      protected void encode(ChannelHandlerContext var1, I var2, ByteBuf var3) throws Exception {
         ByteToMessageCodec.this.encode(var1, var2, var3);
      }
   }
}
