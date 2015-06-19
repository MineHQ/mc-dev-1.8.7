package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutUpdateEntityNBT implements Packet<PacketListenerPlayOut> {
   private int a;
   private NBTTagCompound b;

   public PacketPlayOutUpdateEntityNBT() {
   }

   public PacketPlayOutUpdateEntityNBT(int var1, NBTTagCompound var2) {
      this.a = var1;
      this.b = var2;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      this.b = var1.h();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      var1.a(this.b);
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
