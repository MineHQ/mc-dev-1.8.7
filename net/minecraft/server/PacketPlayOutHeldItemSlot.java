package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutHeldItemSlot implements Packet<PacketListenerPlayOut> {
   private int a;

   public PacketPlayOutHeldItemSlot() {
   }

   public PacketPlayOutHeldItemSlot(int var1) {
      this.a = var1;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.readByte();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeByte(this.a);
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
