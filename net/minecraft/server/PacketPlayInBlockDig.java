package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;

public class PacketPlayInBlockDig implements Packet<PacketListenerPlayIn> {
   private BlockPosition a;
   private EnumDirection b;
   private PacketPlayInBlockDig.EnumPlayerDigType c;

   public PacketPlayInBlockDig() {
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.c = (PacketPlayInBlockDig.EnumPlayerDigType)var1.a(PacketPlayInBlockDig.EnumPlayerDigType.class);
      this.a = var1.c();
      this.b = EnumDirection.fromType1(var1.readUnsignedByte());
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a((Enum)this.c);
      var1.a(this.a);
      var1.writeByte(this.b.a());
   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   public BlockPosition a() {
      return this.a;
   }

   public EnumDirection b() {
      return this.b;
   }

   public PacketPlayInBlockDig.EnumPlayerDigType c() {
      return this.c;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }

   public static enum EnumPlayerDigType {
      START_DESTROY_BLOCK,
      ABORT_DESTROY_BLOCK,
      STOP_DESTROY_BLOCK,
      DROP_ALL_ITEMS,
      DROP_ITEM,
      RELEASE_USE_ITEM;

      private EnumPlayerDigType() {
      }
   }
}
