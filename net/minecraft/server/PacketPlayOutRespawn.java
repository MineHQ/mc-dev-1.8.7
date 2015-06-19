package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;
import net.minecraft.server.WorldSettings;
import net.minecraft.server.WorldType;

public class PacketPlayOutRespawn implements Packet<PacketListenerPlayOut> {
   private int a;
   private EnumDifficulty b;
   private WorldSettings.EnumGamemode c;
   private WorldType d;

   public PacketPlayOutRespawn() {
   }

   public PacketPlayOutRespawn(int var1, EnumDifficulty var2, WorldType var3, WorldSettings.EnumGamemode var4) {
      this.a = var1;
      this.b = var2;
      this.c = var4;
      this.d = var3;
   }

   public void a(PacketListenerPlayOut var1) {
      var1.a(this);
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.readInt();
      this.b = EnumDifficulty.getById(var1.readUnsignedByte());
      this.c = WorldSettings.EnumGamemode.getById(var1.readUnsignedByte());
      this.d = WorldType.getType(var1.c(16));
      if(this.d == null) {
         this.d = WorldType.NORMAL;
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeInt(this.a);
      var1.writeByte(this.b.a());
      var1.writeByte(this.c.getId());
      var1.a(this.d.name());
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayOut)var1);
   }
}
