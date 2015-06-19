package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutBlockAction implements Packet<PacketListenerPlayOut> {
   private BlockPosition a;
   private int b;
   private int c;
   private Block d;

   public PacketPlayOutBlockAction() {
   }

   public PacketPlayOutBlockAction(BlockPosition var1, Block var2, int var3, int var4) {
      this.a = var1;
      this.b = var3;
      this.c = var4;
      this.d = var2;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.c();
      this.b = var1.readUnsignedByte();
      this.c = var1.readUnsignedByte();
      this.d = Block.getById(var1.e() & 4095);
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);
      var1.writeByte(this.b);
      var1.writeByte(this.c);
      var1.b(Block.getId(this.d) & 4095);
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
