package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.MobEffect;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutEntityEffect implements Packet<PacketListenerPlayOut> {
   private int a;
   private byte b;
   private byte c;
   private int d;
   private byte e;

   public PacketPlayOutEntityEffect() {
   }

   public PacketPlayOutEntityEffect(int var1, MobEffect var2) {
      this.a = var1;
      this.b = (byte)(var2.getEffectId() & 255);
      this.c = (byte)(var2.getAmplifier() & 255);
      if(var2.getDuration() > 32767) {
         this.d = 32767;
      } else {
         this.d = var2.getDuration();
      }

      this.e = (byte)(var2.isShowParticles()?1:0);
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      this.b = var1.readByte();
      this.c = var1.readByte();
      this.d = var1.e();
      this.e = var1.readByte();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      var1.writeByte(this.b);
      var1.writeByte(this.c);
      var1.b(this.d);
      var1.writeByte(this.e);
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
