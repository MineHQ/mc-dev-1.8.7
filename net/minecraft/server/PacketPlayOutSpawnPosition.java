package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutSpawnPosition implements Packet<PacketListenerPlayOut> {
   public BlockPosition position;

   public PacketPlayOutSpawnPosition() {
   }

   public PacketPlayOutSpawnPosition(BlockPosition var1) {
      this.position = var1;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.position = var1.c();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.position);
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
