package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Entity;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutAnimation implements Packet<PacketListenerPlayOut> {
   private int a;
   private int b;

   public PacketPlayOutAnimation() {
   }

   public PacketPlayOutAnimation(Entity var1, int var2) {
      this.a = var1.getId();
      this.b = var2;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      this.b = var1.readUnsignedByte();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      var1.writeByte(this.b);
   }

   public void a(PacketListenerPlayOut var1) {
      var1.a(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayOut)var1);
   }
}
