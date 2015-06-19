package io.netty.handler.codec.bytes;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

@ChannelHandler.Sharable
public class ByteArrayEncoder extends MessageToMessageEncoder<byte[]> {
   public ByteArrayEncoder() {
   }

   protected void encode(ChannelHandlerContext var1, byte[] var2, List<Object> var3) throws Exception {
      var3.add(Unpooled.wrappedBuffer(var2));
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.encode(var1, (byte[])var2, var3);
   }
}
