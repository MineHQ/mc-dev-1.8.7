package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;

public class PacketPlayInResourcePackStatus implements Packet<PacketListenerPlayIn> {
   private String a;
   private PacketPlayInResourcePackStatus.EnumResourcePackStatus b;

   public PacketPlayInResourcePackStatus() {
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.c(40);
      this.b = (PacketPlayInResourcePackStatus.EnumResourcePackStatus)var1.a(PacketPlayInResourcePackStatus.EnumResourcePackStatus.class);
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);
      var1.a((Enum)this.b);
   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }

   public static enum EnumResourcePackStatus {
      SUCCESSFULLY_LOADED,
      DECLINED,
      FAILED_DOWNLOAD,
      ACCEPTED;

      private EnumResourcePackStatus() {
      }
   }
}
