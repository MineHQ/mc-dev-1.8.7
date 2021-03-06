package net.minecraft.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.server.Chunk;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;
import net.minecraft.server.PacketPlayOutMapChunk;

public class PacketPlayOutMapChunkBulk implements Packet<PacketListenerPlayOut> {
   private int[] a;
   private int[] b;
   private PacketPlayOutMapChunk.ChunkMap[] c;
   private boolean d;

   public PacketPlayOutMapChunkBulk() {
   }

   public PacketPlayOutMapChunkBulk(List<Chunk> var1) {
      int var2 = var1.size();
      this.a = new int[var2];
      this.b = new int[var2];
      this.c = new PacketPlayOutMapChunk.ChunkMap[var2];
      this.d = !((Chunk)var1.get(0)).getWorld().worldProvider.o();

      for(int var3 = 0; var3 < var2; ++var3) {
         Chunk var4 = (Chunk)var1.get(var3);
         PacketPlayOutMapChunk.ChunkMap var5 = PacketPlayOutMapChunk.a(var4, true, this.d, '\uffff');
         this.a[var3] = var4.locX;
         this.b[var3] = var4.locZ;
         this.c[var3] = var5;
      }

   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.d = var1.readBoolean();
      int var2 = var1.e();
      this.a = new int[var2];
      this.b = new int[var2];
      this.c = new PacketPlayOutMapChunk.ChunkMap[var2];

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         this.a[var3] = var1.readInt();
         this.b[var3] = var1.readInt();
         this.c[var3] = new PacketPlayOutMapChunk.ChunkMap();
         this.c[var3].b = var1.readShort() & '\uffff';
         this.c[var3].a = new byte[PacketPlayOutMapChunk.a(Integer.bitCount(this.c[var3].b), this.d, true)];
      }

      for(var3 = 0; var3 < var2; ++var3) {
         var1.readBytes(this.c[var3].a);
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeBoolean(this.d);
      var1.b(this.c.length);

      int var2;
      for(var2 = 0; var2 < this.a.length; ++var2) {
         var1.writeInt(this.a[var2]);
         var1.writeInt(this.b[var2]);
         var1.writeShort((short)(this.c[var2].b & '\uffff'));
      }

      for(var2 = 0; var2 < this.a.length; ++var2) {
         var1.writeBytes(this.c[var2].a);
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
