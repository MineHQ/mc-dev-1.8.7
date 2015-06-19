package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutBlockBreakAnimation implements Packet<PacketListenerPlayOut> {
   private int a;
   private BlockPosition b;
   private int c;

   public PacketPlayOutBlockBreakAnimation() {
   }

   public PacketPlayOutBlockBreakAnimation(int var1, BlockPosition var2, int var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      this.b = var1.c();
      this.c = var1.readUnsignedByte();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      var1.a(this.b);
      var1.writeByte(this.c);
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
