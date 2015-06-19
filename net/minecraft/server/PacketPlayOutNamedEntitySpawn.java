package net.minecraft.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutNamedEntitySpawn implements Packet<PacketListenerPlayOut> {
   private int a;
   private UUID b;
   private int c;
   private int d;
   private int e;
   private byte f;
   private byte g;
   private int h;
   private DataWatcher i;
   private List<DataWatcher.WatchableObject> j;

   public PacketPlayOutNamedEntitySpawn() {
   }

   public PacketPlayOutNamedEntitySpawn(EntityHuman var1) {
      this.a = var1.getId();
      this.b = var1.getProfile().getId();
      this.c = MathHelper.floor(var1.locX * 32.0D);
      this.d = MathHelper.floor(var1.locY * 32.0D);
      this.e = MathHelper.floor(var1.locZ * 32.0D);
      this.f = (byte)((int)(var1.yaw * 256.0F / 360.0F));
      this.g = (byte)((int)(var1.pitch * 256.0F / 360.0F));
      ItemStack var2 = var1.inventory.getItemInHand();
      this.h = var2 == null?0:Item.getId(var2.getItem());
      this.i = var1.getDataWatcher();
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      this.b = var1.g();
      this.c = var1.readInt();
      this.d = var1.readInt();
      this.e = var1.readInt();
      this.f = var1.readByte();
      this.g = var1.readByte();
      this.h = var1.readShort();
      this.j = DataWatcher.b(var1);
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      var1.a(this.b);
      var1.writeInt(this.c);
      var1.writeInt(this.d);
      var1.writeInt(this.e);
      var1.writeByte(this.f);
      var1.writeByte(this.g);
      var1.writeShort(this.h);
      this.i.a(var1);
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
