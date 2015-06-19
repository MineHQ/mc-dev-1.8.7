package io.netty.channel.socket.nio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.FileRegion;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.socket.DefaultSocketChannelConfig;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.util.internal.OneTimeTask;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.spi.SelectorProvider;

public class NioSocketChannel extends AbstractNioByteChannel implements SocketChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
   private final SocketChannelConfig config;

   private static java.nio.channels.SocketChannel newSocket(SelectorProvider var0) {
      try {
         return var0.openSocketChannel();
      } catch (IOException var2) {
         throw new ChannelException("Failed to open a socket.", var2);
      }
   }

   public NioSocketChannel() {
      this(newSocket(DEFAULT_SELECTOR_PROVIDER));
   }

   public NioSocketChannel(SelectorProvider var1) {
      this(newSocket(var1));
   }

   public NioSocketChannel(java.nio.channels.SocketChannel var1) {
      this((Channel)null, var1);
   }

   public NioSocketChannel(Channel var1, java.nio.channels.SocketChannel var2) {
      super(var1, var2);
      this.config = new NioSocketChannel.NioSocketChannelConfig(this, var2.socket(), null);
   }

   public ServerSocketChannel parent() {
      return (ServerSocketChannel)super.parent();
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public SocketChannelConfig config() {
      return this.config;
   }

   protected java.nio.channels.SocketChannel javaChannel() {
      return (java.nio.channels.SocketChannel)super.javaChannel();
   }

   public boolean isActive() {
      java.nio.channels.SocketChannel var1 = this.javaChannel();
      return var1.isOpen() && var1.isConnected();
   }

   public boolean isInputShutdown() {
      return super.isInputShutdown();
   }

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   public InetSocketAddress remoteAddress() {
      return (InetSocketAddress)super.remoteAddress();
   }

   public boolean isOutputShutdown() {
      return this.javaChannel().socket().isOutputShutdown() || !this.isActive();
   }

   public ChannelFuture shutdownOutput() {
      return this.shutdownOutput(this.newPromise());
   }

   public ChannelFuture shutdownOutput(final ChannelPromise var1) {
      NioEventLoop var2 = this.eventLoop();
      if(var2.inEventLoop()) {
         try {
            this.javaChannel().socket().shutdownOutput();
            var1.setSuccess();
         } catch (Throwable var4) {
            var1.setFailure(var4);
         }
      } else {
         var2.execute(new OneTimeTask() {
            public void run() {
               NioSocketChannel.this.shutdownOutput(var1);
            }
         });
      }

      return var1;
   }

   protected SocketAddress localAddress0() {
      return this.javaChannel().socket().getLocalSocketAddress();
   }

   protected SocketAddress remoteAddress0() {
      return this.javaChannel().socket().getRemoteSocketAddress();
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.javaChannel().socket().bind(var1);
   }

   protected boolean doConnect(SocketAddress var1, SocketAddress var2) throws Exception {
      if(var2 != null) {
         this.javaChannel().socket().bind(var2);
      }

      boolean var3 = false;

      boolean var5;
      try {
         boolean var4 = this.javaChannel().connect(var1);
         if(!var4) {
            this.selectionKey().interestOps(8);
         }

         var3 = true;
         var5 = var4;
      } finally {
         if(!var3) {
            this.doClose();
         }

      }

      return var5;
   }

   protected void doFinishConnect() throws Exception {
      if(!this.javaChannel().finishConnect()) {
         throw new Error();
      }
   }

   protected void doDisconnect() throws Exception {
      this.doClose();
   }

   protected void doClose() throws Exception {
      this.javaChannel().close();
   }

   protected int doReadBytes(ByteBuf var1) throws Exception {
      return var1.writeBytes((ScatteringByteChannel)this.javaChannel(), var1.writableBytes());
   }

   protected int doWriteBytes(ByteBuf var1) throws Exception {
      int var2 = var1.readableBytes();
      return var1.readBytes((GatheringByteChannel)this.javaChannel(), var2);
   }

   protected long doWriteFileRegion(FileRegion var1) throws Exception {
      long var2 = var1.transfered();
      return var1.transferTo(this.javaChannel(), var2);
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      while(true) {
         int var2 = var1.size();
         if(var2 == 0) {
            this.clearOpWrite();
            break;
         }

         long var3;
         boolean var5;
         boolean var6;
         var3 = 0L;
         var5 = false;
         var6 = false;
         ByteBuffer[] var7 = var1.nioBuffers();
         int var8 = var1.nioBufferCount();
         long var9 = var1.nioBufferSize();
         java.nio.channels.SocketChannel var11 = this.javaChannel();
         int var13;
         label51:
         switch(var8) {
         case 0:
            super.doWrite(var1);
            return;
         case 1:
            ByteBuffer var12 = var7[0];
            var13 = this.config().getWriteSpinCount() - 1;

            while(true) {
               if(var13 < 0) {
                  break label51;
               }

               int var14 = var11.write(var12);
               if(var14 == 0) {
                  var6 = true;
                  break label51;
               }

               var9 -= (long)var14;
               var3 += (long)var14;
               if(var9 == 0L) {
                  var5 = true;
                  break label51;
               }

               --var13;
            }
         default:
            for(var13 = this.config().getWriteSpinCount() - 1; var13 >= 0; --var13) {
               long var16 = var11.write(var7, 0, var8);
               if(var16 == 0L) {
                  var6 = true;
                  break;
               }

               var9 -= var16;
               var3 += var16;
               if(var9 == 0L) {
                  var5 = true;
                  break;
               }
            }
         }

         var1.removeBytes(var3);
         if(!var5) {
            this.incompleteWrite(var6);
            break;
         }
      }

   }

   // $FF: synthetic method
   // $FF: bridge method
   protected SelectableChannel javaChannel() {
      return this.javaChannel();
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
   public Channel parent() {
      return this.parent();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelConfig config() {
      return this.config();
   }

   private final class NioSocketChannelConfig extends DefaultSocketChannelConfig {
      private NioSocketChannelConfig(NioSocketChannel var2, Socket var3) {
         super(var2, var3);
      }

      protected void autoReadCleared() {
         NioSocketChannel.this.setReadPending(false);
      }

      // $FF: synthetic method
      NioSocketChannelConfig(NioSocketChannel var2, Socket var3, Object var4) {
         this();
      }
   }
}
