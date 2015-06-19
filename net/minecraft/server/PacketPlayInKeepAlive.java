package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;

public class PacketPlayInKeepAlive implements Packet<PacketListenerPlayIn> {
   private int a;

   public PacketPlayInKeepAlive() {
   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
   }

   public int a() {
      return this.a;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }
}
