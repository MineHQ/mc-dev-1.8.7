package net.minecraft.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutWindowItems implements Packet<PacketListenerPlayOut> {
   private int a;
   private ItemStack[] b;

   public PacketPlayOutWindowItems() {
   }

   public PacketPlayOutWindowItems(int var1, List<ItemStack> var2) {
      this.a = var1;
      this.b = new ItemStack[var2.size()];

      for(int var3 = 0; var3 < this.b.length; ++var3) {
         ItemStack var4 = (ItemStack)var2.get(var3);
         this.b[var3] = var4 == null?null:var4.cloneItemStack();
      }

   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.readUnsignedByte();
      short var2 = var1.readShort();
      this.b = new ItemStack[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.b[var3] = var1.i();
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeByte(this.a);
      var1.writeShort(this.b.length);
      ItemStack[] var2 = this.b;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack var5 = var2[var4];
         var1.a(var5);
      }

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
