package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;

public class PacketPlayInBlockPlace implements Packet<PacketListenerPlayIn> {
   private static final BlockPosition a = new BlockPosition(-1, -1, -1);
   private BlockPosition b;
   private int c;
   private ItemStack d;
   private float e;
   private float f;
   private float g;

   public PacketPlayInBlockPlace() {
   }

   public PacketPlayInBlockPlace(ItemStack var1) {
      this(a, 255, var1, 0.0F, 0.0F, 0.0F);
   }

   public PacketPlayInBlockPlace(BlockPosition var1, int var2, ItemStack var3, float var4, float var5, float var6) {
      this.b = var1;
      this.c = var2;
      this.d = var3 != null?var3.cloneItemStack():null;
      this.e = var4;
      this.f = var5;
      this.g = var6;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.b = var1.c();
      this.c = var1.readUnsignedByte();
      this.d = var1.i();
      this.e = (float)var1.readUnsignedByte() / 16.0F;
      this.f = (float)var1.readUnsignedByte() / 16.0F;
      this.g = (float)var1.readUnsignedByte() / 16.0F;
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.b);
      var1.writeByte(this.c);
      var1.a(this.d);
      var1.writeByte((int)(this.e * 16.0F));
      var1.writeByte((int)(this.f * 16.0F));
      var1.writeByte((int)(this.g * 16.0F));
   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   public BlockPosition a() {
      return this.b;
   }

   public int getFace() {
      return this.c;
   }

   public ItemStack getItemStack() {
      return this.d;
   }

   public float d() {
      return this.e;
   }

   public float e() {
      return this.f;
   }

   public float f() {
      return this.g;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }
}
