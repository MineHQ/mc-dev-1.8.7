package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutChat implements Packet<PacketListenerPlayOut> {
   private IChatBaseComponent a;
   private byte b;

   public PacketPlayOutChat() {
   }

   public PacketPlayOutChat(IChatBaseComponent var1) {
      this(var1, (byte)1);
   }

   public PacketPlayOutChat(IChatBaseComponent var1, byte var2) {
      this.a = var1;
      this.b = var2;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.d();
      this.b = var1.readByte();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);
      var1.writeByte(this.b);
   }

   public void a(PacketListenerPlayOut var1) {
      var1.a(this);
   }

   public boolean b() {
      return this.b == 1 || this.b == 2;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayOut)var1);
   }
}
