package io.netty.channel.udt.nio;

import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.channel.udt.nio.NioUdtMessageConnectorChannel;
import io.netty.channel.udt.nio.NioUdtProvider;

public class NioUdtMessageRendezvousChannel extends NioUdtMessageConnectorChannel {
   public NioUdtMessageRendezvousChannel() {
      super((SocketChannelUDT)NioUdtProvider.newRendezvousChannelUDT(TypeUDT.DATAGRAM));
   }
}
