package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.socks.SocksMessage;

@ChannelHandler.Sharable
public class SocksMessageEncoder extends MessageToByteEncoder<SocksMessage> {
   private static final String name = "SOCKS_MESSAGE_ENCODER";

   public SocksMessageEncoder() {
   }

   /** @deprecated */
   @Deprecated
   public static String getName() {
      return "SOCKS_MESSAGE_ENCODER";
   }

   protected void encode(ChannelHandlerContext var1, SocksMessage var2, ByteBuf var3) throws Exception {
      var2.encodeAsByteBuf(var3);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) throws Exception {
      this.encode(var1, (SocksMessage)var2, var3);
   }
}
