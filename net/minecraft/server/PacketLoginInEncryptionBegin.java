package net.minecraft.server;

import java.io.IOException;
import java.security.PrivateKey;
import javax.crypto.SecretKey;
import net.minecraft.server.MinecraftEncryption;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketLoginInListener;

public class PacketLoginInEncryptionBegin implements Packet<PacketLoginInListener> {
   private byte[] a = new byte[0];
   private byte[] b = new byte[0];

   public PacketLoginInEncryptionBegin() {
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.a();
      this.b = var1.a();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);
      var1.a(this.b);
   }

   public void a(PacketLoginInListener var1) {
      var1.a(this);
   }

   public SecretKey a(PrivateKey var1) {
      return MinecraftEncryption.a(var1, this.a);
   }

   public byte[] b(PrivateKey var1) {
      return var1 == null?this.b:MinecraftEncryption.b(var1, this.b);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketLoginInListener)var1);
   }
}
