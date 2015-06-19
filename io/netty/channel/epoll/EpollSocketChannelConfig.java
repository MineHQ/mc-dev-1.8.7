package io.netty.channel.epoll;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.epoll.Native;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.internal.PlatformDependent;
import java.util.Map;

public final class EpollSocketChannelConfig extends DefaultChannelConfig implements SocketChannelConfig {
   private final EpollSocketChannel channel;
   private volatile boolean allowHalfClosure;

   EpollSocketChannelConfig(EpollSocketChannel var1) {
      super(var1);
      this.channel = var1;
      if(PlatformDependent.canEnableTcpNoDelayByDefault()) {
         this.setTcpNoDelay(true);
      }

   }

   public Map<ChannelOption<?>, Object> getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE, EpollChannelOption.TCP_CORK, EpollChannelOption.TCP_KEEPCNT, EpollChannelOption.TCP_KEEPIDLE, EpollChannelOption.TCP_KEEPINTVL});
   }

   public <T> T getOption(ChannelOption<T> var1) {
      return var1 == ChannelOption.SO_RCVBUF?Integer.valueOf(this.getReceiveBufferSize()):(var1 == ChannelOption.SO_SNDBUF?Integer.valueOf(this.getSendBufferSize()):(var1 == ChannelOption.TCP_NODELAY?Boolean.valueOf(this.isTcpNoDelay()):(var1 == ChannelOption.SO_KEEPALIVE?Boolean.valueOf(this.isKeepAlive()):(var1 == ChannelOption.SO_REUSEADDR?Boolean.valueOf(this.isReuseAddress()):(var1 == ChannelOption.SO_LINGER?Integer.valueOf(this.getSoLinger()):(var1 == ChannelOption.IP_TOS?Integer.valueOf(this.getTrafficClass()):(var1 == ChannelOption.ALLOW_HALF_CLOSURE?Boolean.valueOf(this.isAllowHalfClosure()):(var1 == EpollChannelOption.TCP_CORK?Boolean.valueOf(this.isTcpCork()):(var1 == EpollChannelOption.TCP_KEEPIDLE?Integer.valueOf(this.getTcpKeepIdle()):(var1 == EpollChannelOption.TCP_KEEPINTVL?Integer.valueOf(this.getTcpKeepIntvl()):(var1 == EpollChannelOption.TCP_KEEPCNT?Integer.valueOf(this.getTcpKeepCnt()):super.getOption(var1))))))))))));
   }

   public <T> boolean setOption(ChannelOption<T> var1, T var2) {
      this.validate(var1, var2);
      if(var1 == ChannelOption.SO_RCVBUF) {
         this.setReceiveBufferSize(((Integer)var2).intValue());
      } else if(var1 == ChannelOption.SO_SNDBUF) {
         this.setSendBufferSize(((Integer)var2).intValue());
      } else if(var1 == ChannelOption.TCP_NODELAY) {
         this.setTcpNoDelay(((Boolean)var2).booleanValue());
      } else if(var1 == ChannelOption.SO_KEEPALIVE) {
         this.setKeepAlive(((Boolean)var2).booleanValue());
      } else if(var1 == ChannelOption.SO_REUSEADDR) {
         this.setReuseAddress(((Boolean)var2).booleanValue());
      } else if(var1 == ChannelOption.SO_LINGER) {
         this.setSoLinger(((Integer)var2).intValue());
      } else if(var1 == ChannelOption.IP_TOS) {
         this.setTrafficClass(((Integer)var2).intValue());
      } else if(var1 == ChannelOption.ALLOW_HALF_CLOSURE) {
         this.setAllowHalfClosure(((Boolean)var2).booleanValue());
      } else if(var1 == EpollChannelOption.TCP_CORK) {
         this.setTcpCork(((Boolean)var2).booleanValue());
      } else if(var1 == EpollChannelOption.TCP_KEEPIDLE) {
         this.setTcpKeepIdle(((Integer)var2).intValue());
      } else if(var1 == EpollChannelOption.TCP_KEEPCNT) {
         this.setTcpKeepCntl(((Integer)var2).intValue());
      } else {
         if(var1 != EpollChannelOption.TCP_KEEPINTVL) {
            return super.setOption(var1, var2);
         }

         this.setTcpKeepIntvl(((Integer)var2).intValue());
      }

      return true;
   }

   public int getReceiveBufferSize() {
      return Native.getReceiveBufferSize(this.channel.fd);
   }

   public int getSendBufferSize() {
      return Native.getSendBufferSize(this.channel.fd);
   }

   public int getSoLinger() {
      return Native.getSoLinger(this.channel.fd);
   }

   public int getTrafficClass() {
      return Native.getTrafficClass(this.channel.fd);
   }

   public boolean isKeepAlive() {
      return Native.isKeepAlive(this.channel.fd) == 1;
   }

   public boolean isReuseAddress() {
      return Native.isReuseAddress(this.channel.fd) == 1;
   }

   public boolean isTcpNoDelay() {
      return Native.isTcpNoDelay(this.channel.fd) == 1;
   }

   public boolean isTcpCork() {
      return Native.isTcpCork(this.channel.fd) == 1;
   }

   public int getTcpKeepIdle() {
      return Native.getTcpKeepIdle(this.channel.fd);
   }

   public int getTcpKeepIntvl() {
      return Native.getTcpKeepIntvl(this.channel.fd);
   }

   public int getTcpKeepCnt() {
      return Native.getTcpKeepCnt(this.channel.fd);
   }

   public EpollSocketChannelConfig setKeepAlive(boolean var1) {
      Native.setKeepAlive(this.channel.fd, var1?1:0);
      return this;
   }

   public EpollSocketChannelConfig setPerformancePreferences(int var1, int var2, int var3) {
      return this;
   }

   public EpollSocketChannelConfig setReceiveBufferSize(int var1) {
      Native.setReceiveBufferSize(this.channel.fd, var1);
      return this;
   }

   public EpollSocketChannelConfig setReuseAddress(boolean var1) {
      Native.setReuseAddress(this.channel.fd, var1?1:0);
      return this;
   }

   public EpollSocketChannelConfig setSendBufferSize(int var1) {
      Native.setSendBufferSize(this.channel.fd, var1);
      return this;
   }

   public EpollSocketChannelConfig setSoLinger(int var1) {
      Native.setSoLinger(this.channel.fd, var1);
      return this;
   }

   public EpollSocketChannelConfig setTcpNoDelay(boolean var1) {
      Native.setTcpNoDelay(this.channel.fd, var1?1:0);
      return this;
   }

   public EpollSocketChannelConfig setTcpCork(boolean var1) {
      Native.setTcpCork(this.channel.fd, var1?1:0);
      return this;
   }

   public EpollSocketChannelConfig setTrafficClass(int var1) {
      Native.setTrafficClass(this.channel.fd, var1);
      return this;
   }

   public EpollSocketChannelConfig setTcpKeepIdle(int var1) {
      Native.setTcpKeepIdle(this.channel.fd, var1);
      return this;
   }

   public EpollSocketChannelConfig setTcpKeepIntvl(int var1) {
      Native.setTcpKeepIntvl(this.channel.fd, var1);
      return this;
   }

   public EpollSocketChannelConfig setTcpKeepCntl(int var1) {
      Native.setTcpKeepCnt(this.channel.fd, var1);
      return this;
   }

   public boolean isAllowHalfClosure() {
      return this.allowHalfClosure;
   }

   public EpollSocketChannelConfig setAllowHalfClosure(boolean var1) {
      this.allowHalfClosure = var1;
      return this;
   }

   public EpollSocketChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public EpollSocketChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public EpollSocketChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public EpollSocketChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public EpollSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public EpollSocketChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   public EpollSocketChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public EpollSocketChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public EpollSocketChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public EpollSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      super.setMessageSizeEstimator(var1);
      return this;
   }

   protected void autoReadCleared() {
      this.channel.clearEpollIn();
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
   public SocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      return this.setMessageSizeEstimator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setAutoClose(boolean var1) {
      return this.setAutoClose(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setAutoRead(boolean var1) {
      return this.setAutoRead(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      return this.setRecvByteBufAllocator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setAllocator(ByteBufAllocator var1) {
      return this.setAllocator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setWriteSpinCount(int var1) {
      return this.setWriteSpinCount(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setMaxMessagesPerRead(int var1) {
      return this.setMaxMessagesPerRead(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setConnectTimeoutMillis(int var1) {
      return this.setConnectTimeoutMillis(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setAllowHalfClosure(boolean var1) {
      return this.setAllowHalfClosure(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setPerformancePreferences(int var1, int var2, int var3) {
      return this.setPerformancePreferences(var1, var2, var3);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setReuseAddress(boolean var1) {
      return this.setReuseAddress(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setTrafficClass(int var1) {
      return this.setTrafficClass(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setKeepAlive(boolean var1) {
      return this.setKeepAlive(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setReceiveBufferSize(int var1) {
      return this.setReceiveBufferSize(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setSendBufferSize(int var1) {
      return this.setSendBufferSize(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setSoLinger(int var1) {
      return this.setSoLinger(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setTcpNoDelay(boolean var1) {
      return this.setTcpNoDelay(var1);
   }
}
