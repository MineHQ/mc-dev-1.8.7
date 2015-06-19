package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketStatusOutListener;

public class PacketStatusOutPong implements Packet<PacketStatusOutListener> {
   private long a;

   public PacketStatusOutPong() {
   }

   public PacketStatusOutPong(long var1) {
      this.a = var1;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.readLong();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeLong(this.a);
   }

   public void a(PacketStatusOutListener var1) {
      var1.a(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketStatusOutListener)var1);
   }
}
