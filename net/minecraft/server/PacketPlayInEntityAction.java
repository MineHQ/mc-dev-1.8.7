package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;

public class PacketPlayInEntityAction implements Packet<PacketListenerPlayIn> {
   private int a;
   private PacketPlayInEntityAction.EnumPlayerAction animation;
   private int c;

   public PacketPlayInEntityAction() {
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      this.animation = (PacketPlayInEntityAction.EnumPlayerAction)var1.a(PacketPlayInEntityAction.EnumPlayerAction.class);
      this.c = var1.e();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      var1.a((Enum)this.animation);
      var1.b(this.c);
   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   public PacketPlayInEntityAction.EnumPlayerAction b() {
      return this.animation;
   }

   public int c() {
      return this.c;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }

   public static enum EnumPlayerAction {
      START_SNEAKING,
      STOP_SNEAKING,
      STOP_SLEEPING,
      START_SPRINTING,
      STOP_SPRINTING,
      RIDING_JUMP,
      OPEN_INVENTORY;

      private EnumPlayerAction() {
      }
   }
}
