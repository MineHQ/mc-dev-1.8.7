package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutEntityEquipment implements Packet<PacketListenerPlayOut> {
   private int a;
   private int b;
   private ItemStack c;

   public PacketPlayOutEntityEquipment() {
   }

   public PacketPlayOutEntityEquipment(int var1, int var2, ItemStack var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3 == null?null:var3.cloneItemStack();
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      this.b = var1.readShort();
      this.c = var1.i();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      var1.writeShort(this.b);
      var1.a(this.c);
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
