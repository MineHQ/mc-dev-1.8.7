package io.netty.channel.socket.oio;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.DefaultSocketChannelConfig;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannelConfig;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class DefaultOioSocketChannelConfig extends DefaultSocketChannelConfig implements OioSocketChannelConfig {
   /** @deprecated */
   @Deprecated
   public DefaultOioSocketChannelConfig(SocketChannel var1, Socket var2) {
      super(var1, var2);
   }

   DefaultOioSocketChannelConfig(OioSocketChannel var1, Socket var2) {
      super(var1, var2);
   }

   public Map<ChannelOption<?>, Object> getOptions() {
      return this.getOptions(super.getOptions(), new ChannelOption[]{ChannelOption.SO_TIMEOUT});
   }

   public <T> T getOption(ChannelOption<T> var1) {
      return var1 == ChannelOption.SO_TIMEOUT?Integer.valueOf(this.getSoTimeout()):super.getOption(var1);
   }

   public <T> boolean setOption(ChannelOption<T> var1, T var2) {
      this.validate(var1, var2);
      if(var1 == ChannelOption.SO_TIMEOUT) {
         this.setSoTimeout(((Integer)var2).intValue());
         return true;
      } else {
         return super.setOption(var1, var2);
      }
   }

   public OioSocketChannelConfig setSoTimeout(int var1) {
      try {
         this.javaSocket.setSoTimeout(var1);
         return this;
      } catch (IOException var3) {
         throw new ChannelException(var3);
      }
   }

   public int getSoTimeout() {
      try {
         return this.javaSocket.getSoTimeout();
      } catch (IOException var2) {
         throw new ChannelException(var2);
      }
   }

   public OioSocketChannelConfig setTcpNoDelay(boolean var1) {
      super.setTcpNoDelay(var1);
      return this;
   }

   public OioSocketChannelConfig setSoLinger(int var1) {
      super.setSoLinger(var1);
      return this;
   }

   public OioSocketChannelConfig setSendBufferSize(int var1) {
      super.setSendBufferSize(var1);
      return this;
   }

   public OioSocketChannelConfig setReceiveBufferSize(int var1) {
      super.setReceiveBufferSize(var1);
      return this;
   }

   public OioSocketChannelConfig setKeepAlive(boolean var1) {
      super.setKeepAlive(var1);
      return this;
   }

   public OioSocketChannelConfig setTrafficClass(int var1) {
      super.setTrafficClass(var1);
      return this;
   }

   public OioSocketChannelConfig setReuseAddress(boolean var1) {
      super.setReuseAddress(var1);
      return this;
   }

   public OioSocketChannelConfig setPerformancePreferences(int var1, int var2, int var3) {
      super.setPerformancePreferences(var1, var2, var3);
      return this;
   }

   public OioSocketChannelConfig setAllowHalfClosure(boolean var1) {
      super.setAllowHalfClosure(var1);
      return this;
   }

   public OioSocketChannelConfig setConnectTimeoutMillis(int var1) {
      super.setConnectTimeoutMillis(var1);
      return this;
   }

   public OioSocketChannelConfig setMaxMessagesPerRead(int var1) {
      super.setMaxMessagesPerRead(var1);
      return this;
   }

   public OioSocketChannelConfig setWriteSpinCount(int var1) {
      super.setWriteSpinCount(var1);
      return this;
   }

   public OioSocketChannelConfig setAllocator(ByteBufAllocator var1) {
      super.setAllocator(var1);
      return this;
   }

   public OioSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1) {
      super.setRecvByteBufAllocator(var1);
      return this;
   }

   public OioSocketChannelConfig setAutoRead(boolean var1) {
      super.setAutoRead(var1);
      return this;
   }

   protected void autoReadCleared() {
      if(this.channel instanceof OioSocketChannel) {
         ((OioSocketChannel)this.channel).setReadPending(false);
      }

   }

   public OioSocketChannelConfig setAutoClose(boolean var1) {
      super.setAutoClose(var1);
      return this;
   }

   public OioSocketChannelConfig setWriteBufferHighWaterMark(int var1) {
      super.setWriteBufferHighWaterMark(var1);
      return this;
   }

   public OioSocketChannelConfig setWriteBufferLowWaterMark(int var1) {
      super.setWriteBufferLowWaterMark(var1);
      return this;
   }

   public OioSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      super.setMessageSizeEstimator(var1);
      return this;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1) {
      return this.setMessageSizeEstimator(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setWriteBufferLowWaterMark(int var1) {
      return this.setWriteBufferLowWaterMark(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setWriteBufferHighWaterMark(int var1) {
      return this.setWriteBufferHighWaterMark(var1);
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
   public SocketChannelConfig setTrafficClass(int var1) {
      return this.setTrafficClass(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setTcpNoDelay(boolean var1) {
      return this.setTcpNoDelay(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setSoLinger(int var1) {
      return this.setSoLinger(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setSendBufferSize(int var1) {
      return this.setSendBufferSize(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setReuseAddress(boolean var1) {
      return this.setReuseAddress(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setReceiveBufferSize(int var1) {
      return this.setReceiveBufferSize(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setPerformancePreferences(int var1, int var2, int var3) {
      return this.setPerformancePreferences(var1, var2, var3);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketChannelConfig setKeepAlive(boolean var1) {
      return this.setKeepAlive(var1);
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
