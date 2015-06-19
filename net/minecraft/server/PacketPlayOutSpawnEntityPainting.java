package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityPainting;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutSpawnEntityPainting implements Packet<PacketListenerPlayOut> {
   private int a;
   private BlockPosition b;
   private EnumDirection c;
   private String d;

   public PacketPlayOutSpawnEntityPainting() {
   }

   public PacketPlayOutSpawnEntityPainting(EntityPainting var1) {
      this.a = var1.getId();
      this.b = var1.getBlockPosition();
      this.c = var1.direction;
      this.d = var1.art.B;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      this.d = var1.c(EntityPainting.EnumArt.A);
      this.b = var1.c();
      this.c = EnumDirection.fromType2(var1.readUnsignedByte());
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      var1.a(this.d);
      var1.a(this.b);
      var1.writeByte(this.c.b());
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
