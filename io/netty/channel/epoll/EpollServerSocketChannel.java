package io.netty.channel.epoll;

import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.epoll.EpollEventLoop;
import io.netty.channel.epoll.EpollServerSocketChannelConfig;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.epoll.Native;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.ServerSocketChannelConfig;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public final class EpollServerSocketChannel extends AbstractEpollChannel implements ServerSocketChannel {
   private final EpollServerSocketChannelConfig config = new EpollServerSocketChannelConfig(this);
   private volatile InetSocketAddress local;

   public EpollServerSocketChannel() {
      super(Native.socketStreamFd(), 4);
   }

   protected boolean isCompatible(EventLoop var1) {
      return var1 instanceof EpollEventLoop;
   }

   protected void doBind(SocketAddress var1) throws Exception {
      InetSocketAddress var2 = (InetSocketAddress)var1;
      checkResolvable(var2);
      Native.bind(this.fd, var2.getAddress(), var2.getPort());
      this.local = Native.localAddress(this.fd);
      Native.listen(this.fd, this.config.getBacklog());
      this.active = true;
   }

   public EpollServerSocketChannelConfig config() {
      return this.config;
   }

   protected InetSocketAddress localAddress0() {
      return this.local;
   }

   protected InetSocketAddress remoteAddress0() {
      return null;
   }

   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
      return new EpollServerSocketChannel.EpollServerSocketUnsafe();
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      throw new UnsupportedOperationException();
   }

   protected Object filterOutboundMessage(Object var1) throws Exception {
      throw new UnsupportedOperationException();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean isOpen() {
      return super.isOpen();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public InetSocketAddress localAddress() {
      return super.localAddress();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public InetSocketAddress remoteAddress() {
      return super.remoteAddress();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelMetadata metadata() {
      return super.metadata();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean isActive() {
      return super.isActive();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected SocketAddress remoteAddress0() {
      return this.remoteAddress0();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected SocketAddress localAddress0() {
      return this.localAddress0();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return this.newUnsafe();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig config() {
      return this.config();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ServerSocketChannelConfig config() {
      return this.config();
   }

   final class EpollServerSocketUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
      EpollServerSocketUnsafe() {
         super();
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         var3.setFailure(new UnsupportedOperationException());
      }

      void epollInReady() {
         assert EpollServerSocketChannel.this.eventLoop().inEventLoop();

         ChannelPipeline var1 = EpollServerSocketChannel.this.pipeline();
         Throwable var2 = null;

         try {
            try {
               while(true) {
                  int var3 = Native.accept(EpollServerSocketChannel.this.fd);
                  if(var3 == -1) {
                     break;
                  }

                  try {
                     this.readPending = false;
                     var1.fireChannelRead(new EpollSocketChannel(EpollServerSocketChannel.this, var3));
                  } catch (Throwable var9) {
                     var1.fireChannelReadComplete();
                     var1.fireExceptionCaught(var9);
                  }
               }
            } catch (Throwable var10) {
               var2 = var10;
            }

            var1.fireChannelReadComplete();
            if(var2 != null) {
               var1.fireExceptionCaught(var2);
            }
         } finally {
            if(!EpollServerSocketChannel.this.config.isAutoRead() && !this.readPending) {
               this.clearEpollIn0();
            }

         }

      }
   }
}
