package io.netty.channel.sctp;

import com.sun.nio.sctp.SctpStandardSocketOptions.InitMaxStreams;
import io.netty.channel.ChannelOption;
import java.net.SocketAddress;

public class SctpChannelOption<T> extends ChannelOption<T> {
   public static final SctpChannelOption<Boolean> SCTP_DISABLE_FRAGMENTS = new SctpChannelOption("SCTP_DISABLE_FRAGMENTS");
   public static final SctpChannelOption<Boolean> SCTP_EXPLICIT_COMPLETE = new SctpChannelOption("SCTP_EXPLICIT_COMPLETE");
   public static final SctpChannelOption<Integer> SCTP_FRAGMENT_INTERLEAVE = new SctpChannelOption("SCTP_FRAGMENT_INTERLEAVE");
   public static final SctpChannelOption<InitMaxStreams> SCTP_INIT_MAXSTREAMS = new SctpChannelOption("SCTP_INIT_MAXSTREAMS");
   public static final SctpChannelOption<Boolean> SCTP_NODELAY = new SctpChannelOption("SCTP_NODELAY");
   public static final SctpChannelOption<SocketAddress> SCTP_PRIMARY_ADDR = new SctpChannelOption("SCTP_PRIMARY_ADDR");
   public static final SctpChannelOption<SocketAddress> SCTP_SET_PEER_PRIMARY_ADDR = new SctpChannelOption("SCTP_SET_PEER_PRIMARY_ADDR");

   /** @deprecated */
   @Deprecated
   protected SctpChannelOption(String var1) {
      super(var1);
   }
}
