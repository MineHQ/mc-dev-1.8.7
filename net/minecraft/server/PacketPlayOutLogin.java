package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;
import net.minecraft.server.WorldSettings;
import net.minecraft.server.WorldType;

public class PacketPlayOutLogin implements Packet<PacketListenerPlayOut> {
   private int a;
   private boolean b;
   private WorldSettings.EnumGamemode c;
   private int d;
   private EnumDifficulty e;
   private int f;
   private WorldType g;
   private boolean h;

   public PacketPlayOutLogin() {
   }

   public PacketPlayOutLogin(int var1, WorldSettings.EnumGamemode var2, boolean var3, int var4, EnumDifficulty var5, int var6, WorldType var7, boolean var8) {
      this.a = var1;
      this.d = var4;
      this.e = var5;
      this.c = var2;
      this.f = var6;
      this.b = var3;
      this.g = var7;
      this.h = var8;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.readInt();
      short var2 = var1.readUnsignedByte();
      this.b = (var2 & 8) == 8;
      int var3 = var2 & -9;
      this.c = WorldSettings.EnumGamemode.getById(var3);
      this.d = var1.readByte();
      this.e = EnumDifficulty.getById(var1.readUnsignedByte());
      this.f = var1.readUnsignedByte();
      this.g = WorldType.getType(var1.c(16));
      if(this.g == null) {
         this.g = WorldType.NORMAL;
      }

      this.h = var1.readBoolean();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeInt(this.a);
      int var2 = this.c.getId();
      if(this.b) {
         var2 |= 8;
      }

      var1.writeByte(var2);
      var1.writeByte(this.d);
      var1.writeByte(this.e.a());
      var1.writeByte(this.f);
      var1.a(this.g.name());
      var1.writeBoolean(this.h);
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
