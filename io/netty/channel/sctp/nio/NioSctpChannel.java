package io.netty.channel.sctp.nio;

import com.sun.nio.sctp.Association;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.NotificationHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.sctp.DefaultSctpChannelConfig;
import io.netty.channel.sctp.SctpChannel;
import io.netty.channel.sctp.SctpChannelConfig;
import io.netty.channel.sctp.SctpMessage;
import io.netty.channel.sctp.SctpNotificationHandler;
import io.netty.channel.sctp.SctpServerChannel;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class NioSctpChannel extends AbstractNioMessageChannel implements SctpChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioSctpChannel.class);
   private final SctpChannelConfig config;
   private final NotificationHandler<?> notificationHandler;
   private RecvByteBufAllocator.Handle allocHandle;

   private static com.sun.nio.sctp.SctpChannel newSctpChannel() {
      try {
         return com.sun.nio.sctp.SctpChannel.open();
      } catch (IOException var1) {
         throw new ChannelException("Failed to open a sctp channel.", var1);
      }
   }

   public NioSctpChannel() {
      this(newSctpChannel());
   }

   public NioSctpChannel(com.sun.nio.sctp.SctpChannel var1) {
      this((Channel)null, var1);
   }

   public NioSctpChannel(Channel var1, com.sun.nio.sctp.SctpChannel var2) {
      super(var1, var2, 1);

      try {
         var2.configureBlocking(false);
         this.config = new NioSctpChannel.NioSctpChannelConfig(this, var2, null);
         this.notificationHandler = new SctpNotificationHandler(this);
      } catch (IOException var6) {
         try {
            var2.close();
         } catch (IOException var5) {
            if(logger.isWarnEnabled()) {
               logger.warn("Failed to close a partially initialized sctp channel.", (Throwable)var5);
            }
         }

         throw new ChannelException("Failed to enter non-blocking mode.", var6);
      }
   }

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   public InetSocketAddress remoteAddress() {
      return (InetSocketAddress)super.remoteAddress();
   }

   public SctpServerChannel parent() {
      return (SctpServerChannel)super.parent();
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public Association association() {
      try {
         return this.javaChannel().association();
      } catch (IOException var2) {
         return null;
      }
   }

   public Set<InetSocketAddress> allLocalAddresses() {
      try {
         Set var1 = this.javaChannel().getAllLocalAddresses();
         LinkedHashSet var2 = new LinkedHashSet(var1.size());
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            SocketAddress var4 = (SocketAddress)var3.next();
            var2.add((InetSocketAddress)var4);
         }

         return var2;
      } catch (Throwable var5) {
         return Collections.emptySet();
      }
   }

   public SctpChannelConfig config() {
      return this.config;
   }

   public Set<InetSocketAddress> allRemoteAddresses() {
      try {
         Set var1 = this.javaChannel().getRemoteAddresses();
         HashSet var2 = new HashSet(var1.size());
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            SocketAddress var4 = (SocketAddress)var3.next();
            var2.add((InetSocketAddress)var4);
         }

         return var2;
      } catch (Throwable var5) {
         return Collections.emptySet();
      }
   }

   protected com.sun.nio.sctp.SctpChannel javaChannel() {
      return (com.sun.nio.sctp.SctpChannel)super.javaChannel();
   }

   public boolean isActive() {
      com.sun.nio.sctp.SctpChannel var1 = this.javaChannel();
      return var1.isOpen() && this.association() != null;
   }

   protected SocketAddress localAddress0() {
      try {
         Iterator var1 = this.javaChannel().getAllLocalAddresses().iterator();
         if(var1.hasNext()) {
            return (SocketAddress)var1.next();
         }
      } catch (IOException var2) {
         ;
      }

      return null;
   }

   protected SocketAddress remoteAddress0() {
      try {
         Iterator var1 = this.javaChannel().getRemoteAddresses().iterator();
         if(var1.hasNext()) {
            return (SocketAddress)var1.next();
         }
      } catch (IOException var2) {
         ;
      }

      return null;
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.javaChannel().bind(var1);
   }

   protected boolean doConnect(SocketAddress var1, SocketAddress var2) throws Exception {
      if(var2 != null) {
         this.javaChannel().bind(var2);
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

   protected int doReadMessages(List<Object> var1) throws Exception {
      com.sun.nio.sctp.SctpChannel var2 = this.javaChannel();
      RecvByteBufAllocator.Handle var3 = this.allocHandle;
      if(var3 == null) {
         this.allocHandle = var3 = this.config().getRecvByteBufAllocator().newHandle();
      }

      ByteBuf var4 = var3.allocate(this.config().getAllocator());
      boolean var5 = true;
      boolean var15 = false;

      byte var7;
      label105: {
         byte var9;
         int var10;
         label106: {
            try {
               var15 = true;
               ByteBuffer var6 = var4.internalNioBuffer(var4.writerIndex(), var4.writableBytes());
               int var18 = var6.position();
               MessageInfo var8 = var2.receive(var6, (Object)null, this.notificationHandler);
               if(var8 == null) {
                  var9 = 0;
                  var15 = false;
                  break label106;
               }

               var1.add(new SctpMessage(var8, var4.writerIndex(var4.writerIndex() + var6.position() - var18)));
               var5 = false;
               var9 = 1;
               var15 = false;
            } catch (Throwable var16) {
               PlatformDependent.throwException(var16);
               var7 = -1;
               var15 = false;
               break label105;
            } finally {
               if(var15) {
                  int var12 = var4.readableBytes();
                  var3.record(var12);
                  if(var5) {
                     var4.release();
                  }

               }
            }

            var10 = var4.readableBytes();
            var3.record(var10);
            if(var5) {
               var4.release();
            }

            return var9;
         }

         var10 = var4.readableBytes();
         var3.record(var10);
         if(var5) {
            var4.release();
         }

         return var9;
      }

      int var19 = var4.readableBytes();
      var3.record(var19);
      if(var5) {
         var4.release();
      }

      return var7;
   }

   protected boolean doWriteMessage(Object var1, ChannelOutboundBuffer var2) throws Exception {
      SctpMessage var3 = (SctpMessage)var1;
      ByteBuf var4 = var3.content();
      int var5 = var4.readableBytes();
      if(var5 == 0) {
         return true;
      } else {
         ByteBufAllocator var6 = this.alloc();
         boolean var7 = var4.nioBufferCount() != 1;
         if(!var7 && !var4.isDirect() && var6.isDirectBufferPooled()) {
            var7 = true;
         }

         ByteBuffer var8;
         if(!var7) {
            var8 = var4.nioBuffer();
         } else {
            var4 = var6.directBuffer(var5).writeBytes(var4);
            var8 = var4.nioBuffer();
         }

         MessageInfo var9 = MessageInfo.createOutgoing(this.association(), (SocketAddress)null, var3.streamIdentifier());
         var9.payloadProtocolID(var3.protocolIdentifier());
         var9.streamNumber(var3.streamIdentifier());
         int var10 = this.javaChannel().send(var8, var9);
         return var10 > 0;
      }
   }

   protected final Object filterOutboundMessage(Object var1) throws Exception {
      if(var1 instanceof SctpMessage) {
         SctpMessage var2 = (SctpMessage)var1;
         ByteBuf var3 = var2.content();
         return var3.isDirect() && var3.nioBufferCount() == 1?var2:new SctpMessage(var2.protocolIdentifier(), var2.streamIdentifier(), this.newDirectBuffer(var2, var3));
      } else {
         throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(var1) + " (expected: " + StringUtil.simpleClassName(SctpMessage.class));
      }
   }

   public ChannelFuture bindAddress(InetAddress var1) {
      return this.bindAddress(var1, this.newPromise());
   }

   public ChannelFuture bindAddress(final InetAddress var1, final ChannelPromise var2) {
      if(this.eventLoop().inEventLoop()) {
         try {
            this.javaChannel().bindAddress(var1);
            var2.setSuccess();
         } catch (Throwable var4) {
            var2.setFailure(var4);
         }
      } else {
         this.eventLoop().execute(new Runnable() {
            public void run() {
               NioSctpChannel.this.bindAddress(var1, var2);
            }
         });
      }

      return var2;
   }

   public ChannelFuture unbindAddress(InetAddress var1) {
      return this.unbindAddress(var1, this.newPromise());
   }

   public ChannelFuture unbindAddress(final InetAddress var1, final ChannelPromise var2) {
      if(this.eventLoop().inEventLoop()) {
         try {
            this.javaChannel().unbindAddress(var1);
            var2.setSuccess();
         } catch (Throwable var4) {
            var2.setFailure(var4);
         }
      } else {
         this.eventLoop().execute(new Runnable() {
            public void run() {
               NioSctpChannel.this.unbindAddress(var1, var2);
            }
         });
      }

      return var2;
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

   private final class NioSctpChannelConfig extends DefaultSctpChannelConfig {
      private NioSctpChannelConfig(NioSctpChannel var2, com.sun.nio.sctp.SctpChannel var3) {
         super(var2, var3);
      }

      protected void autoReadCleared() {
         NioSctpChannel.this.setReadPending(false);
      }

      // $FF: synthetic method
      NioSctpChannelConfig(NioSctpChannel var2, com.sun.nio.sctp.SctpChannel var3, Object var4) {
         this();
      }
   }
}
