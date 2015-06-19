package io.netty.channel.epoll;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.Native;
import io.netty.channel.socket.DatagramChannelConfig;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;

public final class EpollDatagramChannelConfig extends DefaultChannelConfig implements DatagramChannelConfig {
   private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR = new FixedRecvByteBufAllocator(2048);
   private final EpollDatagramChannel datagramChannel;
   private boolean activeOnOpen;

   EpollDatagramChannelConfig(EpollDatagramChannel var1) {
      super(var1);
      this.datagramChannel = var1;
      this.setRecvByteBufAllocator(DEFAULT_RCVBUF_ALLOCATOR);
   }

   public Map<ChannelOption<?>, Object> getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_BROADCAST, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.IP_MULTICAST_LOOP_DISABLED, ChannelOption.IP_MULTICAST_ADDR, ChannelOption.IP_MULTICAST_IF, ChannelOption.IP_MULTICAST_TTL, ChannelOption.IP_TOS, ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, EpollChannelOption.SO_REUSEPORT});
   }

   public <T> T getOption(ChannelOption<T> var1) {
      return var1 == ChannelOption.SO_BROADCAST?Boolean.valueOf(this.isBroadcast()):(var1 == ChannelOption.SO_RCVBUF?Integer.valueOf(this.getReceiveBufferSize()):(var1 == ChannelOption.SO_SNDBUF?Integer.valueOf(this.getSendBufferSize()):(var1 == ChannelOption.SO_REUSEADDR?Boolean.valueOf(this.isReuseAddress()):(var1 == ChannelOption.IP_MULTICAST_LOOP_DISABLED?Boolean.valueOf(this.isLoopbackModeDisabled()):(var1 == ChannelOption.IP_MULTICAST_ADDR?this.getInterface():(var1 == ChannelOption.IP_MULTICAST_IF?this.getNetworkInterface():(var1 == ChannelOption.IP_MULTICAST_TTL?Integer.valueOf(this.getTimeToLive()):(var1 == ChannelOption.IP_TOS?Integer.valueOf(this.getTrafficClass()):(var1 == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION?Boolean.valueOf(this.activeOnOpen):(var1 == EpollChannelOption.SO_REUSEPORT?Boolean.valueOf(this.isReusePort()):super.getOption(var1)))))))))));
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
      } else if(var1 == ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) {
         this.setActiveOnOpen(((Boolean)var2).booleanValue());
      } else {
         if(var1 != EpollChannelOption.SO_REUSEPORT) {
            return super.setOption(var1, var2);
         }

         this.setReusePort(((Boolean)var2).booleanValue());
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

   public EpollDatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      super.setMessageSizeEstimator(var1);
      return this;
   }

   public EpollDatagramChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public EpollDatagramChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public EpollDatagramChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public EpollDatagramChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public EpollDatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public EpollDatagramChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public EpollDatagramChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public EpollDatagramChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public EpollDatagramChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public int getSendBufferSize() {
      return Native.getSendBufferSize(this.datagramChannel.fd);
   }

   public EpollDatagramChannelConfig setSendBufferSize(int var1) {
      Native.setSendBufferSize(this.datagramChannel.fd, var1);
      return this;
   }

   public int getReceiveBufferSize() {
      return Native.getReceiveBufferSize(this.datagramChannel.fd);
   }

   public EpollDatagramChannelConfig setReceiveBufferSize(int var1) {
      Native.setReceiveBufferSize(this.datagramChannel.fd, var1);
      return this;
   }

   public int getTrafficClass() {
      return Native.getTrafficClass(this.datagramChannel.fd);
   }

   public EpollDatagramChannelConfig setTrafficClass(int var1) {
      Native.setTrafficClass(this.datagramChannel.fd, var1);
      return this;
   }

   public boolean isReuseAddress() {
      return Native.isReuseAddress(this.datagramChannel.fd) == 1;
   }

   public EpollDatagramChannelConfig setReuseAddress(boolean var1) {
      Native.setReuseAddress(this.datagramChannel.fd, var1?1:0);
      return this;
   }

   public boolean isBroadcast() {
      return Native.isBroadcast(this.datagramChannel.fd) == 1;
   }

   public EpollDatagramChannelConfig setBroadcast(boolean var1) {
      Native.setBroadcast(this.datagramChannel.fd, var1?1:0);
      return this;
   }

   public boolean isLoopbackModeDisabled() {
      return false;
   }

   public DatagramChannelConfig setLoopbackModeDisabled(boolean var1) {
      throw new UnsupportedOperationException("Multicast not supported");
   }

   public int getTimeToLive() {
      return -1;
   }

   public EpollDatagramChannelConfig setTimeToLive(int var1) {
      throw new UnsupportedOperationException("Multicast not supported");
   }

   public InetAddress getInterface() {
      return null;
   }

   public EpollDatagramChannelConfig setInterface(InetAddress var1) {
      throw new UnsupportedOperationException("Multicast not supported");
   }

   public NetworkInterface getNetworkInterface() {
      return null;
   }

   public EpollDatagramChannelConfig setNetworkInterface(NetworkInterface var1) {
      throw new UnsupportedOperationException("Multicast not supported");
   }

   public boolean isReusePort() {
      return Native.isReusePort(this.datagramChannel.fd) == 1;
   }

   public EpollDatagramChannelConfig setReusePort(boolean var1) {
      Native.setReusePort(this.datagramChannel.fd, var1?1:0);
      return this;
   }

   protected void autoReadCleared() {
      this.datagramChannel.clearEpollIn();
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

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      return this.setMessageSizeEstimator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setAutoClose(boolean var1) {
      return this.setAutoClose(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setAutoRead(boolean var1) {
      return this.setAutoRead(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      return this.setRecvByteBufAllocator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setAllocator(ByteBufAllocator var1) {
      return this.setAllocator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setConnectTimeoutMillis(int var1) {
      return this.setConnectTimeoutMillis(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setWriteSpinCount(int var1) {
      return this.setWriteSpinCount(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setMaxMessagesPerRead(int var1) {
      return this.setMaxMessagesPerRead(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setNetworkInterface(NetworkInterface var1) {
      return this.setNetworkInterface(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setInterface(InetAddress var1) {
      return this.setInterface(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setTimeToLive(int var1) {
      return this.setTimeToLive(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setBroadcast(boolean var1) {
      return this.setBroadcast(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setReuseAddress(boolean var1) {
      return this.setReuseAddress(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setTrafficClass(int var1) {
      return this.setTrafficClass(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setReceiveBufferSize(int var1) {
      return this.setReceiveBufferSize(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public DatagramChannelConfig setSendBufferSize(int var1) {
      return this.setSendBufferSize(var1);
   }
}
