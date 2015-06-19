package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutServerDifficulty implements Packet<PacketListenerPlayOut> {
   private EnumDifficulty a;
   private boolean b;

   public PacketPlayOutServerDifficulty() {
   }

   public PacketPlayOutServerDifficulty(EnumDifficulty var1, boolean var2) {
      this.a = var1;
      this.b = var2;
   }

   public void a(PacketListenerPlayOut var1) {
      var1.a(this);
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = EnumDifficulty.getById(var1.readUnsignedByte());
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeByte(this.a.a());
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayOut)var1);
   }
}
