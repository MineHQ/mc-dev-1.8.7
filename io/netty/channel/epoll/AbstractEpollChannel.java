package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.EventLoop;
import io.netty.channel.epoll.EpollEventLoop;
import io.netty.channel.epoll.Native;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.OneTimeTask;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.UnresolvedAddressException;

abstract class AbstractEpollChannel extends AbstractChannel {
   private static final ChannelMetadata DATA = new ChannelMetadata(false);
   private final int readFlag;
   protected int flags;
   protected volatile boolean active;
   volatile int fd;
   int id;

   AbstractEpollChannel(int var1, int var2) {
      this((Channel)null, var1, var2, false);
   }

   AbstractEpollChannel(Channel var1, int var2, int var3, boolean var4) {
      super(var1);
      this.fd = var2;
      this.readFlag = var3;
      this.flags |= var3;
      this.active = var4;
   }

   public boolean isActive() {
      return this.active;
   }

   public ChannelMetadata metadata() {
      return DATA;
   }

   protected void doClose() throws Exception {
      this.active = false;
      this.doDeregister();
      int var1 = this.fd;
      this.fd = -1;
      Native.close(var1);
   }

   public InetSocketAddress remoteAddress() {
      return (InetSocketAddress)super.remoteAddress();
   }

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   protected void doDisconnect() throws Exception {
      this.doClose();
   }

   protected boolean isCompatible(EventLoop var1) {
      return var1 instanceof EpollEventLoop;
   }

   public boolean isOpen() {
      return this.fd != -1;
   }

   protected void doDeregister() throws Exception {
      ((EpollEventLoop)this.eventLoop()).remove(this);
   }

   protected void doBeginRead() throws Exception {
      ((AbstractEpollChannel.AbstractEpollUnsafe)this.unsafe()).readPending = true;
      if((this.flags & this.readFlag) == 0) {
         this.flags |= this.readFlag;
         this.modifyEvents();
      }

   }

   final void clearEpollIn() {
      if(this.isRegistered()) {
         EventLoop var1 = this.eventLoop();
         final AbstractEpollChannel.AbstractEpollUnsafe var2 = (AbstractEpollChannel.AbstractEpollUnsafe)this.unsafe();
         if(var1.inEventLoop()) {
            var2.clearEpollIn0();
         } else {
            var1.execute(new OneTimeTask() {
               public void run() {
                  if(!AbstractEpollChannel.this.config().isAutoRead() && !var2.readPending) {
                     var2.clearEpollIn0();
                  }

               }
            });
         }
      } else {
         this.flags &= ~this.readFlag;
      }

   }

   protected final void setEpollOut() {
      if((this.flags & 2) == 0) {
         this.flags |= 2;
         this.modifyEvents();
      }

   }

   protected final void clearEpollOut() {
      if((this.flags & 2) != 0) {
         this.flags &= -3;
         this.modifyEvents();
      }

   }

   private void modifyEvents() {
      if(this.isOpen()) {
         ((EpollEventLoop)this.eventLoop()).modify(this);
      }

   }

   protected void doRegister() throws Exception {
      EpollEventLoop var1 = (EpollEventLoop)this.eventLoop();
      var1.add(this);
   }

   protected abstract AbstractEpollChannel.AbstractEpollUnsafe newUnsafe();

   protected final ByteBuf newDirectBuffer(ByteBuf var1) {
      return this.newDirectBuffer(var1, var1);
   }

   protected final ByteBuf newDirectBuffer(Object var1, ByteBuf var2) {
      int var3 = var2.readableBytes();
      if(var3 == 0) {
         ReferenceCountUtil.safeRelease(var1);
         return Unpooled.EMPTY_BUFFER;
      } else {
         ByteBufAllocator var4 = this.alloc();
         if(var4.isDirectBufferPooled()) {
            return newDirectBuffer0(var1, var2, var4, var3);
         } else {
            ByteBuf var5 = ByteBufUtil.threadLocalDirectBuffer();
            if(var5 == null) {
               return newDirectBuffer0(var1, var2, var4, var3);
            } else {
               var5.writeBytes(var2, var2.readerIndex(), var3);
               ReferenceCountUtil.safeRelease(var1);
               return var5;
            }
         }
      }
   }

   private static ByteBuf newDirectBuffer0(Object var0, ByteBuf var1, ByteBufAllocator var2, int var3) {
      ByteBuf var4 = var2.directBuffer(var3);
      var4.writeBytes(var1, var1.readerIndex(), var3);
      ReferenceCountUtil.safeRelease(var0);
      return var4;
   }

   protected static void checkResolvable(InetSocketAddress var0) {
      if(var0.isUnresolved()) {
         throw new UnresolvedAddressException();
      }
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return this.newUnsafe();
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

   protected abstract class AbstractEpollUnsafe extends AbstractChannel.AbstractUnsafe {
      protected boolean readPending;

      protected AbstractEpollUnsafe() {
         super();
      }

      abstract void epollInReady();

      void epollRdHupReady() {
      }

      protected void flush0() {
         if(!this.isFlushPending()) {
            super.flush0();
         }
      }

      void epollOutReady() {
         super.flush0();
      }

      private boolean isFlushPending() {
         return (AbstractEpollChannel.this.flags & 2) != 0;
      }

      protected final void clearEpollIn0() {
         if((AbstractEpollChannel.this.flags & AbstractEpollChannel.this.readFlag) != 0) {
            AbstractEpollChannel.this.flags &= ~AbstractEpollChannel.this.readFlag;
            AbstractEpollChannel.this.modifyEvents();
         }

      }
   }
}
