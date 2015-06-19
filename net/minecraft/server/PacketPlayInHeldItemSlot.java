package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;

public class PacketPlayInHeldItemSlot implements Packet<PacketListenerPlayIn> {
   private int itemInHandIndex;

   public PacketPlayInHeldItemSlot() {
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.itemInHandIndex = var1.readShort();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeShort(this.itemInHandIndex);
   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   public int a() {
      return this.itemInHandIndex;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }
}
