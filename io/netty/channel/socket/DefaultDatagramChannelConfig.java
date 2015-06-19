package io.netty.channel.socket;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Map;

public class DefaultDatagramChannelConfig extends DefaultChannelConfig implements DatagramChannelConfig {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultDatagramChannelConfig.class);
   private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR = new FixedRecvByteBufAllocator(2048);
   private final DatagramSocket javaSocket;
   private volatile boolean activeOnOpen;

   public DefaultDatagramChannelConfig(DatagramChannel var1, DatagramSocket var2) {
      super(var1);
      if(var2 == null) {
         throw new NullPointerException("javaSocket");
      } else {
         this.javaSocket = var2;
         this.setRecvByteBufAllocator(DEFAULT_RCVBUF_ALLOCATOR);
      }
   }

   public Map<ChannelOption<?>, Object> getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_BROADCAST, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.IP_MULTICAST_LOOP_DISABLED, ChannelOption.IP_MULTICAST_ADDR, ChannelOption.IP_MULTICAST_IF, ChannelOption.IP_MULTICAST_TTL, ChannelOption.IP_TOS, ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION});
   }

   public <T> T getOption(ChannelOption<T> var1) {
      return var1 == ChannelOption.SO_BROADCAST?Boolean.valueOf(this.isBroadcast()):(var1 == ChannelOption.SO_RCVBUF?Integer.valueOf(this.getReceiveBufferSize()):(var1 == ChannelOption.SO_SNDBUF?Integer.valueOf(this.getSendBufferSize()):(var1 == ChannelOption.SO_REUSEADDR?Boolean.valueOf(this.isReuseAddress()):(var1 == ChannelOption.IP_MULTICAST_LOOP_DISABLED?Boolean.valueOf(this.isLoopbackModeDisabled()):(var1 == ChannelOption.IP_MULTICAST_ADDR?this.getInterface():(var1 == ChannelOption.IP_MULTICAST_IF?this.getNetworkInterface():(var1 == ChannelOption.IP_MULTICAST_TTL?Integer.valueOf(this.getTimeToLive()):(var1 == ChannelOption.IP_TOS?Integer.valueOf(this.getTrafficClass()):(var1 == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION?Boolean.valueOf(this.activeOnOpen):super.getOption(var1))))))))));
   }

   public <T> boolean setOption(ChannelOption<T> var1, T var2) {
      this.validate(var1, var2);
      if(var1 == ChannelOption.SO_BROADCAST) {
         this.setBroadcast(((Boolean)var2).booleanValue());
      } else if(var1 == ChannelOption.SO_RCVBUF) {
         this.setReceiveBufferSize(((Integer)var2).intValue());
      } else if(var1 == ChannelOption.SO_SNDBUF) {
         this.setSendBufferSize(((Integer)var2).intValue());
      } else if(var1 == ChannelOption.SO_REUSEADDR) {
         this.setReuseAddress(((Boolean)var2).booleanValue());
      } else if(var1 == ChannelOption.IP_MULTICAST_LOOP_DISABLED) {
         this.setLoopbackModeDisabled(((Boolean)var2).booleanValue());
      } else if(var1 == ChannelOption.IP_MULTICAST_ADDR) {
         this.setInterface((InetAddress)var2);
      } else if(var1 == ChannelOption.IP_MULTICAST_IF) {
         this.setNetworkInterface((NetworkInterface)var2);
      } else if(var1 == ChannelOption.IP_MULTICAST_TTL) {
         this.setTimeToLive(((Integer)var2).intValue());
      } else if(var1 == ChannelOption.IP_TOS) {
         this.setTrafficClass(((Integer)var2).intValue());
      } else {
         if(var1 != ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
            return super.setOption(var1, var2);
         }

         this.setActiveOnOpen(((Boolean)var2).booleanValue());
      }

      return true;
   }

   private void setActiveOnOpen(boolean var1) {
      if(this.channel.isRegistered()) {
         throw new IllegalStateException("Can only changed before channel was registered");
      } else {
         this.activeOnOpen = var1;
      }
   }

   public boolean isBroadcast() {
      try {
         return this.javaSocket.getBroadcast();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public DatagramChannelConfig setBroadcast(boolean var1) {
      try {
         if(var1 && !PlatformDependent.isWindows() && !PlatformDependent.isRoot() && !this.javaSocket.getLocalAddress().isAnyLocalAddress()) {
            logger.warn("A non-root user can\'t receive a broadcast packet if the socket is not bound to a wildcard address; setting the SO_BROADCAST flag anyway as requested on the socket which is bound to " + this.javaSocket.getLocalSocketAddress() + '.');
         }

         this.javaSocket.setBroadcast(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public InetAddress getInterface() {
      if(this.javaSocket instanceof MulticastSocket) {
         try {
            return ((MulticastSocket)this.javaSocket).getInterface();
         } catch (SocketException var2) {
            throw new ChannelException(var2);
         }
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public DatagramChannelConfig setInterface(InetAddress var1) {
      if(this.javaSocket instanceof MulticastSocket) {
         try {
            ((MulticastSocket)this.javaSocket).setInterface(var1);
            return this;
         } catch (SocketException var3) {
            throw new ChannelException(var3);
         }
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public boolean isLoopbackModeDisabled() {
      if(this.javaSocket instanceof MulticastSocket) {
         try {
            return ((MulticastSocket)this.javaSocket).getLoopbackMode();
         } catch (SocketException var2) {
            throw new ChannelException(var2);
         }
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public DatagramChannelConfig setLoopbackModeDisabled(boolean var1) {
      if(this.javaSocket instanceof MulticastSocket) {
         try {
            ((MulticastSocket)this.javaSocket).setLoopbackMode(var1);
            return this;
         } catch (SocketException var3) {
            throw new ChannelException(var3);
         }
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public NetworkInterface getNetworkInterface() {
      if(this.javaSocket instanceof MulticastSocket) {
         try {
            return ((MulticastSocket)this.javaSocket).getNetworkInterface();
         } catch (SocketException var2) {
            throw new ChannelException(var2);
         }
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public DatagramChannelConfig setNetworkInterface(NetworkInterface var1) {
      if(this.javaSocket instanceof MulticastSocket) {
         try {
            ((MulticastSocket)this.javaSocket).setNetworkInterface(var1);
            return this;
         } catch (SocketException var3) {
            throw new ChannelException(var3);
         }
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public boolean isReuseAddress() {
      try {
         return this.javaSocket.getReuseAddress();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public DatagramChannelConfig setReuseAddress(boolean var1) {
      try {
         this.javaSocket.setReuseAddress(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public int getReceiveBufferSize() {
      try {
         return this.javaSocket.getReceiveBufferSize();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public DatagramChannelConfig setReceiveBufferSize(int var1) {
      try {
         this.javaSocket.setReceiveBufferSize(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public int getSendBufferSize() {
      try {
         return this.javaSocket.getSendBufferSize();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public DatagramChannelConfig setSendBufferSize(int var1) {
      try {
         this.javaSocket.setSendBufferSize(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public int getTimeToLive() {
      if(this.javaSocket instanceof MulticastSocket) {
         try {
            return ((MulticastSocket)this.javaSocket).getTimeToLive();
         } catch (IOException var2) {
            throw new ChannelException(var2);
         }
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public DatagramChannelConfig setTimeToLive(int var1) {
      if(this.javaSocket instanceof MulticastSocket) {
         try {
            ((MulticastSocket)this.javaSocket).setTimeToLive(var1);
            return this;
         } catch (IOException var3) {
            throw new ChannelException(var3);
         }
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public int getTrafficClass() {
      try {
         return this.javaSocket.getTrafficClass();
      } catch (SocketException var2) {
         throw new ChannelException(var2);
      }
   }

   public DatagramChannelConfig setTrafficClass(int var1) {
      try {
         this.javaSocket.setTrafficClass(var1);
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public DatagramChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public DatagramChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public DatagramChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public DatagramChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public DatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public DatagramChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public DatagramChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public DatagramChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public DatagramChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public DatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      super.setMessageSizeEstimator(var1);
      return this;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      return this.setMessageSizeEstimator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setWriteBufferLowWaterMark(int var1) {
      return this.setWriteBufferLowWaterMark(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setWriteBufferHighWaterMark(int var1) {
      return this.setWriteBufferHighWaterMark(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setAutoClose(boolean var1) {
      return this.setAutoClose(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setAutoRead(boolean var1) {
      return this.setAutoRead(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      return this.setRecvByteBufAllocator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setAllocator(ByteBufAllocator var1) {
      return this.setAllocator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setWriteSpinCount(int var1) {
      return this.setWriteSpinCount(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setMaxMessagesPerRead(int var1) {
      return this.setMaxMessagesPerRead(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig setConnectTimeoutMillis(int var1) {
      return this.setConnectTimeoutMillis(var1);
   }
}
