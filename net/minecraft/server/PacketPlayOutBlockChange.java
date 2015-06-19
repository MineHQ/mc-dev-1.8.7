package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;
import net.minecraft.server.World;

public class PacketPlayOutBlockChange implements Packet<PacketListenerPlayOut> {
   private BlockPosition a;
   public IBlockData block;

   public PacketPlayOutBlockChange() {
   }

   public PacketPlayOutBlockChange(World var1, BlockPosition var2) {
      this.a = var2;
      this.block = var1.getType(var2);
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.c();
      this.block = (IBlockData)Block.d.a(var1.e());
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);
      var1.b(Block.d.b(this.block));
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
