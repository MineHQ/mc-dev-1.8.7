package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;

public class PacketPlayInSettings implements Packet<PacketListenerPlayIn> {
   private String a;
   private int b;
   private EntityHuman.EnumChatVisibility c;
   private boolean d;
   private int e;

   public PacketPlayInSettings() {
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.c(7);
      this.b = var1.readByte();
      this.c = EntityHuman.EnumChatVisibility.a(var1.readByte());
      this.d = var1.readBoolean();
      this.e = var1.readUnsignedByte();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);
      var1.writeByte(this.b);
      var1.writeByte(this.c.a());
      var1.writeBoolean(this.d);
      var1.writeByte(this.e);
   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   public String a() {
      return this.a;
   }

   public EntityHuman.EnumChatVisibility c() {
      return this.c;
   }

   public boolean d() {
      return this.d;
   }

   public int e() {
      return this.e;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }
}
