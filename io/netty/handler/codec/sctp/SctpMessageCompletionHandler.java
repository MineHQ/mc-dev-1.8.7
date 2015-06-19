package io.netty.handler.codec.sctp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SctpMessageCompletionHandler extends MessageToMessageDecoder<SctpMessage> {
   private final Map<Integer, ByteBuf> fragments = new HashMap();

   public SctpMessageCompletionHandler() {
   }

   protected void decode(ChannelHandlerContext var1, SctpMessage var2, List<Object> var3) throws Exception {
      ByteBuf var4 = var2.content();
      int var5 = var2.protocolIdentifier();
      int var6 = var2.streamIdentifier();
      boolean var7 = var2.isComplete();
      ByteBuf var8;
      if(this.fragments.containsKey(Integer.valueOf(var6))) {
         var8 = (ByteBuf)this.fragments.remove(Integer.valueOf(var6));
      } else {
         var8 = Unpooled.EMPTY_BUFFER;
      }

      if(var7 && !var8.isReadable()) {
         var3.add(var2);
      } else if(!var7 && var8.isReadable()) {
         this.fragments.put(Integer.valueOf(var6), Unpooled.wrappedBuffer(new ByteBuf[]{var8, var4}));
      } else if(var7 && var8.isReadable()) {
         this.fragments.remove(Integer.valueOf(var6));
         SctpMessage var9 = new SctpMessage(var5, var6, Unpooled.wrappedBuffer(new ByteBuf[]{var8, var4}));
         var3.add(var9);
      } else {
         this.fragments.put(Integer.valueOf(var6), var4);
      }

      var4.retain();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (SctpMessage)var2, var3);
   }
}
