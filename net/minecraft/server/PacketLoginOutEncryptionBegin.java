package net.minecraft.server;

import java.io.IOException;
import java.security.PublicKey;
import net.minecraft.server.MinecraftEncryption;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketLoginOutListener;

public class PacketLoginOutEncryptionBegin implements Packet<PacketLoginOutListener> {
   private String a;
   private PublicKey b;
   private byte[] c;

   public PacketLoginOutEncryptionBegin() {
   }

   public PacketLoginOutEncryptionBegin(String var1, PublicKey var2, byte[] var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.c(20);
      this.b = MinecraftEncryption.a(var1.a());
      this.c = var1.a();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);
      var1.a(this.b.getEncoded());
      var1.a(this.c);
   }

   public void a(PacketLoginOutListener var1) {
      var1.a(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketLoginOutListener)var1);
   }
}
