package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutExperience implements Packet<PacketListenerPlayOut> {
   private float a;
   private int b;
   private int c;

   public PacketPlayOutExperience() {
   }

   public PacketPlayOutExperience(float var1, int var2, int var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.readFloat();
      this.c = var1.e();
      this.b = var1.e();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeFloat(this.a);
      var1.b(this.c);
      var1.b(this.b);
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
