package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutSpawnEntityExperienceOrb implements Packet<PacketListenerPlayOut> {
   private int a;
   private int b;
   private int c;
   private int d;
   private int e;

   public PacketPlayOutSpawnEntityExperienceOrb() {
   }

   public PacketPlayOutSpawnEntityExperienceOrb(EntityExperienceOrb var1) {
      this.a = var1.getId();
      this.b = MathHelper.floor(var1.locX * 32.0D);
      this.c = MathHelper.floor(var1.locY * 32.0D);
      this.d = MathHelper.floor(var1.locZ * 32.0D);
      this.e = var1.j();
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      this.b = var1.readInt();
      this.c = var1.readInt();
      this.d = var1.readInt();
      this.e = var1.readShort();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      var1.writeInt(this.b);
      var1.writeInt(this.c);
      var1.writeInt(this.d);
      var1.writeShort(this.e);
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
