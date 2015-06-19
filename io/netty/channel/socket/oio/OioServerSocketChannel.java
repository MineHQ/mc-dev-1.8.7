package io.netty.channel.socket.oio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.oio.AbstractOioMessageChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.ServerSocketChannelConfig;
import io.netty.channel.socket.oio.DefaultOioServerSocketChannelConfig;
import io.netty.channel.socket.oio.OioServerSocketChannelConfig;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OioServerSocketChannel extends AbstractOioMessageChannel implements ServerSocketChannel {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OioServerSocketChannel.class);
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   final ServerSocket socket;
   final Lock shutdownLock;
   private final OioServerSocketChannelConfig config;

   private static ServerSocket newServerSocket() {
      try {
         return new ServerSocket();
      } catch (IOException var1) {
         throw new ChannelException("failed to create a server socket", var1);
      }
   }

   public OioServerSocketChannel() {
      this(newServerSocket());
   }

   public OioServerSocketChannel(ServerSocket var1) {
      super((Channel)null);
      this.shutdownLock = new ReentrantLock();
      if(var1 == null) {
         throw new NullPointerException("socket");
      } else {
         boolean var2 = false;

         try {
            var1.setSoTimeout(1000);
            var2 = true;
         } catch (IOException var10) {
            throw new ChannelException("Failed to set the server socket timeout.", var10);
         } finally {
            if(!var2) {
               try {
                  var1.close();
               } catch (IOException var11) {
                  if(logger.isWarnEnabled()) {
                     logger.warn("Failed to close a partially initialized socket.", (Throwable)var11);
                  }
               }
            }

         }

         this.socket = var1;
         this.config = new DefaultOioServerSocketChannelConfig(this, var1);
      }
   }

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public OioServerSocketChannelConfig config() {
      return this.config;
   }

   public InetSocketAddress remoteAddress() {
      return null;
   }

   public boolean isOpen() {
      return !this.socket.isClosed();
   }

   public boolean isActive() {
      return this.isOpen() && this.socket.isBound();
   }

   protected SocketAddress localAddress0() {
      return this.socket.getLocalSocketAddress();
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.socket.bind(var1, this.config.getBacklog());
   }

   protected void doClose() throws Exception {
      this.socket.close();
   }

   protected int doReadMessages(List<Object> var1) throws Exception {
      if(this.socket.isClosed()) {
         return -1;
      } else {
         try {
            Socket var2 = this.socket.accept();

            try {
               var1.add(new OioSocketChannel(this, var2));
               return 1;
            } catch (Throwable var6) {
               logger.warn("Failed to create a new channel from an accepted socket.", var6);

               try {
                  var2.close();
               } catch (Throwable var5) {
                  logger.warn("Failed to close a socket.", var5);
               }
            }
         } catch (SocketTimeoutException var7) {
            ;
         }

         return 0;
      }
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      throw new UnsupportedOperationException();
   }

   protected Object filterOutboundMessage(Object var1) throws Exception {
      throw new UnsupportedOperationException();
   }

   protected void doConnect(SocketAddress var1, SocketAddress var2) throws Exception {
      throw new UnsupportedOperationException();
   }

   protected SocketAddress remoteAddress0() {
      return null;
   }

   protected void doDisconnect() throws Exception {
      throw new UnsupportedOperationException();
   }

   protected void setReadPending(boolean var1) {
      super.setReadPending(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketAddress remoteAddress() {
      return this.remoteAddress();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketAddress localAddress() {
      return this.localAddress();
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
}
