package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.EnumProtocolDirection;
import net.minecraft.server.HandshakeListener;
import net.minecraft.server.LazyInitVar;
import net.minecraft.server.LegacyPingHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.PacketDecoder;
import net.minecraft.server.PacketEncoder;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketPlayOutKickDisconnect;
import net.minecraft.server.PacketPrepender;
import net.minecraft.server.PacketSplitter;
import net.minecraft.server.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerConnection {
   private static final Logger e = LogManager.getLogger();
   public static final LazyInitVar<NioEventLoopGroup> a = new LazyInitVar() {
      protected NioEventLoopGroup a() {
         return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Server IO #%d").setDaemon(true).build());
      }

      // $FF: synthetic method
      protected Object init() {
         return this.a();
      }
   };
   public static final LazyInitVar<EpollEventLoopGroup> b = new LazyInitVar() {
      protected EpollEventLoopGroup a() {
         return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
      }

      // $FF: synthetic method
      protected Object init() {
         return this.a();
      }
   };
   public static final LazyInitVar<LocalEventLoopGroup> c = new LazyInitVar() {
      protected LocalEventLoopGroup a() {
         return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
      }

      // $FF: synthetic method
      protected Object init() {
         return this.a();
      }
   };
   private final MinecraftServer f;
   public volatile boolean d;
   private final List<ChannelFuture> g = Collections.synchronizedList(Lists.newArrayList());
   private final List<NetworkManager> h = Collections.synchronizedList(Lists.newArrayList());

   public ServerConnection(MinecraftServer var1) {
      this.f = var1;
      this.d = true;
   }

   public void a(InetAddress var1, int var2) throws IOException {
      List var3 = this.g;
      synchronized(this.g) {
         Class var4;
         LazyInitVar var5;
         if(Epoll.isAvailable() && this.f.ai()) {
            var4 = EpollServerSocketChannel.class;
            var5 = b;
            e.info("Using epoll channel type");
         } else {
            var4 = NioServerSocketChannel.class;
            var5 = a;
            e.info("Using default channel type");
         }

         this.g.add(((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).channel(var4)).childHandler(new ChannelInitializer() {
            protected void initChannel(Channel var1) throws Exception {
               try {
                  var1.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
               } catch (ChannelException var3) {
                  ;
               }

               var1.pipeline().addLast((String)"timeout", (ChannelHandler)(new ReadTimeoutHandler(30))).addLast((String)"legacy_query", (ChannelHandler)(new LegacyPingHandler(ServerConnection.this))).addLast((String)"splitter", (ChannelHandler)(new PacketSplitter())).addLast((String)"decoder", (ChannelHandler)(new PacketDecoder(EnumProtocolDirection.SERVERBOUND))).addLast((String)"prepender", (ChannelHandler)(new PacketPrepender())).addLast((String)"encoder", (ChannelHandler)(new PacketEncoder(EnumProtocolDirection.CLIENTBOUND)));
               NetworkManager var2 = new NetworkManager(EnumProtocolDirection.SERVERBOUND);
               ServerConnection.this.h.add(var2);
               var1.pipeline().addLast((String)"packet_handler", (ChannelHandler)var2);
               var2.a((PacketListener)(new HandshakeListener(ServerConnection.this.f, var2)));
            }
         }).group((EventLoopGroup)var5.c()).localAddress(var1, var2)).bind().syncUninterruptibly());
      }
   }

   public void b() {
      this.d = false;
      Iterator var1 = this.g.iterator();

      while(var1.hasNext()) {
         ChannelFuture var2 = (ChannelFuture)var1.next();

         try {
            var2.channel().close().sync();
         } catch (InterruptedException var4) {
            e.error("Interrupted whilst closing channel");
         }
      }

   }

   public void c() {
      List var1 = this.h;
      synchronized(this.h) {
         Iterator var2 = this.h.iterator();

         while(true) {
            while(true) {
               final NetworkManager var3;
               do {
                  if(!var2.hasNext()) {
                     return;
                  }

                  var3 = (NetworkManager)var2.next();
               } while(var3.h());

               if(!var3.g()) {
                  var2.remove();
                  var3.l();
               } else {
                  try {
                     var3.a();
                  } catch (Exception var8) {
                     if(var3.c()) {
                        CrashReport var10 = CrashReport.a(var8, "Ticking memory connection");
                        CrashReportSystemDetails var6 = var10.a("Ticking connection");
                        var6.a("Connection", new Callable() {
                           public String a() throws Exception {
                              return var3.toString();
                           }

                           // $FF: synthetic method
                           public Object call() throws Exception {
                              return this.a();
                           }
                        });
                        throw new ReportedException(var10);
                     }

                     e.warn((String)("Failed to handle packet for " + var3.getSocketAddress()), (Throwable)var8);
                     final ChatComponentText var5 = new ChatComponentText("Internal server error");
                     var3.a(new PacketPlayOutKickDisconnect(var5), new GenericFutureListener() {
                        public void operationComplete(Future<? super Void> var1) throws Exception {
                           var3.close(var5);
                        }
                     }, new GenericFutureListener[0]);
                     var3.k();
                  }
               }
            }
         }
      }
   }

   public MinecraftServer d() {
      return this.f;
   }
}
