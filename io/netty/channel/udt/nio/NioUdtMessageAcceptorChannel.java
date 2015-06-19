package io.netty.channel.udt.nio;

import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.udt.nio.NioUdtAcceptorChannel;
import io.netty.channel.udt.nio.NioUdtMessageConnectorChannel;
import java.util.List;

public class NioUdtMessageAcceptorChannel extends NioUdtAcceptorChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);

   public NioUdtMessageAcceptorChannel() {
      super(TypeUDT.DATAGRAM);
   }

   protected int doReadMessages(List<Object> var1) throws Exception {
      SocketChannelUDT var2 = this.javaChannel().accept();
      if(var2 == null) {
         return 0;
      } else {
         var1.add(new NioUdtMessageConnectorChannel(this, var2));
         return 1;
      }
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }
}
