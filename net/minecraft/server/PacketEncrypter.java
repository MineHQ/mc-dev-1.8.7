package net.minecraft.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import javax.crypto.Cipher;
import net.minecraft.server.PacketEncryptionHandler;

public class PacketEncrypter extends MessageToByteEncoder<ByteBuf> {
   private final PacketEncryptionHandler a;

   public PacketEncrypter(Cipher var1) {
      this.a = new PacketEncryptionHandler(var1);
   }

   protected void a(ChannelHandlerContext var1, ByteBuf var2, ByteBuf var3) throws Exception {
      this.a.a(var2, var3);
   }

   // $FF: synthetic method
   protected void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) throws Exception {
      this.a(var1, (ByteBuf)var2, var3);
   }
}
