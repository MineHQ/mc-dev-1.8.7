package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutResourcePackSend implements Packet<PacketListenerPlayOut> {
   private String a;
   private String b;

   public PacketPlayOutResourcePackSend() {
   }

   public PacketPlayOutResourcePackSend(String var1, String var2) {
      this.a = var1;
      this.b = var2;
      if(var2.length() > 40) {
         throw new IllegalArgumentException("Hash is too long (max 40, was " + var2.length() + ")");
      }
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.c(32767);
      this.b = var1.c(40);
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
