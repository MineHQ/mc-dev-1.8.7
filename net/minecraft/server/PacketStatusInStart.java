package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketStatusInListener;

public class PacketStatusInStart implements Packet<PacketStatusInListener> {
   public PacketStatusInStart() {
   }

   public void a(PacketDataSerializer var1) throws IOException {
   }

   public void b(PacketDataSerializer var1) throws IOException {
   }

   public void a(PacketStatusInListener var1) {
      var1.a(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketStatusInListener)var1);
   }
}
