package net.minecraft.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutEntityMetadata implements Packet<PacketListenerPlayOut> {
   private int a;
   private List<DataWatcher.WatchableObject> b;

   public PacketPlayOutEntityMetadata() {
   }

   public PacketPlayOutEntityMetadata(int var1, DataWatcher var2, boolean var3) {
      this.a = var1;
      if(var3) {
         this.b = var2.c();
      } else {
         this.b = var2.b();
      }

   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      this.b = DataWatcher.b(var1);
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      DataWatcher.a(this.b, var1);
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
