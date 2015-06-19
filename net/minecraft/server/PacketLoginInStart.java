package net.minecraft.server;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketLoginInListener;

public class PacketLoginInStart implements Packet<PacketLoginInListener> {
   private GameProfile a;

   public PacketLoginInStart() {
   }

   public PacketLoginInStart(GameProfile var1) {
      this.a = var1;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = new GameProfile((UUID)null, var1.c(16));
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a.getName());
   }

   public void a(PacketLoginInListener var1) {
      var1.a(this);
   }

   public GameProfile a() {
      return this.a;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketLoginInListener)var1);
   }
}
