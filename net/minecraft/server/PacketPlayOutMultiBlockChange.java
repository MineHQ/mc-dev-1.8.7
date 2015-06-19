package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutMultiBlockChange implements Packet<PacketListenerPlayOut> {
   private ChunkCoordIntPair a;
   private PacketPlayOutMultiBlockChange.MultiBlockChangeInfo[] b;

   public PacketPlayOutMultiBlockChange() {
   }

   public PacketPlayOutMultiBlockChange(int var1, short[] var2, Chunk var3) {
      this.a = new ChunkCoordIntPair(var3.locX, var3.locZ);
      this.b = new PacketPlayOutMultiBlockChange.MultiBlockChangeInfo[var1];

      for(int var4 = 0; var4 < this.b.length; ++var4) {
         this.b[var4] = new PacketPlayOutMultiBlockChange.MultiBlockChangeInfo(var2[var4], var3);
      }

   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = new ChunkCoordIntPair(var1.readInt(), var1.readInt());
      this.b = new PacketPlayOutMultiBlockChange.MultiBlockChangeInfo[var1.e()];

      for(int var2 = 0; var2 < this.b.length; ++var2) {
         this.b[var2] = new PacketPlayOutMultiBlockChange.MultiBlockChangeInfo(var1.readShort(), (IBlockData)Block.d.a(var1.e()));
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeInt(this.a.x);
      var1.writeInt(this.a.z);
      var1.b(this.b.length);
      PacketPlayOutMultiBlockChange.MultiBlockChangeInfo[] var2 = this.b;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PacketPlayOutMultiBlockChange.MultiBlockChangeInfo var5 = var2[var4];
         var1.writeShort(var5.b());
         var1.b(Block.d.b(var5.c()));
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

   public class MultiBlockChangeInfo {
      private final short b;
      private final IBlockData c;

      public MultiBlockChangeInfo(short var2, IBlockData var3) {
         this.b = var2;
         this.c = var3;
      }

      public MultiBlockChangeInfo(short var2, Chunk var3) {
         this.b = var2;
         this.c = var3.getBlockData(this.a());
      }

      public BlockPosition a() {
         return new BlockPosition(PacketPlayOutMultiBlockChange.this.a.a(this.b >> 12 & 15, this.b & 255, this.b >> 8 & 15));
      }

      public short b() {
         return this.b;
      }

      public IBlockData c() {
         return this.c;
      }
   }
}