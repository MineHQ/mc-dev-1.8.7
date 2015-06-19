package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketStatusInListener;

public class PacketStatusInPing implements Packet<PacketStatusInListener> {
   private long a;

   public PacketStatusInPing() {
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.readLong();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeLong(this.a);
   }

   public void a(PacketStatusInListener var1) {
      var1.a(this);
   }

   public long a() {
      return this.a;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketStatusInListener)var1);
   }
}
