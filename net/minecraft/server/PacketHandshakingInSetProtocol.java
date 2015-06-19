package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.EnumProtocol;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketHandshakingInListener;
import net.minecraft.server.PacketListener;

public class PacketHandshakingInSetProtocol implements Packet<PacketHandshakingInListener> {
   private int a;
   private String b;
   private int c;
   private EnumProtocol d;

   public PacketHandshakingInSetProtocol() {
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      this.b = var1.c(255);
      this.c = var1.readUnsignedShort();
      this.d = EnumProtocol.a(var1.e());
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      var1.a(this.b);
      var1.writeShort(this.c);
      var1.b(this.d.a());
   }

   public void a(PacketHandshakingInListener var1) {
      var1.a(this);
   }

   public EnumProtocol a() {
      return this.d;
   }

   public int b() {
      return this.a;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketHandshakingInListener)var1);
   }
}
