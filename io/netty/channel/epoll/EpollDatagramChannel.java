package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AbstractChannel;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.epoll.EpollDatagramChannelConfig;
import io.netty.channel.epoll.Native;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;

public final class EpollDatagramChannel extends AbstractEpollChannel implements DatagramChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(true);
   private static final String EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(DatagramPacket.class) + ", " + StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(InetSocketAddress.class) + ">, " + StringUtil.simpleClassName(ByteBuf.class) + ')';
   private volatile InetSocketAddress local;
   private volatile InetSocketAddress remote;
   private volatile boolean connected;
   private final EpollDatagramChannelConfig config = new EpollDatagramChannelConfig(this);

   public EpollDatagramChannel() {
      super(Native.socketDgramFd(), 1);
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public boolean isActive() {
      return this.fd != -1 && (((Boolean)this.config.getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION)).booleanValue() && this.isRegistered() || this.active);
   }

   public boolean isConnected() {
      return this.connected;
   }

   public ChannelFuture joinGroup(InetAddress var1) {
      return this.joinGroup(var1, this.newPromise());
   }

   public ChannelFuture joinGroup(InetAddress var1, ChannelPromise var2) {
      try {
         return this.joinGroup(var1, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), (InetAddress)null, var2);
      } catch (SocketException var4) {
         var2.setFailure(var4);
         return var2;
      }
   }

   public ChannelFuture joinGroup(InetSocketAddress var1, NetworkInterface var2) {
      return this.joinGroup(var1, var2, this.newPromise());
   }

   public ChannelFuture joinGroup(InetSocketAddress var1, NetworkInterface var2, ChannelPromise var3) {
      return this.joinGroup(var1.getAddress(), var2, (InetAddress)null, var3);
   }

   public ChannelFuture joinGroup(InetAddress var1, NetworkInterface var2, InetAddress var3) {
      return this.joinGroup(var1, var2, var3, this.newPromise());
   }

   public ChannelFuture joinGroup(InetAddress var1, NetworkInterface var2, InetAddress var3, ChannelPromise var4) {
      if(var1 == null) {
         throw new NullPointerException("multicastAddress");
      } else if(var2 == null) {
         throw new NullPointerException("networkInterface");
      } else {
         var4.setFailure(new UnsupportedOperationException("Multicast not supported"));
         return var4;
      }
   }

   public ChannelFuture leaveGroup(InetAddress var1) {
      return this.leaveGroup(var1, this.newPromise());
   }

   public ChannelFuture leaveGroup(InetAddress var1, ChannelPromise var2) {
      try {
         return this.leaveGroup(var1, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), (InetAddress)null, var2);
      } catch (SocketException var4) {
         var2.setFailure(var4);
         return var2;
      }
   }

   public ChannelFuture leaveGroup(InetSocketAddress var1, NetworkInterface var2) {
      return this.leaveGroup(var1, var2, this.newPromise());
   }

   public ChannelFuture leaveGroup(InetSocketAddress var1, NetworkInterface var2, ChannelPromise var3) {
      return this.leaveGroup(var1.getAddress(), var2, (InetAddress)null, var3);
   }

   public ChannelFuture leaveGroup(InetAddress var1, NetworkInterface var2, InetAddress var3) {
      return this.leaveGroup(var1, var2, var3, this.newPromise());
   }

   public ChannelFuture leaveGroup(InetAddress var1, NetworkInterface var2, InetAddress var3, ChannelPromise var4) {
      if(var1 == null) {
         throw new NullPointerException("multicastAddress");
      } else if(var2 == null) {
         throw new NullPointerException("networkInterface");
      } else {
         var4.setFailure(new UnsupportedOperationException("Multicast not supported"));
         return var4;
      }
   }

   public ChannelFuture block(InetAddress var1, NetworkInterface var2, InetAddress var3) {
      return this.block(var1, var2, var3, this.newPromise());
   }

   public ChannelFuture block(InetAddress var1, NetworkInterface var2, InetAddress var3, ChannelPromise var4) {
      if(var1 == null) {
         throw new NullPointerException("multicastAddress");
      } else if(var3 == null) {
         throw new NullPointerException("sourceToBlock");
      } else if(var2 == null) {
         throw new NullPointerException("networkInterface");
      } else {
         var4.setFailure(new UnsupportedOperationException("Multicast not supported"));
         return var4;
      }
   }

   public ChannelFuture block(InetAddress var1, InetAddress var2) {
      return this.block(var1, var2, this.newPromise());
   }

   public ChannelFuture block(InetAddress var1, InetAddress var2, ChannelPromise var3) {
      try {
         return this.block(var1, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), var2, var3);
      } catch (Throwable var5) {
         var3.setFailure(var5);
         return var3;
      }
   }

   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
      return new EpollDatagramChannel.EpollDatagramChannelUnsafe();
   }

   protected InetSocketAddress localAddress0() {
      return this.local;
   }

   protected InetSocketAddress remoteAddress0() {
      return this.remote;
   }

   protected void doBind(SocketAddress var1) throws Exception {
      InetSocketAddress var2 = (InetSocketAddress)var1;
      checkResolvable(var2);
      Native.bind(this.fd, var2.getAddress(), var2.getPort());
      this.local = Native.localAddress(this.fd);
      this.active = true;
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      while(true) {
         Object var2 = var1.current();
         if(var2 == null) {
            this.clearEpollOut();
         } else {
            try {
               boolean var3 = false;

               for(int var4 = this.config().getWriteSpinCount() - 1; var4 >= 0; --var4) {
                  if(this.doWriteMessage(var2)) {
                     var3 = true;
                     break;
                  }
               }

               if(var3) {
                  var1.remove();
                  continue;
               }

               this.setEpollOut();
            } catch (IOException var5) {
               var1.remove(var5);
               continue;
            }
         }

         return;
      }
   }

   private boolean doWriteMessage(Object var1) throws IOException {
      ByteBuf var2;
      InetSocketAddress var3;
      if(var1 instanceof AddressedEnvelope) {
         AddressedEnvelope var4 = (AddressedEnvelope)var1;
         var2 = (ByteBuf)var4.content();
         var3 = (InetSocketAddress)var4.recipient();
      } else {
         var2 = (ByteBuf)var1;
         var3 = null;
      }

      int var8 = var2.readableBytes();
      if(var8 == 0) {
         return true;
      } else {
         if(var3 == null) {
            var3 = this.remote;
            if(var3 == null) {
               throw new NotYetConnectedException();
            }
         }

         int var5;
         if(var2.hasMemoryAddress()) {
            long var6 = var2.memoryAddress();
            var5 = Native.sendToAddress(this.fd, var6, var2.readerIndex(), var2.writerIndex(), var3.getAddress(), var3.getPort());
         } else {
            ByteBuffer var9 = var2.internalNioBuffer(var2.readerIndex(), var2.readableBytes());
            var5 = Native.sendTo(this.fd, var9, var9.position(), var9.limit(), var3.getAddress(), var3.getPort());
         }

         return var5 > 0;
      }
   }

   protected Object filterOutboundMessage(Object var1) {
      ByteBuf var3;
      if(var1 instanceof DatagramPacket) {
         DatagramPacket var5 = (DatagramPacket)var1;
         var3 = (ByteBuf)var5.content();
         return var3.hasMemoryAddress()?var1:new DatagramPacket(this.newDirectBuffer(var5, var3), (InetSocketAddress)var5.recipient());
      } else if(var1 instanceof ByteBuf) {
         ByteBuf var4 = (ByteBuf)var1;
         return var4.hasMemoryAddress()?var1:this.newDirectBuffer(var4);
      } else {
         if(var1 instanceof AddressedEnvelope) {
            AddressedEnvelope var2 = (AddressedEnvelope)var1;
            if(var2.content() instanceof ByteBuf && (var2.recipient() == null || var2.recipient() instanceof InetSocketAddress)) {
               var3 = (ByteBuf)var2.content();
               if(var3.hasMemoryAddress()) {
                  return var2;
               }

               return new DefaultAddressedEnvelope(this.newDirectBuffer(var2, var3), (InetSocketAddress)var2.recipient());
            }
         }

         throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(var1) + EXPECTED_TYPES);
      }
   }

   public EpollDatagramChannelConfig config() {
      return this.config;
   }

   protected void doDisconnect() throws Exception {
      this.connected = false;
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
   public DatagramChannelConfig config() {
      return this.config();
   }

   static final class DatagramSocketAddress extends InetSocketAddress {
      private static final long serialVersionUID = 1348596211215015739L;
      final int receivedAmount;

      DatagramSocketAddress(String var1, int var2, int var3) {
         super(var1, var2);
         this.receivedAmount = var3;
      }
   }

   final class EpollDatagramChannelUnsafe extends AbstractEpollChannel.AbstractEpollUnsafe {
      private RecvByteBufAllocator.Handle allocHandle;

      EpollDatagramChannelUnsafe() {
         super();
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         boolean var4 = false;

         try {
            try {
               InetSocketAddress var5 = (InetSocketAddress)var1;
               if(var2 != null) {
                  InetSocketAddress var6 = (InetSocketAddress)var2;
                  EpollDatagramChannel.this.doBind(var6);
               }

               AbstractEpollChannel.checkResolvable(var5);
               EpollDatagramChannel.this.remote = var5;
               EpollDatagramChannel.this.local = Native.localAddress(EpollDatagramChannel.this.fd);
               var4 = true;
            } finally {
               if(!var4) {
                  EpollDatagramChannel.this.doClose();
               } else {
                  var3.setSuccess();
                  EpollDatagramChannel.this.connected = true;
               }

            }
         } catch (Throwable var11) {
            var3.setFailure(var11);
         }

      }

      void epollInReady() {
         EpollDatagramChannelConfig var1 = EpollDatagramChannel.this.config();
         RecvByteBufAllocator.Handle var2 = this.allocHandle;
         if(var2 == null) {
            this.allocHandle = var2 = var1.getRecvByteBufAllocator().newHandle();
         }

         assert EpollDatagramChannel.this.eventLoop().inEventLoop();

         ChannelPipeline var3 = EpollDatagramChannel.this.pipeline();

         try {
            while(true) {
               ByteBuf var4 = null;

               try {
                  var4 = var2.allocate(var1.getAllocator());
                  int var5 = var4.writerIndex();
                  EpollDatagramChannel.DatagramSocketAddress var6;
                  if(var4.hasMemoryAddress()) {
                     var6 = Native.recvFromAddress(EpollDatagramChannel.this.fd, var4.memoryAddress(), var5, var4.capacity());
                  } else {
                     ByteBuffer var7 = var4.internalNioBuffer(var5, var4.writableBytes());
                     var6 = Native.recvFrom(EpollDatagramChannel.this.fd, var7, var7.position(), var7.limit());
                  }

                  if(var6 == null) {
                     return;
                  }

                  int var19 = var6.receivedAmount;
                  var4.writerIndex(var4.writerIndex() + var19);
                  var2.record(var19);
                  this.readPending = false;
                  var3.fireChannelRead(new DatagramPacket(var4, (InetSocketAddress)this.localAddress(), var6));
                  var4 = null;
               } catch (Throwable var16) {
                  var3.fireChannelReadComplete();
                  var3.fireExceptionCaught(var16);
               } finally {
                  if(var4 != null) {
                     var4.release();
                  }

               }
            }
         } finally {
            if(!EpollDatagramChannel.this.config().isAutoRead() && !this.readPending) {
               EpollDatagramChannel.this.clearEpollIn();
            }

         }
      }
   }
}
