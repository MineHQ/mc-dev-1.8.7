package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;

public class PacketPlayInSetCreativeSlot implements Packet<PacketListenerPlayIn> {
   private int slot;
   private ItemStack b;

   public PacketPlayInSetCreativeSlot() {
   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.slot = var1.readShort();
      this.b = var1.i();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeShort(this.slot);
      var1.a(this.b);
   }

   public int a() {
      return this.slot;
   }

   public ItemStack getItemStack() {
      return this.b;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }
}
