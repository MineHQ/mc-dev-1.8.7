package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutPlayerListHeaderFooter implements Packet<PacketListenerPlayOut> {
   private IChatBaseComponent a;
   private IChatBaseComponent b;

   public PacketPlayOutPlayerListHeaderFooter() {
   }

   public PacketPlayOutPlayerListHeaderFooter(IChatBaseComponent var1) {
      this.a = var1;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.d();
      this.b = var1.d();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);
      var1.a(this.b);
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
