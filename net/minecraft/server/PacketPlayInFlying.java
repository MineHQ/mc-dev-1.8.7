package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;

public class PacketPlayInFlying implements Packet<PacketListenerPlayIn> {
   protected double x;
   protected double y;
   protected double z;
   protected float yaw;
   protected float pitch;
   protected boolean f;
   protected boolean hasPos;
   protected boolean hasLook;

   public PacketPlayInFlying() {
   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.f = var1.readUnsignedByte() != 0;
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.writeByte(this.f?1:0);
   }

   public double a() {
      return this.x;
   }

   public double b() {
      return this.y;
   }

   public double c() {
      return this.z;
   }

   public float d() {
      return this.yaw;
   }

   public float e() {
      return this.pitch;
   }

   public boolean f() {
      return this.f;
   }

   public boolean g() {
      return this.hasPos;
   }

   public boolean h() {
      return this.hasLook;
   }

   public void a(boolean var1) {
      this.hasPos = var1;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }

   public static class PacketPlayInLook extends PacketPlayInFlying {
      public PacketPlayInLook() {
         this.hasLook = true;
      }

      public void a(PacketDataSerializer var1) throws IOException {
         this.yaw = var1.readFloat();
         this.pitch = var1.readFloat();
         super.a(var1);
      }

      public void b(PacketDataSerializer var1) throws IOException {
         var1.writeFloat(this.yaw);
         var1.writeFloat(this.pitch);
         super.b(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public void a(PacketListener var1) {
         super.a((PacketListenerPlayIn)var1);
      }
   }

   public static class PacketPlayInPosition extends PacketPlayInFlying {
      public PacketPlayInPosition() {
         this.hasPos = true;
      }

      public void a(PacketDataSerializer var1) throws IOException {
         this.x = var1.readDouble();
         this.y = var1.readDouble();
         this.z = var1.readDouble();
         super.a(var1);
      }

      public void b(PacketDataSerializer var1) throws IOException {
         var1.writeDouble(this.x);
         var1.writeDouble(this.y);
         var1.writeDouble(this.z);
         super.b(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public void a(PacketListener var1) {
         super.a((PacketListenerPlayIn)var1);
      }
   }

   public static class PacketPlayInPositionLook extends PacketPlayInFlying {
      public PacketPlayInPositionLook() {
         this.hasPos = true;
         this.hasLook = true;
      }

      public void a(PacketDataSerializer var1) throws IOException {
         this.x = var1.readDouble();
         this.y = var1.readDouble();
         this.z = var1.readDouble();
         this.yaw = var1.readFloat();
         this.pitch = var1.readFloat();
         super.a(var1);
      }

      public void b(PacketDataSerializer var1) throws IOException {
         var1.writeDouble(this.x);
         var1.writeDouble(this.y);
         var1.writeDouble(this.z);
         var1.writeFloat(this.yaw);
         var1.writeFloat(this.pitch);
         super.b(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public void a(PacketListener var1) {
         super.a((PacketListenerPlayIn)var1);
      }
   }
}
