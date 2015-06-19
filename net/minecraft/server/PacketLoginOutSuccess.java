package net.minecraft.server;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketLoginOutListener;

public class PacketLoginOutSuccess implements Packet<PacketLoginOutListener> {
   private GameProfile a;

   public PacketLoginOutSuccess() {
   }

   public PacketLoginOutSuccess(GameProfile var1) {
      this.a = var1;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      String var2 = var1.c(36);
      String var3 = var1.c(16);
      UUID var4 = UUID.fromString(var2);
      this.a = new GameProfile(var4, var3);
   }

   public void b(PacketDataSerializer var1) throws IOException {
      UUID var2 = this.a.getId();
      var1.a(var2 == null?"":var2.toString());
      var1.a(this.a.getName());
   }

   public void a(PacketLoginOutListener var1) {
      var1.a(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketLoginOutListener)var1);
   }
}
