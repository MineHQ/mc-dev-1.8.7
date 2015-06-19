package net.minecraft.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;
import net.minecraft.server.EnumProtocol;
import net.minecraft.server.EnumProtocolDirection;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketPlayOutNamedEntitySpawn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
   private static final Logger a = LogManager.getLogger();
   private static final Marker b;
   private final EnumProtocolDirection c;

   public PacketEncoder(EnumProtocolDirection var1) {
      this.c = var1;
   }

   protected void a(ChannelHandlerContext var1, Packet var2, ByteBuf var3) throws Exception {
      Integer var4 = ((EnumProtocol)var1.channel().attr(NetworkManager.c).get()).a(this.c, var2);
      if(a.isDebugEnabled()) {
         a.debug(b, "OUT: [{}:{}] {}", new Object[]{var1.channel().attr(NetworkManager.c).get(), var4, var2.getClass().getName()});
      }

      if(var4 == null) {
         throw new IOException("Can\'t serialize unregistered packet");
      } else {
         PacketDataSerializer var5 = new PacketDataSerializer(var3);
         var5.b(var4.intValue());

         try {
            if(var2 instanceof PacketPlayOutNamedEntitySpawn) {
               var2 = var2;
            }

            var2.b(var5);
         } catch (Throwable var7) {
            a.error((Object)var7);
         }

      }
   }

   // $FF: synthetic method
   protected void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) throws Exception {
      this.a(var1, (Packet)var2, var3);
   }

   static {
      b = MarkerManager.getMarker("PACKET_SENT", NetworkManager.b);
   }
}
