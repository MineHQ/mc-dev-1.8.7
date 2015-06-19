package io.netty.handler.codec.sctp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.sctp.SctpMessageCompletionHandler;
import java.util.List;

public class SctpInboundByteStreamHandler extends MessageToMessageDecoder<SctpMessage> {
   private final int protocolIdentifier;
   private final int streamIdentifier;

   public SctpInboundByteStreamHandler(int var1, int var2) {
      this.protocolIdentifier = var1;
      this.streamIdentifier = var2;
   }

   public final boolean acceptInboundMessage(Object var1) throws Exception {
      return super.acceptInboundMessage(var1)?this.acceptInboundMessage((SctpMessage)var1):false;
   }

   protected boolean acceptInboundMessage(SctpMessage var1) {
      return var1.protocolIdentifier() == this.protocolIdentifier && var1.streamIdentifier() == this.streamIdentifier;
   }

   protected void decode(ChannelHandlerContext var1, SctpMessage var2, List<Object> var3) throws Exception {
      if(!var2.isComplete()) {
         throw new CodecException(String.format("Received SctpMessage is not complete, please add %s in the pipeline before this handler", new Object[]{SctpMessageCompletionHandler.class.getSimpleName()}));
      } else {
         var3.add(var2.content().retain());
      }
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (SctpMessage)var2, var3);
   }
}
