package net.minecraft.server;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.server.Entity;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;
import net.minecraft.server.WorldServer;

public class PacketPlayInSpectate implements Packet<PacketListenerPlayIn> {
   private UUID a;

   public PacketPlayInSpectate() {
   }

   public PacketPlayInSpectate(UUID var1) {
      this.a = var1;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.g();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);
   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   public Entity a(WorldServer var1) {
      return var1.getEntity(this.a);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }
}
