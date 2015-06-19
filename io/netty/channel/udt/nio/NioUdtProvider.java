package io.netty.channel.udt.nio;

import com.barchart.udt.SocketUDT;
import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.ChannelUDT;
import com.barchart.udt.nio.KindUDT;
import com.barchart.udt.nio.RendezvousChannelUDT;
import com.barchart.udt.nio.SelectorProviderUDT;
import com.barchart.udt.nio.ServerSocketChannelUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.UdtServerChannel;
import io.netty.channel.udt.nio.NioUdtByteAcceptorChannel;
import io.netty.channel.udt.nio.NioUdtByteConnectorChannel;
import io.netty.channel.udt.nio.NioUdtByteRendezvousChannel;
import io.netty.channel.udt.nio.NioUdtMessageAcceptorChannel;
import io.netty.channel.udt.nio.NioUdtMessageConnectorChannel;
import io.netty.channel.udt.nio.NioUdtMessageRendezvousChannel;
import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;

public final class NioUdtProvider<T extends UdtChannel> implements ChannelFactory<T> {
   public static final ChannelFactory<UdtServerChannel> BYTE_ACCEPTOR;
   public static final ChannelFactory<UdtChannel> BYTE_CONNECTOR;
   public static final SelectorProvider BYTE_PROVIDER;
   public static final ChannelFactory<UdtChannel> BYTE_RENDEZVOUS;
   public static final ChannelFactory<UdtServerChannel> MESSAGE_ACCEPTOR;
   public static final ChannelFactory<UdtChannel> MESSAGE_CONNECTOR;
   public static final SelectorProvider MESSAGE_PROVIDER;
   public static final ChannelFactory<UdtChannel> MESSAGE_RENDEZVOUS;
   private final KindUDT kind;
   private final TypeUDT type;

   public static ChannelUDT channelUDT(Channel var0) {
      return (ChannelUDT)(var0 instanceof NioUdtByteAcceptorChannel?((NioUdtByteAcceptorChannel)var0).javaChannel():(var0 instanceof NioUdtByteConnectorChannel?((NioUdtByteConnectorChannel)var0).javaChannel():(var0 instanceof NioUdtByteRendezvousChannel?((NioUdtByteRendezvousChannel)var0).javaChannel():(var0 instanceof NioUdtMessageAcceptorChannel?((NioUdtMessageAcceptorChannel)var0).javaChannel():(var0 instanceof NioUdtMessageConnectorChannel?((NioUdtMessageConnectorChannel)var0).javaChannel():(var0 instanceof NioUdtMessageRendezvousChannel?((NioUdtMessageRendezvousChannel)var0).javaChannel():null))))));
   }

   static ServerSocketChannelUDT newAcceptorChannelUDT(TypeUDT var0) {
      try {
         return SelectorProviderUDT.from(var0).openServerSocketChannel();
      } catch (IOException var2) {
         throw new ChannelException("failed to open a server socket channel", var2);
      }
   }

   static SocketChannelUDT newConnectorChannelUDT(TypeUDT var0) {
      try {
         return SelectorProviderUDT.from(var0).openSocketChannel();
      } catch (IOException var2) {
         throw new ChannelException("failed to open a socket channel", var2);
      }
   }

   static RendezvousChannelUDT newRendezvousChannelUDT(TypeUDT var0) {
      try {
         return SelectorProviderUDT.from(var0).openRendezvousChannel();
      } catch (IOException var2) {
         throw new ChannelException("failed to open a rendezvous channel", var2);
      }
   }

   public static SocketUDT socketUDT(Channel var0) {
      ChannelUDT var1 = channelUDT(var0);
      return var1 == null?null:var1.socketUDT();
   }

   private NioUdtProvider(TypeUDT var1, KindUDT var2) {
      this.type = var1;
      this.kind = var2;
   }

   public KindUDT kind() {
      return this.kind;
   }

   public T newChannel() {
      switch(NioUdtProvider.SyntheticClass_1.$SwitchMap$com$barchart$udt$nio$KindUDT[this.kind.ordinal()]) {
      case 1:
         switch(NioUdtProvider.SyntheticClass_1.$SwitchMap$com$barchart$udt$TypeUDT[this.type.ordinal()]) {
         case 1:
            return new NioUdtMessageAcceptorChannel();
         case 2:
            return new NioUdtByteAcceptorChannel();
         default:
            throw new IllegalStateException("wrong type=" + this.type);
         }
      case 2:
         switch(NioUdtProvider.SyntheticClass_1.$SwitchMap$com$barchart$udt$TypeUDT[this.type.ordinal()]) {
         case 1:
            return new NioUdtMessageConnectorChannel();
         case 2:
            return new NioUdtByteConnectorChannel();
         default:
            throw new IllegalStateException("wrong type=" + this.type);
         }
      case 3:
         switch(NioUdtProvider.SyntheticClass_1.$SwitchMap$com$barchart$udt$TypeUDT[this.type.ordinal()]) {
         case 1:
            return new NioUdtMessageRendezvousChannel();
         case 2:
            return new NioUdtByteRendezvousChannel();
         default:
            throw new IllegalStateException("wrong type=" + this.type);
         }
      default:
         throw new IllegalStateException("wrong kind=" + this.kind);
      }
   }

   public TypeUDT type() {
      return this.type;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Channel newChannel() {
      return this.newChannel();
   }

   static {
      BYTE_ACCEPTOR = new NioUdtProvider(TypeUDT.STREAM, KindUDT.ACCEPTOR);
      BYTE_CONNECTOR = new NioUdtProvider(TypeUDT.STREAM, KindUDT.CONNECTOR);
      BYTE_PROVIDER = SelectorProviderUDT.STREAM;
      BYTE_RENDEZVOUS = new NioUdtProvider(TypeUDT.STREAM, KindUDT.RENDEZVOUS);
      MESSAGE_ACCEPTOR = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.ACCEPTOR);
      MESSAGE_CONNECTOR = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.CONNECTOR);
      MESSAGE_PROVIDER = SelectorProviderUDT.DATAGRAM;
      MESSAGE_RENDEZVOUS = new NioUdtProvider(TypeUDT.DATAGRAM, KindUDT.RENDEZVOUS);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$com$barchart$udt$TypeUDT;
      // $FF: synthetic field
      static final int[] $SwitchMap$com$barchart$udt$nio$KindUDT = new int[KindUDT.values().length];

      static {
         try {
            $SwitchMap$com$barchart$udt$nio$KindUDT[KindUDT.ACCEPTOR.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$com$barchart$udt$nio$KindUDT[KindUDT.CONNECTOR.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$com$barchart$udt$nio$KindUDT[KindUDT.RENDEZVOUS.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         $SwitchMap$com$barchart$udt$TypeUDT = new int[TypeUDT.values().length];

         try {
            $SwitchMap$com$barchart$udt$TypeUDT[TypeUDT.DATAGRAM.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$barchart$udt$TypeUDT[TypeUDT.STREAM.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
