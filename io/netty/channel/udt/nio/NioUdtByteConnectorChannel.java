package io.netty.channel.udt.nio;

import com.barchart.udt.StatusUDT;
import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.FileRegion;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.channel.udt.DefaultUdtChannelConfig;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.UdtChannelConfig;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SelectableChannel;

public class NioUdtByteConnectorChannel extends AbstractNioByteChannel implements UdtChannel {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioUdtByteConnectorChannel.class);
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   private final UdtChannelConfig config;

   public NioUdtByteConnectorChannel() {
      this(TypeUDT.STREAM);
   }

   public NioUdtByteConnectorChannel(Channel var1, SocketChannelUDT var2) {
      super(var1, var2);

      try {
         var2.configureBlocking(false);
         switch(NioUdtByteConnectorChannel.SyntheticClass_1.$SwitchMap$com$barchart$udt$StatusUDT[var2.socketUDT().status().ordinal()]) {
         case 1:
         case 2:
            this.config = new DefaultUdtChannelConfig(this, var2, true);
            break;
         default:
            this.config = new DefaultUdtChannelConfig(this, var2, false);
         }

      } catch (Exception var6) {
         try {
            var2.close();
         } catch (Exception var5) {
            if(logger.isWarnEnabled()) {
               logger.warn("Failed to close channel.", (Throwable)var5);
            }
         }

         throw new ChannelException("Failed to configure channel.", var6);
      }
   }

   public NioUdtByteConnectorChannel(SocketChannelUDT var1) {
      this((Channel)null, var1);
   }

   public NioUdtByteConnectorChannel(TypeUDT var1) {
      this(NioUdtProvider.newConnectorChannelUDT(var1));
   }

   public UdtChannelConfig config() {
      return this.config;
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.javaChannel().bind(var1);
   }

   protected void doClose() throws Exception {
      this.javaChannel().close();
   }

   protected boolean doConnect(SocketAddress var1, SocketAddress var2) throws Exception {
      this.doBind((SocketAddress)(var2 != null?var2:new InetSocketAddress(0)));
      boolean var3 = false;

      boolean var5;
      try {
         boolean var4 = this.javaChannel().connect(var1);
         if(!var4) {
            this.selectionKey().interestOps(this.selectionKey().interestOps() | 8);
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

   protected void doDisconnect() throws Exception {
      this.doClose();
   }

   protected void doFinishConnect() throws Exception {
      if(this.javaChannel().finishConnect()) {
         this.selectionKey().interestOps(this.selectionKey().interestOps() & -9);
      } else {
         throw new Error("Provider error: failed to finish connect. Provider library should be upgraded.");
      }
   }

   protected int doReadBytes(ByteBuf var1) throws Exception {
      return var1.writeBytes((ScatteringByteChannel)this.javaChannel(), var1.writableBytes());
   }

   protected int doWriteBytes(ByteBuf var1) throws Exception {
      int var2 = var1.readableBytes();
      return var1.readBytes((GatheringByteChannel)this.javaChannel(), var2);
   }

   protected long doWriteFileRegion(FileRegion var1) throws Exception {
      throw new UnsupportedOperationException();
   }

   public boolean isActive() {
      SocketChannelUDT var1 = this.javaChannel();
      return var1.isOpen() && var1.isConnectFinished();
   }

   protected SocketChannelUDT javaChannel() {
      return (SocketChannelUDT)super.javaChannel();
   }

   protected SocketAddress localAddress0() {
      return this.javaChannel().socket().getLocalSocketAddress();
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   protected SocketAddress remoteAddress0() {
      return this.javaChannel().socket().getRemoteSocketAddress();
   }

   public InetSocketAddress localAddress() {
      return (InetSocketAddress)super.localAddress();
   }

   public InetSocketAddress remoteAddress() {
      return (InetSocketAddress)super.remoteAddress();
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
   public ChannelConfig config() {
      return this.config();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$com$barchart$udt$StatusUDT = new int[StatusUDT.values().length];

      static {
         try {
            $SwitchMap$com$barchart$udt$StatusUDT[StatusUDT.INIT.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$barchart$udt$StatusUDT[StatusUDT.OPENED.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
