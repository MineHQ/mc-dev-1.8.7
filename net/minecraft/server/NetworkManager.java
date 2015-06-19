package net.minecraft.server;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.crypto.SecretKey;
import net.minecraft.server.CancelledPacketHandleException;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.EnumProtocol;
import net.minecraft.server.EnumProtocolDirection;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.LazyInitVar;
import net.minecraft.server.MinecraftEncryption;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketCompressor;
import net.minecraft.server.PacketDecompressor;
import net.minecraft.server.PacketDecrypter;
import net.minecraft.server.PacketEncrypter;
import net.minecraft.server.PacketListener;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NetworkManager extends SimpleChannelInboundHandler<Packet> {
   private static final Logger g = LogManager.getLogger();
   public static final Marker a = MarkerManager.getMarker("NETWORK");
   public static final Marker b;
   public static final AttributeKey<EnumProtocol> c;
   public static final LazyInitVar<NioEventLoopGroup> d;
   public static final LazyInitVar<EpollEventLoopGroup> e;
   public static final LazyInitVar<LocalEventLoopGroup> f;
   private final EnumProtocolDirection h;
   private final Queue<NetworkManager.QueuedPacket> i = Queues.newConcurrentLinkedQueue();
   private final ReentrantReadWriteLock j = new ReentrantReadWriteLock();
   public Channel channel;
   private SocketAddress l;
   private PacketListener m;
   private IChatBaseComponent n;
   private boolean o;
   private boolean p;

   public NetworkManager(EnumProtocolDirection var1) {
      this.h = var1;
   }

   public void channelActive(ChannelHandlerContext var1) throws Exception {
      super.channelActive(var1);
      this.channel = var1.channel();
      this.l = this.channel.remoteAddress();

      try {
         this.a(EnumProtocol.HANDSHAKING);
      } catch (Throwable var3) {
         g.fatal((Object)var3);
      }

   }

   public void a(EnumProtocol var1) {
      this.channel.attr(c).set(var1);
      this.channel.config().setAutoRead(true);
      g.debug("Enabled auto read");
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      this.close(new ChatMessage("disconnect.endOfStream", new Object[0]));
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      ChatMessage var3;
      if(var2 instanceof TimeoutException) {
         var3 = new ChatMessage("disconnect.timeout", new Object[0]);
      } else {
         var3 = new ChatMessage("disconnect.genericReason", new Object[]{"Internal Exception: " + var2});
      }

      this.close(var3);
   }

   protected void a(ChannelHandlerContext var1, Packet var2) throws Exception {
      if(this.channel.isOpen()) {
         try {
            var2.a(this.m);
         } catch (CancelledPacketHandleException var4) {
            ;
         }
      }

   }

   public void a(PacketListener var1) {
      Validate.notNull(var1, "packetListener", new Object[0]);
      g.debug("Set listener of {} to {}", new Object[]{this, var1});
      this.m = var1;
   }

   public void handle(Packet var1) {
      if(this.g()) {
         this.m();
         this.a((Packet)var1, (GenericFutureListener[])null);
      } else {
         this.j.writeLock().lock();

         try {
            this.i.add(new NetworkManager.QueuedPacket(var1, (GenericFutureListener[])null));
         } finally {
            this.j.writeLock().unlock();
         }
      }

   }

   public void a(Packet var1, GenericFutureListener<? extends Future<? super Void>> var2, GenericFutureListener... var3) {
      if(this.g()) {
         this.m();
         this.a(var1, (GenericFutureListener[])ArrayUtils.add(var3, 0, var2));
      } else {
         this.j.writeLock().lock();

         try {
            this.i.add(new NetworkManager.QueuedPacket(var1, (GenericFutureListener[])ArrayUtils.add(var3, 0, var2)));
         } finally {
            this.j.writeLock().unlock();
         }
      }

   }

   private void a(final Packet var1, final GenericFutureListener<? extends Future<? super Void>>[] var2) {
      final EnumProtocol var3 = EnumProtocol.a(var1);
      final EnumProtocol var4 = (EnumProtocol)this.channel.attr(c).get();
      if(var4 != var3) {
         g.debug("Disabled auto read");
         this.channel.config().setAutoRead(false);
      }

      if(this.channel.eventLoop().inEventLoop()) {
         if(var3 != var4) {
            this.a(var3);
         }

         ChannelFuture var5 = this.channel.writeAndFlush(var1);
         if(var2 != null) {
            var5.addListeners(var2);
         }

         var5.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
      } else {
         this.channel.eventLoop().execute(new Runnable() {
            public void run() {
               if(var3 != var4) {
                  NetworkManager.this.a(var3);
               }

               ChannelFuture var1x = NetworkManager.this.channel.writeAndFlush(var1);
               if(var2 != null) {
                  var1x.addListeners(var2);
               }

               var1x.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            }
         });
      }

   }

   private void m() {
      if(this.channel != null && this.channel.isOpen()) {
         this.j.readLock().lock();

         try {
            while(!this.i.isEmpty()) {
               NetworkManager.QueuedPacket var1 = (NetworkManager.QueuedPacket)this.i.poll();
               this.a(var1.a, var1.b);
            }
         } finally {
            this.j.readLock().unlock();
         }

      }
   }

   public void a() {
      this.m();
      if(this.m instanceof IUpdatePlayerListBox) {
         ((IUpdatePlayerListBox)this.m).c();
      }

      this.channel.flush();
   }

   public SocketAddress getSocketAddress() {
      return this.l;
   }

   public void close(IChatBaseComponent var1) {
      if(this.channel.isOpen()) {
         this.channel.close().awaitUninterruptibly();
         this.n = var1;
      }

   }

   public boolean c() {
      return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
   }

   public void a(SecretKey var1) {
      this.o = true;
      this.channel.pipeline().addBefore("splitter", "decrypt", new PacketDecrypter(MinecraftEncryption.a(2, var1)));
      this.channel.pipeline().addBefore("prepender", "encrypt", new PacketEncrypter(MinecraftEncryption.a(1, var1)));
   }

   public boolean g() {
      return this.channel != null && this.channel.isOpen();
   }

   public boolean h() {
      return this.channel == null;
   }

   public PacketListener getPacketListener() {
      return this.m;
   }

   public IChatBaseComponent j() {
      return this.n;
   }

   public void k() {
      this.channel.config().setAutoRead(false);
   }

   public void a(int var1) {
      if(var1 >= 0) {
         if(this.channel.pipeline().get("decompress") instanceof PacketDecompressor) {
            ((PacketDecompressor)this.channel.pipeline().get("decompress")).a(var1);
         } else {
            this.channel.pipeline().addBefore("decoder", "decompress", new PacketDecompressor(var1));
         }

         if(this.channel.pipeline().get("compress") instanceof PacketCompressor) {
            ((PacketCompressor)this.channel.pipeline().get("decompress")).a(var1);
         } else {
            this.channel.pipeline().addBefore("encoder", "compress", new PacketCompressor(var1));
         }
      } else {
         if(this.channel.pipeline().get("decompress") instanceof PacketDecompressor) {
            this.channel.pipeline().remove("decompress");
         }

         if(this.channel.pipeline().get("compress") instanceof PacketCompressor) {
            this.channel.pipeline().remove("compress");
         }
      }

   }

   public void l() {
      if(this.channel != null && !this.channel.isOpen()) {
         if(!this.p) {
            this.p = true;
            if(this.j() != null) {
               this.getPacketListener().a(this.j());
            } else if(this.getPacketListener() != null) {
               this.getPacketListener().a(new ChatComponentText("Disconnected"));
            }
         } else {
            g.warn("handleDisconnection() called twice");
         }

      }
   }

   // $FF: synthetic method
   protected void channelRead0(ChannelHandlerContext var1, Object var2) throws Exception {
      this.a(var1, (Packet)var2);
   }

   static {
      b = MarkerManager.getMarker("NETWORK_PACKETS", a);
      c = AttributeKey.valueOf("protocol");
      d = new LazyInitVar() {
         protected NioEventLoopGroup a() {
            return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
         }

         // $FF: synthetic method
         protected Object init() {
            return this.a();
         }
      };
      e = new LazyInitVar() {
         protected EpollEventLoopGroup a() {
            return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
         }

         // $FF: synthetic method
         protected Object init() {
            return this.a();
         }
      };
      f = new LazyInitVar() {
         protected LocalEventLoopGroup a() {
            return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
         }

         // $FF: synthetic method
         protected Object init() {
            return this.a();
         }
      };
   }

   static class QueuedPacket {
      private final Packet a;
      private final GenericFutureListener<? extends Future<? super Void>>[] b;

      public QueuedPacket(Packet var1, GenericFutureListener... var2) {
         this.a = var1;
         this.b = var2;
      }
   }
}
