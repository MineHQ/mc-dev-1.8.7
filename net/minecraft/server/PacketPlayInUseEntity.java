package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Entity;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class PacketPlayInUseEntity implements Packet<PacketListenerPlayIn> {
   private int a;
   private PacketPlayInUseEntity.EnumEntityUseAction action;
   private Vec3D c;

   public PacketPlayInUseEntity() {
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      this.action = (PacketPlayInUseEntity.EnumEntityUseAction)var1.a(PacketPlayInUseEntity.EnumEntityUseAction.class);
      if(this.action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
         this.c = new Vec3D((double)var1.readFloat(), (double)var1.readFloat(), (double)var1.readFloat());
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      var1.a((Enum)this.action);
      if(this.action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
         var1.writeFloat((float)this.c.a);
         var1.writeFloat((float)this.c.b);
         var1.writeFloat((float)this.c.c);
      }

   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   public Entity a(World var1) {
      return var1.a(this.a);
   }

   public PacketPlayInUseEntity.EnumEntityUseAction a() {
      return this.action;
   }

   public Vec3D b() {
      return this.c;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }

   public static enum EnumEntityUseAction {
      INTERACT,
      ATTACK,
      INTERACT_AT;

      private EnumEntityUseAction() {
      }
   }
}
