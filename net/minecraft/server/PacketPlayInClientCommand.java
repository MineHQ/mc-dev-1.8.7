package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;

public class PacketPlayInClientCommand implements Packet<PacketListenerPlayIn> {
   private PacketPlayInClientCommand.EnumClientCommand a;

   public PacketPlayInClientCommand() {
   }

   public PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand var1) {
      this.a = var1;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = (PacketPlayInClientCommand.EnumClientCommand)var1.a(PacketPlayInClientCommand.EnumClientCommand.class);
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a((Enum)this.a);
   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   public PacketPlayInClientCommand.EnumClientCommand a() {
      return this.a;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }

   public static enum EnumClientCommand {
      PERFORM_RESPAWN,
      REQUEST_STATS,
      OPEN_INVENTORY_ACHIEVEMENT;

      private EnumClientCommand() {
      }
   }
}
